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
 * Copyright (C) 2019 The Android Open Source Project
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
  public static void detachBadgeDrawable(  BadgeDrawable badgeDrawable,  View anchor){
    if (badgeDrawable == null) {
      return;
    }
    if (badgeDrawable.getCustomBadgeParent() != null) {
      badgeDrawable.getCustomBadgeParent().setForeground(null);
    }
 else {
      anchor.getOverlay().remove(badgeDrawable);
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
