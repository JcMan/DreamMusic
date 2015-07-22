package dream.app.com.dreammusic.entry;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2015/7/22.
 */
public class UserBean extends BmobObject {

    public static final String LOGINID = "loginId";
    public static final String PASSWORD = "password";

    private static final long serialVersionUID = 1L;
    private String loginId;
    private String userName;
    private String password;
    public String getLoginId() {
        return loginId;
    }
    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    @Override
    public String toString() {
        return "UserBean [loginId=" + loginId + ", userName=" + userName
                + ", password=" + password + "]";
    }
}
