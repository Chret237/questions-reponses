package android.support.design.transformation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/* loaded from: classes-dex2jar.jar:android/support/design/transformation/ExpandableTransformationBehavior.class */
public abstract class ExpandableTransformationBehavior extends ExpandableBehavior {
    private AnimatorSet currentAnimation;

    public ExpandableTransformationBehavior() {
    }

    public ExpandableTransformationBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    protected abstract AnimatorSet onCreateExpandedStateChangeAnimation(View view, View view2, boolean z, boolean z2);

    @Override // android.support.design.transformation.ExpandableBehavior
    protected boolean onExpandedStateChange(View view, View view2, boolean z, boolean z2) {
        boolean z3 = this.currentAnimation != null;
        if (z3) {
            this.currentAnimation.cancel();
        }
        AnimatorSet animatorSetOnCreateExpandedStateChangeAnimation = onCreateExpandedStateChangeAnimation(view, view2, z, z3);
        this.currentAnimation = animatorSetOnCreateExpandedStateChangeAnimation;
        animatorSetOnCreateExpandedStateChangeAnimation.addListener(new AnimatorListenerAdapter(this) { // from class: android.support.design.transformation.ExpandableTransformationBehavior.1
            final ExpandableTransformationBehavior this$0;

            {
                this.this$0 = this;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                this.this$0.currentAnimation = null;
            }
        });
        this.currentAnimation.start();
        if (z2) {
            return true;
        }
        this.currentAnimation.end();
        return true;
    }
}
