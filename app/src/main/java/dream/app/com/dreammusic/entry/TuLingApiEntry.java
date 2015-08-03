package dream.app.com.dreammusic.entry;

/**
 * Created by Administrator on 2015/7/8.
 */
public class TuLingApiEntry {
    private static final String TULING_URL= "http://www.tuling123.com/openapi/api?key=30c76f7e9ecf83670a130a8e403b3936&info=";

    public static String getTuLingUrl(String info){
        return TULING_URL+info;
    }
}
