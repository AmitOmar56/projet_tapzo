package com.axovel.mytapzoapp.handler;

import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

/**
 * Created by Umesh Chauhan.
 */
public class AmazonIndia {

    public static String getProductName(AccessibilityNodeInfo source, int level) {
        if (source != null) {
            int childCount = source.getChildCount();
            for (int i = 0; i < childCount; i++) {
                if (source.getChild(i) != null) {
                    if (source.getChild(i).getContentDescription() != null) {
                        String name = source.getChild(i).getContentDescription().toString();
                        name = name.trim();
                        if (level == 1 && !name.isEmpty()) {
                            if (!name.contains("Purchased on")) {
                                return name;
                            }
                        }
                        if (!name.isEmpty() && source.getChild(i).getViewIdResourceName() != null && source.getChild(i).getViewIdResourceName().equals("title")) {
                            return name;
                        }
                        if (i == 0 && source.getChild(i).getChildCount() > 0) {
                            if (level < 1) {
                                name = getProductName(source.getChild(i), (level + 1));
                                if (name != null) {
                                    if (!name.contains("Purchased on")) {
                                        return name;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    /*
    *
    * Deprecated
    *
     */
    public static String[] getAmazonProductDetails(AccessibilityNodeInfo source, int level) {
        String[] details = null;
        int childCount = source.getChildCount();
        Log.i("AIndia-ChildCount", childCount + "");
        for (int i = 0; i < childCount; i++) {
            if (source.getChild(i) != null && source.getChild(i).getContentDescription() != null && source.getChild(i).getClassName().equals("android.view.View") && source.getChild(i).getParent() == null) {
                String productName = source.getChild(i).getContentDescription().toString();
                if (productName.contains(".from ₹ ")) {
                    details = productName.split(".from ₹ ");
                    Log.i("AIndia Details-[]", productName + " " + details.length + " " + details[0] + " " + details[1]);
                    return details;
                }
            }
            if (source.getChild(i) != null && source.getChild(i).getChildCount() > 0) {
                details = getAmazonProductDetails(source.getChild(i), (level + 1));
                if (details != null) {
                    return details;
                }
            }
        }

        return details;
    }

    public static String getProductPriceWithID(AccessibilityNodeInfo source) {
        // Trying to get product price by id
        String productPrice = null;
        int childCount = source.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (source.getChild(i) != null) {
                if (source.getChild(i).getChildCount() > 0) {
                    productPrice = getProductPriceWithID(source.getChild(i));

                    // Got Product Price bypassing further traverse.
                    return productPrice;
                }
            }
            List<AccessibilityNodeInfo> findAccessibilityNodeInfosByViewId = source.findAccessibilityNodeInfosByViewId("priceblock_dealprice");
            if (findAccessibilityNodeInfosByViewId.size() > 0) {
                AccessibilityNodeInfo parent = findAccessibilityNodeInfosByViewId.get(0);
                if (parent.getText() != null) {
                    productPrice = parent.getText().toString();
                    Log.i("AIndia Product PriceID1", productPrice + "");
                }
            }
        }
        /*List<AccessibilityNodeInfo> findAccessibilityNodeInfosByViewId = source.findAccessibilityNodeInfosByViewId("in.amazon.mShop.android.shopping:id/priceblock_dealprice");
        if (findAccessibilityNodeInfosByViewId.size() > 0) {
            AccessibilityNodeInfo parent = findAccessibilityNodeInfosByViewId.get(0);
            if(parent.getText() != null) {
                productPrice = parent.getText().toString();
                Log.i("AIndia Product PriceID1", productPrice+"");
            }
        }*/
        return productPrice;
    }

    public static String getProductPrice(AccessibilityNodeInfo source, int level, String type) {

        String productPrice = null;
        //        Log.i("AIndia", "Level 1 - Extraction Price ");
        if (source != null) {
            // Counting Childs
            int childCount = source.getChildCount();
            for (int i = 0; i < childCount; i++) {
                if (source.getChild(i) != null && source.getChild(i).getContentDescription() != null) {
                    if (level == 2 && i == 0 && source.getChild(i).getClassName().equals("android.widget.EditText") && source.getChild(i).getParent() != null && source.getChild(i).getParent().getClassName().equals("android.view.View") && source.getChild(i).getParent().getParent() != null && source.getChild(i).getParent().getParent().getClassName().equals("android.view.View") && source.getChild(i).getParent().getParent().getParent() != null && source.getChild(i).getParent().getParent().getParent().getClassName().equals("android.webkit.WebView") && source.getChild(i).getParent().getParent().getParent().getParent() != null && source.getChild(i).getParent().getParent().getParent().getParent().getClassName().equals("android.webkit.WebView") && source.getChild(i).getParent().getParent().getParent().getParent().getParent() != null && source.getChild(i).getParent().getParent().getParent().getParent().getParent().getClassName().equals("android.widget.LinearLayout") && source.getChild(i).getParent().getParent().getParent().getParent().getParent().getParent() != null && source.getChild(i).getParent().getParent().getParent().getParent().getParent().getParent().getClassName().equals("android.support.v4.widget.DrawerLayout") && source.getChild(i).getParent().getParent().getParent().getParent().getParent().getParent().getParent() != null && source.getChild(i).getParent().getParent().getParent().getParent().getParent().getParent().getParent().getClassName().equals("android.widget.FrameLayout")) {
                        //                        Log.i("Child", "" + source.getChild(i).getContentDescription() + " " + level + " " + i);
                        // AccessibilityUtil.printParent(source.getChild(i));
                        productPrice = source.getChild(i).getContentDescription().toString();
                        Log.i("AIndia ProductPrice 2>>", productPrice);
                        return productPrice;
                    }
                    int child = source.getChild(i).getChildCount();
                    if (child > 0) {
                        productPrice = getProductPrice(source.getChild(i), (level + 1), "Level One Extraction");
                        if (productPrice != null) {
                            return productPrice;
                        }
                    }
                }

                //                if (i == 0 && source.getContentDescription() != null) {
                //                    Log.i("Parent", "" + source.getContentDescription() + " " + level + " " + i);
                //                }
            }
        }
        return productPrice;
    }

    public static String getProductPrice(AccessibilityNodeInfo source, int level) {
        String productPrice = null;
        if (source != null) {
            // Counting Childs
            int childCount = source.getChildCount();
            for (int i = 0; i < childCount; i++) {
                // Level One Test
                if (level == 1 && source.getChild(i) != null && source.getChild(i).getClassName().equals("android.view.View") && source.getChild(i).getParent().getClassName().equals("android.view.View") && source.getChild(i).getParent().getParent().getClassName().equals("android.webkit.WebView") && source.getChild(i).getParent().getParent().getParent().getClassName().equals("android.webkit.WebView") && source.getChild(i).getParent().getParent().getParent().getParent().getClassName().equals("android.widget.LinearLayout") && source.getChild(i).getParent().getParent().getParent().getParent().getParent().getClassName().equals("android.support.v4.widget.DrawerLayout") && source.getChild(i).getParent().getParent().getParent().getParent().getParent().getParent().getClassName().equals("android.widget.FrameLayout")) {
                    if (source.getChild(i).getContentDescription() != null) {
                        productPrice = source.getChild(i).getContentDescription().toString();
                        Log.i("AIndia ProductPrice 3>>", productPrice);
                        if (productPrice.contains(".") && !productPrice.contains("₹")) {
                            productPrice = productPrice.trim();
                            try {
                                productPrice = productPrice.replaceAll("\\s+", "");
                            } catch (Exception e) {
                                // Error Handling
                            }
                            // Formatting Price
                            if (productPrice.matches("[0-9,.]+")) {
                                String[] splitedPrice = productPrice.split("\\.");
                                if (splitedPrice.length > 0) {
                                    if (splitedPrice[0] != null) {
                                        if (splitedPrice[0].contains(",")) {
                                            splitedPrice[0] = splitedPrice[0].replace(",", "");
                                        }
                                        if (splitedPrice[0].matches("[0-9]+")) {
                                            productPrice = splitedPrice[0];
                                            if (getPriceAmazon(source, i, childCount) != null) {
                                                if (getOfferPriceAmazon(source, i, childCount) != null) {
                                                    if (getDealPriceAmazon(source, i, childCount) != null) {
                                                        productPrice = getDealPriceAmazon(source, i, childCount);
                                                    } else {
                                                        productPrice = getOfferPriceAmazon(source, i, childCount);
                                                    }
                                                } else {
                                                    productPrice = getPriceAmazon(source, i, childCount);
                                                }
                                            }
                                            //                                            Log.i("Product Price L1-1", productPrice + " " + i);
                                            return productPrice;
                                        }
                                    }
                                }
                            }
                        } else if (productPrice.contains("₹")) {
                            productPrice = productPrice.replace("₹", "");
                            productPrice = productPrice.trim();
                            try {
                                productPrice = productPrice.replaceAll("\\s+", "");
                            } catch (Exception e) {
                                // Handling Errors
                            }
                            if (productPrice.endsWith("00")) {
                                productPrice = productPrice.substring(0, productPrice.length() - 2);
                            }
                            if (productPrice.contains(",")) {
                                productPrice = productPrice.replace(",", "");
                            }
                            if (productPrice.matches("[0-9]+")) {
                                //                                Log.i("Product Price L1-2", productPrice);
                                return productPrice;
                            }
                        }
                    }
                }
                // Level two Test
                if (level == 2 && source.getChild(i) != null && source.getChild(i).getClassName().equals("android.view.View") && source.getChild(i).getParent() != null && source.getChild(i).getParent().getClassName().equals("android.view.View") && source.getChild(i).getParent().getParent() != null && source.getChild(i).getParent().getParent().getClassName().equals("android.view.View") && source.getChild(i).getParent().getParent().getParent() != null && source.getChild(i).getParent().getParent().getParent().getClassName().equals("android.webkit.WebView") && source.getChild(i).getParent().getParent().getParent().getParent() != null && source.getChild(i).getParent().getParent().getParent().getParent().getClassName().equals("android.webkit.WebView") && source.getChild(i).getParent().getParent().getParent().getParent().getParent() != null && source.getChild(i).getParent().getParent().getParent().getParent().getParent().getClassName().equals("android.widget.LinearLayout") && source.getChild(i).getParent().getParent().getParent().getParent().getParent().getParent() != null && source.getChild(i).getParent().getParent().getParent().getParent().getParent().getParent().getClassName().equals("android.support.v4.widget.DrawerLayout") && source.getChild(i).getParent().getParent().getParent().getParent().getParent().getParent().getParent() != null && source.getChild(i).getParent().getParent().getParent().getParent().getParent().getParent().getParent().getClassName().equals("android.widget.FrameLayout")) {
                    if (source.getChild(i).getContentDescription() != null) {
                        productPrice = source.getChild(i).getContentDescription().toString();
                        Log.i("AIndia ProductPrice 3>>", productPrice);
                        if (productPrice.contains(".") && !productPrice.contains("₹")) {
                            productPrice = productPrice.trim();
                            if (productPrice.matches("[0-9,.]+")) {
                                String[] splitedPrice = productPrice.split("\\.");
                                if (splitedPrice.length > 0) {
                                    if (splitedPrice[0] != null) {
                                        if (splitedPrice[0].contains(",")) {
                                            splitedPrice[0] = splitedPrice[0].replace(",", "");
                                        }
                                        if (splitedPrice[0].matches("[0-9]+")) {
                                            productPrice = splitedPrice[0];
                                            //                                            Log.i("Product Price L2", productPrice);
                                            return productPrice;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (source.getChild(i) != null && source.getChild(i).getChildCount() > 0) {
                    productPrice = getProductPrice(source.getChild(i), (level + 1));
                    if (productPrice != null) {
                        return productPrice;
                    }
                }
            }
        }
        return null;
    }

    public static String getProductPricev2(AccessibilityNodeInfo source, int level) {
        String productPrice = null;
        if (source != null) {
            // Counting Childs
            int childCount = source.getChildCount();
            for (int i = 0; i < childCount; i++) {
                // Level One Test
                if ((source.getChild(i) != null && source.getChild(i).getClassName().equals("android.view.View")) && source.getChild(i).getParent().getClassName().equals("android.view.View") && source.getChild(i).getParent().getParent().getClassName().equals("android.webkit.WebView") && source.getChild(i).getParent().getParent().getParent().getClassName().equals("android.webkit.WebView") && (source.getChild(i).getParent().getParent().getParent().getParent().getClassName().equals("android.widget.LinearLayout") || source.getChild(i).getParent().getParent().getParent().getParent().getClassName().equals("android.widget.RelativeLayout")) && source.getChild(i).getParent().getParent().getParent().getParent().getParent().getClassName().equals("android.support.v4.widget.DrawerLayout") && source.getChild(i).getParent().getParent().getParent().getParent().getParent().getParent().getClassName().equals("android.widget.FrameLayout")) {
                    if (source.getChild(i).getContentDescription() != null) {
                        productPrice = source.getChild(i).getContentDescription().toString();
                        Log.i("AIndia ProductPrice 4>>", productPrice);
                        productPrice = productPrice.trim();
                        try {
                            productPrice = productPrice.replaceAll("\\s+", "");
                        } catch (Exception e) {
                            // Error Handling
                            e.printStackTrace();
                        }
                        // Formatting Price
                        if (productPrice.matches("[0-9,.]+")) {
                            String[] splitedPrice = productPrice.split("\\.");
                            if (splitedPrice.length > 0) {
                                if (splitedPrice[0] != null) {
                                    if (splitedPrice[0].contains(",")) {
                                        splitedPrice[0] = splitedPrice[0].replace(",", "");
                                    }
                                    if (splitedPrice[0].matches("[0-9]+")) {
                                        productPrice = splitedPrice[0];
                                        if (getPriceAmazon(source, i, childCount) != null) {
                                            if (getOfferPriceAmazon(source, i, childCount) != null) {
                                                if (getDealPriceAmazon(source, i, childCount) != null) {
                                                    productPrice = getDealPriceAmazon(source, i, childCount);
                                                } else {
                                                    productPrice = getOfferPriceAmazon(source, i, childCount);
                                                }
                                            } else {
                                                productPrice = getPriceAmazon(source, i, childCount);
                                            }
                                        }
                                        //                                        Log.i("Product Pricev2 L1-1", productPrice + " " + i);
                                        return productPrice;
                                    }
                                }
                            }

                        } else if (productPrice.matches("[0-9]+")) {
                            if (getPriceAmazon(source, i, childCount) != null) {
                                if (getOfferPriceAmazon(source, i, childCount) != null) {
                                    if (getDealPriceAmazon(source, i, childCount) != null) {
                                        productPrice = getDealPriceAmazon(source, i, childCount);
                                    } else {
                                        productPrice = getOfferPriceAmazon(source, i, childCount);
                                    }
                                } else {
                                    productPrice = getPriceAmazon(source, i, childCount);
                                }
                            }
                            //                            Log.i("Product Pricev2 L1-2", productPrice + " " + i);
                            return productPrice;
                        }
                    }
                }
                if (source.getChild(i) != null && source.getChild(i).getChildCount() > 0) {
                    productPrice = getProductPricev2(source.getChild(i), (level + 1));
                    if (productPrice != null) {
                        return productPrice;
                    }
                }
            }
        }
        return productPrice;
    }

    public static String getProductPricev3(AccessibilityNodeInfo source, int level) {
        String productPrice = null;
        if (source != null) {
            int childCount = source.getChildCount();
            for (int i = 0; i < childCount; i++) {
                if ((source.getChild(i) != null && source.getChild(i).getClassName().equals("android.widget.EditText")) && (source.getChild(i).getParent() != null && source.getChild(i).getParent().getClassName().equals("android.view.View")) && (source.getChild(i).getParent().getParent() != null && source.getChild(i).getParent().getParent().getClassName().equals("android.view.View")) && (source.getChild(i).getParent().getParent().getParent() != null && source.getChild(i).getParent().getParent().getParent().getClassName().equals("android.webkit.WebView")) && (source.getChild(i).getParent().getParent().getParent().getParent() != null && source.getChild(i).getParent().getParent().getParent().getParent().getClassName().equals("android.webkit.WebView")) && (source.getChild(i).getParent().getParent().getParent().getParent().getParent() != null && source.getChild(i).getParent().getParent().getParent().getParent().getParent().getClassName().equals("android.widget.RelativeLayout")) && (source.getChild(i).getParent().getParent().getParent().getParent().getParent().getParent() != null && source.getChild(i).getParent().getParent().getParent().getParent().getParent().getParent().getClassName().equals("android.support.v4.widget.DrawerLayout")) && (source.getChild(i).getParent().getParent().getParent().getParent().getParent().getParent().getParent() != null && source.getChild(i).getParent().getParent().getParent().getParent().getParent().getParent().getParent().getClassName().equals("android.widget.FrameLayout"))) {
                    if (source.getChild(i).getContentDescription() != null) {
                        productPrice = source.getChild(i).getContentDescription().toString();
                        Log.i("AIndia ProductPrice 5>>", productPrice);
                        productPrice = productPrice.trim();
                        try {
                            productPrice = productPrice.replaceAll("\\s+", "");
                            productPrice = productPrice.replace("rupees", "");
                            productPrice = productPrice.replace("rupee", "");
                        } catch (Exception e) {
                            // Error Handling
                            e.printStackTrace();
                        }
                        // Formatting Price
                        if (productPrice.matches("[0-9,.]+")) {
                            String[] splitedPrice = productPrice.split("\\.");
                            if (splitedPrice.length > 0) {
                                if (splitedPrice[0] != null) {
                                    if (splitedPrice[0].contains(",")) {
                                        splitedPrice[0] = splitedPrice[0].replace(",", "");
                                    }
                                    if (splitedPrice[0].matches("[0-9]+")) {
                                        productPrice = splitedPrice[0];
                                        if (getPriceAmazon(source, i, childCount) != null) {
                                            if (getOfferPriceAmazon(source, i, childCount) != null) {
                                                if (getDealPriceAmazon(source, i, childCount) != null) {
                                                    productPrice = getDealPriceAmazon(source, i, childCount);
                                                } else {
                                                    productPrice = getOfferPriceAmazon(source, i, childCount);
                                                }
                                            } else {
                                                productPrice = getPriceAmazon(source, i, childCount);
                                            }
                                        }
                                        //                                        Log.i("Product Pricev2 L1-1", productPrice + " " + i);
                                        return productPrice;
                                    }
                                }
                            }

                        } else if (productPrice.matches("[0-9]+")) {
                            if (getPriceAmazon(source, i, childCount) != null) {
                                if (getOfferPriceAmazon(source, i, childCount) != null) {
                                    if (getDealPriceAmazon(source, i, childCount) != null) {
                                        productPrice = getDealPriceAmazon(source, i, childCount);
                                    } else {
                                        productPrice = getOfferPriceAmazon(source, i, childCount);
                                    }
                                } else {
                                    productPrice = getPriceAmazon(source, i, childCount);
                                }
                            }
                            //                            Log.i("Product Pricev2 L1-2", productPrice + " " + i);
                            return productPrice;
                        }
                    }
                }
                if (source.getChild(i) != null && source.getChild(i).getChildCount() > 0) {
                    productPrice = getProductPricev3(source.getChild(i), (level + 1));
                    if (productPrice != null) {
                        return productPrice;
                    }
                }
            }
        }
        return productPrice;
    }

    private static String getPriceAmazon(AccessibilityNodeInfo source, int mrpChild, int childCount) {
        String price = null;
        if (childCount > (mrpChild + 2)) {
            if (source.getChild(mrpChild + 2).getContentDescription() != null) {
                String productPrice = source.getChild(mrpChild + 2).getContentDescription().toString();
                if (productPrice.contains(".") && !productPrice.contains("₹")) {
                    productPrice = productPrice.trim();
                    try {
                        productPrice = productPrice.replaceAll("\\s+", "");
                    } catch (Exception e) {
                        // Handling Errors
                    }
                    if (productPrice.matches("[0-9,.]+")) {
                        String[] splitedPrice = productPrice.split("\\.");
                        if (splitedPrice.length > 0) {
                            if (splitedPrice[0] != null) {
                                if (splitedPrice[0].contains(",")) {
                                    splitedPrice[0] = splitedPrice[0].replace(",", "");
                                }
                                if (splitedPrice[0].matches("[0-9]+")) {
                                    productPrice = splitedPrice[0];
                                    //                                    Log.i("Product Price L1-1-1", productPrice);
                                    price = productPrice;
                                }
                            }
                        }
                    }
                } else {
                    productPrice = productPrice.trim();
                    try {
                        productPrice = productPrice.replaceAll("\\s+", "");
                    } catch (Exception e) {
                        // Handling Errors
                    }
                    if (productPrice.matches("[0-9,.]+")) {
                        String[] splitedPrice = productPrice.split("\\.");
                        if (splitedPrice.length > 0) {
                            if (splitedPrice[0] != null) {
                                if (splitedPrice[0].contains(",")) {
                                    splitedPrice[0] = splitedPrice[0].replace(",", "");
                                }
                                if (splitedPrice[0].matches("[0-9]+")) {
                                    productPrice = splitedPrice[0];
                                    //                                    Log.i("Product Price L1-1-1-2", productPrice);
                                    price = productPrice;
                                }
                            }
                        }
                    }
                }
            }
        }
        return price;
    }

    private static String getOfferPriceAmazon(AccessibilityNodeInfo source, int mrpChild, int childCount) {
        String price = null;
        if (childCount > (mrpChild + 4)) {
            if (source.getChild(mrpChild + 4).getContentDescription() != null) {
                String productPrice = source.getChild(mrpChild + 4).getContentDescription().toString();
                if (productPrice.contains(".") && !productPrice.contains("₹")) {
                    productPrice = productPrice.trim();
                    try {
                        productPrice = productPrice.replaceAll("\\s+", "");
                    } catch (Exception e) {
                        // Handling Errors
                    }
                    if (productPrice.matches("[0-9,.]+")) {
                        String[] splitedPrice = productPrice.split("\\.");
                        if (splitedPrice.length > 0) {
                            if (splitedPrice[0] != null) {
                                if (splitedPrice[0].contains(",")) {
                                    splitedPrice[0] = splitedPrice[0].replace(",", "");
                                }
                                if (splitedPrice[0].matches("[0-9]+")) {
                                    productPrice = splitedPrice[0];
                                    //                                    Log.i("Product Price L1-1-2", productPrice);
                                    price = productPrice;
                                }
                            }
                        }
                    }
                } else {
                    productPrice = productPrice.trim();
                    try {
                        productPrice = productPrice.replaceAll("\\s+", "");
                    } catch (Exception e) {
                        // Handling Errors
                    }
                    if (productPrice.matches("[0-9,.]+")) {
                        String[] splitedPrice = productPrice.split("\\.");
                        if (splitedPrice.length > 0) {
                            if (splitedPrice[0] != null) {
                                if (splitedPrice[0].contains(",")) {
                                    splitedPrice[0] = splitedPrice[0].replace(",", "");
                                }
                                if (splitedPrice[0].matches("[0-9]+")) {
                                    productPrice = splitedPrice[0];
                                    //                                    Log.i("Product Price L1-1-2-2", productPrice);
                                    price = productPrice;
                                }
                            }
                        }
                    }
                }
            }
        }
        return price;
    }

    private static String getDealPriceAmazon(AccessibilityNodeInfo source, int mrpChild, int childCount) {
        String price = null;
        if (childCount > (mrpChild + 6)) {
            if (source.getChild(mrpChild + 5) != null && source.getChild(mrpChild + 5).getContentDescription() != null) {
                // Log.i("Product Deal Price", source.getChild(mrpChild + 5).getContentDescription().toString());
                if (source.getChild(mrpChild + 6).getContentDescription() != null) {
                    String productPrice = source.getChild(mrpChild + 6).getContentDescription().toString();
                    if (productPrice.contains(".") && !productPrice.contains("₹")) {
                        productPrice = productPrice.trim();
                        try {
                            productPrice = productPrice.replaceAll("\\s+", "");
                        } catch (Exception e) {
                            // Handling Errors
                        }
                        if (productPrice.matches("[0-9,.]+")) {
                            String[] splitedPrice = productPrice.split("\\.");
                            if (splitedPrice.length > 0) {
                                if (splitedPrice[0] != null) {
                                    if (splitedPrice[0].contains(",")) {
                                        splitedPrice[0] = splitedPrice[0].replace(",", "");
                                    }
                                    if (splitedPrice[0].matches("[0-9]+")) {
                                        productPrice = splitedPrice[0];
                                        //                                        Log.i("Product Price L1-1-3", productPrice);
                                        price = productPrice;
                                    }
                                }
                            }
                        }
                    } else {
                        productPrice = productPrice.trim();
                        try {
                            productPrice = productPrice.replaceAll("\\s+", "");
                        } catch (Exception e) {
                            // Handling Errors
                        }
                        if (productPrice.matches("[0-9,.]+")) {
                            String[] splitedPrice = productPrice.split("\\.");
                            if (splitedPrice.length > 0) {
                                if (splitedPrice[0] != null) {
                                    if (splitedPrice[0].contains(",")) {
                                        splitedPrice[0] = splitedPrice[0].replace(",", "");
                                    }
                                    if (splitedPrice[0].matches("[0-9]+")) {
                                        productPrice = splitedPrice[0];
                                        //                                        Log.i("Product Price L1-1-3-2", productPrice);
                                        price = productPrice;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return price;
    }

    public static String getPriceFromRange(AccessibilityNodeInfo source, int level) {
        int childCount = source.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (source.getChild(i) != null && source.getChild(i).getContentDescription() != null) {
                if (source.getChild(i).getClassName().equals("android.view.View") && source.getChild(i).getParent() != null && source.getChild(i).getParent().getClassName().equals("android.view.View") && source.getChild(i).getParent().getParent() != null && source.getChild(i).getParent().getParent().getClassName().equals("android.webkit.WebView") && source.getChild(i).getParent().getParent().getParent() != null && source.getChild(i).getParent().getParent().getParent().getClassName().equals("android.webkit.WebView") && source.getChild(i).getParent().getParent().getParent().getParent() != null && source.getChild(i).getParent().getParent().getParent().getParent().getClassName().equals("android.widget.LinearLayout") && source.getChild(i).getParent().getParent().getParent().getParent().getParent() != null && source.getChild(i).getParent().getParent().getParent().getParent().getParent().getClassName().equals("android.support.v4.widget.DrawerLayout") && source.getChild(i).getParent().getParent().getParent().getParent().getParent().getParent() != null && source.getChild(i).getParent().getParent().getParent().getParent().getParent().getParent().getClassName().equals("android.widget.FrameLayout")) {
                    if (!source.getChild(i).getContentDescription().toString().isEmpty() && source.getChild(i).getContentDescription().toString().contains(" - ")) {
                        String price = source.getChild(i).getContentDescription().toString().trim();
                        //                        Log.i("Actual price range str", price + "  Position:" + i + "  Level:" + level);
                        Log.i("AIndia ProductPrice 1>>", price + "  Position:" + i + "  Level:" + level);
                        if (price.contains(".")) {
                            price = price.substring(0, price.indexOf("."));
                        }
                        price = price.replaceAll("\\s", "");
                        //                        Log.i("Price Range", price + " Position:" + i + "Level:" + level);
                        if (price.contains("-")) {
                            price = price.split("-")[0];
                        }
                        if (price.matches("[0-9]+")) {
                            //                            Log.i("Price Range", price + " Position:" + i + "Level:" + level);
                            if (price != null) {
                                return price;
                            }
                        }
                    }
                }
            }
            if (source.getChild(i) != null && source.getChild(i).getChildCount() > 0) {
                String price = getPriceFromRange(source.getChild(i), (level + 1));
                if (price != null) {
                    return price;
                }
            }
        }
        return null;
    }

    // To print tree of all children and sub children and so on..
    public static String printAmazonPageLayoutStructureForward(AccessibilityNodeInfo source, int level) {

        if (source != null) {

            StringBuilder builder = new StringBuilder();

            builder.append(getSpaceAccToLevel(level));
            builder.append(level);
            builder.append("  " + source.getClassName());

            if (source.getContentDescription() != null) {
                builder.append("  " + source.getContentDescription().toString());
            }

            Log.i("AILayoutInfo ++++++", builder.toString());

            // Counting Children..
            int childCount = source.getChildCount();
            for (int i = 0; i < childCount; i++) {
                AccessibilityNodeInfo child = source.getChild(i);
                if (child != null) {
                    //                    if (level < 5) {
                    printAmazonPageLayoutStructureForward(child, (level + 1));
                    //                    }
                }
            }
        }
        return null;
    }

    // To print parent, grand parent and so on..
    public static String printAmazonPageLayoutStructureBackward(AccessibilityNodeInfo source, int level) {

        if (source != null) {

            StringBuilder builder = new StringBuilder();

            builder.append(getSpaceAccToLevel(Math.abs(level)));
            builder.append(level);
            builder.append("  " + source.getClassName());

            if (source.getContentDescription() != null) {
                builder.append("  " + source.getContentDescription().toString());
            }

            Log.i("AILayoutInfo ++++++", builder.toString());

            AccessibilityNodeInfo parent = source.getParent();
            if (parent != null) {
                printAmazonPageLayoutStructureBackward(parent, (level - 1));
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
