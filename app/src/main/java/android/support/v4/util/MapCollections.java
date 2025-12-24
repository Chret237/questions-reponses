package android.support.v4.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/* loaded from: classes-dex2jar.jar:android/support/v4/util/MapCollections.class */
abstract class MapCollections<K, V> {
    MapCollections<K, V>.EntrySet mEntrySet;
    MapCollections<K, V>.KeySet mKeySet;
    MapCollections<K, V>.ValuesCollection mValues;

    /* loaded from: classes-dex2jar.jar:android/support/v4/util/MapCollections$ArrayIterator.class */
    final class ArrayIterator<T> implements Iterator<T> {
        boolean mCanRemove = false;
        int mIndex;
        final int mOffset;
        int mSize;
        final MapCollections this$0;

        ArrayIterator(MapCollections mapCollections, int i) {
            this.this$0 = mapCollections;
            this.mOffset = i;
            this.mSize = mapCollections.colGetSize();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.mIndex < this.mSize;
        }

        @Override // java.util.Iterator
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T t = (T) this.this$0.colGetEntry(this.mIndex, this.mOffset);
            this.mIndex++;
            this.mCanRemove = true;
            return t;
        }

        @Override // java.util.Iterator
        public void remove() {
            if (!this.mCanRemove) {
                throw new IllegalStateException();
            }
            int i = this.mIndex - 1;
            this.mIndex = i;
            this.mSize--;
            this.mCanRemove = false;
            this.this$0.colRemoveAt(i);
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v4/util/MapCollections$EntrySet.class */
    final class EntrySet implements Set<Map.Entry<K, V>> {
        final MapCollections this$0;

        EntrySet(MapCollections mapCollections) {
            this.this$0 = mapCollections;
        }

        @Override // java.util.Set, java.util.Collection
        public boolean add(Map.Entry<K, V> entry) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Set, java.util.Collection
        public boolean addAll(Collection<? extends Map.Entry<K, V>> collection) {
            int iColGetSize = this.this$0.colGetSize();
            for (Map.Entry<K, V> entry : collection) {
                this.this$0.colPut(entry.getKey(), entry.getValue());
            }
            return iColGetSize != this.this$0.colGetSize();
        }

        @Override // java.util.Set, java.util.Collection
        public void clear() {
            this.this$0.colClear();
        }

        @Override // java.util.Set, java.util.Collection
        public boolean contains(Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            int iColIndexOfKey = this.this$0.colIndexOfKey(entry.getKey());
            if (iColIndexOfKey < 0) {
                return false;
            }
            return ContainerHelpers.equal(this.this$0.colGetEntry(iColIndexOfKey, 1), entry.getValue());
        }

        @Override // java.util.Set, java.util.Collection
        public boolean containsAll(Collection<?> collection) {
            Iterator<?> it = collection.iterator();
            while (it.hasNext()) {
                if (!contains(it.next())) {
                    return false;
                }
            }
            return true;
        }

        @Override // java.util.Set, java.util.Collection
        public boolean equals(Object obj) {
            return MapCollections.equalsSetHelper(this, obj);
        }

        @Override // java.util.Set, java.util.Collection
        public int hashCode() {
            int iHashCode = 0;
            for (int iColGetSize = this.this$0.colGetSize() - 1; iColGetSize >= 0; iColGetSize--) {
                Object objColGetEntry = this.this$0.colGetEntry(iColGetSize, 0);
                Object objColGetEntry2 = this.this$0.colGetEntry(iColGetSize, 1);
                iHashCode += (objColGetEntry == null ? 0 : objColGetEntry.hashCode()) ^ (objColGetEntry2 == null ? 0 : objColGetEntry2.hashCode());
            }
            return iHashCode;
        }

        @Override // java.util.Set, java.util.Collection
        public boolean isEmpty() {
            return this.this$0.colGetSize() == 0;
        }

        @Override // java.util.Set, java.util.Collection, java.lang.Iterable
        public Iterator<Map.Entry<K, V>> iterator() {
            return new MapIterator(this.this$0);
        }

        @Override // java.util.Set, java.util.Collection
        public boolean remove(Object obj) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Set, java.util.Collection
        public boolean removeAll(Collection<?> collection) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Set, java.util.Collection
        public boolean retainAll(Collection<?> collection) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Set, java.util.Collection
        public int size() {
            return this.this$0.colGetSize();
        }

        @Override // java.util.Set, java.util.Collection
        public Object[] toArray() {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Set, java.util.Collection
        public <T> T[] toArray(T[] tArr) {
            throw new UnsupportedOperationException();
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v4/util/MapCollections$KeySet.class */
    final class KeySet implements Set<K> {
        final MapCollections this$0;

        KeySet(MapCollections mapCollections) {
            this.this$0 = mapCollections;
        }

        @Override // java.util.Set, java.util.Collection
        public boolean add(K k) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Set, java.util.Collection
        public boolean addAll(Collection<? extends K> collection) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Set, java.util.Collection
        public void clear() {
            this.this$0.colClear();
        }

        @Override // java.util.Set, java.util.Collection
        public boolean contains(Object obj) {
            return this.this$0.colIndexOfKey(obj) >= 0;
        }

        @Override // java.util.Set, java.util.Collection
        public boolean containsAll(Collection<?> collection) {
            return MapCollections.containsAllHelper(this.this$0.colGetMap(), collection);
        }

        @Override // java.util.Set, java.util.Collection
        public boolean equals(Object obj) {
            return MapCollections.equalsSetHelper(this, obj);
        }

        @Override // java.util.Set, java.util.Collection
        public int hashCode() {
            int iHashCode = 0;
            for (int iColGetSize = this.this$0.colGetSize() - 1; iColGetSize >= 0; iColGetSize--) {
                Object objColGetEntry = this.this$0.colGetEntry(iColGetSize, 0);
                iHashCode += objColGetEntry == null ? 0 : objColGetEntry.hashCode();
            }
            return iHashCode;
        }

        @Override // java.util.Set, java.util.Collection
        public boolean isEmpty() {
            return this.this$0.colGetSize() == 0;
        }

        @Override // java.util.Set, java.util.Collection, java.lang.Iterable
        public Iterator<K> iterator() {
            return new ArrayIterator(this.this$0, 0);
        }

        @Override // java.util.Set, java.util.Collection
        public boolean remove(Object obj) {
            int iColIndexOfKey = this.this$0.colIndexOfKey(obj);
            if (iColIndexOfKey < 0) {
                return false;
            }
            this.this$0.colRemoveAt(iColIndexOfKey);
            return true;
        }

        @Override // java.util.Set, java.util.Collection
        public boolean removeAll(Collection<?> collection) {
            return MapCollections.removeAllHelper(this.this$0.colGetMap(), collection);
        }

        @Override // java.util.Set, java.util.Collection
        public boolean retainAll(Collection<?> collection) {
            return MapCollections.retainAllHelper(this.this$0.colGetMap(), collection);
        }

        @Override // java.util.Set, java.util.Collection
        public int size() {
            return this.this$0.colGetSize();
        }

        @Override // java.util.Set, java.util.Collection
        public Object[] toArray() {
            return this.this$0.toArrayHelper(0);
        }

        @Override // java.util.Set, java.util.Collection
        public <T> T[] toArray(T[] tArr) {
            return (T[]) this.this$0.toArrayHelper(tArr, 0);
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v4/util/MapCollections$MapIterator.class */
    final class MapIterator implements Iterator<Map.Entry<K, V>>, Map.Entry<K, V> {
        int mEnd;
        boolean mEntryValid = false;
        int mIndex = -1;
        final MapCollections this$0;

        MapIterator(MapCollections mapCollections) {
            this.this$0 = mapCollections;
            this.mEnd = mapCollections.colGetSize() - 1;
        }

        @Override // java.util.Map.Entry
        public boolean equals(Object obj) {
            if (!this.mEntryValid) {
                throw new IllegalStateException("This container does not support retaining Map.Entry objects");
            }
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            boolean z = false;
            if (ContainerHelpers.equal(entry.getKey(), this.this$0.colGetEntry(this.mIndex, 0))) {
                z = false;
                if (ContainerHelpers.equal(entry.getValue(), this.this$0.colGetEntry(this.mIndex, 1))) {
                    z = true;
                }
            }
            return z;
        }

        @Override // java.util.Map.Entry
        public K getKey() {
            if (this.mEntryValid) {
                return (K) this.this$0.colGetEntry(this.mIndex, 0);
            }
            throw new IllegalStateException("This container does not support retaining Map.Entry objects");
        }

        @Override // java.util.Map.Entry
        public V getValue() {
            if (this.mEntryValid) {
                return (V) this.this$0.colGetEntry(this.mIndex, 1);
            }
            throw new IllegalStateException("This container does not support retaining Map.Entry objects");
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.mIndex < this.mEnd;
        }

        @Override // java.util.Map.Entry
        public int hashCode() {
            if (!this.mEntryValid) {
                throw new IllegalStateException("This container does not support retaining Map.Entry objects");
            }
            int iHashCode = 0;
            Object objColGetEntry = this.this$0.colGetEntry(this.mIndex, 0);
            Object objColGetEntry2 = this.this$0.colGetEntry(this.mIndex, 1);
            int iHashCode2 = objColGetEntry == null ? 0 : objColGetEntry.hashCode();
            if (objColGetEntry2 != null) {
                iHashCode = objColGetEntry2.hashCode();
            }
            return iHashCode2 ^ iHashCode;
        }

        @Override // java.util.Iterator
        public Map.Entry<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            this.mIndex++;
            this.mEntryValid = true;
            return this;
        }

        @Override // java.util.Iterator
        public void remove() {
            if (!this.mEntryValid) {
                throw new IllegalStateException();
            }
            this.this$0.colRemoveAt(this.mIndex);
            this.mIndex--;
            this.mEnd--;
            this.mEntryValid = false;
        }

        @Override // java.util.Map.Entry
        public V setValue(V v) {
            if (this.mEntryValid) {
                return (V) this.this$0.colSetValue(this.mIndex, v);
            }
            throw new IllegalStateException("This container does not support retaining Map.Entry objects");
        }

        public String toString() {
            return getKey() + "=" + getValue();
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v4/util/MapCollections$ValuesCollection.class */
    final class ValuesCollection implements Collection<V> {
        final MapCollections this$0;

        ValuesCollection(MapCollections mapCollections) {
            this.this$0 = mapCollections;
        }

        @Override // java.util.Collection
        public boolean add(V v) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Collection
        public boolean addAll(Collection<? extends V> collection) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Collection
        public void clear() {
            this.this$0.colClear();
        }

        @Override // java.util.Collection
        public boolean contains(Object obj) {
            return this.this$0.colIndexOfValue(obj) >= 0;
        }

        @Override // java.util.Collection
        public boolean containsAll(Collection<?> collection) {
            Iterator<?> it = collection.iterator();
            while (it.hasNext()) {
                if (!contains(it.next())) {
                    return false;
                }
            }
            return true;
        }

        @Override // java.util.Collection
        public boolean isEmpty() {
            return this.this$0.colGetSize() == 0;
        }

        @Override // java.util.Collection, java.lang.Iterable
        public Iterator<V> iterator() {
            return new ArrayIterator(this.this$0, 1);
        }

        @Override // java.util.Collection
        public boolean remove(Object obj) {
            int iColIndexOfValue = this.this$0.colIndexOfValue(obj);
            if (iColIndexOfValue < 0) {
                return false;
            }
            this.this$0.colRemoveAt(iColIndexOfValue);
            return true;
        }

        @Override // java.util.Collection
        public boolean removeAll(Collection<?> collection) {
            int iColGetSize = this.this$0.colGetSize();
            int i = 0;
            boolean z = false;
            while (i < iColGetSize) {
                int i2 = iColGetSize;
                int i3 = i;
                if (collection.contains(this.this$0.colGetEntry(i, 1))) {
                    this.this$0.colRemoveAt(i);
                    i3 = i - 1;
                    i2 = iColGetSize - 1;
                    z = true;
                }
                i = i3 + 1;
                iColGetSize = i2;
            }
            return z;
        }

        @Override // java.util.Collection
        public boolean retainAll(Collection<?> collection) {
            int iColGetSize = this.this$0.colGetSize();
            int i = 0;
            boolean z = false;
            while (i < iColGetSize) {
                int i2 = iColGetSize;
                int i3 = i;
                if (!collection.contains(this.this$0.colGetEntry(i, 1))) {
                    this.this$0.colRemoveAt(i);
                    i3 = i - 1;
                    i2 = iColGetSize - 1;
                    z = true;
                }
                i = i3 + 1;
                iColGetSize = i2;
            }
            return z;
        }

        @Override // java.util.Collection
        public int size() {
            return this.this$0.colGetSize();
        }

        @Override // java.util.Collection
        public Object[] toArray() {
            return this.this$0.toArrayHelper(1);
        }

        @Override // java.util.Collection
        public <T> T[] toArray(T[] tArr) {
            return (T[]) this.this$0.toArrayHelper(tArr, 1);
        }
    }

    MapCollections() {
    }

    public static <K, V> boolean containsAllHelper(Map<K, V> map, Collection<?> collection) {
        Iterator<?> it = collection.iterator();
        while (it.hasNext()) {
            if (!map.containsKey(it.next())) {
                return false;
            }
        }
        return true;
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x0033  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static <T> boolean equalsSetHelper(java.util.Set<T> r3, java.lang.Object r4) {
        /*
            r0 = 1
            r5 = r0
            r0 = r3
            r1 = r4
            if (r0 != r1) goto L9
            r0 = 1
            return r0
        L9:
            r0 = r4
            boolean r0 = r0 instanceof java.util.Set
            if (r0 == 0) goto L37
            r0 = r4
            java.util.Set r0 = (java.util.Set) r0
            r4 = r0
            r0 = r3
            int r0 = r0.size()     // Catch: java.lang.Throwable -> L39
            r1 = r4
            int r1 = r1.size()     // Catch: java.lang.Throwable -> L39
            if (r0 != r1) goto L33
            r0 = r3
            r1 = r4
            boolean r0 = r0.containsAll(r1)     // Catch: java.lang.Throwable -> L39
            r6 = r0
            r0 = r6
            if (r0 == 0) goto L33
            goto L35
        L33:
            r0 = 0
            r5 = r0
        L35:
            r0 = r5
            return r0
        L37:
            r0 = 0
            return r0
        L39:
            r3 = move-exception
            goto L37
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.util.MapCollections.equalsSetHelper(java.util.Set, java.lang.Object):boolean");
    }

    public static <K, V> boolean removeAllHelper(Map<K, V> map, Collection<?> collection) {
        int size = map.size();
        Iterator<?> it = collection.iterator();
        while (it.hasNext()) {
            map.remove(it.next());
        }
        return size != map.size();
    }

    public static <K, V> boolean retainAllHelper(Map<K, V> map, Collection<?> collection) {
        int size = map.size();
        Iterator<K> it = map.keySet().iterator();
        while (it.hasNext()) {
            if (!collection.contains(it.next())) {
                it.remove();
            }
        }
        return size != map.size();
    }

    protected abstract void colClear();

    protected abstract Object colGetEntry(int i, int i2);

    protected abstract Map<K, V> colGetMap();

    protected abstract int colGetSize();

    protected abstract int colIndexOfKey(Object obj);

    protected abstract int colIndexOfValue(Object obj);

    protected abstract void colPut(K k, V v);

    protected abstract void colRemoveAt(int i);

    protected abstract V colSetValue(int i, V v);

    public Set<Map.Entry<K, V>> getEntrySet() {
        if (this.mEntrySet == null) {
            this.mEntrySet = new EntrySet(this);
        }
        return this.mEntrySet;
    }

    public Set<K> getKeySet() {
        if (this.mKeySet == null) {
            this.mKeySet = new KeySet(this);
        }
        return this.mKeySet;
    }

    public Collection<V> getValues() {
        if (this.mValues == null) {
            this.mValues = new ValuesCollection(this);
        }
        return this.mValues;
    }

    public Object[] toArrayHelper(int i) {
        int iColGetSize = colGetSize();
        Object[] objArr = new Object[iColGetSize];
        for (int i2 = 0; i2 < iColGetSize; i2++) {
            objArr[i2] = colGetEntry(i2, i);
        }
        return objArr;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v11 */
    /* JADX WARN: Type inference failed for: r0v16, types: [java.lang.Object[]] */
    public <T> T[] toArrayHelper(T[] tArr, int i) {
        int iColGetSize = colGetSize();
        T[] tArr2 = tArr;
        if (tArr.length < iColGetSize) {
            tArr2 = (Object[]) Array.newInstance(tArr.getClass().getComponentType(), iColGetSize);
        }
        for (int i2 = 0; i2 < iColGetSize; i2++) {
            tArr2[i2] = colGetEntry(i2, i);
        }
        if (tArr2.length > iColGetSize) {
            tArr2[iColGetSize] = null;
        }
        return tArr2;
    }
}
