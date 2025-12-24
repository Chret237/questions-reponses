package android.support.v4.text;

import java.nio.CharBuffer;
import java.util.Locale;

/* loaded from: classes-dex2jar.jar:android/support/v4/text/TextDirectionHeuristicsCompat.class */
public final class TextDirectionHeuristicsCompat {
    private static final int STATE_FALSE = 1;
    private static final int STATE_TRUE = 0;
    private static final int STATE_UNKNOWN = 2;
    public static final TextDirectionHeuristicCompat LTR = new TextDirectionHeuristicInternal(null, false);
    public static final TextDirectionHeuristicCompat RTL = new TextDirectionHeuristicInternal(null, true);
    public static final TextDirectionHeuristicCompat FIRSTSTRONG_LTR = new TextDirectionHeuristicInternal(FirstStrong.INSTANCE, false);
    public static final TextDirectionHeuristicCompat FIRSTSTRONG_RTL = new TextDirectionHeuristicInternal(FirstStrong.INSTANCE, true);
    public static final TextDirectionHeuristicCompat ANYRTL_LTR = new TextDirectionHeuristicInternal(AnyStrong.INSTANCE_RTL, false);
    public static final TextDirectionHeuristicCompat LOCALE = TextDirectionHeuristicLocale.INSTANCE;

    /* loaded from: classes-dex2jar.jar:android/support/v4/text/TextDirectionHeuristicsCompat$AnyStrong.class */
    private static class AnyStrong implements TextDirectionAlgorithm {
        private final boolean mLookForRtl;
        static final AnyStrong INSTANCE_RTL = new AnyStrong(true);
        static final AnyStrong INSTANCE_LTR = new AnyStrong(false);

        private AnyStrong(boolean z) {
            this.mLookForRtl = z;
        }

        @Override // android.support.v4.text.TextDirectionHeuristicsCompat.TextDirectionAlgorithm
        public int checkRtl(CharSequence charSequence, int i, int i2) {
            boolean z = false;
            int i3 = i;
            while (i3 < i2 + i) {
                int iIsRtlText = TextDirectionHeuristicsCompat.isRtlText(Character.getDirectionality(charSequence.charAt(i3)));
                if (iIsRtlText != 0) {
                    if (iIsRtlText != 1) {
                        continue;
                        i3++;
                        z = z;
                    } else if (!this.mLookForRtl) {
                        return 1;
                    }
                } else if (this.mLookForRtl) {
                    return 0;
                }
                z = true;
                i3++;
                z = z;
            }
            if (z) {
                return this.mLookForRtl ? 1 : 0;
            }
            return 2;
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v4/text/TextDirectionHeuristicsCompat$FirstStrong.class */
    private static class FirstStrong implements TextDirectionAlgorithm {
        static final FirstStrong INSTANCE = new FirstStrong();

        private FirstStrong() {
        }

        @Override // android.support.v4.text.TextDirectionHeuristicsCompat.TextDirectionAlgorithm
        public int checkRtl(CharSequence charSequence, int i, int i2) {
            int iIsRtlTextOrFormat = 2;
            for (int i3 = i; i3 < i2 + i && iIsRtlTextOrFormat == 2; i3++) {
                iIsRtlTextOrFormat = TextDirectionHeuristicsCompat.isRtlTextOrFormat(Character.getDirectionality(charSequence.charAt(i3)));
            }
            return iIsRtlTextOrFormat;
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v4/text/TextDirectionHeuristicsCompat$TextDirectionAlgorithm.class */
    private interface TextDirectionAlgorithm {
        int checkRtl(CharSequence charSequence, int i, int i2);
    }

    /* loaded from: classes-dex2jar.jar:android/support/v4/text/TextDirectionHeuristicsCompat$TextDirectionHeuristicImpl.class */
    private static abstract class TextDirectionHeuristicImpl implements TextDirectionHeuristicCompat {
        private final TextDirectionAlgorithm mAlgorithm;

        TextDirectionHeuristicImpl(TextDirectionAlgorithm textDirectionAlgorithm) {
            this.mAlgorithm = textDirectionAlgorithm;
        }

        private boolean doCheck(CharSequence charSequence, int i, int i2) {
            int iCheckRtl = this.mAlgorithm.checkRtl(charSequence, i, i2);
            if (iCheckRtl == 0) {
                return true;
            }
            if (iCheckRtl != 1) {
                return defaultIsRtl();
            }
            return false;
        }

        protected abstract boolean defaultIsRtl();

        @Override // android.support.v4.text.TextDirectionHeuristicCompat
        public boolean isRtl(CharSequence charSequence, int i, int i2) {
            if (charSequence == null || i < 0 || i2 < 0 || charSequence.length() - i2 < i) {
                throw new IllegalArgumentException();
            }
            return this.mAlgorithm == null ? defaultIsRtl() : doCheck(charSequence, i, i2);
        }

        @Override // android.support.v4.text.TextDirectionHeuristicCompat
        public boolean isRtl(char[] cArr, int i, int i2) {
            return isRtl(CharBuffer.wrap(cArr), i, i2);
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v4/text/TextDirectionHeuristicsCompat$TextDirectionHeuristicInternal.class */
    private static class TextDirectionHeuristicInternal extends TextDirectionHeuristicImpl {
        private final boolean mDefaultIsRtl;

        TextDirectionHeuristicInternal(TextDirectionAlgorithm textDirectionAlgorithm, boolean z) {
            super(textDirectionAlgorithm);
            this.mDefaultIsRtl = z;
        }

        @Override // android.support.v4.text.TextDirectionHeuristicsCompat.TextDirectionHeuristicImpl
        protected boolean defaultIsRtl() {
            return this.mDefaultIsRtl;
        }
    }

    /* loaded from: classes-dex2jar.jar:android/support/v4/text/TextDirectionHeuristicsCompat$TextDirectionHeuristicLocale.class */
    private static class TextDirectionHeuristicLocale extends TextDirectionHeuristicImpl {
        static final TextDirectionHeuristicLocale INSTANCE = new TextDirectionHeuristicLocale();

        TextDirectionHeuristicLocale() {
            super(null);
        }

        @Override // android.support.v4.text.TextDirectionHeuristicsCompat.TextDirectionHeuristicImpl
        protected boolean defaultIsRtl() {
            boolean z = true;
            if (TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault()) != 1) {
                z = false;
            }
            return z;
        }
    }

    private TextDirectionHeuristicsCompat() {
    }

    static int isRtlText(int i) {
        if (i != 0) {
            return (i == 1 || i == 2) ? 0 : 2;
        }
        return 1;
    }

    static int isRtlTextOrFormat(int i) {
        if (i == 0) {
            return 1;
        }
        if (i == 1 || i == 2) {
            return 0;
        }
        switch (i) {
            case 14:
            case 15:
                return 1;
            case 16:
            case 17:
                return 0;
            default:
                return 2;
        }
    }
}
