package com.axovel.mytapzoapp.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.axovel.mytapzoapp.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Umesh Chauhan on 09-03-2016.
 * Axovel Private Limited
 */
public class General {

    public enum accessibilityStatus{
        On,
        Off,
        notDefined
    }


//     "http://13.126.71.6:8081/All/iphone 6"

    public static final String API_KEY = "6iWjIGaxmCmsxfZ7boBpiYZQwqVCMeCI";
    // public static final String BASE_URL = "http://api.datayuge.in/v5/pricecompare/search/singlesearch?apikey=6iWjIGaxmCmsxfZ7boBpiYZQwqVCMeCI&product=";
    // public static final String BASE_URL = "http://192.168.1.21/Cashgain/?";
    // public static final String BASE_URL = "http://newpricecomparison.cashgainapp.com";
    public static final String BASE_URL ="http://163.172.63.19:8081";
    public static final String GET_COUPON_BASE_URL = "http://cashgainapp.com/app2/coupon_lists.php?company_name=";
    public static final String SAVE_USER_SEARCH_DATA_BASE_URL = "http://cashgainapp.com/app2/zen_search_data.php?";
    public static final String GET_SIMILAR_APPS = "http://play.cashgainapp.com?packageName=";
    public static final String DINE_OUT_SECRET_KEY = "e249c439ed7697df2a4b045d97d4b9b7e1854c3ff8dd668c779013653913572e";
    public static final String DINE_OUT_SEARCH_URL = "http://api.dineout.co.in/external_api/api_v1/search_restaurants";
    public static final String KHAUGALI_SEARCH_URL = "http://khaugalideals.com/tp_ws/cash_gain/getdeals.php";
    public static final String NEARBY_SEARCH_URL = "http://cashgainapp.com/app2/restaurant_search_api.php?rest_name=";
    public static final String GET_USER_ID_URL = "http://cashgainapp.com/app2/get_zen_user_id.php";
    public static Context previousAct;
    public static ProgressDialog mAppProgressDialog;
    public static void showProgress(final Context act){

        if(mAppProgressDialog == null || act != previousAct) {
            mAppProgressDialog = new ProgressDialog(act, R.style.TransparentProgressDialog);
            previousAct = act;
        }
        mAppProgressDialog.setMessage("Getting Data..!!");
        mAppProgressDialog.setTitle(null);
        mAppProgressDialog.show();


       mAppProgressDialog.setProgressStyle(R.style.TransparentProgressDialog);
        LayoutInflater inflater = LayoutInflater.from(act);
        View v = inflater.inflate(R.layout.layout_custom_progress_dialog, null);
        ((TextView) v.findViewById(R.id.loadingText)).setText("Please wait..!!");
        mAppProgressDialog.setContentView(v);
        mAppProgressDialog.setCancelable(false);
        mAppProgressDialog.setIndeterminate(true);
    }

    public static Typeface getAppDefaultTypeface(Context cxt){
        Typeface typeface = Typeface.createFromAsset(cxt.getResources().getAssets(),
                "seguisym.ttf");
        return typeface;
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        Log.i("Device:",manufacturer +" "+model);
        return manufacturer + "+" + model;
    }

    public static String getEncodedString(String str){
        // Adding Encoding to App Name
        String encodedAppName = null;
        try {
            encodedAppName = URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException ignored) {
            // Can be safely ignored because UTF-8 is always supported
            ignored.printStackTrace();
        }
        return encodedAppName;
    }
}
