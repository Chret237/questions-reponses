package org.questionsreponses.Presenter;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import com.github.clans.fab.BuildConfig;
import java.util.ArrayList;
import java.util.Random;
import org.questionsreponses.Model.Quizz;
import org.questionsreponses.View.Interfaces.HomeView;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/Presenter/LaunchQuizzGameAsyntask.class */
public class LaunchQuizzGameAsyntask extends AsyncTask<Void, Void, ArrayList<Quizz>> {
    private Context context;
    private HomePresenter homePresenter;
    private HomeView.IHome iHome;
    private int position;
    private String title;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public ArrayList<Quizz> doInBackground(Void... voidArr) throws Resources.NotFoundException {
        Quizz quizz;
        ArrayList<Quizz> bibleByCategory = CommonPresenter.getBibleByCategory(this.context, CommonPresenter.getCategorieBible(this.position), this.iHome.retrieveAllQuizzData());
        ArrayList<Quizz> arrayList = new ArrayList<>();
        int totalMaxQuestion = CommonPresenter.getTotalMaxQuestion(this.context);
        String str = BuildConfig.FLAVOR;
        for (int i = 0; i < totalMaxQuestion; i++) {
            String keyCategoryByTitle = CommonPresenter.getKeyCategoryByTitle(this.context, this.title);
            String dataFromSharePreferences = CommonPresenter.getDataFromSharePreferences(this.context, keyCategoryByTitle);
            this.homePresenter.resetQuizzIdSaveList(this.context, bibleByCategory, keyCategoryByTitle, dataFromSharePreferences);
            int size = bibleByCategory.size();
            do {
                quizz = bibleByCategory.get(new Random().nextInt(size));
                if (dataFromSharePreferences.indexOf(quizz.getId() + "-") >= 0) {
                }
                String str2 = dataFromSharePreferences + quizz.getId() + "-";
                str = str + "-" + quizz.getId() + "-";
                arrayList.add(quizz);
                CommonPresenter.saveDataInSharePreferences(this.context, keyCategoryByTitle, str2.trim());
            } while (str.indexOf("-" + quizz.getId() + "-") >= 0);
            String str22 = dataFromSharePreferences + quizz.getId() + "-";
            str = str + "-" + quizz.getId() + "-";
            arrayList.add(quizz);
            CommonPresenter.saveDataInSharePreferences(this.context, keyCategoryByTitle, str22.trim());
        }
        return arrayList;
    }

    public void initialize(Context context, String str, int i, HomeView.IHome iHome) {
        this.context = context;
        this.title = str;
        this.position = i;
        this.iHome = iHome;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public void onPostExecute(ArrayList<Quizz> arrayList) {
        super.onPostExecute((LaunchQuizzGameAsyntask) arrayList);
        this.iHome.launchQuizzGameToPlay(arrayList.toString(), this.title);
    }

    @Override // android.os.AsyncTask
    protected void onPreExecute() {
        this.homePresenter = new HomePresenter(this.iHome);
        super.onPreExecute();
    }
}
