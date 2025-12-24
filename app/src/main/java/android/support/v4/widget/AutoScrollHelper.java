package android.support.v4.widget;

import android.content.res.Resources;
import android.os.SystemClock;
import android.support.v4.view.ViewCompat;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

/* loaded from: classes-dex2jar.jar:android/support/v4/widget/AutoScrollHelper.class */
public abstract class AutoScrollHelper implements View.OnTouchListener {
    private static final int DEFAULT_ACTIVATION_DELAY = ViewConfiguration.getTapTimeout();
    private static final int DEFAULT_EDGE_TYPE = 1;
    private static final float DEFAULT_MAXIMUM_EDGE = Float.MAX_VALUE;
    private static final int DEFAULT_MAXIMUM_VELOCITY_DIPS = 1575;
    private static final int DEFAULT_MINIMUM_VELOCITY_DIPS = 315;
    private static final int DEFAULT_RAMP_DOWN_DURATION = 500;
    private static final int DEFAULT_RAMP_UP_DURATION = 500;
    private static final float DEFAULT_RELATIVE_EDGE = 0.2f;
    private static final float DEFAULT_RELATIVE_VELOCITY = 1.0f;
    public static final int EDGE_TYPE_INSIDE = 0;
    public static final int EDGE_TYPE_INSIDE_EXTEND = 1;
    public static final int EDGE_TYPE_OUTSIDE = 2;
    private static final int HORIZONTAL = 0;
    public static final float NO_MAX = Float.MAX_VALUE;
    public static final float NO_MIN = 0.0f;
    public static final float RELATIVE_UNSPECIFIED = 0.0f;
    private static final int VERTICAL = 1;
    private int mActivationDelay;
    private boolean mAlreadyDelayed;
    boolean mAnimating;
    private int mEdgeType;
    private boolean mEnabled;
    private boolean mExclusive;
    boolean mNeedsCancel;
    boolean mNeedsReset;
    private Runnable mRunnable;
    final View mTarget;
    final ClampedScroller mScroller = new ClampedScroller();
    private final Interpolator mEdgeInterpolator = new AccelerateInterpolator();
    private float[] mRelativeEdges = {0.0f, 0.0f};
    private float[] mMaximumEdges = {Float.MAX_VALUE, Float.MAX_VALUE};
    private float[] mRelativeVelocity = {0.0f, 0.0f};
    private float[] mMinimumVelocity = {0.0f, 0.0f};
    private float[] mMaximumVelocity = {Float.MAX_VALUE, Float.MAX_VALUE};

    /* loaded from: classes-dex2jar.jar:android/support/v4/widget/AutoScrollHelper$ClampedScroller.class */
    private static class ClampedScroller {
        private int mEffectiveRampDown;
        private int mRampDownDuration;
        private int mRampUpDuration;
        private float mStopValue;
        private float mTargetVelocityX;
        private float mTargetVelocityY;
        private long mStartTime = Long.MIN_VALUE;
        private long mStopTime = -1;
        private long mDeltaTime = 0;
        private int mDeltaX = 0;
        private int mDeltaY = 0;

        ClampedScroller() {
        }

        private float getValueAt(long j) {
            if (j < this.mStartTime) {
                return 0.0f;
            }
            long j2 = this.mStopTime;
            if (j2 < 0 || j < j2) {
                return AutoScrollHelper.constrain((j - this.mStartTime) / this.mRampUpDuration, 0.0f, AutoScrollHelper.DEFAULT_RELATIVE_VELOCITY) * 0.5f;
            }
            float f = this.mStopValue;
            return (AutoScrollHelper.DEFAULT_RELATIVE_VELOCITY - f) + (f * AutoScrollHelper.constrain((j - j2) / this.mEffectiveRampDown, 0.0f, AutoScrollHelper.DEFAULT_RELATIVE_VELOCITY));
        }

        private float interpolateValue(float f) {
            return ((-4.0f) * f * f) + (f * 4.0f);
        }

        public void computeScrollDelta() {
            if (this.mDeltaTime == 0) {
                throw new RuntimeException("Cannot compute scroll delta before calling start()");
            }
            long jCurrentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis();
            float fInterpolateValue = interpolateValue(getValueAt(jCurrentAnimationTimeMillis));
            long j = this.mDeltaTime;
            this.mDeltaTime = jCurrentAnimationTimeMillis;
            float f = (jCurrentAnimationTimeMillis - j) * fInterpolateValue;
            this.mDeltaX = (int) (this.mTargetVelocityX * f);
            this.mDeltaY = (int) (f * this.mTargetVelocityY);
        }

        public int getDeltaX() {
            return this.mDeltaX;
        }

        public int getDeltaY() {
            return this.mDeltaY;
        }

        public int getHorizontalDirection() {
            float f = this.mTargetVelocityX;
            return (int) (f / Math.abs(f));
        }

        public int getVerticalDirection() {
            float f = this.mTargetVelocityY;
            return (int) (f / Math.abs(f));
        }

        public boolean isFinished() {
            return this.mStopTime > 0 && AnimationUtils.currentAnimationTimeMillis() > this.mStopTime + ((long) this.mEffectiveRampDown);
        }

        public void requestStop() {
            long jCurrentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis();
            this.mEffectiveRampDown = AutoScrollHelper.constrain((int) (jCurrentAnimationTimeMillis - this.mStartTime), 0, this.mRampDownDuration);
            this.mStopValue = getValueAt(jCurrentAnimationTimeMillis);
            this.mStopTime = jCurrentAnimationTimeMillis;
        }

        public void setRampDownDuration(int i) {
            this.mRampDownDuration = i;
        }

        public void setRampUpDuration(int i) {
            this.mRampUpDuration = i;
        }

        public void setTargetVelocity(float f, float f2) {
            this.mTargetVelocityX = f;
            this.mTargetVelocityY = f2;
        }

        public void start() {
            long jCurrentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis();
            this.mStartTime = jCurrentAnimationTimeMillis;
            this.mStopTime = -1L;
            this.mDeltaTime = jCurrentAnimationTimeMillis;
            this.mStopValue = 0.5f;
            this.mDeltaX = 0;
            this.mDeltaY = 0;
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v4/widget/AutoScrollHelper$ScrollAnimationRunnable.class */
    private class ScrollAnimationRunnable implements Runnable {
        final AutoScrollHelper this$0;

        ScrollAnimationRunnable(AutoScrollHelper autoScrollHelper) {
            this.this$0 = autoScrollHelper;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.this$0.mAnimating) {
                if (this.this$0.mNeedsReset) {
                    this.this$0.mNeedsReset = false;
                    this.this$0.mScroller.start();
                }
                ClampedScroller clampedScroller = this.this$0.mScroller;
                if (clampedScroller.isFinished() || !this.this$0.shouldAnimate()) {
                    this.this$0.mAnimating = false;
                    return;
                }
                if (this.this$0.mNeedsCancel) {
                    this.this$0.mNeedsCancel = false;
                    this.this$0.cancelTargetTouch();
                }
                clampedScroller.computeScrollDelta();
                this.this$0.scrollTargetBy(clampedScroller.getDeltaX(), clampedScroller.getDeltaY());
                ViewCompat.postOnAnimation(this.this$0.mTarget, this);
            }
        }
    }

    public AutoScrollHelper(View view) {
        this.mTarget = view;
        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        int i = (int) ((displayMetrics.density * 1575.0f) + 0.5f);
        int i2 = (int) ((displayMetrics.density * 315.0f) + 0.5f);
        float f = i;
        setMaximumVelocity(f, f);
        float f2 = i2;
        setMinimumVelocity(f2, f2);
        setEdgeType(1);
        setMaximumEdges(Float.MAX_VALUE, Float.MAX_VALUE);
        setRelativeEdges(DEFAULT_RELATIVE_EDGE, DEFAULT_RELATIVE_EDGE);
        setRelativeVelocity(DEFAULT_RELATIVE_VELOCITY, DEFAULT_RELATIVE_VELOCITY);
        setActivationDelay(DEFAULT_ACTIVATION_DELAY);
        setRampUpDuration(500);
        setRampDownDuration(500);
    }

    private float computeTargetVelocity(int i, float f, float f2, float f3) {
        float edgeValue = getEdgeValue(this.mRelativeEdges[i], f2, this.mMaximumEdges[i], f);
        if (edgeValue == 0.0f) {
            return 0.0f;
        }
        float f4 = this.mRelativeVelocity[i];
        float f5 = this.mMinimumVelocity[i];
        float f6 = this.mMaximumVelocity[i];
        float f7 = f4 * f3;
        return edgeValue > 0.0f ? constrain(edgeValue * f7, f5, f6) : -constrain((-edgeValue) * f7, f5, f6);
    }

    static float constrain(float f, float f2, float f3) {
        return f > f3 ? f3 : f < f2 ? f2 : f;
    }

    static int constrain(int i, int i2, int i3) {
        return i > i3 ? i3 : i < i2 ? i2 : i;
    }

    private float constrainEdgeValue(float f, float f2) {
        if (f2 == 0.0f) {
            return 0.0f;
        }
        int i = this.mEdgeType;
        if (i != 0 && i != 1) {
            if (i == 2 && f < 0.0f) {
                return f / (-f2);
            }
            return 0.0f;
        }
        if (f >= f2) {
            return 0.0f;
        }
        if (f >= 0.0f) {
            return DEFAULT_RELATIVE_VELOCITY - (f / f2);
        }
        if (this.mAnimating && this.mEdgeType == 1) {
            return DEFAULT_RELATIVE_VELOCITY;
        }
        return 0.0f;
    }

    private float getEdgeValue(float f, float f2, float f3, float f4) {
        float interpolation;
        float fConstrain = constrain(f * f2, 0.0f, f3);
        float fConstrainEdgeValue = constrainEdgeValue(f2 - f4, fConstrain) - constrainEdgeValue(f4, fConstrain);
        if (fConstrainEdgeValue < 0.0f) {
            interpolation = -this.mEdgeInterpolator.getInterpolation(-fConstrainEdgeValue);
        } else {
            if (fConstrainEdgeValue <= 0.0f) {
                return 0.0f;
            }
            interpolation = this.mEdgeInterpolator.getInterpolation(fConstrainEdgeValue);
        }
        return constrain(interpolation, -1.0f, DEFAULT_RELATIVE_VELOCITY);
    }

    private void requestStop() {
        if (this.mNeedsReset) {
            this.mAnimating = false;
        } else {
            this.mScroller.requestStop();
        }
    }

    private void startAnimating() {
        int i;
        if (this.mRunnable == null) {
            this.mRunnable = new ScrollAnimationRunnable(this);
        }
        this.mAnimating = true;
        this.mNeedsReset = true;
        if (this.mAlreadyDelayed || (i = this.mActivationDelay) <= 0) {
            this.mRunnable.run();
        } else {
            ViewCompat.postOnAnimationDelayed(this.mTarget, this.mRunnable, i);
        }
        this.mAlreadyDelayed = true;
    }

    public abstract boolean canTargetScrollHorizontally(int i);

    public abstract boolean canTargetScrollVertically(int i);

    void cancelTargetTouch() {
        long jUptimeMillis = SystemClock.uptimeMillis();
        MotionEvent motionEventObtain = MotionEvent.obtain(jUptimeMillis, jUptimeMillis, 3, 0.0f, 0.0f, 0);
        this.mTarget.onTouchEvent(motionEventObtain);
        motionEventObtain.recycle();
    }

    public boolean isEnabled() {
        return this.mEnabled;
    }

    public boolean isExclusive() {
        return this.mExclusive;
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0030  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0097  */
    @Override // android.view.View.OnTouchListener
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean onTouch(android.view.View r7, android.view.MotionEvent r8) {
        /*
            r6 = this;
            r0 = r6
            boolean r0 = r0.mEnabled
            r12 = r0
            r0 = 0
            r13 = r0
            r0 = r12
            if (r0 != 0) goto L10
            r0 = 0
            return r0
        L10:
            r0 = r8
            int r0 = r0.getActionMasked()
            r11 = r0
            r0 = r11
            if (r0 == 0) goto L37
            r0 = r11
            r1 = 1
            if (r0 == r1) goto L30
            r0 = r11
            r1 = 2
            if (r0 == r1) goto L41
            r0 = r11
            r1 = 3
            if (r0 == r1) goto L30
            goto L8c
        L30:
            r0 = r6
            r0.requestStop()
            goto L8c
        L37:
            r0 = r6
            r1 = 1
            r0.mNeedsCancel = r1
            r0 = r6
            r1 = 0
            r0.mAlreadyDelayed = r1
        L41:
            r0 = r6
            r1 = 0
            r2 = r8
            float r2 = r2.getX()
            r3 = r7
            int r3 = r3.getWidth()
            float r3 = (float) r3
            r4 = r6
            android.view.View r4 = r4.mTarget
            int r4 = r4.getWidth()
            float r4 = (float) r4
            float r0 = r0.computeTargetVelocity(r1, r2, r3, r4)
            r9 = r0
            r0 = r6
            r1 = 1
            r2 = r8
            float r2 = r2.getY()
            r3 = r7
            int r3 = r3.getHeight()
            float r3 = (float) r3
            r4 = r6
            android.view.View r4 = r4.mTarget
            int r4 = r4.getHeight()
            float r4 = (float) r4
            float r0 = r0.computeTargetVelocity(r1, r2, r3, r4)
            r10 = r0
            r0 = r6
            android.support.v4.widget.AutoScrollHelper$ClampedScroller r0 = r0.mScroller
            r1 = r9
            r2 = r10
            r0.setTargetVelocity(r1, r2)
            r0 = r6
            boolean r0 = r0.mAnimating
            if (r0 != 0) goto L8c
            r0 = r6
            boolean r0 = r0.shouldAnimate()
            if (r0 == 0) goto L8c
            r0 = r6
            r0.startAnimating()
        L8c:
            r0 = r13
            r12 = r0
            r0 = r6
            boolean r0 = r0.mExclusive
            if (r0 == 0) goto La5
            r0 = r13
            r12 = r0
            r0 = r6
            boolean r0 = r0.mAnimating
            if (r0 == 0) goto La5
            r0 = 1
            r12 = r0
        La5:
            r0 = r12
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.widget.AutoScrollHelper.onTouch(android.view.View, android.view.MotionEvent):boolean");
    }

    public abstract void scrollTargetBy(int i, int i2);

    public AutoScrollHelper setActivationDelay(int i) {
        this.mActivationDelay = i;
        return this;
    }

    public AutoScrollHelper setEdgeType(int i) {
        this.mEdgeType = i;
        return this;
    }

    public AutoScrollHelper setEnabled(boolean z) {
        if (this.mEnabled && !z) {
            requestStop();
        }
        this.mEnabled = z;
        return this;
    }

    public AutoScrollHelper setExclusive(boolean z) {
        this.mExclusive = z;
        return this;
    }

    public AutoScrollHelper setMaximumEdges(float f, float f2) {
        float[] fArr = this.mMaximumEdges;
        fArr[0] = f;
        fArr[1] = f2;
        return this;
    }

    public AutoScrollHelper setMaximumVelocity(float f, float f2) {
        float[] fArr = this.mMaximumVelocity;
        fArr[0] = f / 1000.0f;
        fArr[1] = f2 / 1000.0f;
        return this;
    }

    public AutoScrollHelper setMinimumVelocity(float f, float f2) {
        float[] fArr = this.mMinimumVelocity;
        fArr[0] = f / 1000.0f;
        fArr[1] = f2 / 1000.0f;
        return this;
    }

    public AutoScrollHelper setRampDownDuration(int i) {
        this.mScroller.setRampDownDuration(i);
        return this;
    }

    public AutoScrollHelper setRampUpDuration(int i) {
        this.mScroller.setRampUpDuration(i);
        return this;
    }

    public AutoScrollHelper setRelativeEdges(float f, float f2) {
        float[] fArr = this.mRelativeEdges;
        fArr[0] = f;
        fArr[1] = f2;
        return this;
    }

    public AutoScrollHelper setRelativeVelocity(float f, float f2) {
        float[] fArr = this.mRelativeVelocity;
        fArr[0] = f / 1000.0f;
        fArr[1] = f2 / 1000.0f;
        return this;
    }

    boolean shouldAnimate() {
        ClampedScroller clampedScroller = this.mScroller;
        int verticalDirection = clampedScroller.getVerticalDirection();
        int horizontalDirection = clampedScroller.getHorizontalDirection();
        return (verticalDirection != 0 && canTargetScrollVertically(verticalDirection)) || (horizontalDirection != 0 && canTargetScrollHorizontally(horizontalDirection));
    }
}
