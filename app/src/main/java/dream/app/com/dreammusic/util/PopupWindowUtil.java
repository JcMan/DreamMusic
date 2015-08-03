package dream.app.com.dreammusic.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import dream.app.com.dreammusic.R;

/**
 * Created by Administrator on 2015/7/5.
 */
public class PopupWindowUtil {
    private Activity mActivity;
    private static  PopupWindow popupWindow;
    public static PopupWindow createPopupWindow(Activity activity,int layout){
        View popView = View.inflate(activity,layout,null);
        popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setAnimationStyle(R.style.popwin_anim);
        popupWindow.setFocusable(true);
        return popupWindow;
    }

    public PopupWindow getPopupWindow() {
        return popupWindow;
    }
}
