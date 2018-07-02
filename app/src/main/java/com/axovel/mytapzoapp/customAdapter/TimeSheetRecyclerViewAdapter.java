package com.axovel.mytapzoapp.customAdapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.axovel.mytapzoapp.R;
import com.axovel.mytapzoapp.models.TimeSheetData;
import com.axovel.mytapzoapp.models.VendarDataList;
import com.axovel.mytapzoapp.utils.Utils;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Created by axovel on 9/8/2017.
 */

public abstract class TimeSheetRecyclerViewAdapter extends RecyclerView.Adapter<TimeSheetRecyclerViewAdapter.MySingleViewAdapterViewHolder> {

    List<Calendar> timeSheetDataList = Collections.emptyList();
    Context context;

    public TimeSheetRecyclerViewAdapter(Context context, List<Calendar> timeSheetDataList) {

        this.context = context;
        this.timeSheetDataList = timeSheetDataList;

    }

    @Override
    public MySingleViewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_sheet_layout, parent, false);

        return new TimeSheetRecyclerViewAdapter.MySingleViewAdapterViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MySingleViewAdapterViewHolder holder, int position) {

        holder.onBindData(position);

    }

    @Override
    public int getItemCount() {
        return timeSheetDataList.size();
    }

    public class MySingleViewAdapterViewHolder extends RecyclerView.ViewHolder {

        private TextView time_Text, time_Session;

        public MySingleViewAdapterViewHolder(View itemView) {
            super(itemView);
//            time_Session=itemView.findViewById(R.id.timeSession);
            time_Text = (TextView) itemView.findViewById(R.id.timeText);


        }

        public void onBindData(final int position) {


            final String time = Utils.getManagedTimeString12(timeSheetDataList.get(position).get(Calendar.HOUR), timeSheetDataList.get(position).get(Calendar.MINUTE), timeSheetDataList.get(position).get(Calendar.AM_PM));

            Log.d("hour", "" + timeSheetDataList.get(position).get(Calendar.HOUR));
            time_Text.setText(time);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (position == getAdapterPosition()) {

                        Toast.makeText(context, time_Text.getText().toString().trim(), Toast.LENGTH_SHORT).show();
                        onTimePicksetAdapter(position, time);
                    }
//
                }
            });


        }
    }

    protected abstract void onTimePicksetAdapter(int position, String time);
}
