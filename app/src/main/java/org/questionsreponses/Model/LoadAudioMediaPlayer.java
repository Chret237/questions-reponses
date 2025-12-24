package org.questionsreponses.Model;

import android.os.AsyncTask;
import org.questionsreponses.View.Interfaces.HomeView;
import org.questionsreponses.View.Interfaces.SearchResultView;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/Model/LoadAudioMediaPlayer.class */
public class LoadAudioMediaPlayer extends AsyncTask<Void, Void, Void> {
    private HomeView.IHome iHome;
    private SearchResultView.ISearchResult iSearchResult;
    private int position;
    private Ressource resAudio;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public Void doInBackground(Void... voidArr) {
        HomeView.IHome iHome = this.iHome;
        if (iHome != null) {
            iHome.loadAudioDataToPlay(this.resAudio);
            return null;
        }
        SearchResultView.ISearchResult iSearchResult = this.iSearchResult;
        if (iSearchResult == null) {
            return null;
        }
        iSearchResult.loadAudioDataToPlay(this.resAudio);
        return null;
    }

    public void initLoadAudioPlayer(Ressource ressource, int i, HomeView.IHome iHome) {
        this.position = i;
        this.resAudio = ressource;
        this.iHome = iHome;
    }

    public void initLoadAudioPlayer(Ressource ressource, int i, SearchResultView.ISearchResult iSearchResult) {
        this.position = i;
        this.resAudio = ressource;
        this.iSearchResult = iSearchResult;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public void onPostExecute(Void r4) {
        super.onPostExecute((LoadAudioMediaPlayer) r4);
        HomeView.IHome iHome = this.iHome;
        if (iHome != null) {
            iHome.enableAudioMediaPlayer(true);
            this.iHome.audioPlayerVisibility(0);
            this.iHome.playerProgressBarVisibility(8);
        } else {
            SearchResultView.ISearchResult iSearchResult = this.iSearchResult;
            if (iSearchResult != null) {
                iSearchResult.enableAudioMediaPlayer(true);
                this.iSearchResult.audioPlayerVisibility(0);
                this.iSearchResult.playerProgressBarVisibility(8);
            }
        }
    }

    @Override // android.os.AsyncTask
    protected void onPreExecute() {
        super.onPreExecute();
        HomeView.IHome iHome = this.iHome;
        if (iHome != null) {
            iHome.enableAudioMediaPlayer(false);
            this.iHome.playerProgressBarVisibility(0);
            this.iHome.textMediaPlayInfoLoading();
        } else {
            SearchResultView.ISearchResult iSearchResult = this.iSearchResult;
            if (iSearchResult != null) {
                iSearchResult.enableAudioMediaPlayer(false);
                this.iSearchResult.playerProgressBarVisibility(0);
                this.iSearchResult.textMediaPlayInfoLoading();
            }
        }
    }
}
