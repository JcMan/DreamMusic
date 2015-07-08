package dream.app.com.dreammusic.ui.activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.adapter.NetMusicAdapter;
import dream.app.com.dreammusic.entry.NetAPIEntry;
import dream.app.com.dreammusic.entry.NetMusicEntry;
import dream.app.com.dreammusic.ui.view.LoadingDialog;
import dream.app.com.dreammusic.util.DialogUtil;
import dream.app.com.dreammusic.util.DownLoadUtil;
import dream.app.com.dreammusic.util.MyHttpUtil;
import dream.app.com.dreammusic.util.PopupWindowUtil;

/**
 * Created by JcMan on 2015/7/6.
 */
public class RadioActivity extends BaseActivity implements AdapterView.OnItemClickListener{
    private String mCh_Name;
    private String mTitle;
    private ListView mListView;
    private List<NetMusicEntry> mList;
    private PopupWindow mPopupWindow;
    private LoadingDialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio);
        initVariable();
        getDataFromIntent();
        initView();
        initListener();
        updateView();

    }

    @Override
    public void initView() {
        super.initView();
        mListView = (ListView) findViewById(R.id.listview_radioactivity);
        loadingDialog = DialogUtil.createLoadingDialog(this,"加载中···");
    }

    @Override
    protected void initListener() {
        super.initListener();
        mListView.setOnItemClickListener(this);
    }

    private void initVariable() {
        mList = new ArrayList<NetMusicEntry>();
    }

    private void updateView() {
        setTitle(mTitle);
        initListView();
    }

    private void initListView() {
        loadingDialog.show();
        MyHttpUtil.getDefaultHttpUtil().send(HttpRequest.HttpMethod.GET,
                NetAPIEntry.getChannelMusicListUrlByCh_Name(mCh_Name), new RequestCallBack<String>(){
            @Override
            public void onSuccess(ResponseInfo<String> stringResponseInfo) {
                loadingDialog.cancel();
                getChannleMusicList(stringResponseInfo);
                String types[] ={NetMusicEntry.THUMB,NetMusicEntry.TITLE,NetMusicEntry.AUTHOR};
                mListView.setAdapter(new NetMusicAdapter(RadioActivity.this,mList,types));
            }

            @Override
            public void onFailure(HttpException e, String s) {
                loadingDialog.cancel();
            }
        });
    }

    private void getChannleMusicList(ResponseInfo<String> stringResponseInfo){
        try {
            JSONObject object = new JSONObject(stringResponseInfo.result);
            JSONObject object1 = object.getJSONObject("result");
            JSONArray array  = object1.getJSONArray("songlist");
            for(int i=0;i<array.length();i++){
                JSONObject object2 = array.getJSONObject(i);
                NetMusicEntry entry = new NetMusicEntry();
                entry.setSongid(object2.getString(NetMusicEntry.SONGID));
                entry.setTitle(object2.getString(NetMusicEntry.TITLE));
                entry.setAuthor(object2.getString(NetMusicEntry.ARTIST));
                entry.setThumb(object2.getString(NetMusicEntry.THUMB));
                mList.add(entry);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getDataFromIntent(){
        NetMusicEntry entry = (NetMusicEntry) getIntent().getBundleExtra("radio").getSerializable("entry");
        mCh_Name = entry.getCh_name();
        mTitle = entry.getName();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final NetMusicEntry entry = (NetMusicEntry) mListView.getItemAtPosition(position);
        String songId = entry.getSongid();
        loadingDialog.show();
        MyHttpUtil.getDefaultHttpUtil().send(HttpRequest.HttpMethod.GET,NetAPIEntry.getUrlBySongId(songId),new RequestCallBack<String>(){

            @Override
            public void onSuccess(ResponseInfo<String> stringResponseInfo) {
                loadingDialog.cancel();
                entry.setFile_link(NetMusicEntry.getFileLink(stringResponseInfo.result));
                mPopupWindow = PopupWindowUtil.createPopupWindow(RadioActivity.this, R.layout.layout_popupwindow_download);
                mPopupWindow.showAtLocation(RadioActivity.this.getWindow().getDecorView(),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                initPopBtnViewAndListener(entry);
            }

            @Override
            public void onFailure(HttpException e, String s){
                loadingDialog.cancel();
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
                        DownLoadUtil downloadUtil = new DownLoadUtil(RadioActivity.this,entry);
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
}
