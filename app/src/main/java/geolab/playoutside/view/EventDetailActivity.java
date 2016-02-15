package geolab.playoutside.view;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.Bind;
import butterknife.ButterKnife;
import geolab.playoutside.R;
import geolab.playoutside.model.MyEvent;

public class EventDetailActivity extends AppCompatActivity {

    private String longitude;
    private String latitude;

    @Bind(R.id.detail_title_text_id) protected TextView title;
    @Bind(R.id.detail_date_text_id) protected TextView date;
    @Bind(R.id.detail_time_text_id) protected TextView time;
    @Bind(R.id.detail_description_text_id) protected TextView description;
    @Bind(R.id.detail_place_text_id) protected TextView place;
    @Bind(R.id.detail_player_count_text_id) protected TextView count;

    Toolbar toolbar_detail;

    private GoogleMap map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        MyEvent myEvent = (MyEvent) bundle.get("event");

        toolbar_detail = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar_detail);

        latitude = (myEvent.getLatitude());
        longitude = (myEvent.getLongitude());


        Double lat = Double.parseDouble(latitude);
        Double lon = Double.parseDouble(longitude);

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();
        LatLng location =new LatLng(lon,lat);

        Marker marker = map.addMarker(new MarkerOptions()
                .position(location)
        );
        // Move the camera instantly to hamburg with a zoom of 15.
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));

        // Zoom in, animating the camera.
        map.animateCamera(CameraUpdateFactory.zoomTo(15), 1500, null);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.getUiSettings().setZoomControlsEnabled(true);




        getSupportActionBar().setTitle("Event Details");



        title.setText(myEvent.getTitle());
        time.setText(myEvent.getTime());
        description.setText(myEvent.getDescription());
        date.setText(myEvent.getDate());
        place.setText(myEvent.getPlace());
        count.setText(myEvent.getPlayerCount());

    }

}
