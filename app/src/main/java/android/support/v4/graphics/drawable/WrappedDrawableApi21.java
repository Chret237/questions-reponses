package android.support.v4.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Outline;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.WrappedDrawableApi14;
import android.util.Log;
import java.lang.reflect.Method;

/* loaded from: classes-dex2jar.jar:android/support/v4/graphics/drawable/WrappedDrawableApi21.class */
class WrappedDrawableApi21 extends WrappedDrawableApi14 {
    private static final String TAG = "WrappedDrawableApi21";
    private static Method sIsProjectedDrawableMethod;

    /* loaded from: classes-dex2jar.jar:android/support/v4/graphics/drawable/WrappedDrawableApi21$DrawableWrapperStateLollipop.class */
    private static class DrawableWrapperStateLollipop extends WrappedDrawableApi14.DrawableWrapperState {
        DrawableWrapperStateLollipop(WrappedDrawableApi14.DrawableWrapperState drawableWrapperState, Resources resources) {
            super(drawableWrapperState, resources);
        }

        @Override // android.support.v4.graphics.drawable.WrappedDrawableApi14.DrawableWrapperState, android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable(Resources resources) {
            return new WrappedDrawableApi21(this, resources);
        }
    }

    WrappedDrawableApi21(Drawable drawable) {
        super(drawable);
        findAndCacheIsProjectedDrawableMethod();
    }

    WrappedDrawableApi21(WrappedDrawableApi14.DrawableWrapperState drawableWrapperState, Resources resources) {
        super(drawableWrapperState, resources);
        findAndCacheIsProjectedDrawableMethod();
    }

    private void findAndCacheIsProjectedDrawableMethod() {
        if (sIsProjectedDrawableMethod == null) {
            try {
                sIsProjectedDrawableMethod = Drawable.class.getDeclaredMethod("isProjected", new Class[0]);
            } catch (Exception e) {
                Log.w(TAG, "Failed to retrieve Drawable#isProjected() method", e);
            }
        }
    }

    @Override // android.graphics.drawable.Drawable
    public Rect getDirtyBounds() {
        return this.mDrawable.getDirtyBounds();
    }

    @Override // android.graphics.drawable.Drawable
    public void getOutline(Outline outline) {
        this.mDrawable.getOutline(outline);
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0036  */
    @Override // android.support.v4.graphics.drawable.WrappedDrawableApi14
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected boolean isCompatTintEnabled() {
        /*
            r3 = this;
            int r0 = android.os.Build.VERSION.SDK_INT
            r4 = r0
            r0 = 0
            r6 = r0
            r0 = r6
            r5 = r0
            r0 = r4
            r1 = 21
            if (r0 != r1) goto L38
            r0 = r3
            android.graphics.drawable.Drawable r0 = r0.mDrawable
            r7 = r0
            r0 = r7
            boolean r0 = r0 instanceof android.graphics.drawable.GradientDrawable
            if (r0 != 0) goto L36
            r0 = r7
            boolean r0 = r0 instanceof android.graphics.drawable.DrawableContainer
            if (r0 != 0) goto L36
            r0 = r7
            boolean r0 = r0 instanceof android.graphics.drawable.InsetDrawable
            if (r0 != 0) goto L36
            r0 = r6
            r5 = r0
            r0 = r7
            boolean r0 = r0 instanceof android.graphics.drawable.RippleDrawable
            if (r0 == 0) goto L38
        L36:
            r0 = 1
            r5 = r0
        L38:
            r0 = r5
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.graphics.drawable.WrappedDrawableApi21.isCompatTintEnabled():boolean");
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isProjected() {
        Method method;
        if (this.mDrawable == null || (method = sIsProjectedDrawableMethod) == null) {
            return false;
        }
        try {
            return ((Boolean) method.invoke(this.mDrawable, new Object[0])).booleanValue();
        } catch (Exception e) {
            Log.w(TAG, "Error calling Drawable#isProjected() method", e);
            return false;
        }
    }

    @Override // android.support.v4.graphics.drawable.WrappedDrawableApi14
    WrappedDrawableApi14.DrawableWrapperState mutateConstantState() {
        return new DrawableWrapperStateLollipop(this.mState, null);
    }

    @Override // android.graphics.drawable.Drawable
    public void setHotspot(float f, float f2) {
        this.mDrawable.setHotspot(f, f2);
    }

    @Override // android.graphics.drawable.Drawable
    public void setHotspotBounds(int i, int i2, int i3, int i4) {
        this.mDrawable.setHotspotBounds(i, i2, i3, i4);
    }

    @Override // android.support.v4.graphics.drawable.WrappedDrawableApi14, android.graphics.drawable.Drawable
    public boolean setState(int[] iArr) {
        if (!super.setState(iArr)) {
            return false;
        }
        invalidateSelf();
        return true;
    }

    @Override // android.support.v4.graphics.drawable.WrappedDrawableApi14, android.graphics.drawable.Drawable, android.support.v4.graphics.drawable.TintAwareDrawable
    public void setTint(int i) {
        if (isCompatTintEnabled()) {
            super.setTint(i);
        } else {
            this.mDrawable.setTint(i);
        }
    }

    @Override // android.support.v4.graphics.drawable.WrappedDrawableApi14, android.graphics.drawable.Drawable, android.support.v4.graphics.drawable.TintAwareDrawable
    public void setTintList(ColorStateList colorStateList) {
        if (isCompatTintEnabled()) {
            super.setTintList(colorStateList);
        } else {
            this.mDrawable.setTintList(colorStateList);
        }
    }

    @Override // android.support.v4.graphics.drawable.WrappedDrawableApi14, android.graphics.drawable.Drawable, android.support.v4.graphics.drawable.TintAwareDrawable
    public void setTintMode(PorterDuff.Mode mode) {
        if (isCompatTintEnabled()) {
            super.setTintMode(mode);
        } else {
            this.mDrawable.setTintMode(mode);
        }
    }
}
