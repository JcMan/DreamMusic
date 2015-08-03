package dream.app.com.dreammusic.bmob;

import cn.bmob.v3.BmobObject;
import dream.app.com.dreammusic.model.Music;

/**
 * Created by Administrator on 2015/7/22.
 */
public class BMusicInfo extends BmobObject {

    public static final String KEY_ID = "_id";
    public static final String KEY_SONG_ID = "songid";
    public static final String KEY_ALBUM_ID = "albumid";
    public static final String KEY_DURATION = "duration";
    public static final String KEY_MUSIC_NAME = "musicname";
    public static final String KEY_ARTIST = "artist";
    public static final String KEY_DATA = "data";
    public static final String KEY_FOLDER = "folder";
    public static final String KEY_FAVORITE = "favorite";

    private String loginId;
    private String title;
    private String artist;
    private String albumid;
    private String duration;
    private String pic_singer;
    private String pic_album;
    private String favorite;
    private String lrc;

    @Override
    public String toString() {
        return "BMusicInfo{" +
                "loginId='" + loginId + '\'' +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", albumid='" + albumid + '\'' +
                ", duration='" + duration + '\'' +
                ", pic_singer='" + pic_singer + '\'' +
                ", pic_album='" + pic_album + '\'' +
                ", favorite='" + favorite + '\'' +
                ", lrc='" + lrc + '\'' +
                '}';
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbumid() {
        return albumid;
    }

    public void setAlbumid(String albumid) {
        this.albumid = albumid;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPic_album() {
        return pic_album;
    }

    public void setPic_album(String pic_album) {
        this.pic_album = pic_album;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

    public String getLrc() {
        return lrc;
    }

    public void setLrc(String lrc) {
        this.lrc = lrc;
    }

    public String getPic_singer() {
        return pic_singer;
    }

    public void setPic_singer(String pic_singer) {
        this.pic_singer = pic_singer;
    }

    public void setInfo(Music music){
        this.title = music.musicName;
        this.artist = music.artist;
        this.albumid = music.albumId+"";
        this.duration = music.duration+"";
        this.favorite = music.favorite+"";
//        this.lrc = ;
//        this.pic_album = m;
//        this.pic_singer;
    }



}
