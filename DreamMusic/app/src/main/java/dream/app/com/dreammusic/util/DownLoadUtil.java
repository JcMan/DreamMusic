package dream.app.com.dreammusic.util;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;

import java.io.File;
import java.io.FileNotFoundException;

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
    private Uri mPicUri;
    private String mDownloadDir = ApplicationConfig.DOWNLOADDIE;
    private NetMusicEntry mEntry;
    private Context mContext;


    public DownLoadUtil(Context context,String music_url,String title,String author){
        mTitle = title;
        mDesc = author;
        mFile = new File(mDownloadDir+mDesc+" - "+mTitle+".mp3");
        mFileUri = Uri.fromFile(mFile);
        mMusicUri = Uri.parse(music_url);
        manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    public DownLoadUtil(Context context,NetMusicEntry entry){
        mEntry = entry;
        mTitle = entry.getTitle();
        mDesc = entry.getAuthor();
        mFile = new File(mDownloadDir+"/"+mDesc+" - "+mTitle+".mp3");
        mFileUri = Uri.fromFile(mFile);
        mMusicUri = Uri.parse(entry.getFile_link());
        manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        try{
            mPicUri = Uri.parse(entry.getPic_big());
        }catch(Exception e){}
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

    public void downloadFile(String url,String name){
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle(name);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationUri(Uri.fromFile(new File(ApplicationConfig.ROOT_PATH+"/"+name)));
        manager.enqueue(request);
    }
    public void downloadFile(String url,String name,boolean isShow){
        File file = new File(ApplicationConfig.ROOT_PATH+"/"+name);
        if (file.exists())
            file.delete();
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle(name);
        if(isShow)
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationUri(Uri.fromFile(file));
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        final long myDownloadReference = manager.enqueue(request);

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (myDownloadReference == reference) {
                    try {
                        manager.openDownloadedFile(reference);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        mContext.registerReceiver(receiver, filter);

    }

    /**
     * 下载完成直接打开文件
     * @param url
     * @param name
     * @param isopen
     */
    public void downloadFile(String url, final String name,int isopen){
        final File file = new File(ApplicationConfig.ROOT_PATH+"/"+name);
        if (file.exists())
            file.delete();
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle(name);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationUri(Uri.fromFile(file));
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        final long myDownloadReference = manager.enqueue(request);
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (myDownloadReference == reference){
                    Intent i = new Intent();
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.setAction(Intent.ACTION_VIEW);
                    String type = getFileType(file);
                    i.setDataAndType(Uri.fromFile(file),type);
                    if(type.equals("application/vnd.android.package-archive"))
                        mContext.startActivity(i);
                }
            }
        };
        if(isopen==1)
            mContext.registerReceiver(receiver, filter);
    }
    public DownLoadUtil(Context context){
        mContext = context;
        manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    /**
     * 获取文件类型
     */
    private String getFileType(File file){
        String type="";
        String fName=file.getName();
        String end=fName.substring(fName.lastIndexOf(".")+1,fName.length()).toLowerCase();

        if(end.equals("m4a")||end.equals("mp3")||end.equals("mid")||end.equals("xmf")||end.equals("ogg")||end.equals("wav"))
            type = "audio";
        else if(end.equals("3gp")||end.equals("mp4"))
            type = "video";
        else if(end.equals("jpg")||end.equals("gif")||end.equals("png")||end.equals("jpeg")||end.equals("bmp"))
            type = "image";
        else if(end.equals("apk"))
            type = "application/vnd.android.package-archive";
        else if(end.equals("txt")||end.equals("java"))
            type = "text";
        else
            type="*";

        if(end.equals("apk")) {}
        else
            type += "/*";
        return type;
    }
}
