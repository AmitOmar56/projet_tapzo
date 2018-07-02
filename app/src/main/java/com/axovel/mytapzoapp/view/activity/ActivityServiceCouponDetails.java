package com.axovel.mytapzoapp.view.activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.axovel.mytapzoapp.R;
import com.axovel.mytapzoapp.utils.General;

/**
 * Created by Umesh Chauhan on 7/15/2016.
 */
public class ActivityServiceCouponDetails extends Activity {

    Activity act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_coupon_details);
        act = this;
        final Intent intent = getIntent();
        TextView txtCompany = ((TextView) findViewById(R.id.txtCompanyName));
        TextView txtDecs = ((TextView) findViewById(R.id.txtDecs));
        TextView btnClose = (TextView) findViewById(R.id.btnClose);
        txtDecs.setText(intent.getExtras().getString("couponDescription"));
        txtCompany.setText(intent.getExtras().getString("companyName"));
        txtCompany.setTypeface(General.getAppDefaultTypeface(this));
        txtDecs.setTypeface(General.getAppDefaultTypeface(this));
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final String coupon = intent.getExtras().getString("coupon");
        findViewById(R.id.txtBtnGetOffer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(coupon, coupon);
                if(!coupon.isEmpty() && !coupon.equals("")) {
                    Toast.makeText(act, "Coupon copied to clip board.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(act, "No Coupon required.", Toast.LENGTH_SHORT).show();
                }
                clipboard.setPrimaryClip(clip);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(intent.getExtras().getString("url")));
                act.startActivity(browserIntent);
            }
        });
    }
}
