package com.google.gson.internal;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.Since;
import com.google.gson.annotations.Until;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes-dex2jar.jar:com/google/gson/internal/Excluder.class */
public final class Excluder implements TypeAdapterFactory, Cloneable {
    public static final Excluder DEFAULT = new Excluder();
    private static final double IGNORE_VERSIONS = -1.0d;
    private boolean requireExpose;
    private double version = IGNORE_VERSIONS;
    private int modifiers = 136;
    private boolean serializeInnerClasses = true;
    private List<ExclusionStrategy> serializationStrategies = Collections.emptyList();
    private List<ExclusionStrategy> deserializationStrategies = Collections.emptyList();

    private boolean isAnonymousOrLocal(Class<?> cls) {
        return !Enum.class.isAssignableFrom(cls) && (cls.isAnonymousClass() || cls.isLocalClass());
    }

    private boolean isInnerClass(Class<?> cls) {
        return cls.isMemberClass() && !isStatic(cls);
    }

    private boolean isStatic(Class<?> cls) {
        return (cls.getModifiers() & 8) != 0;
    }

    private boolean isValidSince(Since since) {
        return since == null || since.value() <= this.version;
    }

    private boolean isValidUntil(Until until) {
        return until == null || until.value() > this.version;
    }

    private boolean isValidVersion(Since since, Until until) {
        return isValidSince(since) && isValidUntil(until);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public Excluder m398clone() {
        try {
            return (Excluder) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    @Override // com.google.gson.TypeAdapterFactory
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        Class<? super T> rawType = typeToken.getRawType();
        boolean zExcludeClass = excludeClass(rawType, true);
        boolean zExcludeClass2 = excludeClass(rawType, false);
        if (zExcludeClass || zExcludeClass2) {
            return new TypeAdapter<T>(this, zExcludeClass2, zExcludeClass, gson, typeToken) { // from class: com.google.gson.internal.Excluder.1
                private TypeAdapter<T> delegate;
                final Excluder this$0;
                final Gson val$gson;
                final boolean val$skipDeserialize;
                final boolean val$skipSerialize;
                final TypeToken val$type;

                {
                    this.this$0 = this;
                    this.val$skipDeserialize = zExcludeClass2;
                    this.val$skipSerialize = zExcludeClass;
                    this.val$gson = gson;
                    this.val$type = typeToken;
                }

                private TypeAdapter<T> delegate() {
                    TypeAdapter<T> delegateAdapter = this.delegate;
                    if (delegateAdapter == null) {
                        delegateAdapter = this.val$gson.getDelegateAdapter(this.this$0, this.val$type);
                        this.delegate = delegateAdapter;
                    }
                    return delegateAdapter;
                }

                @Override // com.google.gson.TypeAdapter
                /* renamed from: read */
                public T read2(JsonReader jsonReader) throws IOException {
                    if (!this.val$skipDeserialize) {
                        return delegate().read2(jsonReader);
                    }
                    jsonReader.skipValue();
                    return null;
                }

                @Override // com.google.gson.TypeAdapter
                public void write(JsonWriter jsonWriter, T t) throws IOException {
                    if (this.val$skipSerialize) {
                        jsonWriter.nullValue();
                    } else {
                        delegate().write(jsonWriter, t);
                    }
                }
            };
        }
        return null;
    }

    public Excluder disableInnerClassSerialization() {
        Excluder excluderM398clone = m398clone();
        excluderM398clone.serializeInnerClasses = false;
        return excluderM398clone;
    }

    public boolean excludeClass(Class<?> cls, boolean z) {
        if (this.version != IGNORE_VERSIONS && !isValidVersion((Since) cls.getAnnotation(Since.class), (Until) cls.getAnnotation(Until.class))) {
            return true;
        }
        if ((!this.serializeInnerClasses && isInnerClass(cls)) || isAnonymousOrLocal(cls)) {
            return true;
        }
        Iterator<ExclusionStrategy> it = (z ? this.serializationStrategies : this.deserializationStrategies).iterator();
        while (it.hasNext()) {
            if (it.next().shouldSkipClass(cls)) {
                return true;
            }
        }
        return false;
    }

    public boolean excludeField(Field field, boolean z) {
        if ((this.modifiers & field.getModifiers()) != 0) {
            return true;
        }
        if ((this.version != IGNORE_VERSIONS && !isValidVersion((Since) field.getAnnotation(Since.class), (Until) field.getAnnotation(Until.class))) || field.isSynthetic()) {
            return true;
        }
        if (this.requireExpose) {
            Expose expose = (Expose) field.getAnnotation(Expose.class);
            if (expose == null) {
                return true;
            }
            if (z) {
                if (!expose.serialize()) {
                    return true;
                }
            } else if (!expose.deserialize()) {
                return true;
            }
        }
        if ((!this.serializeInnerClasses && isInnerClass(field.getType())) || isAnonymousOrLocal(field.getType())) {
            return true;
        }
        List<ExclusionStrategy> list = z ? this.serializationStrategies : this.deserializationStrategies;
        if (list.isEmpty()) {
            return false;
        }
        FieldAttributes fieldAttributes = new FieldAttributes(field);
        Iterator<ExclusionStrategy> it = list.iterator();
        while (it.hasNext()) {
            if (it.next().shouldSkipField(fieldAttributes)) {
                return true;
            }
        }
        return false;
    }

    public Excluder excludeFieldsWithoutExposeAnnotation() {
        Excluder excluderM398clone = m398clone();
        excluderM398clone.requireExpose = true;
        return excluderM398clone;
    }

    public Excluder withExclusionStrategy(ExclusionStrategy exclusionStrategy, boolean z, boolean z2) {
        Excluder excluderM398clone = m398clone();
        if (z) {
            ArrayList arrayList = new ArrayList(this.serializationStrategies);
            excluderM398clone.serializationStrategies = arrayList;
            arrayList.add(exclusionStrategy);
        }
        if (z2) {
            ArrayList arrayList2 = new ArrayList(this.deserializationStrategies);
            excluderM398clone.deserializationStrategies = arrayList2;
            arrayList2.add(exclusionStrategy);
        }
        return excluderM398clone;
    }

    public Excluder withModifiers(int... iArr) {
        Excluder excluderM398clone = m398clone();
        excluderM398clone.modifiers = 0;
        for (int i : iArr) {
            excluderM398clone.modifiers = i | excluderM398clone.modifiers;
        }
        return excluderM398clone;
    }

    public Excluder withVersion(double d) {
        Excluder excluderM398clone = m398clone();
        excluderM398clone.version = d;
        return excluderM398clone;
    }
}
