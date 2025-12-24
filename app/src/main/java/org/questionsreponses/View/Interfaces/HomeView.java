package org.questionsreponses.View.Interfaces;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;
import java.util.ArrayList;
import java.util.List;
import org.questionsreponses.Model.Quizz;
import org.questionsreponses.Model.Ressource;
import org.questionsreponses.Model.Survey;
import retrofit2.Call;
import retrofit2.http.GET;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Interfaces/HomeView.class */
public class HomeView {

    /* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Interfaces/HomeView$IApiRessource.class */
    public interface IApiRessource {
        @GET("webservice/audios/questions-reponses/")
        Call<List<Ressource>> getAllAudios();

        @GET("webservice/liste/quizz/")
        Call<List<Quizz>> getAllQuizz();

        @GET("webservice/videos/questions-reponses/")
        Call<List<Ressource>> getAllVideos();
    }

    /* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Interfaces/HomeView$IAudioFrag.class */
    public interface IAudioFrag {
        void events();

        void hideProgressBar();

        void initialize();

        void initializeAllAudios(List<Ressource> list);

        void loadAudioData(List<Ressource> list, int i, int i2);

        List<Ressource> retrieveAllAudiosList();

        void showProgressBar();
    }

    /* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Interfaces/HomeView$IAudioRecycler.class */
    public interface IAudioRecycler {
        void addQRAudioToFavorite(Context context);

        void downloadQRAudio(Context context);

        void playNextQRAudio();

        void playPreviousQRAudio();

        void shareQRAudio(Context context);
    }

    /* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Interfaces/HomeView$IHome.class */
    public interface IHome {
        void askPermissionToSaveFile();

        void audioPlayerVisibility(int i);

        void contactUs(View view);

        void continueBackPressedAction();

        void deleteAllSelectedQuizz();

        void deleteQuizzSavedVisibility(int i);

        void displaySettings();

        void enableAllLevelMenu(Boolean[] boolArr);

        void enableAudioMediaPlayer(boolean z);

        void events();

        void fabSubMenuVisibility(int i);

        void initialize();

        void initializeAllQuizzData(ArrayList<Quizz> arrayList);

        void initializeResourceList(String str, List<Ressource> list);

        void launchQuizzGameThatNotFinished(String str, String str2, String str3);

        void launchQuizzGameToPlay(String str, String str2);

        void loadAudioDataToPlay(Ressource ressource);

        void loadQuizzSave(List<Survey> list, int i);

        void modifyTitleOfSaveQuizzGame(String str, int i);

        void onAudioSelected(Ressource ressource, int i);

        void onHomeIAudioRecycler(IAudioRecycler iAudioRecycler);

        void onMenuQuizzSelected(String str, int i);

        void openOrCloseNavigationDrawer();

        void parameter(View view);

        void playNotification();

        void playerProgressBarVisibility(int i);

        void privacyPolicy(View view);

        void progressBarVisibility(int i);

        void readTextFromTextToSpeech(String str);

        ArrayList<Quizz> retrieveAllQuizzData();

        List<Ressource> retrieveResourceList(String str);

        void searchMedia(View view);

        void setIQuizzSaveRecyclerReference(IQuizzSaveRecycler iQuizzSaveRecycler);

        void shareApp(View view);

        void showQuizzSpiritualGrowth(int i);

        void stopNotification();

        void stopOtherMediaPlayerSound(MediaPlayer mediaPlayer, Ressource ressource);

        void textMediaPlayInfoLoading();

        void updateApplication(View view);

        void youtubeDownloader(View view);
    }

    /* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Interfaces/HomeView$IPresenter.class */
    public interface IPresenter {
    }

    /* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Interfaces/HomeView$IQuizzFrag.class */
    public interface IQuizzFrag {
        void events();

        void initialize();

        void loadQuizzMenu(List<String> list, int i, int i2);

        ArrayList<Quizz> retrieveAllQuizzData();
    }

    /* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Interfaces/HomeView$IQuizzSaveRecycler.class */
    public interface IQuizzSaveRecycler {
        void deleteAllCheckedQuizz(Context context);
    }

    /* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Interfaces/HomeView$IVideoFrag.class */
    public interface IVideoFrag {
        void events();

        void hideProgressBar();

        void initialize();

        void initializeAllVideos(List<Ressource> list);

        void launchVideoToPlay(Ressource ressource, int i);

        void loadVideoData(List<Ressource> list, int i, int i2);

        void onFragmentVideoIVideoRecycler(IVideoRecycler iVideoRecycler);

        List<Ressource> retrieveAllVideosList();

        void scrollVideoDataToPosition(int i);

        void showProgressBar();
    }

    /* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Interfaces/HomeView$IVideoRecycler.class */
    public interface IVideoRecycler {
        void playNextQRVideo();

        void playPreviousQRVideo();
    }
}
