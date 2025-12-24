package org.questionsreponses.Presenter;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import com.github.clans.fab.BuildConfig;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import org.json.JSONException;
import org.questionsreponses.Model.Quizz;
import org.questionsreponses.Model.Survey;
import org.questionsreponses.View.Interfaces.SpiritualGrowthView;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/Presenter/LoadSpiritualGrowthAsyntask.class */
public class LoadSpiritualGrowthAsyntask extends AsyncTask<Void, Void, ArrayList<Survey>> {
    private Context context;
    private SpiritualGrowthPresenter growthPresenter;
    private SpiritualGrowthView.IPresenter iPresenter;
    private String keyGroup;
    private int minScoreToUnlocked;
    private String title;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public ArrayList<Survey> doInBackground(Void... voidArr) throws JSONException, Resources.NotFoundException, IOException {
        Quizz quizz;
        ArrayList<Quizz> allTheBible = CommonPresenter.getAllTheBible(this.context, null);
        ArrayList<Survey> arrayList = new ArrayList<>();
        String str = BuildConfig.FLAVOR;
        for (int i = 0; i < 20; i++) {
            String keyCategoryByTitle = CommonPresenter.getKeyCategoryByTitle(this.context, null);
            String dataFromSharePreferences = CommonPresenter.getDataFromSharePreferences(this.context, keyCategoryByTitle);
            this.growthPresenter.resetQuizzIdSaveList(this.context, allTheBible, keyCategoryByTitle, dataFromSharePreferences);
            int size = allTheBible.size();
            do {
                quizz = allTheBible.get(new Random().nextInt(size));
                if (dataFromSharePreferences.indexOf(quizz.getId() + "-") >= 0) {
                }
                String str2 = dataFromSharePreferences + quizz.getId() + "-";
                str = str + "-" + quizz.getId() + "-";
                Survey survey = new Survey();
                survey.setQuestion(quizz.getQuestion());
                survey.setCategorie(quizz.getCategorie());
                survey.setExplication(quizz.getExplication());
                survey.setProposition_1(quizz.getProposition_1());
                survey.setProposition_2(quizz.getProposition_2());
                survey.setProposition_3(quizz.getProposition_3());
                survey.setReponse(quizz.getReponse());
                survey.setTotal_question(20);
                survey.setTotal_trouve(0);
                survey.setTotal_erreur(0);
                survey.setReponse_choisie(null);
                survey.setTitre_quizz(this.title);
                survey.setClef_groupe(this.keyGroup);
                survey.setDate(CommonPresenter.getCurrentDate());
                arrayList.add(survey);
                CommonPresenter.saveDataInSharePreferences(this.context, keyCategoryByTitle, str2.trim());
            } while (str.indexOf("-" + quizz.getId() + "-") >= 0);
            String str22 = dataFromSharePreferences + quizz.getId() + "-";
            str = str + "-" + quizz.getId() + "-";
            Survey survey2 = new Survey();
            survey2.setQuestion(quizz.getQuestion());
            survey2.setCategorie(quizz.getCategorie());
            survey2.setExplication(quizz.getExplication());
            survey2.setProposition_1(quizz.getProposition_1());
            survey2.setProposition_2(quizz.getProposition_2());
            survey2.setProposition_3(quizz.getProposition_3());
            survey2.setReponse(quizz.getReponse());
            survey2.setTotal_question(20);
            survey2.setTotal_trouve(0);
            survey2.setTotal_erreur(0);
            survey2.setReponse_choisie(null);
            survey2.setTitre_quizz(this.title);
            survey2.setClef_groupe(this.keyGroup);
            survey2.setDate(CommonPresenter.getCurrentDate());
            arrayList.add(survey2);
            CommonPresenter.saveDataInSharePreferences(this.context, keyCategoryByTitle, str22.trim());
        }
        return arrayList;
    }

    public void initialize(Context context, String str, String str2, int i, SpiritualGrowthPresenter spiritualGrowthPresenter, SpiritualGrowthView.IPresenter iPresenter) {
        this.context = context;
        this.title = str;
        this.keyGroup = str2;
        this.growthPresenter = spiritualGrowthPresenter;
        this.iPresenter = iPresenter;
        this.minScoreToUnlocked = i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public void onPostExecute(ArrayList<Survey> arrayList) {
        super.onPostExecute((LoadSpiritualGrowthAsyntask) arrayList);
        if (arrayList == null || arrayList.size() <= 0) {
            this.iPresenter.onLoadQuizzSpiritualGrowthFailed(this.context);
        } else {
            this.iPresenter.onLoadQuizzSpiritualGrowthFinished(arrayList.toString(), this.title, this.keyGroup, this.minScoreToUnlocked);
        }
    }

    @Override // android.os.AsyncTask
    protected void onPreExecute() {
        super.onPreExecute();
    }
}
