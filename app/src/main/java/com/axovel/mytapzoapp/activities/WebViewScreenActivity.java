package com.axovel.mytapzoapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.axovel.mytapzoapp.R;
import com.axovel.mytapzoapp.utils.Constants;

public class WebViewScreenActivity extends AppCompatActivity implements Constants {

    private TextView headerText;

    private WebView webView = null;
    ProgressBar progressBar;
    //    private final int SELECT_PHOTO = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_web_view_screen);
        getWindow().setFeatureInt(Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);
        webView = (WebView) findViewById(R.id.webview);
        webView.clearView();
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);


        headerText = (TextView) findViewById(R.id.headerText);
        headerText.setText(getIntent().getStringExtra(HEADER));


        webView.setWebViewClient(new WebViewClient());
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);
        //        webView.addJavascriptInterface(new MyJavascriptInterface(this), "Android");

        // Makes Progress bar Visible


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // Do something here on page load start time.

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // Show the toast message after finishing page loading.
//                Toast.makeText(WebViewScreenActivity.this,"Page Loading Finish.",Toast.LENGTH_SHORT).show();
            }
        });


        // Add setWebChromeClient on WebView.
        webView.setWebChromeClient(new WebChromeClient() {

            public void onProgressChanged(WebView webView1, int newProgress) {

//                WebViewStatusTextView.setText("loading = " + newProgress + "%");
                progressBar.setProgress(newProgress);

                if (newProgress == 100) {
                    // Page loading finish

//                    WebViewStatusTextView.setText("Page Load Finish.");
                    progressBar.setVisibility(View.GONE);
                }
            }
        });


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

        webView.loadUrl(getIntent().getStringExtra(GET_WEBVIEW_URL));
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


    public void backButton(View view) {
        finish();
    }
}
