package org.questionsreponses.Model;

import android.content.Context;
import android.os.AsyncTask;
import java.util.List;
import org.questionsreponses.Presenter.CommonPresenter;
import org.questionsreponses.View.Interfaces.SearchResultView;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/Model/LoadSearchResult.class */
public class LoadSearchResult extends AsyncTask<Void, Void, List<Ressource>> {
    private Context context;
    private SearchResultView.ILoadSearchResult iLoadSearch;
    private String keySearchWord;
    private String typeResource;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public List<Ressource> doInBackground(Void... voidArr) {
        return CommonPresenter.getResultSearchBy(this.context, this.typeResource, this.keySearchWord);
    }

    public void initializeAttributes(Context context, SearchResultView.ILoadSearchResult iLoadSearchResult, String str, String str2) {
        this.context = context;
        this.iLoadSearch = iLoadSearchResult;
        this.typeResource = str;
        this.keySearchWord = str2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public void onPostExecute(List<Ressource> list) {
        super.onPostExecute((LoadSearchResult) list);
        if (list != null) {
            this.iLoadSearch.onLoadSearchSuccess(this.context, list, this.typeResource);
        } else {
            this.iLoadSearch.onLoadSearchError();
        }
    }

    @Override // android.os.AsyncTask
    protected void onPreExecute() {
        super.onPreExecute();
    }
}
