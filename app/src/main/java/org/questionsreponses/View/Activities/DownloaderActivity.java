package org.questionsreponses.View.Activities;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.github.clans.fab.FloatingActionButton;
import java.util.ArrayList;
import org.questionsreponses.C0598R;
import org.questionsreponses.Model.Youtube;
import org.questionsreponses.Presenter.CommonPresenter;
import org.questionsreponses.Presenter.DownloaderPresenter;
import org.questionsreponses.View.Interfaces.DownloaderView;
import org.questionsreponses.View.WebView.YoutubeWebChromeClient;
import org.questionsreponses.View.WebView.YoutubeWebClient;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Activities/DownloaderActivity.class */
public class DownloaderActivity extends AppCompatActivity implements DownloaderView.IDownloader {
    private static final int PERMISSION_TO_SAVE_FILE = 123;
    private boolean clickSimulated;
    private DownloaderPresenter downloaderPresenter;
    private FloatingActionButton fabBack;
    private FloatingActionButton fabCancel;
    private FloatingActionButton fabDownload;
    private View fabDownloadLayout;
    private FloatingActionButton fabInfo;
    private FloatingActionButton fabToward;
    private ProgressBar progressBar;
    private WebView webView;
    private YoutubeWebChromeClient youtubeWebChromeClient;

    @Override // org.questionsreponses.View.Interfaces.DownloaderView.IDownloader
    public void askPermissionToSaveFile() {
        if (ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 123);
        }
    }

    @Override // org.questionsreponses.View.Interfaces.DownloaderView.IDownloader
    public void closeActivity() {
        finish();
    }

    @Override // org.questionsreponses.View.Interfaces.DownloaderView.IDownloader
    public void displayFormatVideoList(ArrayList<Youtube> arrayList) {
        CommonPresenter.showFormatVideo(this, arrayList);
    }

    @Override // org.questionsreponses.View.Interfaces.DownloaderView.IDownloader
    public void events() {
        this.fabBack.setOnClickListener(new View.OnClickListener(this) { // from class: org.questionsreponses.View.Activities._$$Lambda$DownloaderActivity$9FEB8fEzOvxyvPATMPi5LSeCKE0
            public final DownloaderActivity f$0;

            {
                this.f$0 = this;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) throws Resources.NotFoundException {
                this.f$0.lambda$events$0$DownloaderActivity(view);
            }
        });
        this.fabToward.setOnClickListener(new View.OnClickListener(this) { // from class: org.questionsreponses.View.Activities._$$Lambda$DownloaderActivity$8jF8KwSvtXIV22F8GwlFo7ud1a0
            public final DownloaderActivity f$0;

            {
                this.f$0 = this;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) throws Resources.NotFoundException {
                this.f$0.lambda$events$1$DownloaderActivity(view);
            }
        });
        this.fabCancel.setOnClickListener(new View.OnClickListener(this) { // from class: org.questionsreponses.View.Activities._$$Lambda$DownloaderActivity$7VtHa_LrusOzIeHj0erdNVtwCG4
            public final DownloaderActivity f$0;

            {
                this.f$0 = this;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) throws Resources.NotFoundException {
                this.f$0.lambda$events$2$DownloaderActivity(view);
            }
        });
        this.fabInfo.setOnClickListener(new View.OnClickListener(this) { // from class: org.questionsreponses.View.Activities._$$Lambda$DownloaderActivity$WuA8WswecSQnyOhOafSn5Uclqhs
            public final DownloaderActivity f$0;

            {
                this.f$0 = this;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) throws Resources.NotFoundException {
                this.f$0.lambda$events$3$DownloaderActivity(view);
            }
        });
        this.fabDownload.setOnClickListener(new View.OnClickListener(this) { // from class: org.questionsreponses.View.Activities._$$Lambda$DownloaderActivity$Sc_242HHdpDtWZFzHqXUTAvE0V8
            public final DownloaderActivity f$0;

            {
                this.f$0 = this;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) throws Resources.NotFoundException {
                this.f$0.lambda$events$4$DownloaderActivity(view);
            }
        });
    }

    @Override // org.questionsreponses.View.Interfaces.DownloaderView.IDownloader
    public void fabButtonVisibility(int i) {
        this.fabDownloadLayout.setVisibility(i);
    }

    @Override // org.questionsreponses.View.Interfaces.DownloaderView.IDownloader
    public void initialize() {
        getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.fabDownloadLayout = findViewById(C0598R.id.fab_download_layout);
        this.webView = (WebView) findViewById(C0598R.id.downloader_webview);
        this.progressBar = (ProgressBar) findViewById(C0598R.id.progress_downloader);
        this.fabBack = (FloatingActionButton) findViewById(C0598R.id.fab_back);
        this.fabCancel = (FloatingActionButton) findViewById(C0598R.id.fab_cancel);
        this.fabDownload = (FloatingActionButton) findViewById(C0598R.id.fab_download_link);
        this.fabToward = (FloatingActionButton) findViewById(C0598R.id.fab_toward);
        this.fabInfo = (FloatingActionButton) findViewById(C0598R.id.fab_info);
    }

    public /* synthetic */ void lambda$events$0$DownloaderActivity(View view) throws Resources.NotFoundException {
        this.downloaderPresenter.retrieveUserAction(view, null);
    }

    public /* synthetic */ void lambda$events$1$DownloaderActivity(View view) throws Resources.NotFoundException {
        this.downloaderPresenter.retrieveUserAction(view, null);
    }

    public /* synthetic */ void lambda$events$2$DownloaderActivity(View view) throws Resources.NotFoundException {
        this.downloaderPresenter.retrieveUserAction(view, null);
    }

    public /* synthetic */ void lambda$events$3$DownloaderActivity(View view) throws Resources.NotFoundException {
        this.downloaderPresenter.retrieveUserAction(view, null);
    }

    public /* synthetic */ void lambda$events$4$DownloaderActivity(View view) throws Resources.NotFoundException {
        this.downloaderPresenter.retrieveUserAction(view, this.webView.getUrl());
    }

    @Override // org.questionsreponses.View.Interfaces.DownloaderView.IDownloader
    public void loadWebView(YoutubeWebClient youtubeWebClient, String str) {
        Log.i("TAG_URL", "LOADING URL : " + str);
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
        this.fabDownload.performClick();
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override // android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.SupportActivity, android.app.Activity
    protected void onCreate(Bundle bundle) throws Resources.NotFoundException {
        super.onCreate(bundle);
        setContentView(C0598R.layout.activity_downloader);
        DownloaderPresenter downloaderPresenter = new DownloaderPresenter(this);
        this.downloaderPresenter = downloaderPresenter;
        downloaderPresenter.loadDownloaderData(this, getIntent());
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity, android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        if (i != 123) {
            return;
        }
        if (iArr.length > 0) {
            if (iArr[0] == 0) {
                for (int i2 = 0; i2 < CommonPresenter.getFolderName().length; i2++) {
                    CommonPresenter.createFolder(this, CommonPresenter.getFolderName()[i2]);
                }
                return;
            }
        }
        Toast.makeText(this, getResources().getString(C0598R.string.lb_storage_file_require), 1).show();
    }

    @Override // org.questionsreponses.View.Interfaces.DownloaderView.IDownloader
    public void progressBarVisibility(int i) {
        this.progressBar.setVisibility(i);
    }

    @Override // org.questionsreponses.View.Interfaces.DownloaderView.IDownloader
    public void webViewGoBack() {
        this.webView.goBack();
    }

    @Override // org.questionsreponses.View.Interfaces.DownloaderView.IDownloader
    public void webViewGoForward() {
        this.webView.goForward();
    }

    @Override // org.questionsreponses.View.Interfaces.DownloaderView.IDownloader
    public void webViewVisibility(int i) {
        this.webView.setVisibility(i);
    }
}
