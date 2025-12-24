package com.squareup.picasso;

import android.graphics.Bitmap;
import com.squareup.picasso.Picasso;

/* loaded from: classes-dex2jar.jar:com/squareup/picasso/GetAction.class */
class GetAction extends Action<Void> {
    GetAction(Picasso picasso, Request request, int i, int i2, Object obj, String str) {
        super(picasso, null, request, i, i2, 0, null, str, obj, false);
    }

    @Override // com.squareup.picasso.Action
    void complete(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
    }

    @Override // com.squareup.picasso.Action
    public void error() {
    }
}
