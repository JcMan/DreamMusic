package dream.app.com.dreammusic.ui.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.config.ApplicationConfig;
import dream.app.com.dreammusic.entry.UserEntry;
import dream.app.com.dreammusic.util.SharedPreferencesUtil;

/**
 * Created by JcMan on 2015/6/28.
 */
public class SettingActivity extends BaseActivity{

    private Button mExitBtn;
    private View mExitLayout;
    private View mAbout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
        initListener();
    }

    @Override
    public void initView() {
        super.initView();
        setTitle("设置");
        mExitLayout = findViewById(R.id.view_setting_exit);
        mExitBtn = (Button) findViewById(R.id.btn_setting_exit);
        mAbout = findViewById(R.id.view_setting_about);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mExitBtn.setOnClickListener(this);
        mExitLayout.setOnClickListener(this);
        mAbout.setOnClickListener(this);
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
}
