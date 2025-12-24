package org.questionsreponses.Model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/Model/Ressource.class */
public class Ressource implements Serializable {

    @SerializedName("auteur")
    private String auteur;

    @SerializedName("date")
    private String date;

    @SerializedName("duree")
    private String duree;

    @SerializedName("etat")
    private boolean etat;

    /* renamed from: id */
    @SerializedName("id")
    private int f87id;

    @SerializedName("src")
    private String src;

    @SerializedName("titre")
    private String titre;

    @SerializedName("type_libelle")
    private String type_libelle;
    private String type_ressource;

    @SerializedName("type_shortcode")
    private String type_shortcode;

    @SerializedName("urlacces")
    private String urlacces;

    public Ressource() {
    }

    public Ressource(int i, String str, boolean z, String str2, String str3, String str4, String str5, String str6, String str7) {
        this.f87id = i;
        this.titre = str;
        this.etat = z;
        this.src = str2;
        this.date = str3;
        this.auteur = str4;
        this.urlacces = str5;
        this.duree = str6;
        this.type_ressource = str7;
    }

    public String getAuteur() {
        return this.auteur;
    }

    public String getDate() {
        return this.date;
    }

    public String getDuree() {
        return this.duree;
    }

    public boolean getEtat() {
        return this.etat;
    }

    public int getId() {
        return this.f87id;
    }

    public String getSrc() {
        return this.src;
    }

    public String getTitre() {
        return this.titre;
    }

    public String getType_libelle() {
        return this.type_libelle;
    }

    public String getType_ressource() {
        return this.type_ressource;
    }

    public String getType_shortcode() {
        return this.type_shortcode;
    }

    public String getUrlacces() {
        return this.urlacces;
    }

    public void setAuteur(String str) {
        this.auteur = str;
    }

    public void setDate(String str) {
        this.date = str;
    }

    public void setDuree(String str) {
        this.duree = str;
    }

    public void setEtat(boolean z) {
        this.etat = z;
    }

    public void setId(int i) {
        this.f87id = i;
    }

    public void setSrc(String str) {
        this.src = str;
    }

    public void setTitre(String str) {
        this.titre = str;
    }

    public void setType_libelle(String str) {
        this.type_libelle = str;
    }

    public void setType_ressource(String str) {
        this.type_ressource = str;
    }

    public void setType_shortcode(String str) {
        this.type_shortcode = str;
    }

    public void setUrlacces(String str) {
        this.urlacces = str;
    }

    public String toString() {
        return "{\"titre\":\"" + this.titre + "\",\"etat\":\"" + this.etat + "\",\"src\":\"" + this.src + "\",\"date\":\"" + this.date + "\",\"type_libelle\":\"" + this.type_libelle + "\",\"type_shortcode\":\"" + this.type_shortcode + "\",\"auteur\":\"" + this.auteur + "\",\"urlacces\":\"" + this.urlacces + "\",\"duree\":\"" + this.duree + "\",\"id\":" + this.f87id + "}";
    }
}
