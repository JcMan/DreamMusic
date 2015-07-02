package dream.app.com.dreammusic.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dream.app.com.dreammusic.NetMusicAdapter;
import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.entry.NetMusic;
import dream.app.com.dreammusic.ui.view.JazzyViewPager;
import dream.app.com.dreammusic.ui.view.ViewPagerIndicator;

/**
 * Created by Administrator on 2015/7/2.
 */
public class FragmentMusicStore extends Fragment {
    private ViewPagerIndicator mIndicator;
    private JazzyViewPager mJazzyViewPager;
    private List<String> mList ;
    private List<View> mPagerViewList;
    private View view_new,view_hot,view_ktv,view_singer,view_radio;
    private ListView listView_new;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_music_store,container,false);
        initVariable();
        initView(view);
        bindData();
        return view;
    }

    private void initVariable(){
        mList = new ArrayList<String>();
        mList.add("新歌榜");
        mList.add("热歌榜");
        mList.add("KTV热歌榜");
        mList.add("歌手列表");
        mList.add("电台列表");
        mPagerViewList = new ArrayList<View>();
    }

    private void bindData() {
        List<NetMusic> list = new ArrayList<NetMusic>();
        for (int i=0;i<30;i++){
            list.add(new NetMusic());
        }
        listView_new.setAdapter(new NetMusicAdapter(getActivity(),list));
    }

    private void initView(View view) {
        initPagerView();
        initListView();
        mIndicator = (ViewPagerIndicator) view.findViewById(R.id.indicator_music_store);
        mJazzyViewPager = (JazzyViewPager) view.findViewById(R.id.viewpager_music_store);
        mJazzyViewPager.setTransitionEffect(JazzyViewPager.TransitionEffect.RotateUp);
        mIndicator.setTabVisibleCount(4);
        mIndicator.setTabItemTitles(mList);
        mJazzyViewPager.setAdapter(new MyPagerAdapter());
        mIndicator.setViewPager(mJazzyViewPager,0);
    }

    private void initListView() {
        listView_new = (ListView) view_new.findViewById(R.id.listview_new_music);
    }

    private void initPagerView() {
        view_new = getLayoutInflater().inflate(R.layout.view_new_music,null);
        view_hot = getLayoutInflater().inflate(R.layout.view_hot_music,null);
        view_ktv = getLayoutInflater().inflate(R.layout.view_ktv_music,null);
        view_singer = getLayoutInflater().inflate(R.layout.view_singer_list,null);
        view_radio = getLayoutInflater().inflate(R.layout.view_radio_list,null);
        mPagerViewList.add(view_new);
        mPagerViewList.add(view_hot);
        mPagerViewList.add(view_ktv);
        mPagerViewList.add(view_singer);
        mPagerViewList.add(view_radio);
    }

    private LayoutInflater getLayoutInflater(){
        return LayoutInflater.from(getActivity());
    }

    private class MyPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mPagerViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            ((ViewPager)container).addView(mPagerViewList.get(position));
            return mPagerViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager)container).removeView(mPagerViewList.get(position));
        }
    }

}
