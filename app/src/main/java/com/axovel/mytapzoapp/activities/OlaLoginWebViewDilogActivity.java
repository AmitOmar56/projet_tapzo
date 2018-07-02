package com.axovel.mytapzoapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.axovel.mytapzoapp.R;
import com.axovel.mytapzoapp.utils.Constants;
import com.axovel.mytapzoapp.utils.PreferenceStore;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class OlaLoginWebViewDilogActivity extends Activity implements Constants {
    private TextView headerText;

    private WebView webView = null;
    private String image;
    private String fare;
    private String estimateTime;
    private String cabType;
    private String pickupPoint,dropPoint;
    private LatLng toPosition=null,fromPosition=null;
    ArrayList<String> urlLogin=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ola_login_web_view_dilog);
        initReferance();
        webView = (WebView) findViewById(R.id.ola_login_webview);
        webView.clearView();
        webView.setWebViewClient(new WebViewClient());
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);





        // Enable Javascript
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        //        webView.getSettings().setBuiltInZoomControls(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setAllowFileAccess(true);
        //        webView.getSettings().setSupportZoom(true);
        webSettings.setDomStorageEnabled(true);

        webView.loadUrl("https://sandbox-t1.olacabs.com/oauth2/authorize?response_type=token&client_id=YzkxM2MyMmYtZTMwZi00NDAwLWJkN2MtN2MxOWIyNDQ5NzAw&redirect_uri=http://localhost/ola/tokens&scope=profile%20booking&state=state123");


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {


                view.loadUrl(url);
                return true;
            }


            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);


                if(url.contains("http://localhost/ola/tokens#access_token=")  ){
                    Log.d("priyesh Ola access ",url);

                    String [] arrOfStr = url.split("=",2);

                    String token=arrOfStr[1];
                    String [] accesstoken=token.split("&",2);
                    String accessToken=accesstoken[0];
                    Log.d("welcome Priyesh",accessToken);
                    PreferenceStore.getPreference(getApplicationContext()).putString(OLALOGIN_URL,accessToken);
                    Intent intent=new Intent(getApplicationContext(),ConfirmationScreenActivity.class);
                    intent.putExtra(CAB_TYPE,cabType);
                    intent.putExtra(CAB_FARE,fare);
                    intent.putExtra(CAB_ESTIMATE_TIME,estimateTime);
                    intent.putExtra(CAB_IMAGE,image);
                    intent.putExtra(PICK_UP_TEXT,pickupPoint);
                    intent.putExtra(DROP_TEXT,dropPoint);
                    intent.putExtra(FROMLATLNG,fromPosition);
                    intent.putExtra(TOLATLNG,toPosition);
                    startActivity(intent);


//                    System.out.println(accesstoken[0]);



                }




//                Log.d("priyesh url ",url);
            }
        });





    }


    private void initReferance(){




        pickupPoint=getIntent().getStringExtra(PICK_UP_TEXT);
        dropPoint= getIntent().getStringExtra(DROP_TEXT);



        fromPosition = getIntent().getParcelableExtra(FROMLATLNG);
        toPosition=getIntent().getParcelableExtra(TOLATLNG);
        image=getIntent().getStringExtra(CAB_IMAGE);
        fare=getIntent().getStringExtra(CAB_FARE);
        estimateTime=getIntent().getStringExtra(CAB_ESTIMATE_TIME);
        cabType=getIntent().getStringExtra(CAB_TYPE);



    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {

            webView.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }
}


