package com.alibaba.fastjson.serializer;

import java.util.Arrays;

/* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/serializer/Labels.class */
public class Labels {

    /* loaded from: classes-dex2jar.jar:com/alibaba/fastjson/serializer/Labels$DefaultLabelFilter.class */
    private static class DefaultLabelFilter implements LabelFilter {
        private String[] excludes;
        private String[] includes;

        public DefaultLabelFilter(String[] strArr, String[] strArr2) {
            if (strArr != null) {
                String[] strArr3 = new String[strArr.length];
                this.includes = strArr3;
                System.arraycopy(strArr, 0, strArr3, 0, strArr.length);
                Arrays.sort(this.includes);
            }
            if (strArr2 != null) {
                String[] strArr4 = new String[strArr2.length];
                this.excludes = strArr4;
                System.arraycopy(strArr2, 0, strArr4, 0, strArr2.length);
                Arrays.sort(this.excludes);
            }
        }

        @Override // com.alibaba.fastjson.serializer.LabelFilter
        public boolean apply(String str) {
            String[] strArr = this.excludes;
            boolean z = true;
            if (strArr == null) {
                String[] strArr2 = this.includes;
                return strArr2 != null && Arrays.binarySearch(strArr2, str) >= 0;
            }
            if (Arrays.binarySearch(strArr, str) >= 0) {
                z = false;
            }
            return z;
        }
    }

    public static LabelFilter excludes(String... strArr) {
        return new DefaultLabelFilter(null, strArr);
    }

    public static LabelFilter includes(String... strArr) {
        return new DefaultLabelFilter(strArr, null);
    }
}
