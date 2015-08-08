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
    private static final int DB_VERSION = 1;
    private static final String TABLE_HISTORY = "table_history";


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
        db.execSQL("create table "
                + TABLE_HISTORY
                + " (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " songId integer, duration integer, musicname varchar(20), "
                + "artist varchar(10), data char,playtime integer,albumId integer,folder char,favorite integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion>oldVersion){
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        }
        onCreate(db);
    }
}
