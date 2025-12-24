package com.github.kiulian.downloader.model.playlist;

import com.github.kiulian.downloader.model.Filter;
import java.util.List;

/* loaded from: classes-dex2jar.jar:com/github/kiulian/downloader/model/playlist/PlaylistInfo.class */
public class PlaylistInfo {
    private PlaylistDetails details;
    private List<PlaylistVideoDetails> videos;

    public PlaylistInfo(PlaylistDetails playlistDetails, List<PlaylistVideoDetails> list) {
        this.details = playlistDetails;
        this.videos = list;
    }

    public PlaylistDetails details() {
        return this.details;
    }

    public PlaylistVideoDetails findVideoById(String str) {
        for (PlaylistVideoDetails playlistVideoDetails : this.videos) {
            if (playlistVideoDetails.videoId().equals(str)) {
                return playlistVideoDetails;
            }
        }
        return null;
    }

    public List<PlaylistVideoDetails> findVideos(Filter<PlaylistVideoDetails> filter) {
        return filter.select(this.videos);
    }

    public List<PlaylistVideoDetails> videos() {
        return this.videos;
    }
}
