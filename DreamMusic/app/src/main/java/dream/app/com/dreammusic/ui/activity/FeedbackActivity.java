package dream.app.com.dreammusic.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.app.tool.logger.Logger;

import net.tsz.afinal.FinalBitmap;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.adapter.MyViewPagerAdapter;
import dream.app.com.dreammusic.bmob.BFeedbackInfo;
import dream.app.com.dreammusic.entry.UserEntry;
import dream.app.com.dreammusic.ui.view.LoadingDialog;
import dream.app.com.dreammusic.ui.view.ViewPagerIndicator;
import dream.app.com.dreammusic.util.DialogUtil;

/**
 * Created by Administrator on 2015/7/23.
 */
public class FeedbackActivity extends BaseActivity{

    private ViewPagerIndicator mIndicator;
    private ViewPager mViewPager;
    private List<View> mViewsList;
    private ListView mAllListView,mMyListView;
    private List<BFeedbackInfo> mAllFeedbackList,mMyFeedbackInfoList;
    private FinalBitmap finalBitmap;
    private Bitmap loadingBitmap;
    private LoadingDialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        initVariable();
        initView();
        initListener();
    }
    @Override
    public void initView() {
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
        mAllListView = (ListView) v_all.findViewById(R.id.listview_feedback_all);
        initAllList();

        mViewsList.add(v_all);
        mViewsList.add(v_my);
        mViewPager.setAdapter(new MyViewPagerAdapter(this,mViewsList));
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
                    mAllListView.setAdapter(new FeedbackListAdapter(mAllFeedbackList));
                    Logger.e(mAllFeedbackList.size()+"");
                }
            }
            @Override
            public void onError(int i, String s) {
                loadingDialog.cancel();
                DialogUtil.showMessageDialog(FeedbackActivity.this,"Error");
            }
        });
    }

    private void initVariable() {
        finalBitmap = FinalBitmap.create(this);
        loadingBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_loading_singer);
        loadingDialog = DialogUtil.createLoadingDialog(this);
        mViewsList = new ArrayList<View>();
        mAllFeedbackList = new ArrayList<BFeedbackInfo>();
        mMyFeedbackInfoList = new ArrayList<BFeedbackInfo>();
    }
    @Override
    protected void clickOnTopRight() {
        super.clickOnTopRight();
        if(UserEntry.getIsLogin()){
            startNewActivityWithAnim(WriteFedbackActivity.class);
        }else
            DialogUtil.showMessageDialog(this,"您还没有登录，请登录后反馈意见");
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
        public View getView(int position, View convertView, ViewGroup parent) {
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
            holder.tv_time.setText(bFeedbackInfo.getTime()+"");
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
