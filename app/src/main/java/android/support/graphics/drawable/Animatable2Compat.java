package android.support.graphics.drawable;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.Drawable;

/* loaded from: classes-dex2jar.jar:android/support/graphics/drawable/Animatable2Compat.class */
public interface Animatable2Compat extends Animatable {

    /* loaded from: classes-dex2jar.jar:android/support/graphics/drawable/Animatable2Compat$AnimationCallback.class */
    public static abstract class AnimationCallback {
        Animatable2.AnimationCallback mPlatformCallback;

        Animatable2.AnimationCallback getPlatformCallback() {
            if (this.mPlatformCallback == null) {
                this.mPlatformCallback = new Animatable2.AnimationCallback(this) { // from class: android.support.graphics.drawable.Animatable2Compat.AnimationCallback.1
                    final AnimationCallback this$0;

                    {
                        this.this$0 = this;
                    }

                    @Override // android.graphics.drawable.Animatable2.AnimationCallback
                    public void onAnimationEnd(Drawable drawable) {
                        this.this$0.onAnimationEnd(drawable);
                    }

                    @Override // android.graphics.drawable.Animatable2.AnimationCallback
                    public void onAnimationStart(Drawable drawable) {
                        this.this$0.onAnimationStart(drawable);
                    }
                };
            }
            return this.mPlatformCallback;
        }

        public void onAnimationEnd(Drawable drawable) {
        }

        public void onAnimationStart(Drawable drawable) {
        }
    }

    void clearAnimationCallbacks();

    void registerAnimationCallback(AnimationCallback animationCallback);

    boolean unregisterAnimationCallback(AnimationCallback animationCallback);
}
