package org.questionsreponses.View.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import com.github.clans.fab.FloatingActionButton;
import java.util.ArrayList;
import org.questionsreponses.C0598R;
import org.questionsreponses.Model.Survey;
import org.questionsreponses.Presenter.CommonPresenter;
import org.questionsreponses.Presenter.QuizzGamePresenter;
import org.questionsreponses.View.Interfaces.QuizzGameView;
import org.questionsreponses.View.ViewPagers.QuizzGameViewPager;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Activities/QuizzGameActivity.class */
public class QuizzGameActivity extends AppCompatActivity implements QuizzGameView.IQuizzGame, TextToSpeech.OnInitListener {
    private LinearLayout chronoLayout;
    private TextView chrono_progress;
    private TextView chrono_title;
    private CountDownTimer downTimer;
    private FloatingActionButton fab_quizz_next;
    private FloatingActionButton fab_quizz_previous;
    private QuizzGameView.IPlaceholder iPlaceholder;
    private Intent intent;
    private TextView itemDate;
    private RatingBar itemRating;
    private TextView itemTitle;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private QuizzGameViewPager mViewPager;
    private ImageButton nav_quizz_back;
    private TextView nav_quizz_correct;
    private TextView nav_quizz_progress;
    private TextView nav_quizz_title;
    private TextView numberPage;
    private LinearLayout numberPageLayout;
    private ProgressBar progressBar;
    private QuizzGamePresenter quizzGamePresenter;
    private ArrayList<Survey> quizzStorage;
    private TextToSpeech textToSpeech;
    private Toolbar toolbar;
    private int totalQuizzGoodAnswer;
    private int totalQuizzWrongAnswer;

    /* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Activities/QuizzGameActivity$PlaceholderFragment.class */
    public static class PlaceholderFragment extends Fragment implements QuizzGameView.IPlaceholder {
        private static final String ARG_SECTION_NUMBER = "section_number";
        private Button[] buttons;
        private int fragNumber;
        private QuizzGameView.IQuizzGame iQuizzGame;
        private ImageButton[] imageButtons;
        private LinearLayout[] linearLayouts;
        private LinearLayout nav_error_layout;
        private Button nav_quizz_btn_continue;
        private Button nav_quizz_btn_detail;
        private Button nav_quizz_btn_save_no;
        private Button nav_quizz_btn_save_yes;
        private RadioGroup nav_quizz_choice;
        private TextView nav_quizz_error_msg;
        private TextView nav_quizz_question;
        private TextView nav_quizz_save_msg;
        private RadioButton nav_quizz_suggest_1;
        private RadioButton nav_quizz_suggest_2;
        private RadioButton nav_quizz_suggest_3;
        private ImageButton nav_quizz_texttospeech;
        private ImageButton nav_quizz_validate;
        private LinearLayout nav_save_layout;
        private QuizzGamePresenter quizzGamePresenter;
        private RadioButton[] radioButtons;
        private TextView[] textViews;

        public static PlaceholderFragment newInstance(int i) {
            PlaceholderFragment placeholderFragment = new PlaceholderFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(ARG_SECTION_NUMBER, i);
            placeholderFragment.setArguments(bundle);
            return placeholderFragment;
        }

        @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IPlaceholder
        public void enableQuizzGameContent(boolean z) {
            this.nav_quizz_suggest_1.setEnabled(z);
            this.nav_quizz_suggest_2.setEnabled(z);
            this.nav_quizz_suggest_3.setEnabled(z);
            this.nav_quizz_validate.setEnabled(z);
        }

        @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IPlaceholder
        public void events() {
            this.nav_quizz_btn_detail.setOnClickListener(new View.OnClickListener(this) { // from class: org.questionsreponses.View.Activities.QuizzGameActivity.PlaceholderFragment.1
                final PlaceholderFragment this$0;

                {
                    this.this$0 = this;
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    this.this$0.quizzGamePresenter.showExplicationQuizz(this.this$0.getActivity(), this.this$0.fragNumber);
                }
            });
            this.nav_quizz_btn_save_no.setOnClickListener(new View.OnClickListener(this) { // from class: org.questionsreponses.View.Activities.QuizzGameActivity.PlaceholderFragment.2
                final PlaceholderFragment this$0;

                {
                    this.this$0 = this;
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    this.this$0.quizzGamePresenter.showExplicationQuizz(this.this$0.getActivity(), this.this$0.fragNumber);
                }
            });
            this.nav_quizz_texttospeech.setOnClickListener(new View.OnClickListener(this) { // from class: org.questionsreponses.View.Activities.QuizzGameActivity.PlaceholderFragment.3
                final PlaceholderFragment this$0;

                {
                    this.this$0 = this;
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    this.this$0.quizzGamePresenter.retrieveUserAction(view);
                }
            });
            this.nav_quizz_validate.setOnClickListener(new View.OnClickListener(this) { // from class: org.questionsreponses.View.Activities.QuizzGameActivity.PlaceholderFragment.4
                final PlaceholderFragment this$0;

                {
                    this.this$0 = this;
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) throws Resources.NotFoundException {
                    this.this$0.quizzGamePresenter.retrieveWidgetsViews(view, this.this$0.nav_quizz_choice, this.this$0.radioButtons, this.this$0.imageButtons, this.this$0.linearLayouts, this.this$0.textViews, this.this$0.buttons);
                }
            });
        }

        @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IPlaceholder
        public void findWidgets(View view) {
            this.nav_error_layout = (LinearLayout) view.findViewById(C0598R.id.nav_error_layout);
            this.nav_quizz_error_msg = (TextView) view.findViewById(C0598R.id.nav_quizz_error_msg);
            this.nav_quizz_btn_detail = (Button) view.findViewById(C0598R.id.nav_quizz_btn_detail);
            Button button = (Button) view.findViewById(C0598R.id.nav_quizz_btn_continue);
            this.nav_quizz_btn_continue = button;
            button.setVisibility(8);
            this.nav_quizz_question = (TextView) view.findViewById(C0598R.id.nav_quizz_question);
            this.nav_quizz_validate = (ImageButton) view.findViewById(C0598R.id.nav_quizz_validate);
            this.nav_quizz_texttospeech = (ImageButton) view.findViewById(C0598R.id.nav_quizz_texttospeech);
            this.nav_quizz_choice = (RadioGroup) view.findViewById(C0598R.id.nav_quizz_choice);
            this.nav_quizz_suggest_1 = (RadioButton) view.findViewById(C0598R.id.nav_quizz_suggest_1);
            this.nav_quizz_suggest_2 = (RadioButton) view.findViewById(C0598R.id.nav_quizz_suggest_2);
            this.nav_quizz_suggest_3 = (RadioButton) view.findViewById(C0598R.id.nav_quizz_suggest_3);
            this.nav_save_layout = (LinearLayout) view.findViewById(C0598R.id.nav_save_layout);
            this.nav_quizz_save_msg = (TextView) view.findViewById(C0598R.id.nav_quizz_save_msg);
            this.nav_quizz_btn_save_no = (Button) view.findViewById(C0598R.id.nav_quizz_btn_save_no);
            Button button2 = (Button) view.findViewById(C0598R.id.nav_quizz_btn_save_yes);
            this.nav_quizz_btn_save_yes = button2;
            button2.setVisibility(8);
            this.radioButtons = new RadioButton[]{this.nav_quizz_suggest_1, this.nav_quizz_suggest_2, this.nav_quizz_suggest_3};
            this.imageButtons = new ImageButton[]{this.nav_quizz_validate, this.nav_quizz_texttospeech};
            this.linearLayouts = new LinearLayout[]{this.nav_error_layout, this.nav_save_layout};
            this.textViews = new TextView[]{this.nav_quizz_save_msg, this.nav_quizz_error_msg};
            this.buttons = new Button[]{this.nav_quizz_btn_save_no};
        }

        @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IPlaceholder
        public QuizzGameView.IQuizzGame getIQuizzGameInstance() {
            return this.iQuizzGame;
        }

        @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IPlaceholder
        public void loadQuizzGameData(Survey survey) {
            CommonPresenter.buildTextViewToHtmlData(this.nav_quizz_question, CommonPresenter.verseBuilder(survey.getQuestion()));
            this.nav_quizz_choice.clearCheck();
            this.nav_quizz_suggest_1.setText(survey.getProposition_1().toUpperCase());
            this.nav_quizz_suggest_2.setText(survey.getProposition_2().toUpperCase());
            this.nav_quizz_suggest_3.setText(survey.getProposition_3().toUpperCase());
            this.quizzGamePresenter.manageRadioButtonStatus(this.radioButtons, survey);
            this.quizzGamePresenter.manageImageButtonStatus(this.imageButtons, survey);
            this.quizzGamePresenter.manageLinearLayoutStatus(this.linearLayouts, survey);
            this.quizzGamePresenter.manageTextViewStatus(getActivity(), this.textViews, survey);
            this.quizzGamePresenter.manageButtonStatus(getActivity(), this.buttons, survey);
        }

        @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IPlaceholder
        public void messageErrorTextValue(String str) {
            this.nav_error_layout.setVisibility(0);
            this.nav_quizz_error_msg.setText(getResources().getString(C0598R.string.lb_nav_quizz_message) + " " + str);
        }

        @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IPlaceholder
        public void messageOkTextValue(String str) {
            this.nav_save_layout.setVisibility(0);
            this.nav_quizz_save_msg.setText(getResources().getString(C0598R.string.lb_nav_quizz_ok_message) + " " + str);
            this.nav_quizz_btn_save_no.setText(C0598R.string.lb_nav_explain);
        }

        @Override // android.support.v4.app.Fragment
        public void onAttach(Context context) {
            super.onAttach(context);
            KeyEvent.Callback activity = getActivity();
            this.iQuizzGame = (QuizzGameView.IQuizzGame) activity;
            ((QuizzGameActivity) activity).setIplaceholderReference(this);
        }

        @Override // android.support.v4.app.Fragment
        public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
            View viewInflate = layoutInflater.inflate(C0598R.layout.fragment_quizz_game, viewGroup, false);
            this.fragNumber = getArguments().getInt(ARG_SECTION_NUMBER) - 1;
            QuizzGamePresenter quizzGamePresenter = new QuizzGamePresenter(this);
            this.quizzGamePresenter = quizzGamePresenter;
            quizzGamePresenter.loadPlaceholderData(viewInflate, this.fragNumber);
            return viewInflate;
        }

        @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IPlaceholder
        public void quizzGameValidate(Context context, Survey survey) {
        }

        @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IPlaceholder
        public void simulateValidateQuizzGame() {
            this.nav_quizz_validate.performClick();
        }
    }

    /* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Activities/QuizzGameActivity$SectionsPagerAdapter.class */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        final QuizzGameActivity this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public SectionsPagerAdapter(QuizzGameActivity quizzGameActivity, FragmentManager fragmentManager) {
            super(fragmentManager);
            this.this$0 = quizzGameActivity;
        }

        @Override // android.support.v4.view.PagerAdapter
        public int getCount() {
            return this.this$0.quizzGamePresenter.getNumberOfQuizzGameFinded();
        }

        @Override // android.support.v4.app.FragmentPagerAdapter
        public Fragment getItem(int i) {
            return PlaceholderFragment.newInstance(i + 1);
        }
    }

    @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IQuizzGame
    public void callOnBackPressedEvent() {
        onBackPressed();
    }

    @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IQuizzGame
    public void changeViewPagerPosition(int i) {
        this.mViewPager.setCurrentItem(i);
    }

    @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IQuizzGame
    public void chronoVisibility(int i) {
        this.chronoLayout.setVisibility(i);
    }

    @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IQuizzGame
    public void closeActivity() {
        finish();
    }

    @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IQuizzGame
    public void events() {
        this.fab_quizz_previous.setOnClickListener(new View.OnClickListener(this) { // from class: org.questionsreponses.View.Activities.QuizzGameActivity.1
            final QuizzGameActivity this$0;

            {
                this.this$0 = this;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                this.this$0.quizzGamePresenter.retrieveUserAction(view, this.this$0.mViewPager.getCurrentItem(), this.this$0.mViewPager.getAdapter().getCount());
            }
        });
        this.fab_quizz_next.setOnClickListener(new View.OnClickListener(this) { // from class: org.questionsreponses.View.Activities.QuizzGameActivity.2
            final QuizzGameActivity this$0;

            {
                this.this$0 = this;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                this.this$0.quizzGamePresenter.retrieveUserAction(view, this.this$0.mViewPager.getCurrentItem(), this.this$0.mViewPager.getAdapter().getCount());
            }
        });
        this.mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(this) { // from class: org.questionsreponses.View.Activities.QuizzGameActivity.3
            final QuizzGameActivity this$0;

            {
                this.this$0 = this;
            }

            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int i) {
            }

            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageScrolled(int i, float f, int i2) {
            }

            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageSelected(int i) {
                this.this$0.quizzGamePresenter.retrieveViewPagerPosition(i, this.this$0.mViewPager.getAdapter().getCount());
            }
        });
        this.nav_quizz_back.setOnClickListener(new View.OnClickListener(this) { // from class: org.questionsreponses.View.Activities.QuizzGameActivity.4
            final QuizzGameActivity this$0;

            {
                this.this$0 = this;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) throws Resources.NotFoundException {
                QuizzGamePresenter quizzGamePresenter = this.this$0.quizzGamePresenter;
                QuizzGameActivity quizzGameActivity = this.this$0;
                quizzGamePresenter.closeActivity(quizzGameActivity, quizzGameActivity.mViewPager.getAdapter().getCount());
            }
        });
    }

    @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IQuizzGame
    public void fabNextQuizzGameVisibility(int i) {
        this.fab_quizz_next.setVisibility(i);
    }

    @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IQuizzGame
    public void fabPreviousQuizzGameVisibility(int i) {
        this.fab_quizz_previous.setVisibility(i);
    }

    @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IQuizzGame
    public void findWidgets() {
        this.textToSpeech = new TextToSpeech(this, this);
        Toolbar toolbar = (Toolbar) findViewById(C0598R.id.toolbar);
        this.toolbar = toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();
        this.mViewPager = (QuizzGameViewPager) findViewById(2131296359);
        this.numberPageLayout = (LinearLayout) findViewById(C0598R.id.numberPageLayout);
        this.numberPage = (TextView) findViewById(C0598R.id.numberPage);
        TextView textView = (TextView) findViewById(C0598R.id.item_title);
        this.itemTitle = textView;
        textView.setVisibility(8);
        this.itemDate = (TextView) findViewById(C0598R.id.item_date);
        this.itemRating = (RatingBar) findViewById(C0598R.id.item_rating);
        this.nav_quizz_back = (ImageButton) findViewById(C0598R.id.nav_quizz_back);
        this.nav_quizz_title = (TextView) findViewById(C0598R.id.nav_quizz_title);
        this.nav_quizz_progress = (TextView) findViewById(C0598R.id.nav_quizz_progress);
        this.nav_quizz_correct = (TextView) findViewById(C0598R.id.nav_quizz_correct);
        this.chrono_title = (TextView) findViewById(C0598R.id.chrono_title);
        this.chrono_progress = (TextView) findViewById(C0598R.id.chrono_progress);
        this.chronoLayout = (LinearLayout) findViewById(C0598R.id.chronoLayout);
        this.progressBar = (ProgressBar) findViewById(C0598R.id.quizz_game_progressbar);
        this.fab_quizz_previous = (FloatingActionButton) findViewById(C0598R.id.fab_quizz_previous);
        this.fab_quizz_next = (FloatingActionButton) findViewById(C0598R.id.fab_quizz_next);
    }

    @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IQuizzGame
    public int getCurrentViewPager() {
        return this.mViewPager.getCurrentItem();
    }

    @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IQuizzGame
    public QuizzGameView.IPlaceholder getIPlaceholderReference() {
        return this.iPlaceholder;
    }

    @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IQuizzGame
    public ArrayList<Survey> getQuizzGameDataFromStorage() {
        return this.quizzStorage;
    }

    @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IQuizzGame
    public ArrayList<Integer> getQuizzGameScore() {
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(Integer.valueOf(this.totalQuizzGoodAnswer));
        arrayList.add(Integer.valueOf(this.totalQuizzWrongAnswer));
        return arrayList;
    }

    @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IQuizzGame
    public void gotoSelectedViewPager(int i) {
        this.mViewPager.setCurrentItem(i);
    }

    @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IQuizzGame
    public void headerQuizzGame(Survey survey) {
        this.itemDate.setText(CommonPresenter.changeFormatDate(survey.getDate()));
        this.itemRating.setNumStars(10);
        this.itemRating.setRating((survey.getTotal_trouve() / survey.getTotal_question()) * 10.0f);
        this.nav_quizz_title.setText("Q/R : " + survey.getTitre_quizz().toUpperCase());
        this.nav_quizz_progress.setText(survey.getTotal_trouve() + "/" + survey.getTotal_question());
        this.nav_quizz_correct.setText(survey.getTotal_trouve() + " TROUVÉ" + CommonPresenter.getPlurialOfString(survey.getTotal_trouve()) + " | " + survey.getTotal_erreur() + " ERREUR" + CommonPresenter.getPlurialOfString(survey.getTotal_erreur()));
    }

    @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IQuizzGame
    public void loadPlaceHolderFragment(int i) {
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        this.mSectionsPagerAdapter = sectionsPagerAdapter;
        this.mViewPager.setAdapter(sectionsPagerAdapter);
        this.mViewPager.setPagingEnabled(false);
        this.mViewPager.setCurrentItem(i);
    }

    @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IQuizzGame
    public void modifyChronoProgressData(String str, String str2) {
        this.chrono_title.setText(str);
        this.chrono_progress.setText(str2);
    }

    @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IQuizzGame
    public void modifyNumberPageValue(String str) {
        this.numberPage.setText(str);
    }

    @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IQuizzGame
    public void numberPageLayoutVisibility(int i) {
        this.numberPageLayout.setVisibility(i);
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onBackPressed() {
        try {
            this.quizzGamePresenter.closeActivity(this, this.mViewPager.getAdapter().getCount());
        } catch (Exception e) {
        }
    }

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.SupportActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C0598R.layout.activity_quizz_game);
        this.intent = getIntent();
        QuizzGamePresenter quizzGamePresenter = new QuizzGamePresenter(this);
        this.quizzGamePresenter = quizzGamePresenter;
        quizzGamePresenter.loadQuizzGameData(this, this.intent);
    }

    @Override // android.speech.tts.TextToSpeech.OnInitListener
    public void onInit(int i) {
        this.quizzGamePresenter.initializeTextToSpeech(i, this.textToSpeech);
    }

    @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IQuizzGame
    public void progressBarVisibility(int i) {
        this.progressBar.setVisibility(i);
    }

    @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IQuizzGame
    public void readTextFromTextToSpeech(String str) {
        try {
            this.textToSpeech.speak(str, 0, null);
            this.textToSpeech.setPitch(1.1f);
            this.textToSpeech.setSpeechRate(1.0f);
        } catch (Exception e) {
        }
    }

    @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IQuizzGame
    public CountDownTimer retrieveCountDownTimerFromStorage() {
        return this.downTimer;
    }

    @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IQuizzGame
    public void setIplaceholderReference(QuizzGameView.IPlaceholder iPlaceholder) {
        this.iPlaceholder = iPlaceholder;
    }

    @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IQuizzGame
    public void setQuizzGameScore(int i, int i2) {
        this.totalQuizzGoodAnswer += i;
        this.totalQuizzWrongAnswer += i2;
    }

    @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IQuizzGame
    public void simulateFabNextQuizzClick() {
        this.fab_quizz_next.performClick();
    }

    @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IQuizzGame
    public void stopReadingFromTextToSpeech() {
        try {
            this.textToSpeech.stop();
        } catch (Exception e) {
        }
    }

    @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IQuizzGame
    public void storageCountDownTimer(CountDownTimer countDownTimer) {
        this.downTimer = countDownTimer;
    }

    @Override // org.questionsreponses.View.Interfaces.QuizzGameView.IQuizzGame
    public void storageQuizzGameData(ArrayList<Survey> arrayList) {
        this.quizzStorage = arrayList;
    }
}
