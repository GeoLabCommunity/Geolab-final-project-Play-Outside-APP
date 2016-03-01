package geolab.playoutside.view;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.Profile;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import geolab.playoutside.R;
import geolab.playoutside.model.MyEvent;

public class EventDetailActivity extends AppCompatActivity {

    private String longitude;
    private String latitude;
    private String imgUrl;
    private String getId;


    @Bind(R.id.detail_title_text_id) protected TextView title;
    @Bind(R.id.detail_date_text_id) protected TextView date;
    @Bind(R.id.detail_time_text_id) protected TextView time;
    @Bind(R.id.detail_description_text_id) protected TextView description;
    @Bind(R.id.detail_place_text_id) protected TextView place;
    @Bind(R.id.detail_player_count_text_id) protected TextView count;
    @Bind(R.id.detail_join_game) protected Button joinGame;
    @Bind(R.id.detail_join_image) protected CircleImageView joinImage;
    @Bind(R.id.detail_join_image1) protected CircleImageView joinImage1;
    @Bind(R.id.detail_join_image2) protected CircleImageView joinImage2;
    @Bind(R.id.detail_join_image3) protected CircleImageView joinImage3;
    @Bind(R.id.detail_join_image4) protected CircleImageView joinImage4;
    @Bind(R.id.detail_join_image5) protected CircleImageView joinImage5;
    @Bind(R.id.detail_join_image6) protected CircleImageView joinImage6;








    Toolbar toolbar_detail;

    private GoogleMap map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        final MyEvent myEvent = (MyEvent) bundle.get("event");

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

        getId = myEvent.getId();


        imgUrl = "https://graph.facebook.com/" + myEvent.getId() + "/picture?height=400";
        Picasso.with(EventDetailActivity.this)
                .load(imgUrl)
                .resize(400, 400)
                .centerCrop()
                .into(joinImage);
        imgUrl = "https://graph.facebook.com/" + myEvent.getId() + "/picture?height=400";
        Picasso.with(EventDetailActivity.this)
                .load(imgUrl)
                .resize(400, 400)
                .centerCrop()
                .into(joinImage1);
        imgUrl = "https://graph.facebook.com/" + myEvent.getId() + "/picture?height=400";
        Picasso.with(EventDetailActivity.this)
                .load(imgUrl)
                .resize(400, 400)
                .centerCrop()
                .into(joinImage2);
        imgUrl = "https://graph.facebook.com/" + myEvent.getId() + "/picture?height=400";
        Picasso.with(EventDetailActivity.this)
                .load(imgUrl)
                .resize(400, 400)
                .centerCrop()
                .into(joinImage3);
        imgUrl = "https://graph.facebook.com/" + myEvent.getId() + "/picture?height=400";
        Picasso.with(EventDetailActivity.this)
                .load(imgUrl)
                .resize(400, 400)
                .centerCrop()
                .into(joinImage4);
        imgUrl = "https://graph.facebook.com/" + myEvent.getId() + "/picture?height=400";
        Picasso.with(EventDetailActivity.this)
                .load(imgUrl)
                .resize(400, 400)
                .centerCrop()
                .into(joinImage5);
        imgUrl = "https://graph.facebook.com/" + myEvent.getId() + "/picture?height=400";
        Picasso.with(EventDetailActivity.this)
                .load(imgUrl)
                .resize(400, 400)
                .centerCrop()
                .into(joinImage6);

        joinGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(EventDetailActivity.this,"Your request has been sent",Toast.LENGTH_LONG).show();
                addEvent();
            }
        });

    }

    private void addEvent(){


        final String URL = "http://geolab.club/iraklilataria/ika/ci/";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(EventDetailActivity.this, response, Toast.LENGTH_LONG).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // System.out.println("error " +error.toString());
                        Toast.makeText(EventDetailActivity.this,"Please fill all fields",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("u_id",Profile.getCurrentProfile().getId());
                params.put("r_id",getId);
                params.toString();
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}
