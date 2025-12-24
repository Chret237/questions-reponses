package org.questionsreponses.View.WebView;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/View/WebView/YoutubeWebChromeClient.class */
public class YoutubeWebChromeClient extends WebChromeClient {
    private Activity mActivity;
    private View mCustomView;
    private WebChromeClient.CustomViewCallback mCustomViewCallback;
    protected FrameLayout mFullscreenContainer;
    private int mOriginalOrientation;
    private int mOriginalSystemUiVisibility;
    private WebView mWebView;

    public YoutubeWebChromeClient(Activity activity, WebView webView) {
        this.mActivity = activity;
    }

    @Override // android.webkit.WebChromeClient
    public Bitmap getDefaultVideoPoster() {
        if (this.mCustomView == null) {
            return null;
        }
        return BitmapFactory.decodeResource(this.mActivity.getResources(), 2130837573);
    }

    @Override // android.webkit.WebChromeClient
    public void onHideCustomView() {
        ((FrameLayout) this.mActivity.getWindow().getDecorView()).removeView(this.mCustomView);
        this.mCustomView = null;
        this.mActivity.getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
        this.mActivity.setRequestedOrientation(this.mOriginalOrientation);
        this.mCustomViewCallback = null;
    }

    protected void onRestoreInstanceState(Bundle bundle) {
        this.mWebView.restoreState(bundle);
    }

    protected void onSaveInstanceState(Bundle bundle) {
        this.mWebView.saveState(bundle);
    }

    @Override // android.webkit.WebChromeClient
    public void onShowCustomView(View view, WebChromeClient.CustomViewCallback customViewCallback) {
        if (this.mCustomView != null) {
            onHideCustomView();
            return;
        }
        this.mCustomView = view;
        this.mOriginalSystemUiVisibility = this.mActivity.getWindow().getDecorView().getSystemUiVisibility();
        this.mOriginalOrientation = this.mActivity.getRequestedOrientation();
        this.mCustomViewCallback = customViewCallback;
        ((FrameLayout) this.mActivity.getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
        this.mActivity.getWindow().getDecorView().setSystemUiVisibility(3846);
    }

    public void setClientWebView(WebView webView) {
    }
}
