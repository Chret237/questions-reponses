package com.squareup.picasso;

import android.content.Context;
import android.graphics.Bitmap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/* loaded from: classes-dex2jar.jar:com/squareup/picasso/LruCache.class */
public class LruCache implements Cache {
    private int evictionCount;
    private int hitCount;
    final LinkedHashMap<String, Bitmap> map;
    private final int maxSize;
    private int missCount;
    private int putCount;
    private int size;

    public LruCache(int i) {
        if (i <= 0) {
            throw new IllegalArgumentException("Max size must be positive.");
        }
        this.maxSize = i;
        this.map = new LinkedHashMap<>(0, 0.75f, true);
    }

    public LruCache(Context context) {
        this(Utils.calculateMemoryCacheSize(context));
    }

    /* JADX WARN: Code restructure failed: missing block: B:23:0x00a8, code lost:
    
        throw new java.lang.IllegalStateException(getClass().getName() + ".sizeOf() is reporting inconsistent results!");
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void trimToSize(int r5) {
        /*
            r4 = this;
        L0:
            r0 = r4
            monitor-enter(r0)
            r0 = r4
            int r0 = r0.size     // Catch: java.lang.Throwable -> La9
            if (r0 < 0) goto L80
            r0 = r4
            java.util.LinkedHashMap<java.lang.String, android.graphics.Bitmap> r0 = r0.map     // Catch: java.lang.Throwable -> La9
            boolean r0 = r0.isEmpty()     // Catch: java.lang.Throwable -> La9
            if (r0 == 0) goto L1a
            r0 = r4
            int r0 = r0.size     // Catch: java.lang.Throwable -> La9
            if (r0 != 0) goto L80
        L1a:
            r0 = r4
            int r0 = r0.size     // Catch: java.lang.Throwable -> La9
            r1 = r5
            if (r0 <= r1) goto L7d
            r0 = r4
            java.util.LinkedHashMap<java.lang.String, android.graphics.Bitmap> r0 = r0.map     // Catch: java.lang.Throwable -> La9
            boolean r0 = r0.isEmpty()     // Catch: java.lang.Throwable -> La9
            if (r0 == 0) goto L2f
            goto L7d
        L2f:
            r0 = r4
            java.util.LinkedHashMap<java.lang.String, android.graphics.Bitmap> r0 = r0.map     // Catch: java.lang.Throwable -> La9
            java.util.Set r0 = r0.entrySet()     // Catch: java.lang.Throwable -> La9
            java.util.Iterator r0 = r0.iterator()     // Catch: java.lang.Throwable -> La9
            java.lang.Object r0 = r0.next()     // Catch: java.lang.Throwable -> La9
            java.util.Map$Entry r0 = (java.util.Map.Entry) r0     // Catch: java.lang.Throwable -> La9
            r7 = r0
            r0 = r7
            java.lang.Object r0 = r0.getKey()     // Catch: java.lang.Throwable -> La9
            java.lang.String r0 = (java.lang.String) r0     // Catch: java.lang.Throwable -> La9
            r6 = r0
            r0 = r7
            java.lang.Object r0 = r0.getValue()     // Catch: java.lang.Throwable -> La9
            android.graphics.Bitmap r0 = (android.graphics.Bitmap) r0     // Catch: java.lang.Throwable -> La9
            r7 = r0
            r0 = r4
            java.util.LinkedHashMap<java.lang.String, android.graphics.Bitmap> r0 = r0.map     // Catch: java.lang.Throwable -> La9
            r1 = r6
            java.lang.Object r0 = r0.remove(r1)     // Catch: java.lang.Throwable -> La9
            r0 = r4
            r1 = r4
            int r1 = r1.size     // Catch: java.lang.Throwable -> La9
            r2 = r7
            int r2 = com.squareup.picasso.Utils.getBitmapBytes(r2)     // Catch: java.lang.Throwable -> La9
            int r1 = r1 - r2
            r0.size = r1     // Catch: java.lang.Throwable -> La9
            r0 = r4
            r1 = r4
            int r1 = r1.evictionCount     // Catch: java.lang.Throwable -> La9
            r2 = 1
            int r1 = r1 + r2
            r0.evictionCount = r1     // Catch: java.lang.Throwable -> La9
            r0 = r4
            monitor-exit(r0)     // Catch: java.lang.Throwable -> La9
            goto L0
        L7d:
            r0 = r4
            monitor-exit(r0)     // Catch: java.lang.Throwable -> La9
            return
        L80:
            java.lang.IllegalStateException r0 = new java.lang.IllegalStateException     // Catch: java.lang.Throwable -> La9
            r7 = r0
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> La9
            r6 = r0
            r0 = r6
            r0.<init>()     // Catch: java.lang.Throwable -> La9
            r0 = r6
            r1 = r4
            java.lang.Class r1 = r1.getClass()     // Catch: java.lang.Throwable -> La9
            java.lang.String r1 = r1.getName()     // Catch: java.lang.Throwable -> La9
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch: java.lang.Throwable -> La9
            r0 = r6
            java.lang.String r1 = ".sizeOf() is reporting inconsistent results!"
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch: java.lang.Throwable -> La9
            r0 = r7
            r1 = r6
            java.lang.String r1 = r1.toString()     // Catch: java.lang.Throwable -> La9
            r0.<init>(r1)     // Catch: java.lang.Throwable -> La9
            r0 = r7
            throw r0     // Catch: java.lang.Throwable -> La9
        La9:
            r6 = move-exception
            r0 = r4
            monitor-exit(r0)     // Catch: java.lang.Throwable -> La9
            goto Lb1
        Laf:
            r0 = r6
            throw r0
        Lb1:
            goto Laf
        */
        throw new UnsupportedOperationException("Method not decompiled: com.squareup.picasso.LruCache.trimToSize(int):void");
    }

    @Override // com.squareup.picasso.Cache
    public final void clear() {
        synchronized (this) {
            evictAll();
        }
    }

    @Override // com.squareup.picasso.Cache
    public final void clearKeyUri(String str) {
        synchronized (this) {
            int length = str.length();
            Iterator<Map.Entry<String, Bitmap>> it = this.map.entrySet().iterator();
            boolean z = false;
            while (it.hasNext()) {
                Map.Entry<String, Bitmap> next = it.next();
                String key = next.getKey();
                Bitmap value = next.getValue();
                int iIndexOf = key.indexOf(10);
                if (iIndexOf == length && key.substring(0, iIndexOf).equals(str)) {
                    it.remove();
                    this.size -= Utils.getBitmapBytes(value);
                    z = true;
                }
            }
            if (z) {
                trimToSize(this.maxSize);
            }
        }
    }

    public final void evictAll() {
        trimToSize(-1);
    }

    public final int evictionCount() {
        int i;
        synchronized (this) {
            i = this.evictionCount;
        }
        return i;
    }

    @Override // com.squareup.picasso.Cache
    public Bitmap get(String str) {
        if (str == null) {
            throw new NullPointerException("key == null");
        }
        synchronized (this) {
            Bitmap bitmap = this.map.get(str);
            if (bitmap != null) {
                this.hitCount++;
                return bitmap;
            }
            this.missCount++;
            return null;
        }
    }

    public final int hitCount() {
        int i;
        synchronized (this) {
            i = this.hitCount;
        }
        return i;
    }

    @Override // com.squareup.picasso.Cache
    public final int maxSize() {
        int i;
        synchronized (this) {
            i = this.maxSize;
        }
        return i;
    }

    public final int missCount() {
        int i;
        synchronized (this) {
            i = this.missCount;
        }
        return i;
    }

    public final int putCount() {
        int i;
        synchronized (this) {
            i = this.putCount;
        }
        return i;
    }

    @Override // com.squareup.picasso.Cache
    public void set(String str, Bitmap bitmap) {
        if (str == null || bitmap == null) {
            throw new NullPointerException("key == null || bitmap == null");
        }
        synchronized (this) {
            this.putCount++;
            this.size += Utils.getBitmapBytes(bitmap);
            Bitmap bitmapPut = this.map.put(str, bitmap);
            if (bitmapPut != null) {
                this.size -= Utils.getBitmapBytes(bitmapPut);
            }
        }
        trimToSize(this.maxSize);
    }

    @Override // com.squareup.picasso.Cache
    public final int size() {
        int i;
        synchronized (this) {
            i = this.size;
        }
        return i;
    }
}
