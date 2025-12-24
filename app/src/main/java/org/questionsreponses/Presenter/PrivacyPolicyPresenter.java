package org.questionsreponses.Presenter;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import org.questionsreponses.C0598R;
import org.questionsreponses.View.Interfaces.PrivacyPolicyView;
import org.questionsreponses.View.WebView.YoutubeWebClient;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/Presenter/PrivacyPolicyPresenter.class */
public class PrivacyPolicyPresenter implements PrivacyPolicyView.IPresenter, PrivacyPolicyView.ILoadWebViewFinished {
    private PrivacyPolicyView.IPrivacyPolicy iPrivacyPolicy;
    private YoutubeWebClient youtubeWebClient;

    public PrivacyPolicyPresenter(PrivacyPolicyView.IPrivacyPolicy iPrivacyPolicy) {
        this.iPrivacyPolicy = iPrivacyPolicy;
        YoutubeWebClient youtubeWebClient = new YoutubeWebClient();
        this.youtubeWebClient = youtubeWebClient;
        youtubeWebClient.setiPrivacyPolicy(iPrivacyPolicy);
    }

    @Override // org.questionsreponses.View.Interfaces.PrivacyPolicyView.IPresenter
    public void loadPrivacyPolicy(Context context) throws Resources.NotFoundException {
        try {
            if (this.iPrivacyPolicy != null && context != null) {
                this.iPrivacyPolicy.initialize();
                this.iPrivacyPolicy.events();
                this.iPrivacyPolicy.progressBarVisibility(0);
                this.iPrivacyPolicy.webViewVisibility(8);
                if (CommonPresenter.isMobileConnected(context)) {
                    this.iPrivacyPolicy.loadWebView(this.youtubeWebClient, context.getResources().getString(C0598R.string.lb_privacy_policy_url));
                } else {
                    CommonPresenter.showMessageSnackBar(CommonPresenter.getViewInTermsOfContext(context), context.getResources().getString(C0598R.string.no_connection));
                    this.iPrivacyPolicy.progressBarVisibility(8);
                    this.iPrivacyPolicy.webViewVisibility(0);
                }
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "PrivacyPolicyPresenter-->loadPrivacyPolicy() : " + e.getMessage());
        }
    }

    @Override // org.questionsreponses.View.Interfaces.PrivacyPolicyView.ILoadWebViewFinished
    public void onLoadWebViewFailure(Context context) {
        if (context != null) {
            try {
                if (this.iPrivacyPolicy != null) {
                    CommonPresenter.showMessageSnackBar(CommonPresenter.getViewInTermsOfContext(context), context.getResources().getString(C0598R.string.no_connection));
                    this.iPrivacyPolicy.progressBarVisibility(8);
                    this.iPrivacyPolicy.webViewVisibility(0);
                }
            } catch (Exception e) {
                Log.e("TAG_ERROR", "PrivacyPolicyPresenter-->onLoadWebViewFailure() : " + e.getMessage());
            }
        }
    }

    @Override // org.questionsreponses.View.Interfaces.PrivacyPolicyView.ILoadWebViewFinished
    public void onLoadWebViewSuccess(Context context) {
        try {
            if (this.iPrivacyPolicy != null) {
                this.iPrivacyPolicy.progressBarVisibility(8);
                this.iPrivacyPolicy.webViewVisibility(0);
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "PrivacyPolicyPresenter-->onLoadWebViewSuccess() : " + e.getMessage());
        }
    }
}
