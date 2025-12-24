package android.support.v7.util;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

/* loaded from: classes-dex2jar.jar:android/support/v7/util/SortedList.class */
public class SortedList<T> {
    private static final int CAPACITY_GROWTH = 10;
    private static final int DELETION = 2;
    private static final int INSERTION = 1;
    public static final int INVALID_POSITION = -1;
    private static final int LOOKUP = 4;
    private static final int MIN_CAPACITY = 10;
    private BatchedCallback mBatchedCallback;
    private Callback mCallback;
    T[] mData;
    private int mNewDataStart;
    private T[] mOldData;
    private int mOldDataSize;
    private int mOldDataStart;
    private int mSize;
    private final Class<T> mTClass;

    /* loaded from: classes-dex2jar.jar:android/support/v7/util/SortedList$BatchedCallback.class */
    public static class BatchedCallback<T2> extends Callback<T2> {
        private final BatchingListUpdateCallback mBatchingListUpdateCallback;
        final Callback<T2> mWrappedCallback;

        public BatchedCallback(Callback<T2> callback) {
            this.mWrappedCallback = callback;
            this.mBatchingListUpdateCallback = new BatchingListUpdateCallback(callback);
        }

        @Override // android.support.v7.util.SortedList.Callback
        public boolean areContentsTheSame(T2 t2, T2 t22) {
            return this.mWrappedCallback.areContentsTheSame(t2, t22);
        }

        @Override // android.support.v7.util.SortedList.Callback
        public boolean areItemsTheSame(T2 t2, T2 t22) {
            return this.mWrappedCallback.areItemsTheSame(t2, t22);
        }

        @Override // android.support.v7.util.SortedList.Callback, java.util.Comparator
        public int compare(T2 t2, T2 t22) {
            return this.mWrappedCallback.compare(t2, t22);
        }

        public void dispatchLastEvent() {
            this.mBatchingListUpdateCallback.dispatchLastEvent();
        }

        @Override // android.support.v7.util.SortedList.Callback
        public Object getChangePayload(T2 t2, T2 t22) {
            return this.mWrappedCallback.getChangePayload(t2, t22);
        }

        @Override // android.support.v7.util.SortedList.Callback
        public void onChanged(int i, int i2) {
            this.mBatchingListUpdateCallback.onChanged(i, i2, null);
        }

        @Override // android.support.v7.util.SortedList.Callback, android.support.v7.util.ListUpdateCallback
        public void onChanged(int i, int i2, Object obj) {
            this.mBatchingListUpdateCallback.onChanged(i, i2, obj);
        }

        @Override // android.support.v7.util.ListUpdateCallback
        public void onInserted(int i, int i2) {
            this.mBatchingListUpdateCallback.onInserted(i, i2);
        }

        @Override // android.support.v7.util.ListUpdateCallback
        public void onMoved(int i, int i2) {
            this.mBatchingListUpdateCallback.onMoved(i, i2);
        }

        @Override // android.support.v7.util.ListUpdateCallback
        public void onRemoved(int i, int i2) {
            this.mBatchingListUpdateCallback.onRemoved(i, i2);
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v7/util/SortedList$Callback.class */
    public static abstract class Callback<T2> implements Comparator<T2>, ListUpdateCallback {
        public abstract boolean areContentsTheSame(T2 t2, T2 t22);

        public abstract boolean areItemsTheSame(T2 t2, T2 t22);

        @Override // java.util.Comparator
        public abstract int compare(T2 t2, T2 t22);

        public Object getChangePayload(T2 t2, T2 t22) {
            return null;
        }

        public abstract void onChanged(int i, int i2);

        public void onChanged(int i, int i2, Object obj) {
            onChanged(i, i2);
        }
    }

    public SortedList(Class<T> cls, Callback<T> callback) {
        this(cls, callback, 10);
    }

    public SortedList(Class<T> cls, Callback<T> callback, int i) {
        this.mTClass = cls;
        this.mData = (T[]) ((Object[]) Array.newInstance((Class<?>) cls, i));
        this.mCallback = callback;
        this.mSize = 0;
    }

    private int add(T t, boolean z) {
        int i;
        int iFindIndexOf = findIndexOf(t, this.mData, 0, this.mSize, 1);
        if (iFindIndexOf == -1) {
            i = 0;
        } else {
            i = iFindIndexOf;
            if (iFindIndexOf < this.mSize) {
                T t2 = this.mData[iFindIndexOf];
                i = iFindIndexOf;
                if (this.mCallback.areItemsTheSame(t2, t)) {
                    if (this.mCallback.areContentsTheSame(t2, t)) {
                        this.mData[iFindIndexOf] = t;
                        return iFindIndexOf;
                    }
                    this.mData[iFindIndexOf] = t;
                    Callback callback = this.mCallback;
                    callback.onChanged(iFindIndexOf, 1, callback.getChangePayload(t2, t));
                    return iFindIndexOf;
                }
            }
        }
        addToData(i, t);
        if (z) {
            this.mCallback.onInserted(i, 1);
        }
        return i;
    }

    private void addAllInternal(T[] tArr) {
        if (tArr.length < 1) {
            return;
        }
        int iSortAndDedup = sortAndDedup(tArr);
        if (this.mSize != 0) {
            merge(tArr, iSortAndDedup);
            return;
        }
        this.mData = tArr;
        this.mSize = iSortAndDedup;
        this.mCallback.onInserted(0, iSortAndDedup);
    }

    private void addToData(int i, T t) {
        int i2 = this.mSize;
        if (i > i2) {
            throw new IndexOutOfBoundsException("cannot add item to " + i + " because size is " + this.mSize);
        }
        T[] tArr = this.mData;
        if (i2 == tArr.length) {
            T[] tArr2 = (T[]) ((Object[]) Array.newInstance((Class<?>) this.mTClass, tArr.length + 10));
            System.arraycopy(this.mData, 0, tArr2, 0, i);
            tArr2[i] = t;
            System.arraycopy(this.mData, i, tArr2, i + 1, this.mSize - i);
            this.mData = tArr2;
        } else {
            System.arraycopy(tArr, i, tArr, i + 1, i2 - i);
            this.mData[i] = t;
        }
        this.mSize++;
    }

    private T[] copyArray(T[] tArr) {
        T[] tArr2 = (T[]) ((Object[]) Array.newInstance((Class<?>) this.mTClass, tArr.length));
        System.arraycopy(tArr, 0, tArr2, 0, tArr.length);
        return tArr2;
    }

    private int findIndexOf(T t, T[] tArr, int i, int i2, int i3) {
        while (i < i2) {
            int i4 = (i + i2) / 2;
            T t2 = tArr[i4];
            int iCompare = this.mCallback.compare(t2, t);
            if (iCompare < 0) {
                i = i4 + 1;
            } else {
                if (iCompare == 0) {
                    if (this.mCallback.areItemsTheSame(t2, t)) {
                        return i4;
                    }
                    int iLinearEqualitySearch = linearEqualitySearch(t, i4, i, i2);
                    if (i3 != 1) {
                        return iLinearEqualitySearch;
                    }
                    if (iLinearEqualitySearch == -1) {
                        iLinearEqualitySearch = i4;
                    }
                    return iLinearEqualitySearch;
                }
                i2 = i4;
            }
        }
        if (i3 != 1) {
            i = -1;
        }
        return i;
    }

    private int findSameItem(T t, T[] tArr, int i, int i2) {
        while (i < i2) {
            if (this.mCallback.areItemsTheSame(tArr[i], t)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x0040, code lost:
    
        r0 = r9 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x0048, code lost:
    
        if (r0 >= r8) goto L29;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x004b, code lost:
    
        r0 = r4.mData[r0];
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x005d, code lost:
    
        if (r4.mCallback.compare(r0, r5) == 0) goto L18;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0063, code lost:
    
        r9 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0070, code lost:
    
        if (r4.mCallback.areItemsTheSame(r0, r5) == false) goto L31;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0074, code lost:
    
        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0075, code lost:
    
        return -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:?, code lost:
    
        return -1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private int linearEqualitySearch(T r5, int r6, int r7, int r8) {
        /*
            r4 = this;
            r0 = r6
            r1 = 1
            int r0 = r0 - r1
            r10 = r0
        L5:
            r0 = r6
            r9 = r0
            r0 = r10
            r1 = r7
            if (r0 < r1) goto L40
            r0 = r4
            T[] r0 = r0.mData
            r1 = r10
            r0 = r0[r1]
            r11 = r0
            r0 = r4
            android.support.v7.util.SortedList$Callback r0 = r0.mCallback
            r1 = r11
            r2 = r5
            int r0 = r0.compare(r1, r2)
            if (r0 == 0) goto L2a
            r0 = r6
            r9 = r0
            goto L40
        L2a:
            r0 = r4
            android.support.v7.util.SortedList$Callback r0 = r0.mCallback
            r1 = r11
            r2 = r5
            boolean r0 = r0.areItemsTheSame(r1, r2)
            if (r0 == 0) goto L3a
            r0 = r10
            return r0
        L3a:
            int r10 = r10 + (-1)
            goto L5
        L40:
            r0 = r9
            r1 = 1
            int r0 = r0 + r1
            r6 = r0
            r0 = r6
            r1 = r8
            if (r0 >= r1) goto L75
            r0 = r4
            T[] r0 = r0.mData
            r1 = r6
            r0 = r0[r1]
            r11 = r0
            r0 = r4
            android.support.v7.util.SortedList$Callback r0 = r0.mCallback
            r1 = r11
            r2 = r5
            int r0 = r0.compare(r1, r2)
            if (r0 == 0) goto L63
            goto L75
        L63:
            r0 = r6
            r9 = r0
            r0 = r4
            android.support.v7.util.SortedList$Callback r0 = r0.mCallback
            r1 = r11
            r2 = r5
            boolean r0 = r0.areItemsTheSame(r1, r2)
            if (r0 == 0) goto L40
            r0 = r6
            return r0
        L75:
            r0 = -1
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v7.util.SortedList.linearEqualitySearch(java.lang.Object, int, int, int):int");
    }

    private void merge(T[] tArr, int i) {
        boolean z = !(this.mCallback instanceof BatchedCallback);
        if (z) {
            beginBatchedUpdates();
        }
        this.mOldData = this.mData;
        int i2 = 0;
        this.mOldDataStart = 0;
        int i3 = this.mSize;
        this.mOldDataSize = i3;
        this.mData = (T[]) ((Object[]) Array.newInstance((Class<?>) this.mTClass, i3 + i + 10));
        this.mNewDataStart = 0;
        while (true) {
            if (this.mOldDataStart >= this.mOldDataSize && i2 >= i) {
                break;
            }
            int i4 = this.mOldDataStart;
            int i5 = this.mOldDataSize;
            if (i4 == i5) {
                int i6 = i - i2;
                System.arraycopy(tArr, i2, this.mData, this.mNewDataStart, i6);
                int i7 = this.mNewDataStart + i6;
                this.mNewDataStart = i7;
                this.mSize += i6;
                this.mCallback.onInserted(i7 - i6, i6);
                break;
            }
            if (i2 == i) {
                int i8 = i5 - i4;
                System.arraycopy(this.mOldData, i4, this.mData, this.mNewDataStart, i8);
                this.mNewDataStart += i8;
                break;
            }
            T t = this.mOldData[i4];
            T t2 = tArr[i2];
            int iCompare = this.mCallback.compare(t, t2);
            if (iCompare > 0) {
                T[] tArr2 = this.mData;
                int i9 = this.mNewDataStart;
                int i10 = i9 + 1;
                this.mNewDataStart = i10;
                tArr2[i9] = t2;
                this.mSize++;
                i2++;
                this.mCallback.onInserted(i10 - 1, 1);
            } else if (iCompare == 0 && this.mCallback.areItemsTheSame(t, t2)) {
                T[] tArr3 = this.mData;
                int i11 = this.mNewDataStart;
                this.mNewDataStart = i11 + 1;
                tArr3[i11] = t2;
                int i12 = i2 + 1;
                this.mOldDataStart++;
                i2 = i12;
                if (!this.mCallback.areContentsTheSame(t, t2)) {
                    Callback callback = this.mCallback;
                    callback.onChanged(this.mNewDataStart - 1, 1, callback.getChangePayload(t, t2));
                    i2 = i12;
                }
            } else {
                T[] tArr4 = this.mData;
                int i13 = this.mNewDataStart;
                this.mNewDataStart = i13 + 1;
                tArr4[i13] = t;
                this.mOldDataStart++;
            }
        }
        this.mOldData = null;
        if (z) {
            endBatchedUpdates();
        }
    }

    private boolean remove(T t, boolean z) {
        int iFindIndexOf = findIndexOf(t, this.mData, 0, this.mSize, 2);
        if (iFindIndexOf == -1) {
            return false;
        }
        removeItemAtIndex(iFindIndexOf, z);
        return true;
    }

    private void removeItemAtIndex(int i, boolean z) {
        T[] tArr = this.mData;
        System.arraycopy(tArr, i + 1, tArr, i, (this.mSize - i) - 1);
        int i2 = this.mSize - 1;
        this.mSize = i2;
        this.mData[i2] = null;
        if (z) {
            this.mCallback.onRemoved(i, 1);
        }
    }

    private void replaceAllInsert(T t) {
        T[] tArr = this.mData;
        int i = this.mNewDataStart;
        tArr[i] = t;
        int i2 = i + 1;
        this.mNewDataStart = i2;
        this.mSize++;
        this.mCallback.onInserted(i2 - 1, 1);
    }

    private void replaceAllInternal(T[] tArr) {
        boolean z = !(this.mCallback instanceof BatchedCallback);
        if (z) {
            beginBatchedUpdates();
        }
        this.mOldDataStart = 0;
        this.mOldDataSize = this.mSize;
        this.mOldData = this.mData;
        this.mNewDataStart = 0;
        int iSortAndDedup = sortAndDedup(tArr);
        this.mData = (T[]) ((Object[]) Array.newInstance((Class<?>) this.mTClass, iSortAndDedup));
        while (true) {
            if (this.mNewDataStart >= iSortAndDedup && this.mOldDataStart >= this.mOldDataSize) {
                break;
            }
            int i = this.mOldDataStart;
            int i2 = this.mOldDataSize;
            if (i >= i2) {
                int i3 = this.mNewDataStart;
                int i4 = iSortAndDedup - i3;
                System.arraycopy(tArr, i3, this.mData, i3, i4);
                this.mNewDataStart += i4;
                this.mSize += i4;
                this.mCallback.onInserted(i3, i4);
                break;
            }
            int i5 = this.mNewDataStart;
            if (i5 >= iSortAndDedup) {
                int i6 = i2 - i;
                this.mSize -= i6;
                this.mCallback.onRemoved(i5, i6);
                break;
            }
            T t = this.mOldData[i];
            T t2 = tArr[i5];
            int iCompare = this.mCallback.compare(t, t2);
            if (iCompare < 0) {
                replaceAllRemove();
            } else if (iCompare > 0) {
                replaceAllInsert(t2);
            } else if (this.mCallback.areItemsTheSame(t, t2)) {
                T[] tArr2 = this.mData;
                int i7 = this.mNewDataStart;
                tArr2[i7] = t2;
                this.mOldDataStart++;
                this.mNewDataStart = i7 + 1;
                if (!this.mCallback.areContentsTheSame(t, t2)) {
                    Callback callback = this.mCallback;
                    callback.onChanged(this.mNewDataStart - 1, 1, callback.getChangePayload(t, t2));
                }
            } else {
                replaceAllRemove();
                replaceAllInsert(t2);
            }
        }
        this.mOldData = null;
        if (z) {
            endBatchedUpdates();
        }
    }

    private void replaceAllRemove() {
        this.mSize--;
        this.mOldDataStart++;
        this.mCallback.onRemoved(this.mNewDataStart, 1);
    }

    private int sortAndDedup(T[] tArr) {
        if (tArr.length == 0) {
            return 0;
        }
        Arrays.sort(tArr, this.mCallback);
        int i = 1;
        int i2 = 0;
        for (int i3 = 1; i3 < tArr.length; i3++) {
            T t = tArr[i3];
            if (this.mCallback.compare(tArr[i2], t) == 0) {
                int iFindSameItem = findSameItem(t, tArr, i2, i);
                if (iFindSameItem != -1) {
                    tArr[iFindSameItem] = t;
                } else {
                    if (i != i3) {
                        tArr[i] = t;
                    }
                    i++;
                }
            } else {
                if (i != i3) {
                    tArr[i] = t;
                }
                i2 = i;
                i++;
            }
        }
        return i;
    }

    private void throwIfInMutationOperation() {
        if (this.mOldData != null) {
            throw new IllegalStateException("Data cannot be mutated in the middle of a batch update operation such as addAll or replaceAll.");
        }
    }

    public int add(T t) {
        throwIfInMutationOperation();
        return add(t, true);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void addAll(Collection<T> collection) {
        addAll(collection.toArray((Object[]) Array.newInstance((Class<?>) this.mTClass, collection.size())), true);
    }

    public void addAll(T... tArr) {
        addAll(tArr, false);
    }

    public void addAll(T[] tArr, boolean z) {
        throwIfInMutationOperation();
        if (tArr.length == 0) {
            return;
        }
        if (z) {
            addAllInternal(tArr);
        } else {
            addAllInternal(copyArray(tArr));
        }
    }

    public void beginBatchedUpdates() {
        throwIfInMutationOperation();
        Callback callback = this.mCallback;
        if (callback instanceof BatchedCallback) {
            return;
        }
        if (this.mBatchedCallback == null) {
            this.mBatchedCallback = new BatchedCallback(callback);
        }
        this.mCallback = this.mBatchedCallback;
    }

    public void clear() {
        throwIfInMutationOperation();
        int i = this.mSize;
        if (i == 0) {
            return;
        }
        Arrays.fill(this.mData, 0, i, (Object) null);
        this.mSize = 0;
        this.mCallback.onRemoved(0, i);
    }

    public void endBatchedUpdates() {
        throwIfInMutationOperation();
        Callback callback = this.mCallback;
        if (callback instanceof BatchedCallback) {
            ((BatchedCallback) callback).dispatchLastEvent();
        }
        Callback callback2 = this.mCallback;
        BatchedCallback batchedCallback = this.mBatchedCallback;
        if (callback2 == batchedCallback) {
            this.mCallback = batchedCallback.mWrappedCallback;
        }
    }

    public T get(int i) throws IndexOutOfBoundsException {
        int i2;
        if (i < this.mSize && i >= 0) {
            T[] tArr = this.mOldData;
            return (tArr == null || i < (i2 = this.mNewDataStart)) ? this.mData[i] : tArr[(i - i2) + this.mOldDataStart];
        }
        throw new IndexOutOfBoundsException("Asked to get item at " + i + " but size is " + this.mSize);
    }

    public int indexOf(T t) {
        if (this.mOldData == null) {
            return findIndexOf(t, this.mData, 0, this.mSize, 4);
        }
        int iFindIndexOf = findIndexOf(t, this.mData, 0, this.mNewDataStart, 4);
        if (iFindIndexOf != -1) {
            return iFindIndexOf;
        }
        int iFindIndexOf2 = findIndexOf(t, this.mOldData, this.mOldDataStart, this.mOldDataSize, 4);
        if (iFindIndexOf2 != -1) {
            return (iFindIndexOf2 - this.mOldDataStart) + this.mNewDataStart;
        }
        return -1;
    }

    public void recalculatePositionOfItemAt(int i) throws IndexOutOfBoundsException {
        throwIfInMutationOperation();
        T t = get(i);
        removeItemAtIndex(i, false);
        int iAdd = add(t, false);
        if (i != iAdd) {
            this.mCallback.onMoved(i, iAdd);
        }
    }

    public boolean remove(T t) {
        throwIfInMutationOperation();
        return remove(t, true);
    }

    public T removeItemAt(int i) throws IndexOutOfBoundsException {
        throwIfInMutationOperation();
        T t = get(i);
        removeItemAtIndex(i, true);
        return t;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void replaceAll(Collection<T> collection) {
        replaceAll(collection.toArray((Object[]) Array.newInstance((Class<?>) this.mTClass, collection.size())), true);
    }

    public void replaceAll(T... tArr) {
        replaceAll(tArr, false);
    }

    public void replaceAll(T[] tArr, boolean z) {
        throwIfInMutationOperation();
        if (z) {
            replaceAllInternal(tArr);
        } else {
            replaceAllInternal(copyArray(tArr));
        }
    }

    public int size() {
        return this.mSize;
    }

    public void updateItemAt(int i, T t) throws IndexOutOfBoundsException {
        throwIfInMutationOperation();
        T t2 = get(i);
        boolean z = t2 == t || !this.mCallback.areContentsTheSame(t2, t);
        if (t2 != t && this.mCallback.compare(t2, t) == 0) {
            this.mData[i] = t;
            if (z) {
                Callback callback = this.mCallback;
                callback.onChanged(i, 1, callback.getChangePayload(t2, t));
                return;
            }
            return;
        }
        if (z) {
            Callback callback2 = this.mCallback;
            callback2.onChanged(i, 1, callback2.getChangePayload(t2, t));
        }
        removeItemAtIndex(i, false);
        int iAdd = add(t, false);
        if (i != iAdd) {
            this.mCallback.onMoved(i, iAdd);
        }
    }
}
