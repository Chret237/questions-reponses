package org.questionsreponses.View.Interfaces;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import java.util.ArrayList;
import org.questionsreponses.Model.Survey;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Interfaces/QuizzGameView.class */
public class QuizzGameView {

    /* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Interfaces/QuizzGameView$IPlaceholder.class */
    public interface IPlaceholder {
        void enableQuizzGameContent(boolean z);

        void events();

        void findWidgets(View view);

        IQuizzGame getIQuizzGameInstance();

        void loadQuizzGameData(Survey survey);

        void messageErrorTextValue(String str);

        void messageOkTextValue(String str);

        void quizzGameValidate(Context context, Survey survey);

        void simulateValidateQuizzGame();
    }

    /* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Interfaces/QuizzGameView$IPresenter.class */
    public interface IPresenter {
        void closeActivity(Context context, int i);

        int getLevelSelectedInSurveyTitle(String str);

        int getMinScoreToUnlockedInSharesPreferences(Context context);

        int getNumberOfQuizzGameFinded();

        int getStepSelectedInSurveyTitle(String str);

        void initializeTextToSpeech(int i, TextToSpeech textToSpeech);

        void loadHeaderQuizzGame(Survey survey);

        void loadPlaceholderData(View view, int i);

        void loadQuizzGameData(Context context, Intent intent);

        void manageButtonStatus(Context context, Button[] buttonArr, Survey survey);

        void manageImageButtonStatus(ImageButton[] imageButtonArr, Survey survey);

        void manageLinearLayoutStatus(LinearLayout[] linearLayoutArr, Survey survey);

        void manageRadioButtonStatus(RadioButton[] radioButtonArr, Survey survey);

        void manageTextViewStatus(Context context, TextView[] textViewArr, Survey survey);

        void onActivityDestroyed(TextToSpeech textToSpeech);

        void onLoadQuizzGameDataFailed(Context context);

        void onLoadQuizzGameDataFinished(Context context, ArrayList<Survey> arrayList);

        void retrieveUserAction(View view);

        void retrieveUserAction(View view, int i, int i2);

        void retrieveViewPagerPosition(int i, int i2);

        void retrieveWidgetsViews(View view, RadioGroup radioGroup, RadioButton[] radioButtonArr, ImageButton[] imageButtonArr, LinearLayout[] linearLayoutArr, TextView[] textViewArr, Button[] buttonArr);

        void setNumberOfQuizzGameFinded(int i);

        void showExplicationQuizz(Context context, int i);
    }

    /* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Interfaces/QuizzGameView$IQuizzGame.class */
    public interface IQuizzGame {
        void callOnBackPressedEvent();

        void changeViewPagerPosition(int i);

        void chronoVisibility(int i);

        void closeActivity();

        void events();

        void fabNextQuizzGameVisibility(int i);

        void fabPreviousQuizzGameVisibility(int i);

        void findWidgets();

        int getCurrentViewPager();

        IPlaceholder getIPlaceholderReference();

        ArrayList<Survey> getQuizzGameDataFromStorage();

        ArrayList<Integer> getQuizzGameScore();

        void gotoSelectedViewPager(int i);

        void headerQuizzGame(Survey survey);

        void loadPlaceHolderFragment(int i);

        void modifyChronoProgressData(String str, String str2);

        void modifyNumberPageValue(String str);

        void numberPageLayoutVisibility(int i);

        void progressBarVisibility(int i);

        void readTextFromTextToSpeech(String str);

        CountDownTimer retrieveCountDownTimerFromStorage();

        void setIplaceholderReference(IPlaceholder iPlaceholder);

        void setQuizzGameScore(int i, int i2);

        void simulateFabNextQuizzClick();

        void stopReadingFromTextToSpeech();

        void storageCountDownTimer(CountDownTimer countDownTimer);

        void storageQuizzGameData(ArrayList<Survey> arrayList);
    }
}
