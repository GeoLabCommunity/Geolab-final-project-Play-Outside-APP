package geolab.playoutside;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import geolab.playoutside.adapters.AllPlayerAdapter;
import geolab.playoutside.adapters.MyStickyAdapter;
import geolab.playoutside.model.AllPlayersModel;
import geolab.playoutside.model.MyEvent;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class AllPlayers extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private StickyListHeadersListView list;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RequestQueue requestQueue;
    private static String getAllPlayer = "http://geolab.club/geolabwork/ika/allplayer.php";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_players_container);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(AllPlayers.this);
        swipeRefreshLayout.setRefreshing(true);


        list = (StickyListHeadersListView) findViewById(R.id.list);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        swipeRefreshLayout.setRefreshing(true);
                                        check();

                                        getAllPlayer(getAllPlayer);

                                    }
                                }
        );



    }

    private void getAllPlayer(String url) {

        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.GET
                , url
                , null
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONArray jsonArray = null;
                try {
                    jsonArray = response.getJSONArray("data");


                    ArrayList<AllPlayersModel> allPlayersModels = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject curObj = jsonArray.getJSONObject(i);

                        String user_id = curObj.getString("user_id");
                        String name = curObj.getString("name");
                        String mail = curObj.getString("email");
                        String age = curObj.getString("age");
                        String reiting = curObj.getString("reiting");




                        AllPlayersModel allPlayersModel = new AllPlayersModel(user_id, name, mail, age, reiting);
                        allPlayersModels.add(allPlayersModel);
                    }

                    AllPlayerAdapter allPlayerAdapter = new AllPlayerAdapter(getApplication(), allPlayersModels);

                    list.setAdapter(allPlayerAdapter);
                    list.setOnItemClickListener(allPlayerAdapter.listener);
                    //               list.setOnItemLongClickListener(adapter.longClickListener);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                swipeRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue = Volley.newRequestQueue(AllPlayers.this);
        requestQueue.add(myRequest);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        getAllPlayer(getAllPlayer);
    }

    public void check() {
        ConnectivityManager connMgr = (ConnectivityManager) getApplication()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if(networkInfo!=null&&networkInfo.isConnected())

        {
            // fetch data
        }

        else

        {
            Toast.makeText(getApplicationContext(), "Internet Connection Is Required", Toast.LENGTH_LONG).show();
        }
    }

}
