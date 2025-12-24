package com.github.kiulian.downloader.model.videos.formats;

import com.alibaba.fastjson.JSONObject;
import com.github.kiulian.downloader.model.videos.quality.VideoQuality;

/* loaded from: classes-dex2jar.jar:com/github/kiulian/downloader/model/videos/formats/VideoFormat.class */
public class VideoFormat extends Format {
    private final int fps;
    private final Integer height;
    private final String qualityLabel;
    private final VideoQuality videoQuality;
    private final Integer width;

    public VideoFormat(JSONObject jSONObject, boolean z, String str) {
        super(jSONObject, z, str);
        this.fps = jSONObject.getInteger("fps").intValue();
        this.qualityLabel = jSONObject.getString("qualityLabel");
        if (jSONObject.containsKey("size")) {
            String[] strArrSplit = jSONObject.getString("size").split("x");
            this.width = Integer.valueOf(Integer.parseInt(strArrSplit[0]));
            this.height = Integer.valueOf(Integer.parseInt(strArrSplit[1]));
        } else {
            this.width = jSONObject.getInteger("width");
            this.height = jSONObject.getInteger("height");
        }
        VideoQuality videoQualityValueOf = null;
        if (jSONObject.containsKey("quality")) {
            try {
                videoQualityValueOf = VideoQuality.valueOf(jSONObject.getString("quality"));
            } catch (IllegalArgumentException e) {
                videoQualityValueOf = null;
            }
        }
        this.videoQuality = videoQualityValueOf;
    }

    public int fps() {
        return this.fps;
    }

    public Integer height() {
        return this.height;
    }

    public String qualityLabel() {
        return this.qualityLabel;
    }

    @Override // com.github.kiulian.downloader.model.videos.formats.Format
    public String type() {
        return Format.VIDEO;
    }

    public VideoQuality videoQuality() {
        VideoQuality videoQuality = this.videoQuality;
        if (videoQuality == null) {
            videoQuality = this.itag.videoQuality();
        }
        return videoQuality;
    }

    public Integer width() {
        return this.width;
    }
}
