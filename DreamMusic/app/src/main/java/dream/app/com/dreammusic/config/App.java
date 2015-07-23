package dream.app.com.dreammusic.config;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by Administrator on 2015/7/20.
 */
public class App extends Application {
    public static Context sContext;
    public static int sScreenWidth;
    public static int sScreenHeight;
    public static String sAppName;
    public static String sAppName_English;


    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        sScreenWidth = dm.widthPixels;
        sScreenHeight = dm.heightPixels;
        sAppName = "飞梦音乐";
        sAppName_English = "dreammusic";
    }

    public static double getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return Double.parseDouble(version);
        } catch (Exception e) {
            e.printStackTrace();
            return 1.0;
        }
    }
}
