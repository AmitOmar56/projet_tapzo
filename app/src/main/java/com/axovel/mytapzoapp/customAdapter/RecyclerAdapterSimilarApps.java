package com.axovel.mytapzoapp.customAdapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.axovel.mytapzoapp.R;
import com.axovel.mytapzoapp.utils.General;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Umesh Chauhan on 7/25/2016.
 */
public class RecyclerAdapterSimilarApps  extends RecyclerView.Adapter<RecyclerAdapterSimilarApps.CustomViewHolder> {

    public JSONArray resultArray;
    Context context;

    public RecyclerAdapterSimilarApps(JSONArray resultArray, Context context){
        this.resultArray = resultArray;
        this.context = context;
    }

    @Override
    public RecyclerAdapterSimilarApps.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recyclerview_item_similar_apps, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(lp);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerAdapterSimilarApps.CustomViewHolder holder, final int position) {
        try {
            final JSONObject obj = resultArray.getJSONObject(position);
            final String companyName = obj.getString("name");
            holder.txtAppName.setText(companyName);
            holder.appDescription.setText(obj.getString("description"));
            Picasso.with(context)
                    .load(obj.getString("image"))
                    .placeholder(R.mipmap.ic_cash_gain_head)
                    .fit().centerInside()
                    .into(holder.imgAppLogo);
            holder.vMainParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(obj.getString("playstore_url")));
                        browserIntent.setFlags(browserIntent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
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
        protected TextView txtAppName;
        protected View vMainParent;
        protected ImageView imgAppLogo;
        protected TextView appDescription;

        public CustomViewHolder(View view) {
            super(view);
            this.txtAppName = (TextView) view.findViewById(R.id.txtAppName);
            this.imgAppLogo = (ImageView) view.findViewById(R.id.imgAppLogo);
            this.appDescription = (TextView) view.findViewById(R.id.txtAppDecs);
            this.vMainParent = view.findViewById(R.id.rlMainParent);

            // Setting Typeface
            this.txtAppName.setTypeface(General.getAppDefaultTypeface(context));
            this.appDescription.setTypeface(General.getAppDefaultTypeface(context));
        }
    }
}
