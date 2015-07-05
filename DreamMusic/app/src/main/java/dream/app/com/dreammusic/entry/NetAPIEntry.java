package dream.app.com.dreammusic.entry;

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

}

