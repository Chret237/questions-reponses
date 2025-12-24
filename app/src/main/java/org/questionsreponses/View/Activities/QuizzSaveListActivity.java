package org.questionsreponses.View.Activities;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import com.github.clans.fab.FloatingActionButton;
import org.questionsreponses.C0598R;
import org.questionsreponses.Model.Survey;
import org.questionsreponses.Presenter.CommonPresenter;
import org.questionsreponses.Presenter.QuizzSaveListPresenter;
import org.questionsreponses.View.Interfaces.QuizzSaveListView;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Activities/QuizzSaveListActivity.class */
public class QuizzSaveListActivity extends AppCompatActivity implements QuizzSaveListView.IQuizzSaveList {
    private FloatingActionButton fab_quizz_list_next;
    private FloatingActionButton fab_quizz_list_previous;
    private QuizzSaveListView.IPlaceholder iPlaceholder;
    private TextView itemDate;
    private RatingBar itemRating;
    private TextView itemTitle;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private ImageButton nav_quizz_back;
    private TextView nav_quizz_correct;
    private TextView nav_quizz_progress;
    private TextView nav_quizz_title;
    private TextView numberPage;
    private QuizzSaveListPresenter saveListPresenter;
    private Toolbar toolbar;

    /* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Activities/QuizzSaveListActivity$PlaceholderFragment.class */
    public static class PlaceholderFragment extends Fragment implements QuizzSaveListView.IPlaceholder {
        private static final String ARG_SECTION_NUMBER = "section_number";
        private int fragNumber;
        private QuizzSaveListView.IQuizzSaveList iQuizzSaveList;
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
        private QuizzSaveListPresenter saveListPresenter;

        public static PlaceholderFragment newInstance(int i) {
            PlaceholderFragment placeholderFragment = new PlaceholderFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(ARG_SECTION_NUMBER, i);
            placeholderFragment.setArguments(bundle);
            return placeholderFragment;
        }

        @Override // org.questionsreponses.View.Interfaces.QuizzSaveListView.IPlaceholder
        public void enableQuizzGameContent(boolean z) {
            this.nav_quizz_suggest_1.setEnabled(z);
            this.nav_quizz_suggest_2.setEnabled(z);
            this.nav_quizz_suggest_3.setEnabled(z);
            this.nav_quizz_validate.setEnabled(z);
        }

        @Override // org.questionsreponses.View.Interfaces.QuizzSaveListView.IPlaceholder
        public void events() {
            this.nav_quizz_btn_detail.setOnClickListener(new View.OnClickListener(this) { // from class: org.questionsreponses.View.Activities.QuizzSaveListActivity.PlaceholderFragment.1
                final PlaceholderFragment this$0;

                {
                    this.this$0 = this;
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    this.this$0.saveListPresenter.showExplicationQuizz(this.this$0.getActivity(), QuizzSaveListPresenter.getSurveysList().get(this.this$0.fragNumber));
                }
            });
            this.nav_quizz_btn_save_no.setOnClickListener(new View.OnClickListener(this) { // from class: org.questionsreponses.View.Activities.QuizzSaveListActivity.PlaceholderFragment.2
                final PlaceholderFragment this$0;

                {
                    this.this$0 = this;
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    this.this$0.saveListPresenter.showExplicationQuizz(this.this$0.getActivity(), QuizzSaveListPresenter.getSurveysList().get(this.this$0.fragNumber));
                }
            });
        }

        @Override // org.questionsreponses.View.Interfaces.QuizzSaveListView.IPlaceholder
        public void initialize(View view) {
            this.nav_error_layout = (LinearLayout) view.findViewById(C0598R.id.nav_error_layout);
            this.nav_quizz_error_msg = (TextView) view.findViewById(C0598R.id.nav_quizz_error_msg);
            this.nav_quizz_btn_detail = (Button) view.findViewById(C0598R.id.nav_quizz_btn_detail);
            Button button = (Button) view.findViewById(C0598R.id.nav_quizz_btn_continue);
            this.nav_quizz_btn_continue = button;
            button.setVisibility(8);
            this.nav_quizz_question = (TextView) view.findViewById(C0598R.id.nav_quizz_question);
            ImageButton imageButton = (ImageButton) view.findViewById(C0598R.id.nav_quizz_validate);
            this.nav_quizz_validate = imageButton;
            imageButton.setVisibility(8);
            ImageButton imageButton2 = (ImageButton) view.findViewById(C0598R.id.nav_quizz_texttospeech);
            this.nav_quizz_texttospeech = imageButton2;
            imageButton2.setVisibility(8);
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
        }

        @Override // org.questionsreponses.View.Interfaces.QuizzSaveListView.IPlaceholder
        public void loadInfosQuizzGame(Context context, Survey survey) {
            QuizzSaveListPresenter quizzSaveListPresenter = new QuizzSaveListPresenter(this.iQuizzSaveList);
            quizzSaveListPresenter.loadHeaderQuizzGame(survey);
            CommonPresenter.buildTextViewToHtmlData(this.nav_quizz_question, CommonPresenter.verseBuilder(survey.getQuestion()));
            this.nav_quizz_choice.clearCheck();
            this.nav_quizz_suggest_1.setText(survey.getProposition_1().toUpperCase());
            this.nav_quizz_suggest_2.setText(survey.getProposition_2().toUpperCase());
            this.nav_quizz_suggest_3.setText(survey.getProposition_3().toUpperCase());
            quizzSaveListPresenter.showUserAnswerOnRadioButton(new RadioButton[]{this.nav_quizz_suggest_1, this.nav_quizz_suggest_2, this.nav_quizz_suggest_3}, survey);
        }

        @Override // org.questionsreponses.View.Interfaces.QuizzSaveListView.IPlaceholder
        public void messageErrorTextValue(String str) {
            this.nav_error_layout.setVisibility(0);
            this.nav_quizz_error_msg.setText(getResources().getString(C0598R.string.lb_nav_quizz_message) + " " + str);
        }

        @Override // org.questionsreponses.View.Interfaces.QuizzSaveListView.IPlaceholder
        public void messageOkTextValue(String str) {
            this.nav_save_layout.setVisibility(0);
            this.nav_quizz_save_msg.setText(getResources().getString(C0598R.string.lb_nav_quizz_ok_message) + " " + str);
            this.nav_quizz_btn_save_no.setText(C0598R.string.lb_nav_explain);
        }

        @Override // android.support.v4.app.Fragment
        public void onAttach(Context context) {
            super.onAttach(context);
            KeyEvent.Callback activity = getActivity();
            this.iQuizzSaveList = (QuizzSaveListView.IQuizzSaveList) activity;
            ((QuizzSaveListActivity) activity).initializeIplaceholderReference(this);
        }

        @Override // android.support.v4.app.Fragment
        public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
            View viewInflate = layoutInflater.inflate(C0598R.layout.fragment_quizz_save_list, viewGroup, false);
            this.saveListPresenter = new QuizzSaveListPresenter(this);
            this.fragNumber = getArguments().getInt(ARG_SECTION_NUMBER) - 1;
            this.saveListPresenter.loadPlaceHolderData(viewInflate, QuizzSaveListPresenter.getSurveysList().get(this.fragNumber));
            return viewInflate;
        }
    }

    /* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Activities/QuizzSaveListActivity$SectionsPagerAdapter.class */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        final QuizzSaveListActivity this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public SectionsPagerAdapter(QuizzSaveListActivity quizzSaveListActivity, FragmentManager fragmentManager) {
            super(fragmentManager);
            this.this$0 = quizzSaveListActivity;
        }

        @Override // android.support.v4.view.PagerAdapter
        public int getCount() {
            return QuizzSaveListPresenter.getSurveysList().size();
        }

        @Override // android.support.v4.app.FragmentPagerAdapter
        public Fragment getItem(int i) {
            return PlaceholderFragment.newInstance(i + 1);
        }

        @Override // android.support.v4.view.PagerAdapter
        public CharSequence getPageTitle(int i) {
            if (i == 0) {
                return "SECTION 1";
            }
            if (i == 1) {
                return "SECTION 2";
            }
            if (i != 2) {
                return null;
            }
            return "SECTION 3";
        }
    }

    @Override // org.questionsreponses.View.Interfaces.QuizzSaveListView.IQuizzSaveList
    public void changeViewPagerPosition(int i) throws Resources.NotFoundException {
        this.mViewPager.setCurrentItem(i);
    }

    @Override // org.questionsreponses.View.Interfaces.QuizzSaveListView.IQuizzSaveList
    public void closeActivity() {
        finish();
    }

    @Override // org.questionsreponses.View.Interfaces.QuizzSaveListView.IQuizzSaveList
    public void events() {
        this.mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(this) { // from class: org.questionsreponses.View.Activities.QuizzSaveListActivity.1
            final QuizzSaveListActivity this$0;

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
                this.this$0.saveListPresenter.changeNumberOfThePage((i + 1) + "/" + QuizzSaveListPresenter.getSurveysList().size());
            }
        });
        this.nav_quizz_back.setOnClickListener(new View.OnClickListener(this) { // from class: org.questionsreponses.View.Activities.QuizzSaveListActivity.2
            final QuizzSaveListActivity this$0;

            {
                this.this$0 = this;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                this.this$0.saveListPresenter.retrieveUserAction(view);
            }
        });
        this.fab_quizz_list_next.setOnClickListener(new View.OnClickListener(this) { // from class: org.questionsreponses.View.Activities.QuizzSaveListActivity.3
            final QuizzSaveListActivity this$0;

            {
                this.this$0 = this;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                this.this$0.saveListPresenter.retrieveUserAction(view, this.this$0.mViewPager.getCurrentItem(), this.this$0.mViewPager.getAdapter().getCount());
            }
        });
        this.fab_quizz_list_previous.setOnClickListener(new View.OnClickListener(this) { // from class: org.questionsreponses.View.Activities.QuizzSaveListActivity.4
            final QuizzSaveListActivity this$0;

            {
                this.this$0 = this;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                this.this$0.saveListPresenter.retrieveUserAction(view, this.this$0.mViewPager.getCurrentItem(), this.this$0.mViewPager.getAdapter().getCount());
            }
        });
    }

    @Override // org.questionsreponses.View.Interfaces.QuizzSaveListView.IQuizzSaveList
    public void initialize() throws Resources.NotFoundException {
        Toolbar toolbar = (Toolbar) findViewById(C0598R.id.toolbar);
        this.toolbar = toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();
        this.mSectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        this.fab_quizz_list_next = (FloatingActionButton) findViewById(C0598R.id.fab_quizz_list_next);
        this.fab_quizz_list_previous = (FloatingActionButton) findViewById(C0598R.id.fab_quizz_list_previous);
        ViewPager viewPager = (ViewPager) findViewById(2131296359);
        this.mViewPager = viewPager;
        viewPager.setAdapter(this.mSectionsPagerAdapter);
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
    }

    @Override // org.questionsreponses.View.Interfaces.QuizzSaveListView.IQuizzSaveList
    public void initializeIplaceholderReference(QuizzSaveListView.IPlaceholder iPlaceholder) {
        this.iPlaceholder = iPlaceholder;
    }

    @Override // org.questionsreponses.View.Interfaces.QuizzSaveListView.IQuizzSaveList
    public void modifyHeaderQuizzGame(Survey survey) {
        this.itemDate.setText(CommonPresenter.changeFormatDate(survey.getDate()));
        this.itemRating.setNumStars(10);
        this.itemRating.setRating((survey.getTotal_trouve() / survey.getTotal_question()) * 10.0f);
        this.nav_quizz_title.setText("Q/R : " + survey.getTitre_quizz().toUpperCase());
        this.nav_quizz_progress.setText(survey.getTotal_trouve() + "/" + survey.getTotal_question());
        this.nav_quizz_correct.setText(survey.getTotal_trouve() + " TROUVÉ" + CommonPresenter.getPlurialOfString(survey.getTotal_trouve()) + " | " + survey.getTotal_erreur() + " ERREUR" + CommonPresenter.getPlurialOfString(survey.getTotal_erreur()));
    }

    @Override // org.questionsreponses.View.Interfaces.QuizzSaveListView.IQuizzSaveList
    public void modifyNumberPage(String str) {
        this.numberPage.setText(str);
    }

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.SupportActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C0598R.layout.activity_quizz_save_list);
        QuizzSaveListPresenter.retrieveDataFromParentActivity(this, getIntent());
        QuizzSaveListPresenter quizzSaveListPresenter = new QuizzSaveListPresenter(this);
        this.saveListPresenter = quizzSaveListPresenter;
        quizzSaveListPresenter.loadQuizzSaveListData(QuizzSaveListPresenter.getSurveysList().size());
    }
}
