package trace.traceapp;


import android.content.ContentValues;
import android.content.Context;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ResultReceiver;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;


import static java.util.concurrent.TimeUnit.NANOSECONDS;

/**
 * Created by tenos on 2/10/17.
 * This is the Class that will store and handle GPS information
 */

public class GPSHandler implements GoogleApiClient.ConnectionCallbacks, OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener, LocationSource {
    Polyline line;
    LatLng locLatLng;
    private AddressResultReceiver mResultReceiver;
    protected String mAddressOutput;
    protected boolean mAddressRequested;
    private GoogleMap mMap;
    private OnLocationChangedListener mListener;
    private String latitude = "";
    private String longitude = "";
    private Criteria criteria;
    private String provider;
    private String accuracy;
    private Location location;
    private double speed;
    private double altitude;
    private String curAddress;
    private double bearing;
    private ArrayList<Location> GPSArray;
    private String filename = "testFilemost.srl";
    LocationRequest mLocationRequest;
    private static final long INTERVAL = 60;
    private static final long FASTEST_INTERVAL =1000*3; //3 seconds
    protected static final String ADDRESS_REQUESTED_KEY = "address-request-pending";
    protected static final String LOCATION_ADDRESS_KEY = "location-address";
    private GoogleApiClient mGoogleApiClient;
    private ArrayList<locNode> locNodeArr;
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

        db = LocationsDB.getInstance(context);
        if (GPSArray != null)
        GPSArray.clear();
        GPSArray= getFromFile();
        if (locNodeArr == null || locNodeArr.size()<1){
            locNodeArr = getLNFromFile();
            Log.i(filename, "============================GETTING LOCNODES FROM FILE++++++++++++++++++++++++");
        }
        for (int i = 0; i<GPSArray.size();i++){
            Log.i(filename, GPSArray.get(i).toString());
        }
        Log.i(filename, GPSArray.toString());
        Log.i (filename, context.getPackageName());
        mResultReceiver = new AddressResultReceiver(new Handler());

    }
    public void onStop() {
        Log.i(filename, "stopping location updates");
        stopLocationUpdates();
        db.close();
        if (mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();

    }
    public void dumpToFile(){

        for (locNode loc : locNodeArr){
            loc.storeSelf(context);
        }
        ContentValues values = new ContentValues();
        for (Location arr : GPSArray){
            values.put(LocationsDB.FIELD_LAT, arr.getLatitude() );
            values.put(LocationsDB.FIELD_LNG, arr.getLongitude() );
            values.put(LocationsDB.FIELD_ACC, arr.getAccuracy() );
            values.put(LocationsDB.FIELD_TIME, arr.getTime());
            db.insert(values);
        }



    }
    public ArrayList<locNode> getLNFromFile(){
        ArrayList<locNode> LN = new ArrayList<>();
        Cursor cursor = db.getAllLocationsLoc();
        if (cursor != null){
            if (cursor.moveToFirst()){
                do {
                    String name = cursor.getString(cursor.getColumnIndex(LocationsDB.FIELD_NAME));
                    String addy = cursor.getString(cursor.getColumnIndex(LocationsDB.FIELD_NAME));
                    double lat = cursor.getDouble(cursor.getColumnIndex(LocationsDB.FIELD_LAT));
                    double lng = cursor.getDouble(cursor.getColumnIndex(LocationsDB.FIELD_LNG));
                    double timV = cursor.getInt(cursor.getColumnIndex(LocationsDB.FIELD_TIMESVISITED));
                    locNode loc = new locNode();
                    loc.setLocName(name);
                    loc.setLocAddress(addy);
                    loc.setLocLatCoord(lat);
                    loc.setLocLongCoord(lng);
                    loc.setTimesVisit((int)timV);
                    LN.add(loc);

                }
                while(cursor.moveToNext());
            }

        }
        return LN;
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
                    double tim = cursor.getDouble(cursor.getColumnIndex(LocationsDB.FIELD_TIME));
                    Location loc = new Location("fused");
                    loc.setLatitude(lat);
                    loc.setLongitude(lng);
                    loc.setAccuracy((float)acc);
                    loc.setTime((long)tim);
                    Log.i("testFile", loc.getTime()+"");
                    nwLoc.add(loc);

                }
                while(cursor.moveToNext());
            }
            db.del();
        }
        GPSArray = nwLoc;
            Log.i(filename, "nwLoc: "+ count);
            Log.i(filename, nwLoc.toString());
            //this.clearLocArray();

        return nwLoc;
    }

    //Sets the users CURRENT location
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

        locLatLng = new LatLng(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude());
        return lastKnownLocation;
    }

    //Getter and setter bull
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
    public String getLatitude() {
        return latitude;
    }
    public String getLongitude() {
        return longitude;
    }
    public String getAccuracy() {return accuracy;}
    public void setMapRef(GoogleMap gmap){this.mMap = gmap; }
    public LatLng getLatLng(){return locLatLng;};
    //Err this is a call back from GPS, no touchy touchy
    public void onLocationChanged(Location location) {

        Log.i(filename, "locationChanged");
        //mAddressOutput = "";
        if (mCurrentLocation == null) {
            mCurrentLocation = location;
            setMostRecentLocation(mCurrentLocation);

        }
        if (mCurrentLocation.getLongitude()==location.getLongitude() && mCurrentLocation.getLatitude()== location.getLatitude())
            return;
        mCurrentLocation.distanceTo(location);
        if (location.getAccuracy()<= 100&& location.distanceTo(mCurrentLocation)> location.getAccuracy())  {
            mCurrentLocation = location;
            startIntentService();
            setMostRecentLocation(mCurrentLocation);
            //This updates our map's blue dot and location
            locLatLng = new LatLng(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude());
            if (mListener != null) {
                mListener.onLocationChanged(location);
                drawPoly();
            }
        }


    }
    public void drawPoly(){
        if (getLocArray() == null || locLatLng == null){
            return;
        }
        PolylineOptions options = new PolylineOptions().width(10).color(Color.BLUE).geodesic(true);
        for (int i = 0; i < getLocArray().size(); i++) {
            LatLng point = new LatLng(getLocArray().get(i).getLatitude(),getLocArray().get(i).getLongitude());
            options.add(point);
        }
        MarkerOptions markerOptions = new MarkerOptions();

// Setting latitude and longitude for the marker
        markerOptions.position(locLatLng);
        //mMap.addMarker(markerOptions); //add Marker in current position
        line = mMap.addPolyline(options);
        mMap.addPolyline(new PolylineOptions());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(locLatLng));
        // mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
    }
    //Be Aware this will return speed in meters/ second
    public double calcSpeed(){
        double speed = 0;
        if (GPSArray.size()>1){
            Location loc1 = GPSArray.get(GPSArray.size()-2);
            Location loc2 = GPSArray.get(GPSArray.size()-1);
            double dist = Math.abs(loc1.distanceTo(loc2));
            Log.i("testFile", "dist: " + dist);
            //Time is in milliseconds so you need teh *1000
            double time = (loc2.getTime() - loc1.getTime())*1000;
            Log.i("testFile", "time : " + time);
            speed = dist/time;
            Log.i("testFile", speed+"");
        }
        return speed;
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
    //These two methods, activate and deactivate are for our map..
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        Log.i(filename, "activate called");
        Toast.makeText(context.getApplicationContext(), "dsfsd", Toast.LENGTH_SHORT);
    }
    protected void startIntentService() {
        if (getLocation() == null){return;}
        Intent intent = new Intent(context, FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, getLocation());
        context.startService(intent);
    }
    public ArrayList<locNode> getLocNodeArr(){
        return locNodeArr;
    }
    public void makeLocNode(String name){

        locNode loc = new locNode();
        loc.SetLoc(getLocation());
        loc.setLocName(name);
        loc.setTimesVisit(1);
        if (curAddress != null) {
            loc.setLocAddress(curAddress);
        }
        locNodeArr.add(loc);
    }
    @Override
    public void deactivate() {
        mListener = null;
    }
    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string
            // or an error message sent from the intent service.
            mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
            mAddressOutput = mAddressOutput.replace('\n', ' ');
            curAddress = mAddressOutput;
            String log = Constants.SUCCESS_RESULT +"";
            Log.i("wtf", log);
            if (resultCode == Constants.SUCCESS_RESULT) {
                log = resultData.describeContents() +"";
            }

        }

    }
}
