package org.questionsreponses.View.WebView;

import android.content.Intent;
import android.net.Uri;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import org.questionsreponses.Presenter.DownloaderPresenter;
import org.questionsreponses.Presenter.PrivacyPolicyPresenter;
import org.questionsreponses.View.Interfaces.DownloaderView;
import org.questionsreponses.View.Interfaces.PrivacyPolicyView;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/View/WebView/YoutubeWebClient.class */
public class YoutubeWebClient extends WebViewClient {
    private DownloaderView.IDownloader iDownloader;
    private PrivacyPolicyView.IPrivacyPolicy iPrivacyPolicy;

    @Override // android.webkit.WebViewClient
    public void onPageFinished(WebView webView, String str) {
        DownloaderView.IDownloader iDownloader = this.iDownloader;
        if (iDownloader != null) {
            new DownloaderPresenter(iDownloader).onLoadWebViewSuccess(webView.getContext());
            return;
        }
        PrivacyPolicyView.IPrivacyPolicy iPrivacyPolicy = this.iPrivacyPolicy;
        if (iPrivacyPolicy != null) {
            new PrivacyPolicyPresenter(iPrivacyPolicy).onLoadWebViewSuccess(webView.getContext());
        }
    }

    @Override // android.webkit.WebViewClient
    public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
        super.onReceivedError(webView, webResourceRequest, webResourceError);
        DownloaderView.IDownloader iDownloader = this.iDownloader;
        if (iDownloader != null) {
            new DownloaderPresenter(iDownloader).onLoadWebViewFailure(webView.getContext());
            return;
        }
        PrivacyPolicyView.IPrivacyPolicy iPrivacyPolicy = this.iPrivacyPolicy;
        if (iPrivacyPolicy != null) {
            new PrivacyPolicyPresenter(iPrivacyPolicy).onLoadWebViewFailure(webView.getContext());
        }
    }

    public void setiDownloader(DownloaderView.IDownloader iDownloader) {
        this.iDownloader = iDownloader;
    }

    public void setiPrivacyPolicy(PrivacyPolicyView.IPrivacyPolicy iPrivacyPolicy) {
        this.iPrivacyPolicy = iPrivacyPolicy;
    }

    @Override // android.webkit.WebViewClient
    public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest webResourceRequest) {
        String string = webResourceRequest.getUrl().toString();
        if (Uri.parse(string).getHost().endsWith("youtube.com") || Uri.parse(string).getHost().endsWith("youtu.be")) {
            return false;
        }
        webView.getContext().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(string)));
        return true;
    }

    @Override // android.webkit.WebViewClient
    public boolean shouldOverrideUrlLoading(WebView webView, String str) {
        if (Uri.parse(str).getHost().endsWith("youtube.com") || Uri.parse(str).getHost().endsWith("youtu.be")) {
            return false;
        }
        webView.getContext().startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
        return true;
    }
}
