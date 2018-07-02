package com.axovel.mytapzoapp.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.axovel.mytapzoapp.utils.Constants;
import com.axovel.mytapzoapp.utils.PreferenceStore;
import com.axovel.mytapzoapp.view.activity.ActivityServiceUiHandler;
import com.axovel.mytapzoapp.asyncTask.GetSimilarApps;
import com.axovel.mytapzoapp.asyncTask.GetUserId;
import com.axovel.mytapzoapp.asyncTask.SaveUserSearchData;
import com.axovel.mytapzoapp.handler.AmazonIndia;
import com.axovel.mytapzoapp.handler.Flipkart;
import com.axovel.mytapzoapp.handler.IndiaTimesShopping;
import com.axovel.mytapzoapp.handler.Paytm;
import com.axovel.mytapzoapp.handler.PlayStore;
import com.axovel.mytapzoapp.handler.Shopclues;
import com.axovel.mytapzoapp.handler.Snapdeal;
import com.axovel.mytapzoapp.utils.AccessibilityUtil;
import com.axovel.mytapzoapp.utils.General;
import com.axovel.mytapzoapp.utils.SignatureCalculation;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by Umesh Chauhan on 08-03-2016.
 * Axovel Private Limited
 */
public class CustomAccessibilityService extends AccessibilityService {

    String lastProduct = "";
    String launcher = "";
    String userID;
    boolean hitZomato = false;
    PreferenceStore preferenceStore;
    boolean showCoupons = true;
    String currentRestaurantName = null;
    String currentRestaurantAddress = "";
    public static SharedPreferences pref;
    // Supported Apps
    String[] packageSupport = {"in.amazon.mShop.android.shopping", "com.flipkart.android", "com.snapdeal.main", "net.one97.paytm", "com.shopclues", "com.shopping", "com.application.zomato", "com.mobikwik_new", "com.freecharge.android", "com.android.vending"};
    // Deprecated Support App
    /* "com.ebay.mobile", "com.application.zomato" */ boolean gotResultFromDineOut = false;
    boolean gotResultFromKhaugali = false;
    boolean gotResultFromNearBy = false;
    JSONArray pendingResponse = null;


    @Override
    public void onCreate() {

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private String printChildEbay(AccessibilityNodeInfo source) {
        String productName = null;
        int childCount = source.getChildCount();
        for (int i = 0; i < childCount; i++) {
            // Log.i("Child", source.getChild(i).toString() + "source ");
            if (source.getChild(i) != null) {
                if (source.getChild(i).getChildCount() > 0) {
                    productName = printChildEbay(source.getChild(i));
                    // Log.i("Product Name - By Trav", productName+"");
                    return productName; // Got Product Name bypassing further traverse.
                }
            }
            List<AccessibilityNodeInfo> findAccessibilityNodeInfosByViewId = source.findAccessibilityNodeInfosByViewId("com.ebay.mobile:id/item_title_textview");
            if (findAccessibilityNodeInfosByViewId.size() > 0) {
                AccessibilityNodeInfo parent = (AccessibilityNodeInfo) findAccessibilityNodeInfosByViewId.get(0);
                String str2 = parent.getText().toString();
                productName = str2;
                // Log.i("Product Name - By Trav", str2 + " " + parent.getClassName() + " " + parent.getParent());
            }
        }
        return productName;
    }

    private String printZomato(AccessibilityNodeInfo source, int level) {
        if (source != null) {
            int childCount = source.getChildCount();

            for (int i = 0; i < childCount; i++) {
                if (source.getChild(i) != null) {
                    int childChildCount = source.getChild(i).getChildCount();
                    if (childChildCount > 0) {
                        printZomato(source.getChild(i), (level + 1));
                        // Log.i("Zomato - With Child", source.toString() + " Level: " + level);
                    } else {
                        // Log.i("Zomato - No Child Stg2", source.toString()+" Level: "+level);
                    }
                    if (i == 0 && source.getChild(i).getClassName().equals("android.widget.TextView") && source.getChild(i).getText() != null && !source.getChild(i).getText().toString().isEmpty()) {
                        String recivedText = source.getChild(i).getText().toString();
                        if (source.getChild(i).getClassName().equals("android.widget.TextView") && source.getChild(i).getParent() != null && source.getChild(i).getParent().getClassName().equals("android.widget.FrameLayout") && source.getChild(i).getParent().getParent() == null) {
                            // Log.i("Zomato - Final12", recivedText + " Level: " + level);
                            // printParent(source);
                            return recivedText;
                        }
                    }
                }
            }

            if (level == 0 && source.getClassName().equals("android.widget.TextView") && source.getText() != null && !source.getText().toString().isEmpty()) {
                String recivedText = source.getText().toString();
                if (source.getClassName().equals("android.widget.TextView") && source.getParent() != null && source.getParent().getClassName().equals("android.widget.FrameLayout") && source.getParent().getParent() == null) {
                    // Log.i("Zomato - Final12", recivedText + " Level: " + level);
                    // printParent(source);
                    return recivedText;
                }
                // Log.i("Zomato - Address", recivedText + " Level: " + level+ " class "+ source.getClassName()+" parent "+source.getParent());
                // printParent(source);
            }
            // Log.i("Zomato - Address if out", source.getText() + " Level: " + level);
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {


//        if (!PreferenceStore.getPreference(getApplicationContext()).getBoolean("VALUEBOOLEAN")) {
//            return;
//        }


        // Returning on null event
        if (event == null) {
            return;
        }
        // Returning on Notifications and announcements
        if (event.getEventType() == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED || event.getEventType() == AccessibilityEvent.TYPE_ANNOUNCEMENT) {
            return;
        }

        try {
            AccessibilityNodeInfo source = event.getSource();
            // Returning if source is null
            if (source == null) {
                return;
            }

            if (!source.getPackageName().equals(launcher)) {

                Log.d("PACKAGE.............", source.getPackageName().toString());

                Set<String> set = PreferenceStore.getPreference(getApplicationContext()).getSet(Constants.DATASET);
                List<String> list = new ArrayList<>(set);

                Log.d("LIST...............", list.toString());

                if (!list.contains(source.getPackageName().toString())) {
                    return;
                }
            }


            // To prevent popup on system apps..
            // Deprecated (Changing app behaviour to work only with limited apps)
            /*if (source.getPackageName().equals("com.android.systemui") ||
                    source.getPackageName().equals("com.cashgainapp.zen") ||
                    source.getPackageName().equals("com.android.keyguard")) {
                return;
            }*/

            ////////////// ********* TESTING ********* /////////////
                /*Log.i("Accessibility Values", "Event Content Desc: " + event.getContentDescription() +
                        "\n Describe Content" + event.describeContents() +
                        "\n Class Name" + event.getClassName() +
                        "\n Text" + event.getText() +
                        "\n Window ID" + event.getWindowId() +
                        "\n Event Type" + event.getEventType() +
                        "\n Record Count" + event.getRecordCount() +
                        "\n Action" + event.getAction() +
                        "\n MovementGranularity" + event.getMovementGranularity() +
                        "\n Event String" + event.toString() + "\n---------------------" +
                        "\n Source: " + source.getContentDescription() + " " +
                        "\n Window ID: " + source.getWindowId() + " " +
                        "\n ChildCount: " + source.getChildCount() +
                        "\n SourceString: " + source.toString() +
                        "\n Root" + getRootInActiveWindow().toString());*/
            // Log.i("Event", event.toString()+"");
            ////////////// ********* TESTING CODE END ********* /////////////

            // Launcher Handling
            if (source.getPackageName().equals(launcher)) {

                //                resetValueForFlipkart();

                // Launcher Detected stopping UI service
                if (AccessibilityUtil.isMyServiceRunning(ChatHeadService.class, getApplicationContext())) {
                    stopService(new Intent(getApplication(), ChatHeadService.class));
                }
                showCoupons = true;
            }
            // Google Chrome App
            else if (source.getPackageName().equals("com.android.chrome")) {
                // TODO -> Future
            }
            // Google Play Store App
            else if (source.getPackageName().equals("com.android.vending")) {

                //                resetValueForFlipkart();

                // Similar Apps
                String appName = PlayStore.getAppName(source, 0);
                if (appName != null && !lastProduct.equals(appName)) {
                    lastProduct = appName;
                    Log.i("Get Similar Apps", General.GET_SIMILAR_APPS + appName);
                    // Adding Encoding to App Name
                    String encodedAppName = General.getEncodedString(appName);
                    // Start Service Directly
                    startUIService();
                    Log.i("Get Similar Apps", General.GET_SIMILAR_APPS + encodedAppName);
                    new GetSimilarApps(getApplicationContext()).execute(General.GET_SIMILAR_APPS + encodedAppName);
                }
            }
            // Mobikwik App
            else if (source.getPackageName().equals("com.mobikwik_new") && event.getRecordCount() == 0 && source.getClassName().equals("android.widget.FrameLayout")) {

                //                resetValueForFlipkart();

                // Checking Event Type
                Log.i("Event Type", event.getEventType() + "");
                Log.i("Event", event.toString() + "");
                Log.i("Source", source.toString() + "");
                // Showing Coupons only when App opens
                if (showCoupons) {
                    showCoupons = false;
                    String url = General.GET_COUPON_BASE_URL + "Mobikwik" + "&search=Company";
                    // Start Service Directly
                    startUIService();
                    Log.i("Request", url + "");
                    new GetCoupons().execute(url);
                }
            }
            // Freecharge App
            else if (source.getPackageName().equals("com.freecharge.android") && event.getRecordCount() == 0 && source.getClassName().equals("android.widget.FrameLayout")) {

                //                resetValueForFlipkart();

                // Checking Event Type
                Log.i("Event Type", event.getEventType() + "");
                Log.i("Event", event.toString() + "");
                Log.i("Source", source.toString() + "");
                // Showing Coupons only when App opens
                if (showCoupons) {
                    showCoupons = false;
                    String url = General.GET_COUPON_BASE_URL + "Freecharge" + "&search=Company";
                    // Start Service Directly
                    startUIService();
                    Log.i("Request", url + "");
                    new GetCoupons().execute(url);
                }
            }
            // Zomato App -- Deprecated
            else if (source.getPackageName().equals("com.application.zomato")) {

                //                resetValueForFlipkart();

                   /* if("com.application.zomato.activities.RestaurantPage".equals(event.getClassName())){
                        Log.i("Zomato - Event", "True");
                    }*/
                // Log.i("Zomato - Event", event.getEventType()+"");
                if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED && "com.application.zomato.activities.RestaurantPage".equals(event.getClassName())) {
                    hitZomato = true;
                }
                // Name Get
                if (hitZomato) {
                    String reciv = printZomato(source, 0);
                    if (reciv != null && !reciv.isEmpty()) {
                        if (currentRestaurantName == null) {
                            hitZomato = false;
                            Log.i("Resturant Name", reciv + " " + event.getEventTime());
                            currentRestaurantName = reciv;
                        }
                    }
                }

                // Address Get
                if (source.getText() != null && source.getClassName().equals("android.widget.TextView") && source.getParent() != null && source.getParent().getClassName().equals("android.widget.ScrollView") && source.getParent().getParent() != null && source.getParent().getParent().getClassName().equals("android.support.v4.view.ViewPager") && source.getParent().getParent().getParent() != null && source.getParent().getParent().getParent().getClassName().equals("android.widget.FrameLayout") && source.getParent().getParent().getParent().getParent() == null) {
                    if (!currentRestaurantAddress.equals(source.getText().toString())) {
                        String address = source.getText().toString();
                        if (!address.contains("Uber") && !address.contains("reviews") && !address.contains("Reviews") && !address.contains("Recommended")) {
                            Log.i("Resturant Address", source.getText() + " " + event.getEventTime());
                            currentRestaurantAddress = source.getText().toString();
                            currentRestaurantName = null;
                        }
                    }
                }
                // Final Hit
                if ((!currentRestaurantAddress.isEmpty() || !currentRestaurantAddress.equals("")) && currentRestaurantName != null && !currentRestaurantName.equals(lastProduct)) {
                    // Log.i("Resturant","Name: "+ currentRestaurantName +" Address: "+ currentRestaurantAddress);
                    lastProduct = currentRestaurantName;
                    String[] addressParts = currentRestaurantAddress.split(",");
                    String cityName = null;
                    if (addressParts != null) {
                        int addressSize = addressParts.length;
                        if (addressSize > 0) {
                            cityName = addressParts[(addressSize - 1)];
                            // Remove PinCode if any
                            String[] cityParts = cityName.split("\\s+");
                            cityName = "";
                            if (cityParts != null) {
                                int citySize = cityParts.length;
                                if (citySize > 0) {
                                    for (int i = 0; i < citySize; i++) {
                                        boolean atleastOneNumber = cityParts[i].matches(".*[0-9]+.*");
                                        if (!atleastOneNumber) {
                                            cityName += " " + cityParts[i];
                                        }
                                    }
                                    cityName = cityName.trim();
                                }
                            }
                        }
                    }
                    // Log.i("Resturant","Name: "+ currentRestaurantName +"\nAddress: "+ currentRestaurantAddress+"\nCity: "+cityName);
                    // TODO HIT API
                    prepareRestaurantRequest(currentRestaurantName, currentRestaurantAddress, cityName);
                }
            }
            // Flipkart App
            else if (source.getPackageName().equals("com.flipkart.android")) {
                // Initial Check
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    if (source.getViewIdResourceName() == null || !source.getViewIdResourceName().equals("com.flipkart.android:id/product_page_recyclerview")) {
                        return;
                    }
                }

                // To print view hierarchy of screen as tree format..
                //                Flipkart.printPageLayoutStructureForward(source, 0);
                //                Flipkart.printPageLayoutStructureBackward(source, 0);

                // Getting Required Data
                String[] data = Flipkart.getProductDetails(source, 0);
                if (data == null) {
                    data = Flipkart.getProductDetailsv2(source, 0, 0);
                }

                if (data != null) {
                    /** Product Name from Event **/
                    String productName = data[0].trim();
                    /** Product Price from Event **/
                    String productPrice = data[1].trim();

                    if (productName.equals("Ends in few hours") || productName.equals("Ending soon")) {
                        data = Flipkart.getFlipkartDatav4(source, 0);
                    }

                    if (data != null) {
                        /** Product Name from Event **/
                        productName = data[0].trim();
                    }

                    if (productName != null && lastProduct != null) {
                        if (lastProduct.trim().endsWith("...more")) {
                            if (productName.contains(lastProduct.trim().replace("...more", "").trim())) {
                                return;
                            }
                        }
                    }

                    // if productName is still null - event is discarded
                    if (productName != null && !lastProduct.equals(productName) && productPrice != null) {

                        //                        setValueForFlipkart();

                        // Formatting Price
                        productPrice = productPrice.replace("Rs.", "");
                        productPrice = productPrice.replace("₹", "");
                        if (productPrice.contains(",")) {
                            productPrice = productPrice.replace(",", "");
                        }
                        if (productPrice.contains("rupees")) {
                            productPrice = productPrice.replace("rupees", "");
                        }
                        productPrice = productPrice.trim();
                        // Final Product price check
                        String regex = "[0-9]+";
                        Log.i("FinalFlipPrice PreCheck", productPrice);
                        if (productPrice.matches(regex)) {
                            lastProduct = productName;
                            if (productName.contains("/")) {
                                productName = productName.replace("/", " ");
                            }
                            if (productName.contains("#")) {
                                productName = productName.replace("#", " ");
                            }
                            if (productName.contains("?")) {
                                productName = productName.replace("?", " ");
                            }

                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("flipAppName", productName);
                            editor.commit();
                            //                            PreferenceStore.getPreference(getApplicationContext()).putString("flipAppName", productName);

                            // Adding Encoding to Product Name
                            String encodedProductName = General.getEncodedString(productName);
                            // Using Hardcoded params for now TODO -> Remove HardCoded Parameter Names
                            String[] productParts = productName.split("\\s+");
                            String brand = productParts[0];
                            String params = "/All/" + encodedProductName + "/" + productPrice + "/0/" + brand;
                            // Save User Search Data
                            // Using Hardcoded params for now TODO -> Remove HardCoded Parameter Names
                            saveSearchData(userID, "FLIPKART", encodedProductName, productPrice);
                            // Start Service Directly
                            startUIService();
                            Log.i("Request", General.BASE_URL + params);

                            // EVOKE FLOATING BUTTON AFTER RECEIVING DATA FROM SERVER
                            new GetPriceComparision().execute(General.BASE_URL + params);
                        }
                    }
                }
            }
            // Amazon India App
            else if (source.getPackageName().equals("in.amazon.mShop.android.shopping")) {

                //                resetValueForFlipkart();

                // Discarding Event if conditions not matched
                //                Log.i("AIndia", "1");

                String productName = null;
                String productPrice = null;

                productPrice = AmazonIndia.getPriceFromRange(source, 0);
                Log.i("AIndia ProductPrice 1>", productPrice + " " + source.getViewIdResourceName() + " " + source.getContentDescription() + " " + source.getChildCount());

                // AccessibilityUtil.printParent(source);
                // AccessibilityUtil.printChildWithDescription(source,0);
                // productPrice = AmazonIndia.getProductPriceWithID(source);

                if (((("android.view.View".equals(event.getClassName())) || ("android.webkit.WebView".equals(event.getClassName())) || ("android.widget.FrameLayout".equals(event.getClassName()))) && (("Product Details".equals(source.getContentDescription())))) || (source.getViewIdResourceName() == null && source.getClassName().equals("android.view.View") && source.getParent() != null && source.getParent().getClassName().equals("android.webkit.WebView") && source.getParent().getParent() != null && source.getParent().getParent().getClassName().equals("android.webkit.WebView") && source.getParent().getParent().getParent() != null && source.getParent().getParent().getParent().getClassName().equals("android.widget.LinearLayout") && source.getParent().getParent().getParent().getParent() != null && source.getParent().getParent().getParent().getParent().getClassName().equals("android.support.v4.widget.DrawerLayout") && source.getParent().getParent().getParent().getParent().getParent() != null && source.getParent().getParent().getParent().getParent().getParent().getClassName().equals("android.widget.FrameLayout"))) {

                    //                    Log.i("AIndia", "2");

                    // Getting Required Data
                    /** Product Name from Event **/
                    if (productName == null) {
                        productName = AmazonIndia.getProductName(source, 0);
                    }

                    //                    Log.i("AIndia", "3c >> " + productName);
                    // AccessibilityUtil.printChildWithDescription(source,0);

                    /** Product Price from Event **/
                    // For Price Layout 1
                    if (productPrice == null) {
                        productPrice = AmazonIndia.getProductPrice(source, 0, "Level One Extraction");
                        Log.i("AIndia ProductPrice 2>", productPrice + "");
                    }

                    // For Price Layout 2
                    if (productPrice == null) {
                        productPrice = AmazonIndia.getProductPrice(source, 0);
                        Log.i("AIndia ProductPrice 3>", productPrice + "");
                    }

                    // For Price Layout 3
                    if (productPrice == null) {
                        productPrice = AmazonIndia.getProductPricev2(source, 0);
                        Log.i("AIndia ProductPrice 4>", productPrice + "");
                    }

                    // For Price Layout 4
                    if (productPrice == null || !StringUtils.isNumeric(productPrice)) {
                        productPrice = AmazonIndia.getProductPricev3(source, 0);
                        Log.i("AIndia ProductPrice 5>", productPrice + "");
                    }

                    // To print view hierarchy of screen as tree format..
                    //                    AmazonIndia.printAmazonPageLayoutStructureForward(source, 0);
                    //                    AmazonIndia.printAmazonPageLayoutStructureBackward(source, 0);

                    // if productName is still null - event is discarded
                    if (productName != null && productPrice != null) {

                        // Log.i("AIndia","5");
                        Log.i("AIndia Product Name", productName + "");
                        Log.i("AIndia Product Price", productPrice + "");

                        if (!lastProduct.equals(productName)) {
                            lastProduct = productName;
                            // Formatting Price
                            if (productPrice.contains(",")) {
                                productPrice = productPrice.replace(",", "");
                            }
                            if (productPrice.contains("rupees")) {
                                productPrice = productPrice.replace("rupees", "");
                            }
                            productPrice = productPrice.trim();
                            if (productName.contains("/")) {
                                productName = productName.replace("/", " ");
                            }
                            if (productName.contains("#")) {
                                productName = productName.replace("#", " ");
                            }
                            if (productName.contains("?")) {
                                productName = productName.replace("?", " ");
                            }

                            // Adding Encoding to Product Name
                            String encodedProductName = General.getEncodedString(productName);

                            // Using Hardcoded params for now TODO -> Remove HardCoded Parameter Names
                            String[] productParts = productName.split("\\s+");
                            String brand = productParts[0];
                            String params = "/All/" + encodedProductName + "/" + productPrice + "/0/" + brand;

                            // Save User Search Data
                            // Using Hardcoded params for now TODO -> Remove HardCoded Parameter Names
                            saveSearchData(userID, "AMAZON", encodedProductName, productPrice);

                            // Start Service Directly
                            startUIService();

                            Log.i("Request", General.BASE_URL + params);

                            // EVOKE FLOATING BUTTON AFTER RECEIVING DATA FROM SERVER
                            new GetPriceComparision().execute(General.BASE_URL + params);
                        }
                    }
                }
            }
            // IndiaTimes Shopping App
            else if (source.getPackageName().equals("com.shopping")) {

                //                resetValueForFlipkart();

                /** Product Price from Event **/
                String productPrice = IndiaTimesShopping.getPrice(source, event, 0);
                if (productPrice != null) {
                    Log.i("ITS Product Price:", productPrice);
                }
                if (source.getContentDescription() != null) {
                    /** Product Name from Event **/
                    String productName = source.getContentDescription().toString();
                    Log.i("ITS Product Name:", productName);
                    if (!source.getClassName().equals("android.webkit.WebView")) {
                        return;
                    }
                    // Checking if it contains Alphabets
                    boolean atleastOneAlpha = productName.matches(".*[a-zA-Z]+.*");
                    if (atleastOneAlpha) {
                        // Checking Alphabets Count
                        int counter = 0;
                        for (int i = 0; i < productName.length(); i++) {
                            if (Character.isLetter(productName.charAt(i))) counter++;
                        }
                        if (counter < 3) {
                            return;
                        }
                        // Discarding Event if conditions not matched
                        if (source.getClassName().equals("android.webkit.WebView") && source.getChildCount() > 9) {
                            if (productName.contains("Buy ") && productName.contains(" at Best Price")) {
                                productName = productName.replace("Buy ", " ");
                                if (productName.contains(" Online at Best Price")) {
                                    productName = productName.replace(" Online at Best Price", " ");
                                } else {
                                    productName = productName.replace(" at Best Price", " ");
                                }
                                productName = productName.trim();
                                // if productName is still null - event is discarded
                                if (!lastProduct.equals(productName) && productPrice != null) {
                                    lastProduct = productName;
                                    // Formatting Price
                                    if (productPrice.contains("₹")) {
                                        productPrice = productPrice.replace("₹", "");
                                    }
                                    if (productPrice.contains("Rs.")) {
                                        productPrice = productPrice.replace("Rs.", "");
                                    }
                                    if (productPrice.contains(",")) {
                                        productPrice = productPrice.replace(",", "");
                                    }
                                    if (productName.contains("/")) {
                                        productName = productName.replace("/", " ");
                                    }
                                    if (productName.contains("#")) {
                                        productName = productName.replace("#", " ");
                                    }
                                    if (productName.contains("?")) {
                                        productName = productName.replace("?", " ");
                                    }
                                    // Adding Encoding to Product Name
                                    String encodedProductName = General.getEncodedString(productName);
                                    // Using Hardcoded params for now TODO -> Remove HardCoded Parameter Names
                                    String[] productParts = productName.split("\\s+");
                                    String brand = productParts[0];
                                    String params = "/All/" + encodedProductName + "/" + productPrice + "/0/" + brand;
                                    // Save User Search Data
                                    // Using Hardcoded params for now TODO -> Remove HardCoded Parameter Names
                                    saveSearchData(userID, "INDIATIMES", encodedProductName, productPrice);
                                    // Start Service Directly
                                    startUIService();
                                    Log.i("Request", General.BASE_URL + params);
                                    // EVOKE FLOATING BUTTON AFTER RECEIVING DATA FROM SERVER
                                    new GetPriceComparision().execute(General.BASE_URL + params);
                                }
                            }
                        }
                    }
                }
            }
            // Ebay - NOT IN USE
            else if (source.getPackageName().equals("com.ebay.mobile")) {

                //                resetValueForFlipkart();

                // Log.i("Source",""+source);
                /** Product Name from Event **/
                String recivedText = null;
                String price = null;
                // Level one extract product Name
                Log.i("Check", "outPro");
                List findAccessibilityNodeInfosByViewId = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    findAccessibilityNodeInfosByViewId = source.findAccessibilityNodeInfosByViewId("com.ebay.mobile:id/item_title_textview");
                }
                if (findAccessibilityNodeInfosByViewId.size() > 0) {
                    Log.i("Check", "In");
                    AccessibilityNodeInfo parent = (AccessibilityNodeInfo) findAccessibilityNodeInfosByViewId.get(0);
                    String str2 = parent.getText().toString();
                    recivedText = str2;
                    Log.i("Product Name - Level 1", str2 + " " + parent.getChildCount());
                    // printParent(parent);

                }
                List findANI = source.findAccessibilityNodeInfosByViewId("com.ebay.mobile:id/price_textview");

                if (findANI.size() > 0) {
                    Log.i("Check", "Inpri");
                    AccessibilityNodeInfo parent = (AccessibilityNodeInfo) findANI.get(0);
                    price = parent.getText().toString();
                    Log.i("Product Price - Level 1", price + " " + recivedText);
                    // printParent(parent);
                }

                // if Level 1 Failed Getting childs - Level 2 Begins
                if (recivedText == null) {
                    try {
                        // If Source has Childs
                        int childCount = source.getChildCount();
                        if (childCount > 0) {
                            for (int i = 0; i < childCount; i++) {
                                if (source.getChild(i) != null) {
                                    recivedText = printChildEbay(source.getChild(i));
                                    if (recivedText != null) // ProductName Received stopping Loop
                                        // Log.i("Product Name - Level 2", recivedText);
                                        break;
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                // if recivedText is still null - event is discarded
                if (recivedText != null && !lastProduct.equals(recivedText) && price != null) {
                    // EVOKE FLOATING BUTTON AFTER RECEIVING DATA FROM SERVER
                    // Log.i("Shopclues FinalHint:", recivedText + " EventType: " + event.getEventType());
                    lastProduct = recivedText;

                    String encodedProductName = null;
                    try {
                        encodedProductName = URLEncoder.encode(recivedText, "UTF-8");
                    } catch (UnsupportedEncodingException ignored) {
                        // Can be safely ignored because UTF-8 is always supported
                        ignored.printStackTrace();
                    }
                    String[] productParts = recivedText.split("\\s+");
                    String brand = productParts[0];
                    String params = "/All/" + encodedProductName + "/" + recivedText + "/0/" + brand;
                    Log.i("Request", General.BASE_URL + params);
                    Log.i("Request-Encoded", encodedProductName + "");
                    new GetPriceComparision().execute(General.BASE_URL + params);
                    // Save User Search Data
                    saveSearchData(userID, "EBAY", encodedProductName, price);
                }
            }
            // SnapDeal App
            else if (source.getPackageName().equals("com.snapdeal.main")) {

                //                resetValueForFlipkart();

                /** Product Name from Event **/
                String productName = null;
                /** Product Price from Event **/
                String productPrice = null;
                // Level one extract product Name
                List findAccessibilityNodeInfosByViewId = source.findAccessibilityNodeInfosByViewId("com.snapdeal.main:id/ptitleView");
                if (findAccessibilityNodeInfosByViewId.size() > 0) {
                    AccessibilityNodeInfo parent = (AccessibilityNodeInfo) findAccessibilityNodeInfosByViewId.get(0);
                    String str2 = parent.getText().toString();
                    productName = str2;
                    Log.i("SnapD Product Name - L1", str2 + " " + parent.getChildCount());
                }
                // Get Product Price
                List ANIPrice = source.findAccessibilityNodeInfosByViewId("com.snapdeal.main:id/sellingPriceTextView");
                if (ANIPrice.size() > 0) {
                    AccessibilityNodeInfo parent = (AccessibilityNodeInfo) ANIPrice.get(0);
                    String str2 = parent.getText().toString();
                    productPrice = str2;
                    Log.i("SnapD Product Price L1", str2 + " " + parent.getChildCount());
                }
                ANIPrice = source.findAccessibilityNodeInfosByViewId("com.snapdeal.main:id/finalPriceTextView");
                if (ANIPrice.size() > 0) {
                    AccessibilityNodeInfo parent = (AccessibilityNodeInfo) ANIPrice.get(0);
                    String str2 = parent.getText().toString();
                    productPrice = str2;
                    Log.i("SnapD Product Price L2", str2 + " " + parent.getChildCount());
                }
                // Get Product Name if Level 1 Failed Getting childs - Level 2 Begins
                if (productName == null) {
                    try {
                        // If Source has Childs
                        int childCount = source.getChildCount();
                        if (childCount > 0) {
                            for (int i = 0; i < childCount; i++) {
                                if (source.getChild(i) != null) {
                                    productName = Snapdeal.getProductNameL2(source.getChild(i));
                                    if (productName != null)
                                        // Product Name Received stopping Loop
                                        break;
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                // if productName is still null - event is discarded
                if (productName != null && !lastProduct.equals(productName) && productPrice != null) {
                    lastProduct = productName;
                    // Formatting Price
                    if (productPrice.contains("Rs.")) {
                        productPrice = productPrice.replace("Rs.", "");
                    }
                    productPrice = productPrice.trim();
                    if (productPrice.contains(",")) {
                        productPrice = productPrice.replace(",", "");
                    }
                    if (productName.contains("/")) {
                        productName = productName.replace("/", " ");
                    }
                    if (productName.contains("#")) {
                        productName = productName.replace("#", " ");
                    }
                    if (productName.contains("?")) {
                        productName = productName.replace("?", " ");
                    }
                    // Adding Encoding to Product Name
                    String encodedProductName = General.getEncodedString(productName);
                    // Using Hardcoded params for now TODO -> Remove HardCoded Parameter Names
                    String[] productParts = productName.split("\\s+");
                    String brand = productParts[0];
                    String params = "/All/" + encodedProductName + "/" + productPrice + "/0/" + brand;
                    // Save User Search Data
                    // Using Hardcoded params for now TODO -> Remove HardCoded Parameter Names
                    // Log.i("User ID", userID);
                    saveSearchData(userID, "SNAPDEAL", encodedProductName, productPrice);
                    // Start Service Directly
                    startUIService();
                    Log.i("Request", com.axovel.mytapzoapp.utils.General.BASE_URL + params);
                    // EVOKE FLOATING BUTTON AFTER RECEIVING DATA FROM SERVER
                    new GetPriceComparision().execute(General.BASE_URL + params);
                }
            }
            // ShopClues App
            else if (source.getPackageName().equals("com.shopclues")) {

                //                resetValueForFlipkart();

                /** Product Name from Event **/
                String productName = null;
                /** Product Price from Event **/
                String productPrice = null;
                AccessibilityUtil.printChild(source, 0);
                // Level one extract product Name
                List findAccessibilityNodeInfosByViewId = source.findAccessibilityNodeInfosByViewId("com.shopclues:id/product_name");
                if (findAccessibilityNodeInfosByViewId.size() > 0) {
                    if (findAccessibilityNodeInfosByViewId.size() > 0) {
                        AccessibilityNodeInfo parent = (AccessibilityNodeInfo) findAccessibilityNodeInfosByViewId.get(0);
                        String str2 = parent.getText().toString();
                        Log.i("Shopc Product Name - L1", str2 + " " + parent.getChildCount() + " " + findAccessibilityNodeInfosByViewId.size());
                        AccessibilityUtil.printParent(parent);
                        if (parent.getText() != null && parent.getClassName().equals("android.widget.TextView") && parent.getParent() != null && parent.getParent().getClassName().equals("android.widget.ScrollView") && parent.getParent().getParent() != null && parent.getParent().getParent().getClassName().equals("android.widget.LinearLayout") && parent.getParent().getParent().getParent() != null && parent.getParent().getParent().getParent().getClassName().equals("android.widget.LinearLayout") && parent.getParent().getParent().getParent().getParent() != null && parent.getParent().getParent().getParent().getParent().getClassName().equals("android.support.v4.widget.DrawerLayout") && parent.getParent().getParent().getParent().getParent().getParent() != null && parent.getParent().getParent().getParent().getParent().getParent().getClassName().equals("android.widget.FrameLayout") && parent.getParent().getParent().getParent().getParent().getParent().getParent() == null) {
                            productName = str2;
                            Log.i("Shopc Product Name - L1", str2 + " " + parent.getChildCount());
                        }
                    }
                }

                // Get Price
                List ANIPrice = source.findAccessibilityNodeInfosByViewId("com.shopclues:id/current_price");
                if (ANIPrice.size() > 0) {
                    AccessibilityNodeInfo parent = (AccessibilityNodeInfo) ANIPrice.get(0);
                    productPrice = parent.getText().toString();
                } else {
                    ANIPrice = source.findAccessibilityNodeInfosByViewId("com.shopclues:id/text_mrp1");
                    if (ANIPrice.size() > 0) {
                        AccessibilityNodeInfo parent = (AccessibilityNodeInfo) ANIPrice.get(0);
                        productPrice = parent.getText().toString();
                    } else {
                        // If above methods fails
                        productPrice = Shopclues.getPrice(source, 0);
                    }
                }
                // If Level 1 Failed Getting childs.
                // Level 2 Begins
                if (productName == null) {
                    try {
                        // If Source has Childs
                        int childCount = source.getChildCount();
                        if (childCount > 0) {
                            for (int i = 0; i < childCount; i++) {
                                if (source.getChild(i) != null) {
                                    productName = Shopclues.getProductNameL2(source.getChild(i));
                                    if (productName != null)
                                        // ProductName Received stopping Loop
                                        break;
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                // If Level 2 Failed Getting childs.
                // Level 3 Begins
                if (productName == null || productPrice == null) {
                    String[] details = Shopclues.getProductNameL3(source, 0);
                    if (details != null) {
                        productName = details[0];
                        productPrice = details[1];
                        if (details[2] != null) {
                            productPrice = details[2];
                        }
                        if (details[3] != null) {
                            productPrice = details[3];
                        }
                    }
                }
                // if productName is still null - event is discarded
                if (productName != null && !lastProduct.equals(productName) && productPrice != null) {
                    Log.i("Shopc Product Name", productName + "");
                    Log.i("Shopc Product Price", productPrice + "");
                    lastProduct = productName;
                    // Formatting Price
                    if (productPrice.contains("Rs.")) {
                        productPrice = productPrice.replace("Rs.", "");
                    }
                    if (productPrice.contains("₹")) {
                        productPrice = productPrice.replace("₹", "");
                    }
                    if (productPrice.contains(",")) {
                        productPrice = productPrice.replace(",", "");
                    }
                    if (productName.contains("/")) {
                        productName = productName.replace("/", " ");
                    }
                    if (productName.contains("#")) {
                        productName = productName.replace("#", " ");
                    }
                    if (productName.contains("?")) {
                        productName = productName.replace("?", " ");
                    }
                    productPrice = productPrice.trim();
                    try {
                        productPrice = productPrice.replaceAll("\\s+", "");
                    } catch (Exception e) {
                        // Handle Error
                    }
                    // Adding Encoding to Product Name
                    String encodedProductName = General.getEncodedString(productName);
                    // Using Hardcoded params for now TODO -> Remove HardCoded Parameter Names
                    String[] productParts = productName.split("\\s+");
                    String brand = productParts[0];
                    String params = "/All/" + encodedProductName + "/" + productPrice + "/0/" + brand;
                    // Save User Search Data
                    // Using Hardcoded params for now TODO -> Remove HardCoded Parameter Names
                    saveSearchData(userID, "SHOPCLUES", encodedProductName, productPrice);
                    // Start Service Directly
                    startUIService();
                    Log.i("Request", com.axovel.mytapzoapp.utils.General.BASE_URL + params);
                    // EVOKE FLOATING BUTTON AFTER RECEIVING DATA FROM SERVER
                    new GetPriceComparision().execute(General.BASE_URL + params);
                }
            }
            // Paytm App
            else if (source.getPackageName().equals("net.one97.paytm")) {

                //                resetValueForFlipkart();

                // Showing Coupons as soon as app is opened
                if (showCoupons && event.getRecordCount() == 0 && source.getClassName().equals("android.widget.FrameLayout")) {
                    // Checking Event Type
                    // Log.i("Event Type", event.getEventType()+"");
                    // Log.i("Event", event.toString()+"");
                    // Log.i("Source", source.toString()+"");
                    showCoupons = false;
                    String url = General.GET_COUPON_BASE_URL + "Paytm.com+CPS+-+India" + "&search=Company";
                    // Start Service Directly
                    startUIService();
                    Log.i("Request", url + "");
                    new GetCoupons().execute(url);
                }
                /** Product Name from Event **/
                String productName = null;
                /** Product Price from Event **/
                String productPrice = null;
                if (("net.one97.paytm.AJRProductDetail".equals(event.getClassName()) || "android.support.v4.view.ViewPager".equals(event.getClassName()))) {
                    List<AccessibilityNodeInfo> findAccessibilityNodeInfosByViewId1 = source.findAccessibilityNodeInfosByViewId("net.one97.paytm:id/product_detail_scroll");
                    if (findAccessibilityNodeInfosByViewId1 != null) {
                        for (AccessibilityNodeInfo source2 : findAccessibilityNodeInfosByViewId1) {
                            if (source2 != null && source2.isVisibleToUser()) {
                                // Level one extract product Name
                                if (productName == null) {
                                    List findAccessibilityNodeInfosByViewId = source2.findAccessibilityNodeInfosByViewId("net.one97.paytm:id/product_name");
                                    if (findAccessibilityNodeInfosByViewId.size() > 0) {
                                        if (findAccessibilityNodeInfosByViewId.size() > 0) {
                                            AccessibilityNodeInfo parent = (AccessibilityNodeInfo) findAccessibilityNodeInfosByViewId.get(0);
                                            String str2 = parent.getText().toString();
                                            productName = str2;
                                            Log.i("Paytm Product Name - L1", str2 + " " + parent.getChildCount());
                                        }
                                    }
                                }
                                List ANIPrice = source2.findAccessibilityNodeInfosByViewId("net.one97.paytm:id/actual_price");
                                if (ANIPrice.size() > 0) {
                                    AccessibilityNodeInfo parent = (AccessibilityNodeInfo) ANIPrice.get(0);
                                    String str2 = parent.getText().toString();
                                    productPrice = str2;
                                    Log.i("Paytm Product Price L1", str2 + " " + parent.getChildCount());
                                }
                                ANIPrice = source2.findAccessibilityNodeInfosByViewId("net.one97.paytm:id/discounted_price");
                                if (ANIPrice.size() > 0) {
                                    AccessibilityNodeInfo parent = (AccessibilityNodeInfo) ANIPrice.get(0);
                                    String str2 = parent.getText().toString();
                                    productPrice = str2;
                                    Log.i("Paytm Product Price L2", str2 + " " + parent.getChildCount());
                                }
                                // if Level 1 Failed Getting childs - Level 2 Begins
                                if (productName == null) {
                                    try {
                                        // If Source has Childs
                                        int childCount = source2.getChildCount();
                                        if (childCount > 0) {
                                            for (int i = 0; i < childCount; i++) {
                                                if (source2.getChild(i) != null) {
                                                    productName = Paytm.getProductNameL2(source2.getChild(i));
                                                    if (productName != null)
                                                        // ProductName Received stopping Loop
                                                        break;
                                                }
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                // if productName is still null - event is discarded
                                if (productName != null && !lastProduct.equals(productName) && productPrice != null) {
                                    lastProduct = productName;
                                    // Formatting Price
                                    if (productPrice.contains("₹")) {
                                        productPrice = productPrice.replace("₹", "");
                                    }
                                    if (productPrice.contains("Rs.")) {
                                        productPrice = productPrice.replace("Rs.", "");
                                    }
                                    if (productPrice.contains(",")) {
                                        productPrice = productPrice.replace(",", "");
                                    }
                                    if (productName.contains("/")) {
                                        productName = productName.replace("/", " ");
                                    }
                                    if (productName.contains("#")) {
                                        productName = productName.replace("#", " ");
                                    }
                                    if (productName.contains("?")) {
                                        productName = productName.replace("?", " ");
                                    }
                                    productPrice = productPrice.trim();
                                    // Adding Encoding to Product Name
                                    String encodedProductName = General.getEncodedString(productName);
                                    // Using Hardcoded params for now TODO -> Remove HardCoded Parameter Names
                                    String[] productParts = productName.split("\\s+");
                                    String brand = productParts[0];
                                    String params = "/All/" + encodedProductName + "/" + productPrice + "/0/" + brand;
                                    // Save User Search Data
                                    // Using Hardcoded params for now TODO -> Remove HardCoded Parameter Names
                                    saveSearchData(userID, "PAYTM", encodedProductName, productPrice);
                                    // Start Service Directly
                                    startUIService();
                                    Log.i("Request", com.axovel.mytapzoapp.utils.General.BASE_URL + params);
                                    // EVOKE FLOATING BUTTON AFTER RECEIVING DATA FROM SERVER
                                    new GetPriceComparision().execute(General.BASE_URL + params);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    public static boolean IS_CURRENT_FLIPKART = false;

    //    private void setValueForFlipkart() {
    //
    //        Log.d("setValueFlipkartTRUE", "setValueFlipkartTRUE>>>>>>>>>>");
    //        //        PreferenceStore.getPreference(getApplicationContext()).putBoolean("isFlipkartMyCur", true);
    //        IS_CURRENT_FLIPKART = true;
    //    }

    //    private void resetValueForFlipkart() {
    //
    //        //        if (PreferenceStore.getPreference(getApplicationContext()).getBoolean("isFlipkartMyCur")) {
    //        //
    //        Log.d("resetValueFlipkartFALSE", "resetValueFlipkartFALSE????????????????");
    //        //            PreferenceStore.getPreference(getApplicationContext()).putBoolean("isFlipkartMyCur", false);
    //        //    }
    //        IS_CURRENT_FLIPKART = false;
    //    }

    private void saveSearchData(String userID, String appName, String encodedProductName, String productPrice) {
        String requestParams = "userid=" + userID + "&app_name=" + appName + "&product_name=" + encodedProductName + "&productPrice=" + productPrice;
        Log.i("Save Search Data", General.SAVE_USER_SEARCH_DATA_BASE_URL + requestParams);
        new SaveUserSearchData().execute(General.SAVE_USER_SEARCH_DATA_BASE_URL + requestParams);
    }

    private class GetPriceComparision extends AsyncTask<String, Void, String[]> {

        protected String[] doInBackground(String... urls) {
            String mResponse = null;
            String dataUrl = "";
            if (urls != null && urls.length >= 1) {
                dataUrl = urls[0];
                URL mURL;
                HttpURLConnection connection = null;
                try {
                    // Create connection
                    // Log.i("Final URL", dataUrl);
                    mURL = new URL(dataUrl);
                    connection = (HttpURLConnection) mURL.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Content-Language", "en-US");
                    connection.setRequestProperty("Accept", "*/*");
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ");
                    connection.setConnectTimeout(20000);
                    InputStream is = connection.getInputStream();
                    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                    String line;
                    StringBuffer response = new StringBuffer();
                    while ((line = rd.readLine()) != null) {
                        response.append(line);
                        response.append('\r');
                    }
                    rd.close();
                    mResponse = response.toString();
                    Log.d("Server response", mResponse);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
            String productString = "";
            productString = dataUrl;
            productString = productString.replace(General.BASE_URL, " ");
            productString = productString.trim();
            String[] response = {mResponse, productString};
            return response;
        }

        protected void onPostExecute(String[] response) {
            // Check if Currently open app is supported or not
            if (getRootInActiveWindow() != null && getRootInActiveWindow().getPackageName() != null && Arrays.asList(packageSupport).contains(getRootInActiveWindow().getPackageName()) && !getRootInActiveWindow().getPackageName().equals(launcher)) {
                // response[0] --> Response, response[1] --> Search Product
                if (response[0] != null && !response[0].isEmpty() && !response[0].equals(" ")) {
                    // Log.i("Search String", response[1]);
                    Log.i("Response String", response[0]);
                    JSONObject jObject = null;
                    try {
                        jObject = new JSONObject(response[0]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        if (jObject != null && jObject.has("matches") && jObject.getInt("matches") > 0) {
                            Log.i("Response String", response[0]);
                            // Set Response
                            Intent uiService = new Intent(getApplication(), ChatHeadService.class);
                            uiService.putExtra("response", response[0]);
                            // bindService(uiService, mServerConn, Context.BIND_AUTO_CREATE);
                            startService(uiService);
                            // ChatHeadService.dataResponse = response[0];
                        } else {
                            stopService(new Intent(getApplication(), ChatHeadService.class));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    private class GetCoupons extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls) {
            String mResponse = null;
            String dataUrl = "";
            if (urls != null && urls.length >= 1) {
                dataUrl = urls[0];
                URL mURL;
                HttpURLConnection connection = null;
                try {
                    // Create connection
                    // Log.i("Final URL", dataUrl);
                    mURL = new URL(dataUrl);
                    connection = (HttpURLConnection) mURL.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Content-Language", "en-US");
                    connection.setRequestProperty("Accept", "*/*");
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ");
                    InputStream is = connection.getInputStream();
                    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                    String line;
                    StringBuffer response = new StringBuffer();
                    while ((line = rd.readLine()) != null) {
                        response.append(line);
                        response.append('\r');
                    }
                    rd.close();
                    mResponse = response.toString();
                    Log.d("Server response", mResponse);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }

            return mResponse;
        }

        protected void onPostExecute(String response) {
            // Check if Currently open app is supported or not
            if (getRootInActiveWindow() != null && getRootInActiveWindow().getPackageName() != null && Arrays.asList(packageSupport).contains(getRootInActiveWindow().getPackageName()) && !getRootInActiveWindow().getPackageName().equals(launcher)) {
                if (response != null && !response.isEmpty() && !response.equals(" ")) {
                    // Log.i("Search String", response[1]);
                    Log.i("Response String", response);
                    JSONObject jsonObject = null;
                    JSONArray jArray = null;
                    try {
                        jsonObject = new JSONObject(response);
                        jArray = jsonObject.getJSONArray("Coupons");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (jArray != null && jArray.length() > 0) {
                        // Set Response
                        Intent uiService = new Intent(getApplication(), ChatHeadService.class);
                        uiService.putExtra("response", response);
                        uiService.putExtra("type", "COUPONS");
                        startService(uiService);
                    } else {
                        stopService(new Intent(getApplication(), ChatHeadService.class));
                    }

                }
            }
        }
    }

    private void prepareRestaurantRequest(String name, String address, String city) {

        // SEND REQUEST
        // Index 0 = URL
        // Index 1 = Restaurant Name
        // Index 2 = Restaurant City
        gotResultFromDineOut = false;
        gotResultFromKhaugali = false;
        gotResultFromNearBy = false;
        pendingResponse = null;
        String[] requestParams = {General.DINE_OUT_SEARCH_URL, name, city};
        new GetOffersFromDineOut().execute(requestParams);
        String[] requestParams1 = {General.KHAUGALI_SEARCH_URL, name, city};
        new GetOffersFromKhaugali().execute(requestParams1);
        String[] requestParams2 = {General.NEARBY_SEARCH_URL, name, city};
        new GetOffersFromNearBy().execute(requestParams2);
    }

    private String getCityFromMap(String city) {
        if (city.contains("Delhi") || city.contains("Faridabad") || city.contains("Ghaziabad") || city.contains("Gurgaon") || city.contains("Noida")) {
            return "Delhi";
        } else if (city.contains("Secunderabad") || city.contains("Hyderabad")) {
            return "Hyderabad";
        } else if (city.contains("Parganas") || city.contains("Howrah") || city.contains("Kolkata")) {
            return "Kolkata";
        } else if (city.contains("Mumbai") || city.contains("Thane")) {
            return "Mumbai";
        } else {
            return city;
        }
    }

    private class GetOffersFromDineOut extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... requestData) {
            // Replacing City from CITY MAP provided by DINE OUT
            requestData[2] = getCityFromMap(requestData[2]);
            //
            JSONObject params = new JSONObject();
            try {
                params.put("city_name", requestData[2]);
                params.put("search_needle", requestData[1]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //
            String encodedCity = null;
            try {
                encodedCity = URLEncoder.encode(requestData[2], "UTF-8");
            } catch (UnsupportedEncodingException ignored) {
                // Can be safely ignored because UTF-8 is always supported
                ignored.printStackTrace();
            }
            String encodedName = null;
            try {
                encodedName = URLEncoder.encode(requestData[1], "UTF-8");
            } catch (UnsupportedEncodingException ignored) {
                // Can be safely ignored because UTF-8 is always supported
                ignored.printStackTrace();
            }
            //
            String paramString = params.toString();
            String hashKey = SignatureCalculation.hmac(paramString, General.DINE_OUT_SECRET_KEY);
            String mResponse = null;
            String dataUrl = "";
            if (requestData != null && requestData.length >= 1) {
                dataUrl = requestData[0] + "?city_name=" + encodedCity + "&search_needle=" + encodedName;
                URL mURL;
                HttpURLConnection connection = null;
                try {
                    // Create connection
                    mURL = new URL(dataUrl);
                    //
                    Log.i("Request Details", "HashKey: " + hashKey + " Params: " + paramString + " URL: " + mURL.toString());
                    //
                    connection = (HttpURLConnection) mURL.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Content-Language", "en-US");
                    connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    connection.setRequestProperty("Auth-key", "49r2xe4-2h26172-6xhg-9i1r-45cashgain");
                    connection.setRequestProperty("Auth-id", "1024");
                    connection.setRequestProperty("Hash-key", hashKey);
                    connection.setUseCaches(false);
                    // connection.setDoInput(true);
                    // connection.setDoOutput(true);
                    // Send request
                    // OutputStreamWriter wr= new OutputStreamWriter(connection.getOutputStream());
                    // wr.write(params.toString());
                    // wr.flush();
                    // wr.close();
                    // Get Response
                    Log.i("Response Code - Dineout", connection.getResponseCode() + "");
                    InputStream is = connection.getInputStream();
                    BufferedReader rd = new BufferedReader(new InputStreamReader(is, "utf-8"));
                    String line;
                    StringBuffer response = new StringBuffer();
                    while ((line = rd.readLine()) != null) {
                        response.append(line);
                        response.append('\n');
                    }
                    rd.close();
                    mResponse = response.toString();
                    Log.d("Response - Dineout", mResponse + " Msg: " + connection.getResponseMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }

            return mResponse;
        }

        protected void onPostExecute(String response) {
            // Check if Currently open app is supported or not
            if (getRootInActiveWindow() != null && getRootInActiveWindow().getPackageName() != null && getRootInActiveWindow().getPackageName().equals("com.application.zomato")) {
                gotResultFromDineOut = true;
                if (response != null && !response.isEmpty() && !response.equals(" ")) {
                    // Log.i("Response String", response);
                    JSONObject jObject;
                    JSONArray jArray = null;
                    try {
                        jObject = new JSONObject(response);
                        if (jObject.has("status") && jObject.getString("status").equals("true") && jObject.has("restaurant_count") && jObject.getInt("restaurant_count") > 0) {
                            jArray = jObject.getJSONArray("output_params");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (jArray != null) {
                        // TODO
                        openUIServiceForRestaurant(jArray, "Dineout");
                    } else {
                        openUIServiceForRestaurant(null, "Dineout");
                    }
                } else {
                    openUIServiceForRestaurant(null, "Dineout");
                }
            }
        }
    }

    /*
    * responseType "Dineout" or "Khaugali" or "Nearby"
    *
    */
    private void openUIServiceForRestaurant(JSONArray response, String responseType) {
        if (response != null) {
            if (responseType.equals("Dineout")) {
                int size = response.length();
                for (int i = 0; i < size; i++) {
                    try {
                        JSONObject details = (JSONObject) response.get(i);
                        String restName = details.getString("profile_name");
                        String restURL = details.getString("url");
                        String restArea = details.getString("area_name");
                        if (details.getInt("no_of_offers") > 0) {
                            JSONArray offers = details.getJSONArray("offers");
                            if (offers != null) {
                                int offerSize = offers.length();
                                for (int j = 0; j < offerSize; j++) {
                                    String offerDesc = offers.getString(j);
                                    if (offerDesc.contains("regular##")) {
                                        offerDesc = offerDesc.replace("regular##", "");
                                    }
                                    JSONObject obj = new JSONObject();
                                    obj.put("restName", restName);
                                    obj.put("restURL", restURL);
                                    obj.put("restOffer", offerDesc);
                                    obj.put("restArea", restArea);
                                    obj.put("avlOn", "DINE OUT DEALS");
                                    if (pendingResponse == null) {
                                        pendingResponse = new JSONArray();
                                        pendingResponse.put(obj);
                                    } else {
                                        pendingResponse.put(obj);
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (responseType.equals("Khaugali")) {
                int size = response.length();
                for (int i = 0; i < size; i++) {
                    try {
                        JSONObject details = (JSONObject) response.get(i);
                        String restName = details.getString("deals_name");
                        String restURL = details.getString("referral_link");
                        String restArea = details.getString("location");
                        String offers = details.getString("offer_details");

                        JSONObject obj = new JSONObject();
                        obj.put("restName", restName);
                        obj.put("restURL", restURL);
                        obj.put("restOffer", offers);
                        obj.put("restArea", restArea);
                        obj.put("avlOn", "KHAUGALI DEALS");
                        if (pendingResponse == null) {
                            pendingResponse = new JSONArray();
                            pendingResponse.put(obj);
                        } else {
                            pendingResponse.put(obj);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (responseType.equals("Nearby")) {
                int size = response.length();
                for (int i = 0; i < size; i++) {
                    try {
                        JSONObject obj = (JSONObject) response.get(i);
                        obj.put("avlOn", "NEAR BY");
                        if (pendingResponse == null) {
                            pendingResponse = new JSONArray();
                            pendingResponse.put(obj);
                        } else {
                            pendingResponse.put(obj);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        if (gotResultFromKhaugali && gotResultFromDineOut && gotResultFromNearBy && pendingResponse != null) {
            if (AccessibilityUtil.isMyServiceRunning(ChatHeadService.class, getApplicationContext()) && !ActivityServiceUiHandler.active) {
                stopService(new Intent(getApplication(), ChatHeadService.class));
                //
                Intent uiService = new Intent(getApplication(), ChatHeadService.class);
                uiService.putExtra("response", "{offers:" + pendingResponse.toString() + "}");
                startService(uiService);
            } else if (!ActivityServiceUiHandler.active) {
                Intent uiService = new Intent(getApplication(), ChatHeadService.class);
                uiService.putExtra("response", "{offers:" + pendingResponse.toString() + "}");
                startService(uiService);
            }
        }
    }

    @Override
    public void onInterrupt() {

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onServiceConnected() {
        super.onServiceConnected();
        Log.i("Accessibility", "Working-CashGain");
        // Getting User ID
        pref = getApplicationContext().getSharedPreferences("MyPref", Activity.MODE_MULTI_PROCESS);
        // Getting User ID
        Log.i("Pref", pref + "");
        if (pref.contains("userID")) {
            userID = pref.getString("userID", null);
            Log.i("UserID", userID + "");
        } else {
            getUserId();
        }
        // Getting Current Launcher
        PackageManager localPackageManager = getPackageManager();
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        String str = localPackageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY).activityInfo.packageName;
        // Adding Launcher to packageNames
        String[] packagesWithLaucher = new String[packageSupport.length + 1];
        for (int i = 0; i < packageSupport.length; i++) {
            packagesWithLaucher[i] = packageSupport[i];
        }
        packagesWithLaucher[(packagesWithLaucher.length - 1)] = str;
        launcher = str;
        // Getting Service Info
        AccessibilityServiceInfo info = getServiceInfo();
        // Type of Events that this service listens (Selecting All for now)
        /* info.eventTypes = AccessibilityEvent.TYPE_WINDOWS_CHANGED |
                AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED |
                AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED; */
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        // Package name of applications on which this service will work
        info.packageNames = packagesWithLaucher;
        // Type of feedback this service will provide
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        info.notificationTimeout = 0;
        // Flags
        info.flags = AccessibilityServiceInfo.DEFAULT;
        info.flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS;
        info.flags = AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS;
        info.flags = AccessibilityServiceInfo.FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY;
        info.flags = AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS;
        // Setting Services
        this.setServiceInfo(info);
    }

    private class GetOffersFromKhaugali extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... requestData) {
            // Replacing City from CITY MAP provided by DINE OUT
            if (requestData[2].contains("Delhi") || requestData[2].contains("Faridabad") || requestData[2].contains("Ghaziabad") || requestData[2].contains("Gurgaon") || requestData[2].contains("Noida")) {
                requestData[2] = "Delhi";
            }
            //
            String encodedCity = null;
            try {
                encodedCity = URLEncoder.encode(requestData[2], "UTF-8");
            } catch (UnsupportedEncodingException ignored) {
                // Can be safely ignored because UTF-8 is always supported
                ignored.printStackTrace();
            }
            String encodedName = null;
            try {
                encodedName = URLEncoder.encode(requestData[1], "UTF-8");
            } catch (UnsupportedEncodingException ignored) {
                // Can be safely ignored because UTF-8 is always supported
                ignored.printStackTrace();
            }
            //
            String mResponse = null;
            String dataUrl = "";
            if (requestData != null && requestData.length >= 1) {
                dataUrl = requestData[0] + "?city_name=" + encodedCity + "&rest_name=" + encodedName;
                URL mURL;
                HttpURLConnection connection = null;
                try {
                    // Create connection
                    mURL = new URL(dataUrl);
                    //
                    // Log.i("Request Details","URL: "+mURL.toString());
                    //
                    connection = (HttpURLConnection) mURL.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Content-Language", "en-US");
                    connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    connection.setUseCaches(false);
                    // Get Response
                    Log.i("ResponseCode - Khaugali", connection.getResponseCode() + "");
                    InputStream is = connection.getInputStream();
                    BufferedReader rd = new BufferedReader(new InputStreamReader(is, "utf-8"));
                    String line;
                    StringBuffer response = new StringBuffer();
                    while ((line = rd.readLine()) != null) {
                        response.append(line);
                        response.append('\n');
                    }
                    rd.close();
                    mResponse = response.toString();
                    Log.d("Response - Khaugali", mResponse + " Msg: " + connection.getResponseMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
            return mResponse;
        }

        protected void onPostExecute(String response) {
            // Check if Currently open app is supported or not
            if (getRootInActiveWindow() != null && getRootInActiveWindow().getPackageName() != null && getRootInActiveWindow().getPackageName().equals("com.application.zomato")) {
                gotResultFromKhaugali = true;
                if (response != null && !response.isEmpty() && !response.equals(" ")) {
                    // Log.i("Response String", response);
                    JSONObject jObject;
                    JSONArray jArray = null;
                    try {
                        jObject = new JSONObject(response);
                        if (jObject.has("message") && jObject.getString("message").equals("success") && jObject.has("data")) {
                            jArray = jObject.getJSONArray("data");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (jArray != null) {
                        // TODO
                        openUIServiceForRestaurant(jArray, "Khaugali");
                    } else {
                        openUIServiceForRestaurant(null, "Khaugali");
                    }
                } else {
                    openUIServiceForRestaurant(null, "Khaugali");
                }
            }
        }
    }


    private class GetOffersFromNearBy extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... requestData) {
            String encodedName = null;
            try {
                encodedName = URLEncoder.encode(requestData[1], "UTF-8");
            } catch (UnsupportedEncodingException ignored) {
                // Can be safely ignored because UTF-8 is always supported
                ignored.printStackTrace();
            }
            //
            String mResponse = null;
            String dataUrl = "";
            if (requestData != null && requestData.length >= 1) {
                dataUrl = requestData[0] + encodedName;
                URL mURL;
                HttpURLConnection connection = null;
                try {
                    // Create connection
                    mURL = new URL(dataUrl);
                    //
                    // Log.i("Request Details","URL: "+mURL.toString());
                    //
                    connection = (HttpURLConnection) mURL.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Content-Language", "en-US");
                    connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    connection.setUseCaches(false);
                    // Get Response
                    Log.i("Response Code - Nearby", connection.getResponseCode() + "");
                    InputStream is = connection.getInputStream();
                    BufferedReader rd = new BufferedReader(new InputStreamReader(is, "utf-8"));
                    String line;
                    StringBuffer response = new StringBuffer();
                    while ((line = rd.readLine()) != null) {
                        response.append(line);
                        response.append('\n');
                    }
                    rd.close();
                    mResponse = response.toString();
                    Log.d("Response - Nearby", mResponse + " Msg: " + connection.getResponseMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
            return mResponse;
        }

        protected void onPostExecute(String response) {
            // Check if Currently open app is supported or not
            if (getRootInActiveWindow() != null && getRootInActiveWindow().getPackageName() != null && getRootInActiveWindow().getPackageName().equals("com.application.zomato")) {
                gotResultFromNearBy = true;
                if (response != null && !response.isEmpty() && !response.equals(" ")) {
                    // Log.i("Response String", response);
                    JSONObject jObject;
                    JSONArray jArray = null;
                    try {
                        jObject = new JSONObject(response);
                        if (jObject.has("status") && jObject.getInt("status") == 200 && jObject.has("result")) {
                            jArray = jObject.getJSONArray("result");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (jArray != null) {
                        // TODO
                        openUIServiceForRestaurant(jArray, "Nearby");
                    } else {
                        openUIServiceForRestaurant(null, "Nearby");
                    }
                } else {
                    openUIServiceForRestaurant(null, "Nearby");
                }
            }
        }

    }

    void getUserId() {
        // Get AdvID
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                AdvertisingIdClient.Info idInfo = null;
                try {
                    idInfo = AdvertisingIdClient.getAdvertisingIdInfo(getApplicationContext());
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String advertId = null;
                try {
                    advertId = idInfo.getId();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return advertId;
            }

            @Override
            protected void onPostExecute(String advertId) {
                // Advertisement Id
                Log.i("AdvertId", advertId + "");
                // Getting Model
                String deviceName = General.getDeviceName();
                // Adding Encoding to Product Name
                String encodedDeviceName = null;
                try {
                    encodedDeviceName = URLEncoder.encode(deviceName, "UTF-8");
                } catch (UnsupportedEncodingException ignored) {
                    // Can be safely ignored because UTF-8 is always supported
                    ignored.printStackTrace();
                }
                // Getting IMEI
                String imei = "";
                PackageManager pm = getApplicationContext().getPackageManager();
                if (pm.checkPermission(android.Manifest.permission.READ_PHONE_STATE, getApplicationContext().getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                    TelephonyManager tm = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
                    if (tm != null) {
                        imei = tm.getDeviceId();
                    }
                }
                Log.i("IMEI", imei + "");
                if (!imei.isEmpty()) {
                    pref.edit().putString("imei", imei).commit();
                }
                // Getting Android Id
                String androidId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                Log.i("Get User ID request", General.GET_USER_ID_URL + "?advertisementId=" + advertId + "&phoneModel=" + encodedDeviceName + "&imeino=" + imei + "&androidId=" + androidId);
                // GET USER ID FROM SERVER AND SAVE IT IN PREFERENCES
                new GetUserId(getApplicationContext()).execute(General.GET_USER_ID_URL + "?advertisementId=" + advertId + "&phoneModel=" + encodedDeviceName + "&imeino=" + imei + "&androidId=" + androidId);
                // Toast.makeText(getApplicationContext(), advertId, Toast.LENGTH_SHORT).show();
            }
        };
        task.execute();
    }

    /**
     * Starts UI Service (POP UP)
     **/
    private void startUIService() {
        // Checking if already on running
        if (AccessibilityUtil.isMyServiceRunning(ChatHeadService.class, getApplicationContext())) {
            // Stopping Service First
            stopService(new Intent(getApplication(), ChatHeadService.class));
            // Start fresh instance
            Intent uiService = new Intent(getApplication(), ChatHeadService.class);
            startService(uiService);
        } else if (!ChatHeadService.isActive()) {
            Intent uiService = new Intent(getApplication(), ChatHeadService.class);
            startService(uiService);
        }
    }
}