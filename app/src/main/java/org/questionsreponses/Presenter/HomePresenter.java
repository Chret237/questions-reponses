package org.questionsreponses.Presenter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.github.clans.fab.BuildConfig;
import com.github.kiulian.downloader.model.videos.formats.Format;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import org.json.JSONException;
import org.questionsreponses.C0598R;
import org.questionsreponses.Model.ApiClient;
import org.questionsreponses.Model.DAORessource;
import org.questionsreponses.Model.DAOSurvey;
import org.questionsreponses.Model.LoadAudioMediaPlayer;
import org.questionsreponses.Model.Quizz;
import org.questionsreponses.Model.Ressource;
import org.questionsreponses.Model.Setting;
import org.questionsreponses.Model.Survey;
import org.questionsreponses.View.Activities.DownloaderActivity;
import org.questionsreponses.View.Activities.PrivacyPolicyActivity;
import org.questionsreponses.View.Interfaces.HomeView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/Presenter/HomePresenter.class */
public class HomePresenter {
    private CountDownTimer downTimer;
    private HomeView.IApiRessource iApiRessource;
    private HomeView.IAudioFrag iAudioFrag;
    private HomeView.IHome iHome;
    private HomeView.IQuizzFrag iQuizzFrag;
    private HomeView.IVideoFrag iVideoFrag;
    private LaunchQuizzGameAsyntask launchQuizzGameAsyntask;
    private LoadAudioMediaPlayer loadAudioMediaPlayer;
    private int maxQuizz;
    private Quizz quizzValue;
    private int totalErreur;
    private int totalTrouve;

    public HomePresenter(HomeView.IAudioFrag iAudioFrag) {
        this.iAudioFrag = iAudioFrag;
    }

    public HomePresenter(HomeView.IHome iHome) {
        this.iHome = iHome;
    }

    public HomePresenter(HomeView.IHome iHome, HomeView.IQuizzFrag iQuizzFrag) {
        this.iHome = iHome;
        this.iQuizzFrag = iQuizzFrag;
    }

    public HomePresenter(HomeView.IQuizzFrag iQuizzFrag) {
        this.iQuizzFrag = iQuizzFrag;
    }

    public HomePresenter(HomeView.IVideoFrag iVideoFrag) {
        this.iVideoFrag = iVideoFrag;
    }

    private void closeAudioMediaPlayer(MediaPlayer mediaPlayer) throws IllegalStateException {
        if (mediaPlayer != null) {
            try {
                if (this.iHome != null) {
                    this.iHome.audioPlayerVisibility(8);
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                    }
                }
            } catch (Exception e) {
                Log.e("TAG_ERROR", "HomePresenter-->closeAudioMediaPlayer() : " + e.getMessage());
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x003a, code lost:
    
        r0.setAccessible(true);
        r0 = r0.get(r0);
        java.lang.Class.forName(r0.getClass().getName()).getMethod("setForceShowIcon", java.lang.Boolean.TYPE).invoke(r0, true);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void showContactMenu(android.view.View r8) throws java.lang.IllegalAccessException, java.lang.IllegalArgumentException, java.lang.reflect.InvocationTargetException {
        /*
            r7 = this;
            r0 = r8
            android.content.Context r0 = r0.getContext()
            r11 = r0
            android.support.v7.widget.PopupMenu r0 = new android.support.v7.widget.PopupMenu
            r1 = r0
            r2 = r8
            android.content.Context r2 = r2.getContext()
            r3 = r8
            r1.<init>(r2, r3)
            r8 = r0
            r0 = r8
            java.lang.Class r0 = r0.getClass()     // Catch: java.lang.Exception -> L7c
            java.lang.reflect.Field[] r0 = r0.getDeclaredFields()     // Catch: java.lang.Exception -> L7c
            r13 = r0
            r0 = r13
            int r0 = r0.length     // Catch: java.lang.Exception -> L7c
            r10 = r0
            r0 = 0
            r9 = r0
        L22:
            r0 = r9
            r1 = r10
            if (r0 >= r1) goto L83
            r0 = r13
            r1 = r9
            r0 = r0[r1]
            r12 = r0
            java.lang.String r0 = "mPopup"
            r1 = r12
            java.lang.String r1 = r1.getName()     // Catch: java.lang.Exception -> L7c
            boolean r0 = r0.equals(r1)     // Catch: java.lang.Exception -> L7c
            if (r0 == 0) goto L76
            r0 = r12
            r1 = 1
            r0.setAccessible(r1)     // Catch: java.lang.Exception -> L7c
            r0 = r12
            r1 = r8
            java.lang.Object r0 = r0.get(r1)     // Catch: java.lang.Exception -> L7c
            r12 = r0
            r0 = r12
            java.lang.Class r0 = r0.getClass()     // Catch: java.lang.Exception -> L7c
            java.lang.String r0 = r0.getName()     // Catch: java.lang.Exception -> L7c
            java.lang.Class r0 = java.lang.Class.forName(r0)     // Catch: java.lang.Exception -> L7c
            java.lang.String r1 = "setForceShowIcon"
            r2 = 1
            java.lang.Class[] r2 = new java.lang.Class[r2]     // Catch: java.lang.Exception -> L7c
            r3 = r2
            r4 = 0
            java.lang.Class r5 = java.lang.Boolean.TYPE     // Catch: java.lang.Exception -> L7c
            r3[r4] = r5     // Catch: java.lang.Exception -> L7c
            java.lang.reflect.Method r0 = r0.getMethod(r1, r2)     // Catch: java.lang.Exception -> L7c
            r1 = r12
            r2 = 1
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch: java.lang.Exception -> L7c
            r3 = r2
            r4 = 0
            r5 = 1
            java.lang.Boolean r5 = java.lang.Boolean.valueOf(r5)     // Catch: java.lang.Exception -> L7c
            r3[r4] = r5     // Catch: java.lang.Exception -> L7c
            java.lang.Object r0 = r0.invoke(r1, r2)     // Catch: java.lang.Exception -> L7c
            goto L83
        L76:
            int r9 = r9 + 1
            goto L22
        L7c:
            r12 = move-exception
            r0 = r12
            r0.printStackTrace()
        L83:
            r0 = r11
            r1 = 2131689675(0x7f0f00cb, float:1.9008372E38)
            java.lang.String r0 = r0.getString(r1)
            r12 = r0
            r0 = r8
            android.view.MenuInflater r0 = r0.getMenuInflater()
            r1 = 2131558400(0x7f0d0000, float:1.8742115E38)
            r2 = r8
            android.view.Menu r2 = r2.getMenu()
            r0.inflate(r1, r2)
            r0 = r8
            org.questionsreponses.Presenter.HomePresenter$5 r1 = new org.questionsreponses.Presenter.HomePresenter$5
            r2 = r1
            r3 = r7
            r4 = r11
            r5 = r12
            r2.<init>(r3, r4, r5)
            r0.setOnMenuItemClickListener(r1)
            r0 = r8
            r0.show()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.questionsreponses.Presenter.HomePresenter.showContactMenu(android.view.View):void");
    }

    public void addQRAudioToFavorite(Context context, HomeView.IAudioRecycler iAudioRecycler) {
        if (iAudioRecycler != null) {
            try {
                iAudioRecycler.addQRAudioToFavorite(context);
            } catch (Exception e) {
                Log.e("TAG_ERROR", "HomePresenter-->addQRAudioToFavorite() : " + e.getMessage());
            }
        }
    }

    public void buildTextViewToHtmlData(TextView textView, String str) {
        CommonPresenter.buildTextViewToHtmlData(textView, str);
    }

    public void cancelAsyntask() {
        try {
            if (this.loadAudioMediaPlayer != null) {
                this.loadAudioMediaPlayer.cancel(true);
            }
            if (this.launchQuizzGameAsyntask != null) {
                this.launchQuizzGameAsyntask.cancel(true);
            }
            if (this.downTimer != null) {
                this.downTimer.cancel();
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "HomePresenter-->cancelAsyntask() : " + e.getMessage());
        }
    }

    public void closeNavigationDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout != null) {
            try {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
            } catch (Exception e) {
                Log.e("TAG_ERROR", "HomePresenter-->closeNavigationDrawer() : " + e.getMessage());
            }
        }
    }

    public void closeQRApplication() {
        try {
            if (this.iHome != null) {
                this.iHome.continueBackPressedAction();
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "HomePresenter-->closeQRApplication() : " + e.getMessage());
        }
    }

    public void confirmBeforeClosingApp(Context context) {
        try {
            if (CommonPresenter.getSettingObjectFromSharePreferences(context, CommonPresenter.KEY_SETTING_CONFIRM_BEFORE_QUIT_APP).getChoice()) {
                CommonPresenter.showConfirmMessage(context, context.getResources().getString(C0598R.string.lb_title_confirm), context.getResources().getString(C0598R.string.lb_msg_confirm), this.iHome);
            } else {
                CommonPresenter.removeSomeSharePreferencesFromApp(context);
                if (this.iHome != null) {
                    this.iHome.continueBackPressedAction();
                }
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "HomePresenter-->confirmBeforeClosingApp() : " + e.getMessage());
        }
    }

    public void deleteAllSelectedQuizz() {
        HomeView.IHome iHome = this.iHome;
        if (iHome != null) {
            iHome.deleteAllSelectedQuizz();
        }
    }

    public void downloadQRAudio(Context context, HomeView.IAudioRecycler iAudioRecycler) {
        if (iAudioRecycler != null) {
            try {
                iAudioRecycler.downloadQRAudio(context);
            } catch (Exception e) {
                Log.e("TAG_ERROR", "HomePresenter-->downloadQRAudio() : " + e.getMessage());
            }
        }
    }

    public void initializeQuizzValues(int i, int i2, int i3) {
        this.maxQuizz = i;
        this.totalTrouve = i2;
        this.totalErreur = i3;
    }

    public void initializeTextToSpeech(int i, TextToSpeech textToSpeech) {
        if (i == 0) {
            try {
                int language = textToSpeech.setLanguage(Locale.FRANCE);
                if (language == -1 || language == -2 || this.iHome == null) {
                    return;
                }
                this.iHome.readTextFromTextToSpeech(BuildConfig.FLAVOR);
            } catch (Exception e) {
                Log.e("TAG_ERROR", "HomePresenter-->initializeTextToSpeech() : " + e.getMessage());
            }
        }
    }

    public void loadAudioData(Context context) {
        try {
            if (this.iAudioFrag != null) {
                this.iAudioFrag.initialize();
                this.iAudioFrag.events();
                this.iAudioFrag.showProgressBar();
                List<Ressource> listRetrieveAllAudiosList = this.iAudioFrag.retrieveAllAudiosList();
                if (listRetrieveAllAudiosList == null || listRetrieveAllAudiosList.size() <= 0) {
                    Setting settingObjectFromSharePreferences = CommonPresenter.getSettingObjectFromSharePreferences(context, CommonPresenter.KEY_SETTING_OFFLINE_MODE);
                    if (!CommonPresenter.isMobileConnected(context) || settingObjectFromSharePreferences.getChoice()) {
                        List<Ressource> allResource = CommonPresenter.getAllResource(context, Format.AUDIO);
                        this.iAudioFrag.loadAudioData(allResource, 1, 0);
                        this.iAudioFrag.hideProgressBar();
                        this.iAudioFrag.initializeAllAudios(allResource);
                        CommonPresenter.saveDataInSharePreferences(context, "VALUE_TOTAL_AUDIO_RETRIEVE_FROM_LVE_SERVER", BuildConfig.FLAVOR + allResource.size());
                    } else {
                        HomeView.IApiRessource iApiRessource = (HomeView.IApiRessource) ApiClient.getApiClientLeVraiEvangile(context).create(HomeView.IApiRessource.class);
                        this.iApiRessource = iApiRessource;
                        iApiRessource.getAllAudios().enqueue(new Callback<List<Ressource>>(this, context) { // from class: org.questionsreponses.Presenter.HomePresenter.3
                            final HomePresenter this$0;
                            final Context val$context;

                            {
                                this.this$0 = this;
                                this.val$context = context;
                            }

                            @Override // retrofit2.Callback
                            public void onFailure(Call<List<Ressource>> call, Throwable th) throws JSONException {
                                List<Ressource> allResource2 = CommonPresenter.getAllResource(this.val$context, Format.AUDIO);
                                this.this$0.iAudioFrag.loadAudioData(allResource2, 1, 0);
                                this.this$0.iAudioFrag.hideProgressBar();
                                CommonPresenter.saveDataInSharePreferences(this.val$context, "VALUE_TOTAL_AUDIO_RETRIEVE_FROM_LVE_SERVER", BuildConfig.FLAVOR + allResource2.size());
                            }

                            @Override // retrofit2.Callback
                            public void onResponse(Call<List<Ressource>> call, Response<List<Ressource>> response) {
                                List<Ressource> listBody = response.body();
                                CommonPresenter.saveDataInSharePreferences(this.val$context, CommonPresenter.KEY_AUDIO_RETRIEVE_FROM_LVE_SERVER, listBody != null ? listBody.toString() : BuildConfig.FLAVOR);
                                this.this$0.iAudioFrag.loadAudioData(listBody, 1, 0);
                                this.this$0.iAudioFrag.hideProgressBar();
                                this.this$0.iAudioFrag.initializeAllAudios(listBody);
                                CommonPresenter.saveDataInSharePreferences(this.val$context, "VALUE_TOTAL_AUDIO_RETRIEVE_FROM_LVE_SERVER", BuildConfig.FLAVOR + listBody.size());
                            }
                        });
                    }
                } else {
                    this.iAudioFrag.loadAudioData(listRetrieveAllAudiosList, 1, 0);
                    this.iAudioFrag.hideProgressBar();
                }
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "HomePresenter-->loadAudioData() : " + e.getMessage());
        }
    }

    /* JADX WARN: Type inference failed for: r0v27, types: [org.questionsreponses.Presenter.HomePresenter$2] */
    public void loadHomeData(Context context) {
        try {
            if (this.iHome != null) {
                this.iHome.initialize();
                this.iHome.events();
                this.iHome.askPermissionToSaveFile();
                for (int i = 0; i < CommonPresenter.getFolderName().length; i++) {
                    CommonPresenter.createFolder(context, CommonPresenter.getFolderName()[i]);
                }
                showQuizzFinished(context, false);
                Setting settingObjectFromSharePreferences = CommonPresenter.getSettingObjectFromSharePreferences(context, CommonPresenter.KEY_SETTING_OFFLINE_MODE);
                if (CommonPresenter.isMobileConnected(context) && !settingObjectFromSharePreferences.getChoice()) {
                    HomeView.IApiRessource iApiRessource = (HomeView.IApiRessource) ApiClient.getApiClientQuestionsReponses(context).create(HomeView.IApiRessource.class);
                    this.iApiRessource = iApiRessource;
                    iApiRessource.getAllQuizz().enqueue(new Callback<List<Quizz>>(this) { // from class: org.questionsreponses.Presenter.HomePresenter.1
                        final HomePresenter this$0;

                        {
                            this.this$0 = this;
                        }

                        @Override // retrofit2.Callback
                        public void onFailure(Call<List<Quizz>> call, Throwable th) {
                        }

                        @Override // retrofit2.Callback
                        public void onResponse(Call<List<Quizz>> call, Response<List<Quizz>> response) {
                            this.this$0.iHome.initializeAllQuizzData((ArrayList) response.body());
                        }
                    });
                }
                String dataFromSharePreferences = CommonPresenter.getDataFromSharePreferences(context, CommonPresenter.KEY_INFO_DOWNLOAD_BY_SHARE);
                if (dataFromSharePreferences == null || dataFromSharePreferences.isEmpty()) {
                    CommonPresenter.showMessage(context, context.getResources().getString(C0598R.string.lb_info_toknow), context.getResources().getString(C0598R.string.lb_info_detail), false);
                    CommonPresenter.saveDataInSharePreferences(context, CommonPresenter.KEY_INFO_DOWNLOAD_BY_SHARE, "INFO_DOWNLOAD_BY_SHARE");
                }
                this.downTimer = new CountDownTimer(this, 500L, 500L, context) { // from class: org.questionsreponses.Presenter.HomePresenter.2
                    final HomePresenter this$0;
                    final Context val$context;

                    {
                        this.this$0 = this;
                        this.val$context = context;
                    }

                    @Override // android.os.CountDownTimer
                    public void onFinish() {
                        this.this$0.verifyQuizzSpiritualGrowthLevel(this.val$context);
                    }

                    @Override // android.os.CountDownTimer
                    public void onTick(long j) {
                    }
                }.start();
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "HomePresenter-->loadHomeData() : " + e.getMessage());
        }
    }

    public void loadQuizzData(Context context) {
        try {
            if (this.iQuizzFrag != null) {
                this.iQuizzFrag.initialize();
                this.iQuizzFrag.events();
                this.iQuizzFrag.loadQuizzMenu(Arrays.asList(context.getResources().getStringArray(C0598R.array.bible_category)), 1, 0);
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "HomePresenter-->loadQuizzData() : " + e.getMessage());
        }
    }

    public void loadVideoData(Context context) {
        try {
            if (this.iVideoFrag != null) {
                this.iVideoFrag.initialize();
                this.iVideoFrag.events();
                this.iVideoFrag.showProgressBar();
                List<Ressource> listRetrieveAllVideosList = this.iVideoFrag.retrieveAllVideosList();
                if (listRetrieveAllVideosList == null || listRetrieveAllVideosList.size() <= 0) {
                    Setting settingObjectFromSharePreferences = CommonPresenter.getSettingObjectFromSharePreferences(context, CommonPresenter.KEY_SETTING_OFFLINE_MODE);
                    if (!CommonPresenter.isMobileConnected(context) || settingObjectFromSharePreferences.getChoice()) {
                        List<Ressource> allResource = CommonPresenter.getAllResource(context, Format.VIDEO);
                        this.iVideoFrag.loadVideoData(allResource, 1, 0);
                        this.iVideoFrag.hideProgressBar();
                        this.iVideoFrag.initializeAllVideos(allResource);
                        CommonPresenter.saveDataInSharePreferences(context, "VALUE_TOTAL_AUDIO_RETRIEVE_FROM_LVE_SERVER", BuildConfig.FLAVOR + allResource.size());
                    } else {
                        HomeView.IApiRessource iApiRessource = (HomeView.IApiRessource) ApiClient.getApiClientLeVraiEvangile(context).create(HomeView.IApiRessource.class);
                        this.iApiRessource = iApiRessource;
                        iApiRessource.getAllVideos().enqueue(new Callback<List<Ressource>>(this, context) { // from class: org.questionsreponses.Presenter.HomePresenter.4
                            final HomePresenter this$0;
                            final Context val$context;

                            {
                                this.this$0 = this;
                                this.val$context = context;
                            }

                            @Override // retrofit2.Callback
                            public void onFailure(Call<List<Ressource>> call, Throwable th) throws JSONException {
                                List<Ressource> allResource2 = CommonPresenter.getAllResource(this.val$context, Format.VIDEO);
                                this.this$0.iVideoFrag.loadVideoData(allResource2, 1, 0);
                                this.this$0.iVideoFrag.hideProgressBar();
                                CommonPresenter.saveDataInSharePreferences(this.val$context, "VALUE_TOTAL_AUDIO_RETRIEVE_FROM_LVE_SERVER", BuildConfig.FLAVOR + allResource2.size());
                            }

                            @Override // retrofit2.Callback
                            public void onResponse(Call<List<Ressource>> call, Response<List<Ressource>> response) throws JSONException {
                                List<Ressource> allResource2 = CommonPresenter.getAllResource(this.val$context, Format.VIDEO);
                                CommonPresenter.saveDataInSharePreferences(this.val$context, CommonPresenter.KEY_VIDEO_RETRIEVE_FROM_LVE_SERVER, allResource2.toString());
                                this.this$0.iVideoFrag.loadVideoData(allResource2, 1, 0);
                                this.this$0.iVideoFrag.hideProgressBar();
                                this.this$0.iVideoFrag.initializeAllVideos(allResource2);
                                CommonPresenter.saveDataInSharePreferences(this.val$context, "VALUE_TOTAL_AUDIO_RETRIEVE_FROM_LVE_SERVER", BuildConfig.FLAVOR + allResource2.size());
                            }
                        });
                        Log.i("TAG_MODE_OFFLINE", "loadVideoData(CONNEXION=OK) && modeOffline.getChoice()=FALSE");
                    }
                } else {
                    this.iVideoFrag.loadVideoData(listRetrieveAllVideosList, 1, 0);
                    this.iVideoFrag.hideProgressBar();
                }
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "HomePresenter-->loadVideoData() : " + e.getMessage());
        }
    }

    public void manageNavigationDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout != null) {
            try {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            } catch (Exception e) {
                Log.e("TAG_ERROR", "HomePresenter-->manageNavigationDrawer() : " + e.getMessage());
            }
        }
    }

    public void onActivityDestroyed(TextToSpeech textToSpeech) {
        if (textToSpeech != null) {
            try {
                textToSpeech.stop();
                textToSpeech.shutdown();
            } catch (Exception e) {
                Log.e("TAG_ERROR", "HomePresenter-->onActivityDestroyed() : " + e.getMessage());
                return;
            }
        }
        if (this.downTimer != null) {
            this.downTimer.cancel();
        }
    }

    public void openNavigationDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout != null) {
            try {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    return;
                }
                drawerLayout.openDrawer(GravityCompat.START);
            } catch (Exception e) {
                Log.e("TAG_ERROR", "HomePresenter-->openNavigationDrawer() : " + e.getMessage());
            }
        }
    }

    public void playAudioNotification(Context context, MediaPlayer mediaPlayer) {
        try {
            if (this.iHome != null) {
                CommonPresenter.saveDataInSharePreferences(context, CommonPresenter.KEY_PLAYER_AUDIO_TO_NOTIF_AUDIO_TIME_ELAPSED, BuildConfig.FLAVOR + mediaPlayer.getCurrentPosition());
                closeAudioMediaPlayer(mediaPlayer);
                this.iHome.playNotification();
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "HomePresenter-->playAudioNotification() : " + e.getMessage());
        }
    }

    public void playNextQRAudio(HomeView.IAudioRecycler iAudioRecycler) {
        if (iAudioRecycler != null) {
            try {
                iAudioRecycler.playNextQRAudio();
            } catch (Exception e) {
                Log.e("TAG_ERROR", "HomePresenter-->playNextQRAudio() : " + e.getMessage());
            }
        }
    }

    public void playNextQRVideo(HomeView.IVideoRecycler iVideoRecycler) {
        if (iVideoRecycler != null) {
            try {
                iVideoRecycler.playNextQRVideo();
            } catch (Exception e) {
                Log.e("TAG_ERROR", "HomePresenter-->playNextQRVideo() : " + e.getMessage());
            }
        }
    }

    public void playPreviousQRAudio(HomeView.IAudioRecycler iAudioRecycler) {
        if (iAudioRecycler != null) {
            try {
                iAudioRecycler.playPreviousQRAudio();
            } catch (Exception e) {
                Log.e("TAG_ERROR", "HomePresenter-->playPreviousQRAudio() : " + e.getMessage());
            }
        }
    }

    public void playPreviousQRVideo(HomeView.IVideoRecycler iVideoRecycler) {
        if (iVideoRecycler != null) {
            try {
                iVideoRecycler.playPreviousQRVideo();
            } catch (Exception e) {
                Log.e("TAG_ERROR", "HomePresenter-->playPreviousQRVideo() : " + e.getMessage());
            }
        }
    }

    public void playQRAudioOnCompletion(Context context, HomeView.IAudioRecycler iAudioRecycler, ImageButton imageButton) {
        if (iAudioRecycler != null) {
            try {
                if (CommonPresenter.getSettingObjectFromSharePreferences(context, CommonPresenter.KEY_SETTING_CONCATENATE_AUDIO_READING).getChoice()) {
                    iAudioRecycler.playNextQRAudio();
                } else {
                    imageButton.setBackgroundResource(C0598R.drawable.btn_media_player_play);
                }
            } catch (Exception e) {
                Log.e("TAG_ERROR", "HomePresenter-->playQRAudioOnCompletion() : " + e.getMessage());
            }
        }
    }

    public void playQRAudioPlayer(Context context, Ressource ressource, int i) throws Resources.NotFoundException {
        try {
            if (this.iHome != null) {
                if (CommonPresenter.isMobileConnected(context)) {
                    LoadAudioMediaPlayer loadAudioMediaPlayer = new LoadAudioMediaPlayer();
                    this.loadAudioMediaPlayer = loadAudioMediaPlayer;
                    loadAudioMediaPlayer.initLoadAudioPlayer(ressource, i, this.iHome);
                    this.loadAudioMediaPlayer.execute(new Void[0]);
                } else {
                    CommonPresenter.showMessage(context, context.getResources().getString(C0598R.string.no_connection).toUpperCase(), context.getResources().getString(C0598R.string.detail_no_connection), false);
                }
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "HomePresenter-->playQRAudioPlayer() : " + e.getMessage());
        }
    }

    public void playQRAudioPlayer(MediaPlayer mediaPlayer, View view) throws IllegalStateException {
        if (mediaPlayer != null) {
            try {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    view.setBackgroundResource(C0598R.drawable.btn_media_player_play);
                } else {
                    mediaPlayer.start();
                    view.setBackgroundResource(C0598R.drawable.btn_media_player_pause);
                }
            } catch (Exception e) {
                Log.e("TAG_ERROR", "HomePresenter-->playQRAudioPlayer() : " + e.getMessage());
            }
        }
    }

    public void playQRVideoPlayer(Context context, Ressource ressource, int i) throws Resources.NotFoundException {
        try {
            if (!CommonPresenter.isMobileConnected(context)) {
                CommonPresenter.showMessage(context, context.getResources().getString(C0598R.string.no_connection).toUpperCase(), context.getResources().getString(C0598R.string.detail_no_connection), false);
            } else if (this.iVideoFrag != null) {
                this.iVideoFrag.launchVideoToPlay(ressource, i);
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "HomePresenter-->playQRVideoPlayer() : " + e.getMessage());
        }
    }

    public void playQuizzGame(Context context, String str, int i) {
        if (this.iHome != null) {
            try {
                LaunchQuizzGameAsyntask launchQuizzGameAsyntask = new LaunchQuizzGameAsyntask();
                this.launchQuizzGameAsyntask = launchQuizzGameAsyntask;
                launchQuizzGameAsyntask.initialize(context, str, i, this.iHome);
                this.launchQuizzGameAsyntask.execute(new Void[0]);
            } catch (Exception e) {
                Log.e("TAG_ERROR", "HomePresenter-->playQuizzGame() : " + e.getMessage());
            }
        }
    }

    public void refreshQuizzEndOrNotEnd(Context context, boolean z) {
        try {
            if (z) {
                showQuizzFinished(context, false);
            } else {
                showQuizzNotFinished(context, false);
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "HomePresenter-->refreshQuizzEndOrNotEnd() : " + e.getMessage());
        }
    }

    public void resetQuizzIdSaveList(Context context, ArrayList<Quizz> arrayList, String str, String str2) {
        if (CommonPresenter.getTotalQuizzIdListBy(str2) + 2 >= arrayList.size()) {
            CommonPresenter.saveDataInSharePreferences(context, str, "-");
        }
    }

    public ArrayList<Quizz> retrieveAllQuizzDataFromHome() {
        ArrayList<Quizz> arrayListRetrieveAllQuizzData = null;
        try {
            if (this.iHome != null) {
                arrayListRetrieveAllQuizzData = this.iHome.retrieveAllQuizzData();
            }
            return arrayListRetrieveAllQuizzData;
        } catch (Exception e) {
            Log.e("TAG_ERROR", "HomePresenter-->retrieveAllQuizzDataFromHome() : " + e.getMessage());
            return null;
        }
    }

    public void retrieveAndSetIAudioRecyclerAdapteur(HomeView.IAudioRecycler iAudioRecycler) {
        try {
            if (this.iHome != null) {
                this.iHome.onHomeIAudioRecycler(iAudioRecycler);
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "HomePresenter-->retrieveAndSetIAudioRecyclerAdapteur() : " + e.getMessage());
        }
    }

    public void retrieveAndSetIVideoRecyclerReference(HomeView.IVideoRecycler iVideoRecycler) {
        try {
            if (this.iVideoFrag != null) {
                this.iVideoFrag.onFragmentVideoIVideoRecycler(iVideoRecycler);
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "HomePresenter-->retrieveAndSetIVideoRecyclerReference() : " + e.getMessage());
        }
    }

    public void retrieveAudioSelected(Context context, Ressource ressource, int i) throws Resources.NotFoundException {
        try {
            if (!CommonPresenter.isMobileConnected(context)) {
                CommonPresenter.showMessage(context, context.getResources().getString(C0598R.string.no_connection).toUpperCase(), context.getResources().getString(C0598R.string.detail_no_connection), false);
            } else if (this.iHome != null) {
                this.iHome.onAudioSelected(ressource, i);
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "HomePresenter-->retrieveAudioSelected() : " + e.getMessage());
        }
    }

    public void retrieveBroadcastReceiver(Context context, Intent intent) {
        if (intent.getAction().equalsIgnoreCase("android.speech.tts.TTS_QUEUE_PROCESSING_COMPLETED")) {
            Log.i("TAG_TTS", "BroadcastReceiver() : Text is finished to read");
        }
    }

    public void retrieveFabSubMenuVisibility(boolean z) {
        try {
            if (this.iHome != null) {
                this.iHome.fabSubMenuVisibility(z ? 0 : 8);
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "HomePresenter-->retrieveFabSubMenuVisibility() : " + e.getMessage());
        }
    }

    public void retrieveMenuQuizzSelected(String str, int i) {
        try {
            if (this.iHome != null) {
                this.iHome.onMenuQuizzSelected(str, i);
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "HomePresenter-->retrieveMenuQuizzSelected() : " + e.getMessage());
        }
    }

    public void retrieveUserAction(Context context, int i, boolean z, MediaPlayer mediaPlayer, ImageButton imageButton) {
        try {
            if (this.iHome != null) {
                if (i == 0) {
                    this.iHome.audioPlayerVisibility(8);
                    if (mediaPlayer.isPlaying()) {
                        imageButton.performClick();
                    }
                } else if (i != 1) {
                    if (i == 2) {
                        this.iHome.audioPlayerVisibility(8);
                        if (mediaPlayer.isPlaying()) {
                            imageButton.performClick();
                        }
                    }
                } else if (z && !mediaPlayer.isPlaying()) {
                    imageButton.performClick();
                    if (mediaPlayer.isPlaying()) {
                        this.iHome.audioPlayerVisibility(0);
                        this.iHome.stopNotification();
                    }
                }
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "HomePresenter-->retrieveUserAction() : " + e.getMessage());
        }
    }

    public void retrieveUserAction(Context context, MenuItem menuItem) {
    }

    public void retrieveUserAction(Context context, View view) {
        try {
            switch (view.getId()) {
                case C0598R.id.fab_app_update /* 2131296392 */:
                    verifyAppUpdate(view.getContext());
                    break;
                case C0598R.id.fab_contact_us /* 2131296395 */:
                    showContactMenu(view);
                    break;
                case C0598R.id.fab_parameter /* 2131296401 */:
                    if (this.iHome != null) {
                        this.iHome.displaySettings();
                        break;
                    }
                    break;
                case C0598R.id.fab_player_volume /* 2131296408 */:
                    CommonPresenter.getApplicationVolume(context);
                    break;
                case C0598R.id.fab_privacy_policy /* 2131296409 */:
                    context.startActivity(new Intent(context, (Class<?>) PrivacyPolicyActivity.class));
                    break;
                case C0598R.id.fab_search /* 2131296415 */:
                    new CommonPresenter().showFormSearch(context);
                    break;
                case C0598R.id.fab_share_app /* 2131296416 */:
                    CommonPresenter.shareApplication(context);
                    break;
                case C0598R.id.fab_youtube_download /* 2131296418 */:
                    if (!CommonPresenter.isPermissionToSaveFileAccepted(view.getContext())) {
                        if (this.iHome != null) {
                            this.iHome.askPermissionToSaveFile();
                            break;
                        }
                    } else {
                        context.startActivity(new Intent(context, (Class<?>) DownloaderActivity.class));
                        break;
                    }
                    break;
                case C0598R.id.nav_quizz_texttospeech /* 2131296498 */:
                    if (this.quizzValue != null) {
                        String str = (((BuildConfig.FLAVOR + this.quizzValue.getQuestion().trim().replace("\n", " ")) + "Choix 1 " + this.quizzValue.getProposition_1()) + "Choix 2 " + this.quizzValue.getProposition_2()) + "Choix 3 " + this.quizzValue.getProposition_3();
                        if (this.iHome != null) {
                            this.iHome.readTextFromTextToSpeech(str);
                            break;
                        }
                    }
                    break;
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "HomePresenter-->retrieveUserAction() : " + e.getMessage());
        }
    }

    public void retrieveUserAction(View view, MediaPlayer mediaPlayer) {
        try {
            int id = view.getId();
            if (id == 2131296338) {
                closeAudioMediaPlayer(mediaPlayer);
            } else if (id == 2131296406) {
                playAudioNotification(view.getContext(), mediaPlayer);
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "HomePresenter-->retrieveUserAction() : " + e.getMessage());
        }
    }

    public void saveRessourceAudioData(Context context, Ressource ressource) {
        try {
            View viewInTermsOfContext = CommonPresenter.getViewInTermsOfContext(context);
            DAORessource dAORessource = new DAORessource(context);
            if (dAORessource.isRessourceExists(ressource.getSrc())) {
                CommonPresenter.showMessageSnackBar(viewInTermsOfContext, context.getString(C0598R.string.audio_already_add_to_favorite));
            } else {
                int size = dAORessource.getAllByTypeRessource(Format.AUDIO).size();
                dAORessource.insertData(ressource.getTitre(), ressource.getEtat(), ressource.getSrc(), Format.AUDIO, ressource.getAuteur(), ressource.getUrlacces(), ressource.getDuree());
                if (size < dAORessource.getAllByTypeRessource(Format.AUDIO).size()) {
                    CommonPresenter.showMessageSnackBar(viewInTermsOfContext, context.getString(C0598R.string.audio_add_to_favorite));
                }
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "HomePresenter-->saveRessourceAudioData() : " + e.getMessage());
        }
    }

    public void scrollAudioDownloadDataItemsToPosition(int i) {
    }

    public void scrollAudioFavoriteDataItemsToPosition(int i) {
    }

    public void setQuizzGameIsFinished(boolean z) {
    }

    public void shareQRAudio(Context context, HomeView.IAudioRecycler iAudioRecycler) {
        if (iAudioRecycler != null) {
            try {
                iAudioRecycler.shareQRAudio(context);
            } catch (Exception e) {
                Log.e("TAG_ERROR", "HomePresenter-->shareQRAudio() : " + e.getMessage());
            }
        }
    }

    public void showQuizzFinished(Context context, boolean z) throws Resources.NotFoundException {
        try {
            if (this.iHome != null) {
                ArrayList<Survey> allFinishedKeyGroup = new DAOSurvey(context).getAllFinishedKeyGroup();
                this.iHome.loadQuizzSave(allFinishedKeyGroup, 1);
                this.iHome.modifyTitleOfSaveQuizzGame(context.getResources().getString(C0598R.string.lb_menu_quizz_end), allFinishedKeyGroup.size());
                this.iHome.deleteQuizzSavedVisibility(8);
                if (z) {
                    this.iHome.openOrCloseNavigationDrawer();
                }
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "HomePresenter-->showQuizzFinished() : " + e.getMessage());
        }
    }

    public void showQuizzNotFinished(Context context, boolean z) throws Resources.NotFoundException {
        try {
            if (this.iHome != null) {
                ArrayList<Survey> allNotFinishedKeyGroup = new DAOSurvey(context).getAllNotFinishedKeyGroup();
                this.iHome.loadQuizzSave(allNotFinishedKeyGroup, 1);
                this.iHome.modifyTitleOfSaveQuizzGame(context.getResources().getString(C0598R.string.lb_menu_quizz_not_end), allNotFinishedKeyGroup.size());
                this.iHome.deleteQuizzSavedVisibility(8);
                if (z) {
                    this.iHome.openOrCloseNavigationDrawer();
                }
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "HomePresenter-->showQuizzNotFinished() : " + e.getMessage());
        }
    }

    public void showQuizzSpiritualGrowth(int i) {
        try {
            if (this.iHome != null) {
                this.iHome.showQuizzSpiritualGrowth(i);
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "HomePresenter-->showQuizzSpiritualGrowth() : " + e.getMessage());
        }
    }

    public void srcollAudioDataItemsToPosition(int i) {
    }

    public void srcollVideoDataItemsToPosition(int i) {
        try {
            if (this.iVideoFrag != null) {
                this.iVideoFrag.scrollVideoDataToPosition(i);
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "HomePresenter-->srcollVideoDataItemsToPosition() : " + e.getMessage());
        }
    }

    public void stopAllOtherMediaSound(MediaPlayer mediaPlayer, Ressource ressource) {
        try {
            if (this.iHome != null) {
                this.iHome.stopOtherMediaPlayerSound(mediaPlayer, ressource);
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "HomePresenter-->stopAllOtherMediaSound() : " + e.getMessage());
        }
    }

    public void stopAudioNotification() {
        try {
            if (this.iHome != null) {
                this.iHome.stopNotification();
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "HomePresenter-->stopAudioNotification() : " + e.getMessage());
        }
    }

    public void verifyAppUpdate(Context context) {
        String packageName = context.getPackageName();
        try {
            context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + packageName)));
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
        }
    }

    public void verifyQuizzSpiritualGrowthLevel(Context context) {
        try {
            if (this.iHome != null) {
                DAOSurvey dAOSurvey = new DAOSurvey(context);
                this.iHome.enableAllLevelMenu(new Boolean[]{true, false, false, false, false, false, false, false, false, false});
                if (dAOSurvey.getAllSpiritualGrowthKeyGroupByLevel(1).size() == 20) {
                    this.iHome.enableAllLevelMenu(new Boolean[]{true, true, false, false, false, false, false, false, false, false});
                }
                if (dAOSurvey.getAllSpiritualGrowthKeyGroupByLevel(2).size() == 20) {
                    this.iHome.enableAllLevelMenu(new Boolean[]{true, true, true, false, false, false, false, false, false, false});
                }
                if (dAOSurvey.getAllSpiritualGrowthKeyGroupByLevel(3).size() == 20) {
                    this.iHome.enableAllLevelMenu(new Boolean[]{true, true, true, true, false, false, false, false, false, false});
                }
                if (dAOSurvey.getAllSpiritualGrowthKeyGroupByLevel(4).size() == 20) {
                    this.iHome.enableAllLevelMenu(new Boolean[]{true, true, true, true, true, false, false, false, false, false});
                }
                if (dAOSurvey.getAllSpiritualGrowthKeyGroupByLevel(5).size() == 20) {
                    this.iHome.enableAllLevelMenu(new Boolean[]{true, true, true, true, true, true, false, false, false, false});
                }
                if (dAOSurvey.getAllSpiritualGrowthKeyGroupByLevel(6).size() == 20) {
                    this.iHome.enableAllLevelMenu(new Boolean[]{true, true, true, true, true, true, true, false, false, false});
                }
                if (dAOSurvey.getAllSpiritualGrowthKeyGroupByLevel(7).size() == 20) {
                    this.iHome.enableAllLevelMenu(new Boolean[]{true, true, true, true, true, true, true, true, false, false});
                }
                if (dAOSurvey.getAllSpiritualGrowthKeyGroupByLevel(8).size() == 20) {
                    this.iHome.enableAllLevelMenu(new Boolean[]{true, true, true, true, true, true, true, true, true, false});
                }
                if (dAOSurvey.getAllSpiritualGrowthKeyGroupByLevel(9).size() == 20) {
                    this.iHome.enableAllLevelMenu(new Boolean[]{true, true, true, true, true, true, true, true, true, true});
                }
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "HomePresenter-->verifyQuizzSpiritualGrowthLevel() : " + e.getMessage());
        }
    }
}
