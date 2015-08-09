package dream.app.com.dreammusic.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.tool.logger.Logger;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.adapter.MyViewPagerAdapter;
import dream.app.com.dreammusic.service.MusicService;
import dream.app.com.dreammusic.ui.view.FlowLayout;
import dream.app.com.dreammusic.ui.view.MyViewPagerIndicator;
import dream.app.com.dreammusic.util.MyHttpUtil;

/**
 * Created by Administrator on 2015/8/8.
 */
public class NovelActivity extends BaseActivity{

    private MyViewPagerIndicator mIndicator;
    private ViewPager mViewPager;
    private List<View> mViewsList;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novel);
        MusicService.addActivity(this);
        initView();
        initListener();
        setTitle("小说中心");
    }

    @Override
    public void initView(){
        super.initView();
        mIndicator = (MyViewPagerIndicator) findViewById(R.id.indicator_novel);
        mViewPager = (ViewPager) findViewById(R.id.viewpager_novel);
        initPagerViews();
        initIndicator();
    }

    private void initIndicator(){
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
        View v_classification = View.inflate(this,R.layout.view_novel_classification,null);
        FlowLayout flowLayout = (FlowLayout) v_classification.findViewById(R.id.flowlayout_novel_classification);
        String[] novel_classes = getResources().getStringArray(R.array.novel_kind_name);
        for (int i = 0; i <novel_classes.length ; i++){
            TextView tv_label = new TextView(this);
            ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(4,4,4,4);
            tv_label.setTextColor(Color.parseColor("#ffffff"));
            tv_label.setLayoutParams(params);
            tv_label.setBackgroundResource(R.drawable.tv_novel_classes);
            tv_label.setText(novel_classes[i]);
            flowLayout.addView(tv_label);
        }
        TextView tv2 = new TextView(this);
        tv2.setText("搜索");
        mViewsList.add(tv);
        mViewsList.add(v_classification);
        mViewsList.add(tv2);
        mViewPager.setAdapter(new MyViewPagerAdapter(this,mViewsList));
    }
}
