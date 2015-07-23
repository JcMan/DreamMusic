package dream.app.com.dreammusic.bmob;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2015/7/23.
 */
public class BFeedbackInfo extends BmobObject{

    public static final String loginid = "loginId";
    public static final String USERNAME = "username";
    public static final String CONTENT = "content";
    public static final String HEADIMAGEURL = "headimageurl";
    public static final String TIME = "time";
    public static final String QQ = "qq";
    public static final String PHONE = "phone";



    private String loginId;
    private String username;
    private String content;
    private String headimageurl;
    private String qq;
    private String phone;
    private long time;


    public void setInfo(String loginid,String username,String headimageurl,String content,String qq,String phone,long time){
        this.loginId = loginid;
        this.username = username;
        this.headimageurl = headimageurl;
        this.content = content;
        this.qq = qq;
        this.phone = phone;
        this.time = time;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHeadimageurl() {
        return headimageurl;
    }

    public void setHeadimageurl(String headimageurl) {
        this.headimageurl = headimageurl;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }



}
