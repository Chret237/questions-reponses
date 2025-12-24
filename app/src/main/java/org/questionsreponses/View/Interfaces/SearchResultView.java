package org.questionsreponses.View.Interfaces;

import android.content.Context;
import android.media.MediaPlayer;
import java.util.List;
import org.questionsreponses.Model.Ressource;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Interfaces/SearchResultView.class */
public class SearchResultView {

    /* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Interfaces/SearchResultView$ILoadSearchResult.class */
    public interface ILoadSearchResult {
        void onLoadSearchError();

        void onLoadSearchSuccess(Context context, List<Ressource> list, String str);
    }

    /* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Interfaces/SearchResultView$IPresenter.class */
    public interface IPresenter {
    }

    /* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Interfaces/SearchResultView$ISearchResult.class */
    public interface ISearchResult {
        void askPermissionToSaveFile();

        void audioPlayerVisibility(int i);

        void closeActivity();

        void enableAudioMediaPlayer(boolean z);

        void events();

        void hideHeader();

        void initialize();

        MediaPlayer instanceMediaPlayer();

        void launchNotificationPlayer();

        void launchVideoToPlay(Ressource ressource, int i);

        void loadAudioDataToPlay(Ressource ressource);

        void loadSearchResult(List<Ressource> list, int i, int i2, String str);

        void onActivityISearchResultRecycler(ISearchResultRecycler iSearchResultRecycler);

        void playNotification();

        void playerProgressBarVisibility(int i);

        void progressBarVisibility(int i);

        void scrollResourceDataToPosition(int i);

        void setSubTitleValue(String str);

        void stopNotification();

        void stopOtherMediaPlayerSound(MediaPlayer mediaPlayer, Ressource ressource);

        void textMediaPlayInfoLoading();
    }

    /* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Interfaces/SearchResultView$ISearchResultRecycler.class */
    public interface ISearchResultRecycler {
        void addQRAudioToFavorite(Context context);

        void downloadQRAudio(Context context);

        void playNextQRAudio();

        void playNextQRVideo();

        void playPreviousQRAudio();

        void playPreviousQRVideo();

        void playQRNotifAudioPlayer(Context context);

        void shareQRAudio(Context context);
    }
}
