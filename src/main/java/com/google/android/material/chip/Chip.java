package com.google.android.material.chip;

import com.ashera.widget.HasWidgets;
import com.ashera.widget.IWidget;
import com.google.android.material.internal.MaterialCheckable;

import r.android.content.res.ColorStateList;
import r.android.graphics.drawable.Drawable;
import r.android.view.View;


public class Chip extends r.android.widget.FrameLayout implements MaterialCheckable<Chip> {
	private Drawable chipIcon;
	private Drawable checkedIcon;
	private ColorStateList chipIconTint;
	private float chipEndPadding;
	private float chipStartPadding;
	private IWidget textWidget;
	private IWidget checkboxWidget;
	private IWidget closeIconWidget;
	private IWidget iconWidget;
	private ColorStateList checkedIconTint;
	private Drawable closeIcon;
	private ColorStateList closeIconTint;
	private float chipCornerRadius;
	private float closeIconEndPadding;
	private float closeIconStartPadding;
	private float closeIconSize;
	private float textEndPadding;
	private float textStartPadding;
	private float minHeight;
	private HasWidgets parentLL;
	private r.android.widget.CompoundButton.OnCheckedChangeListener onCheckedChangeListener;
	private boolean checkedIconVisible = true;
	private boolean checkable;
	private boolean checked;
	private boolean shouldEnsureMinTouchTargetSize;
	private OnCheckedChangeListener<Chip> internalOnCheckedChangeListener;
	private ColorStateList chipBackgroundColor;
	private ColorStateList chipStrokeColor;
	private float chipStrokeWidth;
	private float iconEndPadding;
	private float iconStartPadding;
	private View.OnClickListener rippleInternalClickListener;
	private View.OnClickListener chipClickListener;

	@com.google.j2objc.annotations.WeakOuter
	private final class ChipClickListener implements View.OnClickListener {
		private final IWidget textWidget;

		private ChipClickListener(IWidget textWidget) {
			this.textWidget = textWidget;
		}

		@Override
		public void onClick(View v) {
			toggle();
			if (rippleInternalClickListener != null) {
				rippleInternalClickListener.onClick(v);
			}
			if (chipClickListener != null) {
				chipClickListener.onClick(v);
			}
			if (internalOnCheckedChangeListener != null) {
				internalOnCheckedChangeListener.onCheckedChanged(Chip.this, isChecked());
			}
			if (internalOnCheckedChangeListener != null) {
				internalOnCheckedChangeListener.onCheckedChanged(Chip.this, isChecked());
			}
			requestLayout();
			textWidget.getFragment().remeasure();
		}
	}
	
	@Override
	public void setInternalOnCheckedChangeListener(OnCheckedChangeListener<Chip> listener) {
		this.internalOnCheckedChangeListener = listener;
	}

	@Override
	public void setChecked(boolean checked) {
		if (checkable) {
			this.checked = checked;
			checkboxWidget.setVisible(checked);
		}
		
		requestLayout();
	}

	@Override
	public boolean isChecked() {
		return checked;
	}

	@Override
	public void toggle() {
		if (checkable && checkedIconVisible) {
			setChecked(!isChecked());
		}
	}

	
	public void initView(HasWidgets parentLL, IWidget textWidget, IWidget iconWidget, IWidget checkboxWidget, IWidget closeIconWidget) {
		this.textWidget = textWidget;
		this.checkboxWidget = checkboxWidget;
		this.closeIconWidget = closeIconWidget;
		this.iconWidget = iconWidget;
		this.parentLL = parentLL;
		
		View.OnClickListener clickListener = new ChipClickListener(textWidget);
        if (com.ashera.widget.PluginInvoker.getOS().equalsIgnoreCase("swt") ) {
			this.textWidget.setAttribute("onClick", clickListener, true);
			this.checkboxWidget.setAttribute("onClick", clickListener, true);
			this.iconWidget.setAttribute("onClick", clickListener, true);
			
        }
        this.parentLL.setAttribute("onClick", clickListener, true);
	}

	public void setText(String text) {
		textWidget.setAttribute("text", text, true);
	}

	public void setCheckable(boolean checkable) {
		this.checkable = checkable;
		checkboxWidget.setVisible(false);
	}

	public void setChipEndPadding(float padding) {
		this.chipEndPadding = padding;
		parentLL.setAttribute("paddingEnd", (int) padding, true);
	}

	public void setChipIconSize(float iconSize) {
		iconWidget.setAttribute("layout_width", (int) iconSize, true);
        iconWidget.setAttribute("layout_height", (int) iconSize, true);
	}
	
	public void setChipIcon(Drawable drawable) {
		this.chipIcon = drawable;
		iconWidget.setAttribute("src", drawable, true);
		iconWidget.setVisible(drawable != null);
	}

	public void setChipIconTint(ColorStateList tint) {
		this.chipIconTint = tint;
		iconWidget.setAttribute("tint", tint, true);
	}

	public void setChipIconVisible(boolean visible) {
		iconWidget.setVisible(visible);
	}

	public void setChipStartPadding(float padding) {
		this.chipStartPadding = padding;
		parentLL.setAttribute("paddingStart", (int) padding, true);
		
	}

	public float getChipEndPadding() {
		return chipEndPadding;
	}

	public Drawable getChipIcon() {
		return chipIcon;
	}

	public ColorStateList getChipIconTint() {
		return chipIconTint;
	}

	public float getChipStartPadding() {
		return chipStartPadding;
	}

	public void setCheckedIcon(Drawable drawable) {
		this.checkedIcon = drawable;
		checkboxWidget.setAttribute("src", drawable, true);
		checkboxWidget.setVisible(drawable != null);
	}

	public void setCheckedIconTint(ColorStateList tint) {
		this.checkedIconTint = tint;
		checkboxWidget.setAttribute("tint", tint, true);
	}

	public void setCheckedIconVisible(boolean isVisible) {
		this.checkedIconVisible = isVisible;
		if (!isVisible) {
			checkboxWidget.setVisible(false);
		} else {
			checkboxWidget.setVisible(checked);
		}
	}

	public void setChipCornerRadius(float radius) {
		this.chipCornerRadius = radius;
		parentLL.setAttribute("cornerRadius", (int) radius, true);
	}

	public void setChipMinHeight(float minHeight) {
		this.minHeight = minHeight;
		setMyAttribute("minHeight", (int) minHeight);
	}

	public void setCloseIcon(Drawable drawable) {
		this.closeIcon = drawable;
		closeIconWidget.setAttribute("src", drawable, true);
		closeIconWidget.setVisible(drawable != null);
	}

	public void setCloseIconEndPadding(float padding) {
		this.closeIconEndPadding = padding;
		closeIconWidget.setAttribute("layout_marginEnd", (int) padding, true);		
	}

	public void setCloseIconSize(float iconSize) {
		this.closeIconSize = iconSize;
		closeIconWidget.setAttribute("layout_width", (int) iconSize, true);
		closeIconWidget.setAttribute("layout_height", (int) iconSize, true);
	}

	public void setCloseIconStartPadding(float padding) {
		this.closeIconStartPadding = padding;
		closeIconWidget.setAttribute("layout_marginStart", (int) padding, true);
	}

	public void setCloseIconTint(ColorStateList tint) {
		this.closeIconTint = tint;
		closeIconWidget.setAttribute("tint", tint, true);
		
	}

	public void setCloseIconVisible(boolean isVisible) {
		closeIconWidget.setVisible(isVisible);
	}

	public void setTextEndPadding(float padding) {
		this.textEndPadding = padding;
		textWidget.setAttribute("paddingEnd", (int) padding, true);
	}

	public void setTextStartPadding(float padding) {
		this.textStartPadding = padding;
		textWidget.setAttribute("paddingStart", (int) padding, true);
	}

	public void setMaxWidth(int width) {
		setMyAttribute("maxWidth", width);
	}

	public Drawable getCheckedIcon() {
		return checkedIcon;
	}

	public ColorStateList getCheckedIconTint() {
		return checkedIconTint;
	}

	public float getChipCornerRadius() {
		return chipCornerRadius;
	}

	public Drawable getCloseIcon() {
		return closeIcon;
	}

	public float getCloseIconEndPadding() {
		return closeIconEndPadding;
	}

	public float getCloseIconSize() {
		return closeIconSize;
	}

	public float getCloseIconStartPadding() {
		return closeIconStartPadding;
	}

	public ColorStateList getCloseIconTint() {
		return closeIconTint;
	}

	public float getTextEndPadding() {
		return this.textEndPadding;
	}

	public float getTextStartPadding() {
		return this.textStartPadding;
	}

	public float getChipMinHeight() {
		return this.minHeight;
	}

	public void setOnCloseIconClickListener(OnClickListener clickListener) {
		this.closeIconWidget.setAttribute("onClick", clickListener, true);		
	}

	public void setOnCheckedChangeListener(r.android.widget.CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
		this.onCheckedChangeListener = onCheckedChangeListener;
	}

	public void setEllipsize(String strValue) {
		textWidget.setAttribute("ellipsize", strValue, false);
	}

	@Override
	public void requestLayout() {
		super.requestLayout();
		if (parentLL != null) {
			parentLL.requestLayout();
		}
	}

	public void setEnsureMinTouchTargetSize(boolean shouldEnsureMinTouchTargetSize) {
		this.shouldEnsureMinTouchTargetSize = shouldEnsureMinTouchTargetSize;
		if (shouldEnsureMinTouchTargetSize) {
			parentLL.setAttribute("layout_marginVertical", "16dp", false);
		} else {
			parentLL.setAttribute("layout_marginVertical", "0dp", false);
		}
		
	}

	public boolean shouldEnsureMinTouchTargetSize() {
		return shouldEnsureMinTouchTargetSize;
	}

	public void setTextAppearance(String strValue) {
		textWidget.setAttribute("textAppearance", strValue, false);
	}

	public void setChipBackgroundColor(ColorStateList chipBackgroundColor) {
		this.chipBackgroundColor = chipBackgroundColor;
		parentLL.setAttribute("background", chipBackgroundColor, true);
		
	}

	public void setChipStrokeColor(ColorStateList chipStrokeColor) {
		this.chipStrokeColor = chipStrokeColor;
		parentLL.setAttribute("cornerRadius", 0, true);
		parentLL.setAttribute("borderRadius", chipCornerRadius, true);
		parentLL.setAttribute("borderColor", chipStrokeColor, true);
		parentLL.setAttribute("borderWidth", "1dp", false);
	}

	public void setChipStrokeWidth(float chipStrokeWidth) {
		this.chipStrokeWidth = chipStrokeWidth;
		parentLL.setAttribute("cornerRadius", 0, true);
		parentLL.setAttribute("borderRadius", chipCornerRadius, true);
		parentLL.setAttribute("borderWidth", (int) chipStrokeWidth, true);
	}

	public void setIconEndPadding(float padding) {
		this.iconEndPadding = padding;
		iconWidget.setAttribute("layout_marginEnd", (int) padding, true);
	}

	public void setIconStartPadding(float padding) {
		this.iconStartPadding = padding;
		iconWidget.setAttribute("layout_marginStart", (int) padding, true);		
	}

	public ColorStateList getChipBackgroundColor() {
		return chipBackgroundColor;
	}

	public ColorStateList getChipStrokeColor() {
		return chipStrokeColor;
	}

	public float getChipStrokeWidth() {
		return chipStrokeWidth;
	}

	public float getIconEndPadding() {
		return iconEndPadding;
	}

	public float getIconStartPadding() {
		return iconStartPadding;
	}
	
	
	public View.OnClickListener getRippleInternalClickListener() {
		return rippleInternalClickListener;
	}

	public void setRippleInternalClickListener(View.OnClickListener rippleInternalClickListener) {
		this.rippleInternalClickListener = rippleInternalClickListener;
	}
	
	public void setChipClickListener(View.OnClickListener chipClickListener) {
		this.chipClickListener = chipClickListener;
	}
}
