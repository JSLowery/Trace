package trace.traceapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.content.Intent;
import android.view.View;
import android.graphics.BitmapFactory;
import android.database.Cursor;
import android.widget.ImageView;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.TextView;

import java.util.ArrayList;

public class StatsActivity extends AppCompatActivity {
    private static GPSHandler appLocationManager = MainActivity.appLocationManager;
    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;
    StatsDB2 statsdb;

    private ListView locListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        locListView = (ListView) findViewById(R.id.LocationList);

        statsdb = StatsDB2.getInstance(getApplicationContext());
        // 1 loads a list of Location objects from a JSON asset in the app
        //contains a Location class that contains all the information
        //about the locations to be displayed.
        final ArrayList<locNode> locationList = appLocationManager.getLocNodeArr();
        // 2 creates an array of strings that will contain the text to be displayed.
        String[] listItems = new String[locationList.size()];

        // 3 Populates the listView with the titles of the locations lodaed in section one.
        for (int i =0; i< locationList.size(); i++)
        {
            locNode location = locationList.get(i);
            listItems[i] = location.getLocName() + " "+ location.getTimesVisit();
            //listItems[i] = location.getTimesVisit()+"";
        }
        TextView nameText = (TextView) findViewById(R.id.usernameStats);
        nameText.setText("Name: " + statsdb.getName());
        TextView distanceText = (TextView) findViewById(R.id.tdistanceStats);
        distanceText.setText("Traveled: " + statsdb.getDistance() + "mi");
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

    //query statement to get name of user and place it into textview
    //textview = ("Name: " + SELECT USER_NAME);

    //query statement to get total distance and place double into text view.

    //query statement to get most visited location.

    public void loadImagefromGallery(View view) {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    @Override
    //Don't know completely how it works but it uses an intent and it brings in a
    //photo from the gallery. Because it uses an intent, we need to create a file
    //internally on the device and reference to it whenever they go to the stats page.
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data) {
            //Represents a Uniform Resource Identifier (URI) reference.
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = (ImageView) findViewById(R.id.imageView2);
            //need to find a way to scale image to the imageview
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }
    }
}

