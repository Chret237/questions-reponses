package android.arch.lifecycle;

import android.app.Application;

/* loaded from: classes-dex2jar.jar:android/arch/lifecycle/AndroidViewModel.class */
public class AndroidViewModel extends ViewModel {
    private Application mApplication;

    public AndroidViewModel(Application application) {
        this.mApplication = application;
    }

    public <T extends Application> T getApplication() {
        return (T) this.mApplication;
    }
}
