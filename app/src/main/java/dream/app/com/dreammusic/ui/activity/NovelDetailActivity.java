package dream.app.com.dreammusic.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import net.tsz.afinal.FinalBitmap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.entry.NovelAPI;
import dream.app.com.dreammusic.ui.view.LoadingDialog;
import dream.app.com.dreammusic.util.DialogUtil;

/**
 * Created by Administrator on 2015/8/10.
 */
public class NovelDetailActivity extends BaseActivity {

    private ImageView mImgView;
    private TextView mNameView,mAuthorView,mStateView,mIntroView;
    private Button mChapterBtn;

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
    }

    @Override
    protected void initListener() {
        super.initListener();
        mChapterBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.btn_noveldetail_chapter:
                getNovelChapters();
                break;
        }
    }

    private void getNovelChapters() {

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
                    intent.putExtra("htmlcontent",htmlContent);
                    startNewActivityWithAnim(NovelChapterActivity.class, intent);
                }catch (Exception e){}
                cancelLoadingDialog();
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
