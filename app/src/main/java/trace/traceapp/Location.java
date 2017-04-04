package trace.traceapp;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by david on 4/4/2017.
 */

public class Location {

    public String title;
    public String statsLabel;
    public String addressLabel;
    public float totalDistance;
    public String mostVisited;

   /* public static ArrayList<Location> getLocationsFromFile(String filename, Context context)
    {
        final ArrayList<Location> locationList = new ArrayList<>();

        try {

            String jsonString = loadJsonFromAsset("locations.json", context);
            JSONObject json = new JSONObject(jsonString);
            JSONArray locations = json.getJSONArray("locations");

        }

        return 0;
    } */

}
