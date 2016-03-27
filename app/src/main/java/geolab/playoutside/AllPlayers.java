package geolab.playoutside;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import java.util.WeakHashMap;

import geolab.playoutside.adapters.AllPlayerAdapter;
import geolab.playoutside.adapters.MyStickyAdapter;
import geolab.playoutside.fragments.AllGamesFragment;
import geolab.playoutside.model.AllPlayersModel;
import geolab.playoutside.model.MyEvent;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;
import se.emilsjolander.stickylistheaders.ExpandableStickyListHeadersListView;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class AllPlayers extends AppCompatActivity implements WaveSwipeRefreshLayout.OnRefreshListener{

   // private StickyListHeadersListView list;
    private WaveSwipeRefreshLayout swipeRefreshLayout;
    private RequestQueue requestQueue;
    private static String getAllPlayer = "http://geolab.club/geolabwork/ika/allplayer.php";
    private ArrayList<AllPlayersModel> allPlayersModelArrayList = new ArrayList<>();

    private ExpandableStickyListHeadersListView list;
    WeakHashMap<View,Integer> mOriginalViewHeightPool = new WeakHashMap<View, Integer>();

   private Toolbar toolbar;
   private ImageView arrow;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_players_container);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        arrow = (ImageView) findViewById(R.id.arrow);
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transport = new Intent(AllPlayers.this, MainActivity.class);
                Bundle bundle = new Bundle();
                transport.putExtra("profile_extra", bundle);
                startActivity(transport);
            }
        });


        swipeRefreshLayout = (WaveSwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setColorSchemeColors(Color.WHITE, Color.WHITE);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setWaveColor(Color.argb(100,20,150,40));

        list = (ExpandableStickyListHeadersListView) findViewById(R.id.list);

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


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.pdf_menu_search_item)
                .getActionView();
        if (null != searchView) {
            searchView.setSearchableInfo(searchManager
                    .getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);
        }

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                getAllPlayer(getAllPlayer+"?name="+newText);

                return false;
            }

            public boolean onQueryTextSubmit(String query) {

                getAllPlayer(getAllPlayer+"?name="+query);

                return false;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);

        return super.onCreateOptionsMenu(menu);
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

                    list.setAnimExecutor(new AnimationExecutor());

                    list.setAdapter(allPlayerAdapter);
                    list.setOnHeaderClickListener(new StickyListHeadersListView.OnHeaderClickListener() {
                        @Override
                        public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {
                            if (list.isHeaderCollapsed(headerId)) {
                                list.expand(headerId);
                            } else {
                                list.collapse(headerId);
                            }
                        }
                    });
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
                AllPlayerAdapter allPlayerAdapter = new AllPlayerAdapter(getApplication(), allPlayersModelArrayList);
                Toast toast = Toast.makeText(getApplicationContext(), "Nothing Found", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

                list.setAdapter(allPlayerAdapter);

            }
        });
        requestQueue = Volley.newRequestQueue(AllPlayers.this);
        requestQueue.add(myRequest);
        swipeRefreshLayout.setRefreshing(false);
    }


    public void check() {
        ConnectivityManager connMgr = (ConnectivityManager) getApplication()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected())

        {
            // fetch data
        } else

        {
            Toast.makeText(getApplicationContext(), "Internet Connection Is Required", Toast.LENGTH_LONG).show();
        }
    }




    class AnimationExecutor implements ExpandableStickyListHeadersListView.IAnimationExecutor {

        @Override
        public void executeAnim(final View target, final int animType) {
            if(ExpandableStickyListHeadersListView.ANIMATION_EXPAND==animType&&target.getVisibility()==View.VISIBLE){
                return;
            }
            if(ExpandableStickyListHeadersListView.ANIMATION_COLLAPSE==animType&&target.getVisibility()!=View.VISIBLE){
                return;
            }
            if(mOriginalViewHeightPool.get(target)==null){
                mOriginalViewHeightPool.put(target,target.getHeight());
            }
            final int viewHeight = mOriginalViewHeightPool.get(target);
            float animStartY = animType == ExpandableStickyListHeadersListView.ANIMATION_EXPAND ? 0f : viewHeight;
            float animEndY = animType == ExpandableStickyListHeadersListView.ANIMATION_EXPAND ? viewHeight : 0f;
            final ViewGroup.LayoutParams lp = target.getLayoutParams();
            ValueAnimator animator = ValueAnimator.ofFloat(animStartY, animEndY);
            animator.setDuration(200);
            target.setVisibility(View.VISIBLE);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    if (animType == ExpandableStickyListHeadersListView.ANIMATION_EXPAND) {
                        target.setVisibility(View.VISIBLE);
                    } else {
                        target.setVisibility(View.GONE);
                    }
                    target.getLayoutParams().height = viewHeight;
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    lp.height = ((Float) valueAnimator.getAnimatedValue()).intValue();
                    target.setLayoutParams(lp);
                    target.requestLayout();
                }
            });
            animator.start();

        }
    }
    private void refresh(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getAllPlayer(getAllPlayer);
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
