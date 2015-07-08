package dream.app.com.dreammusic.util;

import android.content.Context;
import android.content.SharedPreferences;

import dream.app.com.dreammusic.config.ApplicationConfig;
import dream.app.com.dreammusic.entry.UserEntry;


/**
 * Created by Administrator on 2015/6/26.
 */
public class SharedPreferencesUtil {
    private static Context mContext;
    private static SharedPreferences mPreference;
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
        SharedPreferences.Editor editor = getSharedPreferences(name).edit();
        return editor;
    }

    public static String getHeadImageUrl(){
        mPreference = getSharedPreferences(ApplicationConfig.USER);
        return mPreference.getString(UserEntry.HEADIMAGE,"");
    }
}
