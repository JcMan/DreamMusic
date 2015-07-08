package dream.app.com.dreammusic.ui.activity;

import android.os.Bundle;
import android.util.Xml;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.app.tool.logger.Logger;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.entry.NetAPIEntry;
import dream.app.com.dreammusic.entry.NetMusicEntry;
import dream.app.com.dreammusic.ui.view.LoadingDialog;
import dream.app.com.dreammusic.util.AnimUtil;
import dream.app.com.dreammusic.util.DialogUtil;
import dream.app.com.dreammusic.util.DownLoadUtil;
import dream.app.com.dreammusic.util.MyHttpUtil;
import dream.app.com.dreammusic.util.PopupWindowUtil;
import dream.app.com.dreammusic.util.ToastUtil;

/**
 * Created by Administrator on 2015/7/8.
 */
public class SearchActivity extends BaseActivity implements AdapterView.OnItemClickListener{

    private List<NetMusicEntry> mList;
    private ListView mListView;
    private String mQuery;
    private LoadingDialog loadingDialog;
    private PopupWindow mPopupWindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initVariable();
        initView();
        initListener();
        getDataFormIntent();
        search();
        loadingDialog = DialogUtil.createLoadingDialog(this,"加载中···");
        loadingDialog.show();
    }

    private void search() {
        try {
            String qurey = URLEncoder.encode(mQuery, "utf-8");
            MyHttpUtil myHttpUtil = new MyHttpUtil(NetAPIEntry.getSearchUrl(qurey));
            myHttpUtil.send(new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> stringResponseInfo) {

                    getNetMusicList(stringResponseInfo.result);
                    mListView.setAdapter(new SearchMusicAdapter());
                    loadingDialog.cancel();

                }
                @Override
                public void onFailure(HttpException e, String s) {
                    loadingDialog.cancel();
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void getDataFormIntent() {
        mQuery = getIntent().getStringExtra("query");
    }

    @Override
    public void initView() {
        super.initView();
        mListView = (ListView) findViewById(R.id.listview_search);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mListView.setOnItemClickListener(this);
    }

    private void initVariable() {
        mList = new ArrayList<NetMusicEntry>();
    }

    void getNetMusicList(String json){
        try {
            JSONObject object = new JSONObject(json);
            JSONArray array = object.getJSONArray(NetMusicEntry.SONG_LIST);
            for(int i=0;i<array.length();i++){
                JSONObject ob = array.getJSONObject(i);
                NetMusicEntry entry = new NetMusicEntry();
                entry.setTitle(getRightString(ob.getString(NetMusicEntry.TITLE)));
                entry.setSong_id(ob.getString(NetMusicEntry.SONG_ID));
                entry.setAuthor(getRightString(ob.getString(NetMusicEntry.AUTHOR)));
                entry.setAlbum_title(getRightString(ob.getString(NetMusicEntry.ALBUM_TITLE)));
                mList.add(entry);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        String song_id = mList.get(position).getSong_id();
        MyHttpUtil myHttpUtil = new MyHttpUtil(NetAPIEntry.getUrlBySongId(song_id));
        loadingDialog.show();
        myHttpUtil.send(new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> stringResponseInfo) {
                NetMusicEntry entry = mList.get(position);
                String file_link = NetMusicEntry.getFileLink(stringResponseInfo.result);
                entry.setFile_link(file_link);
                mPopupWindow = PopupWindowUtil.createPopupWindow(SearchActivity.this, R.layout.layout_popupwindow_download);
                mPopupWindow.showAtLocation(SearchActivity.this.getWindow().getDecorView(),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                initPopBtnViewAndListener(entry);
                loadingDialog.cancel();
            }

            @Override
            public void onFailure(HttpException e, String s){
                loadingDialog.cancel();
                ToastUtil.showMessage(SearchActivity.this,"该歌曲暂时无法下载");
            }
        });
    }

    private void initPopBtnViewAndListener(final NetMusicEntry entry) {
        Button btn_download = (Button) mPopupWindow.getContentView().findViewById(R.id.btn_popupwindow_download);
        Button btn_cancel = (Button) mPopupWindow.getContentView().findViewById(R.id.btn_popupwindow_cancel);
        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        DownLoadUtil downloadUtil = new DownLoadUtil(SearchActivity.this,entry);
                        downloadUtil.download();
                    }
                }.start();
                mPopupWindow.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });
    }

    class SearchMusicAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder = null;
            if(convertView==null){
                convertView = LayoutInflater.from(SearchActivity.this).inflate(R.layout.list_item_searchresult,null);
                holder = new Holder();
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name_search_list);
                holder.tv_author = (TextView) convertView.findViewById(R.id.tv_author_search_list);
                convertView.setTag(holder);
            }else
                holder = (Holder) convertView.getTag();
            NetMusicEntry entry = mList.get(position);
            holder.tv_name.setText(entry.getTitle());
            if (entry.getAlbum_title().length()>2)
                holder.tv_author.setText(entry.getAuthor()+" - "+"《"+entry.getAlbum_title()+"》");
            else
                holder.tv_author.setText(entry.getAuthor());
            return convertView;
        }

        class Holder{
            TextView tv_name;
            TextView tv_author;
        }
    }

    private String getRightString(String str){
        String re[] = {"<em>","<",">","<\\/em>","/em"};
        for(int i=0;i<re.length;i++)
            str = str.replaceAll(re[i],"");
        return str;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(AnimUtil.BASE_SLIDE_REMAIN,AnimUtil.BASE_SLIDE_RIGHT_OUT);
    }
}
