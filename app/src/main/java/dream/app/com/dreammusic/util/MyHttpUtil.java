package dream.app.com.dreammusic.util;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * Created by Administrator on 2015/7/5.
 */
public class MyHttpUtil {

    private HttpUtils mHttpUtil;
    private String mUrl;
    private RequestCallBack<String> mCallback;
    public static HttpUtils getDefaultHttpUtil(){
        return new HttpUtils();
    }

    public MyHttpUtil(String url){
        mHttpUtil = new HttpUtils();
        mUrl = url;
    }


    public  void send(RequestCallBack<String> callback){
        mCallback = callback;
        mHttpUtil.send(HttpRequest.HttpMethod.GET,mUrl,new RequestCallBack<String>(){

            @Override
            public void onSuccess(ResponseInfo<String> stringResponseInfo) {
                mCallback.onSuccess(stringResponseInfo);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                mCallback.onFailure(e,s);
            }
        });
    }
}
