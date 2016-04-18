package geolab.playoutside.fragments;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

import geolab.playoutside.R;
import geolab.playoutside.adapters.MyStickyAdapter;
import geolab.playoutside.model.MyEvent;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;
import se.emilsjolander.stickylistheaders.ExpandableStickyListHeadersListView;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by GeoLab on 1/11/16.
 */
public class Category extends android.support.v4.app.Fragment implements WaveSwipeRefreshLayout.OnRefreshListener {

    private ArrayList<MyEvent> myEvents = new ArrayList<>();
    private JsonArrayRequest jsonObjectRequest;
    private RequestQueue requestQueue;
    private static String GET_JSON_INFO = "http://geolab.club/geolabwork/ika/filter.php";
    private WaveSwipeRefreshLayout swipeRefreshLayout;
    private int categoryId;
    private String stringSearch;
    private String tabTitle;
    private String subcategory;

    private  long days;


    private ExpandableStickyListHeadersListView list;
    WeakHashMap<View,Integer> mOriginalViewHeightPool = new WeakHashMap<View, Integer>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        stringSearch = getActivity().getIntent().getStringExtra("search");
        System.out.println(stringSearch+"strsrch");

        View v = inflater.inflate(R.layout.content_fragment, container, false);


        tabTitle = getArguments().getString("tabTitle");
        subcategory= getArguments().getString("subcategory");
        System.out.println(subcategory+"iiii");





        list = (ExpandableStickyListHeadersListView) v.findViewById(R.id.list);
        swipeRefreshLayout = (WaveSwipeRefreshLayout) v.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setColorSchemeColors(Color.WHITE, Color.WHITE);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setWaveColor(Color.argb(100,20,150,40));
        check();
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        check();

                                        swipeRefreshLayout.setRefreshing(true);



                                        getJSONInfo(GET_JSON_INFO+"?category="+tabTitle);
                                    }
                                }
        );


        return v;
    }

    public static Category newInstance(String text) {

        Category f = new Category();
        Bundle b = new Bundle();
        b.putString("tabTitle", text);
        f.setArguments(b);

        return f;
    }
    public void updateSubcategoryData(String subcategory){
        getJSONInfo(GET_JSON_INFO+"?category="+subcategory);
    }

    private void getJSONInfo(String url) {

        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.GET
                , url
                , null
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response+"jres");

                JSONArray jsonArray = null;
                try {
                    jsonArray = response.getJSONArray("data");

                    if (jsonArray == null) {
                        System.out.println("nothing found");
                    }else{

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

                            switch (subcategory) {
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
                                case "Baseball":
                                    categoryId=4;
                                    break;
                                case "Golf":
                                    categoryId=5;
                                    break;
                                case "Joker":
                                    categoryId=6;
                                    break;
                                case "Poker":
                                    categoryId=7;
                                    break;
                                case "Seka":
                                    categoryId=8;
                                    break;
                                case "Bura":
                                    categoryId=9;
                                    break;
                                case "Blackjack":
                                    categoryId=10;
                                    break;
                                case "Ping-pong":
                                    categoryId=11;
                                    break;
                                case "Backgammon":
                                    categoryId=12;
                                    break;
                                case "Checkers":
                                    categoryId=13;
                                    break;
                                case "Chess":
                                    categoryId=14;
                                    break;
                                case "Domino":
                                    categoryId=15;
                                    break;
                                case "Billiard":
                                    categoryId=16;
                                    break;
                                case "Badminton":
                                    categoryId=17;
                                    break;
                                case "Tennis":
                                    categoryId=18;
                                    break;
                                case "Dartboard":
                                    categoryId=19;
                                    break;
                                case "Frisbee":
                                    categoryId=20;
                                    break;
                                case "Bowling":
                                    categoryId=21;
                                    break;
                                default:
                                    categoryId = 0;
                            }
                            checkDate(date);

                            if(days>=0){

                            MyEvent myEvent = new MyEvent(eventId, user_id, time, date, subcategory, description, location, count, latitude, longitude, categoryId, event_players);
                            myEvents.add(myEvent);}
                        }

                        MyStickyAdapter adapter = new MyStickyAdapter(getActivity(), myEvents);
                        list.setAnimExecutor(new AnimationExecutor());

                        list.setAdapter(adapter);
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
                        list.setOnItemClickListener(adapter.listener);

                    }
                }catch(JSONException e){
                        e.printStackTrace();
                    }


                    swipeRefreshLayout.setRefreshing(false);
                }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast toast = Toast.makeText(getContext(),"Nothing Found", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                MyStickyAdapter adapter = new MyStickyAdapter(getActivity(),myEvents);
                list.setAnimExecutor(new AnimationExecutor());

                list.setAdapter(adapter);
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
                list.setOnItemClickListener(adapter.listener);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(myRequest);
        swipeRefreshLayout.setRefreshing(false);
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
    private class Task extends AsyncTask<Void, Void, String[]> {


        @Override
        protected String[] doInBackground(Void... params) {
            return new String[0];
        }

        @Override protected void onPostExecute(String[] result) {
            // Call setRefreshing(false) when the list has been refreshed.
            swipeRefreshLayout.setRefreshing(false);
            super.onPostExecute(result);
        }


    }
    private void refresh(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getJSONInfo(GET_JSON_INFO+"?category="+tabTitle);
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
    public void checkDate(String date){
        String year = date.split("-")[0];
        String month = date.split("-")[1];
        String  day = date.split("-")[2];
        String everything = day+"/"+month+"/"+year;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
        Date d = null;
        try {
            d = formatter.parse(everything);//catch exception
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        Calendar thatDay = Calendar.getInstance();
        thatDay.setTime(d);



        Calendar today = Calendar.getInstance();

        long diff = thatDay.getTimeInMillis() - today.getTimeInMillis();

        days = Math.round(diff * 1f / TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)+0.5);
    }
}
