package dream.app.com.dreammusic.util;

import android.content.Context;
import android.content.SharedPreferences;

import dream.app.com.dreammusic.config.ApplicationConfig;
import dream.app.com.dreammusic.entry.SettingEntry;
import dream.app.com.dreammusic.entry.UserEntry;


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

    public static String getHeadImageUrl(){
        mPreference = getSharedPreferences(ApplicationConfig.USER);
        return mPreference.getString(UserEntry.HEADIMAGE,"");
    }

    public static boolean getIsSetSleep(){
        mPreference = getSharedPreferences(ApplicationConfig.SETTING);
        return mPreference.getBoolean(SettingEntry.SETSLEEP,false);
    }

    public static void setIsSetSleep(boolean result){
        mEditor = getEditor(ApplicationConfig.SETTING);
        mEditor.putBoolean(SettingEntry.SETSLEEP,result);
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
}
