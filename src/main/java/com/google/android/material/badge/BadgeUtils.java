package com.google.android.material.badge;
import r.android.graphics.Rect;
import r.android.view.View;
import r.android.widget.FrameLayout;
public class BadgeUtils {
  private static final String LOG_TAG="BadgeUtils";
  public static void updateBadgeBounds(  Rect rect,  float centerX,  float centerY,  float halfWidth,  float halfHeight){
    rect.set((int)(centerX - halfWidth),(int)(centerY - halfHeight),(int)(centerX + halfWidth),(int)(centerY + halfHeight));
  }
  public static void attachBadgeDrawable(  BadgeDrawable badgeDrawable,  View anchor,  FrameLayout customBadgeParent){
    setBadgeDrawableBounds(badgeDrawable,anchor,customBadgeParent);
    if (badgeDrawable.getCustomBadgeParent() != null) {
      badgeDrawable.getCustomBadgeParent().setForeground(badgeDrawable);
    }
 else {
      anchor.getOverlay().add(badgeDrawable);
    }
  }
  public static void setBadgeDrawableBounds(  BadgeDrawable badgeDrawable,  View anchor,  FrameLayout compatBadgeParent){
    Rect badgeBounds=new Rect();
    anchor.getDrawingRect(badgeBounds);
    badgeDrawable.setBounds(badgeBounds);
    badgeDrawable.updateBadgeCoordinates(anchor,compatBadgeParent);
  }
  public static final boolean USE_COMPAT_PARENT=false;
}
