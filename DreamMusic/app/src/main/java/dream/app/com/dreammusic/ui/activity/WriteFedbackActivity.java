package dream.app.com.dreammusic.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.bmob.v3.listener.SaveListener;
import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.bmob.BFeedbackInfo;
import dream.app.com.dreammusic.service.MusicService;
import dream.app.com.dreammusic.ui.view.LoadingDialog;
import dream.app.com.dreammusic.util.DialogUtil;
import dream.app.com.dreammusic.util.SharedPreferencesUtil;
import dream.app.com.dreammusic.util.ToastUtil;

/**
 * Created by Administrator on 2015/7/22.
 */
public class WriteFedbackActivity extends BaseActivity {

    private EditText mEditContent;
    private EditText mEditQQ,mEditPhone;
    private Button mBtn;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writefeedback);
        MusicService.addActivity(this);
        initVariable();
        initView();
        initListener();
        setTitle("反馈意见");

    }

    private void initVariable() {
        loadingDialog = DialogUtil.createLoadingDialog(this);
    }

    @Override
    public void initView() {
        super.initView();
        mEditContent = (EditText) findViewById(R.id.et_writefeedback_content);
        mEditQQ = (EditText) findViewById(R.id.et_writefeedback_qq);
        mEditPhone = (EditText) findViewById(R.id.et_writefeedback_phone);
        mBtn = (Button) findViewById(R.id.btn_writefeedback_send);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(v.getId()==R.id.btn_writefeedback_send){
            sendFeedback();
        }
    }

    private void sendFeedback() {
        loadingDialog.show();
        String content = mEditContent.getText().toString();
        String qq = mEditQQ.getText().toString();
        String phone = mEditPhone.getText().toString();
        if(content.length()<1){
            loadingDialog.cancel();
            DialogUtil.showMessageDialog(this,"反馈内容不能为空");
            return;
        }
        if(qq.length()<1&&phone.length()<1){
            loadingDialog.cancel();
            DialogUtil.showMessageDialog(this,"QQ和手机号请至少填一个");
            return;
        }
        final String loginId = SharedPreferencesUtil.getUid();
        String headImageUrl = SharedPreferencesUtil.getHeadImageUrl();
        String username = SharedPreferencesUtil.getUserName();
        long time = (System.currentTimeMillis()/1000);
        BFeedbackInfo bFeedbackInfo = new BFeedbackInfo();
        bFeedbackInfo.setInfo(loginId,username,headImageUrl,content,qq,phone,time);
        bFeedbackInfo.save(this, new SaveListener(){
            @Override
            public void onSuccess() {
                loadingDialog.cancel();
                ToastUtil.showMessage(WriteFedbackActivity.this, "发送成功");
                onBackPressed();
            }
            @Override
            public void onFailure(int i, String s) {
                loadingDialog.cancel();
                ToastUtil.showMessage(WriteFedbackActivity.this,"发送失败");
            }
        });
    }
}
