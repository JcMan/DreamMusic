package dream.app.com.dreammusic.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.adapter.MyViewPagerAdapter;
import dream.app.com.dreammusic.service.MusicService;
import dream.app.com.dreammusic.ui.view.MyViewPagerIndicator;

/**
 * Created by Administrator on 2015/8/8.
 */
public class NovelActivity extends BaseActivity{

    private MyViewPagerIndicator mIndicator;
    private ViewPager mViewPager;
    private List<View> mViewsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novel);
        MusicService.addActivity(this);
        initView();
        initListener();
        setTitle("小说中心");
    }

    @Override
    public void initView() {
        super.initView();
        mIndicator = (MyViewPagerIndicator) findViewById(R.id.indicator_novel);
        mViewPager = (ViewPager) findViewById(R.id.viewpager_novel);
        initPagerViews();
        initIndicator();
    }

    private void initIndicator() {
        mIndicator.setVisibleTabCount(3);
        String titles[] = {"书架","分类","搜索"};
        List<String> _List = Arrays.asList(titles);
        mIndicator.setTitles(_List);
        mIndicator.setViewPager(mViewPager,0);
    }

    private void initPagerViews() {
        mViewsList = new ArrayList<View>();
        TextView tv = new TextView(this);
        tv.setText("书架");
        TextView tv1 = new TextView(this);
        tv1.setText("分类");
        TextView tv2 = new TextView(this);
        tv2.setText("搜索");
        mViewsList.add(tv);
        mViewsList.add(tv1);
        mViewsList.add(tv2);
        mViewPager.setAdapter(new MyViewPagerAdapter(this,mViewsList));
    }
}
