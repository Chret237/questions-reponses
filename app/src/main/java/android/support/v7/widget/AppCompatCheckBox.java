package android.support.v7.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.TintableCompoundButton;
import android.support.v7.appcompat.C0287R;
import android.support.v7.content.res.AppCompatResources;
import android.util.AttributeSet;
import android.widget.CheckBox;

/* loaded from: classes-dex2jar.jar:android/support/v7/widget/AppCompatCheckBox.class */
public class AppCompatCheckBox extends CheckBox implements TintableCompoundButton {
    private final AppCompatCompoundButtonHelper mCompoundButtonHelper;

    public AppCompatCheckBox(Context context) {
        this(context, null);
    }

    public AppCompatCheckBox(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, C0287R.attr.checkboxStyle);
    }

    public AppCompatCheckBox(Context context, AttributeSet attributeSet, int i) {
        super(TintContextWrapper.wrap(context), attributeSet, i);
        AppCompatCompoundButtonHelper appCompatCompoundButtonHelper = new AppCompatCompoundButtonHelper(this);
        this.mCompoundButtonHelper = appCompatCompoundButtonHelper;
        appCompatCompoundButtonHelper.loadFromAttributes(attributeSet, i);
    }

    @Override // android.widget.CompoundButton, android.widget.TextView
    public int getCompoundPaddingLeft() throws NoSuchFieldException {
        int compoundPaddingLeft = super.getCompoundPaddingLeft();
        AppCompatCompoundButtonHelper appCompatCompoundButtonHelper = this.mCompoundButtonHelper;
        int compoundPaddingLeft2 = compoundPaddingLeft;
        if (appCompatCompoundButtonHelper != null) {
            compoundPaddingLeft2 = appCompatCompoundButtonHelper.getCompoundPaddingLeft(compoundPaddingLeft);
        }
        return compoundPaddingLeft2;
    }

    @Override // android.support.v4.widget.TintableCompoundButton
    public ColorStateList getSupportButtonTintList() {
        AppCompatCompoundButtonHelper appCompatCompoundButtonHelper = this.mCompoundButtonHelper;
        return appCompatCompoundButtonHelper != null ? appCompatCompoundButtonHelper.getSupportButtonTintList() : null;
    }

    @Override // android.support.v4.widget.TintableCompoundButton
    public PorterDuff.Mode getSupportButtonTintMode() {
        AppCompatCompoundButtonHelper appCompatCompoundButtonHelper = this.mCompoundButtonHelper;
        return appCompatCompoundButtonHelper != null ? appCompatCompoundButtonHelper.getSupportButtonTintMode() : null;
    }

    @Override // android.widget.CompoundButton
    public void setButtonDrawable(int i) throws NoSuchFieldException {
        setButtonDrawable(AppCompatResources.getDrawable(getContext(), i));
    }

    @Override // android.widget.CompoundButton
    public void setButtonDrawable(Drawable drawable) throws NoSuchFieldException {
        super.setButtonDrawable(drawable);
        AppCompatCompoundButtonHelper appCompatCompoundButtonHelper = this.mCompoundButtonHelper;
        if (appCompatCompoundButtonHelper != null) {
            appCompatCompoundButtonHelper.onSetButtonDrawable();
        }
    }

    @Override // android.support.v4.widget.TintableCompoundButton
    public void setSupportButtonTintList(ColorStateList colorStateList) throws NoSuchFieldException {
        AppCompatCompoundButtonHelper appCompatCompoundButtonHelper = this.mCompoundButtonHelper;
        if (appCompatCompoundButtonHelper != null) {
            appCompatCompoundButtonHelper.setSupportButtonTintList(colorStateList);
        }
    }

    @Override // android.support.v4.widget.TintableCompoundButton
    public void setSupportButtonTintMode(PorterDuff.Mode mode) throws NoSuchFieldException {
        AppCompatCompoundButtonHelper appCompatCompoundButtonHelper = this.mCompoundButtonHelper;
        if (appCompatCompoundButtonHelper != null) {
            appCompatCompoundButtonHelper.setSupportButtonTintMode(mode);
        }
    }
}
