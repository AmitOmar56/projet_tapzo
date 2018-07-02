package com.axovel.mytapzoapp.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.axovel.mytapzoapp.CustomViewGroups.CustomTimeVGroup;
import com.axovel.mytapzoapp.R;
import com.axovel.mytapzoapp.app.AppController;
import com.axovel.mytapzoapp.customAdapter.BookingAdapter;
import com.axovel.mytapzoapp.customAdapter.ExpandableListAdapter;
import com.axovel.mytapzoapp.models.Booking;
import com.axovel.mytapzoapp.utils.MyProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.axovel.mytapzoapp.activities.FoodScreenActivity.sStringToHMACMD5;

public class BookingActivity extends AppCompatActivity {
    ExpandableListAdapter listAdapter;
    //    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    private List<Booking> bookingList = new ArrayList<>();
    private RecyclerView recyclerView;
    private BookingAdapter mAdapter;
    private String result;
    private String offer;
    String str;
    String restorent_id;
    String Booking_result;
    Timestamp timestamp;
    Long time_id;
    String time_id1;
    String Full_date;
    String current_Time;
    RecyclerView mRecyclerView;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        init();
        getDataFromIntent();


        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        //   tabLayout.addTab(tabLayout.newTab().setText("));
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view1);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        setData();
        Log.d("current_Time", current_Time);
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        setClickListenersOnTab();
    }

    private void getDataFromIntent() {
        if (getIntent() != null) {
            str = getIntent().getStringExtra("test");
            restorent_id = getIntent().getStringExtra("rest_id");
//            Log.d("restorent_id", restorent_id);
//            Log.d("offer", str);
        }

    }


    private void SetdataonApi(String str1, String str2) {

        //final String name[]=new String[11];

        MyProgressDialog.showPDialog(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://api.dineoutdeals.in/external_api/api_v3/get_restaurant_slot?" + str1 + "&" + str2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                morningList.clear();
                afterNoonList.clear();
                eveningList.clear();

                Log.d("amit ", response);
                try {
                    Object json = new JSONTokener(response).nextValue();
                    JSONObject jsonObject = (JSONObject) json;


                    //JSONArray Res_name = jsonObject.getJSONArray("output_params");
                    JSONObject jsonObject1 = jsonObject.getJSONObject("output_params");
                    JSONObject jsonObject2 = jsonObject1.getJSONObject("data");
                    JSONArray jsonArray = jsonObject2.getJSONArray("slots");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String compare = jsonArray.getString(i).replace(":", "");
                        if (Integer.valueOf(compare) < 1200 && Integer.valueOf(compare) > Integer.valueOf(current_Time.replace(":", ""))) {
                            morningList.add(jsonArray.getString(i));
                        }
                        if (Integer.valueOf(compare) > 1200 && Integer.valueOf(compare) <= 1645 && Integer.valueOf(compare) > Integer.valueOf(current_Time.replace(":", ""))) {
                            afterNoonList.add(jsonArray.getString(i));
                        } else if (Integer.valueOf(compare) > 1645 && Integer.valueOf(compare) > Integer.valueOf(current_Time.replace(":", ""))) {
                            eveningList.add(jsonArray.getString(i));
                        }
                    }
                    Log.d("jsonObject1", jsonObject1.toString());
                    Log.d("jsonObject2", jsonObject.toString());
                    Log.d("jsonArray", jsonArray.toString());
                    Log.d("morningList", morningList.toString());
                    Log.d("afterNoonList", afterNoonList.toString());
                    Log.d("eveningList", eveningList.toString());

                    MyProgressDialog.hidePDialog();

                    setDataToTimeCells();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley..,.", "ErrorListener: " + error.getMessage());
//
//                Toast.makeText(getApplicationContext(), R.string.someting_went_wrong, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Auth-id", "1018");
                headers.put("Auth-key", "abcdef-12345-abcdef-12345");
                headers.put("Hash-key", Booking_result);
                headers.put("cache-control", "no-cache");
                headers.put("postman-token", "8253d5c9-f111-18f5-ce99-35ad044b0361");
                return headers;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringRequest);

        /*=====================for  add with header*=============================*/
//        LayoutInflater myinflater = getLayoutInflater();
//        ViewGroup myHeader = (ViewGroup)myinflater.inflate(R.layout.demo, list, false);
//        list.addHeaderView(myHeader, null, false);
    }

    private void init() {

        morningTab = (LinearLayout) findViewById(R.id.morningTab);
        afternoonTab = (LinearLayout) findViewById(R.id.afternoonTab);
        eveningTab = (LinearLayout) findViewById(R.id.eveningTab);

        customMorningVGroup = (CustomTimeVGroup) findViewById(R.id.customMorningVGroup);
        customAfternoonVGroup = (CustomTimeVGroup) findViewById(R.id.customAfternoonVGroup);
        customEveningVGroup = (CustomTimeVGroup) findViewById(R.id.customEveningVGroup);

        morningImg = (ImageView) findViewById(R.id.morningImg);
        afternoonImg = (ImageView) findViewById(R.id.afternoonImg);
        eveningImg = (ImageView) findViewById(R.id.eveningImg);
    }

    private List<String> morningList = new ArrayList<>();
    private List<String> afterNoonList = new ArrayList<>();
    private List<String> eveningList = new ArrayList<>();

    private LinearLayout morningTab = null;
    private LinearLayout afternoonTab = null;
    private LinearLayout eveningTab = null;

    private CustomTimeVGroup customMorningVGroup = null;
    private CustomTimeVGroup customAfternoonVGroup = null;
    private CustomTimeVGroup customEveningVGroup = null;

    private ImageView morningImg;
    private ImageView afternoonImg;
    private ImageView eveningImg;

    private void setClickListenersOnTab() {

        // For very first time..
        customMorningVGroup.setVisibility(View.VISIBLE);
        customAfternoonVGroup.setVisibility(View.GONE);
        customEveningVGroup.setVisibility(View.GONE);
        morningImg.setImageResource(R.drawable.up_image);
        afternoonImg.setImageResource(R.drawable.downimage);
        afternoonImg.setImageResource(R.drawable.downimage);
        morningTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                customAfternoonVGroup.setVisibility(View.GONE);
                customEveningVGroup.setVisibility(View.GONE);
                customMorningVGroup.setVisibility(View.VISIBLE);
                morningImg.setImageResource(R.drawable.up_image);
                afternoonImg.setImageResource(R.drawable.downimage);
                eveningImg.setImageResource(R.drawable.downimage);
            }
        });

        afternoonTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                customEveningVGroup.setVisibility(View.GONE);
                customMorningVGroup.setVisibility(View.GONE);
                customAfternoonVGroup.setVisibility(View.VISIBLE);
                afternoonImg.setImageResource(R.drawable.up_image);
                morningImg.setImageResource(R.drawable.downimage);
                eveningImg.setImageResource(R.drawable.downimage);
            }
        });

        eveningTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                customAfternoonVGroup.setVisibility(View.GONE);
                customMorningVGroup.setVisibility(View.GONE);
                customEveningVGroup.setVisibility(View.VISIBLE);
                eveningImg.setImageResource(R.drawable.up_image);
                afternoonImg.setImageResource(R.drawable.downimage);
                morningImg.setImageResource(R.drawable.downimage);
            }
        });
    }

    private void setDataToTimeCells() {

//         If data is not available for any tab..
//        if (morningList.size() == 0) {
//
//            morningTab.setVisibility(View.GONE);
//
//            customAfternoonVGroup.setVisibility(View.VISIBLE);
//
//        } else if (afterNoonList.size() == 0) {
//
//            afternoonTab.setVisibility(View.GONE);
//
//            morningTab.setVisibility(View.GONE);
//
//            customEveningVGroup.setVisibility(View.VISIBLE);
//
//        } else if (eveningList.size() == 0) {
//
//            eveningTab.setVisibility(View.GONE);
//        }


        // For Morning time cells..
        addTimeCells(morningList, customMorningVGroup);

        // For Afternoon time cells..
        addTimeCells(afterNoonList, customAfternoonVGroup);

        // For Evening time cells..
        addTimeCells(eveningList, customEveningVGroup);
    }

    private void addTimeCells(List<String> dataList, CustomTimeVGroup customTimeVGroup) {

        for (int i = 0; i < dataList.size(); i++) {

            LinearLayout timeCellLLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.layout_single_time_cell, null);

            TextView timeTView = (TextView) timeCellLLayout.findViewById(R.id.timeTView);
            final String timeStr = dataList.get(i);
            timeTView.setText(timeStr);
            timeTView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(BookingActivity.this, timeStr, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(BookingActivity.this, Offer_BookingActivity.class);

                    intent.putExtra("offer", str);
                    intent.putExtra("rest_id", restorent_id);
                    intent.putExtra("time", timeStr);
                    intent.putExtra("Full_date", Full_date);
                    Log.d("restorent_id", restorent_id);
                    Log.d("timeStr", timeStr);
                    Log.d("restorent_id", restorent_id);

                    startActivity(intent);
                }
            });

            customTimeVGroup.addView(timeCellLLayout);
        }
    }


    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        Log.d("size", morningList.toString());
        //Adding child data
        listDataHeader.add("morningList");
        listDataHeader.add("afterNoonList");
        listDataHeader.add("eveningList");

        listDataChild.put(listDataHeader.get(0), morningList);
        listDataChild.put(listDataHeader.get(1), afterNoonList);
        listDataChild.put(listDataHeader.get(2), eveningList);
        Log.d("size", morningList.toString());


    }

    private void setData() {

        SimpleDateFormat sdf = new SimpleDateFormat("EE");
        SimpleDateFormat sdf1 = new SimpleDateFormat(("d"));
        SimpleDateFormat sdf2 = new SimpleDateFormat(("yyyy-mm-dd"));
        SimpleDateFormat cur_Time = new SimpleDateFormat("HH:mm");
        timestamp = new Timestamp(System.currentTimeMillis());
        time_id = timestamp.getTime();
        String time_id2 = time_id.toString();
        final String rest_id = "restaurant_id" + "=" + restorent_id;
        time_id1 = "timestamp" + "=" + time_id;

        for (int i = 0; i < 30; i++) {

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, i);
            String day = sdf.format(calendar.getTime());
            String date = sdf1.format(calendar.getTime());
            current_Time = cur_Time.format(calendar.getTime());

            Booking booking = new Booking(day, date);
            booking.setTimeMiles(calendar.getTimeInMillis());
            calendar.setTimeInMillis(calendar.getTimeInMillis());

            int mYear = calendar.get(Calendar.YEAR);
            int mMonth = calendar.get(Calendar.MONTH) + 1;
            int mDay = calendar.get(Calendar.DAY_OF_MONTH);
            // Log.d("current_Time", current_Time);
//            Log.d("mYear", String.valueOf(mYear));
//            Log.d("mMonth", String.valueOf(mMonth));
//            Log.d("mDay", String.valueOf(mDay));
            booking.setFulldate(String.valueOf(mYear) + "-" + String.valueOf(mMonth) + "-" + String.valueOf(mDay));
           /* int month = cl.get(Calendar.MONTH) + 1;

            String date1 = "" + cl.get(Calendar.DAY_OF_MONTH) + ":" + month + ":" + cl.get(Calendar.YEAR);
            String time = "" + cl.get(Calendar.HOUR_OF_DAY) + ":" + cl.get(Calendar.MINUTE) + ":" + cl.get(Calendar.SECOND);*/

            bookingList.add(booking);
            Log.d("day", day);


            if (i == 0) {
//                JSONObject obj = new JSONObject();
//                try {
//
//                    obj.put("restaurant_id", restorent_id);
//                    obj.put("timestamp", time_id2);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                String url = "{" + "\"" + "restaurant_id" + "\"" + ":" + "\"" + restorent_id + "\"" + "," + "\"" + "timestamp" + "\"" + ":" + "\"" + time_id2 + "\"" + "}";

                booking.setSelected(true);
                Full_date = bookingList.get(0).getFulldate();
                Booking_result = sStringToHMACMD5(url, "e249c439ed7697df2a4b045d97d4b9b7e1854c3ff8dd668c779013653913572e");
                Log.d("Booking_result", Booking_result);
                Log.d("obj.toString()", url);
                Log.d("dataapi", rest_id);
                Log.d("dataapi", time_id1);

                SetdataonApi(rest_id, time_id1);

                Toast.makeText(getApplicationContext(), "On Click\nPosition : " + (day) + "\nTime : " + date, Toast.LENGTH_SHORT).show();


            }
        }


        mAdapter = new BookingAdapter(bookingList, this) {

            @Override
            protected void onItemClick(int position) {

                //  SetdataonApi();

                String timestamp = "" + bookingList.get(position).getTimeMiles();

                Full_date = bookingList.get(position).getFulldate();

                Log.d("miles", timestamp);
                Log.d("Full_date", Full_date);
                String string = "timestamp" + "=" + timestamp;
//                    JSONObject obj = new JSONObject();
//                    try {
//
//                    obj.put("restaurant_id", restorent_id);
//                    obj.put("timestamp", timestamp);
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                String url1 = "{" + "\"" + "restaurant_id" + "\"" + ":" + "\"" + restorent_id + "\"" + "," + "\"" + "timestamp" + "\"" + ":" + "\"" + timestamp + "\"" + "}";


                Booking_result = sStringToHMACMD5(url1.toString(), "e249c439ed7697df2a4b045d97d4b9b7e1854c3ff8dd668c779013653913572e");
                SetdataonApi(rest_id, string);

                Log.d("click", url1.toString());
                Log.d("string", string);
                for (int i = 0; i < bookingList.size(); i++) {

                    if (i == position) {

                        bookingList.get(i).setSelected(true);


                        Log.d("restorent_id", rest_id);
                        Log.d(" string", string);
                        Log.d("newkey", Booking_result);


                    } else {
                        bookingList.get(i).setSelected(false);
                    }
                }

                mAdapter.notifyDataSetChanged();
            }
        };

        recyclerView.setAdapter(mAdapter);

        //mAdapter.notifyDataSetChanged();
        Log.d("heloo", bookingList.toString());
    }

    public void backButton1(View view) {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

}
