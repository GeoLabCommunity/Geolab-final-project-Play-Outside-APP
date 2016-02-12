package geolab.playoutside;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import geolab.playoutside.adapters.CustomExpAdapter;
import geolab.playoutside.adapters.MyStickyAdapter;
import geolab.playoutside.fragments.Action;
import geolab.playoutside.fragments.AllGamesFragment;
import geolab.playoutside.fragments.Ball;
import geolab.playoutside.fragments.CardGameFragment;
import geolab.playoutside.fragments.Computer;
import geolab.playoutside.fragments.DialogFragment;
import geolab.playoutside.fragments.Gun;
import geolab.playoutside.fragments.TableGameFragment;
import geolab.playoutside.model.ExpMenuItem;
import geolab.playoutside.model.MyEvent;
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

    /**
     * The {@link ViewPager} that will host the section contents.
     *
     */



    private ViewPager mViewPager;
    private int[] tabIcons = {
            R.drawable.comp,
            R.drawable.ball,
            R.drawable.card,
            R.drawable.cal,
            R.drawable.comp,
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

        isInternetAvailable() ;

        fragments = new ArrayList<>();
        fragments.add(new Action());
        fragments.add(new AllGamesFragment());
        fragments.add(new Ball());
        fragments.add(new CardGameFragment());
        fragments.add(new Gun());


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

                DialogFragment dialogFragment = new DialogFragment();
                dialogFragment.show(getFragmentManager(), "string");

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
                System.out.println(index);

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

    public boolean isInternetAvailable() {

        try {
            Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1    www.google.com");
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal == 0);
            if (reachable) {
                System.out.println("Internet access");
                return reachable;
            } else {
                System.out.println("No Internet access");
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
        return false;
    }



}
