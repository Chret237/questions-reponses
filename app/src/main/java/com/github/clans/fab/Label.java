package com.github.clans.fab;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.animation.Animation;
import android.widget.TextView;

/* loaded from: classes-dex2jar.jar:com/github/clans/fab/Label.class */
public class Label extends TextView {
    private static final Xfermode PORTER_DUFF_CLEAR = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
    private Drawable mBackgroundDrawable;
    private int mColorNormal;
    private int mColorPressed;
    private int mColorRipple;
    private int mCornerRadius;
    private FloatingActionButton mFab;
    GestureDetector mGestureDetector;
    private boolean mHandleVisibilityChanges;
    private Animation mHideAnimation;
    private int mRawHeight;
    private int mRawWidth;
    private int mShadowColor;
    private int mShadowRadius;
    private int mShadowXOffset;
    private int mShadowYOffset;
    private Animation mShowAnimation;
    private boolean mShowShadow;
    private boolean mUsingStyle;

    /* loaded from: classes-dex2jar.jar:com/github/clans/fab/Label$Shadow.class */
    private class Shadow extends Drawable {
        private Paint mErase;
        private Paint mPaint;
        final Label this$0;

        private Shadow(Label label) {
            this.this$0 = label;
            this.mPaint = new Paint(1);
            this.mErase = new Paint(1);
            init();
        }

        private void init() {
            this.this$0.setLayerType(1, null);
            this.mPaint.setStyle(Paint.Style.FILL);
            this.mPaint.setColor(this.this$0.mColorNormal);
            this.mErase.setXfermode(Label.PORTER_DUFF_CLEAR);
            if (this.this$0.isInEditMode()) {
                return;
            }
            this.mPaint.setShadowLayer(this.this$0.mShadowRadius, this.this$0.mShadowXOffset, this.this$0.mShadowYOffset, this.this$0.mShadowColor);
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            RectF rectF = new RectF(this.this$0.mShadowRadius + Math.abs(this.this$0.mShadowXOffset), this.this$0.mShadowRadius + Math.abs(this.this$0.mShadowYOffset), this.this$0.mRawWidth, this.this$0.mRawHeight);
            canvas.drawRoundRect(rectF, this.this$0.mCornerRadius, this.this$0.mCornerRadius, this.mPaint);
            canvas.drawRoundRect(rectF, this.this$0.mCornerRadius, this.this$0.mCornerRadius, this.mErase);
        }

        @Override // android.graphics.drawable.Drawable
        public int getOpacity() {
            return 0;
        }

        @Override // android.graphics.drawable.Drawable
        public void setAlpha(int i) {
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(ColorFilter colorFilter) {
        }
    }

    public Label(Context context) {
        super(context);
        this.mShowShadow = true;
        this.mHandleVisibilityChanges = true;
        this.mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener(this) { // from class: com.github.clans.fab.Label.2
            final Label this$0;

            {
                this.this$0 = this;
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onDown(MotionEvent motionEvent) {
                this.this$0.onActionDown();
                if (this.this$0.mFab != null) {
                    this.this$0.mFab.onActionDown();
                }
                return super.onDown(motionEvent);
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                this.this$0.onActionUp();
                if (this.this$0.mFab != null) {
                    this.this$0.mFab.onActionUp();
                }
                return super.onSingleTapUp(motionEvent);
            }
        });
    }

    public Label(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mShowShadow = true;
        this.mHandleVisibilityChanges = true;
        this.mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener(this) { // from class: com.github.clans.fab.Label.2
            final Label this$0;

            {
                this.this$0 = this;
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onDown(MotionEvent motionEvent) {
                this.this$0.onActionDown();
                if (this.this$0.mFab != null) {
                    this.this$0.mFab.onActionDown();
                }
                return super.onDown(motionEvent);
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                this.this$0.onActionUp();
                if (this.this$0.mFab != null) {
                    this.this$0.mFab.onActionUp();
                }
                return super.onSingleTapUp(motionEvent);
            }
        });
    }

    public Label(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mShowShadow = true;
        this.mHandleVisibilityChanges = true;
        this.mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener(this) { // from class: com.github.clans.fab.Label.2
            final Label this$0;

            {
                this.this$0 = this;
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onDown(MotionEvent motionEvent) {
                this.this$0.onActionDown();
                if (this.this$0.mFab != null) {
                    this.this$0.mFab.onActionDown();
                }
                return super.onDown(motionEvent);
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                this.this$0.onActionUp();
                if (this.this$0.mFab != null) {
                    this.this$0.mFab.onActionUp();
                }
                return super.onSingleTapUp(motionEvent);
            }
        });
    }

    private int calculateMeasuredHeight() {
        if (this.mRawHeight == 0) {
            this.mRawHeight = getMeasuredHeight();
        }
        return getMeasuredHeight() + calculateShadowHeight();
    }

    private int calculateMeasuredWidth() {
        if (this.mRawWidth == 0) {
            this.mRawWidth = getMeasuredWidth();
        }
        return getMeasuredWidth() + calculateShadowWidth();
    }

    /* JADX WARN: Type inference failed for: r4v2, types: [int[], int[][]] */
    private Drawable createFillDrawable() {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, createRectDrawable(this.mColorPressed));
        stateListDrawable.addState(new int[0], createRectDrawable(this.mColorNormal));
        if (!Util.hasLollipop()) {
            this.mBackgroundDrawable = stateListDrawable;
            return stateListDrawable;
        }
        RippleDrawable rippleDrawable = new RippleDrawable(new ColorStateList(new int[]{new int[0]}, new int[]{this.mColorRipple}), stateListDrawable, null);
        setOutlineProvider(new ViewOutlineProvider(this) { // from class: com.github.clans.fab.Label.1
            final Label this$0;

            {
                this.this$0 = this;
            }

            @Override // android.view.ViewOutlineProvider
            public void getOutline(View view, Outline outline) {
                outline.setOval(0, 0, view.getWidth(), view.getHeight());
            }
        });
        setClipToOutline(true);
        this.mBackgroundDrawable = rippleDrawable;
        return rippleDrawable;
    }

    private Drawable createRectDrawable(int i) {
        int i2 = this.mCornerRadius;
        ShapeDrawable shapeDrawable = new ShapeDrawable(new RoundRectShape(new float[]{i2, i2, i2, i2, i2, i2, i2, i2}, null, null));
        shapeDrawable.getPaint().setColor(i);
        return shapeDrawable;
    }

    private void playHideAnimation() {
        if (this.mHideAnimation != null) {
            this.mShowAnimation.cancel();
            startAnimation(this.mHideAnimation);
        }
    }

    private void playShowAnimation() {
        if (this.mShowAnimation != null) {
            this.mHideAnimation.cancel();
            startAnimation(this.mShowAnimation);
        }
    }

    private void setBackgroundCompat(Drawable drawable) {
        if (Util.hasJellyBean()) {
            setBackground(drawable);
        } else {
            setBackgroundDrawable(drawable);
        }
    }

    private void setShadow(FloatingActionButton floatingActionButton) {
        this.mShadowColor = floatingActionButton.getShadowColor();
        this.mShadowRadius = floatingActionButton.getShadowRadius();
        this.mShadowXOffset = floatingActionButton.getShadowXOffset();
        this.mShadowYOffset = floatingActionButton.getShadowYOffset();
        this.mShowShadow = floatingActionButton.hasShadow();
    }

    int calculateShadowHeight() {
        return this.mShowShadow ? this.mShadowRadius + Math.abs(this.mShadowYOffset) : 0;
    }

    int calculateShadowWidth() {
        return this.mShowShadow ? this.mShadowRadius + Math.abs(this.mShadowXOffset) : 0;
    }

    void hide(boolean z) {
        if (z) {
            playHideAnimation();
        }
        setVisibility(4);
    }

    boolean isHandleVisibilityChanges() {
        return this.mHandleVisibilityChanges;
    }

    void onActionDown() {
        if (this.mUsingStyle) {
            this.mBackgroundDrawable = getBackground();
        }
        Drawable drawable = this.mBackgroundDrawable;
        if (drawable instanceof StateListDrawable) {
            ((StateListDrawable) drawable).setState(new int[]{android.R.attr.state_pressed});
            return;
        }
        if (Util.hasLollipop()) {
            Drawable drawable2 = this.mBackgroundDrawable;
            if (drawable2 instanceof RippleDrawable) {
                RippleDrawable rippleDrawable = (RippleDrawable) drawable2;
                rippleDrawable.setState(new int[]{android.R.attr.state_enabled, android.R.attr.state_pressed});
                rippleDrawable.setHotspot(getMeasuredWidth() / 2, getMeasuredHeight() / 2);
                rippleDrawable.setVisible(true, true);
            }
        }
    }

    void onActionUp() {
        if (this.mUsingStyle) {
            this.mBackgroundDrawable = getBackground();
        }
        Drawable drawable = this.mBackgroundDrawable;
        if (drawable instanceof StateListDrawable) {
            ((StateListDrawable) drawable).setState(new int[0]);
            return;
        }
        if (Util.hasLollipop()) {
            Drawable drawable2 = this.mBackgroundDrawable;
            if (drawable2 instanceof RippleDrawable) {
                RippleDrawable rippleDrawable = (RippleDrawable) drawable2;
                rippleDrawable.setState(new int[0]);
                rippleDrawable.setHotspot(getMeasuredWidth() / 2, getMeasuredHeight() / 2);
                rippleDrawable.setVisible(true, true);
            }
        }
    }

    @Override // android.widget.TextView, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        setMeasuredDimension(calculateMeasuredWidth(), calculateMeasuredHeight());
    }

    @Override // android.widget.TextView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        FloatingActionButton floatingActionButton = this.mFab;
        if (floatingActionButton == null || floatingActionButton.getOnClickListener() == null || !this.mFab.isEnabled()) {
            return super.onTouchEvent(motionEvent);
        }
        int action = motionEvent.getAction();
        if (action == 1 || action == 3) {
            onActionUp();
            this.mFab.onActionUp();
        }
        this.mGestureDetector.onTouchEvent(motionEvent);
        return super.onTouchEvent(motionEvent);
    }

    void setColors(int i, int i2, int i3) {
        this.mColorNormal = i;
        this.mColorPressed = i2;
        this.mColorRipple = i3;
    }

    void setCornerRadius(int i) {
        this.mCornerRadius = i;
    }

    void setFab(FloatingActionButton floatingActionButton) {
        this.mFab = floatingActionButton;
        setShadow(floatingActionButton);
    }

    void setHandleVisibilityChanges(boolean z) {
        this.mHandleVisibilityChanges = z;
    }

    void setHideAnimation(Animation animation) {
        this.mHideAnimation = animation;
    }

    void setShowAnimation(Animation animation) {
        this.mShowAnimation = animation;
    }

    void setShowShadow(boolean z) {
        this.mShowShadow = z;
    }

    void setUsingStyle(boolean z) {
        this.mUsingStyle = z;
    }

    void show(boolean z) {
        if (z) {
            playShowAnimation();
        }
        setVisibility(0);
    }

    void updateBackground() {
        LayerDrawable layerDrawable;
        if (this.mShowShadow) {
            layerDrawable = new LayerDrawable(new Drawable[]{new Shadow(), createFillDrawable()});
            layerDrawable.setLayerInset(1, this.mShadowRadius + Math.abs(this.mShadowXOffset), this.mShadowRadius + Math.abs(this.mShadowYOffset), this.mShadowRadius + Math.abs(this.mShadowXOffset), this.mShadowRadius + Math.abs(this.mShadowYOffset));
        } else {
            layerDrawable = new LayerDrawable(new Drawable[]{createFillDrawable()});
        }
        setBackgroundCompat(layerDrawable);
    }
}
