package android.support.v4.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/* loaded from: classes-dex2jar.jar:android/support/v4/util/ArraySet.class */
public final class ArraySet<E> implements Collection<E>, Set<E> {
    private static final int BASE_SIZE = 4;
    private static final int CACHE_SIZE = 10;
    private static final boolean DEBUG = false;
    private static final int[] INT = new int[0];
    private static final Object[] OBJECT = new Object[0];
    private static final String TAG = "ArraySet";
    private static Object[] sBaseCache;
    private static int sBaseCacheSize;
    private static Object[] sTwiceBaseCache;
    private static int sTwiceBaseCacheSize;
    Object[] mArray;
    private MapCollections<E, E> mCollections;
    private int[] mHashes;
    int mSize;

    public ArraySet() {
        this(0);
    }

    public ArraySet(int i) {
        if (i == 0) {
            this.mHashes = INT;
            this.mArray = OBJECT;
        } else {
            allocArrays(i);
        }
        this.mSize = 0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public ArraySet(ArraySet<E> arraySet) {
        this();
        if (arraySet != 0) {
            addAll((ArraySet) arraySet);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public ArraySet(Collection<E> collection) {
        this();
        if (collection != 0) {
            addAll(collection);
        }
    }

    private void allocArrays(int i) {
        if (i == 8) {
            synchronized (ArraySet.class) {
                try {
                    if (sTwiceBaseCache != null) {
                        Object[] objArr = sTwiceBaseCache;
                        this.mArray = objArr;
                        sTwiceBaseCache = (Object[]) objArr[0];
                        this.mHashes = (int[]) objArr[1];
                        objArr[1] = null;
                        objArr[0] = null;
                        sTwiceBaseCacheSize--;
                        return;
                    }
                } finally {
                }
            }
        } else if (i == 4) {
            synchronized (ArraySet.class) {
                try {
                    if (sBaseCache != null) {
                        Object[] objArr2 = sBaseCache;
                        this.mArray = objArr2;
                        sBaseCache = (Object[]) objArr2[0];
                        this.mHashes = (int[]) objArr2[1];
                        objArr2[1] = null;
                        objArr2[0] = null;
                        sBaseCacheSize--;
                        return;
                    }
                } finally {
                }
            }
        }
        this.mHashes = new int[i];
        this.mArray = new Object[i];
    }

    private static void freeArrays(int[] iArr, Object[] objArr, int i) {
        if (iArr.length == 8) {
            synchronized (ArraySet.class) {
                try {
                    if (sTwiceBaseCacheSize < 10) {
                        objArr[0] = sTwiceBaseCache;
                        objArr[1] = iArr;
                        while (true) {
                            i--;
                            if (i < 2) {
                                break;
                            } else {
                                objArr[i] = null;
                            }
                        }
                        sTwiceBaseCache = objArr;
                        sTwiceBaseCacheSize++;
                    }
                } finally {
                }
            }
            return;
        }
        if (iArr.length == 4) {
            synchronized (ArraySet.class) {
                try {
                    if (sBaseCacheSize < 10) {
                        objArr[0] = sBaseCache;
                        objArr[1] = iArr;
                        while (true) {
                            i--;
                            if (i < 2) {
                                break;
                            } else {
                                objArr[i] = null;
                            }
                        }
                        sBaseCache = objArr;
                        sBaseCacheSize++;
                    }
                } finally {
                }
            }
        }
    }

    private MapCollections<E, E> getCollection() {
        if (this.mCollections == null) {
            this.mCollections = new MapCollections<E, E>(this) { // from class: android.support.v4.util.ArraySet.1
                final ArraySet this$0;

                {
                    this.this$0 = this;
                }

                @Override // android.support.v4.util.MapCollections
                protected void colClear() {
                    this.this$0.clear();
                }

                @Override // android.support.v4.util.MapCollections
                protected Object colGetEntry(int i, int i2) {
                    return this.this$0.mArray[i];
                }

                @Override // android.support.v4.util.MapCollections
                protected Map<E, E> colGetMap() {
                    throw new UnsupportedOperationException("not a map");
                }

                @Override // android.support.v4.util.MapCollections
                protected int colGetSize() {
                    return this.this$0.mSize;
                }

                @Override // android.support.v4.util.MapCollections
                protected int colIndexOfKey(Object obj) {
                    return this.this$0.indexOf(obj);
                }

                @Override // android.support.v4.util.MapCollections
                protected int colIndexOfValue(Object obj) {
                    return this.this$0.indexOf(obj);
                }

                @Override // android.support.v4.util.MapCollections
                protected void colPut(E e, E e2) {
                    this.this$0.add(e);
                }

                @Override // android.support.v4.util.MapCollections
                protected void colRemoveAt(int i) {
                    this.this$0.removeAt(i);
                }

                @Override // android.support.v4.util.MapCollections
                protected E colSetValue(int i, E e) {
                    throw new UnsupportedOperationException("not a map");
                }
            };
        }
        return this.mCollections;
    }

    private int indexOf(Object obj, int i) {
        int i2 = this.mSize;
        if (i2 == 0) {
            return -1;
        }
        int iBinarySearch = ContainerHelpers.binarySearch(this.mHashes, i2, i);
        if (iBinarySearch >= 0 && !obj.equals(this.mArray[iBinarySearch])) {
            int i3 = iBinarySearch + 1;
            while (i3 < i2 && this.mHashes[i3] == i) {
                if (obj.equals(this.mArray[i3])) {
                    return i3;
                }
                i3++;
            }
            for (int i4 = iBinarySearch - 1; i4 >= 0 && this.mHashes[i4] == i; i4--) {
                if (obj.equals(this.mArray[i4])) {
                    return i4;
                }
            }
            return i3 ^ (-1);
        }
        return iBinarySearch;
    }

    private int indexOfNull() {
        int i = this.mSize;
        if (i == 0) {
            return -1;
        }
        int iBinarySearch = ContainerHelpers.binarySearch(this.mHashes, i, 0);
        if (iBinarySearch >= 0 && this.mArray[iBinarySearch] != null) {
            int i2 = iBinarySearch + 1;
            while (i2 < i && this.mHashes[i2] == 0) {
                if (this.mArray[i2] == null) {
                    return i2;
                }
                i2++;
            }
            do {
                iBinarySearch--;
                if (iBinarySearch < 0 || this.mHashes[iBinarySearch] != 0) {
                    return i2 ^ (-1);
                }
            } while (this.mArray[iBinarySearch] != null);
            return iBinarySearch;
        }
        return iBinarySearch;
    }

    @Override // java.util.Collection, java.util.Set
    public boolean add(E e) {
        int iHashCode;
        int iIndexOf;
        if (e == null) {
            iIndexOf = indexOfNull();
            iHashCode = 0;
        } else {
            iHashCode = e.hashCode();
            iIndexOf = indexOf(e, iHashCode);
        }
        if (iIndexOf >= 0) {
            return false;
        }
        int i = iIndexOf ^ (-1);
        int i2 = this.mSize;
        if (i2 >= this.mHashes.length) {
            int i3 = 4;
            if (i2 >= 8) {
                i3 = (i2 >> 1) + i2;
            } else if (i2 >= 4) {
                i3 = 8;
            }
            int[] iArr = this.mHashes;
            Object[] objArr = this.mArray;
            allocArrays(i3);
            int[] iArr2 = this.mHashes;
            if (iArr2.length > 0) {
                System.arraycopy(iArr, 0, iArr2, 0, iArr.length);
                System.arraycopy(objArr, 0, this.mArray, 0, objArr.length);
            }
            freeArrays(iArr, objArr, this.mSize);
        }
        int i4 = this.mSize;
        if (i < i4) {
            int[] iArr3 = this.mHashes;
            int i5 = i + 1;
            System.arraycopy(iArr3, i, iArr3, i5, i4 - i);
            Object[] objArr2 = this.mArray;
            System.arraycopy(objArr2, i, objArr2, i5, this.mSize - i);
        }
        this.mHashes[i] = iHashCode;
        this.mArray[i] = e;
        this.mSize++;
        return true;
    }

    public void addAll(ArraySet<? extends E> arraySet) {
        int i = arraySet.mSize;
        ensureCapacity(this.mSize + i);
        if (this.mSize != 0) {
            for (int i2 = 0; i2 < i; i2++) {
                add(arraySet.valueAt(i2));
            }
        } else if (i > 0) {
            System.arraycopy(arraySet.mHashes, 0, this.mHashes, 0, i);
            System.arraycopy(arraySet.mArray, 0, this.mArray, 0, i);
            this.mSize = i;
        }
    }

    @Override // java.util.Collection, java.util.Set
    public boolean addAll(Collection<? extends E> collection) {
        ensureCapacity(this.mSize + collection.size());
        Iterator<? extends E> it = collection.iterator();
        boolean zAdd = false;
        while (true) {
            boolean z = zAdd;
            if (!it.hasNext()) {
                return z;
            }
            zAdd = z | add(it.next());
        }
    }

    public void append(E e) {
        int i = this.mSize;
        int iHashCode = e == null ? 0 : e.hashCode();
        int[] iArr = this.mHashes;
        if (i >= iArr.length) {
            throw new IllegalStateException("Array is full");
        }
        if (i > 0 && iArr[i - 1] > iHashCode) {
            add(e);
            return;
        }
        this.mSize = i + 1;
        this.mHashes[i] = iHashCode;
        this.mArray[i] = e;
    }

    @Override // java.util.Collection, java.util.Set
    public void clear() {
        int i = this.mSize;
        if (i != 0) {
            freeArrays(this.mHashes, this.mArray, i);
            this.mHashes = INT;
            this.mArray = OBJECT;
            this.mSize = 0;
        }
    }

    @Override // java.util.Collection, java.util.Set
    public boolean contains(Object obj) {
        return indexOf(obj) >= 0;
    }

    @Override // java.util.Collection, java.util.Set
    public boolean containsAll(Collection<?> collection) {
        Iterator<?> it = collection.iterator();
        while (it.hasNext()) {
            if (!contains(it.next())) {
                return false;
            }
        }
        return true;
    }

    public void ensureCapacity(int i) {
        int[] iArr = this.mHashes;
        if (iArr.length < i) {
            Object[] objArr = this.mArray;
            allocArrays(i);
            int i2 = this.mSize;
            if (i2 > 0) {
                System.arraycopy(iArr, 0, this.mHashes, 0, i2);
                System.arraycopy(objArr, 0, this.mArray, 0, this.mSize);
            }
            freeArrays(iArr, objArr, this.mSize);
        }
    }

    @Override // java.util.Collection, java.util.Set
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Set)) {
            return false;
        }
        Set set = (Set) obj;
        if (size() != set.size()) {
            return false;
        }
        for (int i = 0; i < this.mSize; i++) {
            try {
                if (!set.contains(valueAt(i))) {
                    return false;
                }
            } catch (ClassCastException | NullPointerException e) {
                return false;
            }
        }
        return true;
    }

    @Override // java.util.Collection, java.util.Set
    public int hashCode() {
        int[] iArr = this.mHashes;
        int i = this.mSize;
        int i2 = 0;
        for (int i3 = 0; i3 < i; i3++) {
            i2 += iArr[i3];
        }
        return i2;
    }

    public int indexOf(Object obj) {
        return obj == null ? indexOfNull() : indexOf(obj, obj.hashCode());
    }

    @Override // java.util.Collection, java.util.Set
    public boolean isEmpty() {
        return this.mSize <= 0;
    }

    @Override // java.util.Collection, java.lang.Iterable, java.util.Set
    public Iterator<E> iterator() {
        return getCollection().getKeySet().iterator();
    }

    @Override // java.util.Collection, java.util.Set
    public boolean remove(Object obj) {
        int iIndexOf = indexOf(obj);
        if (iIndexOf < 0) {
            return false;
        }
        removeAt(iIndexOf);
        return true;
    }

    public boolean removeAll(ArraySet<? extends E> arraySet) {
        int i = arraySet.mSize;
        int i2 = this.mSize;
        for (int i3 = 0; i3 < i; i3++) {
            remove(arraySet.valueAt(i3));
        }
        return i2 != this.mSize;
    }

    @Override // java.util.Collection, java.util.Set
    public boolean removeAll(Collection<?> collection) {
        Iterator<?> it = collection.iterator();
        boolean zRemove = false;
        while (true) {
            boolean z = zRemove;
            if (!it.hasNext()) {
                return z;
            }
            zRemove = z | remove(it.next());
        }
    }

    public E removeAt(int i) {
        Object[] objArr = this.mArray;
        E e = (E) objArr[i];
        int i2 = this.mSize;
        if (i2 <= 1) {
            freeArrays(this.mHashes, objArr, i2);
            this.mHashes = INT;
            this.mArray = OBJECT;
            this.mSize = 0;
        } else {
            int[] iArr = this.mHashes;
            int i3 = 8;
            if (iArr.length <= 8 || i2 >= iArr.length / 3) {
                int i4 = this.mSize - 1;
                this.mSize = i4;
                if (i < i4) {
                    int[] iArr2 = this.mHashes;
                    int i5 = i + 1;
                    System.arraycopy(iArr2, i5, iArr2, i, i4 - i);
                    Object[] objArr2 = this.mArray;
                    System.arraycopy(objArr2, i5, objArr2, i, this.mSize - i);
                }
                this.mArray[this.mSize] = null;
            } else {
                if (i2 > 8) {
                    i3 = i2 + (i2 >> 1);
                }
                int[] iArr3 = this.mHashes;
                Object[] objArr3 = this.mArray;
                allocArrays(i3);
                this.mSize--;
                if (i > 0) {
                    System.arraycopy(iArr3, 0, this.mHashes, 0, i);
                    System.arraycopy(objArr3, 0, this.mArray, 0, i);
                }
                int i6 = this.mSize;
                if (i < i6) {
                    int i7 = i + 1;
                    System.arraycopy(iArr3, i7, this.mHashes, i, i6 - i);
                    System.arraycopy(objArr3, i7, this.mArray, i, this.mSize - i);
                }
            }
        }
        return e;
    }

    @Override // java.util.Collection, java.util.Set
    public boolean retainAll(Collection<?> collection) {
        boolean z = false;
        for (int i = this.mSize - 1; i >= 0; i--) {
            if (!collection.contains(this.mArray[i])) {
                removeAt(i);
                z = true;
            }
        }
        return z;
    }

    @Override // java.util.Collection, java.util.Set
    public int size() {
        return this.mSize;
    }

    @Override // java.util.Collection, java.util.Set
    public Object[] toArray() {
        int i = this.mSize;
        Object[] objArr = new Object[i];
        System.arraycopy(this.mArray, 0, objArr, 0, i);
        return objArr;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v16, types: [java.lang.Object[]] */
    @Override // java.util.Collection, java.util.Set
    public <T> T[] toArray(T[] tArr) {
        T[] tArr2 = tArr;
        if (tArr.length < this.mSize) {
            tArr2 = (Object[]) Array.newInstance(tArr.getClass().getComponentType(), this.mSize);
        }
        System.arraycopy(this.mArray, 0, tArr2, 0, this.mSize);
        int length = tArr2.length;
        int i = this.mSize;
        if (length > i) {
            tArr2[i] = null;
        }
        return tArr2;
    }

    public String toString() {
        if (isEmpty()) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder(this.mSize * 14);
        sb.append('{');
        for (int i = 0; i < this.mSize; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            E eValueAt = valueAt(i);
            if (eValueAt != this) {
                sb.append(eValueAt);
            } else {
                sb.append("(this Set)");
            }
        }
        sb.append('}');
        return sb.toString();
    }

    public E valueAt(int i) {
        return (E) this.mArray[i];
    }
}
