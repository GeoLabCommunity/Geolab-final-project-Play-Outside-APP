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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;

public class Add_Event_Activity extends AppCompatActivity implements OnMapReadyCallback, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
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
    private String getmember;
    private String getplace;
    private String latitude;
    private String longitude;


    private Spinner spinner;
    private Spinner member_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__event_);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        spinner = (Spinner) findViewById(R.id.spinner);
        member_spinner = (Spinner) findViewById(R.id.member_spinner);

        ArrayAdapter member_adapter = ArrayAdapter.createFromResource(this, R.array.member_array, R.layout.spinner_item);
        member_spinner.setAdapter(member_adapter);
        member_adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.district_array, R.layout.spinner_item);
        spinner.setAdapter(adapter);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        timeclick = (LinearLayout) findViewById(R.id.detail_time_click);
        dateclick = (LinearLayout) findViewById(R.id.detail_date_click);
        timeenter = (TextView) findViewById(R.id.detail_time_text_id);
        dateenter = (TextView) findViewById(R.id.detail_date_text_id);


        ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMapAsync(this);

        //       loginToFB();
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
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
        dateenter.setText(date);

    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String time = hourOfDay+":"+minute;
        timeenter.setText(time);

    }
    private void getInfoFromActivity() {

        this.gettime = timeenter.getText().toString();
        this.getdate =  dateenter.getText().toString();
        this.getmember =  member_spinner.getSelectedItem().toString();
        this.getplace = spinner.getSelectedItem().toString();

    }
    public void sendServerData(View v) {
        final HttpClient httpclient = new DefaultHttpClient();
        final HttpPost httppost = new HttpPost("http://vanisgimnazia.ge/instagram/fileUpload.php");
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<>(4);
            getInfoFromActivity();
            nameValuePairs.add(new BasicNameValuePair("time", gettime));
            nameValuePairs.add(new BasicNameValuePair("date", getdate));
            nameValuePairs.add(new BasicNameValuePair("member_count", getmember));
            nameValuePairs.add(new BasicNameValuePair("place", getplace));
            nameValuePairs.add(new BasicNameValuePair("latitude", latitude));
            nameValuePairs.add(new BasicNameValuePair("longitude", longitude));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        httpclient.execute(httppost);
                    } catch (IOException e) {
                        e.getCause();
                    }
                }
            });
            thread.start();
        } catch (IOException e) {
            e.getMessage();
        }
        Toast.makeText(getApplicationContext(), "Post has been uploaded",
                Toast.LENGTH_SHORT).show();
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
