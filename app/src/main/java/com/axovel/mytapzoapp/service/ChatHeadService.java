package com.axovel.mytapzoapp.service;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.axovel.mytapzoapp.R;
import com.axovel.mytapzoapp.view.activity.ActivityServiceUiHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.Hashtable;
import java.util.Map;

/**
 * Created by Umesh Chauhan on 10-03-2016.
 * Axovel Private Limited
 */
public class ChatHeadService extends Service {

    private WindowManager windowManager;
    private RelativeLayout chatheadView, removeView;
    private ImageView chatheadImg, removeImg;
    private TextView txt1, txtNew, txtLoading;
    private int x_init_cord, y_init_cord, x_init_margin, y_init_margin;
    private Point szWindow = new Point();
    private boolean isLeft = true;
    private String sMsg = "";
    static private String dataResponse = null;

    public static String getDataResponse() {
        return dataResponse;
    }

    public static boolean isActive() {
        return active;
    }

    public static void setActive(boolean active) {
        ChatHeadService.active = active;
    }

    /**
     * Status of Result UI
     **/
    public static boolean active = false;

    @SuppressWarnings ("deprecation")

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        Log.d("UI SERVICE", "ChatHeadService.onCreate()");
    }

    private void handleStart() {

        try {
            windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

            removeView = (RelativeLayout) inflater.inflate(R.layout.uiservice_remove, null);
            WindowManager.LayoutParams paramRemove = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_TOAST, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, PixelFormat.TRANSLUCENT);
            paramRemove.gravity = Gravity.TOP | Gravity.LEFT;

            removeView.setVisibility(View.GONE);
            removeImg = (ImageView) removeView.findViewById(R.id.remove_img);
            windowManager.addView(removeView, paramRemove);

            chatheadView = (RelativeLayout) inflater.inflate(R.layout.uiservice_chathead, null);
            chatheadImg = (ImageView) chatheadView.findViewById(R.id.chathead_img);
            txtNew = (TextView) chatheadView.findViewById(R.id.txtLoadingComp);

            txtLoading = (TextView) chatheadView.findViewById(R.id.txtLoading);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                windowManager.getDefaultDisplay().getSize(szWindow);
            } else {
                int w = windowManager.getDefaultDisplay().getWidth();
                int h = windowManager.getDefaultDisplay().getHeight();
                szWindow.set(w, h);
            }

            WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_TOAST, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, PixelFormat.TRANSLUCENT);
            params.gravity = Gravity.TOP | Gravity.LEFT;
            params.x = 0;
            params.y = 250;
            windowManager.addView(chatheadView, params);

        } catch (Exception e) {
            e.printStackTrace();
        }

        chatheadView.setOnTouchListener(new View.OnTouchListener() {

            long time_start = 0, time_end = 0;
            boolean isLongclick = false, inBounded = false;
            int remove_img_width = 0, remove_img_height = 0;

            Handler handler_longClick = new Handler();
            Runnable runnable_longClick = new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    Log.d("UI SERVICE", "Into runnable_longClick");

                    isLongclick = true;
                    removeView.setVisibility(View.VISIBLE);
                    chathead_longclick();
                }
            };

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) chatheadView.getLayoutParams();

                int x_cord = (int) event.getRawX();
                int y_cord = (int) event.getRawY();
                int x_cord_Destination, y_cord_Destination;

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        time_start = System.currentTimeMillis();
                        handler_longClick.postDelayed(runnable_longClick, 600);

                        remove_img_width = removeImg.getLayoutParams().width;
                        remove_img_height = removeImg.getLayoutParams().height;

                        x_init_cord = x_cord;
                        y_init_cord = y_cord;

                        x_init_margin = layoutParams.x;
                        y_init_margin = layoutParams.y;

                        break;
                    case MotionEvent.ACTION_MOVE:
                        int x_diff_move = x_cord - x_init_cord;
                        int y_diff_move = y_cord - y_init_cord;

                        x_cord_Destination = x_init_margin + x_diff_move;
                        y_cord_Destination = y_init_margin + y_diff_move;

                        if (isLongclick) {
                            int x_bound_left = szWindow.x / 2 - (int) (remove_img_width * 1.5);
                            int x_bound_right = szWindow.x / 2 + (int) (remove_img_width * 1.5);
                            int y_bound_top = szWindow.y - (int) (remove_img_height * 1.5);

                            if ((x_cord >= x_bound_left && x_cord <= x_bound_right) && y_cord >= y_bound_top) {
                                inBounded = true;

                                int x_cord_remove = (int) ((szWindow.x - (remove_img_height * 1.5)) / 2);
                                int y_cord_remove = (int) (szWindow.y - ((remove_img_width * 1.5) + getStatusBarHeight()));

                                if (removeImg.getLayoutParams().height == remove_img_height) {
                                    removeImg.getLayoutParams().height = (int) (remove_img_height * 1.5);
                                    removeImg.getLayoutParams().width = (int) (remove_img_width * 1.5);

                                    WindowManager.LayoutParams param_remove = (WindowManager.LayoutParams) removeView.getLayoutParams();
                                    param_remove.x = x_cord_remove;
                                    param_remove.y = y_cord_remove;

                                    windowManager.updateViewLayout(removeView, param_remove);
                                }

                                layoutParams.x = x_cord_remove + (Math.abs(removeView.getWidth() - chatheadView.getWidth())) / 2;
                                layoutParams.y = y_cord_remove + (Math.abs(removeView.getHeight() - chatheadView.getHeight())) / 2;

                                windowManager.updateViewLayout(chatheadView, layoutParams);
                                break;
                            } else {
                                inBounded = false;
                                removeImg.getLayoutParams().height = remove_img_height;
                                removeImg.getLayoutParams().width = remove_img_width;

                                WindowManager.LayoutParams param_remove = (WindowManager.LayoutParams) removeView.getLayoutParams();
                                int x_cord_remove = (szWindow.x - removeView.getWidth()) / 2;
                                int y_cord_remove = szWindow.y - (removeView.getHeight() + getStatusBarHeight());

                                param_remove.x = x_cord_remove;
                                param_remove.y = y_cord_remove;

                                windowManager.updateViewLayout(removeView, param_remove);
                            }

                        }

                        layoutParams.x = x_cord_Destination;
                        layoutParams.y = y_cord_Destination;

                        windowManager.updateViewLayout(chatheadView, layoutParams);
                        break;
                    case MotionEvent.ACTION_UP:
                        isLongclick = false;
                        removeView.setVisibility(View.GONE);
                        removeImg.getLayoutParams().height = remove_img_height;
                        removeImg.getLayoutParams().width = remove_img_width;
                        handler_longClick.removeCallbacks(runnable_longClick);

                        if (inBounded) {
                            if (ActivityServiceUiHandler.active) {
                                ActivityServiceUiHandler.myDialog.finish();
                            }

                            stopService(new Intent(ChatHeadService.this, ChatHeadService.class));
                            inBounded = false;
                            break;
                        }

                        int x_diff = x_cord - x_init_cord;
                        int y_diff = y_cord - y_init_cord;
                        Log.i("Values", x_diff + " " + y_diff);
                        if (Math.abs(x_diff) < 14 && Math.abs(y_diff) < 14) {
                            time_end = System.currentTimeMillis();
                            if ((time_end - time_start) < 300) {
                                chathead_click();
                            }
                        }

                        y_cord_Destination = y_init_margin + y_diff;

                        int BarHeight = getStatusBarHeight();
                        if (y_cord_Destination < 0) {
                            y_cord_Destination = 0;
                        } else if (y_cord_Destination + (chatheadView.getHeight() + BarHeight) > szWindow.y) {
                            y_cord_Destination = szWindow.y - (chatheadView.getHeight() + BarHeight);
                        }
                        layoutParams.y = y_cord_Destination;

                        inBounded = false;
                        resetPosition(x_cord);

                        break;
                    default:
                        Log.d("UI SERVICE", "chatheadView.setOnTouchListener  -> event.getAction() : default");
                        break;
                }
                return true;
            }
        });

        try {

            WindowManager.LayoutParams paramsTxt = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_PHONE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, PixelFormat.TRANSLUCENT);
            paramsTxt.gravity = Gravity.TOP | Gravity.LEFT;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                windowManager.getDefaultDisplay().getSize(szWindow);
            } else {
                int w = windowManager.getDefaultDisplay().getWidth();
                int h = windowManager.getDefaultDisplay().getHeight();
                szWindow.set(w, h);
            }

            WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) chatheadView.getLayoutParams();

            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                Log.d("UI SERVICE", "ChatHeadService.onConfigurationChanged -> landscap");

                if (layoutParams.y + (chatheadView.getHeight() + getStatusBarHeight()) > szWindow.y) {
                    layoutParams.y = szWindow.y - (chatheadView.getHeight() + getStatusBarHeight());
                    windowManager.updateViewLayout(chatheadView, layoutParams);
                }

                if (layoutParams.x != 0 && layoutParams.x < szWindow.x) {
                    resetPosition(szWindow.x);
                }

            } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                Log.d("UI SERVICE", "ChatHeadService.onConfigurationChanged -> portrait");

                if (layoutParams.x > szWindow.x) {
                    resetPosition(szWindow.x);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetPosition(int x_cord_now) {

        try {
            if (x_cord_now <= szWindow.x / 2) {
                isLeft = true;
                moveToLeft(x_cord_now);
            } else {
                isLeft = false;
                moveToRight(x_cord_now);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void moveToLeft(final int x_cord_now) {

        try {
            final int x = szWindow.x - x_cord_now;

            new CountDownTimer(500, 5) {
                WindowManager.LayoutParams mParams = (WindowManager.LayoutParams) chatheadView.getLayoutParams();

                public void onTick(long t) {
                    long step = (500 - t) / 5;
                    mParams.x = 0 - (int) (double) bounceValue(step, x);
                    try {
                        windowManager.updateViewLayout(chatheadView, mParams);
                    } catch (Exception e) {
                        e.printStackTrace();
                        // Toast.makeText(getBaseContext(), "Please Wait..!!", Toast.LENGTH_SHORT).show();
                    }
                }

                public void onFinish() {
                    mParams.x = 0;
                    try {
                        windowManager.updateViewLayout(chatheadView, mParams);
                    } catch (Exception e) {
                        e.printStackTrace();
                        // Toast.makeText(getBaseContext(), "Please Wait..!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void moveToRight(final int x_cord_now) {

        try {
            new CountDownTimer(500, 5) {
                WindowManager.LayoutParams mParams = (WindowManager.LayoutParams) chatheadView.getLayoutParams();

                public void onTick(long t) {
                    long step = (500 - t) / 5;
                    mParams.x = szWindow.x + (int) (double) bounceValue(step, x_cord_now) - chatheadView.getWidth();
                    try {
                        windowManager.updateViewLayout(chatheadView, mParams);
                    } catch (Exception e) {
                        e.printStackTrace();
                        // Toast.makeText(getBaseContext(), "Please Wait..!!", Toast.LENGTH_SHORT).show();
                    }
                }

                public void onFinish() {
                    mParams.x = szWindow.x - chatheadView.getWidth();
                    try {
                        windowManager.updateViewLayout(chatheadView, mParams);
                    } catch (Exception e) {
                        e.printStackTrace();
                        // Toast.makeText(getBaseContext(), "Please Wait..!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double bounceValue(long step, long scale) {
        return scale * Math.exp(-0.055 * step) * Math.cos(0.08 * step);
    }

    private int getStatusBarHeight() {
        return ((int) Math.ceil(25 * getApplicationContext().getResources().getDisplayMetrics().density));
    }

    //    private String newFlipkartUrl = null;

    private void chathead_click() {

        Log.i("Click", ChatHeadService.isActive() + "");
        try {
            if (txtNew.getVisibility() == View.VISIBLE && dataResponse != null) {
                txtNew.setVisibility(View.GONE);
            }
            // If Still getting the data
            if (dataResponse == null) {
                txtLoading.setText(getApplicationContext().getResources().getString(R.string.pop_msg_still_trying));
                txtLoading.setVisibility(View.VISIBLE);
            }

            //            if (newFlipkartUrl != null) {
            //                Intent intent = new Intent(Intent.ACTION_VIEW);
            //                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //                intent.setData(Uri.parse(newFlipkartUrl));
            //                startActivity(intent);
            //            }

            if (ChatHeadService.isActive()) {
                ActivityServiceUiHandler.myDialog.finish();
            } else {
                Log.i("Response", ChatHeadService.getDataResponse() + "");
                if (ChatHeadService.getDataResponse() != null) {
                    Intent it = new Intent(this, ActivityServiceUiHandler.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    it.putExtra("response", ChatHeadService.getDataResponse());
                    startActivity(it);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    private void getUniqueProductFlipkart() {
    //
    //        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://163.172.63.19/getProductDetail5.php", new Response.Listener<String>() {
    //            @Override
    //            public void onResponse(String response) {
    //
    //                Log.d("Response data >>>", response);
    //
    //                try {
    //                    Object json = new JSONTokener(response).nextValue();
    //                    if (json instanceof JSONObject) {
    //
    //                        JSONObject jsonObject = (JSONObject) json;
    //
    //                        if (jsonObject.getString("status").equals("success")) {
    //
    //                            JSONArray jsonArray = jsonObject.getJSONArray("response");
    //                            if (jsonArray.length() > 0) {
    //                                newFlipkartUrl = jsonArray.getJSONObject(0).getString("aff_link");
    //                            }
    //                        }
    //                    } else {
    //                    }
    //                } catch (JSONException e) {
    //                    e.printStackTrace();
    //                }
    //            }
    //        }, new Response.ErrorListener() {
    //            @Override
    //            public void onErrorResponse(VolleyError error) {
    //                Log.d("Volley..,.", "ErrorListener: " + error.getMessage());
    //            }
    //        }) {
    //            @Override
    //            protected Map<String, String> getParams() throws AuthFailureError {
    //
    //                Map<String, String> map = new Hashtable<>();
    //                map.put("product_name", /*"Apple iPhone 6 (Space Grey, 16 GB)"*/ pref.getString("flipAppName", ""));
    //
    //                Log.d("MAP>.,.", map.toString());
    //
    //                return map;
    //            }
    //        };
    //
    //        // Adding request to request queue
    //        addToRequestQueue(stringRequest);
    //    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setShouldCache(false);
        req.setTag("tag");
        req.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES * 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getApplicationContext()).add(req);
    }

    private void chathead_longclick() {

        Log.d("UI SERVICE", "Into ChatHeadService.chathead_longclick() ");

        try {
            WindowManager.LayoutParams param_remove = (WindowManager.LayoutParams) removeView.getLayoutParams();
            int x_cord_remove = (szWindow.x - removeView.getWidth()) / 2;
            int y_cord_remove = szWindow.y - (removeView.getHeight() + getStatusBarHeight());

            param_remove.x = x_cord_remove;
            param_remove.y = y_cord_remove;

            windowManager.updateViewLayout(removeView, param_remove);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(), "Please Wait..!!", Toast.LENGTH_SHORT).show();
        }
    }

    private SharedPreferences pref;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        Log.d("UI SERVICE", "ChatHeadService.onStartCommand() -> startId=" + startId);
        // active = true;

        pref = getApplicationContext().getSharedPreferences("MyPref", Activity.MODE_MULTI_PROCESS);

        try {
            if (intent != null && txtNew != null) {
                // Getting response
                if (intent.hasExtra("response")) {
                    dataResponse = (String) intent.getExtras().get("response");
                    if (intent.hasExtra("type")) {
                        if (intent.getExtras().get("type").toString().equals("APPS")) {
                            txtNew.setText(getApplicationContext().getResources().getString(R.string.post_pop_msg_apps));
                        } else if (intent.getExtras().get("type").toString().equals("COUPONS")) {
                            txtNew.setText(getApplicationContext().getResources().getString(R.string.post_pop_msg_coupons));
                        }
                    } else { // Price Comparision
                        txtNew.setText(getApplicationContext().getResources().getString(R.string.post_pop_msg_price_comp));
                    }
                    txtNew.setVisibility(View.VISIBLE);
                    txtLoading.setVisibility(View.GONE);
                } else {
                    if (txtLoading != null) {
                        txtLoading.setVisibility(View.VISIBLE);
                        dataResponse = null;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (startId == Service.START_STICKY) {
            handleStart();
            //            getUniqueProductFlipkart();
            return super.onStartCommand(intent, flags, startId);
        } else {
            return Service.START_NOT_STICKY;
        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        // active = false;

        try {
            ChatHeadService.dataResponse = null;
            if (chatheadView != null) {
                windowManager.removeView(chatheadView);
            }

            if (removeView != null) {
                windowManager.removeView(removeView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        Log.d("UI SERVICE", "ChatHeadService.onBind()");
        return null;
    }

}
