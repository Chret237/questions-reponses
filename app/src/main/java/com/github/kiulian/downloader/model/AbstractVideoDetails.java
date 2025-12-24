package com.github.kiulian.downloader.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes-dex2jar.jar:com/github/kiulian/downloader/model/AbstractVideoDetails.class */
public abstract class AbstractVideoDetails {
    protected String author;
    protected boolean isLive;
    private int lengthSeconds;
    private List<String> thumbnails;
    protected String title;
    protected String videoId;

    public AbstractVideoDetails() {
    }

    public AbstractVideoDetails(JSONObject jSONObject) {
        this.videoId = jSONObject.getString("videoId");
        this.lengthSeconds = jSONObject.getIntValue("lengthSeconds");
        JSONArray jSONArray = jSONObject.getJSONObject("thumbnail").getJSONArray("thumbnails");
        this.thumbnails = new ArrayList(jSONArray.size());
        for (int i = 0; i < jSONArray.size(); i++) {
            JSONObject jSONObject2 = jSONArray.getJSONObject(i);
            if (jSONObject2.containsKey("url")) {
                this.thumbnails.add(jSONObject2.getString("url"));
            }
        }
    }

    public String author() {
        return this.author;
    }

    protected boolean isDownloadable() {
        return (isLive() || lengthSeconds() == 0) ? false : true;
    }

    public boolean isLive() {
        return this.isLive;
    }

    public int lengthSeconds() {
        return this.lengthSeconds;
    }

    public List<String> thumbnails() {
        return this.thumbnails;
    }

    public String title() {
        return this.title;
    }

    public String videoId() {
        return this.videoId;
    }
}
