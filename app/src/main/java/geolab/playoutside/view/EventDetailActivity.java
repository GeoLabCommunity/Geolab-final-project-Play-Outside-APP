package geolab.playoutside.view;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import geolab.playoutside.Add_Event_Activity;
import geolab.playoutside.MainActivity;
import geolab.playoutside.R;
import geolab.playoutside.fragments.AllGamesFragment;
import geolab.playoutside.gcm.RegistrationIntentService;
import geolab.playoutside.model.MyEvent;

public class EventDetailActivity extends AppCompatActivity {

    private String longitude;
    private String latitude;
    private String description_intent;
    private String title_intent;
    private String time_intent;
    private String date_intent;
    private String count_intent;
    private String getId;
    private String eventId_intent;
    private String place_intent;



    private CallbackManager callbackManager;
    private AccessToken accessToken;
    private String user_id;
    private String str_firstName;
    private String email_json;
    private String birth_day;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";

    private String imgUrl;

    private int circleCounter;


    @Bind(R.id.detail_title_text_id)
    protected TextView title;
    @Bind(R.id.detail_date_text_id)
    protected TextView date;
    @Bind(R.id.detail_time_text_id)
    protected TextView time;
    @Bind(R.id.detail_description_text_id)
    protected TextView description;
    @Bind(R.id.detail_place_text_id)
    protected TextView place;
    @Bind(R.id.detail_player_count_text_id)
    protected TextView count;
    @Bind(R.id.detail_join_game)
    protected Button joinGame;
    @Bind(R.id.detail_join_image)
    protected CircleImageView joinImage;

    private ImageView edit;
    private ImageView delete;
    private String URL = "http://geolab.club/geolabwork/ika/delete.php";



    Toolbar toolbar_detail;

    private GoogleMap map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            final MyEvent myEvent = (MyEvent) bundle.get("event");
            latitude = (myEvent.getLatitude());
            longitude = (myEvent.getLongitude());
            getId = myEvent.getId();
            eventId_intent = String.valueOf(myEvent.getEventId());
            description_intent = myEvent.getDescription();
            title_intent = myEvent.getTitle();
            time_intent = myEvent.getTitle();
            time_intent = myEvent.getTime();
            date_intent = myEvent.getDate();
            count_intent = myEvent.getPlayerCount();
            place_intent = myEvent.getPlace();
        }

        Bundle bundle2 = getIntent().getBundleExtra("Extra");
        if(bundle2 != null){
            String k = bundle2.getString("message");

        }

        final Boolean check = (Boolean) bundle.get("check");
        if(check==true){
            setContentView(R.layout.activity_event_detail_admin);

            edit= (ImageView) findViewById(R.id.admin_edit_id);
            delete = (ImageView) findViewById(R.id.admin_delete_id);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new SweetAlertDialog(EventDetailActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Are you sure?")
                            .setContentText("Won't be able to recover this file!")
                            .setCancelText("No,cancel plz!")
                            .setConfirmText("Yes,delete it!")
                            .showCancelButton(true)
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    // reuse previous dialog instance, keep widget user state, reset them if you need
//                            sDialog.setTitleText("Cancelled!")
//                                    .setContentText("Your imaginary file is safe :)")
//                                    .setConfirmText("OK")
//                                    .showCancelButton(false)
//                                    .setCancelClickListener(null)
//                                    .setConfirmClickListener(null)
//                                    .changeAlertType(SweetAlertDialog.ERROR_TYPE);

                                    // or you can new a SweetAlertDialog to show
                                    sDialog.dismiss();
                                    new SweetAlertDialog(EventDetailActivity.this, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("Cancelled!")
                                            .setContentText("Your imaginary file is safe :)")
                                            .setConfirmText("OK")
                                            .show();
                                }
                            })
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    delete(URL+"?eventId="+eventId_intent);

                                    Intent intent = new Intent(EventDetailActivity.this, MainActivity.class);
                                    startActivity(intent);

                                    sDialog.setTitleText("Deleted!")
                                            .setContentText("Your imaginary file has been deleted!")
                                            .setConfirmText("OK")
                                            .showCancelButton(false)
                                            .setCancelClickListener(null)
                                            .setConfirmClickListener(null)
                                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                                }

                            })
                            .show();
                }
            });

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent addactivity = new Intent(EventDetailActivity.this, Add_Event_Activity.class);
                    Bundle bundle = new Bundle();
                    addactivity.putExtra("check",true);
                    addactivity.putExtra("event",eventId_intent);
                    addactivity.putExtras(bundle);

                    startActivity(addactivity);
                }
            });


        }else{
            setContentView(R.layout.activity_event_detail);
        }

        ButterKnife.bind(this);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();




        toolbar_detail = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar_detail);






        Double lat = Double.parseDouble(latitude);
        Double lon = Double.parseDouble(longitude);

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();
        LatLng location = new LatLng(lon, lat);

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


        title.setText(title_intent);
        time.setText(time_intent);
        description.setText(description_intent);
        date.setText(date_intent);
        place.setText(place_intent);
        count.setText(count_intent);

        try {
            circleCounter = NumberFormat.getInstance().parse(count_intent).intValue();
        } catch (ParseException e) {
            e.printStackTrace();
        }



        LinearLayout content = (LinearLayout) findViewById(R.id.content);
        content.removeAllViews();


        final ArrayList<CircleImageView> circleImageViewlist = new ArrayList< CircleImageView>();
        for (int i = 0; i < circleCounter; i++) {

            final CircleImageView circleImageView = new CircleImageView(getApplication());
            circleImageView.setId(i);
            imgUrl = "https://graph.facebook.com/" + getId + "/picture?height=400";
            Picasso.with(EventDetailActivity.this)
                    .load(imgUrl)
                    .resize(200, 200)
                    .centerCrop()
                    .into(circleImageView);
            circleImageViewlist.add(circleImageView);


            circleImageViewlist.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    for(int i = 0; i < circleCounter; i++){
                if(circleImageViewlist.get(i).getId()== v.getId()){
                    Toast.makeText(EventDetailActivity.this,"Your request has been sent"+circleImageViewlist.get(i),Toast.LENGTH_LONG).show();
                }
            }

                }
            });
            content.addView(circleImageView);
        }


        joinGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(EventDetailActivity.this,"Your request has been sent",Toast.LENGTH_LONG).show();
                addEvent();
            }
        });

    }

    private void addEvent(){


        final String URL = "http://geolab.club/geolabwork/ika/ci/";

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
                        new SweetAlertDialog(EventDetailActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Login via Facebook")
                                .setContentText("For JOIN this Event you need to Login!")
                                .setCancelText("No,cancel")
                                .setConfirmText("Yes, Login")
                                .showCancelButton(true)
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        loginToFB();
                                        sDialog.setTitleText("")
                                                .setContentText("Your request has been sent")
                                                .setConfirmText("OK")
                                                .showCancelButton(false)
                                                .setCancelClickListener(null)
                                                .setConfirmClickListener(null)
                                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                    }

                                })
                                .show();

                   //      System.out.println("shecdoma " +error.toString());


                    }

                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("u_id",Profile.getCurrentProfile().getId());
                params.put("r_id",getId);
                params.put("event_id",eventId_intent);
                params.toString();
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void delete(String URL){


        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();

                params.toString();
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(EventDetailActivity.this);
        requestQueue.add(stringRequest);
    }

    public void loginToFB() {
        accessToken = AccessToken.getCurrentAccessToken();
        System.out.println(accessToken);
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "user_photos", "public_profile", "user_birthday"));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                                if (graphResponse.getError() != null) {
                                    System.out.println("ERROR");
                                } else {
                                    try {
                                        user_id = jsonObject.getString("id");
                                        str_firstName = jsonObject.getString("name");
                                        birth_day = jsonObject.getString("birthday");
                                        email_json = jsonObject.getString("email");


                                        if (checkPlayServices()) {



                                            // Start IntentService to register this application with GCM.
                                            Intent intent = new Intent(getApplicationContext(), RegistrationIntentService.class);
//
                                            startService(intent);
                                        }

                                        fb_intent_info();

                                    } catch (NullPointerException ex) {
                                        ex.getMessage();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,birthday,gender,email");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }
                    @Override
                    public void onCancel() {
                        Toast.makeText(EventDetailActivity.this, "onCancel", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.d("FacebookException", exception.getMessage());
                        Toast.makeText(EventDetailActivity.this, "Please, check your connection", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    private void fb_intent_info() {
        Intent intent = new Intent(EventDetailActivity.this, MainActivity.class);
        intent.putExtra("fb_name", str_firstName);
        intent.putExtra("fb_id", user_id);
        intent.putExtra("fb_email", email_json);
        intent.putExtra("fb_age", birth_day);
        intent.putExtra("access",accessToken);
        startActivity(intent);
    }
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

}
