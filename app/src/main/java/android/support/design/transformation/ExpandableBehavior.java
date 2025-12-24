package android.support.design.transformation;

import android.content.Context;
import android.support.design.expandable.ExpandableWidget;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import java.util.List;

/* loaded from: classes-dex2jar.jar:android/support/design/transformation/ExpandableBehavior.class */
public abstract class ExpandableBehavior extends CoordinatorLayout.Behavior<View> {
    private static final int STATE_COLLAPSED = 2;
    private static final int STATE_EXPANDED = 1;
    private static final int STATE_UNINITIALIZED = 0;
    private int currentState;

    public ExpandableBehavior() {
        this.currentState = 0;
    }

    public ExpandableBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.currentState = 0;
    }

    /* JADX WARN: Removed duplicated region for block: B:8:0x0019  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean didStateChange(boolean r4) {
        /*
            r3 = this;
            r0 = 0
            r7 = r0
            r0 = 0
            r6 = r0
            r0 = r4
            if (r0 == 0) goto L1d
            r0 = r3
            int r0 = r0.currentState
            r5 = r0
            r0 = r5
            if (r0 == 0) goto L19
            r0 = r6
            r4 = r0
            r0 = r5
            r1 = 2
            if (r0 != r1) goto L1b
        L19:
            r0 = 1
            r4 = r0
        L1b:
            r0 = r4
            return r0
        L1d:
            r0 = r7
            r4 = r0
            r0 = r3
            int r0 = r0.currentState
            r1 = 1
            if (r0 != r1) goto L2a
            r0 = 1
            r4 = r0
        L2a:
            r0 = r4
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.design.transformation.ExpandableBehavior.didStateChange(boolean):boolean");
    }

    public static <T extends ExpandableBehavior> T from(View view, Class<T> cls) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (!(layoutParams instanceof CoordinatorLayout.LayoutParams)) {
            throw new IllegalArgumentException("The view is not a child of CoordinatorLayout");
        }
        CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) layoutParams).getBehavior();
        if (behavior instanceof ExpandableBehavior) {
            return cls.cast(behavior);
        }
        throw new IllegalArgumentException("The view is not associated with ExpandableBehavior");
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected ExpandableWidget findExpandableWidget(CoordinatorLayout coordinatorLayout, View view) {
        List<View> dependencies = coordinatorLayout.getDependencies(view);
        int size = dependencies.size();
        for (int i = 0; i < size; i++) {
            View view2 = dependencies.get(i);
            if (layoutDependsOn(coordinatorLayout, view, view2)) {
                return (ExpandableWidget) view2;
            }
        }
        return null;
    }

    @Override // android.support.design.widget.CoordinatorLayout.Behavior
    public abstract boolean layoutDependsOn(CoordinatorLayout coordinatorLayout, View view, View view2);

    /* JADX WARN: Multi-variable type inference failed */
    @Override // android.support.design.widget.CoordinatorLayout.Behavior
    public boolean onDependentViewChanged(CoordinatorLayout coordinatorLayout, View view, View view2) {
        ExpandableWidget expandableWidget = (ExpandableWidget) view2;
        if (!didStateChange(expandableWidget.isExpanded())) {
            return false;
        }
        this.currentState = expandableWidget.isExpanded() ? 1 : 2;
        return onExpandedStateChange((View) expandableWidget, view, expandableWidget.isExpanded(), true);
    }

    protected abstract boolean onExpandedStateChange(View view, View view2, boolean z, boolean z2);

    @Override // android.support.design.widget.CoordinatorLayout.Behavior
    public boolean onLayoutChild(CoordinatorLayout coordinatorLayout, View view, int i) {
        ExpandableWidget expandableWidgetFindExpandableWidget;
        if (ViewCompat.isLaidOut(view) || (expandableWidgetFindExpandableWidget = findExpandableWidget(coordinatorLayout, view)) == null || !didStateChange(expandableWidgetFindExpandableWidget.isExpanded())) {
            return false;
        }
        int i2 = expandableWidgetFindExpandableWidget.isExpanded() ? 1 : 2;
        this.currentState = i2;
        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener(this, view, i2, expandableWidgetFindExpandableWidget) { // from class: android.support.design.transformation.ExpandableBehavior.1
            final ExpandableBehavior this$0;
            final View val$child;
            final ExpandableWidget val$dep;
            final int val$expectedState;

            {
                this.this$0 = this;
                this.val$child = view;
                this.val$expectedState = i2;
                this.val$dep = expandableWidgetFindExpandableWidget;
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // android.view.ViewTreeObserver.OnPreDrawListener
            public boolean onPreDraw() {
                this.val$child.getViewTreeObserver().removeOnPreDrawListener(this);
                if (this.this$0.currentState != this.val$expectedState) {
                    return false;
                }
                ExpandableBehavior expandableBehavior = this.this$0;
                ExpandableWidget expandableWidget = this.val$dep;
                expandableBehavior.onExpandedStateChange((View) expandableWidget, this.val$child, expandableWidget.isExpanded(), false);
                return false;
            }
        });
        return false;
    }
}
