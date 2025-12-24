package android.support.design.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.design.C0029R;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.drawable.DrawableWrapper;

/* loaded from: classes-dex2jar.jar:android/support/design/widget/ShadowDrawableWrapper.class */
public class ShadowDrawableWrapper extends DrawableWrapper {
    static final double COS_45 = Math.cos(Math.toRadians(45.0d));
    static final float SHADOW_BOTTOM_SCALE = 1.0f;
    static final float SHADOW_HORIZ_SCALE = 0.5f;
    static final float SHADOW_MULTIPLIER = 1.5f;
    static final float SHADOW_TOP_SCALE = 0.25f;
    private boolean addPaddingForCorners;
    final RectF contentBounds;
    float cornerRadius;
    final Paint cornerShadowPaint;
    Path cornerShadowPath;
    private boolean dirty;
    final Paint edgeShadowPaint;
    float maxShadowSize;
    private boolean printedShadowClipWarning;
    float rawMaxShadowSize;
    float rawShadowSize;
    private float rotation;
    private final int shadowEndColor;
    private final int shadowMiddleColor;
    float shadowSize;
    private final int shadowStartColor;

    public ShadowDrawableWrapper(Context context, Drawable drawable, float f, float f2, float f3) {
        super(drawable);
        this.dirty = true;
        this.addPaddingForCorners = true;
        this.printedShadowClipWarning = false;
        this.shadowStartColor = ContextCompat.getColor(context, C0029R.color.design_fab_shadow_start_color);
        this.shadowMiddleColor = ContextCompat.getColor(context, C0029R.color.design_fab_shadow_mid_color);
        this.shadowEndColor = ContextCompat.getColor(context, C0029R.color.design_fab_shadow_end_color);
        Paint paint = new Paint(5);
        this.cornerShadowPaint = paint;
        paint.setStyle(Paint.Style.FILL);
        this.cornerRadius = Math.round(f);
        this.contentBounds = new RectF();
        Paint paint2 = new Paint(this.cornerShadowPaint);
        this.edgeShadowPaint = paint2;
        paint2.setAntiAlias(false);
        setShadowSize(f2, f3);
    }

    private void buildComponents(Rect rect) {
        float f = this.rawMaxShadowSize * SHADOW_MULTIPLIER;
        this.contentBounds.set(rect.left + this.rawMaxShadowSize, rect.top + f, rect.right - this.rawMaxShadowSize, rect.bottom - f);
        getWrappedDrawable().setBounds((int) this.contentBounds.left, (int) this.contentBounds.top, (int) this.contentBounds.right, (int) this.contentBounds.bottom);
        buildShadowCorners();
    }

    private void buildShadowCorners() {
        float f = this.cornerRadius;
        RectF rectF = new RectF(-f, -f, f, f);
        RectF rectF2 = new RectF(rectF);
        float f2 = this.shadowSize;
        rectF2.inset(-f2, -f2);
        Path path = this.cornerShadowPath;
        if (path == null) {
            this.cornerShadowPath = new Path();
        } else {
            path.reset();
        }
        this.cornerShadowPath.setFillType(Path.FillType.EVEN_ODD);
        this.cornerShadowPath.moveTo(-this.cornerRadius, 0.0f);
        this.cornerShadowPath.rLineTo(-this.shadowSize, 0.0f);
        this.cornerShadowPath.arcTo(rectF2, 180.0f, 90.0f, false);
        this.cornerShadowPath.arcTo(rectF, 270.0f, -90.0f, false);
        this.cornerShadowPath.close();
        float f3 = -rectF2.top;
        if (f3 > 0.0f) {
            float f4 = this.cornerRadius / f3;
            float f5 = (SHADOW_BOTTOM_SCALE - f4) / 2.0f;
            Paint paint = this.cornerShadowPaint;
            int i = this.shadowStartColor;
            int i2 = this.shadowMiddleColor;
            int i3 = this.shadowEndColor;
            paint.setShader(new RadialGradient(0.0f, 0.0f, f3, new int[]{0, i, i2, i3}, new float[]{0.0f, f4, f5 + f4, SHADOW_BOTTOM_SCALE}, Shader.TileMode.CLAMP));
        }
        Paint paint2 = this.edgeShadowPaint;
        float f6 = rectF.top;
        float f7 = rectF2.top;
        int i4 = this.shadowStartColor;
        int i5 = this.shadowMiddleColor;
        int i6 = this.shadowEndColor;
        paint2.setShader(new LinearGradient(0.0f, f6, 0.0f, f7, new int[]{i4, i5, i6}, new float[]{0.0f, SHADOW_HORIZ_SCALE, SHADOW_BOTTOM_SCALE}, Shader.TileMode.CLAMP));
        this.edgeShadowPaint.setAntiAlias(false);
    }

    public static float calculateHorizontalPadding(float f, float f2, boolean z) {
        float f3 = f;
        if (z) {
            double d = f;
            double d2 = COS_45;
            double d3 = f2;
            Double.isNaN(d3);
            Double.isNaN(d);
            f3 = (float) (d + ((1.0d - d2) * d3));
        }
        return f3;
    }

    public static float calculateVerticalPadding(float f, float f2, boolean z) {
        if (!z) {
            return f * SHADOW_MULTIPLIER;
        }
        double d = f * SHADOW_MULTIPLIER;
        double d2 = COS_45;
        double d3 = f2;
        Double.isNaN(d3);
        Double.isNaN(d);
        return (float) (d + ((1.0d - d2) * d3));
    }

    private void drawShadow(Canvas canvas) {
        int iSave = canvas.save();
        canvas.rotate(this.rotation, this.contentBounds.centerX(), this.contentBounds.centerY());
        float f = this.cornerRadius;
        float f2 = (-f) - this.shadowSize;
        float f3 = f * 2.0f;
        boolean z = this.contentBounds.width() - f3 > 0.0f;
        boolean z2 = this.contentBounds.height() - f3 > 0.0f;
        float f4 = this.rawShadowSize;
        float f5 = f / ((f4 - (SHADOW_HORIZ_SCALE * f4)) + f);
        float f6 = f / ((f4 - (SHADOW_TOP_SCALE * f4)) + f);
        float f7 = f / ((f4 - (f4 * SHADOW_BOTTOM_SCALE)) + f);
        int iSave2 = canvas.save();
        canvas.translate(this.contentBounds.left + f, this.contentBounds.top + f);
        canvas.scale(f5, f6);
        canvas.drawPath(this.cornerShadowPath, this.cornerShadowPaint);
        if (z) {
            canvas.scale(SHADOW_BOTTOM_SCALE / f5, SHADOW_BOTTOM_SCALE);
            canvas.drawRect(0.0f, f2, this.contentBounds.width() - f3, -this.cornerRadius, this.edgeShadowPaint);
        }
        canvas.restoreToCount(iSave2);
        int iSave3 = canvas.save();
        canvas.translate(this.contentBounds.right - f, this.contentBounds.bottom - f);
        canvas.scale(f5, f7);
        canvas.rotate(180.0f);
        canvas.drawPath(this.cornerShadowPath, this.cornerShadowPaint);
        if (z) {
            canvas.scale(SHADOW_BOTTOM_SCALE / f5, SHADOW_BOTTOM_SCALE);
            canvas.drawRect(0.0f, f2, this.contentBounds.width() - f3, (-this.cornerRadius) + this.shadowSize, this.edgeShadowPaint);
        }
        canvas.restoreToCount(iSave3);
        int iSave4 = canvas.save();
        canvas.translate(this.contentBounds.left + f, this.contentBounds.bottom - f);
        canvas.scale(f5, f7);
        canvas.rotate(270.0f);
        canvas.drawPath(this.cornerShadowPath, this.cornerShadowPaint);
        if (z2) {
            canvas.scale(SHADOW_BOTTOM_SCALE / f7, SHADOW_BOTTOM_SCALE);
            canvas.drawRect(0.0f, f2, this.contentBounds.height() - f3, -this.cornerRadius, this.edgeShadowPaint);
        }
        canvas.restoreToCount(iSave4);
        int iSave5 = canvas.save();
        canvas.translate(this.contentBounds.right - f, this.contentBounds.top + f);
        canvas.scale(f5, f6);
        canvas.rotate(90.0f);
        canvas.drawPath(this.cornerShadowPath, this.cornerShadowPaint);
        if (z2) {
            canvas.scale(SHADOW_BOTTOM_SCALE / f6, SHADOW_BOTTOM_SCALE);
            canvas.drawRect(0.0f, f2, this.contentBounds.height() - f3, -this.cornerRadius, this.edgeShadowPaint);
        }
        canvas.restoreToCount(iSave5);
        canvas.restoreToCount(iSave);
    }

    private static int toEven(float f) {
        int iRound = Math.round(f);
        int i = iRound;
        if (iRound % 2 == 1) {
            i = iRound - 1;
        }
        return i;
    }

    @Override // android.support.v7.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        if (this.dirty) {
            buildComponents(getBounds());
            this.dirty = false;
        }
        drawShadow(canvas);
        super.draw(canvas);
    }

    public float getCornerRadius() {
        return this.cornerRadius;
    }

    public float getMaxShadowSize() {
        return this.rawMaxShadowSize;
    }

    public float getMinHeight() {
        float f = this.rawMaxShadowSize;
        return (Math.max(f, this.cornerRadius + ((f * SHADOW_MULTIPLIER) / 2.0f)) * 2.0f) + (this.rawMaxShadowSize * SHADOW_MULTIPLIER * 2.0f);
    }

    public float getMinWidth() {
        float f = this.rawMaxShadowSize;
        return (Math.max(f, this.cornerRadius + (f / 2.0f)) * 2.0f) + (this.rawMaxShadowSize * 2.0f);
    }

    @Override // android.support.v7.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
    public int getOpacity() {
        return -3;
    }

    @Override // android.support.v7.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
    public boolean getPadding(Rect rect) {
        int iCeil = (int) Math.ceil(calculateVerticalPadding(this.rawMaxShadowSize, this.cornerRadius, this.addPaddingForCorners));
        int iCeil2 = (int) Math.ceil(calculateHorizontalPadding(this.rawMaxShadowSize, this.cornerRadius, this.addPaddingForCorners));
        rect.set(iCeil2, iCeil, iCeil2, iCeil);
        return true;
    }

    public float getShadowSize() {
        return this.rawShadowSize;
    }

    @Override // android.support.v7.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
    protected void onBoundsChange(Rect rect) {
        this.dirty = true;
    }

    public void setAddPaddingForCorners(boolean z) {
        this.addPaddingForCorners = z;
        invalidateSelf();
    }

    @Override // android.support.v7.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
    public void setAlpha(int i) {
        super.setAlpha(i);
        this.cornerShadowPaint.setAlpha(i);
        this.edgeShadowPaint.setAlpha(i);
    }

    public void setCornerRadius(float f) {
        float fRound = Math.round(f);
        if (this.cornerRadius == fRound) {
            return;
        }
        this.cornerRadius = fRound;
        this.dirty = true;
        invalidateSelf();
    }

    public void setMaxShadowSize(float f) {
        setShadowSize(this.rawShadowSize, f);
    }

    public final void setRotation(float f) {
        if (this.rotation != f) {
            this.rotation = f;
            invalidateSelf();
        }
    }

    public void setShadowSize(float f) {
        setShadowSize(f, this.rawMaxShadowSize);
    }

    public void setShadowSize(float f, float f2) {
        if (f < 0.0f || f2 < 0.0f) {
            throw new IllegalArgumentException("invalid shadow size");
        }
        float even = toEven(f);
        float even2 = toEven(f2);
        float f3 = even;
        if (even > even2) {
            if (!this.printedShadowClipWarning) {
                this.printedShadowClipWarning = true;
            }
            f3 = even2;
        }
        if (this.rawShadowSize == f3 && this.rawMaxShadowSize == even2) {
            return;
        }
        this.rawShadowSize = f3;
        this.rawMaxShadowSize = even2;
        this.shadowSize = Math.round(f3 * SHADOW_MULTIPLIER);
        this.maxShadowSize = even2;
        this.dirty = true;
        invalidateSelf();
    }
}
