package dream.app.com.dreammusic.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import net.tsz.afinal.FinalBitmap;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import dream.app.com.dreammusic.MainActivity;
import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.adapter.MyViewPagerAdapter;
import dream.app.com.dreammusic.entry.NetAPIEntry;
import dream.app.com.dreammusic.entry.NetMusicEntry;
import dream.app.com.dreammusic.entry.ShareEntry;
import dream.app.com.dreammusic.ui.view.LoadingDialog;
import dream.app.com.dreammusic.ui.view.ViewPagerIndicator;
import dream.app.com.dreammusic.util.DialogUtil;
import dream.app.com.dreammusic.util.DownLoadUtil;
import dream.app.com.dreammusic.util.MyHttpUtil;

/**
 * Created by JcMan on 2015/7/22.
 */
public class ShareActivity extends BaseActivity {

    private ViewPager mViewPager;
    private ViewPagerIndicator mIndicator;
    private Button mDloadBtn;
    private List<View> mViewsList;
    private TextView tv_lrc,tv_title,tv_singer,tv_size,tv_album_title,tv_album_info;
    private ImageView mSingerView,mAlbumView;
    private LoadingDialog loadingDialog;
    private FinalBitmap finalBitmap;
    private Bitmap loadingBitmap;

    private String mTitle,mSinger,mSize,mSongId;
    private String artist_500;
    private String album_500;
    private String mFileLink;
    private String mAlbumTitle;
    private String mAlbumInfo;
    private String mAlbumId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        initVariable();
        initView();
        initListener();
        getDataFromIntent();
        setTitle("歌曲推荐");

    }

    private void initVariable() {
        loadingDialog = DialogUtil.createLoadingDialog(this, "加载中···");
        finalBitmap = FinalBitmap.create(this);
        loadingBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_loading_image_big);
    }

    @Override
    public void initView() {
        super.initView();
        mViewPager = (ViewPager) findViewById(R.id.viewpager_shareactivity);
        mIndicator = (ViewPagerIndicator) findViewById(R.id.indicator_shareactivity);
        mDloadBtn = (Button) findViewById(R.id.btn_shareactivity_download);

        initPagerViews();
    }

    @Override
    protected void initListener() {
        super.initListener();
        mDloadBtn.setOnClickListener(this);
    }

    private void initPagerViews() {
        mViewsList = new ArrayList<View>();
        View v_music_info = View.inflate(this,R.layout.view_share_music_info,null);
        View v_album_info = View.inflate(this,R.layout.view_share_album_info,null);

        tv_lrc = (TextView) v_music_info.findViewById(R.id.tv_shareactivity_lrc);
        tv_title = (TextView) v_music_info.findViewById(R.id.tv_share_title);
        tv_singer = (TextView) v_music_info.findViewById(R.id.tv_share_singer);
        tv_size = (TextView) v_music_info.findViewById(R.id.tv_share_size);
        mSingerView = (ImageView) v_music_info.findViewById(R.id.iv_share_singer);

        mAlbumView = (ImageView) v_album_info.findViewById(R.id.iv_shareactivity_album);
        tv_album_title = (TextView) v_album_info.findViewById(R.id.tv_share_album_title);
        tv_album_info = (TextView) v_album_info.findViewById(R.id.tv_share_album_info);

        mViewsList.add(v_music_info);
        mViewsList.add(v_album_info);
        mViewPager.setAdapter(new MyViewPagerAdapter(this,mViewsList));
        initIndicator();
    }

    private void initIndicator() {
        List<String> _List = new ArrayList<String>();
        _List.add("歌曲信息");
        _List.add("专辑信息");
        mIndicator.setTabVisibleCount(2);
        mIndicator.setTabItemTitles(_List);
        mIndicator.setViewPager(mViewPager,0);
    }

    @Override
    public void onBackPressed(){
        startNewActivity(MainActivity.class);
        super.onBackPressed();
    }

    public void getDataFromIntent() {
        String json  = getIntent().getExtras().getString(JPushInterface.EXTRA_EXTRA);
        try {
            JSONObject ob = new JSONObject(json);
            mSongId = ob.getString("songid");
            String musicUrl = NetAPIEntry.getUrlBySongId(mSongId);
            send(musicUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initMusicInfoView() {
        tv_title.setText("歌曲："+mTitle);
        tv_singer.setText("歌手："+mSinger);
        tv_size.setText("大小："+mSize);
        finalBitmap.display(mSingerView,artist_500,loadingBitmap,loadingBitmap);
    }

    private void send(String musicUrl) {
        MyHttpUtil myhttp = new MyHttpUtil(musicUrl);
        loadingDialog.show();
        myhttp.send(new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> stringResponseInfo) {
                loadingDialog.cancel();
                try {
                    JSONObject object = new JSONObject(stringResponseInfo.result).getJSONObject("songinfo");
                    mFileLink = NetMusicEntry.getFileLink(stringResponseInfo.result);
                    mSize = NetMusicEntry.getFileSize(stringResponseInfo.result);
                    mTitle = object.getString(ShareEntry.TITLE);
                    mSinger = object.getString(ShareEntry.AUTHOR);
                    mAlbumId = object.getString(ShareEntry.ALBUM_ID);
                    mAlbumTitle = object.getString(ShareEntry.ALBUM_TITLE);
                    artist_500 = object.getString(ShareEntry.ARTIST_500_500);
                    album_500 = object.getString(ShareEntry.ALBUM_500_500);
                    String lrc_link = object.getString(ShareEntry.LRC_LINK);
                    initMusicInfoView();
                    initAlbumView();
                    getLrc(lrc_link);
                    getAlbumInfo(mAlbumId);
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

    private void initAlbumView() {
        tv_album_title.setText(mAlbumTitle);
        finalBitmap.display(mAlbumView,album_500,loadingBitmap,loadingBitmap);
    }

    private void getAlbumInfo(String albumid){
        String albumUrl = NetAPIEntry.getAlbumInfoUrl(albumid);
        MyHttpUtil myHttpUtil = new MyHttpUtil(albumUrl);
        myHttpUtil.send(new RequestCallBack<String>(){
            @Override
            public void onSuccess(ResponseInfo<String> stringResponseInfo) {
                try {
                    JSONObject object = new JSONObject(stringResponseInfo.result).getJSONObject("albumInfo");
                    String info = object.getString("info");
                    tv_album_info.setText(info);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(HttpException e, String s) {
            }
        });
    }

    private void getLrc(String lrc_link) {
        MyHttpUtil myHttpUtil = new MyHttpUtil(lrc_link);
        myHttpUtil.send(new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> stringResponseInfo) {
                String s[] = stringResponseInfo.result.split("\n");
                StringBuilder sb = new StringBuilder();
                for(int i=0;i<s.length;i++){
                    if(s[i].length()>11){
                        String _S = s[i].split("]")[1];
                        sb.append(_S).append("\n");
                    }
                }
                tv_lrc.setText("    "+sb.toString());
            }
            @Override
            public void onFailure(HttpException e, String s) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(v.getId()==R.id.btn_shareactivity_download){
            DownLoadUtil downLoadUtil = new DownLoadUtil(this,mFileLink,mTitle,mSinger);
            downLoadUtil.download();
        }
    }
}
