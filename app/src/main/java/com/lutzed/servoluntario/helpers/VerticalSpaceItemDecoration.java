package com.lutzed.servoluntario.helpers;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int mVerticalSpaceHeight;
    private final boolean mIgnoreHeaderPadding;

    public VerticalSpaceItemDecoration(int mVerticalSpaceHeight, boolean ignoreHeaderPadding) {
        this.mVerticalSpaceHeight = mVerticalSpaceHeight;
        mIgnoreHeaderPadding = ignoreHeaderPadding;
    }

    public VerticalSpaceItemDecoration(int mVerticalSpaceHeight) {
        this(mVerticalSpaceHeight, false);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
            outRect.bottom = mVerticalSpaceHeight;
        }

        if (mIgnoreHeaderPadding && parent.getChildAdapterPosition(view) == 0) {
            outRect.set(-parent.getPaddingLeft(), -parent.getPaddingTop(), -parent.getPaddingRight(), mVerticalSpaceHeight);
        }
    }
}
