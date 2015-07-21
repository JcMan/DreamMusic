package dream.app.com.dreammusic.ui.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.adapter.MyViewPagerAdapter;
import dream.app.com.dreammusic.adapter.SeekBarChangeListenerAdapter;
import dream.app.com.dreammusic.anim.PlayPageTransform;
import dream.app.com.dreammusic.config.App;
import dream.app.com.dreammusic.config.ApplicationConfig;
import dream.app.com.dreammusic.entry.BgEntry;
import dream.app.com.dreammusic.service.MusicService;
import dream.app.com.dreammusic.ui.view.CDView;
import dream.app.com.dreammusic.ui.view.LrcView;
import dream.app.com.dreammusic.util.AnimUtil;
import dream.app.com.dreammusic.util.ImageTools;
import dream.app.com.dreammusic.util.MusicUtil;

/**
 * Created by Administrator on 2015/7/20.
 */
public class LrcActivity extends Activity implements View.OnClickListener,MusicService.IMusicCompletionListener{

    private MusicService mMusicService;
    private ServiceConnection conn;


    private View view_bg;
    private ImageButton mBackBtn,mPreBtn,mStartBtn,mPauseBtn,mNextBtn;
    private SeekBar mSeekbar;
    private TextView tv_top_title,tv_top_singer,tv_bottom_end_time,tv_bottom_current_time;
    private CDView mCDView;
    private LrcView mLrcView_1;
    private LrcView mLrcView_9;
    private MediaPlayer mPlayer;
    private ViewPager mViewPager;
    private MyViewPagerAdapter mAdapter;
    private List<View> mViewsList;

    private String rootPath = ApplicationConfig.ROOT_PATH;
    private String title;
    private String singer;
    private int songid;

    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            if(mMusicService!=null){
                mSeekbar.setProgress(mMusicService.getPlayerPosition());
                mLrcView_1.changeCurrent(mPlayer.getCurrentPosition());
                mLrcView_9.changeCurrent(mPlayer.getCurrentPosition());
                updateTimeView();
            }
            mHandler.sendEmptyMessageDelayed(1, 500);
        };
    };
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lrc);
        getDataFromIntent();
        initVariable();
        initView();
        initListener();
        bindService();
        mPlayer = new MediaPlayer();


    }

    private void bindService() {
        Intent intent = new Intent(this,MusicService.class);
        bindService(intent,conn, Context.BIND_AUTO_CREATE);
    }

    private void updateView() {

    }

    private void initVariable() {
        initConn();
        mViewsList = new ArrayList<View>();
    }



    private void initListener() {
        mBackBtn.setOnClickListener(this);
        mPauseBtn.setOnClickListener(this);
        mPreBtn.setOnClickListener(this);
        mStartBtn.setOnClickListener(this);
        mNextBtn.setOnClickListener(this);
        mSeekbar.setOnSeekBarChangeListener(mSeekBarChangeListener);
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager_lrc);
        view_bg = findViewById(R.id.lrc_bg_layout);

        tv_top_singer = (TextView) findViewById(R.id.tv_lrc_top_singer);
        tv_top_title = (TextView) findViewById(R.id.tv_lrc_top_title);
        mSeekbar = (SeekBar) findViewById(R.id.sb_play_progress);

        mBackBtn = (ImageButton) findViewById(R.id.ib_lrc_top_back);
        mPreBtn = (ImageButton) findViewById(R.id.ib_play_pre);
        mStartBtn = (ImageButton) findViewById(R.id.ib_play_start);
        mPauseBtn = (ImageButton) findViewById(R.id.ib_play_pause);
        mNextBtn = (ImageButton) findViewById(R.id.ib_play_next);
        tv_bottom_current_time = (TextView) findViewById(R.id.tv_music_currentTime);
        tv_bottom_end_time = (TextView) findViewById(R.id.tv_music_endTime);

        initPagerViews();
        mAdapter = new MyViewPagerAdapter(this,mViewsList);
        mViewPager.setPageTransformer(true,new PlayPageTransform());
        mViewPager.setAdapter(mAdapter);
    }

    private void initPagerViews() {
        View pager_view_1 = View.inflate(this,R.layout.lrc_play_page_1,null);
        View pager_view_2 = View.inflate(this,R.layout.lrc_play_page_2,null);
        mCDView = (CDView) pager_view_1.findViewById(R.id.cdview);
        mLrcView_1 = (LrcView) pager_view_1.findViewById(R.id.play_first_lrc);
        mLrcView_9 = (LrcView) pager_view_2.findViewById(R.id.play_second_lrc);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_cd_singer_200);
        mCDView.setImage(ImageTools.scaleBitmap(bm, (int) (App.sScreenWidth * 0.8)));
        mCDView.start();
        mViewsList.add(pager_view_1);
        mViewsList.add(pager_view_2);
    }

    private void setLrc(String lrc) {
        mLrcView_1.setLrcPath(rootPath+lrc);
        mLrcView_9.setLrcPath(rootPath+lrc);
    }

    private void setBackground(){
        view_bg.setBackground(new BitmapDrawable(BgEntry.getDefaultBg(this)));
    }

    private void setMusic(String music) {
        mPlayer.reset();
        try {
            mPlayer.setDataSource(rootPath+music);
            mPlayer.prepare();
            mPlayer.start();
        } catch (Exception e) {}
    }

    @Override
    protected void onResume() {
        super.onResume();
        setBackground();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(AnimUtil.BASE_SLIDE_REMAIN,AnimUtil.BASE_SLIDE_RIGHT_OUT);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_lrc_top_back:
                onBackPressed();
                break;
            case R.id.ib_play_pre:
                pre();
                break;
            case R.id.ib_play_start:
                start();
                break;
            case R.id.ib_play_pause:
                pause();
                break;
            case R.id.ib_play_next:
                next();
                break;
        }
    }

    private void next() {
        mMusicService.next();
        play();
    }

    private void pre() {
        mMusicService.pre();
        play();
    }

    private void start() {
        mMusicService.start();
        updateStartAndPauseBtn(true);
        updateCDView(true);
    }

    private void pause() {
        mMusicService.pause();
        updateStartAndPauseBtn(false);
        updateCDView(false);
    }

    private void play(){
        title = mMusicService.getMusicName();
        singer = mMusicService.getSinger();
        updateTitleAndSinger();
        updateStartAndPauseBtn(true);
        mSeekbar.setMax(mMusicService.getMusicDuration());
        setLrcPath(mMusicService.getSongId());
    }

    private void setLrcPath(int songid) {
        mLrcView_1.setLrcPath(MusicUtil.getLrcPath(songid));
        mLrcView_9.setLrcPath(MusicUtil.getLrcPath(songid));
    }

    public void getDataFromIntent() {
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        singer = intent.getStringExtra("singer");
        songid = intent.getIntExtra("songid",0);
    }
    private void updateTitleAndSinger() {
        tv_top_title.setText(title+"");
        tv_top_singer.setText(singer+"");
    }

    private void updateCDView(boolean result){
        if(result)
            mCDView.start();
        else
            mCDView.pause();
    }

    private void initConn() {
        conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mMusicService = ((MusicService.MusicBinder)service).getService();
                mMusicService.setOnMusicCompletion(LrcActivity.this);
                mSeekbar.setMax(mMusicService.getMusicDuration());
                mHandler.sendEmptyMessageDelayed(1, 500);
                updateCDView(mMusicService.isPlaying());
                updateTitleAndSinger();
                updateBottomView();
                setLrcPath();
            }

            @Override
            public void onServiceDisconnected(ComponentName name){
                mMusicService = null;
            }
        };
    }

    private void setLrcPath() {
        setLrcPath(mMusicService.getSongId());
    }

    private void updateBottomView() {
        updateStartAndPauseBtn(mMusicService.isPlaying());
        updateTimeView();
    }

    private void updateTimeView() {
        tv_bottom_end_time.setText(MusicUtil.makeTimeString(mMusicService.getMusicDuration()));
        tv_bottom_current_time.setText(MusicUtil.makeTimeString(mMusicService.getPlayerPosition()));
    }

    private void updateStartAndPauseBtn(boolean result) {
        if(result==true){
            mStartBtn.setVisibility(View.GONE);
            mPauseBtn.setVisibility(View.VISIBLE);
        }else{
            mStartBtn.setVisibility(View.VISIBLE);
            mPauseBtn.setVisibility(View.GONE);
        }
    }

    private SeekBarChangeListenerAdapter mSeekBarChangeListener = new SeekBarChangeListenerAdapter(){
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            super.onStopTrackingTouch(seekBar);
            int progress = seekBar.getProgress();
            mMusicService.seekTo(progress);
        }
    };

    /**
     * 播放完成播放下一首
     */
    @Override
    public void onMusicCompletion() {
        play();
    }
}
