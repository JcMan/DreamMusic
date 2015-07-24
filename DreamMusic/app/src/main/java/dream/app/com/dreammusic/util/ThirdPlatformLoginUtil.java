package dream.app.com.dreammusic.util;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;

import com.app.tool.logger.Logger;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.UMQQSsoHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import dream.app.com.dreammusic.config.ApplicationConfig;
import dream.app.com.dreammusic.entry.UserEntry;

/**
 * Created by JcMan on 2015/6/26.
 */
public class ThirdPlatformLoginUtil {
    private static Activity mActivity;
    private static UMSocialService mControler;
    private static android.os.Handler mHandler;
    public static void init(Activity activity){
        mActivity = activity;
        mControler = UMServiceFactory.getUMSocialService("com.umeng.share");
        String appId = "100424468";
        String appKey = "c7394704798a158208a74ab60104f0ba";
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(activity,
                appId, appKey);
        qqSsoHandler.setTargetUrl("http://www.umeng.com");
        qqSsoHandler.addToSocialSDK();
    }

    public static void login(SHARE_MEDIA platform, android.os.Handler handler){
        mHandler = handler;
        mControler.doOauthVerify(mActivity, platform,uAuthListener);
    }

    public static  void getUserInfo(SHARE_MEDIA platform) {
        mControler.getPlatformInfo(mActivity, platform,uDataListener);
    }

    private static SocializeListeners.UMAuthListener uAuthListener = new SocializeListeners.UMAuthListener() {

        @Override
        public void onStart(SHARE_MEDIA arg0) {

        }
        @Override
        public void onError(SocializeException arg0, SHARE_MEDIA arg1){

        }
        @Override
        public void onComplete(Bundle arg0, SHARE_MEDIA arg1){
            String uid = arg0.getString("uid");
            if(uid!=null)
                getUserInfo(arg1);
        }
        @Override
        public void onCancel(SHARE_MEDIA arg0){

        }
    };
    private static SocializeListeners.UMDataListener uDataListener = new SocializeListeners.UMDataListener(){
        @Override
        public void onStart() {

        }
        @Override
        public void onComplete(int arg0, Map<String, Object> arg1){
            Message msg = new Message();
            JSONObject object = new JSONObject();
            try {
                String name = (String) arg1.get("screen_name");
                String url = (String) arg1.get("profile_image_url");
                String uid = (String) (arg1.get("uid")+"");
                object.put("UserName",name);
                object.put("HeadImage",url);
                object.put("uid",uid);
                Logger.e(arg1.toString());
                saveUserInfo(name, url,uid);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            msg.obj = object.toString();
            mHandler.sendMessage(msg);
        }

        /**
         * 保存用户登录信息
         * @param name
         * @param url
         */
        private void saveUserInfo(String name, String url,String uid){
            SharedPreferences.Editor editor = SharedPreferencesUtil.getEditor(ApplicationConfig.USER);
            editor.putString(UserEntry.USERNAME,name);
            editor.putString(UserEntry.HEADIMAGE,url);
            editor.putString(UserEntry.UID,uid);
            editor.putBoolean(UserEntry.LOGIN,true);
            editor.commit();
        }
    };
}
