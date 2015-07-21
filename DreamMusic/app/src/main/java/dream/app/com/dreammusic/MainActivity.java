package dream.app.com.dreammusic;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tool.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Random;

import cn.jpush.android.api.InstrumentedActivity;
import cn.jpush.android.api.JPushInterface;
import dream.app.com.dreammusic.config.ApplicationConfig;
import dream.app.com.dreammusic.entry.BgEntry;
import dream.app.com.dreammusic.entry.UserEntry;
import dream.app.com.dreammusic.fragment.FragmentMain;
import dream.app.com.dreammusic.fragment.FragmentMenuLogin;
import dream.app.com.dreammusic.fragment.FragmentMenuUser;
import dream.app.com.dreammusic.jpush.ExampleUtil;
import dream.app.com.dreammusic.model.Music;
import dream.app.com.dreammusic.myinterface.FragmentPlayMusicListener;
import dream.app.com.dreammusic.service.AlarmTimerService;
import dream.app.com.dreammusic.service.MusicService;
import dream.app.com.dreammusic.ui.activity.AlarmTimerActivity;
import dream.app.com.dreammusic.ui.activity.ChangeBgActivity;
import dream.app.com.dreammusic.ui.activity.LrcActivity;
import dream.app.com.dreammusic.ui.activity.MessageActivity;
import dream.app.com.dreammusic.ui.activity.MusicStoreActivity;
import dream.app.com.dreammusic.ui.activity.SettingActivity;
import dream.app.com.dreammusic.ui.view.LoadingDialog;
import dream.app.com.dreammusic.util.ActivityUtil;
import dream.app.com.dreammusic.util.AnimUtil;
import dream.app.com.dreammusic.util.DialogUtil;
import dream.app.com.dreammusic.util.MusicUtil;
import dream.app.com.dreammusic.util.SharedPreferencesUtil;
import dream.app.com.dreammusic.util.ThirdPlatformLoginUtil;

public class MainActivity extends InstrumentedActivity implements Handler.Callback,
        FragmentMenuLogin.LoginListener,View.OnClickListener,
        FragmentMain.FragmentClickListener,SeekBar.OnSeekBarChangeListener,
        MusicService.IMusicCompletionListener,FragmentPlayMusicListener {

    public static boolean isForeground = false;
    private DrawerLayout mSlideMenu;
    private TextView mSearchMusic,mChangeMainBg,mSleepTime,mSetting,mExit,mMessage;
    private TextView mMusicName,mSinger;
    private ImageButton mRightLogo,mLeftBack,mNext,mStart,mPause;
    private ImageView mBottomSinger;
    private ImageView mSingerImg;
    private Handler mHandler;
    private android.app.Fragment mFragment;
    private View mDrawerView;
    private LoadingDialog loadingDialog;
    private View view_main;
    private SeekBar mSeekBar;

    private AlarmTimerReceiver receiver_alarm;

    private MusicService mMusicService;
    private ServiceConnection conn;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUtil();
        initVariable();
        initView();
        initListener();
        loadingDialog = DialogUtil.createLoadingDialog(this,"加载中···");
        mHandler = new Handler(this);
        registerReceiver();
        bindService();
    }

    private void initVariable() {
        initConn();
    }

    /**
     * 绑定音乐服务
     */
    private void bindService() {
        Intent intent = new Intent(this,MusicService.class);
        bindService(intent,conn,0);
    }

    private void registerReceiver() {
        receiver_alarm = new AlarmTimerReceiver();
        IntentFilter filter = new IntentFilter(ApplicationConfig.RECEIVER_ALARM);
        registerReceiver(receiver_alarm,filter);
    }

    private void unregisterReceiver(){
        unregisterReceiver(receiver_alarm);
    }

    @Override
    protected void onResume(){
        super.onResume();
        isForeground = true;
        setMusicCompletionListener();
        updateLoginView();
        updatePlayView();
        updateBg();
    }


    private void setMusicCompletionListener() {
        if(mMusicService!=null){
            mMusicService.setOnMusicCompletion(this);
        }
    }

    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mHandler.removeMessages(0);
        stopAllService();
        unregisterReceiver();
        super.onDestroy();
    }



    /**
     * 关闭所有服务
     */
    private void stopAllService() {
        stopService(new Intent(this,AlarmTimerService.class));
    }

    /**
     * 更新登录界面，如果退出账号
     */
    private void updateLoginView(){
        SharedPreferences preferences = SharedPreferencesUtil.getSharedPreferences(ApplicationConfig.USER);
        boolean login = preferences.getBoolean(UserEntry.LOGIN,false);
        if(!login){
            mFragment = new FragmentMenuLogin();
            getFragmentManager().beginTransaction().replace(R.id.fr_login_layout,mFragment).commit();
        }
    }

    /**
     * 初始化工具类
     */
    private void initUtil() {
        Logger.init("dream").hideThreadInfo();
        ThirdPlatformLoginUtil.init(this);
        SharedPreferencesUtil.init(this);
//        initJPush();
    }
    public void initView(){
        mSlideMenu = (DrawerLayout) findViewById(R.id.slidemenu);
        mSearchMusic = (TextView) findViewById(R.id.tv_search_music);
        mChangeMainBg = (TextView) findViewById(R.id.tv_change_mainbg);
        mSetting = (TextView) findViewById(R.id.tv_setting);
        mMessage = (TextView) findViewById(R.id.tv_message);
        mSleepTime = (TextView) findViewById(R.id.tv_set_sleep_time);
        mExit = (TextView) findViewById(R.id.tv_exit);
        mRightLogo = (ImageButton) findViewById(R.id.ib_top_logo_right);
        mLeftBack  = (ImageButton) findViewById(R.id.ib_top_back);
        mLeftBack.setVisibility(View.GONE);
        mRightLogo.setVisibility(View.VISIBLE);
        mDrawerView = findViewById(R.id.drawer_layout);
        view_main = findViewById(R.id.layout_activity_main);

        mNext = (ImageButton) findViewById(R.id.ib_bottom_next);
        mPause = (ImageButton) findViewById(R.id.ib_bottom_pause);
        mStart = (ImageButton) findViewById(R.id.ib_bottom_start);
        mBottomSinger = (ImageView) findViewById(R.id.iv_bottom_singer);

        mSeekBar = (SeekBar) findViewById(R.id.probar_bottom);
        mMusicName = (TextView) findViewById(R.id.tv_bottom_title);
        mSinger = (TextView) findViewById(R.id.tv_bottom_singer);
        mSingerImg = (ImageView) findViewById(R.id.iv_bottom_singer);
        mSeekBar.setMax(0);
        initLoginView();
        initMainView();
    }
    /**
     * 初始化MainFragment
     */
    private void initMainView(){
        getFragmentManager().beginTransaction().replace(R.id.fragment_main, new FragmentMain()).commit();
    }

    /**
     * 初始化登录界面
     */
    private void initLoginView() {
        SharedPreferences preferences  = SharedPreferencesUtil.getSharedPreferences(ApplicationConfig.USER);
        boolean login = preferences.getBoolean(UserEntry.LOGIN,false);
        if(!login){
            mFragment = new FragmentMenuLogin();
        }else{
            String Username = preferences.getString(UserEntry.USERNAME,"飞梦音乐");
            String HeadImageUrl = preferences.getString(UserEntry.HEADIMAGE,"");
            mFragment = new FragmentMenuUser(Username,HeadImageUrl);
        }
        getFragmentManager().beginTransaction().replace(R.id.fr_login_layout, mFragment).commit();
    }

    protected void initListener() {
        mSearchMusic.setOnClickListener(this);
        mChangeMainBg.setOnClickListener(this);
        mSleepTime.setOnClickListener(this);
        mSetting.setOnClickListener(this);
        mMessage.setOnClickListener(this);
        mExit.setOnClickListener(this);
        mRightLogo.setOnClickListener(this);
        mBottomSinger.setOnClickListener(this);

        mNext.setOnClickListener(this);
        mPause.setOnClickListener(this);
        mStart.setOnClickListener(this);
        mSeekBar.setOnSeekBarChangeListener(this);
    }

    private void updateBg() {
        view_main.setBackground(new BitmapDrawable(BgEntry.getDefaultBg(this)));
    }

    /**
     * 打开或关闭侧滑菜单
     */
    protected void clickOnRightLogo(){
        boolean isOpen = mSlideMenu.isDrawerOpen(mDrawerView);
        if(isOpen)
            mSlideMenu.closeDrawer(mDrawerView);
        else
            mSlideMenu.openDrawer(mDrawerView);
    }
    @Override
    public boolean handleMessage(Message msg){
        if(mMusicService!=null){
            mSeekBar.setProgress(mMusicService.getPlayerPosition());
            mHandler.sendEmptyMessageDelayed(0,500);
        }
        return false;
    }



    private void startMusic() {
         mMusicService.start();
         updatePlayView();
    }

    private void play(int position){
        mMusicService.play(position);
        updatePlayView();
    }

    private void pauseMusic() {
        mMusicService.pause();
        updatePlayView();
    }

    private boolean isPlaying(){
        return mMusicService.getState()==MusicService.STATE_PALYING;
    }

    private boolean isPause(){
        return mMusicService.getState()==MusicService.STATE_PAUSE;
    }

    /**
     * 下一首
     */
    private void playNext() {
        mMusicService.next();
        updatePlayView();
    }

    private void startNewActivity(Class pclass){
        startNewActivity(pclass, AnimUtil.BASE_SLIDE_RIGHT_IN, AnimUtil.BASE_SLIDE_REMAIN);
    }

    /**
     * 退出程序
     */
    private void exit() {
        mMusicService.stop();
        stopService(new Intent(this,MusicService.class));
        finish();
    }

    @Override
    public void loginSuccess(JSONObject object) {
        try {
            String name = object.getString(UserEntry.USERNAME);
            String headimageurl = object.getString(UserEntry.HEADIMAGE);
            mFragment = new FragmentMenuUser(name,headimageurl);
            getFragmentManager().beginTransaction().replace(R.id.fr_login_layout,mFragment).commit();
            showMessage("登录成功");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loginFailure(JSONObject object) {
        showMessage("登录失败");
    }

   private static long back_pressed = 0;
    //@Override
    /*public void onBackPressed() {
        if(back_pressed+2000>System.currentTimeMillis())
            super.onBackPressed();
        else {
            showMessage("再按一次退出飞梦音乐");
            back_pressed = System.currentTimeMillis();
        }
    }*/

    private void showMessage(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }

    protected void startNewActivity(Class pclass,int inStyle,int outStyle){
        startActivity(new Intent(this,pclass));
        overridePendingTransition(inStyle, outStyle);
    }


    private void initJPush(){
        JPushInterface.init(getApplicationContext());
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
        //registerMessageReceiver();
    }

    /**
     * 得到从JPush得到的推送消息
     */
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "dream.app.com.dreammusic.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver(){
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    /**
     * 来自FragmentClickListener的接口方法
     * @param id
     */
    @Override
    public void click(int id){
        switch (id){
            case R.id.view_music_store:
                startMusicStoreActivity();
                break;
            case R.id.ib_fagment_main_diange:
                dianGe();
                break;
        }
    }

    /**
     * 点歌
     */
    private void dianGe() {
        int num = new Random().nextInt(MusicUtil.getLocalMusicNumber(this));
        play(num);
    }

    private void startMusicStoreActivity() {
        Intent intent  = new Intent();
        intent.putExtra(ActivityUtil.TITLE,"乐库");
        startNewActivityWithAnim(MusicStoreActivity.class, intent);
    }

    protected void startNewActivityWithAnim(Class pclass ,Intent intent){
        intent.setClass(this,pclass);
        startActivity(intent);
        overridePendingTransition(AnimUtil.BASE_SLIDE_RIGHT_IN, AnimUtil.BASE_SLIDE_REMAIN);
    }

    /**
     * 连接MusicService
     */
    private void initConn(){
        conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mMusicService = ((MusicService.MusicBinder)service).getService();
                mMusicService.setOnMusicCompletion(MainActivity.this);
                if(mMusicService.isStop()){
                    play(0);
                    pauseMusic();
                }
                initPlayView();
            }
            @Override
            public void onServiceDisconnected(ComponentName name) {
                mMusicService = null;
            }
        };
    }

    /**
     * 更新播放界面
     */
    private void initPlayView() {
        if(mMusicService!=null&&mMusicService.getPosition()!=-1){
            updatePlayView();
        }
    }

    private void updatePlayView(){
        if(mMusicService!=null){
            int state = mMusicService.getState();
            mSeekBar.setMax(mMusicService.getMusicDuration());
            mHandler.sendEmptyMessageDelayed(0,500);
            updateNameAndSingerAndImg();
            updatePlayerView(state);
        }
    }

    private void updateNameAndSingerAndImg() {
        String name = mMusicService.getMusicName();
        String singer = mMusicService.getSinger();
        mMusicName.setText(name);
        mSinger.setText(singer);
    }

    private void updatePlayerView(int state) {
        if(state==MusicService.STATE_PALYING){
            mPause.setVisibility(View.VISIBLE);
            mStart.setVisibility(View.GONE);
        }else if(state==MusicService.STATE_PAUSE||state==MusicService.STATE_STOP){
            mPause.setVisibility(View.GONE);
            mStart.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 歌曲播放完成播放下一曲
     */
    @Override
    public void onMusicCompletion() {
        updatePlayView();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int position = seekBar.getProgress();
        seekTo(position);
    }

    private void seekTo(int position) {
        mMusicService.seekTo(position);
    }

    /**
     * 来自Fragment的播放音乐的接口
     * @param position
     */
    @Override
    public void onPlay(int position) {
        play(position);
    }

    /**
     * 来自Fragment的播放音乐的接口
     */
    @Override
    public void onUpdateMusicList(List<Music> list) {
        mMusicService.setMusicList(list);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_setting:
                startNewActivity(SettingActivity.class);
                break;
            case R.id.tv_message:
                startNewActivity(MessageActivity.class);
                break;
            case R.id.ib_top_logo_right:
                clickOnRightLogo();
                break;
            case R.id.tv_set_sleep_time:
                startNewActivity(AlarmTimerActivity.class);
                break;
            case R.id.tv_change_mainbg:
                startNewActivity(ChangeBgActivity.class);
                break;
            case R.id.tv_exit:
                exit();
                break;
            case R.id.ib_bottom_next:
                playNext();
                break;
            case R.id.ib_bottom_pause:
                pauseMusic();
                break;
            case R.id.ib_bottom_start:
                startMusic();
                break;
            case R.id.iv_bottom_singer:
                openLrcActivity();
                break;
            default:
                break;
        }
    }

    /**
     * 打开LrcActivity
     */
    private void openLrcActivity(){
        Intent intent = new Intent();
        intent.putExtra("title",mMusicName.getText().toString());
        intent.putExtra("singer",mSinger.getText().toString());
        intent.putExtra("songid",mMusicService.getSongId());
        startNewActivityWithAnim(LrcActivity.class, intent);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent){
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())){
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                if (!ExampleUtil.isEmpty(extras)) {
                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                }
            }
        }
    }

    class AlarmTimerReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            mMusicService.pause();
        }
    }
}

