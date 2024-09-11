package com.google.android.material.badge;
import java.util.Locale;
public final class BadgeState {
  private final State overridingState;
  private final State currentState=new State();
  final float badgeRadius;
  final float badgeWithTextRadius;
  final float badgeWidth;
  final float badgeHeight;
  final float badgeWithTextWidth;
  final float badgeWithTextHeight;
  final int horizontalInset;
  final int horizontalInsetWithText;
  int offsetAlignmentMode;
  int badgeFixedEdge;
  State getOverridingState(){
    return overridingState;
  }
  boolean isVisible(){
    return currentState.isVisible;
  }
  void setVisible(  boolean visible){
    overridingState.isVisible=visible;
    currentState.isVisible=visible;
  }
  boolean hasNumber(){
    return currentState.number != State.BADGE_NUMBER_NONE;
  }
  int getNumber(){
    return currentState.number;
  }
  void setNumber(  int number){
    overridingState.number=number;
    currentState.number=number;
  }
  void clearNumber(){
    setNumber(State.BADGE_NUMBER_NONE);
  }
  boolean hasText(){
    return currentState.text != null;
  }
  String getText(){
    return currentState.text;
  }
  void setText(  String text){
    overridingState.text=text;
    currentState.text=text;
  }
  void clearText(){
    setText(null);
  }
  int getAlpha(){
    return currentState.alpha;
  }
  void setAlpha(  int alpha){
    overridingState.alpha=alpha;
    currentState.alpha=alpha;
  }
  int getMaxCharacterCount(){
    return currentState.maxCharacterCount;
  }
  void setMaxCharacterCount(  int maxCharacterCount){
    overridingState.maxCharacterCount=maxCharacterCount;
    currentState.maxCharacterCount=maxCharacterCount;
  }
  int getMaxNumber(){
    return currentState.maxNumber;
  }
  void setMaxNumber(  int maxNumber){
    overridingState.maxNumber=maxNumber;
    currentState.maxNumber=maxNumber;
  }
  int getBackgroundColor(){
    return currentState.backgroundColor;
  }
  void setBackgroundColor(  int backgroundColor){
    overridingState.backgroundColor=backgroundColor;
    currentState.backgroundColor=backgroundColor;
  }
  int getBadgeTextColor(){
    return currentState.badgeTextColor;
  }
  void setBadgeTextColor(  int badgeTextColor){
    overridingState.badgeTextColor=badgeTextColor;
    currentState.badgeTextColor=badgeTextColor;
  }
  int getBadgeGravity(){
    return currentState.badgeGravity;
  }
  void setBadgeGravity(  int badgeGravity){
    overridingState.badgeGravity=badgeGravity;
    currentState.badgeGravity=badgeGravity;
  }
  int getBadgeHorizontalPadding(){
    return currentState.badgeHorizontalPadding;
  }
  void setBadgeHorizontalPadding(  int horizontalPadding){
    overridingState.badgeHorizontalPadding=horizontalPadding;
    currentState.badgeHorizontalPadding=horizontalPadding;
  }
  int getBadgeVerticalPadding(){
    return currentState.badgeVerticalPadding;
  }
  void setBadgeVerticalPadding(  int verticalPadding){
    overridingState.badgeVerticalPadding=verticalPadding;
    currentState.badgeVerticalPadding=verticalPadding;
  }
  int getHorizontalOffsetWithoutText(){
    return currentState.horizontalOffsetWithoutText;
  }
  void setHorizontalOffsetWithoutText(  int offset){
    overridingState.horizontalOffsetWithoutText=offset;
    currentState.horizontalOffsetWithoutText=offset;
  }
  int getVerticalOffsetWithoutText(){
    return currentState.verticalOffsetWithoutText;
  }
  void setVerticalOffsetWithoutText(  int offset){
    overridingState.verticalOffsetWithoutText=offset;
    currentState.verticalOffsetWithoutText=offset;
  }
  int getHorizontalOffsetWithText(){
    return currentState.horizontalOffsetWithText;
  }
  void setHorizontalOffsetWithText(  int offset){
    overridingState.horizontalOffsetWithText=offset;
    currentState.horizontalOffsetWithText=offset;
  }
  int getVerticalOffsetWithText(){
    return currentState.verticalOffsetWithText;
  }
  void setVerticalOffsetWithText(  int offset){
    overridingState.verticalOffsetWithText=offset;
    currentState.verticalOffsetWithText=offset;
  }
  int getLargeFontVerticalOffsetAdjustment(){
    return currentState.largeFontVerticalOffsetAdjustment;
  }
  void setLargeFontVerticalOffsetAdjustment(  int offsetAdjustment){
    overridingState.largeFontVerticalOffsetAdjustment=offsetAdjustment;
    currentState.largeFontVerticalOffsetAdjustment=offsetAdjustment;
  }
  int getAdditionalHorizontalOffset(){
    return currentState.additionalHorizontalOffset;
  }
  void setAdditionalHorizontalOffset(  int offset){
    overridingState.additionalHorizontalOffset=offset;
    currentState.additionalHorizontalOffset=offset;
  }
  int getAdditionalVerticalOffset(){
    return currentState.additionalVerticalOffset;
  }
  void setAdditionalVerticalOffset(  int offset){
    overridingState.additionalVerticalOffset=offset;
    currentState.additionalVerticalOffset=offset;
  }
  Locale getNumberLocale(){
    return currentState.numberLocale;
  }
  void setNumberLocale(  Locale locale){
    overridingState.numberLocale=locale;
    currentState.numberLocale=locale;
  }
  boolean isAutoAdjustedToGrandparentBounds(){
    return currentState.autoAdjustToWithinGrandparentBounds;
  }
  void setAutoAdjustToGrandparentBounds(  boolean autoAdjustToGrandparentBounds){
    overridingState.autoAdjustToWithinGrandparentBounds=autoAdjustToGrandparentBounds;
    currentState.autoAdjustToWithinGrandparentBounds=autoAdjustToGrandparentBounds;
  }
public static final class State {
    private static final int BADGE_NUMBER_NONE=-1;
    private static final int NOT_SET=-2;
    private int badgeResId;
    private Integer backgroundColor;
    private Integer badgeTextColor;
    private Integer badgeTextAppearanceResId;
    private Integer badgeShapeAppearanceResId;
    private Integer badgeShapeAppearanceOverlayResId;
    private Integer badgeWithTextShapeAppearanceResId;
    private Integer badgeWithTextShapeAppearanceOverlayResId;
    private int alpha=255;
    private String text;
    private int number=NOT_SET;
    private int maxCharacterCount=NOT_SET;
    private int maxNumber=NOT_SET;
    private Locale numberLocale;
    private int contentDescriptionQuantityStrings;
    private int contentDescriptionExceedsMaxBadgeNumberRes;
    private Integer badgeGravity;
    private Boolean isVisible=true;
    private Integer badgeHorizontalPadding;
    private Integer badgeVerticalPadding;
    private Integer horizontalOffsetWithoutText;
    private Integer verticalOffsetWithoutText;
    private Integer horizontalOffsetWithText;
    private Integer verticalOffsetWithText;
    private Integer additionalHorizontalOffset;
    private Integer additionalVerticalOffset;
    private Integer largeFontVerticalOffsetAdjustment;
    private Boolean autoAdjustToWithinGrandparentBounds;
    private Integer badgeFixedEdge;
  }
  public BadgeState(  r.android.content.Context context,  State storedState){
    if (storedState == null) {
      storedState=new State();
    }
    badgeRadius=com.ashera.widget.PluginInvoker.convertDpToPixel("4dp");
    horizontalInset=0;
    badgeWithTextRadius=com.ashera.widget.PluginInvoker.convertDpToPixel("10dp");
    badgeWidth=0;
    badgeHeight=0;
    badgeWithTextWidth=0;
    badgeWithTextHeight=0;
    horizontalInsetWithText=0;
    overridingState=storedState;
    this.currentState.numberLocale=Locale.getDefault();
    this.currentState.badgeHorizontalPadding=(int)com.ashera.widget.PluginInvoker.convertDpToPixel("4dp");
    this.currentState.badgeVerticalPadding=0;
    this.currentState.horizontalOffsetWithoutText=0;
    this.currentState.verticalOffsetWithoutText=0;
    this.currentState.horizontalOffsetWithText=0;
    this.currentState.verticalOffsetWithText=0;
    this.currentState.additionalHorizontalOffset=0;
    this.currentState.additionalVerticalOffset=0;
    this.currentState.largeFontVerticalOffsetAdjustment=0;
    this.currentState.autoAdjustToWithinGrandparentBounds=false;
    this.currentState.badgeGravity=BadgeDrawable.TOP_END;
    this.currentState.badgeTextColor=r.android.graphics.Color.WHITE;
    this.currentState.backgroundColor=r.android.graphics.Color.RED;
    this.currentState.maxCharacterCount=4;
    offsetAlignmentMode=1;
  }
}
