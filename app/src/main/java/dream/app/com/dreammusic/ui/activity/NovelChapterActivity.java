package dream.app.com.dreammusic.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.app.tool.logger.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;

import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.adapter.ChapterListAdapter;
import dream.app.com.dreammusic.db.NovelInfoDAO;
import dream.app.com.dreammusic.entry.ChapterEntry;
import dream.app.com.dreammusic.entry.NovelAPI;
import dream.app.com.dreammusic.ui.view.ScrollRelativeLayout;
import dream.app.com.dreammusic.util.TextUtil;
import dream.app.com.dreammusic.util.ToastUtil;

/**
 * Created by Administrator on 2015/8/10.
 */
public class NovelChapterActivity extends BaseActivity implements AdapterView.OnItemClickListener{

    private String mFrom;
    private String mBookUrl;
    private String mBookName;
    private String mHtmlContent;
    private ListView mListView;
    private List<ChapterEntry> mChapterList;
    private ChapterListAdapter mChapterAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novelchapter);
        getDataFromIntent();
        initVariable();
        initView();
        initListener();
        initListView();
        setTitle("目录");
    }

    @Override
    public void initView() {
        super.initView();
        mListView = (ListView) findViewById(R.id.listview_novelchapter);

    }

    @Override
    protected void initListener() {
        super.initListener();
        mListView.setOnItemClickListener(this);
    }

    private void initVariable() {

    }

    private void initListView() {
        try {
            Document doc = Jsoup.parse(mHtmlContent);
            mChapterList = NovelAPI.getNetNovelChapters(doc,mBookUrl.replace("index.html",""));
            setNetListViewAdapter();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void setNetListViewAdapter(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mChapterAdapter = new ChapterListAdapter(NovelChapterActivity.this,mChapterList);
                mListView.setAdapter(mChapterAdapter);
            }
        });
    }
    private void getDataFromIntent(){
        Intent intent = getIntent();
        mFrom = intent.getStringExtra("from");
        mBookUrl = intent.getStringExtra("bookurl");
        mBookName = intent.getStringExtra("name");
        mHtmlContent = intent.getStringExtra("htmlcontent");

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        final ChapterEntry entry = (ChapterEntry) mListView.getItemAtPosition(position);
        showLoadingDlg();
        new Thread(new Runnable(){
            @Override
            public void run(){
                try {
                    Document doc = Jsoup.connect(entry.getmChapterUrl()).get();
                    String content  = NovelAPI.getChapterContent(doc);
                    String name = entry.getmChapterName();
                    TextUtil.writeNovelContent(name,content);
                    int chapter = NovelAPI.getChapter(entry.getmChapterUrl());
                    if(mFrom.equals("local")){
                        NovelInfoDAO dao = new NovelInfoDAO(NovelChapterActivity.this);
                        dao.updateChapterByName(mBookName,chapter);
                    }
                    Intent intent = new Intent();
                    intent.putExtra("firstpageurl",entry.getmChapterUrl());
                    startNewActivityWithAnim(ReadNovelActivity.class,intent);
                } catch (IOException e){
                    e.printStackTrace();
                }
                cancelLoadingDlg();
            }
        }).start();
    }
}
