package android.support.v4.os;

import android.os.Parcel;

@Deprecated
/* loaded from: classes-dex2jar.jar:android/support/v4/os/ParcelableCompatCreatorCallbacks.class */
public interface ParcelableCompatCreatorCallbacks<T> {
    T createFromParcel(Parcel parcel, ClassLoader classLoader);

    T[] newArray(int i);
}
