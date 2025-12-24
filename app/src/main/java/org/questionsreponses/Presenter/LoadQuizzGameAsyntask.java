package org.questionsreponses.Presenter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import com.github.clans.fab.BuildConfig;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import org.json.JSONException;
import org.questionsreponses.Model.Quizz;
import org.questionsreponses.Model.Survey;
import org.questionsreponses.View.Interfaces.QuizzGameView;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/Presenter/LoadQuizzGameAsyntask.class */
public class LoadQuizzGameAsyntask extends AsyncTask<Void, Void, ArrayList<Survey>> {
    private Context context;
    private QuizzGameView.IPresenter iPresenter;
    private Intent intent;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public ArrayList<Survey> doInBackground(Void... voidArr) throws JSONException, IOException {
        ArrayList<Quizz> allTheBible;
        String stringExtra = this.intent.getStringExtra(CommonPresenter.KEY_QUIZZ_GAME_LIST_SELECTED);
        String stringExtra2 = this.intent.getStringExtra(CommonPresenter.KEY_QUIZZ_GAME_TITLE_SELECTED);
        String stringExtra3 = this.intent.getStringExtra(CommonPresenter.KEY_QUIZZ_GROUPKEY_SELECTED);
        if (stringExtra3 == null || stringExtra3.isEmpty()) {
            allTheBible = CommonPresenter.getAllTheBible(this.context, stringExtra);
            Random random = new Random();
            stringExtra3 = random.nextInt(1000000) + "-" + random.nextInt(1000000) + "-" + random.nextInt(1000000);
        } else {
            allTheBible = null;
        }
        ArrayList<Survey> arrayList = new ArrayList<>();
        if (allTheBible != null) {
            for (int i = 0; i < allTheBible.size(); i++) {
                Survey survey = new Survey();
                survey.setQuestion(allTheBible.get(i).getQuestion());
                survey.setCategorie(allTheBible.get(i).getCategorie());
                survey.setExplication(allTheBible.get(i).getExplication());
                survey.setProposition_1(allTheBible.get(i).getProposition_1());
                survey.setProposition_2(allTheBible.get(i).getProposition_2());
                survey.setProposition_3(allTheBible.get(i).getProposition_3());
                survey.setReponse(allTheBible.get(i).getReponse());
                survey.setTotal_question(allTheBible.size());
                survey.setTotal_trouve(0);
                survey.setTotal_erreur(0);
                survey.setTitre_quizz(stringExtra2);
                survey.setClef_groupe(stringExtra3);
                survey.setDate(CommonPresenter.getCurrentDate());
                arrayList.add(survey);
            }
        } else {
            ArrayList<Survey> allTheSurveyBy = CommonPresenter.getAllTheSurveyBy(stringExtra);
            if (allTheSurveyBy != null && allTheSurveyBy.size() > 0) {
                int total_trouve = allTheSurveyBy.get(0).getTotal_trouve() + allTheSurveyBy.get(0).getTotal_erreur();
                int i2 = 0;
                while (true) {
                    int i3 = i2;
                    if (i3 >= allTheSurveyBy.size()) {
                        break;
                    }
                    Survey survey2 = new Survey();
                    survey2.setQuestion(allTheSurveyBy.get(i3).getQuestion());
                    survey2.setCategorie(allTheSurveyBy.get(i3).getCategorie());
                    survey2.setExplication(allTheSurveyBy.get(i3).getExplication());
                    survey2.setProposition_1(allTheSurveyBy.get(i3).getProposition_1());
                    survey2.setProposition_2(allTheSurveyBy.get(i3).getProposition_2());
                    survey2.setProposition_3(allTheSurveyBy.get(i3).getProposition_3());
                    survey2.setReponse(allTheSurveyBy.get(i3).getReponse());
                    survey2.setTotal_question(allTheSurveyBy.size());
                    int i4 = i3 + 1;
                    survey2.setTotal_trouve(i4 <= total_trouve ? allTheSurveyBy.get(i3).getTotal_trouve() : 0);
                    survey2.setTotal_erreur(i4 <= total_trouve ? allTheSurveyBy.get(i3).getTotal_erreur() : 0);
                    survey2.setReponse_choisie(i4 <= total_trouve ? allTheSurveyBy.get(i3).getReponse_choisie().equalsIgnoreCase("choix4") ? BuildConfig.FLAVOR : allTheSurveyBy.get(i3).getReponse_choisie() : null);
                    survey2.setTitre_quizz(stringExtra2);
                    survey2.setClef_groupe(stringExtra3);
                    survey2.setDate(allTheSurveyBy.get(i3).getDate());
                    arrayList.add(survey2);
                    i2 = i4;
                }
            }
        }
        return arrayList;
    }

    public void initialize(Context context, Intent intent, QuizzGameView.IPresenter iPresenter) {
        this.context = context;
        this.intent = intent;
        this.iPresenter = iPresenter;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public void onPostExecute(ArrayList<Survey> arrayList) {
        super.onPostExecute((LoadQuizzGameAsyntask) arrayList);
        if (arrayList == null || arrayList.size() <= 0) {
            this.iPresenter.onLoadQuizzGameDataFailed(this.context);
        } else {
            this.iPresenter.setNumberOfQuizzGameFinded(arrayList.size());
            this.iPresenter.onLoadQuizzGameDataFinished(this.context, arrayList);
        }
    }

    @Override // android.os.AsyncTask
    protected void onPreExecute() {
        super.onPreExecute();
    }
}
