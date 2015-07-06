package dream.app.com.dreammusic.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.List;

import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.adapter.NetMusicAdapter;
import dream.app.com.dreammusic.adapter.RadioAndSingerAdapter;
import dream.app.com.dreammusic.entry.NetAPIEntry;
import dream.app.com.dreammusic.entry.NetMusicEntry;
import dream.app.com.dreammusic.ui.activity.RadioActivity;
import dream.app.com.dreammusic.ui.view.LoadingDialog;
import dream.app.com.dreammusic.util.AnimUtil;
import dream.app.com.dreammusic.util.DialogUtil;
import dream.app.com.dreammusic.util.TextUtil;

/**
 * Created by JcMan on 2015/7/3.
 */
public class FragmentNetRadioList extends Fragment implements AdapterView.OnItemClickListener{
    private ListView mRadioMusicListView;
    private List<NetMusicEntry> mList;
    private LoadingDialog loadingDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_radio_list,container,false);
        initView(view);
        initListener();
        loadingDialog = DialogUtil.createLoadingDialog(getActivity(), TextUtil.LOADINGNOW);
        return view;
    }

    private void initListener() {
        mRadioMusicListView.setOnItemClickListener(this);
    }

    private void initView(View view) {
        mRadioMusicListView = (ListView) view.findViewById(R.id.listview_radio_music);
    }

    @Override
    public void onResume(){
        super.onResume();
        if (mList==null||mList.size()<1){
            loadingDialog.show();
            HttpUtils httpUtil = new HttpUtils();
            httpUtil.send(HttpRequest.HttpMethod.GET, NetAPIEntry.getRadioListUrl(), new RequestCallBack<String>() {

                @Override
                public void onSuccess(ResponseInfo<String> stringResponseInfo) {
                    loadingDialog.cancel();
                    mList = new ArrayList<NetMusicEntry>();
                    NetMusicEntry.setNetChannelList(stringResponseInfo, mList);
                    String[] types = {NetMusicEntry.THUMB,NetMusicEntry.NAME};
                    mRadioMusicListView.setAdapter(new RadioAndSingerAdapter(getActivity(), mList, types));
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
        Intent intent = new Intent(getActivity(), RadioActivity.class);
        Bundle bundle = new Bundle();
        NetMusicEntry entry = (NetMusicEntry) mRadioMusicListView.getItemAtPosition(position);
        bundle.putSerializable("entry",entry);
        intent.putExtra("radio",bundle);
        startActivity(intent);
        getActivity().overridePendingTransition(AnimUtil.BASE_SLIDE_RIGHT_IN,AnimUtil.BASE_SLIDE_REMAIN);
    }
}
