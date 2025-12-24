package com.google.gson;

import com.google.gson.internal.C$Gson$Preconditions;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;

/* loaded from: classes-dex2jar.jar:com/google/gson/JsonPrimitive.class */
public final class JsonPrimitive extends JsonElement {
    private static final Class<?>[] PRIMITIVE_TYPES = {Integer.TYPE, Long.TYPE, Short.TYPE, Float.TYPE, Double.TYPE, Byte.TYPE, Boolean.TYPE, Character.TYPE, Integer.class, Long.class, Short.class, Float.class, Double.class, Byte.class, Boolean.class, Character.class};
    private Object value;

    public JsonPrimitive(Boolean bool) {
        setValue(bool);
    }

    public JsonPrimitive(Character ch) {
        setValue(ch);
    }

    public JsonPrimitive(Number number) {
        setValue(number);
    }

    JsonPrimitive(Object obj) {
        setValue(obj);
    }

    public JsonPrimitive(String str) {
        setValue(str);
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x003c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static boolean isIntegral(com.google.gson.JsonPrimitive r2) {
        /*
            r0 = r2
            java.lang.Object r0 = r0.value
            r2 = r0
            r0 = r2
            boolean r0 = r0 instanceof java.lang.Number
            r5 = r0
            r0 = 0
            r4 = r0
            r0 = r4
            r3 = r0
            r0 = r5
            if (r0 == 0) goto L3e
            r0 = r2
            java.lang.Number r0 = (java.lang.Number) r0
            r2 = r0
            r0 = r2
            boolean r0 = r0 instanceof java.math.BigInteger
            if (r0 != 0) goto L3c
            r0 = r2
            boolean r0 = r0 instanceof java.lang.Long
            if (r0 != 0) goto L3c
            r0 = r2
            boolean r0 = r0 instanceof java.lang.Integer
            if (r0 != 0) goto L3c
            r0 = r2
            boolean r0 = r0 instanceof java.lang.Short
            if (r0 != 0) goto L3c
            r0 = r4
            r3 = r0
            r0 = r2
            boolean r0 = r0 instanceof java.lang.Byte
            if (r0 == 0) goto L3e
        L3c:
            r0 = 1
            r3 = r0
        L3e:
            r0 = r3
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.gson.JsonPrimitive.isIntegral(com.google.gson.JsonPrimitive):boolean");
    }

    private static boolean isPrimitiveOrString(Object obj) {
        if (obj instanceof String) {
            return true;
        }
        Class<?> cls = obj.getClass();
        for (Class<?> cls2 : PRIMITIVE_TYPES) {
            if (cls2.isAssignableFrom(cls)) {
                return true;
            }
        }
        return false;
    }

    @Override // com.google.gson.JsonElement
    public JsonPrimitive deepCopy() {
        return this;
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        JsonPrimitive jsonPrimitive = (JsonPrimitive) obj;
        if (this.value == null) {
            return jsonPrimitive.value == null;
        }
        if (isIntegral(this) && isIntegral(jsonPrimitive)) {
            if (getAsNumber().longValue() != jsonPrimitive.getAsNumber().longValue()) {
                z = false;
            }
            return z;
        }
        if (!(this.value instanceof Number) || !(jsonPrimitive.value instanceof Number)) {
            return this.value.equals(jsonPrimitive.value);
        }
        double dDoubleValue = getAsNumber().doubleValue();
        double dDoubleValue2 = jsonPrimitive.getAsNumber().doubleValue();
        boolean z2 = true;
        if (dDoubleValue != dDoubleValue2) {
            z2 = Double.isNaN(dDoubleValue) && Double.isNaN(dDoubleValue2);
        }
        return z2;
    }

    @Override // com.google.gson.JsonElement
    public BigDecimal getAsBigDecimal() {
        Object obj = this.value;
        return obj instanceof BigDecimal ? (BigDecimal) obj : new BigDecimal(this.value.toString());
    }

    @Override // com.google.gson.JsonElement
    public BigInteger getAsBigInteger() {
        Object obj = this.value;
        return obj instanceof BigInteger ? (BigInteger) obj : new BigInteger(this.value.toString());
    }

    @Override // com.google.gson.JsonElement
    public boolean getAsBoolean() {
        return isBoolean() ? getAsBooleanWrapper().booleanValue() : Boolean.parseBoolean(getAsString());
    }

    @Override // com.google.gson.JsonElement
    Boolean getAsBooleanWrapper() {
        return (Boolean) this.value;
    }

    @Override // com.google.gson.JsonElement
    public byte getAsByte() {
        return isNumber() ? getAsNumber().byteValue() : Byte.parseByte(getAsString());
    }

    @Override // com.google.gson.JsonElement
    public char getAsCharacter() {
        return getAsString().charAt(0);
    }

    @Override // com.google.gson.JsonElement
    public double getAsDouble() {
        return isNumber() ? getAsNumber().doubleValue() : Double.parseDouble(getAsString());
    }

    @Override // com.google.gson.JsonElement
    public float getAsFloat() {
        return isNumber() ? getAsNumber().floatValue() : Float.parseFloat(getAsString());
    }

    @Override // com.google.gson.JsonElement
    public int getAsInt() {
        return isNumber() ? getAsNumber().intValue() : Integer.parseInt(getAsString());
    }

    @Override // com.google.gson.JsonElement
    public long getAsLong() {
        return isNumber() ? getAsNumber().longValue() : Long.parseLong(getAsString());
    }

    @Override // com.google.gson.JsonElement
    public Number getAsNumber() {
        Object obj = this.value;
        return obj instanceof String ? new LazilyParsedNumber((String) this.value) : (Number) obj;
    }

    @Override // com.google.gson.JsonElement
    public short getAsShort() {
        return isNumber() ? getAsNumber().shortValue() : Short.parseShort(getAsString());
    }

    @Override // com.google.gson.JsonElement
    public String getAsString() {
        return isNumber() ? getAsNumber().toString() : isBoolean() ? getAsBooleanWrapper().toString() : (String) this.value;
    }

    public int hashCode() {
        long jDoubleToLongBits;
        if (this.value == null) {
            return 31;
        }
        if (isIntegral(this)) {
            jDoubleToLongBits = getAsNumber().longValue();
        } else {
            Object obj = this.value;
            if (!(obj instanceof Number)) {
                return obj.hashCode();
            }
            jDoubleToLongBits = Double.doubleToLongBits(getAsNumber().doubleValue());
        }
        return (int) ((jDoubleToLongBits >>> 32) ^ jDoubleToLongBits);
    }

    public boolean isBoolean() {
        return this.value instanceof Boolean;
    }

    public boolean isNumber() {
        return this.value instanceof Number;
    }

    public boolean isString() {
        return this.value instanceof String;
    }

    void setValue(Object obj) {
        if (obj instanceof Character) {
            this.value = String.valueOf(((Character) obj).charValue());
        } else {
            C$Gson$Preconditions.checkArgument((obj instanceof Number) || isPrimitiveOrString(obj));
            this.value = obj;
        }
    }
}
