package com.axovel.mytapzoapp.handler;

import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

/**
 * Created by Umesh Chauhan.
 */
public class Paytm {

    public static String getProductNameL2(AccessibilityNodeInfo source) {
        String productName = null;
        int childCount = source.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (source.getChild(i) != null) {
                if (source.getChild(i).getChildCount() > 0) {
                    productName = getProductNameL2(source.getChild(i));
                    Log.i("Paytm Product Name L2", productName+"");
                    // Got Product Name bypassing further traverse.
                    return productName;
                }
            }
            List<AccessibilityNodeInfo> findAccessibilityNodeInfosByViewId = source.findAccessibilityNodeInfosByViewId("net.one97.paytm:id/product_name");
            if (findAccessibilityNodeInfosByViewId.size() > 0) {
                AccessibilityNodeInfo parent = findAccessibilityNodeInfosByViewId.get(0);
                String str2 = parent.getText().toString();
                productName = str2;
            }
        }
        return productName;
    }
}
