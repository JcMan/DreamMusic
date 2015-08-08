package dream.app.com.dreammusic.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.adapter.LocalMusicAdapter;
import dream.app.com.dreammusic.config.ApplicationConfig;
import dream.app.com.dreammusic.db.PlayHistoryDAO;
import dream.app.com.dreammusic.model.Music;
import dream.app.com.dreammusic.myinterface.FragmentPlayMusicListener;
import dream.app.com.dreammusic.util.MusicUtil;

/**
 * Created by Administrator on 2015/7/19.
 */
public class FragmentHistory extends Fragment implements AdapterView.OnItemClickListener{

    private ListView mListView;
    private LocalMusicAdapter adapter;
    private List<Music> mList;
    private FragmentPlayMusicListener mListener;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View _View = inflater.inflate(R.layout.fragment_history,container,false);
        initView(_View);
        return _View;
    }

    private void initView(View view){
        mListView = (ListView) view.findViewById(R.id.listview_history);
        PlayHistoryDAO playHistoryDAO = new PlayHistoryDAO(getActivity());
        mList = playHistoryDAO.getHistory();
        adapter = new LocalMusicAdapter(getActivity(),mList);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mListener.onUpdateMusicList(mList);
        mListener.onPlay(position);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (FragmentPlayMusicListener)getActivity();
    }
}
