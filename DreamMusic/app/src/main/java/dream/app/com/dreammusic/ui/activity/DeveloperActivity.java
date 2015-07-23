package dream.app.com.dreammusic.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bmob.BTPFileResponse;
import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadListener;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.bmob.BUpdateInfo;
import dream.app.com.dreammusic.config.ApplicationConfig;
import dream.app.com.dreammusic.util.DialogUtil;
import dream.app.com.dreammusic.util.ToastUtil;

/**
 * Created by Administrator on 2015/7/23.
 */
public class DeveloperActivity extends BaseActivity {

    private EditText mEditVersion,mEditPasw;
    private Button mUploadBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer);
        initView();
        initListener();
        setTitle("开发者");
    }

    @Override
    public void initView() {
        super.initView();
        mEditVersion = (EditText) findViewById(R.id.et_deve_version);
        mEditPasw = (EditText) findViewById(R.id.et_deve_pasw);
        mUploadBtn = (Button) findViewById(R.id.btn_deve_upload);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mUploadBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(v.getId()==R.id.btn_deve_upload){
            uploadFile();
        }
    }

    private void uploadFile() {
        final String version = mEditVersion.getText().toString();
        String pasw = mEditPasw.getText().toString();
        if(!pasw.equals("342222")){
            DialogUtil.showMessageDialog(this,"密码错误");
            return;
        }
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
        String file_path = ApplicationConfig.ROOT_PATH+"/dreammusic_"+version+".apk";
        BTPFileResponse response = BmobProFile.getInstance(this).upload(file_path,new UploadListener() {
            @Override
            public void onSuccess(String s, String s2, BmobFile bmobFile) {
                progressDialog.cancel();
                DialogUtil.showMessageDialog(DeveloperActivity.this, "上传成功");
                BUpdateInfo bUpdateInfo = new BUpdateInfo();
                bUpdateInfo.setContent("未添加");
                bUpdateInfo.setAppurl(bmobFile.getUrl());
                bUpdateInfo.setVersion(Double.parseDouble(version));
                bUpdateInfo.save(DeveloperActivity.this,new SaveListener() {
                    @Override
                    public void onSuccess() {
                        ToastUtil.showMessage(DeveloperActivity.this,"信息保存成功");
                    }
                    @Override
                    public void onFailure(int i, String s) {
                        ToastUtil.showMessage(DeveloperActivity.this,"信息保存失败");
                    }
                });
            }

            @Override
            public void onProgress(int i) {
                progressDialog.setProgress(i);
            }

            @Override
            public void onError(int i, String s) {
                progressDialog.cancel();
                DialogUtil.showMessageDialog(DeveloperActivity.this,"上传失败");
            }
        });
    }
}
