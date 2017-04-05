package trace.traceapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by lotusland on 4/4/17.
 */

public class StatsDB extends SQLiteOpenHelper {
    private static StatsDB sInstance;
    /** Database name */
    private static String DBNAME = "statssqlite";

    /** Version number of the database */
    private static int VERSION = 1;

    /** A constant, stores the the table name */
    public static final String DATABASE_TABLE_STATS = "stats";
    public static final String FIELD_TDISTANCE_STATS = "tdistance";
    /** Field 1 of the table locations, which is the primary key */
    public static final String FIELD_ROW_ID_STATS = "_id_stats";

    /** An instance variable for SQLiteDatabase */
    private SQLiteDatabase mDB;


    private StatsDB(Context context) {
        super(context, DBNAME, null, VERSION);
        Log.i("testFile", "started the DB?");
        if (mDB == null) {
            mDB = getWritableDatabase();

            if (mDB.isOpen()){
                Log.i("testFile", "mDB is open I think");
                Cursor statsCursor = mDB.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+DATABASE_TABLE_STATS+"'", null);
                if(statsCursor!=null) {
                    statsCursor.moveToFirst();
                    Log.i("testFile",statsCursor.toString());
                }
            }else
                onCreate(mDB);

        }else
            Log.i("testFile", "database was open");
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql =     "create table " + DATABASE_TABLE_STATS + " ( " +
                FIELD_ROW_ID_STATS + " integer primary key autoincrement , " +
                FIELD_TDISTANCE_STATS + " double , " +
                " ) ";
        //add most frequently visited
        //add home
        Log.i("testFile", "oncreate was called");
        db.execSQL(sql);
    }

    /** Inserts a new location to the table locations */
    public long insert_stats(ContentValues contentValues){
        long rowID = mDB.insert(DATABASE_TABLE_STATS, null, contentValues);
        return rowID;
    }

    /** Deletes all locations from the table */
    public int del_stats(){
        int cnt = mDB.delete(DATABASE_TABLE_STATS, null , null);
        return cnt;
    }

    /** Returns all the locations from the table */
    public Cursor getDistance(){
        if (mDB != null)
            return mDB.query(DATABASE_TABLE_STATS, new String[] {FIELD_TDISTANCE_STATS} , null, null, null, null, null);
        Cursor c = null;
        return c;
    }

    public static synchronized StatsDB getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new StatsDB(context.getApplicationContext());

        }
        return sInstance;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
