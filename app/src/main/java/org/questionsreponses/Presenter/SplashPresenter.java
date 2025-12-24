package org.questionsreponses.Presenter;

import android.content.Context;
import android.util.Log;
import org.questionsreponses.View.Interfaces.SplashView;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/Presenter/SplashPresenter.class */
public class SplashPresenter {
    private SplashView.ISplash iSplash;

    public SplashPresenter(SplashView.ISplash iSplash) {
        this.iSplash = iSplash;
    }

    public void loadSplashData(Context context) {
        try {
            if (this.iSplash == null || context == null) {
                return;
            }
            this.iSplash.hideHeader();
            this.iSplash.initialize();
            this.iSplash.events();
            this.iSplash.initSharePreferences();
            this.iSplash.displayHome();
            CommonPresenter.removeSomeSharePreferencesFromApp(context);
            CommonPresenter.initializeAppSetting(context);
            CommonPresenter.initializeNotificationTimeLapsed(context);
            CommonPresenter.initializeCategoriesQuizz(context);
        } catch (Exception e) {
            Log.e("TAG_ERROR", "SplashPresenter-->loadSplashData() : " + e.getMessage());
        }
    }
}
