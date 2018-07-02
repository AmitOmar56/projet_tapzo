package com.axovel.mytapzoapp.asyncTask;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.axovel.mytapzoapp.service.CustomAccessibilityService.pref;


/**
 * Created by Umesh Chauhan on 8/25/2016.
 */
public class GetUserId extends AsyncTask<String, Void, String> {

    private Context mContext;

    public GetUserId(Context context) {
        mContext = context;
    }

    protected String doInBackground(String... urls) {
        String mResponse = null;
        String dataUrl = "";
        if (urls != null && urls.length >= 1) {
            dataUrl = urls[0];
            URL mURL;
            HttpURLConnection connection = null;
            try {
                // Create connection
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
                Log.i("Server Response UserId", mResponse);

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
        if (response != null && !response.isEmpty() && !response.equals(" ")) {
            Log.i("Response String", response);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (jsonObject != null && jsonObject.has("userId")) {
                try {
                    if (pref == null) {
                        pref = PreferenceManager.getDefaultSharedPreferences(mContext);
                    }
                    Log.i("Pref", "" + pref);
                    String userId = jsonObject.getString("userId");
                    Log.i("User ID", userId);
                    SharedPreferences.Editor edit = pref.edit();
                    edit.putString("userID", userId);
                    edit.commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
