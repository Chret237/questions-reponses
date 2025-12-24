package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.Type;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/parser/deserializer/FieldTypeResolver.class */
public interface FieldTypeResolver extends ParseProcess {
    Type resolve(Object obj, String str);
}
