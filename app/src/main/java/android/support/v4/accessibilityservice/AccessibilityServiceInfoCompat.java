package android.support.v4.accessibilityservice;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.pm.PackageManager;
import android.os.Build;

/* loaded from: classes-dex2jar.jar:android/support/v4/accessibilityservice/AccessibilityServiceInfoCompat.class */
public final class AccessibilityServiceInfoCompat {
    public static final int CAPABILITY_CAN_FILTER_KEY_EVENTS = 8;
    public static final int CAPABILITY_CAN_REQUEST_ENHANCED_WEB_ACCESSIBILITY = 4;
    public static final int CAPABILITY_CAN_REQUEST_TOUCH_EXPLORATION = 2;
    public static final int CAPABILITY_CAN_RETRIEVE_WINDOW_CONTENT = 1;
    public static final int FEEDBACK_ALL_MASK = -1;
    public static final int FEEDBACK_BRAILLE = 32;
    public static final int FLAG_INCLUDE_NOT_IMPORTANT_VIEWS = 2;
    public static final int FLAG_REPORT_VIEW_IDS = 16;
    public static final int FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY = 8;
    public static final int FLAG_REQUEST_FILTER_KEY_EVENTS = 32;
    public static final int FLAG_REQUEST_TOUCH_EXPLORATION_MODE = 4;

    private AccessibilityServiceInfoCompat() {
    }

    public static String capabilityToString(int i) {
        return i != 1 ? i != 2 ? i != 4 ? i != 8 ? "UNKNOWN" : "CAPABILITY_CAN_FILTER_KEY_EVENTS" : "CAPABILITY_CAN_REQUEST_ENHANCED_WEB_ACCESSIBILITY" : "CAPABILITY_CAN_REQUEST_TOUCH_EXPLORATION" : "CAPABILITY_CAN_RETRIEVE_WINDOW_CONTENT";
    }

    public static String feedbackTypeToString(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        while (i > 0) {
            int iNumberOfTrailingZeros = 1 << Integer.numberOfTrailingZeros(i);
            i &= iNumberOfTrailingZeros ^ (-1);
            if (sb.length() > 1) {
                sb.append(", ");
            }
            if (iNumberOfTrailingZeros == 1) {
                sb.append("FEEDBACK_SPOKEN");
            } else if (iNumberOfTrailingZeros == 2) {
                sb.append("FEEDBACK_HAPTIC");
            } else if (iNumberOfTrailingZeros == 4) {
                sb.append("FEEDBACK_AUDIBLE");
            } else if (iNumberOfTrailingZeros == 8) {
                sb.append("FEEDBACK_VISUAL");
            } else if (iNumberOfTrailingZeros == 16) {
                sb.append("FEEDBACK_GENERIC");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public static String flagToString(int i) {
        if (i == 1) {
            return "DEFAULT";
        }
        if (i == 2) {
            return "FLAG_INCLUDE_NOT_IMPORTANT_VIEWS";
        }
        if (i == 4) {
            return "FLAG_REQUEST_TOUCH_EXPLORATION_MODE";
        }
        if (i == 8) {
            return "FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY";
        }
        if (i == 16) {
            return "FLAG_REPORT_VIEW_IDS";
        }
        if (i != 32) {
            return null;
        }
        return "FLAG_REQUEST_FILTER_KEY_EVENTS";
    }

    public static int getCapabilities(AccessibilityServiceInfo accessibilityServiceInfo) {
        return Build.VERSION.SDK_INT >= 18 ? accessibilityServiceInfo.getCapabilities() : accessibilityServiceInfo.getCanRetrieveWindowContent() ? 1 : 0;
    }

    public static String loadDescription(AccessibilityServiceInfo accessibilityServiceInfo, PackageManager packageManager) {
        return Build.VERSION.SDK_INT >= 16 ? accessibilityServiceInfo.loadDescription(packageManager) : accessibilityServiceInfo.getDescription();
    }
}
