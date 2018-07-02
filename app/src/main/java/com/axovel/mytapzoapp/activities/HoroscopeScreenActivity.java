package com.axovel.mytapzoapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.axovel.mytapzoapp.R;
import com.axovel.mytapzoapp.app.AppController;
import com.axovel.mytapzoapp.customAdapter.HoroScope_pagerAdapter;
import com.axovel.mytapzoapp.models.DataObject;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

public class HoroscopeScreenActivity extends AppCompatActivity {
    private Button btn;
    private ViewPager viewPager;
    private String strHoro;
    private String date;
    private String horoscope;
    private String sunsign;
    private String new_sunsign;
    private String month;
    private int horoview;
    private String Aries_text = "Mar 21-Apr 20";
    private String Taurus_text = "Apr 21-May 21";
    private String Gemini_text = "May 22-Jun 21";
    private String Cancer_text = "Jun 22-Jul 22";
    private String Leo_text = "Jul 23- Aug 22";
    private String Virgo_text = "Aug 23-Sep 23";
    private String Libra_text = "Sep 24-Oct 23";
    private String Scorpio_text = "Oct 24-Nov 22";
    private String Sagittarius_text = "Nov 23-Dec 21";
    private String Capricorn_text = "Dec 22-Jan 20";
    private String Aquarius_text = "Jan 21-Feb 19";
    private String Pisces_text = "Feb 20-Mar 20";

    //  private ArrayList<DataObject> dataObjectList;
    //  List<DataObject> arrayList = new ArrayList<DataObject>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horoscope_screen);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Aries"));
        tabLayout.addTab(tabLayout.newTab().setText("Taurus"));
        tabLayout.addTab(tabLayout.newTab().setText("Gemini"));
        tabLayout.addTab(tabLayout.newTab().setText("Cancer"));
        tabLayout.addTab(tabLayout.newTab().setText("Leo"));
        tabLayout.addTab(tabLayout.newTab().setText("Virgo"));
        tabLayout.addTab(tabLayout.newTab().setText("Libra"));
        tabLayout.addTab(tabLayout.newTab().setText("Scorpio"));
        tabLayout.addTab(tabLayout.newTab().setText("Sagittarius"));
        tabLayout.addTab(tabLayout.newTab().setText("Capricorn"));
        tabLayout.addTab(tabLayout.newTab().setText("Aquarius"));
        tabLayout.addTab(tabLayout.newTab().setText("Pisces"));
//        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);


              viewPager = (ViewPager) findViewById(R.id.pager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

       // tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));



        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.getTabAt(position).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                // uploadFunction("");
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



//        horoScope_pagerAdapter = new HoroScope_pagerAdapter(this,null);
//
//        viewPager.setAdapter(horoScope_pagerAdapter);

//        uploadFunction("Aries");
//        uploadFunction("Taurus");
//        uploadFunction("Gemini");
//        uploadFunction("Cancer");
//        uploadFunction("Leo");
//        uploadFunction("Virgo");
//        uploadFunction("Libra");
//        uploadFunction("Scorpio");
//        uploadFunction("Capricom");
//        uploadFunction("Aquarius");
//        uploadFunction("Pisces");


//        btn= (Button) findViewById(R.id.send);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String url="olacabs://app/launch?lat=28.585106&lng=77.071299&category=compact&utm_source=317d32c3ae534f39a911551538e50002&landing_page=bk&drop_lat=28.641529&drop_lng=77.120915";
//                try {
//                    PackageManager pm = getApplicationContext().getPackageManager();
//                    pm.getPackageInfo("com.olacab", PackageManager.GET_ACTIVITIES);
////                    String uri =
////                            "uber://?action=setPickup&pickup=my_location&client_id=<CLIENT_ID>";
//                    Intent intent = new Intent(Intent.ACTION_VIEW);
//                    intent.setData(Uri.parse(url));
//                    startActivity(intent);
//                } catch (PackageManager.NameNotFoundException e) {
//                    // No Uber app! Open mobile website.
//                    Intent i = new Intent(Intent.ACTION_VIEW);
//                    i.setData(Uri.parse(url));
//                    startActivity(i);
//                }
//            }
//        });

    }

    //        uploadFunction("Aries");
//        uploadFunction("Taurus");
//        uploadFunction("Gemini");
//        uploadFunction("Cancer");
//        uploadFunction("Leo");
//        uploadFunction("Virgo");
//        uploadFunction("Libra");
//        uploadFunction("Scorpio");
//        uploadFunction("Capricom");
//        uploadFunction("Aquarius");
//        uploadFunction("Pisces");


    @Override
    protected void onResume() {
        super.onResume();
        manageViewPager();


    }

    private String[] urlStrs = {"Aries", "Taurus", "Gemini", "Cancer", "Leo", "Virgo", "Libra", "Scorpio", "Sagittarius", "Capricorn", "Aquarius", "Pisces"};

    private void manageViewPager() {

        ArrayList<View> pagerViews = new ArrayList<>();

        for (String url : urlStrs) {

            View view = LayoutInflater.from(this).inflate(R.layout.horoscope_pager_layout, null);
            getDataAndSetToView(url, view);

            pagerViews.add(view);
        }

        HoroScope_pagerAdapter horoScope_pagerAdapter = new HoroScope_pagerAdapter(getApplicationContext(), pagerViews);
        viewPager.setAdapter(horoScope_pagerAdapter);
    }

    private void getDataAndSetToView(String urlStr, final View view) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://horoscope-api.herokuapp.com/horoscope/today/" + urlStr, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.d("welcome  ", response);
                try {
                    JSONObject json = (JSONObject) new JSONTokener(response).nextValue();
                    date = json.getString("date");
                    horoscope = json.getString("horoscope");
                    sunsign = json.getString("sunsign");
                    //horoscope = horoscope.replace("[","").replace("'","").replace("\n","").replace("]","");
                    horoscope = horoscope.replaceAll("\\[|\\]|'|\"|", "");
                    horoscope = horoscope.replace("\\n", "\n");
                    horoscope = horoscope.trim();

                    Log.d("welcome", date);
                    Log.d("welcome", horoscope);
                    Log.d("welcome", sunsign);

                    // int image = 0;


                    if (sunsign.equals("Aries")) {
                        month = Aries_text;
                        //image = R.drawable.aries_icon;
                        horoview= R.drawable.aries_icon;
                    }
                    if (sunsign.equals("Taurus")) {
                        month = Taurus_text;
                        horoview=R.drawable.taurus__icon;
                    }
                    if (sunsign.equals("Gemini")) {
                        month = Gemini_text;
                        horoview=R.drawable.gemini_icon;
                    }
                    if (sunsign.equals("Cancer")) {
                        month = Cancer_text;
                        horoview=R.drawable.cancer_icon;
                    }
                    if (sunsign.equals("Leo")) {
                        month = Leo_text;
                        horoview=R.drawable.leo_icon;
                    }
                    if (sunsign.equals("Virgo")) {
                        month = Virgo_text;
                        horoview=R.drawable.virgo_icon;
                    }
                    if (sunsign.equals("Libra")) {
                        month = Libra_text;
                        horoview=R.drawable.saa;
                    }
                    if (sunsign.equals("Scorpio")) {
                        month = Scorpio_text;
                        horoview=R.drawable.scorpio_icon;
                    }
                    if (sunsign.equals("Sagittarius")) {
                        month = Sagittarius_text;
                        horoview=R.drawable.sagittarius_icon;
                    }
                    if (sunsign.equals("Capricorn")) {
                        month = Capricorn_text;
                        horoview=R.drawable.capricorn_icon;
                    }
                    if (sunsign.equals("Aquarius")) {
                        month = Aquarius_text;
                        horoview=R.drawable.aquarius_icon;
                    }
                    if (sunsign.equals("Pisces")) {
                        month = Pisces_text;
                        horoview=R.drawable.pisces_icon;
                    }

                    setDataToView(new DataObject(month, sunsign, horoscope,horoview), view );

//                    dataObjectList.add(new DataObject(date,horoscope,sunsign));
//                    arrayList.add();
                }
//               zewPager.setAdapter(horoScope_pagerAdapter);

                catch (JSONException e) {
                    e.printStackTrace();
//                    Toast.makeText(getApplicationContext(), R.string.unable_to_fetch_data, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // Log.d("error ",error.getLocalizedMessage());


//                Toast.makeText(getApplicationContext(), R.string.someting_went_wrong, Toast.LENGTH_SHORT).show();
            }
        });

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void setDataToView(DataObject dataObject, View view) {

        TextView date = (TextView) view.findViewById(R.id.month_id1);
        TextView sunsign = (TextView) view.findViewById(R.id.sunsign_id);
        TextView horoscope = (TextView) view.findViewById(R.id.horoscope_id);
        ImageView horoview=(ImageView) view.findViewById(R.id.horo_img);

        date.setText(dataObject.getDate());
        sunsign.setText(dataObject.getSunsign());
        horoscope.setText(dataObject.getHoroscope());

        horoview.setBackgroundResource(dataObject.getHoroview());
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), HomeScreenActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }


    public void backButton(View view){
//        Intent intent=new Intent(HoroscopeScreenActivity.this,HomeScreenActivity.class);
//        startActivity(intent);
        finish();
    }
}
