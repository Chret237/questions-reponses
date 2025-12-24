package org.questionsreponses.View.Services;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Services/App.class */
public class App extends Application {
    public static final String CHANNEL_ID = "app_questions_reponses";
    public static final String CHANNEL_NAME = "appQR";

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            ((NotificationManager) getSystemService(NotificationManager.class)).createNotificationChannel(new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, 1));
        }
    }

    @Override // android.app.Application
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }
}
