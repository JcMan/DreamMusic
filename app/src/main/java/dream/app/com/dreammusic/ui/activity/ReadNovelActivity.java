package dream.app.com.dreammusic.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.app.tool.logger.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;

import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.adapter.JazzyAdapter;
import dream.app.com.dreammusic.config.ApplicationConfig;
import dream.app.com.dreammusic.entry.NovelAPI;
import dream.app.com.dreammusic.service.MusicService;
import dream.app.com.dreammusic.ui.view.LoadingDialog;
import dream.app.com.dreammusic.ui.view.jazzy.JazzyViewPager;
import dream.app.com.dreammusic.util.BookPageFactory;
import dream.app.com.dreammusic.util.DialogUtil;
import dream.app.com.dreammusic.util.TextUtil;

/**
 * Created by Administrator on 2015/8/11.
 */
public class ReadNovelActivity extends Activity implements ViewPager.OnPageChangeListener{

    private JazzyViewPager mJazzy;
    private List<View> mViewsList;
    private JazzyAdapter mAdapter;
    private boolean mLoadData ;
    private LoadingDialog loadingDialog;
    private BookPageFactory factory;

    private String mFirstPageUrl;
    private String mBaseUrl;
    private int mCurrentChapter;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_readnovel);
        getDataFromIntent();
        MusicService.addActivity(this);
        initView();
        loadingDialog = DialogUtil.createLoadingDialog(this);
        initReadView(factory);
    }

    private void initReadView(BookPageFactory factory){
        try {
            factory = new BookPageFactory(this);
            factory.openbook(ApplicationConfig.NOVEL_DIR+"/temp.txt");
            mViewsList = factory.getChapterContentViews();
            if(mViewsList!=null&&mViewsList.size()>1){
                mAdapter = new JazzyAdapter(this,mViewsList,mJazzy);
                mJazzy.setTransitionEffect(JazzyViewPager.TransitionEffect.Tablet);
                mJazzy.setAdapter(mAdapter);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void getDataFromIntent(){
        mFirstPageUrl = getIntent().getStringExtra("firstpageurl");
        mCurrentChapter = Integer.parseInt(mFirstPageUrl.substring(mFirstPageUrl.lastIndexOf("/") + 1,
                mFirstPageUrl.length()).replace(".html", ""));
        mBaseUrl = mFirstPageUrl.substring(0, mFirstPageUrl.lastIndexOf("/"))+"/";
    }
    public void initView(){
        mJazzy = (JazzyViewPager) findViewById(R.id.jazzyviewpager_readnovel);
        mJazzy.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels){
        if(!mLoadData&&position==mViewsList.size()-1){
            mLoadData = true;
            loadingDialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String nextChapterUrl = mBaseUrl+(++mCurrentChapter)+".html";
                        Document document = Jsoup.connect(nextChapterUrl).get();
                        String content = NovelAPI.getChapterContent(document);
                        String name = NovelAPI.getChapterName(document);
                        if(content!=null&&content.length()>10){
                            TextUtil.writeNovelContent(name,content);
                            mHandler.sendEmptyMessage(0);
                        }
                    }catch (Exception e){}
                    mLoadData = false;
                    cancelLoadingDialog();
                }
            }).start();
        }
    }

    @Override
    public void onPageSelected(int position){

    }

    @Override
    public void onPageScrollStateChanged(int state){

    }

    private void cancelLoadingDialog(){
        runOnUiThread(new Runnable(){
            @Override
            public void run(){
                loadingDialog.cancel();
            }
        });
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            initReadView(factory);
        }
    };
}
