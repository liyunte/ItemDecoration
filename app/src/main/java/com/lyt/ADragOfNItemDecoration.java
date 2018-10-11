package com.lyt;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * ========================================
 * <p/>
 * 版 权：江苏精易达信息技术股份有限公司 版权所有 （C） 2018
 * <p/>
 * 作 者：liyunte
 * <p/>
 * <p/>
 * 版 本：1.0
 * <p/>
 * 创建日期： 2018/10/10 14:09
 * <p/>
 * 描 述：最多只能有一个头部一个底部的item间距
 * <p/>
 * <p/>
 * 修订历史：
 * <p/>
 * ========================================
 */

public class ADragOfNItemDecoration extends RecyclerView.ItemDecoration {
    private int allSpanSizes;
    private int ceAllSpanSizes;
    private int itemMargin;
    private boolean hasFooter;
    private boolean hasHeader;
    public ADragOfNItemDecoration(Context context, int itemMargin) {
        this(context,750,itemMargin,false,false);
    }
    public ADragOfNItemDecoration(Context context, int designWidth, int itemMargin) {
        this(context,designWidth,itemMargin,false,false);
    }

    public ADragOfNItemDecoration(Context context, int designWidth, int itemMargin, boolean hasHeader, boolean hasFooter) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int width = dm.widthPixels;//实际手机屏幕宽度
        this.itemMargin = itemMargin*width/designWidth;
        this.hasFooter = hasFooter;
        this.hasHeader = hasHeader;
    }

    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        super.getItemOffsets(outRect, itemPosition, parent);
        RecyclerView.LayoutManager manager = parent.getLayoutManager();
        if (manager instanceof GridLayoutManager){
            int spanCount = ((GridLayoutManager) manager).getSpanCount();
           GridLayoutManager.SpanSizeLookup spanSizeLookup =((GridLayoutManager) manager).getSpanSizeLookup();
            int childCount = parent.getAdapter().getItemCount();
              if (hasHeader&&hasFooter){
                  if (itemPosition==0){
                      outRect.set(0,0,0,0);
                  }else if (itemPosition==childCount-1){
                      outRect.set(0,0,0,0);
                  }else {
                      itemPosition = itemPosition-1;
                      childCount = childCount-2;
                      int spanSize = spanSizeLookup.getSpanSize(itemPosition+1);
                      for (int i=0;i<childCount;i++){
                          ceAllSpanSizes+=spanSizeLookup.getSpanSize(i+1);
                      }
                      int yu = ceAllSpanSizes%spanCount;
                      int bottomCount = 0;
                      int bottomSpanSizes =0;
                      int ceChildCount = childCount+1;
                      if (yu==0){
                          while (bottomSpanSizes<spanCount){
                              ceChildCount--;
                              bottomCount++;
                              bottomSpanSizes += spanSizeLookup.getSpanSize(ceChildCount);
                          }
                      }else {
                          while (bottomSpanSizes<yu){
                              ceChildCount--;
                              bottomCount++;
                              bottomSpanSizes += spanSizeLookup.getSpanSize(ceChildCount);
                          }
                      }
                      setOutRect(outRect,spanSize,spanCount,childCount,itemPosition,bottomCount);
                  }
              }else
            if (hasHeader){
                if (itemPosition==0){
                    outRect.set(0,0,0,0);
                }else {
                    itemPosition = itemPosition-1;
                    childCount = childCount-1;
                    int spanSize = spanSizeLookup.getSpanSize(itemPosition+1);
                    for (int i=0;i<childCount;i++){
                        ceAllSpanSizes+=spanSizeLookup.getSpanSize(i+1);
                    }
                    int yu = ceAllSpanSizes%spanCount;
                    int bottomCount = 0;
                    int bottomSpanSizes =0;
                    int ceChildCount = childCount;
                    if (yu==0){
                        while (bottomSpanSizes<spanCount){
                            bottomSpanSizes += spanSizeLookup.getSpanSize(ceChildCount);
                            ceChildCount--;
                            bottomCount++;
                        }
                    }else {
                        while (bottomSpanSizes<yu){
                            bottomSpanSizes += spanSizeLookup.getSpanSize(ceChildCount);
                            ceChildCount--;
                            bottomCount++;
                        }
                    }
                    setOutRect(outRect,spanSize,spanCount,childCount,itemPosition,bottomCount);
                }
            }else if (hasFooter){
                if (itemPosition==childCount-1){
                    outRect.set(0,0,0,0);
                }else {
                    childCount = childCount-1;
                    int spanSize = spanSizeLookup.getSpanSize(itemPosition);
                    for (int i=0;i<childCount;i++){
                        ceAllSpanSizes+=spanSizeLookup.getSpanSize(i);
                    }
                    int yu = ceAllSpanSizes%spanCount;
                    int bottomCount = 0;
                    int bottomSpanSizes =0;
                    int ceChildCount = childCount;
                    if (yu==0){
                        while (bottomSpanSizes<spanCount){
                            ceChildCount--;
                            bottomCount++;
                            bottomSpanSizes += spanSizeLookup.getSpanSize(ceChildCount);

                        }
                    }else {
                        while (bottomSpanSizes<yu){
                            ceChildCount--;
                            bottomCount++;
                            bottomSpanSizes += spanSizeLookup.getSpanSize(ceChildCount);
                        }
                    }
                    setOutRect(outRect,spanSize,spanCount,childCount,itemPosition,bottomCount);
                }
            }else {
                int spanSize = spanSizeLookup.getSpanSize(itemPosition);
                for (int i=0;i<childCount;i++){
                    ceAllSpanSizes+=spanSizeLookup.getSpanSize(i);
                }
                int yu = ceAllSpanSizes%spanCount;
                int bottomCount = 0;
                int bottomSpanSizes =0;
                int ceChildCount = childCount;
                if (yu==0){
                    while (bottomSpanSizes<spanCount){
                        ceChildCount--;
                        bottomCount++;
                        bottomSpanSizes += spanSizeLookup.getSpanSize(ceChildCount);
                    }
                }else {
                    while (bottomSpanSizes<yu){
                        ceChildCount--;
                        bottomCount++;
                        bottomSpanSizes += spanSizeLookup.getSpanSize(ceChildCount);
                    }
                }
                setOutRect(outRect,spanSize,spanCount,childCount,itemPosition,bottomCount);
            }

        }

    }
    private void setOutRect(Rect outRect,int spanSize,int spanCount,int childCount,int itemPosition,int bottomCount){
        allSpanSizes = allSpanSizes+spanSize;
        if (allSpanSizes%spanCount==0){
            if (itemPosition>=childCount-bottomCount){
                outRect.set(0,0,0,0);
            }else {
                outRect.set(0,0,0,itemMargin);
            }
        }else {
            if (itemPosition>=childCount-bottomCount){
                outRect.set(0,0,itemMargin,0);
            }else {
                outRect.set(0,0,itemMargin,itemMargin);
            }

        }


    }
}
