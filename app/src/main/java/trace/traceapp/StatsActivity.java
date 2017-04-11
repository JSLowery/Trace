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
        // 1 loads a list of Location objects from a JSON asset in the app
        //contains a Location class that contains all the information
        //about the locations to be displayed.
        final ArrayList<Loc> locationList = Loc.getLocationsFromFile("locations.json", this);
        // 2 creates an array of strings that will contain the text to be displayed.
        String[] listItems = new String[locationList.size()];

        // 3 Populates the listView with the titles of the locations lodaed in section one.
        for (int i =0; i< locationList.size(); i++)
        {
            Loc location = locationList.get(i);
            listItems[i] = location.title;
            //listItems[i] = location.addressLabel;
        }

        // 4 creates and sets a simpl adapter for the listView.
        //it takes in the current context, a layout file what each row should look like
        // and the data that will populate the list as arguments.
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems);
        //ArrayAdapter subadapter = new ArrayAdapter(this, android.R.layout.simple_list_item_2, listItems);
        locListView.setAdapter(adapter);
        //locListView.setAdapter(subadapter);
    }

    //An adapter loads information to be displayed from a data source, then inserts
    //the views into the listView.
    //ListView is a subclass of AdapterView.
    //it fetches the items to be displayed, and decides how it should be
    //displayed, and passese the info on to the ListView

    /*
    RIght now, this is a simple ArrayAdapter that will purely grab the title from the
    JSON file and display it as the TITLE for each list view. In order to create more details
    such as address, stats, etc. I need to build an adapter class that will extend off
    of the BaseAdapter class and inherit its get methods and set methods. I will have
    to create a constructor to hand the subtext in each list view as well as other
    information.
     */






}

