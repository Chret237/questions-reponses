package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.parser.ParserConfig;
import java.lang.reflect.Constructor;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/parser/deserializer/ThrowableDeserializer.class */
public class ThrowableDeserializer extends JavaBeanDeserializer {
    public ThrowableDeserializer(ParserConfig parserConfig, Class<?> cls) {
        super(parserConfig, cls, cls);
    }

    private Throwable createException(String str, Throwable th, Class<?> cls) throws Exception {
        Constructor<?> constructor;
        Constructor<?> constructor2;
        Constructor<?> constructor3;
        Constructor<?>[] constructors = cls.getConstructors();
        int length = constructors.length;
        Constructor<?> constructor4 = null;
        Constructor<?> constructor5 = null;
        Constructor<?> constructor6 = null;
        int i = 0;
        while (i < length) {
            Constructor<?> constructor7 = constructors[i];
            Class<?>[] parameterTypes = constructor7.getParameterTypes();
            if (parameterTypes.length == 0) {
                constructor = constructor4;
                constructor2 = constructor5;
                constructor3 = constructor7;
            } else if (parameterTypes.length == 1 && parameterTypes[0] == String.class) {
                constructor = constructor4;
                constructor2 = constructor7;
                constructor3 = constructor6;
            } else {
                constructor = constructor4;
                constructor2 = constructor5;
                constructor3 = constructor6;
                if (parameterTypes.length == 2) {
                    constructor = constructor4;
                    constructor2 = constructor5;
                    constructor3 = constructor6;
                    if (parameterTypes[0] == String.class) {
                        constructor = constructor4;
                        constructor2 = constructor5;
                        constructor3 = constructor6;
                        if (parameterTypes[1] == Throwable.class) {
                            constructor3 = constructor6;
                            constructor2 = constructor5;
                            constructor = constructor7;
                        }
                    }
                }
            }
            i++;
            constructor4 = constructor;
            constructor5 = constructor2;
            constructor6 = constructor3;
        }
        if (constructor4 != null) {
            return (Throwable) constructor4.newInstance(str, th);
        }
        if (constructor5 != null) {
            return (Throwable) constructor5.newInstance(str);
        }
        if (constructor6 != null) {
            return (Throwable) constructor6.newInstance(new Object[0]);
        }
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:18:0x005a  */
    /* JADX WARN: Type inference failed for: r0v67, types: [com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer] */
    @Override // com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer, com.alibaba.fastjson.parser.deserializer.ObjectDeserializer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public <T> T deserialze(com.alibaba.fastjson.parser.DefaultJSONParser r6, java.lang.reflect.Type r7, java.lang.Object r8) {
        /*
            Method dump skipped, instructions count: 804
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.deserializer.ThrowableDeserializer.deserialze(com.alibaba.fastjson.parser.DefaultJSONParser, java.lang.reflect.Type, java.lang.Object):java.lang.Object");
    }

    @Override // com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer, com.alibaba.fastjson.parser.deserializer.ObjectDeserializer
    public int getFastMatchToken() {
        return 12;
    }
}
