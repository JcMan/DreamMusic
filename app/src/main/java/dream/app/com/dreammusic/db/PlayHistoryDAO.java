package dream.app.com.dreammusic.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import dream.app.com.dreammusic.model.Music;

/**
 * Created by Administrator on 2015/8/8.
 */
public class PlayHistoryDAO{

    private static final String TABLE_HISTORY = "table_history";
    private Context mContext;

    public PlayHistoryDAO(Context context){
        mContext = context;
    }

    public boolean saveHistory(Music music,int playtime){
        SQLiteDatabase db = DatabaseHelper.getInstance(mContext);
        ContentValues values = createParams(music);
        values.put("playtime", playtime);
        long id = db.insert(TABLE_HISTORY,null,values);
        return id > 0;
    }

    public List<Music> getHistory(){
        SQLiteDatabase db = DatabaseHelper.getInstance(mContext);
        String sql = "select * from "+TABLE_HISTORY;
        return parseCursor(db.rawQuery(sql,null));
    }
    private List<Music> parseCursor(Cursor cursor){
        List<Music> list = new ArrayList<Music>();
        List<Integer> time_list = new ArrayList<Integer>();
        while (cursor.moveToNext()) {
            Music music = new Music();
            music.duration = cursor.getInt(cursor
                    .getColumnIndex("duration"));
            music.songId = cursor.getInt(cursor
                    .getColumnIndex("songId"));
            music.albumId = cursor.getInt(cursor
                    .getColumnIndex("albumId"));
            music.musicName = cursor.getString(cursor
                    .getColumnIndex("musicname"));
            music.artist = cursor.getString(cursor
                    .getColumnIndex("artist"));
            String filePath = cursor.getString(cursor
                    .getColumnIndex("data"));
            music.data = filePath;
            String folderPath = filePath.substring(0,
                    filePath.lastIndexOf(File.separator));
            music.folder = folderPath;
            time_list.add(cursor.getInt(cursor.getColumnIndex("playtime")));
            list.add(music);
        }
        reverseList(list);

        for(int i=0;i<list.size();i++){
            Music music = list.get(i);
            for(int j=i+1;j<list.size();j++){
                if(music.songId==list.get(j).songId){
                    list.remove(j);
                    j--;
                }
            }
        }
        cursor.close();
        return list;
    }

    public static void reverseList(List<Music> list){
        List<Music> _List = new ArrayList<Music>();
        for(int i=0;i<list.size();i++){
            _List.add(list.get(list.size()-1-i));
        }
        list.clear();
        for(int i=0;i<_List.size();i++){
            list.add(_List.get(i));
        }
    }
    public boolean hasData() {
        SQLiteDatabase db = DatabaseHelper.getInstance(mContext);
        String sql = "select count(*) from " + TABLE_HISTORY;
        Cursor cursor = db.rawQuery(sql, null);
        boolean has = false;
        if(cursor.moveToFirst()){
            int count = cursor.getInt(0);
            if(count > 0) {
                has = true;
            }
        }
        cursor.close();
        return has;
    }

    public int getDataCount() {
        SQLiteDatabase db = DatabaseHelper.getInstance(mContext);
        String sql = "select count(*) from " + TABLE_HISTORY;
        Cursor cursor = db.rawQuery(sql, null);
        int count = 0;
        if(cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        return count;
    }

    private ContentValues createParams(Music music){
        ContentValues values = new ContentValues();
        values.put("songId",music.songId);
        values.put("musicname",music.musicName);
        values.put("albumId",music.albumId);
        values.put("duration",music.duration);
        values.put("artist",music.artist);
        values.put("data",music.data);
        values.put("folder",music.folder);
        values.put("favorite",music.favorite);
        return values;
    }

    class History{
        Music music;
        int playtime;
    }
}
