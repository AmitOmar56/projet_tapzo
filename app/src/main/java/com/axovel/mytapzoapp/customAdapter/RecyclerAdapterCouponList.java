package com.axovel.mytapzoapp.customAdapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.axovel.mytapzoapp.R;
import com.axovel.mytapzoapp.utils.General;
import com.axovel.mytapzoapp.view.activity.ActivityServiceCouponDetails;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Umesh Chauhan on 7/14/2016.
 */
public class RecyclerAdapterCouponList extends RecyclerView.Adapter<RecyclerAdapterCouponList.CustomViewHolder> {

    public JSONArray resultArray;
    Context context;

    public RecyclerAdapterCouponList(JSONArray resultArray, Context context){
        this.resultArray = resultArray;
        this.context = context;
    }

    @Override
    public RecyclerAdapterCouponList.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recyclerview_item_coupon, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(lp);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerAdapterCouponList.CustomViewHolder holder, final int position) {
        try {
            final JSONObject obj = resultArray.getJSONObject(position);
            final String companyName = obj.getString("OfferName");
            if(companyName.contains(" CPS - India")){
                companyName.replace(" CPS - India","");
            }
            holder.txtCompanyName.setText(companyName);
            holder.txtOfferDetails.setText(obj.getString("Title"));
            holder.txtOfferExpiry.setText(obj.getString("Expiry"));
            Log.i("Test","Outside "+position);
            holder.vDeal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Log.i("Test","Inside "+position);
                        Intent details = new Intent(context, ActivityServiceCouponDetails.class);
                        details.putExtra("companyName",companyName);
                        details.putExtra("couponDescription",obj.getString("Description"));
                        details.putExtra("coupon",obj.getString("Code"));
                        details.putExtra("url",obj.getString("OfferPage"));
                        context.startActivity(details);
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
        protected TextView txtCompanyName;
        protected TextView txtOfferDetails;
        protected View vDeal;
        protected TextView txtOfferExpiry;

        public CustomViewHolder(View view) {
            super(view);
            this.txtCompanyName = (TextView) view.findViewById(R.id.txtCouponFrom);
            this.txtOfferDetails = (TextView) view.findViewById(R.id.txtOfferDetails);
            this.vDeal = view.findViewById(R.id.cvDeal);
            this.txtOfferExpiry = (TextView) view.findViewById(R.id.txtOfferExpiry);

            // Setting Typeface
            this.txtCompanyName.setTypeface(General.getAppDefaultTypeface(context));
            this.txtOfferDetails.setTypeface(General.getAppDefaultTypeface(context));
            this.txtOfferExpiry.setTypeface(General.getAppDefaultTypeface(context));
        }
    }
}