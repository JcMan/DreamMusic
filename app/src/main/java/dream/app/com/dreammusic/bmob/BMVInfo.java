package dream.app.com.dreammusic.bmob;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2015/8/5.
 */
public class BMVInfo extends BmobObject {

    private String image_url;
    private String down_url;
    private String singer;
    private String title;

    public BMVInfo(String title, String singer, String down_url, String image_url) {
        this.title = title;
        this.singer = singer;
        this.down_url = down_url;
        this.image_url = image_url;
    }

    @Override
    public String toString() {
        return "BMVInfo{" +
                "image_url='" + image_url + '\'' +
                ", down_url='" + down_url + '\'' +
                ", singer='" + singer + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getDown_url() {
        return down_url;
    }

    public void setDown_url(String down_url) {
        this.down_url = down_url;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static void reverseList(List<BMVInfo> list){
        List<BMVInfo> _List = new ArrayList<BMVInfo>();
        for(int i=0;i<list.size();i++){
            _List.add(list.get(list.size()-1-i));
        }
        list.clear();
        for(int i=0;i<_List.size();i++){
            list.add(_List.get(i));
        }
    }
}
