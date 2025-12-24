package org.questionsreponses.Presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import java.util.ArrayList;
import org.questionsreponses.C0598R;
import org.questionsreponses.Model.DAOSurvey;
import org.questionsreponses.Model.Survey;
import org.questionsreponses.View.Interfaces.QuizzSaveListView;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/Presenter/QuizzSaveListPresenter.class */
public class QuizzSaveListPresenter {
    private static ArrayList<Survey> surveysList;
    private QuizzSaveListView.IPlaceholder iPlaceholder;
    private QuizzSaveListView.IQuizzSaveList iQuizzSaveList;

    public QuizzSaveListPresenter(QuizzSaveListView.IPlaceholder iPlaceholder) {
        this.iPlaceholder = iPlaceholder;
    }

    public QuizzSaveListPresenter(QuizzSaveListView.IQuizzSaveList iQuizzSaveList) {
        this.iQuizzSaveList = iQuizzSaveList;
    }

    public static ArrayList<Survey> getSurveysList() {
        return surveysList;
    }

    public static void retrieveDataFromParentActivity(Context context, Intent intent) {
        try {
            if (intent != null) {
                surveysList = new DAOSurvey(context).getAllByKeyGroup(intent.getExtras().getString(CommonPresenter.KEY_QUIZZ_ACTIVITY_LIST));
            } else {
                ((Activity) context).finish();
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "QuizzSaveListPresenter-->retrieveDataFromParentActivity() : " + e.getMessage());
        }
    }

    public void changeNumberOfThePage(String str) {
        try {
            if (this.iQuizzSaveList != null) {
                this.iQuizzSaveList.modifyNumberPage(str);
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "QuizzSaveListPresenter-->changeNumberOfThePage() : " + e.getMessage());
        }
    }

    public void loadHeaderQuizzGame(Survey survey) {
        this.iQuizzSaveList.modifyHeaderQuizzGame(survey);
    }

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /* JADX WARN: Multi-variable type inference failed */
    public void loadPlaceHolderData(View view, Survey survey) {
        if (survey != null) {
            try {
                if (this.iPlaceholder != null) {
                    this.iPlaceholder.initialize(view);
                    this.iPlaceholder.events();
                    this.iPlaceholder.loadInfosQuizzGame(view.getContext(), survey);
                    String reponse_choisie = survey.getReponse_choisie();
                    String reponse = survey.getReponse();
                    boolean z = -1;
                    switch (reponse.hashCode()) {
                        case -1361223688:
                            if (reponse.equals("choix1")) {
                                z = false;
                                break;
                            }
                            break;
                        case -1361223687:
                            if (reponse.equals("choix2")) {
                                z = true;
                                break;
                            }
                            break;
                        case -1361223686:
                            if (reponse.equals("choix3")) {
                                z = 2;
                                break;
                            }
                            break;
                    }
                    if (z) {
                        if (!z) {
                            if (z == 2) {
                                if (reponse_choisie.equalsIgnoreCase(survey.getReponse())) {
                                    this.iPlaceholder.messageOkTextValue(survey.getProposition_3().toUpperCase());
                                } else {
                                    this.iPlaceholder.messageErrorTextValue(survey.getProposition_3().toUpperCase());
                                }
                            }
                        } else if (reponse_choisie.equalsIgnoreCase(survey.getReponse())) {
                            this.iPlaceholder.messageOkTextValue(survey.getProposition_2().toUpperCase());
                        } else {
                            this.iPlaceholder.messageErrorTextValue(survey.getProposition_2().toUpperCase());
                        }
                    } else if (reponse_choisie.equalsIgnoreCase(survey.getReponse())) {
                        this.iPlaceholder.messageOkTextValue(survey.getProposition_1().toUpperCase());
                    } else {
                        this.iPlaceholder.messageErrorTextValue(survey.getProposition_1().toUpperCase());
                    }
                    this.iPlaceholder.enableQuizzGameContent(false);
                }
            } catch (Exception e) {
                Log.e("TAG_ERROR", "QuizzSaveListPresenter-->loadPlaceHolderData() : " + e.getMessage());
            }
        }
    }

    public void loadQuizzSaveListData(int i) {
        if (i > 0) {
            try {
                if (this.iQuizzSaveList != null) {
                    this.iQuizzSaveList.initialize();
                    this.iQuizzSaveList.events();
                    this.iQuizzSaveList.modifyNumberPage("1/" + i);
                }
            } catch (Exception e) {
                Log.e("TAG_ERROR", "QuizzSaveListPresenter-->loadQuizzSaveListData() : " + e.getMessage());
            }
        }
    }

    public void retrieveUserAction(View view) {
        QuizzSaveListView.IQuizzSaveList iQuizzSaveList;
        if (view.getId() == 2131296482 && (iQuizzSaveList = this.iQuizzSaveList) != null) {
            iQuizzSaveList.closeActivity();
        }
    }

    public void retrieveUserAction(View view, int i, int i2) {
        int i3;
        try {
            switch (view.getId()) {
                case C0598R.id.fab_quizz_list_next /* 2131296411 */:
                    i3 = i + 1;
                    if (i3 >= i2) {
                        i3 = 0;
                        break;
                    }
                    break;
                case C0598R.id.fab_quizz_list_previous /* 2131296412 */:
                    i3 = i - 1;
                    if (i3 < 0) {
                        i3 = i2 - 1;
                        break;
                    }
                    break;
                default:
                    i3 = -1;
                    break;
            }
            if (i3 < 0 || this.iQuizzSaveList == null) {
                return;
            }
            this.iQuizzSaveList.changeViewPagerPosition(i3);
        } catch (Exception e) {
            Log.e("TAG_ERROR", "QuizzSaveListPresenter-->retrieveUserAction() : " + e.getMessage());
        }
    }

    public void showExplicationQuizz(Context context, Survey survey) {
        if (survey != null) {
            CommonPresenter.showMessage(context, context.getResources().getString(C0598R.string.lb_nav_explain), survey.getExplication(), false);
        }
    }

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /* JADX WARN: Multi-variable type inference failed */
    public void showUserAnswerOnRadioButton(RadioButton[] radioButtonArr, Survey survey) {
        try {
            String reponse_choisie = survey.getReponse_choisie();
            boolean z = -1;
            switch (reponse_choisie.hashCode()) {
                case -1361223688:
                    if (reponse_choisie.equals("choix1")) {
                        z = false;
                        break;
                    }
                    break;
                case -1361223687:
                    if (reponse_choisie.equals("choix2")) {
                        z = true;
                        break;
                    }
                    break;
                case -1361223686:
                    if (reponse_choisie.equals("choix3")) {
                        z = 2;
                        break;
                    }
                    break;
            }
            if (!z) {
                radioButtonArr[0].setChecked(true);
            } else if (z) {
                radioButtonArr[1].setChecked(true);
            } else {
                if (z != 2) {
                    return;
                }
                radioButtonArr[2].setChecked(true);
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "QuizzSaveListPresenter-->showUserAnswerOnRadioButton() : " + e.getMessage());
        }
    }
}
