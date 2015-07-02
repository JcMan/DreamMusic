package dream.app.com.dreammusic.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.fragment.FragmentMusicStore;
import dream.app.com.dreammusic.util.ActivityUtil;
import dream.app.com.dreammusic.util.AnimUtil;

/**
 * Created by JcMan on 2015/7/2.
 */
public class CommonActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);
        initView();
        initListener();
        setTitle(gettitleFromIntent());
        getFragmentManager().beginTransaction().add(R.id.layout_common_view,new FragmentMusicStore()).commit();
    }

    private String gettitleFromIntent() {
        String title = "";
        Intent intent = getIntent();
        title+=intent.getExtras().getString(ActivityUtil.TITLE);
        return title;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(AnimUtil.BASE_SLIDE_REMAIN,AnimUtil.BASE_SLIDE_RIGHT_OUT);
    }
}
