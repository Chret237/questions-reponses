package org.questionsreponses.View.Interfaces;

import android.content.Context;
import java.util.ArrayList;
import org.questionsreponses.Model.Survey;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Interfaces/SpiritualGrowthView.class */
public class SpiritualGrowthView {

    /* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Interfaces/SpiritualGrowthView$IPresenter.class */
    public interface IPresenter {
        void onLoadQuizzSpiritualGrowthFailed(Context context);

        void onLoadQuizzSpiritualGrowthFinished(String str, String str2, String str3, int i);
    }

    /* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Interfaces/SpiritualGrowthView$ISpiritualGrowth.class */
    public interface ISpiritualGrowth {
        void closeActivity();

        void events();

        void initialize();

        void launchQuizzSpiritualGrowth(String str, String str2, String str3, int i);

        void loadQuizzStep(ArrayList<Survey> arrayList, int i, int i2);

        void modifyHeaderToolBar(String str, String str2);

        void readTextFromTextToSpeech(String str);
    }
}
