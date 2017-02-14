package trace.traceapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.LocationRequest;

import java.util.Random;

/**
 * Created by tenos on 2/10/17.
 * This is the Class that will store and handle GPS information
 */

class GPSHandler implements LocationListener {
    private LocationManager locationManager;
    private String latitude = "";
    private String longitude = "";
    private Criteria criteria;
    private String provider;

    GPSHandler(Context context) {
        locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        provider = locationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            int permissionCheck = ContextCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_FINE_LOCATION);
            Log.d("LOOK AT MEE", "test");
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000,
                10, this);
        Location curLoc = locationManager.getLastKnownLocation(provider);
        if (curLoc!= null){
            setMostRecentLocation(curLoc);
        }

    }
    protected void createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
    String getLatitude() {
        return latitude;
    }

    String getLongitude() {
        return longitude;
    }
    private void setMostRecentLocation(Location lastKnownLocation) {
        double lon;
        lon = lastKnownLocation.getLongitude();
        double lat;
        lat = lastKnownLocation.getLatitude();
        longitude = lon + "";
        latitude = lat + "";
    }
    @Override
    public void onLocationChanged(Location location) {
        Log.i("Location", "lat "+location.getLatitude()+" lon "+location.getLongitude());
//        Random r = new Random();
//        int Low = 10;
//        int High = 100;
//        String str = r.nextInt(High-Low) + Low+","+r.nextInt(High-Low) + Low;
//        String[] MockLoc = str.split(",");
//        LocationManager mocLocationProvider = (LocationManager)  Context.getSystemService(Context.LOCATION_SERVICE);
//        Location location1 = new Location(mocLocationProvider);
//        Double lat = Double.valueOf(Moc100kLoc[0]);
//        location1.setLatitude(lat);
//        Double longi = Double.valueOf(MockLoc[1]);
//        location1.setLongitude(longi);
//        Double alti = Double.valueOf(MockLoc[2]);
//        location1.setAltitude(alti);
        double lon = location.getLongitude();/// * 1E6);
        double lat = location.getLatitude();// * 1E6);

//      int lontitue = (int) lon;
//      int latitute = (int) lat;
        latitude = lat + "";
        longitude = lon + "";

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
