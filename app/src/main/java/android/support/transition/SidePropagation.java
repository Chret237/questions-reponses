package android.support.transition;

import android.graphics.Rect;
import android.view.ViewGroup;

/* loaded from: classes-dex2jar.jar:android/support/transition/SidePropagation.class */
public class SidePropagation extends VisibilityPropagation {
    private float mPropagationSpeed = 3.0f;
    private int mSide = 80;

    /* JADX WARN: Removed duplicated region for block: B:10:0x0029  */
    /* JADX WARN: Removed duplicated region for block: B:11:0x002f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private int distance(android.view.View r5, int r6, int r7, int r8, int r9, int r10, int r11, int r12, int r13) {
        /*
            r4 = this;
            r0 = r4
            int r0 = r0.mSide
            r16 = r0
            r0 = 0
            r15 = r0
            r0 = 1
            r17 = r0
            r0 = 1
            r14 = r0
            r0 = r16
            r1 = 8388611(0x800003, float:1.1754948E-38)
            if (r0 != r1) goto L35
            r0 = r5
            int r0 = android.support.v4.view.ViewCompat.getLayoutDirection(r0)
            r1 = 1
            if (r0 != r1) goto L21
            goto L24
        L21:
            r0 = 0
            r14 = r0
        L24:
            r0 = r14
            if (r0 == 0) goto L2f
        L29:
            r0 = 5
            r14 = r0
            goto L5a
        L2f:
            r0 = 3
            r14 = r0
            goto L5a
        L35:
            r0 = r16
            r14 = r0
            r0 = r16
            r1 = 8388613(0x800005, float:1.175495E-38)
            if (r0 != r1) goto L5a
            r0 = r5
            int r0 = android.support.v4.view.ViewCompat.getLayoutDirection(r0)
            r1 = 1
            if (r0 != r1) goto L4f
            r0 = r17
            r14 = r0
            goto L52
        L4f:
            r0 = 0
            r14 = r0
        L52:
            r0 = r14
            if (r0 == 0) goto L29
            goto L2f
        L5a:
            r0 = r14
            r1 = 3
            if (r0 == r1) goto Laa
            r0 = r14
            r1 = 5
            if (r0 == r1) goto L9a
            r0 = r14
            r1 = 48
            if (r0 == r1) goto L8a
            r0 = r14
            r1 = 80
            if (r0 == r1) goto L7a
            r0 = r15
            r6 = r0
            goto Lb7
        L7a:
            r0 = r7
            r1 = r11
            int r0 = r0 - r1
            r1 = r8
            r2 = r6
            int r1 = r1 - r2
            int r1 = java.lang.Math.abs(r1)
            int r0 = r0 + r1
            r6 = r0
            goto Lb7
        L8a:
            r0 = r13
            r1 = r7
            int r0 = r0 - r1
            r1 = r8
            r2 = r6
            int r1 = r1 - r2
            int r1 = java.lang.Math.abs(r1)
            int r0 = r0 + r1
            r6 = r0
            goto Lb7
        L9a:
            r0 = r6
            r1 = r10
            int r0 = r0 - r1
            r1 = r9
            r2 = r7
            int r1 = r1 - r2
            int r1 = java.lang.Math.abs(r1)
            int r0 = r0 + r1
            r6 = r0
            goto Lb7
        Laa:
            r0 = r12
            r1 = r6
            int r0 = r0 - r1
            r1 = r9
            r2 = r7
            int r1 = r1 - r2
            int r1 = java.lang.Math.abs(r1)
            int r0 = r0 + r1
            r6 = r0
        Lb7:
            r0 = r6
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.transition.SidePropagation.distance(android.view.View, int, int, int, int, int, int, int, int):int");
    }

    private int getMaxDistance(ViewGroup viewGroup) {
        int i = this.mSide;
        return (i == 3 || i == 5 || i == 8388611 || i == 8388613) ? viewGroup.getWidth() : viewGroup.getHeight();
    }

    @Override // android.support.transition.TransitionPropagation
    public long getStartDelay(ViewGroup viewGroup, Transition transition, TransitionValues transitionValues, TransitionValues transitionValues2) {
        int i;
        int iCenterX;
        int iCenterY;
        if (transitionValues == null && transitionValues2 == null) {
            return 0L;
        }
        Rect epicenter = transition.getEpicenter();
        if (transitionValues2 == null || getViewVisibility(transitionValues) == 0) {
            i = -1;
        } else {
            transitionValues = transitionValues2;
            i = 1;
        }
        int viewX = getViewX(transitionValues);
        int viewY = getViewY(transitionValues);
        int[] iArr = new int[2];
        viewGroup.getLocationOnScreen(iArr);
        int iRound = iArr[0] + Math.round(viewGroup.getTranslationX());
        int iRound2 = iArr[1] + Math.round(viewGroup.getTranslationY());
        int width = iRound + viewGroup.getWidth();
        int height = iRound2 + viewGroup.getHeight();
        if (epicenter != null) {
            iCenterX = epicenter.centerX();
            iCenterY = epicenter.centerY();
        } else {
            iCenterX = (iRound + width) / 2;
            iCenterY = (iRound2 + height) / 2;
        }
        float fDistance = distance(viewGroup, viewX, viewY, iCenterX, iCenterY, iRound, iRound2, width, height) / getMaxDistance(viewGroup);
        long duration = transition.getDuration();
        long j = duration;
        if (duration < 0) {
            j = 300;
        }
        return Math.round(((j * i) / this.mPropagationSpeed) * fDistance);
    }

    public void setPropagationSpeed(float f) {
        if (f == 0.0f) {
            throw new IllegalArgumentException("propagationSpeed may not be 0");
        }
        this.mPropagationSpeed = f;
    }

    public void setSide(int i) {
        this.mSide = i;
    }
}
