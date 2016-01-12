package geolab.playoutside.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import geolab.playoutside.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TableGameFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TableGameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TableGameFragment extends Fragment {



    public TableGameFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_table_game, container, false);
    }



    public static TableGameFragment newInstance(String text) {

        TableGameFragment f = new TableGameFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }
}
