package android.support.design.chip;

import android.R;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.C0029R;
import android.support.design.animation.MotionSpec;
import android.support.design.chip.ChipDrawable;
import android.support.design.resources.TextAppearance;
import android.support.design.ripple.RippleUtils;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.text.BidiFormatter;
import android.support.v4.view.PointerIconCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.widget.ExploreByTouchHelper;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.github.clans.fab.BuildConfig;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/* loaded from: classes-dex2jar.jar:android/support/design/chip/Chip.class */
public class Chip extends AppCompatCheckBox implements ChipDrawable.Delegate {
    private static final int CLOSE_ICON_VIRTUAL_ID = 0;
    private static final String NAMESPACE_ANDROID = "http://schemas.android.com/apk/res/android";
    private static final String TAG = "Chip";
    private ChipDrawable chipDrawable;
    private boolean closeIconFocused;
    private boolean closeIconHovered;
    private boolean closeIconPressed;
    private boolean deferredCheckedValue;
    private int focusedVirtualView;
    private final ResourcesCompat.FontCallback fontCallback;
    private CompoundButton.OnCheckedChangeListener onCheckedChangeListenerInternal;
    private View.OnClickListener onCloseIconClickListener;
    private final Rect rect;
    private final RectF rectF;
    private RippleDrawable ripple;
    private final ChipTouchHelper touchHelper;
    private static final Rect EMPTY_BOUNDS = new Rect();
    private static final int[] SELECTED_STATE = {R.attr.state_selected};

    /* loaded from: classes-dex2jar.jar:android/support/design/chip/Chip$ChipTouchHelper.class */
    private class ChipTouchHelper extends ExploreByTouchHelper {
        final Chip this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        ChipTouchHelper(Chip chip, Chip chip2) {
            super(chip2);
            this.this$0 = chip;
        }

        @Override // android.support.v4.widget.ExploreByTouchHelper
        protected int getVirtualViewAt(float f, float f2) {
            return (this.this$0.hasCloseIcon() && this.this$0.getCloseIconTouchBounds().contains(f, f2)) ? 0 : -1;
        }

        @Override // android.support.v4.widget.ExploreByTouchHelper
        protected void getVisibleVirtualViews(List<Integer> list) {
            if (this.this$0.hasCloseIcon()) {
                list.add(0);
            }
        }

        @Override // android.support.v4.widget.ExploreByTouchHelper
        protected boolean onPerformActionForVirtualView(int i, int i2, Bundle bundle) {
            if (i2 == 16 && i == 0) {
                return this.this$0.performCloseIconClick();
            }
            return false;
        }

        @Override // android.support.v4.widget.ExploreByTouchHelper
        protected void onPopulateNodeForHost(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            accessibilityNodeInfoCompat.setCheckable(this.this$0.chipDrawable != null && this.this$0.chipDrawable.isCheckable());
            accessibilityNodeInfoCompat.setClassName(Chip.class.getName());
            CharSequence text = this.this$0.getText();
            if (Build.VERSION.SDK_INT >= 23) {
                accessibilityNodeInfoCompat.setText(text);
            } else {
                accessibilityNodeInfoCompat.setContentDescription(text);
            }
        }

        @Override // android.support.v4.widget.ExploreByTouchHelper
        protected void onPopulateNodeForVirtualView(int i, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            boolean zHasCloseIcon = this.this$0.hasCloseIcon();
            CharSequence charSequence = BuildConfig.FLAVOR;
            if (!zHasCloseIcon) {
                accessibilityNodeInfoCompat.setContentDescription(BuildConfig.FLAVOR);
                accessibilityNodeInfoCompat.setBoundsInParent(Chip.EMPTY_BOUNDS);
                return;
            }
            CharSequence closeIconContentDescription = this.this$0.getCloseIconContentDescription();
            if (closeIconContentDescription != null) {
                accessibilityNodeInfoCompat.setContentDescription(closeIconContentDescription);
            } else {
                CharSequence text = this.this$0.getText();
                Context context = this.this$0.getContext();
                int i2 = C0029R.string.mtrl_chip_close_icon_content_description;
                if (!TextUtils.isEmpty(text)) {
                    charSequence = text;
                }
                accessibilityNodeInfoCompat.setContentDescription(context.getString(i2, charSequence).trim());
            }
            accessibilityNodeInfoCompat.setBoundsInParent(this.this$0.getCloseIconTouchBoundsInt());
            accessibilityNodeInfoCompat.addAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLICK);
            accessibilityNodeInfoCompat.setEnabled(this.this$0.isEnabled());
        }
    }

    public Chip(Context context) {
        this(context, null);
    }

    public Chip(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, C0029R.attr.chipStyle);
    }

    public Chip(Context context, AttributeSet attributeSet, int i) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        super(context, attributeSet, i);
        this.focusedVirtualView = Integer.MIN_VALUE;
        this.rect = new Rect();
        this.rectF = new RectF();
        this.fontCallback = new ResourcesCompat.FontCallback(this) { // from class: android.support.design.chip.Chip.1
            final Chip this$0;

            {
                this.this$0 = this;
            }

            @Override // android.support.v4.content.res.ResourcesCompat.FontCallback
            public void onFontRetrievalFailed(int i2) {
            }

            @Override // android.support.v4.content.res.ResourcesCompat.FontCallback
            public void onFontRetrieved(Typeface typeface) {
                Chip chip = this.this$0;
                chip.setText(chip.getText());
                this.this$0.requestLayout();
                this.this$0.invalidate();
            }
        };
        validateAttributes(attributeSet);
        ChipDrawable chipDrawableCreateFromAttributes = ChipDrawable.createFromAttributes(context, attributeSet, i, C0029R.style.Widget_MaterialComponents_Chip_Action);
        setChipDrawable(chipDrawableCreateFromAttributes);
        ChipTouchHelper chipTouchHelper = new ChipTouchHelper(this, this);
        this.touchHelper = chipTouchHelper;
        ViewCompat.setAccessibilityDelegate(this, chipTouchHelper);
        initOutlineProvider();
        setChecked(this.deferredCheckedValue);
        chipDrawableCreateFromAttributes.setShouldDrawText(false);
        setText(chipDrawableCreateFromAttributes.getText());
        setEllipsize(chipDrawableCreateFromAttributes.getEllipsize());
        setIncludeFontPadding(false);
        if (getTextAppearance() != null) {
            updateTextPaintDrawState(getTextAppearance());
        }
        setSingleLine();
        setGravity(8388627);
        updatePaddingInternal();
    }

    private void applyChipDrawable(ChipDrawable chipDrawable) {
        chipDrawable.setDelegate(this);
    }

    private float calculateTextOffsetFromStart(ChipDrawable chipDrawable) {
        float chipStartPadding = getChipStartPadding() + chipDrawable.calculateChipIconWidth() + getTextStartPadding();
        return ViewCompat.getLayoutDirection(this) == 0 ? chipStartPadding : -chipStartPadding;
    }

    private int[] createCloseIconDrawableState() {
        int i = isEnabled() ? 1 : 0;
        int i2 = i;
        if (this.closeIconFocused) {
            i2 = i + 1;
        }
        int i3 = i2;
        if (this.closeIconHovered) {
            i3 = i2 + 1;
        }
        int i4 = i3;
        if (this.closeIconPressed) {
            i4 = i3 + 1;
        }
        int i5 = i4;
        if (isChecked()) {
            i5 = i4 + 1;
        }
        int[] iArr = new int[i5];
        int i6 = 0;
        if (isEnabled()) {
            iArr[0] = 16842910;
            i6 = 1;
        }
        int i7 = i6;
        if (this.closeIconFocused) {
            iArr[i6] = 16842908;
            i7 = i6 + 1;
        }
        int i8 = i7;
        if (this.closeIconHovered) {
            iArr[i7] = 16843623;
            i8 = i7 + 1;
        }
        int i9 = i8;
        if (this.closeIconPressed) {
            iArr[i8] = 16842919;
            i9 = i8 + 1;
        }
        if (isChecked()) {
            iArr[i9] = 16842913;
        }
        return iArr;
    }

    private void ensureFocus() {
        if (this.focusedVirtualView == Integer.MIN_VALUE) {
            setFocusedVirtualView(-1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public RectF getCloseIconTouchBounds() {
        this.rectF.setEmpty();
        if (hasCloseIcon()) {
            this.chipDrawable.getCloseIconTouchBounds(this.rectF);
        }
        return this.rectF;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Rect getCloseIconTouchBoundsInt() {
        RectF closeIconTouchBounds = getCloseIconTouchBounds();
        this.rect.set((int) closeIconTouchBounds.left, (int) closeIconTouchBounds.top, (int) closeIconTouchBounds.right, (int) closeIconTouchBounds.bottom);
        return this.rect;
    }

    private TextAppearance getTextAppearance() {
        ChipDrawable chipDrawable = this.chipDrawable;
        return chipDrawable != null ? chipDrawable.getTextAppearance() : null;
    }

    private boolean handleAccessibilityExit(MotionEvent motionEvent) throws IllegalAccessException, NoSuchFieldException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        if (motionEvent.getAction() != 10) {
            return false;
        }
        try {
            Field declaredField = ExploreByTouchHelper.class.getDeclaredField("mHoveredVirtualViewId");
            declaredField.setAccessible(true);
            if (((Integer) declaredField.get(this.touchHelper)).intValue() == Integer.MIN_VALUE) {
                return false;
            }
            Method declaredMethod = ExploreByTouchHelper.class.getDeclaredMethod("updateHoveredVirtualView", Integer.TYPE);
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(this.touchHelper, Integer.MIN_VALUE);
            return true;
        } catch (IllegalAccessException e) {
            Log.e(TAG, "Unable to send Accessibility Exit event", e);
            return false;
        } catch (NoSuchFieldException e2) {
            Log.e(TAG, "Unable to send Accessibility Exit event", e2);
            return false;
        } catch (NoSuchMethodException e3) {
            Log.e(TAG, "Unable to send Accessibility Exit event", e3);
            return false;
        } catch (InvocationTargetException e4) {
            Log.e(TAG, "Unable to send Accessibility Exit event", e4);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasCloseIcon() {
        ChipDrawable chipDrawable = this.chipDrawable;
        return (chipDrawable == null || chipDrawable.getCloseIcon() == null) ? false : true;
    }

    private void initOutlineProvider() {
        if (Build.VERSION.SDK_INT >= 21) {
            setOutlineProvider(new ViewOutlineProvider(this) { // from class: android.support.design.chip.Chip.2
                final Chip this$0;

                {
                    this.this$0 = this;
                }

                @Override // android.view.ViewOutlineProvider
                public void getOutline(View view, Outline outline) {
                    if (this.this$0.chipDrawable != null) {
                        this.this$0.chipDrawable.getOutline(outline);
                    } else {
                        outline.setAlpha(0.0f);
                    }
                }
            });
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x002d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean moveFocus(boolean r4) {
        /*
            r3 = this;
            r0 = r3
            r0.ensureFocus()
            r0 = 1
            r5 = r0
            r0 = r4
            if (r0 == 0) goto L1c
            r0 = r3
            int r0 = r0.focusedVirtualView
            r1 = -1
            if (r0 != r1) goto L2d
            r0 = r3
            r1 = 0
            r0.setFocusedVirtualView(r1)
            r0 = r5
            r4 = r0
            goto L2f
        L1c:
            r0 = r3
            int r0 = r0.focusedVirtualView
            if (r0 != 0) goto L2d
            r0 = r3
            r1 = -1
            r0.setFocusedVirtualView(r1)
            r0 = r5
            r4 = r0
            goto L2f
        L2d:
            r0 = 0
            r4 = r0
        L2f:
            r0 = r4
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.design.chip.Chip.moveFocus(boolean):boolean");
    }

    private void setCloseIconFocused(boolean z) {
        if (this.closeIconFocused != z) {
            this.closeIconFocused = z;
            refreshDrawableState();
        }
    }

    private void setCloseIconHovered(boolean z) {
        if (this.closeIconHovered != z) {
            this.closeIconHovered = z;
            refreshDrawableState();
        }
    }

    private void setCloseIconPressed(boolean z) {
        if (this.closeIconPressed != z) {
            this.closeIconPressed = z;
            refreshDrawableState();
        }
    }

    private void setFocusedVirtualView(int i) {
        int i2 = this.focusedVirtualView;
        if (i2 != i) {
            if (i2 == 0) {
                setCloseIconFocused(false);
            }
            this.focusedVirtualView = i;
            if (i == 0) {
                setCloseIconFocused(true);
            }
        }
    }

    private void unapplyChipDrawable(ChipDrawable chipDrawable) {
        if (chipDrawable != null) {
            chipDrawable.setDelegate(null);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x0068  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void updatePaddingInternal() {
        /*
            Method dump skipped, instructions count: 209
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.design.chip.Chip.updatePaddingInternal():void");
    }

    private void updateTextPaintDrawState(TextAppearance textAppearance) {
        TextPaint paint = getPaint();
        paint.drawableState = this.chipDrawable.getState();
        textAppearance.updateDrawState(getContext(), paint, this.fontCallback);
    }

    private void validateAttributes(AttributeSet attributeSet) {
        if (attributeSet == null) {
            return;
        }
        if (attributeSet.getAttributeValue(NAMESPACE_ANDROID, "background") != null) {
            throw new UnsupportedOperationException("Do not set the background; Chip manages its own background drawable.");
        }
        if (attributeSet.getAttributeValue(NAMESPACE_ANDROID, "drawableLeft") != null) {
            throw new UnsupportedOperationException("Please set left drawable using R.attr#chipIcon.");
        }
        if (attributeSet.getAttributeValue(NAMESPACE_ANDROID, "drawableStart") != null) {
            throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
        }
        if (attributeSet.getAttributeValue(NAMESPACE_ANDROID, "drawableEnd") != null) {
            throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
        }
        if (attributeSet.getAttributeValue(NAMESPACE_ANDROID, "drawableRight") != null) {
            throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
        }
        if (!attributeSet.getAttributeBooleanValue(NAMESPACE_ANDROID, "singleLine", true) || attributeSet.getAttributeIntValue(NAMESPACE_ANDROID, "lines", 1) != 1 || attributeSet.getAttributeIntValue(NAMESPACE_ANDROID, "minLines", 1) != 1 || attributeSet.getAttributeIntValue(NAMESPACE_ANDROID, "maxLines", 1) != 1) {
            throw new UnsupportedOperationException("Chip does not support multi-line text");
        }
        if (attributeSet.getAttributeIntValue(NAMESPACE_ANDROID, "gravity", 8388627) != 8388627) {
            Log.w(TAG, "Chip text must be vertically center and start aligned");
        }
    }

    @Override // android.view.View
    protected boolean dispatchHoverEvent(MotionEvent motionEvent) {
        return handleAccessibilityExit(motionEvent) || this.touchHelper.dispatchHoverEvent(motionEvent) || super.dispatchHoverEvent(motionEvent);
    }

    @Override // android.view.View
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        return this.touchHelper.dispatchKeyEvent(keyEvent) || super.dispatchKeyEvent(keyEvent);
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        ChipDrawable chipDrawable = this.chipDrawable;
        if ((chipDrawable == null || !chipDrawable.isCloseIconStateful()) ? false : this.chipDrawable.setCloseIconState(createCloseIconDrawableState())) {
            invalidate();
        }
    }

    public Drawable getCheckedIcon() {
        ChipDrawable chipDrawable = this.chipDrawable;
        return chipDrawable != null ? chipDrawable.getCheckedIcon() : null;
    }

    public ColorStateList getChipBackgroundColor() {
        ChipDrawable chipDrawable = this.chipDrawable;
        return chipDrawable != null ? chipDrawable.getChipBackgroundColor() : null;
    }

    public float getChipCornerRadius() {
        ChipDrawable chipDrawable = this.chipDrawable;
        return chipDrawable != null ? chipDrawable.getChipCornerRadius() : 0.0f;
    }

    public Drawable getChipDrawable() {
        return this.chipDrawable;
    }

    public float getChipEndPadding() {
        ChipDrawable chipDrawable = this.chipDrawable;
        return chipDrawable != null ? chipDrawable.getChipEndPadding() : 0.0f;
    }

    public Drawable getChipIcon() {
        ChipDrawable chipDrawable = this.chipDrawable;
        return chipDrawable != null ? chipDrawable.getChipIcon() : null;
    }

    public float getChipIconSize() {
        ChipDrawable chipDrawable = this.chipDrawable;
        return chipDrawable != null ? chipDrawable.getChipIconSize() : 0.0f;
    }

    public ColorStateList getChipIconTint() {
        ChipDrawable chipDrawable = this.chipDrawable;
        return chipDrawable != null ? chipDrawable.getChipIconTint() : null;
    }

    public float getChipMinHeight() {
        ChipDrawable chipDrawable = this.chipDrawable;
        return chipDrawable != null ? chipDrawable.getChipMinHeight() : 0.0f;
    }

    public float getChipStartPadding() {
        ChipDrawable chipDrawable = this.chipDrawable;
        return chipDrawable != null ? chipDrawable.getChipStartPadding() : 0.0f;
    }

    public ColorStateList getChipStrokeColor() {
        ChipDrawable chipDrawable = this.chipDrawable;
        return chipDrawable != null ? chipDrawable.getChipStrokeColor() : null;
    }

    public float getChipStrokeWidth() {
        ChipDrawable chipDrawable = this.chipDrawable;
        return chipDrawable != null ? chipDrawable.getChipStrokeWidth() : 0.0f;
    }

    @Deprecated
    public CharSequence getChipText() {
        return getText();
    }

    public Drawable getCloseIcon() {
        ChipDrawable chipDrawable = this.chipDrawable;
        return chipDrawable != null ? chipDrawable.getCloseIcon() : null;
    }

    public CharSequence getCloseIconContentDescription() {
        ChipDrawable chipDrawable = this.chipDrawable;
        return chipDrawable != null ? chipDrawable.getCloseIconContentDescription() : null;
    }

    public float getCloseIconEndPadding() {
        ChipDrawable chipDrawable = this.chipDrawable;
        return chipDrawable != null ? chipDrawable.getCloseIconEndPadding() : 0.0f;
    }

    public float getCloseIconSize() {
        ChipDrawable chipDrawable = this.chipDrawable;
        return chipDrawable != null ? chipDrawable.getCloseIconSize() : 0.0f;
    }

    public float getCloseIconStartPadding() {
        ChipDrawable chipDrawable = this.chipDrawable;
        return chipDrawable != null ? chipDrawable.getCloseIconStartPadding() : 0.0f;
    }

    public ColorStateList getCloseIconTint() {
        ChipDrawable chipDrawable = this.chipDrawable;
        return chipDrawable != null ? chipDrawable.getCloseIconTint() : null;
    }

    @Override // android.widget.TextView
    public TextUtils.TruncateAt getEllipsize() {
        ChipDrawable chipDrawable = this.chipDrawable;
        return chipDrawable != null ? chipDrawable.getEllipsize() : null;
    }

    @Override // android.widget.TextView, android.view.View
    public void getFocusedRect(Rect rect) {
        if (this.focusedVirtualView == 0) {
            rect.set(getCloseIconTouchBoundsInt());
        } else {
            super.getFocusedRect(rect);
        }
    }

    public MotionSpec getHideMotionSpec() {
        ChipDrawable chipDrawable = this.chipDrawable;
        return chipDrawable != null ? chipDrawable.getHideMotionSpec() : null;
    }

    public float getIconEndPadding() {
        ChipDrawable chipDrawable = this.chipDrawable;
        return chipDrawable != null ? chipDrawable.getIconEndPadding() : 0.0f;
    }

    public float getIconStartPadding() {
        ChipDrawable chipDrawable = this.chipDrawable;
        return chipDrawable != null ? chipDrawable.getIconStartPadding() : 0.0f;
    }

    public ColorStateList getRippleColor() {
        ChipDrawable chipDrawable = this.chipDrawable;
        return chipDrawable != null ? chipDrawable.getRippleColor() : null;
    }

    public MotionSpec getShowMotionSpec() {
        ChipDrawable chipDrawable = this.chipDrawable;
        return chipDrawable != null ? chipDrawable.getShowMotionSpec() : null;
    }

    @Override // android.widget.TextView
    public CharSequence getText() {
        ChipDrawable chipDrawable = this.chipDrawable;
        return chipDrawable != null ? chipDrawable.getText() : BuildConfig.FLAVOR;
    }

    public float getTextEndPadding() {
        ChipDrawable chipDrawable = this.chipDrawable;
        return chipDrawable != null ? chipDrawable.getTextEndPadding() : 0.0f;
    }

    public float getTextStartPadding() {
        ChipDrawable chipDrawable = this.chipDrawable;
        return chipDrawable != null ? chipDrawable.getTextStartPadding() : 0.0f;
    }

    public boolean isCheckable() {
        ChipDrawable chipDrawable = this.chipDrawable;
        return chipDrawable != null && chipDrawable.isCheckable();
    }

    @Deprecated
    public boolean isCheckedIconEnabled() {
        return isCheckedIconVisible();
    }

    public boolean isCheckedIconVisible() {
        ChipDrawable chipDrawable = this.chipDrawable;
        return chipDrawable != null && chipDrawable.isCheckedIconVisible();
    }

    @Deprecated
    public boolean isChipIconEnabled() {
        return isChipIconVisible();
    }

    public boolean isChipIconVisible() {
        ChipDrawable chipDrawable = this.chipDrawable;
        return chipDrawable != null && chipDrawable.isChipIconVisible();
    }

    @Deprecated
    public boolean isCloseIconEnabled() {
        return isCloseIconVisible();
    }

    public boolean isCloseIconVisible() {
        ChipDrawable chipDrawable = this.chipDrawable;
        return chipDrawable != null && chipDrawable.isCloseIconVisible();
    }

    @Override // android.support.design.chip.ChipDrawable.Delegate
    public void onChipDrawableSizeChange() {
        updatePaddingInternal();
        requestLayout();
        if (Build.VERSION.SDK_INT >= 21) {
            invalidateOutline();
        }
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    protected int[] onCreateDrawableState(int i) {
        int[] iArrOnCreateDrawableState = super.onCreateDrawableState(i + 1);
        if (isChecked()) {
            mergeDrawableStates(iArrOnCreateDrawableState, SELECTED_STATE);
        }
        return iArrOnCreateDrawableState;
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    protected void onDraw(Canvas canvas) {
        ChipDrawable chipDrawable;
        if (TextUtils.isEmpty(getText()) || (chipDrawable = this.chipDrawable) == null || chipDrawable.shouldDrawText()) {
            super.onDraw(canvas);
            return;
        }
        int iSave = canvas.save();
        canvas.translate(calculateTextOffsetFromStart(this.chipDrawable), 0.0f);
        super.onDraw(canvas);
        canvas.restoreToCount(iSave);
    }

    @Override // android.widget.TextView, android.view.View
    protected void onFocusChanged(boolean z, int i, Rect rect) {
        if (z) {
            setFocusedVirtualView(-1);
        } else {
            setFocusedVirtualView(Integer.MIN_VALUE);
        }
        invalidate();
        super.onFocusChanged(z, i, rect);
        this.touchHelper.onFocusChanged(z, i, rect);
    }

    @Override // android.view.View
    public boolean onHoverEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 7) {
            setCloseIconHovered(getCloseIconTouchBounds().contains(motionEvent.getX(), motionEvent.getY()));
        } else if (actionMasked == 10) {
            setCloseIconHovered(false);
        }
        return super.onHoverEvent(motionEvent);
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0069  */
    @Override // android.widget.TextView, android.view.View, android.view.KeyEvent.Callback
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean onKeyDown(int r5, android.view.KeyEvent r6) {
        /*
            Method dump skipped, instructions count: 252
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.design.chip.Chip.onKeyDown(int, android.view.KeyEvent):boolean");
    }

    @Override // android.widget.Button, android.widget.TextView, android.view.View
    public PointerIcon onResolvePointerIcon(MotionEvent motionEvent, int i) {
        if (getCloseIconTouchBounds().contains(motionEvent.getX(), motionEvent.getY()) && isEnabled()) {
            return PointerIcon.getSystemIcon(getContext(), PointerIconCompat.TYPE_HAND);
        }
        return null;
    }

    /* JADX WARN: Code restructure failed: missing block: B:9:0x0028, code lost:
    
        if (r0 != 3) goto L25;
     */
    @Override // android.widget.TextView, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean onTouchEvent(android.view.MotionEvent r5) {
        /*
            r4 = this;
            r0 = r5
            int r0 = r0.getActionMasked()
            r6 = r0
            r0 = r4
            android.graphics.RectF r0 = r0.getCloseIconTouchBounds()
            r1 = r5
            float r1 = r1.getX()
            r2 = r5
            float r2 = r2.getY()
            boolean r0 = r0.contains(r1, r2)
            r8 = r0
            r0 = 0
            r7 = r0
            r0 = r6
            if (r0 == 0) goto L5d
            r0 = r6
            r1 = 1
            if (r0 == r1) goto L42
            r0 = r6
            r1 = 2
            if (r0 == r1) goto L2e
            r0 = r6
            r1 = 3
            if (r0 == r1) goto L53
            goto L6c
        L2e:
            r0 = r4
            boolean r0 = r0.closeIconPressed
            if (r0 == 0) goto L6c
            r0 = r8
            if (r0 != 0) goto L67
            r0 = r4
            r1 = 0
            r0.setCloseIconPressed(r1)
            goto L67
        L42:
            r0 = r4
            boolean r0 = r0.closeIconPressed
            if (r0 == 0) goto L53
            r0 = r4
            boolean r0 = r0.performCloseIconClick()
            r0 = 1
            r6 = r0
            goto L55
        L53:
            r0 = 0
            r6 = r0
        L55:
            r0 = r4
            r1 = 0
            r0.setCloseIconPressed(r1)
            goto L6e
        L5d:
            r0 = r8
            if (r0 == 0) goto L6c
            r0 = r4
            r1 = 1
            r0.setCloseIconPressed(r1)
        L67:
            r0 = 1
            r6 = r0
            goto L6e
        L6c:
            r0 = 0
            r6 = r0
        L6e:
            r0 = r6
            if (r0 != 0) goto L7a
            r0 = r4
            r1 = r5
            boolean r0 = super.onTouchEvent(r1)
            if (r0 == 0) goto L7c
        L7a:
            r0 = 1
            r7 = r0
        L7c:
            r0 = r7
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.design.chip.Chip.onTouchEvent(android.view.MotionEvent):boolean");
    }

    public boolean performCloseIconClick() {
        boolean z;
        playSoundEffect(0);
        View.OnClickListener onClickListener = this.onCloseIconClickListener;
        if (onClickListener != null) {
            onClickListener.onClick(this);
            z = true;
        } else {
            z = false;
        }
        this.touchHelper.sendEventForVirtualView(0, 1);
        return z;
    }

    @Override // android.view.View
    public void setBackground(Drawable drawable) {
        if (drawable != this.chipDrawable && drawable != this.ripple) {
            throw new UnsupportedOperationException("Do not set the background; Chip manages its own background drawable.");
        }
        super.setBackground(drawable);
    }

    @Override // android.view.View
    public void setBackgroundColor(int i) {
        throw new UnsupportedOperationException("Do not set the background color; Chip manages its own background drawable.");
    }

    @Override // android.view.View
    public void setBackgroundDrawable(Drawable drawable) {
        if (drawable != this.chipDrawable && drawable != this.ripple) {
            throw new UnsupportedOperationException("Do not set the background drawable; Chip manages its own background drawable.");
        }
        super.setBackgroundDrawable(drawable);
    }

    @Override // android.view.View
    public void setBackgroundResource(int i) {
        throw new UnsupportedOperationException("Do not set the background resource; Chip manages its own background drawable.");
    }

    @Override // android.view.View
    public void setBackgroundTintList(ColorStateList colorStateList) {
        throw new UnsupportedOperationException("Do not set the background tint list; Chip manages its own background drawable.");
    }

    @Override // android.view.View
    public void setBackgroundTintMode(PorterDuff.Mode mode) {
        throw new UnsupportedOperationException("Do not set the background tint mode; Chip manages its own background drawable.");
    }

    public void setCheckable(boolean z) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setCheckable(z);
        }
    }

    public void setCheckableResource(int i) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setCheckableResource(i);
        }
    }

    @Override // android.widget.CompoundButton, android.widget.Checkable
    public void setChecked(boolean z) {
        CompoundButton.OnCheckedChangeListener onCheckedChangeListener;
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable == null) {
            this.deferredCheckedValue = z;
            return;
        }
        if (chipDrawable.isCheckable()) {
            boolean zIsChecked = isChecked();
            super.setChecked(z);
            if (zIsChecked == z || (onCheckedChangeListener = this.onCheckedChangeListenerInternal) == null) {
                return;
            }
            onCheckedChangeListener.onCheckedChanged(this, z);
        }
    }

    public void setCheckedIcon(Drawable drawable) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setCheckedIcon(drawable);
        }
    }

    @Deprecated
    public void setCheckedIconEnabled(boolean z) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        setCheckedIconVisible(z);
    }

    @Deprecated
    public void setCheckedIconEnabledResource(int i) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        setCheckedIconVisible(i);
    }

    public void setCheckedIconResource(int i) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setCheckedIconResource(i);
        }
    }

    public void setCheckedIconVisible(int i) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setCheckedIconVisible(i);
        }
    }

    public void setCheckedIconVisible(boolean z) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setCheckedIconVisible(z);
        }
    }

    public void setChipBackgroundColor(ColorStateList colorStateList) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipBackgroundColor(colorStateList);
        }
    }

    public void setChipBackgroundColorResource(int i) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipBackgroundColorResource(i);
        }
    }

    public void setChipCornerRadius(float f) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipCornerRadius(f);
        }
    }

    public void setChipCornerRadiusResource(int i) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipCornerRadiusResource(i);
        }
    }

    public void setChipDrawable(ChipDrawable chipDrawable) {
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != chipDrawable) {
            unapplyChipDrawable(chipDrawable2);
            this.chipDrawable = chipDrawable;
            applyChipDrawable(chipDrawable);
            if (!RippleUtils.USE_FRAMEWORK_RIPPLE) {
                this.chipDrawable.setUseCompatRipple(true);
                ViewCompat.setBackground(this, this.chipDrawable);
            } else {
                this.ripple = new RippleDrawable(RippleUtils.convertToRippleDrawableColor(this.chipDrawable.getRippleColor()), this.chipDrawable, null);
                this.chipDrawable.setUseCompatRipple(false);
                ViewCompat.setBackground(this, this.ripple);
            }
        }
    }

    public void setChipEndPadding(float f) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipEndPadding(f);
        }
    }

    public void setChipEndPaddingResource(int i) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipEndPaddingResource(i);
        }
    }

    public void setChipIcon(Drawable drawable) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipIcon(drawable);
        }
    }

    @Deprecated
    public void setChipIconEnabled(boolean z) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        setChipIconVisible(z);
    }

    @Deprecated
    public void setChipIconEnabledResource(int i) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        setChipIconVisible(i);
    }

    public void setChipIconResource(int i) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipIconResource(i);
        }
    }

    public void setChipIconSize(float f) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipIconSize(f);
        }
    }

    public void setChipIconSizeResource(int i) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipIconSizeResource(i);
        }
    }

    public void setChipIconTint(ColorStateList colorStateList) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipIconTint(colorStateList);
        }
    }

    public void setChipIconTintResource(int i) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipIconTintResource(i);
        }
    }

    public void setChipIconVisible(int i) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipIconVisible(i);
        }
    }

    public void setChipIconVisible(boolean z) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipIconVisible(z);
        }
    }

    public void setChipMinHeight(float f) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipMinHeight(f);
        }
    }

    public void setChipMinHeightResource(int i) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipMinHeightResource(i);
        }
    }

    public void setChipStartPadding(float f) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipStartPadding(f);
        }
    }

    public void setChipStartPaddingResource(int i) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipStartPaddingResource(i);
        }
    }

    public void setChipStrokeColor(ColorStateList colorStateList) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipStrokeColor(colorStateList);
        }
    }

    public void setChipStrokeColorResource(int i) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipStrokeColorResource(i);
        }
    }

    public void setChipStrokeWidth(float f) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipStrokeWidth(f);
        }
    }

    public void setChipStrokeWidthResource(int i) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setChipStrokeWidthResource(i);
        }
    }

    @Deprecated
    public void setChipText(CharSequence charSequence) {
        setText(charSequence);
    }

    @Deprecated
    public void setChipTextResource(int i) {
        setText(getResources().getString(i));
    }

    public void setCloseIcon(Drawable drawable) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setCloseIcon(drawable);
        }
    }

    public void setCloseIconContentDescription(CharSequence charSequence) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setCloseIconContentDescription(charSequence);
        }
    }

    @Deprecated
    public void setCloseIconEnabled(boolean z) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        setCloseIconVisible(z);
    }

    @Deprecated
    public void setCloseIconEnabledResource(int i) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        setCloseIconVisible(i);
    }

    public void setCloseIconEndPadding(float f) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setCloseIconEndPadding(f);
        }
    }

    public void setCloseIconEndPaddingResource(int i) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setCloseIconEndPaddingResource(i);
        }
    }

    public void setCloseIconResource(int i) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setCloseIconResource(i);
        }
    }

    public void setCloseIconSize(float f) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setCloseIconSize(f);
        }
    }

    public void setCloseIconSizeResource(int i) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setCloseIconSizeResource(i);
        }
    }

    public void setCloseIconStartPadding(float f) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setCloseIconStartPadding(f);
        }
    }

    public void setCloseIconStartPaddingResource(int i) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setCloseIconStartPaddingResource(i);
        }
    }

    public void setCloseIconTint(ColorStateList colorStateList) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setCloseIconTint(colorStateList);
        }
    }

    public void setCloseIconTintResource(int i) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setCloseIconTintResource(i);
        }
    }

    public void setCloseIconVisible(int i) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setCloseIconVisible(i);
        }
    }

    public void setCloseIconVisible(boolean z) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setCloseIconVisible(z);
        }
    }

    @Override // android.widget.TextView
    public void setCompoundDrawables(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        if (drawable != null) {
            throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
        }
        if (drawable3 != null) {
            throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
        }
        super.setCompoundDrawables(drawable, drawable2, drawable3, drawable4);
    }

    @Override // android.widget.TextView
    public void setCompoundDrawablesRelative(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        if (drawable != null) {
            throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
        }
        if (drawable3 != null) {
            throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
        }
        super.setCompoundDrawablesRelative(drawable, drawable2, drawable3, drawable4);
    }

    @Override // android.widget.TextView
    public void setCompoundDrawablesRelativeWithIntrinsicBounds(int i, int i2, int i3, int i4) {
        if (i != 0) {
            throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
        }
        if (i3 != 0) {
            throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
        }
        super.setCompoundDrawablesRelativeWithIntrinsicBounds(i, i2, i3, i4);
    }

    @Override // android.widget.TextView
    public void setCompoundDrawablesRelativeWithIntrinsicBounds(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        if (drawable != null) {
            throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
        }
        if (drawable3 != null) {
            throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
        }
        super.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, drawable2, drawable3, drawable4);
    }

    @Override // android.widget.TextView
    public void setCompoundDrawablesWithIntrinsicBounds(int i, int i2, int i3, int i4) {
        if (i != 0) {
            throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
        }
        if (i3 != 0) {
            throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
        }
        super.setCompoundDrawablesWithIntrinsicBounds(i, i2, i3, i4);
    }

    @Override // android.widget.TextView
    public void setCompoundDrawablesWithIntrinsicBounds(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        if (drawable != null) {
            throw new UnsupportedOperationException("Please set left drawable using R.attr#chipIcon.");
        }
        if (drawable3 != null) {
            throw new UnsupportedOperationException("Please set right drawable using R.attr#closeIcon.");
        }
        super.setCompoundDrawablesWithIntrinsicBounds(drawable, drawable2, drawable3, drawable4);
    }

    @Override // android.widget.TextView
    public void setEllipsize(TextUtils.TruncateAt truncateAt) {
        if (this.chipDrawable == null) {
            return;
        }
        if (truncateAt == TextUtils.TruncateAt.MARQUEE) {
            throw new UnsupportedOperationException("Text within a chip are not allowed to scroll.");
        }
        super.setEllipsize(truncateAt);
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setEllipsize(truncateAt);
        }
    }

    @Override // android.widget.TextView
    public void setGravity(int i) {
        if (i != 8388627) {
            Log.w(TAG, "Chip text must be vertically center and start aligned");
        } else {
            super.setGravity(i);
        }
    }

    public void setHideMotionSpec(MotionSpec motionSpec) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setHideMotionSpec(motionSpec);
        }
    }

    public void setHideMotionSpecResource(int i) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setHideMotionSpecResource(i);
        }
    }

    public void setIconEndPadding(float f) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setIconEndPadding(f);
        }
    }

    public void setIconEndPaddingResource(int i) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setIconEndPaddingResource(i);
        }
    }

    public void setIconStartPadding(float f) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setIconStartPadding(f);
        }
    }

    public void setIconStartPaddingResource(int i) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setIconStartPaddingResource(i);
        }
    }

    @Override // android.widget.TextView
    public void setLines(int i) {
        if (i > 1) {
            throw new UnsupportedOperationException("Chip does not support multi-line text");
        }
        super.setLines(i);
    }

    @Override // android.widget.TextView
    public void setMaxLines(int i) {
        if (i > 1) {
            throw new UnsupportedOperationException("Chip does not support multi-line text");
        }
        super.setMaxLines(i);
    }

    @Override // android.widget.TextView
    public void setMaxWidth(int i) {
        super.setMaxWidth(i);
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setMaxWidth(i);
        }
    }

    @Override // android.widget.TextView
    public void setMinLines(int i) {
        if (i > 1) {
            throw new UnsupportedOperationException("Chip does not support multi-line text");
        }
        super.setMinLines(i);
    }

    void setOnCheckedChangeListenerInternal(CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListenerInternal = onCheckedChangeListener;
    }

    public void setOnCloseIconClickListener(View.OnClickListener onClickListener) {
        this.onCloseIconClickListener = onClickListener;
    }

    public void setRippleColor(ColorStateList colorStateList) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setRippleColor(colorStateList);
        }
    }

    public void setRippleColorResource(int i) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setRippleColorResource(i);
        }
    }

    public void setShowMotionSpec(MotionSpec motionSpec) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setShowMotionSpec(motionSpec);
        }
    }

    public void setShowMotionSpecResource(int i) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setShowMotionSpecResource(i);
        }
    }

    @Override // android.widget.TextView
    public void setSingleLine(boolean z) {
        if (!z) {
            throw new UnsupportedOperationException("Chip does not support multi-line text");
        }
        super.setSingleLine(z);
    }

    @Override // android.widget.TextView
    public void setText(CharSequence charSequence, TextView.BufferType bufferType) {
        if (this.chipDrawable == null) {
            return;
        }
        CharSequence charSequence2 = charSequence;
        if (charSequence == null) {
            charSequence2 = BuildConfig.FLAVOR;
        }
        CharSequence charSequenceUnicodeWrap = BidiFormatter.getInstance().unicodeWrap(charSequence2);
        if (this.chipDrawable.shouldDrawText()) {
            charSequenceUnicodeWrap = null;
        }
        super.setText(charSequenceUnicodeWrap, bufferType);
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setText(charSequence2);
        }
    }

    @Override // android.widget.TextView
    public void setTextAppearance(int i) {
        super.setTextAppearance(i);
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setTextAppearanceResource(i);
        }
        if (getTextAppearance() != null) {
            getTextAppearance().updateMeasureState(getContext(), getPaint(), this.fontCallback);
            updateTextPaintDrawState(getTextAppearance());
        }
    }

    @Override // android.widget.TextView
    public void setTextAppearance(Context context, int i) {
        super.setTextAppearance(context, i);
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setTextAppearanceResource(i);
        }
        if (getTextAppearance() != null) {
            getTextAppearance().updateMeasureState(context, getPaint(), this.fontCallback);
            updateTextPaintDrawState(getTextAppearance());
        }
    }

    public void setTextAppearance(TextAppearance textAppearance) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setTextAppearance(textAppearance);
        }
        if (getTextAppearance() != null) {
            getTextAppearance().updateMeasureState(getContext(), getPaint(), this.fontCallback);
            updateTextPaintDrawState(textAppearance);
        }
    }

    public void setTextAppearanceResource(int i) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setTextAppearanceResource(i);
        }
        setTextAppearance(getContext(), i);
    }

    public void setTextEndPadding(float f) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setTextEndPadding(f);
        }
    }

    public void setTextEndPaddingResource(int i) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setTextEndPaddingResource(i);
        }
    }

    public void setTextStartPadding(float f) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setTextStartPadding(f);
        }
    }

    public void setTextStartPaddingResource(int i) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setTextStartPaddingResource(i);
        }
    }
}
