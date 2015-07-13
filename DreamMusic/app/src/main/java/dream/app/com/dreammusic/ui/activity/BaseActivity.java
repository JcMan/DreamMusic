package dream.app.com.dreammusic.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.util.AnimUtil;

/**
 * Created by Administrator on 2015/6/26.
 */
public class BaseActivity extends Activity implements View.OnClickListener{
    private ImageButton mTopBack,mTopRightLogo,mTopToggle;
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
        mTopRightLogo.setOnClickListener(this);
        mTopToggle.setOnClickListener(this);
    }

    /**
     * 初始化控件
     */
    public void initView(){
        mTopBack = (ImageButton) findViewById(R.id.ib_top_back);
        mTopRightLogo = (ImageButton) findViewById(R.id.ib_top_logo_right);
        mTopTitle = (TextView) findViewById(R.id.tv_top_title);
        mTopToggle = (ImageButton) findViewById(R.id.ib_top_toggle);
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
    protected void setTopRightLogoGone(){
        mTopRightLogo.setVisibility(View.GONE);
    }

    /**
     * 设置左侧Logo按钮可见
     */
    protected  void setTopLeftRightVisible(){
        mTopRightLogo.setVisibility(View.VISIBLE);
    }

    protected void setTopRightToggleVisible(){
        mTopToggle.setVisibility(View.VISIBLE);
    }

    protected void setTopRightToggleGone(){
        mTopToggle.setVisibility(View.GONE);
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
    protected void clickOnRightLogo(){

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

    protected void startNewActivityWithAnim(Class pclass){
        startActivity(new Intent(this,pclass));
        overridePendingTransition(AnimUtil.BASE_SLIDE_RIGHT_IN,AnimUtil.BASE_SLIDE_REMAIN);
    }
    protected void startNewActivityWithAnim(Class pclass ,Intent intent){
        intent.setClass(this,pclass);
        startActivity(intent);
        overridePendingTransition(AnimUtil.BASE_SLIDE_RIGHT_IN,AnimUtil.BASE_SLIDE_REMAIN);
    }

    protected void clickOnToggle() {

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
            case R.id.ib_top_logo_right:
                clickOnRightLogo();
                break;
            case R.id.ib_top_toggle:
                clickOnToggle();
                break;
            default:
                break;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(AnimUtil.BASE_SLIDE_REMAIN,AnimUtil.BASE_SLIDE_RIGHT_OUT);

    }




}
