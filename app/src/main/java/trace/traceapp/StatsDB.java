package trace.traceapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * close the databaseeeeee after use
 */

public class StatsDB extends SQLiteOpenHelper {
    private static StatsDB sInstance;
    /** Database name */
    private static String DBNAME = "statssqlite";

    /** Version number of the database */
    private static int VERSION = 1;

    private static boolean init = false;

    /** A constant, stores the the table name */
    public static final String DATABASE_TABLE_STATS = "stats";
    public static final String FIELD_TDISTANCE_STATS = "tdistance";
    /** Field 1 of the table locations, which is the primary key */
    public static final String FIELD_ROW_ID_STATS = "_id_stats";
    public static final String FIELD_HOME_STATS = "home";
    public static final String FIELD_HOMECOUNT_STATS = "homecount";
    public static final String FIELD_MOSTFREQ_STATS = "mostfrequent";
    public static final String FIELD_MOSTFREQCOUNT_STATS = "mostfrequentcount";
    public static final String FIELD_NAME_STATS = "name";
    public static final String FIELD_INIT_STATS = "initdb";
    /** An instance variable for SQLiteDatabase */
    private SQLiteDatabase mDB;


    public StatsDB(Context context) {
        super(context, DBNAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_STATS);
        
        String sql = "create table " + DATABASE_TABLE_STATS + "( " +
                FIELD_ROW_ID_STATS + " integer primary key autoincrement , " +
                FIELD_INIT_STATS + " boolean ," +
                FIELD_TDISTANCE_STATS + " double ," +
                FIELD_NAME_STATS + " string ," +
                FIELD_HOME_STATS + " string ," +
                FIELD_HOMECOUNT_STATS + "integer ," +
                FIELD_MOSTFREQ_STATS + " string ," +
                FIELD_MOSTFREQCOUNT_STATS + " integer " +
                " ) ";
        db.execSQL(sql);

        /*
        if(!init){
            //drop the dang table
            mDB.delete(DATABASE_TABLE_STATS, null, null);
            ContentValues values = new ContentValues();
            values.put(FIELD_TDISTANCE_STATS,0.0);
            values.put(FIELD_INIT_STATS,1);
            values.put(FIELD_NAME_STATS,"name");
            values.put(FIELD_HOME_STATS,"home");
            values.put(FIELD_HOMECOUNT_STATS,0);
            values.put(FIELD_MOSTFREQ_STATS,"mostfreq");
            values.put(FIELD_MOSTFREQCOUNT_STATS,0);
            mDB.insert(DATABASE_TABLE_STATS, null, values);
            //first try inserting row instead of initializing
            //
        }
        */


    }

    public void del(){
        mDB.delete(DATABASE_TABLE_STATS, null , null);
    }

    //gets and sets (updates)
    public void updateDistance(double distance){
        mDB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FIELD_TDISTANCE_STATS,distance);
        mDB.update(DATABASE_TABLE_STATS, values, null, null);
    }

    public void updateName(String name){
        mDB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FIELD_NAME_STATS,name);
        mDB.update(DATABASE_TABLE_STATS, values, null, null);
    }

    public void updateHomeName(String homename){
        mDB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FIELD_HOME_STATS,homename);
        mDB.update(DATABASE_TABLE_STATS, values, null, null);
    }

    public void updateHomeCount(int homecount){
        mDB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FIELD_HOMECOUNT_STATS,homecount);
        mDB.update(DATABASE_TABLE_STATS, values, null, null);
    }

    public void updateMostFreqName(String mfname){
        mDB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FIELD_MOSTFREQ_STATS,mfname);
        mDB.update(DATABASE_TABLE_STATS, values, null, null);
    }

    public void updateMostFreqCount(int mostfreqcount){
        mDB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FIELD_MOSTFREQCOUNT_STATS,mostfreqcount);
        mDB.update(DATABASE_TABLE_STATS, values, null, null);
    }

    public double getDistance(){
        mDB = this.getReadableDatabase();
        String query = "select " + FIELD_TDISTANCE_STATS + " from " + DATABASE_TABLE_STATS;
        Cursor cursor = mDB.rawQuery(query,null);
        cursor.moveToFirst();
        final double dist = cursor.getDouble(cursor.getColumnIndex(FIELD_TDISTANCE_STATS));
        return dist;
    }

    public int getHomeCount(){
        mDB = this.getReadableDatabase();
        String query = "select " + FIELD_HOMECOUNT_STATS + " from " + DATABASE_TABLE_STATS;
        Cursor cursor = mDB.rawQuery(query,null);
        cursor.moveToFirst();
        final int homecount = cursor.getInt(cursor.getColumnIndex(FIELD_HOMECOUNT_STATS));
        return homecount;
    }

    public int getMostFreqCount(){
        mDB = this.getReadableDatabase();
        String query = "select " + FIELD_MOSTFREQCOUNT_STATS + " from " + DATABASE_TABLE_STATS;
        Cursor cursor = mDB.rawQuery(query,null);
        cursor.moveToFirst();
        final int mfcount = cursor.getInt(cursor.getColumnIndex(FIELD_MOSTFREQCOUNT_STATS));
        return mfcount;
    }

    public String getName(){
        mDB = this.getReadableDatabase();
        String query = "select " + FIELD_NAME_STATS + " from " + DATABASE_TABLE_STATS;
        //try {
            Cursor cursor = mDB.rawQuery(query, null);
            cursor.moveToFirst();
            if (cursor != null) {
                final String name = cursor.getString(cursor.getColumnIndex(FIELD_NAME_STATS));
                return name;
            }
        //}catch(){

        //}
        else return "";
    }

    public String getHomeName(){
        mDB = this.getReadableDatabase();
        String query = "select " + FIELD_HOME_STATS + " from " + DATABASE_TABLE_STATS;
        Cursor cursor = mDB.rawQuery(query,null);
        cursor.moveToFirst();
        final String homename = cursor.getString(cursor.getColumnIndex(FIELD_HOME_STATS));
        return homename;
    }

    public String getMostFreq(){
        mDB = this.getReadableDatabase();
        String query = "select " + FIELD_MOSTFREQ_STATS + " from " + DATABASE_TABLE_STATS;
        Cursor cursor = mDB.rawQuery(query,null);
        cursor.moveToFirst();
        final String mostfreqname = cursor.getString(cursor.getColumnIndex(FIELD_MOSTFREQ_STATS));
        return mostfreqname;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
