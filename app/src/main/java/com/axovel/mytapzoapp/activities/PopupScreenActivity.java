package com.axovel.mytapzoapp.activities;

import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.CompoundButton;

import com.axovel.mytapzoapp.R;
import com.axovel.mytapzoapp.utils.Constants;
import com.axovel.mytapzoapp.utils.PreferenceStore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PopupScreenActivity extends AppCompatActivity {


    private SwitchCompat amazon, flipkart, paytm, shopclues, shopping, freecharge, vending, zomato, snapdeal, mobikwik;

    private List<String> list = new ArrayList<String>();
    private Set<String> set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_screen);
        Set<String> set1 = PreferenceStore.getPreference(getApplicationContext()).getSet(Constants.DATASET);
        if(set1==null)
        {
            return;
        }
        List<String> list1 = new ArrayList<>(set1);
        Log.d("welcome priyesh ",list1.toString());


        freecharge = (SwitchCompat) findViewById(R.id.switchButtonfreecharge);
        freecharge.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    list.add("com.freecharge.android");
                    list.contains("com.freecharge.android");
                } else {
                    list.remove("com.freecharge.android");
                }



            }


        });


        if(list1.contains("com.freecharge.android")){


                freecharge.setChecked(true);

    }





        flipkart = (SwitchCompat) findViewById(R.id.switchButtonflipcart);
        flipkart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                Log.d("Switch State=", "" + b);

                if (b) {
                    list.add("com.flipkart.android");
                } else {
                    list.remove("com.flipkart.android");
                }


            }
        });
        if(list1.contains("com.flipkart.android")){


                flipkart.setChecked(true);

        }


        amazon = (SwitchCompat) findViewById(R.id.switchButtonamazon);
        amazon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    list.add("in.amazon.mShop.android.shopping");
                } else {
                    list.remove("in.amazon.mShop.android.shopping");
                }

            }
        });
        if(list1.contains("in.amazon.mShop.android.shopping")){

                amazon.setChecked(true);

        }



        shopclues = (SwitchCompat) findViewById(R.id.switchButtonshopclues);
        shopclues.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    list.add("com.shopclues");
                } else {
                    list.remove("com.shopclues");
                }

            }
        });

        if(list1.contains("com.shopclues")){


                shopclues.setChecked(true);

        }

        shopping = (SwitchCompat) findViewById(R.id.switchButtonshopping);
        shopping.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                if (b) {
                    list.add("com.shopping");
                } else {
                    list.remove("com.shopping");
                }
            }
        });
        if(list1.contains("com.shopping")){


                shopping.setChecked(true);

        }




        vending = (SwitchCompat) findViewById(R.id.switchButtonvending);
        vending.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                if (b) {
                    list.add("com.android.vending");
                } else {
                    list.remove("com.android.vending");
                }


            }
        });

        if(list1.contains("com.android.vending")){


                vending.setChecked(true);

        }


        zomato = (SwitchCompat) findViewById(R.id.switchButtonzomato);
        zomato.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                if (b) {
                    list.add("com.application.zomato");
                } else {
                    list.remove("com.application.zomato");
                }


            }
        });
        if(list1.contains("com.application.zomato")){


                zomato.setChecked(true);

        }


        snapdeal = (SwitchCompat) findViewById(R.id.switchButtonSnapdeal);
        snapdeal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                if (b) {
                    list.add("com.snapdeal.main");
                } else {
                    list.remove("com.snapdeal.main");
                }


            }
        });

        if(list1.contains("com.snapdeal.main")){


                snapdeal.setChecked(true);

        }


        mobikwik = (SwitchCompat) findViewById(R.id.switchButtonmobikwik);
        mobikwik.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                if (b) {
                    list.add("com.mobikwik_new");
                } else {
                    list.remove("com.mobikwik_new");
                }

            }
        });
        if(list1.contains("com.mobikwik_new")){


                mobikwik.setChecked(true);

        }



        paytm = (SwitchCompat) findViewById(R.id.switchButtonpaytm);
        paytm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                if (b) {
                    list.add("net.one97.paytm");
                } else {
                    list.remove("net.one97.paytm");
                }
            }
        });

        if(list1.contains("net.one97.paytm")){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                paytm.setChecked(true);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        set = new HashSet<>(list);

        Log.d("SET..........", set.toString());

        PreferenceStore.getPreference(getApplicationContext()).putSet(Constants.DATASET, set);
    }


    public void backButton(View view) {
        finish();
    }
}
