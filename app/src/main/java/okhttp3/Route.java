package okhttp3;

import java.net.InetSocketAddress;
import java.net.Proxy;

/* loaded from: classes-dex2jar.jar:okhttp3/Route.class */
public final class Route {
    final Address address;
    final InetSocketAddress inetSocketAddress;
    final Proxy proxy;

    public Route(Address address, Proxy proxy, InetSocketAddress inetSocketAddress) {
        if (address == null) {
            throw new NullPointerException("address == null");
        }
        if (proxy == null) {
            throw new NullPointerException("proxy == null");
        }
        if (inetSocketAddress == null) {
            throw new NullPointerException("inetSocketAddress == null");
        }
        this.address = address;
        this.proxy = proxy;
        this.inetSocketAddress = inetSocketAddress;
    }

    public Address address() {
        return this.address;
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x003b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean equals(@javax.annotation.Nullable java.lang.Object r4) {
        /*
            r3 = this;
            r0 = r4
            boolean r0 = r0 instanceof okhttp3.Route
            if (r0 == 0) goto L3b
            r0 = r4
            okhttp3.Route r0 = (okhttp3.Route) r0
            r4 = r0
            r0 = r4
            okhttp3.Address r0 = r0.address
            r1 = r3
            okhttp3.Address r1 = r1.address
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L3b
            r0 = r4
            java.net.Proxy r0 = r0.proxy
            r1 = r3
            java.net.Proxy r1 = r1.proxy
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L3b
            r0 = r4
            java.net.InetSocketAddress r0 = r0.inetSocketAddress
            r1 = r3
            java.net.InetSocketAddress r1 = r1.inetSocketAddress
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L3b
            r0 = 1
            r5 = r0
            goto L3d
        L3b:
            r0 = 0
            r5 = r0
        L3d:
            r0 = r5
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.Route.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        return ((((527 + this.address.hashCode()) * 31) + this.proxy.hashCode()) * 31) + this.inetSocketAddress.hashCode();
    }

    public Proxy proxy() {
        return this.proxy;
    }

    public boolean requiresTunnel() {
        return this.address.sslSocketFactory != null && this.proxy.type() == Proxy.Type.HTTP;
    }

    public InetSocketAddress socketAddress() {
        return this.inetSocketAddress;
    }

    public String toString() {
        return "Route{" + this.inetSocketAddress + "}";
    }
}
