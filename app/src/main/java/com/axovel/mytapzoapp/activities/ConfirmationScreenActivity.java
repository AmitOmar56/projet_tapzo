package com.axovel.mytapzoapp.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationListener;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.axovel.mytapzoapp.R;
import com.axovel.mytapzoapp.app.AppController;
import com.axovel.mytapzoapp.models.CabDataList;
import com.axovel.mytapzoapp.utils.Constants;
import com.axovel.mytapzoapp.utils.Utils;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.axovel.mytapzoapp.R.drawable.marker;
import static com.axovel.mytapzoapp.utils.Constants.BASE_URL;
import static com.axovel.mytapzoapp.utils.Constants.DROP_LAT;
import static com.axovel.mytapzoapp.utils.Constants.DROP_LNG;
import static com.axovel.mytapzoapp.utils.Constants.LUX_CAB_CATEGORY;
import static com.axovel.mytapzoapp.utils.Constants.MICRO_CAB_CATEGORY;
import static com.axovel.mytapzoapp.utils.Constants.MINI_CAB_CATEGORY;
import static com.axovel.mytapzoapp.utils.Constants.PICKUP_LAN;
import static com.axovel.mytapzoapp.utils.Constants.PICKUP_LAT;
import static com.axovel.mytapzoapp.utils.Constants.PRIME_CAB_CATEGORY;
import static com.axovel.mytapzoapp.utils.Constants.SEDAN_CAB_CATEGORY;
import static com.axovel.mytapzoapp.utils.Constants.SHARE_CAB_CATEGORY;
import static com.axovel.mytapzoapp.utils.Constants.SUV_CAB_CATEGORY;

public class ConfirmationScreenActivity extends AppCompatActivity implements Constants, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {


    private TextView pickUpText, destText;
    private String pickupPoint, dropPoint;
    private TextView estimateTimeText;
    private LatLng toPosition, fromPosition;
    private String categry;
    private TextView cabName, cabEstimatefare;
    private ImageView cabIcon;
    CabDataList cabDataList;
    private GoogleMap mMap;
    private String image;
    private String fare;
    private String cabType;
    private int REQUEST_CODE_AUTOCOMPLETE_DESTINATION = 1;
    private int REQUEST_CODE_AUTOCOMPLETE_PICKUP = 2;
    private final int REQUEST_CHECK_SETTINGS = 1;
    private int PLACE_PICKER_REQUEST = 444;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest locationRequest;
    LatLng latLng, from_position, to_position;
    private double pickUP_latitude;
    private double pickUP_longitude;
    private double destination_latitude;
    private double destination_longitude;
    private List<CabDataList> data = new ArrayList<>();
    private LinearLayout chairSeatLayout, bannerLayout;
    private TextView seatText;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_screen);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        initReferance();
        buildGoogleApiClient();


        createLocationRequest();
        turnOnGpsForCurrentLocation();


        chairSeatLayout.setVisibility(View.GONE);
        bannerLayout.setVisibility(View.GONE);

        if(getIntent().getStringExtra(CAB_DISPLAY_NAME).equals("Share")){
            chooseSeatFun(view);

            chairSeatLayout.setVisibility(View.VISIBLE);
            bannerLayout.setVisibility(View.VISIBLE);
        }
//           setData(from_position.latitude,from_position.longitude,to_position.latitude,to_position.longitude,cabType);


    }


    private void initReferance() {


        pickUpText = (TextView) findViewById(R.id.pickUpText);
        destText = (TextView) findViewById(R.id.destinationText);
        pickupPoint = getIntent().getStringExtra(PICK_UP_TEXT);
        dropPoint = getIntent().getStringExtra(DROP_TEXT);

        pickUpText.setText(pickupPoint);
        destText.setText(dropPoint);
        fromPosition = getIntent().getParcelableExtra(FROMLATLNG);
        toPosition = getIntent().getParcelableExtra(TOLATLNG);
        image = getIntent().getStringExtra(CAB_IMAGE);
        fare = getIntent().getStringExtra(CAB_FARE);
        cabType = getIntent().getStringExtra(CAB_TYPE);


        cabName = (TextView) findViewById(R.id.carNameText);
        cabEstimatefare = (TextView) findViewById(R.id.fordesEstimatedfare);
        cabIcon = (ImageView) findViewById(R.id.bookCarIcon);
        estimateTimeText = (TextView) findViewById(R.id.estimatetime);
        cabName.setText(cabType);
        cabEstimatefare.setText(fare);

        Glide.with(getApplicationContext()).load(image).into(cabIcon);
        estimateTimeText.setText(getIntent().getStringExtra(CAB_ESTIMATE_TIME));
        pickUP_latitude = fromPosition.latitude;
        Log.d("pick", "" + pickUP_latitude);
        pickUP_longitude = fromPosition.longitude;
        Log.d("picklng", "" + pickUP_longitude);
        destination_latitude = toPosition.latitude;
        destination_longitude = toPosition.longitude;
        Log.d("droplng", "" + destination_latitude);
        Log.d("droplng", "" + destination_longitude);
        chairSeatLayout = (LinearLayout) findViewById(R.id.seatsLinear);
        bannerLayout = (LinearLayout) findViewById(R.id.paymentLinearmethod);
        cabName.setText(getIntent().getStringExtra(DISPLAY_CAB_NAME));
        seatText= (TextView) findViewById(R.id.seatsText);

//        setData(pickUP_latitude,pickUP_longitude,destination_latitude,destination_longitude,cabType);


    }

    private void buildGoogleApiClient() {


        mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();
    }

    private void createLocationRequest() {

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);


    }

    private void turnOnGpsForCurrentLocation() {

        LocationSettingsRequest.Builder locationSettingsRequestBuilder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        locationSettingsRequestBuilder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, locationSettingsRequestBuilder.build());
        result.setResultCallback(mResultCallbackFromSettings);
    }

    private ResultCallback<LocationSettingsResult> mResultCallbackFromSettings = new ResultCallback<LocationSettingsResult>() {
        @Override
        public void onResult(LocationSettingsResult result) {

            final Status status = result.getStatus();
            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    // All location settings are satisfied. The client can initialize location
                    // requests here.
                    Log.d("ResultCallback", "LocationSettingsStatusCodes.SUCCESS....");
                    break;
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    // Location settings are not satisfied. But could be fixed by showing the user
                    // a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        Log.d("ResultCallback", "LocationSettingsStatusCodes.RESOLUTION_REQUIRED....");
                        status.startResolutionForResult(ConfirmationScreenActivity.this, REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException e) {
                        // Ignore the error.
                    }
                    break;
                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    Log.d("ResultCallback", "Settings change unavailable. We have no way to fix the settings so we won't show the dialog.");
                    break;
            }
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setPadding(0, 0, 0, Utils.dpToPx(getApplicationContext(), 65));
        mMap.addMarker(new MarkerOptions().position(toPosition).snippet("").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_map_icon)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(toPosition, 16f));

    }


    private void setData(double pick_latitude, double pick_longitude, double drop_latitude, double drop_longitude, String cabCategry) {

        Log.d("WELCOMEPICK", "" + pick_latitude);
        Log.d("WELCOMEPpiclan", "" + pick_longitude);
        Log.d("WELCOMEdes", "" + drop_latitude);
        Log.d("WELCOMEPICK", "" + drop_longitude);
//        Log.d("WELCOMECATEGRY",cabCategry);


        String url = BASE_URL + PICKUP_LAT + pick_latitude + PICKUP_LAN + pick_longitude + DROP_LAT + drop_latitude + DROP_LNG + drop_longitude + "mini";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.d("welcome  ", response);
                try {
                    Object json = new JSONTokener(response).nextValue();

                    JSONObject jsonObject = (JSONObject) json;
                    JSONArray jsonArray = jsonObject.getJSONArray("categories");

                    JSONArray jsonArray1 = jsonObject.getJSONArray("ride_estimate");


                    JSONObject dataObj = jsonArray1.getJSONObject(0);
                    JSONObject catObj = jsonArray.getJSONObject(0);
                    int minimum = dataObj.has("amount_min") ? dataObj.getInt("amount_min") : 0;
//                    cabDataList.setMinimumFare(minimum);
                    int maximum = dataObj.has("amount_max") ? dataObj.getInt("amount_max") : 0;
//                    cabDataList.setMaximumFare(maximum);
                    Log.d("aaaaaaaaaaaaminimum", "" + minimum);
                    Log.d("maximum", "" + maximum);

                    String dNmae = catObj.has("display_name") ? catObj.getString("display_name") : "";
//                    cabDataList.setDisplayNmae(dNmae);

                    String ava = catObj.has("ride_later_enabled") ? catObj.getString("ride_later_enabled") : "";
//                    cabDataList.setIsCheck(ava);

                    String dimg = catObj.has("image") ? catObj.getString("image") : "";
//                    cabDataList.setCar_icon(dimg);

                    int estimateTime = catObj.has("eta") ? catObj.getInt("eta") : 0;
//                    cabDataList.setEstimateTime(estimateTime);

                    cabDataList = new CabDataList(dNmae, ava, estimateTime, maximum, minimum, dimg, WITH_DESTINATION_CODE);
                    data.add(cabDataList);
                    setDataOnScreen();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                CabDetailsRecyclerViewAdapter cabDetailsRecyclerViewAdapter = new CabDetailsRecyclerViewAdapter((ArrayList<CabDataList>) data, getApplication());
//
//                recyclerView.setAdapter(cabDetailsRecyclerViewAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // Log.d("error ",error.getLocalizedMessage());

//                MyProgressDialog.hidePDialog();
//                if(SHARE_CAB_CATEGORY.equals(categorys))
//                manageDataSetShare();
                Log.d("Volley..,.", "ErrorListener: " + error.getMessage());
//
//                Toast.makeText(getApplicationContext(), R.string.someting_went_wrong, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String credentials = "cashgainapp@gmail.com:Anchal09@";
                String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Content-Type", "application/json");
                headers.put("X-APP-TOKEN", "317d32c3ae534f39a911551538e50002");
                headers.put("Authorization", auth);
                return headers;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringRequest);


    }

    private void setDataOnScreen() {

        cabName = (TextView) findViewById(R.id.carNameText);
        cabEstimatefare = (TextView) findViewById(R.id.fordesEstimatedfare);
        cabIcon = (ImageView) findViewById(R.id.bookCarIcon);
        estimateTimeText = (TextView) findViewById(R.id.estimatetime);
        cabName.setText(cabDataList.getDisplayNmae());
        cabEstimatefare.setText("₹" + cabDataList.getMinimumFare() + "-" + cabDataList.getMaximumFare());

        Picasso.with(getApplicationContext()).load(cabDataList.getCar_icon()).into(cabIcon);
        estimateTimeText.setText(cabDataList.getEstimateTime() + "mins");


    }


    public void rateCardFun(View view) {


//        final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
//        dialog.setContentView(R.layout.rate_card_layout);
////        dialog.getWindow().getAttributes().windowAnimations = R.style.dialogAnimation;
//
////        ImageView backButton = (ImageView) dialog.findViewById(R.id.backButton);
//        TextView baseFare = (TextView) dialog.findViewById(R.id.baseFareRatetitle);
//        TextView basefareCalculation = (TextView) dialog.findViewById(R.id.fareRatePerhour);
//
//        baseFare.setText("");
//        basefareCalculation.setText("");
//
//
//        dialog.show();

        AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmationScreenActivity.this);
        LayoutInflater inflater = (ConfirmationScreenActivity.this).getLayoutInflater();
        View v=inflater.inflate(R.layout.rate_card_layout, null);

        builder.setView(v);
        final AlertDialog alertDialog=builder.create();




        alertDialog.show();


    }

    public void fareCalculatorFun(View view) {
        Toast.makeText(getApplicationContext(), "Under Progress.....", Toast.LENGTH_SHORT).show();
    }

    public void applyCoupanFun(View view) {
        Toast.makeText(getApplicationContext(), "Under Progress.....", Toast.LENGTH_SHORT).show();
    }

    public void bookLaterFun(View view) {
        Toast.makeText(getApplicationContext(), "Under Progress.....", Toast.LENGTH_SHORT).show();
    }

    public void bookNowFun(View view) {

        Intent intent = new Intent(getApplicationContext(), CabOntheWayScreenActivity.class);

//        Log.d("LAT_LONG", )

        intent.putExtra(FROMLATLNG, fromPosition);
        intent.putExtra(TOLATLNG, toPosition);
        String pickUPaddress = pickUpText.getText().toString().trim();
        intent.putExtra(PICK_UP_TEXT, pickUPaddress);
        String droopPoint = destText.getText().toString().trim();
        intent.putExtra(DROP_TEXT, droopPoint);
        intent.putExtra("CAB_DISPLAY_NAME", cabName.getText().toString().trim());

        startActivity(intent);
    }

    public void backButton(View view) {
//        Intent intent=new Intent(getApplicationContext(),Cab_Details_Activity.class);
//        startActivity(intent);
        finish();
    }

    public void destinationFun(View view) {
        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(this);
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE_DESTINATION);
        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

//            Log.e(TAG, message);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }


    public void pickupFun(View view) {
        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(this);
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE_PICKUP);
        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

//            Log.e(TAG, message);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLACE_PICKER_REQUEST) {

            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();

//                pickUpText.setText(toastMsg);
                pickUpText.setVisibility(View.VISIBLE);

            }
        }

        // Check that the result was from the autocomplete widget.
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE_DESTINATION) {
            if (resultCode == RESULT_OK) {
                // Get the user's selected place from the Intent.
                Place place = PlaceAutocomplete.getPlace(this, data);
                double toLatitude = place.getLatLng().latitude;
                Log.d("priyesh lotitude", "" + toLatitude);
                double toLongitude = place.getLatLng().longitude;
                Log.d("priyesh longitude", "" + toLongitude);
                destination_latitude = toLatitude;
                destination_longitude = toLongitude;
                to_position = new LatLng(destination_latitude, destination_longitude);
//                Log.i(TAG, "Place Selected: " + place.getName());
                String address = (String) place.getAddress();
                String locality = (String) place.getId();
                String resource = (String) place.getAttributions();
                dropPoint = address + locality + resource;

                // Format the place's details and display them in the TextView.
                destText.setText(address);
                setData(pickUP_latitude, pickUP_longitude, toLatitude, toLongitude, cabType);
                CharSequence attributions = place.getAttributions();
                if (!TextUtils.isEmpty(attributions)) {
//                    destinationText.setText(Html.fromHtml(attributions.toString()));
//                } else {
//                    destinationText.setText("");
                }
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
//                Log.e(TAG, "Error: Status = " + status.toString());
            } else if (resultCode == RESULT_CANCELED) {
                // Indicates that the activity closed before a selection was made. For example if
                // the user pressed the back button.
            }
        }
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {

                // All required changes were successfully made
                if (mGoogleApiClient.isConnected()) {
                    Log.d("onActivityResult", "resultCode == RESULT_OK....");
                }

            } else if (resultCode == RESULT_CANCELED) {
                // The user was asked to change settings, but chose not to
                openAlertDialog(R.string.gps_settings_disabled, R.string.choose_yes_on_dialog, requestForLocation);
                Log.d("onActivityResult", "resultCode == RESULT_CANCELED....");
            }
        }
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE_PICKUP) {
            if (resultCode == RESULT_OK) {
                // Get the user's selected place from the Intent.
                Place place = PlaceAutocomplete.getPlace(this, data);
                double toLatitude = place.getLatLng().latitude;
                double toLongitude = place.getLatLng().longitude;
                LatLng latLng = new LatLng(toLatitude, toLongitude);
                from_position = latLng;

                pickUP_latitude = toLatitude;
                pickUP_longitude = toLongitude;
                from_position = new LatLng(pickUP_latitude, pickUP_longitude);

                String address = (String) place.getAddress();
                String locality = (String) place.getId();
                String resource = (String) place.getAttributions();

                // Format the place's details and display them in the TextView.
//                destinationText.setText(formatPlaceDetails(getResources(), place.getName(),
//                        place.getId(), place.getAddress(), place.getPhoneNumber(),
//                        place.getWebsiteUri()));
                pickUpText.setText(address);
                setData(toLatitude, toLongitude, destination_latitude, destination_longitude, cabType);

                if (destText.getText().toString().trim().isEmpty()) {


                } else {


                }

                // Display attributions if required.
                CharSequence attributions = place.getAttributions();
                if (!TextUtils.isEmpty(attributions)) {
//                    destinationText.setText(Html.fromHtml(attributions.toString()));
//                } else {
//                    destinationText.setText("");
                }
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
//                Log.e(TAG, "Error: Status = " + status.toString());
            } else if (resultCode == RESULT_CANCELED) {
                // Indicates that the activity closed before a selection was made. For example if
                // the user pressed the back button.
            }
        }
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {

                // All required changes were successfully made
                if (mGoogleApiClient.isConnected()) {
                    Log.d("onActivityResult", "resultCode == RESULT_OK....");
                }

            } else if (resultCode == RESULT_CANCELED) {
                // The user was asked to change settings, but chose not to
                openAlertDialog(R.string.gps_settings_disabled, R.string.choose_yes_on_dialog, requestForLocation);
                Log.d("onActivityResult", "resultCode == RESULT_CANCELED....");
            }
        }
    }

    private String requestForMarsh = "forMarsh";
    private String requestForLocation = "forLocation";

    private void openAlertDialog(final int title, int message, final String from) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmationScreenActivity.this);

        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                //                locatingTextView.setVisibility(View.GONE);

                if (from.equals(requestForLocation)) {
                    turnOnGpsForCurrentLocation();
                } else if (from.equals(requestForMarsh)) {
                    ActivityCompat.requestPermissions(ConfirmationScreenActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                }
            }
        });

        builder.show();
    }

    public void rideLaterFun(View view) {

        Intent intent = new Intent(ConfirmationScreenActivity.this, RideLaterActivity.class);
        startActivity(intent);

    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void chooseSeatFun(View view) {

//        final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
//        dialog.setContentView(R.layout.share_layout);
////        dialog.getWindow().getAttributes().windowAnimations = R.style.dialogAnimation;
//
////        ImageView backButton = (ImageView) dialog.findViewById(R.id.backButton);
//        final TextView shareTextOne = (TextView) dialog.findViewById(R.id.shareText1);
//        TextView shareTextTwo = (TextView) dialog.findViewById(R.id.shareText2);
//        shareTextOne.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                seatText.setText("1 Seat");
//                dialog.dismiss();
//
//            }
//        });
//        shareTextTwo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                seatText.setText("2 Seat");
//                dialog.dismiss();
//
//            }
//        });
//        dialog.show();
        AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmationScreenActivity.this);
        LayoutInflater inflater = (ConfirmationScreenActivity.this).getLayoutInflater();
        View v=inflater.inflate(R.layout.share_layout, null);
        final TextView shareTextOne = (TextView) v.findViewById(R.id.shareText1);
        final TextView shareTextTwo = (TextView) v.findViewById(R.id.shareText2);
        builder.setView(v);
        final AlertDialog alertDialog=builder.create();
        shareTextOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Log.d("welcome",shareTextOne.getText().toString().trim());
                alertDialog.cancel();

            }
        });
        shareTextTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("welcome",shareTextTwo.getText().toString().trim());
                alertDialog.cancel();


            }
        });


        alertDialog.show();






    }

    public void showPaymentNotificationFun(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmationScreenActivity.this);
        LayoutInflater inflater = (ConfirmationScreenActivity.this).getLayoutInflater();
        View v=inflater.inflate(R.layout.payment_method_layout, null);

        builder.setView(v);
        final AlertDialog alertDialog=builder.create();




        alertDialog.show();






    }


}
