package com.axovel.mytapzoapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.axovel.mytapzoapp.R;
import com.axovel.mytapzoapp.customAdapter.UpcommingRidesRecyclerViewAdapter;
import com.axovel.mytapzoapp.models.CabDataList;

import java.util.ArrayList;
import java.util.List;

public class RideLaterListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UpcommingRidesRecyclerViewAdapter upcomingRideRecyclerViewAdapter;
    private List<CabDataList> getdata = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_later_list);

        initRefrenceId();
        getdata = datalistCancel();
        setAdapter();
    }

    private void initRefrenceId() {

        recyclerView = (RecyclerView) findViewById(R.id.upcommingRidesRecyclerView);


    }

    private List<CabDataList> datalistCancel() {
        List<CabDataList> data = new ArrayList<>();
        data.add(new CabDataList("http://d1foexe15giopy.cloudfront.net/mini.png", "Mini", "RamPhalChowk", "Rajori Garden", "13 sept", "1:20"));
        data.add(new CabDataList("http://d1foexe15giopy.cloudfront.net/mini.png", "Mini", "RamPhalChowk", "Rajori Garden", "13 sept", "1:20"));
        data.add(new CabDataList("http://d1foexe15giopy.cloudfront.net/mini.png", "Mini", "RamPhalChowk", "Rajori Garden", "13 sept", "1:20"));

        data.add(new CabDataList("http://d1foexe15giopy.cloudfront.net/mini.png", "Mini", "RamPhalChowk", "Rajori Garden", "13 sept", "1:20"));
        data.add(new CabDataList("http://d1foexe15giopy.cloudfront.net/mini.png", "Mini", "RamPhalChowk", "Rajori Garden", "13 sept", "1:20"));
        data.add(new CabDataList("http://d1foexe15giopy.cloudfront.net/mini.png", "Mini", "RamPhalChowk", "Rajori Garden", "13 sept", "1:20"));
        data.add(new CabDataList("http://d1foexe15giopy.cloudfront.net/mini.png", "Mini", "RamPhalChowk", "Rajori Garden", "13 sept", "1:20"));
        data.add(new CabDataList("http://d1foexe15giopy.cloudfront.net/mini.png", "Mini", "RamPhalChowk", "Rajori Garden", "13 sept", "1:20"));
        data.add(new CabDataList("http://d1foexe15giopy.cloudfront.net/mini.png", "Mini", "RamPhalChowk", "Rajori Garden", "13 sept", "1:20"));
        data.add(new CabDataList("http://d1foexe15giopy.cloudfront.net/mini.png", "Mini", "RamPhalChowk", "Rajori Garden", "13 sept", "1:20"));
        data.add(new CabDataList("http://d1foexe15giopy.cloudfront.net/mini.png", "Mini", "RamPhalChowk", "Rajori Garden", "13 sept", "1:20"));
        data.add(new CabDataList("http://d1foexe15giopy.cloudfront.net/mini.png", "Mini", "RamPhalChowk", "Rajori Garden", "13 sept", "1:20"));
        data.add(new CabDataList("http://d1foexe15giopy.cloudfront.net/mini.png", "Mini", "RamPhalChowk", "Rajori Garden", "13 sept", "1:20"));
        data.add(new CabDataList("http://d1foexe15giopy.cloudfront.net/mini.png", "Mini", "RamPhalChowk", "Rajori Garden", "13 sept", "1:20"));
        data.add(new CabDataList("http://d1foexe15giopy.cloudfront.net/mini.png", "Mini", "RamPhalChowk", "Rajori Garden", "13 sept", "1:20"));
        data.add(new CabDataList("http://d1foexe15giopy.cloudfront.net/mini.png", "Mini", "RamPhalChowk", "Rajori Garden", "13 sept", "1:20"));


        return data;
    }

    private void setAdapter() {

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        upcomingRideRecyclerViewAdapter = new UpcommingRidesRecyclerViewAdapter(getApplicationContext(), (ArrayList<CabDataList>) getdata) {
            @Override
            protected void removeRideLaterList(int Position) {
                getdata.remove(Position);
                setAdapter();
            }
        };
        recyclerView.setAdapter(upcomingRideRecyclerViewAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


    }


}
