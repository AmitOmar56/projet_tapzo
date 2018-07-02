package com.axovel.mytapzoapp.customAdapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.axovel.mytapzoapp.R;
import com.axovel.mytapzoapp.activities.Cab_Details_Activity;
import com.axovel.mytapzoapp.activities.FlightScreenActivity;
import com.axovel.mytapzoapp.activities.FoodScreenActivity;
import com.axovel.mytapzoapp.activities.HoroscopeScreenActivity;
import com.axovel.mytapzoapp.activities.OlaLoginWebViewDilogActivity;
import com.axovel.mytapzoapp.activities.ShoppingScreenActivity;
import com.axovel.mytapzoapp.activities.WebViewScreenActivity;
import com.axovel.mytapzoapp.models.TapzoComponentData;
import com.axovel.mytapzoapp.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by axovel on 7/30/2017.
 */

public class ComponentRecyclerAdapter extends RecyclerView.Adapter<ComponentRecyclerAdapter.MySeperateComponent> implements Constants{

//    ItemClickListner itemClickListner;
//    private final int VIEW_ITEM = 1;
//    private final int VIEW_PROG = 0;
//    private boolean loading;
//    private OnLoadMoreListener onLoadMoreListener = null;
//
//    // The minimum amount of items to have below your current scroll position
//    // before loading more.
//    private int visibleThreshold = 3;
//    private int lastVisibleItem, totalItemCount;


    private List<TapzoComponentData> tapzoComponentData;
    private Context context;


    public ComponentRecyclerAdapter(ArrayList<TapzoComponentData> tapzoComponentData, Context context) {
        this.tapzoComponentData = tapzoComponentData;
        this.context = context;


    }

    @Override
    public MySeperateComponent onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_recyclerview_layout, parent, false);

        return new MySeperateComponent(view);
    }

    @Override
    public void onBindViewHolder(MySeperateComponent holder, int position) {

        holder.onBindData(position);
    }

    @Override
    public int getItemCount() {
        return tapzoComponentData.size();
    }

    public class MySeperateComponent extends RecyclerView.ViewHolder {

        private ItemClickListner itemClickListener;
        private ImageView componentImage = null;
        private TextView componentNameText = null;
        private TextView componentOffersText = null;


        public MySeperateComponent(final View itemView) {
            super(itemView);

            componentImage = (ImageView) itemView.findViewById(R.id.componentImageview);
            componentNameText = (TextView) itemView.findViewById(R.id.coponentNameTextView);
            componentOffersText = (TextView) itemView.findViewById(R.id.componentOffersTextView);
        }

        public void onBindData(final int pos) {

            componentNameText.setText(tapzoComponentData.get(pos).getComponentName());
            componentOffersText.setText(tapzoComponentData.get(pos).getComponentOffers());
            componentImage.setImageResource(tapzoComponentData.get(pos).getComponentImage());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(pos == 0){
                        context.startActivity(new Intent(context, Cab_Details_Activity.class));
                    }
                    if(pos == 1){
                        context.startActivity(new Intent(context, FoodScreenActivity.class));
                    }
                    if(pos==2){
                        context.startActivity(new Intent(context, ShoppingScreenActivity.class));


                    }
                    if(pos==3){
                        context.startActivity(new Intent(context, FlightScreenActivity.class));
                    }
                    if(pos==4){
                        context.startActivity(new Intent(context, HoroscopeScreenActivity.class));
                    }

                    if (pos==5){

                        Intent i = new Intent(context,WebViewScreenActivity.class);
//                        i.setData(Uri.parse("https://mobikwik.go2cloud.org/aff_c?offer_id=187&aff_id=95"));
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.putExtra(GET_WEBVIEW_URL,"https://mobikwik.go2cloud.org/aff_c?offer_id=187&aff_id=95");
                        i.putExtra(HEADER,"Bill Payments");
                        context.startActivity(i);


                    }
                    if(pos==6){
                        Intent i = new Intent(context,WebViewScreenActivity.class);
//                        i.setData(Uri.parse("https://in.bookmyshow.com"));
//                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.putExtra(GET_WEBVIEW_URL,"https://in.bookmyshow.com");
                        i.putExtra(HEADER,"Book My Show");
                        context.startActivity(i);
                    }

                    if(pos==7){
                        Intent i = new Intent(context,WebViewScreenActivity.class);
//                        i.setData(Uri.parse("https://affiliate.trivago.com/api/creative/20556/javascript"));
//                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.putExtra(GET_WEBVIEW_URL,"https://affiliate.trivago.com/api/creative/20561/javascript");
                        i.putExtra(HEADER,"Hotels");
                        context.startActivity(i);
                    }
                    if(pos==8){
                        Intent i = new Intent(context,OlaLoginWebViewDilogActivity.class);
                        context.startActivity(i);
                    }
                }
            });
        }
    }
}

