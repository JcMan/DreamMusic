package dream.app.com.dreammusic.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.adapter.MyViewPagerAdapter;
import dream.app.com.dreammusic.ui.view.ViewPagerIndicator;

/**
 * Created by Administrator on 2015/7/23.
 */
public class FeedbackActivity extends BaseActivity{

    private ViewPagerIndicator mIndicator;
    private ViewPager mViewPager;
    private List<View> mViewsList;
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
        TextView v_all = new TextView(this);
        TextView v_my = new TextView(this);
        v_all.setText("all");
        v_my.setText("my");
        mViewsList.add(v_all);
        mViewsList.add(v_my);
        mViewPager.setAdapter(new MyViewPagerAdapter(this,mViewsList));
    }
    private void initVariable() {
        mViewsList = new ArrayList<View>();
    }

    @Override
    protected void clickOnTopRight() {
        super.clickOnTopRight();

    }
}
