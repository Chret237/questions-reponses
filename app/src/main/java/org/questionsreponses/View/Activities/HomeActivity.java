package org.questionsreponses.View.Activities;

import android.R;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.github.clans.fab.BuildConfig;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.github.kiulian.downloader.model.videos.formats.Format;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.questionsreponses.C0598R;
import org.questionsreponses.Model.Quizz;
import org.questionsreponses.Model.Ressource;
import org.questionsreponses.Model.Survey;
import org.questionsreponses.Presenter.CommonPresenter;
import org.questionsreponses.Presenter.HomePresenter;
import org.questionsreponses.View.Adapters.HomePagerAdapter;
import org.questionsreponses.View.Adapters.QuizzSaveRecyclerAdapter;
import org.questionsreponses.View.Fragments.AudioFragment;
import org.questionsreponses.View.Fragments.QuizzFragment;
import org.questionsreponses.View.Fragments.VideoFragment;
import org.questionsreponses.View.Interfaces.HomeView;
import org.questionsreponses.View.Interfaces.NotificationView;
import org.questionsreponses.View.Services.PlayerAudioService;
import org.questionsreponses.View.ViewPagers.HomeViewPager;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Activities/HomeActivity.class */
public class HomeActivity extends AppCompatActivity implements HomeView.IHome, TextToSpeech.OnInitListener {
    private MenuItem action_quizz_level_1;
    private MenuItem action_quizz_level_10;
    private MenuItem action_quizz_level_2;
    private MenuItem action_quizz_level_3;
    private MenuItem action_quizz_level_4;
    private MenuItem action_quizz_level_5;
    private MenuItem action_quizz_level_6;
    private MenuItem action_quizz_level_7;
    private MenuItem action_quizz_level_8;
    private MenuItem action_quizz_level_9;
    private View audioPlayerContent;
    private ImageButton audio_player_next;
    private ImageButton audio_player_play;
    private ImageButton audio_player_previous;
    private ProgressBar audio_player_progressbar;
    private SeekBar audio_player_seekbar;
    private TextView audio_player_soustitre;
    private TextView audio_player_time_end;
    private TextView audio_player_time_progress;
    private TextView audio_player_titre;
    private ImageButton btn_player_close;
    private DrawerLayout drawer;
    private FloatingActionButton fab_app_update;
    private FloatingActionButton fab_contact_us;
    private FloatingActionMenu fab_menu;
    private FloatingActionButton fab_parameter;
    private FloatingActionButton fab_player_download;
    private FloatingActionButton fab_player_favorite;
    private FloatingActionButton fab_player_notification;
    private FloatingActionButton fab_player_share_app;
    private FloatingActionButton fab_player_volume;
    private FloatingActionButton fab_privacy_policy;
    private FloatingActionButton fab_search;
    private FloatingActionButton fab_share_app;
    private FloatingActionButton fab_youtube_download;
    private ArrayList<Fragment> fragmentsHome;
    private GridLayoutManager gridLayout;
    private HomePresenter homePresenter;
    private HomeView.IAudioFrag iAudioFrag;
    private HomeView.IAudioRecycler iAudioRecycler;
    private HomeView.IQuizzFrag iQuizzFrag;
    private HomeView.IQuizzSaveRecycler iQuizzSaveRecycler;
    private HomeView.IVideoFrag iVideoFrag;
    private MediaPlayer mediaPlayer;
    private LinearLayout nav_drawer_content;
    private ImageButton nav_quizz_back;
    private ImageButton nav_quizz_delete;
    private TextView nav_quizz_title;
    private HomePagerAdapter pagerAdapter;
    private HomeViewPager pagerHome;
    private ArrayList<Quizz> quizzList;
    private QuizzSaveRecyclerAdapter quizzSaveAdapter;
    private RecyclerView recyclerSaveView;
    private Hashtable<String, List<Ressource>> resourceList;
    private TabLayout tabLayout;
    private TextToSpeech textToSpeech;
    private ArrayList<String> titresHome;
    private Toolbar toolbar;
    private BroadcastReceiver ttsReceiver;
    private boolean QUIZZ_END_IS_SELECTED = true;
    private double timeElapsed = 0.0d;
    private double finalTime = 0.0d;
    private Handler durationHandler = new Handler();
    private boolean isAudioSelected = false;
    private Runnable updateSeekBarTime = new Runnable(this) { // from class: org.questionsreponses.View.Activities.HomeActivity.8
        final HomeActivity this$0;

        {
            this.this$0 = this;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                this.this$0.finalTime = this.this$0.mediaPlayer.getDuration();
                this.this$0.timeElapsed = this.this$0.mediaPlayer.getCurrentPosition();
                this.this$0.audio_player_seekbar.setProgress((int) this.this$0.timeElapsed);
                this.this$0.audio_player_seekbar.setMax((int) this.this$0.finalTime);
                long j = (long) this.this$0.timeElapsed;
                this.this$0.audio_player_time_progress.setText(String.format("%d min, %d sec", Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(j)), Long.valueOf(TimeUnit.MILLISECONDS.toSeconds(j) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(j)))));
                this.this$0.audio_player_time_end.setText(String.format("%d min, %d sec", Long.valueOf(TimeUnit.MILLISECONDS.toMinutes((long) this.this$0.finalTime)), Long.valueOf(TimeUnit.MILLISECONDS.toSeconds((long) this.this$0.finalTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) this.this$0.finalTime)))));
                this.this$0.durationHandler.postDelayed(this.this$0.updateSeekBarTime, 100L);
            } catch (Exception e) {
            }
        }
    };

    @Override // org.questionsreponses.View.Interfaces.HomeView.IHome
    public void askPermissionToSaveFile() {
        if (ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, CommonPresenter.VALUE_PERMISSION_TO_SAVE_FILE);
        }
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IHome
    public void audioPlayerVisibility(int i) {
        this.audioPlayerContent.setVisibility(i);
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IHome
    public void contactUs(View view) {
        this.homePresenter.retrieveUserAction(this, view);
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IHome
    public void continueBackPressedAction() throws IllegalStateException {
        CommonPresenter.stopAudioMediaPlayer(this.mediaPlayer);
        this.homePresenter.cancelAsyntask();
        finish();
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IHome
    public void deleteAllSelectedQuizz() {
        this.iQuizzSaveRecycler.deleteAllCheckedQuizz(this);
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IHome
    public void deleteQuizzSavedVisibility(int i) {
        this.nav_quizz_delete.setVisibility(i);
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IHome
    public void displaySettings() {
        startActivity(new Intent(this, (Class<?>) SettingsActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IHome
    public void enableAllLevelMenu(Boolean[] boolArr) {
        try {
            this.action_quizz_level_1.setEnabled(boolArr[0].booleanValue());
            this.action_quizz_level_2.setEnabled(boolArr[1].booleanValue());
            this.action_quizz_level_3.setEnabled(boolArr[2].booleanValue());
            this.action_quizz_level_4.setEnabled(boolArr[3].booleanValue());
            this.action_quizz_level_5.setEnabled(boolArr[4].booleanValue());
            this.action_quizz_level_6.setEnabled(boolArr[5].booleanValue());
            this.action_quizz_level_7.setEnabled(boolArr[6].booleanValue());
            this.action_quizz_level_8.setEnabled(boolArr[7].booleanValue());
            this.action_quizz_level_9.setEnabled(boolArr[8].booleanValue());
            this.action_quizz_level_10.setEnabled(boolArr[9].booleanValue());
        } catch (Exception e) {
        }
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IHome
    public void enableAudioMediaPlayer(boolean z) {
        this.audio_player_time_progress.setEnabled(z);
        this.fab_player_download.setEnabled(z);
        this.fab_player_share_app.setEnabled(z);
        this.fab_player_favorite.setEnabled(z);
        this.fab_player_volume.setEnabled(z);
        this.audio_player_seekbar.setEnabled(z);
        this.audio_player_previous.setEnabled(z);
        this.audio_player_play.setEnabled(z);
        this.audio_player_next.setEnabled(z);
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IHome
    public void events() {
        this.pagerHome.addOnPageChangeListener(new ViewPager.OnPageChangeListener(this) { // from class: org.questionsreponses.View.Activities.HomeActivity.2
            final HomeActivity this$0;

            {
                this.this$0 = this;
            }

            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int i) {
            }

            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageScrolled(int i, float f, int i2) {
            }

            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageSelected(int i) {
                HomePresenter homePresenter = this.this$0.homePresenter;
                HomeActivity homeActivity = this.this$0;
                homePresenter.retrieveUserAction(homeActivity, i, homeActivity.isAudioSelected, this.this$0.mediaPlayer, this.this$0.audio_player_play);
            }
        });
        this.fab_player_notification.setOnClickListener(new View.OnClickListener(this) { // from class: org.questionsreponses.View.Activities._$$Lambda$HomeActivity$6rYGehvYQgAom1Bv6_sqcqQf94I
            public final HomeActivity f$0;

            {
                this.f$0 = this;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.lambda$events$0$HomeActivity(view);
            }
        });
        this.btn_player_close.setOnClickListener(new View.OnClickListener(this) { // from class: org.questionsreponses.View.Activities._$$Lambda$HomeActivity$_fyUNUiZdTOX3mDxeYyzV5wFG6o
            public final HomeActivity f$0;

            {
                this.f$0 = this;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.lambda$events$1$HomeActivity(view);
            }
        });
        this.fab_player_volume.setOnClickListener(new View.OnClickListener(this) { // from class: org.questionsreponses.View.Activities._$$Lambda$HomeActivity$o_xWActCz20bXztrJj8aa3o0RMQ
            public final HomeActivity f$0;

            {
                this.f$0 = this;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.lambda$events$2$HomeActivity(view);
            }
        });
        this.fab_player_download.setOnClickListener(new View.OnClickListener(this) { // from class: org.questionsreponses.View.Activities._$$Lambda$HomeActivity$IrPgmvzj3boJN6LQpZUStkkBULM
            public final HomeActivity f$0;

            {
                this.f$0 = this;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.lambda$events$3$HomeActivity(view);
            }
        });
        this.fab_player_share_app.setOnClickListener(new View.OnClickListener(this) { // from class: org.questionsreponses.View.Activities._$$Lambda$HomeActivity$INfE6H_GuT_ESFCkaHTfg0Rv1SA
            public final HomeActivity f$0;

            {
                this.f$0 = this;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.lambda$events$4$HomeActivity(view);
            }
        });
        this.fab_player_favorite.setOnClickListener(new View.OnClickListener(this) { // from class: org.questionsreponses.View.Activities._$$Lambda$HomeActivity$zomDsta8b3gwiMOJtDOAv0JDpq0
            public final HomeActivity f$0;

            {
                this.f$0 = this;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.lambda$events$5$HomeActivity(view);
            }
        });
        this.fab_menu.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener(this) { // from class: org.questionsreponses.View.Activities._$$Lambda$HomeActivity$Aqk1d_8Uq4W5oeJIjgnZJtKzmXc
            public final HomeActivity f$0;

            {
                this.f$0 = this;
            }

            @Override // com.github.clans.fab.FloatingActionMenu.OnMenuToggleListener
            public final void onMenuToggle(boolean z) {
                this.f$0.lambda$events$6$HomeActivity(z);
            }
        });
        this.drawer.addDrawerListener(new DrawerLayout.DrawerListener(this) { // from class: org.questionsreponses.View.Activities.HomeActivity.3
            final HomeActivity this$0;

            {
                this.this$0 = this;
            }

            @Override // android.support.v4.widget.DrawerLayout.DrawerListener
            public void onDrawerClosed(View view) {
            }

            @Override // android.support.v4.widget.DrawerLayout.DrawerListener
            public void onDrawerOpened(View view) {
            }

            @Override // android.support.v4.widget.DrawerLayout.DrawerListener
            public void onDrawerSlide(View view, float f) {
            }

            @Override // android.support.v4.widget.DrawerLayout.DrawerListener
            public void onDrawerStateChanged(int i) {
            }
        });
        this.nav_quizz_back.setOnClickListener(new View.OnClickListener(this) { // from class: org.questionsreponses.View.Activities._$$Lambda$HomeActivity$dY6dmHkivoZl8idfved3EPHqoXs
            public final HomeActivity f$0;

            {
                this.f$0 = this;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.lambda$events$7$HomeActivity(view);
            }
        });
        this.nav_quizz_delete.setOnClickListener(new View.OnClickListener(this) { // from class: org.questionsreponses.View.Activities._$$Lambda$HomeActivity$_i_8pF0GZWZNT7gWAU_sRTm61HQ
            public final HomeActivity f$0;

            {
                this.f$0 = this;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.lambda$events$8$HomeActivity(view);
            }
        });
        this.audio_player_play.setOnClickListener(new View.OnClickListener(this) { // from class: org.questionsreponses.View.Activities._$$Lambda$HomeActivity$TWA3dRpsgEGse9ANJdCxz6FOPdo
            public final HomeActivity f$0;

            {
                this.f$0 = this;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) throws IllegalStateException {
                this.f$0.lambda$events$9$HomeActivity(view);
            }
        });
        this.audio_player_next.setOnClickListener(new View.OnClickListener(this) { // from class: org.questionsreponses.View.Activities._$$Lambda$HomeActivity$DPFLk3_sUO0z8JFul0GyazObHAU
            public final HomeActivity f$0;

            {
                this.f$0 = this;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.lambda$events$10$HomeActivity(view);
            }
        });
        this.audio_player_previous.setOnClickListener(new View.OnClickListener(this) { // from class: org.questionsreponses.View.Activities._$$Lambda$HomeActivity$87Pn2gEvmDuhrNHxsk7Z_lP0i6M
            public final HomeActivity f$0;

            {
                this.f$0 = this;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.lambda$events$11$HomeActivity(view);
            }
        });
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IHome
    public void fabSubMenuVisibility(int i) {
        this.fab_contact_us.setVisibility(i);
        this.fab_share_app.setVisibility(i);
        this.fab_parameter.setVisibility(i);
        this.fab_search.setVisibility(i);
        this.fab_app_update.setVisibility(i);
        this.fab_youtube_download.setVisibility(i);
        this.fab_privacy_policy.setVisibility(i);
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IHome
    public void initialize() {
        this.resourceList = new Hashtable<>();
        this.textToSpeech = new TextToSpeech(this, this);
        Toolbar toolbar = (Toolbar) findViewById(C0598R.id.toolbar);
        this.toolbar = toolbar;
        setSupportActionBar(toolbar);
        this.tabLayout = (TabLayout) findViewById(C0598R.id.tabs);
        this.recyclerSaveView = (RecyclerView) findViewById(C0598R.id.quizzSaveRecyclerView);
        this.drawer = (DrawerLayout) findViewById(C0598R.id.drawer_layout);
        this.pagerHome = (HomeViewPager) super.findViewById(C0598R.id.viewpagerHome);
        this.fab_menu = (FloatingActionMenu) findViewById(C0598R.id.fab_menu);
        this.fab_contact_us = (FloatingActionButton) findViewById(C0598R.id.fab_contact_us);
        this.fab_share_app = (FloatingActionButton) findViewById(C0598R.id.fab_share_app);
        this.fab_parameter = (FloatingActionButton) findViewById(C0598R.id.fab_parameter);
        this.fab_search = (FloatingActionButton) findViewById(C0598R.id.fab_search);
        this.fab_app_update = (FloatingActionButton) findViewById(C0598R.id.fab_app_update);
        this.fab_youtube_download = (FloatingActionButton) findViewById(C0598R.id.fab_youtube_download);
        this.fab_privacy_policy = (FloatingActionButton) findViewById(C0598R.id.fab_privacy_policy);
        this.audioPlayerContent = findViewById(C0598R.id.audioPlayerContent);
        ArrayList<String> arrayList = new ArrayList<>();
        this.titresHome = arrayList;
        arrayList.add(getResources().getString(C0598R.string.lb_quizz));
        this.titresHome.add(getResources().getString(C0598R.string.lb_audios));
        this.titresHome.add(getResources().getString(C0598R.string.lb_videos));
        ArrayList<Fragment> arrayList2 = new ArrayList<>();
        this.fragmentsHome = arrayList2;
        arrayList2.add(Fragment.instantiate(this, QuizzFragment.class.getName()));
        this.fragmentsHome.add(Fragment.instantiate(this, AudioFragment.class.getName()));
        this.fragmentsHome.add(Fragment.instantiate(this, VideoFragment.class.getName()));
        HomePagerAdapter homePagerAdapter = new HomePagerAdapter(super.getSupportFragmentManager(), this.fragmentsHome, this.titresHome);
        this.pagerAdapter = homePagerAdapter;
        this.pagerHome.setAdapter(homePagerAdapter);
        this.pagerHome.setPagingEnabled(true);
        this.tabLayout.setupWithViewPager(this.pagerHome);
        LinearLayout linearLayout = (LinearLayout) findViewById(C0598R.id.nav_drawer_content);
        this.nav_drawer_content = linearLayout;
        CommonPresenter.getNavDrawerDimension(this, linearLayout);
        this.nav_quizz_back = (ImageButton) findViewById(C0598R.id.nav_quizz_arrow_back);
        this.nav_quizz_delete = (ImageButton) findViewById(C0598R.id.nav_quizz_item_delete);
        this.nav_quizz_title = (TextView) findViewById(C0598R.id.nav_quizz_title);
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver(this) { // from class: org.questionsreponses.View.Activities.HomeActivity.1
            final HomeActivity this$0;

            {
                this.this$0 = this;
            }

            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                this.this$0.homePresenter.retrieveBroadcastReceiver(context, intent);
            }
        };
        this.ttsReceiver = broadcastReceiver;
        registerReceiver(broadcastReceiver, new IntentFilter("android.speech.tts.TTS_QUEUE_PROCESSING_COMPLETED"));
        this.audio_player_progressbar = (ProgressBar) findViewById(C0598R.id.audio_player_progressbar);
        this.audio_player_titre = (TextView) findViewById(C0598R.id.audio_player_titre);
        this.audio_player_time_progress = (TextView) findViewById(C0598R.id.audio_player_time_progress);
        this.audio_player_soustitre = (TextView) findViewById(C0598R.id.audio_player_soustitre);
        this.audio_player_time_end = (TextView) findViewById(C0598R.id.audio_player_time_end);
        this.fab_player_download = (FloatingActionButton) findViewById(C0598R.id.fab_player_download);
        this.fab_player_share_app = (FloatingActionButton) findViewById(C0598R.id.fab_player_share_app);
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(C0598R.id.fab_player_favorite);
        this.fab_player_favorite = floatingActionButton;
        floatingActionButton.setVisibility(8);
        this.fab_player_volume = (FloatingActionButton) findViewById(C0598R.id.fab_player_volume);
        this.fab_player_notification = (FloatingActionButton) findViewById(C0598R.id.fab_player_notification);
        this.audio_player_seekbar = (SeekBar) findViewById(C0598R.id.audio_player_seekbar);
        this.audio_player_previous = (ImageButton) findViewById(C0598R.id.audio_player_previous);
        this.audio_player_play = (ImageButton) findViewById(C0598R.id.audio_player_play);
        this.audio_player_next = (ImageButton) findViewById(C0598R.id.audio_player_next);
        this.btn_player_close = (ImageButton) findViewById(C0598R.id.btn_player_close);
        this.mediaPlayer = new MediaPlayer();
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IHome
    public void initializeAllQuizzData(ArrayList<Quizz> arrayList) {
        this.quizzList = arrayList;
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IHome
    public void initializeResourceList(String str, List<Ressource> list) {
        this.resourceList.put(str, list);
    }

    public /* synthetic */ void lambda$events$0$HomeActivity(View view) {
        this.homePresenter.retrieveUserAction(view, this.mediaPlayer);
    }

    public /* synthetic */ void lambda$events$1$HomeActivity(View view) {
        this.homePresenter.retrieveUserAction(view, this.mediaPlayer);
    }

    public /* synthetic */ void lambda$events$10$HomeActivity(View view) {
        this.homePresenter.playNextQRAudio(this.iAudioRecycler);
    }

    public /* synthetic */ void lambda$events$11$HomeActivity(View view) {
        this.homePresenter.playPreviousQRAudio(this.iAudioRecycler);
    }

    public /* synthetic */ void lambda$events$2$HomeActivity(View view) {
        this.homePresenter.retrieveUserAction(this, view);
    }

    public /* synthetic */ void lambda$events$3$HomeActivity(View view) {
        this.homePresenter.downloadQRAudio(this, this.iAudioRecycler);
    }

    public /* synthetic */ void lambda$events$4$HomeActivity(View view) {
        this.homePresenter.shareQRAudio(this, this.iAudioRecycler);
    }

    public /* synthetic */ void lambda$events$5$HomeActivity(View view) {
        this.homePresenter.addQRAudioToFavorite(this, this.iAudioRecycler);
    }

    public /* synthetic */ void lambda$events$6$HomeActivity(boolean z) {
        this.homePresenter.retrieveFabSubMenuVisibility(z);
    }

    public /* synthetic */ void lambda$events$7$HomeActivity(View view) {
        this.homePresenter.closeNavigationDrawer(this.drawer);
    }

    public /* synthetic */ void lambda$events$8$HomeActivity(View view) {
        this.homePresenter.deleteAllSelectedQuizz();
    }

    public /* synthetic */ void lambda$events$9$HomeActivity(View view) throws IllegalStateException {
        this.homePresenter.playQRAudioPlayer(this.mediaPlayer, view);
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IHome
    public void launchQuizzGameThatNotFinished(String str, String str2, String str3) {
        Intent intent = new Intent(this, (Class<?>) QuizzGameActivity.class);
        intent.putExtra(CommonPresenter.KEY_QUIZZ_GAME_LIST_SELECTED, str);
        intent.putExtra(CommonPresenter.KEY_QUIZZ_GAME_TITLE_SELECTED, str2);
        intent.putExtra(CommonPresenter.KEY_QUIZZ_GROUPKEY_SELECTED, str3);
        startActivityForResult(intent, 5);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IHome
    public void launchQuizzGameToPlay(String str, String str2) {
        Intent intent = new Intent(this, (Class<?>) QuizzGameActivity.class);
        intent.putExtra(CommonPresenter.KEY_QUIZZ_GAME_LIST_SELECTED, str);
        intent.putExtra(CommonPresenter.KEY_QUIZZ_GAME_TITLE_SELECTED, str2);
        startActivityForResult(intent, 5);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IHome
    public void loadAudioDataToPlay(Ressource ressource) throws IllegalStateException {
        this.audio_player_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(this) { // from class: org.questionsreponses.View.Activities.HomeActivity.4
            final HomeActivity this$0;

            {
                this.this$0 = this;
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
                this.this$0.durationHandler.removeCallbacks(this.this$0.updateSeekBarTime);
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) throws IllegalStateException {
                this.this$0.durationHandler.removeCallbacks(this.this$0.updateSeekBarTime);
                this.this$0.finalTime = r0.mediaPlayer.getDuration();
                this.this$0.timeElapsed = seekBar.getProgress();
                this.this$0.mediaPlayer.seekTo((int) this.this$0.timeElapsed);
                long j = (long) this.this$0.timeElapsed;
                this.this$0.audio_player_time_progress.setText(String.format("%d min, %d sec", Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(j)), Long.valueOf(TimeUnit.MILLISECONDS.toSeconds(j) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(j)))));
                this.this$0.audio_player_time_end.setText(String.format("%d min, %d sec", Long.valueOf(TimeUnit.MILLISECONDS.toMinutes((long) this.this$0.finalTime)), Long.valueOf(TimeUnit.MILLISECONDS.toSeconds((long) this.this$0.finalTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) this.this$0.finalTime)))));
                this.this$0.durationHandler.postDelayed(this.this$0.updateSeekBarTime, 100L);
            }
        });
        CommonPresenter.stopAudioMediaPlayer(this.mediaPlayer);
        MediaPlayer mediaPlayer = new MediaPlayer();
        this.mediaPlayer = mediaPlayer;
        this.homePresenter.stopAllOtherMediaSound(mediaPlayer, ressource);
        this.mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener(this, ressource) { // from class: org.questionsreponses.View.Activities.HomeActivity.5
            final HomeActivity this$0;
            final Ressource val$audio;

            {
                this.this$0 = this;
                this.val$audio = ressource;
            }

            @Override // android.media.MediaPlayer.OnPreparedListener
            public void onPrepared(MediaPlayer mediaPlayer2) throws IllegalStateException {
                String dataFromSharePreferences;
                try {
                    this.this$0.mediaPlayer.start();
                    String dataFromSharePreferences2 = CommonPresenter.getDataFromSharePreferences(this.this$0.getApplicationContext(), CommonPresenter.KEY_NOTIF_AUDIO_TO_PLAYER_AUDIO_ID);
                    if (dataFromSharePreferences2 != null && !dataFromSharePreferences2.equalsIgnoreCase("0") && (dataFromSharePreferences = CommonPresenter.getDataFromSharePreferences(this.this$0.getApplicationContext(), CommonPresenter.KEY_NOTIF_AUDIO_TO_PLAYER_AUDIO_TIME_ELAPSED)) != null && !dataFromSharePreferences.equalsIgnoreCase("0") && Integer.parseInt(dataFromSharePreferences2) == this.val$audio.getId()) {
                        this.this$0.mediaPlayer.seekTo(Integer.parseInt(dataFromSharePreferences));
                        CommonPresenter.saveDataInSharePreferences(this.this$0.getApplicationContext(), CommonPresenter.KEY_NOTIF_AUDIO_TO_PLAYER_AUDIO_TIME_ELAPSED, "0");
                        CommonPresenter.saveDataInSharePreferences(this.this$0.getApplicationContext(), CommonPresenter.KEY_NOTIF_AUDIO_TO_PLAYER_AUDIO_ID, "0");
                    }
                    this.this$0.finalTime = this.this$0.mediaPlayer.getDuration();
                    this.this$0.timeElapsed = this.this$0.mediaPlayer.getCurrentPosition();
                    this.this$0.audio_player_seekbar.setProgress((int) this.this$0.timeElapsed);
                    this.this$0.audio_player_seekbar.setMax((int) this.this$0.finalTime);
                    this.this$0.durationHandler.postDelayed(this.this$0.updateSeekBarTime, 100L);
                    this.this$0.audio_player_titre.setText(this.val$audio.getTitre());
                    this.this$0.audio_player_soustitre.setText(CommonPresenter.changeFormatDate(this.val$audio.getDate()) + " | " + this.val$audio.getAuteur());
                    this.this$0.audio_player_play.setBackgroundResource(C0598R.drawable.btn_media_player_pause);
                } catch (Exception e) {
                }
            }
        });
        this.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener(this) { // from class: org.questionsreponses.View.Activities.HomeActivity.6
            final HomeActivity this$0;

            {
                this.this$0 = this;
            }

            @Override // android.media.MediaPlayer.OnCompletionListener
            public void onCompletion(MediaPlayer mediaPlayer2) {
                HomePresenter homePresenter = this.this$0.homePresenter;
                HomeActivity homeActivity = this.this$0;
                homePresenter.playQRAudioOnCompletion(homeActivity, homeActivity.iAudioRecycler, this.this$0.audio_player_play);
                Log.i("TAG_SET_ON_COMPLETION", this.this$0.iAudioRecycler == null ? "iAudioRecycler IS NULL" : "iAudioRecycler IS NOT NULL");
            }
        });
        this.mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener(this) { // from class: org.questionsreponses.View.Activities.HomeActivity.7
            final HomeActivity this$0;

            {
                this.this$0 = this;
            }

            @Override // android.media.MediaPlayer.OnErrorListener
            public boolean onError(MediaPlayer mediaPlayer2, int i, int i2) {
                return true;
            }
        });
        this.homePresenter.stopAudioNotification();
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IHome
    public void loadQuizzSave(List<Survey> list, int i) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, i);
        this.gridLayout = gridLayoutManager;
        this.recyclerSaveView.setLayoutManager(gridLayoutManager);
        this.recyclerSaveView.setHasFixedSize(true);
        QuizzSaveRecyclerAdapter quizzSaveRecyclerAdapter = new QuizzSaveRecyclerAdapter(list, this);
        this.quizzSaveAdapter = quizzSaveRecyclerAdapter;
        this.recyclerSaveView.setAdapter(quizzSaveRecyclerAdapter);
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IHome
    public void modifyTitleOfSaveQuizzGame(String str, int i) {
        this.homePresenter.buildTextViewToHtmlData(this.nav_quizz_title, str.toUpperCase() + " <sup>(<font color=\"#F5876E\">" + i + "</font>)</sup>");
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) throws Resources.NotFoundException {
        if (i == 5) {
            boolean z = -1;
            if (i2 == -1) {
                String stringExtra = intent.getStringExtra(CommonPresenter.KEY_QUIZZ_GAME_RETURN_DATA);
                int iHashCode = stringExtra.hashCode();
                if (iHashCode != -1693519744) {
                    if (iHashCode != -1641847436) {
                        if (iHashCode == -1340028425 && stringExtra.equals(CommonPresenter.KEY_QUIZZ_GAME_WAS_NOT_FINISHED_BEFORE)) {
                            z = 2;
                        }
                    } else if (stringExtra.equals(CommonPresenter.KEY_QUIZZ_GAME_IS_FINISHED)) {
                        z = false;
                    }
                } else if (stringExtra.equals(CommonPresenter.KEY_QUIZZ_GAME_IS_NOT_FINISHED)) {
                    z = true;
                }
                if (!z) {
                    this.homePresenter.showQuizzFinished(this, false);
                } else if (z || z == 2) {
                    this.homePresenter.showQuizzNotFinished(this, false);
                }
            } else if (i2 == 0) {
                Log.i("TAG_QUIZZ_GAME_SELECTED", "Activity.RESULT_CANCELED = " + i);
                Log.i("TAG_QUIZZ_GAME_SELECTED", "Activity.RESULT_CANCELED = " + i2);
            }
        }
        super.onActivityResult(i, i2, intent);
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IHome
    public void onAudioSelected(Ressource ressource, int i) throws Resources.NotFoundException {
        this.isAudioSelected = true;
        this.homePresenter.playQRAudioPlayer(this, ressource, i);
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onBackPressed() {
        this.homePresenter.confirmBeforeClosingApp(this);
    }

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.SupportActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C0598R.layout.activity_home);
        HomePresenter homePresenter = new HomePresenter(this);
        this.homePresenter = homePresenter;
        homePresenter.loadHomeData(this);
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0598R.menu.menu_home, menu);
        this.action_quizz_level_1 = menu.findItem(C0598R.id.action_quizz_level_1);
        this.action_quizz_level_2 = menu.findItem(C0598R.id.action_quizz_level_2);
        this.action_quizz_level_3 = menu.findItem(C0598R.id.action_quizz_level_3);
        this.action_quizz_level_4 = menu.findItem(C0598R.id.action_quizz_level_4);
        this.action_quizz_level_5 = menu.findItem(C0598R.id.action_quizz_level_5);
        this.action_quizz_level_6 = menu.findItem(C0598R.id.action_quizz_level_6);
        this.action_quizz_level_7 = menu.findItem(C0598R.id.action_quizz_level_7);
        this.action_quizz_level_8 = menu.findItem(C0598R.id.action_quizz_level_8);
        this.action_quizz_level_9 = menu.findItem(C0598R.id.action_quizz_level_9);
        this.action_quizz_level_10 = menu.findItem(C0598R.id.action_quizz_level_10);
        return true;
    }

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        this.homePresenter.onActivityDestroyed(this.textToSpeech);
        super.onDestroy();
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IHome
    public void onHomeIAudioRecycler(HomeView.IAudioRecycler iAudioRecycler) {
        this.iAudioRecycler = iAudioRecycler;
    }

    @Override // android.speech.tts.TextToSpeech.OnInitListener
    public void onInit(int i) {
        this.homePresenter.initializeTextToSpeech(i, this.textToSpeech);
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IHome
    public void onMenuQuizzSelected(String str, int i) {
        this.homePresenter.playQuizzGame(this, str, i);
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) throws Resources.NotFoundException {
        int itemId = menuItem.getItemId();
        if (itemId == 2131296281) {
            this.homePresenter.showQuizzFinished(this, true);
            this.QUIZZ_END_IS_SELECTED = true;
        } else if (itemId != 2131296299) {
            switch (itemId) {
                case C0598R.id.action_quizz_level_1 /* 2131296283 */:
                    this.homePresenter.showQuizzSpiritualGrowth(1);
                    break;
                case C0598R.id.action_quizz_level_10 /* 2131296284 */:
                    this.homePresenter.showQuizzSpiritualGrowth(10);
                    break;
                case C0598R.id.action_quizz_level_2 /* 2131296285 */:
                    this.homePresenter.showQuizzSpiritualGrowth(2);
                    break;
                case C0598R.id.action_quizz_level_3 /* 2131296286 */:
                    this.homePresenter.showQuizzSpiritualGrowth(3);
                    break;
                case C0598R.id.action_quizz_level_4 /* 2131296287 */:
                    this.homePresenter.showQuizzSpiritualGrowth(4);
                    break;
                case C0598R.id.action_quizz_level_5 /* 2131296288 */:
                    this.homePresenter.showQuizzSpiritualGrowth(5);
                    break;
                case C0598R.id.action_quizz_level_6 /* 2131296289 */:
                    this.homePresenter.showQuizzSpiritualGrowth(6);
                    break;
                case C0598R.id.action_quizz_level_7 /* 2131296290 */:
                    this.homePresenter.showQuizzSpiritualGrowth(7);
                    break;
                case C0598R.id.action_quizz_level_8 /* 2131296291 */:
                    this.homePresenter.showQuizzSpiritualGrowth(8);
                    break;
                case C0598R.id.action_quizz_level_9 /* 2131296292 */:
                    this.homePresenter.showQuizzSpiritualGrowth(9);
                    break;
            }
        } else {
            this.homePresenter.showQuizzNotFinished(this, true);
            this.QUIZZ_END_IS_SELECTED = false;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity, android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        if (i != 123) {
            return;
        }
        if (iArr.length > 0) {
            if (iArr[0] == 0) {
                for (int i2 = 0; i2 < CommonPresenter.getFolderName().length; i2++) {
                    CommonPresenter.createFolder(this, CommonPresenter.getFolderName()[i2]);
                }
                return;
            }
        }
        Toast.makeText(this, getResources().getString(C0598R.string.lb_storage_file_require), 1).show();
    }

    @Override // android.app.Activity
    protected void onRestart() {
        super.onRestart();
        this.homePresenter.verifyQuizzSpiritualGrowthLevel(this);
        this.homePresenter.refreshQuizzEndOrNotEnd(this, this.QUIZZ_END_IS_SELECTED);
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IHome
    public void openOrCloseNavigationDrawer() {
        this.homePresenter.manageNavigationDrawer(this.drawer);
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IHome
    public void parameter(View view) {
        this.homePresenter.retrieveUserAction(this, view);
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IHome
    public void playNotification() {
        Intent intent = new Intent(this, (Class<?>) PlayerAudioService.class);
        intent.setAction(NotificationView.ACTION.STARTFOREGROUND_ACTION);
        startService(intent);
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IHome
    public void playerProgressBarVisibility(int i) {
        this.audio_player_progressbar.setVisibility(i);
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IHome
    public void privacyPolicy(View view) {
        this.homePresenter.retrieveUserAction(this, view);
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IHome
    public void progressBarVisibility(int i) {
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IHome
    public void readTextFromTextToSpeech(String str) {
        try {
            this.textToSpeech.speak(str, 0, null);
            this.textToSpeech.setPitch(1.3f);
            this.textToSpeech.setSpeechRate(0.7f);
        } catch (Exception e) {
        }
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IHome
    public ArrayList<Quizz> retrieveAllQuizzData() {
        return this.quizzList;
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IHome
    public List<Ressource> retrieveResourceList(String str) {
        return this.resourceList.containsKey(str) ? this.resourceList.get(str) : null;
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IHome
    public void searchMedia(View view) {
        this.homePresenter.retrieveUserAction(this, view);
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IHome
    public void setIQuizzSaveRecyclerReference(HomeView.IQuizzSaveRecycler iQuizzSaveRecycler) {
        this.iQuizzSaveRecycler = iQuizzSaveRecycler;
    }

    public void setiAudioFrag(HomeView.IAudioFrag iAudioFrag) {
        this.iAudioFrag = iAudioFrag;
    }

    public void setiQuizzFrag(HomeView.IQuizzFrag iQuizzFrag) {
        this.iQuizzFrag = iQuizzFrag;
    }

    public void setiVideoFrag(HomeView.IVideoFrag iVideoFrag) {
        this.iVideoFrag = iVideoFrag;
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IHome
    public void shareApp(View view) {
        this.homePresenter.retrieveUserAction(this, view);
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IHome
    public void showQuizzSpiritualGrowth(int i) {
        Intent intent = new Intent(this, (Class<?>) SpiritualGrowthActivity.class);
        intent.putExtra(CommonPresenter.KEY_QUIZZ_SPIRITUAL_GROWTH_LEVEL_SELECTED, BuildConfig.FLAVOR + i);
        startActivityForResult(intent, 15);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IHome
    public void stopNotification() {
        Intent intent = new Intent(this, (Class<?>) PlayerAudioService.class);
        intent.setAction(NotificationView.ACTION.STOPFOREGROUND_ACTION);
        startService(intent);
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IHome
    public void stopOtherMediaPlayerSound(MediaPlayer mediaPlayer, Ressource ressource) throws IllegalStateException, IOException, SecurityException, IllegalArgumentException {
        if (((AudioManager) getSystemService(Format.AUDIO)).requestAudioFocus(new AudioManager.OnAudioFocusChangeListener(this) { // from class: org.questionsreponses.View.Activities.HomeActivity.9
            final HomeActivity this$0;

            {
                this.this$0 = this;
            }

            @Override // android.media.AudioManager.OnAudioFocusChangeListener
            public void onAudioFocusChange(int i) {
                if (i == -3) {
                    Log.i("TAG_AUDIO_PAYER", "AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");
                    return;
                }
                if (i == -2) {
                    Log.i("TAG_AUDIO_PAYER", "AudioManager.AUDIOFOCUS_LOSS_TRANSIENT");
                } else if (i == -1) {
                    Log.i("TAG_AUDIO_PAYER", "AudioManager.AUDIOFOCUS_LOSS");
                } else {
                    if (i != 1) {
                        return;
                    }
                    Log.i("TAG_AUDIO_PAYER", "AudioManager.AUDIOFOCUS_GAIN");
                }
            }
        }, 3, 1) == 1) {
            try {
                mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(ressource.getUrlacces() + ressource.getSrc()));
                mediaPlayer.prepareAsync();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IHome
    public void textMediaPlayInfoLoading() {
        this.audio_player_titre.setText(getResources().getString(C0598R.string.lb_audio_player_title));
        this.audio_player_soustitre.setText(getResources().getString(C0598R.string.lb_audio_player_date_auteur));
        this.audio_player_play.setBackgroundResource(C0598R.drawable.btn_media_player_play);
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IHome
    public void updateApplication(View view) {
        this.homePresenter.retrieveUserAction(this, view);
    }

    @Override // org.questionsreponses.View.Interfaces.HomeView.IHome
    public void youtubeDownloader(View view) {
        this.homePresenter.retrieveUserAction(this, view);
    }
}
