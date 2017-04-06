package trace.traceapp;


import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import java.util.ArrayList;


import static android.location.Location.distanceBetween;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

/**
 * Created by tenos on 2/10/17.
 * This is the Class that will store and handle GPS information
 */

public class GPSHandler implements GoogleApiClient.ConnectionCallbacks, OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    private LocationManager locationManager;
    private String latitude = "";
    private String longitude = "";
    private Criteria criteria;
    private String provider;
    private String accuracy;
    private Location location;
    private double speed;
    private double altitude;
    private double bearing;
    private ArrayList<Location> GPSArray;
    private String filename = "testFilemost.srl";
    LocationRequest mLocationRequest;
    private static final long INTERVAL = 60;
    private static final long FASTEST_INTERVAL =1000*10; //10 seconds
    protected static final String ADDRESS_REQUESTED_KEY = "address-request-pending";
    protected static final String LOCATION_ADDRESS_KEY = "location-address";
    private GoogleApiClient mGoogleApiClient;
    LocationsDB db;
    StatsDB statsdb;
    private Location mCurrentLocation;
    private Location mPreviousLocation;
    String path = String.valueOf(Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOCUMENTS));
    private Context context;
    public GPSHandler(Context context) {
        this.context = context;
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener( this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();
        createLocationRequest();
        statsdb = new StatsDB(context);
        //statsdb.insertDistance(7.0);
        db = LocationsDB.getInstance(context);
        if (GPSArray != null)
        GPSArray.clear();
        GPSArray= getFromFile();

        for (int i = 0; i<GPSArray.size();i++){
            Log.i(filename, GPSArray.get(i).toString());
        }
        Log.i(filename, GPSArray.toString());
        Log.i (filename, context.getPackageName());


    }
    public void onStop() {
        Log.i(filename, "stopping location updates");
        stopLocationUpdates();
        if (mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();

    }
    public void dumpToFile(){


        ContentValues values = new ContentValues();
        for (Location arr : GPSArray){
            values.put(LocationsDB.FIELD_LAT, arr.getLatitude() );
            values.put(LocationsDB.FIELD_LNG, arr.getLongitude() );
            values.put(LocationsDB.FIELD_ACC, arr.getAccuracy() );
            db.insert(values);
        }



    }
    public ArrayList<Location> getFromFile()
    {
        if (GPSArray != null)
        GPSArray.clear();
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener( this)
                    .addApi(LocationServices.API)
                    .build();
        }
        Log.i(filename, "getFromFile was called!!!!!!!!!!!");
        ArrayList<Location> nwLoc = new ArrayList<>();
        int count= 0;
        Cursor cursor = db.getAllLocations();
        if (cursor != null){
            if (cursor.moveToFirst()){
                do {
                    count++;
                    double lat = cursor.getDouble(cursor.getColumnIndex(LocationsDB.FIELD_LAT));
                    double lng = cursor.getDouble(cursor.getColumnIndex(LocationsDB.FIELD_LNG));
                    double acc = cursor.getDouble(cursor.getColumnIndex(LocationsDB.FIELD_ACC));
                    Location loc = new Location("fused");
                    loc.setLatitude(lat);
                    loc.setLongitude(lng);
                    loc.setAccuracy((float)acc);

                    nwLoc.add(loc);

                }
                while(cursor.moveToNext());
            }
            db.del();
        }
            Log.i(filename, "nwLoc: "+ count);
            Log.i(filename, nwLoc.toString());
            //this.clearLocArray();

        return nwLoc;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
    public String getAccuracy() {return accuracy;}
    private Location setMostRecentLocation(Location lastKnownLocation) {
        double lon;
        lon = lastKnownLocation.getLongitude();
        double lat;
        lat = lastKnownLocation.getLatitude();
        double acc;
        acc = lastKnownLocation.getAccuracy();
        String prov = lastKnownLocation.getProvider();
        Log.i(filename, lastKnownLocation.toString());
        longitude = lon + "";
        latitude = lat + "";
        accuracy = acc + "";
        location = lastKnownLocation;
        provider = prov;
        bearing = lastKnownLocation.getBearing();
        speed = lastKnownLocation.getSpeed();
        altitude = lastKnownLocation.getAltitude();
        Log.i(filename, "Added location: "+ latitude+ " "+ longitude);
        addLocArray(lastKnownLocation);
        return lastKnownLocation;
    }
    public ArrayList<Location> getLocArray(){return GPSArray;}
    private void addLocArray(Location location){
        GPSArray.add(location);
    }
    public void clearLocArray(){GPSArray = new ArrayList<>(); db.del();}
    public String getProvider(){
        return provider;
    }
    public Location getLocation(){
        return location;
    }
    public void SetLocation(Location loc){
        setMostRecentLocation(loc);
    }
    public void getArrayFromService(ArrayList<Location> locArray){
        GPSArray = locArray;
    }
    public double getSpeed(){return speed;}
    public double getBearing(){return bearing;}
    public double getAltitude(){return altitude;}


    public void onLocationChanged(Location location) {
        /*
        Log.i(filename, "locationChanged");
        //mAddressOutput = "";
        if (mCurrentLocation == null) {
            mCurrentLocation = location;
            setMostRecentLocation(mCurrentLocation);
        }
        if (mCurrentLocation.getLongitude()==location.getLongitude() && mCurrentLocation.getLatitude()== location.getLatitude())
            return;
        mCurrentLocation.distanceTo(location);
        if (location.getAccuracy()<=100&& location.distanceTo(mCurrentLocation)> 10)  {
            mPreviousLocation = mCurrentLocation;
            mCurrentLocation = location;
        setMostRecentLocation(mCurrentLocation);

        }
        */
        Location loc1 = new Location("nel");
        Location loc2 = new Location("nel");
        loc1.setLatitude(40.3216491);
        loc1.setLongitude(-75.9911328);
        loc2.setLatitude(40.3216481);
        loc2.setLongitude(-75.9911320);
        float[] results = new float[1];
        //distanceBetween(mPreviousLocation.getLatitude(), mPreviousLocation.getLongitude(),
        //                mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), results);
        distanceBetween(loc1.getLatitude(), loc1.getLongitude(),
                loc2.getLatitude(), loc2.getLongitude(), results);
        //get the distance to add to
        double dist = statsdb.getDistance();
        //remove row with old result
        statsdb.deleteStats();
        //put it back with the new result added
        dist += results[0];
        statsdb.insertDistance(dist);

//        if (mLocationRequest.getPriority() == LocationRequest.PRIORITY_HIGH_ACCURACY) {
//            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//            Toast.makeText(this, "Priority changed", Toast.LENGTH_SHORT).show();
//
//        }
    }

    public double getTotalDistance(){
        return statsdb.getDistance();
    }
    //stoplocationupdates and startlocationupdates are for the google playservice api calls
    protected void stopLocationUpdates() {
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);

        }
    }
    protected void startLocationUpdates() {
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

    }
    public void onConnected(@Nullable Bundle bundle) {
        location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient) ;

        //LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        Toast.makeText(context, "Connected to Google Play Services", Toast.LENGTH_SHORT).show();
        Log.i(filename, "Connected to GPS");
        startLocationUpdates();
    }
    //location request for google play api calls
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
    public void onDisconnected(){
        Toast.makeText(context, "Connected from Google Play Services.", Toast.LENGTH_SHORT).show();
    }
    public void onConnectionSuspended(int i) {

    }


    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}
