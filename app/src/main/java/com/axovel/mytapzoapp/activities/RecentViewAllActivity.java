package com.axovel.mytapzoapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.axovel.mytapzoapp.R;
import com.axovel.mytapzoapp.app.AppController;
import com.axovel.mytapzoapp.customAdapter.ProductRecyclerViewAdapter;
import com.axovel.mytapzoapp.models.ProductSearchData;
import com.axovel.mytapzoapp.utils.Constants;
import com.axovel.mytapzoapp.utils.MyProgressDialog;
import com.axovel.mytapzoapp.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RecentViewAllActivity extends AppCompatActivity implements Constants{

    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;

    private List<ProductSearchData> data = new ArrayList<>();

    private TextView headerText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_view_all);

        headerText= (TextView) findViewById(R.id.headerText);
        String productNmae=getIntent().getStringExtra("PRODUCT_NAME");
        headerText.setText("Recently Searched Item");
        setDataAdapterList("iphone");

        recyclerView = (RecyclerView) findViewById(R.id.productSearchRecyclerView);
        recyclerView.setPadding(Utils.dpToPx(getApplicationContext(), 1), 0, 0, 0);
        recyclerView.setHasFixedSize(true);
        gridLayoutManager = new GridLayoutManager(RecentViewAllActivity.this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    private void setDataAdapterList(String productNmae) {

        MyProgressDialog.showPDialog(RecentViewAllActivity.this);

        data.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, PRODUCT_SEARCH_URL + productNmae, new Response.Listener<String>() {
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
                                    ProductSearchData productSearchData = new ProductSearchData(productSeller, productTitle, productPrice,afflitateLink, productImage);
                                    data.add(productSearchData);



                                }
                            } else if (jsonObject.get(key) instanceof JSONObject) {
//                                    parseJson(data.getJSONObject(key));
                            } else {
//                                    System.out.println("" + key + " : " + data.optString(key));
                            }

                        }

                        ProductRecyclerViewAdapter productRecyclerViewAdapter = new ProductRecyclerViewAdapter((ArrayList<ProductSearchData>) data, getApplication());

                        recyclerView.setAdapter(productRecyclerViewAdapter);

                        MyProgressDialog.hidePDialog();



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

    public void backButton(View view){
//        Intent intent=new Intent(RecentViewAllActivity.this,ShoppingScreenActivity.class);
//        startActivity(intent);
        finish();
    }


}
