package dream.app.com.dreammusic.ui.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.service.MusicService;
import dream.app.com.dreammusic.util.DialogUtil;
import dream.app.com.dreammusic.util.DownLoadUtil;
import dream.app.com.dreammusic.util.FontUtil;
import dream.app.com.dreammusic.util.SharedPreferencesUtil;

/**
 * Created by Administrator on 2015/8/26.
 */
public class NovelSettingActivity extends BaseActivity{

    private Dialog dlg_font;
    private static final String fontPath = "DreamMusic/font/";
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novel_setting);
        MusicService.addActivity(this);
        initView();
        initListener();
        setTitle("设置");
    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void onClick(View v){
        super.onClick(v);
        switch (v.getId()){
            case R.id.v_novel_setting_font:
                showFontSettingDlg();
                break;
            case R.id.btn_font_kaiti:
                setFont("kaiti");
                break;
            case R.id.btn_font_fangsong:
                setFont("fangsong");
                break;
            case R.id.btn_font_xiaoli:
                setFont("xiaoli");
                break;
            case R.id.btn_font_youyuan:
                setFont("youyuan");
                break;
            case R.id.btn_font_xitong:
                dlg_font.dismiss();
                SharedPreferencesUtil.setNovelFont("system");
                break;
        }
    }

    private void setFont(String font){
        dlg_font.dismiss();
        if(FontUtil.fontExists(font)){
            SharedPreferencesUtil.setNovelFont(font);
        }else{
            DownLoadUtil downLoadUtil = new DownLoadUtil(this);
            downLoadUtil.downloadFile(FontUtil.getFontUrl(font),fontPath+font+".ttf",font);
        }
    }

    private void showFontSettingDlg(){
        dlg_font = new Dialog(this,R.style.Theme_loading_dialog);
        View v = View.inflate(this,R.layout.dialog_novel_setting_font,null);
        Button btn_kaiti = (Button) v.findViewById(R.id.btn_font_kaiti);
        Button btn_fangsong = (Button) v.findViewById(R.id.btn_font_fangsong);
        Button btn_xiaoli = (Button) v.findViewById(R.id.btn_font_xiaoli);
        Button btn_youyuan = (Button) v.findViewById(R.id.btn_font_youyuan);
        Button btn_xitong = (Button) v.findViewById(R.id.btn_font_xitong);
        FontUtil.setFontBtnState(btn_kaiti,btn_fangsong,btn_xiaoli,btn_youyuan);
        dlg_font.setContentView(v);
        DialogUtil.setDialogAttr(dlg_font,this);
        dlg_font.show();
    }
}
