package com.alibaba.fastjson;

import com.alibaba.fastjson.serializer.JSONSerializable;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/JSONPObject.class */
public class JSONPObject implements JSONSerializable {
    public static String SECURITY_PREFIX = "/**/";
    private String function;
    private final List<Object> parameters = new ArrayList();

    public JSONPObject() {
    }

    public JSONPObject(String str) {
        this.function = str;
    }

    public void addParameter(Object obj) {
        this.parameters.add(obj);
    }

    public String getFunction() {
        return this.function;
    }

    public List<Object> getParameters() {
        return this.parameters;
    }

    public void setFunction(String str) {
        this.function = str;
    }

    public String toJSONString() {
        return toString();
    }

    public String toString() {
        return JSON.toJSONString(this);
    }

    @Override // com.alibaba.fastjson.serializer.JSONSerializable
    public void write(JSONSerializer jSONSerializer, Object obj, Type type, int i) throws IOException {
        SerializeWriter serializeWriter = jSONSerializer.out;
        if ((SerializerFeature.BrowserSecure.mask & i) != 0 || serializeWriter.isEnabled(SerializerFeature.BrowserSecure.mask)) {
            serializeWriter.write(SECURITY_PREFIX);
        }
        serializeWriter.write(this.function);
        serializeWriter.write(40);
        for (int i2 = 0; i2 < this.parameters.size(); i2++) {
            if (i2 != 0) {
                serializeWriter.write(44);
            }
            jSONSerializer.write(this.parameters.get(i2));
        }
        serializeWriter.write(41);
    }
}
