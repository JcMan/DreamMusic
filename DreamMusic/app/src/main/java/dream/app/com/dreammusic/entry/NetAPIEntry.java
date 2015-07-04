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

}

