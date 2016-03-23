package geolab.playoutside;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.Profile;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import geolab.playoutside.adapters.MyStickyAdapter;
import geolab.playoutside.model.MyEvent;
import geolab.playoutside.view.EventDetailActivity;

public class ViewProfile extends AppCompatActivity implements
        RatingBar.OnRatingBarChangeListener {
    private String fb_id;
    private String nameJsn;
    private String ageJsn;
    private String emailJsn;

    private Boolean check;

    private int categoryId;
    private String eventId_intent;

    private String currentUrl = "http://geolab.club/geolabwork/ika/currentevent.php?";

    private String acceptUrl = "http://geolab.club/geolabwork/ika/accept.php";

    private String profileUrl ="http://geolab.club/geolabwork/ika/viewprofile.php?";
    private RequestQueue requestQueue;
    private ImageView imageProfile;
    private TextView name;
    private TextView age;
    private TextView email;

    private ImageView accept;
    private ImageView reject;

   private RatingBar getRatingBar;
   private RatingBar setRatingBar;
   private TextView countText;
   private int count;
   private float curRate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        Bundle bundle = getIntent().getBundleExtra("Extra");
        if(bundle != null){

            check =bundle.getBoolean("check");
            eventId_intent = bundle.getString("event_id");
            fb_id = bundle.getString("fb_id");
            if(check != false){
                setContentView(R.layout.activity_view_profile_checked);
                getProfileInfo(profileUrl+"event_id="+eventId_intent+"&fb_id="+fb_id);
                accept = (ImageView) findViewById(R.id.profile_accept);
                reject = (ImageView) findViewById(R.id.profile_reject) ;


                accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        accept(acceptUrl);

                        getCurrentEvent(currentUrl+"event_id="+eventId_intent);

                    }
                });

                reject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(ViewProfile.this, "Rejected", Toast.LENGTH_LONG).show();
                        getCurrentEvent(currentUrl+"event_id="+eventId_intent);

                    }
                });

            }else {


                getProfileInfo(profileUrl + "event_id=" + eventId_intent + "&fb_id=" + fb_id);
                setContentView(R.layout.activity_view_profile);
            }

        }




        imageProfile = (ImageView) findViewById(R.id.profile_image);
        name = (TextView) findViewById(R.id.profile_name);
        age = (TextView) findViewById(R.id.profile_age);
        email = (TextView) findViewById(R.id.profile_email);


        getRatingBar = (RatingBar) findViewById(R.id.getRating_id);
        setRatingBar = (RatingBar) findViewById(R.id.setRating_id);
        countText = (TextView) findViewById(R.id.countText_id);


        setRatingBar.setRating(curRate);
        getRatingBar.setOnRatingBarChangeListener(this);






    }
    private void getProfileInfo(String url) {

        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.GET
                , url
                , null
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONArray jsonArray = null;
                try {
                    jsonArray = response.getJSONArray("data");


                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject curObj = jsonArray.getJSONObject(i);


                        fb_id = curObj.getString("user_id");
                        nameJsn = curObj.getString("name");
                        ageJsn = curObj.getString("age");
                        emailJsn = curObj.getString("email");

                    }

                   String imgUrl = "https://graph.facebook.com/" + fb_id + "/picture?height=400";
                    Picasso.with(ViewProfile.this)
                            .load(imgUrl)
                            .resize(400, 400)
                            .centerCrop()
                            .into(imageProfile);

                    name.setText(nameJsn);
                    email.setText(emailJsn);

                    SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        Date dat = sf.parse(ageJsn);
                        Calendar birthDate = Calendar.getInstance();
                        birthDate.setTimeInMillis(dat.getTime());
                        age.setText("(" + String.valueOf(getAge(birthDate, Calendar.getInstance())) + ")");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error+"   nnn");

            }
        });

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(myRequest);

    }
    private void accept(String url){


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(ViewProfile.this, response, Toast.LENGTH_LONG).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // System.out.println("error " +error.toString());
                        Toast.makeText(ViewProfile.this,"Something wrong!",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("event_id",eventId_intent);
                params.put("fb_id",fb_id);

                params.toString();
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private int getAge(Calendar birthDate, Calendar currentDate) {
        int age = currentDate.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);

        if (currentDate.get(Calendar.MONTH) < birthDate.get(Calendar.MONTH)) {
            age--;
        } else if (currentDate.get(Calendar.MONTH) == birthDate.get(Calendar.MONTH)
                && currentDate.get(Calendar.DAY_OF_MONTH) < birthDate.get(Calendar.DAY_OF_MONTH)) {
            age--;
        }

        return age;
    }

    private void getCurrentEvent(String url) {

        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.GET
                , url
                , null
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONArray jsonArray = null;
                try {
                    jsonArray = response.getJSONArray("data");
                    System.out.println(jsonArray+"hhhh");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject curObj = jsonArray.getJSONObject(i);

                        int eventId = curObj.getInt("event_id");
                        String user_id = curObj.getString("user_id");
                        String subcategory = curObj.getString("subcategory");
                        String description = curObj.getString("description");
                        String date = curObj.getString("date");
                        String time = curObj.getString("time");
                        String count = curObj.getString("count");
                        String location = curObj.getString("location");
                        String latitude = curObj.getString("latitude");
                        String longitude = curObj.getString("longitude");

                        JSONArray array = curObj.getJSONArray("event_player");
                        List<String> event_players = new ArrayList<>();
                        for (int j = 0; j < array.length(); j++) {
                            event_players.add(array.get(j).toString());
                        }

                        switch(subcategory) {
                            case "Football":
                                categoryId=0;
                                break;
                            case "Basketball":
                                categoryId=1;
                                break;
                            case "Rugby":
                                categoryId=2;
                                break;
                            case "Volleyball":
                                categoryId=3;
                                break;
                            case "Joker":
                                categoryId=4;
                                break;
                            case "Poker":
                                categoryId=5;
                                break;
                            case "Ping-pong":
                                categoryId=6;
                                break;
                            case "Badminton":
                                categoryId=7;
                                break;
                            default:
                                categoryId=0;
                        }
                        MyEvent myEvent = new MyEvent(eventId, user_id, time, date, subcategory, description, location, count, latitude, longitude, categoryId,event_players);

                        Intent detailIntent = new Intent(ViewProfile.this, EventDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("event",myEvent);
                        detailIntent.putExtra("fromadapter",bundle);

                        startActivity(detailIntent);

                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error+"   nnn");

            }
        });
        requestQueue = Volley.newRequestQueue(ViewProfile.this);
        requestQueue.add(myRequest);
    }


    public void onRatingChanged(RatingBar rateBar, float rating,
                                boolean fromUser) {
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        curRate = Float.valueOf(decimalFormat.format((curRate * count + rating)
                / ++count));
        Toast.makeText(ViewProfile.this,
                "New Rating: " + curRate, Toast.LENGTH_SHORT).show();
        setRatingBar.setRating(curRate);
        countText.setText(count + " Ratings");
    }


}
