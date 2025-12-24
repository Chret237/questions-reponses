package android.support.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.util.Property;
import android.view.View;

/* loaded from: classes-dex2jar.jar:android/support/transition/TranslationAnimationCreator.class */
class TranslationAnimationCreator {

    /* loaded from: classes-dex2jar.jar:android/support/transition/TranslationAnimationCreator$TransitionPositionListener.class */
    private static class TransitionPositionListener extends AnimatorListenerAdapter {
        private final View mMovingView;
        private float mPausedX;
        private float mPausedY;
        private final int mStartX;
        private final int mStartY;
        private final float mTerminalX;
        private final float mTerminalY;
        private int[] mTransitionPosition;
        private final View mViewInHierarchy;

        TransitionPositionListener(View view, View view2, int i, int i2, float f, float f2) {
            this.mMovingView = view;
            this.mViewInHierarchy = view2;
            this.mStartX = i - Math.round(view.getTranslationX());
            this.mStartY = i2 - Math.round(this.mMovingView.getTranslationY());
            this.mTerminalX = f;
            this.mTerminalY = f2;
            int[] iArr = (int[]) this.mViewInHierarchy.getTag(C0149R.id.transition_position);
            this.mTransitionPosition = iArr;
            if (iArr != null) {
                this.mViewInHierarchy.setTag(C0149R.id.transition_position, null);
            }
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            if (this.mTransitionPosition == null) {
                this.mTransitionPosition = new int[2];
            }
            this.mTransitionPosition[0] = Math.round(this.mStartX + this.mMovingView.getTranslationX());
            this.mTransitionPosition[1] = Math.round(this.mStartY + this.mMovingView.getTranslationY());
            this.mViewInHierarchy.setTag(C0149R.id.transition_position, this.mTransitionPosition);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            this.mMovingView.setTranslationX(this.mTerminalX);
            this.mMovingView.setTranslationY(this.mTerminalY);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorPauseListener
        public void onAnimationPause(Animator animator) {
            this.mPausedX = this.mMovingView.getTranslationX();
            this.mPausedY = this.mMovingView.getTranslationY();
            this.mMovingView.setTranslationX(this.mTerminalX);
            this.mMovingView.setTranslationY(this.mTerminalY);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorPauseListener
        public void onAnimationResume(Animator animator) {
            this.mMovingView.setTranslationX(this.mPausedX);
            this.mMovingView.setTranslationY(this.mPausedY);
        }
    }

    private TranslationAnimationCreator() {
    }

    static Animator createAnimation(View view, TransitionValues transitionValues, int i, int i2, float f, float f2, float f3, float f4, TimeInterpolator timeInterpolator) {
        float translationX = view.getTranslationX();
        float translationY = view.getTranslationY();
        if (((int[]) transitionValues.view.getTag(C0149R.id.transition_position)) != null) {
            f = (r0[0] - i) + translationX;
            f2 = (r0[1] - i2) + translationY;
        }
        int iRound = Math.round(f - translationX);
        int iRound2 = Math.round(f2 - translationY);
        view.setTranslationX(f);
        view.setTranslationY(f2);
        if (f == f3 && f2 == f4) {
            return null;
        }
        ObjectAnimator objectAnimatorOfPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(view, PropertyValuesHolder.ofFloat((Property<?, Float>) View.TRANSLATION_X, f, f3), PropertyValuesHolder.ofFloat((Property<?, Float>) View.TRANSLATION_Y, f2, f4));
        TransitionPositionListener transitionPositionListener = new TransitionPositionListener(view, transitionValues.view, i + iRound, i2 + iRound2, translationX, translationY);
        objectAnimatorOfPropertyValuesHolder.addListener(transitionPositionListener);
        AnimatorUtils.addPauseListener(objectAnimatorOfPropertyValuesHolder, transitionPositionListener);
        objectAnimatorOfPropertyValuesHolder.setInterpolator(timeInterpolator);
        return objectAnimatorOfPropertyValuesHolder;
    }
}
