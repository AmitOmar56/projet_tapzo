package com.axovel.mytapzoapp.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.axovel.mytapzoapp.R;
import com.axovel.mytapzoapp.customAdapter.TimeSheetRecyclerViewAdapter;
import com.axovel.mytapzoapp.customAdapter.VendarAppHorizontalAdapter;
import com.axovel.mytapzoapp.models.TimeSheetData;
import com.axovel.mytapzoapp.models.VendarDataList;
import com.axovel.mytapzoapp.utils.Utils;

import java.util.Date;

import io.mahendra.calendarview.widget.CalendarView;


public class RideLaterActivity extends AppCompatActivity {


    List<Calendar> timeSheetDataList;
    private CalendarView calendarView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_later);

        calendarView = (CalendarView) findViewById(R.id.cal);
        calendarView.setIsOverflowDateVisible(true);
        calendarView.setCurrentDay(new Date());

        RecyclerView timeRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_time);
        timeSheetDataList = getData();
        TimeSheetRecyclerViewAdapter timeSheetRecyclerViewAdapter = new TimeSheetRecyclerViewAdapter(getApplicationContext(), timeSheetDataList) {
            @Override
            protected void onTimePicksetAdapter(int position, String time) {

                Log.d("welcomeTime",time);

            }
        };

        LinearLayoutManager horizontalLayoutManagerapp = new LinearLayoutManager(RideLaterActivity.this, LinearLayoutManager.HORIZONTAL, false);
        timeRecyclerView.setLayoutManager(horizontalLayoutManagerapp);
        timeRecyclerView.setAdapter(timeSheetRecyclerViewAdapter);


        calendarView.setOnDateLongClickListener(new CalendarView.OnDateLongClickListener() {
            @Override
            public void onDateLongClick(@NonNull Date selectedDate) {

                //OnDateLongClick Action here

            }
        });


        calendarView.setOnMonthChangedListener(new CalendarView.OnMonthChangedListener() {
            @Override
            public void onMonthChanged(@NonNull Date monthDate) {

                //OnMonthChanged Action Here

            }
        });
        calendarView.setOnDateClickListener(new CalendarView.OnDateClickListener() {
            @Override
            public void onDateClick(@NonNull Date selectedDate) {

                //OnDateClick Action Here

                Log.d("date", "" + selectedDate);

            }
        });

        calendarView.setOnMonthTitleClickListener(new CalendarView.OnMonthTitleClickListener() {
            @Override
            public void onMonthTitleClick(@NonNull Date selectedDate) {

                // OnMonthTitleClick Action here

            }
        });
    }

    private List<Calendar> getData() {

        List<Calendar> timeData = new ArrayList<>();

       Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.AM_PM, Calendar.AM);

        int i = 1;

        while (i<=24*6) {

            Calendar calendarInner = Calendar.getInstance();
            calendarInner.setTime(calendar.getTime());

            timeData.add(calendarInner);
            calendar.add(Calendar.MINUTE, 10);

            i++;
        }
        return timeData;
    }
}