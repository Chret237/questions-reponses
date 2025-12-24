package com.squareup.picasso;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import com.squareup.picasso.Picasso;

/* loaded from: classes-dex2jar.jar:com/squareup/picasso/Target.class */
public interface Target {
    void onBitmapFailed(Drawable drawable);

    void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom);

    void onPrepareLoad(Drawable drawable);
}
