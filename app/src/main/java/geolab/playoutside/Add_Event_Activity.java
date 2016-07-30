package geolab.playoutside;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vi.swipenumberpicker.OnValueChangeListener;
import com.vi.swipenumberpicker.SwipeNumberPicker;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import geolab.playoutside.db.Category_db;
import geolab.playoutside.fragment_categories.SubCategoryIcon;
import geolab.playoutside.fragments.AllGamesFragment;
import geolab.playoutside.fragments.Category;
import hotchemi.stringpicker.StringPickerDialog;

public class Add_Event_Activity extends AppCompatActivity implements OnMapReadyCallback, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener, View.OnClickListener{

    private TextView timeenter;
    private TextView dateenter;
    private TextView getplaceView;
    private String date;
    private String time;
    private String getplace;
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


    private String subcategoryData = null;

    private RequestQueue mRequestQueue;
    private  ImageView arrow;
    private ViewPager mViewPager;
    private MyPagerAdapter mSectionsPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add__event_);
        toolbar = (Toolbar) findViewById(R.id.detail_toolbar_id);
        setSupportActionBar(toolbar);
        tabLayout = (TabLayout) findViewById(R.id.category_tabs);

        getSupportActionBar().setTitle("");





        ArrayList<Fragment> fragmentList = new ArrayList<>();
        ArrayList<String> titleList = new ArrayList<>();

        fragmentList.add(SubCategoryIcon.newInstance(Category_db.ball_icon.length, Category_db.ball_icon, Category_db.bal_tag,"BALL"));
        fragmentList.add(SubCategoryIcon.newInstance(Category_db.card_icon.length, Category_db.card_icon, Category_db.card_tag,"CARD"));
        fragmentList.add(SubCategoryIcon.newInstance(Category_db.table_icon.length, Category_db.table_icon, Category_db.table_tag,"TABLE"));
        fragmentList.add(SubCategoryIcon.newInstance(Category_db.action_icon.length, Category_db.action_icon, Category_db.action_tag,"ACTION"));


        titleList.add("BALL");
        titleList.add("CARD");
        titleList.add("TABLE");
        titleList.add("ACTION");



        mSectionsPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), fragmentList, titleList);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.category_container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"));



        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(Add_Event_Activity.this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(Add_Event_Activity.this,
                    Manifest.permission.READ_CONTACTS)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(Add_Event_Activity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
                ActivityCompat.requestPermissions(Add_Event_Activity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }




        final Bundle bundle = getIntent().getExtras();


        if (bundle!=null){
            eventId = (String) bundle.get("event");
        }
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

//        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
//            @Override
//            public void onTabSelected(TabLayout.Tab tab){
//                int position = tab.getPosition();
//                category = (String) tabLayout.getTabAt(position).getText();
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });


        timeenter = (TextView) findViewById(R.id.detail_time_text_id);
        dateenter = (TextView) findViewById(R.id.detail_date_text_id);
        getplaceView = (TextView) findViewById(R.id.detail_getplace);




         Button send= (Button) findViewById(R.id.detail_join_game);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendServerData();
            }
        });

        ((MapFragment) getFragmentManager().findFragmentById(R.id.map))

                .getMapAsync(this);

        dateenter.setOnClickListener(new View.OnClickListener() {
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

        timeenter.setOnClickListener(new View.OnClickListener() {
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


            }
        });

        arrow = (ImageView) findViewById(R.id.arrow_add);
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Add_Event_Activity.this,"Please fill all fields",Toast.LENGTH_LONG).show();


                Intent ika = new Intent(Add_Event_Activity.this, MainActivity.class);
//
                startActivity(ika);
            }
        });


    }


    public void setSubCategoryData(String subcategory, String category){
        this.category = category;
        this.subcategoryData = subcategory;
        System.out.println("222"+subcategoryData + "dcdkbc" +category);
    }



    /**
     * Shevamowmot location manageri
     */
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

            new SweetAlertDialog(Add_Event_Activity.this, SweetAlertDialog.WARNING_TYPE)
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


    private GoogleMap map;
    static  LatLng point = new LatLng(41.7126944502416, 44.78326041251421);

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        map.getUiSettings().setZoomControlsEnabled(true);
        map.setMyLocationEnabled(true);
        map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                checkGPSStatus();

                return false;
            }
        });
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 13));
        map.animateCamera(CameraUpdateFactory.zoomTo(13), 1500, null);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                // TODO Auto-generated method stub
                getCompleteAddressString(point.latitude, point.longitude);
                map.clear();
                System.out.println(point.latitude + " " + point.longitude);
                latitude = String.valueOf(point.latitude);
                longitude = String.valueOf(point.longitude);
                map.addMarker(new MarkerOptions().position(point));
            }
        });
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
        getplaceView = (TextView) findViewById(R.id.detail_getplace);
        this.getplace = String.valueOf(getplaceView.getText());
        this.getdescription = String.valueOf(description.getText());
    }


    private void sendServerData() {
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



    private class MyPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> list;
        private ArrayList<String> titleList;

        public MyPagerAdapter(FragmentManager fm, ArrayList<Fragment> list, ArrayList<String> titleList) {
            super(fm);
            this.list = list;
            this.titleList = titleList;
        }

        @Override
        public Fragment getItem(int pos) {
            return list.get(pos);
        }

        @Override
        public int getCount() {
            return list.size();
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
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
                        Intent intent = new Intent(Add_Event_Activity.this, MainActivity.class);

                        startActivity(intent);
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
                if(category.isEmpty() && subcategoryData.isEmpty() && getdescription.isEmpty() && date.isEmpty() && time.isEmpty() && getplace.isEmpty() && latitude.isEmpty() && longitude.isEmpty() ){
                    Toast.makeText(Add_Event_Activity.this,"Please fill all fields",Toast.LENGTH_LONG).show();
                }else {

                    params.put("user_id", Profile.getCurrentProfile().getId());
                    params.put("category", category);
                    params.put("subcategory", subcategoryData);
                    params.put("date", date);
                    params.put("time", time);
                    params.put("description", getdescription);
                    params.put("location", getplace);
                    params.put("latitude", latitude);
                    params.put("longitude", longitude);
                }

                    params.toString();
                    return params;

            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void update(){

        getInfoFromActivity();


        final String URL = "http://geolab.club/geolabwork/ika/";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(Add_Event_Activity.this, response, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Add_Event_Activity.this, MainActivity.class);
                        startActivity(intent);
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
                if(eventId.isEmpty() && category.isEmpty() && subcategoryData.isEmpty() && getdescription.isEmpty() && date.isEmpty() && time.isEmpty() && getplace.isEmpty() && latitude.isEmpty() && longitude.isEmpty() ){
                    Toast.makeText(Add_Event_Activity.this,"Please fill all fields",Toast.LENGTH_LONG).show();
                }else {
                    params.put("eventId",eventId);
                    params.put("user_id", Profile.getCurrentProfile().getId());
                    params.put("category", category);
                    params.put("subcategory", subcategoryData);
                    params.put("date", date);
                    params.put("time", time);
                    params.put("description", getdescription);
                    params.put("location", getplace);
                    params.put("latitude", latitude);
                    params.put("longitude", longitude);
                }

                params.toString();
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("  ");
                }
                strAdd = strReturnedAddress.toString();
               getplaceView.setText(strReturnedAddress.toString());
            } else {
                getplaceView.setText("Cannot get Address!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            getplaceView.setText("Cannot get Address!");
        }
        return strAdd;
    }

}
