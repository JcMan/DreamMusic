package dream.app.com.dreammusic.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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

    public static LoadingDialog createLoadingDialog(Context context){
        final LoadingDialog dialog = new LoadingDialog(context,R.layout.layout_loading_dialog,R.style.Theme_loading_dialog);
        dialog.setCanceledOnTouchOutside(false);
        TextView textView = (TextView) dialog.findViewById(R.id.tv_loading_msg);
        textView.setText("加载中···");
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

    public static void showMessageDialog(Activity activity,String msg){
        final Dialog dialog = new Dialog(activity,R.style.Theme_loading_dialog);
        View _View = View.inflate(activity,R.layout.dialog_message,null);
        TextView tv_content = (TextView) _View.findViewById(R.id.tv_dialog_msg_content);
        tv_content.setText(msg);
        Button btn = (Button) _View.findViewById(R.id.btn_dialog_message_ok);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(_View);
        setDialogAttr(dialog, activity);
        dialog.show();
    }
}
