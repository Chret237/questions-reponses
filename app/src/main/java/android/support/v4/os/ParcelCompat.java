package android.support.v4.os;

import android.os.Parcel;

/* loaded from: classes-dex2jar.jar:android/support/v4/os/ParcelCompat.class */
public final class ParcelCompat {
    private ParcelCompat() {
    }

    public static boolean readBoolean(Parcel parcel) {
        return parcel.readInt() != 0;
    }

    public static void writeBoolean(Parcel parcel, boolean z) {
        parcel.writeInt(z ? 1 : 0);
    }
}
