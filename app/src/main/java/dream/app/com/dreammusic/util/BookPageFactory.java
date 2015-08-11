/**
 *  Author :  hmg25
 *  Description :
 */
package dream.app.com.dreammusic.util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.drawable.BitmapDrawable;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class BookPageFactory {

	private File book_file = null;
	private MappedByteBuffer m_mbBuf = null;
	private int m_mbBufLen = 0;
	private int m_mbBufBegin = 0;
	private int m_mbBufEnd = 0;
	private String m_strCharsetName = "utf-8";
	private Bitmap m_book_bg = null;
	private int mWidth;
	private int mHeight;

	private Vector<String> m_lines = new Vector<String>();


	private int m_fontSize = 25; //字体大小
	private int m_textColor = Color.parseColor("#383838");  //字体颜色
	private int m_backColor = Color.parseColor("#00000000"); // 背景颜色
	private int marginWidth = 15; // 左右与边缘的距离
	private int marginHeight = 20; // 上下与边缘的距离
	private int mLineCount; // 每页可以显示的行数
	private float mVisibleHeight; // 绘制内容的宽
	private float mVisibleWidth; // 绘制内容的宽
	private boolean m_isfirstPage,m_islastPage;
	private Paint mPaint;

	private Activity mActivity;

	public BookPageFactory(int w, int h){
		mWidth = w;
		mHeight = h;
		init();
	}

	public BookPageFactory(Activity activity) {
		WindowManager manager = activity.getWindowManager();
		Display display = manager.getDefaultDisplay();
		mWidth  =  display.getWidth();
		mHeight =  display.getHeight();
		mActivity = activity;
		init();
	}
	private void init() {
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setTextAlign(Align.LEFT);
		mPaint.setTextSize(m_fontSize);
		mPaint.setColor(m_textColor);
		mVisibleWidth = mWidth - marginWidth * 2;
		mVisibleHeight = mHeight - marginHeight * 2;
		mLineCount = (int) (mVisibleHeight / (m_fontSize+8)); // 可显示的行数
	}

	@SuppressWarnings("resource")
	public void openbook(String strFilePath) throws IOException {
		book_file = new File(strFilePath);
		long lLen = book_file.length();
		m_mbBufLen = (int) lLen;
		m_mbBuf = new RandomAccessFile(book_file, "r").getChannel().map(
				FileChannel.MapMode.READ_ONLY, 0, lLen);
	}


	protected byte[] readParagraphBack(int nFromPos) {
		int nEnd = nFromPos;
		int i;
		byte b0, b1;
		if (m_strCharsetName.equals("UTF-16LE")) {
			i = nEnd - 2;
			while (i > 0) {
				b0 = m_mbBuf.get(i);
				b1 = m_mbBuf.get(i + 1);
				if (b0 == 0x0a && b1 == 0x00 && i != nEnd - 2) {
					i += 2;
					break;
				}
				i--;
			}

		} else if (m_strCharsetName.equals("UTF-16BE")) {
			i = nEnd - 2;
			while (i > 0) {
				b0 = m_mbBuf.get(i);
				b1 = m_mbBuf.get(i + 1);
				if (b0 == 0x00 && b1 == 0x0a && i != nEnd - 2) {
					i += 2;
					break;
				}
				i--;
			}
		} else {
			i = nEnd - 1;
			while (i > 0) {
				b0 = m_mbBuf.get(i);
				if (b0 == 0x0a && i != nEnd - 1) {
					i++;
					break;
				}
				i--;
			}
		}
		if (i < 0)
			i = 0;
		int nParaSize = nEnd - i;
		int j;
		byte[] buf = new byte[nParaSize];
		for (j = 0; j < nParaSize; j++) {
			buf[j] = m_mbBuf.get(i + j);
		}
		return buf;
	}

	// 读取上一段落
	protected byte[] readParagraphForward(int nFromPos) {
		int nStart = nFromPos;
		int i = nStart;
		byte b0, b1;
		// 根据编码格式判断换行
		if (m_strCharsetName.equals("UTF-16LE")){
			while (i < m_mbBufLen - 1){
				b0 = m_mbBuf.get(i++);
				b1 = m_mbBuf.get(i++);
				if (b0 == 0x0a && b1 == 0x00){
					break;
				}
			}
		} else if (m_strCharsetName.equals("UTF-16BE")){
			while (i < m_mbBufLen - 1){
				b0 = m_mbBuf.get(i++);
				b1 = m_mbBuf.get(i++);
				if (b0 == 0x00 && b1 == 0x0a){
					break;
				}
			}
		} else {
			while (i < m_mbBufLen){
				b0 = m_mbBuf.get(i++);
				if (b0 == 0x0a){
					break;
				}
			}
		}
		int nParaSize = i - nStart;
		byte[] buf = new byte[nParaSize];
		for (i = 0; i < nParaSize; i++){
			buf[i] = m_mbBuf.get(nFromPos + i);
		}
		return buf;
	}

	protected Vector<String> pageDown(){
		String strParagraph = "";
		Vector<String> lines = new Vector<String>();
		while (lines.size() < mLineCount && m_mbBufEnd < m_mbBufLen){
			byte[] paraBuf = readParagraphForward(m_mbBufEnd); // 读取一个段落
			m_mbBufEnd += paraBuf.length;
			try {
				strParagraph = new String(paraBuf, m_strCharsetName);
//				Log.e("log", strParagraph);

			} catch (UnsupportedEncodingException e){
				e.printStackTrace();
			}
			String strReturn = "";
			if (strParagraph.indexOf("\r\n") != -1){
				strReturn = "\r\n";
				strParagraph = strParagraph.replaceAll("\r\n", "");
			} else if (strParagraph.indexOf("\n") != -1){
				strReturn = "\n";
				strParagraph = strParagraph.replaceAll("\n", "");
			}
			if (strParagraph.length() == 0) {
				lines.add(strParagraph);
			}
			while (strParagraph.length() > 0){
				int nSize = mPaint.breakText(strParagraph, true, mVisibleWidth,
						null);
				lines.add(strParagraph.substring(0, nSize));
				strParagraph = strParagraph.substring(nSize);
				if (lines.size() >= mLineCount) {
					break;
				}
			}
			if (strParagraph.length() != 0){
				try {
					m_mbBufEnd -= (strParagraph + strReturn)
							.getBytes(m_strCharsetName).length;
				} catch (UnsupportedEncodingException e){
					e.printStackTrace();
				}
			}
		}
		return lines;
	}

	protected void pageUp(){
		if (m_mbBufBegin < 0)
			m_mbBufBegin = 0;
		Vector<String> lines = new Vector<String>();
		String strParagraph = "";
		while (lines.size() < mLineCount && m_mbBufBegin > 0){
			Vector<String> paraLines = new Vector<String>();
			byte[] paraBuf = readParagraphBack(m_mbBufBegin);
			m_mbBufBegin -= paraBuf.length;
			try {
				strParagraph = new String(paraBuf, m_strCharsetName);
			} catch (UnsupportedEncodingException e){
				e.printStackTrace();
			}

			strParagraph = strParagraph.replaceAll("\r\n", "");
			strParagraph = strParagraph.replaceAll("\n", "");

			if (strParagraph.length() == 0){
				paraLines.add(strParagraph);
			}

			while (strParagraph.length() > 0) {
				int nSize = mPaint.breakText(strParagraph, true, mVisibleWidth,
						null);
				paraLines.add(strParagraph.substring(0, nSize));
				strParagraph = strParagraph.substring(nSize);
			}
			lines.addAll(0, paraLines);
		}
		while (lines.size() > mLineCount) {
			try {
				m_mbBufBegin += lines.get(0).getBytes(m_strCharsetName).length;
				lines.remove(0);
			} catch (UnsupportedEncodingException e){
				e.printStackTrace();
			}
		}
		m_mbBufEnd = m_mbBufBegin;
		return;
	}

	protected void prePage() throws IOException {
		if (m_mbBufBegin <= 0){
			m_mbBufBegin = 0;
			m_isfirstPage=true;
			return;
		}else m_isfirstPage = false;
		m_lines.clear();
		pageUp();
		m_lines = pageDown();
	}

	public void nextPage() throws IOException{
		if (m_mbBufEnd >= m_mbBufLen){
			m_islastPage=true;
			return;
		}else
			m_islastPage=false;
		m_lines.clear();
		m_mbBufBegin = m_mbBufEnd;
		m_lines = pageDown();
	}

	public void onDraw(Canvas c){
		if (m_lines.size() == 0)
			m_lines = pageDown();
		if (m_lines.size() > 0){
			if (m_book_bg == null)
				c.drawColor(m_backColor);
			else
				c.drawBitmap(m_book_bg, 0, 0, null);
			int y = marginHeight;
			for (String strLine : m_lines){
				y += m_fontSize;
				c.drawText(strLine, marginWidth, y, mPaint);
			}
		}
		float fPercent = (float) (m_mbBufBegin * 1.0 / m_mbBufLen);
		DecimalFormat df = new DecimalFormat("#0.0");
		String strPercent = df.format(fPercent * 100) + "%";
		int nPercentWidth = (int) mPaint.measureText("999.9%") + 1;
		c.drawText(strPercent, mWidth - nPercentWidth, mHeight - 5, mPaint);
	}

	public void setBgBitmap(Bitmap BG){
		m_book_bg = BG;
	}

	public boolean isfirstPage() {
		return m_isfirstPage;
	}
	public boolean islastPage() {
		return m_islastPage;
	}

	public void setBeginPosition(int pos){
		m_mbBufBegin = pos;
	}

	public void setEndPosition(int pos) {
		m_mbBufEnd = pos;
	}

	public int getEndPosition(){
		return m_mbBufEnd;
	}
	private String getNextString() {
		byte[] buf = readParagraphForward(mStartPos);
		mStartPos+=buf.length;
		String s = "";
		try {
			s = new String(buf,m_strCharsetName);
		} catch (UnsupportedEncodingException e){
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 得到每一章的对应的十几页的Bitmap
	 *  Author :  hmg25
	 *  Version:  1.0 
	 *  Description :
	 */
	public List<Bitmap> getChapterContentBitmaps(){
		List<Bitmap> _List = new ArrayList<Bitmap>();
		boolean flag = true;
		while(flag){
			try {
				nextPage();
				if (islastPage()) {
					flag = false;
				}else{
					Bitmap bitmap = drawCancas();
					_List.add(bitmap);
				}
			} catch (IOException e) {}
		}
		return _List;
	}

	public List<View> getChapterContentViews(){
		List<Bitmap> bitmaps = getChapterContentBitmaps();
		List<View> views = new ArrayList<View>();
		for (int i = 0; i < bitmaps.size(); i++) {
			View v = new View(mActivity);
			v.setBackground(new BitmapDrawable(bitmaps.get(i)));
			views.add(v);
		}
		View vlast = new View(mActivity);
		vlast.setBackground(new BitmapDrawable());
		views.add(vlast);
		return views;
	}

	public Bitmap drawCancas(){
		Bitmap bitmap = Bitmap.createBitmap(mWidth,mHeight,Bitmap.Config.ARGB_4444);
		Canvas canvas = new Canvas(bitmap);
		if (m_lines.size() == 0)
			m_lines = pageDown();
		if (m_lines.size() > 0){
			if (m_book_bg == null)
				canvas.drawColor(m_backColor);
			else
				canvas.drawBitmap(m_book_bg, 0, 0, null);
			int y = marginHeight;
			for (String strLine : m_lines){
				y += (m_fontSize+8);
				canvas.drawText(strLine, marginWidth, y, mPaint);
			}
		}
		return bitmap;
	}

	private int mStartPos = 0;
	public void showGraph(){
		int flag = 0;
		String strLine = "";
		String content = "";
		String regex = "第.{1,8}章.{0,}\r\n";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(strLine);
		while(mStartPos<m_mbBufLen-1){
			while(strLine.length()<1000&&mStartPos<m_mbBufLen-1)
				strLine+=getNextString();
			matcher = pattern.matcher(strLine);
			if(matcher.find()){
				if(flag==0){
					content  = strLine;
					flag=1;
				}else{
					content+=strLine.substring(0, matcher.start());
					content = strLine.substring(matcher.start(),strLine.length());
				}
			}else
				content+=strLine;
			strLine="";
		}
	}


}
