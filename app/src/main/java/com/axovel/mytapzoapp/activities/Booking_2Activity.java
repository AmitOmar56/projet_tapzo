package com.axovel.mytapzoapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.axovel.mytapzoapp.R;
import com.axovel.mytapzoapp.app.AppController;
import com.axovel.mytapzoapp.utils.MyProgressDialog;
import com.axovel.mytapzoapp.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.Map;

import static com.axovel.mytapzoapp.activities.FoodScreenActivity.sStringToHMACMD5;

public class Booking_2Activity extends AppCompatActivity {
    int minteger = 0;
    int finteger = 0;
    int Mnum;
    int Fnum;
    String Email;
    String Name;
    String Phone;
    TextView male_number;
    TextView female_number;
    EditText email;
    EditText name;
    EditText phone;
    String rest_id;
    String offer_id;
    String date_id;
    String time;
    String Total_People;
    String Result;
    String rest_name;
    String rest_add;
    String dinner_name;
    String people;
    String booking_id;
    String date_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_2);
        male_number = (TextView) findViewById(R.id.male_number);
        female_number = (TextView) findViewById(R.id.female_number);
        email = (EditText) findViewById(R.id.guest_email);
        name = (EditText) findViewById(R.id.guest_name);
        phone = (EditText) findViewById(R.id.guest_mobile);

        getDataFromIntent();


    }

    private void getDataFromIntent() {
        if (getIntent() != null) {
            rest_id = getIntent().getStringExtra("rest_id");
            time = getIntent().getStringExtra("time");
            date_id = getIntent().getStringExtra("date");
        }

    }


    public void male_neg(View view) {
        if (minteger > 0) {
            minteger = minteger - 1;
            male_display(minteger);
        }
    }

    public void male_pos(View view) {
        minteger = minteger + 1;
        male_display(minteger);
    }

    public void female_neg(View view) {
        if (finteger > 0) {
            finteger = finteger - 1;
            female_display(finteger);
        }
    }

    public void female_pos(View view) {
        finteger = finteger + 1;
        female_display(finteger);
    }

    private void male_display(int number) {
        TextView male_dis = (TextView) findViewById(R.id.male_number);
        male_dis.setText("" + number);
    }

    private void female_display(int number) {
        TextView female_dis = (TextView) findViewById(R.id.female_number);
        female_dis.setText("" + number);
    }

    public void confirm(View view) {

        boolean isExecuteNext = true;
        Email = email.getText().toString();
        Name = name.getText().toString();
        Phone = phone.getText().toString();
        Mnum = Integer.parseInt(male_number.getText().toString());
        Fnum = Integer.parseInt(female_number.getText().toString());
        int People = (Mnum) + (Fnum);

        Total_People = "" + People;
        Log.d("Email", Email);
        Log.d("Name", Name);
        Log.d("Phone", Phone);
        // Log.d("Mnum", Mnum);
        // Log.d("Fnum", Fnum);
        // String str="name=amit&phone=9838467868&email=amit.axovel@gmail.com&booking_date=2017-09-15&booking_time=2130&rest_id=21335&people=2&male=1";

        String str = "{" + "\"" + "name" + "\"" + ":" + "\"" + Name + "\"" + "," + "\"" + "phone" + "\"" + ":" + "\"" + Phone + "\"" + "," + "\"" + "email" + "\"" + ":" + "\"" + Email + "\"" + "," + "\"" + "booking_date" + "\"" + ":" + "\"" + date_id + "\"" + "," + "\"" + "booking_time" + "\"" + ":" + "\"" + time.replace(":", "") + "\"" + "," + "\"" + "rest_id" + "\"" + ":" + "\"" + rest_id + "\"" + "," + "\"" + "people" + "\"" + ":" + "\"" + Total_People + "\"" + "," + "\"" + "male" + "\"" + ":" + "\"" + Mnum + "\"" + "}";

        String str_new = "name=" + Name + "&" + "phone=" + Phone + "&" + "email=" + Email + "&" + "booking_date=" + date_id + "&" + "booking_time=" + time.replace(":", "") + "&" + "rest_id=" + rest_id + "&" + "people=" + Total_People + "&" + "male=" + Mnum;
        Log.d("NewString", str_new);
        Log.d("New_String", str);

        if (Mnum == 0 && Fnum == 0) {
            Toast.makeText(this, "Please Enter No Of Male or Female", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Name.isEmpty()) {
            Log.d("Phone.length()", Phone.length() + "");
            name.setError(getResources().getString(R.string.Name_error));
            isExecuteNext = false;
        }

        if(Email.isEmpty()){
            email.setError(getResources().getString(R.string.enter_email));
        } else if (!Utils.isValidEmail(Email)) {
            email.setError(getResources().getString(R.string.enter_valid_email));
            isExecuteNext = false;
        }

        if (Phone.isEmpty()) {
            phone.setError(getResources().getString(R.string.Phone_error));
            isExecuteNext = false;
        } else if (Phone.length() < 10) {
            phone.setError(getResources().getString(R.string.Phone_length_validation));
            isExecuteNext = false;
        }

        if (!isExecuteNext) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.please_fill_all_required_field), Toast.LENGTH_SHORT).show();
            return;
        } else {
            JSONObject obj = new JSONObject();
            try {
                obj.put("name", Name);
                obj.put("phone", Phone);
                obj.put("email", Email);
                obj.put("booking_date", "2017-09-15");
                obj.put("booking_time", time.replace(":", ""));
                obj.put("rest_id", rest_id);
                obj.put("people", Total_People);
                obj.put("male", Mnum);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Result = sStringToHMACMD5(str, "e249c439ed7697df2a4b045d97d4b9b7e1854c3ff8dd668c779013653913572e");
            Log.d("hmacLast", Result);
            Log.d("String", obj.toString());


            Setdatato_Api(str_new);


        }
    }

    private void senddata() {
        Intent intent = new Intent(Booking_2Activity.this, Booking_DetailActivity.class);

        intent.putExtra("booking_id", booking_id);
        intent.putExtra("rest_name", rest_name);
        intent.putExtra("rest_add", rest_add);
        intent.putExtra("date_time", date_time);
        intent.putExtra("people", people);
        intent.putExtra("dinner_name", dinner_name);
        intent.putExtra("time", time);
        intent.putExtra("date", date_id);
        Log.d("value-", dinner_name);
        Log.d("booking_id-", booking_id);
        Log.d("rest_name-", rest_name);
        Log.d("rest_add-", rest_add);
        Log.d("people-", people);
        Log.d("dinner_name-", dinner_name);


        startActivity(intent);
    }

    private void Setdatato_Api(String urlStr) {
        MyProgressDialog.showPDialog(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://api.dineoutdeals.in/external_api/api_v3/generate_booking?" + urlStr, new Response.Listener<String>() {

           @Override
            public void onResponse(String response) {


                Log.d("amit ", response);
                try {
                    Object json = new JSONTokener(response).nextValue();
                    JSONObject jsonObject = (JSONObject) json;
                    JSONObject output = jsonObject.getJSONObject("output_params");
                    booking_id = output.getString("booking_id");
                    rest_name = output.getString("restaurant_name");
                    rest_add = output.getString("restaurant_address");
                    date_time = output.getString("dining_date_time");
                    people = output.getString("people");
                    dinner_name = output.getString("diner_name");
                    Log.d("value", dinner_name);
                    Log.d("booking_id", booking_id);
                    Log.d("rest_name", rest_name);
                    Log.d("rest_add", rest_add);
                    Log.d("people", people);
                    Log.d("dinner_name", dinner_name);
                    MyProgressDialog.hidePDialog();
                    senddata();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Volley..,.", "ErrorListener: " + error.getMessage());
//
//                Toast.makeText(getApplicationContext(), R.string.someting_went_wrong, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Auth-id", "1018");
                headers.put("Auth-key", "abcdef-12345-abcdef-12345");
                headers.put("Hash-key", Result);
                headers.put("cache-control", "no-cache");
                headers.put("postman-token", "8253d5c9-f111-18f5-ce99-35ad044b0361");
                return headers;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public void backButton2(View view) {
        finish();
    }
}
