package org.questionsreponses.Model;

import com.google.gson.annotations.SerializedName;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/Model/Quizz.class */
public class Quizz {

    @SerializedName("categorie")
    private String categorie;

    @SerializedName("explication")
    private String explication;

    /* renamed from: id */
    @SerializedName("id")
    private int f86id;

    @SerializedName("choix1")
    private String proposition_1;

    @SerializedName("choix2")
    private String proposition_2;

    @SerializedName("choix3")
    private String proposition_3;

    @SerializedName("question")
    private String question;

    @SerializedName("choixcorrecte")
    private String reponse;

    public Quizz() {
    }

    public Quizz(int i, String str, String str2, String str3, String str4, String str5, String str6, String str7) {
        this.f86id = i;
        this.question = str;
        this.categorie = str2;
        this.explication = str3;
        this.proposition_1 = str4;
        this.proposition_2 = str5;
        this.proposition_3 = str6;
        this.reponse = str7;
    }

    public String getCategorie() {
        return this.categorie;
    }

    public String getExplication() {
        return this.explication;
    }

    public int getId() {
        return this.f86id;
    }

    public String getProposition_1() {
        return this.proposition_1;
    }

    public String getProposition_2() {
        return this.proposition_2;
    }

    public String getProposition_3() {
        return this.proposition_3;
    }

    public String getQuestion() {
        return this.question;
    }

    public String getReponse() {
        return this.reponse;
    }

    public void setCategorie(String str) {
        this.categorie = str;
    }

    public void setExplication(String str) {
        this.explication = str;
    }

    public void setId(int i) {
        this.f86id = i;
    }

    public void setProposition_1(String str) {
        this.proposition_1 = str;
    }

    public void setProposition_2(String str) {
        this.proposition_2 = str;
    }

    public void setProposition_3(String str) {
        this.proposition_3 = str;
    }

    public void setQuestion(String str) {
        this.question = str;
    }

    public void setReponse(String str) {
        this.reponse = str;
    }

    public String toString() {
        return "{\"question\":\"" + this.question + "\",\"categorie\":\"" + this.categorie + "\",\"explication\":\"" + this.explication + "\",\"choix1\":\"" + this.proposition_1 + "\",\"choix2\":\"" + this.proposition_2 + "\",\"choix3\":\"" + this.proposition_3 + "\",\"choixcorrecte\":\"" + this.reponse + "\",\"id\":" + this.f86id + "}";
    }
}
