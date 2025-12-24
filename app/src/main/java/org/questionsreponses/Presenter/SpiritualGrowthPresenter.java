package org.questionsreponses.Presenter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.SQLException;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import com.github.clans.fab.BuildConfig;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import org.questionsreponses.C0598R;
import org.questionsreponses.Model.DAOSurvey;
import org.questionsreponses.Model.Quizz;
import org.questionsreponses.Model.Survey;
import org.questionsreponses.View.Interfaces.SpiritualGrowthView;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/Presenter/SpiritualGrowthPresenter.class */
public class SpiritualGrowthPresenter implements SpiritualGrowthView.IPresenter {
    private CountDownTimer downTimer;
    private LoadSpiritualGrowthAsyntask growthAsyntask;
    private SpiritualGrowthView.ISpiritualGrowth iSpiritualGrowth;
    private String textToRead;

    public SpiritualGrowthPresenter(SpiritualGrowthView.ISpiritualGrowth iSpiritualGrowth) {
        this.iSpiritualGrowth = iSpiritualGrowth;
    }

    private ArrayList<Survey> getAllLevelSteps(Context context, int i, int i2) throws Resources.NotFoundException, SQLException {
        int i3;
        ArrayList<Survey> arrayList = new ArrayList<>();
        ArrayList<Survey> allSpiritualGrowthKeyGroupByLevel = new DAOSurvey(context).getAllSpiritualGrowthKeyGroupByLevel(i);
        if (allSpiritualGrowthKeyGroupByLevel == null || allSpiritualGrowthKeyGroupByLevel.size() <= 0) {
            i3 = 1;
        } else {
            i3 = 1;
            for (int i4 = 0; i4 < allSpiritualGrowthKeyGroupByLevel.size(); i4++) {
                Survey survey = allSpiritualGrowthKeyGroupByLevel.get(i4);
                arrayList.add(new Survey(survey.getId(), survey.getClef_groupe(), survey.getTotal_question(), survey.getTotal_trouve(), survey.getTotal_erreur(), survey.getTitre_quizz(), survey.getDate(), true, i2, i));
                i3++;
            }
        }
        int i5 = i3;
        while (i5 <= 20) {
            Random random = new Random();
            arrayList.add(new Survey(i5 * (-1), (random.nextInt(1000000) + "-" + random.nextInt(1000000) + "-" + random.nextInt(1000000)) + "-" + context.getResources().getString(C0598R.string.lb_key_group_spiritual_growth) + "-level-" + i + "-step-" + i5, 20, 0, 0, context.getResources().getString(C0598R.string.lb_level) + " " + i + " - " + context.getResources().getString(C0598R.string.lb_step) + " " + i5, CommonPresenter.getCurrentDate(), i5 == i3, i2, i));
            i5++;
        }
        return arrayList;
    }

    private String getKeyGroupeSelectedInSharesPreferences(Context context) {
        return CommonPresenter.getDataFromSharePreferences(context, CommonPresenter.KEY_QUIZZ_GROUPKEY_SELECTED);
    }

    private int getLevelSelectedInSharesPreferences(Context context) {
        String dataFromSharePreferences = CommonPresenter.getDataFromSharePreferences(context, CommonPresenter.KEY_QUIZZ_SPIRITUAL_GROWTH_LEVEL_SELECTED);
        return (dataFromSharePreferences == null || dataFromSharePreferences.isEmpty()) ? 0 : Integer.parseInt(dataFromSharePreferences);
    }

    private int getMinScoreToUnlockedInSharesPreferences(Context context) {
        String dataFromSharePreferences = CommonPresenter.getDataFromSharePreferences(context, CommonPresenter.KEY_QUIZZ_MIN_SCORE_TO_UNLOCK);
        return (dataFromSharePreferences == null || dataFromSharePreferences.isEmpty()) ? -1 : Integer.parseInt(dataFromSharePreferences);
    }

    public void closeActivity() {
        try {
            if (this.iSpiritualGrowth != null) {
                this.iSpiritualGrowth.closeActivity();
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "SpiritualGrowthPresenter-->closeActivity() : " + e.getMessage());
        }
    }

    public void initializeTextToSpeech(int i, TextToSpeech textToSpeech) {
        if (i == 0) {
            try {
                int language = textToSpeech.setLanguage(Locale.FRANCE);
                if (language == -1 || language == -2 || this.iSpiritualGrowth == null) {
                    return;
                }
                this.iSpiritualGrowth.readTextFromTextToSpeech(BuildConfig.FLAVOR);
            } catch (Exception e) {
                Log.e("TAG_ERROR", "SpiritualGrowthPresenter-->initializeTextToSpeech() : " + e.getMessage());
            }
        }
    }

    public void launchQuizzSpiritualGrowth(Context context, String str, String str2, int i) {
        LoadSpiritualGrowthAsyntask loadSpiritualGrowthAsyntask = new LoadSpiritualGrowthAsyntask();
        this.growthAsyntask = loadSpiritualGrowthAsyntask;
        loadSpiritualGrowthAsyntask.initialize(context, str, str2, i, this, this);
        this.growthAsyntask.execute(new Void[0]);
    }

    /* JADX WARN: Type inference failed for: r0v75, types: [org.questionsreponses.Presenter.SpiritualGrowthPresenter$1] */
    public void loadSpiritualGrowthData(Context context, Intent intent, int i) throws Resources.NotFoundException, NumberFormatException {
        String string;
        int i2;
        if (intent != null) {
            try {
                if (this.iSpiritualGrowth != null) {
                    this.iSpiritualGrowth.initialize();
                    this.iSpiritualGrowth.events();
                    String stringExtra = intent.getStringExtra(CommonPresenter.KEY_QUIZZ_SPIRITUAL_GROWTH_LEVEL_SELECTED);
                    String str = context.getResources().getString(C0598R.string.lb_level) + " " + stringExtra;
                    if (Integer.parseInt(stringExtra) <= 6) {
                        i2 = 20 - (7 - Integer.parseInt(stringExtra));
                        string = context.getResources().getString(C0598R.string.lb_level_explication).replace("{NUMBER_ANSWER}", BuildConfig.FLAVOR + i2);
                    } else {
                        string = context.getResources().getString(C0598R.string.lb_level_find_all);
                        i2 = 20;
                    }
                    this.textToRead = string;
                    if (CommonPresenter.getSettingObjectFromSharePreferences(context, CommonPresenter.KEY_SETTING_AUTOMATIC_SOUND_READING).getChoice()) {
                        ArrayList<Survey> allSpiritualGrowthKeyGroupByLevel = new DAOSurvey(context).getAllSpiritualGrowthKeyGroupByLevel(Integer.parseInt(stringExtra));
                        if (allSpiritualGrowthKeyGroupByLevel.size() == 20) {
                            this.textToRead = context.getResources().getString(C0598R.string.lb_quizz_game_level_unlocked);
                            if (Integer.parseInt(stringExtra) == 10) {
                                this.textToRead = context.getResources().getString(C0598R.string.lb_quizz_game_all_level_finished);
                            }
                        }
                        this.downTimer = new CountDownTimer(this, 500L, 500L, allSpiritualGrowthKeyGroupByLevel, i) { // from class: org.questionsreponses.Presenter.SpiritualGrowthPresenter.1
                            final SpiritualGrowthPresenter this$0;
                            final int val$positionScroll;
                            final ArrayList val$tmpSurvey;

                            {
                                this.this$0 = this;
                                this.val$tmpSurvey = allSpiritualGrowthKeyGroupByLevel;
                                this.val$positionScroll = i;
                            }

                            @Override // android.os.CountDownTimer
                            public void onFinish() {
                                if (!(this.val$tmpSurvey.size() == 20 && this.val$positionScroll == -1) && (this.val$positionScroll != 0 || this.val$tmpSurvey.size() >= 20)) {
                                    return;
                                }
                                this.this$0.iSpiritualGrowth.readTextFromTextToSpeech(this.this$0.textToRead);
                            }

                            @Override // android.os.CountDownTimer
                            public void onTick(long j) {
                            }
                        }.start();
                    }
                    this.iSpiritualGrowth.modifyHeaderToolBar(str, this.textToRead);
                    ArrayList<Survey> allLevelSteps = getAllLevelSteps(context, Integer.parseInt(stringExtra), i2);
                    StringBuilder sb = new StringBuilder();
                    sb.append(BuildConfig.FLAVOR);
                    sb.append(allLevelSteps == null ? "IS NULL" : allLevelSteps.get(0).toString());
                    Log.i("TAG_SPIRITUAL_GROWTH", sb.toString());
                    int i3 = 0;
                    if (i == -1) {
                        int i4 = Integer.parseInt(getKeyGroupeSelectedInSharesPreferences(context).split("-step-")[1]);
                        i3 = 0;
                        if (i4 >= 4) {
                            i3 = i4 - 2;
                        }
                    }
                    this.iSpiritualGrowth.loadQuizzStep(allLevelSteps, 1, i3);
                }
            } catch (Exception e) {
                Log.e("TAG_ERROR", "SpiritualGrowthPresenter-->loadSpiritualGrowthData() : " + e.getMessage());
            }
        }
    }

    public void onActivityDestroyed(TextToSpeech textToSpeech) {
        if (textToSpeech != null) {
            try {
                textToSpeech.stop();
                textToSpeech.shutdown();
            } catch (Exception e) {
                Log.e("TAG_ERROR", "SpiritualGrowthPresenter-->onActivityDestroyed() : " + e.getMessage());
                return;
            }
        }
        if (this.downTimer != null) {
            this.downTimer.cancel();
        }
    }

    @Override // org.questionsreponses.View.Interfaces.SpiritualGrowthView.IPresenter
    public void onLoadQuizzSpiritualGrowthFailed(Context context) {
        try {
            if (this.iSpiritualGrowth != null) {
                CommonPresenter.showMessageSnackBar(CommonPresenter.getViewInTermsOfContext(context), context.getResources().getString(C0598R.string.lb_nav_quizz_error));
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "SpiritualGrowthPresenter-->onLoadQuizzSpiritualGrowthFailed() : " + e.getMessage());
        }
    }

    @Override // org.questionsreponses.View.Interfaces.SpiritualGrowthView.IPresenter
    public void onLoadQuizzSpiritualGrowthFinished(String str, String str2, String str3, int i) {
        try {
            if (this.iSpiritualGrowth != null) {
                this.iSpiritualGrowth.launchQuizzSpiritualGrowth(str, str2, str3, i);
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "SpiritualGrowthPresenter-->onLoadQuizzSpiritualGrowthFinished() : " + e.getMessage());
        }
    }

    public void resetQuizzIdSaveList(Context context, ArrayList<Quizz> arrayList, String str, String str2) {
        if (CommonPresenter.getTotalQuizzIdListBy(str2) + 2 >= arrayList.size()) {
            CommonPresenter.saveDataInSharePreferences(context, str, "-");
        }
    }

    public void saveKeyGroupeSelectedInSharesPreferences(Context context, String str) {
        CommonPresenter.saveDataInSharePreferences(context, CommonPresenter.KEY_QUIZZ_GROUPKEY_SELECTED, str);
    }

    public void saveLevelSelectedInSharesPreferences(Context context, int i) {
        CommonPresenter.saveDataInSharePreferences(context, CommonPresenter.KEY_QUIZZ_SPIRITUAL_GROWTH_LEVEL_SELECTED, BuildConfig.FLAVOR + i);
    }

    public void saveMinScoreToUnlockedInSharesPreferences(Context context, int i) {
        CommonPresenter.saveDataInSharePreferences(context, CommonPresenter.KEY_QUIZZ_MIN_SCORE_TO_UNLOCK, BuildConfig.FLAVOR + i);
    }
}
