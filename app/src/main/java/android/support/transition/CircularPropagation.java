package android.support.transition;

import android.graphics.Rect;
import android.view.ViewGroup;

/* loaded from: classes-dex2jar.jar:android/support/transition/CircularPropagation.class */
public class CircularPropagation extends VisibilityPropagation {
    private float mPropagationSpeed = 3.0f;

    private static float distance(float f, float f2, float f3, float f4) {
        float f5 = f3 - f;
        float f6 = f4 - f2;
        return (float) Math.sqrt((f5 * f5) + (f6 * f6));
    }

    @Override // android.support.transition.TransitionPropagation
    public long getStartDelay(ViewGroup viewGroup, Transition transition, TransitionValues transitionValues, TransitionValues transitionValues2) {
        int i;
        int iRound;
        int iRound2;
        if (transitionValues == null && transitionValues2 == null) {
            return 0L;
        }
        if (transitionValues2 == null || getViewVisibility(transitionValues) == 0) {
            i = -1;
        } else {
            i = 1;
            transitionValues = transitionValues2;
        }
        int viewX = getViewX(transitionValues);
        int viewY = getViewY(transitionValues);
        Rect epicenter = transition.getEpicenter();
        if (epicenter != null) {
            iRound = epicenter.centerX();
            iRound2 = epicenter.centerY();
        } else {
            viewGroup.getLocationOnScreen(new int[2]);
            iRound = Math.round(r0[0] + (viewGroup.getWidth() / 2) + viewGroup.getTranslationX());
            iRound2 = Math.round(r0[1] + (viewGroup.getHeight() / 2) + viewGroup.getTranslationY());
        }
        float fDistance = distance(viewX, viewY, iRound, iRound2) / distance(0.0f, 0.0f, viewGroup.getWidth(), viewGroup.getHeight());
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
}
