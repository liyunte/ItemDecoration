package com.lyt.itemdecoration;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

/**
 * <p/>
 * 描 述：1拖N布局
 * <p/>
 * ========================================
 */

public class ADragOfNLayoutManager extends GridLayoutManager {
    private List<SpanSizeBean> spanSizeBeanList = new ArrayList<>();
    private boolean hasFooter;//是否有底部
    private boolean hasHeader;//是否有头部
    private int spanCount;//列数
    public ADragOfNLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes,boolean hasHeader,boolean hasFooter) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.hasFooter = hasFooter;
        this.hasHeader = hasHeader;
    }

    public ADragOfNLayoutManager(Context context, int spanCount,boolean hasHeader,boolean hasFooter) {
        super(context, spanCount);
        this.hasFooter = hasFooter;
        this.hasHeader = hasHeader;
        this.spanCount = spanCount;
    }

    public ADragOfNLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout,boolean hasHeader,boolean hasFooter) {
        super(context, spanCount, orientation, reverseLayout);
        this.spanCount = spanCount;
        this.hasFooter = hasFooter;
        this.hasHeader = hasHeader;
    }

    public void notifyDataSetChanges(RecyclerView.Adapter adapter,@NonNull List<SpanSizeBean> spanSizeBeanList){
       this.spanSizeBeanList = spanSizeBeanList;
        setSpanSizeLookup(adapter);
    }

private void setSpanSizeLookup(final RecyclerView.Adapter adapter){
    setSpanSizeLookup(new SpanSizeLookup() {
        @Override
        public int getSpanSize(int position) {
            if (spanSizeBeanList.size()>0){
                if (hasHeader&&hasFooter){
                    if (position==0||position == adapter.getItemCount()-1){
                        return spanCount;
                    }else {
                        return spanSizeBeanList.get(position-1)!=null?spanSizeBeanList.get(position-1).getSpanSize(): 4;
                    }

                }else if (hasHeader){
                    if (position == 0){
                        return spanCount;
                    }else {
                        return spanSizeBeanList.get(position-1)!=null?spanSizeBeanList.get(position-1).getSpanSize(): 0;
                    }
                }else if (hasFooter){
                    if (position == adapter.getItemCount()-1){
                        return spanCount;
                    }else {
                        return spanSizeBeanList.get(position)!=null?spanSizeBeanList.get(position).getSpanSize(): 0;
                    }

                }else {
                    return spanSizeBeanList.get(position)!=null?spanSizeBeanList.get(position).getSpanSize(): 0;
                }
            }
            return spanCount;
        }
    });
}
    @Override
    public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);
        setSpanSizeLookup(view.getAdapter());
    }

    public static class SpanSizeBean {
        private int spanSize=1;

        public SpanSizeBean() {
        }

        public SpanSizeBean(int spanSize) {
            this.spanSize = spanSize;
        }

        public int getSpanSize() {
            return spanSize;
        }

        public void setSpanSize(int spanSize) {
            this.spanSize = spanSize;
        }
    }
}
