package dream.app.com.dreammusic.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.app.tool.logger.Logger;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.adapter.NetMusicAdapter;
import dream.app.com.dreammusic.entry.NetAPIEntry;
import dream.app.com.dreammusic.entry.NetMusicEntry;
import dream.app.com.dreammusic.util.ToastUtil;

/**
 * Created by JcMan on 2015/7/3.
 */
public class FragmentNetNewMusic extends Fragment {
    private ListView mNewMusicListView;
    private List<NetMusicEntry> mList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_new_music,container,false);
        initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        HttpUtils httpUtil = new HttpUtils();
        httpUtil.send(HttpRequest.HttpMethod.GET,NetAPIEntry.getNewMusicUrl(), new RequestCallBack<String>(){

            @Override
            public void onSuccess(ResponseInfo<String> stringResponseInfo) {
                mList = new ArrayList<NetMusicEntry>();
                try {
                    JSONObject object = new JSONObject(stringResponseInfo.result);
                    JSONArray array = object.getJSONArray(NetMusicEntry.SONG_LIST);
                    for(int i=0;i<array.length();i++){
                        JSONObject obj = array.getJSONObject(i);
                        NetMusicEntry entry = new NetMusicEntry();
                        entry.setAuthor(obj.getString(NetMusicEntry.AUTHOR));
                        entry.setTitle(obj.getString(NetMusicEntry.TITLE));
                        entry.setPic_small(obj.getString(NetMusicEntry.PIC_SMALL));
                        entry.setPic_big(obj.getString(NetMusicEntry.PIC_BIG));
                        entry.setSong_id(obj.getString(NetMusicEntry.SONG_ID));
                        mList.add(entry);
                    }
                    String[] types = {NetMusicEntry.PIC_SMALL,NetMusicEntry.TITLE,NetMusicEntry.AUTHOR};
                    mNewMusicListView.setAdapter(new NetMusicAdapter(getActivity(),mList,types));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }

    private void initView(View view) {
        mNewMusicListView = (ListView) view.findViewById(R.id.listview_new_music);
    }
}
