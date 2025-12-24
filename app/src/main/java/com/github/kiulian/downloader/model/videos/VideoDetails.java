package com.github.kiulian.downloader.model.videos;

import com.alibaba.fastjson.JSONObject;
import com.github.kiulian.downloader.model.AbstractVideoDetails;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes-dex2jar.jar:com/github/kiulian/downloader/model/videos/VideoDetails.class */
public class VideoDetails extends AbstractVideoDetails {
    private int averageRating;
    private boolean isLiveContent;
    private List<String> keywords;
    private String liveUrl;
    private String shortDescription;
    private long viewCount;

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v15, types: [java.util.List] */
    public VideoDetails(JSONObject jSONObject, String str) {
        super(jSONObject);
        this.title = jSONObject.getString("title");
        this.author = jSONObject.getString("author");
        this.isLive = jSONObject.getBooleanValue("isLive");
        this.keywords = jSONObject.containsKey("keywords") ? jSONObject.getJSONArray("keywords").toJavaList(String.class) : new ArrayList();
        this.shortDescription = jSONObject.getString("shortDescription");
        this.averageRating = jSONObject.getIntValue("averageRating");
        this.viewCount = jSONObject.getLongValue("viewCount");
        this.isLiveContent = jSONObject.getBooleanValue("isLiveContent");
        this.liveUrl = str;
    }

    public VideoDetails(String str) {
        this.videoId = str;
    }

    public int averageRating() {
        return this.averageRating;
    }

    public String description() {
        return this.shortDescription;
    }

    @Override // com.github.kiulian.downloader.model.AbstractVideoDetails
    public boolean isDownloadable() {
        return (isLive() || (this.isLiveContent && lengthSeconds() == 0)) ? false : true;
    }

    public boolean isLiveContent() {
        return this.isLiveContent;
    }

    public List<String> keywords() {
        return this.keywords;
    }

    public String liveUrl() {
        return this.liveUrl;
    }

    public long viewCount() {
        return this.viewCount;
    }
}
