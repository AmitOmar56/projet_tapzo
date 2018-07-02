package com.axovel.mytapzoapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.axovel.mytapzoapp.R;

public class Offer_BookingActivity extends AppCompatActivity {
    String offer;
    TextView textView;
    String Restorent_id;
    String Time;
    String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer__booking);
        textView=(TextView) findViewById(R.id.offer_id);

        getDataFromIntent();

        textView.setText(offer);
    }
    private void getDataFromIntent() {
        if (getIntent() != null) {
            offer = getIntent().getStringExtra("offer");
            Restorent_id=getIntent().getStringExtra("rest_id");
            Time=getIntent().getStringExtra("time");
            date=getIntent().getStringExtra("Full_date");
            Log.d("restorent_id", Restorent_id);
            Log.d("offer", offer);
            Log.d("Time", Time);
            Log.d("date",date);
        }

    }

    public void offer(View view){

        Intent intent =new Intent(Offer_BookingActivity.this,Booking_2Activity.class);

        intent.putExtra("rest_id",Restorent_id);
        intent.putExtra("time",Time);
        intent.putExtra("date",date);
        startActivity(intent);
    }
    public void backButton4(View view){
        finish();
    }
}
