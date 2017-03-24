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
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import java.util.ArrayList;


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
    private static final long FASTEST_INTERVAL =1000;
    protected static final String ADDRESS_REQUESTED_KEY = "address-request-pending";
    protected static final String LOCATION_ADDRESS_KEY = "location-address";
    private GoogleApiClient mGoogleApiClient;
    LocationsDB db;
    private Location mCurrentLocation;
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

         db = new LocationsDB(context);
        GPSArray= getFromFile();
        for (int i = 0; i<GPSArray.size();i++){
            Log.i(filename, GPSArray.get(i).toString());
        }
        Log.i(filename, GPSArray.toString());
        Log.i (filename, context.getPackageName());


    }
    public void dumpToFile(){


        ContentValues values = new ContentValues();
        for (Location arr : GPSArray){
            values.put(LocationsDB.FIELD_LAT, arr.getLatitude() );
            values.put(LocationsDB.FIELD_LNG, arr.getLongitude() );
            values.put(LocationsDB.FIELD_ACC, arr.getAccuracy() );
            db.insert(values);
        }
        db.close();
            //String Path =getActivity().getFilesDir().toString() ;

//            ObjectOutput out = null;
//
//            try {
//                out = new ObjectOutputStream(new FileOutputStream((new File(context.getFilesDir(),"")+File.separator+filename)));
//                out.writeObject(GPSArray);
//                out.close();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        stopLocationUpdates();
        if (mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();

    }
    private ArrayList<Location> getFromFile()
    {
        ArrayList<Location> nwLoc = new ArrayList<>();
        Cursor cursor = db.getAllLocations();
        if (cursor != null){
            if (cursor.moveToFirst()){
                do {
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
        }

//        ObjectInputStream input;
//
//        ArrayList<Location> arr = new ArrayList<>();
//        try {
//            input = new ObjectInputStream(new FileInputStream(new File(new File(context.getFilesDir(),"")+File.separator+filename)));
//            arr = (ArrayList<Location>) input.readObject();
//
//            Log.v(filename,"Person a="+arr.get(0).getProvider());
//            input.close();
//        } catch (StreamCorruptedException e) {
//            e.printStackTrace();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }

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


        //mAddressOutput = "";
        if (mCurrentLocation == null) {
            mCurrentLocation = location;
            setMostRecentLocation(mCurrentLocation);
        }
        mCurrentLocation.distanceTo(location);
        if (location.getAccuracy()<=100 && location.distanceTo(mCurrentLocation)> 10)  {
            mCurrentLocation = location;
        setMostRecentLocation(mCurrentLocation);

        }





//        if (mLocationRequest.getPriority() == LocationRequest.PRIORITY_HIGH_ACCURACY) {
//            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//            Toast.makeText(this, "Priority changed", Toast.LENGTH_SHORT).show();
//
//        }
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
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient) ;

        //LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        Toast.makeText(context, "Connected to Google Play Services", Toast.LENGTH_SHORT).show();
        Log.i("onCon", "Connected to GPS");
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
