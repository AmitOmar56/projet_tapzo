package com.axovel.mytapzoapp.customAdapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.axovel.mytapzoapp.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    List<String> itemname = new ArrayList<String>();
    List<String> image = new ArrayList<String>();
    List<String> cuisine = new ArrayList<String>();
    List<String> offers = new ArrayList<String>();
    List<String> costFor2=new ArrayList<String>();
    List<String> rating=new ArrayList<String>();
    List<String> list_id=new ArrayList<>();

    public CustomListAdapter(Activity context, List<String> itemname, List<String> image, List<String> cuisine,List<String> offers, List<String> costFor2, List<String> rating,List<String>list_id) {

        super(context, R.layout.food_list, itemname);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=itemname;
        this.image=image;
        this.cuisine=cuisine;
        this.offers=offers;
        this.costFor2=costFor2;
        this.rating=rating;
        this.list_id=list_id;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.food_list, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.pic);
        TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);
        TextView offer=(TextView) rowView.findViewById(R.id.textView2);
        TextView costFor=(TextView) rowView.findViewById(R.id.costFor2);
        TextView rate=(TextView) rowView.findViewById(R.id.rate);
        TextView apiname=(TextView) rowView.findViewById(R.id.xmlview);
//        TextView costFor=(TextView) rowView.findViewById(R.id.textView3);

        apiname.setText(list_id.get(position));
        Log.d("apiname",list_id.get(position));
        txtTitle.setText(itemname.get(position));

        String imagerest="http://api.dineoutdeals.in/assets/images/uploads/restaurant/sharpen/1/k/q/p14036-149042729158d61d9bc109b.jpg";

        Log.d("imagerest",imagerest);

//        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));
//        ImageLoader.getInstance().displayImage(imagerest, imageView, Utils.imageLoadingOptions());
      //  Picasso.with(context).load("http://www.benandjerrys.ca/files/live/sites/systemsite/files/our-values/initiative-details/productive-waste.jpg").into(imageView);
//        Picasso.with(context).load("http://i.imgur.com/DvpvklR.png").into(imageView);
        Glide.with(context).load(image.get(position)).into(imageView);
        extratxt.setText(cuisine.get(position));
        costFor.setText(costFor2.get(position));
        if(rating.get(position).equals("")){
            rate.setVisibility(View.GONE);
        }
        else {
            rate.setText(" " + rating.get(position) + " ");
        }
        if(offers.get(position).equals("")){
            offer.setVisibility(View.GONE);
        }
        else{
            offer.setText(offers.get(position));
        }
        return rowView;
    };
}