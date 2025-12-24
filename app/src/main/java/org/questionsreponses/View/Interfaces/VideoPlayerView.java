package org.questionsreponses.View.Interfaces;

import android.content.Context;
import org.questionsreponses.Model.Ressource;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Interfaces/VideoPlayerView.class */
public class VideoPlayerView {

    /* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Interfaces/VideoPlayerView$IPresenter.class */
    public interface IPresenter {
    }

    /* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Interfaces/VideoPlayerView$IVideoPlayer.class */
    public interface IVideoPlayer {
        void addQRVideoToFavorite(Context context);

        void btnNavigationVisibility(int i);

        void closeActivity();

        void displayPlayer(Ressource ressource, int i, int i2);

        void downloadQRVideo(Context context);

        void events();

        void fabVisibility(int i);

        void headerVisibility(int i);

        void hideHeader();

        void initialize();

        void pauseNotificationAudio();

        void playNextQRVideo();

        void playPreviousQRVideo();

        void progressBarVisibility(int i);

        void shareQRVideo(Context context);

        void showPlayerWidgetsOnTouch();
    }
}
