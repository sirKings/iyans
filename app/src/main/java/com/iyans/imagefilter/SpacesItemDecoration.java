package com.iyans.imagefilter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.RecyclerView.State;
import android.view.View;

public class SpacesItemDecoration extends ItemDecoration {
    private int space;

    public SpacesItemDecoration(int space) {
        this.space = space;
    }

    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
        if (parent.getChildAdapterPosition(view) == state.getItemCount() - 1) {
            outRect.left = this.space;
            outRect.right = 0;
            return;
        }
        outRect.right = this.space;
        outRect.left = 0;
    }
}
