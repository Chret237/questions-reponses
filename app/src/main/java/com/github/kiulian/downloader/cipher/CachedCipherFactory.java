package com.github.kiulian.downloader.cipher;

import com.github.clans.fab.BuildConfig;
import com.github.kiulian.downloader.YoutubeException;
import com.github.kiulian.downloader.downloader.Downloader;
import com.github.kiulian.downloader.downloader.request.RequestWebpage;
import com.github.kiulian.downloader.downloader.response.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes-dex2jar.jar:com/github/kiulian/downloader/cipher/CachedCipherFactory.class */
public class CachedCipherFactory implements CipherFactory {
    private static final String FUNCTION_REVERSE_PATTERN = "\\{\\w\\.reverse\\(\\)\\}";
    private static final String FUNCTION_SPLICE_PATTERN = "\\{\\w\\.splice\\(0,\\w\\)\\}";
    private static final String FUNCTION_SWAP1_PATTERN = "\\{var\\s\\w=\\w\\[0];\\w\\[0]=\\w\\[\\w%\\w.length];\\w\\[\\w]=\\w\\}";
    private static final String FUNCTION_SWAP2_PATTERN = "\\{var\\s\\w=\\w\\[0];\\w\\[0]=\\w\\[\\w%\\w.length];\\w\\[\\w%\\w.length]=\\w\\}";
    private static final String FUNCTION_SWAP3_PATTERN = "function\\(\\w+,\\w+\\)\\{var\\s\\w=\\w\\[0];\\w\\[0]=\\w\\[\\w%\\w.length];\\w\\[\\w%\\w.length]=\\w\\}";
    private static final String[] INITIAL_FUNCTION_PATTERNS = {"\\b[cs]\\s*&&\\s*[adf]\\.set\\([^,]+\\s*,\\s*encodeURIComponent\\s*\\(\\s*([a-zA-Z0-9$]+)\\(", "\\b[a-zA-Z0-9]+\\s*&&\\s*[a-zA-Z0-9]+\\.set\\([^,]+\\s*,\\s*encodeURIComponent\\s*\\(\\s*([a-zA-Z0-9$]+)\\(", "(?:\\b|[^a-zA-Z0-9$])([a-zA-Z0-9$]{2})\\s*=\\s*function\\(\\s*a\\s*\\)\\s*\\{\\s*a\\s*=\\s*a\\.split\\(\\s*\"\"\\s*\\)", "([a-zA-Z0-9$]+)\\s*=\\s*function\\(\\s*a\\s*\\)\\s*\\{\\s*a\\s*=\\s*a\\.split\\(\\s*\"\"\\s*\\)", "([\"'])signature\\1\\s*,\\s*([a-zA-Z0-9$]+)\\(", "\\.sig\\|\\|([a-zA-Z0-9$]+)\\(", "yt\\.akamaized\\.net/\\)\\s*\\|\\|\\s*.*?\\s*[cs]\\s*&&\\s*[adf]\\.set\\([^,]+\\s*,\\s*(?:encodeURIComponent\\s*\\()?\\s*()$", "\\b[cs]\\s*&&\\s*[adf]\\.set\\([^,]+\\s*,\\s*([a-zA-Z0-9$]+)\\(", "\\b[a-zA-Z0-9]+\\s*&&\\s*[a-zA-Z0-9]+\\.set\\([^,]+\\s*,\\s*([a-zA-Z0-9$]+)\\(", "\\bc\\s*&&\\s*a\\.set\\([^,]+\\s*,\\s*\\([^)]*\\)\\s*\\(\\s*([a-zA-Z0-9$]+)\\(", "\\bc\\s*&&\\s*[a-zA-Z0-9]+\\.set\\([^,]+\\s*,\\s*\\([^)]*\\)\\s*\\(\\s*([a-zA-Z0-9$]+)\\("};
    private static final Pattern[] JS_FUNCTION_PATTERNS = {Pattern.compile("\\w+\\.(\\w+)\\(\\w,(\\d+)\\)"), Pattern.compile("\\w+\\[(\\\"\\w+\\\")\\]\\(\\w,(\\d+)\\)")};
    private Downloader downloader;
    private List<Pattern> knownInitialFunctionPatterns = new ArrayList();
    private Map<Pattern, CipherFunction> functionsEquivalentMap = new HashMap();
    private Map<String, Cipher> ciphers = new HashMap();

    public CachedCipherFactory(Downloader downloader) {
        this.downloader = downloader;
        for (String str : INITIAL_FUNCTION_PATTERNS) {
            addInitialFunctionPattern(this.knownInitialFunctionPatterns.size(), str);
        }
        addFunctionEquivalent(FUNCTION_REVERSE_PATTERN, new ReverseFunction());
        addFunctionEquivalent(FUNCTION_SPLICE_PATTERN, new SpliceFunction());
        addFunctionEquivalent(FUNCTION_SWAP1_PATTERN, new SwapFunctionV1());
        SwapFunctionV2 swapFunctionV2 = new SwapFunctionV2();
        addFunctionEquivalent(FUNCTION_SWAP2_PATTERN, swapFunctionV2);
        addFunctionEquivalent(FUNCTION_SWAP3_PATTERN, swapFunctionV2);
    }

    private String getInitialFunctionName(String str) throws YoutubeException {
        Iterator<Pattern> it = this.knownInitialFunctionPatterns.iterator();
        while (it.hasNext()) {
            Matcher matcher = it.next().matcher(str);
            if (matcher.find()) {
                return matcher.group(1);
            }
        }
        throw new YoutubeException.CipherException("Initial function name not found");
    }

    private List<JsFunction> getTransformFunctions(String str) throws YoutubeException {
        Matcher matcher = Pattern.compile(Pattern.quote(getInitialFunctionName(str).replaceAll("[^$A-Za-z0-9_]", BuildConfig.FLAVOR)) + "=function\\(\\w\\)\\{[a-z=\\.\\(\\\"\\)]*;(.*);(?:.+)\\}").matcher(str);
        if (!matcher.find()) {
            throw new YoutubeException.CipherException("Transformation functions not found");
        }
        String[] strArrSplit = matcher.group(1).split(";");
        ArrayList arrayList = new ArrayList(strArrSplit.length);
        for (String str2 : strArrSplit) {
            arrayList.add(parseFunction(str2));
        }
        return arrayList;
    }

    private Map<String, CipherFunction> getTransformFunctionsMap(String[] strArr) throws YoutubeException {
        HashMap map = new HashMap();
        for (String str : strArr) {
            String[] strArrSplit = str.split(":", 2);
            map.put(strArrSplit[0], mapFunction(strArrSplit[1]));
        }
        return map;
    }

    private String[] getTransformObject(String str, String str2) throws YoutubeException {
        Matcher matcher = Pattern.compile(String.format("var %s=\\{(.*?)\\};", Pattern.quote(str.replaceAll("[^$A-Za-z0-9_]", BuildConfig.FLAVOR))), 32).matcher(str2);
        if (matcher.find()) {
            return matcher.group(1).replaceAll("\n", " ").split(", ");
        }
        throw new YoutubeException.CipherException("Transform object not found");
    }

    private CipherFunction mapFunction(String str) throws YoutubeException {
        for (Map.Entry<Pattern, CipherFunction> entry : this.functionsEquivalentMap.entrySet()) {
            if (entry.getKey().matcher(str).find()) {
                return entry.getValue();
            }
        }
        throw new YoutubeException.CipherException("Map function not found");
    }

    private JsFunction parseFunction(String str) throws YoutubeException {
        String str2;
        for (Pattern pattern : JS_FUNCTION_PATTERNS) {
            Matcher matcher = pattern.matcher(str);
            if (matcher.find()) {
                String[] strArrSplit = str.split("\\.");
                if (strArrSplit.length > 1) {
                    str2 = strArrSplit[0];
                } else {
                    String[] strArrSplit2 = str.split("\\[");
                    if (strArrSplit2.length > 1) {
                        str2 = strArrSplit2[0];
                    }
                }
                return new JsFunction(str2, matcher.group(1), matcher.group(2));
            }
        }
        throw new YoutubeException.CipherException("Could not parse js function");
    }

    @Override // com.github.kiulian.downloader.cipher.CipherFactory
    public void addFunctionEquivalent(String str, CipherFunction cipherFunction) {
        this.functionsEquivalentMap.put(Pattern.compile(str), cipherFunction);
    }

    @Override // com.github.kiulian.downloader.cipher.CipherFactory
    public void addInitialFunctionPattern(int i, String str) {
        this.knownInitialFunctionPatterns.add(i, Pattern.compile(str));
    }

    public void clearCache() {
        this.ciphers.clear();
    }

    @Override // com.github.kiulian.downloader.cipher.CipherFactory
    public Cipher createCipher(String str) throws YoutubeException {
        Cipher cipher = this.ciphers.get(str);
        Cipher defaultCipher = cipher;
        if (cipher == null) {
            Response<String> responseDownloadWebpage = this.downloader.downloadWebpage(new RequestWebpage(str));
            if (!responseDownloadWebpage.mo13ok()) {
                throw new YoutubeException.DownloadException(String.format("Could not load url: %s, exception: %s", str, responseDownloadWebpage.error().getMessage()));
            }
            String strData = responseDownloadWebpage.data();
            List<JsFunction> transformFunctions = getTransformFunctions(strData);
            defaultCipher = new DefaultCipher(transformFunctions, getTransformFunctionsMap(getTransformObject(transformFunctions.get(0).getVar(), strData)));
            this.ciphers.put(str, defaultCipher);
        }
        return defaultCipher;
    }
}
