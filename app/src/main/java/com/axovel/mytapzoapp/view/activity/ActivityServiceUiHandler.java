package com.axovel.mytapzoapp.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.axovel.mytapzoapp.R;
import com.axovel.mytapzoapp.customAdapter.RecyclerAdapterCouponList;
import com.axovel.mytapzoapp.customAdapter.RecyclerAdapterProductList;
import com.axovel.mytapzoapp.customAdapter.RecyclerAdapterRestaurantDeals;
import com.axovel.mytapzoapp.customAdapter.RecyclerAdapterSimilarApps;
import com.axovel.mytapzoapp.models.APIResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActivityServiceUiHandler extends Activity {

    public static boolean active = false;
    public static Activity myDialog;
    private String dataResponse = null;
    private View top;
    public static RecyclerAdapterProductList adapterProductList;
    private Activity act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.uiservice_layout);

        act = this;
        Intent intent = getIntent();
        dataResponse = (String) intent.getExtras().get("response");
        JSONObject jObject = null;
        JSONArray jArray = null;
        boolean isExcatProduct = false;
        String productName = null;
        String productImage = null;
        RecyclerView resultList = (RecyclerView) findViewById(R.id.recycler_view_result);
        try {
            try {
                jObject = new JSONObject(dataResponse);
                Log.i("Final-1", "" + jObject);
            } catch (Exception e) {
                jArray = new JSONArray(dataResponse);
            }
            if (jObject != null && jObject.has("result")) { // Shopping Results
                jArray = jObject.getJSONArray("result");
                // if (jObject.has("productDetails") ? isExcatProduct = true : isExcatProduct) ;
                /*isExcatProduct = true;
                if (isExcatProduct) {
					JSONArray productDetail = jObject.getJSONArray("productDetails");
					JSONObject productDetailObj = (JSONObject) productDetail.get(0);
					productName = productDetailObj.getString("product_name");
					JSONArray productImageArr = productDetailObj.getJSONArray("product_image_thumbnail");
					productImage = (String) productImageArr.get(0);
					((TextView) findViewById(R.id.txtResultType)).setText("Price Comparison");
				} else {
					((TextView) findViewById(R.id.txtResultType)).setText("Similar Products");
				}*/
            } else if (jObject != null && jObject.has("offers")) { // Restaurant Deals
                ((TextView) findViewById(R.id.txtPriceCompAndClose)).setText("Best Restaurant Deals");
                (findViewById(R.id.txtHeader)).setVisibility(View.GONE);
                jArray = jObject.getJSONArray("offers");
                RecyclerAdapterRestaurantDeals adapterRestaurantDeals = new RecyclerAdapterRestaurantDeals(jArray, act);
                resultList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                resultList.setAdapter(adapterRestaurantDeals);
            } else if (jObject != null && jObject.has("resultType") && jObject.getString("resultType").equals("SIMILAR COUPONS")) {
                jArray = jObject.getJSONArray("Coupons");
                ((TextView) findViewById(R.id.txtPriceCompAndClose)).setText("Coupons");
                (findViewById(R.id.txtHeader)).setVisibility(View.GONE);
                RecyclerAdapterCouponList adapterCouponsDeals = new RecyclerAdapterCouponList(jArray, act);
                resultList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                resultList.setAdapter(adapterCouponsDeals);
            } else if (jObject != null && jObject.has("resultType") && jObject.getString("resultType").equals("SIMILAR APPS")) {
                jArray = jObject.getJSONArray("Apps");
                ((TextView) findViewById(R.id.txtPriceCompAndClose)).setText("Similar Apps");
                (findViewById(R.id.txtHeader)).setVisibility(View.GONE);
                RecyclerAdapterSimilarApps adapterSimilarApps = new RecyclerAdapterSimilarApps(jArray, act);
                resultList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                resultList.setAdapter(adapterSimilarApps);
            } else if (jObject != null) {

                String[] expectedSites = {"www.flipkart.com", "www.paytm.com", "www.amazon.in", "www.snapdeal.com", "www.ebay.in", "www.shopclues.com", "www.jabong.com", "www.myntra.com"};
                JSONArray newJArray = new JSONArray();
                for (int i = 0; i < expectedSites.length; i++) {
                    if (jObject.has(expectedSites[i])) {
                        JSONArray products = jObject.getJSONArray(expectedSites[i]);
                        if (products.length() > 0) {
                            for (int j = 0; j < products.length(); j++) {
                                JSONObject product = (JSONObject) products.get(j);
                                APIResponse newProduct = new APIResponse();
                                newProduct.setTitle(product.getString("title"));
                                newProduct.setImage(product.getString("imgLink"));
                                newProduct.setPrice(product.getString("selling_price"));
                                newProduct.setUrl(product.getString("Affiliate-Link"));
                                newProduct.setSaving(product.getString("saved_amount"));
                                if (product.has("offers") && !product.isNull("offers") && product.getJSONArray("offers") != null) {
                                    JSONArray arr = product.getJSONArray("offers");
                                    // String[] offers = new String[arr.length()];
                                    List<String> list = new ArrayList<String>();
                                    for (int k = 0; k < arr.length(); k++) {
                                        // offers[k] = arr.getString(k);
                                        list.add(arr.getString(k));
                                    }
                                    newProduct.setOffers(list);
                                }
                                if (i == 0) {
                                    newProduct.setSite("FLIPKART");
                                } else if (i == 1) {
                                    newProduct.setSite("PAYTM");
                                } else if (i == 2) {
                                    newProduct.setSite("AMAZON");
                                } else if (i == 3) {
                                    newProduct.setSite("SNAPDEAL");
                                } else if (i == 4) {
                                    newProduct.setSite("EBAY");
                                } else if (i == 5) {
                                    newProduct.setSite("SHOPCLUES");
                                } else if (i == 6) {
                                    newProduct.setSite("JABONG");
                                } else if (i == 7) {
                                    newProduct.setSite("MYNTRA");
                                }
                                newJArray.put(newProduct.getJSONObject());
                                Log.i("Final", "" + newProduct);
                            }
                        }
                    }
                }

                Log.i("Final", "" + newJArray);
                ((TextView) findViewById(R.id.txtPriceCompAndClose)).setText("Price Comparison");
                adapterProductList = new RecyclerAdapterProductList(newJArray, act, isExcatProduct, null, null);
                resultList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                resultList.setAdapter(adapterProductList);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        top = (View) findViewById(R.id.dialog_top);
        // TextView btnBestPrice = (TextView) findViewById(R.id.btnBestPrice);
        myDialog = ActivityServiceUiHandler.this;
        top.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        /*btnBestPrice.setOnClickListener(new OnClickListener() {
            @Override
			public void onClick(View v) {
				finish();
			}
		});*/
        final TextView txtview = (TextView) findViewById(R.id.txtPriceCompAndClose);
        txtview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= txtview.getRight() - txtview.getTotalPaddingRight()) {
                        // your action for drawable click event
                        finish();
                        return true;
                    }
                }
                return true;
            }
        });

        //        if (CustomAccessibilityService.IS_CURRENT_FLIPKART) {
        //            //            if (PreferenceStore.getPreference(getApplicationContext()).getBoolean("isFlipkartMyCur")) {
        //            openUniqueProductFlipkart();
        //        }
    }

    //    String newFlipkartUrl = null;

    //    private void openUniqueProductFlipkart() {
    //
    //        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://163.172.63.19/getProductDetail5.php", new Response.Listener<String>() {
    //            @Override
    //            public void onResponse(String response) {
    //
    //                Log.d("Response data >>>", response);
    //
    //                try {
    //                    Object json = new JSONTokener(response).nextValue();
    //                    if (json instanceof JSONObject) {
    //
    //                        JSONObject jsonObject = (JSONObject) json;
    //
    //                        if (jsonObject.getString("status").equals("success")) {
    //
    //                            JSONArray jsonArray = jsonObject.getJSONArray("response");
    //                            if (jsonArray.length() > 0) {
    //                                newFlipkartUrl = jsonArray.getJSONObject(0).getString("aff_link");
    //                            }
    //                        }
    //                    } else {
    //                    }
    //                } catch (JSONException e) {
    //                    e.printStackTrace();
    //                }
    //            }
    //        }, new Response.ErrorListener() {
    //            @Override
    //            public void onErrorResponse(VolleyError error) {
    //                Log.d("Volley..,.", "ErrorListener: " + error.getMessage());
    //            }
    //        }) {
    //            @Override
    //            protected Map<String, String> getParams() throws AuthFailureError {
    //
    //                Map<String, String> map = new Hashtable<>();
    //                map.put("product_name", "Apple iPhone 6 (Space Grey, 16 GB)" /*PreferenceStore.getPreference(getApplicationContext()).getString("flipAppName")*/);
    //
    //                Log.d("MAP>.,.", map.toString());
    //
    //                return map;
    //            }
    //        };
    //
    //        // Adding request to request queue
    //        addToRequestQueue(stringRequest);
    //    }
    //
    //    public <T> void addToRequestQueue(Request<T> req) {
    //        req.setShouldCache(false);
    //        req.setTag("tag");
    //        req.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES * 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    //        Volley.newRequestQueue(getApplicationContext()).add(req);
    //    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        active = true;
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        active = false;
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        active = false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //        if (PreferenceStore.getPreference(getApplicationContext()).getBoolean("isFlipkartMyCur")) {
        //        if (CustomAccessibilityService.IS_CURRENT_FLIPKART) {
        //            if (newFlipkartUrl != null) {
        //                Intent intent = new Intent(Intent.ACTION_VIEW);
        //                intent.setData(Uri.parse(newFlipkartUrl));
        //                startActivity(intent);
        //            }
        //        }
    }
}
