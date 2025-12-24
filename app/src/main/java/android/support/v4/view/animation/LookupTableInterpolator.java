package android.support.v4.view.animation;

import android.view.animation.Interpolator;

/* loaded from: classes-dex2jar.jar:android/support/v4/view/animation/LookupTableInterpolator.class */
abstract class LookupTableInterpolator implements Interpolator {
    private final float mStepSize;
    private final float[] mValues;

    protected LookupTableInterpolator(float[] fArr) {
        this.mValues = fArr;
        this.mStepSize = 1.0f / (fArr.length - 1);
    }

    @Override // android.animation.TimeInterpolator
    public float getInterpolation(float f) {
        if (f >= 1.0f) {
            return 1.0f;
        }
        if (f <= 0.0f) {
            return 0.0f;
        }
        int iMin = Math.min((int) ((r0.length - 1) * f), this.mValues.length - 2);
        float f2 = iMin;
        float f3 = this.mStepSize;
        float f4 = (f - (f2 * f3)) / f3;
        float[] fArr = this.mValues;
        return fArr[iMin] + (f4 * (fArr[iMin + 1] - fArr[iMin]));
    }
}
