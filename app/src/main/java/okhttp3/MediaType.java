package okhttp3;

import java.nio.charset.Charset;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;

/* loaded from: classes-dex2jar.jar:okhttp3/MediaType.class */
public final class MediaType {
    private static final String QUOTED = "\"([^\"]*)\"";
    private static final String TOKEN = "([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)";

    @Nullable
    private final String charset;
    private final String mediaType;
    private final String subtype;
    private final String type;
    private static final Pattern TYPE_SUBTYPE = Pattern.compile("([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)/([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)");
    private static final Pattern PARAMETER = Pattern.compile(";\\s*(?:([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)=(?:([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)|\"([^\"]*)\"))?");

    private MediaType(String str, String str2, String str3, @Nullable String str4) {
        this.mediaType = str;
        this.type = str2;
        this.subtype = str3;
        this.charset = str4;
    }

    @Nullable
    public static MediaType parse(String str) {
        Matcher matcher = TYPE_SUBTYPE.matcher(str);
        if (!matcher.lookingAt()) {
            return null;
        }
        String lowerCase = matcher.group(1).toLowerCase(Locale.US);
        String lowerCase2 = matcher.group(2).toLowerCase(Locale.US);
        Matcher matcher2 = PARAMETER.matcher(str);
        int iEnd = matcher.end();
        String str2 = null;
        while (true) {
            String str3 = str2;
            if (iEnd >= str.length()) {
                return new MediaType(str, lowerCase, lowerCase2, str3);
            }
            matcher2.region(iEnd, str.length());
            if (!matcher2.lookingAt()) {
                return null;
            }
            String strGroup = matcher2.group(1);
            String strGroup2 = str3;
            if (strGroup != null) {
                if (strGroup.equalsIgnoreCase("charset")) {
                    String strGroup3 = matcher2.group(2);
                    if (strGroup3 != null) {
                        strGroup2 = strGroup3;
                        if (strGroup3.startsWith("'")) {
                            strGroup2 = strGroup3;
                            if (strGroup3.endsWith("'")) {
                                strGroup2 = strGroup3;
                                if (strGroup3.length() > 2) {
                                    strGroup2 = strGroup3.substring(1, strGroup3.length() - 1);
                                }
                            }
                        }
                    } else {
                        strGroup2 = matcher2.group(3);
                    }
                    if (str3 != null && !strGroup2.equalsIgnoreCase(str3)) {
                        return null;
                    }
                } else {
                    strGroup2 = str3;
                }
            }
            iEnd = matcher2.end();
            str2 = strGroup2;
        }
    }

    @Nullable
    public Charset charset() {
        return charset(null);
    }

    @Nullable
    public Charset charset(@Nullable Charset charset) {
        Charset charsetForName = charset;
        try {
            if (this.charset != null) {
                charsetForName = Charset.forName(this.charset);
            }
        } catch (IllegalArgumentException e) {
            charsetForName = charset;
        }
        return charsetForName;
    }

    public boolean equals(@Nullable Object obj) {
        return (obj instanceof MediaType) && ((MediaType) obj).mediaType.equals(this.mediaType);
    }

    public int hashCode() {
        return this.mediaType.hashCode();
    }

    public String subtype() {
        return this.subtype;
    }

    public String toString() {
        return this.mediaType;
    }

    public String type() {
        return this.type;
    }
}
