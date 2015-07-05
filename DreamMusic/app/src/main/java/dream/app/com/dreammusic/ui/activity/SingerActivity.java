package dream.app.com.dreammusic.ui.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.app.tool.logger.Logger;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import net.tsz.afinal.FinalBitmap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.entry.NetAPIEntry;
import dream.app.com.dreammusic.entry.NetMusicEntry;
import dream.app.com.dreammusic.ui.view.CircleView;
import dream.app.com.dreammusic.ui.view.LoadingDialog;
import dream.app.com.dreammusic.util.DialogUtil;
import dream.app.com.dreammusic.util.DownLoadUtil;
import dream.app.com.dreammusic.util.MyHttpUtil;

/**
 * Created by Administrator on 2015/7/5.
 */
public class SingerActivity extends BaseActivity{

    private ListView mAllMusicListView;
    private List<NetMusicEntry> mList;
    private String mSinger = "飞梦音乐";
    private String mAvatar_Big = "";
    private String mTingUID = "";
    private Bitmap loadBitmap;
    private LoadingDialog loadingdialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singer);
        loadingdialog = DialogUtil.createLoadingDialog(this,"加载中···");
        initVariable();
        initView();
        initListener();
        getDataFromIntent();
        updateView();
       /* mList = new ArrayList<NetMusicEntry>();
        for (int i=0;i<20;i++){
            mList.add(new NetMusicEntry());
        }
        mAllMusicListView.setAdapter(new AllMusicSingerAdapter(mList));*/
        getSingerInfo();
    }

    private void initVariable() {
        loadBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_loading_image_big);
    }

    private void updateView() {
        setTitle(mSinger);
        setSingerPic();
    }

    private void setSingerPic() {
        FinalBitmap finalBitmap = FinalBitmap.create(this);
        finalBitmap.display((ImageView)findViewById(R.id.iv_allmusic_pic),mAvatar_Big,loadBitmap,loadBitmap);
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    public void initView() {
        super.initView();
        mAllMusicListView = (ListView) findViewById(R.id.listview_allmusic_singer);
    }

    public void getDataFromIntent() {
        Bundle bundle = getIntent().getBundleExtra("singer");
        NetMusicEntry entry = (NetMusicEntry) bundle.getSerializable("entry");
        mSinger = entry.getName();
        mAvatar_Big = entry.getAvatar_big();
        mTingUID = entry.getTing_uid();
    }

    public void getSingerInfo() {
        loadingdialog.show();
        MyHttpUtil.getDefaultHttpUtil().send(HttpRequest.HttpMethod.GET,
                NetAPIEntry.getSingerInfoUrlByTing_uid(mTingUID), new RequestCallBack<String>(){

            @Override
            public void onSuccess(ResponseInfo<String> stringResponseInfo){
                TextView tv_desc = (TextView) findViewById(R.id.tv_allmusic_desc);
                tv_desc.setText("  "+getInfo(stringResponseInfo));
                MyHttpUtil.getDefaultHttpUtil().send(HttpRequest.HttpMethod.GET,
                        NetAPIEntry.getMusicOfSingerUrlByTingUid(mTingUID),new RequestCallBack<String>(){
                    @Override
                    public void onSuccess(ResponseInfo<String> stringResponseInfo) {
                        loadingdialog.cancel();
                        setList(stringResponseInfo);
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        loadingdialog.cancel();
                    }
                });
            }
            @Override
            public void onFailure(HttpException e, String s) {
                loadingdialog.cancel();
            }
        });
    }

    private String getInfo(ResponseInfo<String> stringResponseInfo) {
        String info = "";
        try {
            JSONObject object = new JSONObject(stringResponseInfo.result);
            info =  object.getString("intro");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return info;
    }

    public void setList(ResponseInfo<String> result) {
        if(mList==null)
            mList = new ArrayList<NetMusicEntry>();
        try {
            JSONObject object = new JSONObject(result.result);
            JSONArray array = object.getJSONArray("songlist");
            for(int i=0;i<array.length();i++){
                JSONObject ob = array.getJSONObject(i);
                NetMusicEntry entry = new NetMusicEntry();
                entry.setTitle(ob.getString(NetMusicEntry.TITLE));
                entry.setAuthor(ob.getString(NetMusicEntry.AUTHOR));
                entry.setSong_id(ob.getString(NetMusicEntry.SONG_ID));
                mList.add(entry);
            }
            mAllMusicListView.setAdapter(new AllMusicSingerAdapter(mList));
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    class AllMusicSingerAdapter extends BaseAdapter{
        private List<NetMusicEntry> list;
        public  AllMusicSingerAdapter(List<NetMusicEntry> list){
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            NetMusicEntry entry = list.get(position);
            Holder holder;
            if(convertView==null){
                holder = new Holder();
                convertView = LayoutInflater.from(SingerActivity.this).inflate(R.layout.list_item_allmusic_singer,null);
                holder.tv_num = (TextView) convertView.findViewById(R.id.tv_allmusic_singer_num);
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_allmusic_singer_name);
                holder.btn_download = (Button) convertView.findViewById(R.id.btn_allmusic_singer_download);
                convertView.setTag(holder);
            }else
                holder = (Holder) convertView.getTag();
            holder.tv_num.setText(""+(position+1));
            holder.tv_name.setText(entry.getTitle());
            holder.btn_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadingdialog.show();
                    final NetMusicEntry entry = (NetMusicEntry) mAllMusicListView.getItemAtPosition(position);
                    MyHttpUtil.getDefaultHttpUtil().send(HttpRequest.HttpMethod.GET,NetAPIEntry.getUrlBySongId(entry.getSong_id()),new RequestCallBack<String>(){

                        @Override
                        public void onSuccess(ResponseInfo<String> stringResponseInfo) {
                            loadingdialog.cancel();
                            NetMusicEntry musciEntry = new NetMusicEntry();
                            entry.setFile_link(NetMusicEntry.getFileLink(stringResponseInfo.result));
                            DownLoadUtil downloadUtil = new DownLoadUtil(SingerActivity.this,entry);
                            downloadUtil.download();
                        }

                        @Override
                        public void onFailure(HttpException e, String s) {
                            loadingdialog.cancel();
                        }
                    });
                }
            });
            return convertView;
        }

        class Holder{
            TextView tv_num;
            TextView tv_name;
            Button btn_download;
        }
    }
}
