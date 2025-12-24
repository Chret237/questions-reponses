package com.github.kiulian.downloader.model.videos.quality;

/* loaded from: classes-dex2jar.jar:com/github/kiulian/downloader/model/videos/quality/VideoQuality.class */
public enum VideoQuality {
    unknown(0),
    noVideo(0),
    tiny(1),
    small(2),
    medium(3),
    large(4),
    hd720(5),
    hd1080(6),
    hd1440(7),
    hd2160(8),
    hd2880p(9),
    highres(10);

    private final Integer order;

    VideoQuality(int i) {
        this.order = Integer.valueOf(i);
    }

    public int compare(VideoQuality videoQuality) {
        if (this == videoQuality) {
            return 0;
        }
        return this.order.compareTo(videoQuality.order);
    }
}
