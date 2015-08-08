package dream.app.com.dreammusic.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import com.app.tool.logger.Logger;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.adapter.MyViewPagerAdapter;
import dream.app.com.dreammusic.adapter.SeekBarChangeListenerAdapter;
import dream.app.com.dreammusic.anim.PlayPageTransform;
import dream.app.com.dreammusic.bmob.BDownloadMusicInfo;
import dream.app.com.dreammusic.config.App;
import dream.app.com.dreammusic.config.ApplicationConfig;
import dream.app.com.dreammusic.entry.BgEntry;
import dream.app.com.dreammusic.entry.NetAPIEntry;
import dream.app.com.dreammusic.entry.NetMusicEntry;
import dream.app.com.dreammusic.entry.UserEntry;
import dream.app.com.dreammusic.service.MusicService;
import dream.app.com.dreammusic.ui.view.CDView;
import dream.app.com.dreammusic.ui.view.LoadingDialog;
import dream.app.com.dreammusic.ui.view.LrcView;
import dream.app.com.dreammusic.util.ActivityUtil;
import dream.app.com.dreammusic.util.AnimUtil;
import dream.app.com.dreammusic.util.DialogUtil;
import dream.app.com.dreammusic.util.ImageTools;
import dream.app.com.dreammusic.util.MusicUtil;
import dream.app.com.dreammusic.util.MyHttpUtil;
import dream.app.com.dreammusic.util.PopupWindowUtil;
import dream.app.com.dreammusic.util.ToastUtil;
import dream.app.com.dreammusic.util.lrc.GetLrc;

/**
 * Created by JcMan on 2015/7/20.
 */
public class LrcActivity extends Activity implements View.OnClickListener,MusicService.IMusicServiceListener{

    private MusicService mMusicService;
    private ServiceConnection conn;


    private View view_bg;
    private ImageButton mBackBtn,mPreBtn,mStartBtn,mPauseBtn,mNextBtn,mShareBtn;
    private SeekBar mSeekbar;
    private TextView tv_top_title,tv_top_singer,tv_bottom_end_time,tv_bottom_current_time;
    private CDView mCDView;
    private LrcView mLrcView_1;
    private LrcView mLrcView_9;

    private ViewPager mViewPager;
    private MyViewPagerAdapter mAdapter;
    private List<View> mViewsList;
    private PopupWindow mPopupWindow;
    private LoadingDialog loadingDialog;
    private String title;
    private String singer;
    private int songid;

    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            if(mMusicService!=null){
                mSeekbar.setProgress(mMusicService.getPlayerPosition());
                mLrcView_1.changeCurrent(mMusicService.getPlayerPosition());
                mLrcView_9.changeCurrent(mMusicService.getPlayerPosition());
                updateTimeView();
            }
            mHandler.sendEmptyMessageDelayed(1, 500);
        };
    };
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lrc);
        MusicService.addActivity(this);
        getDataFromIntent();
        initVariable();
        initView();
        initListener();
        bindService();


    }

    private void bindService() {
        Intent intent = new Intent(this,MusicService.class);
        bindService(intent,conn, Context.BIND_AUTO_CREATE);
    }

    private void initVariable() {
        initConn();
        mViewsList = new ArrayList<View>();
        loadingDialog = DialogUtil.createLoadingDialog(this,"加载中···");
    }



    private void initListener() {
        mBackBtn.setOnClickListener(this);
        mPauseBtn.setOnClickListener(this);
        mPreBtn.setOnClickListener(this);
        mStartBtn.setOnClickListener(this);
        mNextBtn.setOnClickListener(this);
        mShareBtn.setOnClickListener(this);
        mSeekbar.setOnSeekBarChangeListener(mSeekBarChangeListener);
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager_lrc);
        view_bg = findViewById(R.id.lrc_bg_layout);

        tv_top_singer = (TextView) findViewById(R.id.tv_lrc_top_singer);
        tv_top_title = (TextView) findViewById(R.id.tv_lrc_top_title);
        mSeekbar = (SeekBar) findViewById(R.id.sb_play_progress);

        mBackBtn = (ImageButton) findViewById(R.id.ib_lrc_top_back);
        mPreBtn = (ImageButton) findViewById(R.id.ib_play_pre);
        mStartBtn = (ImageButton) findViewById(R.id.ib_play_start);
        mPauseBtn = (ImageButton) findViewById(R.id.ib_play_pause);
        mNextBtn = (ImageButton) findViewById(R.id.ib_play_next);
        mShareBtn = (ImageButton) findViewById(R.id.ib_lrc_share);

        tv_bottom_current_time = (TextView) findViewById(R.id.tv_music_currentTime);
        tv_bottom_end_time = (TextView) findViewById(R.id.tv_music_endTime);

        initPagerViews();

        mAdapter = new MyViewPagerAdapter(this,mViewsList);
        mViewPager.setPageTransformer(true, new PlayPageTransform());
        mViewPager.setAdapter(mAdapter);
    }

    private void initPagerViews() {
        View pager_view_1 = View.inflate(this,R.layout.lrc_play_page_1,null);
        View pager_view_2 = View.inflate(this,R.layout.lrc_play_page_2,null);
        mCDView = (CDView) pager_view_1.findViewById(R.id.cdview);
        mLrcView_1 = (LrcView) pager_view_1.findViewById(R.id.play_first_lrc);
        mLrcView_9 = (LrcView) pager_view_2.findViewById(R.id.play_second_lrc);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_logo);
        mCDView.setImage(ImageTools.scaleBitmap(bm, (int) (App.sScreenWidth * 0.8)));
        mCDView.start();
        mViewsList.add(pager_view_1);
        mViewsList.add(pager_view_2);
    }

    private void setBackground(){
        String path = ApplicationConfig.ARTIST_DIR + singer + ".jpg";
        File file = new File(path);
        if(file.exists()){
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            view_bg.setBackground(new BitmapDrawable(bitmap));
        }else
            view_bg.setBackground(new BitmapDrawable(BgEntry.getDefaultBg(this)));
    }

    @Override
    protected void onResume(){
        super.onResume();
        setBackground();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(AnimUtil.BASE_SLIDE_REMAIN, AnimUtil.BASE_SLIDE_RIGHT_OUT);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_lrc_top_back:
                onBackPressed();
                break;
            case R.id.ib_play_pre:
                pre();
                break;
            case R.id.ib_play_start:
                start();
                break;
            case R.id.ib_play_pause:
                pause();
                break;
            case R.id.ib_play_next:
                next();
                break;
            case R.id.ib_lrc_share:
                BmobQuery<BDownloadMusicInfo> query = new BmobQuery<BDownloadMusicInfo>();
                query.addWhereEqualTo(BDownloadMusicInfo.LOGINID, UserEntry.getUid());
                query.addWhereEqualTo(BDownloadMusicInfo.TITLE,title);
                query.findObjects(this,new FindListener<BDownloadMusicInfo>(){
                    @Override
                    public void onSuccess(final List<BDownloadMusicInfo> bDownloadMusicInfos) {
                        if(bDownloadMusicInfos!=null&&bDownloadMusicInfos.size()>0){
                            if(!bDownloadMusicInfos.get(0).isShare()){
                                showShareDialog(bDownloadMusicInfos);
                            }else{
                                DialogUtil.showMessageDialog(LrcActivity.this,"该歌曲您已经分享过了");
                            }
                        }else{
                            DialogUtil.showMessageDialog(LrcActivity.this,"只有下载的歌曲才可以分享");
                        }
                    }

                    @Override
                    public void onError(int i, String s) {

                    }
                });
                break;
        }
    }

    private void showShareDialog(final List<BDownloadMusicInfo> bDownloadMusicInfos) {
        final Dialog dialog = new Dialog(this, R.style.Theme_loading_dialog);
        View _View = View.inflate(this,R.layout.dialog_share_music,null);
        TextView tv_name = (TextView) _View.findViewById(R.id.tv_dialog_share_name);
        TextView top_title = (TextView) _View.findViewById(R.id.tv_dialog_top_title);
        final EditText edit = (EditText) _View.findViewById(R.id.et_share_content);
        top_title.setText("歌曲分享");
        tv_name.setText(singer+" - "+title);
        Button btn_cancel = (Button) _View.findViewById(R.id.btn_dialog_share_cancel);
        Button btn_ok = (Button) _View.findViewById(R.id.btn_dialog_share_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                BDownloadMusicInfo info = bDownloadMusicInfos.get(0);
                String objectId = info.getObjectId();
                info = new BDownloadMusicInfo();
                info.setShare(true);
                info.setDesc(edit.getText().toString());
                info.update(LrcActivity.this,objectId,new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        DialogUtil.showMessageDialog(LrcActivity.this, "分享成功");
                    }
                    @Override
                    public void onFailure(int i, String s) {
                        DialogUtil.showMessageDialog(LrcActivity.this,"分享失败");
                    }
                });
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(_View);
        DialogUtil.setDialogAttr(dialog, LrcActivity.this);
        dialog.show();
    }

    private void next() {
        mMusicService.next();
        play();
    }
    private void pre() {
        mMusicService.pre();
        play();
    }

    private void start() {
        mMusicService.start();
    }

    private void pause(){
        mMusicService.pause();
    }

    private void play(){
        title = mMusicService.getMusicName();
        singer = mMusicService.getSinger();
        songid = mMusicService.getSongId();
        mSeekbar.setMax(mMusicService.getMusicDuration());
        setLrc();
        mCDView.start();
        updateUI();
        setBackground();
    }

    private void updateUI(){
        updateTitleAndSinger();
        updateStartAndPauseBtn();
        updateSingerImg();
        updateCDView();
    }

    private void updateCDView(){
        updateCDView(mMusicService.isPlaying());
    }

    private void updateStartAndPauseBtn(){
        updateStartAndPauseBtn(mMusicService.isPlaying());
    }

    private void setLrc() {
        File file = new File(ApplicationConfig.LRC_DIR+songid+".lrc");
        if(!file.exists())
            downloadDefaultLrc();
        setLrcPath(mMusicService.getSongId());
    }

    private void updateSingerImg(){
        updateSingerImg(mMusicService.getSongId());
    }

    private void updateSingerImg(int songId) {
        String path = ApplicationConfig.ARTIST_DIR+songId+".jpg";
        File file = new File(path);
        Bitmap bitmap = null;
        if(file.exists()){
            bitmap = BitmapFactory.decodeFile(path);
            bitmap = ImageTools.scaleBitmap(bitmap,(int)(App.sScreenWidth*0.8));
        }else{
            downloadPhoto();
            bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_logo);
            bitmap = ImageTools.scaleBitmap(bitmap,(int)(App.sScreenWidth*0.8));
        }
        mCDView.setImage(bitmap);
    }

    private void downloadPhoto() {
        downloadPhoto(singer);
    }

    private void setLrcPath(int songid) {
        mLrcView_1.setLrcPath(MusicUtil.getLrcPath(songid));
        mLrcView_9.setLrcPath(MusicUtil.getLrcPath(songid));
    }

    public void getDataFromIntent(){
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        singer = intent.getStringExtra("singer");
        songid = intent.getIntExtra("songid",0);
    }
    private void updateTitleAndSinger() {
        tv_top_title.setText(title+"");
        tv_top_singer.setText(singer+"");
    }

    private void updateCDView(boolean result){

        if(result)
            mCDView.start();
        else
            mCDView.pause();
    }

    private void initConn() {
        conn = new ServiceConnection(){
            @Override
            public void onServiceConnected(ComponentName name, IBinder service){
                mMusicService = ((MusicService.MusicBinder)service).getService();
                mMusicService.setOnMusicCompletion(LrcActivity.this);
                mSeekbar.setMax(mMusicService.getMusicDuration());
                mHandler.sendEmptyMessageDelayed(1, 500);
                updateCDView(mMusicService.isPlaying());
                updateSingerImg();
                updateTitleAndSinger();
                updateBottomView();
                setLrc();
            }

            @Override
            public void onServiceDisconnected(ComponentName name){
                mMusicService = null;
            }
        };
    }

    private void setLrcPath() {
        setLrcPath(mMusicService.getSongId());
    }

    private void updateBottomView() {
        updateStartAndPauseBtn(mMusicService.isPlaying());
        updateTimeView();
    }

    private void updateTimeView() {
        tv_bottom_end_time.setText(MusicUtil.makeTimeString(mMusicService.getMusicDuration()));
        tv_bottom_current_time.setText(MusicUtil.makeTimeString(mMusicService.getPlayerPosition()));
    }

    private void updateStartAndPauseBtn(boolean result) {
        if(result==true){
            mStartBtn.setVisibility(View.GONE);
            mPauseBtn.setVisibility(View.VISIBLE);
        }else{
            mStartBtn.setVisibility(View.VISIBLE);
            mPauseBtn.setVisibility(View.GONE);
        }
    }

    private SeekBarChangeListenerAdapter mSeekBarChangeListener = new SeekBarChangeListenerAdapter(){
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            super.onStopTrackingTouch(seekBar);
            int progress = seekBar.getProgress();
            mMusicService.seekTo(progress);
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_MENU&&event.getAction()==KeyEvent.ACTION_DOWN){
            mPopupWindow = PopupWindowUtil.createPopupWindow(this,R.layout.popw_lrc_bottom);
            mPopupWindow.showAtLocation(this.getWindow().getDecorView(),
                    Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            setPopwListener();
            return false;
        }
        return super.onKeyDown(keyCode,event);
    }

    private void setPopwListener() {
        View view_lrc = mPopupWindow.getContentView().findViewById(R.id.view_popw_lrc);
        View view_photo = mPopupWindow.getContentView().findViewById(R.id.view_popw_photo);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 switch (v.getId()){
                     case R.id.view_popw_lrc:
                         showDloadLrcDlg();
                         break;
                     case R.id.view_popw_photo:
                         showDloadPhotoDlg();
                         break;
                 }
            }
        };

        view_lrc.setOnClickListener(listener);
        view_photo.setOnClickListener(listener);
    }

    private void showDloadPhotoDlg() {
        mPopupWindow.dismiss();
        final Dialog dialog = new Dialog(this, R.style.Theme_loading_dialog);
        View _View = View.inflate(this, R.layout.dialog_dload_photo, null);
        TextView tv_title_dlg = (TextView) _View.findViewById(R.id.tv_dialog_top_title);
        tv_title_dlg.setText("搜索写真");
        final EditText edit_singer = (EditText) _View.findViewById(R.id.et_dialog_photo_singer);
        edit_singer.setText(singer);
        Button btn_cancel = (Button) _View.findViewById(R.id.btn_dialog_photo_cancel);
        Button btn_download = (Button) _View.findViewById(R.id.btn_dialog_photo_download);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.show();
                dialog.dismiss();
                downloadPhoto(edit_singer.getText().toString());

                MyHttpUtil myHttpUtil = new MyHttpUtil(NetAPIEntry.getSingerImageUrl(edit_singer.getText().toString()));
                myHttpUtil.send(new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        loadingDialog.cancel();
                        Intent intent = new Intent(LrcActivity.this,SingerImageActivity.class);
                        intent.putExtra("data",responseInfo.result);
                        intent.putExtra("singer",singer);
                        startActivity(intent);
                        overridePendingTransition(AnimUtil.BASE_SLIDE_RIGHT_IN,AnimUtil.BASE_SLIDE_REMAIN);

                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        Logger.e("failuere");
                        loadingDialog.cancel();
                    }
                });


            }

        });
        dialog.setContentView(_View);
        DialogUtil.setDialogAttr(dialog, this);
        dialog.show();
    }

    private void downloadPhoto(String _Singer){
        MyHttpUtil myHttpUtil = new MyHttpUtil(NetAPIEntry.getTingUidUrl(_Singer));
        myHttpUtil.send(new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> stringResponseInfo) {
                try {
                    JSONObject object = new JSONObject(stringResponseInfo.result);
                    JSONObject ob = object.getJSONObject(NetMusicEntry.ARTIST);
                    String ting_uid = ob.getString("ting_uid");
                    getPicUrl(ting_uid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(HttpException e, String s){
//                Logger.e("写真下失败");
            }
        });
    }
    private void getPicUrl(String ting_uid){
        MyHttpUtil myHttpUtil = new MyHttpUtil(NetAPIEntry.getSingerInfoUrlByTing_uid(ting_uid));
        myHttpUtil.send(new RequestCallBack<String>(){
            @Override
            public void onSuccess(ResponseInfo<String> stringResponseInfo){
                try {
                    final String path = ApplicationConfig.ARTIST_DIR+songid+".jpg";
                    JSONObject object = new JSONObject(stringResponseInfo.result);
                    String pic_url = object.getString(NetMusicEntry.AVATAR_BIG);
                    FinalHttp finalHttp = new FinalHttp();
                    finalHttp.download(pic_url,path,new AjaxCallBack<File>() {
                        @Override
                        public void onSuccess(File file){
                            super.onSuccess(file);
                            Bitmap bitmap = BitmapFactory.decodeFile(path);
                            Bitmap bmp = ImageTools.scaleBitmap(bitmap,(int)(App.sScreenWidth*0.8));
                            mCDView.setImage(bmp);
                        }
                        @Override
                        public void onFailure(Throwable t, int errorNo, String strMsg){
                            super.onFailure(t, errorNo, strMsg);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(HttpException e, String s) {
                loadingDialog.cancel();
            }
        });
    }

    private void showDloadLrcDlg(){
        mPopupWindow.dismiss();
        final Dialog dialog = new Dialog(this, R.style.Theme_loading_dialog);
        View _View = View.inflate(this, R.layout.dialog_dload_lrc, null);
        TextView tv_title_dlg = (TextView) _View.findViewById(R.id.tv_dialog_top_title);
        tv_title_dlg.setText("搜索歌词");
        final EditText edit_title = (EditText) _View.findViewById(R.id.et_dialog_lrc_title);
        final EditText edit_singer = (EditText) _View.findViewById(R.id.et_dialog_lrc_singer);
        edit_title.setText(title);
        edit_singer.setText(singer);
        Button btn_cancel = (Button) _View.findViewById(R.id.btn_dialog_lrc_cancel);
        Button btn_download = (Button) _View.findViewById(R.id.btn_dialog_lrc_download);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                final String _Title = edit_title.getText().toString();
                final String _Singer = edit_singer.getText().toString();
                downloadLrc(_Title, _Singer);
            }

        });
        dialog.setContentView(_View);
        DialogUtil.setDialogAttr(dialog, this);
        dialog.show();
    }

    private void downloadLrc(final String _Title, final String _Singer) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<String> lrcList = GetLrc.getLrc(_Title, _Singer);
                if(lrcList==null||lrcList.size()<10){
                    LrcActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showMessage(LrcActivity.this, "歌词下载失败");
                        }
                    });
                }else{
                    LrcActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MusicUtil.saveLrcFile(lrcList, songid + "");
                            setLrcPath();
                            ToastUtil.showMessage(LrcActivity.this, "歌词下载完成");
                        }
                    });
                }
            }
        }).start();
    }

    private void downloadDefaultLrc(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<String> lrcList = GetLrc.getLrc(title, singer);
                if(lrcList!=null&&lrcList.size()>10)
                    LrcActivity.this.runOnUiThread(new Runnable(){
                        @Override
                        public void run() {
                            MusicUtil.saveLrcFile(lrcList, songid + "");
                            setLrcPath();
                        }
                    });
                }
        }).start();
    }
    /************************************************************/
    /**
     * IMusicService的接口方法
     */
    @Override
    public void onMusicCompletion() {
        play();
    }

    @Override
    public void onMusicPlay() {
        play();
    }

    @Override
    public void onMusicPause() {
          updateUI();
    }

    @Override
    public void onMusicStop() {
        updateUI();
    }

    @Override
    public void onMusicStart() {
        updateUI();
    }

    @Override
    public void onMusicExit() {

    }
    /*****************************************************************/
}
