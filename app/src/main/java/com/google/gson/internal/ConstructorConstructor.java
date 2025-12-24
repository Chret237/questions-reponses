package com.google.gson.internal;

import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/* loaded from: classes-dex2jar.jar:com/google/gson/internal/ConstructorConstructor.class */
public final class ConstructorConstructor {
    private final Map<Type, InstanceCreator<?>> instanceCreators;

    public ConstructorConstructor(Map<Type, InstanceCreator<?>> map) {
        this.instanceCreators = map;
    }

    private <T> ObjectConstructor<T> newDefaultConstructor(Class<? super T> cls) throws NoSuchMethodException, SecurityException {
        try {
            Constructor<? super T> declaredConstructor = cls.getDeclaredConstructor(new Class[0]);
            if (!declaredConstructor.isAccessible()) {
                declaredConstructor.setAccessible(true);
            }
            return new ObjectConstructor<T>(this, declaredConstructor) { // from class: com.google.gson.internal.ConstructorConstructor.3
                final ConstructorConstructor this$0;
                final Constructor val$constructor;

                {
                    this.this$0 = this;
                    this.val$constructor = declaredConstructor;
                }

                @Override // com.google.gson.internal.ObjectConstructor
                public T construct() {
                    try {
                        return (T) this.val$constructor.newInstance(null);
                    } catch (IllegalAccessException e) {
                        throw new AssertionError(e);
                    } catch (InstantiationException e2) {
                        throw new RuntimeException("Failed to invoke " + this.val$constructor + " with no args", e2);
                    } catch (InvocationTargetException e3) {
                        throw new RuntimeException("Failed to invoke " + this.val$constructor + " with no args", e3.getTargetException());
                    }
                }
            };
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private <T> ObjectConstructor<T> newDefaultImplementationConstructor(Type type, Class<? super T> cls) {
        if (Collection.class.isAssignableFrom(cls)) {
            return SortedSet.class.isAssignableFrom(cls) ? new ObjectConstructor<T>(this) { // from class: com.google.gson.internal.ConstructorConstructor.4
                final ConstructorConstructor this$0;

                {
                    this.this$0 = this;
                }

                @Override // com.google.gson.internal.ObjectConstructor
                public T construct() {
                    return (T) new TreeSet();
                }
            } : EnumSet.class.isAssignableFrom(cls) ? new ObjectConstructor<T>(this, type) { // from class: com.google.gson.internal.ConstructorConstructor.5
                final ConstructorConstructor this$0;
                final Type val$type;

                {
                    this.this$0 = this;
                    this.val$type = type;
                }

                @Override // com.google.gson.internal.ObjectConstructor
                public T construct() {
                    Type type2 = this.val$type;
                    if (!(type2 instanceof ParameterizedType)) {
                        throw new JsonIOException("Invalid EnumSet type: " + this.val$type.toString());
                    }
                    Type type3 = ((ParameterizedType) type2).getActualTypeArguments()[0];
                    if (type3 instanceof Class) {
                        return (T) EnumSet.noneOf((Class) type3);
                    }
                    throw new JsonIOException("Invalid EnumSet type: " + this.val$type.toString());
                }
            } : Set.class.isAssignableFrom(cls) ? new ObjectConstructor<T>(this) { // from class: com.google.gson.internal.ConstructorConstructor.6
                final ConstructorConstructor this$0;

                {
                    this.this$0 = this;
                }

                @Override // com.google.gson.internal.ObjectConstructor
                public T construct() {
                    return (T) new LinkedHashSet();
                }
            } : Queue.class.isAssignableFrom(cls) ? new ObjectConstructor<T>(this) { // from class: com.google.gson.internal.ConstructorConstructor.7
                final ConstructorConstructor this$0;

                {
                    this.this$0 = this;
                }

                @Override // com.google.gson.internal.ObjectConstructor
                public T construct() {
                    return (T) new ArrayDeque();
                }
            } : new ObjectConstructor<T>(this) { // from class: com.google.gson.internal.ConstructorConstructor.8
                final ConstructorConstructor this$0;

                {
                    this.this$0 = this;
                }

                @Override // com.google.gson.internal.ObjectConstructor
                public T construct() {
                    return (T) new ArrayList();
                }
            };
        }
        if (Map.class.isAssignableFrom(cls)) {
            return ConcurrentNavigableMap.class.isAssignableFrom(cls) ? new ObjectConstructor<T>(this) { // from class: com.google.gson.internal.ConstructorConstructor.9
                final ConstructorConstructor this$0;

                {
                    this.this$0 = this;
                }

                @Override // com.google.gson.internal.ObjectConstructor
                public T construct() {
                    return (T) new ConcurrentSkipListMap();
                }
            } : ConcurrentMap.class.isAssignableFrom(cls) ? new ObjectConstructor<T>(this) { // from class: com.google.gson.internal.ConstructorConstructor.10
                final ConstructorConstructor this$0;

                {
                    this.this$0 = this;
                }

                @Override // com.google.gson.internal.ObjectConstructor
                public T construct() {
                    return (T) new ConcurrentHashMap();
                }
            } : SortedMap.class.isAssignableFrom(cls) ? new ObjectConstructor<T>(this) { // from class: com.google.gson.internal.ConstructorConstructor.11
                final ConstructorConstructor this$0;

                {
                    this.this$0 = this;
                }

                @Override // com.google.gson.internal.ObjectConstructor
                public T construct() {
                    return (T) new TreeMap();
                }
            } : (!(type instanceof ParameterizedType) || String.class.isAssignableFrom(TypeToken.get(((ParameterizedType) type).getActualTypeArguments()[0]).getRawType())) ? new ObjectConstructor<T>(this) { // from class: com.google.gson.internal.ConstructorConstructor.13
                final ConstructorConstructor this$0;

                {
                    this.this$0 = this;
                }

                @Override // com.google.gson.internal.ObjectConstructor
                public T construct() {
                    return (T) new LinkedTreeMap();
                }
            } : new ObjectConstructor<T>(this) { // from class: com.google.gson.internal.ConstructorConstructor.12
                final ConstructorConstructor this$0;

                {
                    this.this$0 = this;
                }

                @Override // com.google.gson.internal.ObjectConstructor
                public T construct() {
                    return (T) new LinkedHashMap();
                }
            };
        }
        return null;
    }

    private <T> ObjectConstructor<T> newUnsafeAllocator(Type type, Class<? super T> cls) {
        return new ObjectConstructor<T>(this, cls, type) { // from class: com.google.gson.internal.ConstructorConstructor.14
            final ConstructorConstructor this$0;
            private final UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
            final Class val$rawType;
            final Type val$type;

            {
                this.this$0 = this;
                this.val$rawType = cls;
                this.val$type = type;
            }

            @Override // com.google.gson.internal.ObjectConstructor
            public T construct() {
                try {
                    return (T) this.unsafeAllocator.newInstance(this.val$rawType);
                } catch (Exception e) {
                    throw new RuntimeException("Unable to invoke no-args constructor for " + this.val$type + ". Registering an InstanceCreator with Gson for this type may fix this problem.", e);
                }
            }
        };
    }

    public <T> ObjectConstructor<T> get(TypeToken<T> typeToken) throws NoSuchMethodException, SecurityException {
        Type type = typeToken.getType();
        Class<? super T> rawType = typeToken.getRawType();
        InstanceCreator<?> instanceCreator = this.instanceCreators.get(type);
        if (instanceCreator != null) {
            return new ObjectConstructor<T>(this, instanceCreator, type) { // from class: com.google.gson.internal.ConstructorConstructor.1
                final ConstructorConstructor this$0;
                final Type val$type;
                final InstanceCreator val$typeCreator;

                {
                    this.this$0 = this;
                    this.val$typeCreator = instanceCreator;
                    this.val$type = type;
                }

                @Override // com.google.gson.internal.ObjectConstructor
                public T construct() {
                    return (T) this.val$typeCreator.createInstance(this.val$type);
                }
            };
        }
        InstanceCreator<?> instanceCreator2 = this.instanceCreators.get(rawType);
        if (instanceCreator2 != null) {
            return new ObjectConstructor<T>(this, instanceCreator2, type) { // from class: com.google.gson.internal.ConstructorConstructor.2
                final ConstructorConstructor this$0;
                final InstanceCreator val$rawTypeCreator;
                final Type val$type;

                {
                    this.this$0 = this;
                    this.val$rawTypeCreator = instanceCreator2;
                    this.val$type = type;
                }

                @Override // com.google.gson.internal.ObjectConstructor
                public T construct() {
                    return (T) this.val$rawTypeCreator.createInstance(this.val$type);
                }
            };
        }
        ObjectConstructor<T> objectConstructorNewDefaultConstructor = newDefaultConstructor(rawType);
        if (objectConstructorNewDefaultConstructor != null) {
            return objectConstructorNewDefaultConstructor;
        }
        ObjectConstructor<T> objectConstructorNewDefaultImplementationConstructor = newDefaultImplementationConstructor(type, rawType);
        return objectConstructorNewDefaultImplementationConstructor != null ? objectConstructorNewDefaultImplementationConstructor : newUnsafeAllocator(type, rawType);
    }

    public String toString() {
        return this.instanceCreators.toString();
    }
}
