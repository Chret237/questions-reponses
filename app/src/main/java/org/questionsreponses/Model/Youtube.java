package org.questionsreponses.Model;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/Model/Youtube.class */
public class Youtube {
    private String description;
    private String download_url;
    private String expires;
    private String extension;
    private String filename;

    /* renamed from: id */
    private String f89id;

    /* renamed from: ip */
    private String f90ip;
    private String ipbits;
    private String itag;
    private String quality;
    private String resolution;
    private String size;
    private String title;
    private String type;
    private String url;
    private String videoId;
    private String view_count;

    public Youtube(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12, String str13, String str14, String str15) {
        this.videoId = str;
        this.itag = str2;
        this.quality = str3;
        this.type = str4;
        this.extension = str5;
        this.resolution = str6;
        this.url = str7;
        this.download_url = str8;
        this.expires = str9;
        this.ipbits = str10;
        this.f90ip = str11;
        this.view_count = str12;
        this.title = str13;
        this.size = str14;
        this.filename = str15;
    }

    public String getDescription() {
        return this.description;
    }

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
        return this.f89id;
    }

    public String getIp() {
        return this.f90ip;
    }

    public String getIpbits() {
        return this.ipbits;
    }

    public String getItag() {
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

    public String getVideoId() {
        return this.videoId;
    }

    public String getView_count() {
        return this.view_count;
    }

    public void setDescription(String str) {
        this.description = str;
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
        this.f89id = str;
    }

    public void setIp(String str) {
        this.f90ip = str;
    }

    public void setIpbits(String str) {
        this.ipbits = str;
    }

    public void setItag(String str) {
        this.itag = str;
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

    public void setVideoId(String str) {
        this.videoId = str;
    }

    public void setView_count(String str) {
        this.view_count = str;
    }

    public String toString() {
        return this.resolution + " (" + this.extension + "/" + this.size + ")";
    }
}
