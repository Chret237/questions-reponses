package android.arch.lifecycle;

import java.util.HashMap;
import java.util.Map;

/* loaded from: classes-dex2jar.jar:android/arch/lifecycle/MethodCallsLogger.class */
public class MethodCallsLogger {
    private Map<String, Integer> mCalledMethods = new HashMap();

    public boolean approveCall(String str, int i) {
        Integer num = this.mCalledMethods.get(str);
        boolean z = false;
        int iIntValue = num != null ? num.intValue() : 0;
        if ((iIntValue & i) != 0) {
            z = true;
        }
        this.mCalledMethods.put(str, Integer.valueOf(i | iIntValue));
        return !z;
    }
}
