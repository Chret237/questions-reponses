package okhttp3.internal.http;

import android.support.v7.widget.ActivityChooserView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import okhttp3.Challenge;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.Util;

/* loaded from: classes-dex2jar.jar:okhttp3/internal/http/HttpHeaders.class */
public final class HttpHeaders {
    private static final Pattern PARAMETER = Pattern.compile(" +([^ \"=]*)=(:?\"([^\"]*)\"|([^ \"=]*)) *(:?,|$)");
    private static final String QUOTED_STRING = "\"([^\"]*)\"";
    private static final String TOKEN = "([^ \"=]*)";

    private HttpHeaders() {
    }

    public static long contentLength(Headers headers) {
        return stringToLong(headers.get("Content-Length"));
    }

    public static long contentLength(Response response) {
        return contentLength(response.headers());
    }

    public static boolean hasBody(Response response) {
        if (response.request().method().equals("HEAD")) {
            return false;
        }
        int iCode = response.code();
        return (((iCode >= 100 && iCode < 200) || iCode == 204 || iCode == 304) && contentLength(response) == -1 && !"chunked".equalsIgnoreCase(response.header("Transfer-Encoding"))) ? false : true;
    }

    public static boolean hasVaryAll(Headers headers) {
        return varyFields(headers).contains("*");
    }

    public static boolean hasVaryAll(Response response) {
        return hasVaryAll(response.headers());
    }

    public static List<Challenge> parseChallenges(Headers headers, String str) {
        String str2;
        String str3;
        String strGroup;
        ArrayList arrayList = new ArrayList();
        for (String str4 : headers.values(str)) {
            int iIndexOf = str4.indexOf(32);
            if (iIndexOf != -1) {
                String strSubstring = str4.substring(0, iIndexOf);
                Matcher matcher = PARAMETER.matcher(str4);
                String str5 = null;
                String strGroup2 = null;
                while (true) {
                    str2 = str5;
                    str3 = strGroup2;
                    if (!matcher.find(iIndexOf)) {
                        break;
                    }
                    if (str4.regionMatches(true, matcher.start(1), "realm", 0, 5)) {
                        strGroup = matcher.group(3);
                    } else {
                        strGroup = str5;
                        if (str4.regionMatches(true, matcher.start(1), "charset", 0, 7)) {
                            strGroup2 = matcher.group(3);
                            strGroup = str5;
                        }
                    }
                    if (strGroup != null && strGroup2 != null) {
                        str2 = strGroup;
                        str3 = strGroup2;
                        break;
                    }
                    iIndexOf = matcher.end();
                    str5 = strGroup;
                }
                if (str2 != null) {
                    Challenge challenge = new Challenge(strSubstring, str2);
                    Challenge challengeWithCharset = challenge;
                    if (str3 != null) {
                        if (str3.equalsIgnoreCase("UTF-8")) {
                            challengeWithCharset = challenge.withCharset(Util.UTF_8);
                        }
                    }
                    arrayList.add(challengeWithCharset);
                }
            }
        }
        return arrayList;
    }

    public static int parseSeconds(String str, int i) {
        long j;
        try {
            j = Long.parseLong(str);
        } catch (NumberFormatException e) {
        }
        if (j > 2147483647L) {
            return ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
        }
        if (j < 0) {
            return 0;
        }
        i = (int) j;
        return i;
    }

    public static void receiveHeaders(CookieJar cookieJar, HttpUrl httpUrl, Headers headers) {
        if (cookieJar == CookieJar.NO_COOKIES) {
            return;
        }
        List<Cookie> all = Cookie.parseAll(httpUrl, headers);
        if (all.isEmpty()) {
            return;
        }
        cookieJar.saveFromResponse(httpUrl, all);
    }

    public static int skipUntil(String str, int i, String str2) {
        while (i < str.length() && str2.indexOf(str.charAt(i)) == -1) {
            i++;
        }
        return i;
    }

    public static int skipWhitespace(String str, int i) {
        char cCharAt;
        while (i < str.length() && ((cCharAt = str.charAt(i)) == ' ' || cCharAt == '\t')) {
            i++;
        }
        return i;
    }

    private static long stringToLong(String str) throws NumberFormatException {
        long j = -1;
        if (str == null) {
            return -1L;
        }
        try {
            j = Long.parseLong(str);
        } catch (NumberFormatException e) {
        }
        return j;
    }

    public static Set<String> varyFields(Headers headers) {
        Set<String> setEmptySet = Collections.emptySet();
        int size = headers.size();
        for (int i = 0; i < size; i++) {
            if ("Vary".equalsIgnoreCase(headers.name(i))) {
                String strValue = headers.value(i);
                Set<String> treeSet = setEmptySet;
                if (setEmptySet.isEmpty()) {
                    treeSet = new TreeSet((Comparator<? super String>) String.CASE_INSENSITIVE_ORDER);
                }
                String[] strArrSplit = strValue.split(",");
                int length = strArrSplit.length;
                int i2 = 0;
                while (true) {
                    setEmptySet = treeSet;
                    if (i2 < length) {
                        treeSet.add(strArrSplit[i2].trim());
                        i2++;
                    }
                }
            }
        }
        return setEmptySet;
    }

    private static Set<String> varyFields(Response response) {
        return varyFields(response.headers());
    }

    public static Headers varyHeaders(Headers headers, Headers headers2) {
        Set<String> setVaryFields = varyFields(headers2);
        if (setVaryFields.isEmpty()) {
            return new Headers.Builder().build();
        }
        Headers.Builder builder = new Headers.Builder();
        int size = headers.size();
        for (int i = 0; i < size; i++) {
            String strName = headers.name(i);
            if (setVaryFields.contains(strName)) {
                builder.add(strName, headers.value(i));
            }
        }
        return builder.build();
    }

    public static Headers varyHeaders(Response response) {
        return varyHeaders(response.networkResponse().request().headers(), response.headers());
    }

    public static boolean varyMatches(Response response, Headers headers, Request request) {
        for (String str : varyFields(response)) {
            if (!Util.equal(headers.values(str), request.headers(str))) {
                return false;
            }
        }
        return true;
    }
}
