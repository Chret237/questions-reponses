package android.support.v7.widget;

import android.R;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.appcompat.C0287R;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.text.AllCapsTransformationMethod;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.util.Property;
import android.view.ActionMode;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.CompoundButton;
import java.lang.reflect.InvocationTargetException;

/* loaded from: classes-dex2jar.jar:android/support/v7/widget/SwitchCompat.class */
public class SwitchCompat extends CompoundButton {
    private static final String ACCESSIBILITY_EVENT_CLASS_NAME = "android.widget.Switch";
    private static final int MONOSPACE = 3;
    private static final int SANS = 1;
    private static final int SERIF = 2;
    private static final int THUMB_ANIMATION_DURATION = 250;
    private static final int TOUCH_MODE_DOWN = 1;
    private static final int TOUCH_MODE_DRAGGING = 2;
    private static final int TOUCH_MODE_IDLE = 0;
    private boolean mHasThumbTint;
    private boolean mHasThumbTintMode;
    private boolean mHasTrackTint;
    private boolean mHasTrackTintMode;
    private int mMinFlingVelocity;
    private Layout mOffLayout;
    private Layout mOnLayout;
    ObjectAnimator mPositionAnimator;
    private boolean mShowText;
    private boolean mSplitTrack;
    private int mSwitchBottom;
    private int mSwitchHeight;
    private int mSwitchLeft;
    private int mSwitchMinWidth;
    private int mSwitchPadding;
    private int mSwitchRight;
    private int mSwitchTop;
    private TransformationMethod mSwitchTransformationMethod;
    private int mSwitchWidth;
    private final Rect mTempRect;
    private ColorStateList mTextColors;
    private CharSequence mTextOff;
    private CharSequence mTextOn;
    private final TextPaint mTextPaint;
    private Drawable mThumbDrawable;
    float mThumbPosition;
    private int mThumbTextPadding;
    private ColorStateList mThumbTintList;
    private PorterDuff.Mode mThumbTintMode;
    private int mThumbWidth;
    private int mTouchMode;
    private int mTouchSlop;
    private float mTouchX;
    private float mTouchY;
    private Drawable mTrackDrawable;
    private ColorStateList mTrackTintList;
    private PorterDuff.Mode mTrackTintMode;
    private VelocityTracker mVelocityTracker;
    private static final Property<SwitchCompat, Float> THUMB_POS = new Property<SwitchCompat, Float>(Float.class, "thumbPos") { // from class: android.support.v7.widget.SwitchCompat.1
        @Override // android.util.Property
        public Float get(SwitchCompat switchCompat) {
            return Float.valueOf(switchCompat.mThumbPosition);
        }

        @Override // android.util.Property
        public void set(SwitchCompat switchCompat, Float f) {
            switchCompat.setThumbPosition(f.floatValue());
        }
    };
    private static final int[] CHECKED_STATE_SET = {R.attr.state_checked};

    public SwitchCompat(Context context) {
        this(context, null);
    }

    public SwitchCompat(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, C0287R.attr.switchStyle);
    }

    public SwitchCompat(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mThumbTintList = null;
        this.mThumbTintMode = null;
        this.mHasThumbTint = false;
        this.mHasThumbTintMode = false;
        this.mTrackTintList = null;
        this.mTrackTintMode = null;
        this.mHasTrackTint = false;
        this.mHasTrackTintMode = false;
        this.mVelocityTracker = VelocityTracker.obtain();
        this.mTempRect = new Rect();
        this.mTextPaint = new TextPaint(1);
        Resources resources = getResources();
        this.mTextPaint.density = resources.getDisplayMetrics().density;
        TintTypedArray tintTypedArrayObtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, attributeSet, C0287R.styleable.SwitchCompat, i, 0);
        Drawable drawable = tintTypedArrayObtainStyledAttributes.getDrawable(C0287R.styleable.SwitchCompat_android_thumb);
        this.mThumbDrawable = drawable;
        if (drawable != null) {
            drawable.setCallback(this);
        }
        Drawable drawable2 = tintTypedArrayObtainStyledAttributes.getDrawable(C0287R.styleable.SwitchCompat_track);
        this.mTrackDrawable = drawable2;
        if (drawable2 != null) {
            drawable2.setCallback(this);
        }
        this.mTextOn = tintTypedArrayObtainStyledAttributes.getText(C0287R.styleable.SwitchCompat_android_textOn);
        this.mTextOff = tintTypedArrayObtainStyledAttributes.getText(C0287R.styleable.SwitchCompat_android_textOff);
        this.mShowText = tintTypedArrayObtainStyledAttributes.getBoolean(C0287R.styleable.SwitchCompat_showText, true);
        this.mThumbTextPadding = tintTypedArrayObtainStyledAttributes.getDimensionPixelSize(C0287R.styleable.SwitchCompat_thumbTextPadding, 0);
        this.mSwitchMinWidth = tintTypedArrayObtainStyledAttributes.getDimensionPixelSize(C0287R.styleable.SwitchCompat_switchMinWidth, 0);
        this.mSwitchPadding = tintTypedArrayObtainStyledAttributes.getDimensionPixelSize(C0287R.styleable.SwitchCompat_switchPadding, 0);
        this.mSplitTrack = tintTypedArrayObtainStyledAttributes.getBoolean(C0287R.styleable.SwitchCompat_splitTrack, false);
        ColorStateList colorStateList = tintTypedArrayObtainStyledAttributes.getColorStateList(C0287R.styleable.SwitchCompat_thumbTint);
        if (colorStateList != null) {
            this.mThumbTintList = colorStateList;
            this.mHasThumbTint = true;
        }
        PorterDuff.Mode tintMode = DrawableUtils.parseTintMode(tintTypedArrayObtainStyledAttributes.getInt(C0287R.styleable.SwitchCompat_thumbTintMode, -1), null);
        if (this.mThumbTintMode != tintMode) {
            this.mThumbTintMode = tintMode;
            this.mHasThumbTintMode = true;
        }
        if (this.mHasThumbTint || this.mHasThumbTintMode) {
            applyThumbTint();
        }
        ColorStateList colorStateList2 = tintTypedArrayObtainStyledAttributes.getColorStateList(C0287R.styleable.SwitchCompat_trackTint);
        if (colorStateList2 != null) {
            this.mTrackTintList = colorStateList2;
            this.mHasTrackTint = true;
        }
        PorterDuff.Mode tintMode2 = DrawableUtils.parseTintMode(tintTypedArrayObtainStyledAttributes.getInt(C0287R.styleable.SwitchCompat_trackTintMode, -1), null);
        if (this.mTrackTintMode != tintMode2) {
            this.mTrackTintMode = tintMode2;
            this.mHasTrackTintMode = true;
        }
        if (this.mHasTrackTint || this.mHasTrackTintMode) {
            applyTrackTint();
        }
        int resourceId = tintTypedArrayObtainStyledAttributes.getResourceId(C0287R.styleable.SwitchCompat_switchTextAppearance, 0);
        if (resourceId != 0) {
            setSwitchTextAppearance(context, resourceId);
        }
        tintTypedArrayObtainStyledAttributes.recycle();
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        this.mTouchSlop = viewConfiguration.getScaledTouchSlop();
        this.mMinFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        refreshDrawableState();
        setChecked(isChecked());
    }

    private void animateThumbToCheckedState(boolean z) {
        ObjectAnimator objectAnimatorOfFloat = ObjectAnimator.ofFloat(this, THUMB_POS, z ? 1.0f : 0.0f);
        this.mPositionAnimator = objectAnimatorOfFloat;
        objectAnimatorOfFloat.setDuration(250L);
        if (Build.VERSION.SDK_INT >= 18) {
            this.mPositionAnimator.setAutoCancel(true);
        }
        this.mPositionAnimator.start();
    }

    private void applyThumbTint() {
        if (this.mThumbDrawable != null) {
            if (this.mHasThumbTint || this.mHasThumbTintMode) {
                Drawable drawableMutate = this.mThumbDrawable.mutate();
                this.mThumbDrawable = drawableMutate;
                if (this.mHasThumbTint) {
                    DrawableCompat.setTintList(drawableMutate, this.mThumbTintList);
                }
                if (this.mHasThumbTintMode) {
                    DrawableCompat.setTintMode(this.mThumbDrawable, this.mThumbTintMode);
                }
                if (this.mThumbDrawable.isStateful()) {
                    this.mThumbDrawable.setState(getDrawableState());
                }
            }
        }
    }

    private void applyTrackTint() {
        if (this.mTrackDrawable != null) {
            if (this.mHasTrackTint || this.mHasTrackTintMode) {
                Drawable drawableMutate = this.mTrackDrawable.mutate();
                this.mTrackDrawable = drawableMutate;
                if (this.mHasTrackTint) {
                    DrawableCompat.setTintList(drawableMutate, this.mTrackTintList);
                }
                if (this.mHasTrackTintMode) {
                    DrawableCompat.setTintMode(this.mTrackDrawable, this.mTrackTintMode);
                }
                if (this.mTrackDrawable.isStateful()) {
                    this.mTrackDrawable.setState(getDrawableState());
                }
            }
        }
    }

    private void cancelPositionAnimator() {
        ObjectAnimator objectAnimator = this.mPositionAnimator;
        if (objectAnimator != null) {
            objectAnimator.cancel();
        }
    }

    private void cancelSuperTouch(MotionEvent motionEvent) {
        MotionEvent motionEventObtain = MotionEvent.obtain(motionEvent);
        motionEventObtain.setAction(3);
        super.onTouchEvent(motionEventObtain);
        motionEventObtain.recycle();
    }

    private static float constrain(float f, float f2, float f3) {
        if (f >= f2) {
            f2 = f;
            if (f > f3) {
                f2 = f3;
            }
        }
        return f2;
    }

    private boolean getTargetCheckedState() {
        return this.mThumbPosition > 0.5f;
    }

    private int getThumbOffset() {
        return (int) (((ViewUtils.isLayoutRtl(this) ? 1.0f - this.mThumbPosition : this.mThumbPosition) * getThumbScrollRange()) + 0.5f);
    }

    private int getThumbScrollRange() {
        Drawable drawable = this.mTrackDrawable;
        if (drawable == null) {
            return 0;
        }
        Rect rect = this.mTempRect;
        drawable.getPadding(rect);
        Drawable drawable2 = this.mThumbDrawable;
        Rect opticalBounds = drawable2 != null ? DrawableUtils.getOpticalBounds(drawable2) : DrawableUtils.INSETS_NONE;
        return ((((this.mSwitchWidth - this.mThumbWidth) - rect.left) - rect.right) - opticalBounds.left) - opticalBounds.right;
    }

    private boolean hitThumb(float f, float f2) {
        if (this.mThumbDrawable == null) {
            return false;
        }
        int thumbOffset = getThumbOffset();
        this.mThumbDrawable.getPadding(this.mTempRect);
        int i = this.mSwitchTop;
        int i2 = (this.mSwitchLeft + thumbOffset) - this.mTouchSlop;
        int i3 = this.mThumbWidth;
        int i4 = this.mTempRect.left;
        int i5 = this.mTempRect.right;
        int i6 = this.mTouchSlop;
        int i7 = this.mSwitchBottom;
        boolean z = false;
        if (f > i2) {
            z = false;
            if (f < i3 + i2 + i4 + i5 + i6) {
                z = false;
                if (f2 > i - r0) {
                    z = false;
                    if (f2 < i7 + i6) {
                        z = true;
                    }
                }
            }
        }
        return z;
    }

    private Layout makeLayout(CharSequence charSequence) {
        TransformationMethod transformationMethod = this.mSwitchTransformationMethod;
        CharSequence transformation = charSequence;
        if (transformationMethod != null) {
            transformation = transformationMethod.getTransformation(charSequence, this);
        }
        return new StaticLayout(transformation, this.mTextPaint, transformation != null ? (int) Math.ceil(Layout.getDesiredWidth(transformation, r0)) : 0, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
    }

    private void setSwitchTypefaceByIndex(int i, int i2) {
        setSwitchTypeface(i != 1 ? i != 2 ? i != 3 ? null : Typeface.MONOSPACE : Typeface.SERIF : Typeface.SANS_SERIF, i2);
    }

    private void stopDrag(MotionEvent motionEvent) {
        this.mTouchMode = 0;
        boolean targetCheckedState = true;
        boolean z = motionEvent.getAction() == 1 && isEnabled();
        boolean zIsChecked = isChecked();
        if (z) {
            this.mVelocityTracker.computeCurrentVelocity(1000);
            float xVelocity = this.mVelocityTracker.getXVelocity();
            if (Math.abs(xVelocity) <= this.mMinFlingVelocity) {
                targetCheckedState = getTargetCheckedState();
            } else if (!ViewUtils.isLayoutRtl(this) ? xVelocity <= 0.0f : xVelocity >= 0.0f) {
                targetCheckedState = false;
            }
        } else {
            targetCheckedState = zIsChecked;
        }
        if (targetCheckedState != zIsChecked) {
            playSoundEffect(0);
        }
        setChecked(targetCheckedState);
        cancelSuperTouch(motionEvent);
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        int i;
        int i2;
        int i3;
        int i4;
        Rect rect = this.mTempRect;
        int i5 = this.mSwitchLeft;
        int i6 = this.mSwitchTop;
        int i7 = this.mSwitchRight;
        int i8 = this.mSwitchBottom;
        int thumbOffset = getThumbOffset() + i5;
        Drawable drawable = this.mThumbDrawable;
        Rect opticalBounds = drawable != null ? DrawableUtils.getOpticalBounds(drawable) : DrawableUtils.INSETS_NONE;
        Drawable drawable2 = this.mTrackDrawable;
        int i9 = thumbOffset;
        if (drawable2 != null) {
            drawable2.getPadding(rect);
            int i10 = thumbOffset + rect.left;
            if (opticalBounds != null) {
                int i11 = i5;
                if (opticalBounds.left > rect.left) {
                    i11 = i5 + (opticalBounds.left - rect.left);
                }
                i3 = opticalBounds.top > rect.top ? (opticalBounds.top - rect.top) + i6 : i6;
                i4 = i7;
                if (opticalBounds.right > rect.right) {
                    i4 = i7 - (opticalBounds.right - rect.right);
                }
                i5 = i11;
                i7 = i4;
                i = i3;
                if (opticalBounds.bottom > rect.bottom) {
                    i5 = i11;
                    i2 = i8 - (opticalBounds.bottom - rect.bottom);
                }
                this.mTrackDrawable.setBounds(i5, i3, i4, i2);
                i9 = i10;
            } else {
                i = i6;
            }
            i2 = i8;
            i3 = i;
            i4 = i7;
            this.mTrackDrawable.setBounds(i5, i3, i4, i2);
            i9 = i10;
        }
        Drawable drawable3 = this.mThumbDrawable;
        if (drawable3 != null) {
            drawable3.getPadding(rect);
            int i12 = i9 - rect.left;
            int i13 = i9 + this.mThumbWidth + rect.right;
            this.mThumbDrawable.setBounds(i12, i6, i13, i8);
            Drawable background = getBackground();
            if (background != null) {
                DrawableCompat.setHotspotBounds(background, i12, i6, i13, i8);
            }
        }
        super.draw(canvas);
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    public void drawableHotspotChanged(float f, float f2) {
        if (Build.VERSION.SDK_INT >= 21) {
            super.drawableHotspotChanged(f, f2);
        }
        Drawable drawable = this.mThumbDrawable;
        if (drawable != null) {
            DrawableCompat.setHotspot(drawable, f, f2);
        }
        Drawable drawable2 = this.mTrackDrawable;
        if (drawable2 != null) {
            DrawableCompat.setHotspot(drawable2, f, f2);
        }
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        int[] drawableState = getDrawableState();
        Drawable drawable = this.mThumbDrawable;
        boolean state = false;
        if (drawable != null) {
            state = false;
            if (drawable.isStateful()) {
                state = false | drawable.setState(drawableState);
            }
        }
        Drawable drawable2 = this.mTrackDrawable;
        boolean state2 = state;
        if (drawable2 != null) {
            state2 = state;
            if (drawable2.isStateful()) {
                state2 = state | drawable2.setState(drawableState);
            }
        }
        if (state2) {
            invalidate();
        }
    }

    @Override // android.widget.CompoundButton, android.widget.TextView
    public int getCompoundPaddingLeft() {
        if (!ViewUtils.isLayoutRtl(this)) {
            return super.getCompoundPaddingLeft();
        }
        int compoundPaddingLeft = super.getCompoundPaddingLeft() + this.mSwitchWidth;
        int i = compoundPaddingLeft;
        if (!TextUtils.isEmpty(getText())) {
            i = compoundPaddingLeft + this.mSwitchPadding;
        }
        return i;
    }

    @Override // android.widget.CompoundButton, android.widget.TextView
    public int getCompoundPaddingRight() {
        if (ViewUtils.isLayoutRtl(this)) {
            return super.getCompoundPaddingRight();
        }
        int compoundPaddingRight = super.getCompoundPaddingRight() + this.mSwitchWidth;
        int i = compoundPaddingRight;
        if (!TextUtils.isEmpty(getText())) {
            i = compoundPaddingRight + this.mSwitchPadding;
        }
        return i;
    }

    public boolean getShowText() {
        return this.mShowText;
    }

    public boolean getSplitTrack() {
        return this.mSplitTrack;
    }

    public int getSwitchMinWidth() {
        return this.mSwitchMinWidth;
    }

    public int getSwitchPadding() {
        return this.mSwitchPadding;
    }

    public CharSequence getTextOff() {
        return this.mTextOff;
    }

    public CharSequence getTextOn() {
        return this.mTextOn;
    }

    public Drawable getThumbDrawable() {
        return this.mThumbDrawable;
    }

    public int getThumbTextPadding() {
        return this.mThumbTextPadding;
    }

    public ColorStateList getThumbTintList() {
        return this.mThumbTintList;
    }

    public PorterDuff.Mode getThumbTintMode() {
        return this.mThumbTintMode;
    }

    public Drawable getTrackDrawable() {
        return this.mTrackDrawable;
    }

    public ColorStateList getTrackTintList() {
        return this.mTrackTintList;
    }

    public PorterDuff.Mode getTrackTintMode() {
        return this.mTrackTintMode;
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        Drawable drawable = this.mThumbDrawable;
        if (drawable != null) {
            drawable.jumpToCurrentState();
        }
        Drawable drawable2 = this.mTrackDrawable;
        if (drawable2 != null) {
            drawable2.jumpToCurrentState();
        }
        ObjectAnimator objectAnimator = this.mPositionAnimator;
        if (objectAnimator == null || !objectAnimator.isStarted()) {
            return;
        }
        this.mPositionAnimator.end();
        this.mPositionAnimator = null;
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    protected int[] onCreateDrawableState(int i) {
        int[] iArrOnCreateDrawableState = super.onCreateDrawableState(i + 1);
        if (isChecked()) {
            mergeDrawableStates(iArrOnCreateDrawableState, CHECKED_STATE_SET);
        }
        return iArrOnCreateDrawableState;
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    protected void onDraw(Canvas canvas) throws IllegalAccessException, SecurityException, IllegalArgumentException, InvocationTargetException {
        int width;
        super.onDraw(canvas);
        Rect rect = this.mTempRect;
        Drawable drawable = this.mTrackDrawable;
        if (drawable != null) {
            drawable.getPadding(rect);
        } else {
            rect.setEmpty();
        }
        int i = this.mSwitchTop;
        int i2 = this.mSwitchBottom;
        int i3 = rect.top;
        int i4 = rect.bottom;
        Drawable drawable2 = this.mThumbDrawable;
        if (drawable != null) {
            if (!this.mSplitTrack || drawable2 == null) {
                drawable.draw(canvas);
            } else {
                Rect opticalBounds = DrawableUtils.getOpticalBounds(drawable2);
                drawable2.copyBounds(rect);
                rect.left += opticalBounds.left;
                rect.right -= opticalBounds.right;
                int iSave = canvas.save();
                canvas.clipRect(rect, Region.Op.DIFFERENCE);
                drawable.draw(canvas);
                canvas.restoreToCount(iSave);
            }
        }
        int iSave2 = canvas.save();
        if (drawable2 != null) {
            drawable2.draw(canvas);
        }
        Layout layout = getTargetCheckedState() ? this.mOnLayout : this.mOffLayout;
        if (layout != null) {
            int[] drawableState = getDrawableState();
            ColorStateList colorStateList = this.mTextColors;
            if (colorStateList != null) {
                this.mTextPaint.setColor(colorStateList.getColorForState(drawableState, 0));
            }
            this.mTextPaint.drawableState = drawableState;
            if (drawable2 != null) {
                Rect bounds = drawable2.getBounds();
                width = bounds.left + bounds.right;
            } else {
                width = getWidth();
            }
            canvas.translate((width / 2) - (layout.getWidth() / 2), (((i + i3) + (i2 - i4)) / 2) - (layout.getHeight() / 2));
            layout.draw(canvas);
        }
        canvas.restoreToCount(iSave2);
    }

    @Override // android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        accessibilityEvent.setClassName(ACCESSIBILITY_EVENT_CLASS_NAME);
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName(ACCESSIBILITY_EVENT_CLASS_NAME);
        CharSequence charSequence = isChecked() ? this.mTextOn : this.mTextOff;
        if (TextUtils.isEmpty(charSequence)) {
            return;
        }
        CharSequence text = accessibilityNodeInfo.getText();
        if (TextUtils.isEmpty(text)) {
            accessibilityNodeInfo.setText(charSequence);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(text);
        sb.append(' ');
        sb.append(charSequence);
        accessibilityNodeInfo.setText(sb);
    }

    @Override // android.widget.TextView, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) throws IllegalAccessException, SecurityException, IllegalArgumentException, InvocationTargetException {
        int iMax;
        int i5;
        int paddingLeft;
        int i6;
        int paddingTop;
        int height;
        super.onLayout(z, i, i2, i3, i4);
        int iMax2 = 0;
        if (this.mThumbDrawable != null) {
            Rect rect = this.mTempRect;
            Drawable drawable = this.mTrackDrawable;
            if (drawable != null) {
                drawable.getPadding(rect);
            } else {
                rect.setEmpty();
            }
            Rect opticalBounds = DrawableUtils.getOpticalBounds(this.mThumbDrawable);
            iMax = Math.max(0, opticalBounds.left - rect.left);
            iMax2 = Math.max(0, opticalBounds.right - rect.right);
        } else {
            iMax = 0;
        }
        if (ViewUtils.isLayoutRtl(this)) {
            paddingLeft = getPaddingLeft() + iMax;
            i5 = ((this.mSwitchWidth + paddingLeft) - iMax) - iMax2;
        } else {
            int width = (getWidth() - getPaddingRight()) - iMax2;
            int i7 = (width - this.mSwitchWidth) + iMax + iMax2;
            i5 = width;
            paddingLeft = i7;
        }
        int gravity = getGravity() & 112;
        if (gravity == 16) {
            int paddingTop2 = ((getPaddingTop() + getHeight()) - getPaddingBottom()) / 2;
            i6 = this.mSwitchHeight;
            paddingTop = paddingTop2 - (i6 / 2);
        } else {
            if (gravity == 80) {
                height = getHeight() - getPaddingBottom();
                paddingTop = height - this.mSwitchHeight;
                this.mSwitchLeft = paddingLeft;
                this.mSwitchTop = paddingTop;
                this.mSwitchBottom = height;
                this.mSwitchRight = i5;
            }
            paddingTop = getPaddingTop();
            i6 = this.mSwitchHeight;
        }
        height = i6 + paddingTop;
        this.mSwitchLeft = paddingLeft;
        this.mSwitchTop = paddingTop;
        this.mSwitchBottom = height;
        this.mSwitchRight = i5;
    }

    @Override // android.widget.TextView, android.view.View
    public void onMeasure(int i, int i2) throws IllegalAccessException, SecurityException, IllegalArgumentException, InvocationTargetException {
        int intrinsicWidth;
        int intrinsicHeight;
        int intrinsicHeight2;
        if (this.mShowText) {
            if (this.mOnLayout == null) {
                this.mOnLayout = makeLayout(this.mTextOn);
            }
            if (this.mOffLayout == null) {
                this.mOffLayout = makeLayout(this.mTextOff);
            }
        }
        Rect rect = this.mTempRect;
        Drawable drawable = this.mThumbDrawable;
        if (drawable != null) {
            drawable.getPadding(rect);
            intrinsicWidth = (this.mThumbDrawable.getIntrinsicWidth() - rect.left) - rect.right;
            intrinsicHeight = this.mThumbDrawable.getIntrinsicHeight();
        } else {
            intrinsicWidth = 0;
            intrinsicHeight = 0;
        }
        this.mThumbWidth = Math.max(this.mShowText ? Math.max(this.mOnLayout.getWidth(), this.mOffLayout.getWidth()) + (this.mThumbTextPadding * 2) : 0, intrinsicWidth);
        Drawable drawable2 = this.mTrackDrawable;
        if (drawable2 != null) {
            drawable2.getPadding(rect);
            intrinsicHeight2 = this.mTrackDrawable.getIntrinsicHeight();
        } else {
            rect.setEmpty();
            intrinsicHeight2 = 0;
        }
        int i3 = rect.left;
        int i4 = rect.right;
        Drawable drawable3 = this.mThumbDrawable;
        int iMax = i4;
        int iMax2 = i3;
        if (drawable3 != null) {
            Rect opticalBounds = DrawableUtils.getOpticalBounds(drawable3);
            iMax2 = Math.max(i3, opticalBounds.left);
            iMax = Math.max(i4, opticalBounds.right);
        }
        int iMax3 = Math.max(this.mSwitchMinWidth, (this.mThumbWidth * 2) + iMax2 + iMax);
        int iMax4 = Math.max(intrinsicHeight2, intrinsicHeight);
        this.mSwitchWidth = iMax3;
        this.mSwitchHeight = iMax4;
        super.onMeasure(i, i2);
        if (getMeasuredHeight() < iMax4) {
            setMeasuredDimension(getMeasuredWidthAndState(), iMax4);
        }
    }

    @Override // android.view.View
    public void onPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onPopulateAccessibilityEvent(accessibilityEvent);
        CharSequence charSequence = isChecked() ? this.mTextOn : this.mTextOff;
        if (charSequence != null) {
            accessibilityEvent.getText().add(charSequence);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:37:0x00e5  */
    @Override // android.widget.TextView, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean onTouchEvent(android.view.MotionEvent r5) {
        /*
            Method dump skipped, instructions count: 312
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v7.widget.SwitchCompat.onTouchEvent(android.view.MotionEvent):boolean");
    }

    @Override // android.widget.CompoundButton, android.widget.Checkable
    public void setChecked(boolean z) {
        super.setChecked(z);
        boolean zIsChecked = isChecked();
        if (getWindowToken() != null && ViewCompat.isLaidOut(this)) {
            animateThumbToCheckedState(zIsChecked);
        } else {
            cancelPositionAnimator();
            setThumbPosition(zIsChecked ? 1.0f : 0.0f);
        }
    }

    @Override // android.widget.TextView
    public void setCustomSelectionActionModeCallback(ActionMode.Callback callback) {
        super.setCustomSelectionActionModeCallback(TextViewCompat.wrapCustomSelectionActionModeCallback(this, callback));
    }

    public void setShowText(boolean z) {
        if (this.mShowText != z) {
            this.mShowText = z;
            requestLayout();
        }
    }

    public void setSplitTrack(boolean z) {
        this.mSplitTrack = z;
        invalidate();
    }

    public void setSwitchMinWidth(int i) {
        this.mSwitchMinWidth = i;
        requestLayout();
    }

    public void setSwitchPadding(int i) {
        this.mSwitchPadding = i;
        requestLayout();
    }

    public void setSwitchTextAppearance(Context context, int i) {
        TintTypedArray tintTypedArrayObtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, i, C0287R.styleable.TextAppearance);
        ColorStateList colorStateList = tintTypedArrayObtainStyledAttributes.getColorStateList(C0287R.styleable.TextAppearance_android_textColor);
        if (colorStateList != null) {
            this.mTextColors = colorStateList;
        } else {
            this.mTextColors = getTextColors();
        }
        int dimensionPixelSize = tintTypedArrayObtainStyledAttributes.getDimensionPixelSize(C0287R.styleable.TextAppearance_android_textSize, 0);
        if (dimensionPixelSize != 0) {
            float f = dimensionPixelSize;
            if (f != this.mTextPaint.getTextSize()) {
                this.mTextPaint.setTextSize(f);
                requestLayout();
            }
        }
        setSwitchTypefaceByIndex(tintTypedArrayObtainStyledAttributes.getInt(C0287R.styleable.TextAppearance_android_typeface, -1), tintTypedArrayObtainStyledAttributes.getInt(C0287R.styleable.TextAppearance_android_textStyle, -1));
        if (tintTypedArrayObtainStyledAttributes.getBoolean(C0287R.styleable.TextAppearance_textAllCaps, false)) {
            this.mSwitchTransformationMethod = new AllCapsTransformationMethod(getContext());
        } else {
            this.mSwitchTransformationMethod = null;
        }
        tintTypedArrayObtainStyledAttributes.recycle();
    }

    public void setSwitchTypeface(Typeface typeface) {
        if ((this.mTextPaint.getTypeface() == null || this.mTextPaint.getTypeface().equals(typeface)) && (this.mTextPaint.getTypeface() != null || typeface == null)) {
            return;
        }
        this.mTextPaint.setTypeface(typeface);
        requestLayout();
        invalidate();
    }

    public void setSwitchTypeface(Typeface typeface, int i) {
        float f = 0.0f;
        boolean z = false;
        if (i <= 0) {
            this.mTextPaint.setFakeBoldText(false);
            this.mTextPaint.setTextSkewX(0.0f);
            setSwitchTypeface(typeface);
            return;
        }
        Typeface typefaceDefaultFromStyle = typeface == null ? Typeface.defaultFromStyle(i) : Typeface.create(typeface, i);
        setSwitchTypeface(typefaceDefaultFromStyle);
        int style = ((typefaceDefaultFromStyle != null ? typefaceDefaultFromStyle.getStyle() : 0) ^ (-1)) & i;
        TextPaint textPaint = this.mTextPaint;
        if ((style & 1) != 0) {
            z = true;
        }
        textPaint.setFakeBoldText(z);
        TextPaint textPaint2 = this.mTextPaint;
        if ((style & 2) != 0) {
            f = -0.25f;
        }
        textPaint2.setTextSkewX(f);
    }

    public void setTextOff(CharSequence charSequence) {
        this.mTextOff = charSequence;
        requestLayout();
    }

    public void setTextOn(CharSequence charSequence) {
        this.mTextOn = charSequence;
        requestLayout();
    }

    public void setThumbDrawable(Drawable drawable) {
        Drawable drawable2 = this.mThumbDrawable;
        if (drawable2 != null) {
            drawable2.setCallback(null);
        }
        this.mThumbDrawable = drawable;
        if (drawable != null) {
            drawable.setCallback(this);
        }
        requestLayout();
    }

    void setThumbPosition(float f) {
        this.mThumbPosition = f;
        invalidate();
    }

    public void setThumbResource(int i) {
        setThumbDrawable(AppCompatResources.getDrawable(getContext(), i));
    }

    public void setThumbTextPadding(int i) {
        this.mThumbTextPadding = i;
        requestLayout();
    }

    public void setThumbTintList(ColorStateList colorStateList) {
        this.mThumbTintList = colorStateList;
        this.mHasThumbTint = true;
        applyThumbTint();
    }

    public void setThumbTintMode(PorterDuff.Mode mode) {
        this.mThumbTintMode = mode;
        this.mHasThumbTintMode = true;
        applyThumbTint();
    }

    public void setTrackDrawable(Drawable drawable) {
        Drawable drawable2 = this.mTrackDrawable;
        if (drawable2 != null) {
            drawable2.setCallback(null);
        }
        this.mTrackDrawable = drawable;
        if (drawable != null) {
            drawable.setCallback(this);
        }
        requestLayout();
    }

    public void setTrackResource(int i) {
        setTrackDrawable(AppCompatResources.getDrawable(getContext(), i));
    }

    public void setTrackTintList(ColorStateList colorStateList) {
        this.mTrackTintList = colorStateList;
        this.mHasTrackTint = true;
        applyTrackTint();
    }

    public void setTrackTintMode(PorterDuff.Mode mode) {
        this.mTrackTintMode = mode;
        this.mHasTrackTintMode = true;
        applyTrackTint();
    }

    @Override // android.widget.CompoundButton, android.widget.Checkable
    public void toggle() {
        setChecked(!isChecked());
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    protected boolean verifyDrawable(Drawable drawable) {
        return super.verifyDrawable(drawable) || drawable == this.mThumbDrawable || drawable == this.mTrackDrawable;
    }
}
