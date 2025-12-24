package org.questionsreponses.View.Activities;

import android.R;
import android.content.Intent;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.github.clans.fab.FloatingActionButton;
import com.github.kiulian.downloader.model.videos.formats.Format;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.questionsreponses.C0598R;
import org.questionsreponses.Model.Ressource;
import org.questionsreponses.Presenter.CommonPresenter;
import org.questionsreponses.Presenter.SearchResultPresenter;
import org.questionsreponses.View.Adapters.SearchResultRecyclerAdapter;
import org.questionsreponses.View.Interfaces.NotificationView;
import org.questionsreponses.View.Interfaces.SearchResultView;
import org.questionsreponses.View.Services.PlayerAudioService;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Activities/SearchResultActivity.class */
public class SearchResultActivity extends AppCompatActivity implements SearchResultView.ISearchResult {
    private SearchResultRecyclerAdapter adapter;
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
    private FloatingActionButton fab_player_download;
    private FloatingActionButton fab_player_favorite;
    private FloatingActionButton fab_player_notification;
    private FloatingActionButton fab_player_share_app;
    private FloatingActionButton fab_player_volume;
    private SearchResultView.ISearchResultRecycler iSearchResultRecycler;
    private MediaPlayer mediaPlayer;
    private ProgressBar progressSearch;
    private RecyclerView recyclerView;
    private SearchResultPresenter resultPresenter;
    private ImageButton searchBack;
    private TextView searchSubtitle;
    private double timeElapsed = 0.0d;
    private double finalTime = 0.0d;
    private Handler durationHandler = new Handler();
    private Runnable updateSeekBarTime = new Runnable(this) { // from class: org.questionsreponses.View.Activities.SearchResultActivity.16
        final SearchResultActivity this$0;

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

    @Override // org.questionsreponses.View.Interfaces.SearchResultView.ISearchResult
    public void askPermissionToSaveFile() {
        if (ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, CommonPresenter.VALUE_PERMISSION_TO_SAVE_FILE);
        }
    }

    @Override // org.questionsreponses.View.Interfaces.SearchResultView.ISearchResult
    public void audioPlayerVisibility(int i) {
        this.audioPlayerContent.setVisibility(i);
    }

    @Override // org.questionsreponses.View.Interfaces.SearchResultView.ISearchResult
    public void closeActivity() throws IllegalStateException {
        CommonPresenter.stopAudioMediaPlayer(this.mediaPlayer);
        finish();
    }

    @Override // org.questionsreponses.View.Interfaces.SearchResultView.ISearchResult
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

    @Override // org.questionsreponses.View.Interfaces.SearchResultView.ISearchResult
    public void events() {
        this.searchBack.setOnClickListener(new View.OnClickListener(this) { // from class: org.questionsreponses.View.Activities.SearchResultActivity.1
            final SearchResultActivity this$0;

            {
                this.this$0 = this;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                this.this$0.resultPresenter.rettrieveUserAction(view);
            }
        });
        this.fab_player_notification.setOnClickListener(new View.OnClickListener(this) { // from class: org.questionsreponses.View.Activities.SearchResultActivity.2
            final SearchResultActivity this$0;

            {
                this.this$0 = this;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                this.this$0.resultPresenter.retrieveUserAction(view, this.this$0.iSearchResultRecycler);
            }
        });
        this.btn_player_close.setOnClickListener(new View.OnClickListener(this) { // from class: org.questionsreponses.View.Activities.SearchResultActivity.3
            final SearchResultActivity this$0;

            {
                this.this$0 = this;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                this.this$0.resultPresenter.retrieveUserAction(view, this.this$0.iSearchResultRecycler);
            }
        });
        this.fab_player_volume.setOnClickListener(new View.OnClickListener(this) { // from class: org.questionsreponses.View.Activities.SearchResultActivity.4
            final SearchResultActivity this$0;

            {
                this.this$0 = this;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                this.this$0.resultPresenter.retrieveUserAction(view, this.this$0.iSearchResultRecycler);
            }
        });
        this.fab_player_download.setOnClickListener(new View.OnClickListener(this) { // from class: org.questionsreponses.View.Activities.SearchResultActivity.5
            final SearchResultActivity this$0;

            {
                this.this$0 = this;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                this.this$0.resultPresenter.downloadQRAudio(view.getContext(), this.this$0.iSearchResultRecycler);
            }
        });
        this.fab_player_share_app.setOnClickListener(new View.OnClickListener(this) { // from class: org.questionsreponses.View.Activities.SearchResultActivity.6
            final SearchResultActivity this$0;

            {
                this.this$0 = this;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                this.this$0.resultPresenter.shareQRAudio(view.getContext(), this.this$0.iSearchResultRecycler);
            }
        });
        this.fab_player_favorite.setOnClickListener(new View.OnClickListener(this) { // from class: org.questionsreponses.View.Activities.SearchResultActivity.7
            final SearchResultActivity this$0;

            {
                this.this$0 = this;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                this.this$0.resultPresenter.addQRAudioToFavorite(view.getContext(), this.this$0.iSearchResultRecycler);
            }
        });
        this.audio_player_play.setOnClickListener(new View.OnClickListener(this) { // from class: org.questionsreponses.View.Activities.SearchResultActivity.8
            final SearchResultActivity this$0;

            {
                this.this$0 = this;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) throws IllegalStateException {
                this.this$0.resultPresenter.playQRAudioPlayer(this.this$0.mediaPlayer, view);
            }
        });
        this.audio_player_next.setOnClickListener(new View.OnClickListener(this) { // from class: org.questionsreponses.View.Activities.SearchResultActivity.9
            final SearchResultActivity this$0;

            {
                this.this$0 = this;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                this.this$0.resultPresenter.playNextQRAudio(this.this$0.iSearchResultRecycler);
            }
        });
        this.audio_player_previous.setOnClickListener(new View.OnClickListener(this) { // from class: org.questionsreponses.View.Activities.SearchResultActivity.10
            final SearchResultActivity this$0;

            {
                this.this$0 = this;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                this.this$0.resultPresenter.playPreviousQRAudio(this.this$0.iSearchResultRecycler);
            }
        });
    }

    @Override // org.questionsreponses.View.Interfaces.SearchResultView.ISearchResult
    public void hideHeader() {
        getSupportActionBar().hide();
    }

    @Override // org.questionsreponses.View.Interfaces.SearchResultView.ISearchResult
    public void initialize() {
        this.recyclerView = (RecyclerView) findViewById(C0598R.id.search_recycler);
        this.searchSubtitle = (TextView) findViewById(C0598R.id.search_subtitle);
        this.searchBack = (ImageButton) findViewById(C0598R.id.search_back);
        this.progressSearch = (ProgressBar) findViewById(C0598R.id.progress_search);
        this.audioPlayerContent = findViewById(C0598R.id.audioPlayerContent);
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

    @Override // org.questionsreponses.View.Interfaces.SearchResultView.ISearchResult
    public MediaPlayer instanceMediaPlayer() {
        return this.mediaPlayer;
    }

    @Override // org.questionsreponses.View.Interfaces.SearchResultView.ISearchResult
    public void launchNotificationPlayer() {
        Intent intent = new Intent(this, (Class<?>) PlayerAudioService.class);
        intent.setAction(NotificationView.ACTION.STARTFOREGROUND_ACTION);
        startService(intent);
    }

    @Override // org.questionsreponses.View.Interfaces.SearchResultView.ISearchResult
    public void launchVideoToPlay(Ressource ressource, int i) {
        Intent intent = new Intent(this, (Class<?>) VideoPlayerActivity.class);
        intent.putExtra(CommonPresenter.KEY_VIDEO_PLAYER_SEND_DATA, ressource);
        intent.putExtra(CommonPresenter.VALUE_POSITION_VIDEO_SELECTED, i);
        startActivityForResult(intent, 10);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override // org.questionsreponses.View.Interfaces.SearchResultView.ISearchResult
    public void loadAudioDataToPlay(Ressource ressource) throws IllegalStateException {
        this.audio_player_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(this) { // from class: org.questionsreponses.View.Activities.SearchResultActivity.12
            final SearchResultActivity this$0;

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
        this.resultPresenter.stopAllOtherMediaSound(mediaPlayer, ressource);
        this.mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener(this, ressource) { // from class: org.questionsreponses.View.Activities.SearchResultActivity.13
            final SearchResultActivity this$0;
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
        this.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener(this) { // from class: org.questionsreponses.View.Activities.SearchResultActivity.14
            final SearchResultActivity this$0;

            {
                this.this$0 = this;
            }

            @Override // android.media.MediaPlayer.OnCompletionListener
            public void onCompletion(MediaPlayer mediaPlayer2) {
                SearchResultPresenter searchResultPresenter = this.this$0.resultPresenter;
                SearchResultActivity searchResultActivity = this.this$0;
                searchResultPresenter.playQRAudioOnCompletion(searchResultActivity, searchResultActivity.iSearchResultRecycler, this.this$0.audio_player_play);
                Log.i("TAG_SET_ON_COMPLETION", this.this$0.iSearchResultRecycler == null ? "iSearchResultRecycler IS NULL" : "iSearchResultRecycler IS NOT NULL");
            }
        });
        this.mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener(this) { // from class: org.questionsreponses.View.Activities.SearchResultActivity.15
            final SearchResultActivity this$0;

            {
                this.this$0 = this;
            }

            @Override // android.media.MediaPlayer.OnErrorListener
            public boolean onError(MediaPlayer mediaPlayer2, int i, int i2) {
                return true;
            }
        });
        this.resultPresenter.stopAudioNotification();
    }

    @Override // org.questionsreponses.View.Interfaces.SearchResultView.ISearchResult
    public void loadSearchResult(List<Ressource> list, int i, int i2, String str) {
        this.recyclerView.setLayoutManager(new GridLayoutManager(this, i));
        this.recyclerView.setHasFixedSize(true);
        SearchResultRecyclerAdapter searchResultRecyclerAdapter = new SearchResultRecyclerAdapter(this, list, str, this);
        this.adapter = searchResultRecyclerAdapter;
        this.recyclerView.setAdapter(searchResultRecyclerAdapter);
        this.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(this) { // from class: org.questionsreponses.View.Activities.SearchResultActivity.11
            final SearchResultActivity this$0;

            {
                this.this$0 = this;
            }

            @Override // android.support.v7.widget.RecyclerView.OnScrollListener
            public void onScrolled(RecyclerView recyclerView, int i3, int i4) {
                super.onScrolled(recyclerView, i3, i4);
            }
        });
        this.recyclerView.scrollToPosition(i2);
    }

    @Override // org.questionsreponses.View.Interfaces.SearchResultView.ISearchResult
    public void onActivityISearchResultRecycler(SearchResultView.ISearchResultRecycler iSearchResultRecycler) {
        this.iSearchResultRecycler = iSearchResultRecycler;
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == 10) {
            boolean z = -1;
            if (i2 == -1) {
                String stringExtra = intent.getStringExtra(CommonPresenter.KEY_VIDEO_PLAYER_RETURN_DATA);
                int iHashCode = stringExtra.hashCode();
                if (iHashCode != -1462737076) {
                    if (iHashCode == -809911600 && stringExtra.equals(CommonPresenter.VALUE_VIDEO_PLAY_PREVIOUS)) {
                        z = true;
                    }
                } else if (stringExtra.equals(CommonPresenter.VALUE_VIDEO_PLAY_NEXT)) {
                    z = false;
                }
                if (!z) {
                    this.resultPresenter.playNextQRVideo(this.iSearchResultRecycler);
                } else if (z) {
                    this.resultPresenter.playPreviousQRVideo(this.iSearchResultRecycler);
                }
            } else if (i2 == 0) {
                Log.i("TAG_VIDEO_SERACH", "Activity.RESULT_CANCELED = " + i);
                Log.i("TAG_VIDEO_SERACH", "Activity.RESULT_CANCELED = " + i2);
            }
        }
        super.onActivityResult(i, i2, intent);
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onBackPressed() throws IllegalStateException {
        CommonPresenter.stopAudioMediaPlayer(this.mediaPlayer);
        super.onBackPressed();
    }

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.SupportActivity, android.app.Activity
    protected void onCreate(Bundle bundle) throws Resources.NotFoundException {
        super.onCreate(bundle);
        setContentView(C0598R.layout.activity_search_result);
        SearchResultPresenter searchResultPresenter = new SearchResultPresenter(this);
        this.resultPresenter = searchResultPresenter;
        searchResultPresenter.loadSearchResultData(this, getIntent());
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

    @Override // org.questionsreponses.View.Interfaces.SearchResultView.ISearchResult
    public void playNotification() {
        Intent intent = new Intent(this, (Class<?>) PlayerAudioService.class);
        intent.setAction(NotificationView.ACTION.STARTFOREGROUND_ACTION);
        startService(intent);
    }

    @Override // org.questionsreponses.View.Interfaces.SearchResultView.ISearchResult
    public void playerProgressBarVisibility(int i) {
        this.audio_player_progressbar.setVisibility(i);
    }

    @Override // org.questionsreponses.View.Interfaces.SearchResultView.ISearchResult
    public void progressBarVisibility(int i) {
        this.progressSearch.setVisibility(i);
    }

    @Override // org.questionsreponses.View.Interfaces.SearchResultView.ISearchResult
    public void scrollResourceDataToPosition(int i) {
        this.recyclerView.scrollToPosition(i);
    }

    @Override // org.questionsreponses.View.Interfaces.SearchResultView.ISearchResult
    public void setSubTitleValue(String str) {
        this.searchSubtitle.setText(str);
    }

    @Override // org.questionsreponses.View.Interfaces.SearchResultView.ISearchResult
    public void stopNotification() {
        Intent intent = new Intent(this, (Class<?>) PlayerAudioService.class);
        intent.setAction(NotificationView.ACTION.STOPFOREGROUND_ACTION);
        startService(intent);
    }

    @Override // org.questionsreponses.View.Interfaces.SearchResultView.ISearchResult
    public void stopOtherMediaPlayerSound(MediaPlayer mediaPlayer, Ressource ressource) throws IllegalStateException, IOException, SecurityException, IllegalArgumentException {
        if (((AudioManager) getSystemService(Format.AUDIO)).requestAudioFocus(new AudioManager.OnAudioFocusChangeListener(this) { // from class: org.questionsreponses.View.Activities.SearchResultActivity.17
            final SearchResultActivity this$0;

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
            Log.i("TAG_AUDIO_PAYER", "AudioManager.AUDIOFOCUS_REQUEST_GRANTED");
            try {
                mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(ressource.getUrlacces() + ressource.getSrc()));
                mediaPlayer.prepareAsync();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override // org.questionsreponses.View.Interfaces.SearchResultView.ISearchResult
    public void textMediaPlayInfoLoading() {
        this.audio_player_titre.setText(getResources().getString(C0598R.string.lb_audio_player_title));
        this.audio_player_soustitre.setText(getResources().getString(C0598R.string.lb_audio_player_date_auteur));
        this.audio_player_play.setBackgroundResource(C0598R.drawable.btn_media_player_play);
    }
}
