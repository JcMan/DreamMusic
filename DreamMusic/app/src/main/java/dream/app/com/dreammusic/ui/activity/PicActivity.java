package dream.app.com.dreammusic.ui.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import net.tsz.afinal.FinalBitmap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dream.app.com.dreammusic.R;

/**
 * Created by Administrator on 2015/7/21.
 */
public class PicActivity extends BaseActivity {

    private List<String> mPicList;
    private GridView mGridView;
    private MyGridViewAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic);
        initView();
        initListener();
        setTitle("歌手写真");
        getDateFromIntent();
    }

    @Override
    public void initView() {
        super.initView();
        mGridView = (GridView) findViewById(R.id.gridview_pic);
    }

    private void getDateFromIntent() {
        mPicList = new ArrayList<String>();
        String json = getIntent().getStringExtra("pic_json");
        try {
            JSONObject object = new JSONObject(json);
            JSONArray mArray = object.getJSONArray("data");
            if(mArray.length()>1){
                for(int i=0;i<mArray.length()-1;i++){
                    JSONObject oneObject = mArray.getJSONObject(i);
                    String url = oneObject.getString("objURL");
                    mPicList.add(url);
                    if(i==mArray.length()-2){
                        mAdapter = new MyGridViewAdapter(PicActivity.this,mPicList);
                        mGridView.setAdapter(mAdapter);
                        break;
                    }
                }
            }
        } catch (JSONException e) {}

    }

    class MyGridViewAdapter extends BaseAdapter {

        private List<String> imageURLs;
        private Context mContext;

        public MyGridViewAdapter(Context context,List<String> list){
            imageURLs = list;
            mContext = context;
        }

        @Override
        public int getCount() {
            return imageURLs.size();
        }

        @Override
        public Object getItem(int position){
            return imageURLs.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_gridview_download_pic,null);
            final ImageView view = (ImageView) convertView.findViewById(R.id.iv_imageview);
            FinalBitmap finalBitmap = FinalBitmap.create(PicActivity.this);
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_loading_image_big);
            finalBitmap.display(view,imageURLs.get(position),bitmap,bitmap);
            return convertView;
        }
    }
}
