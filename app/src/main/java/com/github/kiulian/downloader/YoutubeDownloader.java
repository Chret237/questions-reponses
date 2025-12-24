package com.github.kiulian.downloader;

import com.github.kiulian.downloader.cipher.CachedCipherFactory;
import com.github.kiulian.downloader.downloader.Downloader;
import com.github.kiulian.downloader.downloader.DownloaderImpl;
import com.github.kiulian.downloader.downloader.request.RequestChannelUploads;
import com.github.kiulian.downloader.downloader.request.RequestPlaylistInfo;
import com.github.kiulian.downloader.downloader.request.RequestSubtitlesInfo;
import com.github.kiulian.downloader.downloader.request.RequestVideoFileDownload;
import com.github.kiulian.downloader.downloader.request.RequestVideoInfo;
import com.github.kiulian.downloader.downloader.request.RequestVideoStreamDownload;
import com.github.kiulian.downloader.downloader.request.RequestWebpage;
import com.github.kiulian.downloader.downloader.response.Response;
import com.github.kiulian.downloader.downloader.response.ResponseImpl;
import com.github.kiulian.downloader.extractor.ExtractorImpl;
import com.github.kiulian.downloader.model.Utils;
import com.github.kiulian.downloader.model.playlist.PlaylistInfo;
import com.github.kiulian.downloader.model.subtitles.SubtitlesInfo;
import com.github.kiulian.downloader.model.videos.VideoInfo;
import com.github.kiulian.downloader.parser.Parser;
import com.github.kiulian.downloader.parser.ParserImpl;
import java.io.File;
import java.io.IOException;
import java.util.List;

/* loaded from: classes-dex2jar.jar:com/github/kiulian/downloader/YoutubeDownloader.class */
public class YoutubeDownloader {
    private final Config config;
    private final Downloader downloader;
    private final Parser parser;

    public YoutubeDownloader() {
        this(Config.buildDefault());
    }

    public YoutubeDownloader(Config config) {
        this.config = config;
        DownloaderImpl downloaderImpl = new DownloaderImpl(config);
        this.downloader = downloaderImpl;
        this.parser = new ParserImpl(config, downloaderImpl, new ExtractorImpl(this.downloader), new CachedCipherFactory(this.downloader));
    }

    public YoutubeDownloader(Config config, Downloader downloader) {
        this(config, downloader, new ParserImpl(config, downloader, new ExtractorImpl(downloader), new CachedCipherFactory(downloader)));
    }

    public YoutubeDownloader(Config config, Downloader downloader, Parser parser) {
        this.config = config;
        this.parser = parser;
        this.downloader = downloader;
    }

    public Response<String> downloadSubtitle(RequestWebpage requestWebpage) {
        return this.downloader.downloadWebpage(requestWebpage);
    }

    public Response<File> downloadVideoFile(RequestVideoFileDownload requestVideoFileDownload) {
        try {
            Utils.createOutDir(requestVideoFileDownload.getOutputDirectory());
            return this.downloader.downloadVideoAsFile(requestVideoFileDownload);
        } catch (IOException e) {
            return ResponseImpl.error(e);
        }
    }

    public Response<Void> downloadVideoStream(RequestVideoStreamDownload requestVideoStreamDownload) {
        return this.downloader.downloadVideoAsStream(requestVideoStreamDownload);
    }

    public Response<PlaylistInfo> getChannelUploads(RequestChannelUploads requestChannelUploads) {
        return this.parser.parseChannelsUploads(requestChannelUploads);
    }

    public Config getConfig() {
        return this.config;
    }

    public Response<PlaylistInfo> getPlaylistInfo(RequestPlaylistInfo requestPlaylistInfo) {
        return this.parser.parsePlaylist(requestPlaylistInfo);
    }

    public Response<List<SubtitlesInfo>> getSubtitlesInfo(RequestSubtitlesInfo requestSubtitlesInfo) {
        return this.parser.parseSubtitlesInfo(requestSubtitlesInfo);
    }

    public Response<VideoInfo> getVideoInfo(RequestVideoInfo requestVideoInfo) {
        return this.parser.parseVideo(requestVideoInfo);
    }
}
