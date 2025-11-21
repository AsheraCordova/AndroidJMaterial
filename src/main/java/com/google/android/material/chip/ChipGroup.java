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
 * Copyright 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.android.material.chip;
import r.android.graphics.drawable.Drawable;
import r.android.view.View;
import r.android.view.ViewGroup;
import com.google.android.material.internal.CheckableGroup;
import com.google.android.material.internal.FlowLayout;
import java.util.List;
public class ChipGroup extends FlowLayout {
public interface OnCheckedChangeListener {
    void onCheckedChanged(    ChipGroup group,    int checkedId);
  }
public interface OnCheckedStateChangeListener {
    void onCheckedChanged(    ChipGroup group,    List<Integer> checkedIds);
  }
public static class LayoutParams extends MarginLayoutParams {
    public LayoutParams(    ViewGroup.LayoutParams source){
      super(source);
    }
    public LayoutParams(    int width,    int height){
      super(width,height);
    }
  }
  private int chipSpacingHorizontal;
  private int chipSpacingVertical;
  private OnCheckedStateChangeListener onCheckedStateChangeListener;
  private final CheckableGroup<Chip> checkableGroup=new CheckableGroup<>();
  private final int defaultCheckedId;
  private final PassThroughHierarchyChangeListener passThroughListener=new PassThroughHierarchyChangeListener();
  protected ViewGroup.LayoutParams generateLayoutParams(  ViewGroup.LayoutParams lp){
    return new ChipGroup.LayoutParams(lp);
  }
  protected ViewGroup.LayoutParams generateDefaultLayoutParams(){
    return new ChipGroup.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
  }
  protected boolean checkLayoutParams(  ViewGroup.LayoutParams p){
    return super.checkLayoutParams(p) && (p instanceof ChipGroup.LayoutParams);
  }
  public void setDividerDrawableHorizontal(  Drawable divider){
    throw new UnsupportedOperationException("Changing divider drawables have no effect. ChipGroup do not use divider drawables as " + "spacing.");
  }
  public void setDividerDrawableVertical(  Drawable divider){
    throw new UnsupportedOperationException("Changing divider drawables have no effect. ChipGroup do not use divider drawables as " + "spacing.");
  }
  public void setShowDividerHorizontal(  int dividerMode){
    throw new UnsupportedOperationException("Changing divider modes has no effect. ChipGroup do not use divider drawables as spacing.");
  }
  public void setShowDividerVertical(  int dividerMode){
    throw new UnsupportedOperationException("Changing divider modes has no effect. ChipGroup do not use divider drawables as spacing.");
  }
  public void setFlexWrap(  int flexWrap){
    throw new UnsupportedOperationException("Changing flex wrap not allowed. ChipGroup exposes a singleLine attribute instead.");
  }
  public void check(  int id){
    checkableGroup.check(id);
  }
  public int getCheckedChipId(){
    return checkableGroup.getSingleCheckedId();
  }
  public List<Integer> getCheckedChipIds(){
    return checkableGroup.getCheckedIdsSortedByChildOrder(this);
  }
  public void clearCheck(){
    checkableGroup.clearCheck();
  }
  public void setOnCheckedChangeListener(  final OnCheckedChangeListener listener){
    if (listener == null) {
      setOnCheckedStateChangeListener(null);
      return;
    }
    setOnCheckedStateChangeListener(new OnCheckedStateChangeListener(){
      public void onCheckedChanged(      ChipGroup group,      List<Integer> checkedIds){
        if (!checkableGroup.isSingleSelection()) {
          return;
        }
        listener.onCheckedChanged(group,getCheckedChipId());
      }
    }
);
  }
  public void setOnCheckedStateChangeListener(  OnCheckedStateChangeListener listener){
    onCheckedStateChangeListener=listener;
  }
  private int getVisibleChipCount(){
    int count=0;
    for (int i=0; i < getChildCount(); i++) {
      if (getChildAt(i) instanceof Chip && isChildVisible(i)) {
        count++;
      }
    }
    return count;
  }
  int getIndexOfChip(  View child){
    if (!(child instanceof Chip)) {
      return -1;
    }
    int index=0;
    for (int i=0; i < getChildCount(); i++) {
      View current=getChildAt(i);
      if (current instanceof Chip && isChildVisible(i)) {
        Chip chip=(Chip)current;
        if (chip == child) {
          return index;
        }
        index++;
      }
    }
    return -1;
  }
  private boolean isChildVisible(  int i){
    return getChildAt(i).getVisibility() == VISIBLE;
  }
  public void setChipSpacing(  int chipSpacing){
    setChipSpacingHorizontal(chipSpacing);
    setChipSpacingVertical(chipSpacing);
  }
  public int getChipSpacingHorizontal(){
    return chipSpacingHorizontal;
  }
  public void setChipSpacingHorizontal(  int chipSpacingHorizontal){
    if (this.chipSpacingHorizontal != chipSpacingHorizontal) {
      this.chipSpacingHorizontal=chipSpacingHorizontal;
      setItemSpacing(chipSpacingHorizontal);
      requestLayout();
    }
  }
  public int getChipSpacingVertical(){
    return chipSpacingVertical;
  }
  public void setChipSpacingVertical(  int chipSpacingVertical){
    if (this.chipSpacingVertical != chipSpacingVertical) {
      this.chipSpacingVertical=chipSpacingVertical;
      setLineSpacing(chipSpacingVertical);
      requestLayout();
    }
  }
  public boolean isSingleLine(){
    return super.isSingleLine();
  }
  public void setSingleLine(  boolean singleLine){
    super.setSingleLine(singleLine);
  }
  public boolean isSingleSelection(){
    return checkableGroup.isSingleSelection();
  }
  public void setSingleSelection(  boolean singleSelection){
    checkableGroup.setSingleSelection(singleSelection);
  }
  public void setSelectionRequired(  boolean selectionRequired){
    checkableGroup.setSelectionRequired(selectionRequired);
  }
  public boolean isSelectionRequired(){
    return checkableGroup.isSelectionRequired();
  }
private class PassThroughHierarchyChangeListener implements OnHierarchyChangeListener {
    private OnHierarchyChangeListener onHierarchyChangeListener;
    public void onChildViewAdded(    View parent,    View child){
      if (parent == ChipGroup.this && child instanceof Chip) {
        int id=child.getId();
        if (id == View.NO_ID) {
          //id=ViewCompat.generateViewId();
          child.setId(id);
        }
        checkableGroup.addCheckable((Chip)child);
      }
      if (onHierarchyChangeListener != null) {
        onHierarchyChangeListener.onChildViewAdded(parent,child);
      }
    }
    public void onChildViewRemoved(    View parent,    View child){
      if (parent == ChipGroup.this && child instanceof Chip) {
        checkableGroup.removeCheckable((Chip)child);
      }
      if (onHierarchyChangeListener != null) {
        onHierarchyChangeListener.onChildViewRemoved(parent,child);
      }
    }
  }
  public ChipGroup(){
    defaultCheckedId=View.NO_ID;
    setOnHierarchyChangeListener(passThroughListener);
    checkableGroup.setOnCheckedStateChangeListener(new CheckableGroup.OnCheckedStateChangeListener(){
      @Override public void onCheckedStateChanged(      java.util.Set<Integer> checkedIds){
        if (onCheckedStateChangeListener != null) {
          onCheckedStateChangeListener.onCheckedChanged(ChipGroup.this,checkableGroup.getCheckedIdsSortedByChildOrder(ChipGroup.this));
        }
      }
    }
);
  }
}
