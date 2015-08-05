package dream.app.com.dreammusic.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.app.tool.logger.Logger;

import net.tsz.afinal.FinalBitmap;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.adapter.MyViewPagerAdapter;
import dream.app.com.dreammusic.bmob.BMVInfo;
import dream.app.com.dreammusic.config.ApplicationConfig;
import dream.app.com.dreammusic.ui.view.JListView;
import dream.app.com.dreammusic.ui.view.MyViewPagerIndicator;
import dream.app.com.dreammusic.util.DialogUtil;
import dream.app.com.dreammusic.util.DownLoadUtil;
import dream.app.com.dreammusic.util.PopupWindowUtil;

/**
 * Created by Administrator on 2015/8/5.
 */
public class MVActivity extends BaseActivity implements JListView.PullToRefreshListener,
        AdapterView.OnItemClickListener ,AdapterView.OnItemLongClickListener{

    private MyViewPagerIndicator mIndicator;
    private ViewPager mViewPager;
    private JListView mJListView;
    private ListView mNetListView;
    private ListView mLocalListView;
    private List<View> mViewsList;
    private List<BMVInfo> mNetMVList;
    private FinalBitmap finalBitmap;
    private Bitmap loadingBitmap;
    private MVAdapter mNetAdapter;
    private PopupWindow mPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mv);
        initVariable();
        initView();
        initListener();
        setTitle("MV");
    }

    @Override
    public void initView() {
        super.initView();
        mIndicator = (MyViewPagerIndicator) findViewById(R.id.indicator_mv);
        mViewPager = (ViewPager) findViewById(R.id.viewpager_mv);
        initViewsList();
        mViewPager.setAdapter(new MyViewPagerAdapter(this, mViewsList));
        initIndicator();
    }

    private void initIndicator(){
        mIndicator.setVisibleTabCount(2);
        List<String> titles = new ArrayList<String>();
        titles.add("网络");
        titles.add("本地");
        mIndicator.setTitles(titles);
        mIndicator.setViewPager(mViewPager, 0);
    }

    private void initViewsList(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View v_net = inflater.inflate(R.layout.view_mv_net, null);
        View v_local = inflater.inflate(R.layout.view_mv_local,null);
        initNetView(v_net);
        initLocalView(v_local);
        mViewsList.add(v_net);
        mViewsList.add(v_local);
    }

    private void initLocalView(View v_local){
        mLocalListView = (ListView) v_local.findViewById(R.id.listview_mv_local);
        mLocalListView.setOnItemClickListener(this);
        File file = new File(ApplicationConfig.MVDIE);
        File[] mvList = file.listFiles();
        mLocalListView.setAdapter(new MVAdapter(mvList));


    }

    private void initNetView(View v){
        mNetMVList = new ArrayList<BMVInfo>();
        mJListView = (JListView) v.findViewById(R.id.jlistview_mv_net);
        mNetListView = (ListView) v.findViewById(R.id.listview_mv_net);
        mNetListView.setOnItemClickListener(this);
        mNetListView.setOnItemLongClickListener(this);
        mJListView.setOnRefreshListener(this);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                mJListView.readyToRefresh();
            }
        }, 100);

    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    private void initVariable(){
        mViewsList = new ArrayList<View>();
        finalBitmap = FinalBitmap.create(this);
        loadingBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_mv_loading);
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        BmobQuery<BMVInfo> query = new BmobQuery<BMVInfo>();
        query.findObjects(this, new FindListener<BMVInfo>(){
            @Override
            public void onSuccess(List<BMVInfo> list){
                mNetMVList.clear();
                if (list != null && list.size() > 0){
                    BMVInfo.reverseList(list);
                    for (int i = 0; i <list.size() ; i++){
                        BMVInfo bmvInfo = list.get(i);
                        mNetMVList.add(bmvInfo);
                    }
                    mNetAdapter = new MVAdapter(mNetMVList);
                    mNetListView.setAdapter(mNetAdapter);
                }
                mJListView.finishRefreshing();
            }
            @Override
            public void onError(int i, String s) {
                DialogUtil.showMessageDialog(MVActivity.this, "访问出错");
                mJListView.finishRefreshing();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId()==R.id.listview_mv_net){
            PlayMV(Uri.parse(mNetMVList.get(position).getDown_url()));
        }else if(parent.getId()==R.id.listview_mv_local){
            File f = (File) mLocalListView.getItemAtPosition(position);
            PlayMV(Uri.fromFile(f));
        }
    }

    private void PlayMV(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri,"video/mp4");
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        mPopupWindow = PopupWindowUtil.createPopupWindow(this, R.layout.layout_popupwindow_download);
        mPopupWindow.showAtLocation(this.getWindow().getDecorView(),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        initPopBtnViewAndListener(mNetMVList.get(position));
        return false;
    }

    private void initPopBtnViewAndListener(final BMVInfo info) {
        Button btn_download = (Button) mPopupWindow.getContentView().findViewById(R.id.btn_popupwindow_download);
        Button btn_cancel = (Button) mPopupWindow.getContentView().findViewById(R.id.btn_popupwindow_cancel);
        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        DownLoadUtil downloadUtil = new DownLoadUtil(MVActivity.this);
                        downloadUtil.downloadMV(info.getDown_url(),info.getSinger()+" - "+info.getTitle()+".mp4");
                    }
                }.start();
                mPopupWindow.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });
    }


    class MVAdapter extends BaseAdapter{

        private List<BMVInfo> mList;
        private File[] mFileList;
        public MVAdapter(List<BMVInfo> list){
            mList = list;
        }

        public MVAdapter(File[] fileList){
            mFileList = fileList;
        }

        @Override
        public int getCount(){
            if(mList!=null)
                return mList.size();
            else
                return mFileList.length;
        }

        @Override
        public Object getItem(int position){
            if(mList!=null)
                return mList.get(position);
            else
                return mFileList[position];
        }

        @Override
        public long getItemId(int position){
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            Holder holder;
            if(convertView==null){
                holder = new Holder();
                convertView = View.inflate(MVActivity.this,R.layout.item_list_mv,null);
                holder.iv_img = (ImageView) convertView.findViewById(R.id.iv_mv_img);
                holder.tv_singer = (TextView) convertView.findViewById(R.id.tv_mv_singer);
                holder.tv_title = (TextView) convertView.findViewById(R.id.tv_mv_title);
                convertView.setTag(holder);
            }else
                holder = (Holder) convertView.getTag();
            if(mList!=null){
                BMVInfo info = mList.get(position);
                finalBitmap.display(holder.iv_img, info.getImage_url(), loadingBitmap, loadingBitmap);
                holder.tv_title.setText(info.getTitle());
                holder.tv_singer.setText(info.getSinger());
            }else if(mFileList!=null){
                File file = mFileList[position];
                String[] _S = file.getName().substring(0, file.getName().length() - 4).split("-");
                holder.tv_singer.setText(_S[0].trim());
                holder.tv_title.setText(_S[1].trim());
                Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(file.getAbsolutePath(), MediaStore.Images.Thumbnails.MICRO_KIND);
                holder.iv_img.setBackground(new BitmapDrawable(bitmap));
            }
            return convertView;
        }

        class Holder{
            ImageView iv_img;
            TextView tv_title;
            TextView tv_singer;
        }
    }
}


