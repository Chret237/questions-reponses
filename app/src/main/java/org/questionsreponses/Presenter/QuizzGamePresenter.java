package org.questionsreponses.Presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.github.clans.fab.BuildConfig;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;
import org.questionsreponses.C0598R;
import org.questionsreponses.Model.DAOSurvey;
import org.questionsreponses.Model.Survey;
import org.questionsreponses.View.Interfaces.QuizzGameView;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/Presenter/QuizzGamePresenter.class */
public class QuizzGamePresenter implements QuizzGameView.IPresenter {
    private CountDownTimer downTimer;
    private QuizzGameView.IPlaceholder iPlaceholder;
    private QuizzGameView.IQuizzGame iQuizzGame;
    private Hashtable<Integer, String> listQuizzGamePlay = new Hashtable<>();
    private int numberOfQuizzGame;
    private LoadQuizzGameAsyntask quizzGameAsyntask;

    public QuizzGamePresenter(QuizzGameView.IPlaceholder iPlaceholder) {
        this.iPlaceholder = iPlaceholder;
    }

    public QuizzGamePresenter(QuizzGameView.IQuizzGame iQuizzGame) {
        this.iQuizzGame = iQuizzGame;
    }

    /* JADX WARN: Removed duplicated region for block: B:6:0x0014  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.lang.String getLevelDifficultyInSharesPreferences(android.content.Context r4) {
        /*
            r3 = this;
            r0 = r4
            java.lang.String r1 = "KEY_QUIZZ_GAME_LEVEL_DIFFICULTY"
            java.lang.String r0 = org.questionsreponses.Presenter.CommonPresenter.getDataFromSharePreferences(r0, r1)
            r5 = r0
            r0 = r5
            if (r0 == 0) goto L14
            r0 = r5
            r4 = r0
            r0 = r5
            boolean r0 = r0.isEmpty()
            if (r0 == 0) goto L17
        L14:
            java.lang.String r0 = "KEY_QUIZZ_GAME_LEVEL_DIFFICULTY_1"
            r4 = r0
        L17:
            r0 = r4
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.questionsreponses.Presenter.QuizzGamePresenter.getLevelDifficultyInSharesPreferences(android.content.Context):java.lang.String");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getTimeByMillisecondes(long j) {
        String string;
        Object objValueOf;
        Object objValueOf2;
        Object objValueOf3;
        int i = ((int) (j / 1000)) % 60;
        int i2 = (int) ((j / 60000) % 60);
        int i3 = (int) ((j / 3600000) % 24);
        StringBuilder sb = new StringBuilder();
        sb.append(BuildConfig.FLAVOR);
        if (i3 > 0) {
            StringBuilder sb2 = new StringBuilder();
            if (i3 < 10) {
                objValueOf3 = "0" + i3;
            } else {
                objValueOf3 = Integer.valueOf(i3);
            }
            sb2.append(objValueOf3);
            sb2.append(" : ");
            string = sb2.toString();
        } else {
            string = BuildConfig.FLAVOR;
        }
        sb.append(string);
        String string2 = sb.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(string2);
        String string3 = BuildConfig.FLAVOR;
        if (i2 > 0) {
            StringBuilder sb4 = new StringBuilder();
            if (i2 < 10) {
                objValueOf2 = "0" + i2;
            } else {
                objValueOf2 = Integer.valueOf(i2);
            }
            sb4.append(objValueOf2);
            sb4.append(" : ");
            string3 = sb4.toString();
        }
        sb3.append(string3);
        String string4 = sb3.toString();
        StringBuilder sb5 = new StringBuilder();
        sb5.append(string4);
        if (i < 10) {
            objValueOf = "0" + i;
        } else {
            objValueOf = Integer.valueOf(i);
        }
        sb5.append(objValueOf);
        return sb5.toString();
    }

    @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IPresenter
    public void closeActivity(Context context, int i) throws Resources.NotFoundException {
        String string;
        String str;
        String string2;
        try {
            ArrayList<Integer> quizzGameScore = this.iQuizzGame.getQuizzGameScore();
            int iIntValue = quizzGameScore.get(0).intValue() + quizzGameScore.get(1).intValue();
            String string3 = context.getResources().getString(C0598R.string.lb_key_group_to_add);
            if (iIntValue <= 0 || iIntValue >= i) {
                this.iQuizzGame.closeActivity();
                return;
            }
            String str2 = CommonPresenter.KEY_QUIZZ_GAME_IS_NOT_FINISHED;
            ArrayList<Survey> quizzGameDataFromStorage = this.iQuizzGame.getQuizzGameDataFromStorage();
            String strReplace = quizzGameDataFromStorage.get(0).getClef_groupe().replace("-" + context.getResources().getString(C0598R.string.lb_key_group_to_add), BuildConfig.FLAVOR);
            if (quizzGameDataFromStorage.get(0).getClef_groupe().indexOf(context.getResources().getString(C0598R.string.lb_key_group_to_add)) >= 0) {
                new DAOSurvey(context).deleteDataBy(quizzGameDataFromStorage.get(0).getClef_groupe());
                str2 = CommonPresenter.KEY_QUIZZ_GAME_WAS_NOT_FINISHED_BEFORE;
            }
            int minScoreToUnlockedInSharesPreferences = quizzGameDataFromStorage.get(0).getClef_groupe().indexOf(context.getResources().getString(C0598R.string.lb_key_group_spiritual_growth)) >= 0 ? getMinScoreToUnlockedInSharesPreferences(context) : -1;
            if (minScoreToUnlockedInSharesPreferences > 0) {
                str = CommonPresenter.KEY_QUIZZ_SPIRITUAL_GROWTH_FAILED;
                if (minScoreToUnlockedInSharesPreferences <= quizzGameScore.get(0).intValue()) {
                    for (int i2 = 0; i2 < quizzGameDataFromStorage.size(); i2++) {
                        new DAOSurvey(context).insertData(quizzGameDataFromStorage.get(i2).getQuestion(), quizzGameDataFromStorage.get(i2).getCategorie(), quizzGameDataFromStorage.get(i2).getExplication(), quizzGameDataFromStorage.get(i2).getProposition_1(), quizzGameDataFromStorage.get(i2).getProposition_2(), quizzGameDataFromStorage.get(i2).getProposition_3(), quizzGameDataFromStorage.get(i2).getReponse(), quizzGameDataFromStorage.get(i2).getReponse_choisie() == null ? BuildConfig.FLAVOR : quizzGameDataFromStorage.get(i2).getReponse_choisie(), quizzGameDataFromStorage.get(i2).getClef_groupe(), quizzGameDataFromStorage.get(i2).getTotal_question(), quizzGameScore.get(0).intValue(), 20 - quizzGameScore.get(0).intValue(), quizzGameDataFromStorage.get(i2).getTitre_quizz());
                    }
                    string = context.getResources().getString(C0598R.string.lb_quizz_game_locked);
                    string2 = context.getResources().getString(C0598R.string.lb_quizz_game_detail_locked);
                    str = CommonPresenter.KEY_QUIZZ_SPIRITUAL_GROWTH_SUCCEEDED;
                    Log.i("TAG_QUIZZ_GAME", "QUIZZ GAME LEVEL HAS BEEN SAVED, SCORE = " + quizzGameScore.get(0));
                } else {
                    string = context.getResources().getString(C0598R.string.lb_quizz_game_not_unlocked);
                    string2 = minScoreToUnlockedInSharesPreferences < 20 ? context.getResources().getString(C0598R.string.lb_level_explication).replace("{NUMBER_ANSWER}", BuildConfig.FLAVOR + minScoreToUnlockedInSharesPreferences) : context.getResources().getString(C0598R.string.lb_level_find_all);
                }
            } else {
                for (int i3 = 0; i3 < quizzGameDataFromStorage.size(); i3++) {
                    new DAOSurvey(context).insertData(quizzGameDataFromStorage.get(i3).getQuestion(), quizzGameDataFromStorage.get(i3).getCategorie(), quizzGameDataFromStorage.get(i3).getExplication(), quizzGameDataFromStorage.get(i3).getProposition_1(), quizzGameDataFromStorage.get(i3).getProposition_2(), quizzGameDataFromStorage.get(i3).getProposition_3(), quizzGameDataFromStorage.get(i3).getReponse(), quizzGameDataFromStorage.get(i3).getReponse_choisie() == null ? BuildConfig.FLAVOR : quizzGameDataFromStorage.get(i3).getReponse_choisie(), strReplace + "-" + string3, quizzGameDataFromStorage.get(i3).getTotal_question(), quizzGameScore.get(0).intValue(), quizzGameScore.get(1).intValue(), quizzGameDataFromStorage.get(i3).getTitre_quizz());
                    StringBuilder sb = new StringBuilder();
                    sb.append("SAVE = ");
                    sb.append(quizzGameDataFromStorage.get(i3).getReponse_choisie() == null ? BuildConfig.FLAVOR : quizzGameDataFromStorage.get(i3).getReponse_choisie());
                    Log.i("TAG_QUIZZ_GAME", sb.toString());
                }
                string = context.getResources().getString(C0598R.string.lb_quizz_game_not_end);
                str = str2;
                string2 = context.getResources().getString(C0598R.string.lb_quizz_not_end_save);
            }
            CommonPresenter.showMessage(context, string.toUpperCase(), string2, true);
            if (CommonPresenter.getSettingObjectFromSharePreferences(context, CommonPresenter.KEY_SETTING_AUTOMATIC_SOUND_READING).getChoice()) {
                this.iQuizzGame.readTextFromTextToSpeech(string2);
            }
            Intent intent = new Intent();
            intent.putExtra(CommonPresenter.KEY_QUIZZ_GAME_RETURN_DATA, str);
            ((Activity) context).setResult(-1, intent);
            CountDownTimer countDownTimerRetrieveCountDownTimerFromStorage = this.iQuizzGame.retrieveCountDownTimerFromStorage();
            if (countDownTimerRetrieveCountDownTimerFromStorage != null) {
                countDownTimerRetrieveCountDownTimerFromStorage.cancel();
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "QuizzGamePresenter-->closeActivity() : " + e.getMessage());
        }
    }

    @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IPresenter
    public int getLevelSelectedInSurveyTitle(String str) {
        if (str == null) {
            return -1;
        }
        try {
            if (str.length() != 0 && !str.isEmpty()) {
                return Integer.parseInt(str.trim().split(" ")[1]);
            }
            return -1;
        } catch (Exception e) {
            Log.e("TAG_ERROR", "QuizzGamePresenter-->getLevelSelectedInSurveyTitle() : " + e.getMessage());
            return -1;
        }
    }

    @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IPresenter
    public int getMinScoreToUnlockedInSharesPreferences(Context context) {
        String dataFromSharePreferences = CommonPresenter.getDataFromSharePreferences(context, CommonPresenter.KEY_QUIZZ_MIN_SCORE_TO_UNLOCK);
        return (dataFromSharePreferences == null || dataFromSharePreferences.isEmpty()) ? -1 : Integer.parseInt(dataFromSharePreferences);
    }

    @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IPresenter
    public int getNumberOfQuizzGameFinded() {
        return this.numberOfQuizzGame;
    }

    @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IPresenter
    public int getStepSelectedInSurveyTitle(String str) {
        if (str == null) {
            return -1;
        }
        try {
            if (str.length() != 0 && !str.isEmpty()) {
                String[] strArrSplit = str.trim().split(" ");
                return Integer.parseInt(strArrSplit[strArrSplit.length - 1]);
            }
            return -1;
        } catch (Exception e) {
            Log.e("TAG_ERROR", "QuizzGamePresenter-->getStepSelectedInSurveyTitle() : " + e.getMessage());
            return -1;
        }
    }

    @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IPresenter
    public void initializeTextToSpeech(int i, TextToSpeech textToSpeech) {
        try {
            if (i == 0) {
                int language = textToSpeech.setLanguage(Locale.FRANCE);
                if (language == -1 || language == -2) {
                    Log.i("TAG_TEXT_TO_SPEECH", "Language is not supported");
                } else {
                    Log.i("TAG_TEXT_TO_SPEECH", "Language is supported");
                    this.iQuizzGame.readTextFromTextToSpeech(BuildConfig.FLAVOR);
                }
            } else {
                Log.i("TAG_TEXT_TO_SPEECH", "Initilization Failed!");
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "QuizzGamePresenter-->initializeTextToSpeech() : " + e.getMessage());
        }
    }

    @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IPresenter
    public void loadHeaderQuizzGame(Survey survey) {
        try {
            if (this.iQuizzGame != null) {
                this.iQuizzGame.headerQuizzGame(survey);
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "QuizzGamePresenter-->loadHeaderQuizzGame() : " + e.getMessage());
        }
    }

    @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IPresenter
    public void loadPlaceholderData(View view, int i) {
        try {
            if (this.iPlaceholder != null) {
                this.iPlaceholder.findWidgets(view);
                this.iPlaceholder.events();
                QuizzGameView.IQuizzGame iQuizzGameInstance = this.iPlaceholder.getIQuizzGameInstance();
                if (iQuizzGameInstance != null) {
                    ArrayList<Survey> quizzGameDataFromStorage = iQuizzGameInstance.getQuizzGameDataFromStorage();
                    if (quizzGameDataFromStorage.size() > i) {
                        this.iPlaceholder.loadQuizzGameData(quizzGameDataFromStorage.get(i));
                    }
                }
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "QuizzGamePresenter-->loadPlaceholderData() : " + e.getMessage());
        }
    }

    @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IPresenter
    public void loadQuizzGameData(Context context, Intent intent) {
        if (intent != null) {
            try {
                if (this.iQuizzGame != null) {
                    if (intent != null) {
                        this.iQuizzGame.findWidgets();
                        this.iQuizzGame.events();
                        this.iQuizzGame.progressBarVisibility(0);
                        this.iQuizzGame.numberPageLayoutVisibility(8);
                        LoadQuizzGameAsyntask loadQuizzGameAsyntask = new LoadQuizzGameAsyntask();
                        this.quizzGameAsyntask = loadQuizzGameAsyntask;
                        loadQuizzGameAsyntask.initialize(context, intent, this);
                        this.quizzGameAsyntask.execute(new Void[0]);
                    } else {
                        this.iQuizzGame.closeActivity();
                    }
                }
            } catch (Exception e) {
                Log.e("TAG_ERROR", "QuizzGamePresenter-->loadQuizzGameData() : " + e.getMessage());
            }
        }
    }

    @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IPresenter
    public void manageButtonStatus(Context context, Button[] buttonArr, Survey survey) {
        try {
            if (survey.getReponse_choisie() != null) {
                buttonArr[0].setText(context.getResources().getString(C0598R.string.lb_nav_explain));
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "QuizzGamePresenter-->manageButtonStatus() : " + e.getMessage());
        }
    }

    @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IPresenter
    public void manageImageButtonStatus(ImageButton[] imageButtonArr, Survey survey) {
        try {
            if (survey.getReponse_choisie() != null) {
                imageButtonArr[0].setVisibility(8);
                imageButtonArr[1].setVisibility(8);
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "QuizzGamePresenter-->manageImageButtonStatus() : " + e.getMessage());
        }
    }

    @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IPresenter
    public void manageLinearLayoutStatus(LinearLayout[] linearLayoutArr, Survey survey) {
        try {
            String reponse_choisie = survey.getReponse_choisie();
            if (reponse_choisie != null) {
                if (reponse_choisie.equalsIgnoreCase(survey.getReponse())) {
                    linearLayoutArr[0].setVisibility(8);
                    linearLayoutArr[1].setVisibility(0);
                } else {
                    linearLayoutArr[0].setVisibility(0);
                    linearLayoutArr[1].setVisibility(8);
                }
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "QuizzGamePresenter-->manageLinearLayoutStatus() : " + e.getMessage());
        }
    }

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IPresenter
    public void manageRadioButtonStatus(RadioButton[] radioButtonArr, Survey survey) {
        try {
            String reponse_choisie = survey.getReponse_choisie();
            if (reponse_choisie != null) {
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
                } else if (z == 2) {
                    radioButtonArr[2].setChecked(true);
                }
                radioButtonArr[0].setEnabled(false);
                radioButtonArr[1].setEnabled(false);
                radioButtonArr[2].setEnabled(false);
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "QuizzGamePresenter-->manageRadioButtonStatus() : " + e.getMessage());
        }
    }

    @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IPresenter
    public void manageTextViewStatus(Context context, TextView[] textViewArr, Survey survey) {
        try {
            String reponse_choisie = survey.getReponse_choisie();
            if (reponse_choisie != null) {
                Hashtable hashtable = new Hashtable();
                hashtable.put("choix1", survey.getProposition_1());
                hashtable.put("choix2", survey.getProposition_2());
                hashtable.put("choix3", survey.getProposition_3());
                String str = (String) hashtable.get(survey.getReponse());
                if (reponse_choisie.equalsIgnoreCase(survey.getReponse())) {
                    textViewArr[0].setText(context.getResources().getString(C0598R.string.lb_nav_quizz_ok_message) + " " + str.toUpperCase());
                } else {
                    textViewArr[1].setText(context.getResources().getString(C0598R.string.lb_nav_quizz_message) + " " + str.toUpperCase());
                }
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "QuizzGamePresenter-->manageTextViewStatus() : " + e.getMessage());
        }
    }

    @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IPresenter
    public void onActivityDestroyed(TextToSpeech textToSpeech) {
        if (textToSpeech != null) {
            try {
                textToSpeech.stop();
                textToSpeech.shutdown();
            } catch (Exception e) {
                Log.e("TAG_ERROR", "QuizzGamePresenter-->onActivityDestroyed() : " + e.getMessage());
                return;
            }
        }
        if (this.downTimer != null) {
            this.downTimer.cancel();
        }
    }

    @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IPresenter
    public void onLoadQuizzGameDataFailed(Context context) {
        try {
            if (this.iQuizzGame != null) {
                this.iQuizzGame.progressBarVisibility(8);
                this.iQuizzGame.numberPageLayoutVisibility(8);
                CommonPresenter.showMessageSnackBar(CommonPresenter.getViewInTermsOfContext(context), context.getResources().getString(C0598R.string.lb_nav_quizz_error));
                this.iQuizzGame.closeActivity();
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "QuizzGamePresenter-->onLoadQuizzGameDataFailed() : " + e.getMessage());
        }
    }

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:84:0x0330 A[Catch: Exception -> 0x036f, TRY_ENTER, TRY_LEAVE, TryCatch #0 {Exception -> 0x036f, blocks: (B:2:0x0000, B:4:0x0007, B:6:0x0015, B:12:0x0071, B:17:0x0144, B:19:0x014c, B:20:0x0150, B:84:0x0330, B:86:0x0342, B:81:0x02f4, B:72:0x02a8, B:58:0x0208, B:59:0x0221, B:60:0x023a, B:61:0x0253, B:62:0x026c, B:63:0x0285, B:22:0x017b, B:26:0x018b, B:30:0x019b, B:34:0x01ab, B:38:0x01bb, B:42:0x01cb, B:15:0x013d, B:10:0x002c), top: B:90:0x0000 }] */
    /* JADX WARN: Removed duplicated region for block: B:93:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r0v111, types: [org.questionsreponses.Presenter.QuizzGamePresenter$1] */
    @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IPresenter
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void onLoadQuizzGameDataFinished(android.content.Context r11, java.util.ArrayList<org.questionsreponses.Model.Survey> r12) throws android.content.res.Resources.NotFoundException {
        /*
            Method dump skipped, instructions count: 917
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.questionsreponses.Presenter.QuizzGamePresenter.onLoadQuizzGameDataFinished(android.content.Context, java.util.ArrayList):void");
    }

    @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IPresenter
    public void retrieveUserAction(View view) {
        try {
            if (this.iPlaceholder != null) {
                QuizzGameView.IQuizzGame iQuizzGameInstance = this.iPlaceholder.getIQuizzGameInstance();
                int id = view.getId();
                if (id != 2131296483) {
                    if (id == 2131296498 && iQuizzGameInstance != null) {
                        Survey survey = iQuizzGameInstance.getQuizzGameDataFromStorage().get(iQuizzGameInstance.getCurrentViewPager());
                        iQuizzGameInstance.readTextFromTextToSpeech((((BuildConfig.FLAVOR + survey.getQuestion().trim().replace("\n", " ")) + "Choix 1 " + survey.getProposition_1()) + "Choix 2 " + survey.getProposition_2()) + "Choix 3 " + survey.getProposition_3());
                    }
                } else if (iQuizzGameInstance != null) {
                    iQuizzGameInstance.simulateFabNextQuizzClick();
                }
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "QuizzGamePresenter-->retrieveUserAction() : " + e.getMessage());
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x0076  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x007b  */
    @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IPresenter
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void retrieveUserAction(android.view.View r5, int r6, int r7) {
        /*
            r4 = this;
            r0 = r4
            org.questionsreponses.View.Interfaces.QuizzGameView$IQuizzGame r0 = r0.iQuizzGame
            r10 = r0
            r0 = r10
            if (r0 == 0) goto Lb9
            r0 = -1
            r8 = r0
            r0 = r10
            java.util.ArrayList r0 = r0.getQuizzGameScore()     // Catch: java.lang.Exception -> L90
            r10 = r0
            r0 = r10
            r1 = 0
            java.lang.Object r0 = r0.get(r1)     // Catch: java.lang.Exception -> L90
            java.lang.Integer r0 = (java.lang.Integer) r0     // Catch: java.lang.Exception -> L90
            int r0 = r0.intValue()     // Catch: java.lang.Exception -> L90
            r1 = r10
            r2 = 1
            java.lang.Object r1 = r1.get(r2)     // Catch: java.lang.Exception -> L90
            java.lang.Integer r1 = (java.lang.Integer) r1     // Catch: java.lang.Exception -> L90
            int r1 = r1.intValue()     // Catch: java.lang.Exception -> L90
            int r0 = r0 + r1
            r9 = r0
            r0 = r5
            int r0 = r0.getId()     // Catch: java.lang.Exception -> L90
            switch(r0) {
                case 2131296413: goto L65;
                case 2131296414: goto L52;
                default: goto L4c;
            }     // Catch: java.lang.Exception -> L90
        L4c:
            r0 = r8
            r6 = r0
            goto L7f
        L52:
            int r6 = r6 + (-1)
            r0 = r6
            if (r0 < 0) goto L5c
            goto L7f
        L5c:
            r0 = r9
            r1 = r7
            if (r0 != r1) goto L76
            goto L7b
        L65:
            int r6 = r6 + 1
            r0 = r6
            r1 = r7
            if (r0 >= r1) goto L70
            goto L7f
        L70:
            r0 = r9
            r1 = r7
            if (r0 != r1) goto L7b
        L76:
            r0 = 0
            r6 = r0
            goto L7f
        L7b:
            r0 = r7
            r1 = 1
            int r0 = r0 - r1
            r6 = r0
        L7f:
            r0 = r6
            if (r0 < 0) goto Lb9
            r0 = r4
            org.questionsreponses.View.Interfaces.QuizzGameView$IQuizzGame r0 = r0.iQuizzGame     // Catch: java.lang.Exception -> L90
            r1 = r6
            r0.changeViewPagerPosition(r1)     // Catch: java.lang.Exception -> L90
            goto Lb9
        L90:
            r5 = move-exception
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r1 = r0
            r1.<init>()
            r10 = r0
            r0 = r10
            java.lang.String r1 = "QuizzGamePresenter-->retrieveUserAction() : "
            java.lang.StringBuilder r0 = r0.append(r1)
            r0 = r10
            r1 = r5
            java.lang.String r1 = r1.getMessage()
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r0 = "TAG_ERROR"
            r1 = r10
            java.lang.String r1 = r1.toString()
            int r0 = android.util.Log.e(r0, r1)
        Lb9:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.questionsreponses.Presenter.QuizzGamePresenter.retrieveUserAction(android.view.View, int, int):void");
    }

    @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IPresenter
    public void retrieveViewPagerPosition(int i, int i2) {
        try {
            if (this.iQuizzGame != null) {
                this.iQuizzGame.modifyNumberPageValue((i + 1) + "/" + i2);
                ArrayList<Integer> quizzGameScore = this.iQuizzGame.getQuizzGameScore();
                int iIntValue = quizzGameScore.get(0).intValue();
                int iIntValue2 = quizzGameScore.get(1).intValue();
                if (quizzGameScore.get(0).intValue() + quizzGameScore.get(1).intValue() < i2) {
                    this.iQuizzGame.fabPreviousQuizzGameVisibility(i > 0 ? 0 : 8);
                    this.iQuizzGame.fabNextQuizzGameVisibility(i < iIntValue + iIntValue2 ? 0 : 8);
                } else {
                    this.iQuizzGame.fabPreviousQuizzGameVisibility(0);
                    this.iQuizzGame.fabNextQuizzGameVisibility(0);
                }
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "QuizzGamePresenter-->retrieveViewPagerPosition() : " + e.getMessage());
        }
    }

    @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IPresenter
    public void retrieveWidgetsViews(View view, RadioGroup radioGroup, RadioButton[] radioButtonArr, ImageButton[] imageButtonArr, LinearLayout[] linearLayoutArr, TextView[] textViewArr, Button[] buttonArr) throws Resources.NotFoundException {
        boolean z;
        String string;
        String str;
        String string2;
        Activity activity = CommonPresenter.getActivity(view);
        if (this.iPlaceholder == null || activity == null) {
            return;
        }
        try {
            Hashtable hashtable = new Hashtable();
            hashtable.put(Integer.valueOf(C0598R.id.nav_quizz_suggest_1), "choix1");
            hashtable.put(Integer.valueOf(C0598R.id.nav_quizz_suggest_2), "choix2");
            hashtable.put(Integer.valueOf(C0598R.id.nav_quizz_suggest_3), "choix3");
            hashtable.put(-1, "choix4");
            String str2 = (String) hashtable.get(Integer.valueOf(radioGroup.getCheckedRadioButtonId()));
            QuizzGameView.IQuizzGame iQuizzGameInstance = this.iPlaceholder.getIQuizzGameInstance();
            if (iQuizzGameInstance != null) {
                int currentViewPager = iQuizzGameInstance.getCurrentViewPager();
                ArrayList<Survey> quizzGameDataFromStorage = iQuizzGameInstance.getQuizzGameDataFromStorage();
                Survey survey = quizzGameDataFromStorage.get(currentViewPager);
                if (this.listQuizzGamePlay.containsKey(Integer.valueOf(currentViewPager))) {
                    return;
                }
                iQuizzGameInstance.stopReadingFromTextToSpeech();
                this.listQuizzGamePlay.put(Integer.valueOf(currentViewPager), str2);
                survey.setReponse_choisie(str2);
                quizzGameDataFromStorage.set(currentViewPager, survey);
                iQuizzGameInstance.storageQuizzGameData(quizzGameDataFromStorage);
                ArrayList<Integer> quizzGameScore = iQuizzGameInstance.getQuizzGameScore();
                if (quizzGameScore.get(0).intValue() + quizzGameScore.get(1).intValue() < survey.getTotal_question()) {
                    if (str2.equalsIgnoreCase(survey.getReponse())) {
                        iQuizzGameInstance.setQuizzGameScore(1, 0);
                        z = true;
                    } else {
                        iQuizzGameInstance.setQuizzGameScore(0, 1);
                        z = false;
                    }
                    quizzGameScore = iQuizzGameInstance.getQuizzGameScore();
                    survey.setTotal_trouve(quizzGameScore.get(0).intValue());
                    survey.setTotal_erreur(quizzGameScore.get(1).intValue());
                    iQuizzGameInstance.headerQuizzGame(survey);
                } else {
                    z = false;
                }
                manageRadioButtonStatus(radioButtonArr, survey);
                manageImageButtonStatus(imageButtonArr, survey);
                manageLinearLayoutStatus(linearLayoutArr, survey);
                manageTextViewStatus(activity, textViewArr, survey);
                manageButtonStatus(activity, buttonArr, survey);
                if (z) {
                    int i = currentViewPager + 1;
                    if (i < survey.getTotal_question()) {
                        iQuizzGameInstance.gotoSelectedViewPager(i);
                    }
                } else {
                    iQuizzGameInstance.fabNextQuizzGameVisibility(0);
                }
                int minScoreToUnlockedInSharesPreferences = quizzGameDataFromStorage.get(0).getClef_groupe().indexOf(activity.getResources().getString(C0598R.string.lb_key_group_spiritual_growth)) >= 0 ? getMinScoreToUnlockedInSharesPreferences(activity) : -1;
                if (quizzGameScore.get(0).intValue() + quizzGameScore.get(1).intValue() == survey.getTotal_question()) {
                    iQuizzGameInstance.fabNextQuizzGameVisibility(8);
                    this.listQuizzGamePlay.put(1000, "FIN DU QUIZZ");
                    ArrayList<Survey> quizzGameDataFromStorage2 = iQuizzGameInstance.getQuizzGameDataFromStorage();
                    String strReplace = quizzGameDataFromStorage2.get(0).getClef_groupe().replace("-" + activity.getResources().getString(C0598R.string.lb_key_group_to_add), BuildConfig.FLAVOR);
                    String str3 = CommonPresenter.KEY_QUIZZ_GAME_IS_FINISHED;
                    if (quizzGameDataFromStorage2.get(0).getClef_groupe().indexOf(activity.getResources().getString(C0598R.string.lb_key_group_to_add)) >= 0) {
                        new DAOSurvey(activity).deleteDataBy(quizzGameDataFromStorage2.get(0).getClef_groupe());
                        str3 = CommonPresenter.KEY_QUIZZ_GAME_WAS_NOT_FINISHED_BEFORE;
                    }
                    if ((minScoreToUnlockedInSharesPreferences > 0 && minScoreToUnlockedInSharesPreferences <= quizzGameScore.get(0).intValue()) || minScoreToUnlockedInSharesPreferences == -1) {
                        for (int i2 = 0; i2 < quizzGameDataFromStorage2.size(); i2++) {
                            new DAOSurvey(activity).insertData(quizzGameDataFromStorage2.get(i2).getQuestion(), quizzGameDataFromStorage2.get(i2).getCategorie(), quizzGameDataFromStorage2.get(i2).getExplication(), quizzGameDataFromStorage2.get(i2).getProposition_1(), quizzGameDataFromStorage2.get(i2).getProposition_2(), quizzGameDataFromStorage2.get(i2).getProposition_3(), quizzGameDataFromStorage2.get(i2).getReponse(), quizzGameDataFromStorage2.get(i2).getReponse_choisie() == null ? BuildConfig.FLAVOR : quizzGameDataFromStorage2.get(i2).getReponse_choisie(), strReplace, quizzGameDataFromStorage2.get(i2).getTotal_question(), quizzGameScore.get(0).intValue(), quizzGameScore.get(1).intValue(), quizzGameDataFromStorage2.get(i2).getTitre_quizz());
                        }
                    }
                    if (minScoreToUnlockedInSharesPreferences > 0) {
                        str = CommonPresenter.KEY_QUIZZ_SPIRITUAL_GROWTH_FAILED;
                        if (minScoreToUnlockedInSharesPreferences <= quizzGameScore.get(0).intValue()) {
                            string2 = activity.getResources().getString(C0598R.string.lb_quizz_game_locked);
                            String string3 = activity.getResources().getString(C0598R.string.lb_quizz_game_detail_locked);
                            str = CommonPresenter.KEY_QUIZZ_SPIRITUAL_GROWTH_SUCCEEDED;
                            Log.i("TAG_QUIZZ_GAME", "QUIZZ GAME LEVEL HAS BEEN SAVED, SCORE = " + quizzGameScore.get(0));
                            string = string3;
                        } else {
                            string2 = activity.getResources().getString(C0598R.string.lb_quizz_game_not_unlocked);
                            string = minScoreToUnlockedInSharesPreferences < 20 ? activity.getResources().getString(C0598R.string.lb_level_explication).replace("{NUMBER_ANSWER}", BuildConfig.FLAVOR + minScoreToUnlockedInSharesPreferences) : activity.getResources().getString(C0598R.string.lb_level_find_all);
                        }
                    } else {
                        String string4 = activity.getResources().getString(C0598R.string.lb_quizz_game_end);
                        string = activity.getResources().getString(C0598R.string.lb_quizz_end_save);
                        str = str3;
                        string2 = string4;
                    }
                    CommonPresenter.showMessage(activity, string2.toUpperCase(), string, true);
                    if (CommonPresenter.getSettingObjectFromSharePreferences(activity, CommonPresenter.KEY_SETTING_AUTOMATIC_SOUND_READING).getChoice()) {
                        iQuizzGameInstance.readTextFromTextToSpeech(string);
                    }
                    Intent intent = new Intent();
                    intent.putExtra(CommonPresenter.KEY_QUIZZ_GAME_RETURN_DATA, str);
                    activity.setResult(-1, intent);
                    CountDownTimer countDownTimerRetrieveCountDownTimerFromStorage = iQuizzGameInstance.retrieveCountDownTimerFromStorage();
                    if (countDownTimerRetrieveCountDownTimerFromStorage != null) {
                        countDownTimerRetrieveCountDownTimerFromStorage.cancel();
                    }
                }
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "QuizzGamePresenter-->retrieveWidgetsViews() : " + e.getMessage());
        }
    }

    @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IPresenter
    public void setNumberOfQuizzGameFinded(int i) {
        this.numberOfQuizzGame = i;
    }

    @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IPresenter
    public void showExplicationQuizz(Context context, int i) {
        QuizzGameView.IQuizzGame iQuizzGameInstance;
        try {
            if (this.iPlaceholder == null || (iQuizzGameInstance = this.iPlaceholder.getIQuizzGameInstance()) == null) {
                return;
            }
            ArrayList<Survey> quizzGameDataFromStorage = iQuizzGameInstance.getQuizzGameDataFromStorage();
            if (quizzGameDataFromStorage.size() > i) {
                CommonPresenter.showMessage(context, context.getResources().getString(C0598R.string.lb_nav_explain), quizzGameDataFromStorage.get(i).getExplication(), false);
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "QuizzGamePresenter-->showExplicationQuizz() : " + e.getMessage());
        }
    }
}
