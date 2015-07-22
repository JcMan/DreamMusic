package dream.app.com.dreammusic.ui.activity;

import android.os.Bundle;
import android.view.View;

import dream.app.com.dreammusic.R;

/**
 * Created by Administrator on 2015/7/18.
 */
public class AboutActivity extends BaseActivity {

    private View view_feedback;
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
        view_feedback = findViewById(R.id.view_about_feedback);
    }

    @Override
    protected void initListener() {
        super.initListener();
        view_feedback.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.view_about_feedback:
                startNewActivityWithAnim(WriteFedbackActivity.class);
                break;
        }
    }
}
