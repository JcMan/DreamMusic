package dream.app.com.dreammusic.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.ui.activity.SearchActivity;
import dream.app.com.dreammusic.util.AnimUtil;
import dream.app.com.dreammusic.util.MusicUtil;
import dream.app.com.dreammusic.util.ToastUtil;

/**
 * Created by JcMan on 2015/7/2.
 */
public class FragmentMain extends Fragment implements View.OnClickListener{
    private View mLocalMusic,mMyLove,mMyMusicList,
            mDowdloadManager,mLocalHistory,mMusicStore,
            mCasuallyListen;
    private View view_main;
    private FragmentClickListener mClickListener;

    private EditText mEdit;
    private Button mBtn;
    private TextView tv_localmusic_number;
    FragmentTransaction transaction ;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main,container,false);
        initView(view);
        initListener();
        transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.animator.enter,R.animator.exit,R.animator.pop_enter,R.animator.pop_exit);
        return view;
    }

    private void updateView() {
        updateLocalMusicNumber();
    }



    private void updateLocalMusicNumber(){
        tv_localmusic_number.setText("" + MusicUtil.getLocalMusicNumber(getActivity()) + "首");
    }
    @Override
    public void onResume() {
        super.onResume();
        updateView();
    }

    /**
     * 设置监听器
     */
    private void initListener(){
        mLocalMusic.setOnClickListener(this);
        mMyLove.setOnClickListener(this);
        mMyMusicList.setOnClickListener(this);
        mDowdloadManager.setOnClickListener(this);
        mLocalHistory.setOnClickListener(this);
        mMusicStore.setOnClickListener(this);
        mCasuallyListen.setOnClickListener(this);
        mBtn.setOnClickListener(this);

    }

    /**
     * 初始化控件
     */
    private void initView(View v){
        mLocalMusic = v.findViewById(R.id.view_local_music);
        mMyLove = v.findViewById(R.id.view_my_love);
        mMyMusicList = v.findViewById(R.id.view_my_music_list);
        mDowdloadManager  = v.findViewById(R.id.view_download_manager);
        mLocalHistory = v.findViewById(R.id.view_localentry_history);
        mMusicStore = v.findViewById(R.id.view_music_store);
        mCasuallyListen = v.findViewById(R.id.view_casually_listen);

        mEdit = (EditText) v.findViewById(R.id.et_search_fragmentmain);
        mBtn = (Button) v.findViewById(R.id.btn_search_fragment);

        view_main = v.findViewById(R.id.layout_fragment_main);
        tv_localmusic_number = (TextView) v.findViewById(R.id.tv_localmusic_number);


    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_search_fragment){
            String query = mEdit.getText().toString();
            if(query.length()<1)
                ToastUtil.showMessage(getActivity(),"输入内容不能为空");
            else{
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("query",mEdit.getText().toString());
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(AnimUtil.BASE_SLIDE_RIGHT_IN,AnimUtil.BASE_SLIDE_REMAIN);
            }
        }else if(v.getId()==R.id.view_local_music){
           transaction.replace(R.id.fragment_main,new FragmentLocalMusic())
            .addToBackStack(null).commit();
        }else
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
