package android.support.v4.os;

import android.os.Build;
import android.os.Handler;
import android.os.Message;

/* loaded from: classes-dex2jar.jar:android/support/v4/os/HandlerCompat.class */
public final class HandlerCompat {
    private HandlerCompat() {
    }

    public static boolean postDelayed(Handler handler, Runnable runnable, Object obj, long j) {
        if (Build.VERSION.SDK_INT >= 28) {
            return handler.postDelayed(runnable, obj, j);
        }
        Message messageObtain = Message.obtain(handler, runnable);
        messageObtain.obj = obj;
        return handler.sendMessageDelayed(messageObtain, j);
    }
}
