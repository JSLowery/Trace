package trace.traceapp;

import android.content.ContentValues;
import android.content.Context;
import android.location.Location;

/**
 * Created by david on 4/11/2017.
 */

public class locNode {

    private String locName = "";
    private String locAddress = "";
    private double locLatCoord;
    private double locLongCoord;
    private int timesVisit = 0;
    private Location loc;
    private boolean withinFlag = false;
    LocationsDB db;
    public locNode() {
    }
    public boolean getFlag (){return withinFlag;}
    public void setFlag(boolean bool){withinFlag = bool;}
    public String getLocName(){
        return locName;
    }
    public int getTimesVisit() {
        return timesVisit;
    }

    public String getLocAddress() {
        return locAddress;
    }

    public double getLocLatCoord() {
        return locLatCoord;
    }

    public double getLocLongCoord() {
        return locLongCoord;
    }

    public Location getLoc() {
        return loc;
    }

    public void setLocName(String name) {
        this.locName = name;
    }

    public void setLocAddress(String address) {
        this.locAddress = address;
    }

    public void setTimesVisit(int visits) {
        this.timesVisit = visits;
    }

    public void setLocLatCoord(double latitude) {
        this.locLatCoord = latitude;
    }

    public void setLocLongCoord(double longitude) {
        this.locLongCoord = longitude;
    }
    public void setLoc(Location loc){
        this.loc = loc;
        setLocLatCoord(loc.getLatitude());
        setLocLongCoord(loc.getLongitude());
    }
    public String fixSql(String str){
        str = str.replaceAll("'","''");
        return str;
    }
    public void storeSelf(Context context){
            ContentValues values = new ContentValues();
            db = LocationsDB.getInstance(context);
            values.put(LocationsDB.FIELD_NAME, fixSql(locName));
            values.put(LocationsDB.FIELD_ADDY, locAddress);
            values.put(LocationsDB.FIELD_LAT, locLatCoord );
            values.put(LocationsDB.FIELD_LNG, locLongCoord );
            values.put(LocationsDB.FIELD_TIMESVISITED, timesVisit);
            db.insertLoc(values);
    }
    /*public void setLocationCoord(float latitude, float longitude) {
        this.locLatCoord = latitude;
        this.locLongCoord = longitude;
    }*/


    



    
}
