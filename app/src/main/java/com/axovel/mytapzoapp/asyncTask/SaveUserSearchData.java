package com.axovel.mytapzoapp.asyncTask;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Umesh Chauhan on 7/16/2016.
 */
public class SaveUserSearchData extends AsyncTask<String, Void, String> {

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

    }
}
