package geolab.playoutside;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.FacebookSdk;
import com.facebook.Profile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import geolab.playoutside.adapters.ChatAdapter;
import geolab.playoutside.adapters.CommentsAdapter;
import geolab.playoutside.model.CommentsModel;
import geolab.playoutside.view.Launch;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;

public class Chat extends AppCompatActivity implements WaveSwipeRefreshLayout.OnRefreshListener{
    private RecyclerView.LayoutManager layoutManager;
    private  RecyclerView recyclerView;
    private WaveSwipeRefreshLayout swipeRefreshLayout;
    private RequestQueue requestQueue;
    private String getComment = "http://geolab.club/geolabwork/ika/chat.php";
    private FloatingActionButton fab;
    private ImageView arrow;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.chat);
        fab = (FloatingActionButton) findViewById(R.id.fab_profile);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog();

            }
        });
        arrow = (ImageView) findViewById(R.id.arrow);
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transport = new Intent(Chat.this, MainActivity.class);
                startActivity(transport);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.reclist);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        swipeRefreshLayout = (WaveSwipeRefreshLayout) findViewById(R.id.swiperefresh1);
        swipeRefreshLayout.setColorSchemeColors(Color.WHITE, Color.WHITE);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setWaveColor(Color.argb(100, 20, 150, 40));
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);

                                        getComments(getComment);
                                    }
                                }
        );
    }
    private void getComments(String url) {

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

                        ArrayList<CommentsModel> commentsModels = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject curObj = jsonArray.getJSONObject(i);


                            String user_id = curObj.getString("user_id");
                            String comment = curObj.getString("comment");
                            String datetime = curObj.getString("datetime");

                            CommentsModel commentsModel = new CommentsModel(user_id,datetime,comment);
                            commentsModels.add(commentsModel);
                        }
                        swipeRefreshLayout.setRefreshing(true);
                        ChatAdapter adapter = new ChatAdapter(Chat.this, commentsModels);

                        adapter.notifyDataSetChanged();

                        recyclerView.setAdapter(adapter);

                    }} catch (JSONException e) {
                    e.printStackTrace();
                }

                swipeRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error+"ragac");
            }
        });
        requestQueue = Volley.newRequestQueue(Chat.this);

        requestQueue.add(myRequest);
        swipeRefreshLayout.setRefreshing(false);
    }
    private void sendCommentToServer(final String comment){

        final String URL = "http://geolab.club/geolabwork/ika/insertchat.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // Toast.makeText(Chat.this, response, Toast.LENGTH_LONG).show();
                        getComments(getComment);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        new SweetAlertDialog(Chat.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Add message?")
                                .setContentText("for messaging You need to login")
                                .setCancelText("No,cancel")
                                .setConfirmText("Yes, Login")
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
                                        MainActivity.getInstance().finish();
                                        Intent intent = new Intent(Chat.this, Launch.class);
                                        startActivity(intent);
                                    }
                                })
                                .show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("user_id",Profile.getCurrentProfile().getId());
                params.put("comment",comment);
                params.toString();
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    protected void showInputDialog() {
        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(Chat.this);
        View promptView = layoutInflater.inflate(R.layout.input_comment_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Chat.this);
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        sendCommentToServer(String.valueOf(editText.getText()));
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void refresh(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getComments(getComment);
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 1300);
    }

    @Override
    public void onResume() {
        //mWaveSwipeRefreshLayout.setRefreshing(true);
        refresh();
        super.onResume();
    }
    @Override
    public void onRefresh() {
        refresh();
    }
}
