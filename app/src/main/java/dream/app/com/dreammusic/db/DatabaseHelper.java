package dream.app.com.dreammusic.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2015/8/8.
 */
public class DatabaseHelper extends SQLiteOpenHelper {


    private static final String DB_NAME = "dream_music";
    private static final int DB_VERSION = 2;
    private static final String TABLE_HISTORY = "table_history";
    private static final String TABLE_NOVEL = "table_novel";


    private static SQLiteDatabase mDb;
    private static DatabaseHelper mHelper;

    public static SQLiteDatabase getInstance(Context context){
        if(mDb == null){
            mDb = getHelper(context).getWritableDatabase();
        }
        return mDb;
    }

    private static DatabaseHelper getHelper(Context context){
        if(mHelper == null){
            mHelper = new DatabaseHelper(context);
        }
        return mHelper;
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists"
                + TABLE_HISTORY
                + " (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " songId integer, duration integer, musicname varchar(20), "
                + "artist varchar(10), data char,playtime integer,albumId integer,folder char,favorite integer)");

        db.execSQL("create table if not exists"
                + TABLE_NOVEL
                + " (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "bookname varchar,author varchar,imgurl varchar,mainpageurl varchar,baseurl varchar,lastchapter integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        if (newVersion>oldVersion){
            onCreate(db);
        }

    }
}
