package geolab.playoutside;

import android.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.borax12.materialdaterangepicker.time.RadialPickerLayout;
import com.borax12.materialdaterangepicker.time.TimePickerDialog;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Calendar;

public class Add_Event_Activity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, OnMapReadyCallback, TimePickerDialog.OnTimeSetListener {
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


    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__event_);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.country_arrays, R.layout.spinner_item);
        spinner.setAdapter(adapter);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        timeclick = (LinearLayout) findViewById(R.id.detail_time_click);
        dateclick = (LinearLayout) findViewById(R.id.detail_date_click);
        timeenter = (TextView) findViewById(R.id.detail_time_text_id);
        dateenter = (TextView) findViewById(R.id.detail_date_text_id);


        ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMapAsync(this);

        //       loginToFB();
        dateclick.setOnClickListener( new View.OnClickListener() {
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

            }
        } );

        timeclick.setOnClickListener( new View.OnClickListener() {
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
        } );


        //shevamowmot location manageri
        checkGPSStatus();


    }

    boolean gps_enabled = false;
    boolean network_enabled = false;

    /**
     * Shevamowmot location manageri
     */
    private void checkGPSStatus(){

        try {
            LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("GPS სერვისი არაა გააქტიურებული");
            dialog.setPositiveButton("გააქტიურება", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
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
    static final LatLng LOCATION = new LatLng(41.7126944502416, 44.78326041251421);
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                // TODO Auto-generated method stub
                map.clear();
                System.out.println("1111 " +point.latitude + " " + point.longitude);
                map.addMarker(new MarkerOptions().position(point));
            }
        });


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.setMyLocationEnabled(true);
        Marker marker = map.addMarker(new MarkerOptions()
                .position(LOCATION)
                .title("title")
                .snippet("small description")
                .draggable(true)
        );
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(LOCATION, 13));

        // Zoom in, animating the camera.
        map.animateCamera(CameraUpdateFactory.zoomTo(13), 1500, null);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getCameraPosition();

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

                                        //fb_intent_info();


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
        startActivity(intent);
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {

        int yearString = year ;
        int monthOfYearString = monthOfYear +1 ;
        int dayOfMonthString = dayOfMonth ;
        int yearEndString = yearEnd ;
        int monthOfYearEndString = monthOfYearEnd +1 ;
        int dayOfMonthEndString = dayOfMonthEnd ;

        String date ="(" + dayOfMonthString + "/" + monthOfYearString + "/" + yearString + ")" + " To- " + "(" +dayOfMonthEndString + "/" + monthOfYearEndString + "/" + yearEndString + ")";

        dateenter.setText( date );


    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int hourOfDayEnd, int minuteEnd) {

        String hourString = hourOfDay < 10 ? "0"+hourOfDay : ""+hourOfDay;
        String minuteString = minute < 10 ? "0"+minute : ""+minute;
        String hourStringEnd = hourOfDayEnd < 10 ? "0"+hourOfDayEnd : ""+hourOfDayEnd;
        String minuteStringEnd = minuteEnd < 10 ? "0"+minuteEnd : ""+minuteEnd;
        String time = hourString+":"+minuteString+" To - "+hourStringEnd+":"+minuteStringEnd;

        timeenter.setText(time);


    }
}
