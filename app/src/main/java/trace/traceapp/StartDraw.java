package trace.traceapp;

/**
 * Created by Drew on 3/21/2017.
 */


import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

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

import java.util.ArrayList;

import static android.Manifest.permission_group.LOCATION;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class StartDraw extends Activity {
    DrawView drawView;
    static GPSHandler appLocationManager; //= MainActivity.appLocationManager;
    //ArrayList<Location> mLocationArray = appLocationManager.getLocArray();
    int arraySize;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appLocationManager = MainActivity.appLocationManager;
        setContentView(R.layout.activity_main);

        //appLocationManager = new GPSHandler();

        drawView = new DrawView(this);

        drawView.setBackgroundColor(Color.WHITE);
        setContentView(drawView);
        drawView.setWillNotDraw(false);
        arraySize = drawView.getLocArraySize();
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        appLocationManager.getFromFile();

        //if (arraySize <= drawView.getLocArraySize()){

            arraySize = drawView.getLocArraySize();
            handler.postAtTime(runnable, System.currentTimeMillis()+interval);
            handler.postDelayed(runnable, interval/6);
        //}
    }

    private int interval = 1000*6; // 6 Second
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        public void run() {
            drawView.invalidate();

            arraySize = drawView.getLocArraySize();

            handler.postAtTime(runnable, System.currentTimeMillis()+interval);
            handler.postDelayed(runnable, interval/6);
        }
    };

    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }

    protected void onPause(){
        super.onPause();

    }
}

