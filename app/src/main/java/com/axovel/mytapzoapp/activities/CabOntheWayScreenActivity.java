package com.axovel.mytapzoapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.axovel.mytapzoapp.R;
import com.axovel.mytapzoapp.app.AppController;
import com.axovel.mytapzoapp.models.CabDataList;
import com.axovel.mytapzoapp.utils.Constants;
import com.axovel.mytapzoapp.utils.MyProgressDialog;
import com.axovel.mytapzoapp.utils.PreferenceStore;
import com.axovel.mytapzoapp.utils.Utils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class CabOntheWayScreenActivity extends AppCompatActivity implements OnMapReadyCallback, Constants {

    private GoogleMap mMap;
    private TextView headerText, pickupText, destText;
    private LatLng latLng, fromPosition, toPosition;
    private double pick_Up_latitude;
    private double pick_Up_langitude;
    private double drop_latitude;
    private double drop_longitude;
    private String categary;
    CabDataList cabDataList;
    private String pickupPoint, dropPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cab_onthe_way_screen);

        fromPosition = getIntent().getParcelableExtra(FROMLATLNG);
        toPosition=getIntent().getParcelableExtra(TOLATLNG);

        latLng = new LatLng(fromPosition.latitude, fromPosition.longitude);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        initrefrence();
        confirmBookingCab();
    }

    private void initrefrence() {

        headerText = (TextView) findViewById(R.id.headerText);
        pickupText = (TextView) findViewById(R.id.pickUpText);
        destText = (TextView) findViewById(R.id.destinationText);
        headerText.setText(getIntent().getStringExtra("CAB_DISPLAY_NAME"));
        pickupPoint = getIntent().getStringExtra(PICK_UP_TEXT);
        dropPoint = getIntent().getStringExtra(DROP_TEXT);
        pick_Up_latitude=fromPosition.latitude;
        pick_Up_langitude=fromPosition.longitude;
        drop_latitude=toPosition.latitude;
        drop_longitude=toPosition.longitude;
        pickupText.setText(pickupPoint);
        destText.setText(dropPoint);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setPadding(0, 0, 0, Utils.dpToPx(getApplicationContext(), 65));
        mMap.addMarker(new MarkerOptions().position(latLng).snippet("").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_map_icon)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f));


    }


    public void confirmBookingCab() {
        final String loginToken=PreferenceStore.getPreference(getApplicationContext()).getString(OLALOGIN_URL);

        MyProgressDialog.showPDialog(CabOntheWayScreenActivity.this);

        String url = CAB_BOOKING_URL;


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.d("LoginResponse", response);

                MyProgressDialog.hidePDialog();


                managedataBooking();
                MyProgressDialog.hidePDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                MyProgressDialog.hidePDialog();
                Log.d("Volley..,.", "ErrorListener: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String credentials = "cashgainapp@gmail.com:Anchal09@";
//                String auth = "Basic "
//                        + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Content-Type", "application/json");
                headers.put("X-APP-TOKEN", "317d32c3ae534f39a911551538e50002");
                headers.put("Authorization", "Bearer"+ loginToken);
                return headers;
            }

            public Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("pickup_lat", "" + pick_Up_langitude);
                params.put("pickup_lng", "" + pick_Up_langitude);
                params.put("drop_lat", "" + drop_latitude);
                params.put("drop_lng", "" + drop_longitude);
                params.put("pickup_mode", "NOW");
                params.put("category", categary);
                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringRequest);


    }

    private void managedataBooking() {


        ImageView userProfile = (ImageView) findViewById(R.id.userProfile);
        TextView driverName = (TextView) findViewById(R.id.driverNmaeText);
        TextView cabnumber = (TextView) findViewById(R.id.cabNumber);
        TextView cabName = (TextView) findViewById(R.id.cabNameText);
        TextView otp = (TextView) findViewById(R.id.cabOtp);

        driverName.setText(cabDataList.getDriverName());
        cabnumber.setText(cabDataList.getCabNumber());
        cabName.setText(cabDataList.getCarModel());
        otp.setText(cabDataList.getCrn());


    }

    public void backButton(View view) {
//        Intent intent=new Intent(getApplicationContext(),ConfirmationScreenActivity.class);
//        startActivity(intent);
        finish();
    }

    public void cancelFun(View view) {

        Intent intent = new Intent(CabOntheWayScreenActivity.this, CancelRideScreenActivity.class);
        startActivity(intent);
    }

    public void callDriverFun(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("8510951953"));
        startActivity(intent);
    }
}
