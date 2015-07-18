package dream.app.com.dreammusic.ui.activity;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.app.tool.logger.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.adapter.GridBgAdapter;
import dream.app.com.dreammusic.entry.BgEntry;
import dream.app.com.dreammusic.util.SharedPreferencesUtil;

/**
 * Created by Administrator on 2015/7/15.
 */
public class ChangeBgActivity extends BaseActivity implements AdapterView.OnItemClickListener{

    private GridView mGridView;
    private List<BgEntry> mBgList;
    private GridBgAdapter adapter;
    private String mDefaultPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_bg);
        getData();
        initView();
        initListener();
        setTitle("更换背景");
    }

    @Override
    public void initView() {
        super.initView();
        mGridView = (GridView) findViewById(R.id.grid_content);
        adapter = new GridBgAdapter(mBgList, mDefaultPath,this);
        mGridView.setAdapter(adapter);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mGridView.setOnItemClickListener(this);
    }

    /**
     * 得到背景图片的路径
     * @return
     */
    String getDefaultBgPath(){
        String path = SharedPreferencesUtil.getDefaultBgPath();
        return path;
    }

    /**
     * 设置背景图片的默认路径
     * @param path
     */
   void setDefaultBgPath(String path){
       SharedPreferencesUtil.setDefaultBgPath(path);
   }

    private void getData() {
        AssetManager am = getAssets();
        try {
            String[] bgList = am.list("bkgs");
            mBgList = new ArrayList<BgEntry>();
            for(String path:bgList){
                BgEntry bgEntry = new BgEntry();
                InputStream is = am.open("bkgs/"+path);
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                bgEntry.path = path;
                bgEntry.bitmap = bitmap;
                mBgList.add(bgEntry);
                is.close();
            }

        } catch (IOException e) {}
        mDefaultPath = getDefaultBgPath();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String path = adapter.getItem(position).path;
        setDefaultBgPath(path);
        adapter.setDeafultPath(path);
        adapter.notifyDataSetChanged();
        onBackPressed();
    }
}
