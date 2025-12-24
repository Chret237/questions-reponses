package android.arch.lifecycle;

import java.util.HashMap;
import java.util.Iterator;

/* loaded from: classes-dex2jar.jar:android/arch/lifecycle/ViewModelStore.class */
public class ViewModelStore {
    private final HashMap<String, ViewModel> mMap = new HashMap<>();

    public final void clear() {
        Iterator<ViewModel> it = this.mMap.values().iterator();
        while (it.hasNext()) {
            it.next().onCleared();
        }
        this.mMap.clear();
    }

    final ViewModel get(String str) {
        return this.mMap.get(str);
    }

    final void put(String str, ViewModel viewModel) {
        ViewModel viewModelPut = this.mMap.put(str, viewModel);
        if (viewModelPut != null) {
            viewModelPut.onCleared();
        }
    }
}
