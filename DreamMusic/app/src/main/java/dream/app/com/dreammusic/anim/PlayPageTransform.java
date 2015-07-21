package dream.app.com.dreammusic.anim;

import android.support.v4.view.ViewPager.PageTransformer;
import android.view.View;

import dream.app.com.dreammusic.config.App;

public class PlayPageTransform implements PageTransformer {

    @Override
    public void transformPage(View view, float position) {
        if(position < -1) { // [-Infinity,-1) 左边看不见了
            view.setAlpha(0.0f);
        }else if(position <= 0) { // [-1,0]左边向中间 或 中间向左边
            view.setAlpha(1 + position);
            view.setTranslationX(App.sScreenWidth * (-position));
        }else if(position <= 1) { // (0,1] 右边向中间 或 中间向右边
            view.setAlpha(1);
        }else if(position > 1) { // (1,+Infinity] 右边看不见了
            view.setAlpha(0.0f);
        }
    }
}
