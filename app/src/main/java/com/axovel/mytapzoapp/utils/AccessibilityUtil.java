package com.axovel.mytapzoapp.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

/**
 * Created by Umesh Chauhan.
 */
public class AccessibilityUtil {

    public static void printParent(AccessibilityNodeInfo parent){
        if(parent!=null){
            Log.i("Parent", parent.getClassName().toString() +" Content Description: "+parent.getContentDescription()+" ResoID "+parent.getViewIdResourceName());
            printParent(parent.getParent());
        }
    }

    public static void printChild(AccessibilityNodeInfo source, int level){
        int childCount = source.getChildCount();
        for(int i=0;i<childCount;i++){
            if(source.getChild(i) != null && source.getChild(i).getText() != null) {
                Log.i("String Child", source.getChild(i).getText() + " Position:" + i + "Level:" + level);
                // AccessibilityUtil.printParent(source.getChild(i));
            }
            if(source.getChild(i)!=null && source.getChild(i).getChildCount()>0){
                printChild(source.getChild(i), (level+1));
            }
            if(i==0){
                Log.i("String Parent", source.getText()+"Level:" + level);
            }
        }
        if(childCount==0){
            Log.i("String Parent-No Child", source.getText()+"Level:" + level);
        }
    }

    public static void printChildWithDescription(AccessibilityNodeInfo source, int level){
        int childCount = source.getChildCount();
        for(int i=0;i<childCount;i++){
            if(source.getChild(i) != null && source.getChild(i).getContentDescription() != null) {
                Log.i("String Child", source.getChild(i).getContentDescription() + " Position:" + i + "Level:" + level);
                AccessibilityUtil.printParent(source.getChild(i));
            }
            if(source.getChild(i)!=null && source.getChild(i).getChildCount()>0){
                printChild(source.getChild(i), (level+1));
            }
            if(i==0){
                Log.i("String Parent", source.getContentDescription()+"Level:" + level);
                AccessibilityUtil.printParent(source);
            }
        }
        if(childCount==0){
            Log.i("String Parent-No Child", source.getContentDescription()+"Level:" + level);
            AccessibilityUtil.printParent(source);
        }
    }

    public static boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
