package com.axovel.mytapzoapp.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.axovel.mytapzoapp.R;
import com.axovel.mytapzoapp.app.AppController;
import com.axovel.mytapzoapp.customAdapter.CustomListAdapter;
import com.axovel.mytapzoapp.customAdapter.FoodPagerAdapter;
import com.axovel.mytapzoapp.utils.EndlessScrollListener;
import com.axovel.mytapzoapp.utils.MyProgressDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class FoodScreenActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    /*************Variables to get location start************/
    private final int REQUEST_CHECK_SETTINGS = 12345;
    private String requestForMarsh = "forMarsh";
    private String requestForLocation = "forLocation";
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest locationRequest;
    private boolean isLocated = false;
    private String finalLatitude = "";
    private String finalLongitude = "";
    private String finalState = "";
    private String finalCountry = "";
    private String finalCity = "";
    /************Variables to get location end******************/

    /***********Variables of previous code start**************/
    LocationManager locationmanager;
    public PlaceAutocompleteFragment autocompleteFragment;
    private String TAG = "Place_Api_Activity";
    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private List<String> name = new ArrayList<String>();
    private List<String> cuisine = new ArrayList<String>();
    private List<String> image = new ArrayList<String>();
    private List<String> offers = new ArrayList<String>();
    private List<String> costFor2 = new ArrayList<String>();
    private List<String> rating = new ArrayList<String>();
    private List<String> restorant_id = new ArrayList<String>();
    private List<String> list_id = new ArrayList<String>();
    private List<String> url = new ArrayList<>();
    TextView tv1;
    String result;
    String latitude;
    String longitude;
    String city_Name;
    ListView list;
    ViewPager viewPager;
    private String fnialAddress;
    private String city;
    double lat;
    double lng;
    private Integer limit_count;
    int images[] = {R.drawable.banner1, R.drawable.banner2, R.drawable.banner3, R.drawable.banner4, R.drawable.banner5};
    private FoodPagerAdapter foodPagerAdapter;
    private ProgressDialog dialog;
    private SwipeRefreshLayout sr;
    private CustomListAdapter adapter = null;


    /*************Variables of previous code end*****************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_screen);

        /**********New**********/
        requestPermissionsIfNotGranted();
        buildGoogleApiClient();
        createLocationRequest();
        turnOnGpsForCurrentLocation();
        /************New**********/

        /*************Previous************/
        list = (ListView) findViewById(R.id.list);
//        location_id();

        place_id();

        MyProgressDialog.showPDialog(this);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewPager = (ViewPager) findViewById(R.id.pager);
        foodPagerAdapter = new FoodPagerAdapter(this, images);
        viewPager.setAdapter(foodPagerAdapter);


        list.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                Log.d("page", "" + page);
                //String count=""+page;
                page = page - 1;
                page = page * 10;
                Log.d("page", "" + page);
                MyProgressDialog.showPDialog(FoodScreenActivity.this);

                setData(page);
//                MyProgressDialog.showPDialog(FoodScreenActivity.this);

                // or loadNextDataFromApi(totalItemsCount);
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });
        /*************Previous***********/
    }

    private void place_id() {

        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        /*
        * The following code example shows setting an AutocompleteFilter on a PlaceAutocompleteFragment to
        * set a filter returning only results with a precise address.
        */
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .build();
        autocompleteFragment.setFilter(typeFilter);
        autocompleteFragment.setHint("Enter Your Location");
        ((EditText) autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input)).setTextSize(10.0f);
        ((EditText) autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input)).setHint(" Enter Your Location");
        ((ImageButton) autocompleteFragment.getView().findViewById(R.id.place_autocomplete_clear_button)).setVisibility(View.GONE);

        ((ImageButton) autocompleteFragment.getView().findViewById(R.id.place_autocomplete_clear_button)).setLayoutParams(new LinearLayout.LayoutParams(70, 25));

        autocompleteFragment.getView().setBackground(getResources().getDrawable(R.drawable.place_search_box));
        ((View) findViewById(R.id.place_autocomplete_search_button)).setVisibility(View.GONE);
//        ((View)findViewById(R.id.place_autocomplete_clear_button)).setVisibility(View.GONE);
//         ((ImageButton)autocompleteFragment.getView().findViewById(R.id.place_autocomplete_clear_button)).setImageResource(R.drawable.downimage);
//        ((ImageButton)autocompleteFragment.getView().findViewById(R.id.place_autocomplete_clear_button)).
//        ((EditText)autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input)).getLayoutParams().width=100;
//        ((EditText)autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input)).getLayoutParams().height=50;

//        autocompleteFragment.setText(fnialAddress);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {

            @Override
            public void onPlaceSelected(Place place) {
                MyProgressDialog.showPDialog(FoodScreenActivity.this);

                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName());
                Log.i(TAG, place.getLatLng().toString());
                LatLng Location = place.getLatLng();
                Log.i(TAG, "Place: " + Location.latitude);
                Log.i(TAG, "Place: " + Location.longitude);

                Double place_lat = Location.latitude;
                Double place_lng = Location.longitude;
                latitude = place_lat.toString();
                longitude = place_lng.toString();
                // city_Name = place.getName().toString();

                Log.d("latitude", place.toString());
                Log.d("longitude", longitude);
                // Log.d("city_Name",city_Name);

                try {
                    Geocoder geocoder = new Geocoder(FoodScreenActivity.this, Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(place_lat, place_lng, 1);


//                 String   add1 = (addresses.get(0).getAdminArea() == null) ? "" : addresses.get(0).getAdminArea().trim();
//                   // String   add2  = (addresses.get(0).getCountryName() == null) ? "" : addresses.get(0).getCountryName().trim();
                    city_Name = (addresses.get(0).getLocality() == null) ? "" : addresses.get(0).getLocality().trim();
                } catch (Exception e) {

                    e.printStackTrace();

                }
                name.clear();
                cuisine.clear();
                costFor2.clear();
                offers.clear();
                url.clear();
                image.clear();
                rating.clear();
                list_id.clear();
                restorant_id.clear();
                Log.d("city_Name-CH",city_Name);
                Log.d("latitude-CH",latitude);
                Log.d("longitude-CH",longitude);
                setData(limit_count);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    private void requestPermissionsIfNotGranted() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).addApi(Places.GEO_DATA_API).addApi(Places.PLACE_DETECTION_API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
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

    // The callback for the management of the user settings regarding location
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
                        status.startResolutionForResult(FoodScreenActivity.this, REQUEST_CHECK_SETTINGS);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

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
        } else if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, intent);
                Log.i(TAG, "Place:" + place.toString());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, intent);
                Log.i(TAG, status.getStatusMessage());
            } else if (requestCode == RESULT_CANCELED) {

            }
        }
    }

    /**
     * Starting the location updates
     */
    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
        }
    }

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
            if (mGoogleApiClient.isConnected()) {
                mGoogleApiClient.disconnect();
            }
        }
    }

    /**
     * Method to verify google play services on the device
     */
    public void checkPlayServicesAvailable() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(FoodScreenActivity.this);
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(this, status, 1000).show();
            } else {
                Toast.makeText(this, R.string.device_not_supported, Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            }
        }
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
        Log.d("ConnectionCallbacks", "onConnectionSuspended....");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("OnConnectionFailedLis", "onConnectionFailed....");
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            Log.d("onLocationChanged", "onLocationChanged....");
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);
            boolean isFetched = saveUserCurrentAddress(latLng);

            if (isFetched) {
                stopLocationUpdates();
                isLocated = true;

                loadRestaurantList(location);
            }
        }
    }

    private void loadRestaurantList(Location location) {

        //        MyProgressDialog.showPDialog(this);

//        TextView textView1=(TextView)findViewById(R.id.textview1) ;
//        TextView textView2=(TextView)findViewById(R.id.textview2);
//        TextView textView3=(TextView)findViewById(R.id.textview3);
//        TextView latituteField = (TextView) findViewById(R.id.View2);
//        TextView longitudeField = (TextView) findViewById(R.id.View3);
        //       TextView addressField = (TextView) findViewById(R.id.currentloctext);

//        textView2.setText("Latitude"+location.getLatitude());
//        textView3.setText("Longitude"+ location.getLongitude());

        lat = location.getLatitude();
        lng = location.getLongitude();

        latitude = String.valueOf(lat);
        longitude = String.valueOf(lng);

        Log.d("latitude", latitude);
        Log.d("longitude", longitude);
        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
        StringBuilder builder = new StringBuilder();
        try {
            List<Address> address = geoCoder.getFromLocation(lat, lng, 1);
            int maxLines = address.get(0).getMaxAddressLineIndex();

            String locality_Name = address.get(0).getAddressLine(0);
            //String locality_Name_1 = address.get(0).getAddressLine(1);
            city = address.get(0).getAdminArea();
            builder.append(locality_Name);
            builder.append(",");
          //  builder.append(locality_Name_1);
            builder.append(",");
            fnialAddress = builder.toString(); //This is the complete address.

//            latituteField.setText(String.valueOf(lat));
//            longitudeField.setText(String.valueOf(lng));
//            addressField.setText(fnialAddress); //This will display the final address.
            autocompleteFragment.setText(fnialAddress);

            city_Name = city.trim();
          //  Log.d("locality_Name_1", locality_Name_1);
            Log.d("city_Name", city_Name);
            Log.d("fnialAddress", fnialAddress);


//            setData();
//
//            String data = "{" + "\"" + "lat" + "\"" + ":" + "\"" + latitude + "\"" + "," + "\"" + "lng" + "\"" + ":" + "\"" + longitude + "\"" + "," + "\"" + "radius" + "\"" + ":" + "\"" + "50" + "\"" + "," + "\"" + "city" + "\"" + ":" + "\"" + city_Name + "\"" + "," + "\"" + "index" + "\"" + ":" + "\"" + "0" + "\"" + "," + "\"" + "limit" + "\"" + ":" + "\"" + "10" + "\"" + "}";
//            result = sStringToHMACMD5(data, "e249c439ed7697df2a4b045d97d4b9b7e1854c3ff8dd668c779013653913572e");
//            String data_api = "lat=" + latitude + "&lng=" + longitude + "&radius=50&city=" + city_Name + "&index=0&limit=10";
//            Log.d("data", data);
//            Log.d("result", result);
//            Log.d("data_api", data_api);

            name.clear();
            cuisine.clear();
            costFor2.clear();
            offers.clear();
            url.clear();
            image.clear();
            rating.clear();
            list_id.clear();
            restorant_id.clear();
            limit_count = 0;
            Log.d("latitude",latitude);
            Log.d("longitude",longitude);
            Log.d("city_Name",city_Name);
            setData(limit_count);


        } catch (IOException e) {
            // Handle IOException
        } catch (NullPointerException e) {
            // Handle NullPointerException
        }
        // Log.d("location.getLatitude()")
    }

    private void setData(final Integer limit) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://cashgainapp.com/app2/api/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

//                name.clear();
//                cuisine.clear();
//                costFor2.clear();
//                offers.clear();
//                url.clear();
//                image.clear();
//                rating.clear();
//                list_id.clear();
//                restorant_id.clear();

                Log.d("amit ", response);
                try {
                    Object json = new JSONTokener(response).nextValue();
                    JSONObject jsonObject = (JSONObject) json;


                    JSONObject dineout_output = jsonObject.getJSONObject("dineout");
                    JSONObject nearby_output = jsonObject.getJSONObject("nearby");


                    //Log.d("Nearby", NearBy.toString());
                    Log.d("output", dineout_output.toString());
                    Log.d("output", nearby_output.toString());

                    if(dineout_output.getString("statusDine").equals("success")) {

                        if (!dineout_output.isNull("data")) {
                            //Value is not null
                            JSONArray Res_name = dineout_output.getJSONArray("data");

                            //  Log.d("jsonObject", Res_name.toString());


//                        DecimalFormat df = new DecimalFormat("#.00");
//                        String angleFormated = df.format(angle);
                            for (int i = 0; i < Res_name.length(); i++) {

                                JSONObject profile_Name = Res_name.getJSONObject(i);
                                name.add(profile_Name.getString("profile_name"));
                                restorant_id.add(profile_Name.getString("r_id"));
                                costFor2.add(profile_Name.getString("costFor2") + " " + "cost for two");
                                rating.add(profile_Name.getString("avg_rating"));
                                JSONArray imageArray = profile_Name.getJSONArray("img");
                                image.add(imageArray.getString(0));
                                JSONArray offerArray = profile_Name.getJSONArray("offers");
//                        offers.add(offerArray.getString(0));
                                offers.add(offerArray.length() > 0 ? offerArray.getString(0) : "");
                                JSONArray cuisineArray = profile_Name.getJSONArray("cuisine");
                                list_id.add("Dinout");
                                url.add("");
                            }
                            Log.d("qwe", cuisine.toString());
                            for (int k = 0; k < Res_name.length(); k++) {
                                JSONObject Cuisine_Name = Res_name.getJSONObject(k);
                                cuisine.add(Cuisine_Name.getString("cuisine").replaceAll("\\[|\\]|'|\"|", ""));
//                            Log.d("Result_3",Arrays.toString(cuisine));
                            }
                        }
                    }
                    if(nearby_output.getString("statusNearBy").equals("success")) {
                        if (!nearby_output.isNull("data")) {

                            DecimalFormat df = new DecimalFormat("#.00");
                            JSONArray NearBy = nearby_output.getJSONArray("data");

                            for (int i = 0; i < NearBy.length(); i++) {

                                JSONObject profile_Name = NearBy.getJSONObject(i);
                                name.add(profile_Name.getString("brand"));
                                restorant_id.add(profile_Name.has("id") ? profile_Name.getString("id") : "");
                                Double value = Double.parseDouble(profile_Name.getString("distance_in_km"));
                                costFor2.add(df.format(value) + " " + " km");
                                rating.add(profile_Name.has("avg_rating") ? profile_Name.getString("avg_rating") : "");
                                image.add(profile_Name.getString("bigimage"));
                                offers.add(profile_Name.getString("title"));
                                cuisine.add(profile_Name.getString("name"));
                                list_id.add("NearBy");
                                url.add(profile_Name.getString("producturl"));

                            }
                        }
                    }

                    if (adapter == null) {
                        adapter = new CustomListAdapter(FoodScreenActivity.this, name, image, cuisine, offers, costFor2, rating, list_id);
                        list.setAdapter(adapter);
                    }

                    adapter.notifyDataSetChanged();
                    click_on_list();

//                    dialog.dismiss();
                    MyProgressDialog.hidePDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley..,.", "ErrorListener: " + error.getMessage());
//                Toast.makeText(getApplicationContext(),"Network is Slow Please wait......", Toast.LENGTH_SHORT).show();
                setData(0);
//                Toast.makeText(getApplicationContext(), R.string.someting_went_wrong, Toast.LENGTH_SHORT).show();
            }
        }) {
//            @Override
//            public Map<String,String> getparams() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<>();
//                headers.put("lat", "28.585262");
//                headers.put("lng", "77.070441");
//                headers.put("radius","50");
//                headers.put("city","Delhi");
//                headers.put("index","0");
//                headers.put("limit","10");
//                headers.put("hash-key","e1dcc0e22cd346bfc124116166317713");
//                return headers;
//            }
//        };

            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("lat", latitude);
                params.put("lng", longitude);
                params.put("radius", "20");
                params.put("city", city_Name);
                params.put("index", limit.toString());
                params.put("limit", "10");
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringRequest);

        // Log.d("hmac",result);
        /*=====================for  add with header*=============================*/
//        LayoutInflater myinflater = getLayoutInflater();
//        ViewGroup myHeader = (ViewGroup)myinflater.inflate(R.layout.activity_food_screen,list, false);
//        list.addHeaderView(myHeader, null, false);
    }

    private void click_on_list() {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String Slecteditem = name.get(+position);
                String Selectitem_2 = list_id.get(+position);
                String Selectitem_3 = url.get(+position);
                if (Selectitem_2.equals("Dinout")) {
                    Toast.makeText(getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(FoodScreenActivity.this, RestroDetailActivity.class));
                    Intent intent = new Intent(FoodScreenActivity.this, BookingActivity.class);

                    intent.putExtra("test", offers.get(+position));
                    intent.putExtra("rest_id", restorant_id.get(+position));
                    // intent.putExtra("offer",offers.get(+position));
                    startActivity(intent);
                } else {
                    Intent viewIntent =
                            new Intent("android.intent.action.VIEW",
                                    Uri.parse(Selectitem_3));
                    startActivity(viewIntent);
                }
            }
        });
    }

    public static String sStringToHMACMD5(String s, String keyString) {
        String sEncodedString = null;
        try {
            SecretKeySpec key = new SecretKeySpec((keyString).getBytes("UTF-8"), "HmacMD5");
            Mac mac = Mac.getInstance("HmacMD5");
            mac.init(key);

            byte[] bytes = mac.doFinal(s.getBytes("ASCII"));

            StringBuffer hash = new StringBuffer();

            for (int i = 0; i < bytes.length; i++) {
                String hex = Integer.toHexString(0xFF & bytes[i]);
                if (hex.length() == 1) {
                    hash.append('0');
                }
                hash.append(hex);
            }
            sEncodedString = hash.toString();
        } catch (UnsupportedEncodingException e) {
        } catch (InvalidKeyException e) {
        } catch (NoSuchAlgorithmException e) {
        }
        return sEncodedString;
    }

    private boolean saveUserCurrentAddress(LatLng latLng) {

        boolean isFetched = false;

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

            finalLatitude = "" + latLng.latitude;
            finalLongitude = "" + latLng.longitude;
            finalState = (addresses.get(0).getAdminArea() == null) ? "" : addresses.get(0).getAdminArea().trim();
            finalCountry = (addresses.get(0).getCountryName() == null) ? "" : addresses.get(0).getCountryName().trim();
            finalCity = (addresses.get(0).getLocality() == null) ? "" : addresses.get(0).getLocality().trim();

            Log.d("Lat>>>>>", "" + finalLatitude);
            Log.d("Long>>>>", "" + finalLongitude);

//            PreferenceStore.getPreference(this).putString(KEY_LAT_PREF, "" + finalLatitude);
//            PreferenceStore.getPreference(this).putString(KEY_LONG_PREF, "" + finalLongitude);
//            PreferenceStore.getPreference(this).putString(KEY_STATE_PREF, "" + finalState);
//            PreferenceStore.getPreference(this).putString(KEY_COUNTRY_PREF, "" + finalCountry);
//            PreferenceStore.getPreference(this).putString(KEY_CITY, "" + finalCity);

            isFetched = true;

        } catch (Exception e) {

            e.printStackTrace();
            isFetched = false;

            finalLatitude = "";
            finalLongitude = "";
            finalState = "";
            finalCountry = "";
        }

        return isFetched;
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (!isLocated) {
                        startLocationUpdates();
                    }
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    openAlertDialog(R.string.location_access_denied, R.string.location_denied_marsh, requestForMarsh);
                }
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void openAlertDialog(final int title, int message, final String from) {

        AlertDialog.Builder builder = new AlertDialog.Builder(FoodScreenActivity.this);

        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (from.equals(requestForLocation)) {
                    turnOnGpsForCurrentLocation();
                } else if (from.equals(requestForMarsh)) {
                    requestPermissionsIfNotGranted();
                }
            }
        });

        builder.show();
    }

    public void food_screen_backButton(View view) {
        finish();
    }

    public void searchEditText(View view) {
        Intent intent = new Intent(this, Restorent_SearchActivity.class);

        intent.putExtra("city", city_Name);
        intent.putExtra("lat", latitude);
        intent.putExtra("lng", longitude);

        Log.d("city_Name", city_Name);
        Log.d("latitude", latitude);
        Log.d("longitude", longitude);

        startActivity(intent);
    }

    public void current_location(View view) {
        MyProgressDialog.showPDialog(this);
        latitude = String.valueOf(lat);
        longitude = String.valueOf(lng);
        city_Name = city.trim();
        autocompleteFragment.setText(fnialAddress);

        name.clear();
        cuisine.clear();
        costFor2.clear();
        offers.clear();
        url.clear();
        image.clear();
        rating.clear();
        list_id.clear();
        restorant_id.clear();
        limit_count = 0;
        Log.d("longitude",longitude);
        Log.d("latitude",latitude);
        Log.d("city_Name",city_Name);
        setData(limit_count);
    }
}
