package android.support.design.internal;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.support.transition.Transition;
import android.support.transition.TransitionValues;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.Map;

/* loaded from: classes-dex2jar.jar:android/support/design/internal/TextScale.class */
public class TextScale extends Transition {
    private static final String PROPNAME_SCALE = "android:textscale:scale";

    private void captureValues(TransitionValues transitionValues) {
        if (transitionValues.view instanceof TextView) {
            transitionValues.values.put(PROPNAME_SCALE, Float.valueOf(((TextView) transitionValues.view).getScaleX()));
        }
    }

    @Override // android.support.transition.Transition
    public void captureEndValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override // android.support.transition.Transition
    public void captureStartValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override // android.support.transition.Transition
    public Animator createAnimator(ViewGroup viewGroup, TransitionValues transitionValues, TransitionValues transitionValues2) {
        ValueAnimator valueAnimatorOfFloat = null;
        if (transitionValues != null) {
            valueAnimatorOfFloat = null;
            if (transitionValues2 != null) {
                valueAnimatorOfFloat = null;
                if (transitionValues.view instanceof TextView) {
                    if (transitionValues2.view instanceof TextView) {
                        TextView textView = (TextView) transitionValues2.view;
                        Map<String, Object> map = transitionValues.values;
                        Map<String, Object> map2 = transitionValues2.values;
                        float fFloatValue = 1.0f;
                        float fFloatValue2 = map.get(PROPNAME_SCALE) != null ? ((Float) map.get(PROPNAME_SCALE)).floatValue() : 1.0f;
                        if (map2.get(PROPNAME_SCALE) != null) {
                            fFloatValue = ((Float) map2.get(PROPNAME_SCALE)).floatValue();
                        }
                        if (fFloatValue2 == fFloatValue) {
                            return null;
                        }
                        valueAnimatorOfFloat = ValueAnimator.ofFloat(fFloatValue2, fFloatValue);
                        valueAnimatorOfFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(this, textView) { // from class: android.support.design.internal.TextScale.1
                            final TextScale this$0;
                            final TextView val$view;

                            {
                                this.this$0 = this;
                                this.val$view = textView;
                            }

                            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                float fFloatValue3 = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                                this.val$view.setScaleX(fFloatValue3);
                                this.val$view.setScaleY(fFloatValue3);
                            }
                        });
                    } else {
                        valueAnimatorOfFloat = null;
                    }
                }
            }
        }
        return valueAnimatorOfFloat;
    }
}
