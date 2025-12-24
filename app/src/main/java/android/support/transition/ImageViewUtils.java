package android.support.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Matrix;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* loaded from: classes-dex2jar.jar:android/support/transition/ImageViewUtils.class */
class ImageViewUtils {
    private static final String TAG = "ImageViewUtils";
    private static Method sAnimateTransformMethod;
    private static boolean sAnimateTransformMethodFetched;

    private ImageViewUtils() {
    }

    static void animateTransform(ImageView imageView, Matrix matrix) throws IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        if (Build.VERSION.SDK_INT < 21) {
            imageView.setImageMatrix(matrix);
            return;
        }
        fetchAnimateTransformMethod();
        Method method = sAnimateTransformMethod;
        if (method != null) {
            try {
                method.invoke(imageView, matrix);
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e2) {
                throw new RuntimeException(e2.getCause());
            }
        }
    }

    private static void fetchAnimateTransformMethod() throws NoSuchMethodException, SecurityException {
        if (sAnimateTransformMethodFetched) {
            return;
        }
        try {
            Method declaredMethod = ImageView.class.getDeclaredMethod("animateTransform", Matrix.class);
            sAnimateTransformMethod = declaredMethod;
            declaredMethod.setAccessible(true);
        } catch (NoSuchMethodException e) {
            Log.i(TAG, "Failed to retrieve animateTransform method", e);
        }
        sAnimateTransformMethodFetched = true;
    }

    static void reserveEndAnimateTransform(ImageView imageView, Animator animator) {
        if (Build.VERSION.SDK_INT < 21) {
            animator.addListener(new AnimatorListenerAdapter(imageView) { // from class: android.support.transition.ImageViewUtils.1
                final ImageView val$view;

                {
                    this.val$view = imageView;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator2) {
                    ImageView.ScaleType scaleType = (ImageView.ScaleType) this.val$view.getTag(C0149R.id.save_scale_type);
                    this.val$view.setScaleType(scaleType);
                    this.val$view.setTag(C0149R.id.save_scale_type, null);
                    if (scaleType == ImageView.ScaleType.MATRIX) {
                        ImageView imageView2 = this.val$view;
                        imageView2.setImageMatrix((Matrix) imageView2.getTag(C0149R.id.save_image_matrix));
                        this.val$view.setTag(C0149R.id.save_image_matrix, null);
                    }
                    animator2.removeListener(this);
                }
            });
        }
    }

    static void startAnimateTransform(ImageView imageView) {
        if (Build.VERSION.SDK_INT < 21) {
            ImageView.ScaleType scaleType = imageView.getScaleType();
            imageView.setTag(C0149R.id.save_scale_type, scaleType);
            if (scaleType == ImageView.ScaleType.MATRIX) {
                imageView.setTag(C0149R.id.save_image_matrix, imageView.getImageMatrix());
            } else {
                imageView.setScaleType(ImageView.ScaleType.MATRIX);
            }
            imageView.setImageMatrix(MatrixUtils.IDENTITY_MATRIX);
        }
    }
}
