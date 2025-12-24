package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Type;
import java.util.Set;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/parser/deserializer/AutowiredObjectDeserializer.class */
public interface AutowiredObjectDeserializer extends ObjectDeserializer {
    Set<Type> getAutowiredFor();
}
