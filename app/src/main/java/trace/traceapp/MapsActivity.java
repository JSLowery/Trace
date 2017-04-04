package trace.traceapp;

import android.app.FragmentManager;
import android.location.Location;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Objects;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<Location> mLocationArray;
    static GPSHandler appLocationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        appLocationManager = MainActivity.appLocationManager;

         mLocationArray =appLocationManager.getLocArray();
        for (Location loc: mLocationArray){
            Log.i("testFile", loc.toString());
        }
        //appLocationManager.clearLocArray();

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    protected void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
    private void drawAllPoints(){
        mMap.clear();
        mLocationArray = appLocationManager.getLocArray();
        double lat = 40.3216491;
        double lng = -75.9911328;
        for (int i = 0;i<mLocationArray.size();i++) {

            if (mLocationArray.size() > 0) {
                lat = mLocationArray.get(i).getLatitude();
                lng = mLocationArray.get(i).getLongitude();
            }

            // Add a marker in Sydney and move the camera mLocationArray.get(0).getLatitude() mLocationArray.get(0).getLongitude()
            LatLng sydney = new LatLng(lat, lng);
            drawMarker(sydney);
//            MarkerOptions mark = new MarkerOptions();
//            mark.position(sydney);
//            mMap.addMarker(mark);
            Log.i("testFile", "Added: "+sydney.latitude+" "+sydney.longitude);

        }
        LatLng sydney = new LatLng(lat, lng);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);

        mLocationArray = appLocationManager.getLocArray();
        String size = mLocationArray.size()+"";
        showToast(size);
        drawAllPoints();

        mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
        mMap.setMaxZoomPreference(20.0f);
        mMap.setMinZoomPreference(5.0f);
        handler.postAtTime(runnable, System.currentTimeMillis()+interval);
        handler.postDelayed(runnable, interval);

    }
    private int interval = 1000*6; // 6 Second
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable(){
        public void run() {
            Log.i("testFile", "runnable fired");
            mLocationArray = appLocationManager.getLocArray();
            Toast.makeText(MapsActivity.this,mLocationArray.size()+"", Toast.LENGTH_SHORT).show();
            if (appLocationManager.getLocation() != null && !Objects.equals(appLocationManager.getLatitude(), "")) {
//                LatLng latl = new LatLng(Double.valueOf(appLocationManager.getLatitude()), Double.valueOf(appLocationManager.getLongitude()));
//                drawMarker(latl);
                drawAllPoints();
                //showToast("speed from loc: "+appLocationManager.getSpeed()+"");
                //showToast("calculated speed: "+appLocationManager.calcSpeed()+"");

            }

//            if (appLocationManager!=null){
//                if (appLocationManager.calcSpeed()>= 1)
//                    interval= (int) (interval/(appLocationManager.calcSpeed()));
//                Log.i("testFile", "interval = "+ interval);
//            }
            handler.postAtTime(runnable, System.currentTimeMillis() + interval);
            handler.postDelayed(runnable, interval);
        }
    };

    private void drawMarker(LatLng point){
// Creating an instance of MarkerOptions
        MarkerOptions markerOptions = new MarkerOptions();

// Setting latitude and longitude for the marker
        markerOptions.position(point);
        Log.i("testFile", "drawMarker"+point.toString());
// Adding marker on the Google Map
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(point));

    }
    protected void onStop() {
        super.onStop();

        handler.removeCallbacks(runnable);
    }
    protected void onPause(){
        super.onPause();
        Log.i("testFile", "Maps called dump file");
        appLocationManager.dumpToFile();
    }
    protected void onResume(){
        super.onResume();
        Log.i("testFile", "Maps called get from file");
        appLocationManager.getFromFile();
    }
    protected void onDestroy(){
        super.onDestroy();
        //appLocationManager.onStop();



    }
}
