package com.axovel.mytapzoapp.customAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.axovel.mytapzoapp.R;
import com.axovel.mytapzoapp.activities.RideLaterListActivity;
import com.axovel.mytapzoapp.models.CabDataList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by axovel on 9/13/2017.
 */

public abstract class UpcommingRidesRecyclerViewAdapter extends  RecyclerView.Adapter<UpcommingRidesRecyclerViewAdapter.MysingleRideLaterViewHolder> {

    private static Context context;
    private ArrayList<CabDataList> rideLaterDataList;


    public UpcommingRidesRecyclerViewAdapter(Context context, ArrayList<CabDataList> rideLaterDataList) {
        this.context = context;
        this.rideLaterDataList = rideLaterDataList;
    }

    @Override
    public MysingleRideLaterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.upcomming_layout, parent, false);

        return new UpcommingRidesRecyclerViewAdapter.MysingleRideLaterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MysingleRideLaterViewHolder holder, int position) {
        
        holder.onBindData(position);

    }

  

    @Override
    public int getItemCount() {
        return rideLaterDataList.size();
    }


    public class MysingleRideLaterViewHolder extends RecyclerView.ViewHolder {
        private TextView cabDisplayText;
        private TextView pickUpAddress;
        private TextView destination_AddressText;
        private TextView date_timeText;
        private TextView cancelBtn;
        private ImageView cabImageView;



        public MysingleRideLaterViewHolder(View itemView) {
            super(itemView);

            cabImageView= (ImageView) itemView.findViewById(R.id.upcomingRideImageView);
            cabDisplayText= (TextView) itemView.findViewById(R.id.upcomingCabName);
            pickUpAddress= (TextView) itemView.findViewById(R.id.pickUpText);
            destination_AddressText= (TextView) itemView.findViewById(R.id.destinationText);
            cancelBtn= (TextView) itemView.findViewById(R.id.cancelUpcomingRides);
            date_timeText= (TextView) itemView.findViewById(R.id.dateAndtimeText);
        }

        public void onBindData(final int position) {

            cabDisplayText.setText(rideLaterDataList.get(position).getDisplayNmae());
            pickUpAddress.setText(rideLaterDataList.get(position).getPickUP_Address());
            destination_AddressText.setText(rideLaterDataList.get(position).getDestination_Address());
            date_timeText.setText(rideLaterDataList.get(position).getDate_rideLater()+rideLaterDataList.get(position).getTime_rideLater());
            Picasso.with(context).load(rideLaterDataList.get(position).getCab_Icon_image()).into(cabImageView);



            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    removeRideLaterList(position);
                }
            });
        }
    }
    protected abstract void removeRideLaterList(int Position);
}
