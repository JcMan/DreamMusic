package dream.app.com.dreammusic.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;

import com.app.tool.logger.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.adapter.NetNovelAdapter;
import dream.app.com.dreammusic.entry.NovelAPI;
import dream.app.com.dreammusic.entry.NovelEntry;
import dream.app.com.dreammusic.service.MusicService;
import dream.app.com.dreammusic.ui.view.LoadListView;
import dream.app.com.dreammusic.util.ToastUtil;

/**
 * Created by Administrator on 2015/8/10.
 */
public class NovelListActivity extends BaseActivity implements LoadListView.ILoadListener,AdapterView.OnItemClickListener{
    private LoadListView mListView;
    private String mClassName;
    private String mHtmlContent;
    private List<NovelEntry> mNovelList;
    private NetNovelAdapter mAdapter;
    private String mUrl;
    private int mNowPage = 1;
    private int mPageCount = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novellist);
        MusicService.addActivity(this);
        initVariable();
        getDataFromIntent();
        initView();
        initListener();
        setTitle(mClassName);
        initListView();
    }

    private void initVariable() {
        mNovelList = new ArrayList<NovelEntry>();
    }

    private void initListView(){
        if(mNovelList!=null){
            mAdapter = new NetNovelAdapter(this,mNovelList);
            mListView.setAdapter(mAdapter);
        }
    }

    @Override
    public void initView(){
        super.initView();
        mListView = (LoadListView) findViewById(R.id.listview_netnovel);
        mListView.setOnItemClickListener(this);
        mListView.setLoadListener(this);
    }

    public void getDataFromIntent(){
        Intent intent = getIntent();
        mClassName = intent.getStringExtra("name");
        mHtmlContent = intent.getStringExtra("html");
        mUrl = intent.getStringExtra("url");
        mPageCount = intent.getIntExtra("pagecount",1);
        mNovelList = NovelAPI.getNovelList(mHtmlContent);
    }

    @Override
    public void onLoad() {
        if(mNowPage+1<=mPageCount){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadMoreNovels();
                }
            },1500);

        }else{
            mListView.loadComplete();
        }
    }

    /**
     * 加载更多小说
     */
    private void loadMoreNovels() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(mNowPage+1<=mPageCount)
                    try {
                        Document doc = (Document) Jsoup.connect(getNextPageurl(++mNowPage)).get();
                        List<NovelEntry> _List = NovelAPI.getNovelList(doc);
                        for (int i = 0; i <_List.size() ; i++) {
                            mNovelList.add(_List.get(i));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                notifyDataChanged();
            }
        }).start();
    }

    /**
     * 得到下一页小说的链接
     * @param nowCount
     * @return
     */
    private String getNextPageurl(int nowCount){
        return mUrl+"&page="+nowCount;
    }

    private void notifyDataChanged(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
                mListView.loadComplete();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final NovelEntry entry = (NovelEntry) mListView.getItemAtPosition(position);
        final String bookurl = entry.getmBookUrl();

        showLoadingDlg();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect(bookurl).get();
                    String htmlContent = doc.toString();
                    Intent intent = new Intent();
                    intent.putExtra("bookurl",bookurl);
                    intent.putExtra("name",entry.getmBookName());
                    intent.putExtra("author",entry.getmAuthor());
                    intent.putExtra("imgurl",entry.getmImgUrl());
                    intent.putExtra("htmlcontent", htmlContent);
                    startNewActivityWithAnim(NovelDetailActivity.class, intent);
                }catch(Exception e){}
                cancelLoadingDlg();


            }
        }).start();
    }
}
