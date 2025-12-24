package android.support.v4.util;

import android.os.Build;
import java.util.Arrays;
import p000.C$r8$backportedMethods$utility$Objects$2$equals;

/* loaded from: classes-dex2jar.jar:android/support/v4/util/ObjectsCompat.class */
public class ObjectsCompat {
    private ObjectsCompat() {
    }

    public static boolean equals(Object obj, Object obj2) {
        if (Build.VERSION.SDK_INT >= 19) {
            return C$r8$backportedMethods$utility$Objects$2$equals.equals(obj, obj2);
        }
        return obj == obj2 || (obj != null && obj.equals(obj2));
    }

    public static int hash(Object... objArr) {
        return Build.VERSION.SDK_INT >= 19 ? Arrays.hashCode(objArr) : Arrays.hashCode(objArr);
    }

    public static int hashCode(Object obj) {
        return obj != null ? obj.hashCode() : 0;
    }
}
