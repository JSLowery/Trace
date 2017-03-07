package trace.traceapp;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.location.LocationListener;

/**
 * Created by tenos on 2/28/17.
 */

public class GPSBGNDS extends IntentService implements LocationListener {
    LocationManager locationManager;
    Location location;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public GPSBGNDS(String name) {
        super(name);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
