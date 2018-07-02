package com.axovel.mytapzoapp.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.axovel.mytapzoapp.R;
import com.axovel.mytapzoapp.utils.Constants;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.api.client.auth.oauth2.Credential;
import com.uber.sdk.android.core.UberSdk;
import com.uber.sdk.core.auth.Scope;
import com.uber.sdk.rides.auth.OAuth2Credentials;
import com.uber.sdk.rides.client.CredentialsSession;
import com.uber.sdk.rides.client.SessionConfiguration;
import com.uber.sdk.rides.client.UberRidesApi;
import com.uber.sdk.rides.client.model.Product;
import com.uber.sdk.rides.client.model.ProductsResponse;
import com.uber.sdk.rides.client.model.Ride;
import com.uber.sdk.rides.client.model.RideRequestParameters;
import com.uber.sdk.rides.client.services.RidesService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import retrofit2.Response;

public class CancelRideScreenActivity extends AppCompatActivity implements Constants {

    RadioButton needMoretime, wanttoChange, expectedashorter, mydriverDenied, myResons, forgetCoupan;

    String cancelResonsText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_ride_screen);


        needMoretime = (RadioButton) findViewById(R.id.needMoreTime);
        wanttoChange = (RadioButton) findViewById(R.id.wanttoChange);
        expectedashorter = (RadioButton) findViewById(R.id.expectedashorter);
        mydriverDenied = (RadioButton) findViewById(R.id.mydriverDenied);
        myResons = (RadioButton) findViewById(R.id.myreson);
        forgetCoupan = (RadioButton) findViewById(R.id.forgetCoupan);
        needMoretime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (needMoretime.isChecked()) {

                    cancelResonsText = "Need More Time To Get Ready";
                    Log.d("priiyesh", cancelResonsText);

                }

            }
        });

        wanttoChange.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (wanttoChange.isChecked()) {
                    cancelResonsText = "Want to Change Drop Locatiion";
                    Log.d("priiyesh", cancelResonsText);
                }

            }
        });

        expectedashorter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (expectedashorter.isChecked()) {
                    cancelResonsText = "Expected a shorter Wait Time";
                    Log.d("priiyesh", cancelResonsText);
                }

            }
        });

        mydriverDenied.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (mydriverDenied.isChecked()) {
                    cancelResonsText = "Driver Denied Duty";
                    Log.d("priiyesh", cancelResonsText);

                }

            }
        });
        myResons.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (myResons.isChecked()) {
                    cancelResonsText = "My Resons Not Apply Coupans";

                    Log.d("priiyesh", cancelResonsText);

                }

            }
        });

        forgetCoupan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (forgetCoupan.isChecked()) {

                    cancelResonsText = "Forget To Apply Code";
                    Log.d("priiyesh", cancelResonsText);

                }

            }
        });
    }


    public void cancelRideFun(View view) {



        final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.cancel_ride_popup_screen);
//        dialog.getWindow().getAttributes().windowAnimations = R.style.dialogAnimation;

//        ImageView backButton = (ImageView) dialog.findViewById(R.id.backButton);
        TextView cancelRide = (TextView) dialog.findViewById(R.id.popup_cancel);
        TextView changeLocation = (TextView) dialog.findViewById(R.id.changeDrop);

        cancelRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        changeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    // The autocomplete activity requires Google Play Services to be available. The intent
                    // builder checks this and throws an exception if it is not the case.
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(CancelRideScreenActivity.this);
                    startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE_CHANGE_DROP_LOCATION);
                } catch (GooglePlayServicesRepairableException e) {
                    // Indicates that Google Play Services is either not installed or not up to date. Prompt
                    // the user to correct the issue.
                    GoogleApiAvailability.getInstance().getErrorDialog(CancelRideScreenActivity.this, e.getConnectionStatusCode(),
                            0 /* requestCode */).show();
                } catch (GooglePlayServicesNotAvailableException e) {
                    // Indicates that Google Play Services is not available and the problem is not easily
                    // resolvable.
                    String message = "Google Play Services is not available: " +
                            GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
            protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                CancelRideScreenActivity.super.onActivityResult(requestCode, resultCode, data);


                // Check that the result was from the autocomplete widget.
                if (requestCode == REQUEST_CODE_AUTOCOMPLETE_CHANGE_DROP_LOCATION) {
                    if (resultCode == RESULT_OK) {
                        // Get the user's selected place from the Intent.
                        Place place = PlaceAutocomplete.getPlace(getApplicationContext(), data);
                        double toLatitude = place.getLatLng().latitude;
                        Log.d("priyesh lotitude", "" + toLatitude);
                        double toLongitude = place.getLatLng().longitude;
                        Log.d("priyesh longitude", "" + toLongitude);

                        String address = (String) place.getAddress();
                        String locality = (String) place.getId();
                        String resource = (String) place.getAttributions();

                        // Display attributions if required.
                        CharSequence attributions = place.getAttributions();
                        if (!TextUtils.isEmpty(attributions)) {
//                    destinationText.setText(Html.fromHtml(attributions.toString()));
//                } else {
//                    destinationText.setText("");
                        }
                    } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                        Status status = PlaceAutocomplete.getStatus(getApplicationContext(), data);
                    } else if (resultCode == RESULT_CANCELED) {
                        // Indicates that the activity closed before a selection was made. For example if
                        // the user pressed the back button.
                    }
                }
//                if (requestCode == REQUEST_CHECK_SETTINGS) {
//                    if (resultCode == RESULT_OK) {
//
//                        // All required changes were successfully made
//                        if (mGoogleApiClient.isConnected()) {
//                            Log.d("onActivityResult", "resultCode == RESULT_OK....");
//                        }
//
//                    } else if (resultCode == RESULT_CANCELED) {
//                        // The user was asked to change settings, but chose not to
//                        openAlertDialog(R.string.gps_settings_disabled, R.string.choose_yes_on_dialog, requestForLocation);
//                        Log.d("onActivityResult", "resultCode == RESULT_CANCELED....");
//                    }
//                }
            }
        });


        dialog.show();


    }

    public void doNotCancelFun(View view) {


//        AlertDialog.Builder alert = new AlertDialog.Builder(CancelRideScreenActivity.this);
//        alert.setTitle("Ola Login");
//        WebView wv = new WebView(getApplicationContext());
//
//        WebSettings webSettings = wv.getSettings();
//        webSettings.setDomStorageEnabled(true);
//        webSettings.setJavaScriptEnabled(true);
//        webSettings.setUseWideViewPort(true);
//        webSettings.setBuiltInZoomControls(true);
//        webSettings.setPluginState(WebSettings.PluginState.ON);
//        webSettings.setAllowFileAccess(true);
//        webSettings.setSupportZoom(true);
//        webSettings.setLoadWithOverviewMode(true);
//        wv.setFocusable(true);
//        wv.setEnabled(true);
//        wv.setClickable(true);
//        wv.setFocusableInTouchMode(true);
//        wv.setFocusableInTouchMode(true);
//        wv.requestFocus(View.FOCUS_DOWN);
//        wv.loadUrl("https://affiliate.trivago.com/api/creative/20561/javascript");
//        wv.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//
//
//                view.loadUrl(url);
//                return true;
//            }
//
//            public void onLoadResource(WebView view, String url) {
//                super.onLoadResource(view, url);
//                Log.d("priyesh url ", url);
//            }
//        });
//
//
//        wv.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                    case MotionEvent.ACTION_UP:
//                        v.requestFocusFromTouch();
//                        break;
//                }
//                return false;
//            }
//
//        });
//
//
//        alert.setView(wv);
//
//        alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int id) {
//                dialog.dismiss();
//            }
//        });
//        alert.show();
//

    }
}
