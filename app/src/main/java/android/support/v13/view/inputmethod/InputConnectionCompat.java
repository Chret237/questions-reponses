package android.support.v13.view.inputmethod;

import android.content.ClipDescription;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.view.inputmethod.InputContentInfo;

/* loaded from: classes-dex2jar.jar:android/support/v13/view/inputmethod/InputConnectionCompat.class */
public final class InputConnectionCompat {
    private static final String COMMIT_CONTENT_ACTION = "android.support.v13.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT";
    private static final String COMMIT_CONTENT_CONTENT_URI_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_URI";
    private static final String COMMIT_CONTENT_DESCRIPTION_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION";
    private static final String COMMIT_CONTENT_FLAGS_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS";
    private static final String COMMIT_CONTENT_LINK_URI_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI";
    private static final String COMMIT_CONTENT_OPTS_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_OPTS";
    private static final String COMMIT_CONTENT_RESULT_RECEIVER = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_RESULT_RECEIVER";
    public static final int INPUT_CONTENT_GRANT_READ_URI_PERMISSION = 1;

    /* loaded from: classes-dex2jar.jar:android/support/v13/view/inputmethod/InputConnectionCompat$OnCommitContentListener.class */
    public interface OnCommitContentListener {
        boolean onCommitContent(InputContentInfoCompat inputContentInfoCompat, int i, Bundle bundle);
    }

    @Deprecated
    public InputConnectionCompat() {
    }

    public static boolean commitContent(InputConnection inputConnection, EditorInfo editorInfo, InputContentInfoCompat inputContentInfoCompat, int i, Bundle bundle) {
        boolean z;
        ClipDescription description = inputContentInfoCompat.getDescription();
        String[] contentMimeTypes = EditorInfoCompat.getContentMimeTypes(editorInfo);
        int length = contentMimeTypes.length;
        int i2 = 0;
        while (true) {
            if (i2 >= length) {
                z = false;
                break;
            }
            if (description.hasMimeType(contentMimeTypes[i2])) {
                z = true;
                break;
            }
            i2++;
        }
        if (!z) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= 25) {
            return inputConnection.commitContent((InputContentInfo) inputContentInfoCompat.unwrap(), i, bundle);
        }
        Bundle bundle2 = new Bundle();
        bundle2.putParcelable(COMMIT_CONTENT_CONTENT_URI_KEY, inputContentInfoCompat.getContentUri());
        bundle2.putParcelable(COMMIT_CONTENT_DESCRIPTION_KEY, inputContentInfoCompat.getDescription());
        bundle2.putParcelable(COMMIT_CONTENT_LINK_URI_KEY, inputContentInfoCompat.getLinkUri());
        bundle2.putInt(COMMIT_CONTENT_FLAGS_KEY, i);
        bundle2.putParcelable(COMMIT_CONTENT_OPTS_KEY, bundle);
        return inputConnection.performPrivateCommand(COMMIT_CONTENT_ACTION, bundle2);
    }

    public static InputConnection createWrapper(InputConnection inputConnection, EditorInfo editorInfo, OnCommitContentListener onCommitContentListener) {
        if (inputConnection == null) {
            throw new IllegalArgumentException("inputConnection must be non-null");
        }
        if (editorInfo == null) {
            throw new IllegalArgumentException("editorInfo must be non-null");
        }
        if (onCommitContentListener != null) {
            return Build.VERSION.SDK_INT >= 25 ? new InputConnectionWrapper(inputConnection, false, onCommitContentListener) { // from class: android.support.v13.view.inputmethod.InputConnectionCompat.1
                final OnCommitContentListener val$listener;

                {
                    this.val$listener = onCommitContentListener;
                }

                @Override // android.view.inputmethod.InputConnectionWrapper, android.view.inputmethod.InputConnection
                public boolean commitContent(InputContentInfo inputContentInfo, int i, Bundle bundle) {
                    if (this.val$listener.onCommitContent(InputContentInfoCompat.wrap(inputContentInfo), i, bundle)) {
                        return true;
                    }
                    return super.commitContent(inputContentInfo, i, bundle);
                }
            } : EditorInfoCompat.getContentMimeTypes(editorInfo).length == 0 ? inputConnection : new InputConnectionWrapper(inputConnection, false, onCommitContentListener) { // from class: android.support.v13.view.inputmethod.InputConnectionCompat.2
                final OnCommitContentListener val$listener;

                {
                    this.val$listener = onCommitContentListener;
                }

                @Override // android.view.inputmethod.InputConnectionWrapper, android.view.inputmethod.InputConnection
                public boolean performPrivateCommand(String str, Bundle bundle) {
                    if (InputConnectionCompat.handlePerformPrivateCommand(str, bundle, this.val$listener)) {
                        return true;
                    }
                    return super.performPrivateCommand(str, bundle);
                }
            };
        }
        throw new IllegalArgumentException("onCommitContentListener must be non-null");
    }

    /* JADX WARN: Multi-variable type inference failed */
    static boolean handlePerformPrivateCommand(String str, Bundle bundle, OnCommitContentListener onCommitContentListener) throws Throwable {
        ResultReceiver resultReceiver;
        if (!TextUtils.equals(COMMIT_CONTENT_ACTION, str) || bundle == null) {
            return false;
        }
        try {
            resultReceiver = (ResultReceiver) bundle.getParcelable(COMMIT_CONTENT_RESULT_RECEIVER);
            try {
                boolean zOnCommitContent = onCommitContentListener.onCommitContent(new InputContentInfoCompat((Uri) bundle.getParcelable(COMMIT_CONTENT_CONTENT_URI_KEY), (ClipDescription) bundle.getParcelable(COMMIT_CONTENT_DESCRIPTION_KEY), (Uri) bundle.getParcelable(COMMIT_CONTENT_LINK_URI_KEY)), bundle.getInt(COMMIT_CONTENT_FLAGS_KEY), (Bundle) bundle.getParcelable(COMMIT_CONTENT_OPTS_KEY));
                if (resultReceiver != null) {
                    resultReceiver.send(zOnCommitContent ? 1 : 0, null);
                }
                return zOnCommitContent;
            } catch (Throwable th) {
                th = th;
                if (resultReceiver != false) {
                    resultReceiver.send(0, null);
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            resultReceiver = null;
        }
    }
}
