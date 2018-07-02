package com.axovel.mytapzoapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.axovel.mytapzoapp.R;
import com.axovel.mytapzoapp.app.AppController;
import com.axovel.mytapzoapp.customAdapter.CustomListAdapter;
import com.axovel.mytapzoapp.utils.EndlessScrollListener;
import com.axovel.mytapzoapp.utils.MyProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Restorent_SearchActivity extends AppCompatActivity {

    private EditText searchBarText;
    private ListView searchList;
    private ProgressDialog dialog;
    private List<String> name = new ArrayList<String>();
    private List<String> cuisine = new ArrayList<String>();
    private List<String> image = new ArrayList<String>();
    private List<String> offers = new ArrayList<String>();
    private List<String> costFor2 = new ArrayList<String>();
    private List<String> rating = new ArrayList<String>();
    private List<String> restorant_id = new ArrayList<String>();
    List<String> list_id = new ArrayList<String>();
    private List<String> url = new ArrayList<>();
    private String result;
    private String city_name;
    private String lat;
    private String lng;
    private String data;
    private CharSequence key = "";
    private int count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity_layout);
        getId();
        getdatafromIntent();
        MyProgressDialog.showPDialog(Restorent_SearchActivity.this);
        apiCall(key,0);
        searchBarText.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if (count == 1) {

                }
                name.clear();
                cuisine.clear();
                costFor2.clear();
                offers.clear();
                url.clear();
                image.clear();
                rating.clear();
                list_id.clear();
                restorant_id.clear();
                key = s;
                if (key.equals("")) {

                    Toast.makeText(getApplicationContext(), "Empty", Toast.LENGTH_SHORT).show();

                } else {
                    apiCall(key, 0);
                }
            }


            @Override
            public void afterTextChanged(Editable s) {


            }
        });
        searchList.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                Log.d("page", "" + page);
                //String count=""+page;
                page = page - 1;
                page = page * 10;
                Log.d("page", "" + page);
//                MyProgressDialog.showPDialog(Restorent_SearchActivity.this);
                apiCall(key, page);
//                MyProgressDialog.showPDialog(FoodScreenActivity.this);

                // or loadNextDataFromApi(totalItemsCount);
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });
    }

    // Append the next page of data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void loadNextDataFromApi(int offset) {
        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyDataSetChanged()`
    }

    private void getdatafromIntent() {
        if (getIntent() != null) {
            city_name = getIntent().getStringExtra("city");
            lat = getIntent().getStringExtra("lat");
            lng = getIntent().getStringExtra("lng");
            Log.d("city_name", city_name);
            Log.d("lat", lat);
            Log.d("lng", lng);

        }

    }

    private void apiCall(final CharSequence s, final Integer limit) {
        count = 1;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://cashgainapp.com/app2/api/?search", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("amit ", response);
                try {
                    Object json = new JSONTokener(response).nextValue();
                    JSONObject jsonObject = (JSONObject) json;


//                    if (jsonObject.getString("status").equals("exception")) {
//                        Toast.makeText(getApplicationContext(), "SearchBox is Empty", Toast.LENGTH_SHORT).show();
//                    }
//
//                    else if(true){
                    JSONObject dineout_output = jsonObject.getJSONObject("dineout");
                    JSONObject nearby_output = jsonObject.getJSONObject("nearby");

//                    JSONArray Res_name = dineout_output.getJSONArray("data");
                    //JSONArray NearBy = nearby_output.getJSONArray("data");


                    //  Log.d("jsonObject", Res_name.toString());
                    //  Log.d("Nearby", NearBy.toString());
                    Log.d("output", dineout_output.toString());
                    Log.d("output", nearby_output.toString());

                    if (dineout_output.getString("statusDine").equals("success")) {
                        if (!dineout_output.isNull("data")) {
                            //Value is not null
                            JSONArray Res_name = dineout_output.getJSONArray("data");

                            for (int i = 0; i < Res_name.length(); i++) {

                                JSONObject profile_Name = Res_name.getJSONObject(i);
                                name.add(profile_Name.getString("profile_name"));
                                restorant_id.add(profile_Name.getString("r_id"));
                                costFor2.add(profile_Name.getString("costFor2") + " " + "cost for two");
                                rating.add(profile_Name.getString("avg_rating"));
                                JSONArray imageArray = profile_Name.getJSONArray("img");
                                image.add(imageArray.getString(0));
                                JSONArray offerArray = profile_Name.getJSONArray("offers");
//                        offers.add(offerArray.getString(0));
                                offers.add(offerArray.length() > 0 ? offerArray.getString(0) : "");
                                JSONArray cuisineArray = profile_Name.getJSONArray("cuisine");
                                list_id.add("Dinout");
                                url.add("");
                            }
                            Log.d("qwe", cuisine.toString());
                            for (int k = 0; k < Res_name.length(); k++) {
                                JSONObject Cuisine_Name = Res_name.getJSONObject(k);
                                cuisine.add(Cuisine_Name.getString("cuisine").replaceAll("\\[|\\]|'|\"|", ""));
//                            Log.d("Result_3",Arrays.toString(cuisine));
                            }
                        }
                    }
                    if (nearby_output.getString("statusNearBy").equals("success")) {
                        if (!nearby_output.isNull("data")) {

                            DecimalFormat df = new DecimalFormat("#.00");
                            JSONArray NearBy = nearby_output.getJSONArray("data");

                            for (int i = 0; i < NearBy.length(); i++) {

                                JSONObject profile_Name = NearBy.getJSONObject(i);
                                name.add(profile_Name.getString("brand"));
                                restorant_id.add(profile_Name.has("id") ? profile_Name.getString("id") : "");
                                Double value = Double.parseDouble(profile_Name.getString("distance_in_km"));
                                costFor2.add(df.format(value) + " " + " km");
                                rating.add(profile_Name.has("avg_rating") ? profile_Name.getString("avg_rating") : "");
                                image.add(profile_Name.getString("bigimage"));
                                offers.add(profile_Name.getString("title"));
                                cuisine.add(profile_Name.getString("name"));
                                list_id.add("NearBy");
                                url.add(profile_Name.getString("producturl"));
                            }
                        }
                    }
                    if (adapter == null) {
                        adapter = new CustomListAdapter(Restorent_SearchActivity.this, name, image, cuisine, offers, costFor2, rating, list_id);
                        searchList.setAdapter(adapter);
                    }

                    adapter.notifyDataSetChanged();
                    click_on_list();

                    MyProgressDialog.hidePDialog();
//                }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley..,.", "ErrorListener: " + error.getMessage());
//                Toast.makeText(getApplicationContext(), "Network is Slow Please wait......", Toast.LENGTH_SHORT).show();
                apiCall(key, 0);
//
//                Toast.makeText(getApplicationContext(), R.string.someting_went_wrong, Toast.LENGTH_SHORT).show();
            }
        }) {
//            @Override
//            public Map<String,String> getparams() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<>();
//                headers.put("lat", "28.585262");
//                headers.put("lng", "77.070441");
//                headers.put("radius","50");
//                headers.put("city","Delhi");
//                headers.put("index","0");
//                headers.put("limit","10");
//                headers.put("hash-key","e1dcc0e22cd346bfc124116166317713");
//                return headers;
//            }
//        };

            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("lat", lat);
                params.put("lng", lng);
                params.put("radius", "20");
                params.put("city", city_name);
                params.put("index", limit.toString());
                params.put("limit", "10");
                params.put("term", s.toString());
                return params;
            }
        };


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringRequest);
//        CustomListAdapter adapter = new CustomListAdapter(this, name,image,cuisine,offers,costFor2,rating,list_id);
//        searchList = (ListView) findViewById(R.id.searchList);
//        searchList.setAdapter(adapter);
        // Log.d("hmac",result);
        /*=====================for  add with header*=============================*/
//        LayoutInflater myinflater = getLayoutInflater();
//        ViewGroup myHeader = (ViewGroup)myinflater.inflate(R.layout.activity_food_screen,list, false);
//        list.addHeaderView(myHeader, null, false);
    }

    private void click_on_list() {
        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String Slecteditem = name.get(+position);
                String Selectitem_2 = list_id.get(+position);
                String Selectitem_3 = url.get(+position);
                Log.d("Selectitem_3", Selectitem_3);
                if (Selectitem_2.equals("Dinout")) {
                    Toast.makeText(getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(FoodScreenActivity.this, RestroDetailActivity.class));
                    Intent intent = new Intent(Restorent_SearchActivity.this, BookingActivity.class);

                    intent.putExtra("test", offers.get(+position));
                    intent.putExtra("rest_id", restorant_id.get(+position));
                    // intent.putExtra("offer",offers.get(+position));
                    startActivity(intent);
                } else {
                    Intent viewIntent =
                            new Intent("android.intent.action.VIEW",
                                    Uri.parse(Selectitem_3));
                    startActivity(viewIntent);
                }

            }
        });
    }

    private CustomListAdapter adapter = null;

    private void getId() {
        searchBarText = (EditText) findViewById(R.id.searchBarText);
        searchList = (ListView) findViewById(R.id.searchList);
    }


}
