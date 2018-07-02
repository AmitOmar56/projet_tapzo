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
import android.widget.Toast;

import com.axovel.mytapzoapp.R;
import com.axovel.mytapzoapp.activities.Cab_Details_Activity;
import com.axovel.mytapzoapp.activities.FlightScreenActivity;
import com.axovel.mytapzoapp.activities.HoroscopeScreenActivity;
import com.axovel.mytapzoapp.activities.ShoppingScreenActivity;
import com.axovel.mytapzoapp.activities.WebViewScreenActivity;
import com.axovel.mytapzoapp.models.Offers_product_Data;
import com.axovel.mytapzoapp.models.VendarDataList;
import com.axovel.mytapzoapp.utils.Constants;

import java.util.Collections;
import java.util.List;

/**
 * Created by axovel on 8/21/2017.
 */

public class VendarAppHorizontalAdapter extends RecyclerView.Adapter<VendarAppHorizontalAdapter.MySingleAppviewHolder> implements Constants{

    List<VendarDataList> vendarDataLists = Collections.emptyList();
    Context context;

    public VendarAppHorizontalAdapter(List<VendarDataList> vendarDataLists, Context context) {
        this.vendarDataLists = vendarDataLists;
        this.context = context;
    }
    @Override
    public MySingleAppviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_layout_horizontal_screen, parent, false);

        return new VendarAppHorizontalAdapter.MySingleAppviewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MySingleAppviewHolder holder, int position) {
        
        holder.onBindData(position);

    }

    @Override
    public int getItemCount() {
        return vendarDataLists.size();
    }

    public class MySingleAppviewHolder extends RecyclerView.ViewHolder {

        private ItemClickListner itemClickListener;
        TextView appNameText;
        ImageView appImageView;
        public MySingleAppviewHolder(View itemView) {

            super(itemView);
            appImageView= (ImageView) itemView.findViewById(R.id.appImageview);
            appNameText= (TextView) itemView.findViewById(R.id.appNmaeText);
        }

        public void onBindData(final int position) {

            appNameText.setText(vendarDataLists.get(position).getAppName());
            appImageView.setImageResource(vendarDataLists.get(position).getAppImage());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(getAdapterPosition()== 0){
                        Intent i = new Intent(context.getApplicationContext(), WebViewScreenActivity.class);
//                        i.setData(Uri.parse("dl.flipkart.com/dl/?affid=anchal09n"));
//                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


                        i.putExtra(GET_WEBVIEW_URL,"dl.flipkart.com/dl/?affid=anchal09n");
                        i.putExtra(HEADER,"Flipkart Offers");
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);


//                        Uri uri = Uri.parse("http://dl.flipkart.com/dl/?affid=anchal09n");
//                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    }
                    if(position==1){

                        Toast.makeText(context.getApplicationContext(),"Under Progress..",Toast.LENGTH_SHORT).show();

                    }


                    if(position==2){


                        Intent i = new Intent(context.getApplicationContext(), WebViewScreenActivity.class);
//
                        i.putExtra(GET_WEBVIEW_URL,"http://tracking.vcommission.com/aff_c?offer_id=1022&aff_id=34482");
                        i.putExtra(HEADER,"Paytm Offers");
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);

//                        Intent i = new Intent(Intent.ACTION_VIEW);
//                        i.setData(Uri.parse("tracking.vcommission.com/aff_c?offer_id=1022&aff_id=34482"));
//                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        context.startActivity(i);
                    }
                    if(position==3){
//                        Intent i = new Intent(Intent.ACTION_VIEW);
//                        i.setData(Uri.parse("tracking.vcommission.com/aff_c?offer_id=22&aff_id=34482"));
//                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        context.startActivity(i);

                        Intent i = new Intent(context.getApplicationContext(), WebViewScreenActivity.class);
//
                        i.putExtra(GET_WEBVIEW_URL,"http://tracking.vcommission.com/aff_c?offer_id=22&aff_id=34482");
                        i.putExtra(HEADER,"Myntra Offers");
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    }
                    if(position==4){
//                        Intent i = new Intent(Intent.ACTION_VIEW);
//                        i.setData(Uri.parse("tracking.vcommission.com/aff_c?offer_id=2672&aff_id=34482"));
//                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        context.startActivity(i);


                        Intent i = new Intent(context.getApplicationContext(), WebViewScreenActivity.class);
//
                        i.putExtra(GET_WEBVIEW_URL,"http://tracking.vcommission.com/aff_c?offer_id=2672&aff_id=34482");
                        i.putExtra(HEADER,"Jbong Offers");
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    }
                }
            });

        }
    }
}
