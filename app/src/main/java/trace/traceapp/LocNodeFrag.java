package trace.traceapp;

import android.app.AlertDialog;

import android.app.Dialog;
//import android.app.DialogFragment;
import android.content.DialogInterface;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Created by tenos on 4/18/17.
 */

public class LocNodeFrag extends DialogFragment {
    static LocNodeFrag newInstance(){
        LocNodeFrag mLNF = new LocNodeFrag();
        Bundle args = new Bundle();
        args.putString("title", "Dialog Frag");
        mLNF.setArguments(args);
        return mLNF;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        String title = getArguments().getString("title");
        final EditText input = new EditText(getActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        Dialog myDialog = new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.ic_media_route_connecting_10_light)
                .setTitle(title)
                .setMessage("Save This Location?")
                .setView(input)
                .setPositiveButton("Save", new DialogInterface.OnClickListener(){
                @Override
                        public void onClick(DialogInterface dialog, int which){
                    ((MapsActivity)getActivity()).saveClicked(input);
                }
        })
                .setNegativeButton("Cancel",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                ((MapsActivity)getActivity()).cancelClicked();
            }
        }).create();
        return myDialog;
    }
}
