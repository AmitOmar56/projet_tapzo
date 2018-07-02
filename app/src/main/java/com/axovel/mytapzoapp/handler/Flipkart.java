package com.axovel.mytapzoapp.handler;

import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

/**
 * Created by Umesh Chauhan.
 */
public class Flipkart {

    public static String[] getProductDetails(AccessibilityNodeInfo source, int level) {
        if (source != null) {
            if (source.getChildCount() > 0) {
                if (source.getChild(0) != null) {
                    if (source.getChild(0).getClassName().equals("android.widget.TextView") && source.getChild(0).getParent() != null && source.getChild(0).getParent().isVisibleToUser() && source.getChild(0).getParent().getClassName().equals("android.widget.LinearLayout") && source.getChild(0).getParent().getParent() != null && source.getChild(0).getParent().getParent().getClassName().equals("android.widget.RelativeLayout")) {
                        if (source.getChildCount() > 2) {
                            if (source.getChild(2) != null) {
                                if (source.getChild(2).getClassName().equals("android.widget.TextView")) {
                                    Log.i("Flip Source Child Count", source.getChildCount() + "");
                                    if (source.getChildCount() > 3 && source.getChild(0).getText() != null && source.getChild(2).getText() != null && !source.getChild(0).getText().equals("Special Price") && !source.getChild(0).getText().equals("₹")) {
                                        //
                                        String productPrice = source.getChild(2).getText().toString();
                                        productPrice = productPrice.replace("₹", "");
                                        if (productPrice.contains(",")) {
                                            productPrice = productPrice.replace(",", "");
                                        }
                                        String regex = "[0-9]+";
                                        if (!productPrice.matches(regex)) {
                                            String[] data = getFlipkartDatav3(source, 0);
                                            if (data != null) {
                                                return data;
                                            }
                                        }
                                        //
                                        Log.i("Flip Product Name", source.getChild(0).getText().toString() + level);
                                        Log.i("Flip Product Price", productPrice + level);
                                        // AccessibilityUtil.printChild(source, 0);
                                        return new String[]{source.getChild(0).getText().toString(), productPrice};
                                    }
                                }
                            }
                        }
                    }
                }
            }

            int childCount = source.getChildCount();
            for (int i = 0; i < childCount; i++) {
                String[] data = getProductDetails(source.getChild(i), (level + 1));
                if (data != null) {
                    return data;
                }
            }
        }
        return null;
    }

    public static String[] getProductDetailsv2(AccessibilityNodeInfo source, int level, int previousPos) {
        if (source != null) {
            int childCount = source.getChildCount();
            if (childCount > 0) {
                for (int i = 0; i < childCount; i++) {
                    if (source.getChild(i) != null) {
                        if (source.getChild(i).getText() != null) {
                            if (childCount >= 3 && source.getChild(2) != null && source.getChild(2).getChild(0) != null && source.getChild(2).getChild(0).getText() != null && source.getChild(2).getChild(0).getText().toString().contains("₹") && source.getChild(2).getChild(1) != null && source.getChild(2).getChild(1).getText() != null) {
                                Log.i("Flip Product Name v2", source.getChild(i).getText().toString());
                                Log.i("Flip Product Price v2", source.getChild(2).getChild(1).getText().toString());
                                return new String[]{source.getChild(i).getText().toString(), source.getChild(2).getChild(1).getText().toString()};
                            } else if (childCount >= 2 && source.getChild(1) != null && source.getChild(1).getChild(0) != null && source.getChild(1).getChild(0).getText() != null && source.getChild(1).getChild(0).getText().toString().contains("₹") && source.getChild(1).getChild(1) != null && source.getChild(1).getChild(1).getText() != null) {
                                Log.i("Flip Product Name v2", source.getChild(i).getText().toString());
                                Log.i("Flip Product Price v2", source.getChild(1).getChild(1).getText().toString());
                                return new String[]{source.getChild(i).getText().toString(), source.getChild(1).getChild(1).getText().toString()};
                            }
                        }
                        if (source.getChild(i).getText() != null) {
                            // Log.i("Flip Log v2", source.getChild(i).getText().toString()+ " Level: "+level+" CurrentChild: "+i+" PreviousChild: "+previousPos);
                        }
                        int childChildCount = source.getChild(i).getChildCount();
                        if (childChildCount > 0) {
                            String[] data = getProductDetailsv2(source.getChild(i), (level + 1), i);
                            if (data != null) {
                                return data;
                            }
                        }
                    }
                }
            }

        }
        return null;
    }

    private static String[] getFlipkartDatav3(AccessibilityNodeInfo source, int level) {
        if (source != null) {
            int childCount = source.getChildCount();
            if (childCount > 0) {
                for (int i = 0; i < childCount; i++) {
                    if (source.getChild(i) != null) {
                        if (source.getChild(i) != null && source.getChild(i).getClassName().toString().equals("android.widget.TextView") && source.getChild(i).getText() != null && (i + 1) < childCount && source.getChild(i + 1).getChildCount() > 1 && source.getChild(i + 1).getChild(1) != null && source.getChild(i + 1).getChild(1).getClassName().toString().equals("android.widget.TextView") && source.getChild(i + 1).getChild(1).getText() != null) {
                            Log.i("Flip Product Name v3", source.getChild(i).getText().toString());
                            Log.i("Flip Product Price v3", source.getChild(i + 1).getChild(1).getText().toString());
                            return new String[]{source.getChild(i).getText().toString(), source.getChild(i + 1).getChild(1).getText().toString()};
                        }
                        int childChildCount = source.getChild(i).getChildCount();
                        if (childChildCount > 0) {
                            String[] data = getProductDetailsv2(source.getChild(i), (level + 1), i);
                            if (data != null) {
                                return data;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public static String[] getFlipkartDatav4(AccessibilityNodeInfo source, int level) {
        if (source != null) {
            int childCount = source.getChildCount();
            for (int i = 0; i < childCount; i++) {
                if ((source.getChild(i) != null && source.getChild(i).getClassName().equals("android.widget.TextView")) && (source.getChild(i).getParent() != null && source.getChild(i).getParent().getClassName().equals("android.widget.LinearLayout")) && (source.getChild(i).getParent().getParent() != null && source.getChild(i).getParent().getParent().getClassName().equals("android.widget.RelativeLayout")) && (source.getChild(i).getParent().getParent().getParent() != null && source.getChild(i).getParent().getParent().getParent().getClassName().equals("android.support.v7.widget.RecyclerView")) && (source.getChild(i).getParent().getParent().getParent().getParent() != null && source.getChild(i).getParent().getParent().getParent().getParent().getClassName().equals("android.support.v4.view.ViewPager")) && (source.getChild(i).getParent().getParent().getParent().getParent().getParent() != null && source.getChild(i).getParent().getParent().getParent().getParent().getParent().getClassName().equals("android.widget.FrameLayout")) && (source.getChild(i).getParent().getParent().getParent().getParent().getParent().getParent() != null && source.getChild(i).getParent().getParent().getParent().getParent().getParent().getParent().getClassName().equals("android.support.v4.widget.DrawerLayout")) && (source.getChild(i).getParent().getParent().getParent().getParent().getParent().getParent().getParent() != null && source.getChild(i).getParent().getParent().getParent().getParent().getParent().getParent().getParent().getClassName().equals("android.widget.FrameLayout"))) {

                    String productName = source.getChild(i).getText().toString().trim();
                    Log.i("Flip Product Name v4", productName);

                    boolean isWrongName = false;

                    if ((productName.length() == 8 || productName.length() == 9) && productName.contains("Colors")) {
                        isWrongName = true;
                    }

                    if (!productName.equals("Offers") && !isWrongName) {
                        return new String[]{source.getChild(i).getText().toString(), null};
                    }
                }
                //                    int childChildCount = source.getChild(i).getChildCount();
                //                    if (childChildCount > 0) {
                String[] data = getFlipkartDatav4(source.getChild(i), (level + 1));
                if (data != null) {
                    return data;
                }
                //                    }
            }
        }
        return null;
    }

    // To print tree of all children and sub children and so on..
    public static String printPageLayoutStructureForward(AccessibilityNodeInfo source, int level) {

        if (source != null) {

            StringBuilder builder = new StringBuilder();

            builder.append(getSpaceAccToLevel(level));
            builder.append(level);
            builder.append("  " + source.getClassName());

            if (source.getContentDescription() != null) {
                builder.append("  " + source.getContentDescription().toString());
            }

            if (source.getText() != null) {
                builder.append("  " + source.getText().toString());
            }

            Log.i("AILayoutInfo ++++++", builder.toString());

            // Counting Children..
            int childCount = source.getChildCount();
            for (int i = 0; i < childCount; i++) {
                AccessibilityNodeInfo child = source.getChild(i);
                if (child != null) {
                    //                    if (level < 5) {
                    printPageLayoutStructureForward(child, (level + 1));
                    //                    }
                }
            }
        }
        return null;
    }

    // To print parent, grand parent and so on..
    public static String printPageLayoutStructureBackward(AccessibilityNodeInfo source, int level) {

        if (source != null) {

            StringBuilder builder = new StringBuilder();

            builder.append(getSpaceAccToLevel(Math.abs(level)));
            builder.append(level);
            builder.append("  " + source.getClassName());

            if (source.getContentDescription() != null) {
                builder.append("  " + source.getContentDescription().toString());
            }

            if (source.getText() != null) {
                builder.append("  " + source.getText().toString());
            }

            Log.i("AILayoutInfo ++++++", builder.toString());

            AccessibilityNodeInfo parent = source.getParent();
            if (parent != null) {
                printPageLayoutStructureBackward(parent, (level - 1));
            }
        }
        return null;
    }

    private static String getSpaceAccToLevel(int level) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < level; i++) {
            builder.append("+----");
        }
        return builder.toString();
    }
}
