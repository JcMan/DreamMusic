package dream.app.com.dreammusic.ui.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.ImageView;

import net.tsz.afinal.FinalBitmap;

import java.util.Random;
import java.util.logging.MemoryHandler;

import dream.app.com.dreammusic.MainActivity;
import dream.app.com.dreammusic.R;

/**
 * Created by Administrator on 2015/6/29.
 */
public class WelcomeActivity extends BaseActivity implements Handler.Callback{

    private static final int MESSAGE_LOADiMAGE = 1;
    private static final int MESSAGE_GOTO_MAIN = 0;

    private static final String imageUrls[] = new String[10];
    private static final int[] beautis = new int[]{R.drawable.ic_beauty_0,R.drawable.ic_beauty_1,R.drawable.ic_beauty_2,R.drawable.ic_beauty_3,
            R.drawable.ic_beauty_4,R.drawable.ic_beauty_5,R.drawable.ic_beauty_6,R.drawable.ic_beauty_7,};

    private Handler mHandler;
    private ImageView mWelcomeView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initImageUrl();
        mWelcomeView = (ImageView) findViewById(R.id.iv_welcome);
        mHandler = new Handler(this);
        mHandler.sendEmptyMessageDelayed(MESSAGE_LOADiMAGE,2000);
        mHandler.sendEmptyMessageDelayed(MESSAGE_GOTO_MAIN,5000);
    }

    private void initImageUrl() {
        imageUrls[0] = "http://h.hiphotos.baidu.com/image/pic/item/a8773912b31bb051f76371c4327adab44bede093.jpg";
        imageUrls[1] = "http://img.pconline.com.cn/images/upload/upc/tx/wallpaper/1307/19/c0/23509344_1374219743119_320x480.jpg";
        imageUrls[2] = "http://img5.imgtn.bdimg.com/it/u=3166118298,3243473625&fm=21&gp=0.jpg";
        imageUrls[3] = "http://a.hiphotos.baidu.com/image/pic/item/ac6eddc451da81cb1939032b5666d0160824317c.jpg";
        imageUrls[4] = "http://g.hiphotos.baidu.com/image/pic/item/35a85edf8db1cb13af1abcf5d954564e93584be1.jpg";
        imageUrls[5] = "http://b.hiphotos.baidu.com/image/pic/item/42166d224f4a20a4c773bc2194529822730ed0e3.jpg";
        imageUrls[6] = "http://g.hiphotos.baidu.com/image/pic/item/35a85edf8db1cb13afb2bcf5d954564e93584b79.jpg";
        imageUrls[6] = "http://h.hiphotos.baidu.com/image/pic/item/9a504fc2d5628535ba1fc30394ef76c6a7ef6310.jpg";
        imageUrls[7] = "http://h.hiphotos.baidu.com/image/pic/item/eac4b74543a9822618233a058e82b9014a90eb36.jpg";
        imageUrls[8] = "http://d.hiphotos.baidu.com/image/pic/item/e4dde71190ef76c6c418ea099916fdfaae5167cd.jpg";
        imageUrls[9] = "imahttp://d.hiphotos.baidu.com/image/pic/item/94cad1c8a786c917ecde431bcd3d70cf3bc75736.jpg";
    }

    @Override
    public boolean handleMessage(Message msg){
        if(msg.what==MESSAGE_GOTO_MAIN){
//            startNewActivity(MainActivity.class,R.anim.base_slide_right_in,R.anim.base_slide_left_out);
            startNewActivity(MainActivity.class);
            finish();
        }else if(msg.what==MESSAGE_LOADiMAGE){
            Random random = new Random();
            int index = Math.abs(random.nextInt(8));
            mWelcomeView.setImageResource(beautis[index]);
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        return false;
    }
}
