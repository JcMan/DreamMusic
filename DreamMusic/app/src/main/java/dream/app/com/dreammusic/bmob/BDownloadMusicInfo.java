package dream.app.com.dreammusic.bmob;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2015/7/26.
 */
public class BDownloadMusicInfo extends BmobObject{

    public static final String LOGINID = "loginId";
    public static final String USERNAME = "username";
    public static final String TITLE = "title";
    public static final String SINGER = "singer";
    public static final String FILE_URL = "file_url";
    public static final String PIC_BIG = "pic_big";
    public static final String DOWNLOADTIME = "downloadtime";
    public static final String SHARE = "share";


    private String loginId;
    private String username;
    private String title;
    private String singer;
    private String file_url;
    private String pic_big;
    private String downloadtime;



    private String desc;
    private boolean share;


    public BDownloadMusicInfo(String loginId, String username, String title, String singer, String file_url, String pic_big, String downloadtime) {
        this.loginId = loginId;
        this.username = username;
        this.title = title;
        this.singer = singer;
        this.file_url = file_url;
        this.pic_big = pic_big;
        this.downloadtime = downloadtime;
        this.share = false;
        this.desc = "";
    }

    public BDownloadMusicInfo(){}

    @Override
    public String toString() {
        return "BDownloadMusicInfo{" +
                "loginId='" + loginId + '\'' +
                ", username='" + username + '\'' +
                ", title='" + title + '\'' +
                ", singer='" + singer + '\'' +
                ", file_url='" + file_url + '\'' +
                ", pic_big='" + pic_big + '\'' +
                ", downloadtime='" + downloadtime + '\'' +
                '}';
    }

    public boolean isShare() {
        return share;
    }

    public void setShare(boolean share) {
        this.share = share;
    }
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public String getPic_big() {
        return pic_big;
    }

    public void setPic_big(String pic_big) {
        this.pic_big = pic_big;
    }

    public String getDownloadtime() {
        return downloadtime;
    }

    public void setDownloadtime(String downloadtime) {
        this.downloadtime = downloadtime;
    }




}
