package com.google.gson.internal;

import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/* loaded from: classes-dex2jar.jar:com/google/gson/internal/UnsafeAllocator.class */
public abstract class UnsafeAllocator {
    static void assertInstantiable(Class<?> cls) {
        int modifiers = cls.getModifiers();
        if (Modifier.isInterface(modifiers)) {
            throw new UnsupportedOperationException("Interface can't be instantiated! Interface name: " + cls.getName());
        }
        if (Modifier.isAbstract(modifiers)) {
            throw new UnsupportedOperationException("Abstract class can't be instantiated! Class name: " + cls.getName());
        }
    }

    public static UnsafeAllocator create() throws NoSuchFieldException, NoSuchMethodException, ClassNotFoundException, SecurityException {
        try {
            Class<?> cls = Class.forName("sun.misc.Unsafe");
            Field declaredField = cls.getDeclaredField("theUnsafe");
            declaredField.setAccessible(true);
            return new UnsafeAllocator(cls.getMethod("allocateInstance", Class.class), declaredField.get(null)) { // from class: com.google.gson.internal.UnsafeAllocator.1
                final Method val$allocateInstance;
                final Object val$unsafe;

                {
                    this.val$allocateInstance = method;
                    this.val$unsafe = obj;
                }

                @Override // com.google.gson.internal.UnsafeAllocator
                public <T> T newInstance(Class<T> cls2) throws Exception {
                    assertInstantiable(cls2);
                    return (T) this.val$allocateInstance.invoke(this.val$unsafe, cls2);
                }
            };
        } catch (Exception e) {
            try {
                Method declaredMethod = ObjectStreamClass.class.getDeclaredMethod("getConstructorId", Class.class);
                declaredMethod.setAccessible(true);
                int iIntValue = ((Integer) declaredMethod.invoke(null, Object.class)).intValue();
                Method declaredMethod2 = ObjectStreamClass.class.getDeclaredMethod("newInstance", Class.class, Integer.TYPE);
                declaredMethod2.setAccessible(true);
                return new UnsafeAllocator(declaredMethod2, iIntValue) { // from class: com.google.gson.internal.UnsafeAllocator.2
                    final int val$constructorId;
                    final Method val$newInstance;

                    {
                        this.val$newInstance = declaredMethod2;
                        this.val$constructorId = iIntValue;
                    }

                    @Override // com.google.gson.internal.UnsafeAllocator
                    public <T> T newInstance(Class<T> cls2) throws Exception {
                        assertInstantiable(cls2);
                        return (T) this.val$newInstance.invoke(null, cls2, Integer.valueOf(this.val$constructorId));
                    }
                };
            } catch (Exception e2) {
                try {
                    Method declaredMethod3 = ObjectInputStream.class.getDeclaredMethod("newInstance", Class.class, Class.class);
                    declaredMethod3.setAccessible(true);
                    return new UnsafeAllocator(declaredMethod3) { // from class: com.google.gson.internal.UnsafeAllocator.3
                        final Method val$newInstance;

                        {
                            this.val$newInstance = declaredMethod3;
                        }

                        @Override // com.google.gson.internal.UnsafeAllocator
                        public <T> T newInstance(Class<T> cls2) throws Exception {
                            assertInstantiable(cls2);
                            return (T) this.val$newInstance.invoke(null, cls2, Object.class);
                        }
                    };
                } catch (Exception e3) {
                    return new UnsafeAllocator() { // from class: com.google.gson.internal.UnsafeAllocator.4
                        @Override // com.google.gson.internal.UnsafeAllocator
                        public <T> T newInstance(Class<T> cls2) {
                            throw new UnsupportedOperationException("Cannot allocate " + cls2);
                        }
                    };
                }
            }
        }
    }

    public abstract <T> T newInstance(Class<T> cls) throws Exception;
}
