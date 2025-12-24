package android.support.v7.widget;

import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.CompoundButtonCompat;
import android.support.v7.appcompat.C0287R;
import android.support.v7.content.res.AppCompatResources;
import android.util.AttributeSet;
import android.widget.CompoundButton;

/* loaded from: classes-dex2jar.jar:android/support/v7/widget/AppCompatCompoundButtonHelper.class */
class AppCompatCompoundButtonHelper {
    private ColorStateList mButtonTintList = null;
    private PorterDuff.Mode mButtonTintMode = null;
    private boolean mHasButtonTint = false;
    private boolean mHasButtonTintMode = false;
    private boolean mSkipNextApply;
    private final CompoundButton mView;

    /* loaded from: classes-dex2jar.jar:android/support/v7/widget/AppCompatCompoundButtonHelper$DirectSetButtonDrawableInterface.class */
    interface DirectSetButtonDrawableInterface {
        void setButtonDrawable(Drawable drawable);
    }

    AppCompatCompoundButtonHelper(CompoundButton compoundButton) {
        this.mView = compoundButton;
    }

    void applyButtonTint() throws NoSuchFieldException {
        Drawable buttonDrawable = CompoundButtonCompat.getButtonDrawable(this.mView);
        if (buttonDrawable != null) {
            if (this.mHasButtonTint || this.mHasButtonTintMode) {
                Drawable drawableMutate = DrawableCompat.wrap(buttonDrawable).mutate();
                if (this.mHasButtonTint) {
                    DrawableCompat.setTintList(drawableMutate, this.mButtonTintList);
                }
                if (this.mHasButtonTintMode) {
                    DrawableCompat.setTintMode(drawableMutate, this.mButtonTintMode);
                }
                if (drawableMutate.isStateful()) {
                    drawableMutate.setState(this.mView.getDrawableState());
                }
                this.mView.setButtonDrawable(drawableMutate);
            }
        }
    }

    int getCompoundPaddingLeft(int i) throws NoSuchFieldException {
        int intrinsicWidth = i;
        if (Build.VERSION.SDK_INT < 17) {
            Drawable buttonDrawable = CompoundButtonCompat.getButtonDrawable(this.mView);
            intrinsicWidth = i;
            if (buttonDrawable != null) {
                intrinsicWidth = i + buttonDrawable.getIntrinsicWidth();
            }
        }
        return intrinsicWidth;
    }

    ColorStateList getSupportButtonTintList() {
        return this.mButtonTintList;
    }

    PorterDuff.Mode getSupportButtonTintMode() {
        return this.mButtonTintMode;
    }

    void loadFromAttributes(AttributeSet attributeSet, int i) {
        int resourceId;
        TypedArray typedArrayObtainStyledAttributes = this.mView.getContext().obtainStyledAttributes(attributeSet, C0287R.styleable.CompoundButton, i, 0);
        try {
            if (typedArrayObtainStyledAttributes.hasValue(C0287R.styleable.CompoundButton_android_button) && (resourceId = typedArrayObtainStyledAttributes.getResourceId(C0287R.styleable.CompoundButton_android_button, 0)) != 0) {
                this.mView.setButtonDrawable(AppCompatResources.getDrawable(this.mView.getContext(), resourceId));
            }
            if (typedArrayObtainStyledAttributes.hasValue(C0287R.styleable.CompoundButton_buttonTint)) {
                CompoundButtonCompat.setButtonTintList(this.mView, typedArrayObtainStyledAttributes.getColorStateList(C0287R.styleable.CompoundButton_buttonTint));
            }
            if (typedArrayObtainStyledAttributes.hasValue(C0287R.styleable.CompoundButton_buttonTintMode)) {
                CompoundButtonCompat.setButtonTintMode(this.mView, DrawableUtils.parseTintMode(typedArrayObtainStyledAttributes.getInt(C0287R.styleable.CompoundButton_buttonTintMode, -1), null));
            }
        } finally {
            typedArrayObtainStyledAttributes.recycle();
        }
    }

    void onSetButtonDrawable() throws NoSuchFieldException {
        if (this.mSkipNextApply) {
            this.mSkipNextApply = false;
        } else {
            this.mSkipNextApply = true;
            applyButtonTint();
        }
    }

    void setSupportButtonTintList(ColorStateList colorStateList) throws NoSuchFieldException {
        this.mButtonTintList = colorStateList;
        this.mHasButtonTint = true;
        applyButtonTint();
    }

    void setSupportButtonTintMode(PorterDuff.Mode mode) throws NoSuchFieldException {
        this.mButtonTintMode = mode;
        this.mHasButtonTintMode = true;
        applyButtonTint();
    }
}
