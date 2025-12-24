package com.github.kiulian.downloader.model.videos.formats;

import com.alibaba.fastjson.JSONObject;
import com.github.kiulian.downloader.model.videos.quality.AudioQuality;

/* loaded from: classes-dex2jar.jar:com/github/kiulian/downloader/model/videos/formats/VideoWithAudioFormat.class */
public class VideoWithAudioFormat extends VideoFormat {
    private final AudioQuality audioQuality;
    private final Integer audioSampleRate;
    private final Integer averageBitrate;

    public VideoWithAudioFormat(JSONObject jSONObject, boolean z, String str) {
        AudioQuality audioQualityValueOf;
        super(jSONObject, z, str);
        this.audioSampleRate = jSONObject.getInteger("audioSampleRate");
        this.averageBitrate = jSONObject.getInteger("averageBitrate");
        if (jSONObject.containsKey("audioQuality")) {
            String[] strArrSplit = jSONObject.getString("audioQuality").split("_");
            try {
                audioQualityValueOf = AudioQuality.valueOf(strArrSplit[strArrSplit.length - 1].toLowerCase());
            } catch (IllegalArgumentException e) {
            }
        } else {
            audioQualityValueOf = null;
        }
        this.audioQuality = audioQualityValueOf;
    }

    public AudioQuality audioQuality() {
        AudioQuality audioQuality = this.audioQuality;
        if (audioQuality == null) {
            audioQuality = this.itag.audioQuality();
        }
        return audioQuality;
    }

    public Integer audioSampleRate() {
        return this.audioSampleRate;
    }

    public Integer averageBitrate() {
        return this.averageBitrate;
    }

    @Override // com.github.kiulian.downloader.model.videos.formats.VideoFormat, com.github.kiulian.downloader.model.videos.formats.Format
    public String type() {
        return Format.AUDIO_VIDEO;
    }
}
