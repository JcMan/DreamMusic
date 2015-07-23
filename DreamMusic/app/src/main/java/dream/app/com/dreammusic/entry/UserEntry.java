package dream.app.com.dreammusic.entry;

import dream.app.com.dreammusic.util.SharedPreferencesUtil;

/**
 * Created by Administrator on 2015/6/27.
 */
public class UserEntry {
    public static final String USERNAME = "UserName";
    public static final String HEADIMAGE = "HeadImage";
    public static final String UID = "uid";
    public static final String LOGIN = "login";

    public static String getUid(){
        return SharedPreferencesUtil.getUid();
    }
    public static boolean getIsLogin(){
        return SharedPreferencesUtil.getIsLogin();
    }
}
