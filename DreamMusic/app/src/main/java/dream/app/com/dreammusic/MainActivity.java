package dream.app.com.dreammusic;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.app.tool.logger.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import cn.jpush.android.api.InstrumentedActivity;
import cn.jpush.android.api.JPushInterface;
import dream.app.com.dreammusic.config.ApplicationConfig;
import dream.app.com.dreammusic.entry.UserEntry;
import dream.app.com.dreammusic.fragment.FragmentMain;
import dream.app.com.dreammusic.fragment.FragmentMenuLogin;
import dream.app.com.dreammusic.fragment.FragmentMenuUser;
import dream.app.com.dreammusic.jpush.ExampleUtil;
import dream.app.com.dreammusic.ui.activity.MusicStoreActivity;
import dream.app.com.dreammusic.ui.activity.SettingActivity;
import dream.app.com.dreammusic.util.ActivityUtil;
import dream.app.com.dreammusic.util.AnimUtil;
import dream.app.com.dreammusic.util.SharedPreferencesUtil;
import dream.app.com.dreammusic.util.ThirdPlatformLoginUtil;

public class MainActivity extends InstrumentedActivity implements Handler.Callback,
        FragmentMenuLogin.LoginListener,View.OnClickListener,FragmentMain.FragmentClickListener{

    public static boolean isForeground = false;

    private DrawerLayout mSlideMenu;
    private TextView mSearchMusic,mChangeMainBg,mSleepTime,mSetting,mExit;
    private ImageButton mRightLogo,mLeftBack;
    private Handler mHandler;
    private android.app.Fragment mFragment;
    private View mDrawerView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUtil();
        initView();
        initListener();
        mHandler = new Handler(this);
    }

    @Override
    protected void onResume(){
        isForeground = true;
        updateLoginView();
        super.onResume();
    }

    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mMessageReceiver);
        super.onDestroy();
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
        initJPush();
    }
    public void initView(){
        mSlideMenu = (DrawerLayout) findViewById(R.id.slidemenu);
        mSearchMusic = (TextView) findViewById(R.id.tv_search_music);
        mChangeMainBg = (TextView) findViewById(R.id.tv_change_mainbg);
        mSetting = (TextView) findViewById(R.id.tv_setting);
        mSleepTime = (TextView) findViewById(R.id.tv_set_sleep_time);
        mExit = (TextView) findViewById(R.id.tv_exit);
        mRightLogo = (ImageButton) findViewById(R.id.ib_top_logo_right);
        mLeftBack  = (ImageButton) findViewById(R.id.ib_top_back);
        mLeftBack.setVisibility(View.GONE);
        mRightLogo.setVisibility(View.VISIBLE);
        mDrawerView = findViewById(R.id.drawer_layout);
        initLoginView();
        initMainView();
    }
    /**
     * 初始化MainFragment
     */
    private void initMainView(){
        getFragmentManager().beginTransaction().add(R.id.fragment_main,new FragmentMain()).commit();
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
        getFragmentManager().beginTransaction().add(R.id.fr_login_layout, mFragment).commit();
    }

    protected void initListener() {
        mSearchMusic.setOnClickListener(this);
        mChangeMainBg.setOnClickListener(this);
        mSleepTime.setOnClickListener(this);
        mSetting.setOnClickListener(this);
        mExit.setOnClickListener(this);
        mRightLogo.setOnClickListener(this);
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

        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_setting:
                startSettingActivity();
                break;
            case R.id.tv_exit:
                exit();
                break;
            case R.id.ib_top_logo_right:
                clickOnRightLogo();
                break;
            default:
                break;
        }
    }

    /**
     * 打开SettingActivity
     */
    private void startSettingActivity() {
        startNewActivity(SettingActivity.class, R.anim.base_slide_right_in,R.anim.base_slide_remain);
    }

    /**
     * 退出程序
     */
    private void exit() {
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
    @Override
    public void onBackPressed() {
        if(back_pressed+2000>System.currentTimeMillis())
            super.onBackPressed();
        else {
            showMessage("再按一次退出飞梦音乐");
            back_pressed = System.currentTimeMillis();
        }
    }

    private void showMessage(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }

    protected void startNewActivity(Class pclass,int inStyle,int outStyle){
        startActivity(new Intent(this,pclass));
        overridePendingTransition(inStyle,outStyle);
    }


    private void initJPush(){
        JPushInterface.init(getApplicationContext());
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
        registerMessageReceiver();
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
    public void click(int id) {
        switch (id){
            case R.id.view_music_store:
                Intent intent  = new Intent();
                intent.putExtra(ActivityUtil.TITLE,"乐库");
                startNewActivityWithAnim(MusicStoreActivity.class, intent);
                break;
        }
    }

    protected void startNewActivityWithAnim(Class pclass ,Intent intent){
        intent.setClass(this,pclass);
        startActivity(intent);
        overridePendingTransition(AnimUtil.BASE_SLIDE_RIGHT_IN,AnimUtil.BASE_SLIDE_REMAIN);
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
}

