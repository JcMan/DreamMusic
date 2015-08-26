package dream.app.com.dreammusic.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.tool.logger.Logger;

import net.tsz.afinal.FinalBitmap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;

import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.db.NovelInfoDAO;
import dream.app.com.dreammusic.entry.ChapterEntry;
import dream.app.com.dreammusic.entry.NovelAPI;
import dream.app.com.dreammusic.entry.NovelEntry;
import dream.app.com.dreammusic.ui.view.LoadingDialog;
import dream.app.com.dreammusic.util.DialogUtil;
import dream.app.com.dreammusic.util.TextUtil;

/**
 * Created by Administrator on 2015/8/10.
 */
public class NovelDetailActivity extends BaseActivity {

    private ImageView mImgView;
    private TextView mNameView,mAuthorView,mStateView,mIntroView;
    private Button mChapterBtn,mAddBookBtn,mReadBtn;

    private String mBookName;
    private String mAuthor;
    private String mBookUrl;
    private String mImgUrl;
    private String mIntro;
    private String mStateinfo;
    private String mHtmlContent;
    private Bitmap loadingbitmap;
    private LoadingDialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noveldetail);
        getDataFromIntent();
        initViriable();
        initView();
        initListener();
        setTitle("书籍详情");
        updateView();
    }

    private void initViriable() {
        loadingbitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_loading_novel);
        loadingDialog = DialogUtil.createLoadingDialog(this);
    }

    @Override
    public void initView() {
        super.initView();
        mImgView = (ImageView) findViewById(R.id.iv_noveldetail);
        mNameView = (TextView) findViewById(R.id.tv_noveldetail_name);
        mStateView = (TextView) findViewById(R.id.tv_noveldetail_state);
        mAuthorView = (TextView) findViewById(R.id.tv_noveldetail_author);
        mIntroView = (TextView) findViewById(R.id.tv_noveldetail_intro);
        mChapterBtn = (Button) findViewById(R.id.btn_noveldetail_chapter);
        mAddBookBtn = (Button) findViewById(R.id.btn_add_book_to_shelf);
        mReadBtn = (Button) findViewById(R.id.btn_novel_detail_read);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mChapterBtn.setOnClickListener(this);
        mAddBookBtn.setOnClickListener(this);
        mReadBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.btn_noveldetail_chapter:
                getNovelChapters();
                break;
            case R.id.btn_add_book_to_shelf:
                addBookToShelf();
                break;
            case R.id.btn_novel_detail_read:
                readBook();
                break;
        }
    }

    private void readBook(){
        showLoadingDlg();
        new Thread(new Runnable(){
            @Override
            public void run(){
                try {
                    Document doc = Jsoup.connect(mBookUrl).get();
                    int firstChapter = getFirstChapter(doc) + 1;
                    String url = mBookUrl.replace("index", firstChapter + "");
                    Document doc_firstchapter = Jsoup.connect(url).get();
                    String content = NovelAPI.getChapterContent(doc_firstchapter);
                    String chaptername = NovelAPI.getNetNovelChapters(doc,mBookUrl.replace("index.html","")).get(0).getmChapterName();
                    String chapterurl = NovelAPI.getNetNovelChapters(doc,mBookUrl.replace("index.html","")).get(0).getmChapterUrl();
                    TextUtil.writeNovelContent(chaptername, content);
                    Intent intent = new Intent();
                    intent.putExtra("firstpageurl",chapterurl);
                    startNewActivityWithAnim(ReadNovelActivity.class,intent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                cancelLoadingDlg();
            }
        }).start();
    }

    private void addBookToShelf(){
        showLoadingDlg();
        new Thread(new Runnable(){
            @Override
            public void run(){
                try {
                    Document doc = Jsoup.connect(mBookUrl).get();
                    NovelInfoDAO dao = new NovelInfoDAO(NovelDetailActivity.this);
                    NovelEntry entry = new NovelEntry();
                    int chapter = getFirstChapter(doc);
                    entry.setmBookName(mBookName);
                    entry.setmAuthor(mAuthor);
                    entry.setmImgUrl(mImgUrl);
                    entry.setmMainPageUrl(mBookUrl);
                    entry.setmBaseUrl(mBookUrl.replace("index.html", ""));
                    entry.setmLastChapter(chapter);
                    if(dao.addNovelInfo(entry)){
                        runOnUiThread(new Runnable(){
                            @Override
                            public void run(){
                                DialogUtil.showMessageDialog(NovelDetailActivity.this,"添加至书架成功");
                            }
                        });
                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                DialogUtil.showMessageDialog(NovelDetailActivity.this,"添加至书架失败");
                            }
                        });
                    }
                } catch (IOException e){
                    e.printStackTrace();
                }
                cancelLoadingDlg();
            }
        }).start();
    }

    private int getFirstChapter(Document doc){
        List<ChapterEntry> _List = NovelAPI.getNetNovelChapters(doc, mBookUrl.replace("index.html",""));
        ChapterEntry entry = _List.get(0);
        String url = entry.getmChapterUrl();
        int chapter = Integer.parseInt(url.substring(url.lastIndexOf("/") + 1,
                url.length()).replace(".html", ""));
        chapter--;
        return chapter;
    }

    private void getNovelChapters(){

        showLoadingDlg();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Document doc = Jsoup.connect(mBookUrl).get();
                    String htmlContent = doc.toString();
                    Intent intent = new Intent();
                    intent.putExtra("from","net");
                    intent.putExtra("bookurl",mBookUrl);
                    intent.putExtra("name",mBookName);
                    intent.putExtra("htmlcontent",htmlContent);
                    cancelLoadingDlg();
                    startNewActivityWithAnim(NovelChapterActivity.class, intent);
                }catch (Exception e){}

            }
        }).start();

    }

    private void updateView() {
        FinalBitmap finalBitmap = FinalBitmap.create(this);
        finalBitmap.display(mImgView,mImgUrl,loadingbitmap,loadingbitmap);
        mNameView.setText(mBookName);
        mAuthorView.setText(mAuthor);
        mIntroView.setText(mIntro);
        getNovelStateInfo();
    }

    private void getNovelStateInfo(){
        loadingDialog.show();
        try {
            Document doc = Jsoup.parse(mHtmlContent);
            mStateinfo = NovelAPI.getNovelState(doc);
            mIntro = NovelAPI.getNovelIntro(doc);
            updateNovelStateView();
            updateIntroView();
            cancelLoadingDialog();
        }catch (Exception e){}
    }
    private void updateIntroView() {
        mIntroView.setText(mIntro);
    }

    private void updateNovelStateView(){
        mStateView.setText("最近更新："+mStateinfo);
    }

    private void getDataFromIntent(){
        Intent intent = getIntent();
        mBookName = intent.getStringExtra("name");
        mAuthor = intent.getStringExtra("author");
        mBookUrl = intent.getStringExtra("bookurl");
        mImgUrl = intent.getStringExtra("imgurl");
        mHtmlContent = intent.getStringExtra("htmlcontent");
    }

    private void cancelLoadingDialog(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingDialog.cancel();
            }
        });
    }
}
