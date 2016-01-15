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
public class Computer extends Fragment {


    public Computer() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_computer, container, false);


    }
    public static Computer newInstance(String text) {

        Computer f = new Computer();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }
}
