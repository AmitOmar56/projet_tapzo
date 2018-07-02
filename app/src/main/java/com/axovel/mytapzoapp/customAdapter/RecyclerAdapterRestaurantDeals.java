package com.axovel.mytapzoapp.customAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.axovel.mytapzoapp.R;
import com.axovel.mytapzoapp.utils.General;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Umesh Chauhan on 15-04-2016.
 * Axovel Private Limited
 */
public class RecyclerAdapterRestaurantDeals extends RecyclerView.Adapter<RecyclerAdapterRestaurantDeals.CustomViewHolder> {

    public JSONArray resultArray;
    Context context;

    public RecyclerAdapterRestaurantDeals(JSONArray resultArray, Context context){
        this.resultArray = resultArray;
        this.context = context;
    }

    @Override
    public RecyclerAdapterRestaurantDeals.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recyclerview_item_rest, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(lp);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerAdapterRestaurantDeals.CustomViewHolder holder, final int position) {
        try {
            final JSONObject obj = resultArray.getJSONObject(position);
            if(obj.has("restArea")) {
                holder.txtRestaurantName.setText(obj.getString("restName") + " - " + obj.getString("restArea"));
            }else{
                holder.txtRestaurantName.setText(obj.getString("restName"));
            }
            holder.txtOfferDetails.setText(obj.getString("restOffer"));
            holder.txtResultFrom.setText(obj.getString("avlOn"));
            holder.vDeal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        SharedPreferences pref = context.getApplicationContext().getSharedPreferences("MyPref", Activity.MODE_PRIVATE);
                        Log.i("Pref", pref+"");
                        String userID = "0";
                        if (pref.contains("userID")) {
                            userID = pref.getString("userID", null);
                            Log.i("UserID", userID + "");
                        }
                        Intent browserIntent;
                        if(obj.has("avlOn") && obj.getString("avlOn").equals("DINE OUT DEALS")) {
                            browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(obj.getString("restURL") + "?aff_sub=" + userID));
                        }else{
                            browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(obj.getString("restURL") + "&aff_sub=" + userID));
                        }
                        context.startActivity(browserIntent);
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return resultArray.length();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtRestaurantName;
        protected TextView txtOfferDetails;
        protected View vDeal;
        protected TextView txtResultFrom;

        public CustomViewHolder(View view) {
            super(view);
            this.txtRestaurantName = (TextView) view.findViewById(R.id.txtRestaurantName);
            this.txtOfferDetails = (TextView) view.findViewById(R.id.txtOfferDetails);
            this.vDeal = view.findViewById(R.id.cvDeal);
            this.txtResultFrom = (TextView) view.findViewById(R.id.txtResultFrom);

            // Setting Typeface
            this.txtRestaurantName.setTypeface(General.getAppDefaultTypeface(context));
            this.txtOfferDetails.setTypeface(General.getAppDefaultTypeface(context));
            this.txtResultFrom.setTypeface(General.getAppDefaultTypeface(context));
        }
    }
}
