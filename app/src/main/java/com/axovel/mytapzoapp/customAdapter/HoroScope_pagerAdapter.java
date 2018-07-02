package com.axovel.mytapzoapp.customAdapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;

/**
 * Created by axovel on 8/16/2017.
 */

public class HoroScope_pagerAdapter extends PagerAdapter {

    //    private LayoutInflater layoutInflater = null;
    private Context context;
    // /    private List<DataObject> dataObjectList;
    private ArrayList<View> views = null;

    @Override
    public int getCount() {
        return views.size();
    }

    public HoroScope_pagerAdapter(Context context, ArrayList<View> views) {

        this.context = context;
        this.views = views;
    }

    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    private android.view.ViewGroup container;

    //    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        container.addView(views.get(position));
        return views.get(position);
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
