package org.questionsreponses.View.Interfaces;

import android.content.Context;
import java.util.ArrayList;
import org.questionsreponses.Model.Youtube;
import org.questionsreponses.View.WebView.YoutubeWebClient;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Interfaces/DownloaderView.class */
public class DownloaderView {

    /* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Interfaces/DownloaderView$IDownloader.class */
    public interface IDownloader {
        void askPermissionToSaveFile();

        void closeActivity();

        void displayFormatVideoList(ArrayList<Youtube> arrayList);

        void events();

        void fabButtonVisibility(int i);

        void initialize();

        void loadWebView(YoutubeWebClient youtubeWebClient, String str);

        void progressBarVisibility(int i);

        void webViewGoBack();

        void webViewGoForward();

        void webViewVisibility(int i);
    }

    /* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Interfaces/DownloaderView$ILoadFormatVideoFinished.class */
    public interface ILoadFormatVideoFinished {
        void onLoadFormatVideoFailure(Context context);

        void onLoadFormatVideoSuccess(ArrayList<Youtube> arrayList);
    }

    /* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Interfaces/DownloaderView$ILoadWebViewFinished.class */
    public interface ILoadWebViewFinished {
        void onLoadWebViewFailure(Context context);

        void onLoadWebViewSuccess(Context context);
    }

    /* loaded from: classes-dex2jar.jar:org/questionsreponses/View/Interfaces/DownloaderView$IPresenter.class */
    public interface IPresenter {
        void onLoadFormatFailure(Context context);

        void onLoadFormatSuccess(ArrayList<Youtube> arrayList);
    }
}
