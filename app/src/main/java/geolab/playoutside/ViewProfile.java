package geolab.playoutside;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import geolab.playoutside.adapters.MyStickyAdapter;
import geolab.playoutside.model.MyEvent;
import geolab.playoutside.view.EventDetailActivity;

public class ViewProfile extends AppCompatActivity {
    private  String eventId_intent;
    private String fb_id;
    private String nameJsn;
    private String ageJsn;
    private String emailJsn;

    private String check;

    private String acceptUrl = "http://geolab.club/geolabwork/ika/accept.php";

    private String profileUrl ="http://geolab.club/geolabwork/ika/viewprofile.php?";
    private RequestQueue requestQueue;
    private ImageView imageProfile;
    private TextView name;
    private TextView age;
    private TextView email;

    private ImageView accept;
    private ImageView reject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getBundleExtra("Extra");
        if(bundle != null){

            check =bundle.getString("check");
            eventId_intent = bundle.getString("event_id");
            fb_id = bundle.getString("fb_id");
            if(check == null){
                setContentView(R.layout.activity_view_profile_checked);
                getProfileInfo(profileUrl+"event_id="+eventId_intent+"&fb_id="+fb_id);
                accept = (ImageView) findViewById(R.id.profile_accept);
                reject = (ImageView) findViewById(R.id.profile_reject) ;


                accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        accept(acceptUrl);
                        Intent transport = new Intent(ViewProfile.this, EventDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("event_id", eventId_intent );
                        transport.putExtra("Extra", bundle);
                        startActivity(transport);
                    }
                });

                reject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(ViewProfile.this, "Rejected", Toast.LENGTH_LONG).show();
                        Intent mainIntent = new Intent(ViewProfile.this, EventDetailActivity.class);
                        startActivity(mainIntent);
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

}
