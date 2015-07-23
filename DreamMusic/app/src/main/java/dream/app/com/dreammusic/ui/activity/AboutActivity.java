package dream.app.com.dreammusic.ui.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.bmob.BUpdateInfo;
import dream.app.com.dreammusic.config.App;
import dream.app.com.dreammusic.ui.view.LoadingDialog;
import dream.app.com.dreammusic.util.DialogUtil;
import dream.app.com.dreammusic.util.DownLoadUtil;

/**
 * Created by Administrator on 2015/7/18.
 */
public class AboutActivity extends BaseActivity {

    private View view_feedback,view_update,view_developer;
    private TextView mVersionView;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initViriable();
        initView();
        initListener();
        updateView();
        setTitle("关于");
    }
    private void updateView() {
        mVersionView.setText("版本："+App.getVersion(this));
    }

    private void initViriable() {
        loadingDialog = DialogUtil.createLoadingDialog(this);
    }

    @Override
    public void initView() {
        super.initView();
        view_feedback = findViewById(R.id.view_about_feedback);
        view_update = findViewById(R.id.view_about_updateversion);
        view_developer = findViewById(R.id.view_about_developer);
        mVersionView = (TextView) findViewById(R.id.tv_about_version);

    }

    @Override
    protected void initListener(){
        super.initListener();
        view_feedback.setOnClickListener(this);
        view_update.setOnClickListener(this);
        view_developer.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.view_about_feedback:
                startNewActivityWithAnim(FeedbackActivity.class);
                break;
            case R.id.view_about_updateversion:
                checkUpdateInfo();
                break;
            case R.id.view_about_developer:
                startNewActivityWithAnim(DeveloperActivity.class);
                break;
        }
    }

    private void checkUpdateInfo() {
        loadingDialog.show();
        BmobQuery<BUpdateInfo> query = new BmobQuery<BUpdateInfo>();
        query.findObjects(this,new FindListener<BUpdateInfo>() {
            @Override
            public void onSuccess(List<BUpdateInfo> bUpdateInfos) {
                loadingDialog.cancel();
                if(bUpdateInfos!=null&&bUpdateInfos.size()>0){
                    BUpdateInfo bUpdateInfo = bUpdateInfos.get(bUpdateInfos.size()-1);
                    double version = bUpdateInfo.getVersion();
                    String appurl = bUpdateInfo.getAppurl();
                    String content = bUpdateInfo.getContent();
                    if(version>App.getVersion(AboutActivity.this)){
                        showUpdateDialog(version,appurl,content);
                    }else{
                        DialogUtil.showMessageDialog(AboutActivity.this,"当前已是最新版本，无需更新");
                    }
                }
                else{
                    DialogUtil.showMessageDialog(AboutActivity.this,"操作失败，请重试");
                }
            }

            @Override
            public void onError(int i, String s) {
                loadingDialog.cancel();
                DialogUtil.showMessageDialog(AboutActivity.this,"操作失败，请重试");
            }
        });
    }

    private void showUpdateDialog(final double version, final String appurl, String content) {
        final Dialog dialog = new Dialog(this, R.style.Theme_loading_dialog);
        View _View = View.inflate(this,R.layout.dialog_updateinfo,null);
        TextView tv_title = (TextView) _View.findViewById(R.id.tv_dialog_top_title);
        TextView tv_version = (TextView) _View.findViewById(R.id.tv_dialog_update_version);
        TextView tv_content = (TextView) _View.findViewById(R.id.tv_dialog_update_content);
        tv_version.setText(App.sAppName+" v"+version);
        tv_title.setText("版本更新");
        String[] _Content = content.split("#");
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<_Content.length;i++){
            sb.append((i+1)+"."+_Content[i]+"\n");
        }
        tv_content.setText(sb.toString());
        Button btn_cancel = (Button) _View.findViewById(R.id.btn_dialog_update_cancel);
        Button btn_update = (Button) _View.findViewById(R.id.btn_dialog_update_ok);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_update.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                DownLoadUtil downLoadUtil = new DownLoadUtil(AboutActivity.this);
                downLoadUtil.downloadFile(appurl,App.sAppName_English+".apk",1);
            }
        });
        dialog.setContentView(_View);
        DialogUtil.setDialogAttr(dialog, AboutActivity.this);
        dialog.show();
    }
}
