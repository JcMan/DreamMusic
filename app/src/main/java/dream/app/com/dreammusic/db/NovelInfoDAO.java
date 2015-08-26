package dream.app.com.dreammusic.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import dream.app.com.dreammusic.entry.NovelEntry;
import dream.app.com.dreammusic.model.Music;

/**
 * Created by Administrator on 2015/8/12.
 */
public class NovelInfoDAO {

    private static final String TABLE_NOVEL = "table_novel";
    private Context mContext;

    public NovelInfoDAO(Context context){
        mContext = context;
    }

    public boolean addNovelInfo(NovelEntry entry){
        SQLiteDatabase db = DatabaseHelper.getInstance(mContext);
        ContentValues values = createValues(entry);
        long id = db.insert(TABLE_NOVEL,null,values);
        return id > 0;
    }

    public boolean deleteNovelInfo(String bookName){
        SQLiteDatabase db = DatabaseHelper.getInstance(mContext);
        return db.delete(TABLE_NOVEL,"1=1 And bookname = '"+bookName+"'",null) > 0;
    }

    private ContentValues createValues(NovelEntry entry) {
        ContentValues values = new ContentValues();
        values.put("bookname",entry.getmBookName());
        values.put("author",entry.getmAuthor());
        values.put("imgurl",entry.getmImgUrl());
        values.put("mainpageurl",entry.getmMainPageUrl());
        values.put("baseurl",entry.getmBaseUrl());
        values.put("lastchapter",entry.getmLastChapter());
        return values;
    }

    public List<NovelEntry> getNovels(){
        SQLiteDatabase db = DatabaseHelper.getInstance(mContext);
        String sql = "select * from "+TABLE_NOVEL;
        return parseCursor(db.rawQuery(sql,null));
    }

    private List<NovelEntry> parseCursor(Cursor cursor){
        List<NovelEntry> _List =  new ArrayList<NovelEntry>();
        while (cursor.moveToNext()){
            NovelEntry entry = new NovelEntry();
            String bookname = cursor.getString(cursor.getColumnIndex("bookname"));
            String author = cursor.getString(cursor.getColumnIndex("author"));
            String imgurl = cursor.getString(cursor.getColumnIndex("imgurl"));
            String mainpageurl = cursor.getString(cursor.getColumnIndex("mainpageurl"));
            String baseurl = cursor.getString(cursor.getColumnIndex("baseurl"));
            int lastchapter = cursor.getInt(cursor.getColumnIndex("lastchapter"));
            entry.setmBookName(bookname);
            entry.setmAuthor(author);
            entry.setmMainPageUrl(mainpageurl);
            entry.setmImgUrl(imgurl);
            entry.setmBaseUrl(baseurl);
            entry.setmLastChapter(lastchapter);
            _List.add(entry);
        }
        return _List;
    }

    public boolean hasData() {
        SQLiteDatabase db = DatabaseHelper.getInstance(mContext);
        String sql = "select count(*) from " + TABLE_NOVEL;
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
        String sql = "select count(*) from " + TABLE_NOVEL;
        Cursor cursor = db.rawQuery(sql, null);
        int count = 0;
        if(cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        return count;
    }

    public void updateChapterByName(String bookname,int chapter){
        SQLiteDatabase db = DatabaseHelper.getInstance(mContext);
        String sql = "update "+TABLE_NOVEL+" set lastchapter = "+chapter+" where bookname = '"+bookname+"'";
        db.execSQL(sql);
    }
}
