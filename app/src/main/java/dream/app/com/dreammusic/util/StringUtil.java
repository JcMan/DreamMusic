package dream.app.com.dreammusic.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Created by Administrator on 2015/7/10.
 */
public class StringUtil {
    public static String getFileSizeString(String path){
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

    public static double getFileSizeDouble(String path){
        File file = new File(path);
        double size = 0;
        if(file.isFile()&&file.exists()){
            FileInputStream in = null;
            try {
                in = new FileInputStream(file);
                size = in.available()/(1024*1024*1.0);
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        return size;
    }

    public static String getTimeDesc(long second){
        String s = "";
        long min = second/60;
        long hour = min/60;
        long day = hour/24;
        long month = day/30;
        long year = month/12;
        if(min>0&&min<60)
            s+=(min+"分钟前");
        else if(hour>0&&hour<24){
            s+=(hour+"小时前");
        }else if(day>0&day<30){
            s+=(day+"天前");
        }else if(month>0&&month<12){
            s+=(month+"个月前");
        }else if(year>0)
            s+=(year+"年前");
        else
            s="刚刚";
        return s;
    }
}
