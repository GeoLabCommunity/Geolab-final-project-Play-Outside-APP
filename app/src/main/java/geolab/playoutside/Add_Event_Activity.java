package geolab.playoutside;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Calendar;

public class Add_Event_Activity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private CallbackManager callbackManager;
    private AccessToken accessToken;
    private String user_id;
    private String str_firstName;
    private String email_json;
    private String birth_day;
    private Button date_pick;
    private Button hour_pick;
    private TextView timeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__event_);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        loginToFB();
//        timeTextView = (TextView) findViewById( R.id.timeTextView );
//        date_pick = (Button) findViewById( R.id.date);
//        hour_pick = (Button) findViewById( R.id.hour );
//        date_pick.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Calendar now = Calendar.getInstance();
//                DatePickerDialog dpd = DatePickerDialog.newInstance(
//                        Add_Event_Activity.this,
//                        now.get(Calendar.YEAR),
//                        now.get(Calendar.MONTH),
//                        now.get(Calendar.DAY_OF_MONTH)
//                );
//                dpd.show(getFragmentManager(), "Datepickerdialog");
//
//            }
//        } );
//
//        hour_pick.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Calendar now = Calendar.getInstance();
//                TimePickerDialog tpd = TimePickerDialog.newInstance(
//                        Add_Event_Activity.this,
//                        now.get(Calendar.HOUR_OF_DAY),
//                        now.get(Calendar.MINUTE),
//                        false
//                );
//                tpd.show(getFragmentManager(), "Timepickerdialog");
//
//            }
//        } );


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

        String yearString = ""+year ;
        String monthOfYearString = ""+monthOfYear ;
        String dayOfMonthString = ""+dayOfMonth ;
        String yearEndString = ""+yearEnd ;
        String monthOfYearEndString = ""+monthOfYearEnd ;
        String dayOfMonthEndString = ""+dayOfMonthEnd ;

        String date = dayOfMonthString + "/" + monthOfYearString + "/" + yearString + "/" + dayOfMonthEndString + "/" + monthOfYearEndString + "/" + yearEndString;

        timeTextView.setText( date );


    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int hourOfDayEnd, int minuteEnd) {

        String hourString = hourOfDay < 10 ? "0"+hourOfDay : ""+hourOfDay;
        String minuteString = minute < 10 ? "0"+minute : ""+minute;
        String hourStringEnd = hourOfDayEnd < 10 ? "0"+hourOfDayEnd : ""+hourOfDayEnd;
        String minuteStringEnd = minuteEnd < 10 ? "0"+minuteEnd : ""+minuteEnd;
        String time = "You picked the following time: From - "+hourString+"h"+minuteString+" To - "+hourStringEnd+"h"+minuteStringEnd;

        timeTextView.setText(time);


    }
}
