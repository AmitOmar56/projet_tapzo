package com.axovel.mytapzoapp.utils;

/**
 * Created by axovel on 7/30/2017.
 */

public interface Constants {

    float VIDEO_HEIGHT_PER = 1f;
    int REQUEST_TIMEOUT = 20000;
    String STATUS="Available";


    String BASE_URL = "https://sandbox-t1.olacabs.com/v1/products?";
    String X_TOCKEN = "&X-APP-TOKEN=317d32c3ae534f39a911551538e50002";
    String PICKUP_LAT = "pickup_lat=";
    String PICKUP_LAN = "&pickup_lng=";
    String DROP_LAT= "&drop_lat=";
    String DROP_LNG="&drop_lng=";




    String MINI_CAB_CATEGORY="&category=mini";
    String MICRO_CAB_CATEGORY="&category=micro";
    String SEDAN_CAB_CATEGORY="&category=sedan";
    String PRIME_CAB_CATEGORY="&category=prime";
    String LUX_CAB_CATEGORY="&category=lux";
    String SUV_CAB_CATEGORY="&category=suv";
    String RENTAL_CAB_CATEGORY="&category=rental";
    String SHARE_CAB_CATEGORY="&category=share";
    String OUTSTATION_CAB_CATEGORY="&category=outstation";



    String KEY_IS_OPEN_WITH_SERVICES = "isOpenWithService";
    int CALL_PERMISSION_REQUEST_CODE = 54;
    int CHAT_DATA_COUNT = 25;
    int DELAY_APP_EXIT = 2000;

    String NULL = "null";

    // Default Search Radius..
    int DEFAULT_SEARCH_RADIUS = 50;

    String SHARED_PREFERENCE = "mySharedPreference";

    String DATASET="mydataset";

    // App User id key..
    String KEY_USER_ID_PREF = "id";
    String KEY_USER_NAME = "userName";
    String DEAL_OF_THE_DAY_URL="https://affiliate-api.flipkart.net/affiliate/offers/v1/dotd/json";
    String PRODUCT_SEARCH_URL="http://13.126.71.6:8081/All/";
    String PRODUCT_NAME="productItemName";

    String HEADER="headerText";
    String GET_WEBVIEW_URL="webView_URL";

    int WITHOUT_DESTINATION_CODE=999;
    int WITH_DESTINATION_CODE=9999;

    String DISPLAY_CAB_NAME ="CABDETAILSNAME";

    String CAB_BOOKING_URL="https://sandbox-t1.olacabs.com/v1/bookings/create";
    String SEATS="seats";
    String FROMLATLNG="fromlatlng";
    String TOLATLNG="tolatlng";
    String PICK_UP_TEXT="pick_UP_text";
    String DROP_TEXT="drop_text";
    int REQUEST_CODE_AUTOCOMPLETE_CHANGE_DROP_LOCATION=901;
    String OLALOGIN_URL="ola_login_url";
    String CAB_TYPE="cab_details_activity";
    String CAB_FARE="fareCab";
    String CAB_IMAGE="cab_Image";
    String CAB_ESTIMATE_TIME="estimareFare";
    String CAB_DISPLAY_NAME="car_header_name";
    String SHARE_SEATE="user_share_seate";

}

