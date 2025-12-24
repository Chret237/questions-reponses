package com.github.kiulian.downloader.model.videos.quality;

/* loaded from: classes-dex2jar.jar:com/github/kiulian/downloader/model/videos/quality/AudioQuality.class */
public enum AudioQuality {
    unknown(0),
    noAudio(0),
    low(1),
    medium(2),
    high(3);

    private final Integer order;

    AudioQuality(int i) {
        this.order = Integer.valueOf(i);
    }

    public int compare(AudioQuality audioQuality) {
        if (this == audioQuality) {
            return 0;
        }
        return this.order.compareTo(audioQuality.order);
    }
}
