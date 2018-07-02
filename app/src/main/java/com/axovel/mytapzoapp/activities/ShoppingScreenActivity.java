package com.axovel.mytapzoapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.axovel.mytapzoapp.R;
import com.axovel.mytapzoapp.app.AppController;
import com.axovel.mytapzoapp.customAdapter.ProductHorizontalAdapter;
import com.axovel.mytapzoapp.customAdapter.ProductRecyclerViewAdapter;
import com.axovel.mytapzoapp.customAdapter.RecentlyHorizontalAdapter;
import com.axovel.mytapzoapp.customAdapter.VendarAppHorizontalAdapter;
import com.axovel.mytapzoapp.models.CabDataList;
import com.axovel.mytapzoapp.models.Offers_product_Data;
import com.axovel.mytapzoapp.models.ProductSearchData;
import com.axovel.mytapzoapp.models.RecentalyProductData;
import com.axovel.mytapzoapp.models.VendarDataList;
import com.axovel.mytapzoapp.utils.Constants;
import com.axovel.mytapzoapp.utils.MyProgressDialog;
import com.axovel.mytapzoapp.utils.PreferenceStore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ShoppingScreenActivity extends AppCompatActivity implements Constants {

    RecyclerView product_recycler_view;
    RecyclerView recent_recycler_view;
    private LinearLayout bestLinear,recentLinear;
    VendarAppHorizontalAdapter vendarAppHorizontalAdapter;
    private List<Offers_product_Data> data=new ArrayList<>();
    private List<RecentalyProductData>dataRecentaly=new ArrayList<>();
    private List<VendarDataList> dataapp;
    private EditText searchItemNmae;
    public static SharedPreferences pref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_screen);

        searchItemNmae= (EditText) findViewById(R.id.searchBarEText);
        bestLinear= (LinearLayout) findViewById(R.id.bestLinearLayout);
        recentLinear= (LinearLayout) findViewById(R.id.recentLinearLayout);
        bestLinear.setVisibility(View.GONE);
        recentLinear.setVisibility(View.GONE);

        if(PreferenceStore.getPreference(getApplicationContext()).getString(PRODUCT_NAME).equals(null)){

        }else {



            String language = PreferenceStore.getPreference(getApplicationContext()).getString(PRODUCT_NAME);
            Log.d("WElcome......", language);

            setdataRecentalyData(language);
        }

        dealsOfTheDay();
        searchItemNmae.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {




                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {

                    String productName=searchItemNmae.getText().toString().trim();
                    setdataRecentalyData(productName);
                    PreferenceStore.getPreference(ShoppingScreenActivity.this).putString(PRODUCT_NAME,productName);
                    Intent intent = new Intent(ShoppingScreenActivity.this, ProductSearchItemShowActivity.class);
                    intent.putExtra("PRODUCT_NAME",productName);
                    startActivity(intent);
                }
                return false;
            }
        });





        //app setting Recycler setting
       RecyclerView horizontal_recycler_viewapp= (RecyclerView) findViewById(R.id.my_recycler_view_offers_app);

        dataapp = fill_with_dataapp();


        vendarAppHorizontalAdapter=new VendarAppHorizontalAdapter(dataapp, getApplicationContext());

        LinearLayoutManager horizontalLayoutManagerapp = new LinearLayoutManager(ShoppingScreenActivity.this, LinearLayoutManager.HORIZONTAL, false);
        horizontal_recycler_viewapp.setLayoutManager(horizontalLayoutManagerapp);
        horizontal_recycler_viewapp.setAdapter(vendarAppHorizontalAdapter);


        //daly product_setting_recyclerview


        product_recycler_view= (RecyclerView) findViewById(R.id.my_recycler_view_offers);

//        data = fill_with_data();





        //recent product recycler setting
        recent_recycler_view= (RecyclerView) findViewById(R.id.my_recycler_view_recentaly);




    }

    private void setdataRecentalyData(String productName) {


        MyProgressDialog.showPDialog(ShoppingScreenActivity.this);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, PRODUCT_SEARCH_URL + productName, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {



                try {
                    JSONObject jsonObject = (JSONObject) new JSONTokener(response).nextValue();


                    if (jsonObject != null) {
                        Iterator<String> it = jsonObject.keys();

                        while (it.hasNext()) {

                            String key = it.next();

                            Log.d("priyesh", key);

                            if (jsonObject.get(key) instanceof JSONArray) {
                                JSONArray arry = jsonObject.getJSONArray(key);
                                int size = arry.length();

                                for (int i = 0; i < size; i++) {
                                    Log.d("welcome", response);


                                    JSONObject dataArr = arry.getJSONObject(i);

                                    String productImage = dataArr.has("imgLink") ? dataArr.getString("imgLink") : "";
                                    int productPrice = dataArr.has("selling_price") ? dataArr.getInt("selling_price") :0;
                                    String productTitle = dataArr.has("title") ? dataArr.getString("title") : "";
                                    String afflitateLink=dataArr.has("Affiliate-Link")?dataArr.getString("Affiliate-Link"):"";
                                    String productSeller=dataArr.has("seller")?dataArr.getString("seller"):"";
                                    RecentalyProductData recentlayProductData = new RecentalyProductData(productSeller, productTitle, productPrice,afflitateLink, productImage);
                                    dataRecentaly.add(0,recentlayProductData);



                                }
                            } else if (jsonObject.get(key) instanceof JSONObject) {
//                                    parseJson(data.getJSONObject(key));
                            } else {
//                                    System.out.println("" + key + " : " + data.optString(key));
                            }

                        }

                       RecentlyHorizontalAdapter recentlyHorizontalAdapter=new RecentlyHorizontalAdapter(dataRecentaly, getApplication());

                        LinearLayoutManager horizontalLayoutManagerRecentaly = new LinearLayoutManager(ShoppingScreenActivity.this, LinearLayoutManager.HORIZONTAL, false);
                        recent_recycler_view.setLayoutManager(horizontalLayoutManagerRecentaly);
                        recentLinear.setVisibility(View.VISIBLE);
                        recent_recycler_view.setAdapter(recentlyHorizontalAdapter);




                    }
                } catch (Throwable e) {
//                    System.out.println("" + key + " : " + data.optString(key));
                    e.printStackTrace();

                }
////
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyProgressDialog.hidePDialog();

                // Log.d("error ",error.getLocalizedMessage());


//                Toast.makeText(getApplicationContext(), R.string.someting_went_wrong, Toast.LENGTH_SHORT).show();
            }
        });
//        {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<>();
////                String credentials = "cashgainapp@gmail.com:Anchal09@";
////                String auth = "Basic "+ Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
//                headers.put("Fk-Affiliate-Id", "anchal09n");
//                headers.put("Fk-Affiliate-Token", "ca0669a8c33348758120a47eca69cedc");
////                headers.put("Authorization", auth);
//                return headers;
//            }
//        };

        AppController.getInstance().addToRequestQueue(stringRequest);




    }

//    @Override
//    protected void onStart() {
//        MyProgressDialog.showPDialog(ShoppingScreenActivity.this);
//        super.onResume();
//        dealsOfTheDay();
//        MyProgressDialog.hidePDialog();
//
//
//    }

    private void dealsOfTheDay() {

        MyProgressDialog.showPDialog(ShoppingScreenActivity.this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, DEAL_OF_THE_DAY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {





                Log.d("welcome",response);

                try {
                    JSONObject jsonObject = (JSONObject) new JSONTokener(response).nextValue();

//                    JSONObject jsonObject = (JSONObject) json;

                    JSONArray jsonArray = jsonObject.getJSONArray("dotdList");

                    for(int i=0;i<jsonArray.length();i++) {


                        JSONObject dataObj = jsonArray.getJSONObject(i);

                        String titleProduct = dataObj.has("title") ? dataObj.getString("title") : "";
                        Log.d("priyesh",titleProduct);
                        String description = dataObj.has("description") ? dataObj.getString("description") : "";
                        String urlSend = dataObj.has("url") ? dataObj.getString("url") : "";

                        Log.d("priyesh",description);

                        JSONArray image=dataObj.getJSONArray("imageUrls");
                        String imageUrl = null;

                        for (int j=0;j<image.length();j++){
                            JSONObject imagedata=image.getJSONObject(j);
                            if(imagedata.getString("resolutionType").equals("mid")){
                                imageUrl=imagedata.has("url") ? imagedata.getString("url") : "";
                                Log.d("pppppp",imageUrl);
                                Log.d("aaaa",imagedata.getString("resolutionType"));

                            }



                        }





                        Offers_product_Data offers_product_data=new Offers_product_Data(titleProduct,description,imageUrl,urlSend);
                        data.add(offers_product_data);

                    }

                    ProductHorizontalAdapter productHorizontalAdapter=new ProductHorizontalAdapter(data, getApplication());

                    LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(ShoppingScreenActivity.this, LinearLayoutManager.HORIZONTAL, false);
                    product_recycler_view.setLayoutManager(horizontalLayoutManager);
                    bestLinear.setVisibility(View.VISIBLE);
                    product_recycler_view.setAdapter(productHorizontalAdapter);

                    MyProgressDialog.hidePDialog();
                }

                catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(getApplicationContext(), R.string.unable_to_fetch_data, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyProgressDialog.hidePDialog();

                // Log.d("error ",error.getLocalizedMessage());


//                Toast.makeText(getApplicationContext(), R.string.someting_went_wrong, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
//                String credentials = "cashgainapp@gmail.com:Anchal09@";
//                String auth = "Basic "+ Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Fk-Affiliate-Id", "anchal09n");
                headers.put("Fk-Affiliate-Token", "ca0669a8c33348758120a47eca69cedc");
//                headers.put("Authorization", auth);
                return headers;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);



    }

    private List<VendarDataList> fill_with_dataapp() {

        List<VendarDataList> dataapp = new ArrayList<>();

        dataapp.add(new VendarDataList(R.drawable.flipkart_image,"Flipkart"));
        dataapp.add(new VendarDataList(R.drawable.amazon,"Amazon Deal"));
        dataapp.add(new VendarDataList(R.drawable.paytm,"Paytm"));
        dataapp.add(new VendarDataList(R.drawable.myntra,"Myntra"));
        dataapp.add(new VendarDataList(R.drawable.jabong,"Jabong"));
        return dataapp;
    }
      public void viewAllFun(View view){
       Intent intent=new Intent(ShoppingScreenActivity.this,ViewAllScreenActivity.class);
       startActivity(intent);
   }
   public void searchProductFun(View view){


       Intent intent=new Intent(ShoppingScreenActivity.this,ProductSearchItemShowActivity.class);
       intent.putExtra("PRODUCT_NAME",searchItemNmae.getText().toString().trim());
       startActivity(intent);
   }

   public void backButton(View view){
//
//       Intent intent=new Intent(ShoppingScreenActivity.this,HomeScreenActivity.class);
//       startActivity(intent);
       finish();
   }

   public void recentViewAll(View view){

       Intent intent=new Intent(ShoppingScreenActivity.this,RecentViewAllActivity.class);
       startActivity(intent);
   }




}
