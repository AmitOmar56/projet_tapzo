package com.axovel.mytapzoapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.axovel.mytapzoapp.R;
import com.axovel.mytapzoapp.customAdapter.FoodPagerAdapter;
import com.axovel.mytapzoapp.customAdapter.Food_ListAdapter;

public class FoodScreen2Activity extends AppCompatActivity {
    ListView list;
    String[] itemname ={
            "axca",
            "adc",
            "csd",
            "erwsc",
            "ewfcs",
            "awezcsx",
            "weacs",
            "aewfsd",
            "axca",
            "adc",
            "csd",
            "erwsc",
            "ewfcs",
            "awezcsx",
            "weacs",
            "aewfsd",
            "axca",
            "adc",
            "csd",
            "erwsc",
            "ewfcs",
            "awezcsx",
            "weacs",
            "aewfsd",
            "axca",
            "adc",
            "csd",
            "erwsc",
            "ewfcs",
            "awezcsx",
            "weacs",
            "aewfsd"
    };
    Integer[] imgid={
            R.drawable.aquarius_icon,
            R.drawable.aries_icon,
            R.drawable.taurus__icon,
            R.drawable.gemini_icon,
            R.drawable.cancer_icon,
            R.drawable.leo_icon,
            R.drawable.virgo_icon,
            R.drawable.libra_icon,
            R.drawable.aquarius_icon,
            R.drawable.aries_icon,
            R.drawable.taurus__icon,
            R.drawable.gemini_icon,
            R.drawable.cancer_icon,
            R.drawable.leo_icon,
            R.drawable.virgo_icon,
            R.drawable.libra_icon,
            R.drawable.aquarius_icon,
            R.drawable.aries_icon,
            R.drawable.taurus__icon,
            R.drawable.gemini_icon,
            R.drawable.cancer_icon,
            R.drawable.leo_icon,
            R.drawable.virgo_icon,
            R.drawable.libra_icon,
            R.drawable.aquarius_icon,
            R.drawable.aries_icon,
            R.drawable.taurus__icon,
            R.drawable.gemini_icon,
            R.drawable.cancer_icon,
            R.drawable.leo_icon,
            R.drawable.virgo_icon,
            R.drawable.libra_icon,
    };

    FoodPagerAdapter foodPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_screen2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Food_ListAdapter adapter=new Food_ListAdapter(this, itemname, imgid);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String Slecteditem= itemname[+position];
                Toast.makeText(getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();


            }
        });

//        LayoutInflater inflater = getLayoutInflater();
//        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.activity_food_layout, list, false);
//        list.addHeaderView(header, null, false);
    }
}
