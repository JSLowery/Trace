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


    public StatsDB(Context context) {
        super(context, DBNAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql =     "create table " + DATABASE_TABLE_STATS + "( " +
                FIELD_ROW_ID_STATS + " integer primary key autoincrement , " +
                FIELD_TDISTANCE_STATS + " double " +
                " ) ";
        //add most frequently visited
        //add home
        db.execSQL(sql);
    }

    /** Inserts a new statistic to the table locations */
    public void insertDistance(double distance){
        mDB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String query = "select * from " + DATABASE_TABLE_STATS;
        Cursor cursor = mDB.rawQuery(query,null);
        values.put(FIELD_TDISTANCE_STATS,distance);
        mDB.insert(DATABASE_TABLE_STATS,null,values);
        //mDB.close();
    }


    public void deleteStats(){
        mDB.delete(DATABASE_TABLE_STATS, null , null);
    }

    public double getDistance(){
        mDB = this.getReadableDatabase();
        String query = "select " + FIELD_TDISTANCE_STATS + " from " + DATABASE_TABLE_STATS;
        Cursor cursor = mDB.rawQuery(query,null);
        cursor.moveToFirst();
        final double dist = cursor.getDouble(cursor.getColumnIndex(FIELD_TDISTANCE_STATS));
        //mDB.close();
        return dist;
    }

    public void updateBalance(double newDist){
        mDB = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(FIELD_TDISTANCE_STATS, newDist);
        mDB.update(DATABASE_TABLE_STATS, values, null, null);//where clause: FIELD_ROW_ID_STATS + " is '" + "1" + "'"
        //mDB.close();
    }

    /*
    public static synchronized StatsDB getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new StatsDB(context.getApplicationContext());

        }
        return sInstance;
    }
    */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
