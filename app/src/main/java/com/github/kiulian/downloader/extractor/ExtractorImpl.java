package com.github.kiulian.downloader.extractor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.clans.fab.BuildConfig;
import com.github.kiulian.downloader.YoutubeException;
import com.github.kiulian.downloader.downloader.Downloader;
import com.github.kiulian.downloader.downloader.request.RequestWebpage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes-dex2jar.jar:com/github/kiulian/downloader/extractor/ExtractorImpl.class */
public class ExtractorImpl implements Extractor {
    private static final String DEFAULT_CLIENT_VERSION = "2.20200720.00.02";
    private final Downloader downloader;
    private static final List<Pattern> YT_PLAYER_CONFIG_PATTERNS = Arrays.asList(Pattern.compile(";ytplayer\\.config = (\\{.*?\\})\\;ytplayer"), Pattern.compile(";ytplayer\\.config = (\\{.*?\\})\\;"), Pattern.compile("ytInitialPlayerResponse\\s*=\\s*(\\{.+?\\})\\;var meta"));
    private static final List<Pattern> YT_INITIAL_DATA_PATTERNS = Arrays.asList(Pattern.compile("window\\[\"ytInitialData\"\\] = (\\{.*?\\});"), Pattern.compile("ytInitialData = (\\{.*?\\});"));
    private static final Pattern SUBTITLES_LANG_CODE_PATTERN = Pattern.compile("lang_code=\"(.{2,3})\"");
    private static final Pattern TEXT_NUMBER_REGEX = Pattern.compile("[0-9]+[0-9, ']*");
    private static final Pattern ASSETS_JS_REGEX = Pattern.compile("\"assets\":.+?\"js\":\\s*\"([^\"]+)\"");
    private static final Pattern EMB_JS_REGEX = Pattern.compile("\"jsUrl\":\\s*\"([^\"]+)\"");

    public ExtractorImpl(Downloader downloader) {
        this.downloader = downloader;
    }

    @Override // com.github.kiulian.downloader.extractor.Extractor
    public String extractClientVersionFromContext(JSONObject jSONObject) {
        JSONArray jSONArray = jSONObject.getJSONArray("serviceTrackingParams");
        if (jSONArray == null) {
            return DEFAULT_CLIENT_VERSION;
        }
        for (int i = 0; i < jSONArray.size(); i++) {
            JSONArray jSONArray2 = jSONArray.getJSONObject(i).getJSONArray("params");
            for (int i2 = 0; i2 < jSONArray2.size(); i2++) {
                if (jSONArray2.getJSONObject(i2).getString("key").equals("cver")) {
                    return jSONArray2.getJSONObject(i2).getString("value");
                }
            }
        }
        return DEFAULT_CLIENT_VERSION;
    }

    @Override // com.github.kiulian.downloader.extractor.Extractor
    public JSONObject extractInitialDataFromHtml(String str) throws YoutubeException {
        Iterator<Pattern> it = YT_INITIAL_DATA_PATTERNS.iterator();
        String strGroup = null;
        while (it.hasNext()) {
            Matcher matcher = it.next().matcher(str);
            if (matcher.find()) {
                strGroup = matcher.group(1);
            }
        }
        if (strGroup == null) {
            throw new YoutubeException.BadPageException("Could not find initial data on web page");
        }
        try {
            return JSON.parseObject(strGroup);
        } catch (Exception e) {
            throw new YoutubeException.BadPageException("Initial data contains invalid json");
        }
    }

    @Override // com.github.kiulian.downloader.extractor.Extractor
    public int extractIntegerFromText(String str) {
        Matcher matcher = TEXT_NUMBER_REGEX.matcher(str);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(0).replaceAll("[, ']", BuildConfig.FLAVOR));
        }
        return 0;
    }

    @Override // com.github.kiulian.downloader.extractor.Extractor
    public String extractJsUrlFromConfig(JSONObject jSONObject, String str) throws YoutubeException {
        String strReplace;
        if (jSONObject.containsKey("assets")) {
            strReplace = jSONObject.getJSONObject("assets").getString("js");
        } else {
            String strData = this.downloader.downloadWebpage(new RequestWebpage("https://www.youtube.com/embed/" + str)).data();
            Matcher matcher = ASSETS_JS_REGEX.matcher(strData);
            if (matcher.find()) {
                strReplace = matcher.group(1).replace("\\", BuildConfig.FLAVOR);
            } else {
                Matcher matcher2 = EMB_JS_REGEX.matcher(strData);
                strReplace = matcher2.find() ? matcher2.group(1).replace("\\", BuildConfig.FLAVOR) : null;
            }
        }
        if (strReplace == null) {
            throw new YoutubeException.BadPageException("Could not extract js url: assets not found");
        }
        return "https://youtube.com" + strReplace;
    }

    @Override // com.github.kiulian.downloader.extractor.Extractor
    public JSONObject extractPlayerConfigFromHtml(String str) throws YoutubeException {
        String strGroup;
        Iterator<Pattern> it = YT_PLAYER_CONFIG_PATTERNS.iterator();
        while (true) {
            if (!it.hasNext()) {
                strGroup = null;
                break;
            }
            Matcher matcher = it.next().matcher(str);
            if (matcher.find()) {
                strGroup = matcher.group(1);
                break;
            }
        }
        if (strGroup == null) {
            throw new YoutubeException.BadPageException("Could not find player config on web page");
        }
        try {
            JSONObject object = JSON.parseObject(strGroup);
            return object.containsKey("args") ? object : new JSONObject().fluentPut("args", new JSONObject().fluentPut("player_response", object));
        } catch (Exception e) {
            throw new YoutubeException.BadPageException("Player config contains invalid json");
        }
    }

    @Override // com.github.kiulian.downloader.extractor.Extractor
    public List<String> extractSubtitlesLanguagesFromXml(String str) throws YoutubeException {
        Matcher matcher = SUBTITLES_LANG_CODE_PATTERN.matcher(str);
        if (!matcher.find()) {
            throw new YoutubeException.BadPageException("Could not find any language code in subtitles xml");
        }
        ArrayList arrayList = new ArrayList();
        do {
            arrayList.add(matcher.group(1));
        } while (matcher.find());
        return arrayList;
    }
}
