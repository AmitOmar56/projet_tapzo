package com.axovel.mytapzoapp.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

/**
 * Created by axovel on 8/1/2017.
 */

public class MarshMallowPermissionUtils {

    public static final int EXTERNAL_STORAGE_READ_PERMISSION_REQUEST_CODE = 4;
    public static final int EXTERNAL_STORAGE_WRITE_PERMISSION_REQUEST_CODE = 2;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 3;

    //    public boolean checkPermissionForRecord() {
    //        int result = ActivityCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO);
    //        if (result == PackageManager.PERMISSION_GRANTED) {
    //            return true;
    //        } else {
    //            return false;
    //        }
    //    }

    //    public void requestPermissionForRecord() {
    //        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.RECORD_AUDIO)) {
    //            Toast.makeText(activity, "Microphone permission needed for recording. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
    //        } else {
    //            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_PERMISSION_REQUEST_CODE);
    //        }
    //    }

    public static boolean checkPermissionForCamera(Activity activity) {
        int result = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        return (result == PackageManager.PERMISSION_GRANTED);
    }

    public static void requestPermissionForCamera(Activity activity) {
        //        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
        //            Toast.makeText(activity, "Camera permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        //        } else {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        //        }
    }

    public static boolean checkPermissionToWriteExternalStorage(Activity activity) {
        int result = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return (result == PackageManager.PERMISSION_GRANTED);
    }

    public static void requestPermissionToWriteExternalStorage(Activity activity) {
        //        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
        //            Toast.makeText(activity, "External Storage permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        //        } else {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_WRITE_PERMISSION_REQUEST_CODE);
        //        }
    }

    public static boolean checkPermissionToReadExternalStorage(Activity activity) {

        if (Build.VERSION.SDK_INT <= 15) {
            return true;
        } else {
            int result = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
            return (result == PackageManager.PERMISSION_GRANTED);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void requestPermissionToReadExternalStorage(Activity activity) {
        //        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
        //            Toast.makeText(activity, "External Storage permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        //        } else {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_READ_PERMISSION_REQUEST_CODE);
        //        }
    }


}
