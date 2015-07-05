package dream.app.com.dreammusic.util;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import com.app.tool.logger.Logger;

import java.io.File;

import dream.app.com.dreammusic.config.ApplicationConfig;
import dream.app.com.dreammusic.entry.NetMusicEntry;

/**
 * Created by Administrator on 2015/7/5.
 */
public class DownLoadUtil {
    private  DownloadManager manager;
    private DownloadManager.Request mRequest;
    private String mTitle = "";
    private String mDesc = "";
    private Uri mMusicUri;
    private Uri mFileUri;
    private File mFile;
    private String mDownloadDir = ApplicationConfig.DOWNLOADDIE;

    public DownLoadUtil(Context context,String music_url,String title,String author){
        mTitle = title;
        mDesc = author;
        mFile = new File(mDownloadDir+mDesc+" - "+mTitle+".mp3");
        mFileUri = Uri.fromFile(mFile);
        mMusicUri = Uri.parse(music_url);
        manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    public DownLoadUtil(Context context,NetMusicEntry entry){
        mTitle = entry.getTitle();
        mDesc = entry.getAuthor();
        mFile = new File(mDownloadDir+"/"+mDesc+" - "+mTitle+".mp3");
        mFileUri = Uri.fromFile(mFile);
        mMusicUri = Uri.parse(entry.getFile_link());
        manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
    }

   /* public DownloadUtil(Context context){
        mContext = context;
        manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    public void setTitle(String title){
        mTitle = title;
    }

    public void setAuthor(String author){
        mDesc = author;
    }*/

    public  long download(){
        if(mFile.exists())
            mFile.delete();
        else{

        }
        return manager.enqueue(getRequest());
    }

    private DownloadManager.Request getRequest(){
        mRequest = new DownloadManager.Request(mMusicUri);
        mRequest.setTitle(mTitle);
        mRequest.setDescription(mDesc);
        mRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        mRequest.setDestinationUri(mFileUri);
        mRequest.allowScanningByMediaScanner();
        return mRequest;
    }
}
