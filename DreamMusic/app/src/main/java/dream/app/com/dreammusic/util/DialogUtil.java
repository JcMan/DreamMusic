package dream.app.com.dreammusic.util;

import android.content.Context;
import android.os.Handler;
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
        },1000*5);
        return dialog;
    }
}
