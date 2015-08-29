package dream.app.com.dreammusic.service;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.widget.RemoteViews;

import com.app.tool.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dream.app.com.dreammusic.MainActivity;
import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.config.ApplicationConfig;
import dream.app.com.dreammusic.db.PlayHistoryDAO;
import dream.app.com.dreammusic.model.Music;
import dream.app.com.dreammusic.sensor.SensorManagerHelper;
import dream.app.com.dreammusic.util.MusicUtil;
import dream.app.com.dreammusic.util.SharedPreferencesUtil;

/**
 * Created by Administrator on 2015/7/18.
 */
public class MusicService extends Service implements MediaPlayer.OnCompletionListener{

    public static final int TYPE_MUSIC_LOCAL = 0;
    public static final int TYPE_MUSIC_DOWNLOAD = 1;
    public static final int TYPE_MUSIC_SUIBIANTING = 2;

    private static final String SERVICE_NOTIFICATION_NEXT_MUSIC = "service_notification_next_music";
    private static final String SERVICE_NOTIFICATION_PRE_MUSIC = "service_notification_pre_music";
    private static final String SERVICE_NOTIFICATION_PAUSE_START_MUSIC = "service_notification_pause_start_music";
    private static final String SERVICE_NOTIFICATION_EXIT_MUSIC = "service_notification_exit_music";

    public static final int STATE_PALYING  = 0;
    public static final int STATE_PAUSE  = 1;
    public static final int STATE_STOP  = 2;

    public static final int PLAY_MODE_SEQ = 0;
    public static final int PLAY_MODE_RANDOM = 1;
    public static final int PLAY_MODE_SINGLE = 2;

    private List<Music> mMusicList;
    private MediaPlayer mPlayer;
    private int mState = STATE_STOP;
    private int mListType = TYPE_MUSIC_LOCAL;
    private int mCurrentPosition = -1;
    private Notification notification;
    private NotificationManager manager ;
    private SensorManagerHelper sensorManagerHelper;
    private Vibrator mVibrator;
    private static List<Activity> mActivityList;
    private BroadcastReceiver receiver_next = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(SERVICE_NOTIFICATION_NEXT_MUSIC)) {
                next();
            }
        }
    };



    private BroadcastReceiver receiver_pre = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(SERVICE_NOTIFICATION_PRE_MUSIC)) {
                pre();
            }
        }
    };
    private BroadcastReceiver receiver_exit = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(SERVICE_NOTIFICATION_EXIT_MUSIC)) {
                Logger.e("exot");
                stopSelf();
                exit();
            }
        }
    };
    private BroadcastReceiver receiver_start_pause = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(SERVICE_NOTIFICATION_PAUSE_START_MUSIC)) {
                if(mState==STATE_PAUSE)
                    start();
                else
                    pause();

            }
        }
    };

    private void updateStartPauseImg() {
        if(mState==STATE_PAUSE){
            notification.contentView.setImageViewResource(R.id.ib_notification_start_pause,R.drawable.ic_fm_item_play_play);
        }else{
            notification.contentView.setImageViewResource(R.id.ib_notification_start_pause,R.drawable.ic_fm_item_play_pause);
        }
        manager.notify(1,notification);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MusicBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate(){
        super.onCreate();
        setMusicList(MusicUtil.queryLocalMusic(this));
        mPlayer = new MediaPlayer();
        mPlayer.setOnCompletionListener(this);
        notification = new Notification(R.drawable.ic_launcher,
                "飞梦音乐", System.currentTimeMillis());
        RemoteViews remoteViews = new RemoteViews(getPackageName(),R.layout.notification_service);
        notification.contentView = remoteViews;
        notification.contentIntent = PendingIntent.getActivities(this,0, new Intent[]{new Intent(this, MainActivity.class)},PendingIntent.FLAG_UPDATE_CURRENT);
        initButtomClickListener(notification.contentView);
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mActivityList = new ArrayList<Activity>();
        initSensor();
        startForeground(1, notification);
    }

    public static void addActivity(Activity activity){
        mActivityList.add(activity);
    }

    public void exit() {    //遍历List，退出每一个Activity
        try {
            for (Activity activity : mActivityList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    private void initSensor(){
        boolean isEnable = SharedPreferencesUtil.getShakeEnable();
        if(isEnable){
            sensorManagerHelper = new SensorManagerHelper(this);
            sensorManagerHelper.setOnShakeListener(new SensorManagerHelper.OnShakeListener() {
                @Override
                public void onShake() {
                    mVibrator.vibrate(new long[]{500, 200}, -1);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            next();
                        }
                    }, 500);
                }
            });
        }
    }

    public void setShakeEnable(){
        sensorManagerHelper.start();
    }

    public void setShakeUnable(){
        sensorManagerHelper.stop();
    }

    private void initButtomClickListener(RemoteViews remoteViews) {
        IntentFilter filter_next = new IntentFilter();
        filter_next.addAction(SERVICE_NOTIFICATION_NEXT_MUSIC);
        registerReceiver(receiver_next, filter_next);
        Intent Intent_next = new Intent(SERVICE_NOTIFICATION_NEXT_MUSIC);
        PendingIntent pendIntent_next = PendingIntent.getBroadcast(this, 0, Intent_next, 0);
        remoteViews.setOnClickPendingIntent(R.id.ib_notification_next,pendIntent_next);

        IntentFilter filter_pre = new IntentFilter();
        filter_pre.addAction(SERVICE_NOTIFICATION_PRE_MUSIC);
        registerReceiver(receiver_pre, filter_pre);
        Intent Intent_pre = new Intent(SERVICE_NOTIFICATION_PRE_MUSIC);
        PendingIntent pendIntent_pre = PendingIntent.getBroadcast(this, 0, Intent_pre, 0);
        remoteViews.setOnClickPendingIntent(R.id.ib_notification_pre,pendIntent_pre);

        IntentFilter filter_start_pause = new IntentFilter();
        filter_start_pause.addAction(SERVICE_NOTIFICATION_PAUSE_START_MUSIC);
        registerReceiver(receiver_start_pause, filter_start_pause);
        Intent Intent_start_pause = new Intent(SERVICE_NOTIFICATION_PAUSE_START_MUSIC);
        PendingIntent pendIntent_pause = PendingIntent.getBroadcast(this, 0, Intent_start_pause, 0);
        remoteViews.setOnClickPendingIntent(R.id.ib_notification_start_pause,pendIntent_pause);

        IntentFilter filter_exit = new IntentFilter();
        filter_exit.addAction(SERVICE_NOTIFICATION_EXIT_MUSIC);
        registerReceiver(receiver_exit, filter_exit);
        Intent Intent_exit = new Intent(SERVICE_NOTIFICATION_EXIT_MUSIC);
        PendingIntent pendIntent_Exit = PendingIntent.getBroadcast(this, 0, Intent_exit, 0);
        remoteViews.setOnClickPendingIntent(R.id.ib_notification_exit,pendIntent_Exit);
    }

    public void setMusicList(List<Music> list){
        mMusicList = list;
    }
    public void setMusicList(List<Music> list,int type){
        mMusicList = list;
        mListType = type;
    }

    public List<Music> getMusicList(){
        return mMusicList;
    }


    public int getPlayerPosition(){
        int position = 0;
        try{
            position = mPlayer.getCurrentPosition();
        }catch (Exception e){}
        return position;
    }

    public int getMusicDuration(){
        try{
            if(mCurrentPosition>-1)
                return getMusic(mCurrentPosition).duration;
        }catch (Exception e){}
        return 0;
    }

    public void seekTo(int position){
        mPlayer.seekTo(position);
    }

    public int getState(){
        return mState;
    }

    public void start(){
        if(mState==STATE_PAUSE){
            mPlayer.start();
            mState=STATE_PALYING;
            updateStartPauseImg();
            listener.onMusicStart();
        }

    }

    public boolean isStart(){
        return mState==STATE_PALYING;
    }

    public boolean isPause(){
        return mState==STATE_PAUSE;
    }

    public Music getMusic(){
        return getMusic(mCurrentPosition);
    }

    public void play(int position){
        if(position>-1&&position<mMusicList.size()){
            Uri uri = Uri.parse(getMusic(position).data);
            mPlayer.reset();
            try {
                mPlayer.setDataSource(this,uri);
                mPlayer.prepare();
                mPlayer.start();
                mState = STATE_PALYING;
                mCurrentPosition = position;
                updateRemoteViews();
                listener.onMusicPlay();
                addToHistory(getMusic());
            } catch (IOException e) {
                next();
            }
        }
    }

    private void addToHistory(Music music){
        PlayHistoryDAO playHistoryDAO = new PlayHistoryDAO(this);
        playHistoryDAO.saveHistory(music,(int)System.currentTimeMillis()/1000);
    }

    private void updateRemoteViews() {
        String path = ApplicationConfig.ARTIST_DIR+getMusic().songId+".jpg";
        File file = new File(path);
        Bitmap bitmap = null;
        if (file.exists()){
            bitmap = BitmapFactory.decodeFile(path);
        }else
            bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher);
        notification.contentView.setImageViewBitmap(R.id.iv_notification_singer, bitmap);
        notification.contentView.setTextViewText(R.id.tv_notification_title,getMusicName());
        notification.contentView.setTextViewText(R.id.tv_notification_singer, getSinger());
        updateStartPauseImg();
        manager.notify(1, notification);
    }
    public boolean isStop(){
        return mState==STATE_STOP;
    }
    public boolean isPlaying(){
        return mState == STATE_PALYING;
    }

    public int getRandomPosition(){
        return Math.abs(new Random().nextInt()%mMusicList.size());
    }

    public void setState(int state){
        mState= state;
    }

    public void next(){
        int mode = SharedPreferencesUtil.getPlayMode();
        switch (mode){
            case PLAY_MODE_SEQ:
                if(mCurrentPosition+1<mMusicList.size()){
                    ++mCurrentPosition;
                }
                break;
            case PLAY_MODE_RANDOM:
                mCurrentPosition = getRandomPosition();
                break;
        }
        play(mCurrentPosition);
    }


    public void pre(){
        if(mCurrentPosition-1>=0){
            play(--mCurrentPosition);
        }
    }

    public void pause(){
        if(mState==STATE_PALYING){
            mPlayer.pause();
            mState = STATE_PAUSE;
            updateStartPauseImg();
            listener.onMusicPause();
        }
    }

    public void stop(){
        if(mState!=STATE_STOP){
            mPlayer.stop();
            mState = STATE_STOP;
            listener.onMusicStop();
        }
    }

    Music getMusic(int position){
        if(position<0)
            return null;
        return mMusicList.get(position);
    }

    public String getMusicName(){
        if(mState==STATE_STOP)
            return "";
        Music music = getMusic(mCurrentPosition);
        if(music==null)
            return "";
        if(music.musicName.contains("-")){
            String[] _S = music.musicName.split("-");
            return _S[1].trim();
        }
        return music.musicName.trim();
    }

    public int getPosition(){
        return mCurrentPosition;
    }


    public String getSinger(){
        if(mState==STATE_STOP)
            return "";
        Music music = getMusic(mCurrentPosition);
        if(music==null)
            return "";
        if(music.musicName.contains("-")){
            String[] _S = music.musicName.split("-");
            return _S[0].trim();
        }
        return music.artist.trim();
    }

    public int getSongId(){
       return getMusic(mCurrentPosition).songId;
    }

    public Bitmap getLocalSingerBitmap(){
        Bitmap bitmap = null;
        Music music = getMusic(mCurrentPosition);
        bitmap = MusicUtil.getMusicBitemp(this,music.songId,music.albumId);
        return bitmap;
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        if(sensorManagerHelper!=null)
            sensorManagerHelper.stop();
    }

    private IMusicServiceListener listener;
    /**
     * 歌曲播放完成，自动播放下一首
     * @param mp
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        next();
        listener.onMusicCompletion();
    }

    public int getListType(){
        return mListType;
    }

    public void setListType(int type){
        mListType = type;
    }

    public void setOnMusicCompletion(IMusicServiceListener listener){
        this.listener = listener;
    }

    public interface IMusicServiceListener{
        public void onMusicCompletion();
        public void onMusicPlay();
        public void onMusicPause();
        public void onMusicStop();
        public void onMusicStart();
        public void onMusicExit();
    }

    public class MusicBinder extends Binder{
        public MusicService getService(){
            return MusicService.this;
        }
    }



    }
