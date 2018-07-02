package com.axovel.mytapzoapp.broadcastReciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Umesh Chauhan on 7/22/2016.
 */
public class NewInstallationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            String packageName = intent.getData().toString();
            if (packageName.contains("package:")) {
                packageName = packageName.replace("package:", "");
            }
            Log.i("New App Installed", packageName + " " + intent.toString());

            // Log.i("Get Similar Apps", General.GET_SIMILAR_APPS + packageName);
            // new GetSimilarApps(context).execute(General.GET_SIMILAR_APPS + packageName);

        }
    }
}
