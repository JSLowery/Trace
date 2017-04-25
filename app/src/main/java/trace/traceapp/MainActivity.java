package trace.traceapp;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;

import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.vision.text.Text;

import java.util.ArrayList;

import static android.Manifest.permission_group.LOCATION;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
        {
    private StatsDB2 statsdb;
    //LocationClient locationClient;
    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 60;
    private static final long FASTEST_INTERVAL =0;
    protected static final String ADDRESS_REQUESTED_KEY = "address-request-pending";
    protected static final String LOCATION_ADDRESS_KEY = "location-address";
     static RetainedFragment dataFragment;
    LocationRequest mLocationRequest;
    Location mCurrentLocation;
    //stuff for address
    //private AddressResultReceiver mResultReceiver;
    protected String mAddressOutput;
    protected boolean mAddressRequested;
    protected TextView mLocationAddressTextView;
    protected FragmentManager fm;
    // end google api stuff
    static GPSHandler appLocationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //stub for permissions, Drew look at me!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //Also, delete this.
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck!= PackageManager.PERMISSION_GRANTED){

        }
        Log.i(TAG, "onCreate");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (appLocationManager ==null)
            Log.i("testFile", "appLocationManager was null");
        if (MapsActivity.appLocationManager != null) {
            Log.i("testfile", "Got GPSHANDLER reference from maps");
            appLocationManager = MapsActivity.appLocationManager;
        }
        else if (StartDraw.appLocationManager != null){
            appLocationManager = StartDraw.appLocationManager;
            Log.i("fileTest", "Got GPSHANDLER reference from draw");
        }
        else {
            appLocationManager = new GPSHandler(this);
            Log.i("testfile", "made a new GPSHANDLER");
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUI();
                Snackbar.make(view, "Write something here!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        statsdb = StatsDB2.getInstance(getApplicationContext());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            //when the drawer is opened initilize username,distance,picture mayb
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                TextView unameDrawerText = (TextView) findViewById(R.id.usernameDrawer);
                TextView distanceDrawerText = (TextView) findViewById(R.id.totalDistanceDrawer);
                unameDrawerText.setText(statsdb.getName());
                distanceDrawerText.setText("Traveled " + statsdb.getDistance() + "mi");
                //add image stuff here dave
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //begin the Google API stuff, this is for a test
        //locationClient = new LocationClient(this, this, this);//this is no longer part of andriod... fuck

        //stuff for getting location
        updateValuesFromBundle(savedInstanceState);

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

        if (id == R.id.nav_stats) {
            //statsdb.updateName("asdjfk;");
            //TextView name = (TextView) findViewById(R.id.username);
            //name.setText(statsdb.getName());
            startActivity(new Intent(MainActivity.this, StatsActivity.class));
        } else if (id == R.id.nav_mapview) {
            startActivity(new Intent(MainActivity.this, MapsActivity.class));
        }  else if (id == R.id.nav_share) {
            startActivity(new Intent(MainActivity.this, StartDraw.class));
        } else if (id == R.id.nav_savecanvas) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    protected void onStart() {
        Log.i("testFile", "Main called get from file");
        appLocationManager.getFromFile();
        super.onStart();
    }

    protected void onStop() {
        super.onStop();




    }


    private double time1= 0.0;
    private double time2= 0.0;
    private double nano1= 0.0;
    private double nano2= 0.0;
    private void updateUI() {
        ArrayList<locNode> LNA =appLocationManager.getLocNodeArr();
        showToast(appLocationManager.getLocNodeArr().size()+"");
        for (locNode LN : LNA) {
                showToast(LN.getLocAddress() + "\n"
                        + LN.getLocName() + "\n"
                        + LN.getLocLatCoord() + "\n"
                        + LN.getLocLongCoord() + "\n"
                        + LN.getTimesVisit());
        }
        if (appLocationManager.getLocation() != null) {

            TextView txt = (TextView) findViewById(R.id.gps);
//             txt.setText(lon);
            txt.setText(" Lattitude: "+appLocationManager.getLatitude()+
                    "\n Longetude: "+ appLocationManager.getLongitude()+
                    "\n Accuracy: "+ appLocationManager.getAccuracy()+
                    "\n Provider: "+ appLocationManager.getProvider()+
                    "\n Altitude: "+ appLocationManager.getAltitude()+
                    "\n Bearing: "+ appLocationManager.getBearing()+
                    "\n Time: " + (time2-time1)/1000+
                    "\n Speed: " + appLocationManager.getSpeed()+
                    "\n Number of objects in arrayLN: "+ appLocationManager.getLocNodeArr().size()+
                    "\n Elapsed time: " + NANOSECONDS.toSeconds((long)(nano2- nano1))



            );
            Toast.makeText(this, "Location Updated", Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "location is null ...............");
        }
        startIntentService();
    }


    protected void startIntentService() {
        if (appLocationManager.getLocation() == null){return;}
        //Intent intent = new Intent(this, FetchAddressIntentService.class);
        //intent.putExtra(Constants.RECEIVER, mResultReceiver);
       // intent.putExtra(Constants.LOCATION_DATA_EXTRA, appLocationManager.getLocation());
       // Log.i(TAG, "starting service");
       // startService(intent);
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save whether the address has been requested.
        savedInstanceState.putBoolean(ADDRESS_REQUESTED_KEY, mAddressRequested);

        // Save the address string.
        savedInstanceState.putString(LOCATION_ADDRESS_KEY, mAddressOutput);
        super.onSaveInstanceState(savedInstanceState);
    }
    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // Check savedInstanceState to see if the address was previously requested.
            if (savedInstanceState.keySet().contains(ADDRESS_REQUESTED_KEY)) {
                mAddressRequested = savedInstanceState.getBoolean(ADDRESS_REQUESTED_KEY);
            }
            // Check savedInstanceState to see if the location address string was previously found
            // and stored in the Bundle. If it was found, display the address string in the UI.
            if (savedInstanceState.keySet().contains(LOCATION_ADDRESS_KEY)) {
                mAddressOutput = savedInstanceState.getString(LOCATION_ADDRESS_KEY);
                displayAddressOutput();
            }
        }
    }
    protected void displayAddressOutput() {
        TextView test = (TextView) findViewById(R.id.location_address_view);
         //mAddressOutput = LOCATION_ADDRESS_KEY;
        //mLocationAddressTextView = (TextView) findViewById(R.id.location_address_view);
        Log.i(TAG, "displayAddressOutpu");
        test.setText(mAddressOutput);
    }
    @Override
    public void onResume() {
        super.onResume();

    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.i("testFile", "Main called dump file");
        appLocationManager.dumpToFile();

    }
    protected void onDestroy(){
        super.onDestroy();
        //appLocationManager.onStop();



    }

    protected void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

//    class AddressResultReceiver extends ResultReceiver {
//        public AddressResultReceiver(Handler handler) {
//            super(handler);
//        }
//        @Override
//        protected void onReceiveResult(int resultCode, Bundle resultData) {
//
//            // Display the address string
//            // or an error message sent from the intent service.
//            mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
//            displayAddressOutput();
//
//            // Show a toast message if an address was found.
//            String log = Constants.SUCCESS_RESULT +"";
//            Log.i("wtf", log);
//            if (resultCode == Constants.SUCCESS_RESULT) {
//                log = resultData.describeContents() +"";
//                showToast(mAddressOutput);
//            }
//
//        }
//
//    }
}
