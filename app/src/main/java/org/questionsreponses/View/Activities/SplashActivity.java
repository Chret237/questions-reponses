package org.questionsreponses.View.Activities;

import android.R;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import org.questionsreponses.C0598R;
import org.questionsreponses.Presenter.CommonPresenter;
import org.questionsreponses.Presenter.SplashPresenter;
import org.questionsreponses.View.Interfaces.SplashView;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Activities/SplashActivity.class */
public class SplashActivity extends AppCompatActivity implements SplashView.ISplash {
    private CountDownTimer downTimer;
    private ImageView logoImage;
    private ImageView splashImage;
    private SplashPresenter splashPresenter;

    /* JADX WARN: Type inference failed for: r1v0, types: [org.questionsreponses.View.Activities.SplashActivity$1] */
    @Override // org.questionsreponses.View.Interfaces.SplashView.ISplash
    public void displayHome() {
        this.downTimer = new CountDownTimer(this, 2000L, 1000L) { // from class: org.questionsreponses.View.Activities.SplashActivity.1
            final SplashActivity this$0;

            {
                this.this$0 = this;
            }

            @Override // android.os.CountDownTimer
            public void onFinish() {
                this.this$0.startActivity(new Intent(this.this$0, (Class<?>) HomeActivity.class));
                this.this$0.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                this.this$0.finish();
            }

            @Override // android.os.CountDownTimer
            public void onTick(long j) {
            }
        }.start();
    }

    @Override // org.questionsreponses.View.Interfaces.SplashView.ISplash
    public void events() throws Resources.NotFoundException {
        this.splashImage.startAnimation(AnimationUtils.loadAnimation(this, C0598R.anim.move_bottom));
        this.logoImage.startAnimation(AnimationUtils.loadAnimation(this, C0598R.anim.move_top));
    }

    @Override // org.questionsreponses.View.Interfaces.SplashView.ISplash
    public void hideHeader() {
        getSupportActionBar().hide();
    }

    @Override // org.questionsreponses.View.Interfaces.SplashView.ISplash
    public void initSharePreferences() {
    }

    @Override // org.questionsreponses.View.Interfaces.SplashView.ISplash
    public void initialize() {
        this.splashImage = (ImageView) findViewById(C0598R.id.splashImage);
        this.logoImage = (ImageView) findViewById(C0598R.id.logoImage);
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onBackPressed() {
        CommonPresenter.cancelCountDownTimer(this.downTimer);
        super.onBackPressed();
    }

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.SupportActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setFlags(1024, 1024);
        setContentView(C0598R.layout.activity_splash);
        SplashPresenter splashPresenter = new SplashPresenter(this);
        this.splashPresenter = splashPresenter;
        splashPresenter.loadSplashData(this);
    }
}
