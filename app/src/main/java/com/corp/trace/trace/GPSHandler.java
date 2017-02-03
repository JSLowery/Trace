package com.corp.trace.trace;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

/**
 * Created by lotusland on 2/2/17.
 */

public class GPSHandler implements LocationManager{

    private Location loc;
    int status;

    public Location getLocation(){
        return this.loc;
    }

    private Location setLocation(Location l){
        this.loc = l;
    }
    // Acquire a reference to the system Location Manager
    LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

    // Define a listener that responds to location updates
    LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            // Called when a new location is found by the network location provider.
            setLocation(location);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            status = 1;
            setLocation(getLocation());
        }

        public void onProviderEnabled(String provider) {
            //getProvider();
        }

        public void onProviderDisabled(String provider) {}
    };

// Register the listener with the Location Manager to receive location updates
    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);


    //boolean addGpsStatusListener (GpsStatus.Listener listener)
}
