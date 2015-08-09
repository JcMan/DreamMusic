package dream.app.com.dreammusic.util;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import dream.app.com.dreammusic.ui.view.LoadingDialog;

/**
 * Created by Administrator on 2015/8/9.
 */
public class JsoupUtil {

    private LoadingDialog loadingDialog;
    private Activity mActivity;
    public JsoupUtil(Activity activity){
        mActivity = activity;
        loadingDialog = DialogUtil.createLoadingDialog(activity);

    }
    public void parseHtml(final String htmlUrl, final Handler handler){
        loadingDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect(htmlUrl).get();
                    Message msg = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("doc", (Parcelable) document);
                    msg.setData(bundle);
//                    msg.obj = document.toString();
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                cancelLoadingDialog();
            }
        }).start();
    }

    private void cancelLoadingDialog(){
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingDialog.cancel();
            }
        });
    }


}
