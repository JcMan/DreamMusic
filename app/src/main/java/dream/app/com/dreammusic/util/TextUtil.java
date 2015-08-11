package dream.app.com.dreammusic.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import dream.app.com.dreammusic.config.ApplicationConfig;
import dream.app.com.dreammusic.entry.NovelAPI;

/**
 * Created by Administrator on 2015/7/5.
 */
public class TextUtil {
    public static final String LOADINGNOW = "加载中···";

    public static void writeTextToSD(String path ,String content){
        File file = new File(path);
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(content);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeNovelContent(String name,String content){
        String path = ApplicationConfig.NOVEL_DIR+"/temp.txt";
        content = content.replace(" ","\n\n        ");
        String s = name+"\n\n"+content;
        writeTextToSD(path,s);
    }
}
