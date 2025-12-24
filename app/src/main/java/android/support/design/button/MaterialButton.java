package android.support.design.button;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.C0029R;
import android.support.design.internal.ThemeEnforcement;
import android.support.design.internal.ViewUtils;
import android.support.design.resources.MaterialResources;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.util.Log;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes-dex2jar.jar:android/support/design/button/MaterialButton.class */
public class MaterialButton extends AppCompatButton {
    public static final int ICON_GRAVITY_START = 1;
    public static final int ICON_GRAVITY_TEXT_START = 2;
    private static final String LOG_TAG = "MaterialButton";
    private Drawable icon;
    private int iconGravity;
    private int iconLeft;
    private int iconPadding;
    private int iconSize;
    private ColorStateList iconTint;
    private PorterDuff.Mode iconTintMode;
    private final MaterialButtonHelper materialButtonHelper;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes-dex2jar.jar:android/support/design/button/MaterialButton$IconGravity.class */
    public @interface IconGravity {
    }

    public MaterialButton(Context context) {
        this(context, null);
    }

    public MaterialButton(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, C0029R.attr.materialButtonStyle);
    }

    public MaterialButton(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        TypedArray typedArrayObtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(context, attributeSet, C0029R.styleable.MaterialButton, i, C0029R.style.Widget_MaterialComponents_Button, new int[0]);
        this.iconPadding = typedArrayObtainStyledAttributes.getDimensionPixelSize(C0029R.styleable.MaterialButton_iconPadding, 0);
        this.iconTintMode = ViewUtils.parseTintMode(typedArrayObtainStyledAttributes.getInt(C0029R.styleable.MaterialButton_iconTintMode, -1), PorterDuff.Mode.SRC_IN);
        this.iconTint = MaterialResources.getColorStateList(getContext(), typedArrayObtainStyledAttributes, C0029R.styleable.MaterialButton_iconTint);
        this.icon = MaterialResources.getDrawable(getContext(), typedArrayObtainStyledAttributes, C0029R.styleable.MaterialButton_icon);
        this.iconGravity = typedArrayObtainStyledAttributes.getInteger(C0029R.styleable.MaterialButton_iconGravity, 1);
        this.iconSize = typedArrayObtainStyledAttributes.getDimensionPixelSize(C0029R.styleable.MaterialButton_iconSize, 0);
        MaterialButtonHelper materialButtonHelper = new MaterialButtonHelper(this);
        this.materialButtonHelper = materialButtonHelper;
        materialButtonHelper.loadFromAttributes(typedArrayObtainStyledAttributes);
        typedArrayObtainStyledAttributes.recycle();
        setCompoundDrawablePadding(this.iconPadding);
        updateIcon();
    }

    private boolean isLayoutRTL() {
        boolean z = true;
        if (ViewCompat.getLayoutDirection(this) != 1) {
            z = false;
        }
        return z;
    }

    private boolean isUsingOriginalBackground() {
        MaterialButtonHelper materialButtonHelper = this.materialButtonHelper;
        return (materialButtonHelper == null || materialButtonHelper.isBackgroundOverwritten()) ? false : true;
    }

    private void updateIcon() {
        Drawable drawable = this.icon;
        if (drawable != null) {
            Drawable drawableMutate = drawable.mutate();
            this.icon = drawableMutate;
            DrawableCompat.setTintList(drawableMutate, this.iconTint);
            PorterDuff.Mode mode = this.iconTintMode;
            if (mode != null) {
                DrawableCompat.setTintMode(this.icon, mode);
            }
            int intrinsicWidth = this.iconSize;
            if (intrinsicWidth == 0) {
                intrinsicWidth = this.icon.getIntrinsicWidth();
            }
            int intrinsicHeight = this.iconSize;
            if (intrinsicHeight == 0) {
                intrinsicHeight = this.icon.getIntrinsicHeight();
            }
            Drawable drawable2 = this.icon;
            int i = this.iconLeft;
            drawable2.setBounds(i, 0, intrinsicWidth + i, intrinsicHeight);
        }
        TextViewCompat.setCompoundDrawablesRelative(this, this.icon, null, null, null);
    }

    @Override // android.view.View
    public ColorStateList getBackgroundTintList() {
        return getSupportBackgroundTintList();
    }

    @Override // android.view.View
    public PorterDuff.Mode getBackgroundTintMode() {
        return getSupportBackgroundTintMode();
    }

    public int getCornerRadius() {
        return isUsingOriginalBackground() ? this.materialButtonHelper.getCornerRadius() : 0;
    }

    public Drawable getIcon() {
        return this.icon;
    }

    public int getIconGravity() {
        return this.iconGravity;
    }

    public int getIconPadding() {
        return this.iconPadding;
    }

    public int getIconSize() {
        return this.iconSize;
    }

    public ColorStateList getIconTint() {
        return this.iconTint;
    }

    public PorterDuff.Mode getIconTintMode() {
        return this.iconTintMode;
    }

    public ColorStateList getRippleColor() {
        return isUsingOriginalBackground() ? this.materialButtonHelper.getRippleColor() : null;
    }

    public ColorStateList getStrokeColor() {
        return isUsingOriginalBackground() ? this.materialButtonHelper.getStrokeColor() : null;
    }

    public int getStrokeWidth() {
        return isUsingOriginalBackground() ? this.materialButtonHelper.getStrokeWidth() : 0;
    }

    @Override // android.support.v7.widget.AppCompatButton, android.support.v4.view.TintableBackgroundView
    public ColorStateList getSupportBackgroundTintList() {
        return isUsingOriginalBackground() ? this.materialButtonHelper.getSupportBackgroundTintList() : super.getSupportBackgroundTintList();
    }

    @Override // android.support.v7.widget.AppCompatButton, android.support.v4.view.TintableBackgroundView
    public PorterDuff.Mode getSupportBackgroundTintMode() {
        return isUsingOriginalBackground() ? this.materialButtonHelper.getSupportBackgroundTintMode() : super.getSupportBackgroundTintMode();
    }

    @Override // android.widget.TextView, android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (Build.VERSION.SDK_INT >= 21 || !isUsingOriginalBackground()) {
            return;
        }
        this.materialButtonHelper.drawStroke(canvas);
    }

    @Override // android.support.v7.widget.AppCompatButton, android.widget.TextView, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        MaterialButtonHelper materialButtonHelper;
        super.onLayout(z, i, i2, i3, i4);
        if (Build.VERSION.SDK_INT != 21 || (materialButtonHelper = this.materialButtonHelper) == null) {
            return;
        }
        materialButtonHelper.updateMaskBounds(i4 - i2, i3 - i);
    }

    @Override // android.widget.TextView, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        if (this.icon == null || this.iconGravity != 2) {
            return;
        }
        int iMeasureText = (int) getPaint().measureText(getText().toString());
        int i3 = this.iconSize;
        int intrinsicWidth = i3;
        if (i3 == 0) {
            intrinsicWidth = this.icon.getIntrinsicWidth();
        }
        int measuredWidth = (((((getMeasuredWidth() - iMeasureText) - ViewCompat.getPaddingEnd(this)) - intrinsicWidth) - this.iconPadding) - ViewCompat.getPaddingStart(this)) / 2;
        int i4 = measuredWidth;
        if (isLayoutRTL()) {
            i4 = -measuredWidth;
        }
        if (this.iconLeft != i4) {
            this.iconLeft = i4;
            updateIcon();
        }
    }

    @Override // android.view.View
    public void setBackground(Drawable drawable) {
        setBackgroundDrawable(drawable);
    }

    @Override // android.view.View
    public void setBackgroundColor(int i) {
        if (isUsingOriginalBackground()) {
            this.materialButtonHelper.setBackgroundColor(i);
        } else {
            super.setBackgroundColor(i);
        }
    }

    @Override // android.support.v7.widget.AppCompatButton, android.view.View
    public void setBackgroundDrawable(Drawable drawable) {
        if (!isUsingOriginalBackground()) {
            super.setBackgroundDrawable(drawable);
        } else {
            if (drawable == getBackground()) {
                getBackground().setState(drawable.getState());
                return;
            }
            Log.i(LOG_TAG, "Setting a custom background is not supported.");
            this.materialButtonHelper.setBackgroundOverwritten();
            super.setBackgroundDrawable(drawable);
        }
    }

    @Override // android.support.v7.widget.AppCompatButton, android.view.View
    public void setBackgroundResource(int i) {
        setBackgroundDrawable(i != 0 ? AppCompatResources.getDrawable(getContext(), i) : null);
    }

    @Override // android.view.View
    public void setBackgroundTintList(ColorStateList colorStateList) {
        setSupportBackgroundTintList(colorStateList);
    }

    @Override // android.view.View
    public void setBackgroundTintMode(PorterDuff.Mode mode) {
        setSupportBackgroundTintMode(mode);
    }

    public void setCornerRadius(int i) {
        if (isUsingOriginalBackground()) {
            this.materialButtonHelper.setCornerRadius(i);
        }
    }

    public void setCornerRadiusResource(int i) {
        if (isUsingOriginalBackground()) {
            setCornerRadius(getResources().getDimensionPixelSize(i));
        }
    }

    public void setIcon(Drawable drawable) {
        if (this.icon != drawable) {
            this.icon = drawable;
            updateIcon();
        }
    }

    public void setIconGravity(int i) {
        this.iconGravity = i;
    }

    public void setIconPadding(int i) {
        if (this.iconPadding != i) {
            this.iconPadding = i;
            setCompoundDrawablePadding(i);
        }
    }

    public void setIconResource(int i) {
        setIcon(i != 0 ? AppCompatResources.getDrawable(getContext(), i) : null);
    }

    public void setIconSize(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("iconSize cannot be less than 0");
        }
        if (this.iconSize != i) {
            this.iconSize = i;
            updateIcon();
        }
    }

    public void setIconTint(ColorStateList colorStateList) {
        if (this.iconTint != colorStateList) {
            this.iconTint = colorStateList;
            updateIcon();
        }
    }

    public void setIconTintMode(PorterDuff.Mode mode) {
        if (this.iconTintMode != mode) {
            this.iconTintMode = mode;
            updateIcon();
        }
    }

    public void setIconTintResource(int i) {
        setIconTint(AppCompatResources.getColorStateList(getContext(), i));
    }

    void setInternalBackground(Drawable drawable) {
        super.setBackgroundDrawable(drawable);
    }

    public void setRippleColor(ColorStateList colorStateList) {
        if (isUsingOriginalBackground()) {
            this.materialButtonHelper.setRippleColor(colorStateList);
        }
    }

    public void setRippleColorResource(int i) {
        if (isUsingOriginalBackground()) {
            setRippleColor(AppCompatResources.getColorStateList(getContext(), i));
        }
    }

    public void setStrokeColor(ColorStateList colorStateList) {
        if (isUsingOriginalBackground()) {
            this.materialButtonHelper.setStrokeColor(colorStateList);
        }
    }

    public void setStrokeColorResource(int i) {
        if (isUsingOriginalBackground()) {
            setStrokeColor(AppCompatResources.getColorStateList(getContext(), i));
        }
    }

    public void setStrokeWidth(int i) {
        if (isUsingOriginalBackground()) {
            this.materialButtonHelper.setStrokeWidth(i);
        }
    }

    public void setStrokeWidthResource(int i) {
        if (isUsingOriginalBackground()) {
            setStrokeWidth(getResources().getDimensionPixelSize(i));
        }
    }

    @Override // android.support.v7.widget.AppCompatButton, android.support.v4.view.TintableBackgroundView
    public void setSupportBackgroundTintList(ColorStateList colorStateList) {
        if (isUsingOriginalBackground()) {
            this.materialButtonHelper.setSupportBackgroundTintList(colorStateList);
        } else if (this.materialButtonHelper != null) {
            super.setSupportBackgroundTintList(colorStateList);
        }
    }

    @Override // android.support.v7.widget.AppCompatButton, android.support.v4.view.TintableBackgroundView
    public void setSupportBackgroundTintMode(PorterDuff.Mode mode) {
        if (isUsingOriginalBackground()) {
            this.materialButtonHelper.setSupportBackgroundTintMode(mode);
        } else if (this.materialButtonHelper != null) {
            super.setSupportBackgroundTintMode(mode);
        }
    }
}
