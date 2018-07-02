package com.axovel.mytapzoapp.customAdapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.axovel.mytapzoapp.models.Offers_product_Data;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

/**
 * Created by axovel on 8/21/2017.
 */

public class ProductHorizontalAdapter extends RecyclerView.Adapter<ProductHorizontalAdapter.MySingleProductviewHolder> {

    List<Offers_product_Data > productHorizontalList = Collections.emptyList();
    Context context;

    public ProductHorizontalAdapter(List<Offers_product_Data> productHorizontalList, Context context) {
        this.productHorizontalList = productHorizontalList;
        this.context = context;
    }


    @Override
    public MySingleProductviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_product_best_offers, parent, false);

        return new MySingleProductviewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MySingleProductviewHolder holder, int position) {

        holder.onBindData(position);
    }

    @Override
    public int getItemCount() {
        return productHorizontalList.size();
    }

    public class MySingleProductviewHolder extends RecyclerView.ViewHolder {

        private ItemClickListner itemClickListener;
        private ImageView imageProduct;
        private TextView titleProduct;
        private TextView priceProduct;
        private TextView descriptionProduct;

        public MySingleProductviewHolder(View itemView) {
            super(itemView);

            imageProduct=(ImageView) itemView.findViewById(R.id.productImageView);
            titleProduct=(TextView) itemView.findViewById(R.id.titleProducttext);
            priceProduct=(TextView)itemView.findViewById(R.id.txtLableProductPrice);
            descriptionProduct=(TextView)itemView.findViewById(R.id.description_text);
        }

        public void onBindData(final int position) {

            titleProduct.setText(productHorizontalList.get(position).getTitleProduct());
            descriptionProduct.setText(productHorizontalList.get(position).getDescriptionProduct());
            String image=productHorizontalList.get(position).getImageUrl();
            Log.d("rrrr",image);
            Glide.with(context).load(productHorizontalList.get(position).getImageUrl()).into(imageProduct);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(position == getAdapterPosition()){
                        String url = productHorizontalList.get(position).getUrlSend();
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    }
//
                }
            });
        }
    }
}
