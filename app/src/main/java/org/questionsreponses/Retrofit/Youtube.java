package org.questionsreponses.Retrofit;

import com.google.gson.annotations.SerializedName;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/Retrofit/Youtube.class */
public class Youtube {

    @SerializedName("download_url")
    private String download_url;

    @SerializedName("expires")
    private String expires;

    @SerializedName("extension")
    private String extension;

    @SerializedName("filename")
    private String filename;

    /* renamed from: id */
    @SerializedName("id")
    private String f95id;

    /* renamed from: ip */
    @SerializedName("ip")
    private int f96ip;

    @SerializedName("ipbits")
    private int ipbits;

    @SerializedName("itag")
    private int itag;

    @SerializedName("quality")
    private String quality;

    @SerializedName("resolution")
    private String resolution;

    @SerializedName("size")
    private String size;

    @SerializedName("title")
    private String title;

    @SerializedName("type")
    private String type;

    @SerializedName("url")
    private String url;

    @SerializedName("video_id")
    private String video_id;

    @SerializedName("view_count")
    private int view_count;

    public String getDownload_url() {
        return this.download_url;
    }

    public String getExpires() {
        return this.expires;
    }

    public String getExtension() {
        return this.extension;
    }

    public String getFilename() {
        return this.filename;
    }

    public String getId() {
        return this.f95id;
    }

    public int getIp() {
        return this.f96ip;
    }

    public int getIpbits() {
        return this.ipbits;
    }

    public int getItag() {
        return this.itag;
    }

    public String getQuality() {
        return this.quality;
    }

    public String getResolution() {
        return this.resolution;
    }

    public String getSize() {
        return this.size;
    }

    public String getTitle() {
        return this.title;
    }

    public String getType() {
        return this.type;
    }

    public String getUrl() {
        return this.url;
    }

    public String getVideo_id() {
        return this.video_id;
    }

    public int getView_count() {
        return this.view_count;
    }

    public void setDownload_url(String str) {
        this.download_url = str;
    }

    public void setExpires(String str) {
        this.expires = str;
    }

    public void setExtension(String str) {
        this.extension = str;
    }

    public void setFilename(String str) {
        this.filename = str;
    }

    public void setId(String str) {
        this.f95id = str;
    }

    public void setIp(int i) {
        this.f96ip = i;
    }

    public void setIpbits(int i) {
        this.ipbits = i;
    }

    public void setItag(int i) {
        this.itag = i;
    }

    public void setQuality(String str) {
        this.quality = str;
    }

    public void setResolution(String str) {
        this.resolution = str;
    }

    public void setSize(String str) {
        this.size = str;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public void setType(String str) {
        this.type = str;
    }

    public void setUrl(String str) {
        this.url = str;
    }

    public void setVideo_id(String str) {
        this.video_id = str;
    }

    public void setView_count(int i) {
        this.view_count = i;
    }

    public String toString() {
        return RetrofitData.createGsonObject().toJson(this);
    }
}
