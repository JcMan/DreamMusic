package dream.app.com.dreammusic.bmob;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2015/7/23.
 */
public class BUpdateInfo extends BmobObject{

    private double version;
    private String appurl;
    private String content;

    @Override
    public String toString() {
        return "BUpdateInfo{" +
                "version=" + version +
                ", appurl='" + appurl + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public String getAppurl() {
        return appurl;
    }

    public void setAppurl(String appurl) {
        this.appurl = appurl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getVersion() {
        return version;
    }

    public void setVersion(double version) {
        this.version = version;
    }

    public static void reverseList(List<BUpdateInfo> list){
        List<BUpdateInfo> _List = new ArrayList<BUpdateInfo>();
        for(int i=0;i<list.size();i++){
            _List.add(list.get(list.size()-1-i));
        }
        list.clear();
        for(int i=0;i<_List.size();i++){
            list.add(_List.get(i));
        }
    }


}
