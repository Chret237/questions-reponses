package android.support.v4.app;

import android.graphics.Rect;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes-dex2jar.jar:android/support/v4/app/FragmentTransitionCompat21.class */
class FragmentTransitionCompat21 extends FragmentTransitionImpl {
    FragmentTransitionCompat21() {
    }

    private static boolean hasSimpleTarget(Transition transition) {
        return (isNullOrEmpty(transition.getTargetIds()) && isNullOrEmpty(transition.getTargetNames()) && isNullOrEmpty(transition.getTargetTypes())) ? false : true;
    }

    @Override // android.support.v4.app.FragmentTransitionImpl
    public void addTarget(Object obj, View view) {
        if (obj != null) {
            ((Transition) obj).addTarget(view);
        }
    }

    @Override // android.support.v4.app.FragmentTransitionImpl
    public void addTargets(Object obj, ArrayList<View> arrayList) {
        Transition transition = (Transition) obj;
        if (transition == null) {
            return;
        }
        if (transition instanceof TransitionSet) {
            TransitionSet transitionSet = (TransitionSet) transition;
            int transitionCount = transitionSet.getTransitionCount();
            for (int i = 0; i < transitionCount; i++) {
                addTargets(transitionSet.getTransitionAt(i), arrayList);
            }
            return;
        }
        if (hasSimpleTarget(transition) || !isNullOrEmpty(transition.getTargets())) {
            return;
        }
        int size = arrayList.size();
        for (int i2 = 0; i2 < size; i2++) {
            transition.addTarget(arrayList.get(i2));
        }
    }

    @Override // android.support.v4.app.FragmentTransitionImpl
    public void beginDelayedTransition(ViewGroup viewGroup, Object obj) {
        TransitionManager.beginDelayedTransition(viewGroup, (Transition) obj);
    }

    @Override // android.support.v4.app.FragmentTransitionImpl
    public boolean canHandle(Object obj) {
        return obj instanceof Transition;
    }

    @Override // android.support.v4.app.FragmentTransitionImpl
    public Object cloneTransition(Object obj) {
        return obj != null ? ((Transition) obj).clone() : null;
    }

    @Override // android.support.v4.app.FragmentTransitionImpl
    public Object mergeTransitionsInSequence(Object obj, Object obj2, Object obj3) {
        Transition ordering = (Transition) obj;
        Transition transition = (Transition) obj2;
        Transition transition2 = (Transition) obj3;
        if (ordering != null && transition != null) {
            ordering = new TransitionSet().addTransition(ordering).addTransition(transition).setOrdering(1);
        } else if (ordering == null) {
            ordering = transition != null ? transition : null;
        }
        if (transition2 == null) {
            return ordering;
        }
        TransitionSet transitionSet = new TransitionSet();
        if (ordering != null) {
            transitionSet.addTransition(ordering);
        }
        transitionSet.addTransition(transition2);
        return transitionSet;
    }

    @Override // android.support.v4.app.FragmentTransitionImpl
    public Object mergeTransitionsTogether(Object obj, Object obj2, Object obj3) {
        TransitionSet transitionSet = new TransitionSet();
        if (obj != null) {
            transitionSet.addTransition((Transition) obj);
        }
        if (obj2 != null) {
            transitionSet.addTransition((Transition) obj2);
        }
        if (obj3 != null) {
            transitionSet.addTransition((Transition) obj3);
        }
        return transitionSet;
    }

    @Override // android.support.v4.app.FragmentTransitionImpl
    public void removeTarget(Object obj, View view) {
        if (obj != null) {
            ((Transition) obj).removeTarget(view);
        }
    }

    @Override // android.support.v4.app.FragmentTransitionImpl
    public void replaceTargets(Object obj, ArrayList<View> arrayList, ArrayList<View> arrayList2) {
        List<View> targets;
        Transition transition = (Transition) obj;
        if (transition instanceof TransitionSet) {
            TransitionSet transitionSet = (TransitionSet) transition;
            int transitionCount = transitionSet.getTransitionCount();
            for (int i = 0; i < transitionCount; i++) {
                replaceTargets(transitionSet.getTransitionAt(i), arrayList, arrayList2);
            }
            return;
        }
        if (hasSimpleTarget(transition) || (targets = transition.getTargets()) == null || targets.size() != arrayList.size() || !targets.containsAll(arrayList)) {
            return;
        }
        int size = arrayList2 == null ? 0 : arrayList2.size();
        for (int i2 = 0; i2 < size; i2++) {
            transition.addTarget(arrayList2.get(i2));
        }
        for (int size2 = arrayList.size() - 1; size2 >= 0; size2--) {
            transition.removeTarget(arrayList.get(size2));
        }
    }

    @Override // android.support.v4.app.FragmentTransitionImpl
    public void scheduleHideFragmentView(Object obj, View view, ArrayList<View> arrayList) {
        ((Transition) obj).addListener(new Transition.TransitionListener(this, view, arrayList) { // from class: android.support.v4.app.FragmentTransitionCompat21.2
            final FragmentTransitionCompat21 this$0;
            final ArrayList val$exitingViews;
            final View val$fragmentView;

            {
                this.this$0 = this;
                this.val$fragmentView = view;
                this.val$exitingViews = arrayList;
            }

            @Override // android.transition.Transition.TransitionListener
            public void onTransitionCancel(Transition transition) {
            }

            @Override // android.transition.Transition.TransitionListener
            public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
                this.val$fragmentView.setVisibility(8);
                int size = this.val$exitingViews.size();
                for (int i = 0; i < size; i++) {
                    ((View) this.val$exitingViews.get(i)).setVisibility(0);
                }
            }

            @Override // android.transition.Transition.TransitionListener
            public void onTransitionPause(Transition transition) {
            }

            @Override // android.transition.Transition.TransitionListener
            public void onTransitionResume(Transition transition) {
            }

            @Override // android.transition.Transition.TransitionListener
            public void onTransitionStart(Transition transition) {
            }
        });
    }

    @Override // android.support.v4.app.FragmentTransitionImpl
    public void scheduleRemoveTargets(Object obj, Object obj2, ArrayList<View> arrayList, Object obj3, ArrayList<View> arrayList2, Object obj4, ArrayList<View> arrayList3) {
        ((Transition) obj).addListener(new Transition.TransitionListener(this, obj2, arrayList, obj3, arrayList2, obj4, arrayList3) { // from class: android.support.v4.app.FragmentTransitionCompat21.3
            final FragmentTransitionCompat21 this$0;
            final Object val$enterTransition;
            final ArrayList val$enteringViews;
            final Object val$exitTransition;
            final ArrayList val$exitingViews;
            final Object val$sharedElementTransition;
            final ArrayList val$sharedElementsIn;

            {
                this.this$0 = this;
                this.val$enterTransition = obj2;
                this.val$enteringViews = arrayList;
                this.val$exitTransition = obj3;
                this.val$exitingViews = arrayList2;
                this.val$sharedElementTransition = obj4;
                this.val$sharedElementsIn = arrayList3;
            }

            @Override // android.transition.Transition.TransitionListener
            public void onTransitionCancel(Transition transition) {
            }

            @Override // android.transition.Transition.TransitionListener
            public void onTransitionEnd(Transition transition) {
            }

            @Override // android.transition.Transition.TransitionListener
            public void onTransitionPause(Transition transition) {
            }

            @Override // android.transition.Transition.TransitionListener
            public void onTransitionResume(Transition transition) {
            }

            @Override // android.transition.Transition.TransitionListener
            public void onTransitionStart(Transition transition) {
                Object obj5 = this.val$enterTransition;
                if (obj5 != null) {
                    this.this$0.replaceTargets(obj5, this.val$enteringViews, null);
                }
                Object obj6 = this.val$exitTransition;
                if (obj6 != null) {
                    this.this$0.replaceTargets(obj6, this.val$exitingViews, null);
                }
                Object obj7 = this.val$sharedElementTransition;
                if (obj7 != null) {
                    this.this$0.replaceTargets(obj7, this.val$sharedElementsIn, null);
                }
            }
        });
    }

    @Override // android.support.v4.app.FragmentTransitionImpl
    public void setEpicenter(Object obj, Rect rect) {
        if (obj != null) {
            ((Transition) obj).setEpicenterCallback(new Transition.EpicenterCallback(this, rect) { // from class: android.support.v4.app.FragmentTransitionCompat21.4
                final FragmentTransitionCompat21 this$0;
                final Rect val$epicenter;

                {
                    this.this$0 = this;
                    this.val$epicenter = rect;
                }

                @Override // android.transition.Transition.EpicenterCallback
                public Rect onGetEpicenter(Transition transition) {
                    Rect rect2 = this.val$epicenter;
                    if (rect2 == null || rect2.isEmpty()) {
                        return null;
                    }
                    return this.val$epicenter;
                }
            });
        }
    }

    @Override // android.support.v4.app.FragmentTransitionImpl
    public void setEpicenter(Object obj, View view) {
        if (view != null) {
            Rect rect = new Rect();
            getBoundsOnScreen(view, rect);
            ((Transition) obj).setEpicenterCallback(new Transition.EpicenterCallback(this, rect) { // from class: android.support.v4.app.FragmentTransitionCompat21.1
                final FragmentTransitionCompat21 this$0;
                final Rect val$epicenter;

                {
                    this.this$0 = this;
                    this.val$epicenter = rect;
                }

                @Override // android.transition.Transition.EpicenterCallback
                public Rect onGetEpicenter(Transition transition) {
                    return this.val$epicenter;
                }
            });
        }
    }

    @Override // android.support.v4.app.FragmentTransitionImpl
    public void setSharedElementTargets(Object obj, View view, ArrayList<View> arrayList) {
        TransitionSet transitionSet = (TransitionSet) obj;
        List<View> targets = transitionSet.getTargets();
        targets.clear();
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            bfsAddViewChildren(targets, arrayList.get(i));
        }
        targets.add(view);
        arrayList.add(view);
        addTargets(transitionSet, arrayList);
    }

    @Override // android.support.v4.app.FragmentTransitionImpl
    public void swapSharedElementTargets(Object obj, ArrayList<View> arrayList, ArrayList<View> arrayList2) {
        TransitionSet transitionSet = (TransitionSet) obj;
        if (transitionSet != null) {
            transitionSet.getTargets().clear();
            transitionSet.getTargets().addAll(arrayList2);
            replaceTargets(transitionSet, arrayList, arrayList2);
        }
    }

    @Override // android.support.v4.app.FragmentTransitionImpl
    public Object wrapTransitionInSet(Object obj) {
        if (obj == null) {
            return null;
        }
        TransitionSet transitionSet = new TransitionSet();
        transitionSet.addTransition((Transition) obj);
        return transitionSet;
    }
}
