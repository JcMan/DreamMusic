package dream.app.com.dreammusic.util;

import android.content.Context;
import android.content.SharedPreferences;

import dream.app.com.dreammusic.config.ApplicationConfig;
import dream.app.com.dreammusic.entry.SettingEntry;
import dream.app.com.dreammusic.entry.UserEntry;
import dream.app.com.dreammusic.service.MusicService;


/**
 * Created by Administrator on 2015/6/26.
 */
public class SharedPreferencesUtil {
    private static Context mContext;
    private static SharedPreferences mPreference;
    private static SharedPreferences.Editor mEditor;
    public static void init(Context context){
        mContext = context;
    }

    /**
     * 根据名称得到SharedPreference对象
     * @param name
     *            SharedPreferences名称
     * @return
     */
    public static SharedPreferences getSharedPreferences(String name){
        mPreference = mContext.getSharedPreferences(name,Context.MODE_PRIVATE);
        return mPreference;
    }

    public static String getUid(){
        mPreference = mContext.getSharedPreferences(ApplicationConfig.USER,Context.MODE_PRIVATE);
        return mPreference.getString(UserEntry.UID,"");
    }



    /**
     * 根据名称得到Editor对象
     * @param name
     *            SharedPreferences名称
     * @return
     */
    public static SharedPreferences.Editor getEditor(String name){
         mEditor = getSharedPreferences(name).edit();
        return mEditor;
    }

    public static String getUpdateInfo(){
        mPreference = getSharedPreferences(ApplicationConfig.SETTING);
        return mPreference.getString(SettingEntry.UPDATEINFO,"");
    }

    public static boolean getIsLogin(){
        mPreference = getSharedPreferences(ApplicationConfig.USER);
        return mPreference.getBoolean(UserEntry.LOGIN, false);
    }

    public static String getHeadImageUrl(){
        mPreference = getSharedPreferences(ApplicationConfig.USER);
        return mPreference.getString(UserEntry.HEADIMAGE, "");
    }

    public static String getUserName(){
        mPreference = getSharedPreferences(ApplicationConfig.USER);
        return mPreference.getString(UserEntry.USERNAME,"");
    }

    public static boolean getIsSetSleep(){
        mPreference = getSharedPreferences(ApplicationConfig.SETTING);
        return mPreference.getBoolean(SettingEntry.SETSLEEP, false);
    }

    public static void setIsSetSleep(boolean result){
        mEditor = getEditor(ApplicationConfig.SETTING);
        mEditor.putBoolean(SettingEntry.SETSLEEP,result);
        mEditor.commit();
    }

    public static void setUpdateInfo(String url){
        mEditor = getEditor(ApplicationConfig.SETTING);
        mEditor.putString(SettingEntry.UPDATEINFO, url);
        mEditor.commit();
    }

    public static String getDefaultBgPath(){
        mPreference = getSharedPreferences(ApplicationConfig.SETTING);
        String path = mPreference.getString(SettingEntry.BG,"001.jpg");
        return path;
    }

    public static void setDefaultBgPath(String path){
        mEditor= getEditor(ApplicationConfig.SETTING);
        mEditor.putString(SettingEntry.BG,path);
        mEditor.commit();
    }

    public static void setFeedbackAllRefreshTime(String time){
        mEditor = getEditor(ApplicationConfig.SETTING);
        mEditor.putString(SettingEntry.FEEDBACK_ALL_REFRESH_TIME,time);
        mEditor.commit();
    }

    public static String getFeedbackAllRefreshTime(){
        mPreference = getSharedPreferences(ApplicationConfig.SETTING);
        return mPreference.getString(SettingEntry.FEEDBACK_ALL_REFRESH_TIME,"刚刚");
    }

    public static boolean getAcceptTuiSong(){
        mPreference = getSharedPreferences(ApplicationConfig.SETTING);
        return mPreference.getBoolean(SettingEntry.ACCEPT_TUISONG, false);
    }

    public static void setAcceptTuiSong(boolean result){
        mEditor = getEditor(ApplicationConfig.SETTING);
        mEditor.putBoolean(SettingEntry.ACCEPT_TUISONG,result);
        mEditor.commit();
    }

    public static void setShakeEnable(boolean isEnable){
        mEditor = getEditor(ApplicationConfig.SETTING);
        mEditor.putBoolean(SettingEntry.SHAKE_ENABLE,isEnable);
        mEditor.commit();
    }

    public static boolean getShakeEnable(){
        mPreference = getSharedPreferences(ApplicationConfig.SETTING);
        return mPreference.getBoolean(SettingEntry.SHAKE_ENABLE,true);
    }

    public static int getPlayMode(){
        mPreference = getSharedPreferences(ApplicationConfig.SETTING);
        return mPreference.getInt("mode", MusicService.PLAY_MODE_SEQ);
    }

    public static void setPlayMode(int mode){
        mEditor = getEditor(ApplicationConfig.SETTING);
        mEditor.putInt("mode",mode);
        mEditor.commit();
    }

    public static void setNovelFont(String font){
        mEditor = getEditor(ApplicationConfig.SETTING);
        mEditor.putString("font", font);
        mEditor.commit();
    }

    public static String getNovelFont(){
        mPreference = getSharedPreferences(ApplicationConfig.SETTING);
        return mPreference.getString("font","system");
    }
}
