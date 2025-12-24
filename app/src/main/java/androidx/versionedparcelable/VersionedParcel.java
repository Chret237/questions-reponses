package androidx.versionedparcelable;

import android.os.BadParcelableException;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.NetworkOnMainThreadException;
import android.os.Parcelable;
import android.support.v4.util.ArraySet;
import android.util.Size;
import android.util.SizeF;
import android.util.SparseBooleanArray;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/* loaded from: classes-dex2jar.jar:androidx/versionedparcelable/VersionedParcel.class */
public abstract class VersionedParcel {
    private static final int EX_BAD_PARCELABLE = -2;
    private static final int EX_ILLEGAL_ARGUMENT = -3;
    private static final int EX_ILLEGAL_STATE = -5;
    private static final int EX_NETWORK_MAIN_THREAD = -6;
    private static final int EX_NULL_POINTER = -4;
    private static final int EX_PARCELABLE = -9;
    private static final int EX_SECURITY = -1;
    private static final int EX_UNSUPPORTED_OPERATION = -7;
    private static final String TAG = "VersionedParcel";
    private static final int TYPE_BINDER = 5;
    private static final int TYPE_PARCELABLE = 2;
    private static final int TYPE_SERIALIZABLE = 3;
    private static final int TYPE_STRING = 4;
    private static final int TYPE_VERSIONED_PARCELABLE = 1;

    /* loaded from: classes-dex2jar.jar:androidx/versionedparcelable/VersionedParcel$ParcelException.class */
    public static class ParcelException extends RuntimeException {
        public ParcelException(Throwable th) {
            super(th);
        }
    }

    private Exception createException(int i, String str) {
        switch (i) {
            case EX_PARCELABLE /* -9 */:
                return (Exception) readParcelable();
            case -8:
            default:
                return new RuntimeException("Unknown exception code: " + i + " msg " + str);
            case EX_UNSUPPORTED_OPERATION /* -7 */:
                return new UnsupportedOperationException(str);
            case EX_NETWORK_MAIN_THREAD /* -6 */:
                return new NetworkOnMainThreadException();
            case EX_ILLEGAL_STATE /* -5 */:
                return new IllegalStateException(str);
            case -4:
                return new NullPointerException(str);
            case -3:
                return new IllegalArgumentException(str);
            case -2:
                return new BadParcelableException(str);
            case -1:
                return new SecurityException(str);
        }
    }

    private static <T extends VersionedParcelable> Class findParcelClass(T t) throws ClassNotFoundException {
        return findParcelClass((Class<? extends VersionedParcelable>) t.getClass());
    }

    private static Class findParcelClass(Class<? extends VersionedParcelable> cls) throws ClassNotFoundException {
        return Class.forName(String.format("%s.%sParcelizer", cls.getPackage().getName(), cls.getSimpleName()), false, cls.getClassLoader());
    }

    protected static Throwable getRootCause(Throwable th) {
        while (th.getCause() != null) {
            th = th.getCause();
        }
        return th;
    }

    private <T> int getType(T t) {
        if (t instanceof String) {
            return 4;
        }
        if (t instanceof Parcelable) {
            return 2;
        }
        if (t instanceof VersionedParcelable) {
            return 1;
        }
        if (t instanceof Serializable) {
            return 3;
        }
        if (t instanceof IBinder) {
            return 5;
        }
        throw new IllegalArgumentException(t.getClass().getName() + " cannot be VersionedParcelled");
    }

    private <T, S extends Collection<T>> S readCollection(int i, S s) {
        int i2 = readInt();
        if (i2 < 0) {
            return null;
        }
        if (i2 != 0) {
            int i3 = readInt();
            if (i2 < 0) {
                return null;
            }
            if (i3 != 1) {
                if (i3 != 2) {
                    if (i3 != 3) {
                        if (i3 == 4) {
                            for (int i4 = i2; i4 > 0; i4--) {
                                s.add(readString());
                            }
                        } else if (i3 == 5) {
                            while (i2 > 0) {
                                s.add(readStrongBinder());
                                i2--;
                            }
                        }
                    } else {
                        for (int i5 = i2; i5 > 0; i5--) {
                            s.add(readSerializable());
                        }
                    }
                } else {
                    for (int i6 = i2; i6 > 0; i6--) {
                        s.add(readParcelable());
                    }
                }
            } else {
                for (int i7 = i2; i7 > 0; i7--) {
                    s.add(readVersionedParcelable());
                }
            }
        }
        return s;
    }

    private Exception readException(int i, String str) {
        return createException(i, str);
    }

    private int readExceptionCode() {
        return readInt();
    }

    protected static <T extends VersionedParcelable> T readFromParcel(String str, VersionedParcel versionedParcel) {
        try {
            return (T) Class.forName(str, true, VersionedParcel.class.getClassLoader()).getDeclaredMethod("read", VersionedParcel.class).invoke(null, versionedParcel);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("VersionedParcel encountered ClassNotFoundException", e);
        } catch (IllegalAccessException e2) {
            throw new RuntimeException("VersionedParcel encountered IllegalAccessException", e2);
        } catch (NoSuchMethodException e3) {
            throw new RuntimeException("VersionedParcel encountered NoSuchMethodException", e3);
        } catch (InvocationTargetException e4) {
            if (e4.getCause() instanceof RuntimeException) {
                throw ((RuntimeException) e4.getCause());
            }
            throw new RuntimeException("VersionedParcel encountered InvocationTargetException", e4);
        }
    }

    private <T> void writeCollection(Collection<T> collection, int i) throws IllegalAccessException, IOException, IllegalArgumentException, InvocationTargetException {
        setOutputField(i);
        if (collection == null) {
            writeInt(-1);
            return;
        }
        int size = collection.size();
        writeInt(size);
        if (size > 0) {
            int type = getType(collection.iterator().next());
            writeInt(type);
            if (type == 1) {
                Iterator<T> it = collection.iterator();
                while (it.hasNext()) {
                    writeVersionedParcelable((VersionedParcelable) it.next());
                }
                return;
            }
            if (type == 2) {
                Iterator<T> it2 = collection.iterator();
                while (it2.hasNext()) {
                    writeParcelable((Parcelable) it2.next());
                }
                return;
            }
            if (type == 3) {
                Iterator<T> it3 = collection.iterator();
                while (it3.hasNext()) {
                    writeSerializable((Serializable) it3.next());
                }
            } else if (type == 4) {
                Iterator<T> it4 = collection.iterator();
                while (it4.hasNext()) {
                    writeString((String) it4.next());
                }
            } else {
                if (type != 5) {
                    return;
                }
                Iterator<T> it5 = collection.iterator();
                while (it5.hasNext()) {
                    writeStrongBinder((IBinder) it5.next());
                }
            }
        }
    }

    private void writeSerializable(Serializable serializable) throws IOException {
        if (serializable == null) {
            writeString(null);
            return;
        }
        String name = serializable.getClass().getName();
        writeString(name);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(serializable);
            objectOutputStream.close();
            writeByteArray(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("VersionedParcelable encountered IOException writing serializable object (name = " + name + ")", e);
        }
    }

    protected static <T extends VersionedParcelable> void writeToParcel(T t, VersionedParcel versionedParcel) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        try {
            findParcelClass(t).getDeclaredMethod("write", t.getClass(), VersionedParcel.class).invoke(null, t, versionedParcel);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("VersionedParcel encountered ClassNotFoundException", e);
        } catch (IllegalAccessException e2) {
            throw new RuntimeException("VersionedParcel encountered IllegalAccessException", e2);
        } catch (NoSuchMethodException e3) {
            throw new RuntimeException("VersionedParcel encountered NoSuchMethodException", e3);
        } catch (InvocationTargetException e4) {
            if (!(e4.getCause() instanceof RuntimeException)) {
                throw new RuntimeException("VersionedParcel encountered InvocationTargetException", e4);
            }
            throw ((RuntimeException) e4.getCause());
        }
    }

    private void writeVersionedParcelableCreator(VersionedParcelable versionedParcelable) {
        try {
            writeString(findParcelClass((Class<? extends VersionedParcelable>) versionedParcelable.getClass()).getName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(versionedParcelable.getClass().getSimpleName() + " does not have a Parcelizer", e);
        }
    }

    protected abstract void closeField();

    protected abstract VersionedParcel createSubParcel();

    public boolean isStream() {
        return false;
    }

    protected <T> T[] readArray(T[] tArr) {
        int i = readInt();
        if (i < 0) {
            return null;
        }
        ArrayList arrayList = new ArrayList(i);
        if (i != 0) {
            int i2 = readInt();
            if (i < 0) {
                return null;
            }
            if (i2 != 1) {
                if (i2 != 2) {
                    if (i2 != 3) {
                        if (i2 == 4) {
                            for (int i3 = i; i3 > 0; i3--) {
                                arrayList.add(readString());
                            }
                        } else if (i2 == 5) {
                            while (i > 0) {
                                arrayList.add(readStrongBinder());
                                i--;
                            }
                        }
                    } else {
                        for (int i4 = i; i4 > 0; i4--) {
                            arrayList.add(readSerializable());
                        }
                    }
                } else {
                    for (int i5 = i; i5 > 0; i5--) {
                        arrayList.add(readParcelable());
                    }
                }
            } else {
                for (int i6 = i; i6 > 0; i6--) {
                    arrayList.add(readVersionedParcelable());
                }
            }
        }
        return (T[]) arrayList.toArray(tArr);
    }

    public <T> T[] readArray(T[] tArr, int i) {
        return !readField(i) ? tArr : (T[]) readArray(tArr);
    }

    protected abstract boolean readBoolean();

    public boolean readBoolean(boolean z, int i) {
        return !readField(i) ? z : readBoolean();
    }

    protected boolean[] readBooleanArray() {
        int i = readInt();
        if (i < 0) {
            return null;
        }
        boolean[] zArr = new boolean[i];
        for (int i2 = 0; i2 < i; i2++) {
            zArr[i2] = readInt() != 0;
        }
        return zArr;
    }

    public boolean[] readBooleanArray(boolean[] zArr, int i) {
        return !readField(i) ? zArr : readBooleanArray();
    }

    protected abstract Bundle readBundle();

    public Bundle readBundle(Bundle bundle, int i) {
        return !readField(i) ? bundle : readBundle();
    }

    public byte readByte(byte b, int i) {
        return !readField(i) ? b : (byte) (readInt() & 255);
    }

    protected abstract byte[] readByteArray();

    public byte[] readByteArray(byte[] bArr, int i) {
        return !readField(i) ? bArr : readByteArray();
    }

    public char[] readCharArray(char[] cArr, int i) {
        if (!readField(i)) {
            return cArr;
        }
        int i2 = readInt();
        if (i2 < 0) {
            return null;
        }
        char[] cArr2 = new char[i2];
        for (int i3 = 0; i3 < i2; i3++) {
            cArr2[i3] = (char) readInt();
        }
        return cArr2;
    }

    protected abstract double readDouble();

    public double readDouble(double d, int i) {
        return !readField(i) ? d : readDouble();
    }

    protected double[] readDoubleArray() {
        int i = readInt();
        if (i < 0) {
            return null;
        }
        double[] dArr = new double[i];
        for (int i2 = 0; i2 < i; i2++) {
            dArr[i2] = readDouble();
        }
        return dArr;
    }

    public double[] readDoubleArray(double[] dArr, int i) {
        return !readField(i) ? dArr : readDoubleArray();
    }

    public Exception readException(Exception exc, int i) {
        if (!readField(i)) {
            return exc;
        }
        int exceptionCode = readExceptionCode();
        if (exceptionCode != 0) {
            exc = readException(exceptionCode, readString());
        }
        return exc;
    }

    protected abstract boolean readField(int i);

    protected abstract float readFloat();

    public float readFloat(float f, int i) {
        return !readField(i) ? f : readFloat();
    }

    protected float[] readFloatArray() {
        int i = readInt();
        if (i < 0) {
            return null;
        }
        float[] fArr = new float[i];
        for (int i2 = 0; i2 < i; i2++) {
            fArr[i2] = readFloat();
        }
        return fArr;
    }

    public float[] readFloatArray(float[] fArr, int i) {
        return !readField(i) ? fArr : readFloatArray();
    }

    protected abstract int readInt();

    public int readInt(int i, int i2) {
        return !readField(i2) ? i : readInt();
    }

    protected int[] readIntArray() {
        int i = readInt();
        if (i < 0) {
            return null;
        }
        int[] iArr = new int[i];
        for (int i2 = 0; i2 < i; i2++) {
            iArr[i2] = readInt();
        }
        return iArr;
    }

    public int[] readIntArray(int[] iArr, int i) {
        return !readField(i) ? iArr : readIntArray();
    }

    public <T> List<T> readList(List<T> list, int i) {
        return !readField(i) ? list : (List) readCollection(i, new ArrayList());
    }

    protected abstract long readLong();

    public long readLong(long j, int i) {
        return !readField(i) ? j : readLong();
    }

    protected long[] readLongArray() {
        int i = readInt();
        if (i < 0) {
            return null;
        }
        long[] jArr = new long[i];
        for (int i2 = 0; i2 < i; i2++) {
            jArr[i2] = readLong();
        }
        return jArr;
    }

    public long[] readLongArray(long[] jArr, int i) {
        return !readField(i) ? jArr : readLongArray();
    }

    protected abstract <T extends Parcelable> T readParcelable();

    public <T extends Parcelable> T readParcelable(T t, int i) {
        return !readField(i) ? t : (T) readParcelable();
    }

    protected Serializable readSerializable() {
        String string = readString();
        if (string == null) {
            return null;
        }
        try {
            return (Serializable) new ObjectInputStream(this, new ByteArrayInputStream(readByteArray())) { // from class: androidx.versionedparcelable.VersionedParcel.1
                final VersionedParcel this$0;

                {
                    this.this$0 = this;
                }

                @Override // java.io.ObjectInputStream
                protected Class<?> resolveClass(ObjectStreamClass objectStreamClass) throws ClassNotFoundException, IOException {
                    Class<?> cls = Class.forName(objectStreamClass.getName(), false, getClass().getClassLoader());
                    return cls != null ? cls : super.resolveClass(objectStreamClass);
                }
            }.readObject();
        } catch (IOException e) {
            throw new RuntimeException("VersionedParcelable encountered IOException reading a Serializable object (name = " + string + ")", e);
        } catch (ClassNotFoundException e2) {
            throw new RuntimeException("VersionedParcelable encountered ClassNotFoundException reading a Serializable object (name = " + string + ")", e2);
        }
    }

    public <T> Set<T> readSet(Set<T> set, int i) {
        return !readField(i) ? set : (Set) readCollection(i, new ArraySet());
    }

    public Size readSize(Size size, int i) {
        if (!readField(i)) {
            return size;
        }
        if (readBoolean()) {
            return new Size(readInt(), readInt());
        }
        return null;
    }

    public SizeF readSizeF(SizeF sizeF, int i) {
        if (!readField(i)) {
            return sizeF;
        }
        if (readBoolean()) {
            return new SizeF(readFloat(), readFloat());
        }
        return null;
    }

    public SparseBooleanArray readSparseBooleanArray(SparseBooleanArray sparseBooleanArray, int i) {
        if (!readField(i)) {
            return sparseBooleanArray;
        }
        int i2 = readInt();
        if (i2 < 0) {
            return null;
        }
        SparseBooleanArray sparseBooleanArray2 = new SparseBooleanArray(i2);
        for (int i3 = 0; i3 < i2; i3++) {
            sparseBooleanArray2.put(readInt(), readBoolean());
        }
        return sparseBooleanArray2;
    }

    protected abstract String readString();

    public String readString(String str, int i) {
        return !readField(i) ? str : readString();
    }

    protected abstract IBinder readStrongBinder();

    public IBinder readStrongBinder(IBinder iBinder, int i) {
        return !readField(i) ? iBinder : readStrongBinder();
    }

    protected <T extends VersionedParcelable> T readVersionedParcelable() {
        String string = readString();
        if (string == null) {
            return null;
        }
        return (T) readFromParcel(string, createSubParcel());
    }

    public <T extends VersionedParcelable> T readVersionedParcelable(T t, int i) {
        return !readField(i) ? t : (T) readVersionedParcelable();
    }

    protected abstract void setOutputField(int i);

    public void setSerializationFlags(boolean z, boolean z2) {
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected <T> void writeArray(T[] tArr) throws IllegalAccessException, IOException, IllegalArgumentException, InvocationTargetException {
        if (tArr == 0) {
            writeInt(-1);
            return;
        }
        int length = tArr.length;
        writeInt(length);
        if (length > 0) {
            int type = getType(tArr[0]);
            writeInt(type);
            if (type == 1) {
                for (Object[] objArr : tArr) {
                    writeVersionedParcelable((VersionedParcelable) objArr);
                }
                return;
            }
            if (type == 2) {
                for (Object[] objArr2 : tArr) {
                    writeParcelable((Parcelable) objArr2);
                }
                return;
            }
            if (type == 3) {
                for (Object[] objArr3 : tArr) {
                    writeSerializable((Serializable) objArr3);
                }
                return;
            }
            if (type == 4) {
                for (Object[] objArr4 : tArr) {
                    writeString((String) objArr4);
                }
            } else {
                if (type != 5) {
                    return;
                }
                for (Object[] objArr5 : tArr) {
                    writeStrongBinder((IBinder) objArr5);
                }
            }
        }
    }

    public <T> void writeArray(T[] tArr, int i) throws IllegalAccessException, IOException, IllegalArgumentException, InvocationTargetException {
        setOutputField(i);
        writeArray(tArr);
    }

    protected abstract void writeBoolean(boolean z);

    public void writeBoolean(boolean z, int i) {
        setOutputField(i);
        writeBoolean(z);
    }

    protected void writeBooleanArray(boolean[] zArr) {
        if (zArr == null) {
            writeInt(-1);
            return;
        }
        writeInt(zArr.length);
        for (boolean z : zArr) {
            writeInt(z ? 1 : 0);
        }
    }

    public void writeBooleanArray(boolean[] zArr, int i) {
        setOutputField(i);
        writeBooleanArray(zArr);
    }

    protected abstract void writeBundle(Bundle bundle);

    public void writeBundle(Bundle bundle, int i) {
        setOutputField(i);
        writeBundle(bundle);
    }

    public void writeByte(byte b, int i) {
        setOutputField(i);
        writeInt(b);
    }

    protected abstract void writeByteArray(byte[] bArr);

    public void writeByteArray(byte[] bArr, int i) {
        setOutputField(i);
        writeByteArray(bArr);
    }

    protected abstract void writeByteArray(byte[] bArr, int i, int i2);

    public void writeByteArray(byte[] bArr, int i, int i2, int i3) {
        setOutputField(i3);
        writeByteArray(bArr, i, i2);
    }

    public void writeCharArray(char[] cArr, int i) {
        setOutputField(i);
        if (cArr == null) {
            writeInt(-1);
            return;
        }
        writeInt(cArr.length);
        for (char c : cArr) {
            writeInt(c);
        }
    }

    protected abstract void writeDouble(double d);

    public void writeDouble(double d, int i) {
        setOutputField(i);
        writeDouble(d);
    }

    protected void writeDoubleArray(double[] dArr) {
        if (dArr == null) {
            writeInt(-1);
            return;
        }
        writeInt(dArr.length);
        for (double d : dArr) {
            writeDouble(d);
        }
    }

    public void writeDoubleArray(double[] dArr, int i) {
        setOutputField(i);
        writeDoubleArray(dArr);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void writeException(Exception exc, int i) {
        setOutputField(i);
        if (exc == 0) {
            writeNoException();
            return;
        }
        int i2 = 0;
        if ((exc instanceof Parcelable) && exc.getClass().getClassLoader() == Parcelable.class.getClassLoader()) {
            i2 = EX_PARCELABLE;
        } else if (exc instanceof SecurityException) {
            i2 = -1;
        } else if (exc instanceof BadParcelableException) {
            i2 = -2;
        } else if (exc instanceof IllegalArgumentException) {
            i2 = -3;
        } else if (exc instanceof NullPointerException) {
            i2 = -4;
        } else if (exc instanceof IllegalStateException) {
            i2 = EX_ILLEGAL_STATE;
        } else if (exc instanceof NetworkOnMainThreadException) {
            i2 = EX_NETWORK_MAIN_THREAD;
        } else if (exc instanceof UnsupportedOperationException) {
            i2 = EX_UNSUPPORTED_OPERATION;
        }
        writeInt(i2);
        if (i2 == 0) {
            if (!(exc instanceof RuntimeException)) {
                throw new RuntimeException(exc);
            }
            throw ((RuntimeException) exc);
        }
        writeString(exc.getMessage());
        if (i2 != EX_PARCELABLE) {
            return;
        }
        writeParcelable((Parcelable) exc);
    }

    protected abstract void writeFloat(float f);

    public void writeFloat(float f, int i) {
        setOutputField(i);
        writeFloat(f);
    }

    protected void writeFloatArray(float[] fArr) {
        if (fArr == null) {
            writeInt(-1);
            return;
        }
        writeInt(fArr.length);
        for (float f : fArr) {
            writeFloat(f);
        }
    }

    public void writeFloatArray(float[] fArr, int i) {
        setOutputField(i);
        writeFloatArray(fArr);
    }

    protected abstract void writeInt(int i);

    public void writeInt(int i, int i2) {
        setOutputField(i2);
        writeInt(i);
    }

    protected void writeIntArray(int[] iArr) {
        if (iArr == null) {
            writeInt(-1);
            return;
        }
        writeInt(iArr.length);
        for (int i : iArr) {
            writeInt(i);
        }
    }

    public void writeIntArray(int[] iArr, int i) {
        setOutputField(i);
        writeIntArray(iArr);
    }

    public <T> void writeList(List<T> list, int i) throws IllegalAccessException, IOException, IllegalArgumentException, InvocationTargetException {
        writeCollection(list, i);
    }

    protected abstract void writeLong(long j);

    public void writeLong(long j, int i) {
        setOutputField(i);
        writeLong(j);
    }

    protected void writeLongArray(long[] jArr) {
        if (jArr == null) {
            writeInt(-1);
            return;
        }
        writeInt(jArr.length);
        for (long j : jArr) {
            writeLong(j);
        }
    }

    public void writeLongArray(long[] jArr, int i) {
        setOutputField(i);
        writeLongArray(jArr);
    }

    protected void writeNoException() {
        writeInt(0);
    }

    protected abstract void writeParcelable(Parcelable parcelable);

    public void writeParcelable(Parcelable parcelable, int i) {
        setOutputField(i);
        writeParcelable(parcelable);
    }

    public void writeSerializable(Serializable serializable, int i) throws IOException {
        setOutputField(i);
        writeSerializable(serializable);
    }

    public <T> void writeSet(Set<T> set, int i) throws IllegalAccessException, IOException, IllegalArgumentException, InvocationTargetException {
        writeCollection(set, i);
    }

    public void writeSize(Size size, int i) {
        setOutputField(i);
        writeBoolean(size != null);
        if (size != null) {
            writeInt(size.getWidth());
            writeInt(size.getHeight());
        }
    }

    public void writeSizeF(SizeF sizeF, int i) {
        setOutputField(i);
        writeBoolean(sizeF != null);
        if (sizeF != null) {
            writeFloat(sizeF.getWidth());
            writeFloat(sizeF.getHeight());
        }
    }

    public void writeSparseBooleanArray(SparseBooleanArray sparseBooleanArray, int i) {
        setOutputField(i);
        if (sparseBooleanArray == null) {
            writeInt(-1);
            return;
        }
        int size = sparseBooleanArray.size();
        writeInt(size);
        for (int i2 = 0; i2 < size; i2++) {
            writeInt(sparseBooleanArray.keyAt(i2));
            writeBoolean(sparseBooleanArray.valueAt(i2));
        }
    }

    protected abstract void writeString(String str);

    public void writeString(String str, int i) {
        setOutputField(i);
        writeString(str);
    }

    protected abstract void writeStrongBinder(IBinder iBinder);

    public void writeStrongBinder(IBinder iBinder, int i) {
        setOutputField(i);
        writeStrongBinder(iBinder);
    }

    protected abstract void writeStrongInterface(IInterface iInterface);

    public void writeStrongInterface(IInterface iInterface, int i) {
        setOutputField(i);
        writeStrongInterface(iInterface);
    }

    protected void writeVersionedParcelable(VersionedParcelable versionedParcelable) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (versionedParcelable == null) {
            writeString(null);
            return;
        }
        writeVersionedParcelableCreator(versionedParcelable);
        VersionedParcel versionedParcelCreateSubParcel = createSubParcel();
        writeToParcel(versionedParcelable, versionedParcelCreateSubParcel);
        versionedParcelCreateSubParcel.closeField();
    }

    public void writeVersionedParcelable(VersionedParcelable versionedParcelable, int i) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        setOutputField(i);
        writeVersionedParcelable(versionedParcelable);
    }
}
