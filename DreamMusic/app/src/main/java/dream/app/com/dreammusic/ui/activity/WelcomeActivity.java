package dream.app.com.dreammusic.ui.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.ImageView;

import net.tsz.afinal.FinalBitmap;

import java.io.File;
import java.util.Random;
import java.util.logging.MemoryHandler;

import cn.jpush.android.api.JPushInterface;
import dream.app.com.dreammusic.MainActivity;
import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.config.ApplicationConfig;

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
