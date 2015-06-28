package dream.app.com.dreammusic.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.animation.AlphaAnimation;

import com.lidroid.xutils.bitmap.BitmapDisplayConfig;

public class Util {
	public static BitmapDisplayConfig getConfig(Context context, int resId) {
		BitmapDisplayConfig config = new BitmapDisplayConfig();

		AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
		alphaAnimation.setDuration(300);
		config.setAnimation(alphaAnimation);
		if (resId != 0) {
			if(context != null){
				Drawable drawable = context.getResources().getDrawable(resId);
				config.setLoadingDrawable(drawable);
				config.setLoadFailedDrawable(drawable);
			}
		} else {

		}
		config.setShowOriginal(false);
		return config;
	}
}
