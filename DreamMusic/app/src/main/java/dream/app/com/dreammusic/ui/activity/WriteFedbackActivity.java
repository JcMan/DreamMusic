package dream.app.com.dreammusic.ui.activity;

import android.os.Bundle;

import dream.app.com.dreammusic.R;

/**
 * Created by Administrator on 2015/7/22.
 */
public class WriteFedbackActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writefeedback);
        initView();
        initListener();
        setTitle("意见反馈区");
    }
}
