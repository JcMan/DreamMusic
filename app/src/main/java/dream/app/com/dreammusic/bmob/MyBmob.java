package dream.app.com.dreammusic.bmob;

/**
 * Created by Administrator on 2015/7/23.
 */
public class MyBmob {

    public static String getFileUrl(String filename){
        String url = "";
        url+="http://testAPi.bmob.cn/";
        url+=filename;
        url+="?t=2&a=a7949946ad1e548ad4976cc67cbd49d8&";
        url+=("e="+(System.currentTimeMillis()/1000)+"&token=");
        return  url;
    }
}
