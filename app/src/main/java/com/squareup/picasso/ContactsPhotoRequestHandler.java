package com.squareup.picasso;

import android.content.ContentResolver;
import android.content.Context;
import android.content.UriMatcher;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestHandler;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes-dex2jar.jar:com/squareup/picasso/ContactsPhotoRequestHandler.class */
class ContactsPhotoRequestHandler extends RequestHandler {
    private static final int ID_CONTACT = 3;
    private static final int ID_DISPLAY_PHOTO = 4;
    private static final int ID_LOOKUP = 1;
    private static final int ID_THUMBNAIL = 2;
    private static final UriMatcher matcher;
    private final Context context;

    /* loaded from: classes-dex2jar.jar:com/squareup/picasso/ContactsPhotoRequestHandler$ContactPhotoStreamIcs.class */
    private static class ContactPhotoStreamIcs {
        private ContactPhotoStreamIcs() {
        }

        static InputStream get(ContentResolver contentResolver, Uri uri) {
            return ContactsContract.Contacts.openContactPhotoInputStream(contentResolver, uri, true);
        }
    }

    static {
        UriMatcher uriMatcher = new UriMatcher(-1);
        matcher = uriMatcher;
        uriMatcher.addURI("com.android.contacts", "contacts/lookup/*/#", 1);
        matcher.addURI("com.android.contacts", "contacts/lookup/*", 1);
        matcher.addURI("com.android.contacts", "contacts/#/photo", 2);
        matcher.addURI("com.android.contacts", "contacts/#", 3);
        matcher.addURI("com.android.contacts", "display_photo/#", 4);
    }

    ContactsPhotoRequestHandler(Context context) {
        this.context = context;
    }

    private InputStream getInputStream(Request request) throws IOException {
        Uri uri;
        ContentResolver contentResolver = this.context.getContentResolver();
        Uri uri2 = request.uri;
        int iMatch = matcher.match(uri2);
        if (iMatch != 1) {
            if (iMatch != 2) {
                uri = uri2;
                if (iMatch != 3) {
                    if (iMatch != 4) {
                        throw new IllegalStateException("Invalid uri: " + uri2);
                    }
                }
            }
            return contentResolver.openInputStream(uri2);
        }
        Uri uriLookupContact = ContactsContract.Contacts.lookupContact(contentResolver, uri2);
        uri = uriLookupContact;
        if (uriLookupContact == null) {
            return null;
        }
        return Build.VERSION.SDK_INT < 14 ? ContactsContract.Contacts.openContactPhotoInputStream(contentResolver, uri) : ContactPhotoStreamIcs.get(contentResolver, uri);
    }

    @Override // com.squareup.picasso.RequestHandler
    public boolean canHandleRequest(Request request) {
        Uri uri = request.uri;
        return "content".equals(uri.getScheme()) && ContactsContract.Contacts.CONTENT_URI.getHost().equals(uri.getHost()) && matcher.match(request.uri) != -1;
    }

    @Override // com.squareup.picasso.RequestHandler
    public RequestHandler.Result load(Request request, int i) throws IOException {
        InputStream inputStream = getInputStream(request);
        return inputStream != null ? new RequestHandler.Result(inputStream, Picasso.LoadedFrom.DISK) : null;
    }
}
