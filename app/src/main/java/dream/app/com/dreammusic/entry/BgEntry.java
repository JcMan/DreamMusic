package dream.app.com.dreammusic.entry;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;

import dream.app.com.dreammusic.R;
import dream.app.com.dreammusic.util.SharedPreferencesUtil;

public class BgEntry {
		public  Bitmap bitmap;
		public  String path;

    public static Bitmap getDefaultBg(Context context){
        Bitmap _Bitmap = null;
        _Bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_bg_main);
        AssetManager am = context.getAssets();
        String path = SharedPreferencesUtil.getDefaultBgPath();
        try {
            InputStream is = am.open("bkgs/"+path);
            _Bitmap = BitmapFactory.decodeStream(is);
        } catch (IOException e) {}

        return _Bitmap;
    }
}