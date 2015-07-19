package dream.app.com.dreammusic.ui.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;

import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.config.ApplicationConfig;
import dream.app.com.dreammusic.service.AlarmTimerService;
import dream.app.com.dreammusic.ui.view.CircleTimerView;
import dream.app.com.dreammusic.util.AnimUtil;

/**
 * Created by Administrator on 2015/7/12.
 */
public class AlarmTimerActivity extends BaseActivity implements CircleTimerView.CircleTimerListener{

    private AlarmTimerService mAlarmTimerService;
    private Button btn_cancel,btn_set;
    private CircleTimerView mCircleTimerView;
    private boolean isCancel = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmtimer);
        initView();
        initListener();

    }
    @Override
    protected void onResume() {
        super.onResume();
        bindService();
    }

    private void updateView() {
        int currentTime  = mAlarmTimerService.getCurrentTime();
        if(currentTime>0){
            mCircleTimerView.setCurrentRadian(currentTime);
            mCircleTimerView.startTimer();
            setbtnVisible(0);
        }
    }

    private void bindService() {
        Intent intent = new Intent(this,AlarmTimerService.class);
        boolean result = bindService(intent,conn, Context.BIND_AUTO_CREATE);
    }

    private void unbindService() {
        unbindService(conn);
    }

    @Override
    public void initView() {
        super.initView();
        btn_cancel = (Button) findViewById(R.id.btn_cancel_alarm);
        btn_set = (Button) findViewById(R.id.btn_set_alarm);
        mCircleTimerView = (CircleTimerView) findViewById(R.id.circleTimeView);
    }

    @Override
    protected void initListener() {
        super.initListener();
        btn_set.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        mCircleTimerView.setCircleTimerListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.btn_cancel_alarm:
                cancelAlarm();
                break;
            case R.id.btn_set_alarm:
                setAlarmTime();
                break;
        }
    }

    private void cancelAlarm(){
        if(mCircleTimerView.getState()==mCircleTimerView.STATE_START){
            mCircleTimerView.pauseTimer();
            mAlarmTimerService.setCurrentTime(0);
            mAlarmTimerService.setState(AlarmTimerService.STATE_CANCEL);
            setbtnVisible(1);
        }
    }
    private void setAlarmTime() {
        int currentTime = mCircleTimerView.getCurrentTime();
        if(currentTime>0){
            mAlarmTimerService.setCurrentTime(currentTime);
            mCircleTimerView.startTimer();
            setbtnVisible(0);
            mAlarmTimerService.setState(AlarmTimerService.STATE_ALARM);
        }
    }

    private void setbtnVisible(int type){
        switch (type){
            case 0:
                btn_cancel.setVisibility(View.VISIBLE);
                btn_set.setVisibility(View.GONE);
                break;
            case 1:
                btn_cancel.setVisibility(View.GONE);
                btn_set.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(AnimUtil.BASE_SLIDE_REMAIN,AnimUtil.BASE_SLIDE_RIGHT_OUT);

    }

    private ServiceConnection conn = new ServiceConnection(){
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mAlarmTimerService = ((AlarmTimerService.AlarmTimerBinder)service).getService();
            updateView();
        }

        @Override
        public void onServiceDisconnected(ComponentName name){
            mAlarmTimerService = null;
        }
    };

    @Override
    protected void onDestroy(){
        super.onDestroy();
        unbindService();
        mCircleTimerView.pauseTimer();
    }

    @Override
    public void onTimerStop() {
        //Logger.e("TimeStop");
        setbtnVisible(1);
        sendBroadcast(new Intent(ApplicationConfig.RECEIVER_ALARM));
        mAlarmTimerService.setState(AlarmTimerService.STATE_CANCEL);

    }

    @Override
    public void onTimerStart(){
    //    Logger.e("TimeStart");
    }

    @Override
    public void onTimerPause() {
      //  Logger.e("TimePause");

    }


}
