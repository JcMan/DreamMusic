package dream.app.com.dreammusic.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.ui.view.LoadingDialog;

public class DialogUtil {

    private static Context mContext;
    public DialogUtil(Context context){
        mContext = context;
    }
    public static LoadingDialog createLoadingDialog(Context context,String msg){
        final LoadingDialog dialog = new LoadingDialog(context,R.layout.layout_loading_dialog,R.style.Theme_loading_dialog);
        dialog.setCanceledOnTouchOutside(false);
        TextView textView = (TextView) dialog.findViewById(R.id.tv_loading_msg);
        textView.setText(msg);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run(){
                dialog.cancel();
            }
        },1000*60);
        return dialog;
    }

    public static Dialog createFeiMengDialog(Context context,int style){
        Dialog dialog = new Dialog(context,style);
        return  dialog;
    }

    public static void setDialogAttr(Dialog dialog,Activity activity) {
        Window dialogWindow = dialog.getWindow();
        WindowManager manager = activity.getWindowManager();
        Display d = manager.getDefaultDisplay();
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        params.width = (int)(d.getWidth()*0.8);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(params);
    }
}
