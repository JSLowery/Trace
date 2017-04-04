package trace.traceapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class StatsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        myListView = (ListView) findViewById(R.id.LocationList);
        final ArrayList<String> names = new ArrayList<>();
    }

    /*private String[] name = {
            "Jeremy",
            "Drew",
            "Vinny",
            "Alan",
            "Dave"
    };

    ArrayAdapter<String> nameAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, name);

    ListView listView = (ListView) findViewById(R.id.LocationList);
    listView.setAdapter(itemsAdapter);
*/
    private ListView myListView;





}

