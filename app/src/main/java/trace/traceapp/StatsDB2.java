package trace.traceapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.ViewDebug;

/**
 * close the databaseeeeee after use
 */

public class StatsDB2 extends SQLiteOpenHelper {
    //private static StatsDB sInstance;
    /** Database name */
    private static String DBNAME = "statssqlite2";

    /** Version number of the database */
    private static int VERSION = 1;

    private static boolean init = false;

    private static StatsDB2 sInstance;
    /** A constant, stores the the table name */
    public static final String DATABASE_TABLE_STATS = "stats2";
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


    public StatsDB2(Context context){
        super(context, DBNAME, null, VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table " + DATABASE_TABLE_STATS + "( " +
                FIELD_ROW_ID_STATS + " integer primary key autoincrement , " +
                FIELD_TDISTANCE_STATS + " double ," +
                FIELD_NAME_STATS + " string ," +
                FIELD_HOME_STATS + " string ," +
                FIELD_HOMECOUNT_STATS + " integer ," +
                FIELD_MOSTFREQ_STATS + " string ," +
                FIELD_MOSTFREQCOUNT_STATS + " integer " +
                " ) ";
        db.execSQL(sql);
        //initializes the database when it's created
        initDB(db);
    }

    public static synchronized StatsDB2 getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new StatsDB2(context.getApplicationContext());

        }
        return sInstance;
    }

    public void initDB(SQLiteDatabase mDB){
        ContentValues values = new ContentValues();
        values.put(FIELD_TDISTANCE_STATS,0.0);
        values.put(FIELD_NAME_STATS,"name");
        values.put(FIELD_HOME_STATS,"home");
        values.put(FIELD_HOMECOUNT_STATS,0);
        values.put(FIELD_MOSTFREQ_STATS,"mostfreq");
        values.put(FIELD_MOSTFREQCOUNT_STATS,0);
        mDB.insert(DATABASE_TABLE_STATS, null, values);
    }

    public Cursor getData(){
        if(mDB != null) {
            mDB = this.getReadableDatabase();
            String query = "select * from " + DATABASE_TABLE_STATS;
            Cursor cursor = mDB.rawQuery(query, null);
            return cursor;
        }
        return null;
    }

    public void insertInto(){
        mDB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FIELD_TDISTANCE_STATS,0.0);
        values.put(FIELD_NAME_STATS,"name");
        values.put(FIELD_HOME_STATS,"home");
        values.put(FIELD_HOMECOUNT_STATS,0);
        values.put(FIELD_MOSTFREQ_STATS,"mostfreq");
        values.put(FIELD_MOSTFREQCOUNT_STATS,0);
        mDB.insert(DATABASE_TABLE_STATS, null, values);
    }

    public void del(){
        mDB.delete(DATABASE_TABLE_STATS, null , null);
    }

    //gets and sets (updates)
    public void updateDistance(double distance){
        //1 meter = 0.000621371 miles
        //convert to miles
        //distance *= 0.000621371;
        mDB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FIELD_TDISTANCE_STATS,distance);
        mDB.update(DATABASE_TABLE_STATS, values, null, null);
    }

    public void updateName(String name){
        if(mDB != null) {
            mDB = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(FIELD_NAME_STATS, name);
            mDB.update(DATABASE_TABLE_STATS, values, null, null);
        }
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
        Cursor cursor = mDB.rawQuery(query, null);
        cursor.moveToFirst();
        final String name = cursor.getString(cursor.getColumnIndex(FIELD_NAME_STATS));
        return name;
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

/*
functions in gpsHandler
    public double getTotalDistance(){return statsdb.getDistance();}
    public String getName(){return statsdb.getName();}

       //Total distance stuff
       float[] results = new float[1];
        distanceBetween(mPreviousLocation.getLatitude(), mPreviousLocation.getLongitude(),
                mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), results);
        //get the distance, add new dist, update
        double dist = statsdb.getDistance();
        dist += results[0];
        statsdb.updateDistance(dist);
*/

/*
//old onCreate stuff

        //if(mDB == null){
          //  mDB = getWritableDatabase();
          //  Log.d("DB: ","getWritabledatabase() works");
            //mDB.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_STATS);
            //if(mDB.isOpen()){
            //Log.d("Constructor: ","is the db open?");
            //Log.d("Name: " , this.getName());
            //mDB.close();
            //mDB.delete(DATABASE_TABLE_STATS, "1", null);
            //Log.d("Rows: ", "rows were removed man");
            //}
            //else{
            //Log.d("Create: ","Oncreate about to run");
            //onCreate(mDB);
            //}
        //}
 */
