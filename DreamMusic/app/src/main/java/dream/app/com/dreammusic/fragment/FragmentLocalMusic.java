package dream.app.com.dreammusic.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.model.Music;
import dream.app.com.dreammusic.myinterface.FragmentPlayMusicListener;
import dream.app.com.dreammusic.ui.view.ScrollRelativeLayout;
import dream.app.com.dreammusic.ui.view.ViewIndicator;
import dream.app.com.dreammusic.util.MusicUtil;
import dream.app.com.dreammusic.util.StringUtil;
import dream.app.com.dreammusic.util.ToastUtil;

/**
 * Created by JcMan on 2015/7/9.
 */
public class FragmentLocalMusic extends Fragment implements View.OnClickListener,
        AdapterView.OnItemLongClickListener,AdapterView.OnItemClickListener{
    private ViewPager mPager;
    private List<View> mViewList;
    private ViewIndicator mIndicator;
    private ScrollRelativeLayout mMainContainer;
    private TextView mLocalMusicListView,mLoveBetterView;
    private List<Music> mLocalList,mLoveList;
    private ListView _List_local;
    private LocalAdapter adapter;

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
        _List_local.setOnItemClickListener(this);
    }

    /**
     * 设置当前ViewPager的当前页
     * @param i
     */
    private void selectTab(int i){
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
        _List_local.setOnItemLongClickListener(this);
        mLocalList = MusicUtil.queryLocalMusic(getActivity());
        adapter = new LocalAdapter(mLocalList,LocalAdapter.TYPE_LOCAL);
        _List_local.setAdapter(adapter);
    }

    private ViewPager.OnPageChangeListener mPagerChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            mIndicator.scroll(position,positionOffset);
        }

        @Override
        public void onPageSelected(int position){
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

    /**
     * 长按ListView的响应
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        Dialog dialog = new Dialog(getActivity(),R.style.Theme_loading_dialog);
        View _View = View.inflate(getActivity(),R.layout.dialog_music_operation,null);
        setDialogListener(dialog,position, _View);
        dialog.setContentView(_View);
        setDialogAttr(dialog);
        dialog.show();
        return false;
    }

    /**
     * 设置Dialog的监听器
     * @param position
     * @param _View
     */
    private void setDialogListener(Dialog dialog,int position, View _View) {
        TextView tv_detail = (TextView) _View.findViewById(R.id.tv_dialogitem_detail);
        TextView tv_rename = (TextView) _View.findViewById(R.id.tv_dialogitem_rename);
        TextView tv_delete = (TextView) _View.findViewById(R.id.tv_dialogitem_delete);
        View.OnClickListener listener = getDialogItemClickListener(dialog,position);
        tv_detail.setOnClickListener(listener);
        tv_rename.setOnClickListener(listener);
        tv_delete.setOnClickListener(listener);
    }

    /**
     * 得到Dialog的监听器
     * @return
     */
    View.OnClickListener getDialogItemClickListener(final Dialog dialog, final int position){
        View.OnClickListener listener = new View.OnClickListener() {
            Music music = (Music) _List_local.getItemAtPosition(position);
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                switch (v.getId()){
                    case R.id.tv_dialogitem_detail:
                        showMusicDetailDialog(music);
                        break;
                    case R.id.tv_dialogitem_rename:
                        showRenameDialog(music);
                        break;
                    case R.id.tv_dialogitem_delete:
                        deleteMusicFile(position);
                        break;
                }
            }
        };
        return listener;
    }

    /**
     * 删除文件
     */
    private void deleteMusicFile(int position) {
        Music music = (Music) _List_local.getItemAtPosition(position);
        File file = new File(music.data);
        if(file.exists()){
            boolean result = file.delete();
            mLocalList.remove(position);
            adapter.notifyDataSetChanged();
            if (result){
                ToastUtil.showMessage(getActivity(), "删除成功");
                mPlayMusicListener.onUpdateMusicList(mLocalList);
            }
            else
                ToastUtil.showMessage(getActivity(),"删除失败");
        }
    }
    /**
     * 弹出重命名对话框
     * @param music
     */
    private void showRenameDialog(final Music music) {
        final Dialog rename_dialog = new Dialog(getActivity(), R.style.Theme_loading_dialog);
        View _View = View.inflate(getActivity(),R.layout.dialog_music_rename,null);
        final EditText editText = (EditText) _View.findViewById(R.id.edit_dialogmusic_rename);
        editText.setText(music.musicName);
        Button btn_cancel = (Button) _View.findViewById(R.id.btn_dialog_music_rename_cancel);
        Button btn_ok = (Button) _View.findViewById(R.id.btn_dialog_music_rename_ok);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rename_dialog.dismiss();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rename_dialog.dismiss();
                File oldFile = new File(music.data);
                String newname = music.data.substring(0,music.data.lastIndexOf("/")+1)+editText.getText().toString()+".mp3";
                File newfile = new File(newname);
                boolean result = oldFile.renameTo(newfile);
                if (result)
                    ToastUtil.showMessage(getActivity(), "重命名成功");
                else
                    ToastUtil.showMessage(getActivity(),"重命名失败");
            }
        });
        rename_dialog.setContentView(_View);
        setDialogAttr(rename_dialog);
        rename_dialog.show();
    }

    /**
     * 弹出详情对话框
     * @param music
     */
    private void showMusicDetailDialog(Music music) {
        Dialog detaildialog = new Dialog(getActivity(), R.style.Theme_loading_dialog);
        View _View = View.inflate(getActivity(),R.layout.dialog_music_detail,null);
        TextView text = (TextView) _View.findViewById(R.id.tv_content_detail);
        TextView sizeView = (TextView) _View.findViewById(R.id.tv_content_filesize);
        text.setText(music.data);
        String filesize = StringUtil.getFileSizeString(music.data);
        sizeView.setText(filesize + "M");
        detaildialog.setContentView(_View);
        setDialogAttr(detaildialog);
        detaildialog.show();
    }

    /**
     * 设置对话框的属性
     * @param dialog
     */
    private void setDialogAttr(Dialog dialog) {
        Window dialogWindow = dialog.getWindow();
        WindowManager manager = getActivity().getWindowManager();
        Display d = manager.getDefaultDisplay();
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        params.width = (int)(d.getWidth()*0.8);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(params);
    }

    private FragmentPlayMusicListener mPlayMusicListener;
    public void setPlayMusicListener(FragmentPlayMusicListener listener){
        mPlayMusicListener = listener;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mPlayMusicListener.onUpdateMusicList(mLocalList);
        mPlayMusicListener.onPlay(position);
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setPlayMusicListener((FragmentPlayMusicListener)getActivity());
    }
}
