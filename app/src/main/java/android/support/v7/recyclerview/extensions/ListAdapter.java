package android.support.v7.recyclerview.extensions;

import android.support.v7.recyclerview.extensions.AsyncDifferConfig;
import android.support.v7.util.AdapterListUpdateCallback;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import java.util.List;

/* loaded from: classes-dex2jar.jar:android/support/v7/recyclerview/extensions/ListAdapter.class */
public abstract class ListAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    private final AsyncListDiffer<T> mHelper;

    protected ListAdapter(AsyncDifferConfig<T> asyncDifferConfig) {
        this.mHelper = new AsyncListDiffer<>(new AdapterListUpdateCallback(this), asyncDifferConfig);
    }

    protected ListAdapter(DiffUtil.ItemCallback<T> itemCallback) {
        this.mHelper = new AsyncListDiffer<>(new AdapterListUpdateCallback(this), new AsyncDifferConfig.Builder(itemCallback).build());
    }

    protected T getItem(int i) {
        return this.mHelper.getCurrentList().get(i);
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mHelper.getCurrentList().size();
    }

    public void submitList(List<T> list) {
        this.mHelper.submitList(list);
    }
}
