package org.questionsreponses.View.Interfaces;

import android.content.Context;
import android.view.View;
import java.util.List;
import org.questionsreponses.Model.Setting;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Interfaces/SettingsView.class */
public class SettingsView {

    /* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Interfaces/SettingsView$ISettingRecycler.class */
    public interface ISettingRecycler {
        void changeNumberOfQuizz(Context context, int i);
    }

    /* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Interfaces/SettingsView$ISettings.class */
    public interface ISettings {
        void closeActivity();

        void events();

        void hideHeader();

        void initialize();

        void loadSettingData(List<Setting> list, int i, int i2);

        void setSettingsISettingRecycler(ISettingRecycler iSettingRecycler);

        void showListOfNumberOfTheQuizz(View view);
    }

    /* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Interfaces/SettingsView$Ipresenter.class */
    public interface Ipresenter {
    }
}
