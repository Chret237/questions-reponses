package com.github.kiulian.downloader.parser;

import com.github.kiulian.downloader.downloader.request.RequestChannelUploads;
import com.github.kiulian.downloader.downloader.request.RequestPlaylistInfo;
import com.github.kiulian.downloader.downloader.request.RequestSubtitlesInfo;
import com.github.kiulian.downloader.downloader.request.RequestVideoInfo;
import com.github.kiulian.downloader.downloader.response.Response;
import com.github.kiulian.downloader.model.playlist.PlaylistInfo;
import com.github.kiulian.downloader.model.subtitles.SubtitlesInfo;
import com.github.kiulian.downloader.model.videos.VideoInfo;
import java.util.List;

/* loaded from: classes-dex2jar.jar:com/github/kiulian/downloader/parser/Parser.class */
public interface Parser {
    Response<PlaylistInfo> parseChannelsUploads(RequestChannelUploads requestChannelUploads);

    Response<PlaylistInfo> parsePlaylist(RequestPlaylistInfo requestPlaylistInfo);

    Response<List<SubtitlesInfo>> parseSubtitlesInfo(RequestSubtitlesInfo requestSubtitlesInfo);

    Response<VideoInfo> parseVideo(RequestVideoInfo requestVideoInfo);
}
