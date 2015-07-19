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
import dream.app.com.dreammusic.config.ApplicationConfig;
import dream.app.com.dreammusic.model.Music;
import dream.app.com.dreammusic.myinterface.FragmentPlayMusicListener;
import dream.app.com.dreammusic.util.MusicUtil;

/**
 * Created by Administrator on 2015/7/19.
 */
public class FragmentDownload extends Fragment implements AdapterView.OnItemClickListener{

    private ListView mListView;
    private DownloadAdapter adapter;
    private List<Music> mList;
    private FragmentPlayMusicListener mListener;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View _View = inflater.inflate(R.layout.fragment_download,container,false);
        initView(_View);
        return _View;
    }

    private void initView(View view) {
        mListView = (ListView) view.findViewById(R.id.listview_download);
        mList = MusicUtil.getMusicListByFolder(getActivity(), ApplicationConfig.DOWNLOADDIE);
        adapter = new DownloadAdapter(mList,DownloadAdapter.TYPE_LOCAL);
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

    class DownloadAdapter extends BaseAdapter {
        public static final int TYPE_LOCAL = 0;
        public static final int TYPE_LOVE = 1;
        private List<Music> _List;
        private int type;

        public DownloadAdapter(List<Music> list,int type){
            _List = list;
            this.type = type;
        }

        @Override
        public int getCount() {
            return _List.size();
        }

        @Override
        public Object getItem(int position) {
            return _List.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LocalHolder holder;
            if(convertView==null){
                holder = new LocalHolder();
                convertView=View.inflate(getActivity(),R.layout.item_localmusic_list,null);
                holder.mImageView = (ImageButton) convertView.findViewById(R.id.iv_local);
                holder.mTitle = (TextView) convertView.findViewById(R.id.tv_item_local_title);
                convertView.setTag(holder);
            }else{
                holder = (LocalHolder) convertView.getTag();
            }
            Music music = (Music) mListView.getItemAtPosition(position);
            if (music.musicName.contains("-")){
                String _S[] = MusicUtil.getMusicName(music.musicName);
                if(music.artist.contains("un"))
                    holder.mTitle.setText(music.musicName);
                else
                    holder.mTitle.setText(_S[0]+" - "+_S[1]);
            }else{
                if(music.artist.contains("un"))
                    holder.mTitle.setText(music.musicName);
                else
                    holder.mTitle.setText(music.artist+" - "+music.musicName);
            }
            return convertView;
        }
        class LocalHolder{
            ImageButton mImageView;
            TextView mTitle;
            TextView mArtist;
        }
    }
}
