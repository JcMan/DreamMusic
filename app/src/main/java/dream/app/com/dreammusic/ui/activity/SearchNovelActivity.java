package dream.app.com.dreammusic.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.util.List;
import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.entry.NovelAPI;
import dream.app.com.dreammusic.entry.NovelEntry;
import dream.app.com.dreammusic.service.MusicService;

/**
 * Created by Administrator on 2015/8/16.
 */
public class SearchNovelActivity extends BaseActivity implements AdapterView.OnItemClickListener{

    private String mHtmlContent;
    private String mType;
    private List<NovelEntry> mNovelList;
    private ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_novel);
        MusicService.addActivity(this);
        getDataFromIntent();
        initView();
        initListener();
        setTitle("搜索结果");
        initListView();
    }


    private void initListView() {
        mListView = (ListView) findViewById(R.id.listview_search_novel);
        mListView.setOnItemClickListener(this);
        mNovelList = NovelAPI.getSearchNovelList(mType,mHtmlContent);
        if(mNovelList!=null&&mNovelList.size()>0){
            mListView.setAdapter(new SearchNovelAdapter(this,mNovelList));
        }
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        mHtmlContent = intent.getStringExtra("htmlcontent");
        mType = intent.getStringExtra("type");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final NovelEntry entry = (NovelEntry) mListView.getItemAtPosition(position);
        final String bookurl = entry.getmBookUrl();
        showLoadingDlg();
        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect(bookurl).get();
                    Intent intent = new Intent();
                    intent.putExtra("bookurl",bookurl);
                    intent.putExtra("name",entry.getmBookName());
                    intent.putExtra("author",entry.getmAuthor());
                    intent.putExtra("htmlcontent", doc.toString());
                    intent.putExtra("imgurl",NovelAPI.getImgUrl(doc));
                    startNewActivityWithAnim(NovelDetailActivity.class, intent);
                }catch (Exception e){}
                cancelLoadingDlg();

            }
        }).start();
    }

    class SearchNovelAdapter extends BaseAdapter{

        private List<NovelEntry> mList;
        private Context mContext;

        public SearchNovelAdapter(Context context,List<NovelEntry> list){
            mList = list;
            mContext = context;
        }
        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            if (convertView==null){
                convertView = View.inflate(mContext,R.layout.item_list_search_novel,null);
                holder = new Holder();
                holder.tv_author = (TextView) convertView.findViewById(R.id.tv_item_search_novel_author);
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_item_search_novel_name);
                holder.tv_state = (TextView) convertView.findViewById(R.id.tv_item_search_novel_state);
                convertView.setTag(holder);
            }else
                holder = (Holder) convertView.getTag();
            NovelEntry entry = mList.get(position);
            holder.tv_author.setText(entry.getmAuthor());
            holder.tv_name.setText(entry.getmBookName());
            holder.tv_state.setText(entry.getmIntroduction());
            return convertView;
        }

        class Holder{
            TextView tv_name,tv_author,tv_state;
        }
    }
}
