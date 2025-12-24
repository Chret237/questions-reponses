package com.squareup.picasso;

/* loaded from: classes-dex2jar.jar:com/squareup/picasso/Callback.class */
public interface Callback {

    /* loaded from: classes-dex2jar.jar:com/squareup/picasso/Callback$EmptyCallback.class */
    public static class EmptyCallback implements Callback {
        @Override // com.squareup.picasso.Callback
        public void onError() {
        }

        @Override // com.squareup.picasso.Callback
        public void onSuccess() {
        }
    }

    void onError();

    void onSuccess();
}
