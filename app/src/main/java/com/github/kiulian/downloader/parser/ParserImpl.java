package com.github.kiulian.downloader.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.kiulian.downloader.Config;
import com.github.kiulian.downloader.YoutubeException;
import com.github.kiulian.downloader.cipher.CipherFactory;
import com.github.kiulian.downloader.downloader.Downloader;
import com.github.kiulian.downloader.downloader.YoutubeCallback;
import com.github.kiulian.downloader.downloader.request.RequestChannelUploads;
import com.github.kiulian.downloader.downloader.request.RequestPlaylistInfo;
import com.github.kiulian.downloader.downloader.request.RequestSubtitlesInfo;
import com.github.kiulian.downloader.downloader.request.RequestVideoInfo;
import com.github.kiulian.downloader.downloader.request.RequestWebpage;
import com.github.kiulian.downloader.downloader.response.Response;
import com.github.kiulian.downloader.downloader.response.ResponseImpl;
import com.github.kiulian.downloader.extractor.Extractor;
import com.github.kiulian.downloader.model.playlist.PlaylistDetails;
import com.github.kiulian.downloader.model.playlist.PlaylistInfo;
import com.github.kiulian.downloader.model.playlist.PlaylistVideoDetails;
import com.github.kiulian.downloader.model.subtitles.SubtitlesInfo;
import com.github.kiulian.downloader.model.videos.VideoDetails;
import com.github.kiulian.downloader.model.videos.VideoInfo;
import com.github.kiulian.downloader.model.videos.formats.AudioFormat;
import com.github.kiulian.downloader.model.videos.formats.Format;
import com.github.kiulian.downloader.model.videos.formats.Itag;
import com.github.kiulian.downloader.model.videos.formats.VideoFormat;
import com.github.kiulian.downloader.model.videos.formats.VideoWithAudioFormat;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;

/* loaded from: classes-dex2jar.jar:com/github/kiulian/downloader/parser/ParserImpl.class */
public class ParserImpl implements Parser {
    private final CipherFactory cipherFactory;
    private final Config config;
    private final Downloader downloader;
    private final Extractor extractor;

    public ParserImpl(Config config, Downloader downloader, Extractor extractor, CipherFactory cipherFactory) {
        this.config = config;
        this.downloader = downloader;
        this.extractor = extractor;
        this.cipherFactory = cipherFactory;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void loadPlaylistContinuation(String str, String str2, List<PlaylistVideoDetails> list, String str3) throws YoutubeException {
        Response<String> responseDownloadWebpage = this.downloader.downloadWebpage((RequestWebpage) ((RequestWebpage) ((RequestWebpage) new RequestWebpage("https://www.youtube.com/youtubei/v1/browse?key=", "POST", new JSONObject().fluentPut("context", new JSONObject().fluentPut("client", new JSONObject().fluentPut("clientName", "WEB").fluentPut("clientVersion", "2.20201021.03.00"))).fluentPut("continuation", str).fluentPut("clickTracking", new JSONObject().fluentPut("clickTrackingParams", str2)).toJSONString()).header("X-YouTube-Client-Name", "1")).header("X-YouTube-Client-Version", str3)).header("Content-Type", "application/json"));
        if (!responseDownloadWebpage.mo13ok()) {
            throw new YoutubeException.DownloadException(String.format("Could not load url: %s, exception: %s", "https://www.youtube.com/youtubei/v1/browse?key=", responseDownloadWebpage.error().getMessage()));
        }
        try {
            JSONObject object = JSON.parseObject(responseDownloadWebpage.data());
            populatePlaylist(object.containsKey("continuationContents") ? object.getJSONObject("continuationContents").getJSONObject("playlistVideoListContinuation") : object.getJSONArray("onResponseReceivedActions").getJSONObject(0).getJSONObject("appendContinuationItemsAction"), list, str3);
        } catch (YoutubeException e) {
            throw e;
        } catch (Exception e2) {
            throw new YoutubeException.BadPageException("Could not parse playlist continuation json");
        }
    }

    private List<SubtitlesInfo> parseCaptions(JSONObject jSONObject) {
        if (!jSONObject.containsKey("captions")) {
            return Collections.emptyList();
        }
        JSONObject jSONObject2 = jSONObject.getJSONObject("captions").getJSONObject("playerCaptionsTracklistRenderer");
        if (jSONObject2 == null || jSONObject2.isEmpty()) {
            return Collections.emptyList();
        }
        JSONArray jSONArray = jSONObject2.getJSONArray("captionTracks");
        if (jSONArray == null || jSONArray.isEmpty()) {
            return Collections.emptyList();
        }
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < jSONArray.size(); i++) {
            JSONObject jSONObject3 = jSONArray.getJSONObject(i);
            String string = jSONObject3.getString("languageCode");
            String string2 = jSONObject3.getString("baseUrl");
            String string3 = jSONObject3.getString("vssId");
            if (string != null && string2 != null && string3 != null) {
                arrayList.add(new SubtitlesInfo(string2, string, string3.startsWith("a."), true));
            }
        }
        return arrayList;
    }

    private PlaylistInfo parseChannelsUploads(String str, YoutubeCallback<PlaylistInfo> youtubeCallback) throws YoutubeException {
        String strSubstring;
        if (str.length() != 24 || !str.startsWith("UC")) {
            String str2 = "https://www.youtube.com/c/" + str + "/videos?view=57";
            Response<String> responseDownloadWebpage = this.downloader.downloadWebpage(new RequestWebpage(str2));
            if (!responseDownloadWebpage.mo13ok()) {
                YoutubeException.DownloadException downloadException = new YoutubeException.DownloadException(String.format("Could not load url: %s, exception: %s", str2, responseDownloadWebpage.error().getMessage()));
                if (youtubeCallback != null) {
                    youtubeCallback.onError(downloadException);
                }
                throw downloadException;
            }
            Scanner scanner = new Scanner(responseDownloadWebpage.data());
            scanner.useDelimiter("list=");
            while (true) {
                if (!scanner.hasNext()) {
                    strSubstring = null;
                    break;
                }
                String next = scanner.next();
                if (next.startsWith("UU")) {
                    strSubstring = next.substring(0, 24);
                    break;
                }
            }
        } else {
            strSubstring = "UU" + str.substring(2);
        }
        if (strSubstring != null) {
            return parsePlaylist(strSubstring, youtubeCallback);
        }
        YoutubeException.BadPageException badPageException = new YoutubeException.BadPageException("Upload Playlist not found");
        if (youtubeCallback != null) {
            youtubeCallback.onError(badPageException);
        }
        throw badPageException;
    }

    private Format parseFormat(JSONObject jSONObject, String str, Itag itag, boolean z, String str2) throws YoutubeException, UnsupportedEncodingException {
        if (jSONObject.containsKey("signatureCipher")) {
            JSONObject jSONObject2 = new JSONObject();
            for (String str3 : jSONObject.getString("signatureCipher").replace("\\u0026", "&").split("&")) {
                String[] strArrSplit = str3.split("=");
                jSONObject2.put(strArrSplit[0], (Object) strArrSplit[1]);
            }
            if (!jSONObject2.containsKey("url")) {
                throw new YoutubeException.BadPageException("Could not found url in cipher data");
            }
            String string = jSONObject2.getString("url");
            try {
                string = URLDecoder.decode(string, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (!string.contains("signature") && (jSONObject2.containsKey("s") || (!string.contains("&sig=") && !string.contains("&lsig=")))) {
                String string2 = jSONObject2.getString("s");
                try {
                    string2 = URLDecoder.decode(string2, "UTF-8");
                } catch (UnsupportedEncodingException e2) {
                    e2.printStackTrace();
                }
                jSONObject.put("url", (Object) (string + "&sig=" + this.cipherFactory.createCipher(str).getSignature(string2)));
            }
        }
        boolean z2 = itag.isVideo() || jSONObject.containsKey("size") || jSONObject.containsKey("width");
        return (z2 && (itag.isAudio() ? true : jSONObject.containsKey("audioQuality"))) ? new VideoWithAudioFormat(jSONObject, z, str2) : z2 ? new VideoFormat(jSONObject, z, str2) : new AudioFormat(jSONObject, z, str2);
    }

    private List<Format> parseFormats(JSONObject jSONObject, String str, String str2) throws YoutubeException {
        if (!jSONObject.containsKey("streamingData")) {
            throw new YoutubeException.BadPageException("streamingData not found");
        }
        JSONObject jSONObject2 = jSONObject.getJSONObject("streamingData");
        JSONArray jSONArray = new JSONArray();
        if (jSONObject2.containsKey("formats")) {
            jSONArray.addAll(jSONObject2.getJSONArray("formats"));
        }
        JSONArray jSONArray2 = new JSONArray();
        if (jSONObject2.containsKey("adaptiveFormats")) {
            jSONArray2.addAll(jSONObject2.getJSONArray("adaptiveFormats"));
        }
        ArrayList arrayList = new ArrayList(jSONArray.size() + jSONArray2.size());
        populateFormats(arrayList, jSONArray, str, false, str2);
        populateFormats(arrayList, jSONArray2, str, true, str2);
        return arrayList;
    }

    private PlaylistInfo parsePlaylist(String str, YoutubeCallback<PlaylistInfo> youtubeCallback) throws YoutubeException {
        String str2 = "https://www.youtube.com/playlist?list=" + str;
        Response<String> responseDownloadWebpage = this.downloader.downloadWebpage(new RequestWebpage(str2));
        if (!responseDownloadWebpage.mo13ok()) {
            YoutubeException.DownloadException downloadException = new YoutubeException.DownloadException(String.format("Could not load url: %s, exception: %s", str2, responseDownloadWebpage.error().getMessage()));
            if (youtubeCallback != null) {
                youtubeCallback.onError(downloadException);
            }
            throw downloadException;
        }
        try {
            JSONObject jSONObjectExtractInitialDataFromHtml = this.extractor.extractInitialDataFromHtml(responseDownloadWebpage.data());
            if (!jSONObjectExtractInitialDataFromHtml.containsKey("metadata")) {
                throw new YoutubeException.BadPageException("Invalid initial data json");
            }
            PlaylistDetails playlistDetails = parsePlaylistDetails(str, jSONObjectExtractInitialDataFromHtml);
            try {
                return new PlaylistInfo(playlistDetails, parsePlaylistVideos(jSONObjectExtractInitialDataFromHtml, playlistDetails.videoCount()));
            } catch (YoutubeException e) {
                if (youtubeCallback != null) {
                    youtubeCallback.onError(e);
                }
                throw e;
            }
        } catch (YoutubeException e2) {
            if (youtubeCallback != null) {
                youtubeCallback.onError(e2);
            }
            throw e2;
        }
    }

    private PlaylistDetails parsePlaylistDetails(String str, JSONObject jSONObject) {
        String string;
        String string2 = jSONObject.getJSONObject("metadata").getJSONObject("playlistMetadataRenderer").getString("title");
        JSONArray jSONArray = jSONObject.getJSONObject("sidebar").getJSONObject("playlistSidebarRenderer").getJSONArray("items");
        try {
            string = jSONArray.getJSONObject(1).getJSONObject("playlistSidebarSecondaryInfoRenderer").getJSONObject("videoOwner").getJSONObject("videoOwnerRenderer").getJSONObject("title").getJSONArray("runs").getJSONObject(0).getString("text");
        } catch (Exception e) {
            string = null;
        }
        JSONArray jSONArray2 = jSONArray.getJSONObject(0).getJSONObject("playlistSidebarPrimaryInfoRenderer").getJSONArray("stats");
        return new PlaylistDetails(str, string2, string, this.extractor.extractIntegerFromText(jSONArray2.getJSONObject(0).getJSONArray("runs").getJSONObject(0).getString("text")), this.extractor.extractIntegerFromText(jSONArray2.getJSONObject(1).getString("simpleText")));
    }

    private List<PlaylistVideoDetails> parsePlaylistVideos(JSONObject jSONObject, int i) throws YoutubeException {
        try {
            JSONObject jSONObject2 = jSONObject.getJSONObject("contents").getJSONObject("twoColumnBrowseResultsRenderer").getJSONArray("tabs").getJSONObject(0).getJSONObject("tabRenderer").getJSONObject("content").getJSONObject("sectionListRenderer").getJSONArray("contents").getJSONObject(0).getJSONObject("itemSectionRenderer").getJSONArray("contents").getJSONObject(0).getJSONObject("playlistVideoListRenderer");
            List<PlaylistVideoDetails> arrayList = i > 0 ? new ArrayList(i) : new LinkedList();
            populatePlaylist(jSONObject2, arrayList, this.extractor.extractClientVersionFromContext(jSONObject.getJSONObject("responseContext")));
            return arrayList;
        } catch (NullPointerException e) {
            throw new YoutubeException.BadPageException("Playlist initial data not found");
        }
    }

    private List<SubtitlesInfo> parseSubtitlesInfo(String str, YoutubeCallback<List<SubtitlesInfo>> youtubeCallback) throws YoutubeException {
        String str2 = "https://video.google.com/timedtext?hl=en&type=list&v=" + str;
        Response<String> responseDownloadWebpage = this.downloader.downloadWebpage(new RequestWebpage(str2));
        if (!responseDownloadWebpage.mo13ok()) {
            YoutubeException.DownloadException downloadException = new YoutubeException.DownloadException(String.format("Could not load url: %s, exception: %s", str2, responseDownloadWebpage.error().getMessage()));
            if (youtubeCallback != null) {
                youtubeCallback.onError(downloadException);
            }
            throw downloadException;
        }
        try {
            List<String> listExtractSubtitlesLanguagesFromXml = this.extractor.extractSubtitlesLanguagesFromXml(responseDownloadWebpage.data());
            ArrayList arrayList = new ArrayList();
            for (String str3 : listExtractSubtitlesLanguagesFromXml) {
                arrayList.add(new SubtitlesInfo(String.format("https://www.youtube.com/api/timedtext?lang=%s&v=%s", str3, str), str3, false));
            }
            return arrayList;
        } catch (YoutubeException e) {
            if (youtubeCallback != null) {
                youtubeCallback.onError(e);
            }
            throw e;
        }
    }

    private VideoDetails parseVideoDetails(String str, JSONObject jSONObject) {
        if (!jSONObject.containsKey("videoDetails")) {
            return new VideoDetails(str);
        }
        JSONObject jSONObject2 = jSONObject.getJSONObject("videoDetails");
        String string = null;
        if (jSONObject2.getBooleanValue("isLive")) {
            string = null;
            if (jSONObject.containsKey("streamingData")) {
                string = jSONObject.getJSONObject("streamingData").getString("hlsManifestUrl");
            }
        }
        return new VideoDetails(jSONObject2, string);
    }

    private void populateFormats(List<Format> list, JSONArray jSONArray, String str, boolean z, String str2) throws YoutubeException.CipherException {
        for (int i = 0; i < jSONArray.size(); i++) {
            JSONObject jSONObject = jSONArray.getJSONObject(i);
            if (!"FORMAT_STREAM_TYPE_OTF".equals(jSONObject.getString("type"))) {
                int intValue = jSONObject.getIntValue("itag");
                try {
                    try {
                        list.add(parseFormat(jSONObject, str, Itag.valueOf("i" + intValue), z, str2));
                    } catch (YoutubeException.CipherException e) {
                        throw e;
                    } catch (YoutubeException e2) {
                        System.err.println("Error " + e2.getMessage() + " parsing format: " + jSONObject);
                    } catch (Exception e3) {
                        e3.printStackTrace();
                    }
                } catch (IllegalArgumentException e4) {
                    System.err.println("Error parsing format: unknown itag " + intValue);
                }
            }
        }
    }

    private void populatePlaylist(JSONObject jSONObject, List<PlaylistVideoDetails> list, String str) throws YoutubeException {
        JSONArray jSONArray;
        if (jSONObject.containsKey("contents")) {
            jSONArray = jSONObject.getJSONArray("contents");
        } else {
            if (!jSONObject.containsKey("continuationItems")) {
                if (jSONObject.containsKey("continuations")) {
                    JSONObject jSONObject2 = jSONObject.getJSONArray("continuations").getJSONObject(0).getJSONObject("nextContinuationData");
                    loadPlaylistContinuation(jSONObject2.getString("continuation"), jSONObject2.getString("clickTrackingParams"), list, str);
                    return;
                }
                return;
            }
            jSONArray = jSONObject.getJSONArray("continuationItems");
        }
        for (int i = 0; i < jSONArray.size(); i++) {
            JSONObject jSONObject3 = jSONArray.getJSONObject(i);
            if (jSONObject3.containsKey("playlistVideoRenderer")) {
                list.add(new PlaylistVideoDetails(jSONObject3.getJSONObject("playlistVideoRenderer")));
            } else if (jSONObject3.containsKey("continuationItemRenderer")) {
                JSONObject jSONObject4 = jSONObject3.getJSONObject("continuationItemRenderer").getJSONObject("continuationEndpoint");
                loadPlaylistContinuation(jSONObject4.getJSONObject("continuationCommand").getString("token"), jSONObject4.getString("clickTrackingParams"), list, str);
            }
        }
    }

    public /* synthetic */ PlaylistInfo lambda$parseChannelsUploads$2$ParserImpl(RequestChannelUploads requestChannelUploads) throws Exception {
        return parseChannelsUploads(requestChannelUploads.getChannelId(), requestChannelUploads.getCallback());
    }

    public /* synthetic */ PlaylistInfo lambda$parsePlaylist$1$ParserImpl(RequestPlaylistInfo requestPlaylistInfo) throws Exception {
        return parsePlaylist(requestPlaylistInfo.getPlaylistId(), requestPlaylistInfo.getCallback());
    }

    public /* synthetic */ List lambda$parseSubtitlesInfo$3$ParserImpl(RequestSubtitlesInfo requestSubtitlesInfo) throws Exception {
        return parseSubtitlesInfo(requestSubtitlesInfo.getVideoId(), requestSubtitlesInfo.getCallback());
    }

    public /* synthetic */ VideoInfo lambda$parseVideo$0$ParserImpl(RequestVideoInfo requestVideoInfo) throws Exception {
        return parseVideo(requestVideoInfo.getVideoId(), requestVideoInfo.getCallback());
    }

    @Override // com.github.kiulian.downloader.parser.Parser
    public Response<PlaylistInfo> parseChannelsUploads(final RequestChannelUploads requestChannelUploads) {
        if (requestChannelUploads.isAsync()) {
            return ResponseImpl.fromFuture(this.config.getExecutorService().submit(new Callable(this, requestChannelUploads) { // from class: com.github.kiulian.downloader.parser._$$Lambda$ParserImpl$ltPkKtgGpp133PfX6iA0G6Y_b9c
                public final ParserImpl f$0;
                public final RequestChannelUploads f$1;

                {
                    this.f$0 = this;
                    this.f$1 = requestChannelUploads;
                }

                @Override // java.util.concurrent.Callable
                public final Object call() {
                    return this.f$0.lambda$parseChannelsUploads$2$ParserImpl(this.f$1);
                }
            }));
        }
        try {
            return ResponseImpl.from(parseChannelsUploads(requestChannelUploads.getChannelId(), requestChannelUploads.getCallback()));
        } catch (YoutubeException e) {
            return ResponseImpl.error(e);
        }
    }

    @Override // com.github.kiulian.downloader.parser.Parser
    public Response<PlaylistInfo> parsePlaylist(final RequestPlaylistInfo requestPlaylistInfo) {
        if (requestPlaylistInfo.isAsync()) {
            return ResponseImpl.fromFuture(this.config.getExecutorService().submit(new Callable(this, requestPlaylistInfo) { // from class: com.github.kiulian.downloader.parser._$$Lambda$ParserImpl$4S3Iw9or9HhRzW_e8_DgA1aTG9M
                public final ParserImpl f$0;
                public final RequestPlaylistInfo f$1;

                {
                    this.f$0 = this;
                    this.f$1 = requestPlaylistInfo;
                }

                @Override // java.util.concurrent.Callable
                public final Object call() {
                    return this.f$0.lambda$parsePlaylist$1$ParserImpl(this.f$1);
                }
            }));
        }
        try {
            return ResponseImpl.from(parsePlaylist(requestPlaylistInfo.getPlaylistId(), requestPlaylistInfo.getCallback()));
        } catch (YoutubeException e) {
            return ResponseImpl.error(e);
        }
    }

    @Override // com.github.kiulian.downloader.parser.Parser
    public Response<List<SubtitlesInfo>> parseSubtitlesInfo(final RequestSubtitlesInfo requestSubtitlesInfo) {
        if (requestSubtitlesInfo.isAsync()) {
            return ResponseImpl.fromFuture(this.config.getExecutorService().submit(new Callable(this, requestSubtitlesInfo) { // from class: com.github.kiulian.downloader.parser._$$Lambda$ParserImpl$9KdXJiuuDXsSPSKgqebojFeLeZE
                public final ParserImpl f$0;
                public final RequestSubtitlesInfo f$1;

                {
                    this.f$0 = this;
                    this.f$1 = requestSubtitlesInfo;
                }

                @Override // java.util.concurrent.Callable
                public final Object call() {
                    return this.f$0.lambda$parseSubtitlesInfo$3$ParserImpl(this.f$1);
                }
            }));
        }
        try {
            return ResponseImpl.from(parseSubtitlesInfo(requestSubtitlesInfo.getVideoId(), requestSubtitlesInfo.getCallback()));
        } catch (YoutubeException e) {
            return ResponseImpl.error(e);
        }
    }

    @Override // com.github.kiulian.downloader.parser.Parser
    public Response<VideoInfo> parseVideo(final RequestVideoInfo requestVideoInfo) {
        if (requestVideoInfo.isAsync()) {
            return ResponseImpl.fromFuture(this.config.getExecutorService().submit(new Callable(this, requestVideoInfo) { // from class: com.github.kiulian.downloader.parser._$$Lambda$ParserImpl$KyAgRptuObCkTot2gxO5IvQwvhs
                public final ParserImpl f$0;
                public final RequestVideoInfo f$1;

                {
                    this.f$0 = this;
                    this.f$1 = requestVideoInfo;
                }

                @Override // java.util.concurrent.Callable
                public final Object call() {
                    return this.f$0.lambda$parseVideo$0$ParserImpl(this.f$1);
                }
            }));
        }
        try {
            return ResponseImpl.from(parseVideo(requestVideoInfo.getVideoId(), requestVideoInfo.getCallback()));
        } catch (YoutubeException e) {
            return ResponseImpl.error(e);
        }
    }

    public VideoInfo parseVideo(String str, YoutubeCallback<VideoInfo> youtubeCallback) throws YoutubeException {
        String str2 = "https://www.youtube.com/watch?v=" + str;
        Response<String> responseDownloadWebpage = this.downloader.downloadWebpage(new RequestWebpage(str2));
        if (!responseDownloadWebpage.mo13ok()) {
            YoutubeException.DownloadException downloadException = new YoutubeException.DownloadException(String.format("Could not load url: %s, exception: %s", str2, responseDownloadWebpage.error().getMessage()));
            if (youtubeCallback != null) {
                youtubeCallback.onError(downloadException);
            }
            throw downloadException;
        }
        try {
            JSONObject jSONObjectExtractPlayerConfigFromHtml = this.extractor.extractPlayerConfigFromHtml(responseDownloadWebpage.data());
            JSONObject jSONObject = jSONObjectExtractPlayerConfigFromHtml.getJSONObject("args").getJSONObject("player_response");
            if (!jSONObject.containsKey("streamingData") && !jSONObject.containsKey("videoDetails")) {
                throw new YoutubeException.BadPageException("streamingData and videoDetails not found");
            }
            VideoDetails videoDetails = parseVideoDetails(str, jSONObject);
            if (!videoDetails.isDownloadable()) {
                return new VideoInfo(videoDetails, Collections.emptyList(), Collections.emptyList());
            }
            try {
                return new VideoInfo(videoDetails, parseFormats(jSONObject, this.extractor.extractJsUrlFromConfig(jSONObjectExtractPlayerConfigFromHtml, str), this.extractor.extractClientVersionFromContext(jSONObjectExtractPlayerConfigFromHtml.getJSONObject("args").getJSONObject("player_response").getJSONObject("responseContext"))), parseCaptions(jSONObject));
            } catch (YoutubeException e) {
                if (youtubeCallback != null) {
                    youtubeCallback.onError(e);
                }
                throw e;
            }
        } catch (YoutubeException e2) {
            if (youtubeCallback != null) {
                youtubeCallback.onError(e2);
            }
            throw e2;
        }
    }
}
