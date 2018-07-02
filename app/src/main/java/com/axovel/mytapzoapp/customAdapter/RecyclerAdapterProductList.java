package com.axovel.mytapzoapp.customAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.axovel.mytapzoapp.R;
import com.axovel.mytapzoapp.utils.General;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.List;

/**
 * Created by Umesh Chauhan on 11-03-2016.
 * Axovel Private Limited
 */
public class RecyclerAdapterProductList extends RecyclerView.Adapter<RecyclerAdapterProductList.CustomViewHolder> {

    public JSONArray resultArray;
    Bitmap[] bitmaps;
    Context context;
    boolean isExcatProduct;
    String productName;
    String productImage;
    LayoutInflater inflater = null;

    public RecyclerAdapterProductList(JSONArray resultArray, Context context, boolean isExcatProduct, String productName, String productImage) {
        this.resultArray = resultArray;
        bitmaps = new Bitmap[resultArray.length()];
        this.context = context;
        this.isExcatProduct = isExcatProduct;
        this.productName = productName;
        this.productImage = productImage;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RecyclerAdapterProductList.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recyclerview_item_product, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(lp);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerAdapterProductList.CustomViewHolder holder, final int position) {
        JSONObject obj = null;
        String productTitle = null;
        String product_lowest_price = "0";
        String product_image = null;
        try {
            obj = resultArray.getJSONObject(position);
            if (obj != null) {
                if (!isExcatProduct) {
                    // Similar Product
                    productTitle = (String) obj.get("title");
                    productTitle = productTitle.trim();
                    product_lowest_price = obj.getString("price");
                    product_image = (String) obj.get("image");
                    Log.i("Adapter", "Similar " + (obj.getString("site")).toUpperCase());
                    // holder.txtLableProductPrice.setText( (String) obj.get("price"));
                    holder.txtResultFrom.setText((obj.getString("site")).toUpperCase() + "");
                    holder.txtResultFrom.setVisibility(View.VISIBLE);
                    int savings = obj.getInt("saving");
                    Log.i("Saving", "" + savings);
                    if (savings < 0) {
                        savings = savings * (-1);
                        holder.txtProductSaving.setText("" + savings);
                        holder.txtLableProductSaving.setText("Extra: ₹ ");
                    } else {
                        holder.txtProductSaving.setText("" + savings);
                        holder.txtLableProductSaving.setText("Save: ₹ ");
                    }
                    // Add Offers
                    // Log.i("String", obj.getString("offers"));
                    if (!obj.isNull("offers") && !holder.isAlreadyDone) {
                        List<String> arr = (List<String>) obj.get("offers");
                        // Getting External View

                        View vwOffer;

                        int numberOfOffers = arr.size();
                        // If Offer Exist
                        for (int i = 0; i < 2; i++) { // TODO STOPPING TILL TWO OFFRERS ONLY - 02-12-2016
                            vwOffer = inflater.inflate(R.layout.layout_offers, null);
                            vwOffer.setVisibility(View.VISIBLE);
                       /* ViewGroup.LayoutParams params = vwOffer.getLayoutParams();
                        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        vwOffer.setLayoutParams(params);*/
                            TextView txtOffer1 = (TextView) vwOffer.findViewById(R.id.offerDetails1);
                            TextView txtOffer2 = (TextView) vwOffer.findViewById(R.id.offerDetails2);
                            txtOffer1.setTypeface(General.getAppDefaultTypeface(context));
                            txtOffer2.setTypeface(General.getAppDefaultTypeface(context));
                            // LinearLayout llOffer1 = (LinearLayout) vwOffer.findViewById(R.id.llOffers1);
                            LinearLayout llOffer2 = (LinearLayout) vwOffer.findViewById(R.id.llOffers2);
                            if (i + 1 < numberOfOffers) {
                                txtOffer1.setText(arr.get(i));
                                txtOffer2.setText(arr.get(i + 1));
                                llOffer2.setVisibility(View.VISIBLE);
                            } else {
                                txtOffer1.setText(arr.get(i));
                                llOffer2.setVisibility(View.INVISIBLE);
                            }
                            txtOffer1.setTypeface(General.getAppDefaultTypeface(context));
                            txtOffer2.setTypeface(General.getAppDefaultTypeface(context));
                            ViewGroup.LayoutParams params = holder.llOffers.getLayoutParams();
                            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                            holder.llOffers.setLayoutParams(params);
                            holder.llOffers.addView(vwOffer);
                            // Increment for Second
                            ++i;
                        }
                        // Adding Dummy to maintain item Size (as per client on 07.12.2016)
                        if (numberOfOffers == 0) {
                            /*vwOffer = inflater.inflate(R.layout.layout_offers, null);
                            vwOffer.setVisibility(View.INVISIBLE);
                            ViewGroup.LayoutParams params = vwOffer.getLayoutParams();
                            params.height = 50;
                            vwOffer.setLayoutParams(params);
                            holder.llOffers.addView(vwOffer);*/
                            /*ViewGroup.LayoutParams params = holder.llOffers.getLayoutParams();
                            params.height = 50;
                            holder.llOffers.setLayoutParams(params);*/
                        }
                        holder.isAlreadyDone = true;
                    }
                } else {
                    // Price Comparison
                    productTitle = productName;
                    product_lowest_price = obj.getString("product_price_after");
                    product_image = productImage;
                    holder.txtLableProductPrice.setText("Lowest Price");
                    Log.i("Adapter", "Com " + (obj.getString("site")).toUpperCase());
                    holder.txtResultFrom.setText((obj.getString("site")).toUpperCase() + "");
                    holder.txtResultFrom.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (productTitle == null) {
            holder.txtProductName.setText("Error");
        } else {
            holder.txtProductName.setText(productTitle.toUpperCase());
        }
        if (product_lowest_price.equals("0")) {
            holder.txtProductPrice.setText("Error");
        } else {
            holder.txtProductPrice.setText(product_lowest_price);
        }
        // show The Image in a ImageView
        /*if(bitmaps[position]==null) {
            new DownloadImageTask(position, holder)
                    .execute(product_image);
        }else{
            holder.imgProduct.setImageBitmap(bitmaps[position]);
        }*/
        //
        Picasso.with(context).load(product_image).placeholder(R.mipmap.ic_cash_gain_head).fit().centerInside().into(holder.imgProduct);

        holder.vProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isExcatProduct) {
                    General.showProgress(context);
                    JSONObject obj = null;
                    String url = null;
                    try {
                        obj = resultArray.getJSONObject(position);
                        if (obj != null) {
                            url = (String) obj.get("url");
                            hitLinkwithAffId(url);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (General.mAppProgressDialog.isShowing()) {
                            General.mAppProgressDialog.dismiss();
                        }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return resultArray.length();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        protected TextView txtProductName;
        protected TextView txtProductSaving;
        protected TextView txtLableProductSaving;
        protected TextView txtProductPrice;
        protected TextView txtLableProductPrice;
        protected ImageView imgProduct;
        protected View vProduct;
        protected TextView txtResultFrom;
        protected LinearLayout llOffers;
        protected boolean isAlreadyDone = false;

        public CustomViewHolder(View view) {
            super(view);
            this.txtProductName = (TextView) view.findViewById(R.id.txtProductName);
            this.txtProductPrice = (TextView) view.findViewById(R.id.txtProductPrice);
            this.txtLableProductPrice = (TextView) view.findViewById(R.id.txtLableProductPrice);
            this.txtProductSaving = (TextView) view.findViewById(R.id.txtProductSaving);
            this.txtLableProductSaving = (TextView) view.findViewById(R.id.txtLableProductSaving);
            this.imgProduct = (ImageView) view.findViewById(R.id.imgProduct);
            this.vProduct = view.findViewById(R.id.cvProduct);
            this.txtResultFrom = (TextView) view.findViewById(R.id.txtResultFrom);
            this.llOffers = (LinearLayout) view.findViewById(R.id.llOffers);

            // Setting Typeface
            this.txtProductName.setTypeface(General.getAppDefaultTypeface(context));
            this.txtProductPrice.setTypeface(General.getAppDefaultTypeface(context));
            this.txtLableProductPrice.setTypeface(General.getAppDefaultTypeface(context));
            this.txtProductSaving.setTypeface(General.getAppDefaultTypeface(context));
            this.txtLableProductSaving.setTypeface(General.getAppDefaultTypeface(context));
            this.txtResultFrom.setTypeface(General.getAppDefaultTypeface(context));
        }
    }


    // Get Image From URL - NOT IN USE
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        int pos;
        RecyclerAdapterProductList.CustomViewHolder holder;

        public DownloadImageTask(int pos, RecyclerAdapterProductList.CustomViewHolder holder) {
            this.pos = pos;
            this.holder = holder;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                if (e != null) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
            }
            return mIcon11;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        protected void onPostExecute(Bitmap result) {
            bitmaps[pos] = result;
            holder.imgProduct.postInvalidateOnAnimation();
        }
    }


    private class GetDataAtLevel2 extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls) {
            // TODO REMOVE DEPRECATED CODE
            CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
            String mResponse = null;
            String dataUrl = "";
            if (urls != null && urls.length >= 1) {
                dataUrl = urls[0];
                try {
                    HttpClient client = new DefaultHttpClient();

                    HttpGet request = new HttpGet(dataUrl);
                    HttpResponse response;

                    response = client.execute(request);

                    HttpEntity httpEntity = response.getEntity();

                    InputStream inputStream = httpEntity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String result = "";
                    String line = "";
                    while (null != (line = reader.readLine())) {
                        result += line;
                    }
                    mResponse = result;
                    Log.d("Server response - L2", mResponse + "");

                } catch (Exception e) {
                    e.printStackTrace();
                }
                // COMMENTED FOR TEST
                // String[] parts = dataUrl.split("\\?");
                // String urlParameters = parts[1];
                // dataUrl = parts[0];
                // dataUrl = dataUrl+"?";
                // URL mURL;
                // HttpURLConnection con = null;
                /*try {
                    // Create con
                    Log.i("Final URL-L2", dataUrl);
                    Log.i("Final URLPARA-L2", urlParameters);
                    mURL = new URL(dataUrl);
                    Log.i("Final URL-l2", mURL+"");
                    con = (HttpURLConnection) mURL.openConnection();
                    con.setRequestMethod("GET");
                    // con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                    // con.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
                    // con.setRequestProperty("Accept-Encoding", "gzip, deflate");
                    // con.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,***///*//**//*;q=0.8");
                // con.setRequestProperty("User-Agent", "Mozilla/5.0");
                // con.setRequestProperty("Connection", "keep-alive");
                //con.setRequestProperty("Cache-Control","max-age=0");
                    /*con.setRequestProperty("Content-Language", "en-US");
                    con.setRequestProperty("Accept", "***///*//**//*");
                    /*con.setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ");
                    con.setUseCaches(false);
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    // Send request
                    DataOutputStream wr = new DataOutputStream(
                            con.getOutputStream());
                    // wr.writeBytes(urlParameters);
                    wr.flush();
                    wr.close();
                    // Get Response
                    Log.i("Response Code", con.getResponseCode() + "");
                    InputStream is = con.getInputStream();
                    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                    String line;
                    StringBuffer response = new StringBuffer();
                    Log.d("Server response-L2-Pre", response.toString());
                    while ((line = rd.readLine()) != null) {
                        response.append(line);
                        response.append('\r');
                    }
                    rd.close();
                    Log.d("Server response - L2", response+"");
                    mResponse = response.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (con != null) {
                        con.disconnect();
                    }
                }*/
            }
            return mResponse;
        }

        protected void onPostExecute(String response) {
            if (response != null && !response.isEmpty()) {
                JSONObject jObject;
                JSONArray jArray = null;
                try {
                    jObject = new JSONObject(response);
                    if (jObject.has("data") && jObject.get("data") != null) {
                        jArray = jObject.getJSONArray("data");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (General.mAppProgressDialog.isShowing()) {
                        General.mAppProgressDialog.dismiss();
                    }
                }
                if (jArray != null && jArray.length() > 0) {
                    try {
                        jObject = (JSONObject) jArray.get(0);
                        String url = (String) jObject.get("product_store_url");
                        hitLinkwithAffId(url);
                        if (General.mAppProgressDialog.isShowing()) {
                            General.mAppProgressDialog.dismiss();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (General.mAppProgressDialog.isShowing()) {
                            General.mAppProgressDialog.dismiss();
                        }
                    }
                } else {
                    Log.d("Server L2", "DATA ARRRAY NULL OR EMPTY");
                    if (General.mAppProgressDialog.isShowing()) {
                        General.mAppProgressDialog.dismiss();
                    }
                }
            } else {
                Log.d("Server L2", "Response NULL");
                if (General.mAppProgressDialog.isShowing()) {
                    General.mAppProgressDialog.dismiss();
                }
            }
        }
    }

    private void hitLinkwithAffId(String url) {
        Log.i("Received URL", url + "");
        try {
            SharedPreferences pref = context.getApplicationContext().getSharedPreferences("MyPref", Activity.MODE_PRIVATE);
            Log.i("Pref", pref + "");
            if (pref.contains("userID")) {
                String userID = pref.getString("userID", null);
                Log.i("UserID", userID + "");
                if (userID != null) {
                    userID = "Z" + userID;
                    if (url.contains("snapdeal.com/")) {
                        // Sending Url as it is (Affiliate closed)
                    } else if (url.contains("myntra.com/")) {
                        url = "http://tracking.vcommission.com/aff_c?offer_id=22&aff_id=34482&url=" + url + "?src=search&utm_source=vcommission&utm_medium=affiliate&aff_sub=" + userID;
                    } else if (url.contains("jabong.com/")) {
                        url = "http://tracking.vcommission.com/aff_c?offer_id=126&aff_id=34482&url=" + url + "?pos=3&utm_source=VCOMMISSION.COM&utm_medium=dc-clicktracker&utm_campaign={affiliate_id}_{transaction_id}&aff_sub=" + userID;
                    } else if (url.contains("indiatimes.com/")) {
                        url = "http://tracking.vcommission.com/aff_c?offer_id=1060&aff_id=34482&url=" + url + "?utm_source=vcommission&utm_medium=affiliate&aff_sub=" + userID;
                    } else if (url.contains("paytm.com/")) {
                        url = "http://tracking.vcommission.com/aff_c?offer_id=1022&aff_id=34482&url=" + url + "&utm_source=VCOMM&utm_medium=affiliate&utm_campaign=VCOMM-generic&utm_term={affiliate_id}_" + userID + "&aff_sub=" + userID;
                    } else if (url.contains("ebay.in/")) {
                        url = "http://tracking.vcommission.com/aff_c?offer_id=1018&aff_id=34482&url=" + url + "?m_pre=http://www.ebay.in/?aff_source=vcom&aff_sub=" + userID;
                    } else if (url.contains("amazon.in/")) {
                        url += "?tag=vcomm297-21&ascsubtag=34482_" + userID;
                    } else if (url.contains("shopclues.com/")) {
                        url = "http://tracking.vcommission.com/aff_c?offer_id=122&aff_id=34482&aff_sub=" + userID + "&url=http://c.affiliateshopclues.com/?a=9&c=69&p=r&E=AXZHEP%2bFivk%3d&s1=&ckmrdr=" + url;
                    } else if (url.contains("flipkart.com/")) {
                        if (url.contains("www.flipkart.com/")) {
                            url = url.replace("www.flipkart.com/", "dl.flipkart.com/dl/");
                        }
                        url += "&affid=anchal09n&affExtParam1=" + userID;
                    }
                    Log.i("Final URL", url + "");
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    browserIntent.setFlags(browserIntent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    context.startActivity(browserIntent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Log.i("Finally URL", url + "");
        }
    }
}


