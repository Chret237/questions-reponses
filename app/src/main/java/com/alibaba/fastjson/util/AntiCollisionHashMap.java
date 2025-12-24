package com.alibaba.fastjson.util;

import android.support.v7.widget.ActivityChooserView;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/util/AntiCollisionHashMap.class */
public class AntiCollisionHashMap<K, V> extends AbstractMap<K, V> implements Map<K, V>, Cloneable, Serializable {
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int KEY = 16777619;
    static final int MAXIMUM_CAPACITY = 1073741824;
    static final int M_MASK = -2023358765;
    static final int SEED = -2128831035;
    private static final long serialVersionUID = 362498820763181265L;
    private transient Set<Map.Entry<K, V>> entrySet;
    volatile transient Set<K> keySet;
    final float loadFactor;
    volatile transient int modCount;
    final int random;
    transient int size;
    transient Entry<K, V>[] table;
    int threshold;
    volatile transient Collection<V> values;

    /* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/util/AntiCollisionHashMap$Entry.class */
    static class Entry<K, V> implements Map.Entry<K, V> {
        final int hash;
        final K key;
        Entry<K, V> next;
        V value;

        Entry(int i, K k, V v, Entry<K, V> entry) {
            this.value = v;
            this.next = entry;
            this.key = k;
            this.hash = i;
        }

        @Override // java.util.Map.Entry
        public final boolean equals(Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            K key = getKey();
            Object key2 = entry.getKey();
            if (key != key2 && (key == null || !key.equals(key2))) {
                return false;
            }
            V value = getValue();
            Object value2 = entry.getValue();
            if (value != value2) {
                return value != null && value.equals(value2);
            }
            return true;
        }

        @Override // java.util.Map.Entry
        public final K getKey() {
            return this.key;
        }

        @Override // java.util.Map.Entry
        public final V getValue() {
            return this.value;
        }

        @Override // java.util.Map.Entry
        public final int hashCode() {
            K k = this.key;
            int iHashCode = 0;
            int iHashCode2 = k == null ? 0 : k.hashCode();
            V v = this.value;
            if (v != null) {
                iHashCode = v.hashCode();
            }
            return iHashCode2 ^ iHashCode;
        }

        @Override // java.util.Map.Entry
        public final V setValue(V v) {
            V v2 = this.value;
            this.value = v;
            return v2;
        }

        public final String toString() {
            return getKey() + "=" + getValue();
        }
    }

    /* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/util/AntiCollisionHashMap$EntryIterator.class */
    private final class EntryIterator extends AntiCollisionHashMap<K, V>.HashIterator<Map.Entry<K, V>> {
        final AntiCollisionHashMap this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        private EntryIterator(AntiCollisionHashMap antiCollisionHashMap) {
            super(antiCollisionHashMap);
            this.this$0 = antiCollisionHashMap;
        }

        @Override // java.util.Iterator
        public Map.Entry<K, V> next() {
            return nextEntry();
        }
    }

    /* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/util/AntiCollisionHashMap$EntrySet.class */
    private final class EntrySet extends AbstractSet<Map.Entry<K, V>> {
        final AntiCollisionHashMap this$0;

        private EntrySet(AntiCollisionHashMap antiCollisionHashMap) {
            this.this$0 = antiCollisionHashMap;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            this.this$0.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            Entry<K, V> entry2 = this.this$0.getEntry(entry.getKey());
            boolean z = false;
            if (entry2 != null) {
                z = false;
                if (entry2.equals(entry)) {
                    z = true;
                }
            }
            return z;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<Map.Entry<K, V>> iterator() {
            return this.this$0.newEntryIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            return this.this$0.removeMapping(obj) != null;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.this$0.size;
        }
    }

    /* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/util/AntiCollisionHashMap$HashIterator.class */
    private abstract class HashIterator<E> implements Iterator<E> {
        Entry<K, V> current;
        int expectedModCount;
        int index;
        Entry<K, V> next;
        final AntiCollisionHashMap this$0;

        HashIterator(AntiCollisionHashMap antiCollisionHashMap) {
            Entry<K, V> entry;
            this.this$0 = antiCollisionHashMap;
            this.expectedModCount = antiCollisionHashMap.modCount;
            if (antiCollisionHashMap.size > 0) {
                Entry<K, V>[] entryArr = antiCollisionHashMap.table;
                do {
                    int i = this.index;
                    if (i >= entryArr.length) {
                        return;
                    }
                    this.index = i + 1;
                    entry = entryArr[i];
                    this.next = entry;
                } while (entry == null);
            }
        }

        @Override // java.util.Iterator
        public final boolean hasNext() {
            return this.next != null;
        }

        final Entry<K, V> nextEntry() {
            Entry<K, V> entry;
            if (this.this$0.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
            Entry<K, V> entry2 = this.next;
            if (entry2 == null) {
                throw new NoSuchElementException();
            }
            Entry<K, V> entry3 = entry2.next;
            this.next = entry3;
            if (entry3 == null) {
                Entry<K, V>[] entryArr = this.this$0.table;
                do {
                    int i = this.index;
                    if (i >= entryArr.length) {
                        break;
                    }
                    this.index = i + 1;
                    entry = entryArr[i];
                    this.next = entry;
                } while (entry == null);
            }
            this.current = entry2;
            return entry2;
        }

        @Override // java.util.Iterator
        public void remove() {
            if (this.current == null) {
                throw new IllegalStateException();
            }
            if (this.this$0.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
            K k = this.current.key;
            this.current = null;
            this.this$0.removeEntryForKey(k);
            this.expectedModCount = this.this$0.modCount;
        }
    }

    /* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/util/AntiCollisionHashMap$KeyIterator.class */
    private final class KeyIterator extends AntiCollisionHashMap<K, V>.HashIterator<K> {
        final AntiCollisionHashMap this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        private KeyIterator(AntiCollisionHashMap antiCollisionHashMap) {
            super(antiCollisionHashMap);
            this.this$0 = antiCollisionHashMap;
        }

        @Override // java.util.Iterator
        public K next() {
            return nextEntry().getKey();
        }
    }

    /* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/util/AntiCollisionHashMap$KeySet.class */
    private final class KeySet extends AbstractSet<K> {
        final AntiCollisionHashMap this$0;

        private KeySet(AntiCollisionHashMap antiCollisionHashMap) {
            this.this$0 = antiCollisionHashMap;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            this.this$0.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            return this.this$0.containsKey(obj);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<K> iterator() {
            return this.this$0.newKeyIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            return this.this$0.removeEntryForKey(obj) != null;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.this$0.size;
        }
    }

    /* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/util/AntiCollisionHashMap$ValueIterator.class */
    private final class ValueIterator extends AntiCollisionHashMap<K, V>.HashIterator<V> {
        final AntiCollisionHashMap this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        private ValueIterator(AntiCollisionHashMap antiCollisionHashMap) {
            super(antiCollisionHashMap);
            this.this$0 = antiCollisionHashMap;
        }

        @Override // java.util.Iterator
        public V next() {
            return nextEntry().value;
        }
    }

    /* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/util/AntiCollisionHashMap$Values.class */
    private final class Values extends AbstractCollection<V> {
        final AntiCollisionHashMap this$0;

        private Values(AntiCollisionHashMap antiCollisionHashMap) {
            this.this$0 = antiCollisionHashMap;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public void clear() {
            this.this$0.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean contains(Object obj) {
            return this.this$0.containsValue(obj);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public Iterator<V> iterator() {
            return this.this$0.newValueIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            return this.this$0.size;
        }
    }

    public AntiCollisionHashMap() {
        this.keySet = null;
        this.values = null;
        this.random = new Random().nextInt(99999);
        this.entrySet = null;
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.threshold = 12;
        this.table = new Entry[16];
        init();
    }

    public AntiCollisionHashMap(int i) {
        this(i, DEFAULT_LOAD_FACTOR);
    }

    public AntiCollisionHashMap(int i, float f) {
        this.keySet = null;
        this.values = null;
        this.random = new Random().nextInt(99999);
        this.entrySet = null;
        if (i < 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " + i);
        }
        int i2 = i > MAXIMUM_CAPACITY ? MAXIMUM_CAPACITY : i;
        if (f <= 0.0f || Float.isNaN(f)) {
            throw new IllegalArgumentException("Illegal load factor: " + f);
        }
        int i3 = 1;
        while (true) {
            int i4 = i3;
            if (i4 >= i2) {
                this.loadFactor = f;
                this.threshold = (int) (i4 * f);
                this.table = new Entry[i4];
                init();
                return;
            }
            i3 = i4 << 1;
        }
    }

    public AntiCollisionHashMap(Map<? extends K, ? extends V> map) {
        this(Math.max(((int) (map.size() / DEFAULT_LOAD_FACTOR)) + 1, 16), DEFAULT_LOAD_FACTOR);
        putAllForCreate(map);
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x0026, code lost:
    
        r4 = r4 + 1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean containsNullValue() {
        /*
            r3 = this;
            r0 = r3
            com.alibaba.fastjson.util.AntiCollisionHashMap$Entry<K, V>[] r0 = r0.table
            r6 = r0
            r0 = 0
            r4 = r0
        L7:
            r0 = r4
            r1 = r6
            int r1 = r1.length
            if (r0 >= r1) goto L2c
            r0 = r6
            r1 = r4
            r0 = r0[r1]
            r5 = r0
        L11:
            r0 = r5
            if (r0 == 0) goto L26
            r0 = r5
            V r0 = r0.value
            if (r0 != 0) goto L1e
            r0 = 1
            return r0
        L1e:
            r0 = r5
            com.alibaba.fastjson.util.AntiCollisionHashMap$Entry<K, V> r0 = r0.next
            r5 = r0
            goto L11
        L26:
            int r4 = r4 + 1
            goto L7
        L2c:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.util.AntiCollisionHashMap.containsNullValue():boolean");
    }

    private Set<Map.Entry<K, V>> entrySet0() {
        Set<Map.Entry<K, V>> entrySet = this.entrySet;
        if (entrySet == null) {
            entrySet = new EntrySet();
            this.entrySet = entrySet;
        }
        return entrySet;
    }

    private V getForNullKey() {
        Entry<K, V> entry = this.table[0];
        while (true) {
            Entry<K, V> entry2 = entry;
            if (entry2 == null) {
                return null;
            }
            if (entry2.key == null) {
                return entry2.value;
            }
            entry = entry2.next;
        }
    }

    static int hash(int i) {
        int i2 = i * i;
        int i3 = i2 ^ ((i2 >>> 20) ^ (i2 >>> 12));
        return (i3 >>> 4) ^ ((i3 >>> 7) ^ i3);
    }

    private int hashString(String str) {
        int iCharAt = this.random * SEED;
        for (int i = 0; i < str.length(); i++) {
            iCharAt = (iCharAt * KEY) ^ str.charAt(i);
        }
        return ((iCharAt >> 1) ^ iCharAt) & M_MASK;
    }

    static int indexFor(int i, int i2) {
        return i & (i2 - 1);
    }

    private void putAllForCreate(Map<? extends K, ? extends V> map) {
        for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            putForCreate(entry.getKey(), entry.getValue());
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void putForCreate(K k, V v) {
        Entry<K, V> entry;
        K k2;
        int iHash = k == 0 ? 0 : k instanceof String ? hash(hashString((String) k)) : hash(k.hashCode());
        int iIndexFor = indexFor(iHash, this.table.length);
        Entry<K, V> entry2 = this.table[iIndexFor];
        while (true) {
            entry = entry2;
            if (entry == null) {
                createEntry(iHash, k, v, iIndexFor);
                return;
            } else if (entry.hash == iHash && ((k2 = entry.key) == k || (k != 0 && k.equals(k2)))) {
                break;
            } else {
                entry2 = entry.next;
            }
        }
        entry.value = v;
    }

    private V putForNullKey(V v) {
        Entry<K, V> entry = this.table[0];
        while (true) {
            Entry<K, V> entry2 = entry;
            if (entry2 == null) {
                this.modCount++;
                addEntry(0, null, v, 0);
                return null;
            }
            if (entry2.key == null) {
                V v2 = entry2.value;
                entry2.value = v;
                return v2;
            }
            entry = entry2.next;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void readObject(ObjectInputStream objectInputStream) throws ClassNotFoundException, IOException {
        objectInputStream.defaultReadObject();
        this.table = new Entry[objectInputStream.readInt()];
        init();
        int i = objectInputStream.readInt();
        for (int i2 = 0; i2 < i; i2++) {
            putForCreate(objectInputStream.readObject(), objectInputStream.readObject());
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        Iterator<Map.Entry<K, V>> it = this.size > 0 ? entrySet0().iterator() : null;
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeInt(this.table.length);
        objectOutputStream.writeInt(this.size);
        if (it != null) {
            while (it.hasNext()) {
                Map.Entry<K, V> next = it.next();
                objectOutputStream.writeObject(next.getKey());
                objectOutputStream.writeObject(next.getValue());
            }
        }
    }

    void addEntry(int i, K k, V v, int i2) {
        Entry<K, V>[] entryArr = this.table;
        entryArr[i2] = new Entry<>(i, k, v, entryArr[i2]);
        int i3 = this.size;
        this.size = i3 + 1;
        if (i3 >= this.threshold) {
            resize(this.table.length * 2);
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void clear() {
        this.modCount++;
        Entry<K, V>[] entryArr = this.table;
        for (int i = 0; i < entryArr.length; i++) {
            entryArr[i] = null;
        }
        this.size = 0;
    }

    @Override // java.util.AbstractMap
    public Object clone() {
        AntiCollisionHashMap antiCollisionHashMap;
        try {
            antiCollisionHashMap = (AntiCollisionHashMap) super.clone();
        } catch (CloneNotSupportedException e) {
            antiCollisionHashMap = null;
        }
        antiCollisionHashMap.table = new Entry[this.table.length];
        antiCollisionHashMap.entrySet = null;
        antiCollisionHashMap.modCount = 0;
        antiCollisionHashMap.size = 0;
        antiCollisionHashMap.init();
        antiCollisionHashMap.putAllForCreate(this);
        return antiCollisionHashMap;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsKey(Object obj) {
        return getEntry(obj) != null;
    }

    /* JADX WARN: Code restructure failed: missing block: B:17:0x0036, code lost:
    
        r5 = r5 + 1;
     */
    @Override // java.util.AbstractMap, java.util.Map
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean containsValue(java.lang.Object r4) {
        /*
            r3 = this;
            r0 = r4
            if (r0 != 0) goto L9
            r0 = r3
            boolean r0 = r0.containsNullValue()
            return r0
        L9:
            r0 = r3
            com.alibaba.fastjson.util.AntiCollisionHashMap$Entry<K, V>[] r0 = r0.table
            r7 = r0
            r0 = 0
            r5 = r0
        L11:
            r0 = r5
            r1 = r7
            int r1 = r1.length
            if (r0 >= r1) goto L3c
            r0 = r7
            r1 = r5
            r0 = r0[r1]
            r6 = r0
        L1d:
            r0 = r6
            if (r0 == 0) goto L36
            r0 = r4
            r1 = r6
            V r1 = r1.value
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L2e
            r0 = 1
            return r0
        L2e:
            r0 = r6
            com.alibaba.fastjson.util.AntiCollisionHashMap$Entry<K, V> r0 = r0.next
            r6 = r0
            goto L1d
        L36:
            int r5 = r5 + 1
            goto L11
        L3c:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.util.AntiCollisionHashMap.containsValue(java.lang.Object):boolean");
    }

    void createEntry(int i, K k, V v, int i2) {
        Entry<K, V>[] entryArr = this.table;
        entryArr[i2] = new Entry<>(i, k, v, entryArr[i2]);
        this.size++;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        return entrySet0();
    }

    /* JADX WARN: Code restructure failed: missing block: B:20:0x005a, code lost:
    
        return r7.value;
     */
    @Override // java.util.AbstractMap, java.util.Map
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public V get(java.lang.Object r5) {
        /*
            r4 = this;
            r0 = r5
            if (r0 != 0) goto L9
            r0 = r4
            java.lang.Object r0 = r0.getForNullKey()
            return r0
        L9:
            r0 = r5
            boolean r0 = r0 instanceof java.lang.String
            if (r0 == 0) goto L1f
            r0 = r4
            r1 = r5
            java.lang.String r1 = (java.lang.String) r1
            int r0 = r0.hashString(r1)
            int r0 = hash(r0)
            r6 = r0
            goto L27
        L1f:
            r0 = r5
            int r0 = r0.hashCode()
            int r0 = hash(r0)
            r6 = r0
        L27:
            r0 = r4
            com.alibaba.fastjson.util.AntiCollisionHashMap$Entry<K, V>[] r0 = r0.table
            r7 = r0
            r0 = r7
            r1 = r6
            r2 = r7
            int r2 = r2.length
            int r1 = indexFor(r1, r2)
            r0 = r0[r1]
            r7 = r0
        L35:
            r0 = r7
            if (r0 == 0) goto L63
            r0 = r7
            int r0 = r0.hash
            r1 = r6
            if (r0 != r1) goto L5b
            r0 = r7
            K r0 = r0.key
            r8 = r0
            r0 = r8
            r1 = r5
            if (r0 == r1) goto L56
            r0 = r5
            r1 = r8
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L5b
        L56:
            r0 = r7
            V r0 = r0.value
            return r0
        L5b:
            r0 = r7
            com.alibaba.fastjson.util.AntiCollisionHashMap$Entry<K, V> r0 = r0.next
            r7 = r0
            goto L35
        L63:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.util.AntiCollisionHashMap.get(java.lang.Object):java.lang.Object");
    }

    final Entry<K, V> getEntry(Object obj) {
        Entry<K, V> entry;
        K k;
        int iHash = obj == null ? 0 : obj instanceof String ? hash(hashString((String) obj)) : hash(obj.hashCode());
        Entry<K, V>[] entryArr = this.table;
        Entry<K, V> entry2 = entryArr[indexFor(iHash, entryArr.length)];
        while (true) {
            entry = entry2;
            if (entry == null) {
                return null;
            }
            if (entry.hash == iHash && ((k = entry.key) == obj || (obj != null && obj.equals(k)))) {
                break;
            }
            entry2 = entry.next;
        }
        return entry;
    }

    void init() {
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<K> keySet() {
        Set<K> keySet = this.keySet;
        if (keySet == null) {
            keySet = new KeySet();
            this.keySet = keySet;
        }
        return keySet;
    }

    Iterator<Map.Entry<K, V>> newEntryIterator() {
        return new EntryIterator();
    }

    Iterator<K> newKeyIterator() {
        return new KeyIterator();
    }

    Iterator<V> newValueIterator() {
        return new ValueIterator();
    }

    /* JADX WARN: Code restructure failed: missing block: B:19:0x0060, code lost:
    
        r0 = r11.value;
        r11.value = r8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x006d, code lost:
    
        return r0;
     */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.AbstractMap, java.util.Map
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public V put(K r7, V r8) {
        /*
            r6 = this;
            r0 = r7
            if (r0 != 0) goto La
            r0 = r6
            r1 = r8
            java.lang.Object r0 = r0.putForNullKey(r1)
            return r0
        La:
            r0 = r7
            boolean r0 = r0 instanceof java.lang.String
            if (r0 == 0) goto L20
            r0 = r6
            r1 = r7
            java.lang.String r1 = (java.lang.String) r1
            int r0 = r0.hashString(r1)
            int r0 = hash(r0)
            r9 = r0
            goto L28
        L20:
            r0 = r7
            int r0 = r0.hashCode()
            int r0 = hash(r0)
            r9 = r0
        L28:
            r0 = r9
            r1 = r6
            com.alibaba.fastjson.util.AntiCollisionHashMap$Entry<K, V>[] r1 = r1.table
            int r1 = r1.length
            int r0 = indexFor(r0, r1)
            r10 = r0
            r0 = r6
            com.alibaba.fastjson.util.AntiCollisionHashMap$Entry<K, V>[] r0 = r0.table
            r1 = r10
            r0 = r0[r1]
            r11 = r0
        L3c:
            r0 = r11
            if (r0 == 0) goto L78
            r0 = r11
            int r0 = r0.hash
            r1 = r9
            if (r0 != r1) goto L6e
            r0 = r11
            K r0 = r0.key
            r12 = r0
            r0 = r12
            r1 = r7
            if (r0 == r1) goto L60
            r0 = r7
            r1 = r12
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L6e
        L60:
            r0 = r11
            V r0 = r0.value
            r7 = r0
            r0 = r11
            r1 = r8
            r0.value = r1
            r0 = r7
            return r0
        L6e:
            r0 = r11
            com.alibaba.fastjson.util.AntiCollisionHashMap$Entry<K, V> r0 = r0.next
            r11 = r0
            goto L3c
        L78:
            r0 = r6
            r1 = r6
            int r1 = r1.modCount
            r2 = 1
            int r1 = r1 + r2
            r0.modCount = r1
            r0 = r6
            r1 = r9
            r2 = r7
            r3 = r8
            r4 = r10
            r0.addEntry(r1, r2, r3, r4)
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.util.AntiCollisionHashMap.put(java.lang.Object, java.lang.Object):java.lang.Object");
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void putAll(Map<? extends K, ? extends V> map) {
        int i;
        int size = map.size();
        if (size == 0) {
            return;
        }
        if (size > this.threshold) {
            int i2 = (int) ((size / this.loadFactor) + 1.0f);
            int i3 = i2;
            if (i2 > MAXIMUM_CAPACITY) {
                i3 = MAXIMUM_CAPACITY;
            }
            int length = this.table.length;
            while (true) {
                i = length;
                if (i >= i3) {
                    break;
                } else {
                    length = i << 1;
                }
            }
            if (i > this.table.length) {
                resize(i);
            }
        }
        for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V remove(Object obj) {
        Entry<K, V> entryRemoveEntryForKey = removeEntryForKey(obj);
        return entryRemoveEntryForKey == null ? null : entryRemoveEntryForKey.value;
    }

    final Entry<K, V> removeEntryForKey(Object obj) {
        K k;
        int iHash = obj == null ? 0 : obj instanceof String ? hash(hashString((String) obj)) : hash(obj.hashCode());
        int iIndexFor = indexFor(iHash, this.table.length);
        Entry<K, V> entry = this.table[iIndexFor];
        Entry<K, V> entry2 = entry;
        while (entry != null) {
            Entry<K, V> entry3 = entry.next;
            if (entry.hash == iHash && ((k = entry.key) == obj || (obj != null && obj.equals(k)))) {
                this.modCount++;
                this.size--;
                if (entry2 == entry) {
                    this.table[iIndexFor] = entry3;
                } else {
                    entry2.next = entry3;
                }
                return entry;
            }
            entry2 = entry;
            entry = entry3;
        }
        return entry;
    }

    final Entry<K, V> removeMapping(Object obj) {
        if (!(obj instanceof Map.Entry)) {
            return null;
        }
        Map.Entry entry = (Map.Entry) obj;
        Object key = entry.getKey();
        int iHash = key == null ? 0 : key instanceof String ? hash(hashString((String) key)) : hash(key.hashCode());
        int iIndexFor = indexFor(iHash, this.table.length);
        Entry<K, V> entry2 = this.table[iIndexFor];
        Entry<K, V> entry3 = entry2;
        while (entry2 != null) {
            Entry<K, V> entry4 = entry2.next;
            if (entry2.hash == iHash && entry2.equals(entry)) {
                this.modCount++;
                this.size--;
                if (entry3 == entry2) {
                    this.table[iIndexFor] = entry4;
                } else {
                    entry3.next = entry4;
                }
                return entry2;
            }
            entry3 = entry2;
            entry2 = entry4;
        }
        return entry2;
    }

    void resize(int i) {
        if (this.table.length == MAXIMUM_CAPACITY) {
            this.threshold = ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
            return;
        }
        Entry<K, V>[] entryArr = new Entry[i];
        transfer(entryArr);
        this.table = entryArr;
        this.threshold = (int) (i * this.loadFactor);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int size() {
        return this.size;
    }

    void transfer(Entry[] entryArr) {
        Entry<K, V>[] entryArr2 = this.table;
        int length = entryArr.length;
        for (int i = 0; i < entryArr2.length; i++) {
            Entry<K, V> entry = entryArr2[i];
            if (entry != null) {
                entryArr2[i] = null;
                while (true) {
                    Entry<K, V> entry2 = entry.next;
                    int iIndexFor = indexFor(entry.hash, length);
                    entry.next = entryArr[iIndexFor];
                    entryArr[iIndexFor] = entry;
                    if (entry2 == null) {
                        break;
                    } else {
                        entry = entry2;
                    }
                }
            }
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Collection<V> values() {
        Collection<V> values = this.values;
        if (values == null) {
            values = new Values();
            this.values = values;
        }
        return values;
    }
}
