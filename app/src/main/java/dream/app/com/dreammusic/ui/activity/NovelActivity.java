package dream.app.com.dreammusic.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;


import com.app.tool.logger.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.adapter.GridBookShelfAdapter;
import dream.app.com.dreammusic.adapter.MyViewPagerAdapter;
import dream.app.com.dreammusic.adapter.NetNovelAdapter;
import dream.app.com.dreammusic.adapter.NovelClassesAdapter;
import dream.app.com.dreammusic.db.NovelInfoDAO;
import dream.app.com.dreammusic.db.PlayHistoryDAO;
import dream.app.com.dreammusic.entry.NovelAPI;
import dream.app.com.dreammusic.entry.NovelEntry;
import dream.app.com.dreammusic.service.MusicService;
import dream.app.com.dreammusic.ui.view.FlowLayout;
import dream.app.com.dreammusic.ui.view.LoadingDialog;
import dream.app.com.dreammusic.ui.view.MyViewPagerIndicator;
import dream.app.com.dreammusic.util.DialogUtil;
import dream.app.com.dreammusic.util.TextUtil;
import dream.app.com.dreammusic.util.ToastUtil;

/**
 * Created by Administrator on 2015/8/8.
 */
public class NovelActivity extends BaseActivity implements AdapterView.OnItemClickListener , AdapterView.OnItemLongClickListener{

    private MyViewPagerIndicator mIndicator;
    private ViewPager mViewPager;
    private List<View> mViewsList;
    private List<NovelEntry> mLocalNovels;
    private ListView mClassesListView;
    private LoadingDialog loadingDialog;
    private GridView mGridView;
    private View v_bookshelf;
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
    protected void onResume(){
        super.onResume();
        NovelInfoDAO dao = new NovelInfoDAO(this);
        setShelfEmptyOrNot(dao.hasData());
        if(dao.hasData()){
            mLocalNovels = dao.getNovels();
            mGridView.setAdapter(new GridBookShelfAdapter(this,mLocalNovels));
        }
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
        initBookShelfView();
        View v_classification = initClassificationView();
        TextView tv2 = new TextView(this);
        tv2.setText("搜索");
        mViewsList.add(v_bookshelf);
        mViewsList.add(v_classification);
        mViewsList.add(tv2);
        mViewPager.setAdapter(new MyViewPagerAdapter(this, mViewsList));
    }

    private void initBookShelfView() {
        v_bookshelf = View.inflate(this, R.layout.view_bookshelf, null);
        mGridView = (GridView) v_bookshelf.findViewById(R.id.gridview_bookshelf);
        mGridView.setOnItemClickListener(this);
        mGridView.setOnItemLongClickListener(this);
        NovelInfoDAO dao = new NovelInfoDAO(this);
        setShelfEmptyOrNot(dao.hasData());
        if(dao.hasData()){
            mLocalNovels = dao.getNovels();
            mGridView.setAdapter(new GridBookShelfAdapter(this, mLocalNovels));
        }
    }

    private void setShelfEmptyOrNot(boolean b){
        if(b){
            mGridView.setVisibility(View.VISIBLE);
            v_bookshelf.findViewById(R.id.iv_bookshelf_empty).setVisibility(View.GONE);
        }else{
            mGridView.setVisibility(View.GONE);
            v_bookshelf.findViewById(R.id.iv_bookshelf_empty).setVisibility(View.VISIBLE);
        }
    }

    /**
     * 初始化分类界面
     * @return
     */
    @NonNull
    private View initClassificationView() {
        View v_classification = View.inflate(this, R.layout.view_novel_classification,null);
        mClassesListView= (ListView) v_classification.findViewById(R.id.listview_novel_classes);
        mClassesListView.setAdapter(new NovelClassesAdapter(this));
        mClassesListView.setOnItemClickListener(this);
        return v_classification;
    }

    private void cancelLoadingDialog(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingDialog.cancel();
            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        if(parent.getId()==R.id.listview_novel_classes){
            getNovelClassContent(position);
        }else if(parent.getId()==R.id.gridview_bookshelf){
            openBook(position);
        }
    }

    private void openBook(int position){
        final NovelEntry entry = (NovelEntry) mGridView.getItemAtPosition(position);
        final String url = entry.getmBaseUrl()+(entry.getmLastChapter()+1)+".html";
        showLoadingDlg();
        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect(url).get();
                    String chaptername = NovelAPI.getChapterName(doc);
                    String content = NovelAPI.getChapterContent(doc);
                    TextUtil.writeNovelContent(chaptername, content);
                    Intent intent = new Intent();
                    intent.putExtra("firstpageurl",url);
                    intent.putExtra("from","local");
                    intent.putExtra("bookname",entry.getmBookName());
                    startNewActivityWithAnim(ReadNovelActivity.class, intent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                cancelLoadingDlg();
            }
        }).start();
    }

    /**
     * 根据类别得到小说列表
     * @param position
     */
    private void getNovelClassContent(int position){
        HashMap<String,String> map = (HashMap<String, String>) mClassesListView.getItemAtPosition(position);
        final String url = map.get("url");
        final String name = map.get("name");
        loadingDialog.show();
        new Thread(new Runnable(){
            @Override
            public void run(){
                try {
                    Document doc = Jsoup.connect(url).get();
                    if(doc!=null){
                        Intent intent = new Intent();
                        intent.putExtra("name",name);
                        intent.putExtra("html",doc.toString());
                        intent.putExtra("url", url);
                        int pageCount = NovelAPI.getPageCount(doc);
                        intent.putExtra("pagecount",pageCount);
                        startNewActivityWithAnim(NovelListActivity.class,intent);
                    }
                } catch (IOException e){
                    e.printStackTrace();
                }
                cancelLoadingDialog();
            }
        }).start();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final Dialog dialog = new Dialog(this,R.style.Theme_loading_dialog);
        View v = View.inflate(this,R.layout.view_bookshelf_operation,null);
        initDialogItemListener(position, dialog, v);
        dialog.setContentView(v);
        DialogUtil.setDialogAttr(dialog, this);
        dialog.show();
        return false;
    }

    private void initDialogItemListener(int position, final Dialog dialog, View v) {
        TextView tv_chapter = (TextView) v.findViewById(R.id.tv_dialogitem_seechapter);
        TextView tv_remove = (TextView) v.findViewById(R.id.tv_dialogitem_removebook);
        final NovelEntry entry = (NovelEntry) mGridView.getItemAtPosition(position);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showLoadingDlg();
                if(v.getId()==R.id.tv_dialogitem_seechapter){
                    new Thread(new Runnable(){
                        @Override
                        public void run() {
                            String homepage = entry.getmMainPageUrl();
                            try {
                                Document document = Jsoup.connect(homepage).get();
                                String htmlContent = document.toString();
                                Intent intent = new Intent();
                                intent.putExtra("htmlcontent",htmlContent);
                                intent.putExtra("from","local");
                                intent.putExtra("bookurl",entry.getmMainPageUrl());
                                intent.putExtra("name",entry.getmBookName());
                                startNewActivityWithAnim(NovelChapterActivity.class,intent);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            cancelLoadingDlg();
                        }
                    }).start();

                }else if(v.getId()==R.id.tv_dialogitem_removebook){

                }
            }
        };
        tv_chapter.setOnClickListener(listener);
        tv_remove.setOnClickListener(listener);
    }
}
