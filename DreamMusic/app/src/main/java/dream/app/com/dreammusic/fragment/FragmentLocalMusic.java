package dream.app.com.dreammusic.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.model.Music;
import dream.app.com.dreammusic.ui.view.ScrollRelativeLayout;
import dream.app.com.dreammusic.ui.view.ViewIndicator;
import dream.app.com.dreammusic.util.MusicUtil;

/**
 * Created by Administrator on 2015/7/9.
 */
public class FragmentLocalMusic extends Fragment implements View.OnClickListener{
    private ViewPager mPager;
    private List<View> mViewList;
    private ViewIndicator mIndicator;
    private ScrollRelativeLayout mMainContainer;
    private TextView mLocalMusicListView,mLoveBetterView;
    private List<Music> mLocalList,mLoveList;
    ListView _List_local;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_local_music,container,false);
        initVariable();
        initView(view);
        initListener();
        selectTab(0);
        return view;
    }

    private void initListener() {
        mLocalMusicListView.setOnClickListener(this);
        mLoveBetterView.setOnClickListener(this);
        mPager.setOnPageChangeListener(mPagerChangeListener);
    }

    private void selectTab(int i) {
        switch (i){
            case 0:
                mLocalMusicListView.setTextColor(getResources().getColor(R.color.color_top_bg));
                mLoveBetterView.setTextColor(getResources().getColor(R.color.color_gray));
                break;
            case 1:
                mLocalMusicListView.setTextColor(getResources().getColor(R.color.color_gray));
                mLoveBetterView.setTextColor(getResources().getColor(R.color.color_top_bg));
                break;
        }
    }

    private void initVariable() {
        mViewList = new ArrayList<View>();
        mLocalList = new ArrayList<Music>();
        mLoveList = new ArrayList<Music>();
    }

    private void initView(View view) {
        mMainContainer = (ScrollRelativeLayout) view.findViewById(R.id.layout_localmusic_container);
        mIndicator = (ViewIndicator) view.findViewById(R.id.indicator_localmusic);
        mPager = (ViewPager) view.findViewById(R.id.viewpager_localmusic);
        mLocalMusicListView = (TextView) view.findViewById(R.id.tv_localmusic_indicator_label);
        mLoveBetterView = (TextView) view.findViewById(R.id.tv_love_indicator_label);
        View view_local = View.inflate(getActivity(),R.layout.view_localmusic_list,null);
        View view_love = View.inflate(getActivity(),R.layout.view_lovebetter,null);
        mViewList.add(view_local);
        initLocalView(view_local);
        mViewList.add(view_love);
        mPager.setAdapter(new LocalMusicPagerAdapter());

    }

    private void initLocalView(View view_local) {
        _List_local = (ListView) view_local.findViewById(R.id.listview_localmusic);
        mLocalList = MusicUtil.queryLocalMusic(getActivity());
        _List_local.setAdapter(new LocalAdapter(mLocalList,LocalAdapter.TYPE_LOCAL));
    }

    private ViewPager.OnPageChangeListener mPagerChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            mIndicator.scroll(position,positionOffset);
        }
        @Override
        public void onPageSelected(int position) {
            selectTab(position);
            mMainContainer.showIndicator();
        }
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_localmusic_indicator_label:
                selectTab(0);
                mPager.setCurrentItem(0);
                break;
            case R.id.tv_love_indicator_label:
                selectTab(1);
                mPager.setCurrentItem(1);
                break;
        }
    }

    class LocalMusicPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ((ViewPager)container).addView(mViewList.get(position));
            return mViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager)container).removeView(mViewList.get(position));
        }
    }

    class LocalAdapter extends BaseAdapter{
        public static final int TYPE_LOCAL = 0;
        public static final int TYPE_LOVE = 1;
        private List<Music> _List;
        private int type;

        public LocalAdapter(List<Music> list,int type){
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
            Music music = (Music) _List_local.getItemAtPosition(position);
            if (music.musicName.contains("-")){
                String _S[] = MusicUtil.getMusicName(music.musicName);
                if(music.artist.contains("un"))
                    music.artist="";
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
