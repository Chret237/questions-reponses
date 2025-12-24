package org.questionsreponses.View.Interfaces;

import android.content.Context;
import android.view.View;
import org.questionsreponses.Model.Survey;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Interfaces/QuizzSaveListView.class */
public class QuizzSaveListView {

    /* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Interfaces/QuizzSaveListView$IPlaceholder.class */
    public interface IPlaceholder {
        void enableQuizzGameContent(boolean z);

        void events();

        void initialize(View view);

        void loadInfosQuizzGame(Context context, Survey survey);

        void messageErrorTextValue(String str);

        void messageOkTextValue(String str);
    }

    /* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Interfaces/QuizzSaveListView$IPresenter.class */
    public interface IPresenter {
    }

    /* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Interfaces/QuizzSaveListView$IQuizzSaveList.class */
    public interface IQuizzSaveList {
        void changeViewPagerPosition(int i);

        void closeActivity();

        void events();

        void initialize();

        void initializeIplaceholderReference(IPlaceholder iPlaceholder);

        void modifyHeaderQuizzGame(Survey survey);

        void modifyNumberPage(String str);
    }
}
