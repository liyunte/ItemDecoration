package com.lyt;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;



public class BaseItemDecoration extends RecyclerView.ItemDecoration {
    private int leftMargin;
    private int rightMargin;
    private int topMargin;
    private int bottomMargin;
    private int itemMargin;
    private int designWidth = 750;//UI设计图上屏幕宽度
    private int headCount;
    private int footCount;
    public BaseItemDecoration(Context context,int itemMargin) {
        this(context,750,itemMargin);
    }
    public BaseItemDecoration(Context context,int designWidth,int itemMargin) {
       this(context,designWidth,itemMargin,itemMargin,itemMargin,itemMargin,itemMargin);
    }

    public BaseItemDecoration(Context context,int designWidth,int itemMargin,int leftMargin, int topMargin, int rightMargin,int bottomMargin ) {
        this(context,designWidth,itemMargin,leftMargin,topMargin,rightMargin,bottomMargin,0,0);
    }

    public BaseItemDecoration(Context context,int designWidth,int itemMargin, int leftMargin, int topMargin, int rightMargin, int bottomMargin, int headCount, int footCount ) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int width = dm.widthPixels;//实际手机屏幕宽度
        this.designWidth =designWidth;
        this.itemMargin = itemMargin*width/designWidth;
        this.leftMargin = leftMargin*width/designWidth;
        this.topMargin = topMargin*width/designWidth;
        this.rightMargin = rightMargin*width/designWidth;
        this.bottomMargin = bottomMargin*width/designWidth;
        this.headCount = headCount;
        this.footCount = footCount;
    }
    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        RecyclerView.LayoutManager manager = parent.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) manager;
           GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();
            if (gridLayoutManager.getOrientation() == GridLayoutManager.HORIZONTAL) {
                setGridLayoutManagerHorizontalOutRect(outRect, itemPosition, parent);
            } else {
                setGridLayoutManagerVerticalOutRect(outRect, itemPosition, parent);
            }
        } else if (manager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) manager;
            if (linearLayoutManager.getOrientation() == LinearLayoutManager.HORIZONTAL) {//水平
                setLinearLayoutManagerHorizontalOutRect(outRect, itemPosition, parent);
            } else {
                setLinearLayoutManagerVerticalOutRect(outRect, itemPosition, parent);
            }
        } else if (manager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) manager;
            if (staggeredGridLayoutManager.getOrientation()==StaggeredGridLayoutManager.HORIZONTAL){
                setStaggeredGridLayoutManagerHorizontalOutRect(outRect,itemPosition,parent);
            }else {
                setStaggeredGridLayoutManagerVerticalOutRect(outRect,itemPosition,parent);
            }
        }else {
            outRect.set(0,0,0,0);
        }
    }

    private void setLinearLayoutManagerHorizontalOutRect(Rect outRect, int itemPosition, RecyclerView parent) {
        if (itemPosition == 0) {
            outRect.set(leftMargin, topMargin, itemMargin, bottomMargin);
        } else if (itemPosition == parent.getAdapter().getItemCount() - 1) {
            outRect.set(0, topMargin, itemMargin, bottomMargin);
        } else {
            outRect.set(0, topMargin, itemMargin, bottomMargin);
        }
    }

    private void setLinearLayoutManagerVerticalOutRect(Rect outRect, int itemPosition, RecyclerView parent) {
         if (itemPosition == 0) {
            outRect.set(leftMargin, topMargin, rightMargin, itemMargin);
        } else if (itemPosition == parent.getAdapter().getItemCount() - 1) {
            outRect.set(leftMargin, 0, rightMargin, bottomMargin);
        } else {
            outRect.set(leftMargin, 0, rightMargin, itemMargin);
        }
    }

    private void setGridLayoutManagerHorizontalOutRect(Rect outRect, int itemPosition, RecyclerView parent) {
        int childCount = parent.getAdapter().getItemCount();
        int spanCount = ((GridLayoutManager)parent.getLayoutManager()).getSpanCount();
        if (isGridHorizontalFirstColumn(itemPosition, childCount, spanCount)
                && isGridHorizontalFirstRow(itemPosition, childCount, spanCount)) {
            //第一行&&第一列
            outRect.set(leftMargin, topMargin, itemMargin, itemMargin);
        } else if (isGridHorizontalEndColumn(itemPosition, childCount, spanCount)
                && isGridHorizontalEndRow(itemPosition, childCount, spanCount)) {
            //最后一列&&最后一行
            outRect.set(0, 0, rightMargin, bottomMargin);
        } else if (isGridHorizontalFirstRow(itemPosition, childCount, spanCount)
                && isGridHorizontalEndColumn(itemPosition, childCount, spanCount)) {
            //第一行&&最后一列
            outRect.set(0, topMargin, rightMargin, itemMargin);
        } else if (isGridHorizontalFirstColumn(itemPosition, childCount, spanCount)
                && isGridHorizontalEndRow(itemPosition, childCount, spanCount)) {
            //第一列&&最后一行
            outRect.set(leftMargin, 0, itemMargin, bottomMargin);
        } else if (isGridHorizontalFirstRow(itemPosition, childCount, spanCount)) {
            //第一行
            outRect.set(0, topMargin, itemMargin, itemMargin);
        } else if (isGridHorizontalFirstColumn(itemPosition, childCount, spanCount)) {
            //第一列
            outRect.set(leftMargin, 0, itemMargin, itemMargin);
        } else if (isGridHorizontalEndColumn(itemPosition, childCount, spanCount)) {
            //最后一列
            outRect.set(0, 0, rightMargin, itemMargin);
        } else if (isGridHorizontalEndRow(itemPosition, childCount, spanCount)) {
            //最后一行
            outRect.set(0, 0, itemMargin, bottomMargin);
        } else {
            outRect.set(0, 0, itemMargin, itemMargin);
        }
    }

    private void setGridLayoutManagerVerticalOutRect(Rect outRect, int itemPosition, RecyclerView parent) {
        int childCount = parent.getAdapter().getItemCount();
        int spanCount = ((GridLayoutManager)parent.getLayoutManager()).getSpanCount();

//        Log.e("liyunte","childCount="+childCount);

        if (headCount>0&&footCount>0){
            if (itemPosition<headCount||itemPosition>=childCount-footCount){
                outRect.set(0,0,0,0);
            }else {
                childCount = childCount-headCount-footCount;
                itemPosition = itemPosition-headCount;
                setDefaultGridLayoutManagerVerticalOutRect(outRect,itemPosition,childCount,spanCount);
            }
        }else if (headCount>0){
            if (itemPosition<headCount){
                outRect.set(0,0,0,0);
            }else {
                childCount = childCount-headCount;
                itemPosition = itemPosition-headCount;
                setDefaultGridLayoutManagerVerticalOutRect(outRect,itemPosition,childCount,spanCount);
            }
        }else if (footCount>0){
            if (itemPosition>=spanCount-footCount){
                outRect.set(0,0,0,0);
            }else {
                childCount = childCount-footCount;
                itemPosition = itemPosition-headCount;
                setDefaultGridLayoutManagerVerticalOutRect(outRect,itemPosition,childCount,spanCount);
            }
        }else {
            setDefaultGridLayoutManagerVerticalOutRect(outRect,itemPosition,childCount,spanCount);
        }


    }

    private void setDefaultGridLayoutManagerVerticalOutRect(Rect outRect,int itemPosition,int childCount,int spanCount){
        if (isGridVerticalFirstColumn(itemPosition, childCount, spanCount)
                && isGridVerticalFirstRow(itemPosition, childCount, spanCount)) {
            //第一行&&第一列
            outRect.set(leftMargin, topMargin, itemMargin, itemMargin);
        } else if (isGridVerticalEndColumn(itemPosition, childCount, spanCount)
                && isGridVerticalEndRow(itemPosition, childCount, spanCount)) {
            //最后一列&&最后一行
            outRect.set(0, 0, rightMargin, bottomMargin);
        } else if (isGridVerticalFirstRow(itemPosition, childCount,spanCount)
                && isGridVerticalEndColumn(itemPosition, childCount, spanCount)) {
            //第一行&&最后一列
            outRect.set(0, topMargin, rightMargin, itemMargin);
        } else if (isGridVerticalFirstColumn(itemPosition, childCount, spanCount)
                && isGridVerticalEndRow(itemPosition, childCount,spanCount)) {
            //第一列&&最后一行
            outRect.set(leftMargin, 0, itemMargin, bottomMargin);
        } else if (isGridVerticalFirstRow(itemPosition, childCount, spanCount)) {
            //第一行
            outRect.set(0, topMargin, itemMargin, itemMargin);
        } else if (isGridVerticalFirstColumn(itemPosition, childCount, spanCount)) {
            //第一列
            outRect.set(leftMargin, 0, itemMargin, itemMargin);
        } else if (isGridVerticalEndColumn(itemPosition, childCount,spanCount)) {
            //最后一列
            outRect.set(0, 0, rightMargin, itemMargin);
        } else if (isGridVerticalEndRow(itemPosition, childCount, spanCount)) {
            //最后一行
            outRect.set(0, 0, itemMargin, bottomMargin);
        } else {
            outRect.set(0, 0, itemMargin, itemMargin);
        }
    }

    private void setStaggeredGridLayoutManagerVerticalOutRect(Rect outRect, int itemPosition, RecyclerView parent){
               outRect.set(leftMargin,topMargin,0,0);
        parent.setPadding(0,0,rightMargin,bottomMargin);
    }
    private void setStaggeredGridLayoutManagerHorizontalOutRect(Rect outRect, int itemPosition, RecyclerView parent){
        outRect.set(leftMargin,topMargin,0,0);
        parent.setPadding(0,0,0,bottomMargin);
    }

    private boolean isGridHorizontalFirstColumn(int itemPosition, int childCount, int spanCount) {
        if (itemPosition < spanCount) {
            return true;
        }
        return false;
    }


    private boolean isGridHorizontalEndColumn(int itemPosition, int childCount, int spanCount) {
        int yu = childCount % spanCount;
        if (yu == 0) {
            if (itemPosition >= childCount - spanCount) {
                return true;
            }
        } else {
            if (itemPosition >= childCount - yu) {
                return true;
            }
        }
        return false;
    }


    private boolean isGridHorizontalFirstRow(int itemPosition, int childCount, int spanCount) {
        if (itemPosition % spanCount == 0) {
            return true;
        }
        return false;
    }

    private boolean isGridHorizontalEndRow(int itemPosition, int childCount, int spanCount) {
        if (((itemPosition + 1) % spanCount) == 0) {
            return true;
        }
        return false;
    }

    private boolean isGridVerticalFirstColumn(int itemPosition, int childCount, int spanCount) {
        if ((itemPosition % spanCount) == 0) {
            return true;
        }
        return false;
    }


    private boolean isGridVerticalEndColumn(int itemPosition, int childCount, int spanCount) {
        if (((itemPosition + 1) % spanCount) == 0) {
            return true;
        }
        return false;
    }


    private boolean isGridVerticalFirstRow(int itemPosition, int childCount, int spanCount) {
        if (itemPosition < spanCount) {
            return true;
        }
        return false;
    }

    private boolean isGridVerticalEndRow(int itemPosition, int childCount, int spanCount) {
        int yu = childCount % spanCount;
        if (yu == 0) {
            if (itemPosition >= childCount - spanCount) {
                return true;
            }
        } else {
            if (itemPosition >= childCount - yu) {
                return true;
            }
        }
        return false;
    }



}
