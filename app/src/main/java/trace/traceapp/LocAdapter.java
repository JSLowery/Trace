package trace.traceapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by david on 4/19/2017.
 */

public class LocAdapter  extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Loc> mDataSource;
    public LocAdapter(Context context, ArrayList<Loc> items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get view for row item
        View rowView = mInflater.inflate(R.layout.activity_stats, parent, false);
        // Get title element
        TextView titleTextView =
                (TextView) rowView.findViewById(R.id.Loc_title);
// Get subtitle element
        TextView subtitleTextView =
                (TextView) rowView.findViewById(R.id.Loc_subtitle);

// Get detail element
        TextView detailTextView =
                (TextView) rowView.findViewById(R.id.Loc_detail);

        Loc location = (Loc) getItem(position);

// 2
        titleTextView.setText(location.title);
       //subtitleTextView.setText(location.description);
        //detailTextView.setText(location.label)
        return rowView;
    }
}
