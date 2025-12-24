package com.alibaba.fastjson.util;

import java.util.Arrays;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/util/IdentityHashMap.class */
public class IdentityHashMap<K, V> {
    public static final int DEFAULT_SIZE = 8192;
    private final Entry<K, V>[] buckets;
    private final int indexMask;

    /* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/util/IdentityHashMap$Entry.class */
    protected static final class Entry<K, V> {
        public final int hashCode;
        public final K key;
        public final Entry<K, V> next;
        public V value;

        public Entry(K k, V v, int i, Entry<K, V> entry) {
            this.key = k;
            this.value = v;
            this.next = entry;
            this.hashCode = i;
        }
    }

    public IdentityHashMap() {
        this(8192);
    }

    public IdentityHashMap(int i) {
        this.indexMask = i - 1;
        this.buckets = new Entry[i];
    }

    public void clear() {
        Arrays.fill(this.buckets, (Object) null);
    }

    /* JADX WARN: Code restructure failed: missing block: B:23:0x004e, code lost:
    
        continue;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.Class findClass(java.lang.String r4) {
        /*
            r3 = this;
            r0 = 0
            r5 = r0
        L2:
            r0 = r3
            com.alibaba.fastjson.util.IdentityHashMap$Entry<K, V>[] r0 = r0.buckets
            r6 = r0
            r0 = r5
            r1 = r6
            int r1 = r1.length
            if (r0 >= r1) goto L54
            r0 = r6
            r1 = r5
            r0 = r0[r1]
            r7 = r0
            r0 = r7
            if (r0 != 0) goto L1a
            goto L4e
        L1a:
            r0 = r7
            r6 = r0
        L1d:
            r0 = r6
            if (r0 == 0) goto L4e
            r0 = r7
            K r0 = r0.key
            r8 = r0
            r0 = r8
            boolean r0 = r0 instanceof java.lang.Class
            if (r0 == 0) goto L46
            r0 = r8
            java.lang.Class r0 = (java.lang.Class) r0
            r8 = r0
            r0 = r8
            java.lang.String r0 = r0.getName()
            r1 = r4
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L46
            r0 = r8
            return r0
        L46:
            r0 = r6
            com.alibaba.fastjson.util.IdentityHashMap$Entry<K, V> r0 = r0.next
            r6 = r0
            goto L1d
        L4e:
            int r5 = r5 + 1
            goto L2
        L54:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.util.IdentityHashMap.findClass(java.lang.String):java.lang.Class");
    }

    public final V get(K k) {
        Entry<K, V> entry = this.buckets[System.identityHashCode(k) & this.indexMask];
        while (true) {
            Entry<K, V> entry2 = entry;
            if (entry2 == null) {
                return null;
            }
            if (k == entry2.key) {
                return entry2.value;
            }
            entry = entry2.next;
        }
    }

    public boolean put(K k, V v) {
        int iIdentityHashCode = System.identityHashCode(k);
        int i = this.indexMask & iIdentityHashCode;
        Entry<K, V> entry = this.buckets[i];
        while (true) {
            Entry<K, V> entry2 = entry;
            if (entry2 == null) {
                this.buckets[i] = new Entry<>(k, v, iIdentityHashCode, this.buckets[i]);
                return false;
            }
            if (k == entry2.key) {
                entry2.value = v;
                return true;
            }
            entry = entry2.next;
        }
    }
}
