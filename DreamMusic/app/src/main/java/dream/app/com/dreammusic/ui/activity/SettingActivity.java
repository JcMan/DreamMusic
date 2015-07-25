package dream.app.com.dreammusic.ui.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.app.tool.logger.Logger;

import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.config.ApplicationConfig;
import dream.app.com.dreammusic.entry.UserEntry;
import dream.app.com.dreammusic.ui.view.SwitchButton;
import dream.app.com.dreammusic.util.SharedPreferencesUtil;

/**
 * Created by JcMan on 2015/6/28.
 */
public class SettingActivity extends BaseActivity implements SwitchButton.OnStateChangedListener{

    private Button mExitBtn;
    private View mExitLayout;
    private View mAbout;
    private SwitchButton mAcceptTSBtn,mShakeBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
        initListener();
        toggleAcceptTuiSongButtn();
        toggleShakeButtn();
    }

    @Override
    public void initView() {
        super.initView();
        setTitle("设置");
        mExitLayout = findViewById(R.id.view_setting_exit);
        mExitBtn = (Button) findViewById(R.id.btn_setting_exit);
        mAbout = findViewById(R.id.view_setting_about);
        mAcceptTSBtn = (SwitchButton) findViewById(R.id.sbtn_accept_tuisong);
        mShakeBtn = (SwitchButton) findViewById(R.id.sbtn_shake_enable);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mExitBtn.setOnClickListener(this);
        mExitLayout.setOnClickListener(this);
        mAbout.setOnClickListener(this);
        mAcceptTSBtn.setOnStateChangedListener(this);
        mShakeBtn.setOnStateChangedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences preferences = SharedPreferencesUtil.getSharedPreferences(ApplicationConfig.USER);
        boolean login = preferences.getBoolean(UserEntry.LOGIN,false);
        if(!login){
            mExitLayout.setVisibility(View.GONE);
        }

    }



    /**
     * 退出当前账号
     */
    private void logout(){
        SharedPreferences.Editor editor = SharedPreferencesUtil.getEditor(ApplicationConfig.USER);
        editor.putBoolean(UserEntry.LOGIN,false);
        editor.commit();
        showMessage("退出成功");
        mExitLayout.setVisibility(View.GONE);
        finish();
    }
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.btn_setting_exit:
                logout();
                break;
            case R.id.view_setting_about:
                startNewActivity(AboutActivity.class);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.base_slide_remain,R.anim.base_slide_right_out);
    }

    @Override
    public void toggleToOn(View v){
        if(v.getId()==R.id.sbtn_accept_tuisong){
            Logger.e("推送打开");
            mAcceptTSBtn.toggleSwitch(true);
            SharedPreferencesUtil.setAcceptTuiSong(true);
        }else if(v.getId()==R.id.sbtn_shake_enable){
            Logger.e("摇晃打开");
            mShakeBtn.toggleSwitch(true);
            SharedPreferencesUtil.setShakeEnable(true);
        }
    }

    @Override
    public void toggleToOff(View v){
        if(v.getId()==R.id.sbtn_accept_tuisong){
            Logger.e("推送关闭");
            mAcceptTSBtn.toggleSwitch(false);
            SharedPreferencesUtil.setAcceptTuiSong(false);
        }else if(v.getId()==R.id.sbtn_shake_enable){
            Logger.e("摇晃关闭");
            mShakeBtn.toggleSwitch(false);
            SharedPreferencesUtil.setShakeEnable(false);
        }
    }

    private void toggleAcceptTuiSongButtn(){
        boolean accept = SharedPreferencesUtil.getAcceptTuiSong();
        mAcceptTSBtn.toggleSwitch(accept);
    }

    private void toggleShakeButtn() {
        boolean enable = SharedPreferencesUtil.getShakeEnable();
        mShakeBtn.toggleSwitch(enable);
    }
}
