//start - license
/*
 * Copyright (c) 2025 Ashera Cordova
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */
//end - license
/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.android.material.bottomsheet;
import static java.lang.Math.max;
import static java.lang.Math.min;
import r.android.content.res.ColorStateList;
import r.android.os.SystemClock;
import r.android.util.Log;
import r.android.view.View;
import r.android.view.View.MeasureSpec;
import r.android.view.ViewGroup;
import r.android.view.ViewGroup.MarginLayoutParams;
import r.android.view.ViewParent;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class BottomSheetBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {
public abstract static class BottomSheetCallback {
    public abstract void onStateChanged(    View bottomSheet,    int newState);
    public abstract void onSlide(    View bottomSheet,    float slideOffset);
    void onLayout(    View bottomSheet){
    }
  }
  public static final int STATE_DRAGGING=1;
  public static final int STATE_SETTLING=2;
  public static final int STATE_EXPANDED=3;
  public static final int STATE_COLLAPSED=4;
  public static final int STATE_HIDDEN=5;
  public static final int STATE_HALF_EXPANDED=6;
  public static final int PEEK_HEIGHT_AUTO=-1;
  public static final int SAVE_PEEK_HEIGHT=0x1;
  public static final int SAVE_FIT_TO_CONTENTS=1 << 1;
  public static final int SAVE_HIDEABLE=1 << 2;
  public static final int SAVE_SKIP_COLLAPSED=1 << 3;
  public static final int SAVE_ALL=-1;
  public static final int SAVE_NONE=0;
  private static final String TAG="BottomSheetBehavior";
  private int saveFlags=SAVE_NONE;
  static final int DEFAULT_SIGNIFICANT_VEL_THRESHOLD=500;
  private static final float HIDE_THRESHOLD=0.5f;
  private static final float HIDE_FRICTION=0.1f;
  private static final int CORNER_ANIMATION_DURATION=500;
  private static final int NO_MAX_SIZE=-1;
  static final int VIEW_INDEX_BOTTOM_SHEET=0;
  private static final int INVALID_POSITION=-1;
  static final int VIEW_INDEX_ACCESSIBILITY_DELEGATE_VIEW=1;
  private boolean fitToContents=true;
  private boolean updateImportantForAccessibilityOnSiblings=false;
  private float maximumVelocity;
  private int significantVelocityThreshold;
  private boolean multipleScrollingChildrenSupported;
  private int peekHeight;
  private boolean peekHeightAuto;
  private int peekHeightMin;
  private int peekHeightGestureInsetBuffer;
  private ColorStateList backgroundTint;
  private int maxWidth=NO_MAX_SIZE;
  private int maxHeight=NO_MAX_SIZE;
  private int gestureInsetBottom;
  private boolean gestureInsetBottomIgnored;
  private boolean paddingBottomSystemWindowInsets;
  private boolean paddingLeftSystemWindowInsets;
  private boolean paddingRightSystemWindowInsets;
  private boolean paddingTopSystemWindowInsets;
  private boolean marginLeftSystemWindowInsets;
  private boolean marginRightSystemWindowInsets;
  private boolean marginTopSystemWindowInsets;
  private int insetBottom;
  private int insetTop;
  private boolean shouldRemoveExpandedCorners;
  private boolean expandedCornersRemoved;
  private final StateSettlingTracker stateSettlingTracker=new StateSettlingTracker();
  int expandedOffset;
  int fitToContentsOffset;
  int halfExpandedOffset;
  float halfExpandedRatio=0.5f;
  int collapsedOffset;
  float elevation=-1;
  boolean hideable;
  private boolean skipCollapsed;
  private boolean draggable=true;
  private boolean draggableOnNestedScroll=true;
  private boolean draggableOnNestedScrollLastDragIgnored;
  int state=STATE_COLLAPSED;
  int lastStableState=STATE_COLLAPSED;
  ViewDragHelper viewDragHelper;
  private boolean ignoreEvents;
  private int lastNestedScrollDy;
  private boolean nestedScrolled;
  private float hideFriction=HIDE_FRICTION;
  private int childHeight;
  int parentWidth;
  int parentHeight;
  WeakReference<V> viewRef;
  final List<WeakReference<View>> nestedScrollingChildrenRef=new ArrayList<>();
  private final ArrayList<BottomSheetCallback> callbacks=new ArrayList<>();
  int activePointerId;
  private int initialY=INVALID_POSITION;
  private WeakReference<View> currentTouchedScrollChildRef;
  boolean touchingScrollingChild;
  public void onAttachedToLayoutParams(  LayoutParams layoutParams){
    super.onAttachedToLayoutParams(layoutParams);
    viewRef=null;
    viewDragHelper=null;
    //bottomContainerBackHelper=null;
  }
  public void onDetachedFromLayoutParams(){
    super.onDetachedFromLayoutParams();
    viewRef=null;
    viewDragHelper=null;
    //bottomContainerBackHelper=null;
  }
  public boolean onMeasureChild(  CoordinatorLayout parent,  V child,  int parentWidthMeasureSpec,  int widthUsed,  int parentHeightMeasureSpec,  int heightUsed){
    MarginLayoutParams lp=(MarginLayoutParams)child.getLayoutParams();
    int childWidthMeasureSpec=getChildMeasureSpec(parentWidthMeasureSpec,parent.getPaddingLeft() + parent.getPaddingRight() + lp.leftMargin+ lp.rightMargin+ widthUsed,maxWidth,lp.width);
    int childHeightMeasureSpec=getChildMeasureSpec(parentHeightMeasureSpec,parent.getPaddingTop() + parent.getPaddingBottom() + lp.topMargin+ lp.bottomMargin+ heightUsed,maxHeight,lp.height);
    child.measure(childWidthMeasureSpec,childHeightMeasureSpec);
    return true;
  }
  private int getChildMeasureSpec(  int parentMeasureSpec,  int padding,  int maxSize,  int childDimension){
    int result=ViewGroup.getChildMeasureSpec(parentMeasureSpec,padding,childDimension);
    if (maxSize == NO_MAX_SIZE) {
      return result;
    }
 else {
      int mode=MeasureSpec.getMode(result);
      int size=MeasureSpec.getSize(result);
switch (mode) {
case MeasureSpec.EXACTLY:
        return MeasureSpec.makeMeasureSpec(min(size,maxSize),MeasureSpec.EXACTLY);
case MeasureSpec.AT_MOST:
case MeasureSpec.UNSPECIFIED:
default :
      return MeasureSpec.makeMeasureSpec(size == 0 ? maxSize : min(size,maxSize),MeasureSpec.AT_MOST);
  }
}
}
public boolean onLayoutChild(CoordinatorLayout parent,final V child,int layoutDirection){
if (false) {
  //child.setFitsSystemWindows(true);
}
if (viewRef == null) {
  peekHeightMin=0;
  //setWindowInsetsListener(child);
  //ViewCompat.setWindowInsetsAnimationCallback(child,new InsetsAnimationCallback(child));
  viewRef=new WeakReference<>(child);
  //bottomContainerBackHelper=new MaterialBottomContainerBackHelper(child);
  if (false) {
    //child.setBackground(materialShapeDrawable);
    //materialShapeDrawable.setElevation(elevation == -1 ? child.getElevation() : elevation);
  }
 else   if (backgroundTint != null) {
    ViewCompat.setBackgroundTintList(child,backgroundTint);
  }
  //updateAccessibilityActions();
  if (child.getImportantForAccessibility() == View.IMPORTANT_FOR_ACCESSIBILITY_AUTO) {
    child.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_YES);
  }
}
if (viewDragHelper == null) {
  viewDragHelper=ViewDragHelper.create(parent,dragCallback);
}
int savedTop=child.getTop();
parent.onLayoutChild(child,layoutDirection);
parentWidth=parent.getWidth();
parentHeight=parent.getHeight();
childHeight=child.getHeight();
if (parentHeight - childHeight < insetTop) {
  if (paddingTopSystemWindowInsets) {
    childHeight=(maxHeight == NO_MAX_SIZE) ? parentHeight : min(parentHeight,maxHeight);
  }
 else {
    int insetHeight=parentHeight - insetTop;
    childHeight=(maxHeight == NO_MAX_SIZE) ? insetHeight : min(insetHeight,maxHeight);
  }
}
fitToContentsOffset=max(0,parentHeight - childHeight);
calculateHalfExpandedOffset();
calculateCollapsedOffset();
if (state == STATE_EXPANDED) {
  ViewCompat.offsetTopAndBottom(child,getExpandedOffset());
}
 else if (state == STATE_HALF_EXPANDED) {
  ViewCompat.offsetTopAndBottom(child,halfExpandedOffset);
}
 else if (hideable && state == STATE_HIDDEN) {
  ViewCompat.offsetTopAndBottom(child,parentHeight);
}
 else if (state == STATE_COLLAPSED) {
  ViewCompat.offsetTopAndBottom(child,collapsedOffset);
}
 else if (state == STATE_DRAGGING || state == STATE_SETTLING) {
  ViewCompat.offsetTopAndBottom(child,savedTop - child.getTop());
}
//updateDrawableForTargetState(state,false);
nestedScrollingChildrenRef.clear();
if (multipleScrollingChildrenSupported) {
  populateScrollingChildren(child);
}
 else {
  nestedScrollingChildrenRef.add(new WeakReference<>(findScrollingChild(child)));
}
for (int i=0; i < callbacks.size(); i++) {
  callbacks.get(i).onLayout(child);
}
return true;
}
private boolean hasScrollingChild(){
for (WeakReference<View> ref : nestedScrollingChildrenRef) {
  if (ref.get() != null) {
    return true;
  }
}
return false;
}
private View getSingleScrollingChildOrNull(){
return !nestedScrollingChildrenRef.isEmpty() ? nestedScrollingChildrenRef.get(0).get() : null;
}
public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout,V child,View directTargetChild,View target,int axes,int type){
lastNestedScrollDy=0;
nestedScrolled=false;
return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
}
private boolean isViewScrollingChild(View v){
for (WeakReference<View> ref : nestedScrollingChildrenRef) {
  if (ref.get() == v) {
    return true;
  }
}
return false;
}
public void onNestedPreScroll(CoordinatorLayout coordinatorLayout,V child,View target,int dx,int dy,int[] consumed,int type){
if (type == ViewCompat.TYPE_NON_TOUCH) {
  return;
}
boolean isTargetScrollingChild=isViewScrollingChild(target);
if (isNestedScrollingCheckEnabled() && !isTargetScrollingChild) {
  return;
}
int currentTop=child.getTop();
int newTop=currentTop - dy;
if (dy > 0) {
  if (!nestedScrolled && !draggableOnNestedScroll && isTargetScrollingChild&& target.canScrollVertically(1)) {
    draggableOnNestedScrollLastDragIgnored=true;
    return;
  }
  if (newTop < getExpandedOffset()) {
    consumed[1]=currentTop - getExpandedOffset();
    ViewCompat.offsetTopAndBottom(child,-consumed[1]);
    setStateInternal(STATE_EXPANDED);
  }
 else {
    if (!draggable) {
      return;
    }
    consumed[1]=dy;
    ViewCompat.offsetTopAndBottom(child,-dy);
    setStateInternal(STATE_DRAGGING);
  }
}
 else if (dy < 0) {
  boolean canScrollUp=target.canScrollVertically(-1);
  if (!nestedScrolled && !draggableOnNestedScroll && isTargetScrollingChild&& canScrollUp) {
    draggableOnNestedScrollLastDragIgnored=true;
    return;
  }
  if (!canScrollUp) {
    if (newTop <= collapsedOffset || canBeHiddenByDragging()) {
      if (!draggable) {
        return;
      }
      consumed[1]=dy;
      ViewCompat.offsetTopAndBottom(child,-dy);
      setStateInternal(STATE_DRAGGING);
    }
 else {
      consumed[1]=currentTop - collapsedOffset;
      ViewCompat.offsetTopAndBottom(child,-consumed[1]);
      setStateInternal(STATE_COLLAPSED);
    }
  }
}
dispatchOnSlide(child.getTop());
lastNestedScrollDy=dy;
nestedScrolled=true;
draggableOnNestedScrollLastDragIgnored=false;
}
public void onStopNestedScroll(CoordinatorLayout coordinatorLayout,V child,View target,int type){
if (child.getTop() == getExpandedOffset()) {
  setStateInternal(STATE_EXPANDED);
  return;
}
if (isNestedScrollingCheckEnabled() && (!isViewScrollingChild(target) || !nestedScrolled)) {
  return;
}
int targetState;
if (lastNestedScrollDy > 0) {
  if (fitToContents) {
    targetState=STATE_EXPANDED;
  }
 else {
    int currentTop=child.getTop();
    if (currentTop > halfExpandedOffset) {
      targetState=STATE_HALF_EXPANDED;
    }
 else {
      targetState=STATE_EXPANDED;
    }
  }
}
 else if (hideable && shouldHide(child,getYVelocity())) {
  targetState=STATE_HIDDEN;
}
 else if (lastNestedScrollDy == 0) {
  int currentTop=child.getTop();
  if (fitToContents) {
    if (Math.abs(currentTop - fitToContentsOffset) < Math.abs(currentTop - collapsedOffset)) {
      targetState=STATE_EXPANDED;
    }
 else {
      targetState=STATE_COLLAPSED;
    }
  }
 else {
    if (currentTop < halfExpandedOffset) {
      if (currentTop < Math.abs(currentTop - collapsedOffset)) {
        targetState=STATE_EXPANDED;
      }
 else {
        if (shouldSkipHalfExpandedStateWhenDragging()) {
          targetState=STATE_COLLAPSED;
        }
 else {
          targetState=STATE_HALF_EXPANDED;
        }
      }
    }
 else {
      if (Math.abs(currentTop - halfExpandedOffset) < Math.abs(currentTop - collapsedOffset)) {
        targetState=STATE_HALF_EXPANDED;
      }
 else {
        targetState=STATE_COLLAPSED;
      }
    }
  }
}
 else {
  if (fitToContents) {
    targetState=STATE_COLLAPSED;
  }
 else {
    int currentTop=child.getTop();
    if (Math.abs(currentTop - halfExpandedOffset) < Math.abs(currentTop - collapsedOffset)) {
      targetState=STATE_HALF_EXPANDED;
    }
 else {
      targetState=STATE_COLLAPSED;
    }
  }
}
startSettling(child,targetState,false);
nestedScrolled=false;
}
public void onNestedScroll(CoordinatorLayout coordinatorLayout,V child,View target,int dxConsumed,int dyConsumed,int dxUnconsumed,int dyUnconsumed,int type,int[] consumed){
}
public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout,V child,View target,float velocityX,float velocityY){
if (isNestedScrollingCheckEnabled() && hasScrollingChild()) {
  return isViewScrollingChild(target) && ((state != STATE_EXPANDED && !draggableOnNestedScrollLastDragIgnored) || super.onNestedPreFling(coordinatorLayout,child,target,velocityX,velocityY));
}
 else {
  return false;
}
}
public boolean isFitToContents(){
return fitToContents;
}
public void setFitToContents(boolean fitToContents){
if (this.fitToContents == fitToContents) {
  return;
}
this.fitToContents=fitToContents;
if (viewRef != null) {
  calculateCollapsedOffset();
}
setStateInternal((this.fitToContents && state == STATE_HALF_EXPANDED) ? STATE_EXPANDED : state);
//updateDrawableForTargetState(state,true);
//updateAccessibilityActions();
}
public void setMaxWidth(int maxWidth){
this.maxWidth=maxWidth;
}
public int getMaxWidth(){
return maxWidth;
}
public void setMaxHeight(int maxHeight){
this.maxHeight=maxHeight;
}
public int getMaxHeight(){
return maxHeight;
}
public void setPeekHeight(int peekHeight){
setPeekHeight(peekHeight,false);
}
public final void setPeekHeight(int peekHeight,boolean animate){
boolean layout=false;
if (peekHeight == PEEK_HEIGHT_AUTO) {
  if (!peekHeightAuto) {
    peekHeightAuto=true;
    layout=true;
  }
}
 else if (peekHeightAuto || this.peekHeight != peekHeight) {
  peekHeightAuto=false;
  this.peekHeight=max(0,peekHeight);
  layout=true;
}
if (layout) {
  updatePeekHeight(animate);
}
}
private void updatePeekHeight(boolean animate){
if (viewRef != null) {
  calculateCollapsedOffset();
  if (state == STATE_COLLAPSED) {
    V view=viewRef.get();
    if (view != null) {
      if (animate) {
        setState(STATE_COLLAPSED);
      }
 else {
        view.requestLayout();
      }
    }
  }
}
}
public int getPeekHeight(){
return peekHeightAuto ? PEEK_HEIGHT_AUTO : peekHeight;
}
public void setHalfExpandedRatio(float ratio){
if ((ratio <= 0) || (ratio >= 1)) {
  throw new IllegalArgumentException("ratio must be a float value between 0 and 1");
}
this.halfExpandedRatio=ratio;
if (viewRef != null) {
  calculateHalfExpandedOffset();
}
}
public float getHalfExpandedRatio(){
return halfExpandedRatio;
}
public void setExpandedOffset(int offset){
if (offset < 0) {
  throw new IllegalArgumentException("offset must be greater than or equal to 0");
}
this.expandedOffset=offset;
//updateDrawableForTargetState(state,true);
}
public int getExpandedOffset(){
return fitToContents ? fitToContentsOffset : Math.max(expandedOffset,paddingTopSystemWindowInsets ? 0 : insetTop);
}
public float calculateSlideOffset(){
if (viewRef == null || viewRef.get() == null) {
  return -1;
}
return calculateSlideOffsetWithTop(viewRef.get().getTop());
}
public void setHideable(boolean hideable){
if (this.hideable != hideable) {
  this.hideable=hideable;
  if (!hideable && state == STATE_HIDDEN) {
    setState(STATE_COLLAPSED);
  }
  //updateAccessibilityActions();
}
}
public boolean isHideable(){
return hideable;
}
public void setSkipCollapsed(boolean skipCollapsed){
this.skipCollapsed=skipCollapsed;
}
public boolean getSkipCollapsed(){
return skipCollapsed;
}
public void setDraggable(boolean draggable){
this.draggable=draggable;
}
public boolean isDraggable(){
return draggable;
}
public void setDraggableOnNestedScroll(boolean draggableOnNestedScroll){
this.draggableOnNestedScroll=draggableOnNestedScroll;
}
public boolean isDraggableOnNestedScroll(){
return draggableOnNestedScroll;
}
public void setSignificantVelocityThreshold(int significantVelocityThreshold){
this.significantVelocityThreshold=significantVelocityThreshold;
}
public int getSignificantVelocityThreshold(){
return this.significantVelocityThreshold;
}
public void setSaveFlags(int flags){
this.saveFlags=flags;
}
public int getSaveFlags(){
return this.saveFlags;
}
public void setHideFriction(float hideFriction){
this.hideFriction=hideFriction;
}
public void setBottomSheetCallback(BottomSheetCallback callback){
Log.w(TAG,"BottomSheetBehavior now supports multiple callbacks. `setBottomSheetCallback()` removes" + " all existing callbacks, including ones set internally by library authors, which" + " may result in unintended behavior. This may change in the future. Please use"+ " `addBottomSheetCallback()` and `removeBottomSheetCallback()` instead to set your"+ " own callbacks.");
callbacks.clear();
if (callback != null) {
  callbacks.add(callback);
}
}
public void addBottomSheetCallback(BottomSheetCallback callback){
if (!callbacks.contains(callback)) {
  callbacks.add(callback);
}
}
public void removeBottomSheetCallback(BottomSheetCallback callback){
callbacks.remove(callback);
}
public void setState(int state){
if (state == STATE_DRAGGING || state == STATE_SETTLING) {
  throw new IllegalArgumentException("STATE_" + (state == STATE_DRAGGING ? "DRAGGING" : "SETTLING") + " should not be set externally.");
}
if (!hideable && state == STATE_HIDDEN) {
  Log.w(TAG,"Cannot set state: " + state);
  return;
}
final int finalState;
if (state == STATE_HALF_EXPANDED && fitToContents && getTopOffsetForState(state) <= fitToContentsOffset) {
  finalState=STATE_EXPANDED;
}
 else {
  finalState=state;
}
if (viewRef == null || viewRef.get() == null) {
  setStateInternal(state);
}
 else {
  final V child=viewRef.get();
  runAfterLayout(child,new Runnable(){
    public void run(){
      startSettling(child,finalState,false);
    }
  }
);
}
}
private void runAfterLayout(V child,Runnable runnable){
if (isLayouting(child)) {
  child.post(runnable);
}
 else {
  runnable.run();
}
}
private boolean isLayouting(V child){
ViewParent parent=child.getParent();
return parent != null && parent.isLayoutRequested() && child.isAttachedToWindow();
}
public void setGestureInsetBottomIgnored(boolean gestureInsetBottomIgnored){
this.gestureInsetBottomIgnored=gestureInsetBottomIgnored;
}
public boolean isGestureInsetBottomIgnored(){
return gestureInsetBottomIgnored;
}
public boolean isShouldRemoveExpandedCorners(){
return shouldRemoveExpandedCorners;
}
public int getState(){
return state;
}
void setStateInternal(int state){
if (this.state == state) {
  return;
}
this.state=state;
if (state == STATE_COLLAPSED || state == STATE_EXPANDED || state == STATE_HALF_EXPANDED || (hideable && state == STATE_HIDDEN)) {
  this.lastStableState=state;
}
if (viewRef == null) {
  return;
}
View bottomSheet=viewRef.get();
if (bottomSheet == null) {
  return;
}
if (state == STATE_EXPANDED) {
  //updateImportantForAccessibility(true);
}
 else if (state == STATE_HALF_EXPANDED || state == STATE_HIDDEN || state == STATE_COLLAPSED) {
  //updateImportantForAccessibility(false);
}
//updateDrawableForTargetState(state,true);
for (int i=0; i < callbacks.size(); i++) {
  callbacks.get(i).onStateChanged(bottomSheet,state);
}
//updateAccessibilityActions();
}
private int calculatePeekHeight(){
if (peekHeightAuto) {
  int desiredHeight=max(peekHeightMin,parentHeight - parentWidth * 9 / 16);
  return min(desiredHeight,childHeight) + insetBottom;
}
if (!gestureInsetBottomIgnored && !paddingBottomSystemWindowInsets && gestureInsetBottom > 0) {
  return max(peekHeight,gestureInsetBottom + peekHeightGestureInsetBuffer);
}
return peekHeight + insetBottom;
}
private void calculateCollapsedOffset(){
int peek=calculatePeekHeight();
if (fitToContents) {
  collapsedOffset=max(parentHeight - peek,fitToContentsOffset);
}
 else {
  collapsedOffset=parentHeight - peek;
}
}
private void calculateHalfExpandedOffset(){
this.halfExpandedOffset=(int)(parentHeight * (1 - halfExpandedRatio));
}
private float calculateSlideOffsetWithTop(int top){
return (top > collapsedOffset || collapsedOffset == getExpandedOffset()) ? (float)(collapsedOffset - top) / (parentHeight - collapsedOffset) : (float)(collapsedOffset - top) / (collapsedOffset - getExpandedOffset());
}
boolean shouldHide(View child,float yvel){
if (skipCollapsed) {
  return true;
}
if (!isHideableWhenDragging()) {
  return false;
}
if (child.getTop() < collapsedOffset) {
  return false;
}
int peek=calculatePeekHeight();
final float newTop=child.getTop() + yvel * hideFriction;
return Math.abs(newTop - collapsedOffset) / (float)peek > HIDE_THRESHOLD;
}
View findScrollingChild(View view){
if (view.getVisibility() != View.VISIBLE) {
  return null;
}
if (view.isNestedScrollingEnabled()) {
  return view;
}
if (view instanceof ViewGroup) {
  ViewGroup group=(ViewGroup)view;
  for (int i=0, count=group.getChildCount(); i < count; i++) {
    View scrollingChild=findScrollingChild(group.getChildAt(i));
    if (scrollingChild != null) {
      return scrollingChild;
    }
  }
}
return null;
}
void populateScrollingChildren(View view){
if (view.getVisibility() != View.VISIBLE) {
  return;
}
if (view.isNestedScrollingEnabled()) {
  nestedScrollingChildrenRef.add(new WeakReference<>(view));
  return;
}
if (view instanceof ViewGroup) {
  ViewGroup group=(ViewGroup)view;
  for (int i=0; i < group.getChildCount(); i++) {
    populateScrollingChildren(group.getChildAt(i));
  }
}
}
private void startSettling(View child,int state,boolean isReleasingView){
int top=getTopOffsetForState(state);
boolean settling=viewDragHelper != null && (isReleasingView ? viewDragHelper.settleCapturedViewAt(child.getLeft(),top) : viewDragHelper.smoothSlideViewTo(child,child.getLeft(),top));
if (settling) {
  setStateInternal(STATE_SETTLING);
  //updateDrawableForTargetState(state,true);
  stateSettlingTracker.continueSettlingToState(state);
}
 else {
  setStateInternal(state);
}
}
private int getTopOffsetForState(int state){
switch (state) {
case STATE_COLLAPSED:
  return collapsedOffset;
case STATE_EXPANDED:
return getExpandedOffset();
case STATE_HALF_EXPANDED:
return halfExpandedOffset;
case STATE_HIDDEN:
return parentHeight;
default :
}
throw new IllegalArgumentException("Invalid state to get top offset: " + state);
}
private final ViewDragHelper.Callback dragCallback=new ViewDragHelper.Callback(){
private long viewCapturedMillis;
public boolean tryCaptureView(View child,int pointerId){
if (state == STATE_DRAGGING) {
return false;
}
if (touchingScrollingChild) {
return false;
}
if (state == STATE_EXPANDED && activePointerId == pointerId) {
View scroll;
if (multipleScrollingChildrenSupported) {
scroll=currentTouchedScrollChildRef != null ? currentTouchedScrollChildRef.get() : null;
}
 else {
scroll=getSingleScrollingChildOrNull();
}
if (scroll != null && scroll.canScrollVertically(-1)) {
return false;
}
}
viewCapturedMillis=SystemClock.uptimeMillis();
return viewRef != null && viewRef.get() == child;
}
public void onViewPositionChanged(View changedView,int left,int top,int dx,int dy){
dispatchOnSlide(top);
}
public void onViewDragStateChanged(int state){
if (state == ViewDragHelper.STATE_DRAGGING && draggable) {
setStateInternal(STATE_DRAGGING);
}
}
private boolean releasedLow(View child){
return child.getTop() > (parentHeight + getExpandedOffset()) / 2;
}
public void onViewReleased(View releasedChild,float xvel,float yvel){
int targetState;
if (yvel < 0) {
if (fitToContents) {
targetState=STATE_EXPANDED;
}
 else {
int currentTop=releasedChild.getTop();
long dragDurationMillis=SystemClock.uptimeMillis() - viewCapturedMillis;
if (shouldSkipHalfExpandedStateWhenDragging()) {
float yPositionPercentage=currentTop * 100f / parentHeight;
if (shouldExpandOnUpwardDrag(dragDurationMillis,yPositionPercentage)) {
  targetState=STATE_EXPANDED;
}
 else {
  targetState=STATE_COLLAPSED;
}
}
 else {
if (currentTop > halfExpandedOffset) {
  targetState=STATE_HALF_EXPANDED;
}
 else {
  targetState=STATE_EXPANDED;
}
}
}
}
 else if (hideable && shouldHide(releasedChild,yvel)) {
if ((Math.abs(xvel) < Math.abs(yvel) && yvel > significantVelocityThreshold) || releasedLow(releasedChild)) {
targetState=STATE_HIDDEN;
}
 else if (fitToContents) {
targetState=STATE_EXPANDED;
}
 else if (Math.abs(releasedChild.getTop() - getExpandedOffset()) < Math.abs(releasedChild.getTop() - halfExpandedOffset)) {
targetState=STATE_EXPANDED;
}
 else {
targetState=STATE_HALF_EXPANDED;
}
}
 else if (yvel == 0.f || Math.abs(xvel) > Math.abs(yvel)) {
int currentTop=releasedChild.getTop();
if (fitToContents) {
if (Math.abs(currentTop - fitToContentsOffset) < Math.abs(currentTop - collapsedOffset)) {
targetState=STATE_EXPANDED;
}
 else {
targetState=STATE_COLLAPSED;
}
}
 else {
if (currentTop < halfExpandedOffset) {
if (currentTop < Math.abs(currentTop - collapsedOffset)) {
  targetState=STATE_EXPANDED;
}
 else {
  if (shouldSkipHalfExpandedStateWhenDragging()) {
    targetState=STATE_COLLAPSED;
  }
 else {
    targetState=STATE_HALF_EXPANDED;
  }
}
}
 else {
if (Math.abs(currentTop - halfExpandedOffset) < Math.abs(currentTop - collapsedOffset)) {
  if (shouldSkipHalfExpandedStateWhenDragging()) {
    targetState=STATE_COLLAPSED;
  }
 else {
    targetState=STATE_HALF_EXPANDED;
  }
}
 else {
  targetState=STATE_COLLAPSED;
}
}
}
}
 else {
if (fitToContents) {
targetState=STATE_COLLAPSED;
}
 else {
int currentTop=releasedChild.getTop();
if (Math.abs(currentTop - halfExpandedOffset) < Math.abs(currentTop - collapsedOffset)) {
if (shouldSkipHalfExpandedStateWhenDragging()) {
  targetState=STATE_COLLAPSED;
}
 else {
  targetState=STATE_HALF_EXPANDED;
}
}
 else {
targetState=STATE_COLLAPSED;
}
}
}
startSettling(releasedChild,targetState,shouldSkipSmoothAnimation());
}
public int clampViewPositionVertical(View child,int top,int dy){
return r.android.util.MathUtils.clamp(top,getExpandedOffset(),getViewVerticalDragRange(child));
}
public int clampViewPositionHorizontal(View child,int left,int dx){
return child.getLeft();
}
public int getViewVerticalDragRange(View child){
if (canBeHiddenByDragging()) {
return parentHeight;
}
 else {
return collapsedOffset;
}
}
}
;
void dispatchOnSlide(int top){
View bottomSheet=viewRef.get();
if (bottomSheet != null && !callbacks.isEmpty()) {
float slideOffset=calculateSlideOffsetWithTop(top);
for (int i=0; i < callbacks.size(); i++) {
callbacks.get(i).onSlide(bottomSheet,slideOffset);
}
}
}
public boolean isNestedScrollingCheckEnabled(){
return true;
}
public boolean shouldSkipHalfExpandedStateWhenDragging(){
return false;
}
public boolean shouldSkipSmoothAnimation(){
return true;
}
public boolean isHideableWhenDragging(){
return true;
}
private boolean canBeHiddenByDragging(){
return isHideable() && isHideableWhenDragging();
}
public boolean shouldExpandOnUpwardDrag(long dragDurationMillis,float yPositionPercentage){
return false;
}
private class StateSettlingTracker {
private int targetState;
private boolean isContinueSettlingRunnablePosted;
private final Runnable continueSettlingRunnable=new Runnable(){
public void run(){
isContinueSettlingRunnablePosted=false;
if (viewDragHelper != null && viewDragHelper.continueSettling(true)) {
continueSettlingToState(targetState);
}
 else if (state == STATE_SETTLING) {
setStateInternal(targetState);
}
}
}
;
void continueSettlingToState(int targetState){
if (viewRef == null || viewRef.get() == null) {
return;
}
this.targetState=targetState;
if (!isContinueSettlingRunnablePosted) {
viewRef.get().postOnAnimation(continueSettlingRunnable);
isContinueSettlingRunnablePosted=true;
}
}
}
private float getYVelocity(){
return 0;
}
}
