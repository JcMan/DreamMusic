/**
 * Copyright (c) www.longdw.com
 */
package dream.app.com.dreammusic.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
            if(music.duration<FILTER_DURATION){
                continue;
            }
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
}
