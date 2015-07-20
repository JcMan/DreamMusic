package dream.app.com.dreammusic.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.model.Music;
import dream.app.com.dreammusic.myinterface.FragmentPlayMusicListener;
import dream.app.com.dreammusic.ui.view.PullToRefreshLayout;
import dream.app.com.dreammusic.util.MusicUtil;

/**
 * Created by Administrator on 2015/7/19.
 */
public class FragmentSuiBianTing extends Fragment implements AdapterView.OnItemClickListener,PullToRefreshLayout.OnRefreshListener{

    private ListView mListView;
    private List<Music> mList;
    List<Music> _List ;
    private FragmentPlayMusicListener mListener;
    private SuiBianTingAdapter adapter;
    private PullToRefreshLayout refreshLayout;
    private View loading;
    private RotateAnimation loadingAnimation;
    private TextView loadTextView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_suibianting,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mListView = (ListView) view.findViewById(R.id.listview_suibianting);
        mListView.setOnItemClickListener(this);
        _List = MusicUtil.queryLocalMusic(getActivity());
        initList();
        init(view);
        adapter = new SuiBianTingAdapter();
        mListView.setAdapter(adapter);
    }

    private void init(View v) {
        refreshLayout = (PullToRefreshLayout) v.findViewById(R.id.refreshview);
        refreshLayout.setOnRefreshListener(this);
        //initExpandableListView();
        loadingAnimation = (RotateAnimation) AnimationUtils.loadAnimation(getActivity(), R.anim.rotating);
        // 添加匀速转动动画
        LinearInterpolator lir = new LinearInterpolator();
        loadingAnimation.setInterpolator(lir);

    }

    private void initList() {
        List<Integer> _ListNum = new ArrayList<Integer>();
        mList = new ArrayList<Music>();
        int size = _List.size()<7?_List.size():7;
        while(mList.size()<=size){
            int num = new Random().nextInt(_List.size());
            int flag = 1;
            for(int i=0;i<_ListNum.size();i++){
                if(num==_ListNum.get(i))
                    flag = 0;
            }
            if(flag==1){
                _ListNum.add(num);
                mList.add(_List.get(num));
            }
        }
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

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        new Handler()
        {
            @Override
            public void handleMessage(Message msg){
                refreshLayout.refreshFinish(PullToRefreshLayout.REFRESH_SUCCEED);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initList();
                        adapter.notifyDataSetChanged();
                    }
                });

            }
        }.sendEmptyMessageDelayed(0, 2000);
    }

    class SuiBianTingAdapter extends BaseAdapter{


        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder ;
            if(convertView==null){
                holder = new Holder();
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_list_suibianting,null);
                holder.text = (TextView) convertView.findViewById(R.id.tv_item_suibianting_title);
                convertView.setTag(holder);
            }else
                holder = (Holder) convertView.getTag();
            Music music = mList.get(position);
            if(music.musicName.contains("-"))
                holder.text.setText(music.musicName);
            else
                holder.text.setText(music.artist+" - "+music.musicName);

            return convertView;
        }

        class Holder{
            TextView text;
        }
    }
}
