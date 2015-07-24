package dream.app.com.dreammusic.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.widget.RemoteViews;

import java.io.File;
import java.io.IOException;
import java.util.List;

import dream.app.com.dreammusic.MainActivity;
import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.config.ApplicationConfig;
import dream.app.com.dreammusic.model.Music;
import dream.app.com.dreammusic.util.MusicUtil;

/**
 * Created by Administrator on 2015/7/18.
 */
public class MusicService extends Service implements MediaPlayer.OnCompletionListener{

    public static final int TYPE_MUSIC_LOCAL = 0;
    public static final int TYPE_MUSIC_DOWNLOAD = 1;
    public static final int TYPE_MUSIC_SUIBIANTING = 2;

    public static final int STATE_PALYING  = 0;
    public static final int STATE_PAUSE  = 1;
    public static final int STATE_STOP  = 2;

    private List<Music> mMusicList;
    private MediaPlayer mPlayer;
    private int mState = STATE_STOP;
    private int mListType = TYPE_MUSIC_LOCAL;
    private int mCurrentPosition = -1;
    private Notification notification;
    @Override
    public IBinder onBind(Intent intent) {
        return new MusicBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate(){
        super.onCreate();
        setMusicList(MusicUtil.queryLocalMusic(this));
        mPlayer = new MediaPlayer();
        mPlayer.setOnCompletionListener(this);
        notification = new Notification(R.drawable.ic_launcher,
                "有通知到来", System.currentTimeMillis());
        RemoteViews remoteViews = new RemoteViews(getPackageName(),R.layout.notification_service);
        notification.contentView = remoteViews;
        notification.contentIntent = PendingIntent.getActivities(this,0, new Intent[]{new Intent(this, MainActivity.class)},PendingIntent.FLAG_UPDATE_CURRENT);
       /* downloadNotification = new Notification(R.drawable.downnoti,apkname+“下载…”,System.currentTimeMillis());
        downloadNotification.contentView = newRemoteViews(getPackageName(),R.layout.notification);
        //设置进度条的最大进度和初始进度
        downloadNotification.contentView.setProgressBar(R.id.pb, 100,0, false);
        downloadNM.notify(downNotiID, downloadNotification);*/
        startForeground(1, notification);
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
        return mPlayer.getCurrentPosition();
    }

    public int getMusicDuration(){
        if(mCurrentPosition>-1)
            return getMusic(mCurrentPosition).duration;
        else return 0;
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
        }
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
            } catch (IOException e) {}
        }
    }

    private void updateRemoteViews() {
        String path = ApplicationConfig.ARTIST_DIR+getMusic().songId+".jpg";
        File file = new File(path);
        Bitmap bitmap = null;
        if (file.exists()){
            bitmap = BitmapFactory.decodeFile(path);
        }else
            bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher);
        notification.contentView.setImageViewBitmap(R.id.iv_notification_singer,bitmap);
        notification.contentView.setTextViewText(R.id.tv_notification_title,getMusicName());
        notification.contentView.setTextViewText(R.id.tv_notification_singer,getSinger());
        startForeground(1, notification);
    }
    public boolean isStop(){
        return mState==STATE_STOP;
    }
    public boolean isPlaying(){
        return mState == STATE_PALYING;
    }

    public void next(){
        if(mCurrentPosition+1<mMusicList.size()){
            play(++mCurrentPosition);
        }
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
        }
    }

    public void stop(){
        if(mState!=STATE_STOP){
            mPlayer.stop();
            mState = STATE_STOP;
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
    public void onDestroy() {
        super.onDestroy();
    }

    private IMusicCompletionListener listener;
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

    public void setOnMusicCompletion(IMusicCompletionListener listener){
        this.listener = listener;
    }

    public interface IMusicCompletionListener{
        public void onMusicCompletion();
    }

    public class MusicBinder extends Binder{
        public MusicService getService(){
            return MusicService.this;
        }
    }


}
