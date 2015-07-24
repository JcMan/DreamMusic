/**
 * Copyright (c) www.longdw.com
 */
package dream.app.com.dreammusic.util;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dream.app.com.dreammusic.config.ApplicationConfig;
import dream.app.com.dreammusic.model.Music;


/**
 * 查询各主页信息，获取封面图片等
 * @author longdw(longdawei1988@gmail.com)
 *
 */
public class MusicUtil  {

    private static String[] proj_music = new String[] {
            MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ARTIST_ID,
            MediaStore.Audio.Media.DURATION };

    public static final int FILTER_SIZE = 1 * 1024 * 1024;// 1MB
    public static final int FILTER_DURATION = 1 * 60 * 1000;// 1分钟




    /**
     *
     * @param context
     * @return
     */
    public static List<Music> queryLocalMusic(Context context) {
        return queryMusic(context, null, null);
    }

    private static List<Music> queryMusic(Context context, String selections, String selection) {
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        ContentResolver cr = context.getContentResolver();
        List<Music> list = getMusicList(cr.query(uri, proj_music, null, null,MediaStore.Audio.Media.ARTIST_KEY));
        return list;
    }

    private static ArrayList<Music> getMusicList(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        ArrayList<Music> musicList = new ArrayList<Music>();
        while (cursor.moveToNext()) {
            Music music = new Music();
            music.duration = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DURATION));
            music.songId = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Audio.Media._ID));

            music.albumId = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
            music.musicName = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.TITLE));
            music.artist = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.ARTIST));
            String filePath = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DATA));
            music.data = filePath;
            String folderPath = filePath.substring(0,
                    filePath.lastIndexOf(File.separator));
            music.folder = folderPath;
            double fileSize = StringUtil.getFileSizeDouble(music.data);
            if(fileSize>0.3&&music.duration > FILTER_DURATION)
                musicList.add(music);
        }
        cursor.close();
        return musicList;
    }

    public static int getLocalMusicNumber(Context context){
        List<Music> _List = queryLocalMusic(context);
        if(_List!=null)
            return _List.size();
        return 0;
    }
    public static String makeTimeString(long milliSecs) {
        StringBuffer sb = new StringBuffer();
        long m = milliSecs / (60 * 1000);
        sb.append(m < 10 ? "0" + m : m);
        sb.append(":");
        long s = (milliSecs % (60 * 1000)) / 1000;
        sb.append(s < 10 ? "0" + s : s);
        return sb.toString();
    }

    public static String[] getMusicName(String fullname){
        String _S[] = fullname.split("-");
        return _S;
    }

    /**
     * 查询指定文件夹的歌曲
     * @param context
     * @param folder
     * @return
     */
    public static List<Music> getMusicListByFolder(Context context,String folder){
        List<Music> _List = queryLocalMusic(context);
        List<Music> list = new ArrayList<Music>();
        for(int i=0;i<_List.size();i++){
            Music music = _List.get(i);
            String _Folder = music.folder+"/";
            if(_Folder.equals(folder)) {
                list.add(music);
            }
        }
        return list;
    }

    public static String getLrcPath(int songid){
        return ApplicationConfig.LRC_DIR+songid+".lrc";
    }
    public static boolean saveLrcFile(List<String> pList ,String musicName){
        if(pList.size()<20)
            return false;
        try {
            FileWriter fw = new FileWriter(ApplicationConfig.LRC_DIR+musicName+".lrc");
            for(int i=0;i<pList.size();i++){
                fw.write(pList.get(i));
                fw.write("\n");
            }
            fw.close();
            return true;
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return false;
    }
    public static void clearList(List pList){
        if(pList!=null&&pList.size()>0)
            pList.clear();
    }

    public static Bitmap getMusicBitemp(Context context, long songid,long albumid)
    {
        Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");

        Bitmap bm = null;
        // 专辑id和歌曲id小于0说明没有专辑、歌曲，并抛出异常
        if (albumid < 0 && songid < 0) {
            throw new IllegalArgumentException("Must specify an album or a song id");
        }
        try {
            if (albumid < 0){
                Uri uri = Uri.parse("content://media/external/audio/media/"+ songid + "/albumart");
                ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
                if (pfd != null) {
                    FileDescriptor fd = pfd.getFileDescriptor();
                    bm = BitmapFactory.decodeFileDescriptor(fd);
                }
            } else {
                Uri uri = ContentUris.withAppendedId(sArtworkUri, albumid);
                ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
                if (pfd != null) {
                    FileDescriptor fd = pfd.getFileDescriptor();
                    bm = BitmapFactory.decodeFileDescriptor(fd);
                } else {
                    return null;
                }
            }
        } catch (FileNotFoundException ex) {}
        return bm;
    }
}
