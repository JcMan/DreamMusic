package dream.app.com.dreammusic.config;

import android.os.Environment;

/**
 * Created by Administrator on 2015/6/26.
 */
public class ApplicationConfig {

    public static final String RECEIVER_ALARM = "dream.app.com.alarm";
    public static String BMOB_APP_ID = "1bd683a9d67cefbd483b4f08b1f4cb15";



    public static final String USER = "User";

    public static final String SETTING = "Setting";
    public static final String ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String APPDIR = ROOT_PATH+"/DreamMusic";
    public static final String DOWNLOADDIE = APPDIR+"/"+"download/";
    public static final String MVDIE = APPDIR+"/"+"mv/";
    public static final String LRC_DIR = APPDIR+"/"+"lrc/";
    public static final String ARTIST_DIR = APPDIR+"/"+"artist/";
    public static final String NOVEL_DIR = APPDIR+"/"+"novel/";



}
