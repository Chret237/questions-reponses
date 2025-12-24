package android.arch.lifecycle;

import android.arch.core.internal.FastSafeIterableMap;
import android.arch.core.internal.SafeIterableMap;
import android.arch.lifecycle.Lifecycle;
import android.util.Log;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/* loaded from: classes-dex2jar.jar:android/arch/lifecycle/LifecycleRegistry.class */
public class LifecycleRegistry extends Lifecycle {
    private static final String LOG_TAG = "LifecycleRegistry";
    private final WeakReference<LifecycleOwner> mLifecycleOwner;
    private FastSafeIterableMap<LifecycleObserver, ObserverWithState> mObserverMap = new FastSafeIterableMap<>();
    private int mAddingObserverCounter = 0;
    private boolean mHandlingEvent = false;
    private boolean mNewEventOccurred = false;
    private ArrayList<Lifecycle.State> mParentStates = new ArrayList<>();
    private Lifecycle.State mState = Lifecycle.State.INITIALIZED;

    /* renamed from: android.arch.lifecycle.LifecycleRegistry$1 */
    /* loaded from: classes-dex2jar.jar:android/arch/lifecycle/LifecycleRegistry$1.class */
    static /* synthetic */ class C00081 {
        static final int[] $SwitchMap$android$arch$lifecycle$Lifecycle$Event;
        static final int[] $SwitchMap$android$arch$lifecycle$Lifecycle$State;

        /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
            jadx.core.utils.exceptions.JadxRuntimeException: Can't find top splitter block for handler:B:46:0x00bd
            	at jadx.core.utils.BlockUtils.getTopSplitterForHandler(BlockUtils.java:1178)
            	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.collectHandlerRegions(ExcHandlersRegionMaker.java:53)
            	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.process(ExcHandlersRegionMaker.java:38)
            	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:27)
            */
        static {
            /*
                android.arch.lifecycle.Lifecycle$State[] r0 = android.arch.lifecycle.Lifecycle.State.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                r4 = r0
                r0 = r4
                android.arch.lifecycle.LifecycleRegistry.C00081.$SwitchMap$android$arch$lifecycle$Lifecycle$State = r0
                r0 = r4
                android.arch.lifecycle.Lifecycle$State r1 = android.arch.lifecycle.Lifecycle.State.INITIALIZED     // Catch: java.lang.NoSuchFieldError -> L99
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L99
                r2 = 1
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L99
            L14:
                int[] r0 = android.arch.lifecycle.LifecycleRegistry.C00081.$SwitchMap$android$arch$lifecycle$Lifecycle$State     // Catch: java.lang.NoSuchFieldError -> L99 java.lang.NoSuchFieldError -> L9d
                android.arch.lifecycle.Lifecycle$State r1 = android.arch.lifecycle.Lifecycle.State.CREATED     // Catch: java.lang.NoSuchFieldError -> L9d
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L9d
                r2 = 2
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L9d
            L1f:
                int[] r0 = android.arch.lifecycle.LifecycleRegistry.C00081.$SwitchMap$android$arch$lifecycle$Lifecycle$State     // Catch: java.lang.NoSuchFieldError -> L9d java.lang.NoSuchFieldError -> La1
                android.arch.lifecycle.Lifecycle$State r1 = android.arch.lifecycle.Lifecycle.State.STARTED     // Catch: java.lang.NoSuchFieldError -> La1
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> La1
                r2 = 3
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> La1
            L2a:
                int[] r0 = android.arch.lifecycle.LifecycleRegistry.C00081.$SwitchMap$android$arch$lifecycle$Lifecycle$State     // Catch: java.lang.NoSuchFieldError -> La1 java.lang.NoSuchFieldError -> La5
                android.arch.lifecycle.Lifecycle$State r1 = android.arch.lifecycle.Lifecycle.State.RESUMED     // Catch: java.lang.NoSuchFieldError -> La5
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> La5
                r2 = 4
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> La5
            L35:
                int[] r0 = android.arch.lifecycle.LifecycleRegistry.C00081.$SwitchMap$android$arch$lifecycle$Lifecycle$State     // Catch: java.lang.NoSuchFieldError -> La5 java.lang.NoSuchFieldError -> La9
                android.arch.lifecycle.Lifecycle$State r1 = android.arch.lifecycle.Lifecycle.State.DESTROYED     // Catch: java.lang.NoSuchFieldError -> La9
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> La9
                r2 = 5
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> La9
            L40:
                android.arch.lifecycle.Lifecycle$Event[] r0 = android.arch.lifecycle.Lifecycle.Event.values()     // Catch: java.lang.NoSuchFieldError -> La9
                int r0 = r0.length
                int[] r0 = new int[r0]
                r4 = r0
                r0 = r4
                android.arch.lifecycle.LifecycleRegistry.C00081.$SwitchMap$android$arch$lifecycle$Lifecycle$Event = r0
                r0 = r4
                android.arch.lifecycle.Lifecycle$Event r1 = android.arch.lifecycle.Lifecycle.Event.ON_CREATE     // Catch: java.lang.NoSuchFieldError -> Lad
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> Lad
                r2 = 1
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> Lad
            L54:
                int[] r0 = android.arch.lifecycle.LifecycleRegistry.C00081.$SwitchMap$android$arch$lifecycle$Lifecycle$Event     // Catch: java.lang.NoSuchFieldError -> Lad java.lang.NoSuchFieldError -> Lb1
                android.arch.lifecycle.Lifecycle$Event r1 = android.arch.lifecycle.Lifecycle.Event.ON_STOP     // Catch: java.lang.NoSuchFieldError -> Lb1
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> Lb1
                r2 = 2
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> Lb1
            L5f:
                int[] r0 = android.arch.lifecycle.LifecycleRegistry.C00081.$SwitchMap$android$arch$lifecycle$Lifecycle$Event     // Catch: java.lang.NoSuchFieldError -> Lb1 java.lang.NoSuchFieldError -> Lb5
                android.arch.lifecycle.Lifecycle$Event r1 = android.arch.lifecycle.Lifecycle.Event.ON_START     // Catch: java.lang.NoSuchFieldError -> Lb5
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> Lb5
                r2 = 3
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> Lb5
            L6a:
                int[] r0 = android.arch.lifecycle.LifecycleRegistry.C00081.$SwitchMap$android$arch$lifecycle$Lifecycle$Event     // Catch: java.lang.NoSuchFieldError -> Lb5 java.lang.NoSuchFieldError -> Lb9
                android.arch.lifecycle.Lifecycle$Event r1 = android.arch.lifecycle.Lifecycle.Event.ON_PAUSE     // Catch: java.lang.NoSuchFieldError -> Lb9
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> Lb9
                r2 = 4
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> Lb9
            L75:
                int[] r0 = android.arch.lifecycle.LifecycleRegistry.C00081.$SwitchMap$android$arch$lifecycle$Lifecycle$Event     // Catch: java.lang.NoSuchFieldError -> Lb9 java.lang.NoSuchFieldError -> Lbd
                android.arch.lifecycle.Lifecycle$Event r1 = android.arch.lifecycle.Lifecycle.Event.ON_RESUME     // Catch: java.lang.NoSuchFieldError -> Lbd
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> Lbd
                r2 = 5
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> Lbd
            L80:
                int[] r0 = android.arch.lifecycle.LifecycleRegistry.C00081.$SwitchMap$android$arch$lifecycle$Lifecycle$Event     // Catch: java.lang.NoSuchFieldError -> Lbd java.lang.NoSuchFieldError -> Lc1
                android.arch.lifecycle.Lifecycle$Event r1 = android.arch.lifecycle.Lifecycle.Event.ON_DESTROY     // Catch: java.lang.NoSuchFieldError -> Lc1
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> Lc1
                r2 = 6
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> Lc1
            L8c:
                int[] r0 = android.arch.lifecycle.LifecycleRegistry.C00081.$SwitchMap$android$arch$lifecycle$Lifecycle$Event     // Catch: java.lang.NoSuchFieldError -> Lc1 java.lang.NoSuchFieldError -> Lc5
                android.arch.lifecycle.Lifecycle$Event r1 = android.arch.lifecycle.Lifecycle.Event.ON_ANY     // Catch: java.lang.NoSuchFieldError -> Lc5
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> Lc5
                r2 = 7
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> Lc5
            L98:
                return
            L99:
                r4 = move-exception
                goto L14
            L9d:
                r4 = move-exception
                goto L1f
            La1:
                r4 = move-exception
                goto L2a
            La5:
                r4 = move-exception
                goto L35
            La9:
                r4 = move-exception
                goto L40
            Lad:
                r4 = move-exception
                goto L54
            Lb1:
                r4 = move-exception
                goto L5f
            Lb5:
                r4 = move-exception
                goto L6a
            Lb9:
                r4 = move-exception
                goto L75
            Lbd:
                r4 = move-exception
                goto L80
            Lc1:
                r4 = move-exception
                goto L8c
            Lc5:
                r4 = move-exception
                goto L98
            */
            throw new UnsupportedOperationException("Method not decompiled: android.arch.lifecycle.LifecycleRegistry.C00081.m27clinit():void");
        }
    }

    /* loaded from: classes-dex2jar.jar:android/arch/lifecycle/LifecycleRegistry$ObserverWithState.class */
    static class ObserverWithState {
        GenericLifecycleObserver mLifecycleObserver;
        Lifecycle.State mState;

        ObserverWithState(LifecycleObserver lifecycleObserver, Lifecycle.State state) {
            this.mLifecycleObserver = Lifecycling.getCallback(lifecycleObserver);
            this.mState = state;
        }

        void dispatchEvent(LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
            Lifecycle.State stateAfter = LifecycleRegistry.getStateAfter(event);
            this.mState = LifecycleRegistry.min(this.mState, stateAfter);
            this.mLifecycleObserver.onStateChanged(lifecycleOwner, event);
            this.mState = stateAfter;
        }
    }

    public LifecycleRegistry(LifecycleOwner lifecycleOwner) {
        this.mLifecycleOwner = new WeakReference<>(lifecycleOwner);
    }

    private void backwardPass(LifecycleOwner lifecycleOwner) {
        Iterator<Map.Entry<LifecycleObserver, ObserverWithState>> itDescendingIterator = this.mObserverMap.descendingIterator();
        while (itDescendingIterator.hasNext() && !this.mNewEventOccurred) {
            Map.Entry<LifecycleObserver, ObserverWithState> next = itDescendingIterator.next();
            ObserverWithState value = next.getValue();
            while (value.mState.compareTo(this.mState) > 0 && !this.mNewEventOccurred && this.mObserverMap.contains(next.getKey())) {
                Lifecycle.Event eventDownEvent = downEvent(value.mState);
                pushParentState(getStateAfter(eventDownEvent));
                value.dispatchEvent(lifecycleOwner, eventDownEvent);
                popParentState();
            }
        }
    }

    private Lifecycle.State calculateTargetState(LifecycleObserver lifecycleObserver) {
        Map.Entry<LifecycleObserver, ObserverWithState> entryCeil = this.mObserverMap.ceil(lifecycleObserver);
        Lifecycle.State state = null;
        Lifecycle.State state2 = entryCeil != null ? entryCeil.getValue().mState : null;
        if (!this.mParentStates.isEmpty()) {
            ArrayList<Lifecycle.State> arrayList = this.mParentStates;
            state = arrayList.get(arrayList.size() - 1);
        }
        return min(min(this.mState, state2), state);
    }

    private static Lifecycle.Event downEvent(Lifecycle.State state) {
        int i = C00081.$SwitchMap$android$arch$lifecycle$Lifecycle$State[state.ordinal()];
        if (i == 1) {
            throw new IllegalArgumentException();
        }
        if (i == 2) {
            return Lifecycle.Event.ON_DESTROY;
        }
        if (i == 3) {
            return Lifecycle.Event.ON_STOP;
        }
        if (i == 4) {
            return Lifecycle.Event.ON_PAUSE;
        }
        if (i == 5) {
            throw new IllegalArgumentException();
        }
        throw new IllegalArgumentException("Unexpected state value " + state);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void forwardPass(LifecycleOwner lifecycleOwner) {
        SafeIterableMap<LifecycleObserver, ObserverWithState>.IteratorWithAdditions iteratorWithAdditions = this.mObserverMap.iteratorWithAdditions();
        while (iteratorWithAdditions.hasNext() && !this.mNewEventOccurred) {
            Map.Entry next = iteratorWithAdditions.next();
            ObserverWithState observerWithState = (ObserverWithState) next.getValue();
            while (observerWithState.mState.compareTo(this.mState) < 0 && !this.mNewEventOccurred && this.mObserverMap.contains(next.getKey())) {
                pushParentState(observerWithState.mState);
                observerWithState.dispatchEvent(lifecycleOwner, upEvent(observerWithState.mState));
                popParentState();
            }
        }
    }

    static Lifecycle.State getStateAfter(Lifecycle.Event event) {
        switch (C00081.$SwitchMap$android$arch$lifecycle$Lifecycle$Event[event.ordinal()]) {
            case 1:
            case 2:
                return Lifecycle.State.CREATED;
            case 3:
            case 4:
                return Lifecycle.State.STARTED;
            case 5:
                return Lifecycle.State.RESUMED;
            case 6:
                return Lifecycle.State.DESTROYED;
            default:
                throw new IllegalArgumentException("Unexpected event value " + event);
        }
    }

    private boolean isSynced() {
        boolean z = true;
        if (this.mObserverMap.size() == 0) {
            return true;
        }
        Lifecycle.State state = this.mObserverMap.eldest().getValue().mState;
        Lifecycle.State state2 = this.mObserverMap.newest().getValue().mState;
        if (state != state2 || this.mState != state2) {
            z = false;
        }
        return z;
    }

    static Lifecycle.State min(Lifecycle.State state, Lifecycle.State state2) {
        Lifecycle.State state3 = state;
        if (state2 != null) {
            state3 = state;
            if (state2.compareTo(state) < 0) {
                state3 = state2;
            }
        }
        return state3;
    }

    private void moveToState(Lifecycle.State state) {
        if (this.mState == state) {
            return;
        }
        this.mState = state;
        if (this.mHandlingEvent || this.mAddingObserverCounter != 0) {
            this.mNewEventOccurred = true;
            return;
        }
        this.mHandlingEvent = true;
        sync();
        this.mHandlingEvent = false;
    }

    private void popParentState() {
        ArrayList<Lifecycle.State> arrayList = this.mParentStates;
        arrayList.remove(arrayList.size() - 1);
    }

    private void pushParentState(Lifecycle.State state) {
        this.mParentStates.add(state);
    }

    private void sync() {
        LifecycleOwner lifecycleOwner = this.mLifecycleOwner.get();
        if (lifecycleOwner == null) {
            Log.w(LOG_TAG, "LifecycleOwner is garbage collected, you shouldn't try dispatch new events from it.");
            return;
        }
        while (!isSynced()) {
            this.mNewEventOccurred = false;
            if (this.mState.compareTo(this.mObserverMap.eldest().getValue().mState) < 0) {
                backwardPass(lifecycleOwner);
            }
            Map.Entry<LifecycleObserver, ObserverWithState> entryNewest = this.mObserverMap.newest();
            if (!this.mNewEventOccurred && entryNewest != null && this.mState.compareTo(entryNewest.getValue().mState) > 0) {
                forwardPass(lifecycleOwner);
            }
        }
        this.mNewEventOccurred = false;
    }

    private static Lifecycle.Event upEvent(Lifecycle.State state) {
        int i = C00081.$SwitchMap$android$arch$lifecycle$Lifecycle$State[state.ordinal()];
        if (i != 1) {
            if (i == 2) {
                return Lifecycle.Event.ON_START;
            }
            if (i == 3) {
                return Lifecycle.Event.ON_RESUME;
            }
            if (i == 4) {
                throw new IllegalArgumentException();
            }
            if (i != 5) {
                throw new IllegalArgumentException("Unexpected state value " + state);
            }
        }
        return Lifecycle.Event.ON_CREATE;
    }

    @Override // android.arch.lifecycle.Lifecycle
    public void addObserver(LifecycleObserver lifecycleObserver) {
        LifecycleOwner lifecycleOwner;
        ObserverWithState observerWithState = new ObserverWithState(lifecycleObserver, this.mState == Lifecycle.State.DESTROYED ? Lifecycle.State.DESTROYED : Lifecycle.State.INITIALIZED);
        if (this.mObserverMap.putIfAbsent(lifecycleObserver, observerWithState) == null && (lifecycleOwner = this.mLifecycleOwner.get()) != null) {
            boolean z = this.mAddingObserverCounter != 0 || this.mHandlingEvent;
            Lifecycle.State stateCalculateTargetState = calculateTargetState(lifecycleObserver);
            this.mAddingObserverCounter++;
            while (observerWithState.mState.compareTo(stateCalculateTargetState) < 0 && this.mObserverMap.contains(lifecycleObserver)) {
                pushParentState(observerWithState.mState);
                observerWithState.dispatchEvent(lifecycleOwner, upEvent(observerWithState.mState));
                popParentState();
                stateCalculateTargetState = calculateTargetState(lifecycleObserver);
            }
            if (!z) {
                sync();
            }
            this.mAddingObserverCounter--;
        }
    }

    @Override // android.arch.lifecycle.Lifecycle
    public Lifecycle.State getCurrentState() {
        return this.mState;
    }

    public int getObserverCount() {
        return this.mObserverMap.size();
    }

    public void handleLifecycleEvent(Lifecycle.Event event) {
        moveToState(getStateAfter(event));
    }

    public void markState(Lifecycle.State state) {
        moveToState(state);
    }

    @Override // android.arch.lifecycle.Lifecycle
    public void removeObserver(LifecycleObserver lifecycleObserver) {
        this.mObserverMap.remove(lifecycleObserver);
    }
}
