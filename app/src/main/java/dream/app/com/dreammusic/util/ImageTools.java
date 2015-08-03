package dream.app.com.dreammusic.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * liteplayer by loader
 * @author qibin
 */
public class ImageTools {


    /**
     * 缩放图片
     * @param bmp
     * @param size
     * @return
     */
    public static Bitmap scaleBitmap(Bitmap bmp, int size) {
        return Bitmap.createScaledBitmap(bmp, size, size, true);
    }

    /**
     * 根据文件uri缩放图片
     * @param uri
     * @return
     */
    public static Bitmap scaleBitmap(String uri, int size) {
        return scaleBitmap(BitmapFactory.decodeFile(uri), size);
    }

}
