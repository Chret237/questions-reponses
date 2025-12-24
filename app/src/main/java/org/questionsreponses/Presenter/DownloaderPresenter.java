package org.questionsreponses.Presenter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import com.github.clans.fab.BuildConfig;
import java.util.ArrayList;
import org.questionsreponses.C0598R;
import org.questionsreponses.Model.Youtube;
import org.questionsreponses.View.Interfaces.DownloaderView;
import org.questionsreponses.View.WebView.YoutubeWebClient;
import org.questionsreponses.youtube.YTDownloaderAsync;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/Presenter/DownloaderPresenter.class */
public class DownloaderPresenter implements DownloaderView.IPresenter, DownloaderView.ILoadWebViewFinished, DownloaderView.ILoadFormatVideoFinished {
    private DownloaderView.IDownloader iDownloader;
    private YoutubeWebClient youtubeWebClient;
    private YTDownloaderAsync ytDownloaderAsync;

    public DownloaderPresenter(DownloaderView.IDownloader iDownloader) {
        this.iDownloader = iDownloader;
        YoutubeWebClient youtubeWebClient = new YoutubeWebClient();
        this.youtubeWebClient = youtubeWebClient;
        youtubeWebClient.setiDownloader(iDownloader);
    }

    private void showFormatVideoToDownload(Context context, String str) {
        if (context == null || str == null) {
            return;
        }
        try {
            if (str.trim().isEmpty() || this.iDownloader == null) {
                return;
            }
            this.iDownloader.progressBarVisibility(0);
            YTDownloaderAsync yTDownloaderAsync = new YTDownloaderAsync();
            this.ytDownloaderAsync = yTDownloaderAsync;
            yTDownloaderAsync.initialize(context, str, "mp4", this);
            this.ytDownloaderAsync.execute(new Void[0]);
        } catch (Exception e) {
            Log.e("TAG_ERROR", "DownloaderPresenter-->showFormatVideoToDownload() : " + e.getMessage());
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:33:0x00ca  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void loadDownloaderData(android.content.Context r5, android.content.Intent r6) throws android.content.res.Resources.NotFoundException {
        /*
            Method dump skipped, instructions count: 305
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.questionsreponses.Presenter.DownloaderPresenter.loadDownloaderData(android.content.Context, android.content.Intent):void");
    }

    @Override // org.questionsreponses.View.Interfaces.DownloaderView.IPresenter
    public void onLoadFormatFailure(Context context) {
        try {
            CommonPresenter.showMessageSnackBar(CommonPresenter.getViewInTermsOfContext(context), context.getResources().getString(C0598R.string.youtube_server_error));
            if (this.iDownloader != null) {
                this.iDownloader.progressBarVisibility(8);
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "DownloaderPresenter-->onLoadFormatFailure() : " + e.getMessage());
        }
    }

    @Override // org.questionsreponses.View.Interfaces.DownloaderView.IPresenter
    public void onLoadFormatSuccess(ArrayList<Youtube> arrayList) {
        try {
            if (this.iDownloader != null) {
                this.iDownloader.displayFormatVideoList(arrayList);
                this.iDownloader.progressBarVisibility(8);
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "DownloaderPresenter-->onLoadFormatSuccess() : " + e.getMessage());
        }
    }

    @Override // org.questionsreponses.View.Interfaces.DownloaderView.ILoadFormatVideoFinished
    public void onLoadFormatVideoFailure(Context context) {
        try {
            CommonPresenter.showMessageSnackBar(CommonPresenter.getViewInTermsOfContext(context), context.getResources().getString(C0598R.string.youtube_server_error));
            if (this.iDownloader != null) {
                this.iDownloader.progressBarVisibility(8);
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "DownloaderPresenter-->onLoadFormatVideoFailure() : " + e.getMessage());
        }
    }

    @Override // org.questionsreponses.View.Interfaces.DownloaderView.ILoadFormatVideoFinished
    public void onLoadFormatVideoSuccess(ArrayList<Youtube> arrayList) {
        try {
            if (this.iDownloader != null) {
                this.iDownloader.displayFormatVideoList(arrayList);
                this.iDownloader.progressBarVisibility(8);
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "DownloaderPresenter-->onLoadFormatVideoSuccess() : " + e.getMessage());
        }
    }

    @Override // org.questionsreponses.View.Interfaces.DownloaderView.ILoadWebViewFinished
    public void onLoadWebViewFailure(Context context) {
        if (context != null) {
            try {
                if (this.iDownloader != null) {
                    CommonPresenter.showMessageSnackBar(CommonPresenter.getViewInTermsOfContext(context), context.getResources().getString(C0598R.string.no_connection));
                    this.iDownloader.progressBarVisibility(8);
                    this.iDownloader.webViewVisibility(0);
                }
            } catch (Exception e) {
                Log.e("TAG_ERROR", "PrivacyPolicyPresenter-->onLoadWebViewFailure() : " + e.getMessage());
            }
        }
    }

    /* JADX WARN: Type inference failed for: r0v17, types: [org.questionsreponses.Presenter.DownloaderPresenter$1] */
    @Override // org.questionsreponses.View.Interfaces.DownloaderView.ILoadWebViewFinished
    public void onLoadWebViewSuccess(Context context) {
        try {
            if (this.iDownloader != null) {
                this.iDownloader.progressBarVisibility(8);
                this.iDownloader.webViewVisibility(0);
                this.iDownloader.fabButtonVisibility(0);
                new CountDownTimer(this, 1000L, 500L, CommonPresenter.getViewInTermsOfContext(context), context) { // from class: org.questionsreponses.Presenter.DownloaderPresenter.1
                    final DownloaderPresenter this$0;
                    final Context val$context;
                    final View val$view;

                    {
                        this.this$0 = this;
                        this.val$view = view;
                        this.val$context = context;
                    }

                    @Override // android.os.CountDownTimer
                    public void onFinish() {
                        CommonPresenter.showMessageSnackBar(this.val$view, this.val$context.getResources().getString(C0598R.string.lb_selected_video));
                    }

                    @Override // android.os.CountDownTimer
                    public void onTick(long j) {
                    }
                }.start();
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "PrivacyPolicyPresenter-->onLoadWebViewSuccess() : " + e.getMessage());
        }
    }

    public void retrieveUserAction(View view, String str) throws Resources.NotFoundException {
        try {
            Activity activity = CommonPresenter.getActivity(view);
            switch (view.getId()) {
                case C0598R.id.fab_back /* 2131296393 */:
                    if (this.iDownloader != null) {
                        this.iDownloader.webViewGoBack();
                        break;
                    }
                    break;
                case C0598R.id.fab_cancel /* 2131296394 */:
                    if (this.iDownloader != null) {
                        this.iDownloader.closeActivity();
                        break;
                    }
                    break;
                case C0598R.id.fab_download_link /* 2131296397 */:
                    if (!CommonPresenter.isPermissionToSaveFileAccepted(view.getContext())) {
                        if (this.iDownloader != null) {
                            this.iDownloader.askPermissionToSaveFile();
                            break;
                        }
                    } else {
                        String videoIdFromYoutubeUrl = CommonPresenter.getVideoIdFromYoutubeUrl(view.getContext(), str);
                        if (!videoIdFromYoutubeUrl.contains("error:{")) {
                            boolean z = true;
                            if (CommonPresenter.getSettingObjectFromSharePreferences(view.getContext(), CommonPresenter.KEY_SETTING_WIFI_EXCLUSIF).getChoice()) {
                                boolean zIsMobileWIFIConnected = CommonPresenter.isMobileWIFIConnected(view.getContext());
                                z = zIsMobileWIFIConnected;
                                if (!zIsMobileWIFIConnected) {
                                    CommonPresenter.showMessage(view.getContext(), view.getContext().getResources().getString(C0598R.string.lb_wifi_only), view.getContext().getResources().getString(C0598R.string.lb_wifi_exclusif_message), false);
                                    z = zIsMobileWIFIConnected;
                                }
                            }
                            if (z) {
                                showFormatVideoToDownload(activity, videoIdFromYoutubeUrl);
                                break;
                            }
                        } else {
                            CommonPresenter.showMessageSnackBar(view, videoIdFromYoutubeUrl.replace("error:{", BuildConfig.FLAVOR).replace("}", BuildConfig.FLAVOR));
                            break;
                        }
                    }
                    break;
                case C0598R.id.fab_info /* 2131296398 */:
                    CommonPresenter.showMessage(view.getContext(), view.getContext().getResources().getString(C0598R.string.lb_info_toknow), view.getContext().getResources().getString(C0598R.string.lb_info_detail), false);
                    break;
                case C0598R.id.fab_toward /* 2131296417 */:
                    if (this.iDownloader != null) {
                        this.iDownloader.webViewGoForward();
                        break;
                    }
                    break;
            }
        } catch (Exception e) {
            Log.e("TAG_ERROR", "DownloaderPresenter-->retrieveUserAction() : " + e.getMessage());
        }
    }
}
