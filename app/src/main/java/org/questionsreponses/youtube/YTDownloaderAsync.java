package org.questionsreponses.youtube;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.github.clans.fab.BuildConfig;
import com.github.kiulian.downloader.Config;
import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.downloader.YoutubeCallback;
import com.github.kiulian.downloader.downloader.request.RequestVideoInfo;
import com.github.kiulian.downloader.model.videos.VideoInfo;
import com.github.kiulian.downloader.model.videos.formats.VideoFormat;
import com.github.kiulian.downloader.model.videos.quality.VideoQuality;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import org.questionsreponses.Model.Youtube;
import org.questionsreponses.View.Interfaces.DownloaderView;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/youtube/YTDownloaderAsync.class */
public class YTDownloaderAsync extends AsyncTask<Void, Void, ArrayList<YTDownloader>> {
    private Config config;
    private Context context;
    private YoutubeDownloader downloader;
    private DownloaderView.IPresenter iPresenter;
    private RequestVideoInfo request;
    private String videoExtension;
    private String videoId;
    private ArrayList<YTDownloader> ytDownloaders;
    private ArrayList<YTDownloader> ytMp4Format;
    private ArrayList<YTDownloader> ytWebmFormat;

    private void fillYoutubeVideoData(List<VideoFormat> list, VideoInfo videoInfo, String str) {
        try {
            if (this.ytDownloaders == null) {
                this.ytDownloaders = new ArrayList<>();
            }
            if (this.ytMp4Format == null) {
                this.ytMp4Format = new ArrayList<>();
            }
            if (this.ytWebmFormat == null) {
                this.ytWebmFormat = new ArrayList<>();
            }
            if (list != null && list.size() > 0) {
                String strTitle = videoInfo.details().title();
                long jViewCount = videoInfo.details().viewCount();
                String strDescription = videoInfo.details().description();
                List<String> listThumbnails = videoInfo.details().thumbnails();
                ArrayList arrayList = new ArrayList();
                if (listThumbnails != null && listThumbnails.size() > 0) {
                    for (int i = 0; i < listThumbnails.size(); i++) {
                        arrayList.add(listThumbnails.get(i));
                    }
                }
                for (int i2 = 0; i2 < list.size(); i2++) {
                    VideoFormat videoFormat = list.get(i2);
                    String strQualityLabel = videoFormat.qualityLabel();
                    String strMimeType = videoFormat.mimeType();
                    String str2 = strMimeType.replace(";", BuildConfig.FLAVOR).split(" ")[0];
                    String strUrl = videoFormat.url();
                    videoFormat.videoQuality();
                    int iIntValue = videoFormat.height().intValue();
                    String str3 = videoFormat.width().intValue() + "x" + iIntValue;
                    String fileSize = getFileSize(videoFormat.contentLength().longValue());
                    String strReplace = str2.replace("video/", BuildConfig.FLAVOR);
                    videoFormat.duration().longValue();
                    String str4 = BuildConfig.FLAVOR + videoFormat.itag();
                    videoFormat.bitrate().intValue();
                    videoFormat.lastModified();
                    String strRemoveAccents = removeAccents(strTitle);
                    YTDownloader yTDownloader = new YTDownloader(this.videoId, this.videoId, str4, str, strQualityLabel, str2, strReplace, str3, strUrl, strUrl, "0", "0.0.0.0", "0.0.0.0", BuildConfig.FLAVOR + jViewCount, strTitle, fileSize, strRemoveAccents, strDescription, strMimeType, arrayList);
                    if (strReplace.equalsIgnoreCase("mp4")) {
                        this.ytMp4Format.add(yTDownloader);
                    }
                    if (strReplace.equalsIgnoreCase("webm")) {
                        this.ytWebmFormat.add(yTDownloader);
                    }
                }
            }
            if (this.videoExtension.equalsIgnoreCase("mp4")) {
                this.ytDownloaders = this.ytMp4Format;
                return;
            }
            if (this.videoExtension.equalsIgnoreCase("webm")) {
                this.ytDownloaders = this.ytWebmFormat;
                return;
            }
            if (this.ytMp4Format.size() > 0) {
                for (int i3 = 0; i3 < this.ytMp4Format.size(); i3++) {
                    this.ytDownloaders.add(this.ytMp4Format.get(i3));
                }
            }
            if (this.ytWebmFormat.size() > 0) {
                for (int i4 = 0; i4 < this.ytWebmFormat.size(); i4++) {
                    this.ytDownloaders.add(this.ytWebmFormat.get(i4));
                }
            }
        } catch (Exception e) {
            if (this.ytDownloaders == null) {
                this.ytDownloaders = new ArrayList<>();
            }
            if (this.ytMp4Format == null) {
                this.ytMp4Format = new ArrayList<>();
            }
            if (this.ytWebmFormat == null) {
                this.ytWebmFormat = new ArrayList<>();
            }
            Log.e("TAG_APP_ERROR", "YTDownloaderAsync-->fillYoutubeVideoData() : " + e.getMessage());
        }
    }

    private List<VideoFormat> findVideoWithQuality(VideoQuality videoQuality, List<VideoFormat> list) {
        LinkedList linkedList = new LinkedList();
        for (int i = 0; i < list.size(); i++) {
            VideoFormat videoFormat = list.get(i);
            if (videoFormat instanceof VideoFormat) {
                VideoFormat videoFormat2 = videoFormat;
                if (videoFormat2.videoQuality() == videoQuality) {
                    linkedList.add(videoFormat2);
                }
            }
        }
        return linkedList;
    }

    private String getFileSize(long j) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        float f = j;
        if (f < 1048576.0f) {
            return decimalFormat.format(f / 1024.0f) + " KB";
        }
        if (f < 1.0737418E9f) {
            return decimalFormat.format(f / 1048576.0f) + " MB";
        }
        if (f >= 1.0995116E12f) {
            return "0";
        }
        return decimalFormat.format(f / 1.0737418E9f) + " GB";
    }

    private String removeAccents(String str) {
        return Normalizer.normalize(str.replace(")", " ").trim(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", BuildConfig.FLAVOR).replaceAll("\\W", "-");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public ArrayList<YTDownloader> doInBackground(Void... voidArr) {
        RequestVideoInfo requestVideoInfoAsync = new RequestVideoInfo(this.videoId).callback(new YoutubeCallback<VideoInfo>(this) { // from class: org.questionsreponses.youtube.YTDownloaderAsync.1
            final YTDownloaderAsync this$0;

            {
                this.this$0 = this;
            }

            @Override // com.github.kiulian.downloader.downloader.YoutubeCallback
            public void onError(Throwable th) {
                Log.e("TAG_APP_TUBE", "throwable.getMessage() : " + th.getMessage());
            }

            @Override // com.github.kiulian.downloader.downloader.YoutubeCallback
            public void onFinished(VideoInfo videoInfo) {
                Log.i("TAG_APP_TUBE", "Finished parsing");
            }
        }).async();
        this.request = requestVideoInfoAsync;
        try {
            VideoInfo videoInfoData = this.downloader.getVideoInfo(requestVideoInfoAsync).data();
            fillYoutubeVideoData(findVideoWithQuality(VideoQuality.hd1080, videoInfoData.videoFormats()), videoInfoData, "hd1080");
            fillYoutubeVideoData(findVideoWithQuality(VideoQuality.hd720, videoInfoData.videoFormats()), videoInfoData, "hd720");
            fillYoutubeVideoData(findVideoWithQuality(VideoQuality.large, videoInfoData.videoFormats()), videoInfoData, "large");
            fillYoutubeVideoData(findVideoWithQuality(VideoQuality.medium, videoInfoData.videoFormats()), videoInfoData, "medium");
            fillYoutubeVideoData(findVideoWithQuality(VideoQuality.small, videoInfoData.videoFormats()), videoInfoData, "small");
            fillYoutubeVideoData(findVideoWithQuality(VideoQuality.tiny, videoInfoData.videoFormats()), videoInfoData, "tiny");
        } catch (Exception e) {
        }
        return this.ytDownloaders;
    }

    public void initialize(Context context, String str, String str2, DownloaderView.IPresenter iPresenter) {
        this.context = context;
        this.videoId = str;
        this.videoExtension = str2;
        this.iPresenter = iPresenter;
    }

    public void initialize(String str, String str2) {
        this.videoId = str;
        this.videoExtension = str2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public void onPostExecute(ArrayList<YTDownloader> arrayList) {
        super.onPostExecute((YTDownloaderAsync) arrayList);
        if (arrayList == null || arrayList.size() <= 0) {
            this.iPresenter.onLoadFormatFailure(this.context);
            return;
        }
        Hashtable hashtable = new Hashtable();
        ArrayList<Youtube> arrayList2 = new ArrayList<>();
        for (int i = 0; i < arrayList.size(); i++) {
            YTDownloader yTDownloader = arrayList.get(i);
            String videoId = yTDownloader.getVideoId();
            String itag = yTDownloader.getItag();
            String quality = yTDownloader.getQuality();
            String type = yTDownloader.getType();
            String extension = yTDownloader.getExtension();
            String resolution = yTDownloader.getResolution();
            String url = yTDownloader.getUrl();
            String download_url = yTDownloader.getDownload_url();
            String expires = yTDownloader.getExpires();
            String ipbits = yTDownloader.getIpbits();
            String ip = yTDownloader.getIp();
            String view_count = yTDownloader.getView_count();
            String title = yTDownloader.getTitle();
            String size = yTDownloader.getSize();
            String filename = yTDownloader.getFilename();
            if (!hashtable.containsKey(resolution)) {
                hashtable.put(resolution, resolution);
                arrayList2.add(new Youtube(videoId, itag, quality, type, extension, resolution, url, download_url, expires, ipbits, ip, view_count, title, size, filename));
            }
        }
        this.iPresenter.onLoadFormatSuccess(arrayList2);
    }

    @Override // android.os.AsyncTask
    protected void onPreExecute() {
        super.onPreExecute();
        YoutubeDownloader youtubeDownloader = new YoutubeDownloader();
        this.downloader = youtubeDownloader;
        Config config = youtubeDownloader.getConfig();
        this.config = config;
        config.setMaxRetries(0);
    }
}
