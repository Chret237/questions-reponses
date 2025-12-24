package android.support.v4.provider;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.text.TextUtils;
import android.util.Log;

/* loaded from: classes-dex2jar.jar:android/support/v4/provider/DocumentsContractApi19.class */
class DocumentsContractApi19 {
    private static final int FLAG_VIRTUAL_DOCUMENT = 512;
    private static final String TAG = "DocumentFile";

    private DocumentsContractApi19() {
    }

    public static boolean canRead(Context context, Uri uri) {
        return context.checkCallingOrSelfUriPermission(uri, 1) == 0 && !TextUtils.isEmpty(getRawType(context, uri));
    }

    public static boolean canWrite(Context context, Uri uri) {
        if (context.checkCallingOrSelfUriPermission(uri, 2) != 0) {
            return false;
        }
        String rawType = getRawType(context, uri);
        int iQueryForInt = queryForInt(context, uri, "flags", 0);
        if (TextUtils.isEmpty(rawType)) {
            return false;
        }
        if ((iQueryForInt & 4) != 0) {
            return true;
        }
        if (!"vnd.android.document/directory".equals(rawType) || (iQueryForInt & 8) == 0) {
            return (TextUtils.isEmpty(rawType) || (iQueryForInt & 2) == 0) ? false : true;
        }
        return true;
    }

    private static void closeQuietly(AutoCloseable autoCloseable) throws Exception {
        if (autoCloseable != null) {
            try {
                autoCloseable.close();
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e2) {
            }
        }
    }

    public static boolean exists(Context context, Uri uri) throws Exception {
        ContentResolver contentResolver = context.getContentResolver();
        boolean z = true;
        Cursor cursor = null;
        Cursor cursor2 = null;
        try {
            try {
                Cursor cursorQuery = contentResolver.query(uri, new String[]{"document_id"}, null, null, null);
                cursor2 = cursorQuery;
                cursor = cursorQuery;
                if (cursorQuery.getCount() <= 0) {
                    z = false;
                }
                closeQuietly(cursorQuery);
                return z;
            } catch (Exception e) {
                StringBuilder sb = new StringBuilder();
                sb.append("Failed query: ");
                sb.append(e);
                cursor2 = cursor;
                Log.w(TAG, sb.toString());
                closeQuietly(cursor);
                return false;
            }
        } catch (Throwable th) {
            closeQuietly(cursor2);
            throw th;
        }
    }

    public static long getFlags(Context context, Uri uri) {
        return queryForLong(context, uri, "flags", 0L);
    }

    public static String getName(Context context, Uri uri) {
        return queryForString(context, uri, "_display_name", null);
    }

    private static String getRawType(Context context, Uri uri) {
        return queryForString(context, uri, "mime_type", null);
    }

    public static String getType(Context context, Uri uri) {
        String rawType = getRawType(context, uri);
        String str = rawType;
        if ("vnd.android.document/directory".equals(rawType)) {
            str = null;
        }
        return str;
    }

    public static boolean isDirectory(Context context, Uri uri) {
        return "vnd.android.document/directory".equals(getRawType(context, uri));
    }

    public static boolean isFile(Context context, Uri uri) {
        String rawType = getRawType(context, uri);
        return ("vnd.android.document/directory".equals(rawType) || TextUtils.isEmpty(rawType)) ? false : true;
    }

    public static boolean isVirtual(Context context, Uri uri) {
        boolean z = false;
        if (!DocumentsContract.isDocumentUri(context, uri)) {
            return false;
        }
        if ((getFlags(context, uri) & 512) != 0) {
            z = true;
        }
        return z;
    }

    public static long lastModified(Context context, Uri uri) {
        return queryForLong(context, uri, "last_modified", 0L);
    }

    public static long length(Context context, Uri uri) {
        return queryForLong(context, uri, "_size", 0L);
    }

    private static int queryForInt(Context context, Uri uri, String str, int i) {
        return (int) queryForLong(context, uri, str, i);
    }

    private static long queryForLong(Context context, Uri uri, String str, long j) throws Exception {
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = null;
        Cursor cursor2 = null;
        try {
            try {
                Cursor cursorQuery = contentResolver.query(uri, new String[]{str}, null, null, null);
                if (!cursorQuery.moveToFirst() || cursorQuery.isNull(0)) {
                    closeQuietly(cursorQuery);
                    return j;
                }
                cursor2 = cursorQuery;
                cursor = cursorQuery;
                long j2 = cursorQuery.getLong(0);
                closeQuietly(cursorQuery);
                return j2;
            } catch (Exception e) {
                StringBuilder sb = new StringBuilder();
                sb.append("Failed query: ");
                sb.append(e);
                Log.w(TAG, sb.toString());
                closeQuietly(cursor);
                return j;
            }
        } catch (Throwable th) {
            closeQuietly(cursor2);
            throw th;
        }
    }

    private static String queryForString(Context context, Uri uri, String str, String str2) throws Exception {
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = null;
        Cursor cursor2 = null;
        try {
            try {
                Cursor cursorQuery = contentResolver.query(uri, new String[]{str}, null, null, null);
                if (!cursorQuery.moveToFirst() || cursorQuery.isNull(0)) {
                    closeQuietly(cursorQuery);
                    return str2;
                }
                cursor2 = cursorQuery;
                cursor = cursorQuery;
                String string = cursorQuery.getString(0);
                closeQuietly(cursorQuery);
                return string;
            } catch (Exception e) {
                StringBuilder sb = new StringBuilder();
                sb.append("Failed query: ");
                sb.append(e);
                Log.w(TAG, sb.toString());
                closeQuietly(cursor);
                return str2;
            }
        } catch (Throwable th) {
            closeQuietly(cursor2);
            throw th;
        }
    }
}
