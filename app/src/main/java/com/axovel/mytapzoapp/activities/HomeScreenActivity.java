package com.axovel.mytapzoapp.activities;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.axovel.mytapzoapp.R;
import com.axovel.mytapzoapp.customAdapter.ComponentRecyclerAdapter;
import com.axovel.mytapzoapp.customAdapter.HomeScreenBannerPagerAdapter;
import com.axovel.mytapzoapp.models.APIResponse;
import com.axovel.mytapzoapp.models.CabDataList;
import com.axovel.mytapzoapp.models.TapzoComponentData;
import com.axovel.mytapzoapp.service.CustomAccessibilityService;
import com.axovel.mytapzoapp.utils.PreferenceStore;
import com.axovel.mytapzoapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import static com.google.ads.AdRequest.LOGTAG;

public class HomeScreenActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    PagerAdapter pagerAdapter;
    ViewPager viewPager;
    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    HomeScreenBannerPagerAdapter homeScreenBannerPagerAdapter;
    CabDataList cabDataList;
    PreferenceStore preferenceStore = new PreferenceStore();

    LinearLayout accessibilityLinearLayOut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        accessibilityLinearLayOut = (LinearLayout) findViewById(R.id.accessibilityLinear);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewPager = (ViewPager) findViewById(R.id.pager);
        homeScreenBannerPagerAdapter = new HomeScreenBannerPagerAdapter(getApplicationContext());

        viewPager.setAdapter(homeScreenBannerPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabDots);
        tabLayout.setupWithViewPager(viewPager, true);
        recyclerView = (RecyclerView) findViewById(R.id.homeScreenModuleComponent);
        recyclerView.setPadding(Utils.dpToPx(getApplicationContext(), 1), 0, 0, 0);
        recyclerView.setHasFixedSize(true);
        gridLayoutManager = new GridLayoutManager(HomeScreenActivity.this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);

        List<TapzoComponentData> tapzoComponentData = getDataSource();
        ComponentRecyclerAdapter componentRecyclerAdapter = new ComponentRecyclerAdapter((ArrayList<TapzoComponentData>) tapzoComponentData, this);

        recyclerView.setAdapter(componentRecyclerAdapter);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        isAccessibilitySettingsOn(getApplicationContext());

    }

    private List<TapzoComponentData> getDataSource() {
        List<TapzoComponentData> data = new ArrayList<TapzoComponentData>();
        data.add(new TapzoComponentData("Cabs", "", R.drawable.ic_taxi));
        data.add(new TapzoComponentData("Food", "offers", R.drawable.ic_dinner_out));
        data.add(new TapzoComponentData("Shopping", "", R.drawable.ic_online_shopping));
        data.add(new TapzoComponentData("Flights", "offers", R.drawable.ic_flightes));
        data.add(new TapzoComponentData("Horoscope", "", R.drawable.horoscope));
        data.add(new TapzoComponentData("Bill Payment", "", R.drawable.ic_deals));
        data.add(new TapzoComponentData("Movie Booking", "", R.drawable.movies_icons));
        data.add(new TapzoComponentData("Hotels", "", R.drawable.my_hotels_icons));
        data.add(new TapzoComponentData("cab", "offers", R.drawable.ic_flightes));
        return data;


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void popupFun(View view) {

//        PreferenceStore.getPreference(HomeScreenActivity.this).putBoolean("VALUEBOOLEAN", true);
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);

        Intent intent = new Intent(getApplicationContext(), PopupScreenActivity.class);
        startActivity(intent);

    }

    public void goTosettingFunction(View view) {

        if (!isAccessibilitySettingsOn(getApplicationContext())) {
            startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
        }

//        startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
//        startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));


    }

    private void getAccebilityLayoutVisible() {

        accessibilityLinearLayOut.animate().translationX(0).translationY(0).setDuration(3000);


    }

    private void visibilityGoneAccessibility() {


        accessibilityLinearLayOut.animate().translationX(0).translationY(140).setDuration(3000);


    }

    private String TAG = "AccessibilitySettings";

    // To check if service is enabled
    private boolean isAccessibilitySettingsOn(Context mContext) {
        int accessibilityEnabled =0;


        final String service = getPackageName() + "/"+CustomAccessibilityService.class.getCanonicalName();

        try {
            accessibilityEnabled = Settings.Secure.getInt(mContext.getApplicationContext().getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED);
            Log.d(TAG, "accessibilityEnabled = " + accessibilityEnabled);
        } catch (Settings.SettingNotFoundException e) {
            Log.d(TAG, "Error finding setting, default accessibility to not found: " + e.getMessage());
        }

        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {



                visibilityGoneAccessibility();




            Log.d(TAG, "***ACCESSIBILITY IS ENABLED*** -----------------");
            String settingValue = Settings.Secure.getString(mContext.getApplicationContext().getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();
                    Log.d(TAG, "-------------- > accessibilityService :: " + accessibilityService + " " + service);
                    if (accessibilityService.equalsIgnoreCase(service)) {
                        Log.v(TAG, "We've found the correct setting - accessibility is switched on!");
                        return true;
                    }
                }
            } else {
                Log.d(TAG, "-------------- > accessibilityService :: settingValue = null");
            }
        } else {



                getAccebilityLayoutVisible();





            Log.d(TAG, "***ACCESSIBILITY IS DISABLED***");
        }

        return false;
    }
}
