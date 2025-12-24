package org.questionsreponses.Presenter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import com.github.kiulian.downloader.model.videos.formats.Format;
import java.util.Hashtable;
import org.questionsreponses.C0598R;
import org.questionsreponses.Model.DAORessource;
import org.questionsreponses.Model.Ressource;
import org.questionsreponses.View.Interfaces.VideoPlayerView;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/Presenter/VideoPlayerPresenter.class */
public class VideoPlayerPresenter {
    private VideoPlayerView.IVideoPlayer iVideoPlayer;

    public VideoPlayerPresenter(VideoPlayerView.IVideoPlayer iVideoPlayer) {
        this.iVideoPlayer = iVideoPlayer;
    }

    public void loadVideoPlayerData(Context context, Intent intent) throws Resources.NotFoundException {
        try {
            if (this.iVideoPlayer != null) {
                this.iVideoPlayer.hideHeader();
                this.iVideoPlayer.initialize();
                this.iVideoPlayer.events();
                this.iVideoPlayer.pauseNotificationAudio();
                this.iVideoPlayer.progressBarVisibility(0);
                this.iVideoPlayer.headerVisibility(8);
                this.iVideoPlayer.fabVisibility(8);
                this.iVideoPlayer.btnNavigationVisibility(8);
                if (!CommonPresenter.isMobileConnected(context)) {
                    CommonPresenter.showMessage(context, context.getResources().getString(C0598R.string.no_connection).toUpperCase(), context.getResources().getString(C0598R.string.detail_no_connection), true);
                } else if (intent != null) {
                    Ressource ressource = (Ressource) intent.getSerializableExtra(CommonPresenter.KEY_VIDEO_PLAYER_SEND_DATA);
                    Hashtable<String, Integer> hashtableResolutionEcran = CommonPresenter.resolutionEcran(context);
                    this.iVideoPlayer.displayPlayer(ressource, hashtableResolutionEcran.get("largeur").intValue(), hashtableResolutionEcran.get("hauteur").intValue());
                } else {
                    this.iVideoPlayer.closeActivity();
                }
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "VideoPlayerPresenter-->loadVideoPlayerData() : " + e.getMessage());
        }
    }

    public void managePlayerVisibility() {
        try {
            if (this.iVideoPlayer != null) {
                this.iVideoPlayer.showPlayerWidgetsOnTouch();
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "VideoPlayerPresenter-->managePlayerVisibility() : " + e.getMessage());
        }
    }

    public void retrieveOnCompletionAction(Context context) {
        try {
            if (this.iVideoPlayer != null) {
                if (CommonPresenter.getSettingObjectFromSharePreferences(context, CommonPresenter.KEY_SETTING_CONCATENATE_VIDEO_READING).getChoice()) {
                    this.iVideoPlayer.playNextQRVideo();
                } else {
                    this.iVideoPlayer.closeActivity();
                }
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "VideoPlayerPresenter-->retrieveOnCompletionAction() : " + e.getMessage());
        }
    }

    public void retrieveUserAction(Context context, View view) {
        try {
            if (this.iVideoPlayer != null) {
                switch (view.getId()) {
                    case C0598R.id.btn_video_nav_left /* 2131296341 */:
                        this.iVideoPlayer.playPreviousQRVideo();
                        break;
                    case C0598R.id.btn_video_nav_right /* 2131296342 */:
                        this.iVideoPlayer.playNextQRVideo();
                        break;
                    case C0598R.id.fab_player_down /* 2131296402 */:
                        this.iVideoPlayer.closeActivity();
                        break;
                    case C0598R.id.fab_player_download /* 2131296403 */:
                        if (CommonPresenter.getSettingObjectFromSharePreferences(context, CommonPresenter.KEY_SETTING_WIFI_EXCLUSIF).getChoice() && !CommonPresenter.isMobileWIFIConnected(context)) {
                            CommonPresenter.showMessage(context, context.getResources().getString(C0598R.string.lb_wifi_only), context.getResources().getString(C0598R.string.lb_wifi_exclusif_message), false);
                            break;
                        } else {
                            this.iVideoPlayer.downloadQRVideo(context);
                            break;
                        }
                        break;
                    case C0598R.id.fab_player_favorite /* 2131296404 */:
                        this.iVideoPlayer.addQRVideoToFavorite(context);
                        break;
                    case C0598R.id.fab_player_share_app /* 2131296407 */:
                        this.iVideoPlayer.shareQRVideo(context);
                        break;
                    case C0598R.id.fab_player_volume /* 2131296408 */:
                        CommonPresenter.getApplicationVolume(context);
                        break;
                }
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "VideoPlayerPresenter-->retrieveUserAction() : " + e.getMessage());
        }
    }

    public void saveRessourceVideoData(Context context, Ressource ressource) {
        try {
            View viewInTermsOfContext = CommonPresenter.getViewInTermsOfContext(context);
            DAORessource dAORessource = new DAORessource(context);
            if (dAORessource.isRessourceExists(ressource.getSrc())) {
                CommonPresenter.showMessageSnackBar(viewInTermsOfContext, context.getString(C0598R.string.video_already_add_to_favorite));
            } else {
                int size = dAORessource.getAllByTypeRessource(Format.VIDEO).size();
                dAORessource.insertData(ressource.getTitre(), ressource.getEtat(), ressource.getSrc(), Format.VIDEO, ressource.getAuteur(), ressource.getUrlacces(), ressource.getDuree());
                if (size < dAORessource.getAllByTypeRessource(Format.VIDEO).size()) {
                    CommonPresenter.showMessageSnackBar(viewInTermsOfContext, context.getString(C0598R.string.video_add_to_favorite));
                }
            }
        } catch (Exception e) {
        }
    }

    public void showWidgetsOnTouchEvent(boolean z) {
        if (z) {
            return;
        }
        try {
            if (this.iVideoPlayer != null) {
                this.iVideoPlayer.showPlayerWidgetsOnTouch();
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "VideoPlayerPresenter-->showWidgetsOnTouchEvent() : " + e.getMessage());
        }
    }
}
