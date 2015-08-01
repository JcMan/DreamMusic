package dream.app.com.dreammusic.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import dream.app.com.dreammusic.R;

/**
 * Created by JcMan on 2015/7/27.
 */
public class DynamicActivity extends BaseActivity{

    private ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic);
        initView();
        initListener();
        setTitle("动态");
        mListView.setAdapter(new DynamicAdapter());
    }

    @Override
    public void initView() {
        super.initView();
        mListView = (ListView) findViewById(R.id.listview_dynamic);
    }

    class DynamicAdapter extends BaseAdapter{

        private List<String> list = new ArrayList<String>();
        public DynamicAdapter(){
            for(int i=0;i<10;i++)
                list.add(""+i);
        }
        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = View.inflate(DynamicActivity.this,R.layout.item_list_share_music,null);
            return convertView;
        }
    }
}
