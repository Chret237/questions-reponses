package org.questionsreponses.Model;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/Model/Survey.class */
public class Survey {
    private String categorie;
    private String clef_groupe;
    private String date;
    private boolean enable;
    private String explication;

    /* renamed from: id */
    private int f88id;
    private int level;
    private int minScoreToUnlocked;
    private String proposition_1;
    private String proposition_2;
    private String proposition_3;
    private String question;
    private String reponse;
    private String reponse_choisie;
    private String titre_quizz;
    private int total_erreur;
    private int total_question;
    private int total_trouve;

    public Survey() {
    }

    public Survey(int i, String str, int i2, int i3, int i4, String str2, String str3) {
        this.f88id = i;
        this.clef_groupe = str;
        this.total_question = i2;
        this.total_trouve = i3;
        this.total_erreur = i4;
        this.titre_quizz = str2;
        this.date = str3;
    }

    public Survey(int i, String str, int i2, int i3, int i4, String str2, String str3, boolean z, int i5, int i6) {
        this.f88id = i;
        this.clef_groupe = str;
        this.total_question = i2;
        this.total_trouve = i3;
        this.total_erreur = i4;
        this.titre_quizz = str2;
        this.date = str3;
        this.enable = z;
        this.minScoreToUnlocked = i5;
        this.level = i6;
    }

    public Survey(int i, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, int i2, int i3, int i4, String str10, String str11) {
        this.f88id = i;
        this.question = str;
        this.categorie = str2;
        this.explication = str3;
        this.proposition_1 = str4;
        this.proposition_2 = str5;
        this.proposition_3 = str6;
        this.reponse = str7;
        this.reponse_choisie = str8;
        this.clef_groupe = str9;
        this.total_question = i2;
        this.total_trouve = i3;
        this.total_erreur = i4;
        this.titre_quizz = str10;
        this.date = str11;
    }

    public String getCategorie() {
        return this.categorie;
    }

    public String getClef_groupe() {
        return this.clef_groupe;
    }

    public String getDate() {
        return this.date;
    }

    public boolean getEnable() {
        return this.enable;
    }

    public String getExplication() {
        return this.explication;
    }

    public int getId() {
        return this.f88id;
    }

    public int getLevel() {
        return this.level;
    }

    public int getMinScoreToUnlocked() {
        return this.minScoreToUnlocked;
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

    public String getReponse_choisie() {
        return this.reponse_choisie;
    }

    public String getTitre_quizz() {
        return this.titre_quizz;
    }

    public int getTotal_erreur() {
        return this.total_erreur;
    }

    public int getTotal_question() {
        return this.total_question;
    }

    public int getTotal_trouve() {
        return this.total_trouve;
    }

    public void setCategorie(String str) {
        this.categorie = str;
    }

    public void setClef_groupe(String str) {
        this.clef_groupe = str;
    }

    public void setDate(String str) {
        this.date = str;
    }

    public void setEnable(boolean z) {
        this.enable = z;
    }

    public void setExplication(String str) {
        this.explication = str;
    }

    public void setId(int i) {
        this.f88id = i;
    }

    public void setLevel(int i) {
        this.level = i;
    }

    public void setMinScoreToUnlocked(int i) {
        this.minScoreToUnlocked = i;
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

    public void setReponse_choisie(String str) {
        this.reponse_choisie = str;
    }

    public void setTitre_quizz(String str) {
        this.titre_quizz = str;
    }

    public void setTotal_erreur(int i) {
        this.total_erreur = i;
    }

    public void setTotal_question(int i) {
        this.total_question = i;
    }

    public void setTotal_trouve(int i) {
        this.total_trouve = i;
    }

    public String toString() {
        return "{\"question\":\"" + this.question + "\",\"categorie\":\"" + this.categorie + "\",\"explication\":\"" + this.explication + "\",\"proposition_1\":\"" + this.proposition_1 + "\",\"proposition_2\":\"" + this.proposition_2 + "\",\"proposition_3\":\"" + this.proposition_3 + "\",\"reponse\":\"" + this.reponse + "\",\"reponse_choisie\":\"" + this.reponse_choisie + "\",\"clef_groupe\":\"" + this.clef_groupe + "\",\"total_question\":\"" + this.total_question + "\",\"total_trouve\":\"" + this.total_trouve + "\",\"total_erreur\":\"" + this.total_erreur + "\",\"titre_quizz\":\"" + this.titre_quizz + "\",\"date\":\"" + this.date + "\",\"enable\":\"" + this.enable + "\",\"minScoreToUnlocked\":\"" + this.minScoreToUnlocked + "\",\"level\":\"" + this.level + "\",\"id\":" + this.f88id + "}";
    }
}
