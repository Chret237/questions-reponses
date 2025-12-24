package android.arch.lifecycle;

import android.arch.lifecycle.Lifecycle;

/* loaded from: classes-dex2jar.jar:android/arch/lifecycle/FullLifecycleObserverAdapter.class */
class FullLifecycleObserverAdapter implements GenericLifecycleObserver {
    private final FullLifecycleObserver mObserver;

    /* renamed from: android.arch.lifecycle.FullLifecycleObserverAdapter$1 */
    /* loaded from: classes-dex2jar.jar:android/arch/lifecycle/FullLifecycleObserverAdapter$1.class */
    static /* synthetic */ class C00071 {
        static final int[] $SwitchMap$android$arch$lifecycle$Lifecycle$Event;

        /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
            jadx.core.utils.exceptions.JadxRuntimeException: Can't find top splitter block for handler:B:27:0x006d
            	at jadx.core.utils.BlockUtils.getTopSplitterForHandler(BlockUtils.java:1178)
            	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.collectHandlerRegions(ExcHandlersRegionMaker.java:53)
            	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.process(ExcHandlersRegionMaker.java:38)
            	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:27)
            */
        static {
            /*
                android.arch.lifecycle.Lifecycle$Event[] r0 = android.arch.lifecycle.Lifecycle.Event.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                r4 = r0
                r0 = r4
                android.arch.lifecycle.FullLifecycleObserverAdapter.C00071.$SwitchMap$android$arch$lifecycle$Lifecycle$Event = r0
                r0 = r4
                android.arch.lifecycle.Lifecycle$Event r1 = android.arch.lifecycle.Lifecycle.Event.ON_CREATE     // Catch: java.lang.NoSuchFieldError -> L59
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L59
                r2 = 1
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L59
            L14:
                int[] r0 = android.arch.lifecycle.FullLifecycleObserverAdapter.C00071.$SwitchMap$android$arch$lifecycle$Lifecycle$Event     // Catch: java.lang.NoSuchFieldError -> L59 java.lang.NoSuchFieldError -> L5d
                android.arch.lifecycle.Lifecycle$Event r1 = android.arch.lifecycle.Lifecycle.Event.ON_START     // Catch: java.lang.NoSuchFieldError -> L5d
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L5d
                r2 = 2
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L5d
            L1f:
                int[] r0 = android.arch.lifecycle.FullLifecycleObserverAdapter.C00071.$SwitchMap$android$arch$lifecycle$Lifecycle$Event     // Catch: java.lang.NoSuchFieldError -> L5d java.lang.NoSuchFieldError -> L61
                android.arch.lifecycle.Lifecycle$Event r1 = android.arch.lifecycle.Lifecycle.Event.ON_RESUME     // Catch: java.lang.NoSuchFieldError -> L61
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L61
                r2 = 3
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L61
            L2a:
                int[] r0 = android.arch.lifecycle.FullLifecycleObserverAdapter.C00071.$SwitchMap$android$arch$lifecycle$Lifecycle$Event     // Catch: java.lang.NoSuchFieldError -> L61 java.lang.NoSuchFieldError -> L65
                android.arch.lifecycle.Lifecycle$Event r1 = android.arch.lifecycle.Lifecycle.Event.ON_PAUSE     // Catch: java.lang.NoSuchFieldError -> L65
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L65
                r2 = 4
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L65
            L35:
                int[] r0 = android.arch.lifecycle.FullLifecycleObserverAdapter.C00071.$SwitchMap$android$arch$lifecycle$Lifecycle$Event     // Catch: java.lang.NoSuchFieldError -> L65 java.lang.NoSuchFieldError -> L69
                android.arch.lifecycle.Lifecycle$Event r1 = android.arch.lifecycle.Lifecycle.Event.ON_STOP     // Catch: java.lang.NoSuchFieldError -> L69
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L69
                r2 = 5
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L69
            L40:
                int[] r0 = android.arch.lifecycle.FullLifecycleObserverAdapter.C00071.$SwitchMap$android$arch$lifecycle$Lifecycle$Event     // Catch: java.lang.NoSuchFieldError -> L69 java.lang.NoSuchFieldError -> L6d
                android.arch.lifecycle.Lifecycle$Event r1 = android.arch.lifecycle.Lifecycle.Event.ON_DESTROY     // Catch: java.lang.NoSuchFieldError -> L6d
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L6d
                r2 = 6
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L6d
            L4c:
                int[] r0 = android.arch.lifecycle.FullLifecycleObserverAdapter.C00071.$SwitchMap$android$arch$lifecycle$Lifecycle$Event     // Catch: java.lang.NoSuchFieldError -> L6d java.lang.NoSuchFieldError -> L71
                android.arch.lifecycle.Lifecycle$Event r1 = android.arch.lifecycle.Lifecycle.Event.ON_ANY     // Catch: java.lang.NoSuchFieldError -> L71
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L71
                r2 = 7
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L71
            L58:
                return
            L59:
                r4 = move-exception
                goto L14
            L5d:
                r4 = move-exception
                goto L1f
            L61:
                r4 = move-exception
                goto L2a
            L65:
                r4 = move-exception
                goto L35
            L69:
                r4 = move-exception
                goto L40
            L6d:
                r4 = move-exception
                goto L4c
            L71:
                r4 = move-exception
                goto L58
            */
            throw new UnsupportedOperationException("Method not decompiled: android.arch.lifecycle.FullLifecycleObserverAdapter.C00071.m24clinit():void");
        }
    }

    FullLifecycleObserverAdapter(FullLifecycleObserver fullLifecycleObserver) {
        this.mObserver = fullLifecycleObserver;
    }

    @Override // android.arch.lifecycle.GenericLifecycleObserver
    public void onStateChanged(LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
        switch (C00071.$SwitchMap$android$arch$lifecycle$Lifecycle$Event[event.ordinal()]) {
            case 1:
                this.mObserver.onCreate(lifecycleOwner);
                return;
            case 2:
                this.mObserver.onStart(lifecycleOwner);
                return;
            case 3:
                this.mObserver.onResume(lifecycleOwner);
                return;
            case 4:
                this.mObserver.onPause(lifecycleOwner);
                return;
            case 5:
                this.mObserver.onStop(lifecycleOwner);
                return;
            case 6:
                this.mObserver.onDestroy(lifecycleOwner);
                return;
            case 7:
                throw new IllegalArgumentException("ON_ANY must not been send by anybody");
            default:
                return;
        }
    }
}
