package okhttp3;

import com.github.clans.fab.BuildConfig;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.annotation.Nullable;
import okhttp3.internal.Util;
import okhttp3.internal.http.HttpDate;

/* loaded from: classes-dex2jar.jar:okhttp3/Headers.class */
public final class Headers {
    private final String[] namesAndValues;

    /* loaded from: classes-dex2jar.jar:okhttp3/Headers$Builder.class */
    public static final class Builder {
        final List<String> namesAndValues = new ArrayList(20);

        private void checkNameAndValue(String str, String str2) {
            if (str == null) {
                throw new NullPointerException("name == null");
            }
            if (str.isEmpty()) {
                throw new IllegalArgumentException("name is empty");
            }
            int length = str.length();
            for (int i = 0; i < length; i++) {
                char cCharAt = str.charAt(i);
                if (cCharAt <= ' ' || cCharAt >= 127) {
                    throw new IllegalArgumentException(Util.format("Unexpected char %#04x at %d in header name: %s", Integer.valueOf(cCharAt), Integer.valueOf(i), str));
                }
            }
            if (str2 == null) {
                throw new NullPointerException("value for name " + str + " == null");
            }
            int length2 = str2.length();
            for (int i2 = 0; i2 < length2; i2++) {
                char cCharAt2 = str2.charAt(i2);
                if ((cCharAt2 <= 31 && cCharAt2 != '\t') || cCharAt2 >= 127) {
                    throw new IllegalArgumentException(Util.format("Unexpected char %#04x at %d in %s value: %s", Integer.valueOf(cCharAt2), Integer.valueOf(i2), str, str2));
                }
            }
        }

        public Builder add(String str) {
            int iIndexOf = str.indexOf(":");
            if (iIndexOf != -1) {
                return add(str.substring(0, iIndexOf).trim(), str.substring(iIndexOf + 1));
            }
            throw new IllegalArgumentException("Unexpected header: " + str);
        }

        public Builder add(String str, String str2) {
            checkNameAndValue(str, str2);
            return addLenient(str, str2);
        }

        Builder addLenient(String str) {
            int iIndexOf = str.indexOf(":", 1);
            return iIndexOf != -1 ? addLenient(str.substring(0, iIndexOf), str.substring(iIndexOf + 1)) : str.startsWith(":") ? addLenient(BuildConfig.FLAVOR, str.substring(1)) : addLenient(BuildConfig.FLAVOR, str);
        }

        Builder addLenient(String str, String str2) {
            this.namesAndValues.add(str);
            this.namesAndValues.add(str2.trim());
            return this;
        }

        public Headers build() {
            return new Headers(this);
        }

        public String get(String str) {
            for (int size = this.namesAndValues.size() - 2; size >= 0; size -= 2) {
                if (str.equalsIgnoreCase(this.namesAndValues.get(size))) {
                    return this.namesAndValues.get(size + 1);
                }
            }
            return null;
        }

        public Builder removeAll(String str) {
            int i = 0;
            while (true) {
                int i2 = i;
                if (i2 >= this.namesAndValues.size()) {
                    return this;
                }
                int i3 = i2;
                if (str.equalsIgnoreCase(this.namesAndValues.get(i2))) {
                    this.namesAndValues.remove(i2);
                    this.namesAndValues.remove(i2);
                    i3 = i2 - 2;
                }
                i = i3 + 2;
            }
        }

        public Builder set(String str, String str2) {
            checkNameAndValue(str, str2);
            removeAll(str);
            addLenient(str, str2);
            return this;
        }
    }

    Headers(Builder builder) {
        this.namesAndValues = (String[]) builder.namesAndValues.toArray(new String[builder.namesAndValues.size()]);
    }

    private Headers(String[] strArr) {
        this.namesAndValues = strArr;
    }

    private static String get(String[] strArr, String str) {
        for (int length = strArr.length - 2; length >= 0; length -= 2) {
            if (str.equalsIgnoreCase(strArr[length])) {
                return strArr[length + 1];
            }
        }
        return null;
    }

    /* renamed from: of */
    public static Headers m16of(Map<String, String> map) {
        if (map == null) {
            throw new NullPointerException("headers == null");
        }
        String[] strArr = new String[map.size() * 2];
        int i = 0;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getKey() == null || entry.getValue() == null) {
                throw new IllegalArgumentException("Headers cannot be null");
            }
            String strTrim = entry.getKey().trim();
            String strTrim2 = entry.getValue().trim();
            if (strTrim.length() == 0 || strTrim.indexOf(0) != -1 || strTrim2.indexOf(0) != -1) {
                throw new IllegalArgumentException("Unexpected header: " + strTrim + ": " + strTrim2);
            }
            strArr[i] = strTrim;
            strArr[i + 1] = strTrim2;
            i += 2;
        }
        return new Headers(strArr);
    }

    /* renamed from: of */
    public static Headers m17of(String... strArr) {
        if (strArr == null) {
            throw new NullPointerException("namesAndValues == null");
        }
        if (strArr.length % 2 != 0) {
            throw new IllegalArgumentException("Expected alternating header names and values");
        }
        String[] strArr2 = (String[]) strArr.clone();
        for (int i = 0; i < strArr2.length; i++) {
            if (strArr2[i] == null) {
                throw new IllegalArgumentException("Headers cannot be null");
            }
            strArr2[i] = strArr2[i].trim();
        }
        for (int i2 = 0; i2 < strArr2.length; i2 += 2) {
            String str = strArr2[i2];
            String str2 = strArr2[i2 + 1];
            if (str.length() == 0 || str.indexOf(0) != -1 || str2.indexOf(0) != -1) {
                throw new IllegalArgumentException("Unexpected header: " + str + ": " + str2);
            }
        }
        return new Headers(strArr2);
    }

    public long byteCount() {
        String[] strArr = this.namesAndValues;
        long length = strArr.length * 2;
        for (int i = 0; i < strArr.length; i++) {
            length += this.namesAndValues[i].length();
        }
        return length;
    }

    public boolean equals(@Nullable Object obj) {
        return (obj instanceof Headers) && Arrays.equals(((Headers) obj).namesAndValues, this.namesAndValues);
    }

    @Nullable
    public String get(String str) {
        return get(this.namesAndValues, str);
    }

    @Nullable
    public Date getDate(String str) {
        String str2 = get(str);
        return str2 != null ? HttpDate.parse(str2) : null;
    }

    public int hashCode() {
        return Arrays.hashCode(this.namesAndValues);
    }

    public String name(int i) {
        return this.namesAndValues[i * 2];
    }

    public Set<String> names() {
        TreeSet treeSet = new TreeSet(String.CASE_INSENSITIVE_ORDER);
        int size = size();
        for (int i = 0; i < size; i++) {
            treeSet.add(name(i));
        }
        return Collections.unmodifiableSet(treeSet);
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        Collections.addAll(builder.namesAndValues, this.namesAndValues);
        return builder;
    }

    public int size() {
        return this.namesAndValues.length / 2;
    }

    public Map<String, List<String>> toMultimap() {
        TreeMap treeMap = new TreeMap(String.CASE_INSENSITIVE_ORDER);
        int size = size();
        for (int i = 0; i < size; i++) {
            String lowerCase = name(i).toLowerCase(Locale.US);
            List list = (List) treeMap.get(lowerCase);
            List arrayList = list;
            if (list == null) {
                arrayList = new ArrayList(2);
                treeMap.put(lowerCase, arrayList);
            }
            arrayList.add(value(i));
        }
        return treeMap;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        int size = size();
        for (int i = 0; i < size; i++) {
            sb.append(name(i));
            sb.append(": ");
            sb.append(value(i));
            sb.append("\n");
        }
        return sb.toString();
    }

    public String value(int i) {
        return this.namesAndValues[(i * 2) + 1];
    }

    public List<String> values(String str) {
        int size = size();
        ArrayList arrayList = null;
        int i = 0;
        while (i < size) {
            ArrayList arrayList2 = arrayList;
            if (str.equalsIgnoreCase(name(i))) {
                arrayList2 = arrayList;
                if (arrayList == null) {
                    arrayList2 = new ArrayList(2);
                }
                arrayList2.add(value(i));
            }
            i++;
            arrayList = arrayList2;
        }
        return arrayList != null ? Collections.unmodifiableList(arrayList) : Collections.emptyList();
    }
}
