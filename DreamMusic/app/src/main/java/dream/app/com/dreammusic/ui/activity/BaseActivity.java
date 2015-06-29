package dream.app.com.dreammusic.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import dream.app.com.dreammusic.R;

/**
 * Created by Administrator on 2015/6/26.
 */
public class BaseActivity extends Activity implements View.OnClickListener{
    private ImageButton mTopBack,mTopLeftLogo;
    private TextView mTopTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * 设置监听器
     */
    protected void initListener() {
        mTopBack.setOnClickListener(this);
        mTopLeftLogo.setOnClickListener(this);
    }

    /**
     * 初始化控件
     */
    public void initView(){
        mTopBack = (ImageButton) findViewById(R.id.ib_top_back);
        mTopLeftLogo = (ImageButton) findViewById(R.id.ib_top_logo_left);
        mTopTitle = (TextView) findViewById(R.id.tv_top_title);
    }

    /**
     * 设置返回按钮不可见
     */
    protected void setTopBackBtnGone(){
        mTopBack.setVisibility(View.GONE);
    }

    /**
     * 设置左侧Logo按钮不可见
     */
    protected void setTopLeftLogoGone(){
        mTopLeftLogo.setVisibility(View.GONE);
    }

    /**
     * 设置左侧Logo按钮可见
     */
    protected  void setTopLeftLogoVisible(){
        mTopLeftLogo.setVisibility(View.VISIBLE);
    }

    /**
     * 设置顶部标题
     * @param title
     */
    protected void setTitle(String title){
        mTopTitle.setText(title);
    }

    /**
     * 响应顶部返回按钮的点击事件
     */
    protected void clickOnBackBtn(){
        onBackPressed();
    }

    /**
     * 响应顶部Logo按钮的点击事件
     */
    protected void clickOnLeftLogo(){

    }

    /**
     * 打开新的Activity，可以设置打开动画
     * @param pclass
     * @param inStyle 新的Activity进入的动画
     * @param outStyle 旧Activity出去的动画
     */
    protected void startNewActivity(Class pclass,int inStyle,int outStyle){
        startActivity(new Intent(this,pclass));
        overridePendingTransition(inStyle,outStyle);
    }

    /**
     * 打开新的Activity，默认的打开方式
     * @param pclass
     */
    protected void startNewActivity(Class pclass){
        startActivity(new Intent(this,pclass));
    }

    public void showMessage(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_top_back:
                clickOnBackBtn();
                break;
            case R.id.ib_top_logo_left:
                clickOnLeftLogo();
                break;
            default:
                break;
        }

    }


}
