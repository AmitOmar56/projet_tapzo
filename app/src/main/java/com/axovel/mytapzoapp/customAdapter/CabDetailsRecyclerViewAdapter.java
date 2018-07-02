package com.axovel.mytapzoapp.customAdapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.axovel.mytapzoapp.R;
import com.axovel.mytapzoapp.activities.ConfirmationScreenActivity;
import com.axovel.mytapzoapp.models.CabDataList;
import com.axovel.mytapzoapp.models.Offers_product_Data;
import com.axovel.mytapzoapp.utils.Constants;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;


/**
 * Created by axovel on 8/28/2017.
 */

public abstract class CabDetailsRecyclerViewAdapter extends RecyclerView.Adapter<CabDetailsRecyclerViewAdapter.MySingleCabDataList> implements Constants {


    List<CabDataList> cabDataList = Collections.emptyList();
    Context context;


    public CabDetailsRecyclerViewAdapter(List<CabDataList> cabDataList, Context context) {
        this.cabDataList = cabDataList;
        this.context = context;
    }

    @Override
    public MySingleCabDataList onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cab_details_layout, parent, false);

        return new CabDetailsRecyclerViewAdapter.MySingleCabDataList(itemView);
    }

    @Override
    public void onBindViewHolder(MySingleCabDataList holder, int position) {
        holder.onBindData(position);

    }

    @Override
    public int getItemCount() {
        return cabDataList.size();
    }

    public class MySingleCabDataList extends RecyclerView.ViewHolder {

        private ItemClickListner itemClickListener;
        private ImageView cabImage = null;
        private TextView displayNameCab = null;
        private TextView avilityCab = null;
        private TextView basePriseCab = null;
        private TextView cabEstimateTime = null;

        public MySingleCabDataList(View itemView) {
            super(itemView);


            cabImage = (ImageView) itemView.findViewById(R.id.cabImageValue);
            displayNameCab = (TextView) itemView.findViewById(R.id.cab_display_Name);
            avilityCab = (TextView) itemView.findViewById(R.id.cabAvilityText);
            basePriseCab = (TextView) itemView.findViewById(R.id.cabBasePrice);
            cabEstimateTime = (TextView) itemView.findViewById(R.id.cabMinTime);
        }

        public void onBindData(final int position) {


            if (cabDataList.get(position).getCheckDest() == WITHOUT_DESTINATION_CODE) {

                String image=cabDataList.get(position).getCab_Icon_image();
                Log.d("image",image);
                Glide.with(context).load(cabDataList.get(position).getCab_Icon_image()).into(cabImage);
            } else if (cabDataList.get(position).getCheckDest() == WITH_DESTINATION_CODE) {

                Log.d("priyesh Image jpg", cabDataList.get(position).getCab_Icon_image());
                String image = cabDataList.get(position).getCab_Icon_image();
                Glide.with(context).load(image).into(cabImage);

            }

            displayNameCab.setText(cabDataList.get(position).getDisplayNmae());


            if (cabDataList.get(position).getIsCheck().equals("true")) {
                avilityCab.setText("Available");


            } else {
                avilityCab.setVisibility(View.INVISIBLE);

            }


            if (cabDataList.get(position).getCheckDest() == WITH_DESTINATION_CODE) {

                if(cabDataList.get(position).getDisplayNmae().equals("Share")){

                    basePriseCab.setText("₹" +cabDataList.get(position).getCostShare());



                }else {
                    basePriseCab.setText("₹" + cabDataList.get(position).getMinimumFare() + "-" + cabDataList.get(position).getMaximumFare());

                }
            } else if (cabDataList.get(position).getCheckDest() == WITHOUT_DESTINATION_CODE) {

                if(cabDataList.get(position).getDisplayNmae().equals("Share")){

                    basePriseCab.setText(cabDataList.get(position).getCostShare());



                }else {
                    basePriseCab.setText("₹" + cabDataList.get(position).getCostPerDistance());
                }

            }


            cabEstimateTime.setText("" + cabDataList.get(position).getEstimateTime() + "minute");

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


//                    if () {
//
////                        if (displayNameCab.getText().toString().trim().equals("Mini")) {
                    Log.d("share DisplayNAMe",displayNameCab.getText().toString().trim());


//
                            onLOginPopup(position,cabDataList.get(position).getCabType(),cabEstimateTime.getText().toString().trim(),cabDataList.get(position).getCab_Icon_image(),basePriseCab.getText().toString().trim(),displayNameCab.getText().toString().trim());

//                        }

//                        if(10==6){
//
//                        }else {
//
//                            if(cabDataList.get(position).getDisplayNmae().equals("Share")){
//
//                                onSharePopup(position);
//
//                            }else {
//                                Intent intent = new Intent(context, ConfirmationScreenActivity.class);
//                                intent.putExtra(DISPLAY_CAB_NAME, cabDataList.get(position).getIdCab());
//                                context.startActivity(intent);
//                            }
//                        }
//
//                    }




                }
            });


        }


    }

    protected abstract void onLOginPopup(int position, String cabType,String estimateTime,String cabImage,String baseFare,String display_Name);

}
