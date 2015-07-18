package dream.app.com.dreammusic.ui.activity;

import android.os.Bundle;

import dream.app.com.dreammusic.R;

/**
 * Created by Administrator on 2015/7/18.
 */
public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initView();
        initListener();
        setTitle("关于");
    }

    @Override
    public void initView() {
        super.initView();
    }
}
