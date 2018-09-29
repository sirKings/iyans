package com.iyans.utility;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.RecyclerView.LayoutParams;
import android.support.v7.widget.RecyclerView.State;
import android.util.AttributeSet;
import android.view.View;

public class DividerItemDecoration extends ItemDecoration {
    private Drawable mDivider;
    private boolean mShowFirstDivider;
    private boolean mShowLastDivider;

    public DividerItemDecoration(Context context, AttributeSet attrs) {
        this.mShowFirstDivider = false;
        this.mShowLastDivider = false;
        TypedArray a = context.obtainStyledAttributes(attrs, new int[]{16843284});
        this.mDivider = a.getDrawable(0);
        a.recycle();
    }

    public DividerItemDecoration(Context context, AttributeSet attrs, boolean showFirstDivider, boolean showLastDivider) {
        this(context, attrs);
        this.mShowFirstDivider = showFirstDivider;
        this.mShowLastDivider = showLastDivider;
    }

    public DividerItemDecoration(Drawable divider) {
        this.mShowFirstDivider = false;
        this.mShowLastDivider = false;
        this.mDivider = divider;
    }

    public DividerItemDecoration(Drawable divider, boolean showFirstDivider, boolean showLastDivider) {
        this(divider);
        this.mShowFirstDivider = showFirstDivider;
        this.mShowLastDivider = showLastDivider;
    }

    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (this.mDivider != null && parent.getChildPosition(view) >= 1) {
            if (getOrientation(parent) == 1) {
                outRect.top = this.mDivider.getIntrinsicHeight();
            } else {
                outRect.left = this.mDivider.getIntrinsicWidth();
            }
        }
    }

    public void onDrawOver(Canvas c, RecyclerView parent, State state) {
        Canvas canvas = c;
        RecyclerView recyclerView = parent;
        if (this.mDivider == null) {
            super.onDrawOver(c, parent, state);
            return;
        }
        int size;
        int left = 0;
        int right = 0;
        int top = 0;
        int bottom = 0;
        int orientation = getOrientation(recyclerView);
        int childCount = parent.getChildCount();
        if (orientation == 1) {
            size = mDivider.getIntrinsicHeight();
            left = parent.getPaddingLeft();
            right = parent.getWidth() - parent.getPaddingRight();
        } else {
            size = mDivider.getIntrinsicWidth();
            top = parent.getPaddingTop();
            bottom = parent.getHeight() - parent.getPaddingBottom();
        }
        for (int i = 0; i < childCount; i++) {
            View child = recyclerView.getChildAt(i);
            LayoutParams params = (LayoutParams) child.getLayoutParams();
            int top2;
            if (orientation == 1) {
                top2 = child.getTop() - params.topMargin;
                bottom = top2 + size;
                top = top2;
            } else {
                top2 = child.getLeft() - params.leftMargin;
                right = top2 + size;
                left = top2;
            }
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(canvas);
        }
        if (mShowLastDivider && childCount > 0) {
            View child2 = recyclerView.getChildAt(childCount - 1);
            LayoutParams params2 = (LayoutParams) child2.getLayoutParams();
            if (orientation == 1) {
                top = child2.getBottom() + params2.bottomMargin;
                bottom = top + size;
            } else {
                left = child2.getRight() + params2.rightMargin;
                right = left + size;
            }
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(canvas);
        }
    }

    private int getOrientation(RecyclerView parent) {
        if (parent.getLayoutManager() instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) parent.getLayoutManager()).getOrientation();
        }
        throw new IllegalStateException("DividerItemDecoration can only be used with a LinearLayoutManager.");
    }
}
