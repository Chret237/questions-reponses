package android.support.v4.util;

/* loaded from: classes-dex2jar.jar:android/support/v4/util/Pair.class */
public class Pair<F, S> {
    public final F first;
    public final S second;

    public Pair(F f, S s) {
        this.first = f;
        this.second = s;
    }

    public static <A, B> Pair<A, B> create(A a, B b) {
        return new Pair<>(a, b);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Pair)) {
            return false;
        }
        Pair pair = (Pair) obj;
        boolean z = false;
        if (ObjectsCompat.equals(pair.first, this.first)) {
            z = false;
            if (ObjectsCompat.equals(pair.second, this.second)) {
                z = true;
            }
        }
        return z;
    }

    public int hashCode() {
        F f = this.first;
        int iHashCode = 0;
        int iHashCode2 = f == null ? 0 : f.hashCode();
        S s = this.second;
        if (s != null) {
            iHashCode = s.hashCode();
        }
        return iHashCode2 ^ iHashCode;
    }

    public String toString() {
        return "Pair{" + String.valueOf(this.first) + " " + String.valueOf(this.second) + "}";
    }
}
