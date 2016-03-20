package geolab.playoutside.view;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import geolab.playoutside.R;
import geolab.playoutside.adapters.MyStickyAdapter;
import geolab.playoutside.model.MyEvent;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class Search extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private String search;
    StickyListHeadersListView list;
    private ArrayList<MyEvent> myEvents;
    private JsonArrayRequest jsonObjectRequest;
    private RequestQueue requestQueue;
    private static String GET_JSON_INFO = "http://geolab.club/geolabwork/ika/search.php";
    private SwipeRefreshLayout swipeRefreshLayout;
    private int categoryId;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        final Intent intent = getIntent();


        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            search = (String) bundle.get("search");

        }

        list = (StickyListHeadersListView) findViewById(R.id.list);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        swipeRefreshLayout.setRefreshing(true);

                                        getJSONInfo(GET_JSON_INFO+"?search="+search);
                                    }
                                }
        );



    }
    private void getJSONInfo(String url) {


        JSONObject categoryObj = new JSONObject();
        try {
            categoryObj.put("category", "TABLE");
        } catch (JSONException e) {
            e.printStackTrace();
        }



        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.GET
                , url
                , categoryObj
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONArray jsonArray = null;
                try {
                    jsonArray = response.getJSONArray("data");
                    System.out.println(jsonArray+"9999");


                    ArrayList<MyEvent> myEvents = new ArrayList<>();
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
                        myEvents.add(myEvent);
                    }

                    System.out.println("jjj   " + myEvents);
                    MyStickyAdapter adapter = new MyStickyAdapter(getApplication(), myEvents);

                    list.setAdapter(adapter);
                    list.setOnItemClickListener(adapter.listener);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                swipeRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error+"   nnn");

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplication());
        requestQueue.add(myRequest);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        getJSONInfo(GET_JSON_INFO);
    }

    public void check() {
        ConnectivityManager connMgr = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if(networkInfo!=null&&networkInfo.isConnected())

        {
            //fetch data
        }

        else

        {
            Toast.makeText(getApplicationContext(), "Internet Connection Is Required", Toast.LENGTH_LONG).show();
        }
    }
}
