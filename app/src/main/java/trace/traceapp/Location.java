package trace.traceapp;

import android.content.Context;

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
 */

public class Location {

    public String title;
    public String statsLabel;
    public String addressLabel;
    public float totalDistance;
    public String mostVisited;

   public static ArrayList<Location> getLocationsFromFile(String filename, Context context)
    {
        final ArrayList<Location> locationList = new ArrayList<>();

        try {
            //grab strings from json file in assets folder
            String jsonString = loadJsonFromAsset("locations.json", context);
            //create an object to assign strings.
            JSONObject json = new JSONObject(jsonString);
            //array to hold objects of JSON
            JSONArray locations = json.getJSONArray("locations");

            for(int i = 0; i <locations.length(); i++) {
                Location location = new Location();
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
