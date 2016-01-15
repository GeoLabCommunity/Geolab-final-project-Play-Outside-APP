package geolab.playoutside.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import geolab.playoutside.Add_Event_Activity;
import geolab.playoutside.MainActivity;
import geolab.playoutside.R;

/**
 * Created by GeoLab on 1/15/16.
 */
public class DialogFragment extends android.app.DialogFragment{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle("For creating new event you need to LOG IN !")
                .setMessage("Do you want to login via facebook?")
                .setIcon(R.drawable.fb)
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent addEvent = new Intent(getActivity(), Add_Event_Activity.class);
                        startActivity(addEvent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(),"Canceled", Toast.LENGTH_SHORT).show();

                    }
                })
                .show();
    }

}
