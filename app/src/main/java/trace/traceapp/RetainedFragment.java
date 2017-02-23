package trace.traceapp;

import android.app.Fragment;
import android.os.Bundle;

/**
 * Created by tenos on 2/17/17.
 */
public class RetainedFragment extends Fragment {

    // data object we want to retain
    private GPSHandler data;

    // this method is only called once for this fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }

    public void setData(GPSHandler data) {
        this.data = data;
    }

    public GPSHandler getData() {
        return data;
    }
}