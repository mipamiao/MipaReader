package com.example.mipareader;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class BookTouch extends ItemTouchHelper.SimpleCallback{
    RecyclerView rv;
    int AllowDir = -1;
    public BookTouch(RecyclerView RV) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        rv = RV;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }
    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        // 设置滑动触发的阈值为 0.5f，表示滑动达到 ItemView 宽度或高度的一半时触发滑动删除
        return 0.25f;
    }


    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if(actionState!=ItemTouchHelper.ACTION_STATE_SWIPE)return;
        if(dX>0){
            if(AllowDir<0)return;
            else AllowDir = -1;
        }else if(dX<0){
            if(AllowDir>0)return;
            else AllowDir = 1;
        }else return;
        BookShelfAdapter.ViewHolder holder = (BookShelfAdapter.ViewHolder)viewHolder;
        int left_termine = holder.GetItemWidth()-viewHolder.itemView.getWidth();
        ValueAnimator animator;
        int x = holder.itemView.getScrollX();
        Log.e("TAG", "onSwiped: ok " + dX+":X:"+x);
        if(dX<0&&x==0)
            animator = ValueAnimator.ofFloat(0.1f,left_termine);
        else if(dX>0&&x==left_termine)
            animator = ValueAnimator.ofFloat(left_termine-0.1f,0);
        else return;
        animator.setDuration(250); // 设置动画持续时间，单位毫秒

        // 设置动画更新监听器
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 获取当前动画的值
                float value = (float) animation.getAnimatedValue();
                viewHolder.itemView.scrollTo((int) value, (int) viewHolder.itemView.getY());
            }
        });
        animator.start();
    }
}
