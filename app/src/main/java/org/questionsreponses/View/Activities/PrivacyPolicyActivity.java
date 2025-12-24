package org.questionsreponses.View.Activities;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import com.github.clans.fab.FloatingActionButton;
import org.questionsreponses.C0598R;
import org.questionsreponses.Presenter.PrivacyPolicyPresenter;
import org.questionsreponses.View.Interfaces.PrivacyPolicyView;
import org.questionsreponses.View.WebView.YoutubeWebChromeClient;
import org.questionsreponses.View.WebView.YoutubeWebClient;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Activities/PrivacyPolicyActivity.class */
public class PrivacyPolicyActivity extends AppCompatActivity implements PrivacyPolicyView.IPrivacyPolicy {
    private FloatingActionButton fab_privacy_policy_cancel;
    private PrivacyPolicyPresenter privacyPolicyPresenter;
    private ProgressBar progressBar;
    private WebView webView;
    private YoutubeWebChromeClient youtubeWebChromeClient;

    @Override // org.questionsreponses.View.Interfaces.PrivacyPolicyView.IPrivacyPolicy
    public void events() {
        this.fab_privacy_policy_cancel.setOnClickListener(new View.OnClickListener(this) { // from class: org.questionsreponses.View.Activities._$$Lambda$PrivacyPolicyActivity$K5LuS46fTOEgcHvyypYSbcP67GI
            public final PrivacyPolicyActivity f$0;

            {
                this.f$0 = this;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.lambda$events$0$PrivacyPolicyActivity(view);
            }
        });
    }

    @Override // org.questionsreponses.View.Interfaces.PrivacyPolicyView.IPrivacyPolicy
    public void initialize() {
        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.webView = (WebView) findViewById(C0598R.id.webview_privacy_policy);
        this.progressBar = (ProgressBar) findViewById(C0598R.id.progress_privacy_policy);
        this.fab_privacy_policy_cancel = (FloatingActionButton) findViewById(C0598R.id.fab_privacy_policy_cancel);
    }

    public /* synthetic */ void lambda$events$0$PrivacyPolicyActivity(View view) {
        finish();
    }

    @Override // org.questionsreponses.View.Interfaces.PrivacyPolicyView.IPrivacyPolicy
    public void loadWebView(YoutubeWebClient youtubeWebClient, String str) {
        this.webView.getSettings().setLoadsImagesAutomatically(true);
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.getSettings().setDomStorageEnabled(true);
        this.webView.getSettings().setUseWideViewPort(true);
        if (Build.VERSION.SDK_INT >= 21) {
            this.webView.getSettings().setMixedContentMode(0);
        }
        this.webView.getSettings().setLoadWithOverviewMode(true);
        this.webView.getSettings().setSupportZoom(true);
        this.webView.getSettings().setBuiltInZoomControls(true);
        this.webView.getSettings().setDisplayZoomControls(false);
        this.webView.setBackgroundColor(0);
        this.webView.loadUrl(str);
        this.webView.setWebViewClient(youtubeWebClient);
        YoutubeWebChromeClient youtubeWebChromeClient = new YoutubeWebChromeClient(this, this.webView);
        this.youtubeWebChromeClient = youtubeWebChromeClient;
        this.webView.setWebChromeClient(youtubeWebChromeClient);
    }

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.SupportActivity, android.app.Activity
    protected void onCreate(Bundle bundle) throws Resources.NotFoundException {
        super.onCreate(bundle);
        setContentView(C0598R.layout.activity_privacy_policy);
        PrivacyPolicyPresenter privacyPolicyPresenter = new PrivacyPolicyPresenter(this);
        this.privacyPolicyPresenter = privacyPolicyPresenter;
        privacyPolicyPresenter.loadPrivacyPolicy(this);
    }

    @Override // org.questionsreponses.View.Interfaces.PrivacyPolicyView.IPrivacyPolicy
    public void progressBarVisibility(int i) {
        this.progressBar.setVisibility(i);
    }

    @Override // org.questionsreponses.View.Interfaces.PrivacyPolicyView.IPrivacyPolicy
    public void webViewVisibility(int i) {
        this.webView.setVisibility(i);
    }
}
