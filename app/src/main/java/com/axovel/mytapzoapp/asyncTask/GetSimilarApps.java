package com.axovel.mytapzoapp.asyncTask;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.axovel.mytapzoapp.service.ChatHeadService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Umesh Chauhan on 7/25/2016.
 */
public class GetSimilarApps extends AsyncTask<String, Void, String> {

    private Context mContext;

    public GetSimilarApps(Context context) {
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
                Log.i("Server Save Status", mResponse);

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
            JSONArray jArray = null;
            try {
                jsonObject = new JSONObject(response);
                jArray = jsonObject.getJSONArray("Apps");
                Log.i("Response", jArray.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (jArray != null && jArray.length() > 0) {
                Intent uiService = new Intent(mContext, ChatHeadService.class);
                uiService.putExtra("response", response);
                uiService.putExtra("type", "APPS");
                mContext.startService(uiService);
            } else {
                mContext.stopService(new Intent(mContext, ChatHeadService.class));
            }
        }
    }
}
