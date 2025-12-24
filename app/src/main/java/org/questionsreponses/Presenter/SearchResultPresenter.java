package org.questionsreponses.Presenter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import com.github.clans.fab.BuildConfig;
import com.github.kiulian.downloader.model.videos.formats.Format;
import java.util.List;
import org.questionsreponses.C0598R;
import org.questionsreponses.Model.LoadAudioMediaPlayer;
import org.questionsreponses.Model.LoadSearchResult;
import org.questionsreponses.Model.Ressource;
import org.questionsreponses.View.Interfaces.SearchResultView;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/Presenter/SearchResultPresenter.class */
public class SearchResultPresenter implements SearchResultView.IPresenter, SearchResultView.ILoadSearchResult {
    private SearchResultView.ISearchResult iSearchResult;
    private LoadAudioMediaPlayer loadAudioMediaPlayer;
    private LoadSearchResult loadSearchResult;

    public SearchResultPresenter(SearchResultView.ISearchResult iSearchResult) {
        this.iSearchResult = iSearchResult;
    }

    private void closeAudioMediaPlayer(MediaPlayer mediaPlayer) throws IllegalStateException {
        if (mediaPlayer != null) {
            try {
                if (this.iSearchResult != null) {
                    this.iSearchResult.audioPlayerVisibility(8);
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                    }
                }
            } catch (Exception e) {
                Log.e("TAG_ERROR", "SearchResultPresenter-->closeAudioMediaPlayer() : " + e.getMessage());
            }
        }
    }

    public void addQRAudioToFavorite(Context context, SearchResultView.ISearchResultRecycler iSearchResultRecycler) {
        if (iSearchResultRecycler != null) {
            try {
                iSearchResultRecycler.addQRAudioToFavorite(context);
            } catch (Exception e) {
                Log.e("TAG_ERROR", "SearchResultPresenter-->addQRAudioToFavorite() : " + e.getMessage());
            }
        }
    }

    public void downloadQRAudio(Context context, SearchResultView.ISearchResultRecycler iSearchResultRecycler) {
        if (iSearchResultRecycler != null) {
            try {
                iSearchResultRecycler.downloadQRAudio(context);
            } catch (Exception e) {
                Log.e("TAG_ERROR", "SearchResultPresenter-->downloadQRAudio() : " + e.getMessage());
            }
        }
    }

    public void loadSearchResultData(Context context, Intent intent) throws Resources.NotFoundException {
        try {
            if (this.iSearchResult != null) {
                this.iSearchResult.hideHeader();
                this.iSearchResult.initialize();
                this.iSearchResult.events();
                this.iSearchResult.progressBarVisibility(0);
                if (!CommonPresenter.isMobileConnected(context)) {
                    CommonPresenter.showMessage(context, context.getResources().getString(C0598R.string.no_connection).toUpperCase(), context.getResources().getString(C0598R.string.detail_no_connection), true);
                } else {
                    if (intent == null) {
                        this.iSearchResult.closeActivity();
                        return;
                    }
                    String stringExtra = intent.getStringExtra(CommonPresenter.KEY_SEARCH_FORM_TYPE_RESSOURCE);
                    String stringExtra2 = intent.getStringExtra(CommonPresenter.KEY_SEARCH_FORM_KEY_WORD);
                    if (stringExtra.equalsIgnoreCase(Format.AUDIO)) {
                        CommonPresenter.saveDataInSharePreferences(context, CommonPresenter.KEY_SEARCH_FORM_TYPE_RESSOURCE, stringExtra);
                        CommonPresenter.saveDataInSharePreferences(context, CommonPresenter.KEY_SEARCH_FORM_KEY_WORD, stringExtra2);
                    }
                    LoadSearchResult loadSearchResult = new LoadSearchResult();
                    this.loadSearchResult = loadSearchResult;
                    loadSearchResult.initializeAttributes(context, this, stringExtra, stringExtra2);
                    this.loadSearchResult.execute(new Void[0]);
                }
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "SearchResultPresenter-->loadSearchResultData() : " + e.getMessage());
        }
    }

    @Override // org.questionsreponses.View.Interfaces.SearchResultView.ILoadSearchResult
    public void onLoadSearchError() {
        try {
            if (this.iSearchResult != null) {
                this.iSearchResult.progressBarVisibility(8);
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "SearchResultPresenter-->onLoadSearchError() : " + e.getMessage());
        }
    }

    @Override // org.questionsreponses.View.Interfaces.SearchResultView.ILoadSearchResult
    public void onLoadSearchSuccess(Context context, List<Ressource> list, String str) {
        try {
            if (this.iSearchResult != null) {
                this.iSearchResult.loadSearchResult(list, 1, 0, str);
                this.iSearchResult.setSubTitleValue(CommonPresenter.getLibelleResultSearch(context, list.size(), str));
                this.iSearchResult.progressBarVisibility(8);
                CommonPresenter.saveDataInSharePreferences(context, CommonPresenter.KEY_NOTIF_AUDIO_PLAYER_LIST, list.toString());
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "SearchResultPresenter-->onLoadSearchSuccess() : " + e.getMessage());
        }
    }

    public void playAudioNotification(Context context, MediaPlayer mediaPlayer) {
        try {
            if (this.iSearchResult != null) {
                CommonPresenter.saveDataInSharePreferences(context, CommonPresenter.KEY_PLAYER_AUDIO_TO_NOTIF_AUDIO_TIME_ELAPSED, BuildConfig.FLAVOR + mediaPlayer.getCurrentPosition());
                closeAudioMediaPlayer(mediaPlayer);
                this.iSearchResult.playNotification();
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "SearchResultPresenter-->playAudioNotification() : " + e.getMessage());
        }
    }

    public void playNextQRAudio(SearchResultView.ISearchResultRecycler iSearchResultRecycler) {
        if (iSearchResultRecycler != null) {
            try {
                iSearchResultRecycler.playNextQRAudio();
            } catch (Exception e) {
                Log.e("TAG_ERROR", "SearchResultPresenter-->playNextQRAudio() : " + e.getMessage());
            }
        }
    }

    public void playNextQRVideo(SearchResultView.ISearchResultRecycler iSearchResultRecycler) {
        if (iSearchResultRecycler != null) {
            try {
                iSearchResultRecycler.playNextQRVideo();
            } catch (Exception e) {
                Log.e("TAG_ERROR", "SearchResultPresenter-->playNextQRVideo() : " + e.getMessage());
            }
        }
    }

    public void playPreviousQRAudio(SearchResultView.ISearchResultRecycler iSearchResultRecycler) {
        if (iSearchResultRecycler != null) {
            try {
                iSearchResultRecycler.playPreviousQRAudio();
            } catch (Exception e) {
                Log.e("TAG_ERROR", "SearchResultPresenter-->playPreviousQRAudio() : " + e.getMessage());
            }
        }
    }

    public void playPreviousQRVideo(SearchResultView.ISearchResultRecycler iSearchResultRecycler) {
        if (iSearchResultRecycler != null) {
            try {
                iSearchResultRecycler.playPreviousQRVideo();
            } catch (Exception e) {
                Log.e("TAG_ERROR", "SearchResultPresenter-->playPreviousQRVideo() : " + e.getMessage());
            }
        }
    }

    public void playQRAudioOnCompletion(Context context, SearchResultView.ISearchResultRecycler iSearchResultRecycler, ImageButton imageButton) {
        if (iSearchResultRecycler != null) {
            try {
                if (CommonPresenter.getSettingObjectFromSharePreferences(context, CommonPresenter.KEY_SETTING_CONCATENATE_AUDIO_READING).getChoice()) {
                    iSearchResultRecycler.playNextQRAudio();
                } else {
                    imageButton.setBackgroundResource(C0598R.drawable.btn_media_player_play);
                }
            } catch (Exception e) {
                Log.e("TAG_ERROR", "SearchResultPresenter-->playQRAudioOnCompletion() : " + e.getMessage());
            }
        }
    }

    public void playQRAudioPlayer(Context context, Ressource ressource, int i) throws Resources.NotFoundException {
        try {
            if (this.iSearchResult != null) {
                if (CommonPresenter.isMobileConnected(context)) {
                    LoadAudioMediaPlayer loadAudioMediaPlayer = new LoadAudioMediaPlayer();
                    this.loadAudioMediaPlayer = loadAudioMediaPlayer;
                    loadAudioMediaPlayer.initLoadAudioPlayer(ressource, i, this.iSearchResult);
                    this.loadAudioMediaPlayer.execute(new Void[0]);
                } else {
                    CommonPresenter.showMessage(context, context.getResources().getString(C0598R.string.no_connection).toUpperCase(), context.getResources().getString(C0598R.string.detail_no_connection), false);
                }
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "SearchResultPresenter-->playQRAudioPlayer() : " + e.getMessage());
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
                Log.e("TAG_ERROR", "SearchResultPresenter-->playQRAudioPlayer() : " + e.getMessage());
            }
        }
    }

    public void playQRNotifAudioPlayer(Context context, int i) throws Resources.NotFoundException {
        try {
            if (!CommonPresenter.isMobileConnected(context) || this.iSearchResult == null) {
                CommonPresenter.showMessage(context, context.getResources().getString(C0598R.string.no_connection).toUpperCase(), context.getResources().getString(C0598R.string.detail_no_connection), false);
            } else {
                MediaPlayer mediaPlayerInstanceMediaPlayer = this.iSearchResult.instanceMediaPlayer();
                CommonPresenter.saveDataInSharePreferences(context, CommonPresenter.KEY_PLAYER_AUDIO_TO_NOTIF_AUDIO_TIME_ELAPSED, BuildConfig.FLAVOR + mediaPlayerInstanceMediaPlayer.getCurrentPosition());
                closeAudioMediaPlayer(mediaPlayerInstanceMediaPlayer);
                CommonPresenter.saveNotificationParameters(context, i);
                this.iSearchResult.launchNotificationPlayer();
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "SearchResultPresenter-->playQRNotifAudioPlayer() : " + e.getMessage());
        }
    }

    public void playQRVideoPlayer(Context context, Ressource ressource, int i) throws Resources.NotFoundException {
        try {
            if (!CommonPresenter.isMobileConnected(context)) {
                CommonPresenter.showMessage(context, context.getResources().getString(C0598R.string.no_connection).toUpperCase(), context.getResources().getString(C0598R.string.detail_no_connection), false);
            } else if (this.iSearchResult != null) {
                this.iSearchResult.launchVideoToPlay(ressource, i);
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "SearchResultPresenter-->playQRVideoPlayer() : " + e.getMessage());
        }
    }

    public void retrieveAndSetISearchResultRecyclerReference(SearchResultView.ISearchResultRecycler iSearchResultRecycler) {
        try {
            if (this.iSearchResult != null) {
                this.iSearchResult.onActivityISearchResultRecycler(iSearchResultRecycler);
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "SearchResultPresenter-->retrieveAndSetISearchResultRecyclerReference() : " + e.getMessage());
        }
    }

    public void retrieveUserAction(View view, SearchResultView.ISearchResultRecycler iSearchResultRecycler) {
        try {
            int id = view.getId();
            if (id != 2131296338) {
                if (id != 2131296406) {
                    if (id == 2131296408) {
                        CommonPresenter.getApplicationVolume(view.getContext());
                    }
                } else if (iSearchResultRecycler != null) {
                    iSearchResultRecycler.playQRNotifAudioPlayer(view.getContext());
                }
            } else if (this.iSearchResult != null) {
                closeAudioMediaPlayer(this.iSearchResult.instanceMediaPlayer());
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "SearchResultPresenter-->retrieveUserAction() : " + e.getMessage());
        }
    }

    public void rettrieveUserAction(View view) {
        try {
            if (view.getId() == 2131296552 && this.iSearchResult != null) {
                this.iSearchResult.closeActivity();
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "SearchResultPresenter-->rettrieveUserAction() : " + e.getMessage());
        }
    }

    public void shareQRAudio(Context context, SearchResultView.ISearchResultRecycler iSearchResultRecycler) {
        if (iSearchResultRecycler != null) {
            try {
                iSearchResultRecycler.shareQRAudio(context);
            } catch (Exception e) {
                Log.e("TAG_ERROR", "SearchResultPresenter-->shareQRAudio() : " + e.getMessage());
            }
        }
    }

    public void srcollResourceDataItemsToPosition(int i) {
        try {
            if (this.iSearchResult != null) {
                this.iSearchResult.scrollResourceDataToPosition(i);
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "SearchResultPresenter-->srcollResourceDataItemsToPosition() : " + e.getMessage());
        }
    }

    public void stopAllOtherMediaSound(MediaPlayer mediaPlayer, Ressource ressource) {
        try {
            if (this.iSearchResult != null) {
                this.iSearchResult.stopOtherMediaPlayerSound(mediaPlayer, ressource);
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "SearchResultPresenter-->stopAllOtherMediaSound() : " + e.getMessage());
        }
    }

    public void stopAudioNotification() {
        try {
            if (this.iSearchResult != null) {
                this.iSearchResult.stopNotification();
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "SearchResultPresenter-->stopAudioNotification() : " + e.getMessage());
        }
    }
}
