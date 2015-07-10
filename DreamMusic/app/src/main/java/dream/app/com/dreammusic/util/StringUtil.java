package dream.app.com.dreammusic.util;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;

/**
 * Created by Administrator on 2015/7/10.
 */
public class StringUtil {
    public static String getFileSize(String path){
        FileInputStream in = null;
        double size = 0;
        try {
             in = new FileInputStream(new File(path));
            size = in.available()/(1024*1024*1.0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        DecimalFormat format = new DecimalFormat("0.0");
        String s = format.format(size);
        return s;
    }
}
