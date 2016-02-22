package geolab.playoutside;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import geolab.playoutside.adapters.CustomExpAdapter;
import geolab.playoutside.fragments.Action;
import geolab.playoutside.fragments.AllGamesFragment;
import geolab.playoutside.fragments.Ball;
import geolab.playoutside.fragments.CardGameFragment;
import geolab.playoutside.fragments.DialogFragment;
import geolab.playoutside.fragments.TableGameFragment;
import geolab.playoutside.model.ExpMenuItem;
import geolab.playoutside.model.SubMenu;


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


    /**
     * The {@link ViewPager} that will host the section contents.
     *
     */



    private ViewPager mViewPager;
    private int[] tabIcons = {
            R.drawable.allgamesb,
            R.drawable.ball,
            R.drawable.cardb,
            R.drawable._pingpong_b,
            R.drawable.activeb,
    };
    Toolbar toolbar;
    DrawerLayout dlDrawer;
    ActionBarDrawerToggle drawerToggle;

    @Bind(R.id.tabs)protected TabLayout tabLayout;
    @Bind(R.id.exp_list_id) protected ExpandableListView expandableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        access = null;


        final Intent intent = getIntent();


        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            name = (String) bundle.get("fb_name");
            userId = (String) bundle.get("fb_id");
            email = (String) bundle.get("fb_email");
            birthday = (String) bundle.get("fb_age");
            access = (AccessToken) bundle.get("access");

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
        }


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Find our drawer view
         dlDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
         drawerToggle = setupDrawerToggle();

        // Tie DrawerLayout events to the ActionBarToggle
        dlDrawer.setDrawerListener(drawerToggle);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout.setupWithViewPager(mViewPager);
        setupTabIcons();
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"));



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userId == null){

                DialogFragment dialogFragment = new DialogFragment();
                dialogFragment.show(getFragmentManager(), "string");}
                else{
                    Intent addEvent = new Intent(MainActivity.this, Add_Event_Activity.class);
                startActivity(addEvent);}
            }
        });

        menuItems = getData();

        expandableListView.setIndicatorBoundsRelative(730,0);
        CustomExpAdapter adapter = new CustomExpAdapter(this,menuItems);
        expandableListView.setAdapter(adapter);


        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                int index = (parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition, childPosition))) ;
                System.out.println(index+"444");

                parent.setItemChecked(index, true);
                SubMenu subMenu = menuItems.get(groupPosition).getSubMenus().get(childPosition);
                Toast.makeText(getApplicationContext(),subMenu.getMenuName(),Toast.LENGTH_SHORT).show();

                mViewPager.setCurrentItem(groupPosition+1);
                dlDrawer.closeDrawers();
                return true;
            }
        });


    }


    private ArrayList<ExpMenuItem> getData(){

        ArrayList<ExpMenuItem> items = new ArrayList<>();
        ArrayList<SubMenu> subMenus1 = new ArrayList<>();

        subMenus1.add(new SubMenu("football",R.drawable._football));
        subMenus1.add(new SubMenu("basketball",R.drawable._basketball));
        subMenus1.add(new SubMenu("rugby",R.drawable._rugby));
        subMenus1.add(new SubMenu("volleyball",R.drawable._volleyball));

        ArrayList<SubMenu> subMenus2 = new ArrayList<>();

        subMenus2.add(new SubMenu("joker",R.drawable.card));
        subMenus2.add(new SubMenu("poker",R.drawable.card));


        ArrayList<SubMenu> subMenus3 = new ArrayList<>();

        subMenus3.add(new SubMenu("ping-pong",R.drawable.card));

        ArrayList<SubMenu> subMenus4 = new ArrayList<>();

        subMenus4.add(new SubMenu("badminton",R.drawable.card));
        subMenus4.add(new SubMenu("frisbee",R.drawable.card));



        items.add(new ExpMenuItem("BALL",subMenus1,R.drawable.ball));
        items.add(new ExpMenuItem("CARD",subMenus2,R.drawable.card));
        items.add(new ExpMenuItem("TABLE",subMenus3,R.drawable.comp));
        items.add(new ExpMenuItem("ACTION",subMenus4,R.drawable.cal));


        return items;
    }





    private void setupTabIcons() {

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        tabLayout.getTabAt(4).setIcon(tabIcons[4]);

    }



    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch(pos) {
                case 1: return Ball.newInstance("fourthFragment");
                case 2: return CardGameFragment.newInstance("seventhFragment");
                case 3: return TableGameFragment.newInstance("secondFragment");
                case 4: return Action.newInstance("thirdFragment");




                default: return AllGamesFragment.newInstance("FirstFragment");
            }
        }

        @Override
        public int getCount() {
            return 5;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "All";
                case 1:
                    return "BALL";
                case 2:
                    return "CARD";
                case 3:
                    return "TABLE";
                case 4:
                    return "ACTION";

            }
            return null;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }
    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, dlDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private int getAge(Calendar birthDate, Calendar currentDate){
        int age = currentDate.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);

        if (currentDate.get(Calendar.MONTH) < birthDate.get(Calendar.MONTH)) {
            age--;
        } else if (currentDate.get(Calendar.MONTH) == birthDate.get(Calendar.MONTH)
                && currentDate.get(Calendar.DAY_OF_MONTH) < birthDate.get(Calendar.DAY_OF_MONTH)) {
            age--;
        }

        return age;
    }


    private void filter(){



        final String URL = "http://geolab.club/iraklilataria/ika/filter.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();
                        System.out.println("response " +response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("error " +error.toString());
                        Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("category",userId);


                params.toString();
                System.out.println("iiii    "+params);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
