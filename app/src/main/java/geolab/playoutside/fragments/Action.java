package geolab.playoutside.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import geolab.playoutside.R;
import geolab.playoutside.adapters.MyStickyAdapter;
import geolab.playoutside.db.Data;
import geolab.playoutside.model.MyEvent;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by GeoLab on 1/11/16.
 */
public class Action extends android.support.v4.app.Fragment implements SwipeRefreshLayout.OnRefreshListener {

    StickyListHeadersListView list;
    private ArrayList<MyEvent> myEvents;
    private JsonArrayRequest jsonObjectRequest;
    private RequestQueue requestQueue;
    private static String GET_JSON_INFO = "http://geolab.club/iraklilataria/ika/category/action.php";
    private SwipeRefreshLayout swipeRefreshLayout;
    private  int categoryId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.sport_fragment, container, false);

        list= (StickyListHeadersListView) v.findViewById(R.id.list);
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

    public static Action newInstance(String text) {

        Action f = new Action();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }

    private void getJSONInfo(String url) {
        swipeRefreshLayout.setRefreshing(true);
        myEvents = new ArrayList<>();
        if (requestQueue == null) {
            requestQueue = new Volley().newRequestQueue(getContext());
        }
        jsonObjectRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                try {
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
                            case "Badminton":
                                categoryId=7;
                                break;
                            default:
                                categoryId=7;
                        }


                        MyEvent myEvent = new MyEvent( user_id, time,date,subcategory, description, location, count,latitude,longitude,categoryId);
                        myEvents.add(myEvent);
                    }

                    System.out.println("jjj   "+myEvents);
                    MyStickyAdapter adapter = new MyStickyAdapter(getActivity(), myEvents);

                    list.setAdapter(adapter);
                    list.setOnItemClickListener(adapter.listener);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        volleyError.getCause();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onRefresh() {
        getJSONInfo(GET_JSON_INFO);
    }
}
