package geolab.playoutside;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vi.swipenumberpicker.OnValueChangeListener;
import com.vi.swipenumberpicker.SwipeNumberPicker;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import geolab.playoutside.db.Category_db;
import geolab.playoutside.fragment_categories.SubCategoryIcon;
import geolab.playoutside.model.ApplicationController;
import geolab.playoutside.model.MyEvent;
import hotchemi.stringpicker.StringPicker;
import hotchemi.stringpicker.StringPickerDialog;

import static geolab.playoutside.R.color.tab_color;

public class Add_Event_Activity extends AppCompatActivity implements OnMapReadyCallback, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener, View.OnClickListener,StringPickerDialog.OnClickListener{
    private CallbackManager callbackManager;
    private AccessToken accessToken;
    private String user_id;
    private String str_firstName;
    private String email_json;
    private String birth_day;
    private LinearLayout timeclick;
    private LinearLayout dateclick;
    private TextView timeenter;
    private TextView dateenter;
    private String gettime;
    private String getdate;
    private String date;
    private String time;
    private String getmember;
    private String getplace;
    private SwipeNumberPicker placeview;
    private String latitude;
    private String longitude;
    private String getdescription;
    private String category;

    private String eventId;

    private MyPagerAdapter selectCategory;
    private ViewPager category_pager;
    private TabLayout tabLayout;
    private Toolbar toolbar;

    private EditText description;


    private RelativeLayout countMember;
    private RelativeLayout location;

    private static final String TAG = StringPickerDialog.class.getSimpleName();
    private TextView mTextView;

    private String subcategoryData = null;

    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add__event_);
        toolbar = (Toolbar) findViewById(R.id.detail_toolbar_id);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Add New Event");

        final Bundle bundle = getIntent().getExtras();


        if (bundle!=null){

            final MyEvent myEvent = (MyEvent) bundle.get("event");

            eventId =String.valueOf(myEvent.getEventId()) ;

        }




        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        selectCategory = new MyPagerAdapter(getSupportFragmentManager());
        category_pager = (ViewPager) findViewById(R.id.category_container);
        category_pager.setAdapter(selectCategory);
        tabLayout = (TabLayout) findViewById(R.id.category_tabs);
        tabLayout.setupWithViewPager(category_pager);
        category = (String) tabLayout.getTabAt(0).getText();

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab){
                int position = tab.getPosition();
                category = (String) tabLayout.getTabAt(position).getText();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        loginToFB();

        countMember = (RelativeLayout) findViewById(R.id.count_member);
        countMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        final SwipeNumberPicker custom = (SwipeNumberPicker) findViewById(R.id.snp_custom);
        custom.setOnValueChangeListener(new OnValueChangeListener() {
            @Override
            public boolean onValueChange(SwipeNumberPicker view, int oldValue, int newValue) {
                getmember=(String.valueOf(newValue));
//                        TextView text = (TextView) findViewById(R.id.count_member_text);
//                        text.setText(String.valueOf(newValue));
                return true;
            }
        });


        mTextView = (TextView) findViewById(R.id.location_text);
        location = (RelativeLayout) findViewById(R.id.location);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putStringArray(getString(R.string.string_picker_dialog_values), getStringArray());
                StringPickerDialog dialog = new StringPickerDialog();
                dialog.setArguments(bundle);
                dialog.show(getSupportFragmentManager(), TAG);
            }
        });


        timeclick = (LinearLayout) findViewById(R.id.detail_time_click);
        dateclick = (LinearLayout) findViewById(R.id.detail_date_click);
        timeenter = (TextView) findViewById(R.id.detail_time_text_id);
        dateenter = (TextView) findViewById(R.id.detail_date_text_id);



        ((MapFragment) getFragmentManager().findFragmentById(R.id.map))




                .getMapAsync(this);



        dateclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        Add_Event_Activity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
                dpd.setMinDate(Calendar.getInstance());

            }
        });

        timeclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        Add_Event_Activity.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        false
                );
                tpd.show(getFragmentManager(), "Timepickerdialog");
                tpd.setMinTime(
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        now.get(Calendar.SECOND));

            }
        });

        //shevamowmot location manageri
        checkGPSStatus();

    }

    public void setSubCategoryData(String subcategory){
        this.subcategoryData = subcategory;
        System.out.println("222"+subcategoryData);
    }

    boolean gps_enabled = false;
    boolean network_enabled = false;

    /**
     * Shevamowmot location manageri
     */
    private void checkGPSStatus() {

        try {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("GPS სერვისი არაა გააქტიურებული");
            dialog.setPositiveButton("გააქტიურება", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                }
            });
            dialog.setNegativeButton("გაუქმება", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    //
                }
            });
            dialog.show();
        }
    }


    private GoogleMap map;
    static  LatLng point = new LatLng(41.7126944502416, 44.78326041251421);

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        map.setMyLocationEnabled(true);
//        Marker marker = map.addMarker(new MarkerOptions()
//                .position(point)
//                .title("title")
//                .snippet("small description")
//                .draggable(true)
//        );
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 13));
        map.animateCamera(CameraUpdateFactory.zoomTo(13), 1500, null);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                // TODO Auto-generated method stub
                map.clear();
                System.out.println(point.latitude + " " + point.longitude);
                latitude = String.valueOf(point.latitude);
                longitude = String.valueOf(point.longitude);
                map.addMarker(new MarkerOptions().position(point));
            }
        });
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
//                        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Add_Event_Activity.this, "onCancel", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.d("FacebookException", exception.getMessage());
                        Toast.makeText(Add_Event_Activity.this, "onError", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    private void fb_intent_info() {
        Intent intent = new Intent(Add_Event_Activity.this, MainActivity.class);
        intent.putExtra("fb_name", str_firstName);
        intent.putExtra("fb_id", user_id);
        intent.putExtra("fb_email", email_json);
        intent.putExtra("fb_age", birth_day);
        intent.putExtra("access",accessToken);
        startActivity(intent);
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        date = dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
        dateenter.setText(date);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        time = hourOfDay+":"+minute;
        timeenter.setText(time);

    }
    private void getInfoFromActivity() {
        description = (EditText) findViewById(R.id.detail_description_text_id);

        placeview= (SwipeNumberPicker) findViewById(R.id.snp_custom);
        mTextView = (TextView) findViewById(R.id.location_text);
        this.getmember =  String.valueOf(placeview.getText().toString());
        this.getplace = String.valueOf(mTextView.getText().toString());
        this.getdescription = String.valueOf(description.getText());

    }


    public void sendServerData(View v) {
         if(eventId==null){
             addEvent();
         }else {
             update();
         }

    }

    @Override
    public void onClick(View view) {
        System.out.println();
        return;
    }

    @Override
    public void onClick(String value) {
        mTextView.setText("< " +value + " >");
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int pos) {
            switch(pos) {
                case 0: return SubCategoryIcon.newInstance(4, Category_db.ball_icon, Category_db.bal_tag);
                case 1: return SubCategoryIcon.newInstance(2, Category_db.card_icon, Category_db.card_tag);
                case 2: return SubCategoryIcon.newInstance(1, Category_db.table_icon, Category_db.table_tag);
                case 3: return SubCategoryIcon.newInstance(1, Category_db.action_icon, Category_db.action_tag);

                default: return SubCategoryIcon.newInstance(4, Category_db.ball_icon, Category_db.bal_tag);

            }
        }

        @Override
        public int getCount() {
            return 4;
        }






        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "BALL";
                case 1:
                    return "CARD";
                case 2:
                    return "TABLE";
                case 3:
                    return "ACTION";

            }
            return null;
        }

    }
    private void addEvent(){

        getInfoFromActivity();


        final String URL = "http://geolab.club/geolabwork/ika/";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(Add_Event_Activity.this, response, Toast.LENGTH_LONG).show();
                        fb_intent_info();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       // System.out.println("error " +error.toString());
                        Toast.makeText(Add_Event_Activity.this,"Please fill all fields",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
               // params.put("eventId",eventId);
                params.put("user_id",user_id);
                params.put("category",category);
                params.put("subcategory",subcategoryData);
                params.put("date",date);
                params.put("time",time);
                params.put("description",getdescription);
                params.put("count",getmember);
                params.put("location",getplace);
                params.put("latitude",latitude);
                params.put("longitude",longitude);

                params.toString();
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private String[] getStringArray() {
        return new String[] {"Gldani","Nadzaladevi","Vaja-Pshavela","Samgori","Isani","VarkeTili","Digomi",
                "Saburtalo","Vake","Rustaveli","Muxiani","Temqa","Wereteli"
                };
    }
    private void update(){

        getInfoFromActivity();


        final String URL = "http://geolab.club/geolabwork/ika/";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(Add_Event_Activity.this, response, Toast.LENGTH_LONG).show();
                        fb_intent_info();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // System.out.println("error " +error.toString());
                        Toast.makeText(Add_Event_Activity.this,"Please fill all fields",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("eventId",eventId);
                params.put("user_id",user_id);
                params.put("category",category);
                params.put("subcategory",subcategoryData);
                params.put("date",date);
                params.put("time",time);
                params.put("description",getdescription);
                params.put("count",getmember);
                params.put("location",getplace);
                params.put("latitude",latitude);
                params.put("longitude",longitude);

                params.toString();
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
