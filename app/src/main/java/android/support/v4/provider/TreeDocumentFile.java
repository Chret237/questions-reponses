package android.support.v4.provider;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.util.Log;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/* loaded from: classes-dex2jar.jar:android/support/v4/provider/TreeDocumentFile.class */
class TreeDocumentFile extends DocumentFile {
    private Context mContext;
    private Uri mUri;

    TreeDocumentFile(DocumentFile documentFile, Context context, Uri uri) {
        super(documentFile);
        this.mContext = context;
        this.mUri = uri;
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

    private static Uri createFile(Context context, Uri uri, String str, String str2) {
        try {
            return DocumentsContract.createDocument(context.getContentResolver(), uri, str, str2);
        } catch (Exception e) {
            return null;
        }
    }

    @Override // android.support.v4.provider.DocumentFile
    public boolean canRead() {
        return DocumentsContractApi19.canRead(this.mContext, this.mUri);
    }

    @Override // android.support.v4.provider.DocumentFile
    public boolean canWrite() {
        return DocumentsContractApi19.canWrite(this.mContext, this.mUri);
    }

    @Override // android.support.v4.provider.DocumentFile
    public DocumentFile createDirectory(String str) {
        Uri uriCreateFile = createFile(this.mContext, this.mUri, "vnd.android.document/directory", str);
        return uriCreateFile != null ? new TreeDocumentFile(this, this.mContext, uriCreateFile) : null;
    }

    @Override // android.support.v4.provider.DocumentFile
    public DocumentFile createFile(String str, String str2) {
        Uri uriCreateFile = createFile(this.mContext, this.mUri, str, str2);
        return uriCreateFile != null ? new TreeDocumentFile(this, this.mContext, uriCreateFile) : null;
    }

    @Override // android.support.v4.provider.DocumentFile
    public boolean delete() {
        try {
            return DocumentsContract.deleteDocument(this.mContext.getContentResolver(), this.mUri);
        } catch (Exception e) {
            return false;
        }
    }

    @Override // android.support.v4.provider.DocumentFile
    public boolean exists() {
        return DocumentsContractApi19.exists(this.mContext, this.mUri);
    }

    @Override // android.support.v4.provider.DocumentFile
    public String getName() {
        return DocumentsContractApi19.getName(this.mContext, this.mUri);
    }

    @Override // android.support.v4.provider.DocumentFile
    public String getType() {
        return DocumentsContractApi19.getType(this.mContext, this.mUri);
    }

    @Override // android.support.v4.provider.DocumentFile
    public Uri getUri() {
        return this.mUri;
    }

    @Override // android.support.v4.provider.DocumentFile
    public boolean isDirectory() {
        return DocumentsContractApi19.isDirectory(this.mContext, this.mUri);
    }

    @Override // android.support.v4.provider.DocumentFile
    public boolean isFile() {
        return DocumentsContractApi19.isFile(this.mContext, this.mUri);
    }

    @Override // android.support.v4.provider.DocumentFile
    public boolean isVirtual() {
        return DocumentsContractApi19.isVirtual(this.mContext, this.mUri);
    }

    @Override // android.support.v4.provider.DocumentFile
    public long lastModified() {
        return DocumentsContractApi19.lastModified(this.mContext, this.mUri);
    }

    @Override // android.support.v4.provider.DocumentFile
    public long length() {
        return DocumentsContractApi19.length(this.mContext, this.mUri);
    }

    @Override // android.support.v4.provider.DocumentFile
    public DocumentFile[] listFiles() throws Exception {
        Cursor cursor;
        ContentResolver contentResolver = this.mContext.getContentResolver();
        Uri uri = this.mUri;
        Uri uriBuildChildDocumentsUriUsingTree = DocumentsContract.buildChildDocumentsUriUsingTree(uri, DocumentsContract.getDocumentId(uri));
        ArrayList arrayList = new ArrayList();
        Cursor cursor2 = null;
        Cursor cursor3 = null;
        try {
            try {
                Cursor cursorQuery = contentResolver.query(uriBuildChildDocumentsUriUsingTree, new String[]{"document_id"}, null, null, null);
                while (true) {
                    cursor = cursorQuery;
                    cursor3 = cursorQuery;
                    cursor2 = cursorQuery;
                    if (!cursorQuery.moveToNext()) {
                        break;
                    }
                    arrayList.add(DocumentsContract.buildDocumentUriUsingTree(this.mUri, cursorQuery.getString(0)));
                }
            } catch (Exception e) {
                StringBuilder sb = new StringBuilder();
                sb.append("Failed query: ");
                sb.append(e);
                Log.w("DocumentFile", sb.toString());
                cursor = cursor2;
            }
            Uri[] uriArr = (Uri[]) arrayList.toArray(new Uri[arrayList.size()]);
            DocumentFile[] documentFileArr = new DocumentFile[uriArr.length];
            for (int i = 0; i < uriArr.length; i++) {
                documentFileArr[i] = new TreeDocumentFile(this, this.mContext, uriArr[i]);
            }
            return documentFileArr;
        } finally {
            closeQuietly(cursor3);
        }
    }

    @Override // android.support.v4.provider.DocumentFile
    public boolean renameTo(String str) throws FileNotFoundException {
        try {
            Uri uriRenameDocument = DocumentsContract.renameDocument(this.mContext.getContentResolver(), this.mUri, str);
            if (uriRenameDocument == null) {
                return false;
            }
            this.mUri = uriRenameDocument;
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
