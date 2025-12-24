package android.support.v4.text;

import android.os.Build;
import android.text.TextUtils;
import com.github.clans.fab.BuildConfig;
import java.util.Locale;

/* loaded from: classes-dex2jar.jar:android/support/v4/text/TextUtilsCompat.class */
public final class TextUtilsCompat {
    private static final String ARAB_SCRIPT_SUBTAG = "Arab";
    private static final String HEBR_SCRIPT_SUBTAG = "Hebr";
    private static final Locale ROOT = new Locale(BuildConfig.FLAVOR, BuildConfig.FLAVOR);

    private TextUtilsCompat() {
    }

    private static int getLayoutDirectionFromFirstChar(Locale locale) {
        byte directionality = Character.getDirectionality(locale.getDisplayName(locale).charAt(0));
        return (directionality == 1 || directionality == 2) ? 1 : 0;
    }

    public static int getLayoutDirectionFromLocale(Locale locale) {
        if (Build.VERSION.SDK_INT >= 17) {
            return TextUtils.getLayoutDirectionFromLocale(locale);
        }
        if (locale == null || locale.equals(ROOT)) {
            return 0;
        }
        String strMaximizeAndGetScript = ICUCompat.maximizeAndGetScript(locale);
        return strMaximizeAndGetScript == null ? getLayoutDirectionFromFirstChar(locale) : (strMaximizeAndGetScript.equalsIgnoreCase(ARAB_SCRIPT_SUBTAG) || strMaximizeAndGetScript.equalsIgnoreCase(HEBR_SCRIPT_SUBTAG)) ? 1 : 0;
    }

    public static String htmlEncode(String str) {
        if (Build.VERSION.SDK_INT >= 17) {
            return TextUtils.htmlEncode(str);
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char cCharAt = str.charAt(i);
            if (cCharAt == '\"') {
                sb.append("&quot;");
            } else if (cCharAt == '<') {
                sb.append("&lt;");
            } else if (cCharAt == '>') {
                sb.append("&gt;");
            } else if (cCharAt == '&') {
                sb.append("&amp;");
            } else if (cCharAt != '\'') {
                sb.append(cCharAt);
            } else {
                sb.append("&#39;");
            }
        }
        return sb.toString();
    }
}
