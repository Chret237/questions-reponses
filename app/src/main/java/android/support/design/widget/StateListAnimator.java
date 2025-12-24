package android.support.design.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.util.StateSet;
import java.util.ArrayList;

/* loaded from: classes-dex2jar.jar:android/support/design/widget/StateListAnimator.class */
public final class StateListAnimator {
    private final ArrayList<Tuple> tuples = new ArrayList<>();
    private Tuple lastMatch = null;
    ValueAnimator runningAnimator = null;
    private final Animator.AnimatorListener animationListener = new AnimatorListenerAdapter(this) { // from class: android.support.design.widget.StateListAnimator.1
        final StateListAnimator this$0;

        {
            this.this$0 = this;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (this.this$0.runningAnimator == animator) {
                this.this$0.runningAnimator = null;
            }
        }
    };

    /* loaded from: classes-dex2jar.jar:android/support/design/widget/StateListAnimator$Tuple.class */
    static class Tuple {
        final ValueAnimator animator;
        final int[] specs;

        Tuple(int[] iArr, ValueAnimator valueAnimator) {
            this.specs = iArr;
            this.animator = valueAnimator;
        }
    }

    private void cancel() {
        ValueAnimator valueAnimator = this.runningAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
            this.runningAnimator = null;
        }
    }

    private void start(Tuple tuple) {
        ValueAnimator valueAnimator = tuple.animator;
        this.runningAnimator = valueAnimator;
        valueAnimator.start();
    }

    public void addState(int[] iArr, ValueAnimator valueAnimator) {
        Tuple tuple = new Tuple(iArr, valueAnimator);
        valueAnimator.addListener(this.animationListener);
        this.tuples.add(tuple);
    }

    public void jumpToCurrentState() {
        ValueAnimator valueAnimator = this.runningAnimator;
        if (valueAnimator != null) {
            valueAnimator.end();
            this.runningAnimator = null;
        }
    }

    public void setState(int[] iArr) {
        Tuple tuple;
        int size = this.tuples.size();
        int i = 0;
        while (true) {
            if (i >= size) {
                tuple = null;
                break;
            }
            Tuple tuple2 = this.tuples.get(i);
            if (StateSet.stateSetMatches(tuple2.specs, iArr)) {
                tuple = tuple2;
                break;
            }
            i++;
        }
        Tuple tuple3 = this.lastMatch;
        if (tuple == tuple3) {
            return;
        }
        if (tuple3 != null) {
            cancel();
        }
        this.lastMatch = tuple;
        if (tuple != null) {
            start(tuple);
        }
    }
}
