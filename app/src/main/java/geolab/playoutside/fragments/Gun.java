package geolab.playoutside.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import geolab.playoutside.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Gun extends Fragment {


    public Gun() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gun, container, false);

    }
    public static Gun newInstance(String text) {

        Gun f = new Gun();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }

}
