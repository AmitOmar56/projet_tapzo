package com.axovel.mytapzoapp.handler;

import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

/**
 * Created by umeshchauhan on 17/09/16.
 */
public class PlayStore {

    public static String getAppName(AccessibilityNodeInfo source, int level) {
        if (source != null) {
            int childCount = source.getChildCount();
            String name;
            for (int i = 0; i < childCount; i++) {
                if (source.getChild(i) != null) {

                    if (source.getChild(i).getText() != null) {
                        name = source.getChild(i).getText().toString();
                        name = name.trim();
                        if (!name.isEmpty()) {
                            // Log.i("App", "" + name + " " + level + " " + i);
                            // AccessibilityUtil.printParent(source.getChild(i));
                            if (source.getChild(i).getClassName().equals("android.widget.TextView")
                                    && source.getChild(i).getParent() != null && (source.getChild(i).getParent().getClassName().equals("android.view.View") || source.getChild(i).getParent().getClassName().equals("android.view.ViewGroup"))
                                    && source.getChild(i).getParent().getParent() != null && source.getChild(i).getParent().getParent().getClassName().equals("android.support.v7.widget.RecyclerView")
                                    && source.getChild(i).getParent().getParent().getParent() != null && source.getChild(i).getParent().getParent().getParent().getClassName().equals("android.widget.FrameLayout")
                                    && source.getChild(i).getParent().getParent().getParent().getParent() != null && source.getChild(i).getParent().getParent().getParent().getParent().getClassName().equals("android.support.v4.widget.DrawerLayout")
                                    && source.getChild(i).getParent().getParent().getParent().getParent().getParent() != null && source.getChild(i).getParent().getParent().getParent().getParent().getParent().getClassName().equals("android.widget.FrameLayout")) {

                                Log.i("App", "" + name + " " + level + " " + i);
                                // AccessibilityUtil.printParent(source.getChild(i));
                                return name;
                            }
                        }
                    }
                    if (source.getChild(i).getChildCount() > 0) {
                        name = getAppName(source.getChild(i), (level + 1));
                        if (name != null) {
                            return name;
                        }
                    }
                }
            }
        }
        return null;
    }
}
