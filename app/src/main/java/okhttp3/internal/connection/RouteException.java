package okhttp3.internal.connection;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* loaded from: classes-dex2jar.jar:okhttp3/internal/connection/RouteException.class */
public final class RouteException extends RuntimeException {
    private static final Method addSuppressedExceptionMethod;
    private IOException lastException;

    static {
        Method declaredMethod;
        try {
            declaredMethod = Throwable.class.getDeclaredMethod("addSuppressed", Throwable.class);
        } catch (Exception e) {
            declaredMethod = null;
        }
        addSuppressedExceptionMethod = declaredMethod;
    }

    public RouteException(IOException iOException) {
        super(iOException);
        this.lastException = iOException;
    }

    private void addSuppressedIfPossible(IOException iOException, IOException iOException2) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Method method = addSuppressedExceptionMethod;
        if (method != null) {
            try {
                method.invoke(iOException, iOException2);
            } catch (IllegalAccessException | InvocationTargetException e) {
            }
        }
    }

    public void addConnectException(IOException iOException) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        addSuppressedIfPossible(iOException, this.lastException);
        this.lastException = iOException;
    }

    public IOException getLastConnectException() {
        return this.lastException;
    }
}
