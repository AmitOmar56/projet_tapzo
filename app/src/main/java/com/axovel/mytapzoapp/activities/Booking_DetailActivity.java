package com.axovel.mytapzoapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.axovel.mytapzoapp.R;

public class Booking_DetailActivity extends AppCompatActivity {
    String name;
    String rest_name;
    String rest_add;
    String No_of_guest;
    String id;
    String time_id;
    String date_id;
    TextView restoname;
    TextView restoadd;
    TextView noofguest;
    TextView restid;
    TextView date;
    TextView time;
    TextView booking_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking__detail);

        booking_name=(TextView)findViewById(R.id.book_name);
        restoname=(TextView)findViewById(R.id.resto_name);
        restoadd=(TextView)findViewById(R.id.resto_add);
        noofguest=(TextView)findViewById(R.id.no_of_guest);
        restid=(TextView)findViewById(R.id.resto_id);
        date=(TextView)findViewById(R.id.date_id);
        time=(TextView)findViewById(R.id.time_id);

        getDataFromIntent();

        booking_name.setText(name);
        restoname.setText(rest_name);
        restoadd.setText(rest_add);
        restid.setText(id);
        noofguest.setText(No_of_guest);
        date.setText(date_id);
        time.setText(time_id);
    }
    public void home(View view){
        startActivity(new Intent(Booking_DetailActivity.this,HomeScreenActivity.class));
    }
    private void getDataFromIntent() {
        if (getIntent() != null) {
            name = getIntent().getStringExtra("dinner_name");
            rest_name=getIntent().getStringExtra("rest_name");
            rest_add=getIntent().getStringExtra("rest_add");
            No_of_guest=getIntent().getStringExtra("people");
            id=getIntent().getStringExtra("booking_id");
            time_id=getIntent().getStringExtra("time");
            date_id=getIntent().getStringExtra("date");

        }
        Log.d("dinner_name", name);
        Log.d("rest_name", rest_name);
        Log.d("rest_add", rest_add);
        Log.d("No_of_guest",No_of_guest);
        Log.d("id",id);


    }
}
