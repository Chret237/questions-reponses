package com.squareup.picasso;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestHandler;
import java.io.IOException;

/* loaded from: classes-dex2jar.jar:com/squareup/picasso/MediaStoreRequestHandler.class */
class MediaStoreRequestHandler extends ContentStreamRequestHandler {
    private static final String[] CONTENT_ORIENTATION = {"orientation"};

    /* loaded from: classes-dex2jar.jar:com/squareup/picasso/MediaStoreRequestHandler$PicassoKind.class */
    enum PicassoKind {
        MICRO(3, 96, 96),
        MINI(1, 512, 384),
        FULL(2, -1, -1);

        final int androidKind;
        final int height;
        final int width;

        PicassoKind(int i, int i2, int i3) {
            this.androidKind = i;
            this.width = i2;
            this.height = i3;
        }
    }

    MediaStoreRequestHandler(Context context) {
        super(context);
    }

    static int getExifOrientation(ContentResolver contentResolver, Uri uri) {
        Cursor cursor = null;
        Cursor cursor2 = null;
        try {
            Cursor cursorQuery = contentResolver.query(uri, CONTENT_ORIENTATION, null, null, null);
            if (cursorQuery != null && cursorQuery.moveToFirst()) {
                cursor2 = cursorQuery;
                cursor = cursorQuery;
                int i = cursorQuery.getInt(0);
                if (cursorQuery != null) {
                    cursorQuery.close();
                }
                return i;
            }
            if (cursorQuery == null) {
                return 0;
            }
            cursorQuery.close();
            return 0;
        } catch (RuntimeException e) {
            if (cursor == null) {
                return 0;
            }
            cursor.close();
            return 0;
        } catch (Throwable th) {
            if (cursor2 != null) {
                cursor2.close();
            }
            throw th;
        }
    }

    static PicassoKind getPicassoKind(int i, int i2) {
        return (i > PicassoKind.MICRO.width || i2 > PicassoKind.MICRO.height) ? (i > PicassoKind.MINI.width || i2 > PicassoKind.MINI.height) ? PicassoKind.FULL : PicassoKind.MINI : PicassoKind.MICRO;
    }

    @Override // com.squareup.picasso.ContentStreamRequestHandler, com.squareup.picasso.RequestHandler
    public boolean canHandleRequest(Request request) {
        Uri uri = request.uri;
        return "content".equals(uri.getScheme()) && "media".equals(uri.getAuthority());
    }

    @Override // com.squareup.picasso.ContentStreamRequestHandler, com.squareup.picasso.RequestHandler
    public RequestHandler.Result load(Request request, int i) throws IOException {
        Bitmap thumbnail;
        ContentResolver contentResolver = this.context.getContentResolver();
        int exifOrientation = getExifOrientation(contentResolver, request.uri);
        String type = contentResolver.getType(request.uri);
        boolean z = type != null && type.startsWith("video/");
        if (request.hasSize()) {
            PicassoKind picassoKind = getPicassoKind(request.targetWidth, request.targetHeight);
            if (!z && picassoKind == PicassoKind.FULL) {
                return new RequestHandler.Result(null, getInputStream(request), Picasso.LoadedFrom.DISK, exifOrientation);
            }
            long id = ContentUris.parseId(request.uri);
            BitmapFactory.Options optionsCreateBitmapOptions = createBitmapOptions(request);
            optionsCreateBitmapOptions.inJustDecodeBounds = true;
            calculateInSampleSize(request.targetWidth, request.targetHeight, picassoKind.width, picassoKind.height, optionsCreateBitmapOptions, request);
            if (z) {
                thumbnail = MediaStore.Video.Thumbnails.getThumbnail(contentResolver, id, picassoKind == PicassoKind.FULL ? 1 : picassoKind.androidKind, optionsCreateBitmapOptions);
            } else {
                thumbnail = MediaStore.Images.Thumbnails.getThumbnail(contentResolver, id, picassoKind.androidKind, optionsCreateBitmapOptions);
            }
            if (thumbnail != null) {
                return new RequestHandler.Result(thumbnail, null, Picasso.LoadedFrom.DISK, exifOrientation);
            }
        }
        return new RequestHandler.Result(null, getInputStream(request), Picasso.LoadedFrom.DISK, exifOrientation);
    }
}
