package com.axovel.mytapzoapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
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

public class ProductSearchItemShowActivity extends AppCompatActivity implements Constants {


    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;

    private List<ProductSearchData> data = new ArrayList<>();

    private TextView headerText;
    private EditText searchBarEText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_search_item_show);
        String productNmae=getIntent().getStringExtra("PRODUCT_NAME");
        setDataAdapterList(productNmae);
        searchBarEText= (EditText) findViewById(R.id.searchBarEText);
//        searchBarEText.setSelection(productNmae.length());
        searchBarEText.setText(productNmae);
        searchBarEText.setSelection(productNmae.length());

        recyclerView = (RecyclerView) findViewById(R.id.productSearchRecyclerView);
        recyclerView.setPadding(Utils.dpToPx(getApplicationContext(), 1), 0, 0, 0);
        recyclerView.setHasFixedSize(true);
        gridLayoutManager = new GridLayoutManager(ProductSearchItemShowActivity.this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        searchBarEText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {




                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {

                    String productName=searchBarEText.getText().toString().trim();
                    setDataAdapterList(productName);

                }
                return false;
            }
        });
    }

        private void setDataAdapterList(String searchString) {

            MyProgressDialog.showPDialog(ProductSearchItemShowActivity.this);

            data.clear();

    StringRequest stringRequest = new StringRequest(Request.Method.GET, PRODUCT_SEARCH_URL + searchString, new Response.Listener<String>() {
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
    finish();
}

//    public void searchProductFun(View view){
//
//        String productName=searchBarEText.getText().toString().trim();
//
//        setDataAdapterList(productName);
//
//
//
//    }



}
