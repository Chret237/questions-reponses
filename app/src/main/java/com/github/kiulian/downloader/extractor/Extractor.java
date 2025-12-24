package com.github.kiulian.downloader.extractor;

import com.alibaba.fastjson.JSONObject;
import com.github.kiulian.downloader.YoutubeException;
import java.util.List;

/* loaded from: classes-dex2jar.jar:com/github/kiulian/downloader/extractor/Extractor.class */
public interface Extractor {
    String extractClientVersionFromContext(JSONObject jSONObject);

    JSONObject extractInitialDataFromHtml(String str) throws YoutubeException;

    int extractIntegerFromText(String str);

    String extractJsUrlFromConfig(JSONObject jSONObject, String str) throws YoutubeException;

    JSONObject extractPlayerConfigFromHtml(String str) throws YoutubeException;

    List<String> extractSubtitlesLanguagesFromXml(String str) throws YoutubeException;
}
