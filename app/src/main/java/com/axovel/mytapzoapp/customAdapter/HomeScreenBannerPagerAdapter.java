package com.axovel.mytapzoapp.customAdapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.axovel.mytapzoapp.R;

/**
 * Created by axovel on 7/28/2017.
 */

public class HomeScreenBannerPagerAdapter extends PagerAdapter{

    private LayoutInflater layoutInflater = null;
    private Context context;
    @Override
    public int getCount() {
        return 5;
    }

    public HomeScreenBannerPagerAdapter(Context context){
        this.context = context;
        this.layoutInflater = (LayoutInflater)this.context.getSystemService(this.context.LAYOUT_INFLATER_SERVICE);
//        this.dataObjectList = dataObjectList;
    }
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    private android.view.ViewGroup container;
    //    @Override
    public Object instantiateItem(ViewGroup container, int position) {
    View view = this.layoutInflater.inflate(R.layout.pager_layout, container, false);
//////        ImageView displayImage = (ImageView)view.findViewById(R.id.large_image);
////        TextView sponsorTitletext = (TextView)view.findViewById(R.id.subcriptionTitleText);
////        TextView sponsorPricetext=(TextView)view.findViewById(R.id.subcriptionPriceText);
////        sponsorTitletext.setText(this.dataObjectList.get(position).getTitlename());
////        sponsorPricetext.setText(this.dataObjectList.get(position).getPrice());
//////        displayImage.setImageResource(this.dataObjectList.get(position).getImageId());
//////        imageText.setText(this.dataObjectList.get(position).getImageName());
        container.addView(view);
        return view;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
