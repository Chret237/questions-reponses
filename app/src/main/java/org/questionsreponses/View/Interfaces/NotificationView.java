package org.questionsreponses.View.Interfaces;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import org.questionsreponses.C0598R;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Interfaces/NotificationView.class */
public class NotificationView {

    /* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Interfaces/NotificationView$ACTION.class */
    public interface ACTION {
        public static final String INIT_ACTION = "org.questionsresponses.action.init";
        public static final String MAIN_ACTION = "org.questionsresponses.action.main";
        public static final String NEXT_ACTION = "org.questionsresponses.action.next";
        public static final String PAUSE_ACTION = "org.levraievangile.action.pause";
        public static final String PLAY_ACTION = "org.questionsresponses.action.play";
        public static final String PREVIOUS_ACTION = "org.questionsresponses.action.previous";
        public static final String STARTFOREGROUND_ACTION = "org.questionsresponses.action.startforeground";
        public static final String STOPFOREGROUND_ACTION = "org.questionsresponses.action.stopforeground";
    }

    /* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Interfaces/NotificationView$NOTIFICATION_ID.class */
    public interface NOTIFICATION_ID {
        public static final int FOREGROUND_SERVICE = 125;
    }

    public static Bitmap getDefaultAlbumArt(Context context) {
        Bitmap bitmapDecodeResource;
        try {
            bitmapDecodeResource = BitmapFactory.decodeResource(context.getResources(), C0598R.mipmap.logo, new BitmapFactory.Options());
        } catch (Error | Exception e) {
            bitmapDecodeResource = null;
        }
        return bitmapDecodeResource;
    }
}
