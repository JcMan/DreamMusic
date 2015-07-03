package dream.app.com.dreammusic.entry;

/**
 * Created by Administrator on 2015/7/3.
 */
public class NetAPIEntry {
    private static final String  NETNEWMUSCIURL= "http://tingapi.ting.baidu.com/v1/restserver/ting?" +
            "from=qianqian&version=2.1.0&method=baidu.ting.billboard.billList&format=json&type=1&offset=0&size=50";

    public static final String NETHOTMUSCIURL = "http://tingapi.ting.baidu.com/v1/restserver/ting?" +
            "from=qianqian&version=2.1.0&method=baidu.ting.billboard.billList&format=json&type=2&offset=0&size=";

    public static String getNewMusicUrl(){
        return NETNEWMUSCIURL;
    }

    public static String getHotMusicUrl(){
        return NETNEWMUSCIURL;
    }
}
