package com.google.gson.internal;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/* loaded from: classes-dex2jar.jar:com/google/gson/internal/LinkedTreeMap.class */
public final class LinkedTreeMap<K, V> extends AbstractMap<K, V> implements Serializable {
    static final boolean $assertionsDisabled = false;
    private static final Comparator<Comparable> NATURAL_ORDER = new Comparator<Comparable>() { // from class: com.google.gson.internal.LinkedTreeMap.1
        @Override // java.util.Comparator
        public int compare(Comparable comparable, Comparable comparable2) {
            return comparable.compareTo(comparable2);
        }
    };
    Comparator<? super K> comparator;
    private LinkedTreeMap<K, V>.EntrySet entrySet;
    final Node<K, V> header;
    private LinkedTreeMap<K, V>.KeySet keySet;
    int modCount;
    Node<K, V> root;
    int size;

    /* loaded from: classes-dex2jar.jar:com/google/gson/internal/LinkedTreeMap$EntrySet.class */
    class EntrySet extends AbstractSet<Map.Entry<K, V>> {
        final LinkedTreeMap this$0;

        EntrySet(LinkedTreeMap linkedTreeMap) {
            this.this$0 = linkedTreeMap;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            this.this$0.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            return (obj instanceof Map.Entry) && this.this$0.findByEntry((Map.Entry) obj) != null;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<Map.Entry<K, V>> iterator() {
            return new LinkedTreeMap<K, V>.LinkedTreeMapIterator<Map.Entry<K, V>>(this) { // from class: com.google.gson.internal.LinkedTreeMap.EntrySet.1
                final EntrySet this$1;

                {
                    this.this$1 = this;
                    LinkedTreeMap linkedTreeMap = this.this$0;
                }

                @Override // java.util.Iterator
                public Map.Entry<K, V> next() {
                    return nextNode();
                }
            };
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            Node<K, V> nodeFindByEntry;
            if (!(obj instanceof Map.Entry) || (nodeFindByEntry = this.this$0.findByEntry((Map.Entry) obj)) == null) {
                return false;
            }
            this.this$0.removeInternal(nodeFindByEntry, true);
            return true;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.this$0.size;
        }
    }

    /* loaded from: classes-dex2jar.jar:com/google/gson/internal/LinkedTreeMap$KeySet.class */
    final class KeySet extends AbstractSet<K> {
        final LinkedTreeMap this$0;

        KeySet(LinkedTreeMap linkedTreeMap) {
            this.this$0 = linkedTreeMap;
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
            return new LinkedTreeMap<K, V>.LinkedTreeMapIterator<K>(this) { // from class: com.google.gson.internal.LinkedTreeMap.KeySet.1
                final KeySet this$1;

                {
                    this.this$1 = this;
                    LinkedTreeMap linkedTreeMap = this.this$0;
                }

                @Override // java.util.Iterator
                public K next() {
                    return nextNode().key;
                }
            };
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            return this.this$0.removeInternalByKey(obj) != null;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.this$0.size;
        }
    }

    /* loaded from: classes-dex2jar.jar:com/google/gson/internal/LinkedTreeMap$LinkedTreeMapIterator.class */
    private abstract class LinkedTreeMapIterator<T> implements Iterator<T> {
        int expectedModCount;
        Node<K, V> lastReturned = null;
        Node<K, V> next;
        final LinkedTreeMap this$0;

        LinkedTreeMapIterator(LinkedTreeMap linkedTreeMap) {
            this.this$0 = linkedTreeMap;
            this.next = this.this$0.header.next;
            this.expectedModCount = this.this$0.modCount;
        }

        @Override // java.util.Iterator
        public final boolean hasNext() {
            return this.next != this.this$0.header;
        }

        final Node<K, V> nextNode() {
            Node<K, V> node = this.next;
            if (node == this.this$0.header) {
                throw new NoSuchElementException();
            }
            if (this.this$0.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
            this.next = node.next;
            this.lastReturned = node;
            return node;
        }

        @Override // java.util.Iterator
        public final void remove() {
            Node<K, V> node = this.lastReturned;
            if (node == null) {
                throw new IllegalStateException();
            }
            this.this$0.removeInternal(node, true);
            this.lastReturned = null;
            this.expectedModCount = this.this$0.modCount;
        }
    }

    /* loaded from: classes-dex2jar.jar:com/google/gson/internal/LinkedTreeMap$Node.class */
    static final class Node<K, V> implements Map.Entry<K, V> {
        int height;
        final K key;
        Node<K, V> left;
        Node<K, V> next;
        Node<K, V> parent;
        Node<K, V> prev;
        Node<K, V> right;
        V value;

        Node() {
            this.key = null;
            this.prev = this;
            this.next = this;
        }

        Node(Node<K, V> node, K k, Node<K, V> node2, Node<K, V> node3) {
            this.parent = node;
            this.key = k;
            this.height = 1;
            this.next = node2;
            this.prev = node3;
            node3.next = this;
            node2.prev = this;
        }

        /* JADX WARN: Removed duplicated region for block: B:11:0x003d  */
        /* JADX WARN: Removed duplicated region for block: B:18:0x0066  */
        @Override // java.util.Map.Entry
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public boolean equals(java.lang.Object r4) {
            /*
                r3 = this;
                r0 = r4
                boolean r0 = r0 instanceof java.util.Map.Entry
                r7 = r0
                r0 = 0
                r6 = r0
                r0 = r6
                r5 = r0
                r0 = r7
                if (r0 == 0) goto L68
                r0 = r4
                java.util.Map$Entry r0 = (java.util.Map.Entry) r0
                r4 = r0
                r0 = r3
                K r0 = r0.key
                r8 = r0
                r0 = r8
                if (r0 != 0) goto L2d
                r0 = r6
                r5 = r0
                r0 = r4
                java.lang.Object r0 = r0.getKey()
                if (r0 != 0) goto L68
                goto L3d
            L2d:
                r0 = r6
                r5 = r0
                r0 = r8
                r1 = r4
                java.lang.Object r1 = r1.getKey()
                boolean r0 = r0.equals(r1)
                if (r0 == 0) goto L68
            L3d:
                r0 = r3
                V r0 = r0.value
                r8 = r0
                r0 = r8
                if (r0 != 0) goto L56
                r0 = r6
                r5 = r0
                r0 = r4
                java.lang.Object r0 = r0.getValue()
                if (r0 != 0) goto L68
                goto L66
            L56:
                r0 = r6
                r5 = r0
                r0 = r8
                r1 = r4
                java.lang.Object r1 = r1.getValue()
                boolean r0 = r0.equals(r1)
                if (r0 == 0) goto L68
            L66:
                r0 = 1
                r5 = r0
            L68:
                r0 = r5
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.gson.internal.LinkedTreeMap.Node.equals(java.lang.Object):boolean");
        }

        public Node<K, V> first() {
            Node<K, V> node = this;
            for (Node<K, V> node2 = this.left; node2 != null; node2 = node2.left) {
                node = node2;
            }
            return node;
        }

        @Override // java.util.Map.Entry
        public K getKey() {
            return this.key;
        }

        @Override // java.util.Map.Entry
        public V getValue() {
            return this.value;
        }

        @Override // java.util.Map.Entry
        public int hashCode() {
            K k = this.key;
            int iHashCode = 0;
            int iHashCode2 = k == null ? 0 : k.hashCode();
            V v = this.value;
            if (v != null) {
                iHashCode = v.hashCode();
            }
            return iHashCode2 ^ iHashCode;
        }

        public Node<K, V> last() {
            Node<K, V> node = this;
            for (Node<K, V> node2 = this.right; node2 != null; node2 = node2.right) {
                node = node2;
            }
            return node;
        }

        @Override // java.util.Map.Entry
        public V setValue(V v) {
            V v2 = this.value;
            this.value = v;
            return v2;
        }

        public String toString() {
            return this.key + "=" + this.value;
        }
    }

    public LinkedTreeMap() {
        this(NATURAL_ORDER);
    }

    public LinkedTreeMap(Comparator<? super K> comparator) {
        this.size = 0;
        this.modCount = 0;
        this.header = new Node<>();
        this.comparator = comparator == null ? NATURAL_ORDER : comparator;
    }

    private boolean equal(Object obj, Object obj2) {
        return obj == obj2 || (obj != null && obj.equals(obj2));
    }

    private void rebalance(Node<K, V> node, boolean z) {
        while (node != null) {
            Node<K, V> node2 = node.left;
            Node<K, V> node3 = node.right;
            int i = node2 != null ? node2.height : 0;
            int i2 = node3 != null ? node3.height : 0;
            int i3 = i - i2;
            if (i3 == -2) {
                Node<K, V> node4 = node3.left;
                Node<K, V> node5 = node3.right;
                int i4 = node5 != null ? node5.height : 0;
                int i5 = 0;
                if (node4 != null) {
                    i5 = node4.height;
                }
                int i6 = i5 - i4;
                if (i6 == -1 || (i6 == 0 && !z)) {
                    rotateLeft(node);
                } else {
                    rotateRight(node3);
                    rotateLeft(node);
                }
                if (z) {
                    return;
                }
            } else if (i3 == 2) {
                Node<K, V> node6 = node2.left;
                Node<K, V> node7 = node2.right;
                int i7 = node7 != null ? node7.height : 0;
                int i8 = 0;
                if (node6 != null) {
                    i8 = node6.height;
                }
                int i9 = i8 - i7;
                if (i9 == 1 || (i9 == 0 && !z)) {
                    rotateRight(node);
                } else {
                    rotateLeft(node2);
                    rotateRight(node);
                }
                if (z) {
                    return;
                }
            } else if (i3 == 0) {
                node.height = i + 1;
                if (z) {
                    return;
                }
            } else {
                node.height = Math.max(i, i2) + 1;
                if (!z) {
                    return;
                }
            }
            node = node.parent;
        }
    }

    private void replaceInParent(Node<K, V> node, Node<K, V> node2) {
        Node<K, V> node3 = node.parent;
        node.parent = null;
        if (node2 != null) {
            node2.parent = node3;
        }
        if (node3 == null) {
            this.root = node2;
        } else if (node3.left == node) {
            node3.left = node2;
        } else {
            node3.right = node2;
        }
    }

    private void rotateLeft(Node<K, V> node) {
        Node<K, V> node2 = node.left;
        Node<K, V> node3 = node.right;
        Node<K, V> node4 = node3.left;
        Node<K, V> node5 = node3.right;
        node.right = node4;
        if (node4 != null) {
            node4.parent = node;
        }
        replaceInParent(node, node3);
        node3.left = node;
        node.parent = node3;
        node.height = Math.max(node2 != null ? node2.height : 0, node4 != null ? node4.height : 0) + 1;
        int i = node.height;
        int i2 = 0;
        if (node5 != null) {
            i2 = node5.height;
        }
        node3.height = Math.max(i, i2) + 1;
    }

    private void rotateRight(Node<K, V> node) {
        Node<K, V> node2 = node.left;
        Node<K, V> node3 = node.right;
        Node<K, V> node4 = node2.left;
        Node<K, V> node5 = node2.right;
        node.left = node5;
        if (node5 != null) {
            node5.parent = node;
        }
        replaceInParent(node, node2);
        node2.right = node;
        node.parent = node2;
        node.height = Math.max(node3 != null ? node3.height : 0, node5 != null ? node5.height : 0) + 1;
        int i = node.height;
        int i2 = 0;
        if (node4 != null) {
            i2 = node4.height;
        }
        node2.height = Math.max(i, i2) + 1;
    }

    private Object writeReplace() throws ObjectStreamException {
        return new LinkedHashMap(this);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void clear() {
        this.root = null;
        this.size = 0;
        this.modCount++;
        Node<K, V> node = this.header;
        node.prev = node;
        node.next = node;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsKey(Object obj) {
        return findByObject(obj) != null;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        LinkedTreeMap<K, V>.EntrySet entrySet = this.entrySet;
        if (entrySet == null) {
            entrySet = new EntrySet(this);
            this.entrySet = entrySet;
        }
        return entrySet;
    }

    Node<K, V> find(K k, boolean z) {
        int iCompareTo;
        Node<K, V> node;
        Comparator<? super K> comparator = this.comparator;
        Node<K, V> node2 = this.root;
        if (node2 != null) {
            Comparable comparable = comparator == NATURAL_ORDER ? (Comparable) k : null;
            while (true) {
                iCompareTo = comparable != null ? comparable.compareTo(node2.key) : comparator.compare(k, node2.key);
                if (iCompareTo == 0) {
                    return node2;
                }
                Node<K, V> node3 = iCompareTo < 0 ? node2.left : node2.right;
                if (node3 == null) {
                    break;
                }
                node2 = node3;
            }
        } else {
            iCompareTo = 0;
        }
        if (!z) {
            return null;
        }
        Node<K, V> node4 = this.header;
        if (node2 != null) {
            node = new Node<>(node2, k, node4, node4.prev);
            if (iCompareTo < 0) {
                node2.left = node;
            } else {
                node2.right = node;
            }
            rebalance(node2, true);
        } else {
            if (comparator == NATURAL_ORDER && !(k instanceof Comparable)) {
                throw new ClassCastException(k.getClass().getName() + " is not Comparable");
            }
            node = new Node<>(node2, k, node4, node4.prev);
            this.root = node;
        }
        this.size++;
        this.modCount++;
        return node;
    }

    Node<K, V> findByEntry(Map.Entry<?, ?> entry) {
        Node<K, V> nodeFindByObject = findByObject(entry.getKey());
        return nodeFindByObject != null && equal(nodeFindByObject.value, entry.getValue()) ? nodeFindByObject : null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    Node<K, V> findByObject(Object obj) {
        Node<K, V> nodeFind = null;
        if (obj != 0) {
            try {
                nodeFind = find(obj, false);
            } catch (ClassCastException e) {
                nodeFind = null;
            }
        }
        return nodeFind;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V get(Object obj) {
        Node<K, V> nodeFindByObject = findByObject(obj);
        return nodeFindByObject != null ? nodeFindByObject.value : null;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<K> keySet() {
        LinkedTreeMap<K, V>.KeySet keySet = this.keySet;
        if (keySet == null) {
            keySet = new KeySet(this);
            this.keySet = keySet;
        }
        return keySet;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V put(K k, V v) {
        if (k == null) {
            throw new NullPointerException("key == null");
        }
        Node<K, V> nodeFind = find(k, true);
        V v2 = nodeFind.value;
        nodeFind.value = v;
        return v2;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V remove(Object obj) {
        Node<K, V> nodeRemoveInternalByKey = removeInternalByKey(obj);
        return nodeRemoveInternalByKey != null ? nodeRemoveInternalByKey.value : null;
    }

    void removeInternal(Node<K, V> node, boolean z) {
        int i;
        if (z) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
        Node<K, V> node2 = node.left;
        Node<K, V> node3 = node.right;
        Node<K, V> node4 = node.parent;
        int i2 = 0;
        if (node2 == null || node3 == null) {
            if (node2 != null) {
                replaceInParent(node, node2);
                node.left = null;
            } else if (node3 != null) {
                replaceInParent(node, node3);
                node.right = null;
            } else {
                replaceInParent(node, null);
            }
            rebalance(node4, false);
            this.size--;
            this.modCount++;
            return;
        }
        Node<K, V> nodeLast = node2.height > node3.height ? node2.last() : node3.first();
        removeInternal(nodeLast, false);
        Node<K, V> node5 = node.left;
        if (node5 != null) {
            i = node5.height;
            nodeLast.left = node5;
            node5.parent = nodeLast;
            node.left = null;
        } else {
            i = 0;
        }
        Node<K, V> node6 = node.right;
        if (node6 != null) {
            i2 = node6.height;
            nodeLast.right = node6;
            node6.parent = nodeLast;
            node.right = null;
        }
        nodeLast.height = Math.max(i, i2) + 1;
        replaceInParent(node, nodeLast);
    }

    Node<K, V> removeInternalByKey(Object obj) {
        Node<K, V> nodeFindByObject = findByObject(obj);
        if (nodeFindByObject != null) {
            removeInternal(nodeFindByObject, true);
        }
        return nodeFindByObject;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int size() {
        return this.size;
    }
}
