package org.questionsreponses.youtube;

import java.util.ArrayList;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/youtube/YTDownloader.class */
public class YTDownloader {
    private String description;
    private String download_url;
    private String expires;
    private String extension;
    private String filename;

    /* renamed from: id */
    private String f97id;

    /* renamed from: ip */
    private String f98ip;
    private String ipbits;
    private String itag;
    private String mimeType;
    private String quality;
    private String qualityLabel;
    private String resolution;
    private String size;
    private ArrayList<String> thumbnails;
    private String title;
    private String type;
    private String url;
    private String videoId;
    private String view_count;

    public YTDownloader(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12, String str13, String str14, String str15, String str16, String str17, String str18, String str19, ArrayList<String> arrayList) {
        this.f97id = str;
        this.videoId = str2;
        this.itag = str3;
        this.quality = str4;
        this.qualityLabel = str5;
        this.type = str6;
        this.extension = str7;
        this.resolution = str8;
        this.url = str9;
        this.download_url = str10;
        this.expires = str11;
        this.ipbits = str12;
        this.f98ip = str13;
        this.view_count = str14;
        this.title = str15;
        this.size = str16;
        this.filename = str17;
        this.description = str18;
        this.mimeType = str19;
        this.thumbnails = arrayList;
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
        return this.f97id;
    }

    public String getIp() {
        return this.f98ip;
    }

    public String getIpbits() {
        return this.ipbits;
    }

    public String getItag() {
        return this.itag;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public String getQuality() {
        return this.quality;
    }

    public String getQualityLabel() {
        return this.qualityLabel;
    }

    public String getResolution() {
        return this.resolution;
    }

    public String getSize() {
        return this.size;
    }

    public ArrayList<String> getThumbnails() {
        return this.thumbnails;
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
        this.f97id = str;
    }

    public void setIp(String str) {
        this.f98ip = str;
    }

    public void setIpbits(String str) {
        this.ipbits = str;
    }

    public void setItag(String str) {
        this.itag = str;
    }

    public void setMimeType(String str) {
        this.mimeType = str;
    }

    public void setQuality(String str) {
        this.quality = str;
    }

    public void setQualityLabel(String str) {
        this.qualityLabel = str;
    }

    public void setResolution(String str) {
        this.resolution = str;
    }

    public void setSize(String str) {
        this.size = str;
    }

    public void setThumbnails(ArrayList<String> arrayList) {
        this.thumbnails = arrayList;
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
}
