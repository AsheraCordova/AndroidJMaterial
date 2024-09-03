package com.google.android.material.badge;
import static com.google.android.material.badge.BadgeUtils.updateBadgeBounds;
import r.android.content.Context;
import r.android.content.res.ColorStateList;
import r.android.graphics.Rect;
import r.android.graphics.drawable.Drawable;
import r.android.util.Log;
import r.android.view.Gravity;
import r.android.view.View;
import r.android.view.ViewGroup;
import r.android.view.ViewParent;
import r.android.widget.FrameLayout;
import java.lang.ref.WeakReference;
import java.text.NumberFormat;
public class BadgeDrawable extends Drawable {
  private static final String TAG="Badge";
  public static final int TOP_END=Gravity.TOP | Gravity.END;
  public static final int TOP_START=Gravity.TOP | Gravity.START;
  public static final int BOTTOM_END=Gravity.BOTTOM | Gravity.END;
  public static final int BOTTOM_START=Gravity.BOTTOM | Gravity.START;
  static final String DEFAULT_EXCEED_MAX_BADGE_NUMBER_SUFFIX="+";
  static final String DEFAULT_EXCEED_MAX_BADGE_TEXT_SUFFIX="\u2026";
  static final int OFFSET_ALIGNMENT_MODE_EDGE=0;
  static final int OFFSET_ALIGNMENT_MODE_LEGACY=1;
  public static final int BADGE_FIXED_EDGE_START=0;
  public static final int BADGE_FIXED_EDGE_END=1;
  static final int BADGE_RADIUS_NOT_SPECIFIED=-1;
  public static final int BADGE_CONTENT_NOT_TRUNCATED=-2;
  private final WeakReference<Context> contextRef;
  private final MaterialShapeDrawable shapeDrawable;
  private final TextDrawableHelper textDrawableHelper;
  private final Rect badgeBounds;
  private final BadgeState state;
  private float badgeCenterX;
  private float badgeCenterY;
  private int maxBadgeNumber;
  private float cornerRadius;
  private float halfBadgeWidth;
  private float halfBadgeHeight;
  private WeakReference<View> anchorViewRef;
  private WeakReference<FrameLayout> customBadgeParentRef;
  public void setVisible(  boolean visible){
    state.setVisible(visible);
    onVisibilityUpdated();
  }
  private void onVisibilityUpdated(){
    boolean visible=state.isVisible();
    setVisible(visible,false);
  }
  public void updateBadgeCoordinates(  View anchorView,  ViewGroup customBadgeParent){
    if (!(customBadgeParent instanceof FrameLayout)) {
      throw new IllegalArgumentException("customBadgeParent must be a FrameLayout");
    }
    updateBadgeCoordinates(anchorView,(FrameLayout)customBadgeParent);
  }
  public void updateBadgeCoordinates(  View anchorView){
    updateBadgeCoordinates(anchorView,null);
  }
  public void updateBadgeCoordinates(  View anchorView,  FrameLayout customBadgeParent){
    this.anchorViewRef=new WeakReference<>(anchorView);
    this.customBadgeParentRef=new WeakReference<>(customBadgeParent);
    updateAnchorParentToNotClip(anchorView);
    updateCenterAndBounds();
    invalidateSelf();
  }
  public FrameLayout getCustomBadgeParent(){
    return customBadgeParentRef != null ? customBadgeParentRef.get() : null;
  }
  private static void updateAnchorParentToNotClip(  View anchorView){
    ViewGroup anchorViewParent=(ViewGroup)anchorView.getParent();
    anchorViewParent.setMyAttribute("clipChildren",false);
    anchorViewParent.setClipToPadding(false);
  }
  public void setBackgroundColor(  int backgroundColor){
    state.setBackgroundColor(backgroundColor);
    onBackgroundColorUpdated();
  }
  private void onBackgroundColorUpdated(){
    ColorStateList backgroundColorStateList=ColorStateList.valueOf(state.getBackgroundColor());
    if (shapeDrawable.getFillColor() != backgroundColorStateList) {
      shapeDrawable.setFillColor(backgroundColorStateList);
      invalidateSelf();
    }
  }
  public void setBadgeTextColor(  int badgeTextColor){
    if (textDrawableHelper.getTextPaint().getColor() != badgeTextColor) {
      state.setBadgeTextColor(badgeTextColor);
      onBadgeTextColorUpdated();
    }
  }
  private void onBadgeTextColorUpdated(){
    textDrawableHelper.getTextPaint().setColor(state.getBadgeTextColor());
    invalidateSelf();
  }
  public boolean hasNumber(){
    return !state.hasText() && state.hasNumber();
  }
  public int getNumber(){
    return state.hasNumber() ? state.getNumber() : 0;
  }
  public void setNumber(  int number){
    number=Math.max(0,number);
    if (this.state.getNumber() != number) {
      state.setNumber(number);
      onNumberUpdated();
    }
  }
  private void onNumberUpdated(){
    if (!hasText()) {
      onBadgeContentUpdated();
    }
  }
  public boolean hasText(){
    return state.hasText();
  }
  public String getText(){
    return state.getText();
  }
  public int getMaxCharacterCount(){
    return state.getMaxCharacterCount();
  }
  public void setMaxCharacterCount(  int maxCharacterCount){
    if (this.state.getMaxCharacterCount() != maxCharacterCount) {
      this.state.setMaxCharacterCount(maxCharacterCount);
      onMaxBadgeLengthUpdated();
    }
  }
  public int getMaxNumber(){
    return state.getMaxNumber();
  }
  private void onMaxBadgeLengthUpdated(){
    updateMaxBadgeNumber();
    textDrawableHelper.setTextSizeDirty(true);
    updateCenterAndBounds();
    invalidateSelf();
  }
  public void setBadgeGravity(  int gravity){
    if (gravity == BOTTOM_START || gravity == BOTTOM_END) {
      Log.w(TAG,"Bottom badge gravities are deprecated; please use a top gravity instead.");
    }
    if (state.getBadgeGravity() != gravity) {
      state.setBadgeGravity(gravity);
      onBadgeGravityUpdated();
    }
  }
  private void onBadgeGravityUpdated(){
    if (anchorViewRef != null && anchorViewRef.get() != null) {
      updateBadgeCoordinates(anchorViewRef.get(),customBadgeParentRef != null ? customBadgeParentRef.get() : null);
    }
  }
  public int getAlpha(){
    return state.getAlpha();
  }
  public void setAlpha(  int alpha){
    state.setAlpha(alpha);
    onAlphaUpdated();
  }
  private void onAlphaUpdated(){
    textDrawableHelper.getTextPaint().setAlpha(getAlpha());
    invalidateSelf();
  }
  public void setHorizontalOffset(  int px){
    setHorizontalOffsetWithoutText(px);
    setHorizontalOffsetWithText(px);
  }
  public int getHorizontalOffset(){
    return state.getHorizontalOffsetWithoutText();
  }
  public void setHorizontalOffsetWithoutText(  int px){
    state.setHorizontalOffsetWithoutText(px);
    updateCenterAndBounds();
  }
  public void setHorizontalOffsetWithText(  int px){
    state.setHorizontalOffsetWithText(px);
    updateCenterAndBounds();
  }
  public void setAdditionalHorizontalOffset(  int px){
    state.setAdditionalHorizontalOffset(px);
    updateCenterAndBounds();
  }
  public void setVerticalOffset(  int px){
    setVerticalOffsetWithoutText(px);
    setVerticalOffsetWithText(px);
  }
  public void setVerticalOffsetWithoutText(  int px){
    state.setVerticalOffsetWithoutText(px);
    updateCenterAndBounds();
  }
  public void setVerticalOffsetWithText(  int px){
    state.setVerticalOffsetWithText(px);
    updateCenterAndBounds();
  }
  public void setAdditionalVerticalOffset(  int px){
    state.setAdditionalVerticalOffset(px);
    updateCenterAndBounds();
  }
  private void onBadgeShapeAppearanceUpdated(){
    Context context=contextRef.get();
    if (context == null) {
      return;
    }
    //shapeDrawable.setShapeAppearanceModel(ShapeAppearanceModel.builder(context,hasBadgeContent() ? state.getBadgeWithTextShapeAppearanceResId() : state.getBadgeShapeAppearanceResId(),hasBadgeContent() ? state.getBadgeWithTextShapeAppearanceOverlayResId() : state.getBadgeShapeAppearanceOverlayResId()).build());
    invalidateSelf();
  }
  private void updateCenterAndBounds(){
    Context context=contextRef.get();
    View anchorView=anchorViewRef != null ? anchorViewRef.get() : null;
    if (context == null || anchorView == null) {
      return;
    }
    Rect tmpRect=new Rect();
    tmpRect.set(badgeBounds);
    Rect anchorRect=new Rect();
    anchorView.getDrawingRect(anchorRect);
    ViewGroup customBadgeParent=customBadgeParentRef != null ? customBadgeParentRef.get() : null;
    if (customBadgeParent != null) {
      //customBadgeParent.offsetDescendantRectToMyCoords(anchorView,anchorRect);
    }
    calculateCenterAndBounds(anchorRect,anchorView);
    updateBadgeBounds(badgeBounds,badgeCenterX,badgeCenterY,halfBadgeWidth,halfBadgeHeight);
    if (cornerRadius != BADGE_RADIUS_NOT_SPECIFIED) {
      shapeDrawable.setCornerSize(cornerRadius);
    }
    if (!tmpRect.equals(badgeBounds)) {
      shapeDrawable.setBounds(badgeBounds);
    }
  }
  private int getTotalVerticalOffsetForState(){
    int vOffset=state.getVerticalOffsetWithoutText();
    if (hasBadgeContent()) {
      vOffset=state.getVerticalOffsetWithText();
      Context context=contextRef.get();
      if (context != null) {
        //float progress=AnimationUtils.lerp(0F,1F,FONT_SCALE_THRESHOLD,1F,MaterialResources.getFontScale(context) - 1F);
        //vOffset=AnimationUtils.lerp(vOffset,vOffset - state.getLargeFontVerticalOffsetAdjustment(),progress);
      }
    }
    if (state.offsetAlignmentMode == OFFSET_ALIGNMENT_MODE_EDGE) {
      vOffset-=Math.round(halfBadgeHeight);
    }
    return vOffset + state.getAdditionalVerticalOffset();
  }
  private int getTotalHorizontalOffsetForState(){
    int hOffset=hasBadgeContent() ? state.getHorizontalOffsetWithText() : state.getHorizontalOffsetWithoutText();
    if (state.offsetAlignmentMode == OFFSET_ALIGNMENT_MODE_LEGACY) {
      hOffset+=hasBadgeContent() ? state.horizontalInsetWithText : state.horizontalInset;
    }
    return hOffset + state.getAdditionalHorizontalOffset();
  }
  private void calculateCenterAndBounds(  Rect anchorRect,  View anchorView){
    cornerRadius=hasBadgeContent() ? state.badgeWithTextRadius : state.badgeRadius;
    if (cornerRadius != BADGE_RADIUS_NOT_SPECIFIED) {
      halfBadgeWidth=cornerRadius;
      halfBadgeHeight=cornerRadius;
    }
 else {
      halfBadgeWidth=Math.round(hasBadgeContent() ? state.badgeWithTextWidth / 2 : state.badgeWidth / 2);
      halfBadgeHeight=Math.round(hasBadgeContent() ? state.badgeWithTextHeight / 2 : state.badgeHeight / 2);
    }
    if (hasBadgeContent()) {
      String badgeContent=getBadgeContent();
      halfBadgeWidth=Math.max(halfBadgeWidth,textDrawableHelper.getTextWidth(badgeContent) / 2f + state.getBadgeHorizontalPadding());
      halfBadgeHeight=Math.max(halfBadgeHeight,textDrawableHelper.getTextHeight(badgeContent) / 2f + state.getBadgeVerticalPadding());
      halfBadgeWidth=Math.max(halfBadgeWidth,halfBadgeHeight);
    }
    int totalVerticalOffset=getTotalVerticalOffsetForState();
switch (state.getBadgeGravity()) {
case BOTTOM_END:
case BOTTOM_START:
      badgeCenterY=anchorRect.bottom - totalVerticalOffset;
    break;
case TOP_END:
case TOP_START:
default :
  badgeCenterY=anchorRect.top + totalVerticalOffset;
break;
}
int totalHorizontalOffset=getTotalHorizontalOffsetForState();
switch (state.getBadgeGravity()) {
case BOTTOM_START:
case TOP_START:
badgeCenterX=state.badgeFixedEdge == BADGE_FIXED_EDGE_START ? (anchorView.getLayoutDirection() == View.LAYOUT_DIRECTION_LTR ? anchorRect.left + halfBadgeWidth - (halfBadgeHeight * 2 - totalHorizontalOffset) : anchorRect.right - halfBadgeWidth + (halfBadgeHeight * 2 - totalHorizontalOffset)) : (anchorView.getLayoutDirection() == View.LAYOUT_DIRECTION_LTR ? anchorRect.left - halfBadgeWidth + totalHorizontalOffset : anchorRect.right + halfBadgeWidth - totalHorizontalOffset);
break;
case BOTTOM_END:
case TOP_END:
default :
badgeCenterX=state.badgeFixedEdge == BADGE_FIXED_EDGE_START ? (anchorView.getLayoutDirection() == View.LAYOUT_DIRECTION_LTR ? anchorRect.right + halfBadgeWidth - totalHorizontalOffset : anchorRect.left - halfBadgeWidth + totalHorizontalOffset) : (anchorView.getLayoutDirection() == View.LAYOUT_DIRECTION_LTR ? anchorRect.right - halfBadgeWidth + (halfBadgeHeight * 2 - totalHorizontalOffset) : anchorRect.left + halfBadgeWidth - (halfBadgeHeight * 2 - totalHorizontalOffset));
break;
}
if (state.isAutoAdjustedToGrandparentBounds()) {
//autoAdjustWithinGrandparentBounds(anchorView);
}
 else {
//autoAdjustWithinViewBounds(anchorView,null);
}
}
private void autoAdjustWithinViewBounds(View anchorView,View ancestorView){
float totalAnchorYOffset;
float totalAnchorXOffset;
ViewParent anchorParent;
ViewParent customAnchorParent=getCustomBadgeParent();
if (customAnchorParent == null) {
totalAnchorYOffset=anchorView.getY();
totalAnchorXOffset=anchorView.getX();
anchorParent=anchorView.getParent();
}
 else {
totalAnchorYOffset=0;
totalAnchorXOffset=0;
anchorParent=customAnchorParent;
}
ViewParent currentViewParent=anchorParent;
while (currentViewParent instanceof View && currentViewParent != ancestorView) {
ViewParent viewGrandparent=currentViewParent.getParent();
if (!(viewGrandparent instanceof ViewGroup) || false/*((ViewGroup)viewGrandparent).getClipChildren()*/) {
break;
}
View currentViewGroup=(View)currentViewParent;
totalAnchorYOffset+=currentViewGroup.getY();
totalAnchorXOffset+=currentViewGroup.getX();
currentViewParent=currentViewParent.getParent();
}
if (!(currentViewParent instanceof View)) {
return;
}
float topCutOff=getTopCutOff(totalAnchorYOffset);
float leftCutOff=getLeftCutOff(totalAnchorXOffset);
float bottomCutOff=getBottomCutOff(((View)currentViewParent).getHeight(),totalAnchorYOffset);
float rightCutOff=getRightCutoff(((View)currentViewParent).getWidth(),totalAnchorXOffset);
if (topCutOff < 0) {
badgeCenterY+=Math.abs(topCutOff);
}
if (leftCutOff < 0) {
badgeCenterX+=Math.abs(leftCutOff);
}
if (bottomCutOff > 0) {
badgeCenterY-=Math.abs(bottomCutOff);
}
if (rightCutOff > 0) {
badgeCenterX-=Math.abs(rightCutOff);
}
}
private void autoAdjustWithinGrandparentBounds(View anchorView){
ViewParent customAnchor=getCustomBadgeParent();
ViewParent anchorParent=null;
if (customAnchor == null) {
anchorParent=anchorView.getParent();
}
 else {
anchorParent=customAnchor;
}
if (anchorParent instanceof View && anchorParent.getParent() instanceof View) {
//autoAdjustWithinViewBounds(anchorView,(View)anchorParent.getParent());
}
}
private float getTopCutOff(float totalAnchorYOffset){
return badgeCenterY - halfBadgeHeight + totalAnchorYOffset;
}
private float getLeftCutOff(float totalAnchorXOffset){
return badgeCenterX - halfBadgeWidth + totalAnchorXOffset;
}
private float getBottomCutOff(float ancestorHeight,float totalAnchorYOffset){
return badgeCenterY + halfBadgeHeight - ancestorHeight + totalAnchorYOffset;
}
private float getRightCutoff(float ancestorWidth,float totalAnchorXOffset){
return badgeCenterX + halfBadgeWidth - ancestorWidth + totalAnchorXOffset;
}
private boolean hasBadgeContent(){
return hasText() || hasNumber();
}
private String getBadgeContent(){
if (hasText()) {
return getTextBadgeText();
}
 else if (hasNumber()) {
return getNumberBadgeText();
}
 else {
return null;
}
}
private String getTextBadgeText(){
String text=getText();
final int maxCharacterCount=getMaxCharacterCount();
if (maxCharacterCount == BADGE_CONTENT_NOT_TRUNCATED) {
return text;
}
if (text != null && text.length() > maxCharacterCount) {
Context context=contextRef.get();
if (context == null) {
return "";
}
text=text.substring(0,maxCharacterCount - 1);
return String.format("%s%s",text,DEFAULT_EXCEED_MAX_BADGE_TEXT_SUFFIX);
}
 else {
return text;
}
}
private String getNumberBadgeText(){
if (maxBadgeNumber == BADGE_CONTENT_NOT_TRUNCATED || getNumber() <= maxBadgeNumber) {
return NumberFormat.getInstance(state.getNumberLocale()).format(getNumber());
}
 else {
Context context=contextRef.get();
if (context == null) {
return "";
}
return String.format(state.getNumberLocale(),"%s%s",maxBadgeNumber,DEFAULT_EXCEED_MAX_BADGE_NUMBER_SUFFIX);
}
}
private void onBadgeContentUpdated(){
textDrawableHelper.setTextSizeDirty(true);
onBadgeShapeAppearanceUpdated();
updateCenterAndBounds();
invalidateSelf();
}
private void updateMaxBadgeNumber(){
if (getMaxCharacterCount() != BADGE_CONTENT_NOT_TRUNCATED) {
maxBadgeNumber=(int)Math.pow(10.0d,(double)getMaxCharacterCount() - 1) - 1;
}
 else {
maxBadgeNumber=getMaxNumber();
}
}
class MaterialShapeDrawable extends Drawable {
private float cornerSize;
public float getCornerSize(){
return cornerSize;
}
private ColorStateList backgroundColorStateList;
public void setCornerSize(float cornerSize){
this.cornerSize=cornerSize;
}
public void setFillColor(ColorStateList backgroundColorStateList){
this.backgroundColorStateList=backgroundColorStateList;
}
public ColorStateList getFillColor(){
return backgroundColorStateList;
}
}
public static BadgeDrawable create(Context context){
return new BadgeDrawable(context,null);
}
public BadgeDrawable(Context context,BadgeState.State savedState){
this.contextRef=new WeakReference<>(context);
badgeBounds=new Rect();
textDrawableHelper=new TextDrawableHelper();
this.state=new BadgeState(context,savedState);
shapeDrawable=new MaterialShapeDrawable();
maxBadgeNumber=9999;
}
class TextPaint {
public int getColor(){
return 0;
}
public void setAlpha(int alpha){
}
public void setColor(int badgeTextColor){
}
}
class TextDrawableHelper {
public TextPaint getTextPaint(){
return new TextPaint();
}
public float getTextWidth(String badgeContent){
if (getMeasureTextHelper() != null) {
return getMeasureTextHelper().getTextWidth();
}
return 0;
}
public float getTextHeight(String badgeContent){
if (getMeasureTextHelper() != null) {
return getMeasureTextHelper().getTextHeight();
}
return 0;
}
public void setTextSizeDirty(boolean b){
}
}
@Override public int getMinimumHeight(){
return badgeBounds.height();
}
@Override public int getMinimumWidth(){
return badgeBounds.width();
}
public Object getAttribute(String key){
switch (key) {
case "text":
return getBadgeContent();
case "textColor":
return colorToNativeColor(state.getBadgeTextColor());
case "borderRadius":
return shapeDrawable.getCornerSize();
case "background":
Drawable bgdrawable=new r.android.graphics.drawable.ColorDrawable();
bgdrawable.setDrawable(colorToNativeColor(state.getBackgroundColor()));
return bgdrawable;
case "textSize":
return "8sp";
case "zIndex":
return "1000";
case "gravity":
return "center";
case "typeface":
return "sans";
case "paddingHorizontal":
return "1dp";
case "alpha":
return (float)state.getAlpha() / 255f;
case "visibility":
return state.isVisible() ? "visible" : "gone";
case "textAppearance":
return textAppearanceResource;
case "borderWidth":
return "0dp";
default :
break;
}
return super.getAttribute(key);
}
private Object colorToNativeColor(int color){
return com.ashera.widget.PluginInvoker.getColor(r.android.graphics.Color.formatColor(color));
}
public r.android.graphics.Rect getBounds(){
updateBadgeCoordinates(anchorViewRef.get(),(FrameLayout)customBadgeParentRef.get());
return shapeDrawable.getBounds();
}
public java.lang.String[] getSimulatedWidgetAttrs(){
return new String[]{"zIndex","textSize","gravity","text","typeface","textColor","background","paddingHorizontal","alpha","visibility","borderRadius","textAppearance","borderWidth"};
}
public java.lang.String getSimulatedWidgetLocalName(){
return r.android.widget.TextView.class.getSimpleName();
}
public java.lang.String getSimulatedWidgetGroupName(){
return r.android.widget.TextView.class.getSimpleName();
}
private String textAppearanceResource;
public String getTextAppearanceResource(){
return textAppearanceResource;
}
public void setTextAppearanceResource(String textAppearanceResource){
this.textAppearanceResource=textAppearanceResource;
}
}
