package geolab.playoutside.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import geolab.playoutside.Add_Event_Activity;
import geolab.playoutside.MainActivity;
import geolab.playoutside.R;
import geolab.playoutside.ViewProfile;
import geolab.playoutside.adapters.MyStickyAdapter;
import geolab.playoutside.model.MyEvent;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

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
    private List<String> profile;

    private String day;
    private String month;
    private String year;
    private String everything;

    private boolean checkprofile = false;

    private RequestQueue requestQueue;


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
    private ImageView arrow;

    private LinearLayout share;
    private LinearLayout sharebutton;


    @Bind(R.id.detail_title_text_id)
    protected TextView title;
    @Bind(R.id.detail_author_id)
    protected CircleImageView authorView;
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


    @Bind(R.id.deal_web_view_holder)
    protected HorizontalScrollView sView;

// Hide the Scollbar

    private ImageView edit;
    private ImageView delete;
    private String URL = "http://geolab.club/geolabwork/ika/delete.php";

    private String currentUrl = "http://geolab.club/geolabwork/ika/currentevent.php?";
    private String cyrcleurl = "http://geolab.club/geolabwork/ika/getcyrcleimage.php?event_id=";



    Toolbar toolbar_detail;

    private GoogleMap map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Bundle bundle2 = getIntent().getBundleExtra("Extra");
//        if(bundle2 != null){
//            String event_id = bundle2.getString("event_id");
//            getCurrentEvent(currentUrl+"event_id="+event_id);
//
//        }

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(EventDetailActivity.this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(EventDetailActivity.this,
                    Manifest.permission.READ_CONTACTS)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

//                ActivityCompat.requestPermissions(Add_Event_Activity.this,
//                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                        1);
                ActivityCompat.requestPermissions(EventDetailActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        Bundle bundle = getIntent().getBundleExtra("fromadapter");


        if (bundle != null) {
            final MyEvent myEvent = (MyEvent) bundle.get("event");

            latitude = (myEvent.getLatitude());
            longitude = (myEvent.getLongitude());
            getId = myEvent.getId();
            eventId_intent = String.valueOf(myEvent.getEventId());
         //   getJSONInfo(cyrcleurl+eventId_intent);
            description_intent = myEvent.getDescription();
            title_intent = myEvent.getTitle();
            time_intent = myEvent.getTime();
            date_intent = myEvent.getDate();
            year = date_intent.split("-")[0]; // "Before"
            month = date_intent.split("-")[1];
            day = date_intent.split("-")[2]; // "After"
            everything = day + "/" + month + "/" + year;
            count_intent = myEvent.getPlayerCount();
            place_intent = myEvent.getPlace();
            profile = myEvent.getEvents();

        }


        boolean bundle3 = getIntent().getBooleanExtra("check", true);

        if (bundle3 == true) {
            setContentView(R.layout.activity_event_detail_admin);

            arrow = (ImageView) findViewById(R.id.arrow);
            arrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent transport = new Intent(EventDetailActivity.this, MainActivity.class);
                    Bundle bundle = new Bundle();
                    transport.putExtra("profile_extra", bundle);
                    startActivity(transport);
                }
            });

            edit = (ImageView) findViewById(R.id.admin_edit_id);
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
                                    delete(URL + "?eventId=" + eventId_intent);

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
                    bundle.putString("event", eventId_intent);
                    addactivity.putExtras(bundle);

                    startActivity(addactivity);
                }
            });


        } else {
            setContentView(R.layout.activity_event_detail);
            share = (LinearLayout) findViewById(R.id.share_content);
            sharebutton = (LinearLayout) findViewById(R.id.shareButton);
            sharebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareResultToFacebook();
                }
            });
        }

        ButterKnife.bind(this);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();


        toolbar_detail = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar_detail);
        getSupportActionBar().setTitle("");

        arrow = (ImageView) findViewById(R.id.arrow);
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transport = new Intent(EventDetailActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                transport.putExtra("profile_extra", bundle);
                startActivity(transport);
            }
        });


        sView.setHorizontalScrollBarEnabled(false);


        Double lat = Double.parseDouble(latitude);
        Double lon = Double.parseDouble(longitude);

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();
        LatLng location = new LatLng(lon, lat);

        Marker marker = map.addMarker(new MarkerOptions()
                        .position(location)
        );

        // Move the camera instantly to hamburg with a zoom of 15.
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        map.setMyLocationEnabled(true);
        map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                checkGPSStatus();

                return false;
            }
        });

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));

        // Zoom in, animating the camera.
        map.animateCamera(CameraUpdateFactory.zoomTo(15), 1500, null);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.getUiSettings().setZoomControlsEnabled(true);


        final float scale = getResources().getDisplayMetrics().density;
        int width  = (int) (62 * scale);
        int height = (int) (62 * scale);
        imgUrl = "https://graph.facebook.com/" + getId + "/picture?height=200";
        Picasso.with(EventDetailActivity.this)
                .load(imgUrl)
                .resize(width, height)
                .centerCrop()
                .into(authorView);

        authorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transport = new Intent(EventDetailActivity.this, ViewProfile.class);
                Bundle bundle = new Bundle();
                bundle.putString("event_id", eventId_intent);
                bundle.putString("fb_id", getId);
                bundle.putBoolean("check", checkprofile);
                transport.putExtra("Extra", bundle);
                startActivity(transport);
            }
        });

        title.setText(title_intent);
        time.setText(time_intent);
        description.setText(description_intent);
        date.setText(everything);
        place.setText(place_intent);
        count.setText(String.valueOf(profile.size()));

        try {
            circleCounter = NumberFormat.getInstance().parse(count_intent).intValue();
        } catch (ParseException e) {
            e.printStackTrace();
        }



        LinearLayout content = (LinearLayout) findViewById(R.id.content);
        content.removeAllViews();


        final ArrayList<CircleImageView> circleImageViewlist = new ArrayList< >();
        for (int i = 0; i < profile.size(); i++) {


            final CircleImageView circleImageView = new CircleImageView(getApplication());
            circleImageView.setId(i);
            imgUrl = "https://graph.facebook.com/" + profile.get(i) + "/picture?height=200";
            Picasso.with(EventDetailActivity.this)
                    .load(imgUrl)
                    .resize(width, height)
                    .centerCrop()
                    .into(circleImageView);
            circleImageViewlist.add(circleImageView);


            circleImageViewlist.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    for(int i = 0; i < profile.size(); i++){
                if(circleImageViewlist.get(i).getId()== v.getId()){
        //            Toast.makeText(EventDetailActivity.this,profile.get(i),Toast.LENGTH_LONG).show();
                    Intent transport = new Intent(EventDetailActivity.this, ViewProfile.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("event_id", eventId_intent );
                    bundle.putString("fb_id",profile.get(i));
                    bundle.putBoolean("check",checkprofile);
                    transport.putExtra("Extra", bundle);
                    startActivity(transport);
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
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                // TODO: Your application init goes here.
                                Intent transport = new Intent(EventDetailActivity.this, MainActivity.class);
                                startActivity(transport);
                            }
                        }, 500);
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
                                        MainActivity.getInstance().finish();
                                        Intent intent = new Intent(EventDetailActivity.this, Launch.class);
                                        startActivity(intent);


                                        sDialog.dismissWithAnimation();
//                                        sDialog.setTitleText("")
//                                                .s1etContentText("Your request has been sent")
//                                                .setConfirmText("OK")
//                                                .showCancelButton(false)
//                                                .setCancelClickListener(null)
//                                                .setConfirmClickListener(null)
//                                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
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

    private void checkGPSStatus() {

        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {

            new SweetAlertDialog(EventDetailActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("GPS server not active!")
                    .setContentText("For activation GPS please confirm")
                    .setCancelText("No,cancel")
                    .setConfirmText("Yes, confirm!")
                    .showCancelButton(true)
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismiss();
                        }
                    })
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {

                            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(myIntent);
                            sDialog.dismiss();
                        }

                    })
                    .show();
        }
    }
    private void shareResultToFacebook() {
        try {

            Bitmap bitmap = getBitmapFromView(share);

            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, getImageUri(this, bitmap));
            shareIntent.setType("image/jpeg");
            startActivity(Intent.createChooser(shareIntent, "Share"));

        } catch (Exception e) {
            e.getMessage();
        }
    }


    private Bitmap getBitmapFromView(LinearLayout view) {
        try {

            view.setDrawingCacheEnabled(true);
            view.buildDrawingCache(true);


            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

            Bitmap returnedBitmap = Bitmap.createBitmap(view.getDrawingCache());

            //Define a bitmap with the same size as the view
            view.setDrawingCacheEnabled(true);

            return returnedBitmap;
        } catch (Exception e) {
            e.getCause();
        }
        return null;
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        try {

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(),
                    inImage, "", "");
            return Uri.parse(path);
        } catch (Exception e) {
            e.getMessage();
            Log.d("blablabla", "getImageUri " + e.getMessage());
        }
        return null;
    }
    private void getJSONInfo(String url) {



        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.GET
                , url
                , null
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONArray jsonArray = null;
                try {

                    jsonArray = response.getJSONArray("data");
                    if(jsonArray==null){

                    }else {


                            for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject curObj = jsonArray.getJSONObject(i);


                                String cyrcle = curObj.getString("fb_id");

                                profile.add(cyrcle);
                            }

                    }} catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });
        requestQueue = Volley.newRequestQueue(EventDetailActivity.this);
        requestQueue.add(myRequest);
    }

}
