package com.google.android.material.internal;
import r.android.view.View;
import r.android.view.ViewGroup;
import androidx.core.view.MarginLayoutParamsCompat;
import androidx.core.view.ViewCompat;
public class FlowLayout extends ViewGroup {
  private int lineSpacing;
  private int itemSpacing;
  private boolean singleLine;
  private int rowCount;
  protected void setLineSpacing(  int lineSpacing){
    this.lineSpacing=lineSpacing;
  }
  protected void setItemSpacing(  int itemSpacing){
    this.itemSpacing=itemSpacing;
  }
  public boolean isSingleLine(){
    return singleLine;
  }
  public void setSingleLine(  boolean singleLine){
    this.singleLine=singleLine;
  }
  protected void onMeasure(  int widthMeasureSpec,  int heightMeasureSpec){
    final int width=MeasureSpec.getSize(widthMeasureSpec);
    final int widthMode=MeasureSpec.getMode(widthMeasureSpec);
    final int height=MeasureSpec.getSize(heightMeasureSpec);
    final int heightMode=MeasureSpec.getMode(heightMeasureSpec);
    final int maxWidth=widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.EXACTLY ? width : Integer.MAX_VALUE;
    int childLeft=getPaddingLeft();
    int childTop=getPaddingTop();
    int childBottom=childTop;
    int childRight=childLeft;
    int maxChildRight=0;
    final int maxRight=maxWidth - getPaddingRight();
    for (int i=0; i < getChildCount(); i++) {
      View child=getChildAt(i);
      if (child.getVisibility() == View.GONE) {
        continue;
      }
      measureChild(child,widthMeasureSpec,heightMeasureSpec);
      LayoutParams lp=child.getLayoutParams();
      int leftMargin=0;
      int rightMargin=0;
      if (lp instanceof MarginLayoutParams) {
        MarginLayoutParams marginLp=(MarginLayoutParams)lp;
        leftMargin+=marginLp.leftMargin;
        rightMargin+=marginLp.rightMargin;
      }
      childRight=childLeft + leftMargin + child.getMeasuredWidth();
      if (childRight > maxRight && !isSingleLine()) {
        childLeft=getPaddingLeft();
        childTop=childBottom + lineSpacing;
      }
      childRight=childLeft + leftMargin + child.getMeasuredWidth();
      childBottom=childTop + child.getMeasuredHeight();
      if (childRight > maxChildRight) {
        maxChildRight=childRight;
      }
      childLeft+=(leftMargin + rightMargin + child.getMeasuredWidth()) + itemSpacing;
      if (i == (getChildCount() - 1)) {
        maxChildRight+=rightMargin;
      }
    }
    maxChildRight+=getPaddingRight();
    childBottom+=getPaddingBottom();
    int finalWidth=getMeasuredDimension(width,widthMode,maxChildRight);
    int finalHeight=getMeasuredDimension(height,heightMode,childBottom);
    setMeasuredDimension(finalWidth,finalHeight);
  }
  private static int getMeasuredDimension(  int size,  int mode,  int childrenEdge){
switch (mode) {
case MeasureSpec.EXACTLY:
      return size;
case MeasureSpec.AT_MOST:
    return Math.min(childrenEdge,size);
default :
  return childrenEdge;
}
}
protected void onLayout(boolean sizeChanged,int left,int top,int right,int bottom){
if (getChildCount() == 0) {
rowCount=0;
return;
}
rowCount=1;
boolean isRtl=ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL;
int paddingStart=isRtl ? getPaddingRight() : getPaddingLeft();
int paddingEnd=isRtl ? getPaddingLeft() : getPaddingRight();
int childStart=paddingStart;
int childTop=getPaddingTop();
int childBottom=childTop;
int childEnd;
final int maxChildEnd=right - left - paddingEnd;
for (int i=0; i < getChildCount(); i++) {
View child=getChildAt(i);
if (child.getVisibility() == View.GONE) {
  child.setTag(r.android.R.id.row_index_key,-1);
  continue;
}
LayoutParams lp=child.getLayoutParams();
int startMargin=0;
int endMargin=0;
if (lp instanceof MarginLayoutParams) {
  MarginLayoutParams marginLp=(MarginLayoutParams)lp;
  startMargin=MarginLayoutParamsCompat.getMarginStart(marginLp);
  endMargin=MarginLayoutParamsCompat.getMarginEnd(marginLp);
}
childEnd=childStart + startMargin + child.getMeasuredWidth();
if (!singleLine && (childEnd > maxChildEnd)) {
  childStart=paddingStart;
  childTop=childBottom + lineSpacing;
  rowCount++;
}
child.setTag(r.android.R.id.row_index_key,rowCount - 1);
childEnd=childStart + startMargin + child.getMeasuredWidth();
childBottom=childTop + child.getMeasuredHeight();
if (isRtl) {
  child.layout(maxChildEnd - childEnd,childTop,maxChildEnd - childStart - startMargin,childBottom);
}
 else {
  child.layout(childStart + startMargin,childTop,childEnd,childBottom);
}
childStart+=(startMargin + endMargin + child.getMeasuredWidth()) + itemSpacing;
}
}
protected int getRowCount(){
return rowCount;
}
public int getRowIndex(View child){
Object index=child.getTag(r.android.R.id.row_index_key);
if (!(index instanceof Integer)) {
return -1;
}
return (int)index;
}
}
