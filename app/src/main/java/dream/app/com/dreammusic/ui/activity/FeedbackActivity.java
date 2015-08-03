package dream.app.com.dreammusic.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import net.tsz.afinal.FinalBitmap;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.adapter.MyViewPagerAdapter;
import dream.app.com.dreammusic.bmob.BFeedbackInfo;
import dream.app.com.dreammusic.entry.UserBean;
import dream.app.com.dreammusic.entry.UserEntry;
import dream.app.com.dreammusic.service.MusicService;
import dream.app.com.dreammusic.ui.view.LoadingDialog;
import dream.app.com.dreammusic.ui.view.ViewPagerIndicator;
import dream.app.com.dreammusic.ui.view.xlistview.XListView;
import dream.app.com.dreammusic.util.DialogUtil;
import dream.app.com.dreammusic.util.SharedPreferencesUtil;
import dream.app.com.dreammusic.util.StringUtil;
import dream.app.com.dreammusic.util.ToastUtil;

/**
 * Created by JcMan on 2015/7/23.
 */
public class FeedbackActivity extends BaseActivity implements XListView.IXListViewListener{

    private ViewPagerIndicator mIndicator;
    private ViewPager mViewPager;
    private List<View> mViewsList;
    private ListView mMyListView;
    private XListView mAllListView;
    private FeedbackListAdapter mAllAdapter;
    private List<BFeedbackInfo> mAllFeedbackList,mMyFeedbackInfoList;
    private FinalBitmap finalBitmap;
    private Bitmap loadingBitmap;
    private LoadingDialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        MusicService.addActivity(this);
        initVariable();
        initView();
        initListener();
        setTitle("意见反馈区");
    }
    @Override
    public void initView(){
        super.initView();
        mIndicator = (ViewPagerIndicator) findViewById(R.id.indicator_feedback);
        mViewPager = (ViewPager) findViewById(R.id.viewpager_feedbackactivity);
        initPagerViews();
        mIndicator.setTabVisibleCount(2);
        List<String> _Li = new ArrayList<String>();
        _Li.add("全部意见");
        _Li.add("我的意见");
        mIndicator.setTabItemTitles(_Li);
        mIndicator.setViewPager(mViewPager,0);
        setTopRightVisible();
        setTopRightImg(R.drawable.ic_feedback_edit);
    }
    private void initPagerViews() {
        View v_all = View.inflate(this,R.layout.view_feedback_all,null);
        View v_my = View.inflate(this,R.layout.view_feedback_my,null);
        mAllListView = (XListView) v_all.findViewById(R.id.listview_feedback_all);
        mAllListView.setPullLoadEnable(true);
        mMyListView = (ListView) v_my.findViewById(R.id.listview_feedback_my);
        initAllList();
        initMyList();
        mViewsList.add(v_all);
        mViewsList.add(v_my);
        mViewPager.setAdapter(new MyViewPagerAdapter(this,mViewsList));
    }

    private void initMyList() {
        loadingDialog.show();
        BmobQuery<BFeedbackInfo> query = new BmobQuery<BFeedbackInfo>();
        query.addWhereEqualTo(UserBean.LOGINID, UserEntry.getUid());
        query.findObjects(this, new FindListener<BFeedbackInfo>(){
            @Override
            public void onSuccess(List<BFeedbackInfo> bFeedbackInfos) {
                loadingDialog.cancel();
                if (bFeedbackInfos != null && bFeedbackInfos.size() > 0){
                    BFeedbackInfo.reverseList(bFeedbackInfos);
                    mMyFeedbackInfoList = bFeedbackInfos;
                    mMyListView.setAdapter(new FeedbackListAdapter(mMyFeedbackInfoList));
                }
            }
            @Override
            public void onError(int i, String s) {
                loadingDialog.cancel();
                DialogUtil.showMessageDialog(FeedbackActivity.this, "Error");
            }
        });
    }

    @Override
    protected void initListener() {
        super.initListener();
        mAllListView.setXListViewListener(this);
    }

    private void initAllList() {
        loadingDialog.show();
        BmobQuery<BFeedbackInfo> query = new BmobQuery<BFeedbackInfo>();
        query.findObjects(this,new FindListener<BFeedbackInfo>() {
            @Override
            public void onSuccess(List<BFeedbackInfo> bFeedbackInfos) {
                loadingDialog.cancel();
                if(bFeedbackInfos!=null&&bFeedbackInfos.size()>0){
                    BFeedbackInfo.reverseList(bFeedbackInfos);
                    mAllFeedbackList = bFeedbackInfos;
                    mAllAdapter = new FeedbackListAdapter(mAllFeedbackList);
                    mAllListView.setAdapter(mAllAdapter);
                }
            }
            @Override
            public void onError(int i, String s) {
                loadingDialog.cancel();
                DialogUtil.showMessageDialog(FeedbackActivity.this,"操作失败，请重试");
            }
        });
    }

    private void initVariable(){
        finalBitmap = FinalBitmap.create(this);
        loadingBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_loading_singer);
        loadingDialog = DialogUtil.createLoadingDialog(this);
        mViewsList = new ArrayList<View>();
        mAllFeedbackList = new ArrayList<BFeedbackInfo>();
        mMyFeedbackInfoList = new ArrayList<BFeedbackInfo>();
    }
    @Override
    protected void clickOnTopRight(){
        super.clickOnTopRight();
        startNewActivityWithAnim(WriteFedbackActivity.class);

    }

    private void onLoad(){
        mAllListView.stopRefresh();
        mAllListView.stopLoadMore();
        String lastRefreshTime = SharedPreferencesUtil.getFeedbackAllRefreshTime();
        mAllListView.setRefreshTime(" "+lastRefreshTime);
    }

    @Override
    public void onRefresh() {
        SharedPreferencesUtil.setFeedbackAllRefreshTime(getRefresgTime());
        BmobQuery<BFeedbackInfo> bmobQuery = new BmobQuery<BFeedbackInfo>();
        bmobQuery.findObjects(this,new FindListener<BFeedbackInfo>() {
            @Override
            public void onSuccess(List<BFeedbackInfo> bFeedbackInfos){
                int i;
                for(i=0;i<bFeedbackInfos.size();i++){
                    if(mAllFeedbackList.get(0).getTime()==bFeedbackInfos.get(i).getTime())
                        break;
                }
                for(i=i+1;i<bFeedbackInfos.size();i++){
                    mAllFeedbackList.add(0,bFeedbackInfos.get(i));
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAllAdapter.notifyDataSetChanged();
                        onLoad();
                        ToastUtil.showMessage(FeedbackActivity.this,"更新完毕");
                    }
                },1500);

            }
            @Override
            public void onError(int i, String s) {
            }
        });
    }

    private String getRefresgTime() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("M-d H:m.s");
        return format.format(date);
    }

    @Override
    public void onLoadMore() {

    }

    class FeedbackListAdapter extends BaseAdapter{
        private List<BFeedbackInfo> _List;
        public FeedbackListAdapter(List<BFeedbackInfo> list){
            _List = list;
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
        public View getView(int position, View convertView, ViewGroup parent){
            FeedHolder holder;
            if (convertView==null){
                holder = new FeedHolder();
                convertView = LayoutInflater.from(FeedbackActivity.this).inflate(R.layout.item_list_feedback,null);
                holder.iv_userhead = (ImageView) convertView.findViewById(R.id.iv_user_feedback);
                holder.tv_username = (TextView) convertView.findViewById(R.id.tv_feedback_username);
                holder.tv_content  = (TextView) convertView.findViewById(R.id.tv_feedback_content);
                holder.tv_time  = (TextView) convertView.findViewById(R.id.tv_feedback_time);
                convertView.setTag(holder);
            }else
                holder = (FeedHolder) convertView.getTag();
            BFeedbackInfo bFeedbackInfo = _List.get(position);
            finalBitmap.display(holder.iv_userhead, bFeedbackInfo.getHeadimageurl(), loadingBitmap, loadingBitmap);
            holder.tv_username.setText(bFeedbackInfo.getUsername());
            holder.tv_content.setText(bFeedbackInfo.getContent());
            long time = System.currentTimeMillis()/1000;
            time-=bFeedbackInfo.getTime();
            holder.tv_time.setText(StringUtil.getTimeDesc(time));
            return convertView;
        }
        class FeedHolder{
            ImageView iv_userhead;
            TextView tv_username;
            TextView tv_content;
            TextView tv_time;
        }
    }
}
