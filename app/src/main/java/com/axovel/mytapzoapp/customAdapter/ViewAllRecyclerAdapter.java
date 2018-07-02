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
import com.axovel.mytapzoapp.models.Offers_product_Data;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

/**
 * Created by axovel on 8/22/2017.
 */

public class ViewAllRecyclerAdapter extends RecyclerView.Adapter<ViewAllRecyclerAdapter.MySingleListProduct> {


    List<Offers_product_Data > productViewAllList = Collections.emptyList();
    Context context;

    public ViewAllRecyclerAdapter(List<Offers_product_Data> productViewAllList, Context context) {
        this.productViewAllList = productViewAllList;
        this.context = context;
    }
    @Override
    public ViewAllRecyclerAdapter.MySingleListProduct onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_all_layout_list_item, parent, false);

        return new ViewAllRecyclerAdapter.MySingleListProduct(itemView);
    }

    @Override
    public void onBindViewHolder(ViewAllRecyclerAdapter.MySingleListProduct holder, int position) {

        holder.onBindData(position);

    }

    @Override
    public int getItemCount() {
        return productViewAllList.size();
    }

    public class MySingleListProduct extends RecyclerView.ViewHolder {

        private ItemClickListner itemClickListener;
        private ImageView imageProduct;
        private TextView titleProduct;
        private TextView priceProduct;
        private TextView descriptionProduct;
        public MySingleListProduct(View itemView) {
            super(itemView);

            imageProduct=(ImageView) itemView.findViewById(R.id.productImageView);
            titleProduct=(TextView) itemView.findViewById(R.id.titleappText);
//            priceProduct=(TextView)itemView.findViewById(R.id.priceText);
            descriptionProduct=(TextView)itemView.findViewById(R.id.descriptionText);
        }

        public void onBindData(final int position) {

            titleProduct.setText(productViewAllList.get(position).getTitleProduct());
            descriptionProduct.setText(productViewAllList.get(position).getDescriptionProduct());
//            imageProduct.setImageResource(productHorizontalList.get(position).getImageProduct());
//            Picasso.with(getApplicationContext()).load(cabDataList.getCar_icon()).into(miniImage);
//            priceProduct.setText(productViewAllList.get(position).getPriceProduct());
            Glide.with(context).load(productViewAllList.get(position).getImageUrl()).into(imageProduct);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(position == getAdapterPosition()){
                        String url = productViewAllList.get(position).getUrlSend();
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    }
//                    if(pos==2){
//                        context.startActivity(new Intent(context, ShoppingScreenActivity.class));
//
//
//                    }
//                    if(pos==3){
//                        context.startActivity(new Intent(context, FlightScreenActivity.class));
//                    }
//                    if(pos==4){
//                        context.startActivity(new Intent(context, HoroscopeScreenActivity.class));
//                    }
                }
            });
        }


        }

}
