package com.corp.trace.trace;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.location.LocationSettingsResult;

/**
 * Created by lotusland on 2/2/17.
 */

public class GPSHandler extends LocationManager {
    GPSHandler() {

    }
}

//    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
//            .addLocationRequest(mLocationRequest);
//    PendingResult<LocationSettingsResult> result =
//            LocationServices.SettingsApi.checkLocationSettings(mGoogleClient,
//                    builder.build());
//
//
//
//    protected void createLocationRequest() {
//        LocationRequest mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(10000);
//        mLocationRequest.setFastestInterval(5000);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//    }
//    result.setResultCallback(new ResultCallback<LocationSettingsResult>()) {
//        @Override
//        public void onResult(LocationSettingsResult result) {
//            final Status status = result.getStatus();
//            final LocationSettingsStates = result.getLocationSettingsStates();
//            switch (status.getStatusCode()) {
//                case LocationSettingsStatusCodes.SUCCESS:
//                    // All location settings are satisfied. The client can
//                    // initialize location requests here.
//                    break;
//                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//                    // Location settings are not satisfied, but this can be fixed
//                    // by showing the user a dialog.
//                    try {
//                        // Show the dialog by calling startResolutionForResult(),
//                        // and check the result in onActivityResult().
//                        status.startResolutionForResult(
//                                OuterClass.this,
//                                REQUEST_CHECK_SETTINGS);
//                    } catch (SendIntentException e) {
//                        // Ignore the error.
//                    }
//                    break;
//                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
//                    // Location settings are not satisfied. However, we have no way
//                    // to fix the settings so we won't show the dialog.
//
//                    break;
//            }
//        }
//    });


//    private Location loc;
//    int status;
//
//    public Location getLocation(){
//        return this.loc;
//    }
//
//    private Location setLocation(Location l){
//        this.loc = l;
//    }
//    // Acquire a reference to the system Location Manager
//    LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
//
//    // Define a listener that responds to location updates
//    LocationListener locationListener = new LocationListener() {
//        public void onLocationChanged(Location location) {
//            // Called when a new location is found by the network location provider.
//            setLocation(location);
//        }
//
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//            status = 1;
//            setLocation(getLocation());
//        }
//
//        public void onProviderEnabled(String provider) {
//            //getProvider();
//        }
//
//        public void onProviderDisabled(String provider) {}
//    };
//
//// Register the listener with the Location Manager to receive location updates
//    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
//
//
//    //boolean addGpsStatusListener (GpsStatus.Listener listener)

