package org.questionsreponses.View.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import com.github.clans.fab.FloatingActionButton;
import com.github.kiulian.downloader.model.videos.formats.Format;
import org.questionsreponses.C0598R;
import org.questionsreponses.Model.Ressource;
import org.questionsreponses.Presenter.CommonPresenter;
import org.questionsreponses.Presenter.VideoPlayerPresenter;
import org.questionsreponses.View.Interfaces.NotificationView;
import org.questionsreponses.View.Interfaces.VideoPlayerView;
import org.questionsreponses.View.Services.PlayerAudioService;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Activities/VideoPlayerActivity.class */
public class VideoPlayerActivity extends AppCompatActivity implements VideoPlayerView.IVideoPlayer {
    private ImageButton btn_video_nav_left;
    private ImageButton btn_video_nav_right;
    private CountDownTimer downTimer;
    private FloatingActionButton fab_player_down;
    private FloatingActionButton fab_player_download;
    private FloatingActionButton fab_player_favorite;
    private View fab_player_layout;
    private FloatingActionButton fab_player_share_app;
    private FloatingActionButton fab_player_volume;
    private MediaController mediaController;
    private VideoPlayerPresenter playerPresenter;
    private VideoView player_video;
    private ProgressBar progress_player_video;
    private TextView title_video;
    private boolean widgetsIsOpened;

    @Override // org.questionsreponses.View.Interfaces.VideoPlayerView.IVideoPlayer
    public void addQRVideoToFavorite(Context context) {
        this.playerPresenter.saveRessourceVideoData(context, (Ressource) getIntent().getSerializableExtra(CommonPresenter.KEY_VIDEO_PLAYER_SEND_DATA));
    }

    @Override // org.questionsreponses.View.Interfaces.VideoPlayerView.IVideoPlayer
    public void btnNavigationVisibility(int i) {
        this.btn_video_nav_left.setVisibility(i);
        this.btn_video_nav_right.setVisibility(i);
    }

    @Override // org.questionsreponses.View.Interfaces.VideoPlayerView.IVideoPlayer
    public void closeActivity() {
        finish();
    }

    @Override // org.questionsreponses.View.Interfaces.VideoPlayerView.IVideoPlayer
    public void displayPlayer(Ressource ressource, int i, int i2) {
        this.title_video.setText(ressource.getTitre());
        this.player_video.setVideoURI(Uri.parse(ressource.getUrlacces() + ressource.getSrc()));
        this.player_video.setMinimumWidth(i);
        this.player_video.setMinimumHeight(i2);
        this.player_video.setOnPreparedListener(new MediaPlayer.OnPreparedListener(this) { // from class: org.questionsreponses.View.Activities.VideoPlayerActivity.8
            final VideoPlayerActivity this$0;

            {
                this.this$0 = this;
            }

            @Override // android.media.MediaPlayer.OnPreparedListener
            public void onPrepared(MediaPlayer mediaPlayer) {
                this.this$0.player_video.start();
                this.this$0.player_video.requestFocus();
                this.this$0.player_video.setBackgroundColor(0);
                this.this$0.playerPresenter.managePlayerVisibility();
                int childCount = this.this$0.mediaController.getChildCount();
                for (int i3 = 0; i3 < childCount; i3++) {
                    this.this$0.mediaController.getChildAt(i3).setVisibility(0);
                }
            }
        });
        this.player_video.setOnCompletionListener(new MediaPlayer.OnCompletionListener(this) { // from class: org.questionsreponses.View.Activities.VideoPlayerActivity.9
            final VideoPlayerActivity this$0;

            {
                this.this$0 = this;
            }

            @Override // android.media.MediaPlayer.OnCompletionListener
            public void onCompletion(MediaPlayer mediaPlayer) {
                this.this$0.playerPresenter.retrieveOnCompletionAction(this.this$0);
            }
        });
        this.player_video.setOnErrorListener(new MediaPlayer.OnErrorListener(this) { // from class: org.questionsreponses.View.Activities.VideoPlayerActivity.10
            final VideoPlayerActivity this$0;

            {
                this.this$0 = this;
            }

            @Override // android.media.MediaPlayer.OnErrorListener
            public boolean onError(MediaPlayer mediaPlayer, int i3, int i4) {
                this.this$0.progress_player_video.setVisibility(8);
                this.this$0.title_video.setVisibility(8);
                return true;
            }
        });
    }

    @Override // org.questionsreponses.View.Interfaces.VideoPlayerView.IVideoPlayer
    public void downloadQRVideo(Context context) {
        Ressource ressource = (Ressource) getIntent().getSerializableExtra(CommonPresenter.KEY_VIDEO_PLAYER_SEND_DATA);
        CommonPresenter.getFileByDownloadManager(context, ressource.getUrlacces() + ressource.getSrc(), ressource.getSrc(), "QR-APP-DOWNLOADER (" + ressource.getDuree() + " | " + ressource.getAuteur() + ")", Format.VIDEO);
        Toast.makeText(context, context.getResources().getString(C0598R.string.lb_downloading), 0).show();
    }

    @Override // org.questionsreponses.View.Interfaces.VideoPlayerView.IVideoPlayer
    public void events() {
        this.fab_player_download.setOnClickListener(new View.OnClickListener(this) { // from class: org.questionsreponses.View.Activities.VideoPlayerActivity.1
            final VideoPlayerActivity this$0;

            {
                this.this$0 = this;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                this.this$0.playerPresenter.retrieveUserAction(this.this$0, view);
            }
        });
        this.fab_player_share_app.setOnClickListener(new View.OnClickListener(this) { // from class: org.questionsreponses.View.Activities.VideoPlayerActivity.2
            final VideoPlayerActivity this$0;

            {
                this.this$0 = this;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                this.this$0.playerPresenter.retrieveUserAction(this.this$0, view);
            }
        });
        this.fab_player_favorite.setOnClickListener(new View.OnClickListener(this) { // from class: org.questionsreponses.View.Activities.VideoPlayerActivity.3
            final VideoPlayerActivity this$0;

            {
                this.this$0 = this;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                this.this$0.playerPresenter.retrieveUserAction(this.this$0, view);
            }
        });
        this.fab_player_volume.setOnClickListener(new View.OnClickListener(this) { // from class: org.questionsreponses.View.Activities.VideoPlayerActivity.4
            final VideoPlayerActivity this$0;

            {
                this.this$0 = this;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                this.this$0.playerPresenter.retrieveUserAction(this.this$0, view);
            }
        });
        this.fab_player_down.setOnClickListener(new View.OnClickListener(this) { // from class: org.questionsreponses.View.Activities.VideoPlayerActivity.5
            final VideoPlayerActivity this$0;

            {
                this.this$0 = this;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                this.this$0.playerPresenter.retrieveUserAction(this.this$0, view);
            }
        });
        this.btn_video_nav_left.setOnClickListener(new View.OnClickListener(this) { // from class: org.questionsreponses.View.Activities.VideoPlayerActivity.6
            final VideoPlayerActivity this$0;

            {
                this.this$0 = this;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                this.this$0.playerPresenter.retrieveUserAction(this.this$0, view);
            }
        });
        this.btn_video_nav_right.setOnClickListener(new View.OnClickListener(this) { // from class: org.questionsreponses.View.Activities.VideoPlayerActivity.7
            final VideoPlayerActivity this$0;

            {
                this.this$0 = this;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                this.this$0.playerPresenter.retrieveUserAction(this.this$0, view);
            }
        });
    }

    @Override // org.questionsreponses.View.Interfaces.VideoPlayerView.IVideoPlayer
    public void fabVisibility(int i) {
        this.fab_player_layout.setVisibility(i);
    }

    @Override // org.questionsreponses.View.Interfaces.VideoPlayerView.IVideoPlayer
    public void headerVisibility(int i) {
        this.title_video.setVisibility(i);
    }

    @Override // org.questionsreponses.View.Interfaces.VideoPlayerView.IVideoPlayer
    public void hideHeader() {
        getSupportActionBar().hide();
    }

    @Override // org.questionsreponses.View.Interfaces.VideoPlayerView.IVideoPlayer
    public void initialize() {
        this.fab_player_layout = findViewById(C0598R.id.fab_player_layout);
        this.title_video = (TextView) findViewById(C0598R.id.titre_video);
        this.progress_player_video = (ProgressBar) findViewById(C0598R.id.progress_player_video);
        this.player_video = (VideoView) findViewById(C0598R.id.player_video);
        MediaController mediaController = new MediaController((Context) this, false);
        this.mediaController = mediaController;
        this.player_video.setMediaController(mediaController);
        this.btn_video_nav_left = (ImageButton) findViewById(C0598R.id.btn_video_nav_left);
        this.btn_video_nav_right = (ImageButton) findViewById(C0598R.id.btn_video_nav_right);
        this.fab_player_download = (FloatingActionButton) findViewById(C0598R.id.fab_player_download);
        this.fab_player_share_app = (FloatingActionButton) findViewById(C0598R.id.fab_player_share_app);
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(C0598R.id.fab_player_favorite);
        this.fab_player_favorite = floatingActionButton;
        floatingActionButton.setVisibility(8);
        this.fab_player_volume = (FloatingActionButton) findViewById(C0598R.id.fab_player_volume);
        this.fab_player_down = (FloatingActionButton) findViewById(C0598R.id.fab_player_down);
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onBackPressed() {
        CommonPresenter.cancelCountDownTimer(this.downTimer);
        CommonPresenter.stopVideoViewPlayer(this.player_video);
        super.onBackPressed();
    }

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.SupportActivity, android.app.Activity
    protected void onCreate(Bundle bundle) throws Resources.NotFoundException {
        super.onCreate(bundle);
        getWindow().setFlags(1024, 1024);
        setContentView(C0598R.layout.activity_video_player);
        VideoPlayerPresenter videoPlayerPresenter = new VideoPlayerPresenter(this);
        this.playerPresenter = videoPlayerPresenter;
        videoPlayerPresenter.loadVideoPlayerData(this, getIntent());
    }

    @Override // android.app.Activity
    public boolean onTouchEvent(MotionEvent motionEvent) {
        this.playerPresenter.showWidgetsOnTouchEvent(this.widgetsIsOpened);
        return super.onTouchEvent(motionEvent);
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        if (Build.VERSION.SDK_INT < 19 || !z) {
            return;
        }
        getWindow().getDecorView().setSystemUiVisibility(5126);
    }

    @Override // org.questionsreponses.View.Interfaces.VideoPlayerView.IVideoPlayer
    public void pauseNotificationAudio() {
        Intent intent = new Intent(this, (Class<?>) PlayerAudioService.class);
        intent.setAction(NotificationView.ACTION.PAUSE_ACTION);
        startService(intent);
    }

    @Override // org.questionsreponses.View.Interfaces.VideoPlayerView.IVideoPlayer
    public void playNextQRVideo() {
        Intent intent = new Intent();
        intent.putExtra(CommonPresenter.KEY_VIDEO_PLAYER_RETURN_DATA, CommonPresenter.VALUE_VIDEO_PLAY_NEXT);
        setResult(-1, intent);
        finish();
    }

    @Override // org.questionsreponses.View.Interfaces.VideoPlayerView.IVideoPlayer
    public void playPreviousQRVideo() {
        Intent intent = new Intent();
        intent.putExtra(CommonPresenter.KEY_VIDEO_PLAYER_RETURN_DATA, CommonPresenter.VALUE_VIDEO_PLAY_PREVIOUS);
        setResult(-1, intent);
        finish();
    }

    @Override // org.questionsreponses.View.Interfaces.VideoPlayerView.IVideoPlayer
    public void progressBarVisibility(int i) {
        this.progress_player_video.setVisibility(i);
    }

    @Override // org.questionsreponses.View.Interfaces.VideoPlayerView.IVideoPlayer
    public void shareQRVideo(Context context) throws Resources.NotFoundException {
        CommonPresenter.shareRessource(context, (Ressource) getIntent().getSerializableExtra(CommonPresenter.KEY_VIDEO_PLAYER_SEND_DATA), Format.VIDEO);
    }

    /* JADX WARN: Type inference failed for: r1v6, types: [org.questionsreponses.View.Activities.VideoPlayerActivity$11] */
    @Override // org.questionsreponses.View.Interfaces.VideoPlayerView.IVideoPlayer
    public void showPlayerWidgetsOnTouch() {
        this.progress_player_video.setVisibility(8);
        this.title_video.setVisibility(0);
        this.fab_player_layout.setVisibility(0);
        this.btn_video_nav_left.setVisibility(0);
        this.btn_video_nav_right.setVisibility(0);
        this.widgetsIsOpened = true;
        this.downTimer = new CountDownTimer(this, 3000L, 1000L) { // from class: org.questionsreponses.View.Activities.VideoPlayerActivity.11
            final VideoPlayerActivity this$0;

            {
                this.this$0 = this;
            }

            @Override // android.os.CountDownTimer
            public void onFinish() {
                this.this$0.title_video.setVisibility(8);
                this.this$0.fab_player_layout.setVisibility(8);
                this.this$0.btn_video_nav_left.setVisibility(8);
                this.this$0.btn_video_nav_right.setVisibility(8);
                this.this$0.widgetsIsOpened = false;
            }

            @Override // android.os.CountDownTimer
            public void onTick(long j) {
            }
        }.start();
    }
}
