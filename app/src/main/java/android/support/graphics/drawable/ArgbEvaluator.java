package android.support.graphics.drawable;

import android.animation.TypeEvaluator;

/* loaded from: classes-dex2jar.jar:android/support/graphics/drawable/ArgbEvaluator.class */
public class ArgbEvaluator implements TypeEvaluator {
    private static final ArgbEvaluator sInstance = new ArgbEvaluator();

    public static ArgbEvaluator getInstance() {
        return sInstance;
    }

    @Override // android.animation.TypeEvaluator
    public Object evaluate(float f, Object obj, Object obj2) {
        int iIntValue = ((Integer) obj).intValue();
        float f2 = ((iIntValue >> 24) & 255) / 255.0f;
        float f3 = ((iIntValue >> 16) & 255) / 255.0f;
        float f4 = ((iIntValue >> 8) & 255) / 255.0f;
        float f5 = (iIntValue & 255) / 255.0f;
        int iIntValue2 = ((Integer) obj2).intValue();
        float f6 = ((iIntValue2 >> 24) & 255) / 255.0f;
        float f7 = ((iIntValue2 >> 16) & 255) / 255.0f;
        float f8 = ((iIntValue2 >> 8) & 255) / 255.0f;
        float f9 = (iIntValue2 & 255) / 255.0f;
        float fPow = (float) Math.pow(f3, 2.2d);
        float fPow2 = (float) Math.pow(f4, 2.2d);
        float fPow3 = (float) Math.pow(f5, 2.2d);
        float fPow4 = (float) Math.pow(f7, 2.2d);
        float fPow5 = (float) Math.pow(f8, 2.2d);
        float fPow6 = (float) Math.pow(f9, 2.2d);
        return Integer.valueOf((Math.round(((float) Math.pow(fPow + ((fPow4 - fPow) * f), 0.45454545454545453d)) * 255.0f) << 16) | (Math.round((f2 + ((f6 - f2) * f)) * 255.0f) << 24) | (Math.round(((float) Math.pow(fPow2 + ((fPow5 - fPow2) * f), 0.45454545454545453d)) * 255.0f) << 8) | Math.round(((float) Math.pow(fPow3 + (f * (fPow6 - fPow3)), 0.45454545454545453d)) * 255.0f));
    }
}
