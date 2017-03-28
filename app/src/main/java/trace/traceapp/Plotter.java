package trace.traceapp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.zip.Inflater;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static trace.traceapp.MainActivity.appLocationManager;

/**
 * Created by lotusland on 2/23/17.
 */



public class Plotter extends MainActivity {
    //gps notes
    //7 sigfigs... first two sigfigs
    //1 deg covers 111,111 meters
    public static final double RADIUS = 6378137.0; /* in meters on the equator */
    ArrayList<Location> coordList;
    ArrayList<Double> YpixelCoords;
    ArrayList<Double> XpixelCoords;
    GPSHandler appLocationManager;
    Context context;
    //Handle the following:
        //onpause
        //ondestroy
        //grab buffer info from background services

    Plotter(/*GPSHandler appLocationManager*/){
        coordList = new ArrayList();
        YpixelCoords = new ArrayList();
        XpixelCoords = new ArrayList();
        //take in appLocationManager for local reference
        //this.appLocationManager = appLocationManager;
    }

    //grabs locations from OnLocationChanged()
    //sends graphics and clears buffer every 7 location updates
    public void pushToPlotter(Location loc){
        if(coordList.size() < 7)
            coordList.add(loc);
        else{
            translate();
            clearBuffer(coordList);
            coordList.add(loc);
        }
    }

    public void clearBuffer(ArrayList list){
        list = null;
    }

    //translates lat and long to pixel coords and stores in buffer
    //uses the translation funcitons below
    //does not account for angles or
    public void translate(){

        for(int i = 0; i < coordList.size(); i++){
            YpixelCoords.add(lat2y(coordList.get(i).getLatitude()));
            XpixelCoords.add(lon2x(coordList.get(i).getLongitude()));

            Log.d("XY:: ", String.valueOf(XpixelCoords.get(i)));
            Log.d("XY:: ", String.valueOf(YpixelCoords.get(i)));

        }
    }

    //translates lat and long into x and y coords
    //two funcitons below from
    //http://wiki.openstreetmap.org/wiki/Mercator#Java
    public static double lat2y(double aLat) {
        return Math.log(Math.tan(Math.PI / 4 + Math.toRadians(aLat) / 2)) * RADIUS;
    }
    public static double lon2x(double aLong) {
        return Math.toRadians(aLong) * RADIUS;
    }


    //not currently in use
    //send the coord list to graphics then clear the buffers
    public void sendToGraphics(){


        clearBuffer(YpixelCoords);
        clearBuffer(XpixelCoords);
    }

    //not currently in use
    //retrieve coordinates from the Background Service
    public void loadBackgroundCoords(ArrayList locations){
        coordList = locations;
    }

    //not currently in use
    //retrieve coordinates from the GPS Service
    public void loadForegroundCoords(ArrayList locations){
        coordList = locations;
    }

}
