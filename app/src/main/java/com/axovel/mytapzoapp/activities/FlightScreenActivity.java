package com.axovel.mytapzoapp.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.axovel.mytapzoapp.R;


public class FlightScreenActivity extends AppCompatActivity {

    private TextView fromshortNameText, fromAddressText, desShortNameText, desAddressText;
    private ImageView changePossition;
    boolean isclick = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_screen);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fromshortNameText = (TextView) findViewById(R.id.niknameText);
        fromAddressText = (TextView) findViewById(R.id.fromAddressText);
        desShortNameText = (TextView) findViewById(R.id.niknamedesText);
        desAddressText = (TextView) findViewById(R.id.destinationaddresstext);
        changePossition = (ImageView) findViewById(R.id.swapeImage);

//        IxigoFlight.init(this, "INR", NetworkUtils.Environment.STAGING);
//
////        IxigoFlight.init(this, "INR","cashgain!2$","cashgain");
//        Log.d("Flight Instance", "" + IxigoFlight.getInstance());


//        IxigoFlight.getInstance().setEventsCallbacks(new FlightScreenActivity());


//        changePossition.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (isclick == false) {
//
//                    fromshortNameText.animate().translationY(110f);
//                    fromAddressText.animate().translationY(110f);
//                    desShortNameText.animate().translationY(-110f);
//                    desAddressText.animate().translationY(-110);
//                    desAddressText.setText("Origin");
//                    fromAddressText.setText("destination");
//                    isclick = true;
//
//                } else if (isclick == true) {
//
//                    fromshortNameText.animate().translationY(0f);
//                    fromAddressText.animate().translationY(0f);
//                    desShortNameText.animate().translationY(0f);
//                    desAddressText.animate().translationY(0);
//                    desAddressText.setText("destination");
//                    fromAddressText.setText("Origin");
//                    isclick = false;
//
//                }
//
//            }
//        });

        changePossition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                IxigoFlight.getInstance().launchMyBookings(FlightScreenActivity.this);
////                IxigoFlight.getInstance().launchFlightSearch(FlightScreenActivity.this, FlightSdkActivity.class);

                AlertDialog.Builder builder = new AlertDialog.Builder(FlightScreenActivity.this);
                LayoutInflater inflater = (FlightScreenActivity.this).getLayoutInflater();
                View v = inflater.inflate(R.layout.rate_card_layout, null);

                builder.setView(v);
                final AlertDialog alertDialog = builder.create();


                alertDialog.show();
            }
        });
    }

    public void backButton(View view) {

        finish();
    }


//    @Override
//    public void onSearch(Context context, FlightSearchRequest flightSearchRequest) {
//
//    }
//
//    @Override
//    public void onBookingSuccessful(Context context, FlightSearchRequest flightSearchRequest, FlightItinerary flightItinerary) {
//
//    }
//
//    @Override
//    public void onRedirect(Context context, FlightSearchRequest flightSearchRequest, IFlightFare iFlightFare) {
//
//    }
//
//    @Override
//    public void onPaymentInitiated(Context context, FlightSearchRequest flightSearchRequest, IFlightFare iFlightFare) {
//
//    }
//
//    @Override
//    public void onTripCancelled(Context context, FlightItinerary flightItinerary) {
//
//    }
}


