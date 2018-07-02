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
import com.axovel.mytapzoapp.models.ProductSearchData;
import com.axovel.mytapzoapp.models.RecentalyProductData;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

/**
 * Created by axovel on 8/21/2017.
 */

public class RecentlyHorizontalAdapter extends RecyclerView.Adapter<RecentlyHorizontalAdapter.MySingleRecentlyViewHolder> {


    List<RecentalyProductData> recentalyProductData = Collections.emptyList();
    Context context;

    public RecentlyHorizontalAdapter(List<RecentalyProductData> recentalyProductData, Context context) {
        this.recentalyProductData = recentalyProductData;
        this.context = context;
    }
    @Override
    public MySingleRecentlyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recentaly_search_layout_shopping_screen, parent, false);

        return new RecentlyHorizontalAdapter.MySingleRecentlyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MySingleRecentlyViewHolder holder, int position) {

        holder.onBindData(position);

    }

    @Override
    public int getItemCount() {
        return recentalyProductData.size();
    }

    public class MySingleRecentlyViewHolder extends RecyclerView.ViewHolder {

        private ItemClickListner itemClickListener;
        private ImageView imageProduct;
        private TextView titleApp;
        private TextView priceProduct;
        private TextView descriptionProduct;
        public MySingleRecentlyViewHolder(View itemView) {
            super(itemView);

            imageProduct=(ImageView) itemView.findViewById(R.id.productImageView);
            titleApp=(TextView) itemView.findViewById(R.id.titleProducttext);
            priceProduct=(TextView)itemView.findViewById(R.id.productPrice);
            descriptionProduct=(TextView)itemView.findViewById(R.id.description_text);
        }

        public void onBindData(final int position) {

            String titleProductApp=recentalyProductData.get(position).getTitleApp();


            if(titleProductApp.equals("www.paytm.com")){
                titleApp.setText("Paytm");
            }
            else if(titleProductApp.equals("www.amazon.in")){
                titleApp.setText("Amazon");
            }
            else if(titleProductApp.equals("www.ebay.in")){
                titleApp.setText("ebay");
            }
            else if(titleProductApp.equals("www.snapdeal.com")){
                titleApp.setText("Snapdeal");
            }
            else if(titleProductApp.equals("www.flipkart.com")){
                titleApp.setText("Flipkart");
            }
            else if(titleProductApp.equals("www.ebay.in")){
                titleApp.setText("ebay");
            }else {
                titleApp.setText("");
            }

            descriptionProduct.setText(recentalyProductData.get(position).getTitleProductdata());
            priceProduct.setText("₹"+recentalyProductData.get(position).getProductPrice());
//            imageProduct.setImageResource(recentalyProductData.get(position).getImageProduct());
//            priceProduct.setText(recentalyProductData.get(position).getPriceProduct());

            Glide.with(context).load(recentalyProductData.get(position).getImageUrl()).into(imageProduct);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(position == getAdapterPosition()){
                        String url = recentalyProductData.get(position).getProductAffiliateLink();
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
