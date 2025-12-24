package org.questionsreponses.View.Interfaces;

import android.content.Context;
import org.questionsreponses.View.WebView.YoutubeWebClient;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Interfaces/PrivacyPolicyView.class */
public class PrivacyPolicyView {

    /* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Interfaces/PrivacyPolicyView$ILoadWebViewFinished.class */
    public interface ILoadWebViewFinished {
        void onLoadWebViewFailure(Context context);

        void onLoadWebViewSuccess(Context context);
    }

    /* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Interfaces/PrivacyPolicyView$IPresenter.class */
    public interface IPresenter {
        void loadPrivacyPolicy(Context context);
    }

    /* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Interfaces/PrivacyPolicyView$IPrivacyPolicy.class */
    public interface IPrivacyPolicy {
        void events();

        void initialize();

        void loadWebView(YoutubeWebClient youtubeWebClient, String str);

        void progressBarVisibility(int i);

        void webViewVisibility(int i);
    }
}
