package com.github.kiulian.downloader.model.videos;

import com.github.kiulian.downloader.model.Filter;
import com.github.kiulian.downloader.model.subtitles.SubtitlesInfo;
import com.github.kiulian.downloader.model.videos.formats.AudioFormat;
import com.github.kiulian.downloader.model.videos.formats.Format;
import com.github.kiulian.downloader.model.videos.formats.VideoFormat;
import com.github.kiulian.downloader.model.videos.formats.VideoWithAudioFormat;
import java.util.LinkedList;
import java.util.List;

/* loaded from: classes-dex2jar.jar:com/github/kiulian/downloader/model/videos/VideoInfo.class */
public class VideoInfo {
    private final List<Format> formats;
    private final List<SubtitlesInfo> subtitlesInfo;
    private final VideoDetails videoDetails;

    public VideoInfo(VideoDetails videoDetails, List<Format> list, List<SubtitlesInfo> list2) {
        this.videoDetails = videoDetails;
        this.formats = list;
        this.subtitlesInfo = list2;
    }

    public List<AudioFormat> audioFormats() {
        LinkedList linkedList = new LinkedList();
        for (Format format : this.formats) {
            if (format instanceof AudioFormat) {
                linkedList.add((AudioFormat) format);
            }
        }
        return linkedList;
    }

    public AudioFormat bestAudioFormat() {
        AudioFormat audioFormat = null;
        for (Format format : this.formats) {
            if (format instanceof AudioFormat) {
                AudioFormat audioFormat2 = (AudioFormat) format;
                if (audioFormat == null || audioFormat2.audioQuality().compare(audioFormat.audioQuality()) > 0) {
                    audioFormat = audioFormat2;
                }
            }
        }
        return audioFormat;
    }

    public VideoFormat bestVideoFormat() {
        VideoFormat videoFormat = null;
        for (Format format : this.formats) {
            if (format instanceof VideoFormat) {
                VideoFormat videoFormat2 = (VideoFormat) format;
                if (videoFormat == null || videoFormat2.videoQuality().compare(videoFormat.videoQuality()) > 0) {
                    videoFormat = videoFormat2;
                }
            }
        }
        return videoFormat;
    }

    public VideoFormat bestVideoWithAudioFormat() {
        VideoFormat videoFormat = null;
        for (Format format : this.formats) {
            if (format instanceof VideoWithAudioFormat) {
                VideoFormat videoFormat2 = (VideoFormat) format;
                if (videoFormat == null || videoFormat2.videoQuality().compare(videoFormat.videoQuality()) > 0) {
                    videoFormat = videoFormat2;
                }
            }
        }
        return videoFormat;
    }

    public VideoDetails details() {
        return this.videoDetails;
    }

    public Format findFormatByItag(int i) {
        for (Format format : this.formats) {
            if (format.itag().m14id() == i) {
                return format;
            }
        }
        return null;
    }

    public List<Format> findFormats(Filter<Format> filter) {
        return filter.select(this.formats);
    }

    public List<Format> formats() {
        return this.formats;
    }

    public List<SubtitlesInfo> subtitlesInfo() {
        return this.subtitlesInfo;
    }

    public List<VideoFormat> videoFormats() {
        LinkedList linkedList = new LinkedList();
        for (Format format : this.formats) {
            if (format instanceof VideoFormat) {
                linkedList.add((VideoFormat) format);
            }
        }
        return linkedList;
    }

    public List<VideoWithAudioFormat> videoWithAudioFormats() {
        LinkedList linkedList = new LinkedList();
        for (Format format : this.formats) {
            if (format instanceof VideoWithAudioFormat) {
                linkedList.add((VideoWithAudioFormat) format);
            }
        }
        return linkedList;
    }
}
