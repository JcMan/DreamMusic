package dream.app.com.dreammusic.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.service.MusicService;

/**
 * Created by JcMan on 2015/7/27.
 */
public class DynamicActivity extends BaseActivity{

    private View v_share,v_novel;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic);
        MusicService.addActivity(this);
        initView();
        initListener();
        setTitle("动态");
    }

    @Override
    protected void initListener() {
        super.initListener();
        v_share.setOnClickListener(this);
        v_novel.setOnClickListener(this);
    }

    @Override
    public void initView(){
        super.initView();
        v_share = findViewById(R.id.view_music_share);
        v_novel = findViewById(R.id.view_music_share_novel);
    }

    @Override
    public void onClick(View v){
        super.onClick(v);
        switch (v.getId()){
            case R.id.view_music_share:
                startNewActivity(UserShareActivity.class);
                break;
            case R.id.view_music_share_novel:
                startNewActivity(NovelActivity.class);
                break;
        }
    }
}
