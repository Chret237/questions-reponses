package org.questionsreponses.View.Activities;

import android.R;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import com.github.clans.fab.BuildConfig;
import java.util.ArrayList;
import org.questionsreponses.C0598R;
import org.questionsreponses.Model.Survey;
import org.questionsreponses.Presenter.CommonPresenter;
import org.questionsreponses.Presenter.SpiritualGrowthPresenter;
import org.questionsreponses.View.Adapters.SpiritualGrowthRecyclerAdapter;
import org.questionsreponses.View.Interfaces.SpiritualGrowthView;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Activities/SpiritualGrowthActivity.class */
public class SpiritualGrowthActivity extends AppCompatActivity implements SpiritualGrowthView.ISpiritualGrowth, TextToSpeech.OnInitListener {
    private GridLayoutManager gridLayout;
    private SpiritualGrowthPresenter growthPresenter;
    private RecyclerView growthRecyclerView;
    private SpiritualGrowthRecyclerAdapter growthdapter;
    private Intent intent;
    private TextToSpeech textToSpeech;

    @Override // org.questionsreponses.View.Interfaces.SpiritualGrowthView.ISpiritualGrowth
    public void closeActivity() {
        finish();
    }

    @Override // org.questionsreponses.View.Interfaces.SpiritualGrowthView.ISpiritualGrowth
    public void events() {
    }

    @Override // org.questionsreponses.View.Interfaces.SpiritualGrowthView.ISpiritualGrowth
    public void initialize() {
        this.textToSpeech = new TextToSpeech(this, this);
        this.growthRecyclerView = (RecyclerView) findViewById(C0598R.id.growthRecyclerView);
    }

    @Override // org.questionsreponses.View.Interfaces.SpiritualGrowthView.ISpiritualGrowth
    public void launchQuizzSpiritualGrowth(String str, String str2, String str3, int i) {
        Intent intent = new Intent(this, (Class<?>) QuizzGameActivity.class);
        intent.putExtra(CommonPresenter.KEY_QUIZZ_GAME_LIST_SELECTED, str);
        intent.putExtra(CommonPresenter.KEY_QUIZZ_GAME_TITLE_SELECTED, str2);
        intent.putExtra(CommonPresenter.KEY_QUIZZ_GROUPKEY_SELECTED, str3);
        intent.putExtra(CommonPresenter.KEY_QUIZZ_MIN_SCORE_TO_UNLOCK, BuildConfig.FLAVOR + i);
        startActivityForResult(intent, 5);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override // org.questionsreponses.View.Interfaces.SpiritualGrowthView.ISpiritualGrowth
    public void loadQuizzStep(ArrayList<Survey> arrayList, int i, int i2) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, i);
        this.gridLayout = gridLayoutManager;
        this.growthRecyclerView.setLayoutManager(gridLayoutManager);
        this.growthRecyclerView.setHasFixedSize(true);
        SpiritualGrowthRecyclerAdapter spiritualGrowthRecyclerAdapter = new SpiritualGrowthRecyclerAdapter(this, arrayList, this);
        this.growthdapter = spiritualGrowthRecyclerAdapter;
        this.growthRecyclerView.setAdapter(spiritualGrowthRecyclerAdapter);
        this.growthRecyclerView.scrollToPosition(i2);
    }

    @Override // org.questionsreponses.View.Interfaces.SpiritualGrowthView.ISpiritualGrowth
    public void modifyHeaderToolBar(String str, String str2) {
        getSupportActionBar().setTitle(str);
        getSupportActionBar().setSubtitle(str2);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x004a  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0052  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0066  */
    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void onActivityResult(int r6, int r7, android.content.Intent r8) throws android.content.res.Resources.NotFoundException, java.lang.NumberFormatException {
        /*
            r5 = this;
            r0 = r6
            r1 = 5
            if (r0 != r1) goto Lc8
            r0 = r7
            r1 = -1
            if (r0 != r1) goto L7e
            r0 = r8
            java.lang.String r1 = "KEY_QUIZZ_GAME_RETURN_DATA"
            java.lang.String r0 = r0.getStringExtra(r1)
            r10 = r0
            r0 = r10
            int r0 = r0.hashCode()
            r9 = r0
            r0 = r9
            r1 = -518691789(0xffffffffe1156433, float:-1.7223656E20)
            if (r0 == r1) goto L3a
            r0 = r9
            r1 = 1053526923(0x3ecb8b8b, float:0.397549)
            if (r0 == r1) goto L2a
            goto L4a
        L2a:
            r0 = r10
            java.lang.String r1 = "KEY_QUIZZ_SPIRITUAL_GROWTH_FAILED"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L4a
            r0 = 1
            r9 = r0
            goto L4d
        L3a:
            r0 = r10
            java.lang.String r1 = "KEY_QUIZZ_SPIRITUAL_GROWTH_SUCCEEDED"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L4a
            r0 = 0
            r9 = r0
            goto L4d
        L4a:
            r0 = -1
            r9 = r0
        L4d:
            r0 = r9
            if (r0 == 0) goto L66
            r0 = r9
            r1 = 1
            if (r0 == r1) goto L5b
            goto Lc8
        L5b:
            java.lang.String r0 = "TAG_QUIZZ_GAME_RETURN"
            java.lang.String r1 = "KEY_QUIZZ_SPIRITUAL_GROWTH_FAILED"
            int r0 = android.util.Log.i(r0, r1)
            goto Lc8
        L66:
            r0 = r5
            org.questionsreponses.Presenter.SpiritualGrowthPresenter r0 = r0.growthPresenter
            r1 = r5
            r2 = r5
            android.content.Intent r2 = r2.intent
            r3 = -1
            r0.loadSpiritualGrowthData(r1, r2, r3)
            java.lang.String r0 = "TAG_QUIZZ_GAME_RETURN"
            java.lang.String r1 = "KEY_QUIZZ_SPIRITUAL_GROWTH_SUCCEEDED"
            int r0 = android.util.Log.i(r0, r1)
            goto Lc8
        L7e:
            r0 = r7
            if (r0 != 0) goto Lc8
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r1 = r0
            r1.<init>()
            r10 = r0
            r0 = r10
            java.lang.String r1 = "Activity.RESULT_CANCELED = "
            java.lang.StringBuilder r0 = r0.append(r1)
            r0 = r10
            r1 = r6
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r0 = "TAG_QUIZZ_GAME_SELECTED"
            r1 = r10
            java.lang.String r1 = r1.toString()
            int r0 = android.util.Log.i(r0, r1)
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r1 = r0
            r1.<init>()
            r10 = r0
            r0 = r10
            java.lang.String r1 = "Activity.RESULT_CANCELED = "
            java.lang.StringBuilder r0 = r0.append(r1)
            r0 = r10
            r1 = r7
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r0 = "TAG_QUIZZ_GAME_SELECTED"
            r1 = r10
            java.lang.String r1 = r1.toString()
            int r0 = android.util.Log.i(r0, r1)
        Lc8:
            r0 = r5
            r1 = r6
            r2 = r7
            r3 = r8
            super.onActivityResult(r1, r2, r3)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.questionsreponses.View.Activities.SpiritualGrowthActivity.onActivityResult(int, int, android.content.Intent):void");
    }

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.SupportActivity, android.app.Activity
    protected void onCreate(Bundle bundle) throws Resources.NotFoundException, NumberFormatException {
        super.onCreate(bundle);
        setContentView(C0598R.layout.activity_spiritual_growth);
        this.intent = getIntent();
        SpiritualGrowthPresenter spiritualGrowthPresenter = new SpiritualGrowthPresenter(this);
        this.growthPresenter = spiritualGrowthPresenter;
        spiritualGrowthPresenter.loadSpiritualGrowthData(this, this.intent, 0);
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0598R.menu.menu_spiritual_growth, menu);
        return true;
    }

    @Override // android.speech.tts.TextToSpeech.OnInitListener
    public void onInit(int i) {
        this.growthPresenter.initializeTextToSpeech(i, this.textToSpeech);
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            this.growthPresenter.closeActivity();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override // org.questionsreponses.View.Interfaces.SpiritualGrowthView.ISpiritualGrowth
    public void readTextFromTextToSpeech(String str) {
        try {
            this.textToSpeech.speak(str, 0, null);
            this.textToSpeech.setPitch(1.3f);
            this.textToSpeech.setSpeechRate(0.7f);
        } catch (Exception e) {
        }
    }
}
