package trace.traceapp;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class LocationsDB extends SQLiteOpenHelper {
    public static final String FIELD_TIME = "tim";
    private static LocationsDB sInstance;
    /** Database name */
    private static String DBNAME = "locationmarkersqlite";

    /** Version number of the database */
    private static int VERSION = 1;

    /** Field 1 of the table locations, which is the primary key */
    public static final String FIELD_ROW_ID = "_id";

    /** Field 2 of the table locations, stores the latitude */
    public static final String FIELD_LAT = "lat";

    /** Field 3 of the table locations, stores the longitude*/
    public static final String FIELD_LNG = "lng";

    /** Field 4 of the table locations, stores the zoom level of map*/
    public static final String FIELD_ACC = "acc";

    /** A constant, stores the the table name */
    private static final String DATABASE_TABLE = "locations";

    /** An instance variable for SQLiteDatabase */
    private SQLiteDatabase mDB;

    /** Constructor */
//    public LocationsDB(Context context) {
//        super(context, DBNAME, null, VERSION);
//        this.mDB = getWritableDatabase();
//    }

    public static synchronized LocationsDB getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new LocationsDB(context.getApplicationContext());

        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * make call to static method "getInstance()" instead.
     */
    private LocationsDB(Context context) {
        super(context, DBNAME, null, VERSION);
        Log.i("testFile", "started the DB?");
        if (mDB == null) {
            mDB = getWritableDatabase();

            if (mDB.isOpen()){
                Log.i("testFile", "mDB is open I think");
//                mDB.execSQL("DROP TABLE "+DATABASE_TABLE+";");
                Cursor cursor = mDB.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+DATABASE_TABLE+"'", null);
                if(cursor!=null) {
                    cursor.moveToFirst();
                    Log.i("testFile",cursor.toString());
                }
            }else
//                mDB.execSQL("DROP TABLE "+DATABASE_TABLE+";");
                onCreate(mDB);
        }else
            Log.i("testFile", "database was open");
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql =     "create table " + DATABASE_TABLE + " ( " +
                FIELD_ROW_ID + " integer primary key autoincrement , " +
                FIELD_LNG + " double , " +
                FIELD_LAT + " double , " +
                FIELD_ACC + " double , " +
                FIELD_TIME + " double " +
                " ) ";
        Log.i("testFile", "oncreate was called");
        db.execSQL(sql);
    }

    /** Inserts a new location to the table locations */
    public long insert(ContentValues contentValues){
        long rowID = mDB.insert(DATABASE_TABLE, null, contentValues);
        return rowID;
    }

    /** Deletes all locations from the table */
    public int del(){
        int cnt = mDB.delete(DATABASE_TABLE, null , null);
        return cnt;
    }

    /** Returns all the locations from the table */
    public Cursor getAllLocations(){
        if (mDB != null)
        return mDB.query(DATABASE_TABLE, new String[] { FIELD_ROW_ID,  FIELD_LAT , FIELD_LNG, FIELD_ACC, FIELD_TIME } , null, null, null, null, null);
        Cursor c = null;
        return c;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}