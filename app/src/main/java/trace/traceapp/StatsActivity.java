package trace.traceapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class StatsActivity extends AppCompatActivity {

    private ListView locListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        locListView = (ListView) findViewById(R.id.LocationList);
        // 1 loads a list of Recipe objects from a JSON asset in the app
        //this starter project contains a Recipe class that contains all the information
        //about the recipes to be displayed.
        final ArrayList<Location> locationList = Location.getLocationsFromFile("locations.json", this);
        // 2 creates an array of strings that will contain the text to be displayed.
        String[] listItems = new String[locationList.size()];

        // 3 Populates the listView with the titles of the recipes lodaed in section one.
        for (int i =0; i< locationList.size(); i++)
        {
            Location location = locationList.get(i);
            listItems[i] = location.title;
            //listItems[i] = location.addressLabel;
        }

        // 4 creates and sets a simpl adapter for the listView.
        //it takes in the current context, a layout file what each row should look like
        // and the data that will populate the list as arguments.
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems);
        locListView.setAdapter(adapter);
    }

    //An adapter loads information to be displayed from a data source, then inserts
    //the views into the listView.






}

