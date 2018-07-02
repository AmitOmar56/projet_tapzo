package com.axovel.mytapzoapp.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;

import com.axovel.mytapzoapp.utils.Constants;
import com.axovel.mytapzoapp.utils.PreferenceStore;
import com.uber.sdk.android.core.auth.LoginManager;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
import com.axovel.mytapzoapp.customAdapter.CabDetailsPagerAdapter;
import com.axovel.mytapzoapp.models.CabDataList;
import com.axovel.mytapzoapp.utils.MyProgressDialog;
import com.axovel.mytapzoapp.utils.Utils;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.uber.sdk.android.core.utils.Preconditions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static android.graphics.Color.rgb;
import static com.axovel.mytapzoapp.utils.Constants.BASE_URL;
import static com.axovel.mytapzoapp.utils.Constants.DISPLAY_CAB_NAME;
import static com.axovel.mytapzoapp.utils.Constants.DROP_LAT;
import static com.axovel.mytapzoapp.utils.Constants.DROP_LNG;
import static com.axovel.mytapzoapp.utils.Constants.DROP_TEXT;
import static com.axovel.mytapzoapp.utils.Constants.FROMLATLNG;
import static com.axovel.mytapzoapp.utils.Constants.LUX_CAB_CATEGORY;
import static com.axovel.mytapzoapp.utils.Constants.MICRO_CAB_CATEGORY;
import static com.axovel.mytapzoapp.utils.Constants.MINI_CAB_CATEGORY;
import static com.axovel.mytapzoapp.utils.Constants.PICKUP_LAN;
import static com.axovel.mytapzoapp.utils.Constants.PICKUP_LAT;
import static com.axovel.mytapzoapp.utils.Constants.PICK_UP_TEXT;
import static com.axovel.mytapzoapp.utils.Constants.PRIME_CAB_CATEGORY;
import static com.axovel.mytapzoapp.utils.Constants.SEATS;
import static com.axovel.mytapzoapp.utils.Constants.SEDAN_CAB_CATEGORY;
import static com.axovel.mytapzoapp.utils.Constants.SHARE_CAB_CATEGORY;
import static com.axovel.mytapzoapp.utils.Constants.SUV_CAB_CATEGORY;
import static com.axovel.mytapzoapp.utils.Constants.TOLATLNG;
import static com.axovel.mytapzoapp.utils.Constants.WITHOUT_DESTINATION_CODE;
import static com.axovel.mytapzoapp.utils.Constants.WITH_DESTINATION_CODE;

public class Cab_Details_Activity extends FragmentActivity implements Constants, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {


    private GoogleMap mMap;
    int currentPage;
    private TextView pickUpText, destText;

    private static final String TAG = null;
    private int seates;

    private LinearLayout drawerMenuView;
    private DrawerLayout drawerLayout;
    private String pickupPoint, dropPoint;
    private int REQUEST_CODE_AUTOCOMPLETE_DESTINATION = 1;
    private int REQUEST_CODE_AUTOCOMPLETE_PICKUP = 2;
    private float ZOOM_DEFAULT = 16f;
    private int screenWidth, screenHeight;
    private final int REQUEST_CHECK_SETTINGS = 1;
    private Marker marker;
    private TextView destinationText, place_ArtibuteText, dropTextView, pickupTextview, currentLocTextView;
    private ImageView dropIconImage;
    private String address1 = "";
    private String address2 = "";
    private String city = "";
    private String country = "";
    private String pinCode = "";

    private int PLACE_PICKER_REQUEST = 444;
    String address;
    LatLng latLng, from_position, to_position;

    private AsyncTask asyncTask = null;
    private LocationRequest locationRequest;
    private GoogleApiClient mGoogleApiClient;
    private boolean isLocated = false;
    private GoogleMap.OnCameraIdleListener onCameraIdleListener;
    View v;
    private double pickUP_latitude;
    private double pickUP_longitude;
    private double destination_latitude;
    private double destination_longitude;
    private LinearLayout olaLinear, uberLinear;
    CabDataList cabDataList;
    List<CabDataList> arrayList = new ArrayList<>();
    PagerAdapter pagerAdapter;
    ViewPager viewPager;
    CabDetailsPagerAdapter cabDetailsPagerAdapter;
    private String userName, userPassword;
    private List<CabDataList> data = new ArrayList<>();
    private SwipeRefreshLayout mSwipeRefreshLayout;

    LoginManager loginManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cab__details_);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        MultiDex.install(this);

        viewPager = (ViewPager) findViewById(R.id.pagercabdetails);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        configureCameraIdle();
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        ininRefrence();
        buildGoogleApiClient();


        createLocationRequest();
        turnOnGpsForCurrentLocation();

//        olaLinear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                viewPager.setCurrentItem(+1, true);
//
//            }
//        });
//        uberLinear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                viewPager.setCurrentItem(-1, true);
//            }
//        });
//        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
////                if(position==0){
////                    Log.d("Welcome","satisj");
////                }
////                if(position==1){
////                    Log.d("Welcome","home");
////                }
//
//            }

//            @Override
//            public void onPageSelected(int position) {
//
//                if (position == 0) {
//                    Log.d("Welcome", "satisj");
//                    uberLinear.setBackgroundColor(rgb(3, 89, 193));
//                    olaLinear.setBackgroundColor(rgb(247, 247, 247));
//                }
//                if (position == 1) {
//
//
//                    olaLinear.setBackgroundColor(rgb(3, 89, 193));
//                    uberLinear.setBackgroundColor(rgb(247, 247, 247));
//
//
//                }
//
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });


    }


    private void uploadUrlRideAvaility(double pick_latitude, double pick_longitude, final String categorys) {

        data.clear();

        MyProgressDialog.showPDialog(Cab_Details_Activity.this);
        String logingUrl = "olacabs://app/launch?lat=" + pick_latitude + "&lng=" + pick_longitude + "&category=mini&utm_source=317d32c3ae534f39a911551538e50002&landing_page=bk&drop_lat=28.637377&drop_lng=77.101444";

        Log.d("welcome  ....", "" + pick_latitude);
        Log.d("lonnnnn....", "" + pick_longitude);

        String url = BASE_URL + PICKUP_LAT + pick_latitude + PICKUP_LAN + pick_longitude + categorys;


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                MyProgressDialog.hidePDialog();

                int maxLogSize = 300;
                for (int i = 0; i <= response.length() / maxLogSize; i++) {
                    int start = i * maxLogSize;
                    int end = (i + 1) * maxLogSize;
                    end = end > response.length() ? response.length() : end;
                    Log.d("Response data >>>", response.substring(start, end));
                }


                Log.d("welcome  ", response);
                try {
                    Object json = new JSONTokener(response).nextValue();

                    JSONObject jsonObject = (JSONObject) json;

                    JSONArray jsonArray = jsonObject.getJSONArray("categories");


                    JSONObject dataObj = jsonArray.getJSONObject(0);
                    JSONArray fareArray = dataObj.getJSONArray("fare_breakup");
                    JSONObject faredataObj = fareArray.getJSONObject(0);
                    String s = dataObj.getString("id");
                    Log.d("aaaaaaaaaaaa", s);

                    String dNmae = dataObj.has("display_name") ? dataObj.getString("display_name") : "";
//                    cabDataList.setDisplayNmae(dNmae);

                    String ava = dataObj.has("ride_later_enabled") ? dataObj.getString("ride_later_enabled") : "";
//                    cabDataList.setIsCheck(ava);

                    String dimg = dataObj.has("image") ? dataObj.getString("image") : "";
//                    cabDataList.setCar_icon(dimg);

                    int estimateTime = dataObj.has("eta") ? dataObj.getInt("eta") : 0;
//                    cabDataList.setEstimateTime(estimateTime);

                    int cost = faredataObj.has("cost_per_distance") ? faredataObj.getInt("cost_per_distance") : 0;
//                    cabDataList.setCostPerDistance(cost);


                    CabDataList cabDataList = new CabDataList(dNmae, ava, dimg, estimateTime, cost, WITHOUT_DESTINATION_CODE);
                    data.add(cabDataList);


                } catch (JSONException e) {
                    e.printStackTrace();
//                    Toast.makeText(getApplicationContext(), R.string.unable_to_fetch_data, Toast.LENGTH_SHORT).show();
                }
                manageRecyclerViewWithViewPager();

                MyProgressDialog.hidePDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // Log.d("error ",error.getLocalizedMessage());

                MyProgressDialog.hidePDialog();
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
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Content-Type", "application/json");
                headers.put("X-APP-TOKEN", "317d32c3ae534f39a911551538e50002");
                headers.put("Authorization", auth);
                return headers;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringRequest);

    }

    private void manageRecyclerViewWithViewPager() {


        final String loginToken = PreferenceStore.getPreference(getApplicationContext()).getString(OLALOGIN_URL);


        cabDetailsPagerAdapter = new CabDetailsPagerAdapter(getApplicationContext(), data) {


            @Override
            protected void onLOginPopup2(int position, final String cabType, final String estimateTime, final String cabImage, final String baseFare, final String display_Name) {


                Log.d("share DisplayNAMe", display_Name);

                if (destination_longitude==0) {
                    Toast.makeText(getApplicationContext(), "Please Enter drop Location", Toast.LENGTH_SHORT).show();
                } else {

                    if (!display_Name.equals("Share")) {


                        if (loginToken.isEmpty()) {
                            Log.d("loginToken Cabdetalis activity", loginToken);

                            Intent intent = new Intent(Cab_Details_Activity.this, OlaLoginWebViewDilogActivity.class);
                            intent.putExtra(CAB_TYPE, cabType);
                            intent.putExtra(CAB_FARE, baseFare);
                            intent.putExtra(CAB_IMAGE, cabImage);
                            intent.putExtra(PICK_UP_TEXT, pickUpText.getText().toString().trim());
                            intent.putExtra(DROP_TEXT, destText.getText().toString().trim());
                            intent.putExtra(FROMLATLNG, from_position);
                            intent.putExtra(CAB_ESTIMATE_TIME, estimateTime);
                            intent.putExtra(CAB_DISPLAY_NAME, display_Name);
                            intent.putExtra(TOLATLNG, to_position);

                            Log.d("cabtuupe", "cabType");
                            startActivity(intent);

                        } else {

                            Log.d("loginToken Cabdetalis activity", loginToken);


                            Intent intent = new Intent(getApplicationContext(), ConfirmationScreenActivity.class);
                            intent.putExtra(CAB_TYPE, cabType);
                            intent.putExtra(CAB_FARE, baseFare);
                            intent.putExtra(CAB_IMAGE, cabImage);
                            intent.putExtra(PICK_UP_TEXT, pickUpText.getText().toString().trim());
                            intent.putExtra(DROP_TEXT, destText.getText().toString().trim());
                            intent.putExtra(FROMLATLNG, from_position);
                            intent.putExtra(CAB_ESTIMATE_TIME, estimateTime);
                            intent.putExtra(TOLATLNG, to_position);
                            intent.putExtra(CAB_DISPLAY_NAME, display_Name);
                            Log.d("cabtuupe", "cabType");
                            startActivity(intent);

                        }


                    } else {


                        if (loginToken.isEmpty()) {


//                            final Dialog dialog = new Dialog(getApplicationContext(), android.R.style.Theme_Translucent_NoTitleBar);
//                            dialog.setContentView(R.layout.share_layout);
//                            TextView shareTextOne = (TextView) dialog.findViewById(R.id.shareText1);
//                            TextView shareTextTwo = (TextView) dialog.findViewById(R.id.shareText2);
//
//                            shareTextOne.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//
//                                    if (loginToken.isEmpty()) {
//                                        Log.d("loginToken Cabdetalis activity", loginToken);
//
//                                        Intent intent = new Intent(Cab_Details_Activity.this, OlaLoginWebViewDilogActivity.class);
//                                        intent.putExtra(CAB_TYPE, cabType);
//                                        intent.putExtra(CAB_FARE, baseFare);
//                                        intent.putExtra(CAB_IMAGE, cabImage);
//                                        intent.putExtra(PICK_UP_TEXT, pickUpText.getText().toString().trim());
//                                        intent.putExtra(DROP_TEXT, destText.getText().toString().trim());
//                                        intent.putExtra(FROMLATLNG, from_position);
//                                        intent.putExtra(CAB_ESTIMATE_TIME, estimateTime);
//                                        intent.putExtra(CAB_DISPLAY_NAME, display_Name);
//                                        intent.putExtra(TOLATLNG, to_position);
//                                        intent.putExtra(SHARE_SEATE, 1);
//
//                                        Log.d("cabtuupe", "cabType");
//                                        startActivity(intent);
//
//                                    } else {
//
//                                        Log.d("loginToken Cabdetalis activity", loginToken);
//
//
//                                        Intent intent = new Intent(getApplicationContext(), ConfirmationScreenActivity.class);
//                                        intent.putExtra(CAB_TYPE, cabType);
//                                        intent.putExtra(CAB_FARE, baseFare);
//                                        intent.putExtra(CAB_IMAGE, cabImage);
//                                        intent.putExtra(PICK_UP_TEXT, pickUpText.getText().toString().trim());
//                                        intent.putExtra(DROP_TEXT, destText.getText().toString().trim());
//                                        intent.putExtra(FROMLATLNG, from_position);
//                                        intent.putExtra(CAB_ESTIMATE_TIME, estimateTime);
//                                        intent.putExtra(TOLATLNG, to_position);
//                                        intent.putExtra(CAB_DISPLAY_NAME, display_Name);
//                                        intent.putExtra(SHARE_SEATE, 2);
//                                        Log.d("cabtuupe", "cabType");
//                                        startActivity(intent);
//
//                                    }
//
//
//                                }
//                            });
//                            shareTextTwo.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//
//                                }
//                            });
//
//
//                            dialog.show();

                            AlertDialog.Builder builder = new AlertDialog.Builder(Cab_Details_Activity.this);
                            LayoutInflater inflater = (Cab_Details_Activity.this).getLayoutInflater();
                            View v=inflater.inflate(R.layout.share_layout, null);
                            final TextView shareTextOne = (TextView) v.findViewById(R.id.shareText1);
                            final TextView shareTextTwo = (TextView) v.findViewById(R.id.shareText2);
                            builder.setView(v);
                            final AlertDialog alertDialog=builder.create();
                            shareTextOne.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    Intent intent = new Intent(Cab_Details_Activity.this, OlaLoginWebViewDilogActivity.class);
                                        intent.putExtra(CAB_TYPE, cabType);
                                        intent.putExtra(CAB_FARE, baseFare);
                                        intent.putExtra(CAB_IMAGE, cabImage);
                                        intent.putExtra(PICK_UP_TEXT, pickUpText.getText().toString().trim());
                                        intent.putExtra(DROP_TEXT, destText.getText().toString().trim());
                                        intent.putExtra(FROMLATLNG, from_position);
                                        intent.putExtra(CAB_ESTIMATE_TIME, estimateTime);
                                        intent.putExtra(CAB_DISPLAY_NAME, display_Name);
                                        intent.putExtra(TOLATLNG, to_position);
                                        intent.putExtra(SHARE_SEATE, 1);

                                        Log.d("cabtuupe", "cabType");
                                        startActivity(intent);
                                    alertDialog.cancel();

                                }
                            });
                            shareTextTwo.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getApplicationContext(), ConfirmationScreenActivity.class);
                                        intent.putExtra(CAB_TYPE, cabType);
                                        intent.putExtra(CAB_FARE, baseFare);
                                        intent.putExtra(CAB_IMAGE, cabImage);
                                        intent.putExtra(PICK_UP_TEXT, pickUpText.getText().toString().trim());
                                        intent.putExtra(DROP_TEXT, destText.getText().toString().trim());
                                        intent.putExtra(FROMLATLNG, from_position);
                                        intent.putExtra(CAB_ESTIMATE_TIME, estimateTime);
                                        intent.putExtra(TOLATLNG, to_position);
                                        intent.putExtra(CAB_DISPLAY_NAME, display_Name);
                                        intent.putExtra(SHARE_SEATE, 2);
                                        Log.d("cabtuupe", "cabType");
                                        startActivity(intent);
                                    alertDialog.cancel();


                                }
                            });


                            alertDialog.show();

                        } else {
                            Intent intent = new Intent(getApplicationContext(), ConfirmationScreenActivity.class);
                            intent.putExtra(CAB_TYPE, cabType);
                            intent.putExtra(CAB_FARE, baseFare);
                            intent.putExtra(CAB_IMAGE, cabImage);
                            intent.putExtra(PICK_UP_TEXT, pickUpText.getText().toString().trim());
                            intent.putExtra(DROP_TEXT, destText.getText().toString().trim());
                            intent.putExtra(FROMLATLNG, from_position);
                            intent.putExtra(CAB_ESTIMATE_TIME, estimateTime);
                            intent.putExtra(TOLATLNG, to_position);
                            intent.putExtra(CAB_DISPLAY_NAME, display_Name);
                            intent.putExtra(SHARE_SEATE, 2);
                            Log.d("cabtuupe", "cabType");
                            startActivity(intent);


                        }


                    }


                }
            }
        };
        viewPager.setAdapter(cabDetailsPagerAdapter);


    }

    private void showVirtualKeyboard() {

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                InputMethodManager m = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                if (m != null) {
                    // m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    m.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
                }
            }

        }, 100);
    }


    private void ininRefrence() {

        olaLinear = (LinearLayout) findViewById(R.id.olaLogo);
//        uberLinear = (LinearLayout) findViewById(R.id.uberLogo);

        pickUpText = (TextView) findViewById(R.id.pickUpText);
        destText = (TextView) findViewById(R.id.destinationText);


    }

    private void turnOnGpsForCurrentLocation() {

        LocationSettingsRequest.Builder locationSettingsRequestBuilder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        locationSettingsRequestBuilder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, locationSettingsRequestBuilder.build());
        result.setResultCallback(mResultCallbackFromSettings);
    }

    private void createLocationRequest() {

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);


    }

    private void buildGoogleApiClient() {


        mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();
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
                        status.startResolutionForResult(Cab_Details_Activity.this, REQUEST_CHECK_SETTINGS);
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


    public void configureCameraIdle() {


        onCameraIdleListener = new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {


                latLng = mMap.getCameraPosition().target;
                Geocoder geocoder = new Geocoder(Cab_Details_Activity.this);

                try {
                    List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    pickUP_latitude = latLng.latitude;
                    pickUP_longitude = latLng.longitude;
                    from_position = new LatLng(pickUP_latitude, pickUP_longitude);
                    if (addressList != null && addressList.size() > 0) {
                        String locality = addressList.get(0).getAddressLine(0);

//                        pinCode = (addressList.get(0).getPostalCode() == null) ? "" : addressList.get(0).getPostalCode().trim();
                        String address2 = (addressList.get(0).getSubAdminArea() == null) ? "" : addressList.get(0).getSubAdminArea().trim();
                        if (!locality.isEmpty() && !country.isEmpty())
                            pickUpText.setText(locality + " " + address2 + " " + city);

                        calldata(pickUP_latitude, pickUP_longitude);


                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };
    }

    private void calldata(double pickUP_latitude, double pickUP_longitude) {


        uploadUrlRideAvaility(pickUP_latitude, pickUP_longitude, MICRO_CAB_CATEGORY);
        uploadUrlRideAvaility(pickUP_latitude, pickUP_longitude, MINI_CAB_CATEGORY);


        uploadUrlRideAvaility(pickUP_latitude, pickUP_longitude, SEDAN_CAB_CATEGORY);
        uploadUrlRideAvaility(pickUP_latitude, pickUP_longitude, PRIME_CAB_CATEGORY);
        uploadUrlRideAvaility(pickUP_latitude, pickUP_longitude, SUV_CAB_CATEGORY);
        uploadUrlRideAvaility(pickUP_latitude, pickUP_longitude, LUX_CAB_CATEGORY);
        uploadShareRideAvaility(pickUP_latitude, pickUP_longitude, SHARE_CAB_CATEGORY);

    }

    private void uploadShareRideAvaility(double pickUP_latitude, double pickUP_longitude, String shareCabCategory) {


        data.clear();

        MyProgressDialog.showPDialog(Cab_Details_Activity.this);

        Log.d("welcome  ....", "" + pickUP_latitude);
        Log.d("lonnnnn....", "" + pickUP_latitude);

        String url = BASE_URL + PICKUP_LAT + pickUP_latitude + PICKUP_LAN + pickUP_longitude + shareCabCategory;


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                MyProgressDialog.hidePDialog();

                int maxLogSize = 300;
                for (int i = 0; i <= response.length() / maxLogSize; i++) {
                    int start = i * maxLogSize;
                    int end = (i + 1) * maxLogSize;
                    end = end > response.length() ? response.length() : end;
                    Log.d("Response data >>>", response.substring(start, end));
                }


                Log.d("welcome  ", response);
                try {
                    Object json = new JSONTokener(response).nextValue();

                    JSONObject jsonObject = (JSONObject) json;

                    JSONArray jsonArray = jsonObject.getJSONArray("categories");


                    JSONObject dataObj = jsonArray.getJSONObject(0);
                    JSONArray fareArray = dataObj.getJSONArray("fare_breakup");
                    JSONObject faredataObj = fareArray.getJSONObject(0);
                    String s = dataObj.getString("id");
                    Log.d("aaaaaaaaaaaa", s);

                    String dNmae = dataObj.has("display_name") ? dataObj.getString("display_name") : "";
//                    cabDataList.setDisplayNmae(dNmae);

                    String ava = dataObj.has("ride_later_enabled") ? dataObj.getString("ride_later_enabled") : "";
//                    cabDataList.setIsCheck(ava);

                    String dimg = dataObj.has("image") ? dataObj.getString("image") : "";
//                    cabDataList.setCar_icon(dimg);

                    int estimateTime = dataObj.has("eta") ? dataObj.getInt("eta") : 0;
//                    cabDataList.setEstimateTime(estimateTime);

                    String costshare = faredataObj.has("fare") ? faredataObj.getString("fare") : "";
//                    cabDataList.setCostPerDistance(cost);


                    CabDataList cabDataList = new CabDataList(dNmae, ava, dimg, estimateTime, costshare, WITHOUT_DESTINATION_CODE);
                    data.add(cabDataList);


                } catch (JSONException e) {
                    e.printStackTrace();
//                    Toast.makeText(getApplicationContext(), R.string.unable_to_fetch_data, Toast.LENGTH_SHORT).show();
                }
                manageRecyclerViewWithViewPager();

                MyProgressDialog.hidePDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // Log.d("error ",error.getLocalizedMessage());

                MyProgressDialog.hidePDialog();
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
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Content-Type", "application/json");
                headers.put("X-APP-TOKEN", "317d32c3ae534f39a911551538e50002");
                headers.put("Authorization", auth);
                return headers;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringRequest);


    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLACE_PICKER_REQUEST) {

            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();

                pickupTextview.setText(toastMsg);
                pickupTextview.setVisibility(View.VISIBLE);

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
                Log.i(TAG, "Place Selected: " + place.getName());
                String address = (String) place.getAddress();
                String locality = (String) place.getId();
                String resource = (String) place.getAttributions();
                dropPoint = address + locality + resource;

                // Format the place's details and display them in the TextView.
//                destinationText.setText(formatPlaceDetails(getResources(), place.getName(),
//                        place.getId(), place.getAddress(), place.getPhoneNumber(),
//                        place.getWebsiteUri()));
                destText.setText(address);

                Log.d("latitude", "" + pickUP_latitude);
                Log.d("longitude", "" + pickUP_longitude);
                uploadShareUrlRideEstimate(pickUP_latitude, pickUP_longitude, toLatitude, toLongitude, SHARE_CAB_CATEGORY);
                uploadUrlRideEstimate(pickUP_latitude, pickUP_longitude, toLatitude, toLongitude, MICRO_CAB_CATEGORY);

                uploadUrlRideEstimate(pickUP_latitude, pickUP_longitude, toLatitude, toLongitude, SEDAN_CAB_CATEGORY);
                uploadUrlRideEstimate(pickUP_latitude, pickUP_longitude, toLatitude, toLongitude, PRIME_CAB_CATEGORY);
                uploadUrlRideEstimate(pickUP_latitude, pickUP_longitude, toLatitude, toLongitude, SUV_CAB_CATEGORY);
                uploadUrlRideEstimate(pickUP_latitude, pickUP_longitude, toLatitude, toLongitude, LUX_CAB_CATEGORY);
                uploadUrlRideEstimate(pickUP_latitude, pickUP_longitude, toLatitude, toLongitude, MINI_CAB_CATEGORY);

                // Display attributions if required.
                CharSequence attributions = place.getAttributions();
                if (!TextUtils.isEmpty(attributions)) {
//                    destinationText.setText(Html.fromHtml(attributions.toString()));
//                } else {
//                    destinationText.setText("");
                }
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.e(TAG, "Error: Status = " + status.toString());
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


                Log.d("latiiiiiiiii", "" + to_position.latitude);
                pickUP_latitude = toLatitude;
                pickUP_longitude = toLongitude;
                from_position = new LatLng(pickUP_latitude, pickUP_longitude);
                Log.i(TAG, "Place Selected: " + place.getName());
                String address = (String) place.getAddress();
                String locality = (String) place.getId();
                String resource = (String) place.getAttributions();

                // Format the place's details and display them in the TextView.
//                destinationText.setText(formatPlaceDetails(getResources(), place.getName(),
//                        place.getId(), place.getAddress(), place.getPhoneNumber(),
//                        place.getWebsiteUri()));
                pickUpText.setText(address);

                if (destText.getText().toString().trim().isEmpty()) {
                    calldata(pickUP_latitude, pickUP_longitude);

                } else {


                    uploadShareUrlRideEstimate(pickUP_latitude, pickUP_longitude, toLatitude, toLongitude, SHARE_CAB_CATEGORY);
                    uploadUrlRideEstimate(pickUP_latitude, pickUP_longitude, toLatitude, toLongitude, MICRO_CAB_CATEGORY);

                    uploadUrlRideEstimate(pickUP_latitude, pickUP_longitude, toLatitude, toLongitude, SEDAN_CAB_CATEGORY);
                    uploadUrlRideEstimate(pickUP_latitude, pickUP_longitude, toLatitude, toLongitude, PRIME_CAB_CATEGORY);
                    uploadUrlRideEstimate(pickUP_latitude, pickUP_longitude, toLatitude, toLongitude, SUV_CAB_CATEGORY);
                    uploadUrlRideEstimate(pickUP_latitude, pickUP_longitude, toLatitude, toLongitude, LUX_CAB_CATEGORY);
                    uploadUrlRideEstimate(pickUP_latitude, pickUP_longitude, toLatitude, toLongitude, MINI_CAB_CATEGORY);

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
                Log.e(TAG, "Error: Status = " + status.toString());
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

    private void uploadShareUrlRideEstimate(double pickUP_latitude, double pickUP_longitude, double toLatitude, double toLongitude, String shareCabCategory) {


        data.clear();

        MyProgressDialog.showPDialog(Cab_Details_Activity.this);

        Log.d("welcome  ....", "" + pickUP_latitude);
        Log.d("lonnnnn....", "" + pickUP_latitude);

        String url = BASE_URL + PICKUP_LAT + pickUP_latitude + PICKUP_LAN + pickUP_longitude + DROP_LAT + toLatitude + DROP_LNG + toLongitude + shareCabCategory;


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                MyProgressDialog.hidePDialog();

                int maxLogSize = 300;
                for (int i = 0; i <= response.length() / maxLogSize; i++) {
                    int start = i * maxLogSize;
                    int end = (i + 1) * maxLogSize;
                    end = end > response.length() ? response.length() : end;
                    Log.d("Response data >>>", response.substring(start, end));
                }


                Log.d("welcome  ", response);
                try {
                    Object json = new JSONTokener(response).nextValue();

                    JSONObject jsonObject = (JSONObject) json;

                    JSONArray jsonArray = jsonObject.getJSONArray("categories");
                    JSONArray jsonArray1 = jsonObject.getJSONArray("ride_estimate");


                    JSONObject dataObjestimate = jsonArray1.getJSONObject(0);
                    JSONArray fareArry = dataObjestimate.getJSONArray("fares");
                    JSONObject fareObj = fareArry.getJSONObject(0);
                    String shareCost = fareObj.getString("cost");


                    JSONObject dataObj = jsonArray.getJSONObject(0);
                    JSONArray fareArray = dataObj.getJSONArray("fare_breakup");
                    JSONObject faredataObj = fareArray.getJSONObject(0);
                    String s = dataObj.getString("id");
                    Log.d("aaaaaaaaaaaa", s);

                    String dNmae = dataObj.has("display_name") ? dataObj.getString("display_name") : "";
//                    cabDataList.setDisplayNmae(dNmae);

                    String ava = dataObj.has("ride_later_enabled") ? dataObj.getString("ride_later_enabled") : "";
//                    cabDataList.setIsCheck(ava);

                    String dimg = dataObj.has("image") ? dataObj.getString("image") : "";
//                    cabDataList.setCar_icon(dimg);

                    int estimateTime = dataObj.has("eta") ? dataObj.getInt("eta") : 0;
//                    cabDataList.setEstimateTime(estimateTime)


                    CabDataList cabDataList = new CabDataList(dNmae, ava, dimg, estimateTime, shareCost, WITH_DESTINATION_CODE);
                    data.add(cabDataList);


                } catch (JSONException e) {
                    e.printStackTrace();
//                    Toast.makeText(getApplicationContext(), R.string.unable_to_fetch_data, Toast.LENGTH_SHORT).show();
                }
                manageRecyclerViewWithViewPager();

                MyProgressDialog.hidePDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // Log.d("error ",error.getLocalizedMessage());

                MyProgressDialog.hidePDialog();
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
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Content-Type", "application/json");
                headers.put("X-APP-TOKEN", "317d32c3ae534f39a911551538e50002");
                headers.put("Authorization", auth);
                return headers;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringRequest);


    }


    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, (com.google.android.gms.location.LocationListener) this);
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setPadding(0, 0, 0, Utils.dpToPx(getApplicationContext(), 65));
        mMap.setOnCameraIdleListener(onCameraIdleListener);
//        mMap.setMyLocationEnabled(true);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

        setPreviousLocationOfUser();
        mMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {

            }
        });


    }

    private void setLatLngAddMarker(LatLng latLng, float zoom) {

        removeLocation();

//        marker = mMap.addMarker(new MarkerOptions().position(latLng)  .snippet("").icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker)));
//        marker.setDraggable(true);
//        currentLocTextView.setVisibility(View.VISIBLE);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));


        setAddressString(latLng);
    }

    private void removeLocation() {
        if (marker != null) {
            marker.setVisible(false);
        }
    }

    private void setPreviousLocationOfUser() {

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location preLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (preLocation == null) {
            preLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        if (preLocation != null) {
            setLatLngAddMarker(new LatLng(preLocation.getLatitude(), preLocation.getLongitude()), ZOOM_DEFAULT);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            Log.d("onLocationChanged", "onLocationChanged....");
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);
            pickUpText.setVisibility(View.GONE);
            isLocated = true;
            setLatLngAddMarker(latLng, ZOOM_DEFAULT);
            stopLocationUpdates();
        }

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("ConnectionCallbacks", "onConnected....");
        // Register to get location updates
        if (!isLocated) {
            startLocationUpdates();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
                                              CharSequence address, CharSequence phoneNumber, Uri websiteUri) {

        return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber, websiteUri));

    }

    private void setAddressString(final LatLng latLng) {

        if (asyncTask != null) {
            asyncTask.cancel(true);
        }

        asyncTask = new AsyncTask<Void, String, String>() {

            @Override
            protected void onPreExecute() {

//                pickupTextview.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(Void... params) {

                try {
                    Geocoder geocoder = new Geocoder(Cab_Details_Activity.this, Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    pickUP_latitude = latLng.latitude;
                    pickUP_longitude = latLng.longitude;
                    LatLng latLng = new LatLng(pickUP_latitude, pickUP_longitude);
                    from_position = latLng;


//                    latString = "" + latLng.latitude;
//                    longString = "" + latLng.longitude;
//                    locality = (addresses.get(0).getAddressLine(0)== null) ? "" : addresses.get(0).getAddressLine(0).trim();;

                    address1 = (addresses.get(0).getAddressLine(0) == null) ? "" : addresses.get(0).getAddressLine(0).trim(); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    //                    address2 = (addresses.get(0).getAddressLine(addresses.get(0).getMaxAddressLineIndex()) == null) ? "" : addresses.get(0).getAddressLine(addresses.get(0).getMaxAddressLineIndex()).trim();
                    address2 = (addresses.get(0).getSubAdminArea() == null) ? "" : addresses.get(0).getSubAdminArea().trim();
                    city = (addresses.get(0).getLocality() == null) ? "" : addresses.get(0).getLocality().trim();
//                    state = (addresses.get(0).getAdminArea() == null) ? "" : addresses.get(0).getAdminArea().trim();
                    country = (addresses.get(0).getCountryName() == null) ? "" : addresses.get(0).getCountryName().trim();
                    pinCode = (addresses.get(0).getPostalCode() == null) ? "" : addresses.get(0).getPostalCode().trim();


                } catch (IOException e) {

                    e.printStackTrace();

                    address1 = "";
                    address2 = "";
                    city = "";
//                    state = "";
                    country = "";
                    pinCode = "";
                }
                String fullAddressString = address1 + ", " + address2 + ", " + city;

                return fullAddressString;
            }

            @Override
            protected void onPostExecute(String s) {

                pickUpText.setText(s);
                uploadUrlRideAvaility(pickUP_latitude, pickUP_longitude, MINI_CAB_CATEGORY);
            }
        }.execute();
    }

    /**
     * Called after the autocomplete activity has finished to return its result.
     */


    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServicesAvailable();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null) {
            stopLocationUpdates();
        }
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    public void checkPlayServicesAvailable() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(Cab_Details_Activity.this);
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(this, status, 1000).show();
            } else {
                Toast.makeText(this, R.string.device_not_supported, Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    protected void stopLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
//                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            }
        }
    }


    private String requestForMarsh = "forMarsh";
    private String requestForLocation = "forLocation";

    private void openAlertDialog(final int title, int message, final String from) {

        AlertDialog.Builder builder = new AlertDialog.Builder(Cab_Details_Activity.this);

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
                    ActivityCompat.requestPermissions(Cab_Details_Activity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                }
            }
        });

        builder.show();
    }


    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mMap.setMyLocationEnabled(true);
                    }
                    //                    if (getIntent().hasExtra(KEY_EMAIL)) {
                    if (!isLocated) {
                        startLocationUpdates();
                    }
                    //                    }
                } else {
                    openAlertDialog(R.string.location_access_denied, R.string.location_denied_marsh, requestForMarsh);
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    public void destinationfun(View view) {
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

            Log.e(TAG, message);
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

            Log.e(TAG, message);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    public void backButton(View view) {
//        Intent intent = new Intent(getApplicationContext(), HomeScreenActivity.class);
//        startActivity(intent);
        finish();
    }


    private void uploadUrlRideEstimate(double pick_latitude, double pick_longitude, double drop_latitude, double drop_longitude, final String cabCategry) {


        data.clear();
        String url = BASE_URL + PICKUP_LAT + pick_latitude + PICKUP_LAN + pick_longitude + DROP_LAT + drop_latitude + DROP_LNG + drop_longitude + cabCategry;

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

                    CabDataList cabDataList = new CabDataList(dNmae, ava, estimateTime, maximum, minimum, dimg, WITH_DESTINATION_CODE);
                    data.add(cabDataList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                manageRecyclerViewWithViewPager();

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


}

