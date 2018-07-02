package com.axovel.mytapzoapp.handler;

import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

/**
 * Created by Umesh Chauhan.
 */
public class Shopclues {

    public static String getPrice(AccessibilityNodeInfo source, int level){
        if(source!=null){
            int childCount = source.getChildCount();
            for(int i=0;i<childCount;i++) {
                if(source.getChild(i)!=null){
                    if (source.getChild(i).getClassName().equals("android.widget.TextView") && source.getChild(i).getText()!=null) {
                        if (source.getChild(i).getText().toString().contains("₹") && !source.getChild(i).getText().toString().contains("₹ ") && source.getChild(i).isVisibleToUser()) {
                            Log.i("Shopc Price L3-1", source.getChild(i).getText().toString() + " Level:" + level + " Res:" + source.getChild(i).getViewIdResourceName() +" i="+i);

                            if(i != 0 && source.getChild(i-1) != null && source.getChild(i-1).getText() !=null){
                                // Log.i("Shopc Name L3", source.getChild(i-1).getText().toString() + " Level:" + level + " Res:" + source.getChild(i-1).getViewIdResourceName() +" i="+(i-1));
                            }
                            if(childCount>(i+1) && source.getChild(i+1) != null && source.getChild(i+1).getText() != null &&  source.getChild(i+1).getText().toString().contains("₹") ){
                                // Log.i("Shopc Discou Price L3", source.getChild(i+1).getText().toString() + " Level:" + level + " Res:" + source.getChild(i+1).getViewIdResourceName() +" i="+(i+1));
                            }
                            return source.getChild(i).getText().toString();
                        }
                    }
                    if (source.getChild(i).getChildCount() > 0) {
                        String price = getPrice(source.getChild(i), (level+1));
                        if(price!=null){
                            return price;
                        }
                    }
                }
            }
        }
        return null;
    }

    public static String getProductNameL2(AccessibilityNodeInfo source) {
        String productName = null;
        int childCount = source.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (source.getChild(i) != null) {
                if (source.getChild(i).getChildCount() > 0) {
                    productName = getProductNameL2(source.getChild(i));
                    Log.i("Shopc Product Name L2", productName+"");
                    // Got Product Name bypassing further traverse.
                    return productName;
                }
            }
            List<AccessibilityNodeInfo> findAccessibilityNodeInfosByViewId = source.findAccessibilityNodeInfosByViewId("com.shopclues:id/product_name");
            if (findAccessibilityNodeInfosByViewId.size() > 0) {
                AccessibilityNodeInfo parent = findAccessibilityNodeInfosByViewId.get(0);
                String str2 = parent.getText().toString();
                productName = str2;
            }
        }
        return productName;
    }

    public static String[] getProductNameL3(AccessibilityNodeInfo source, int level) {
        String[] details = null;
        int childCount = source.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (source.getChild(i) != null) {
                if (source.getChild(i).getText() != null && source.getChild(i).getText().toString().contains("₹") && source.getChild(i).getClassName().equals("android.widget.TextView") &&
                        source.getChild(i).getParent()!=null && source.getChild(i).getParent().getClassName().equals("android.widget.ScrollView") &&
                        source.getChild(i).getParent().getParent()!=null && source.getChild(i).getParent().getParent().getClassName().equals("android.widget.RelativeLayout") &&
                        source.getChild(i).getParent().getParent().getParent()!=null && source.getChild(i).getParent().getParent().getParent().getClassName().equals("android.view.ViewGroup") &&
                        source.getChild(i).getParent().getParent().getParent().getParent()!=null && source.getChild(i).getParent().getParent().getParent().getParent().getClassName().equals("android.support.v4.widget.DrawerLayout") &&
                        source.getChild(i).getParent().getParent().getParent().getParent().getParent()!=null && source.getChild(i).getParent().getParent().getParent().getParent().getParent().getClassName().equals("android.widget.FrameLayout")) {
                    details = new String[4];
                    Log.i("Shopc Price L3", source.getChild(i).getText().toString() + " Level:" + level + " Res:" + source.getChild(i).getViewIdResourceName() +" i="+i);
                    // AccessibilityUtil.printParent(source.getChild(i));
                    // Product Price
                    details[1] = source.getChild(i).getText().toString();
                    if(i != 0 && source.getChild(i-1) != null && source.getChild(i-1).getText() !=null && !source.getChild(i-1).getText().toString().contains("₹")){
                        Log.i("Shopc Name L3", source.getChild(i-1).getText().toString() + " Level:" + level + " Res:" + source.getChild(i-1).getViewIdResourceName() +" i="+(i-1));
                        // Product Name
                        details[0] = source.getChild(i-1).getText().toString();
                        // AccessibilityUtil.printParent(source.getChild(i-1));
                    }else if(i>2 && source.getChild(i-2) != null && source.getChild(i-2).getText() !=null && !source.getChild(i-2).getText().toString().contains("₹")){
                        Log.i("Shopc Name L3", source.getChild(i-2).getText().toString() + " Level:" + level + " Res:" + source.getChild(i-2).getViewIdResourceName() +" i="+(i-2));
                        // Product Name
                        details[0] = source.getChild(i-2).getText().toString();
                    }
                    if(childCount>(i+1) && source.getChild(i+1) != null && source.getChild(i+1).getText() != null &&  source.getChild(i+1).getText().toString().contains("₹") ){
                        Log.i("Shopc Discou Price L3", source.getChild(i+1).getText().toString() + " Level:" + level + " Res:" + source.getChild(i+1).getViewIdResourceName() +" i="+(i+1));
                        // Product Discounted Prices
                        details[2] = source.getChild(i+1).getText().toString();
                    }
                    if(childCount>(i+2) && source.getChild(i+2) != null && source.getChild(i+2).getText() != null &&  source.getChild(i+2).getText().toString().contains("₹") ){
                        Log.i("Shopc Discou + Price L3", source.getChild(i+2).getText().toString() + " Level:" + level + " Res:" + source.getChild(i+2).getViewIdResourceName() +" i="+(i+2));
                        // Product Discounted Prices
                        details[3] = source.getChild(i+2).getText().toString();
                    }
                    if(details[0] != null && !details[0].isEmpty()) {
                        return details;
                    }
                }
                if(source.getChild(i).getChildCount()>0){
                    details = getProductNameL3(source.getChild(i), (level+1));
                    if(details != null && details[0] != null && !details[0].isEmpty() ){
                        return details;
                    }
                }
            }
        }
        return details;
    }
}
