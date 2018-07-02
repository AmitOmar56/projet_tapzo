package com.axovel.mytapzoapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.axovel.mytapzoapp.R;
import com.axovel.mytapzoapp.app.AppController;
import com.axovel.mytapzoapp.customAdapter.ProductHorizontalAdapter;
import com.axovel.mytapzoapp.customAdapter.VendarAppHorizontalAdapter;
import com.axovel.mytapzoapp.customAdapter.ViewAllRecyclerAdapter;
import com.axovel.mytapzoapp.models.Offers_product_Data;
import com.axovel.mytapzoapp.utils.Constants;
import com.axovel.mytapzoapp.utils.MyProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewAllScreenActivity extends AppCompatActivity implements Constants {

    ViewAllRecyclerAdapter viewAllRecyclerAdapter;
    private List<Offers_product_Data> data=new ArrayList<>();
    RecyclerView vertical_recycler_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_screen);

        setDataAdapterList();

        vertical_recycler_view= (RecyclerView) findViewById(R.id.my_recycler_view);
//
//        data = fill_with_data();
//
//
//        viewAllRecyclerAdapter=new ViewAllRecyclerAdapter(data, getApplication());
//
//        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(ViewAllScreenActivity.this, LinearLayoutManager.VERTICAL, false);
//        vertical_recycler_view.setLayoutManager(horizontalLayoutManager);
//        vertical_recycler_view.setAdapter(viewAllRecyclerAdapter);

    }

    private void setDataAdapterList() {

        MyProgressDialog.showPDialog(ViewAllScreenActivity.this);

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
                            if(imagedata.getString("resolutionType").equals("low")){
                                imageUrl=imagedata.has("url") ? imagedata.getString("url") : "";
                                Log.d("pppppp",imageUrl);
                                Log.d("aaaa",imagedata.getString("resolutionType"));

                            }



                        }





                        Offers_product_Data offers_product_data=new Offers_product_Data(titleProduct,description,imageUrl,urlSend);
                        data.add(offers_product_data);

                    }

                    ViewAllRecyclerAdapter viewAllRecyclerAdapter=new ViewAllRecyclerAdapter(data, getApplication());

                    LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(ViewAllScreenActivity.this, LinearLayoutManager.VERTICAL, false);
                    vertical_recycler_view.setLayoutManager(horizontalLayoutManager);
                    vertical_recycler_view.setAdapter(viewAllRecyclerAdapter);

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

    private List<Offers_product_Data> fill_with_data() {
        List<Offers_product_Data> data = new ArrayList<>();

        data.add(new Offers_product_Data( "Flipkart","selfi Stick","",""));





        return data;


    }

    public void backButton(View view){
//        Intent intent=new Intent(ViewAllScreenActivity.this,ShoppingScreenActivity.class);
//        startActivity(intent);
        finish();
    }
}
