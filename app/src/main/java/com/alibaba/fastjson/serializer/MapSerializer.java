package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Type;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/serializer/MapSerializer.class */
public class MapSerializer extends SerializeFilterable implements ObjectSerializer {
    public static MapSerializer instance = new MapSerializer();
    private static final int NON_STRINGKEY_AS_STRING = SerializerFeature.m12of(new SerializerFeature[]{SerializerFeature.BrowserCompatible, SerializerFeature.WriteNonStringKeyAsString, SerializerFeature.BrowserSecure});

    @Override // com.alibaba.fastjson.serializer.ObjectSerializer
    public void write(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type, int i) throws IOException {
        write(jSONSerializer, obj, obj2, type, i, false);
    }

    /* JADX WARN: Removed duplicated region for block: B:112:0x0256 A[Catch: all -> 0x0528, TRY_ENTER, TRY_LEAVE, TryCatch #0 {all -> 0x0528, blocks: (B:27:0x009b, B:29:0x00a3, B:31:0x00b1, B:45:0x00ee, B:48:0x0109, B:50:0x011c, B:52:0x0126, B:54:0x014f, B:58:0x015e, B:61:0x0169, B:63:0x0174, B:66:0x017d, B:69:0x018d, B:72:0x019e, B:74:0x01a9, B:78:0x01b8, B:81:0x01c3, B:83:0x01ce, B:86:0x01d7, B:89:0x01e7, B:92:0x01f8, B:94:0x0203, B:98:0x0212, B:101:0x021d, B:103:0x0228, B:106:0x0231, B:109:0x0243, B:112:0x0256, B:114:0x0261, B:118:0x0270, B:121:0x027b, B:123:0x0286, B:126:0x028f, B:129:0x02a1, B:132:0x02b4, B:136:0x02c7, B:140:0x02d6, B:143:0x02e1, B:147:0x02f0, B:150:0x02f9, B:151:0x030a, B:153:0x031a, B:157:0x032c, B:161:0x033b, B:164:0x0346, B:168:0x0355, B:171:0x035e, B:172:0x036f, B:175:0x0383, B:178:0x038e, B:180:0x0396, B:187:0x03af, B:192:0x03e2, B:195:0x03f5, B:197:0x03fd, B:199:0x0409, B:201:0x0412, B:203:0x041b, B:205:0x0421, B:223:0x0472, B:226:0x047d, B:229:0x048a, B:231:0x0498, B:233:0x04a3, B:235:0x04ab, B:237:0x04b3, B:242:0x04d2, B:243:0x04e6, B:208:0x042f, B:210:0x0438, B:212:0x0441, B:218:0x0460, B:220:0x0468, B:215:0x044e, B:217:0x0454, B:189:0x03cb, B:38:0x00d5), top: B:261:0x009b }] */
    /* JADX WARN: Removed duplicated region for block: B:11:0x0038 A[PHI: r18
  0x0038: PHI (r18v10 java.util.Map<java.lang.String, java.lang.Object>) = 
  (r18v0 java.util.Map<java.lang.String, java.lang.Object>)
  (r18v1 java.util.Map<java.lang.String, java.lang.Object>)
  (r18v3 java.util.Map<java.lang.String, java.lang.Object>)
  (r18v11 java.util.Map<java.lang.String, java.lang.Object>)
 binds: [B:16:0x005e, B:18:0x006a, B:260:0x0539, B:9:0x0032] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:12:0x003f  */
    /* JADX WARN: Removed duplicated region for block: B:149:0x02f8  */
    /* JADX WARN: Removed duplicated region for block: B:170:0x035d  */
    /* JADX WARN: Removed duplicated region for block: B:241:0x04cf  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0106  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x019e A[Catch: all -> 0x0528, TRY_ENTER, TRY_LEAVE, TryCatch #0 {all -> 0x0528, blocks: (B:27:0x009b, B:29:0x00a3, B:31:0x00b1, B:45:0x00ee, B:48:0x0109, B:50:0x011c, B:52:0x0126, B:54:0x014f, B:58:0x015e, B:61:0x0169, B:63:0x0174, B:66:0x017d, B:69:0x018d, B:72:0x019e, B:74:0x01a9, B:78:0x01b8, B:81:0x01c3, B:83:0x01ce, B:86:0x01d7, B:89:0x01e7, B:92:0x01f8, B:94:0x0203, B:98:0x0212, B:101:0x021d, B:103:0x0228, B:106:0x0231, B:109:0x0243, B:112:0x0256, B:114:0x0261, B:118:0x0270, B:121:0x027b, B:123:0x0286, B:126:0x028f, B:129:0x02a1, B:132:0x02b4, B:136:0x02c7, B:140:0x02d6, B:143:0x02e1, B:147:0x02f0, B:150:0x02f9, B:151:0x030a, B:153:0x031a, B:157:0x032c, B:161:0x033b, B:164:0x0346, B:168:0x0355, B:171:0x035e, B:172:0x036f, B:175:0x0383, B:178:0x038e, B:180:0x0396, B:187:0x03af, B:192:0x03e2, B:195:0x03f5, B:197:0x03fd, B:199:0x0409, B:201:0x0412, B:203:0x041b, B:205:0x0421, B:223:0x0472, B:226:0x047d, B:229:0x048a, B:231:0x0498, B:233:0x04a3, B:235:0x04ab, B:237:0x04b3, B:242:0x04d2, B:243:0x04e6, B:208:0x042f, B:210:0x0438, B:212:0x0441, B:218:0x0460, B:220:0x0468, B:215:0x044e, B:217:0x0454, B:189:0x03cb, B:38:0x00d5), top: B:261:0x009b }] */
    /* JADX WARN: Removed duplicated region for block: B:92:0x01f8 A[Catch: all -> 0x0528, TRY_ENTER, TRY_LEAVE, TryCatch #0 {all -> 0x0528, blocks: (B:27:0x009b, B:29:0x00a3, B:31:0x00b1, B:45:0x00ee, B:48:0x0109, B:50:0x011c, B:52:0x0126, B:54:0x014f, B:58:0x015e, B:61:0x0169, B:63:0x0174, B:66:0x017d, B:69:0x018d, B:72:0x019e, B:74:0x01a9, B:78:0x01b8, B:81:0x01c3, B:83:0x01ce, B:86:0x01d7, B:89:0x01e7, B:92:0x01f8, B:94:0x0203, B:98:0x0212, B:101:0x021d, B:103:0x0228, B:106:0x0231, B:109:0x0243, B:112:0x0256, B:114:0x0261, B:118:0x0270, B:121:0x027b, B:123:0x0286, B:126:0x028f, B:129:0x02a1, B:132:0x02b4, B:136:0x02c7, B:140:0x02d6, B:143:0x02e1, B:147:0x02f0, B:150:0x02f9, B:151:0x030a, B:153:0x031a, B:157:0x032c, B:161:0x033b, B:164:0x0346, B:168:0x0355, B:171:0x035e, B:172:0x036f, B:175:0x0383, B:178:0x038e, B:180:0x0396, B:187:0x03af, B:192:0x03e2, B:195:0x03f5, B:197:0x03fd, B:199:0x0409, B:201:0x0412, B:203:0x041b, B:205:0x0421, B:223:0x0472, B:226:0x047d, B:229:0x048a, B:231:0x0498, B:233:0x04a3, B:235:0x04ab, B:237:0x04b3, B:242:0x04d2, B:243:0x04e6, B:208:0x042f, B:210:0x0438, B:212:0x0441, B:218:0x0460, B:220:0x0468, B:215:0x044e, B:217:0x0454, B:189:0x03cb, B:38:0x00d5), top: B:261:0x009b }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void write(com.alibaba.fastjson.serializer.JSONSerializer r9, java.lang.Object r10, java.lang.Object r11, java.lang.reflect.Type r12, int r13, boolean r14) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 1344
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.serializer.MapSerializer.write(com.alibaba.fastjson.serializer.JSONSerializer, java.lang.Object, java.lang.Object, java.lang.reflect.Type, int, boolean):void");
    }
}
