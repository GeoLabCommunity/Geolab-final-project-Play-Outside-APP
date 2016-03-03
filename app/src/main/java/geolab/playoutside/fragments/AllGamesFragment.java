package geolab.playoutside.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.net.InetAddress;
import java.util.ArrayList;

import geolab.playoutside.MainActivity;
import geolab.playoutside.R;
import geolab.playoutside.adapters.MyStickyAdapter;
import geolab.playoutside.db.Data;
import geolab.playoutside.model.MyEvent;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by GeoLab on 1/11/16.
 */
public class AllGamesFragment extends android.support.v4.app.Fragment implements SwipeRefreshLayout.OnRefreshListener {

    StickyListHeadersListView list;
    private ArrayList<MyEvent> myEvents;
    private JsonArrayRequest jsonObjectRequest;
//    private RequestQueue requestQueue;
    private static String GET_JSON_INFO = "http://geolab.club/iraklilataria/ika/getdata.php";
    private static String GET_JSON_INFO1 = "http://geolab.club/iraklilataria/ika/search.php";
    private SwipeRefreshLayout swipeRefreshLayout;
    private int categoryId;
    private String stringSearch;
    private String tabTitle;

    private RequestQueue requestQueue;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        requestQueue = Volley.newRequestQueue(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        stringSearch = getActivity().getIntent().getStringExtra("search");
        System.out.println(stringSearch+"strsrch");

        View v = inflater.inflate(R.layout.sport_fragment, container, false);

        list = (StickyListHeadersListView) v.findViewById(R.id.list);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        swipeRefreshLayout.setRefreshing(true);

                                        getJSONInfo(GET_JSON_INFO);
                                    }
                                }
        );


        return v;
    }

    public void updateData(Context ctx, String searchString){
        requestQueue = Volley.newRequestQueue(ctx);
        getJSONInfo(GET_JSON_INFO1 + "?search=" + searchString);
    }

    public static AllGamesFragment newInstance(String text) {

        AllGamesFragment f = new AllGamesFragment();
        Bundle b = new Bundle();
        b.putString("tabTitle", text);

        f.setArguments(b);

        return f;
    }

    private void getJSONInfo(String url) {



        JSONObject categoryObj = new JSONObject();
        try {
            categoryObj.put("search", tabTitle);
        } catch (JSONException e) {
            e.printStackTrace();
        }



        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.GET
                , url
                , null
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


                        String user_id = curObj.getString("user_id");
                        String subcategory = curObj.getString("subcategory");
                        String description = curObj.getString("description");
                        String date = curObj.getString("date");
                        String time = curObj.getString("time");
                        String count = curObj.getString("count");
                        String location = curObj.getString("location");
                        String latitude = curObj.getString("latitude");
                        String longitude = curObj.getString("longitude");

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


                        MyEvent myEvent = new MyEvent(user_id, time, date, subcategory, description, location, count, latitude, longitude, categoryId);
                        myEvents.add(myEvent);
                    }

                    System.out.println("jjj   " + myEvents);
                    MyStickyAdapter adapter = new MyStickyAdapter(getActivity(), myEvents);

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
        requestQueue.add(myRequest);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        getJSONInfo(GET_JSON_INFO);
    }

    public void check() {
        ConnectivityManager connMgr = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

    if(networkInfo!=null&&networkInfo.isConnected())

    {
        // fetch data
    }

    else

    {
        Toast.makeText(getContext(), "Internet Connection Is Required", Toast.LENGTH_LONG).show();
    }
}
}
