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

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.w3c.dom.Text;

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
import dream.app.com.dreammusic.util.TextUtil;

/**
 * Created by JcMan on 2015/7/3.
 */
public class FragmentNetKtvMusic extends Fragment implements AdapterView.OnItemClickListener{
    private ListView mKtvMusicListView;
    private List<NetMusicEntry> mList;
    private LoadingDialog loadingDialog;
    private PopupWindow mPopupWindow;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_ktv_music,container,false);
        initView(view);
        initListener();
        loadingDialog = DialogUtil.createLoadingDialog(getActivity(), TextUtil.LOADINGNOW);
        return view;
    }

    private void initView(View view) {
        mKtvMusicListView = (ListView) view.findViewById(R.id.listview_ktv_music);
    }
    private void initListener() {
        mKtvMusicListView.setOnItemClickListener(this);
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
            httpUtil.send(HttpRequest.HttpMethod.GET, NetAPIEntry.getKtvMusicUrl(), new RequestCallBack<String>(){

                @Override
                public void onSuccess(ResponseInfo<String> stringResponseInfo) {
                    loadingDialog.cancel();
                    mList = new ArrayList<NetMusicEntry>();
                    NetMusicEntry.setNetMusicEntryList(stringResponseInfo,mList);
                    String[] types = {NetMusicEntry.PIC_SMALL,NetMusicEntry.TITLE,NetMusicEntry.AUTHOR};
                    mKtvMusicListView.setAdapter(new NetMusicAdapter(getActivity(),mList,types));
                }
                @Override
                public void onFailure(HttpException e, String s) {
                    loadingDialog.cancel();
                }
            });
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final NetMusicEntry entry = (NetMusicEntry) mKtvMusicListView.getItemAtPosition(position);
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
