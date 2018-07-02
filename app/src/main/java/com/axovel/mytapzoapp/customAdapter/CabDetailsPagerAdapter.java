package com.axovel.mytapzoapp.customAdapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.axovel.mytapzoapp.R;
import com.axovel.mytapzoapp.models.CabDataList;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by axovel on 8/28/2017.
 */

public abstract class CabDetailsPagerAdapter extends PagerAdapter {


    private LayoutInflater layoutInflater = null;
    private Context context;
    private List<CabDataList> data;


    public CabDetailsPagerAdapter(Context context, List<CabDataList> data) {
        this.context = context;
        this.layoutInflater = (LayoutInflater) this.context.getSystemService(this.context.LAYOUT_INFLATER_SERVICE);
        this.data = data;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {

        return view == ((View) object);

    }


    private android.view.ViewGroup container;

    //    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = this.layoutInflater.inflate(R.layout.car_recycler_view_container, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.cardetailsRecyclerview);
            CabDetailsRecyclerViewAdapter cabDetailsRecyclerViewAdapter = new CabDetailsRecyclerViewAdapter((ArrayList<CabDataList>) data, context) {



                @Override
                protected void onLOginPopup(int position, String cabType, String estimateTime, String cabImage,String baseFare,String display_Name) {

                    Log.d("share DisplayNAMe",display_Name);

                    onLOginPopup2(position,cabType,estimateTime,cabImage,baseFare,display_Name);

                }


            };
            recyclerView.setAdapter(cabDetailsRecyclerViewAdapter);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


    protected abstract void onLOginPopup2(int position, String cabType, String estimateTime,String cabImage,String baseFare,String display_Name);
}
