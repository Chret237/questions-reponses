package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.LazilyParsedNumber;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

/* loaded from: classes-dex2jar.jar:com/google/gson/internal/bind/TypeAdapters.class */
public final class TypeAdapters {
    public static final TypeAdapter<AtomicBoolean> ATOMIC_BOOLEAN;
    public static final TypeAdapterFactory ATOMIC_BOOLEAN_FACTORY;
    public static final TypeAdapter<AtomicInteger> ATOMIC_INTEGER;
    public static final TypeAdapter<AtomicIntegerArray> ATOMIC_INTEGER_ARRAY;
    public static final TypeAdapterFactory ATOMIC_INTEGER_ARRAY_FACTORY;
    public static final TypeAdapterFactory ATOMIC_INTEGER_FACTORY;
    public static final TypeAdapter<BigDecimal> BIG_DECIMAL;
    public static final TypeAdapter<BigInteger> BIG_INTEGER;
    public static final TypeAdapter<BitSet> BIT_SET;
    public static final TypeAdapterFactory BIT_SET_FACTORY;
    public static final TypeAdapter<Boolean> BOOLEAN;
    public static final TypeAdapter<Boolean> BOOLEAN_AS_STRING;
    public static final TypeAdapterFactory BOOLEAN_FACTORY;
    public static final TypeAdapter<Number> BYTE;
    public static final TypeAdapterFactory BYTE_FACTORY;
    public static final TypeAdapter<Calendar> CALENDAR;
    public static final TypeAdapterFactory CALENDAR_FACTORY;
    public static final TypeAdapter<Character> CHARACTER;
    public static final TypeAdapterFactory CHARACTER_FACTORY;
    public static final TypeAdapter<Class> CLASS;
    public static final TypeAdapterFactory CLASS_FACTORY;
    public static final TypeAdapter<Currency> CURRENCY;
    public static final TypeAdapterFactory CURRENCY_FACTORY;
    public static final TypeAdapter<Number> DOUBLE;
    public static final TypeAdapterFactory ENUM_FACTORY;
    public static final TypeAdapter<Number> FLOAT;
    public static final TypeAdapter<InetAddress> INET_ADDRESS;
    public static final TypeAdapterFactory INET_ADDRESS_FACTORY;
    public static final TypeAdapter<Number> INTEGER;
    public static final TypeAdapterFactory INTEGER_FACTORY;
    public static final TypeAdapter<JsonElement> JSON_ELEMENT;
    public static final TypeAdapterFactory JSON_ELEMENT_FACTORY;
    public static final TypeAdapter<Locale> LOCALE;
    public static final TypeAdapterFactory LOCALE_FACTORY;
    public static final TypeAdapter<Number> LONG;
    public static final TypeAdapter<Number> NUMBER;
    public static final TypeAdapterFactory NUMBER_FACTORY;
    public static final TypeAdapter<Number> SHORT;
    public static final TypeAdapterFactory SHORT_FACTORY;
    public static final TypeAdapter<String> STRING;
    public static final TypeAdapter<StringBuffer> STRING_BUFFER;
    public static final TypeAdapterFactory STRING_BUFFER_FACTORY;
    public static final TypeAdapter<StringBuilder> STRING_BUILDER;
    public static final TypeAdapterFactory STRING_BUILDER_FACTORY;
    public static final TypeAdapterFactory STRING_FACTORY;
    public static final TypeAdapterFactory TIMESTAMP_FACTORY;
    public static final TypeAdapter<URI> URI;
    public static final TypeAdapterFactory URI_FACTORY;
    public static final TypeAdapter<URL> URL;
    public static final TypeAdapterFactory URL_FACTORY;
    public static final TypeAdapter<UUID> UUID;
    public static final TypeAdapterFactory UUID_FACTORY;

    /* renamed from: com.google.gson.internal.bind.TypeAdapters$35 */
    /* loaded from: classes-dex2jar.jar:com/google/gson/internal/bind/TypeAdapters$35.class */
    static final class C049935 implements TypeAdapterFactory {
        final Class val$clazz;
        final TypeAdapter val$typeAdapter;

        C049935(Class cls, TypeAdapter typeAdapter) {
            this.val$clazz = cls;
            this.val$typeAdapter = typeAdapter;
        }

        @Override // com.google.gson.TypeAdapterFactory
        public <T2> TypeAdapter<T2> create(Gson gson, TypeToken<T2> typeToken) {
            Class<? super T2> rawType = typeToken.getRawType();
            if (this.val$clazz.isAssignableFrom(rawType)) {
                return (TypeAdapter<T2>) new TypeAdapter<T1>(this, rawType) { // from class: com.google.gson.internal.bind.TypeAdapters.35.1
                    final C049935 this$0;
                    final Class val$requestedType;

                    {
                        this.this$0 = this;
                        this.val$requestedType = rawType;
                    }

                    @Override // com.google.gson.TypeAdapter
                    /* renamed from: read */
                    public T1 read2(JsonReader jsonReader) throws IOException {
                        T1 t1 = (T1) this.this$0.val$typeAdapter.read2(jsonReader);
                        if (t1 == null || this.val$requestedType.isInstance(t1)) {
                            return t1;
                        }
                        throw new JsonSyntaxException("Expected a " + this.val$requestedType.getName() + " but was " + t1.getClass().getName());
                    }

                    @Override // com.google.gson.TypeAdapter
                    public void write(JsonWriter jsonWriter, T1 t1) throws IOException {
                        this.this$0.val$typeAdapter.write(jsonWriter, t1);
                    }
                };
            }
            return null;
        }

        public String toString() {
            return "Factory[typeHierarchy=" + this.val$clazz.getName() + ",adapter=" + this.val$typeAdapter + "]";
        }
    }

    /* renamed from: com.google.gson.internal.bind.TypeAdapters$36 */
    /* loaded from: classes-dex2jar.jar:com/google/gson/internal/bind/TypeAdapters$36.class */
    static /* synthetic */ class C050036 {
        static final int[] $SwitchMap$com$google$gson$stream$JsonToken;

        /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
            jadx.core.utils.exceptions.JadxRuntimeException: Can't find top splitter block for handler:B:39:0x009d
            	at jadx.core.utils.BlockUtils.getTopSplitterForHandler(BlockUtils.java:1178)
            	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.collectHandlerRegions(ExcHandlersRegionMaker.java:53)
            	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.process(ExcHandlersRegionMaker.java:38)
            	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:27)
            */
        static {
            /*
                com.google.gson.stream.JsonToken[] r0 = com.google.gson.stream.JsonToken.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                r4 = r0
                r0 = r4
                com.google.gson.internal.bind.TypeAdapters.C050036.$SwitchMap$com$google$gson$stream$JsonToken = r0
                r0 = r4
                com.google.gson.stream.JsonToken r1 = com.google.gson.stream.JsonToken.NUMBER     // Catch: java.lang.NoSuchFieldError -> L7d
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L7d
                r2 = 1
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L7d
            L14:
                int[] r0 = com.google.gson.internal.bind.TypeAdapters.C050036.$SwitchMap$com$google$gson$stream$JsonToken     // Catch: java.lang.NoSuchFieldError -> L7d java.lang.NoSuchFieldError -> L81
                com.google.gson.stream.JsonToken r1 = com.google.gson.stream.JsonToken.BOOLEAN     // Catch: java.lang.NoSuchFieldError -> L81
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L81
                r2 = 2
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L81
            L1f:
                int[] r0 = com.google.gson.internal.bind.TypeAdapters.C050036.$SwitchMap$com$google$gson$stream$JsonToken     // Catch: java.lang.NoSuchFieldError -> L81 java.lang.NoSuchFieldError -> L85
                com.google.gson.stream.JsonToken r1 = com.google.gson.stream.JsonToken.STRING     // Catch: java.lang.NoSuchFieldError -> L85
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L85
                r2 = 3
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L85
            L2a:
                int[] r0 = com.google.gson.internal.bind.TypeAdapters.C050036.$SwitchMap$com$google$gson$stream$JsonToken     // Catch: java.lang.NoSuchFieldError -> L85 java.lang.NoSuchFieldError -> L89
                com.google.gson.stream.JsonToken r1 = com.google.gson.stream.JsonToken.NULL     // Catch: java.lang.NoSuchFieldError -> L89
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L89
                r2 = 4
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L89
            L35:
                int[] r0 = com.google.gson.internal.bind.TypeAdapters.C050036.$SwitchMap$com$google$gson$stream$JsonToken     // Catch: java.lang.NoSuchFieldError -> L89 java.lang.NoSuchFieldError -> L8d
                com.google.gson.stream.JsonToken r1 = com.google.gson.stream.JsonToken.BEGIN_ARRAY     // Catch: java.lang.NoSuchFieldError -> L8d
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L8d
                r2 = 5
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L8d
            L40:
                int[] r0 = com.google.gson.internal.bind.TypeAdapters.C050036.$SwitchMap$com$google$gson$stream$JsonToken     // Catch: java.lang.NoSuchFieldError -> L8d java.lang.NoSuchFieldError -> L91
                com.google.gson.stream.JsonToken r1 = com.google.gson.stream.JsonToken.BEGIN_OBJECT     // Catch: java.lang.NoSuchFieldError -> L91
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L91
                r2 = 6
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L91
            L4c:
                int[] r0 = com.google.gson.internal.bind.TypeAdapters.C050036.$SwitchMap$com$google$gson$stream$JsonToken     // Catch: java.lang.NoSuchFieldError -> L91 java.lang.NoSuchFieldError -> L95
                com.google.gson.stream.JsonToken r1 = com.google.gson.stream.JsonToken.END_DOCUMENT     // Catch: java.lang.NoSuchFieldError -> L95
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L95
                r2 = 7
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L95
            L58:
                int[] r0 = com.google.gson.internal.bind.TypeAdapters.C050036.$SwitchMap$com$google$gson$stream$JsonToken     // Catch: java.lang.NoSuchFieldError -> L95 java.lang.NoSuchFieldError -> L99
                com.google.gson.stream.JsonToken r1 = com.google.gson.stream.JsonToken.NAME     // Catch: java.lang.NoSuchFieldError -> L99
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L99
                r2 = 8
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L99
            L64:
                int[] r0 = com.google.gson.internal.bind.TypeAdapters.C050036.$SwitchMap$com$google$gson$stream$JsonToken     // Catch: java.lang.NoSuchFieldError -> L99 java.lang.NoSuchFieldError -> L9d
                com.google.gson.stream.JsonToken r1 = com.google.gson.stream.JsonToken.END_OBJECT     // Catch: java.lang.NoSuchFieldError -> L9d
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> L9d
                r2 = 9
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> L9d
            L70:
                int[] r0 = com.google.gson.internal.bind.TypeAdapters.C050036.$SwitchMap$com$google$gson$stream$JsonToken     // Catch: java.lang.NoSuchFieldError -> L9d java.lang.NoSuchFieldError -> La1
                com.google.gson.stream.JsonToken r1 = com.google.gson.stream.JsonToken.END_ARRAY     // Catch: java.lang.NoSuchFieldError -> La1
                int r1 = r1.ordinal()     // Catch: java.lang.NoSuchFieldError -> La1
                r2 = 10
                r0[r1] = r2     // Catch: java.lang.NoSuchFieldError -> La1
            L7c:
                return
            L7d:
                r4 = move-exception
                goto L14
            L81:
                r4 = move-exception
                goto L1f
            L85:
                r4 = move-exception
                goto L2a
            L89:
                r4 = move-exception
                goto L35
            L8d:
                r4 = move-exception
                goto L40
            L91:
                r4 = move-exception
                goto L4c
            L95:
                r4 = move-exception
                goto L58
            L99:
                r4 = move-exception
                goto L64
            L9d:
                r4 = move-exception
                goto L70
            La1:
                r4 = move-exception
                goto L7c
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.gson.internal.bind.TypeAdapters.C050036.m411clinit():void");
        }
    }

    /* loaded from: classes-dex2jar.jar:com/google/gson/internal/bind/TypeAdapters$EnumTypeAdapter.class */
    private static final class EnumTypeAdapter<T extends Enum<T>> extends TypeAdapter<T> {
        private final Map<String, T> nameToConstant = new HashMap();
        private final Map<T, String> constantToName = new HashMap();

        public EnumTypeAdapter(Class<T> cls) {
            try {
                for (T t : cls.getEnumConstants()) {
                    String strName = t.name();
                    SerializedName serializedName = (SerializedName) cls.getField(strName).getAnnotation(SerializedName.class);
                    if (serializedName != null) {
                        String strValue = serializedName.value();
                        String[] strArrAlternate = serializedName.alternate();
                        int length = strArrAlternate.length;
                        int i = 0;
                        while (true) {
                            strName = strValue;
                            if (i < length) {
                                this.nameToConstant.put(strArrAlternate[i], t);
                                i++;
                            }
                        }
                    }
                    this.nameToConstant.put(strName, t);
                    this.constantToName.put(t, strName);
                }
            } catch (NoSuchFieldException e) {
                throw new AssertionError(e);
            }
        }

        @Override // com.google.gson.TypeAdapter
        /* renamed from: read */
        public T read2(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() != JsonToken.NULL) {
                return this.nameToConstant.get(jsonReader.nextString());
            }
            jsonReader.nextNull();
            return null;
        }

        @Override // com.google.gson.TypeAdapter
        public void write(JsonWriter jsonWriter, T t) throws IOException {
            jsonWriter.value(t == null ? null : this.constantToName.get(t));
        }
    }

    static {
        TypeAdapter<Class> typeAdapterNullSafe = new TypeAdapter<Class>() { // from class: com.google.gson.internal.bind.TypeAdapters.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.google.gson.TypeAdapter
            /* renamed from: read */
            public Class read2(JsonReader jsonReader) throws IOException {
                throw new UnsupportedOperationException("Attempted to deserialize a java.lang.Class. Forgot to register a type adapter?");
            }

            @Override // com.google.gson.TypeAdapter
            public void write(JsonWriter jsonWriter, Class cls) throws IOException {
                throw new UnsupportedOperationException("Attempted to serialize java.lang.Class: " + cls.getName() + ". Forgot to register a type adapter?");
            }
        }.nullSafe();
        CLASS = typeAdapterNullSafe;
        CLASS_FACTORY = newFactory(Class.class, typeAdapterNullSafe);
        TypeAdapter<BitSet> typeAdapterNullSafe2 = new TypeAdapter<BitSet>() { // from class: com.google.gson.internal.bind.TypeAdapters.2
            /* JADX WARN: Removed duplicated region for block: B:15:0x004c  */
            @Override // com.google.gson.TypeAdapter
            /* renamed from: read, reason: avoid collision after fix types in other method */
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public java.util.BitSet read2(com.google.gson.stream.JsonReader r5) throws java.io.IOException {
                /*
                    r4 = this;
                    java.util.BitSet r0 = new java.util.BitSet
                    r1 = r0
                    r1.<init>()
                    r10 = r0
                    r0 = r5
                    r0.beginArray()
                    r0 = r5
                    com.google.gson.stream.JsonToken r0 = r0.peek()
                    r9 = r0
                    r0 = 0
                    r6 = r0
                L15:
                    r0 = r9
                    com.google.gson.stream.JsonToken r1 = com.google.gson.stream.JsonToken.END_ARRAY
                    if (r0 == r1) goto Lbe
                    int[] r0 = com.google.gson.internal.bind.TypeAdapters.C050036.$SwitchMap$com$google$gson$stream$JsonToken
                    r1 = r9
                    int r1 = r1.ordinal()
                    r0 = r0[r1]
                    r7 = r0
                    r0 = 1
                    r8 = r0
                    r0 = r7
                    r1 = 1
                    if (r0 == r1) goto La0
                    r0 = r7
                    r1 = 2
                    if (r0 == r1) goto L97
                    r0 = r7
                    r1 = 3
                    if (r0 != r1) goto L75
                    r0 = r5
                    java.lang.String r0 = r0.nextString()
                    r9 = r0
                    r0 = r9
                    int r0 = java.lang.Integer.parseInt(r0)     // Catch: java.lang.NumberFormatException -> L52
                    r7 = r0
                    r0 = r7
                    if (r0 == 0) goto L4c
                    goto La7
                L4c:
                    r0 = 0
                    r8 = r0
                    goto La7
                L52:
                    r5 = move-exception
                    java.lang.StringBuilder r0 = new java.lang.StringBuilder
                    r1 = r0
                    r1.<init>()
                    r5 = r0
                    r0 = r5
                    java.lang.String r1 = "Error: Expecting: bitset number value (1, 0), Found: "
                    java.lang.StringBuilder r0 = r0.append(r1)
                    r0 = r5
                    r1 = r9
                    java.lang.StringBuilder r0 = r0.append(r1)
                    com.google.gson.JsonSyntaxException r0 = new com.google.gson.JsonSyntaxException
                    r1 = r0
                    r2 = r5
                    java.lang.String r2 = r2.toString()
                    r1.<init>(r2)
                    throw r0
                L75:
                    java.lang.StringBuilder r0 = new java.lang.StringBuilder
                    r1 = r0
                    r1.<init>()
                    r5 = r0
                    r0 = r5
                    java.lang.String r1 = "Invalid bitset value type: "
                    java.lang.StringBuilder r0 = r0.append(r1)
                    r0 = r5
                    r1 = r9
                    java.lang.StringBuilder r0 = r0.append(r1)
                    com.google.gson.JsonSyntaxException r0 = new com.google.gson.JsonSyntaxException
                    r1 = r0
                    r2 = r5
                    java.lang.String r2 = r2.toString()
                    r1.<init>(r2)
                    throw r0
                L97:
                    r0 = r5
                    boolean r0 = r0.nextBoolean()
                    r8 = r0
                    goto La7
                La0:
                    r0 = r5
                    int r0 = r0.nextInt()
                    if (r0 == 0) goto L4c
                La7:
                    r0 = r8
                    if (r0 == 0) goto Lb2
                    r0 = r10
                    r1 = r6
                    r0.set(r1)
                Lb2:
                    int r6 = r6 + 1
                    r0 = r5
                    com.google.gson.stream.JsonToken r0 = r0.peek()
                    r9 = r0
                    goto L15
                Lbe:
                    r0 = r5
                    r0.endArray()
                    r0 = r10
                    return r0
                */
                throw new UnsupportedOperationException("Method not decompiled: com.google.gson.internal.bind.TypeAdapters.C04822.read2(com.google.gson.stream.JsonReader):java.util.BitSet");
            }

            @Override // com.google.gson.TypeAdapter
            public void write(JsonWriter jsonWriter, BitSet bitSet) throws IOException {
                jsonWriter.beginArray();
                int length = bitSet.length();
                for (int i = 0; i < length; i++) {
                    jsonWriter.value(bitSet.get(i) ? 1L : 0L);
                }
                jsonWriter.endArray();
            }
        }.nullSafe();
        BIT_SET = typeAdapterNullSafe2;
        BIT_SET_FACTORY = newFactory(BitSet.class, typeAdapterNullSafe2);
        BOOLEAN = new TypeAdapter<Boolean>() { // from class: com.google.gson.internal.bind.TypeAdapters.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.google.gson.TypeAdapter
            /* renamed from: read */
            public Boolean read2(JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() != JsonToken.NULL) {
                    return jsonReader.peek() == JsonToken.STRING ? Boolean.valueOf(Boolean.parseBoolean(jsonReader.nextString())) : Boolean.valueOf(jsonReader.nextBoolean());
                }
                jsonReader.nextNull();
                return null;
            }

            @Override // com.google.gson.TypeAdapter
            public void write(JsonWriter jsonWriter, Boolean bool) throws IOException {
                jsonWriter.value(bool);
            }
        };
        BOOLEAN_AS_STRING = new TypeAdapter<Boolean>() { // from class: com.google.gson.internal.bind.TypeAdapters.4
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.google.gson.TypeAdapter
            /* renamed from: read */
            public Boolean read2(JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() != JsonToken.NULL) {
                    return Boolean.valueOf(jsonReader.nextString());
                }
                jsonReader.nextNull();
                return null;
            }

            @Override // com.google.gson.TypeAdapter
            public void write(JsonWriter jsonWriter, Boolean bool) throws IOException {
                jsonWriter.value(bool == null ? "null" : bool.toString());
            }
        };
        BOOLEAN_FACTORY = newFactory(Boolean.TYPE, Boolean.class, BOOLEAN);
        BYTE = new TypeAdapter<Number>() { // from class: com.google.gson.internal.bind.TypeAdapters.5
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.google.gson.TypeAdapter
            /* renamed from: read */
            public Number read2(JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                try {
                    return Byte.valueOf((byte) jsonReader.nextInt());
                } catch (NumberFormatException e) {
                    throw new JsonSyntaxException(e);
                }
            }

            @Override // com.google.gson.TypeAdapter
            public void write(JsonWriter jsonWriter, Number number) throws IOException {
                jsonWriter.value(number);
            }
        };
        BYTE_FACTORY = newFactory(Byte.TYPE, Byte.class, BYTE);
        SHORT = new TypeAdapter<Number>() { // from class: com.google.gson.internal.bind.TypeAdapters.6
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.google.gson.TypeAdapter
            /* renamed from: read */
            public Number read2(JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                try {
                    return Short.valueOf((short) jsonReader.nextInt());
                } catch (NumberFormatException e) {
                    throw new JsonSyntaxException(e);
                }
            }

            @Override // com.google.gson.TypeAdapter
            public void write(JsonWriter jsonWriter, Number number) throws IOException {
                jsonWriter.value(number);
            }
        };
        SHORT_FACTORY = newFactory(Short.TYPE, Short.class, SHORT);
        INTEGER = new TypeAdapter<Number>() { // from class: com.google.gson.internal.bind.TypeAdapters.7
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.google.gson.TypeAdapter
            /* renamed from: read */
            public Number read2(JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                try {
                    return Integer.valueOf(jsonReader.nextInt());
                } catch (NumberFormatException e) {
                    throw new JsonSyntaxException(e);
                }
            }

            @Override // com.google.gson.TypeAdapter
            public void write(JsonWriter jsonWriter, Number number) throws IOException {
                jsonWriter.value(number);
            }
        };
        INTEGER_FACTORY = newFactory(Integer.TYPE, Integer.class, INTEGER);
        TypeAdapter<AtomicInteger> typeAdapterNullSafe3 = new TypeAdapter<AtomicInteger>() { // from class: com.google.gson.internal.bind.TypeAdapters.8
            @Override // com.google.gson.TypeAdapter
            /* renamed from: read, reason: avoid collision after fix types in other method */
            public AtomicInteger read2(JsonReader jsonReader) throws IOException {
                try {
                    return new AtomicInteger(jsonReader.nextInt());
                } catch (NumberFormatException e) {
                    throw new JsonSyntaxException(e);
                }
            }

            @Override // com.google.gson.TypeAdapter
            public void write(JsonWriter jsonWriter, AtomicInteger atomicInteger) throws IOException {
                jsonWriter.value(atomicInteger.get());
            }
        }.nullSafe();
        ATOMIC_INTEGER = typeAdapterNullSafe3;
        ATOMIC_INTEGER_FACTORY = newFactory(AtomicInteger.class, typeAdapterNullSafe3);
        TypeAdapter<AtomicBoolean> typeAdapterNullSafe4 = new TypeAdapter<AtomicBoolean>() { // from class: com.google.gson.internal.bind.TypeAdapters.9
            @Override // com.google.gson.TypeAdapter
            /* renamed from: read, reason: avoid collision after fix types in other method */
            public AtomicBoolean read2(JsonReader jsonReader) throws IOException {
                return new AtomicBoolean(jsonReader.nextBoolean());
            }

            @Override // com.google.gson.TypeAdapter
            public void write(JsonWriter jsonWriter, AtomicBoolean atomicBoolean) throws IOException {
                jsonWriter.value(atomicBoolean.get());
            }
        }.nullSafe();
        ATOMIC_BOOLEAN = typeAdapterNullSafe4;
        ATOMIC_BOOLEAN_FACTORY = newFactory(AtomicBoolean.class, typeAdapterNullSafe4);
        TypeAdapter<AtomicIntegerArray> typeAdapterNullSafe5 = new TypeAdapter<AtomicIntegerArray>() { // from class: com.google.gson.internal.bind.TypeAdapters.10
            @Override // com.google.gson.TypeAdapter
            /* renamed from: read, reason: avoid collision after fix types in other method */
            public AtomicIntegerArray read2(JsonReader jsonReader) throws IOException {
                ArrayList arrayList = new ArrayList();
                jsonReader.beginArray();
                while (jsonReader.hasNext()) {
                    try {
                        arrayList.add(Integer.valueOf(jsonReader.nextInt()));
                    } catch (NumberFormatException e) {
                        throw new JsonSyntaxException(e);
                    }
                }
                jsonReader.endArray();
                int size = arrayList.size();
                AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(size);
                for (int i = 0; i < size; i++) {
                    atomicIntegerArray.set(i, ((Integer) arrayList.get(i)).intValue());
                }
                return atomicIntegerArray;
            }

            @Override // com.google.gson.TypeAdapter
            public void write(JsonWriter jsonWriter, AtomicIntegerArray atomicIntegerArray) throws IOException {
                jsonWriter.beginArray();
                int length = atomicIntegerArray.length();
                for (int i = 0; i < length; i++) {
                    jsonWriter.value(atomicIntegerArray.get(i));
                }
                jsonWriter.endArray();
            }
        }.nullSafe();
        ATOMIC_INTEGER_ARRAY = typeAdapterNullSafe5;
        ATOMIC_INTEGER_ARRAY_FACTORY = newFactory(AtomicIntegerArray.class, typeAdapterNullSafe5);
        LONG = new TypeAdapter<Number>() { // from class: com.google.gson.internal.bind.TypeAdapters.11
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.google.gson.TypeAdapter
            /* renamed from: read */
            public Number read2(JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                try {
                    return Long.valueOf(jsonReader.nextLong());
                } catch (NumberFormatException e) {
                    throw new JsonSyntaxException(e);
                }
            }

            @Override // com.google.gson.TypeAdapter
            public void write(JsonWriter jsonWriter, Number number) throws IOException {
                jsonWriter.value(number);
            }
        };
        FLOAT = new TypeAdapter<Number>() { // from class: com.google.gson.internal.bind.TypeAdapters.12
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.google.gson.TypeAdapter
            /* renamed from: read */
            public Number read2(JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() != JsonToken.NULL) {
                    return Float.valueOf((float) jsonReader.nextDouble());
                }
                jsonReader.nextNull();
                return null;
            }

            @Override // com.google.gson.TypeAdapter
            public void write(JsonWriter jsonWriter, Number number) throws IOException {
                jsonWriter.value(number);
            }
        };
        DOUBLE = new TypeAdapter<Number>() { // from class: com.google.gson.internal.bind.TypeAdapters.13
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.google.gson.TypeAdapter
            /* renamed from: read */
            public Number read2(JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() != JsonToken.NULL) {
                    return Double.valueOf(jsonReader.nextDouble());
                }
                jsonReader.nextNull();
                return null;
            }

            @Override // com.google.gson.TypeAdapter
            public void write(JsonWriter jsonWriter, Number number) throws IOException {
                jsonWriter.value(number);
            }
        };
        TypeAdapter<Number> typeAdapter = new TypeAdapter<Number>() { // from class: com.google.gson.internal.bind.TypeAdapters.14
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.google.gson.TypeAdapter
            /* renamed from: read */
            public Number read2(JsonReader jsonReader) throws IOException {
                JsonToken jsonTokenPeek = jsonReader.peek();
                int i = C050036.$SwitchMap$com$google$gson$stream$JsonToken[jsonTokenPeek.ordinal()];
                if (i == 1 || i == 3) {
                    return new LazilyParsedNumber(jsonReader.nextString());
                }
                if (i == 4) {
                    jsonReader.nextNull();
                    return null;
                }
                throw new JsonSyntaxException("Expecting number, got: " + jsonTokenPeek);
            }

            @Override // com.google.gson.TypeAdapter
            public void write(JsonWriter jsonWriter, Number number) throws IOException {
                jsonWriter.value(number);
            }
        };
        NUMBER = typeAdapter;
        NUMBER_FACTORY = newFactory(Number.class, typeAdapter);
        CHARACTER = new TypeAdapter<Character>() { // from class: com.google.gson.internal.bind.TypeAdapters.15
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.google.gson.TypeAdapter
            /* renamed from: read */
            public Character read2(JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                String strNextString = jsonReader.nextString();
                if (strNextString.length() == 1) {
                    return Character.valueOf(strNextString.charAt(0));
                }
                throw new JsonSyntaxException("Expecting character, got: " + strNextString);
            }

            @Override // com.google.gson.TypeAdapter
            public void write(JsonWriter jsonWriter, Character ch) throws IOException {
                jsonWriter.value(ch == null ? null : String.valueOf(ch));
            }
        };
        CHARACTER_FACTORY = newFactory(Character.TYPE, Character.class, CHARACTER);
        STRING = new TypeAdapter<String>() { // from class: com.google.gson.internal.bind.TypeAdapters.16
            @Override // com.google.gson.TypeAdapter
            /* renamed from: read, reason: avoid collision after fix types in other method */
            public String read2(JsonReader jsonReader) throws IOException {
                JsonToken jsonTokenPeek = jsonReader.peek();
                if (jsonTokenPeek != JsonToken.NULL) {
                    return jsonTokenPeek == JsonToken.BOOLEAN ? Boolean.toString(jsonReader.nextBoolean()) : jsonReader.nextString();
                }
                jsonReader.nextNull();
                return null;
            }

            @Override // com.google.gson.TypeAdapter
            public void write(JsonWriter jsonWriter, String str) throws IOException {
                jsonWriter.value(str);
            }
        };
        BIG_DECIMAL = new TypeAdapter<BigDecimal>() { // from class: com.google.gson.internal.bind.TypeAdapters.17
            @Override // com.google.gson.TypeAdapter
            /* renamed from: read, reason: avoid collision after fix types in other method */
            public BigDecimal read2(JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                try {
                    return new BigDecimal(jsonReader.nextString());
                } catch (NumberFormatException e) {
                    throw new JsonSyntaxException(e);
                }
            }

            @Override // com.google.gson.TypeAdapter
            public void write(JsonWriter jsonWriter, BigDecimal bigDecimal) throws IOException {
                jsonWriter.value(bigDecimal);
            }
        };
        BIG_INTEGER = new TypeAdapter<BigInteger>() { // from class: com.google.gson.internal.bind.TypeAdapters.18
            @Override // com.google.gson.TypeAdapter
            /* renamed from: read, reason: avoid collision after fix types in other method */
            public BigInteger read2(JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                try {
                    return new BigInteger(jsonReader.nextString());
                } catch (NumberFormatException e) {
                    throw new JsonSyntaxException(e);
                }
            }

            @Override // com.google.gson.TypeAdapter
            public void write(JsonWriter jsonWriter, BigInteger bigInteger) throws IOException {
                jsonWriter.value(bigInteger);
            }
        };
        STRING_FACTORY = newFactory(String.class, STRING);
        TypeAdapter<StringBuilder> typeAdapter2 = new TypeAdapter<StringBuilder>() { // from class: com.google.gson.internal.bind.TypeAdapters.19
            @Override // com.google.gson.TypeAdapter
            /* renamed from: read, reason: avoid collision after fix types in other method */
            public StringBuilder read2(JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() != JsonToken.NULL) {
                    return new StringBuilder(jsonReader.nextString());
                }
                jsonReader.nextNull();
                return null;
            }

            @Override // com.google.gson.TypeAdapter
            public void write(JsonWriter jsonWriter, StringBuilder sb) throws IOException {
                jsonWriter.value(sb == null ? null : sb.toString());
            }
        };
        STRING_BUILDER = typeAdapter2;
        STRING_BUILDER_FACTORY = newFactory(StringBuilder.class, typeAdapter2);
        TypeAdapter<StringBuffer> typeAdapter3 = new TypeAdapter<StringBuffer>() { // from class: com.google.gson.internal.bind.TypeAdapters.20
            @Override // com.google.gson.TypeAdapter
            /* renamed from: read, reason: avoid collision after fix types in other method */
            public StringBuffer read2(JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() != JsonToken.NULL) {
                    return new StringBuffer(jsonReader.nextString());
                }
                jsonReader.nextNull();
                return null;
            }

            @Override // com.google.gson.TypeAdapter
            public void write(JsonWriter jsonWriter, StringBuffer stringBuffer) throws IOException {
                jsonWriter.value(stringBuffer == null ? null : stringBuffer.toString());
            }
        };
        STRING_BUFFER = typeAdapter3;
        STRING_BUFFER_FACTORY = newFactory(StringBuffer.class, typeAdapter3);
        TypeAdapter<URL> typeAdapter4 = new TypeAdapter<URL>() { // from class: com.google.gson.internal.bind.TypeAdapters.21
            @Override // com.google.gson.TypeAdapter
            /* renamed from: read, reason: avoid collision after fix types in other method */
            public URL read2(JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                String strNextString = jsonReader.nextString();
                return "null".equals(strNextString) ? null : new URL(strNextString);
            }

            @Override // com.google.gson.TypeAdapter
            public void write(JsonWriter jsonWriter, URL url) throws IOException {
                jsonWriter.value(url == null ? null : url.toExternalForm());
            }
        };
        URL = typeAdapter4;
        URL_FACTORY = newFactory(URL.class, typeAdapter4);
        TypeAdapter<URI> typeAdapter5 = new TypeAdapter<URI>() { // from class: com.google.gson.internal.bind.TypeAdapters.22
            @Override // com.google.gson.TypeAdapter
            /* renamed from: read, reason: avoid collision after fix types in other method */
            public URI read2(JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                try {
                    String strNextString = jsonReader.nextString();
                    return "null".equals(strNextString) ? null : new URI(strNextString);
                } catch (URISyntaxException e) {
                    throw new JsonIOException(e);
                }
            }

            @Override // com.google.gson.TypeAdapter
            public void write(JsonWriter jsonWriter, URI uri) throws IOException {
                jsonWriter.value(uri == null ? null : uri.toASCIIString());
            }
        };
        URI = typeAdapter5;
        URI_FACTORY = newFactory(URI.class, typeAdapter5);
        TypeAdapter<InetAddress> typeAdapter6 = new TypeAdapter<InetAddress>() { // from class: com.google.gson.internal.bind.TypeAdapters.23
            @Override // com.google.gson.TypeAdapter
            /* renamed from: read, reason: avoid collision after fix types in other method */
            public InetAddress read2(JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() != JsonToken.NULL) {
                    return InetAddress.getByName(jsonReader.nextString());
                }
                jsonReader.nextNull();
                return null;
            }

            @Override // com.google.gson.TypeAdapter
            public void write(JsonWriter jsonWriter, InetAddress inetAddress) throws IOException {
                jsonWriter.value(inetAddress == null ? null : inetAddress.getHostAddress());
            }
        };
        INET_ADDRESS = typeAdapter6;
        INET_ADDRESS_FACTORY = newTypeHierarchyFactory(InetAddress.class, typeAdapter6);
        TypeAdapter<UUID> typeAdapter7 = new TypeAdapter<UUID>() { // from class: com.google.gson.internal.bind.TypeAdapters.24
            @Override // com.google.gson.TypeAdapter
            /* renamed from: read, reason: avoid collision after fix types in other method */
            public UUID read2(JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() != JsonToken.NULL) {
                    return UUID.fromString(jsonReader.nextString());
                }
                jsonReader.nextNull();
                return null;
            }

            @Override // com.google.gson.TypeAdapter
            public void write(JsonWriter jsonWriter, UUID uuid) throws IOException {
                jsonWriter.value(uuid == null ? null : uuid.toString());
            }
        };
        UUID = typeAdapter7;
        UUID_FACTORY = newFactory(UUID.class, typeAdapter7);
        TypeAdapter<Currency> typeAdapterNullSafe6 = new TypeAdapter<Currency>() { // from class: com.google.gson.internal.bind.TypeAdapters.25
            @Override // com.google.gson.TypeAdapter
            /* renamed from: read, reason: avoid collision after fix types in other method */
            public Currency read2(JsonReader jsonReader) throws IOException {
                return Currency.getInstance(jsonReader.nextString());
            }

            @Override // com.google.gson.TypeAdapter
            public void write(JsonWriter jsonWriter, Currency currency) throws IOException {
                jsonWriter.value(currency.getCurrencyCode());
            }
        }.nullSafe();
        CURRENCY = typeAdapterNullSafe6;
        CURRENCY_FACTORY = newFactory(Currency.class, typeAdapterNullSafe6);
        TIMESTAMP_FACTORY = new TypeAdapterFactory() { // from class: com.google.gson.internal.bind.TypeAdapters.26
            @Override // com.google.gson.TypeAdapterFactory
            public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
                if (typeToken.getRawType() != Timestamp.class) {
                    return null;
                }
                return (TypeAdapter<T>) new TypeAdapter<Timestamp>(this, gson.getAdapter(Date.class)) { // from class: com.google.gson.internal.bind.TypeAdapters.26.1
                    final C048926 this$0;
                    final TypeAdapter val$dateTypeAdapter;

                    {
                        this.this$0 = this;
                        this.val$dateTypeAdapter = typeAdapter;
                    }

                    @Override // com.google.gson.TypeAdapter
                    /* renamed from: read, reason: avoid collision after fix types in other method */
                    public Timestamp read2(JsonReader jsonReader) throws IOException {
                        Date date = (Date) this.val$dateTypeAdapter.read2(jsonReader);
                        return date != null ? new Timestamp(date.getTime()) : null;
                    }

                    @Override // com.google.gson.TypeAdapter
                    public void write(JsonWriter jsonWriter, Timestamp timestamp) throws IOException {
                        this.val$dateTypeAdapter.write(jsonWriter, timestamp);
                    }
                };
            }
        };
        TypeAdapter<Calendar> typeAdapter8 = new TypeAdapter<Calendar>() { // from class: com.google.gson.internal.bind.TypeAdapters.27
            private static final String DAY_OF_MONTH = "dayOfMonth";
            private static final String HOUR_OF_DAY = "hourOfDay";
            private static final String MINUTE = "minute";
            private static final String MONTH = "month";
            private static final String SECOND = "second";
            private static final String YEAR = "year";

            @Override // com.google.gson.TypeAdapter
            /* renamed from: read, reason: avoid collision after fix types in other method */
            public Calendar read2(JsonReader jsonReader) throws IOException, NumberFormatException {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                jsonReader.beginObject();
                int i = 0;
                int i2 = 0;
                int i3 = 0;
                int i4 = 0;
                int i5 = 0;
                int i6 = 0;
                while (jsonReader.peek() != JsonToken.END_OBJECT) {
                    String strNextName = jsonReader.nextName();
                    int iNextInt = jsonReader.nextInt();
                    if (YEAR.equals(strNextName)) {
                        i = iNextInt;
                    } else if (MONTH.equals(strNextName)) {
                        i2 = iNextInt;
                    } else if (DAY_OF_MONTH.equals(strNextName)) {
                        i3 = iNextInt;
                    } else if (HOUR_OF_DAY.equals(strNextName)) {
                        i4 = iNextInt;
                    } else if (MINUTE.equals(strNextName)) {
                        i5 = iNextInt;
                    } else if (SECOND.equals(strNextName)) {
                        i6 = iNextInt;
                    }
                }
                jsonReader.endObject();
                return new GregorianCalendar(i, i2, i3, i4, i5, i6);
            }

            @Override // com.google.gson.TypeAdapter
            public void write(JsonWriter jsonWriter, Calendar calendar) throws IOException {
                if (calendar == null) {
                    jsonWriter.nullValue();
                    return;
                }
                jsonWriter.beginObject();
                jsonWriter.name(YEAR);
                jsonWriter.value(calendar.get(1));
                jsonWriter.name(MONTH);
                jsonWriter.value(calendar.get(2));
                jsonWriter.name(DAY_OF_MONTH);
                jsonWriter.value(calendar.get(5));
                jsonWriter.name(HOUR_OF_DAY);
                jsonWriter.value(calendar.get(11));
                jsonWriter.name(MINUTE);
                jsonWriter.value(calendar.get(12));
                jsonWriter.name(SECOND);
                jsonWriter.value(calendar.get(13));
                jsonWriter.endObject();
            }
        };
        CALENDAR = typeAdapter8;
        CALENDAR_FACTORY = newFactoryForMultipleTypes(Calendar.class, GregorianCalendar.class, typeAdapter8);
        TypeAdapter<Locale> typeAdapter9 = new TypeAdapter<Locale>() { // from class: com.google.gson.internal.bind.TypeAdapters.28
            @Override // com.google.gson.TypeAdapter
            /* renamed from: read, reason: avoid collision after fix types in other method */
            public Locale read2(JsonReader jsonReader) throws IOException {
                String strNextToken = null;
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                StringTokenizer stringTokenizer = new StringTokenizer(jsonReader.nextString(), "_");
                String strNextToken2 = stringTokenizer.hasMoreElements() ? stringTokenizer.nextToken() : null;
                String strNextToken3 = stringTokenizer.hasMoreElements() ? stringTokenizer.nextToken() : null;
                if (stringTokenizer.hasMoreElements()) {
                    strNextToken = stringTokenizer.nextToken();
                }
                return (strNextToken3 == null && strNextToken == null) ? new Locale(strNextToken2) : strNextToken == null ? new Locale(strNextToken2, strNextToken3) : new Locale(strNextToken2, strNextToken3, strNextToken);
            }

            @Override // com.google.gson.TypeAdapter
            public void write(JsonWriter jsonWriter, Locale locale) throws IOException {
                jsonWriter.value(locale == null ? null : locale.toString());
            }
        };
        LOCALE = typeAdapter9;
        LOCALE_FACTORY = newFactory(Locale.class, typeAdapter9);
        TypeAdapter<JsonElement> typeAdapter10 = new TypeAdapter<JsonElement>() { // from class: com.google.gson.internal.bind.TypeAdapters.29
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.google.gson.TypeAdapter
            /* renamed from: read */
            public JsonElement read2(JsonReader jsonReader) throws IOException {
                switch (C050036.$SwitchMap$com$google$gson$stream$JsonToken[jsonReader.peek().ordinal()]) {
                    case 1:
                        return new JsonPrimitive((Number) new LazilyParsedNumber(jsonReader.nextString()));
                    case 2:
                        return new JsonPrimitive(Boolean.valueOf(jsonReader.nextBoolean()));
                    case 3:
                        return new JsonPrimitive(jsonReader.nextString());
                    case 4:
                        jsonReader.nextNull();
                        return JsonNull.INSTANCE;
                    case 5:
                        JsonArray jsonArray = new JsonArray();
                        jsonReader.beginArray();
                        while (jsonReader.hasNext()) {
                            jsonArray.add(read2(jsonReader));
                        }
                        jsonReader.endArray();
                        return jsonArray;
                    case 6:
                        JsonObject jsonObject = new JsonObject();
                        jsonReader.beginObject();
                        while (jsonReader.hasNext()) {
                            jsonObject.add(jsonReader.nextName(), read2(jsonReader));
                        }
                        jsonReader.endObject();
                        return jsonObject;
                    default:
                        throw new IllegalArgumentException();
                }
            }

            @Override // com.google.gson.TypeAdapter
            public void write(JsonWriter jsonWriter, JsonElement jsonElement) throws IOException {
                if (jsonElement == null || jsonElement.isJsonNull()) {
                    jsonWriter.nullValue();
                    return;
                }
                if (jsonElement.isJsonPrimitive()) {
                    JsonPrimitive asJsonPrimitive = jsonElement.getAsJsonPrimitive();
                    if (asJsonPrimitive.isNumber()) {
                        jsonWriter.value(asJsonPrimitive.getAsNumber());
                        return;
                    } else if (asJsonPrimitive.isBoolean()) {
                        jsonWriter.value(asJsonPrimitive.getAsBoolean());
                        return;
                    } else {
                        jsonWriter.value(asJsonPrimitive.getAsString());
                        return;
                    }
                }
                if (jsonElement.isJsonArray()) {
                    jsonWriter.beginArray();
                    Iterator<JsonElement> it = jsonElement.getAsJsonArray().iterator();
                    while (it.hasNext()) {
                        write(jsonWriter, it.next());
                    }
                    jsonWriter.endArray();
                    return;
                }
                if (!jsonElement.isJsonObject()) {
                    throw new IllegalArgumentException("Couldn't write " + jsonElement.getClass());
                }
                jsonWriter.beginObject();
                for (Map.Entry<String, JsonElement> entry : jsonElement.getAsJsonObject().entrySet()) {
                    jsonWriter.name(entry.getKey());
                    write(jsonWriter, entry.getValue());
                }
                jsonWriter.endObject();
            }
        };
        JSON_ELEMENT = typeAdapter10;
        JSON_ELEMENT_FACTORY = newTypeHierarchyFactory(JsonElement.class, typeAdapter10);
        ENUM_FACTORY = new TypeAdapterFactory() { // from class: com.google.gson.internal.bind.TypeAdapters.30
            @Override // com.google.gson.TypeAdapterFactory
            public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
                Class<? super T> rawType = typeToken.getRawType();
                if (!Enum.class.isAssignableFrom(rawType) || rawType == Enum.class) {
                    return null;
                }
                Class<? super T> superclass = rawType;
                if (!rawType.isEnum()) {
                    superclass = rawType.getSuperclass();
                }
                return new EnumTypeAdapter(superclass);
            }
        };
    }

    private TypeAdapters() {
        throw new UnsupportedOperationException();
    }

    public static <TT> TypeAdapterFactory newFactory(TypeToken<TT> typeToken, TypeAdapter<TT> typeAdapter) {
        return new TypeAdapterFactory(typeToken, typeAdapter) { // from class: com.google.gson.internal.bind.TypeAdapters.31
            final TypeToken val$type;
            final TypeAdapter val$typeAdapter;

            {
                this.val$type = typeToken;
                this.val$typeAdapter = typeAdapter;
            }

            @Override // com.google.gson.TypeAdapterFactory
            public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken2) {
                return typeToken2.equals(this.val$type) ? this.val$typeAdapter : null;
            }
        };
    }

    public static <TT> TypeAdapterFactory newFactory(Class<TT> cls, TypeAdapter<TT> typeAdapter) {
        return new TypeAdapterFactory(cls, typeAdapter) { // from class: com.google.gson.internal.bind.TypeAdapters.32
            final Class val$type;
            final TypeAdapter val$typeAdapter;

            {
                this.val$type = cls;
                this.val$typeAdapter = typeAdapter;
            }

            @Override // com.google.gson.TypeAdapterFactory
            public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
                return typeToken.getRawType() == this.val$type ? this.val$typeAdapter : null;
            }

            public String toString() {
                return "Factory[type=" + this.val$type.getName() + ",adapter=" + this.val$typeAdapter + "]";
            }
        };
    }

    public static <TT> TypeAdapterFactory newFactory(Class<TT> cls, Class<TT> cls2, TypeAdapter<? super TT> typeAdapter) {
        return new TypeAdapterFactory(cls, cls2, typeAdapter) { // from class: com.google.gson.internal.bind.TypeAdapters.33
            final Class val$boxed;
            final TypeAdapter val$typeAdapter;
            final Class val$unboxed;

            {
                this.val$unboxed = cls;
                this.val$boxed = cls2;
                this.val$typeAdapter = typeAdapter;
            }

            @Override // com.google.gson.TypeAdapterFactory
            public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
                Class<? super T> rawType = typeToken.getRawType();
                return (rawType == this.val$unboxed || rawType == this.val$boxed) ? this.val$typeAdapter : null;
            }

            public String toString() {
                return "Factory[type=" + this.val$boxed.getName() + "+" + this.val$unboxed.getName() + ",adapter=" + this.val$typeAdapter + "]";
            }
        };
    }

    public static <TT> TypeAdapterFactory newFactoryForMultipleTypes(Class<TT> cls, Class<? extends TT> cls2, TypeAdapter<? super TT> typeAdapter) {
        return new TypeAdapterFactory(cls, cls2, typeAdapter) { // from class: com.google.gson.internal.bind.TypeAdapters.34
            final Class val$base;
            final Class val$sub;
            final TypeAdapter val$typeAdapter;

            {
                this.val$base = cls;
                this.val$sub = cls2;
                this.val$typeAdapter = typeAdapter;
            }

            @Override // com.google.gson.TypeAdapterFactory
            public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
                Class<? super T> rawType = typeToken.getRawType();
                return (rawType == this.val$base || rawType == this.val$sub) ? this.val$typeAdapter : null;
            }

            public String toString() {
                return "Factory[type=" + this.val$base.getName() + "+" + this.val$sub.getName() + ",adapter=" + this.val$typeAdapter + "]";
            }
        };
    }

    public static <T1> TypeAdapterFactory newTypeHierarchyFactory(Class<T1> cls, TypeAdapter<T1> typeAdapter) {
        return new C049935(cls, typeAdapter);
    }
}
