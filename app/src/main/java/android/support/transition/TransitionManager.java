package android.support.transition;

import android.support.v4.util.ArrayMap;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes-dex2jar.jar:android/support/transition/TransitionManager.class */
public class TransitionManager {
    private static final String LOG_TAG = "TransitionManager";
    private static Transition sDefaultTransition = new AutoTransition();
    private static ThreadLocal<WeakReference<ArrayMap<ViewGroup, ArrayList<Transition>>>> sRunningTransitions = new ThreadLocal<>();
    static ArrayList<ViewGroup> sPendingTransitions = new ArrayList<>();
    private ArrayMap<Scene, Transition> mSceneTransitions = new ArrayMap<>();
    private ArrayMap<Scene, ArrayMap<Scene, Transition>> mScenePairTransitions = new ArrayMap<>();

    /* loaded from: classes-dex2jar.jar:android/support/transition/TransitionManager$MultiListener.class */
    private static class MultiListener implements ViewTreeObserver.OnPreDrawListener, View.OnAttachStateChangeListener {
        ViewGroup mSceneRoot;
        Transition mTransition;

        MultiListener(Transition transition, ViewGroup viewGroup) {
            this.mTransition = transition;
            this.mSceneRoot = viewGroup;
        }

        private void removeListeners() {
            this.mSceneRoot.getViewTreeObserver().removeOnPreDrawListener(this);
            this.mSceneRoot.removeOnAttachStateChangeListener(this);
        }

        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public boolean onPreDraw() {
            ArrayList<Transition> arrayList;
            removeListeners();
            if (!TransitionManager.sPendingTransitions.remove(this.mSceneRoot)) {
                return true;
            }
            ArrayMap<ViewGroup, ArrayList<Transition>> runningTransitions = TransitionManager.getRunningTransitions();
            ArrayList<Transition> arrayList2 = runningTransitions.get(this.mSceneRoot);
            ArrayList arrayList3 = null;
            if (arrayList2 == null) {
                arrayList = new ArrayList<>();
                runningTransitions.put(this.mSceneRoot, arrayList);
            } else {
                arrayList = arrayList2;
                if (arrayList2.size() > 0) {
                    arrayList3 = new ArrayList(arrayList2);
                    arrayList = arrayList2;
                }
            }
            arrayList.add(this.mTransition);
            this.mTransition.addListener(new TransitionListenerAdapter(this, runningTransitions) { // from class: android.support.transition.TransitionManager.MultiListener.1
                final MultiListener this$0;
                final ArrayMap val$runningTransitions;

                {
                    this.this$0 = this;
                    this.val$runningTransitions = runningTransitions;
                }

                /* JADX WARN: Multi-variable type inference failed */
                @Override // android.support.transition.TransitionListenerAdapter, android.support.transition.Transition.TransitionListener
                public void onTransitionEnd(Transition transition) {
                    ((ArrayList) this.val$runningTransitions.get(this.this$0.mSceneRoot)).remove(transition);
                }
            });
            this.mTransition.captureValues(this.mSceneRoot, false);
            if (arrayList3 != null) {
                Iterator it = arrayList3.iterator();
                while (it.hasNext()) {
                    ((Transition) it.next()).resume(this.mSceneRoot);
                }
            }
            this.mTransition.playTransition(this.mSceneRoot);
            return true;
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewAttachedToWindow(View view) {
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewDetachedFromWindow(View view) {
            removeListeners();
            TransitionManager.sPendingTransitions.remove(this.mSceneRoot);
            ArrayList<Transition> arrayList = TransitionManager.getRunningTransitions().get(this.mSceneRoot);
            if (arrayList != null && arrayList.size() > 0) {
                Iterator<Transition> it = arrayList.iterator();
                while (it.hasNext()) {
                    it.next().resume(this.mSceneRoot);
                }
            }
            this.mTransition.clearValues(true);
        }
    }

    public static void beginDelayedTransition(ViewGroup viewGroup) {
        beginDelayedTransition(viewGroup, null);
    }

    public static void beginDelayedTransition(ViewGroup viewGroup, Transition transition) {
        if (sPendingTransitions.contains(viewGroup) || !ViewCompat.isLaidOut(viewGroup)) {
            return;
        }
        sPendingTransitions.add(viewGroup);
        Transition transition2 = transition;
        if (transition == null) {
            transition2 = sDefaultTransition;
        }
        Transition transitionMo119clone = transition2.mo119clone();
        sceneChangeSetup(viewGroup, transitionMo119clone);
        Scene.setCurrentScene(viewGroup, null);
        sceneChangeRunTransition(viewGroup, transitionMo119clone);
    }

    private static void changeScene(Scene scene, Transition transition) {
        ViewGroup sceneRoot = scene.getSceneRoot();
        if (sPendingTransitions.contains(sceneRoot)) {
            return;
        }
        if (transition == null) {
            scene.enter();
            return;
        }
        sPendingTransitions.add(sceneRoot);
        Transition transitionMo119clone = transition.mo119clone();
        transitionMo119clone.setSceneRoot(sceneRoot);
        Scene currentScene = Scene.getCurrentScene(sceneRoot);
        if (currentScene != null && currentScene.isCreatedFromLayoutResource()) {
            transitionMo119clone.setCanRemoveViews(true);
        }
        sceneChangeSetup(sceneRoot, transitionMo119clone);
        scene.enter();
        sceneChangeRunTransition(sceneRoot, transitionMo119clone);
    }

    public static void endTransitions(ViewGroup viewGroup) {
        sPendingTransitions.remove(viewGroup);
        ArrayList<Transition> arrayList = getRunningTransitions().get(viewGroup);
        if (arrayList == null || arrayList.isEmpty()) {
            return;
        }
        ArrayList arrayList2 = new ArrayList(arrayList);
        for (int size = arrayList2.size() - 1; size >= 0; size--) {
            ((Transition) arrayList2.get(size)).forceToEnd(viewGroup);
        }
    }

    static ArrayMap<ViewGroup, ArrayList<Transition>> getRunningTransitions() {
        ArrayMap<ViewGroup, ArrayList<Transition>> arrayMap;
        WeakReference<ArrayMap<ViewGroup, ArrayList<Transition>>> weakReference = sRunningTransitions.get();
        if (weakReference != null && (arrayMap = weakReference.get()) != null) {
            return arrayMap;
        }
        ArrayMap<ViewGroup, ArrayList<Transition>> arrayMap2 = new ArrayMap<>();
        sRunningTransitions.set(new WeakReference<>(arrayMap2));
        return arrayMap2;
    }

    private Transition getTransition(Scene scene) {
        Scene currentScene;
        ArrayMap<Scene, Transition> arrayMap;
        Transition transition;
        ViewGroup sceneRoot = scene.getSceneRoot();
        if (sceneRoot != null && (currentScene = Scene.getCurrentScene(sceneRoot)) != null && (arrayMap = this.mScenePairTransitions.get(scene)) != null && (transition = arrayMap.get(currentScene)) != null) {
            return transition;
        }
        Transition transition2 = this.mSceneTransitions.get(scene);
        if (transition2 == null) {
            transition2 = sDefaultTransition;
        }
        return transition2;
    }

    /* renamed from: go */
    public static void m2go(Scene scene) {
        changeScene(scene, sDefaultTransition);
    }

    /* renamed from: go */
    public static void m3go(Scene scene, Transition transition) {
        changeScene(scene, transition);
    }

    private static void sceneChangeRunTransition(ViewGroup viewGroup, Transition transition) {
        if (transition == null || viewGroup == null) {
            return;
        }
        MultiListener multiListener = new MultiListener(transition, viewGroup);
        viewGroup.addOnAttachStateChangeListener(multiListener);
        viewGroup.getViewTreeObserver().addOnPreDrawListener(multiListener);
    }

    private static void sceneChangeSetup(ViewGroup viewGroup, Transition transition) {
        ArrayList<Transition> arrayList = getRunningTransitions().get(viewGroup);
        if (arrayList != null && arrayList.size() > 0) {
            Iterator<Transition> it = arrayList.iterator();
            while (it.hasNext()) {
                it.next().pause(viewGroup);
            }
        }
        if (transition != null) {
            transition.captureValues(viewGroup, true);
        }
        Scene currentScene = Scene.getCurrentScene(viewGroup);
        if (currentScene != null) {
            currentScene.exit();
        }
    }

    public void setTransition(Scene scene, Scene scene2, Transition transition) {
        ArrayMap<Scene, Transition> arrayMap = this.mScenePairTransitions.get(scene2);
        ArrayMap<Scene, Transition> arrayMap2 = arrayMap;
        if (arrayMap == null) {
            arrayMap2 = new ArrayMap<>();
            this.mScenePairTransitions.put(scene2, arrayMap2);
        }
        arrayMap2.put(scene, transition);
    }

    public void setTransition(Scene scene, Transition transition) {
        this.mSceneTransitions.put(scene, transition);
    }

    public void transitionTo(Scene scene) {
        changeScene(scene, getTransition(scene));
    }
}
