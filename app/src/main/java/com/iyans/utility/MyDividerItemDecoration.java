package com.iyans.utility;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.RecyclerView.LayoutParams;
import android.support.v7.widget.RecyclerView.State;
import android.util.TypedValue;
import android.view.View;

public class MyDividerItemDecoration extends ItemDecoration {
    private static final int[] ATTRS = new int[]{16843284};
    public static final int HORIZONTAL_LIST = 0;
    public static final int VERTICAL_LIST = 1;
    private Context context;
    private int leftMargin;
    private Drawable mDivider;
    private int mOrientation;
    private int rightMargin;

    public MyDividerItemDecoration(Context context, int orientation, int leftMargin, int rightMargin) {
        this.context = context;
        this.leftMargin = leftMargin;
        this.rightMargin = rightMargin;
        TypedArray a = context.obtainStyledAttributes(ATTRS);
        this.mDivider = a.getDrawable(0);
        a.recycle();
        setOrientation(orientation);
    }

    public void setOrientation(int orientation) {
        if (orientation == 0 || orientation == 1) {
            this.mOrientation = orientation;
            return;
        }
        throw new IllegalArgumentException("invalid orientation");
    }

    public void onDrawOver(Canvas c, RecyclerView parent, State state) {
        if (this.mOrientation == 1) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            int top = child.getBottom() + ((LayoutParams) child.getLayoutParams()).bottomMargin;
            this.mDivider.setBounds(dpToPx(this.leftMargin) + left, top, right - dpToPx(this.rightMargin), this.mDivider.getIntrinsicHeight() + top);
            this.mDivider.draw(c);
        }
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        int top = parent.getPaddingTop();
        int bottom = parent.getHeight() - parent.getPaddingBottom();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            int left = child.getRight() + ((LayoutParams) child.getLayoutParams()).rightMargin;
            this.mDivider.setBounds(left, dpToPx(this.leftMargin) + top, this.mDivider.getIntrinsicHeight() + left, bottom - dpToPx(this.rightMargin));
            this.mDivider.draw(c);
        }
    }

    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
        if (this.mOrientation == 1) {
            outRect.set(0, 0, 0, this.mDivider.getIntrinsicHeight());
        } else {
            outRect.set(0, 0, this.mDivider.getIntrinsicWidth(), 0);
        }
    }

    private int dpToPx(int dp) {
        return Math.round(TypedValue.applyDimension(1, (float) dp, this.context.getResources().getDisplayMetrics()));
    }
}
