package org.questionsreponses.View.Services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.internal.view.SupportMenu;
import android.util.Log;
import android.widget.RemoteViews;
import com.github.clans.fab.BuildConfig;
import com.github.kiulian.downloader.model.videos.formats.Format;
import java.io.IOException;
import java.util.List;
import org.json.JSONException;
import org.questionsreponses.C0598R;
import org.questionsreponses.Model.Ressource;
import org.questionsreponses.Presenter.CommonPresenter;
import org.questionsreponses.View.Activities.HomeActivity;
import org.questionsreponses.View.Interfaces.NotificationView;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Services/PlayerAudioService.class */
public class PlayerAudioService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnInfoListener {
    private RemoteViews bigViews;
    private MediaPlayer mediaPlayer = null;
    private Notification notification;
    private NotificationManagerCompat notificationManager;
    private int positionSelected;
    private List<Ressource> ressources;
    private RemoteViews views;

    private void playAudioMediaPlayer() throws IllegalStateException, IOException, SecurityException, IllegalArgumentException {
        CommonPresenter.stopAudioMediaPlayer(this.mediaPlayer);
        MediaPlayer mediaPlayer = new MediaPlayer();
        this.mediaPlayer = mediaPlayer;
        mediaPlayer.setWakeMode(getApplicationContext(), 1);
        stopOtherMediaPlayerSound();
        this.mediaPlayer.setOnPreparedListener(this);
        this.mediaPlayer.setOnCompletionListener(this);
        this.mediaPlayer.setOnErrorListener(this);
        this.mediaPlayer.setOnInfoListener(this);
    }

    private void showNotification() {
        this.views = new RemoteViews(getPackageName(), C0598R.layout.notification_audio_player_small);
        this.bigViews = new RemoteViews(getPackageName(), C0598R.layout.notification_audio_player_large);
        this.views.setImageViewBitmap(C0598R.id.notif_logo, NotificationView.getDefaultAlbumArt(this));
        this.bigViews.setImageViewBitmap(C0598R.id.notif_logo, NotificationView.getDefaultAlbumArt(this));
        Intent intent = new Intent(this, (Class<?>) HomeActivity.class);
        intent.setAction(NotificationView.ACTION.MAIN_ACTION);
        intent.setFlags(268468224);
        PendingIntent activity = PendingIntent.getActivity(this, 0, intent, 0);
        Intent intent2 = new Intent(this, (Class<?>) PlayerAudioService.class);
        intent2.setAction(NotificationView.ACTION.PREVIOUS_ACTION);
        PendingIntent service = PendingIntent.getService(this, 0, intent2, 0);
        Intent intent3 = new Intent(this, (Class<?>) PlayerAudioService.class);
        intent3.setAction(NotificationView.ACTION.PLAY_ACTION);
        PendingIntent service2 = PendingIntent.getService(this, 0, intent3, 0);
        Intent intent4 = new Intent(this, (Class<?>) PlayerAudioService.class);
        intent4.setAction(NotificationView.ACTION.NEXT_ACTION);
        PendingIntent service3 = PendingIntent.getService(this, 0, intent4, 0);
        Intent intent5 = new Intent(this, (Class<?>) PlayerAudioService.class);
        intent5.setAction(NotificationView.ACTION.STOPFOREGROUND_ACTION);
        PendingIntent service4 = PendingIntent.getService(this, 0, intent5, 0);
        this.views.setOnClickPendingIntent(C0598R.id.notif_player_previous, service);
        this.views.setImageViewResource(C0598R.id.notif_player_previous, C0598R.drawable.btn_media_player_previous);
        this.views.setOnClickPendingIntent(C0598R.id.notif_player_play, service2);
        this.views.setImageViewResource(C0598R.id.notif_player_play, C0598R.drawable.btn_media_player_play);
        this.views.setOnClickPendingIntent(C0598R.id.notif_player_next, service3);
        this.views.setImageViewResource(C0598R.id.notif_player_next, C0598R.drawable.btn_media_player_next);
        this.views.setOnClickPendingIntent(C0598R.id.notif_player_close, service4);
        this.views.setImageViewResource(C0598R.id.notif_player_close, C0598R.drawable.ic_player_media_cancel_32dp);
        this.bigViews.setOnClickPendingIntent(C0598R.id.notif_player_previous, service);
        this.bigViews.setImageViewResource(C0598R.id.notif_player_previous, C0598R.drawable.btn_media_player_previous);
        this.bigViews.setOnClickPendingIntent(C0598R.id.notif_player_play, service2);
        this.bigViews.setImageViewResource(C0598R.id.notif_player_play, C0598R.drawable.btn_media_player_play);
        this.bigViews.setOnClickPendingIntent(C0598R.id.notif_player_next, service3);
        this.bigViews.setImageViewResource(C0598R.id.notif_player_next, C0598R.drawable.btn_media_player_next);
        this.bigViews.setOnClickPendingIntent(C0598R.id.notif_player_close, service4);
        this.bigViews.setImageViewResource(C0598R.id.notif_player_close, C0598R.drawable.ic_player_media_cancel_32dp);
        this.views.setTextViewText(C0598R.id.notif_player_title, getResources().getString(C0598R.string.lb_buffering_title));
        this.views.setTextViewText(C0598R.id.notif_player_subtitle, getResources().getString(C0598R.string.lb_buffering_title));
        this.bigViews.setTextViewText(C0598R.id.notif_player_title, getResources().getString(C0598R.string.lb_buffering_title));
        this.bigViews.setTextViewText(C0598R.id.notif_player_subtitle, getResources().getString(C0598R.string.lb_buffering_detail));
        viewNormalColor();
        this.notificationManager = NotificationManagerCompat.from(this);
        Notification notificationBuild = new NotificationCompat.Builder(this, App.CHANNEL_ID).setVisibility(1).build();
        this.notification = notificationBuild;
        notificationBuild.contentView = this.views;
        this.notification.bigContentView = this.bigViews;
        this.notification.flags = 2;
        this.notification.icon = C0598R.mipmap.ic_launcher;
        this.notification.contentIntent = activity;
        this.notificationManager.notify(NotificationView.NOTIFICATION_ID.FOREGROUND_SERVICE, this.notification);
    }

    private void stopOtherMediaPlayerSound() throws IllegalStateException, IOException, SecurityException, IllegalArgumentException {
        if (((AudioManager) getSystemService(Format.AUDIO)).requestAudioFocus(new AudioManager.OnAudioFocusChangeListener(this) { // from class: org.questionsreponses.View.Services.PlayerAudioService.1
            final PlayerAudioService this$0;

            {
                this.this$0 = this;
            }

            @Override // android.media.AudioManager.OnAudioFocusChangeListener
            public void onAudioFocusChange(int i) {
                if (i == -3) {
                    Log.i("TAG_NOTIF_AUDIO_PAYER", "AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");
                    return;
                }
                if (i == -2) {
                    Log.i("TAG_NOTIF_AUDIO_PAYER", "AudioManager.AUDIOFOCUS_LOSS_TRANSIENT");
                } else if (i == -1) {
                    Log.i("TAG_NOTIF_AUDIO_PAYER", "AudioManager.AUDIOFOCUS_LOSS");
                } else {
                    if (i != 1) {
                        return;
                    }
                    Log.i("TAG_NOTIF_AUDIO_PAYER", "AudioManager.AUDIOFOCUS_GAIN");
                }
            }
        }, 3, 1) == 1) {
            Log.i("TAG_NOTIF_AUDIO_PAYER", "AudioManager.AUDIOFOCUS_REQUEST_GRANTED");
            try {
                this.mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(this.ressources.get(this.positionSelected).getUrlacces() + this.ressources.get(this.positionSelected).getSrc()));
                this.mediaPlayer.prepareAsync();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void viewFailureColor() {
        this.views.setTextColor(C0598R.id.notif_player_title, SupportMenu.CATEGORY_MASK);
        this.views.setTextColor(C0598R.id.notif_player_subtitle, SupportMenu.CATEGORY_MASK);
        this.bigViews.setTextColor(C0598R.id.notif_player_title, SupportMenu.CATEGORY_MASK);
        this.bigViews.setTextColor(C0598R.id.notif_player_subtitle, SupportMenu.CATEGORY_MASK);
    }

    private void viewNormalColor() {
        this.views.setTextColor(C0598R.id.notif_player_title, -1);
        this.views.setTextColor(C0598R.id.notif_player_subtitle, Color.rgb(18, 17, 74));
        this.bigViews.setTextColor(C0598R.id.notif_player_title, -1);
        this.bigViews.setTextColor(C0598R.id.notif_player_subtitle, Color.rgb(18, 17, 74));
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override // android.media.MediaPlayer.OnCompletionListener
    public void onCompletion(MediaPlayer mediaPlayer) throws IllegalStateException, JSONException, IOException, SecurityException, IllegalArgumentException {
        this.positionSelected = Integer.parseInt(CommonPresenter.getDataFromSharePreferences(getApplicationContext(), CommonPresenter.KEY_NOTIF_PLAYER_PLAY_NEXT));
        CommonPresenter.saveNotificationParameters(getApplicationContext(), this.positionSelected);
        playAudioMediaPlayer();
    }

    @Override // android.app.Service
    public void onDestroy() throws IllegalStateException {
        super.onDestroy();
        CommonPresenter.stopAudioMediaPlayer(this.mediaPlayer);
        CommonPresenter.saveDataInSharePreferences(getApplicationContext(), CommonPresenter.KEY_NOTIFICATION_IS_PLAYING, "NO");
    }

    @Override // android.media.MediaPlayer.OnErrorListener
    public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
        if (CommonPresenter.isMobileConnected(getApplicationContext())) {
            this.views.setTextViewText(C0598R.id.notif_player_title, getResources().getString(C0598R.string.lb_impossible_toplay_detail));
            this.views.setTextViewText(C0598R.id.notif_player_subtitle, getResources().getString(C0598R.string.lb_impossible_toplay_detail));
            this.bigViews.setTextViewText(C0598R.id.notif_player_title, getResources().getString(C0598R.string.lb_impossible_toplay));
            this.bigViews.setTextViewText(C0598R.id.notif_player_subtitle, getResources().getString(C0598R.string.lb_impossible_toplay_detail));
        } else {
            this.views.setTextViewText(C0598R.id.notif_player_title, getResources().getString(C0598R.string.no_connection));
            this.views.setTextViewText(C0598R.id.notif_player_subtitle, getResources().getString(C0598R.string.no_connection));
            this.bigViews.setTextViewText(C0598R.id.notif_player_title, getResources().getString(C0598R.string.lb_impossible_toplay));
            this.bigViews.setTextViewText(C0598R.id.notif_player_subtitle, getResources().getString(C0598R.string.no_connection));
        }
        this.views.setImageViewResource(C0598R.id.notif_player_play, C0598R.drawable.btn_media_player_play);
        this.bigViews.setImageViewResource(C0598R.id.notif_player_play, C0598R.drawable.btn_media_player_play);
        viewFailureColor();
        startForeground(NotificationView.NOTIFICATION_ID.FOREGROUND_SERVICE, this.notification);
        return false;
    }

    @Override // android.media.MediaPlayer.OnInfoListener
    public boolean onInfo(MediaPlayer mediaPlayer, int i, int i2) {
        if (i == 701) {
            this.views.setTextViewText(C0598R.id.notif_player_title, getResources().getString(C0598R.string.lb_buffering_title));
            this.views.setTextViewText(C0598R.id.notif_player_subtitle, getResources().getString(C0598R.string.lb_buffering_title));
            this.bigViews.setTextViewText(C0598R.id.notif_player_title, getResources().getString(C0598R.string.lb_buffering_title));
            this.bigViews.setTextViewText(C0598R.id.notif_player_subtitle, getResources().getString(C0598R.string.lb_buffering_detail));
        } else if (i == 702) {
            this.views.setTextViewText(C0598R.id.notif_player_title, this.ressources.get(this.positionSelected).getTitre());
            this.views.setTextViewText(C0598R.id.notif_player_subtitle, this.ressources.get(this.positionSelected).getTitre());
            this.bigViews.setTextViewText(C0598R.id.notif_player_title, this.ressources.get(this.positionSelected).getTitre());
            String[] strArrSplit = this.ressources.get(this.positionSelected).getDuree().split(":");
            this.bigViews.setTextViewText(C0598R.id.notif_player_subtitle, ((Integer.parseInt(strArrSplit[0]) * 60) + Integer.parseInt(strArrSplit[1])) + "min | " + this.ressources.get(this.positionSelected).getAuteur());
        }
        viewNormalColor();
        startForeground(NotificationView.NOTIFICATION_ID.FOREGROUND_SERVICE, this.notification);
        return false;
    }

    @Override // android.media.MediaPlayer.OnPreparedListener
    public void onPrepared(MediaPlayer mediaPlayer) throws IllegalStateException {
        mediaPlayer.start();
        String dataFromSharePreferences = CommonPresenter.getDataFromSharePreferences(getApplicationContext(), CommonPresenter.KEY_PLAYER_AUDIO_TO_NOTIF_AUDIO_TIME_ELAPSED);
        if (dataFromSharePreferences != null && !dataFromSharePreferences.equalsIgnoreCase("0")) {
            mediaPlayer.seekTo(Integer.parseInt(dataFromSharePreferences));
            CommonPresenter.saveDataInSharePreferences(getApplicationContext(), CommonPresenter.KEY_PLAYER_AUDIO_TO_NOTIF_AUDIO_TIME_ELAPSED, "0");
        }
        this.views.setTextViewText(C0598R.id.notif_player_title, this.ressources.get(this.positionSelected).getTitre());
        this.views.setTextViewText(C0598R.id.notif_player_subtitle, this.ressources.get(this.positionSelected).getTitre());
        this.bigViews.setTextViewText(C0598R.id.notif_player_title, this.ressources.get(this.positionSelected).getTitre());
        String[] strArrSplit = this.ressources.get(this.positionSelected).getDuree().split(":");
        this.bigViews.setTextViewText(C0598R.id.notif_player_subtitle, ((Integer.parseInt(strArrSplit[0]) * 60) + Integer.parseInt(strArrSplit[1])) + "min | " + this.ressources.get(this.positionSelected).getAuteur());
        this.views.setImageViewResource(C0598R.id.notif_player_play, C0598R.drawable.btn_media_player_pause);
        this.bigViews.setImageViewResource(C0598R.id.notif_player_play, C0598R.drawable.btn_media_player_pause);
        viewNormalColor();
        startForeground(NotificationView.NOTIFICATION_ID.FOREGROUND_SERVICE, this.notification);
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) throws IllegalStateException {
        if (intent != null) {
            try {
                if (intent.getAction().equals(NotificationView.ACTION.STARTFOREGROUND_ACTION)) {
                    this.ressources = CommonPresenter.getJsonToResourcesList(getApplicationContext(), CommonPresenter.KEY_NOTIF_AUDIO_PLAYER_LIST, Format.AUDIO);
                    this.positionSelected = Integer.parseInt(CommonPresenter.getDataFromSharePreferences(getApplicationContext(), CommonPresenter.KEY_NOTIF_PLAYER_SELECTED));
                    CommonPresenter.saveDataInSharePreferences(getApplicationContext(), CommonPresenter.KEY_NOTIF_AUDIO_TO_PLAYER_AUDIO_ID, BuildConfig.FLAVOR + this.ressources.get(this.positionSelected).getId());
                    showNotification();
                    playAudioMediaPlayer();
                    CommonPresenter.saveDataInSharePreferences(getApplicationContext(), CommonPresenter.KEY_NOTIFICATION_IS_PLAYING, "YES");
                    return 1;
                }
            } catch (Exception e) {
                Log.e("TAG_ERROR", "PlayerAudioService-->onStartCommand() : " + e.getMessage());
                return 1;
            }
        }
        if (intent != null && intent.getAction().equals(NotificationView.ACTION.PLAY_ACTION)) {
            if (this.mediaPlayer.isPlaying()) {
                this.mediaPlayer.pause();
                this.views.setImageViewResource(C0598R.id.notif_player_play, C0598R.drawable.btn_media_player_play);
                this.bigViews.setImageViewResource(C0598R.id.notif_player_play, C0598R.drawable.btn_media_player_play);
            } else {
                this.mediaPlayer.start();
                this.views.setImageViewResource(C0598R.id.notif_player_play, C0598R.drawable.btn_media_player_pause);
                this.bigViews.setImageViewResource(C0598R.id.notif_player_play, C0598R.drawable.btn_media_player_pause);
                stopOtherMediaPlayerSound();
            }
            startForeground(NotificationView.NOTIFICATION_ID.FOREGROUND_SERVICE, this.notification);
            return 1;
        }
        if (intent != null && intent.getAction().equals(NotificationView.ACTION.PREVIOUS_ACTION)) {
            this.positionSelected = Integer.parseInt(CommonPresenter.getDataFromSharePreferences(getApplicationContext(), CommonPresenter.KEY_NOTIF_PLAYER_PREVIOUS));
            CommonPresenter.saveDataInSharePreferences(getApplicationContext(), CommonPresenter.KEY_NOTIF_AUDIO_TO_PLAYER_AUDIO_ID, BuildConfig.FLAVOR + this.ressources.get(this.positionSelected).getId());
            CommonPresenter.saveNotificationParameters(getApplicationContext(), this.positionSelected);
            playAudioMediaPlayer();
            return 1;
        }
        if (intent != null && intent.getAction().equals(NotificationView.ACTION.NEXT_ACTION)) {
            this.positionSelected = Integer.parseInt(CommonPresenter.getDataFromSharePreferences(getApplicationContext(), CommonPresenter.KEY_NOTIF_PLAYER_PLAY_NEXT));
            CommonPresenter.saveDataInSharePreferences(getApplicationContext(), CommonPresenter.KEY_NOTIF_AUDIO_TO_PLAYER_AUDIO_ID, BuildConfig.FLAVOR + this.ressources.get(this.positionSelected).getId());
            CommonPresenter.saveNotificationParameters(getApplicationContext(), this.positionSelected);
            playAudioMediaPlayer();
            return 1;
        }
        if (intent != null && intent.getAction().equals(NotificationView.ACTION.PAUSE_ACTION)) {
            if (this.mediaPlayer == null || !this.mediaPlayer.isPlaying()) {
                return 1;
            }
            this.mediaPlayer.pause();
            this.views.setImageViewResource(C0598R.id.notif_player_play, C0598R.drawable.btn_media_player_play);
            this.bigViews.setImageViewResource(C0598R.id.notif_player_play, C0598R.drawable.btn_media_player_play);
            return 1;
        }
        if (intent == null || !intent.getAction().equals(NotificationView.ACTION.STOPFOREGROUND_ACTION)) {
            return 1;
        }
        if (this.mediaPlayer != null && this.mediaPlayer.isPlaying()) {
            CommonPresenter.saveDataInSharePreferences(getApplicationContext(), CommonPresenter.KEY_NOTIF_AUDIO_TO_PLAYER_AUDIO_TIME_ELAPSED, BuildConfig.FLAVOR + this.mediaPlayer.getCurrentPosition());
            CommonPresenter.saveDataInSharePreferences(getApplicationContext(), CommonPresenter.KEY_NOTIF_AUDIO_TO_PLAYER_AUDIO_ID, BuildConfig.FLAVOR + this.ressources.get(this.positionSelected).getId());
            this.mediaPlayer.stop();
            CommonPresenter.saveDataInSharePreferences(getApplicationContext(), CommonPresenter.KEY_NOTIFICATION_IS_PLAYING, "NO");
        }
        stopForeground(true);
        stopSelf();
        return 1;
    }
}
