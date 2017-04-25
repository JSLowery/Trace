package trace.traceapp;

/**
 * Created by Drew on 3/21/2017.
 */

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class DrawView extends View {
    Paint paint = new Paint();
    private Location location;
    private double longList[];// = {151,151,152,152,151, 150}; //test longitude values
    private double latList[];// = {-35,-36,-35,-36,-35,-35.5};  //test latitude values
    private float result[] = new float[3];
    private static final String TAG = "DrawActivity";

    private int w;
    private int h;
    private double metPerPx;
    private static final double downScale = 1.2;

    ArrayList<Location> mLocationArray = appLocationManager.getLocArray();
    static GPSHandler appLocationManager  = MainActivity.appLocationManager;

    public DrawView(Context context) {
        super(context);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);

    }

    @Override
    public void onDraw(Canvas canvas) {
        int xPrev, yPrev;
        int xNext, yNext;
        int xChange, yChange;
        int quadrant;
        float angle, dist;
        double longRange[] = new double[2];
        double latRange[] = new double[2];

        appLocationManager = MainActivity.appLocationManager;
        mLocationArray =appLocationManager.getLocArray();

        if (mLocationArray.size() > 1) {
            //BEGIN to find how many meters per pixel for scale
            populateLatList();
            populateLongList();
            //first we must find the range of lat and long
            findRange(longList, longRange);//places minLong in longRange[0], maxLong in longRange[1]
            findRange(latList, latRange);//places minLat in latRange[0], maxLat in latRange[1]
            metPerPx = findMetPerPx(longRange, latRange);//Based on range, finds the max distance possible
            //and returns how many meters per pixel is possible within that range for scale
            //END

            //BEGIN to find (x,y) coord to place the first (lat,long) point on canvas
            //(latRange[0], longRange[0]) is min (lat,long), (latRange[1], longRange[1]) is max (lat,long)
            //First we must find the relative (x,y) coord of max and min (lat,long)
            //distanceBetween of min (lat,long) and max (lat,long)
            location.distanceBetween(latRange[0], longRange[0], latRange[1], longRange[1], result);
            dist = result[0];//dist is distance between (lat,long) min and max in meters
            angle = (result[1] + result[2]) / 2;//angle is angle between (lat,long) min and max in degrees
            quadrant = findQuadrant(angle);//quadrant is which quadrant of an x,y plain of angle (1-4)
            xChange = findXChange(quadrant, dist, angle);//returns pixel change on x-axis in int
            yChange = findYChange(quadrant, dist, angle);//returns pixel change on y-axis in int
            xPrev = findXStart(quadrant, xChange);//returns relative x value of min (lat,long)
            yPrev = findYStart(quadrant, yChange);//returns relative y value of min (lat,long)
            xNext = xPrev + xChange;//xNext is now relative x value of max (lat,long)
            yNext = yPrev + yChange;//yNext is now relative y value of max (lat,long)
            //distanceBetween of max (lat,long) and first (lat,long)
            location.distanceBetween(latRange[1], longRange[1], latList[0], longList[0], result);
            dist = result[0];//dist is distance between max (lat,long) and first (lat,long) in meters
            angle = (result[1] + result[2]) / 2;//angle is angle between max (lat,long) and first (lat,long) in degrees
            quadrant = findQuadrant(angle);//quadrant is which quadrant of an x,y plain of angle (1-4)
            xChange = findXChange(quadrant, dist, angle);//returns pixel change on x-axis in int
            yChange = findYChange(quadrant, dist, angle);//returns pixel change on y-axis in int
            xPrev = xNext;//set xPrev to x value of max (lat,long)
            yPrev = yNext;//set yPrev to y value of max (lat,long)
            xNext = xPrev + xChange;//xNext is now x value of first (lat,long)
            yNext = yPrev + yChange;//yNext is now y value of first (lat,long)
            //END

            //BEGIN drawing (lat,long) points now that we have the relative (x,y) values for the first
            //(lat,long) point
            for (int i = 1; i < latList.length; i++) {
                xPrev = xNext;//set xPrev to xNext for line continuity
                yPrev = yNext;//set yPrev to yNext for line continuity
                //distanceBetween of prev (lat,long) and next (lat,long)
                location.distanceBetween(latList[i - 1], longList[i - 1], latList[i], longList[i], result);
                dist = result[0];
                angle = (result[1] + result[2]) / 2;
                quadrant = findQuadrant(angle);
                xChange = findXChange(quadrant, dist, angle);
                yChange = findYChange(quadrant, dist, angle);
                xNext = xPrev + xChange;
                yNext = yPrev + yChange;
                canvas.drawLine(xPrev, yPrev, xNext, yNext, paint);//draw line from (xPrev,yPrev) to (xNext,yNext)
                //output for testing
                Log.d(TAG, "Distance " + String.valueOf(i) + "........" + String.valueOf(dist));
                Log.d(TAG, "Bearing " + String.valueOf(i) + "........." + String.valueOf(angle));
                Log.d(TAG, "Theta (rads) " + String.valueOf(i) + "...." + String.valueOf(findTheta(angle)));
                Log.d(TAG, "Sin " + String.valueOf(i) + "............." + String.valueOf(Math.sin(degToRad(angle))));
                Log.d(TAG, "xPrev " + String.valueOf(i) + "..........." + String.valueOf(xPrev));
                Log.d(TAG, "yPrev " + String.valueOf(i) + "..........." + String.valueOf(yPrev));
                Log.d(TAG, "xChange " + String.valueOf(i) + "........." + String.valueOf(xChange));
                Log.d(TAG, "yChange " + String.valueOf(i) + "........." + String.valueOf(yChange));
                //loop until end of (lat,long) points
            }
        }
        //END
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        this.w = w;
        this.h = h;
        super.onSizeChanged(w, h, oldW, oldH);
    }

    public int getLocArraySize(){return mLocationArray.size();}

    private void populateLatList(){
        latList = new double[mLocationArray.size()];
        for(int i = 0; i < mLocationArray.size(); i++){
            Location loc = mLocationArray.get(i);
            latList[i] = loc.getLatitude();
        }
    }

    private void populateLongList(){
        longList = new double[mLocationArray.size()];
        for(int i = 0; i < mLocationArray.size(); i++){
            Location loc = mLocationArray.get(i);
            longList[i] = loc.getLongitude();
        }
    }

    private void findRange(double list[], double result[]){
        double min = 180;
        double max = -180;
        for (int i = 0; i < list.length; i++){
            if (list[i] < min) {min = list[i];}
            if (list[i] > max) {max = list[i];}
        }
        result[0] = min;
        result[1] = max;
    }

    private double findAvg(double list[]){
        double sum = 0;
        for (int i = 0; i < list.length; i++) {sum += list[i];}
        return sum / list.length;
    }

    private double findMetPerPx (double longRange[], double latRange[]){
        double longAvg = findAvg(longRange), latAvg = findAvg(latRange), xMax, yMax;
        float result[] = new float [3];

        location.distanceBetween(latAvg,longRange[0],latAvg,longRange[1],result);
        xMax = (result[0] / w) * downScale;
        location.distanceBetween(latRange[0],longAvg,latRange[1],longAvg,result);
        yMax = (result[0] / h) * downScale;

        if (xMax > yMax){return xMax;}
        else {return yMax;}
    }

    private int findQuadrant (float angle){
        if (angle <= -90){return 3;}
        else if (angle <= 0){return 1;}
        else if (angle <= 90){return 2;}
        else {return 4;}
    }

    private double degToRad(double deg) {return (deg * Math.PI / 180.0);}

    private double findTheta(float angle){
        switch (findQuadrant(angle)) {
            case 1: return degToRad(-angle);
            case 2: return degToRad(angle);
            case 3: return degToRad(-angle -90);
            case 4: return degToRad(angle -90);
        }
        return -1;
    }

    private float findAdjOffset(float dist, float angle){
        double theta = findTheta(angle);
        return (float) (Math.cos(theta) * dist);
    }

    private float findOppOffset(float dist, float angle){
        double theta = findTheta(angle);
        return (float) (Math.sin(theta) * dist);
    }

    private int findXChange(int quadrant, float dist, float angle){
        switch (quadrant) {
            case 1: return (int) ((findOppOffset(dist, angle) / metPerPx) * -1);
            case 2: return (int) ((findOppOffset(dist, angle) / metPerPx));
            case 3: return (int) ((findAdjOffset(dist, angle) / metPerPx) * -1);
            case 4: return (int) ((findAdjOffset(dist, angle) / metPerPx));
        }
        return -1;
    }

    private int findYChange(int quadrant, float dist, float angle) {
        switch (quadrant) {
            case 1: return (int) ((findAdjOffset(dist, angle) / metPerPx) * -1);
            case 2: return (int) ((findAdjOffset(dist, angle) / metPerPx) * -1);
            case 3: return (int) ((findOppOffset(dist, angle) / metPerPx));
            case 4: return (int) ((findOppOffset(dist, angle) / metPerPx));
        }
        return -1;
    }

    private int findXStart(int quadrant, int xChange){
        switch (quadrant) {
            case 1: return w - (w - Math.abs(xChange))/2;
            case 2: return (w - Math.abs(xChange))/2;
            case 3: return w - (w - Math.abs(xChange))/2;
            case 4: return (w - Math.abs(xChange))/2;
        }
        return -1;
    }

    private int findYStart(int quadrant, int yChange){
        switch (quadrant) {
            case 1: return h - (h - Math.abs(yChange))/2;
            case 2: return h - (h - Math.abs(yChange))/2;
            case 3: return (h - Math.abs(yChange))/2;
            case 4: return (h - Math.abs(yChange))/2;
        }
        return -1;
    }

}