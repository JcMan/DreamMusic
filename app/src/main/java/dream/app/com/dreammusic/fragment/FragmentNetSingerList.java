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
import dream.app.com.dreammusic.adapter.RadioAndSingerAdapter;
import dream.app.com.dreammusic.entry.NetAPIEntry;
import dream.app.com.dreammusic.entry.NetMusicEntry;
import dream.app.com.dreammusic.ui.activity.SingerActivity;
import dream.app.com.dreammusic.ui.view.LoadingDialog;
import dream.app.com.dreammusic.util.ActivityUtil;
import dream.app.com.dreammusic.util.AnimUtil;
import dream.app.com.dreammusic.util.DialogUtil;
import dream.app.com.dreammusic.util.TextUtil;

/**
 * Created by JcMan on 2015/7/3.
 */
public class FragmentNetSingerList extends Fragment implements AdapterView.OnItemClickListener{

    private ListView mSingerListView;
    private List<NetMusicEntry> mList;
    private LoadingDialog loadingDialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_singer_list,container,false);
        initView(view);
        initListener();
        loadingDialog = DialogUtil.createLoadingDialog(getActivity(), TextUtil.LOADINGNOW);
        return view;
    }

    private void initListener() {
        mSingerListView.setOnItemClickListener(this);
    }

    private void initView(View view) {
        mSingerListView = (ListView) view.findViewById(R.id.listview_singer_list);
    }

    @Override
    public void onResume(){
        super.onResume();
        updateListView();
    }

    private void updateListView() {
        if(mList==null||mList.size()<1){
            loadingDialog.show();
            HttpUtils httpUtil = new HttpUtils();
            httpUtil.send(HttpRequest.HttpMethod.GET, NetAPIEntry.getSingerListUrl(), new RequestCallBack<String>() {

                @Override
                public void onSuccess(ResponseInfo<String> stringResponseInfo) {
                    loadingDialog.cancel();
                    mList = new ArrayList<NetMusicEntry>();
                    NetMusicEntry.setNetSingerList(stringResponseInfo, mList);
                    String[] types = {NetMusicEntry.AVATAR_MIDDLE,NetMusicEntry.NAME};
                    mSingerListView.setAdapter(new RadioAndSingerAdapter(getActivity(), mList, types));
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
        Intent intent = new Intent(getActivity(), SingerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("entry",(NetMusicEntry)mSingerListView.getItemAtPosition(position));
        intent.putExtra("singer",bundle);
        startActivity(intent);
        getActivity().overridePendingTransition(AnimUtil.BASE_SLIDE_RIGHT_IN,AnimUtil.BASE_SLIDE_REMAIN);

    }
}
