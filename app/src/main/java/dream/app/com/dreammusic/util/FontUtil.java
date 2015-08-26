package dream.app.com.dreammusic.util;

import android.widget.Button;

import java.io.File;

import dream.app.com.dreammusic.config.ApplicationConfig;

/**
 * Created by Administrator on 2015/8/26.
 */
public class FontUtil {

    private static final String FONT_KAITI = "http://ac-eukobooe.clouddn.com/aIzBSVSzhIlZmB8dA6SE1Mju8FsjyQP8RPBLjedG.ttf";
    private static final String FONT_FANGSONG = "http://ac-eukobooe.clouddn.com/gekmrvu5oNVkD3e7IjN1suVSryl8dTcbgZ5EpB2S.ttf";
    private static final String FONT_XIAOLI = "http://ac-eukobooe.clouddn.com/H8XLLYn48oBMzuSUkHQFh3v1aZgGw2vO6Ce6oRlf.ttf";
    private static final String FONT_YOUYUAN = "http://ac-eukobooe.clouddn.com/qsfdbuhhcVx8LucE4bc2pEzFtGPJExlfoPMaX9e6.ttf";

    private static String[] fonts = {"kaiti","fangsong","xiaoli","youyuan"};
    public static void setFontBtnState(Button...btns){
        for (int i = 0; i < btns.length; i++) {
            if(fontExists(fonts[i])){
                btns[i].setText("设置");
            }
        }
    }

    public static boolean fontExists(String font){
        String fontPath = ApplicationConfig.FONT_DIR;
        File file = new File(fontPath+font+".ttf");
        if (file.exists())
            return true;
        return false;
    }

    public static String getFontUrl(String font){
        if(font.equals("kaiti"))
            return FONT_KAITI;
        else if(font.equals("fangsong"))
            return FONT_FANGSONG;
        else if(font.equals("xiaoli"))
            return FONT_XIAOLI;
        else
            return FONT_YOUYUAN;
    }
}
