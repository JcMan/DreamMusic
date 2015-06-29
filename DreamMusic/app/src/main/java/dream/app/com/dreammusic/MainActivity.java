package dream.app.com.dreammusic;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.tool.logger.Logger;
import com.umeng.socialize.bean.SHARE_MEDIA;

import net.tsz.afinal.FinalBitmap;

import org.json.JSONException;
import org.json.JSONObject;

import dream.app.com.dreammusic.config.ApplicationConfig;
import dream.app.com.dreammusic.entry.UserEntry;
import dream.app.com.dreammusic.fragment.FragmentMenuLogin;
import dream.app.com.dreammusic.fragment.FragmentMenuUser;
import dream.app.com.dreammusic.ui.activity.BaseActivity;
import dream.app.com.dreammusic.ui.activity.SettingActivity;
import dream.app.com.dreammusic.ui.view.CircleView;
import dream.app.com.dreammusic.ui.view.SlideMenu;
import dream.app.com.dreammusic.util.SharedPreferencesUtil;
import dream.app.com.dreammusic.util.ThirdPlatformLoginUtil;

import static dream.app.com.dreammusic.entry.UserEntry.USERNAME;

public class MainActivity extends BaseActivity implements Handler.Callback,FragmentMenuLogin.LoginListener{

    private SlideMenu mSlideMenu;
    private TextView mSearchMusic,mChangeMainBg,mSleepTime,mSetting,mExit;
    private Handler mHandler;
    private android.app.Fragment mFragment;

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
        super.onResume();
        updateLoginView();
    }

    /**
     * 更新登录界面，如果退出账号
     */
    private void updateLoginView() {
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
    }
    @Override
    public void initView(){
        super.initView();
        mSlideMenu = (SlideMenu) findViewById(R.id.slidemenu);
        mSearchMusic = (TextView) findViewById(R.id.tv_search_music);
        mChangeMainBg = (TextView) findViewById(R.id.tv_change_mainbg);
        mSetting = (TextView) findViewById(R.id.tv_setting);
        mSleepTime = (TextView) findViewById(R.id.tv_set_sleep_time);
        mExit = (TextView) findViewById(R.id.tv_exit);
        setTopBackBtnGone();
        setTopLeftLogoVisible();
        initLoginView();
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

    @Override
    protected void initListener() {
        super.initListener();
        mSlideMenu.setOnClickListener(this);
        mSearchMusic.setOnClickListener(this);
        mChangeMainBg.setOnClickListener(this);
        mSleepTime.setOnClickListener(this);
        mSetting.setOnClickListener(this);
        mExit.setOnClickListener(this);
    }
    @Override
    protected void clickOnLeftLogo(){
        super.clickOnLeftLogo();
        mSlideMenu.toggle();
    }
    @Override
    public boolean handleMessage(Message msg){

        return false;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.tv_setting:
                startNewActivity(SettingActivity.class,R.anim.base_slide_right_in,R.anim.base_slide_remain);
                break;
            default:
                break;
        }
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
}
