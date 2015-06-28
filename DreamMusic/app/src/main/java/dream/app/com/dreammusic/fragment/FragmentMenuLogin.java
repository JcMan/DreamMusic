package dream.app.com.dreammusic.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;

import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.entry.UserEntry;
import dream.app.com.dreammusic.util.ThirdPlatformLoginUtil;

/**
 * Created by Administrator on 2015/6/27.
 */
public class FragmentMenuLogin extends android.app.Fragment implements View.OnClickListener{
    private ImageButton mLoginQQ,mLoginSina,mLoginRenRen;
    private LoginListener mLoginListener;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            try {
                JSONObject object = new JSONObject(msg.obj.toString());
                if(object.getString(UserEntry.USERNAME)!=null&&object.getString(UserEntry.USERNAME).length()>1){
                    mLoginListener.loginSuccess(object);
                }
                else
                    mLoginListener.loginFailure(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_third_platform,container,false);
        mLoginQQ = (ImageButton) view.findViewById(R.id.ib_login_qq);
        mLoginRenRen = (ImageButton) view.findViewById(R.id.ib_login_renren);
        mLoginSina = (ImageButton) view.findViewById(R.id.ib_login_sina);
        mLoginQQ.setOnClickListener(this);
        mLoginRenRen.setOnClickListener(this);
        mLoginSina.setOnClickListener(this);
        return view;
    }

    public interface LoginListener{
        public abstract void loginSuccess(JSONObject object) throws JSONException;
        public abstract  void loginFailure(JSONObject object);
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.ib_login_qq:
                ThirdPlatformLoginUtil.login(SHARE_MEDIA.QQ, mHandler);
                break;
            case  R.id.ib_login_renren:
                ThirdPlatformLoginUtil.login(SHARE_MEDIA.RENREN,mHandler);
                break;
            case R.id.ib_login_sina:
                ThirdPlatformLoginUtil.login(SHARE_MEDIA.SINA,mHandler);
                break;
        }
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mLoginListener = (LoginListener) activity;
    }
}
