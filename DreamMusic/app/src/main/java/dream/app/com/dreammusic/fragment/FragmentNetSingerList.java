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
import dream.app.com.dreammusic.adapter.RadioAndSingerAdapter;
import dream.app.com.dreammusic.entry.NetAPIEntry;
import dream.app.com.dreammusic.entry.NetMusicEntry;

/**
 * Created by JcMan on 2015/7/3.
 */
public class FragmentNetSingerList extends Fragment {

    private ListView mSingerListView;
    private List<NetMusicEntry> mList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_singer_list,container,false);
        initView(view);

        return view;
    }

    private void initView(View view) {
        mSingerListView = (ListView) view.findViewById(R.id.listview_singer_list);
    }


    @Override
    public void onResume(){
        super.onResume();
        HttpUtils httpUtil = new HttpUtils();
        httpUtil.send(HttpRequest.HttpMethod.GET, NetAPIEntry.getSingerListUrl(), new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> stringResponseInfo) {
                mList = new ArrayList<NetMusicEntry>();
                NetMusicEntry.setNetSingerList(stringResponseInfo, mList);
                String[] types = {NetMusicEntry.AVATAR_MIDDLE,NetMusicEntry.NAME};
                mSingerListView.setAdapter(new RadioAndSingerAdapter(getActivity(), mList, types));
            }
            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }
}
