package dream.app.com.dreammusic.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2015/6/26.
 */
public class ToastUtil {
    public static void showMessage(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
}
