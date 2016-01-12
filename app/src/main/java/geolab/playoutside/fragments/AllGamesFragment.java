package geolab.playoutside.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import geolab.playoutside.R;
import geolab.playoutside.adapters.MyStickyAdapter;
import geolab.playoutside.db.Data;
import geolab.playoutside.model.MyEvent;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by GeoLab on 1/11/16.
 */
public class AllGamesFragment extends android.support.v4.app.Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.sport_fragment, container, false);

        StickyListHeadersListView list = (StickyListHeadersListView) v.findViewById(R.id.list);
        MyStickyAdapter adapter = new MyStickyAdapter(getActivity(), getData());
        list.setAdapter(adapter);

        list.setOnItemClickListener(adapter.listener);


        return v;
    }

    private ArrayList<MyEvent> getData() {
        ArrayList<MyEvent> models = new ArrayList<>();
        for (int i = 0; i < Data.time.length; i++) {
            MyEvent model = new MyEvent(Data.time[i],Data.time[i], Data.title[i], Data.description[i], Data.place[i], Data.player[i], Data.id[i]);
            models.add(model);
        }

        return models;
    }

    public static AllGamesFragment newInstance(String text) {

        AllGamesFragment f = new AllGamesFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }
}
