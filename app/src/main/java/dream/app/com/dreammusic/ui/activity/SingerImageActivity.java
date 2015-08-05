package dream.app.com.dreammusic.ui.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.tool.logger.Logger;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.adapter.MyViewPagerAdapter;
import dream.app.com.dreammusic.config.App;
import dream.app.com.dreammusic.config.ApplicationConfig;
import dream.app.com.dreammusic.service.MusicService;
import dream.app.com.dreammusic.ui.view.LoadingDialog;
import dream.app.com.dreammusic.util.AnimUtil;
import dream.app.com.dreammusic.util.DialogUtil;
import dream.app.com.dreammusic.util.ToastUtil;

/**
 * Created by Administrator on 2015/7/31.
 */
public class SingerImageActivity extends Activity implements View.OnClickListener{

    private ViewPager mViewPager;
    private List<View> mViewsList;
    private List<String> mUrlsList;
    private Button mBtn;
    private int mIndex = 0;
    private String mSinger;
    private LoadingDialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singerimage);
        MusicService.addActivity(this);
        getDateFromIntent();
        initView();
        loadingDialog = DialogUtil.createLoadingDialog(this);


    }

    private void initView(){
        mViewPager = (ViewPager) findViewById(R.id.viewpager_singerimage);
        mBtn = (Button) findViewById(R.id.btn_singerimage_down);
        mBtn.setOnClickListener(this);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mIndex = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        initPagerViews();
        mViewPager.setAdapter(new MyViewPagerAdapter(this,mViewsList));
    }

    private void initPagerViews(){
        FinalBitmap finalBitmap = FinalBitmap.create(this);
        Bitmap bitmap_loading = BitmapFactory.decodeResource(getResources(), R.drawable.ic_singer_loading);
        Bitmap bitmap_failure =BitmapFactory.decodeResource(getResources(),R.drawable.ic_singer_loading_failure);
        if(mUrlsList.size()!=0){
            for(int i=0;i<mUrlsList.size();i++){
                ImageView iv = new ImageView(this);
                LinearLayout.LayoutParams pa = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                iv.setLayoutParams(pa);
                finalBitmap.display(iv,mUrlsList.get(i),App.sScreenWidth,App.sScreenHeight,bitmap_loading,bitmap_failure);
                mViewsList.add(iv);
            }
        }else{
            ImageView iv = new ImageView(this);
            LinearLayout.LayoutParams pa = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            iv.setLayoutParams(pa);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            iv.setImageBitmap(bitmap_failure);

            mViewsList.add(iv);
        }
    }

    public void getDateFromIntent() {
        mViewsList = new ArrayList<View>();
        mUrlsList = new ArrayList<String>();
        String data = getIntent().getStringExtra("data");
        mSinger = getIntent().getStringExtra("singer");
        try {
            JSONObject object = new JSONObject(data).getJSONObject("data");
            JSONArray array = object.getJSONArray("urls");
            for (int i = 0; i < array.length(); i++) {
                String url = get480ImageUrl(array.getString(i));
                mUrlsList.add(url);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v){
        loadingDialog.show();
        FinalHttp finalHttp = new FinalHttp();
        String path = ApplicationConfig.ARTIST_DIR+mSinger+".jpg";
        finalHttp.download(mUrlsList.get(mIndex), path, new AjaxCallBack<File>() {
            @Override
            public void onSuccess(File file){
                super.onSuccess(file);
                loadingDialog.cancel();
                onBackPressed();
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                loadingDialog.cancel();
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(AnimUtil.BASE_SLIDE_REMAIN, AnimUtil.BASE_SLIDE_RIGHT_OUT);
    }

    private String get480ImageUrl(String url){
        return url.replace("{size}","480");
    }
}
