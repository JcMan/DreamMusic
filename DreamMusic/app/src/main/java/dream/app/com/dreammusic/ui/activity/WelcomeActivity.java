package dream.app.com.dreammusic.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;

import java.io.File;

import cn.jpush.android.api.JPushInterface;
import dream.app.com.dreammusic.MainActivity;
import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.config.ApplicationConfig;
import dream.app.com.dreammusic.service.AlarmTimerService;

/**
 * Created by JcMan on 2015/6/29.
 */
public class WelcomeActivity extends BaseActivity implements Handler.Callback{

    private static final int MESSAGE_LOADIMAGE = 1;
    private static final int MESSAGE_GOTO_MAIN = 0;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mHandler = new Handler(this);
        mHandler.sendEmptyMessageDelayed(MESSAGE_GOTO_MAIN,3000);
        initDirs();
        startService();

    }

    private void startService() {
        startService(new Intent(this, AlarmTimerService.class));
    }

    private void initDirs() {
        File file = new File(ApplicationConfig.DOWNLOADDIE);
        if(!file.exists()){
            file.mkdirs();

        }
    }

    @Override
    public boolean handleMessage(Message msg){
        if(msg.what==MESSAGE_GOTO_MAIN){
            startNewActivity(MainActivity.class);
            finish();
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }
}
