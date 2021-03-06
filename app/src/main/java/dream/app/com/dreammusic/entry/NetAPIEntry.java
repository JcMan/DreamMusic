package dream.app.com.dreammusic.entry;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2015/7/3.
 */
public class NetAPIEntry {
    private static final String  NETNEWMUSCIURL= "http://tingapi.ting.baidu.com/v1/restserver/ting?" +
            "from=qianqian&version=2.1.0&method=baidu.ting.billboard.billList&format=json&type=1&offset=0&size=100";

    public static final String NETHOTMUSCIURL = "http://tingapi.ting.baidu.com/v1/restserver/ting?" +
            "from=qianqian&version=2.1.0&method=baidu.ting.billboard.billList&format=json&type=2&offset=0&size=100";

    public static final String NETKTVMUSICURL = "http://tingapi.ting.baidu.com/v1/restserver/ting?" +
            "from=qianqian&version=2.1.0&method=baidu.ting.billboard.billList&format=json&type=6&offset=0&size=100";

    public static final String NETRADIOLISTURL = "http://tingapi.ting.baidu.com/v1/restserver/ting?" +
            "from=qianqian&version=2.1.0&method=baidu.ting.radio.getCategoryList&format=json";

    public static final String NETSINGERLIST = "http://tingapi.ting.baidu.com/v1/restserver/ting?" +
            "from=qianqian&version=2.1.0&method=baidu.ting.artist.get72HotArtist&format=json?=1&offset=0&limit=100";

    public static final String NETHITOMUSICURL = "http://tingapi.ting.baidu.com/v1/restserver/ting?" +
            "from=qianqian&version=2.1.0&method=baidu.ting.billboard.billList&format=json&type=18&offset=0&size=50";

    private static final String SONGIDURL = "http://tingapi.ting.baidu.com/v1/restserver/ting?" +
            "from=qianqian&version=2.1.0&method=baidu.ting.song.getInfos&format=json&songid=JcMan&ts=1408284347323&e=JoN56kTXnnbEpd9MVczkYJCSx%2FE1mkLx%2BPMIkTcOEu4%3D&nw=2&ucf=1&res=1";
    public static final String SINGERINFOURL = "http://tingapi.ting.baidu.com/v1/restserver/ting?" +
            "from=qianqian&version=2.1.0&method=baidu.ting.artist.getinfo&format=json&tinguid=";

    public static final String MUSICOFSINGER = "http://tingapi.ting.baidu.com/v1/restserver/ting?" +
            "from=qianqian&version=2.1.0&method=baidu.ting.artist.getSongList&format=json?=2&tinguid=JcMan&offset=0&limits=100";
    public static final String CHANNLEMUSICLISTURL = "http://tingapi.ting.baidu.com/v1/restserver/ting?" +
            "from=qianqian&version=2.1.0&method=baidu.ting.radio.getChannelSong&format=json&pn=0&rn=100&channelname=";

    public static final String SEARCHURL = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.search.common&format=json&query=JcMan&page_no=1&page_size=100";

    public static final String PIC_URL = "http://image.baidu.com/i?tn=baiduimagejson&ie=utf-8&ic=0&rn=20&pn=1&width=200&height=200&word=";

    public static final String TING_UID_URL = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.search.common&format=json&query=JcMan&page_no=1&page_size=30";

    public static final String ALBUM_URL = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.album.getAlbumInfo&format=json&album_id=";

    public static final String SINGER_IMAGE_URL = "http://avatar.mobile.kugou.com/api/v1/singer/photopreview?singer=";


    public static String getNewMusicUrl(){
        return NETNEWMUSCIURL;
    }
    public static String getHotMusicUrl(){
        return NETHOTMUSCIURL;
    }
    public static String getKtvMusicUrl(){
        return NETKTVMUSICURL;
    }
    public static String getRadioListUrl(){
        return NETRADIOLISTURL;
    }
    public static String getSingerListUrl(){
        return NETSINGERLIST;
    }
    public static String getHitoMusicUrl(){
        return NETHITOMUSICURL;
    }
    public static String getUrlBySongId(String songId){
         return SONGIDURL.replace("JcMan",songId);
    }
    public static String getSingerInfoUrlByTing_uid(String ting_uid){
        return SINGERINFOURL+ting_uid;
    }

    public static String getMusicOfSingerUrlByTingUid(String uid){
        return MUSICOFSINGER.replace("JcMan",uid);
    }

    public static String getChannelMusicListUrlByCh_Name(String ch_name){
        return CHANNLEMUSICLISTURL+ch_name;
    }

    public static String getSearchUrl(String query){
        return SEARCHURL.replace("JcMan",query);
    }

    public static String getPicUrlBySinger(String singer){
        try {
            return PIC_URL+ URLEncoder.encode(singer,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getTingUidUrl(String singer){
        try {
            return TING_UID_URL.replace("JcMan",URLEncoder.encode(singer,"utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getAlbumInfoUrl(String albumid){
        return ALBUM_URL+albumid;
    }

    public static String getSingerImageUrl(String singer){
        try {
            singer = URLEncoder.encode(singer,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return SINGER_IMAGE_URL+singer;
    }

    public static String getMVUrl(String key){
        String s = "http://mapi.yinyuetai.com/search/video.json?D-A=0&offset=0&size=50" +
                "&deviceinfo=%7B%22aid%22%3A%2210201027%22%2C%22os%22%3A%22Android%22%2C%22ov%22%3A%224.3%22%2C%22rn%22%3A%22480*800%22%2C%22dn%22%3A%22Coolpad%208702%22%2C%22cr%22%3A%2246002%22%2C%22as%22%3A%22WIFI%22%2C%22uid%22%3A%22d867915aa4c1762406eb5224a5eaa0ba%22%2C%22clid%22%3A110004000%7D&keyword=";
        try {
            key = URLEncoder.encode(key,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        s+=key;
        return s;
    }
}

