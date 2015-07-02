package dream.app.com.dreammusic.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dream.app.com.dreammusic.R;

/**
 * Created by Administrator on 2015/7/2.
 */
public class FragmentMain extends Fragment implements View.OnClickListener{
    private View mLocalMusic,mMyLove,mMyMusicList,
            mDowdloadManager,mLocalHistory,mMusicStore,
            mCasuallyListen;
    private FragmentClickListener mClickListener;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main,container,false);
        initView(view);
        initListener();
        return view;
    }

    /**
     * 设置监听器
     */
    private void initListener() {
        mLocalMusic.setOnClickListener(this);
        mMyLove.setOnClickListener(this);
        mMyMusicList.setOnClickListener(this);
        mDowdloadManager.setOnClickListener(this);
        mLocalHistory.setOnClickListener(this);
        mMusicStore.setOnClickListener(this);
        mCasuallyListen.setOnClickListener(this);
    }

    /**
     * 初始化控件
     */
    private void initView(View v) {
        mLocalMusic = v.findViewById(R.id.view_local_music);
        mMyLove = v.findViewById(R.id.view_my_love);
        mMyMusicList = v.findViewById(R.id.view_my_music_list);
        mDowdloadManager  = v.findViewById(R.id.view_download_manager);
        mLocalHistory = v.findViewById(R.id.view_localentry_history);
        mMusicStore = v.findViewById(R.id.view_music_store);
        mCasuallyListen = v.findViewById(R.id.view_casually_listen);
    }

    @Override
    public void onClick(View v) {
        mClickListener.click(v.getId());
    }

    public interface FragmentClickListener{
        public  abstract void click(int id);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mClickListener = (FragmentClickListener) activity;
    }
}
