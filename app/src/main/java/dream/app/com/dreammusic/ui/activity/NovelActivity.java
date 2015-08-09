package dream.app.com.dreammusic.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.adapter.MyViewPagerAdapter;
import dream.app.com.dreammusic.adapter.NetNovelAdapter;
import dream.app.com.dreammusic.entry.NovelAPI;
import dream.app.com.dreammusic.entry.NovelEntry;
import dream.app.com.dreammusic.service.MusicService;
import dream.app.com.dreammusic.ui.view.FlowLayout;
import dream.app.com.dreammusic.ui.view.LoadingDialog;
import dream.app.com.dreammusic.ui.view.MyViewPagerIndicator;
import dream.app.com.dreammusic.util.DialogUtil;
import dream.app.com.dreammusic.util.ToastUtil;

/**
 * Created by Administrator on 2015/8/8.
 */
public class NovelActivity extends BaseActivity implements FlowLayout.IFlowClickListener,AdapterView.OnItemClickListener{

    private MyViewPagerIndicator mIndicator;
    private ViewPager mViewPager;
    private List<View> mViewsList;
    private ListView mClassesListView;
    private LoadingDialog loadingDialog;
    private List<NovelEntry> mNetNovelList;
    private NetNovelAdapter mNetNovelAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novel);
        MusicService.addActivity(this);
        loadingDialog = DialogUtil.createLoadingDialog(this);
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
        mIndicator.setViewPager(mViewPager, 0);
    }

    private void initPagerViews() {
        mViewsList = new ArrayList<View>();
        TextView tv = new TextView(this);
        tv.setText("书架");
        View v_classification = initClassificationView();
        TextView tv2 = new TextView(this);
        tv2.setText("搜索");
        mViewsList.add(tv);
        mViewsList.add(v_classification);
        mViewsList.add(tv2);
        mViewPager.setAdapter(new MyViewPagerAdapter(this, mViewsList));
    }

    /**
     * 初始化分类界面
     * @return
     */
    @NonNull
    private View initClassificationView() {
        View v_classification = View.inflate(this, R.layout.view_novel_classification, null);
        FlowLayout flowLayout = (FlowLayout) v_classification.findViewById(R.id.flowlayout_novel_classification);
        flowLayout.setFlowClickListener(this);
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
        mClassesListView = (ListView) v_classification.findViewById(R.id.listview_novel_classes);
        mClassesListView.setOnItemClickListener(this);
        loadingDialog.show();
        initClassesListView();

        return v_classification;
    }

    /**
     * 初始化分类界面的ListView并绑定数据
     */
    private void initClassesListView(){
        new Thread(new Runnable(){
            @Override
            public void run(){
                try {
                    Document doc = Jsoup.connect(NovelAPI.getClassificationUrl(NovelActivity.this, "玄幻·魔法")).get();
                    mNetNovelList = NovelAPI.getNovelList(doc);
                    if(mNetNovelList.size()>0){
                        setClassesListViewAdapter();
                    }
                } catch (IOException e){
                    e.printStackTrace();
                }
                cancelLoadingDialog();
            }
        }).start();
    }

    @Override
    public void flowClick(View v){
        TextView tv_label = (TextView)v;
        String label = tv_label.getText().toString();
        String classification_url = NovelAPI.getClassificationUrl(this,label);
        ToastUtil.showMessage(this, classification_url);
    }
    private void cancelLoadingDialog(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingDialog.cancel();
            }
        });
    }

    private void setClassesListViewAdapter(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mNetNovelAdapter = new NetNovelAdapter(NovelActivity.this,mNetNovelList);
                mClassesListView.setAdapter(mNetNovelAdapter);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
