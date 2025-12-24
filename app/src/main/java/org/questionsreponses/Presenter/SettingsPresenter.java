package org.questionsreponses.Presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import com.github.clans.fab.BuildConfig;
import java.util.ArrayList;
import org.questionsreponses.C0598R;
import org.questionsreponses.Model.DAOSurvey;
import org.questionsreponses.View.Interfaces.SettingsView;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/Presenter/SettingsPresenter.class */
public class SettingsPresenter {
    private SettingsView.ISettings iSettings;

    public SettingsPresenter(SettingsView.ISettings iSettings) {
        this.iSettings = iSettings;
    }

    private void saveLevelDifficultyInSharesPreferences(Context context, int i) {
        try {
            CommonPresenter.saveDataInSharePreferences(context, CommonPresenter.KEY_QUIZZ_GAME_LEVEL_DIFFICULTY, "KEY_QUIZZ_GAME_LEVEL_DIFFICULTY_" + i);
        } catch (Exception e) {
            Log.e("TAG_ERROR", "SettingsPresenter-->saveLevelDifficultyInSharesPreferences() : " + e.getMessage());
        }
    }

    public void changeLevelDifficulty(Context context, int i) {
        try {
            switch (i) {
                case 1:
                    saveLevelDifficultyInSharesPreferences(context, 1);
                    break;
                case 2:
                    saveLevelDifficultyInSharesPreferences(context, 2);
                    break;
                case 3:
                    saveLevelDifficultyInSharesPreferences(context, 3);
                    break;
                case 4:
                    saveLevelDifficultyInSharesPreferences(context, 4);
                    break;
                case 5:
                    saveLevelDifficultyInSharesPreferences(context, 5);
                    break;
                case 6:
                    saveLevelDifficultyInSharesPreferences(context, 6);
                    break;
                default:
                    return;
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "SettingsPresenter-->changeLevelDifficulty() : " + e.getMessage());
        }
    }

    public void displayNumberOfTheQuizzList(View view) {
        try {
            if (this.iSettings != null) {
                this.iSettings.showListOfNumberOfTheQuizz(view);
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "SettingsPresenter-->displayNumberOfTheQuizzList() : " + e.getMessage());
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:6:0x0014  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.String getLevelDifficultyInSharesPreferences(android.content.Context r4) {
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
        throw new UnsupportedOperationException("Method not decompiled: org.questionsreponses.Presenter.SettingsPresenter.getLevelDifficultyInSharesPreferences(android.content.Context):java.lang.String");
    }

    public void loadSettingsData(Context context) {
        try {
            if (this.iSettings != null) {
                this.iSettings.hideHeader();
                this.iSettings.initialize();
                this.iSettings.events();
                ArrayList arrayList = new ArrayList();
                arrayList.add(CommonPresenter.getSettingObjectFromSharePreferences(context, CommonPresenter.KEY_SETTING_CONFIRM_BEFORE_QUIT_APP));
                arrayList.add(CommonPresenter.getSettingObjectFromSharePreferences(context, CommonPresenter.KEY_SETTING_NUMBER_OF_QUIZZ));
                arrayList.add(CommonPresenter.getSettingObjectFromSharePreferences(context, CommonPresenter.KEY_SETTING_CONCATENATE_AUDIO_READING));
                arrayList.add(CommonPresenter.getSettingObjectFromSharePreferences(context, CommonPresenter.KEY_SETTING_CONCATENATE_VIDEO_READING));
                arrayList.add(CommonPresenter.getSettingObjectFromSharePreferences(context, CommonPresenter.KEY_SETTING_WIFI_EXCLUSIF));
                arrayList.add(CommonPresenter.getSettingObjectFromSharePreferences(context, CommonPresenter.KEY_SETTING_OFFLINE_MODE));
                arrayList.add(CommonPresenter.getSettingObjectFromSharePreferences(context, CommonPresenter.KEY_SETTING_AUTOMATIC_SOUND_READING));
                arrayList.add(CommonPresenter.getSettingObjectFromSharePreferences(context, CommonPresenter.KEY_SETTING_LEVEL_DIFFICULTY));
                arrayList.add(CommonPresenter.getSettingObjectFromSharePreferences(context, CommonPresenter.KEY_SETTING_INITIALIZE_QUIZZ_GAME));
                this.iSettings.loadSettingData(arrayList, 1, 0);
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "SettingsPresenter-->loadSettingsData() : " + e.getMessage());
        }
    }

    public void modifyNumberOfTheQuizz(SettingsView.ISettingRecycler iSettingRecycler, int i, Context context) {
        if (iSettingRecycler != null) {
            try {
                iSettingRecycler.changeNumberOfQuizz(context, i);
            } catch (Exception e) {
                Log.e("TAG_ERROR", "SettingsPresenter-->modifyNumberOfTheQuizz() : " + e.getMessage());
            }
        }
    }

    public void reinitializeQuizzData(Context context, int i) {
        try {
            DAOSurvey dAOSurvey = new DAOSurvey(context);
            if (i == 1) {
                dAOSurvey.dropAllData();
            } else if (i == 2) {
                dAOSurvey.deleteQuizzFinished();
            } else if (i == 3) {
                dAOSurvey.deleteQuizzNotFinished();
            } else if (i == 4) {
                dAOSurvey.deleteQuizzSpiritualGrowth();
            }
            Intent intent = new Intent();
            intent.putExtra(CommonPresenter.KEY_QUIZZ_GAME_RETURN_DATA, BuildConfig.FLAVOR + i);
            ((Activity) context).setResult(-1, intent);
            CommonPresenter.showMessageSnackBar(CommonPresenter.getViewInTermsOfContext(context), context.getResources().getString(C0598R.string.lb_data_delete));
        } catch (Exception e) {
            Log.e("TAG_ERROR", "SettingsPresenter-->reinitializeQuizzData() : " + e.getMessage());
        }
    }

    public void retrieveUserAction(View view) {
        try {
            if (view.getId() == 2131296568 && this.iSettings != null) {
                this.iSettings.closeActivity();
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "SettingsPresenter-->retrieveUserAction() : " + e.getMessage());
        }
    }

    public void setSettingsISettingRecycler(SettingsView.ISettingRecycler iSettingRecycler) {
        try {
            if (this.iSettings != null) {
                this.iSettings.setSettingsISettingRecycler(iSettingRecycler);
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "SettingsPresenter-->setSettingsISettingRecycler() : " + e.getMessage());
        }
    }
}
