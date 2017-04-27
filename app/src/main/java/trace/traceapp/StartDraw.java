package trace.traceapp;

/**
 * Created by Drew on 3/21/2017.
 */


import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaScannerConnection;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static android.Manifest.permission_group.LOCATION;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class StartDraw extends Activity {
    DrawView drawView;
    static GPSHandler appLocationManager; //= MainActivity.appLocationManager;
    //ArrayList<Location> mLocationArray = appLocationManager.getLocArray();
    int arraySize;
    private DrawView customView;
    private Button clearDrawingBtn;
    private Button saveDrawingBtn;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appLocationManager = MainActivity.appLocationManager;
        //setContentView(R.layout.activity_main);

        //appLocationManager = new GPSHandler();

        drawView = new DrawView(this);

        drawView.setBackgroundColor(Color.WHITE);
        setContentView(R.layout.custom_layout_draw);
        drawView.setWillNotDraw(false);
        arraySize = drawView.getLocArraySize();
        drawView = (DrawView)findViewById(R.id.custom_view);
        clearDrawingBtn = (Button)findViewById(R.id.clear_drawing);
        clearDrawingBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        appLocationManager.clearLocArray();
                        drawView.invalidate();
                        arraySize = drawView.getLocArraySize();
                    }
                }
        );
        saveDrawingBtn = (Button)findViewById(R.id.save_drawing);
        saveDrawingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                File folder = new File(Environment.getExternalStorageDirectory().toString());
                boolean success = false;
                if (!folder.exists())
                {
                    success = folder.mkdirs();
                }

                System.out.println(success+"folder");

                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/sample.png");

                if ( !file.exists() )
                {
                    try {
                        success = file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println(success+"file");



                FileOutputStream ostream = null;
                try
                {
                    ostream = new FileOutputStream(file);

                    System.out.println(ostream);
                    View targetView = drawView;

                    // myDrawView.setDrawingCacheEnabled(true);
                    //   Bitmap save = Bitmap.createBitmap(myDrawView.getDrawingCache());
                    //   myDrawView.setDrawingCacheEnabled(false);
                    // copy this bitmap otherwise distroying the cache will destroy
                    // the bitmap for the referencing drawable and you'll not
                    // get the captured view
                    //   Bitmap save = b1.copy(Bitmap.Config.ARGB_8888, false);
                    //BitmapDrawable d = new BitmapDrawable(b);
                    //canvasView.setBackgroundDrawable(d);
                    //   myDrawView.destroyDrawingCache();
                    // Bitmap save = myDrawView.getBitmapFromMemCache("0");
                    // myDrawView.setDrawingCacheEnabled(true);
                    //Bitmap save = myDrawView.getDrawingCache(false);
                    Bitmap well = drawView.getBitmap();
                    Bitmap save = Bitmap.createBitmap(320, 480, Bitmap.Config.ARGB_8888);
                    Paint paint = new Paint();
                    paint.setColor(Color.WHITE);
                    Canvas now = new Canvas(save);
                    now.drawRect(new Rect(0,0,320,480), paint);
                    now.drawBitmap(well, new Rect(0,0,well.getWidth(),well.getHeight()), new Rect(0,0,320,480), null);

                    // Canvas now = new Canvas(save);
                    //myDrawView.layout(0, 0, 100, 100);
                    //myDrawView.draw(now);
                    if(save == null) {
                        System.out.println("NULL bitmap save\n");
                    }
                    save.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                    //scanPhoto("sample.png");
                    //bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                    ostream.flush();
                    ostream.close();
                }catch (NullPointerException e)
                {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Null error", Toast.LENGTH_SHORT).show();
                }

                catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "File error", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });

    }

    public void scanPhoto(final String imageFileName) {
         MediaScannerConnection msConn = null;
        final MediaScannerConnection.MediaScannerConnectionClient msConC = new MediaScannerConnection.MediaScannerConnectionClient() {
            public void onMediaScannerConnected() {
                //msConn.scanFile(imageFileName, null);
                final Boolean flag = false;
                Log.i("msClient",
                        "connection established");
            }

            public void onScanCompleted(String path, Uri uri) {
               // msConn.disconnect();
                //flag = new Boolean(true);
                Log.i("msClient", "scan completed");
            }
        };
         msConn = new MediaScannerConnection(this,msConC);
        msConn.connect();
        msConn.scanFile(imageFileName, null);


//        msConn.scanFile(imageFileName, null);
//        msConn.disconnect();
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
            Log.i("test", "i am running");
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

