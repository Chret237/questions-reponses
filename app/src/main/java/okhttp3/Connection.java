package okhttp3;

import java.net.Socket;
import javax.annotation.Nullable;

/* loaded from: classes-dex2jar.jar:okhttp3/Connection.class */
public interface Connection {
    @Nullable
    Handshake handshake();

    Protocol protocol();

    Route route();

    Socket socket();
}
