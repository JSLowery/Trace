package trace.traceapp;

import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Objects;



public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<Location> mLocationArray;
    private ArrayList<locNode> mLocNodeArray;
    static GPSHandler appLocationManager;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
         mContext = getApplicationContext();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
        appLocationManager = MainActivity.appLocationManager;
         mLocationArray =appLocationManager.getLocArray();
        mLocNodeArray = appLocationManager.getLocNodeArr();
        Button buttonClear = (Button) findViewById(R.id.ClearButton);
        buttonClear.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0){
                appLocationManager.clearLocArray();
                mMap.clear();
                drawAllPoints();
            }
        });
        Button buttonOpenDialog = (Button) findViewById(R.id.SaveNodeButton);
        buttonOpenDialog.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0){
                openDialog();
            }
        });


    }
    void openDialog(){
        DialogFragment myDialogFrag = LocNodeFrag.newInstance();
        myDialogFrag.show(getSupportFragmentManager(),"dialog");
    }
    public void saveClicked(EditText input){
        showToast("saved "+input.getEditableText());
        appLocationManager.makeLocNode(input.getEditableText()+"");
        ArrayList<locNode> arr = appLocationManager.getLocNodeArr();
        showToast(arr.get(0).getLocAddress());
        drawAllPoints();
    }
    public void cancelClicked(){
        showToast("Not Saved");
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
    public void drawAllPoints(){
        //mMap.clear();
        mLocNodeArray = appLocationManager.getLocNodeArr();
        double lat = 40.3216491;
        double lng = -75.9911328;
        for (int i = 0;i<mLocNodeArray.size();i++) {

            if (mLocNodeArray.size() > 0) {
                lat = mLocNodeArray.get(i).getLocLatCoord();
                lng = mLocNodeArray.get(i).getLocLongCoord();
            }

            // Add a marker and move the camera mLocationArray.get(0).getLatitude() mLocationArray.get(0).getLongitude()
            LatLng marker = new LatLng(lat, lng);
            drawMarker(marker, mLocNodeArray.get(i).getLocName(), mLocNodeArray.get(i).getLocAddress(), mLocNodeArray.get(i).getTimesVisited());

            Log.i("testFile", "Added: "+marker.latitude+" "+marker.longitude);

        }
        LatLng sydney = new LatLng(lat, lng);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.i("testfile", "on map ready called setting up locationenabled");
        showToast("on map ready called");
        appLocationManager.setMapRef(mMap);
        mMap.setMyLocationEnabled(true);
        mMap.setLocationSource(appLocationManager);

        mLocationArray = appLocationManager.getLocArray();
        String size = mLocationArray.size()+"";
        showToast(size);
        drawAllPoints();

        mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
        mMap.setMaxZoomPreference(20.0f);
        mMap.setMinZoomPreference(5.0f);
//        handler.postAtTime(runnable, System.currentTimeMillis()+interval);
//        handler.postDelayed(runnable, interval/6);
        if (appLocationManager.getLatLng()!= null)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(appLocationManager.getLatLng()));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {

            public boolean onMyLocationButtonClick() {

                return false;
            }
        });


        //appLocationManager.drawPoly();
    }
    private int interval = 1000*6; // 6 Second
//    private Handler handler = new Handler();
//    private Runnable runnable = new Runnable(){
//        public void run() {
//            Log.i("testFile", "runnable fired");
//
//            int arrSize = mLocationArray.size();
//            mLocationArray = appLocationManager.getLocArray();
//            Toast.makeText(MapsActivity.this,mLocationArray.size()+"", Toast.LENGTH_SHORT).show();
//
//
//
//            handler.postAtTime(runnable, System.currentTimeMillis() + interval);
//            handler.postDelayed(runnable, interval);
//        }
//    };

    private void drawMarker(LatLng point, String name, String address, int timesVis){
// Creating an instance of MarkerOptions
        MarkerOptions markerOptions = new MarkerOptions();

// Setting latitude and longitude for the marker
        markerOptions.position(point)
        .title(name)
        .snippet(address + "\nTimesVisited: "+ timesVis)
        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        Log.i("testFile", "drawMarker"+point.toString());
// Adding marker on the Google Map
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                LinearLayout info = new LinearLayout(mContext);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(mContext);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(mContext);
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });
        mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(point));

    }

    protected void onStop() {
        super.onStop();

//        handler.removeCallbacks(runnable);
    }
    protected void onPause(){
        super.onPause();
        Log.i("testFile", "Maps called dump file");
        mMap.setMyLocationEnabled(false);
        appLocationManager.switchTo_ScreenOff_Updates();
    }
    protected void onResume(){
        super.onResume();
        //mMap.setMyLocationEnabled(true);
        Log.i("testFile", "Maps called get from file");
        appLocationManager.getFromFile();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        appLocationManager.switchTo_ScreenOn_Updates();
//        handler.postAtTime(runnable, System.currentTimeMillis()+interval);
//        handler.postDelayed(runnable, interval/6);
    }
    protected void onDestroy(){
        super.onDestroy();
        //appLocationManager.onStop();



    }
}
