package com.axovel.mytapzoapp.customAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.axovel.mytapzoapp.R;
import com.axovel.mytapzoapp.models.Booking;

import java.util.List;

/**
 * Created by Soft1 on 07-Sep-17.
 */

public abstract class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.MyViewHolder> {

    private final Context context;
    private List<Booking> bookingList;
    private View viewbooking;

    public BookingAdapter(List<Booking> bookingList, Context context) {
        this.context = context;
        this.bookingList = bookingList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.booking_list, parent, false);



        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Booking booking = bookingList.get(position);
        holder.title.setText(booking.getDay());
        holder.about.setText(booking.getDate());
        holder.onBindData(position);
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title, about;
        public LinearLayout booking_linear_id;
        public boolean id = false;
        private int position = 0;

        public MyViewHolder(View view) {
            super(view);

            getId(itemView);
            //handleListener();

        }

//        private void handleListener() {
//            booking_linear_id.setOnClickListener(this);
//        }

        private void getId(View itemView) {
            title = (TextView) itemView.findViewById(R.id.booking_title);
            about = (TextView) itemView.findViewById(R.id.booking_review);
            booking_linear_id = (LinearLayout) itemView.findViewById(R.id.booking_linear_id);
        }

        public void onBindData(final int position) {

            if(bookingList.get(position).isSelected()){
                booking_linear_id.setBackgroundColor(context.getResources().getColor(R.color.fair_color_red));

            } else {
                booking_linear_id.setBackgroundColor(0xffffffff);

            }

            booking_linear_id.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    onItemClick(position);
                }
            });
        }

//        @Override
//        public void onClick(View view) {
//            switch (view.getId()) {
//                case R.id.booking_linear_id:
//
//                    position=getAdapterPosition();
//                    booking_linear_id.setBackgroundColor(context.getResources().getColor(R.color.fair_color_red));
//                    break;
//            }
//        }
    }

    protected abstract void onItemClick(int position);
}
