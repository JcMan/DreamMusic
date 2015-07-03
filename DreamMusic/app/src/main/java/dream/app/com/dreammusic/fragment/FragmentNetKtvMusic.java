package dream.app.com.dreammusic.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import dream.app.com.dreammusic.entry.NetAPIEntry;
import dream.app.com.dreammusic.entry.NetMusicEntry;

/**
 * Created by JcMan on 2015/7/3.
 */
public class FragmentNetKtvMusic extends Fragment {
    private ListView mKtvMusicListView;
    private List<NetMusicEntry> mList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_ktv_music,container,false);
        initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        HttpUtils httpUtil = new HttpUtils();
        httpUtil.send(HttpRequest.HttpMethod.GET, NetAPIEntry.getKtvMusicUrl(), new RequestCallBack<String>(){

            @Override
            public void onSuccess(ResponseInfo<String> stringResponseInfo) {
                mList = new ArrayList<NetMusicEntry>();
                NetMusicEntry.setNetMusicEntryList(stringResponseInfo,mList);
                String[] types = {NetMusicEntry.PIC_SMALL,NetMusicEntry.TITLE,NetMusicEntry.AUTHOR};
                mKtvMusicListView.setAdapter(new NetMusicAdapter(getActivity(),mList,types));
            }
            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }



    private void initView(View view) {
        mKtvMusicListView = (ListView) view.findViewById(R.id.listview_ktv_music);
    }
}
