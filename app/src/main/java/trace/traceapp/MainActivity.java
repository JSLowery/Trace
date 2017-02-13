package trace.traceapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.icu.text.DateFormat;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.Date;

import static trace.traceapp.R.layout.activity_main;
import static trace.traceapp.R.layout.content_main;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    //LocationClient locationClient;
    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    LocationRequest mLocationRequest;
    Location mCurrentLocation;
    String mLastUpdateTime;
    TextView GPA_first;
    TextView GPA_second;
    // end google api stuff
    static GPSHandler appLocationManager;
    private GoogleApiClient mGoogleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String lon;
        String lat;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        ActivityCompat.requestPermissions(this,
//                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        appLocationManager = new GPSHandler(MainActivity.this);
        appLocationManager.getLatitude();
        appLocationManager.getLongitude();
        TextView txt = (TextView) findViewById(R.id.gps);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appLocationManager.getLatitude();
                appLocationManager.getLongitude();
                Snackbar.make(view, "Latitude: "+appLocationManager.getLatitude() + " Longitude: " +appLocationManager.getLongitude(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //begin the Google API stuff, this is for a test
        //locationClient = new LocationClient(this, this, this);//this is no longer part of andriod... fuck
        createLocationRequest();
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        GPA_first = (TextView) findViewById(R.id.GFA_gps);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            //GPSHandler appLocationManager = new GPSHandler(MainActivity.this);
            appLocationManager.getLatitude();
            appLocationManager.getLongitude();
            String lon = appLocationManager.getLongitude();
            String lat = appLocationManager.getLatitude();
            Toast.makeText(this, lat, Toast.LENGTH_LONG).show();
            Toast.makeText(this, lon, Toast.LENGTH_LONG).show();
            TextView txt = (TextView) findViewById(R.id.gps);
            // txt.setText(lon);
            txt.setText("Lattitude: "+appLocationManager.getLatitude()+ " Longetude:"+ appLocationManager.getLongitude());
           // mLatitudeTextView.setText(String.format("%s: %f", mLatitudeLabel,
              //      mCurrentLocation.getLatitude()));
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
        stopLocationUpdates();

    }

    public void onDisconnected(){
        Toast.makeText(this, "Connected from Google Play Services.", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient) ;
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        Toast.makeText(this, "Connected to Google Play Services", Toast.LENGTH_SHORT).show();
        startLocationUpdates();
    }
    //location request for google play api calls
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_NO_POWER);
    }
    //stoplocationupdates and startlocationupdates are for the google playservice api calls
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        Log.d(TAG, "Location update stopped .......................");
    }
    protected void startLocationUpdates() {
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
        Log.d(TAG, "Location update started ..............: ");
    }
    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Firing onLocationChanged..............................................");
        mCurrentLocation = location;

        //mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
    }
    private void updateUI() {
        Log.d(TAG, "UI update initiated .............");
        if (null != mCurrentLocation) {
            String lat = String.valueOf(mCurrentLocation.getLatitude());
            String lng = String.valueOf(mCurrentLocation.getLongitude());
            GPA_first.setText("Latitude: " + lat + "\n" +
                    "Longitude: " + lng + "\n" +
                    "Accuracy: " + mCurrentLocation.getAccuracy() + "\n" +
                    "Provider: " + mCurrentLocation.getProvider());
            TextView txt = (TextView) findViewById(R.id.gps);
            // txt.setText(lon);
            txt.setText("Lattitude: "+appLocationManager.getLatitude()+ " Longetude:"+ appLocationManager.getLongitude());
            Toast.makeText(this, "Location Updated", Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "location is null ...............");
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
            Log.d(TAG, "Location update resumed .....................");
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }
    protected void onDestroy(){
        super.onDestroy();
        stopLocationUpdates();
    }
}
