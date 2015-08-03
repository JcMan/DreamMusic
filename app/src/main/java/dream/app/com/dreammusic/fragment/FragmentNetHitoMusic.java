package dream.app.com.dreammusic.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.app.tool.logger.Logger;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

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
import dream.app.com.dreammusic.util.ToastUtil;

/**
 * Created by JcMan on 2015/7/3.
 */
public class FragmentNetHitoMusic extends Fragment implements AdapterView.OnItemClickListener{
    private ListView mHitoMusicListView;
    private List<NetMusicEntry> mList;
    private LoadingDialog loadingDialog;
    private PopupWindow mPopupWindow;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_hito_music,container,false);
        initView(view);
        initListener();
        loadingDialog = DialogUtil.createLoadingDialog(getActivity(),"加载中···");
        return view;
    }

    private void initListener() {
        mHitoMusicListView.setOnItemClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateListView();
    }

    private void updateListView() {
        if(mList==null||mList.size()<1){
            loadingDialog.show();
            HttpUtils httpUtil = new HttpUtils();
            httpUtil.send(HttpRequest.HttpMethod.GET, NetAPIEntry.getHitoMusicUrl(), new RequestCallBack<String>(){

                @Override
                public void onSuccess(ResponseInfo<String> stringResponseInfo) {
                    loadingDialog.cancel();
                    mList = new ArrayList<NetMusicEntry>();
                    NetMusicEntry.setNetHitoMusicList(stringResponseInfo, mList);
                    String[] types = {NetMusicEntry.PIC_SMALL,NetMusicEntry.TITLE,NetMusicEntry.AUTHOR};
                    mHitoMusicListView.setAdapter(new NetMusicAdapter(getActivity(),mList,types));
                }
                @Override
                public void onFailure(HttpException e, String s) {
                    loadingDialog.cancel();
                    ToastUtil.showMessage(getActivity(), "加载失败");
                }
            });
        }
    }

    private void initView(View view) {
        mHitoMusicListView = (ListView) view.findViewById(R.id.listview_hito_music);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final NetMusicEntry entry = (NetMusicEntry) mHitoMusicListView.getItemAtPosition(position);
        String songId = entry.getSong_id();
        loadingDialog.show();
        MyHttpUtil.getDefaultHttpUtil().send(HttpRequest.HttpMethod.GET,NetAPIEntry.getUrlBySongId(songId),new RequestCallBack<String>(){

            @Override
            public void onSuccess(ResponseInfo<String> stringResponseInfo) {
                loadingDialog.cancel();
                entry.setFile_link(NetMusicEntry.getFileLink(stringResponseInfo.result));
                mPopupWindow = PopupWindowUtil.createPopupWindow(getActivity(), R.layout.layout_popupwindow_download);
                mPopupWindow.showAtLocation(getActivity().getWindow().getDecorView(),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                initPopBtnViewAndListener(entry);
            }
            @Override
            public void onFailure(HttpException e, String s) {
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
                        DownLoadUtil downloadUtil = new DownLoadUtil(getActivity(),entry);
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
