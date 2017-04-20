package trace.traceapp;

import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.LayoutInflater;


import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by david on 4/4/2017.
 * code was found and used through a tutorial at
 * https://www.raywenderlich.com/124438/android-listview-tutorial
 *
 * Location class is used to grab data from a json file
 *
 * JSON is JavaScript Object Notation, and is a way to store information
 * in a readable manner and is easy to access.
 *
 * Total Distance - double
 * Name - name of the user
 * Home - name of the location Node
 * Home_Count - How many times you have visited a certain location
 * MostFreq  - Name of most frequently lcoation
 * MostFreqCount - number of most frequently visited location.
 *
 *
 */


/*
public class Loc extends CursorAdapter {
    public Loc(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    //@Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.activity_stats, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    //@Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView Name = (TextView) view.findViewById(R.id.textView5);
        TextView totalDistance = (TextView) view.findViewById(R.id.textView4);
        TextView mostVisitedLoc = (TextView) view.findViewById(R.id.textView3);
        // Extract properties from cursor
        String nameExtract = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        double totalDistExtract = cursor.getInt(cursor.getColumnIndexOrThrow("total distance"));
        double mostVisitExtract = cursor.getInt(cursor.getColumnIndexOrThrow("most frequent"));
        // Populate fields with extracted properties
        Name.setText(nameExtract);
        totalDistance.setText(String.valueOf(totalDistExtract));
        mostVisitedLoc.setText(String.valueOf(mostVisitExtract));
    }

}*/


public class Loc{

    public String title;
    public String statsLabel;
    public String addressLabel;
    public float totalDistance;
    public String mostVisited;

   public static ArrayList<Loc> getLocationsFromFile(String filename, Context context)
    {
        final ArrayList<Loc> locationList = new ArrayList<>();

        try {
            //grab strings from json file in assets folder
            String jsonString = loadJsonFromAsset("locations.json", context);
            //create an object to assign strings.
            JSONObject json = new JSONObject(jsonString);
            //array to hold objects of JSON
            JSONArray locations = json.getJSONArray("locations");

            for(int i = 0; i <locations.length(); i++) {
                Loc location = new Loc();
                //create object to pass string.

                location.title = locations.getJSONObject(i).getString("title");
                //location.statsLabel = locations.getJSONObject(i).getString("statsLabel");
                //location.addressLabel = locations.getJSONObject(i).getString("addressLabel");
                //location.totalDistance = locations.getJSONObject(i).getFloat("totalDistance");

                //add to array
                locationList.add(location);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return locationList;
    }

    private static String loadJsonFromAsset(String filename, Context context)
    {
        String json = null;

        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        }
        catch (java.io.IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return json;
    }


}



