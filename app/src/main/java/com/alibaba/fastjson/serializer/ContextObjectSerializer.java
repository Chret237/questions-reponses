package com.alibaba.fastjson.serializer;

import java.io.IOException;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/serializer/ContextObjectSerializer.class */
public interface ContextObjectSerializer extends ObjectSerializer {
    void write(JSONSerializer jSONSerializer, Object obj, BeanContext beanContext) throws IOException;
}
