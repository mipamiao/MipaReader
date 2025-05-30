package com.example.mipareader.UI.event;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int verticalSpaceHeight;

    public VerticalSpaceItemDecoration(Context context, int verticalSpaceHeight) {
        this.verticalSpaceHeight = verticalSpaceHeight;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.bottom = verticalSpaceHeight;

        // 如果是最后一项，不添加底部间隔（可根据需求调整）
        if (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1) {
            outRect.bottom = 0;
        }
    }
}
