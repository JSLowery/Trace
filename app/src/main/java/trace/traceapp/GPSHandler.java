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

/**
 * Created by tenos on 2/10/17.
 */

public class GPSHandler implements LocationListener {
    private LocationManager locationManager;
    private String latitude = "";
    private String longitude = "";
    private Criteria criteria;
    private String provider;

    public GPSHandler(Context context) {
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
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1,
                0, this);
        setMostRecentLocation(locationManager.getLastKnownLocation(provider));

    }
    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
    private void setMostRecentLocation(Location lastKnownLocation) {
        double lon;
        lon = (double) lastKnownLocation.getLongitude();
        double lat;
        lat = (double) lastKnownLocation.getLatitude();
        longitude = lon + "";
        latitude = lat + "";
    }
    @Override
    public void onLocationChanged(Location location) {
        double lon = (double) (location.getLongitude());/// * 1E6);
        double lat = (double) (location.getLatitude());// * 1E6);

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
