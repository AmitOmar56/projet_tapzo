package com.axovel.mytapzoapp.CustomViewGroups;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.axovel.mytapzoapp.utils.Utils;

/**
 * Created by Soft1 on 19-Sep-17.
 */

public class CustomTimeVGroup extends ViewGroup {

    private int columnCount = 4;
    private int itemHeight = 0;

    public CustomTimeVGroup(Context context) {
        super(context);

        init();
    }


    public CustomTimeVGroup(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public CustomTimeVGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {

        itemHeight = Utils.dpToPx(getContext(), 60);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int itemWidth = getMeasuredWidth() / columnCount;
        measureChildren(MeasureSpec.makeMeasureSpec(itemWidth, MeasureSpec.getMode(widthMeasureSpec)), heightMeasureSpec);

        int childCount = getChildCount();

        int rowCount = (childCount % columnCount == 0) ? (childCount / columnCount) : ((childCount / columnCount) + 1);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), itemHeight * rowCount);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int itemWidth = getWidth() / columnCount;
        int childCount = getChildCount();
        int x = 0, y = 0;

        for (int i = 0; i < childCount; i++) {

            View view = getChildAt(i);

            view.layout(x, y, x + itemWidth, y + itemHeight);
            x += itemWidth;

            if ((i + 1) % columnCount == 0) {
                x = 0;
                y += itemHeight;
            }
        }
    }
}
