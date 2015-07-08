package dream.app.com.dreammusic.entry;

/**
 * Created by Administrator on 2015/7/8.
 */
public class MessageEntry {
    
    public static final int TYPE_MENG = 0;
    public static final int TYPE_ME = 1;


    private int type ;
    private String content;

    public  MessageEntry(int type,String contnet){
        this.type = type;
        this.content = contnet;
    }

    public MessageEntry(){}

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
