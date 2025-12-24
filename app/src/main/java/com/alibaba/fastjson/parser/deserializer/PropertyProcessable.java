package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Type;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/parser/deserializer/PropertyProcessable.class */
public interface PropertyProcessable extends ParseProcess {
    void apply(String str, Object obj);

    Type getType(String str);
}
