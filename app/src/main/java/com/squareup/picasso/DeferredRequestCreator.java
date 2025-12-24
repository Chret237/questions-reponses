package com.squareup.picasso;

import android.view.ViewTreeObserver;
import android.widget.ImageView;
import java.lang.ref.WeakReference;

/* loaded from: classes-dex2jar.jar:com/squareup/picasso/DeferredRequestCreator.class */
class DeferredRequestCreator implements ViewTreeObserver.OnPreDrawListener {
    Callback callback;
    final RequestCreator creator;
    final WeakReference<ImageView> target;

    DeferredRequestCreator(RequestCreator requestCreator, ImageView imageView) {
        this(requestCreator, imageView, null);
    }

    DeferredRequestCreator(RequestCreator requestCreator, ImageView imageView, Callback callback) {
        this.creator = requestCreator;
        this.target = new WeakReference<>(imageView);
        this.callback = callback;
        imageView.getViewTreeObserver().addOnPreDrawListener(this);
    }

    void cancel() {
        this.callback = null;
        ImageView imageView = this.target.get();
        if (imageView == null) {
            return;
        }
        ViewTreeObserver viewTreeObserver = imageView.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.removeOnPreDrawListener(this);
        }
    }

    @Override // android.view.ViewTreeObserver.OnPreDrawListener
    public boolean onPreDraw() {
        ImageView imageView = this.target.get();
        if (imageView == null) {
            return true;
        }
        ViewTreeObserver viewTreeObserver = imageView.getViewTreeObserver();
        if (!viewTreeObserver.isAlive()) {
            return true;
        }
        int width = imageView.getWidth();
        int height = imageView.getHeight();
        if (width <= 0 || height <= 0) {
            return true;
        }
        viewTreeObserver.removeOnPreDrawListener(this);
        this.creator.unfit().resize(width, height).into(imageView, this.callback);
        return true;
    }
}
