package dream.app.com.dreammusic.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import dream.app.com.dreammusic.config.ApplicationConfig;

/**
 * Created by JcMan on 2015/7/12.
 */
public class AlarmTimerService extends Service {

    public static final int STATE_ALARM = 0;
    public static final int STATE_CANCEL = 1;

    private int currentTime = 0;
    private int mState = STATE_CANCEL;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(currentTime>=1){
                currentTime--;
            }else if(mState==STATE_ALARM){
                sendBroadcast(new Intent(ApplicationConfig.RECEIVER_ALARM));
                mState = STATE_CANCEL;
            }
            mHandler.sendEmptyMessageDelayed(0, 1000);
        }
    };

    public void setState(int state){
        mState = state;
    }
    @Override
    public IBinder onBind(Intent intent) {
        return new AlarmTimerBinder();
    }

    public class AlarmTimerBinder extends Binder{
        public AlarmTimerService getService(){
            return AlarmTimerService.this;
        }
    }


    public int getCurrentTime(){
        return currentTime;
    }

    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Logger.e("onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler.sendEmptyMessageDelayed(0, 1000);
//        Logger.e("onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(0);
//        Logger.e("onDestory");

    }
}
