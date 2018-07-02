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

import com.axovel.mytapzoapp.R;
import com.axovel.mytapzoapp.models.Offers_product_Data;
import com.axovel.mytapzoapp.models.ProductSearchData;
import com.axovel.mytapzoapp.models.TapzoComponentData;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by axovel on 8/23/2017.
 */

public class ProductRecyclerViewAdapter extends RecyclerView.Adapter<ProductRecyclerViewAdapter.MySingleRecyclerSearchViewHolder> {


    private List<ProductSearchData> productSearchData;
    private Context context;


    public ProductRecyclerViewAdapter(List<ProductSearchData> productSearchData, Context context) {
        this.productSearchData = productSearchData;
        this.context = context;


    }

    @Override
    public ProductRecyclerViewAdapter.MySingleRecyclerSearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_search_grid_recycler_layout, parent, false);

        return new ProductRecyclerViewAdapter.MySingleRecyclerSearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductRecyclerViewAdapter.MySingleRecyclerSearchViewHolder holder, int position) {

        holder.onBindData(position);

    }

    @Override
    public int getItemCount() {
        return productSearchData.size();
    }

    public class MySingleRecyclerSearchViewHolder extends RecyclerView.ViewHolder {

        private ItemClickListner itemClickListener;
        private ImageView productImage = null;
        private TextView titleApp = null;
        private TextView titleProduct = null;
        private TextView productPrice=null;
        public MySingleRecyclerSearchViewHolder(View itemView) {
            super(itemView);

            productImage = (ImageView) itemView.findViewById(R.id.productImageView);
            titleApp = (TextView) itemView.findViewById(R.id.titleProducttext);
            titleProduct = (TextView) itemView.findViewById(R.id.description_text);
            productPrice = (TextView) itemView.findViewById(R.id.productPrice);
        }

        public void onBindData(final int position) {

            String titleProductApp=productSearchData.get(position).getTitleApp();


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


            Log.d("welll",productSearchData.get(position).getTitleProductdata());


            titleProduct.setText(productSearchData.get(position).getTitleProductdata());
            productPrice.setText("₹"+ productSearchData.get(position).getProductPrice());

            Glide.with(context).load(productSearchData.get(position).getImageUrl()).into(productImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(position == getAdapterPosition()){
                        String url = productSearchData.get(position).getProductAffiliateLink();
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
