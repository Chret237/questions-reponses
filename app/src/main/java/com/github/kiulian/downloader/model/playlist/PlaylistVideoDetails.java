package com.github.kiulian.downloader.model.playlist;

import com.alibaba.fastjson.JSONObject;
import com.github.kiulian.downloader.model.AbstractVideoDetails;

/* loaded from: classes-dex2jar.jar:com/github/kiulian/downloader/model/playlist/PlaylistVideoDetails.class */
public class PlaylistVideoDetails extends AbstractVideoDetails {
    private int index;
    private boolean isPlayable;

    public PlaylistVideoDetails() {
    }

    public PlaylistVideoDetails(JSONObject jSONObject) {
        super(jSONObject);
        if (jSONObject.containsKey("shortBylineText")) {
            this.author = jSONObject.getJSONObject("shortBylineText").getJSONArray("runs").getJSONObject(0).getString("text");
        }
        JSONObject jSONObject2 = jSONObject.getJSONObject("title");
        if (jSONObject2.containsKey("simpleText")) {
            this.title = jSONObject2.getString("simpleText");
        } else {
            this.title = jSONObject2.getJSONArray("runs").getJSONObject(0).getString("text");
        }
        if (!thumbnails().isEmpty()) {
            this.isLive = thumbnails().get(0).contains("/hqdefault_live.jpg?");
        }
        if (jSONObject.containsKey("index")) {
            this.index = jSONObject.getJSONObject("index").getIntValue("simpleText");
        }
        this.isPlayable = jSONObject.getBooleanValue("isPlayable");
    }

    public int index() {
        return this.index;
    }

    @Override // com.github.kiulian.downloader.model.AbstractVideoDetails
    protected boolean isDownloadable() {
        return this.isPlayable && super.isDownloadable();
    }

    public boolean isPlayable() {
        return this.isPlayable;
    }
}
