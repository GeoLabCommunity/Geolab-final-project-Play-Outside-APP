package geolab.playoutside;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import geolab.playoutside.adapters.CustomExpAdapter;
import geolab.playoutside.fragments.AllGamesFragment;
import geolab.playoutside.fragments.Category;
import geolab.playoutside.fragments.DialogFragment;
import geolab.playoutside.gcm.RegistrationIntentService;
import geolab.playoutside.model.ExpMenuItem;
import geolab.playoutside.model.SubMenu;
import geolab.playoutside.view.Lounch;


public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private MyPagerAdapter mSectionsPagerAdapter;

    private ArrayList<ExpMenuItem> menuItems;
    private ArrayList<Fragment> fragments;

    private String userId;
    private String birthday;
    private String name;
    private String email;
    private String imgUrl;
    private AccessToken access;

    private CircleImageView fb_image;
    private TextView fb_name;
    private TextView fb_age;
    private TextView fb_mail;

    private LinearLayout profile;
    private LinearLayout myGame;
    private LinearLayout logout;
    private LinearLayout allPlayer;
    public static MainActivity mainActivity;






    private String subcategory;

    /**
     * The {@link ViewPager} that will host the section contents.
     */


    private ViewPager mViewPager;

    Toolbar toolbar;
    DrawerLayout dlDrawer;
    ActionBarDrawerToggle drawerToggle;

    @Bind(R.id.tabs)
    protected TabLayout tabLayout;
    @Bind(R.id.exp_list_id)
    protected ExpandableListView expandableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mainActivity=this;




        isNetworkAvailable();

        final Intent intent = getIntent();

        final Bundle bundle = intent.getExtras();
        if (bundle != null) {
            name = (String) bundle.get("fb_name");
            userId = (String) bundle.get("fb_id");
            email = (String) bundle.get("fb_email");
            birthday = (String) bundle.get("fb_age");
            access = (AccessToken) bundle.get("access");

            sendPlayerInfo();

            fb_name = (TextView) findViewById(R.id.fb_name);
            fb_mail = (TextView) findViewById(R.id.fb_mail);
            fb_age = (TextView) findViewById(R.id.fb_age);
            fb_image = (CircleImageView) findViewById(R.id.fb_image);

            fb_name.setText(name);
            fb_mail.setText(email);


            imgUrl = "https://graph.facebook.com/" + userId + "/picture?height=400";
            Picasso.with(MainActivity.this)
                    .load(imgUrl)
                    .resize(400, 400)
                    .centerCrop()
                    .into(fb_image);

            SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date dat = sf.parse(birthday);
                Calendar birthDate = Calendar.getInstance();
                birthDate.setTimeInMillis(dat.getTime());
                fb_age.setText("(" + String.valueOf(getAge(birthDate, Calendar.getInstance())) + ")");
            } catch (ParseException e) {
                e.printStackTrace();
            }


            new PreferenceManager.OnActivityDestroyListener() {
                @Override
                public void onActivityDestroy() {
                    MainActivity.getInstance().finish();
                }
            };
        }


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Find our drawer view
        dlDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();

        // Tie DrawerLayout events to the ActionBarToggle
        dlDrawer.setDrawerListener(drawerToggle);


        profile = (LinearLayout) findViewById(R.id.profile);
        myGame = (LinearLayout) findViewById(R.id.myGame);
        logout = (LinearLayout) findViewById(R.id.logout);
        allPlayer = (LinearLayout) findViewById(R.id.allplayers);

        allPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        final float scale = getResources().getDisplayMetrics().density;
        int dpWidthInPx  = (int) (21 * scale);
        int dpHeightInPx = (int) (21 * scale);

        if(bundle == null) {

            logout.removeAllViews();
            ImageView login = new ImageView(getApplication());
            login.setImageResource(R.drawable.logout);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dpWidthInPx, dpHeightInPx);
            login.setLayoutParams(layoutParams);

            TextView loginText = new TextView(getApplication());
            loginText.setText("Login");
            loginText.setPadding(dpHeightInPx, 0, 0, 0);
            loginText.setTextColor(getResources().getColor(R.color.login_logout));
            logout.addView(login);
            logout.addView(loginText);
        }

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bundle != null) {
                    Intent transport = new Intent(MainActivity.this, ViewProfile.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("fb_id", userId);
                    transport.putExtra("Extra", bundle);
                    startActivity(transport);
                    dlDrawer.closeDrawers();
                }else{
                    Toast.makeText(MainActivity.this, "You must login to see your profile", Toast.LENGTH_LONG).show();
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                LoginManager.getInstance().logOut();
                Intent intent = new Intent(MainActivity.this, Lounch.class);
                startActivity(intent);
                dlDrawer.closeDrawers();
                finish();}
        });

        myGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bundle != null) {
                    mViewPager.setCurrentItem(0);
                    ((AllGamesFragment) mSectionsPagerAdapter.getItem(0)).admin(getApplicationContext(), userId);
                    dlDrawer.closeDrawers();
                } else {
                    Toast.makeText(MainActivity.this, "You must login to see your game", Toast.LENGTH_LONG).show();
                }

            }
        });


        ArrayList<Fragment> fragmentList = new ArrayList<>();
        ArrayList<String> titleList = new ArrayList<>();

        fragmentList.add(AllGamesFragment.newInstance("ALL"));
        fragmentList.add(Category.newInstance("BALL"));
        fragmentList.add(Category.newInstance("CARD"));
        fragmentList.add(Category.newInstance("TABLE"));
        fragmentList.add(Category.newInstance("ACTION"));

        titleList.add("ALL");
        titleList.add("BALL");
        titleList.add("CARD");
        titleList.add("TABLE");
        titleList.add("ACTION");


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), fragmentList, titleList);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"));


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bundle == null) {

                    DialogFragment dialogFragment = new DialogFragment();
                    dialogFragment.show(getFragmentManager(), "string");
                } else {
                    Intent addEvent = new Intent(MainActivity.this, Add_Event_Activity.class);
                    startActivity(addEvent);
                }
            }
        });

        menuItems = getData();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            expandableListView.setIndicatorBoundsRelative(730, 0);
        }
        CustomExpAdapter adapter = new CustomExpAdapter(this, menuItems);
        expandableListView.setAdapter(adapter);


        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                int index = (parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition, childPosition)));
                System.out.println(index + "444");

                parent.setItemChecked(index, true);
                SubMenu subMenu = menuItems.get(groupPosition).getSubMenus().get(childPosition);
                Toast.makeText(getApplicationContext(), subMenu.getMenuName(), Toast.LENGTH_SHORT).show();
                mViewPager.setCurrentItem(groupPosition + 1);
                dlDrawer.closeDrawers();
                subcategory = subMenu.getMenuName();

                ((Category) mSectionsPagerAdapter.getItem(groupPosition + 1)).updateSubcategoryData(subcategory);

                return true;
            }
        });
    }

    private ArrayList<ExpMenuItem> getData() {

        ArrayList<ExpMenuItem> items = new ArrayList<>();
        ArrayList<SubMenu> subMenus1 = new ArrayList<>();

        subMenus1.add(new SubMenu("football", R.drawable.foot));
        subMenus1.add(new SubMenu("basketball", R.drawable.bass));
        subMenus1.add(new SubMenu("rugby", R.drawable.rug));
        subMenus1.add(new SubMenu("volleyball", R.drawable.vol));

        ArrayList<SubMenu> subMenus2 = new ArrayList<>();

        subMenus2.add(new SubMenu("joker", R.drawable.jok));
        subMenus2.add(new SubMenu("poker", R.drawable.pok));


        ArrayList<SubMenu> subMenus3 = new ArrayList<>();

        subMenus3.add(new SubMenu("ping-pong", R.drawable.pingp));

        ArrayList<SubMenu> subMenus4 = new ArrayList<>();

        subMenus4.add(new SubMenu("badminton", R.drawable.bad));

        items.add(new ExpMenuItem("BALL", subMenus1, R.drawable.ball));
        items.add(new ExpMenuItem("CARD", subMenus2, R.drawable.card));
        items.add(new ExpMenuItem("TABLE", subMenus3, R.drawable.ping));
        items.add(new ExpMenuItem("ACTION", subMenus4, R.drawable.badm));

        return items;
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> list;
        private ArrayList<String> titleList;

        public MyPagerAdapter(FragmentManager fm, ArrayList<Fragment> list, ArrayList<String> titleList) {
            super(fm);
            this.list = list;
            this.titleList = titleList;
        }

        @Override
        public Fragment getItem(int pos) {
            return list.get(pos);
        }

        @Override
        public int getCount() {
            return 5;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, dlDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
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
                mViewPager.setCurrentItem(0);
                ((AllGamesFragment) mSectionsPagerAdapter.getItem(0)).updateData(getApplicationContext(), newText);
                return false;
            }

            public boolean onQueryTextSubmit(String query) {
                mViewPager.setCurrentItem(0);
                ((AllGamesFragment) mSectionsPagerAdapter.getItem(0)).updateData(getApplicationContext(), query);
                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);

        return super.onCreateOptionsMenu(menu);
    }

    private int getAge(Calendar birthDate, Calendar currentDate) {
        int age = currentDate.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);

        if (currentDate.get(Calendar.MONTH) < birthDate.get(Calendar.MONTH)) {
            age--;
        } else if (currentDate.get(Calendar.MONTH) == birthDate.get(Calendar.MONTH)
                && currentDate.get(Calendar.DAY_OF_MONTH) < birthDate.get(Calendar.DAY_OF_MONTH)) {
            age--;
        }

        return age;
    }


    public void isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
        } else {
            Toast.makeText(this, "Internet Connection Is Required", Toast.LENGTH_LONG).show();

        }
    }

    private void sendPlayerInfo() {


        final String URL = "http://geolab.club/geolabwork/ika/sendPlayerInfo/sendplayerinfo.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // System.out.println("error " +error.toString());
                        //Toast.makeText(MainActivity.this,"Please fill all fields",Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", userId);
                params.put("age", birthday);
                params.put("name", name);
                params.put("email", email);
                params.toString();
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Exit Application?");
        alertDialogBuilder
                .setMessage("Click yes to exit!")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                moveTaskToBack(true);
                                LoginManager.getInstance().logOut();
                                finish();
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(1);
                            }
                        })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    public static MainActivity getInstance(){
        return   mainActivity;
    }

}
