package com.axovel.mytapzoapp.activities;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.axovel.mytapzoapp.R;
import com.uber.sdk.android.core.auth.AccessTokenManager;
import com.uber.sdk.android.core.auth.AuthenticationError;
import com.uber.sdk.android.core.auth.LoginActivity;
import com.uber.sdk.android.core.auth.LoginCallback;
import com.uber.sdk.android.core.auth.LoginManager;
import com.uber.sdk.android.rides.RideParameters;
import com.uber.sdk.android.rides.RideRequestActivity;
import com.uber.sdk.android.rides.RideRequestActivityBehavior;
import com.uber.sdk.android.rides.RideRequestButton;
import com.uber.sdk.android.rides.RideRequestButtonCallback;
import com.uber.sdk.android.rides.RideRequestViewCallback;
import com.uber.sdk.android.rides.RideRequestViewError;
import com.uber.sdk.core.auth.AccessToken;
import com.uber.sdk.core.auth.Scope;
import com.uber.sdk.rides.client.ServerTokenSession;
import com.uber.sdk.rides.client.SessionConfiguration;
import com.uber.sdk.rides.client.error.ApiError;

import java.util.Arrays;

import static com.google.api.client.util.Preconditions.checkState;
import static com.uber.sdk.rides.client.utils.Preconditions.checkNotNull;

public class UberSdk extends AppCompatActivity {

    private static final String DROPOFF_ADDR = "One Embarcadero Center, San Francisco";
    private static final Double DROPOFF_LAT = 37.795079;
    private static final Double DROPOFF_LONG = -122.397805;
    private static final String DROPOFF_NICK = "Embarcadero";
    private static final String ERROR_LOG_TAG = "UberSDK-MainActivity";
    private static final String PICKUP_ADDR = "1455 Market Street, San Francisco";
    private static final Double PICKUP_LAT = 37.775304;
    private static final Double PICKUP_LONG = -122.417522;
    private static final String PICKUP_NICK = "Uber HQ";
    private static final String UBERX_PRODUCT_ID = "a1111c8c-c720-46c3-8534-2fcdd730040d";
    private static final int WIDGET_REQUEST_CODE = 1234;

    private static final String CLIENT_ID = "zlIWDBucAyNIEZ8Hl_SFSJQMIkkfr7qb";
    private static final String REDIRECT_URI = "http://www.latlong.net/myuber";
    private static final String SERVER_TOKEN ="V5KNsu0Axn3A9bJAw4LsPGpwqYpF30n9nPWmUhmc" ;

    private RideRequestButton blackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uber_sdk);

        SessionConfiguration config = new SessionConfiguration.Builder()
                // mandatory
                .setClientId(CLIENT_ID)
                // required for enhanced button features
                .setServerToken(SERVER_TOKEN)
                // required for implicit grant authentication
                .setRedirectUri(REDIRECT_URI)
                // required scope for Ride Request Widget features
                .setScopes(Arrays.asList(Scope.RIDE_WIDGETS))
                // optional: set sandbox as operating environment
                .setEnvironment(SessionConfiguration.Environment.SANDBOX)
                .build();
        ServerTokenSession session = new ServerTokenSession(config);


    }




}
