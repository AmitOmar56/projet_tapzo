package com.axovel.mytapzoapp.handler;

import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Umesh Chauhan.
 */
public class IndiaTimesShopping {

    public static String getPrice(AccessibilityNodeInfo parent, AccessibilityEvent event, int level){
        int sourceSize = parent.getChildCount();
        for(int i = 0; i<sourceSize;i++){
            String price = "";
            if(parent.getChild(i).getContentDescription()!=null) {
                price = parent.getChild(i).getContentDescription().toString();
            }
            Pattern p = Pattern.compile("\\₹.*?\\₹");
            Matcher m = p.matcher(price);
            if(m.find())
                return m.group().subSequence(1, m.group().length()-1).toString();

            if(parent.getChild(i).getChildCount()>0){
                price = getPrice(parent.getChild(i), event, ++level);
                if(price!=null) {
                    return price;
                }
            }
        }
        return null;
    }
}
