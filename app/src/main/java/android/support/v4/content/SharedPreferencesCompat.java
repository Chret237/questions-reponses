package android.support.v4.content;

import android.content.SharedPreferences;

@Deprecated
/* loaded from: classes-dex2jar.jar:android/support/v4/content/SharedPreferencesCompat.class */
public final class SharedPreferencesCompat {

    @Deprecated
    /* loaded from: classes-dex2jar.jar:android/support/v4/content/SharedPreferencesCompat$EditorCompat.class */
    public static final class EditorCompat {
        private static EditorCompat sInstance;
        private final Helper mHelper = new Helper();

        /* loaded from: classes-dex2jar.jar:android/support/v4/content/SharedPreferencesCompat$EditorCompat$Helper.class */
        private static class Helper {
            Helper() {
            }

            public void apply(SharedPreferences.Editor editor) {
                try {
                    editor.apply();
                } catch (AbstractMethodError e) {
                    editor.commit();
                }
            }
        }

        private EditorCompat() {
        }

        @Deprecated
        public static EditorCompat getInstance() {
            if (sInstance == null) {
                sInstance = new EditorCompat();
            }
            return sInstance;
        }

        @Deprecated
        public void apply(SharedPreferences.Editor editor) {
            this.mHelper.apply(editor);
        }
    }

    private SharedPreferencesCompat() {
    }
}
