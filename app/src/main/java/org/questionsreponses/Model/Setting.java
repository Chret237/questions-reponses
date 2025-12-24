package org.questionsreponses.Model;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/Model/Setting.class */
public class Setting {
    private boolean choice;
    private String libelle;
    private String title;
    private int total;

    public Setting(String str, String str2, boolean z, int i) {
        this.title = str;
        this.libelle = str2;
        this.choice = z;
        this.total = i;
    }

    public boolean getChoice() {
        return this.choice;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public String getTitle() {
        return this.title;
    }

    public int getTotal() {
        return this.total;
    }

    public void setChoice(boolean z) {
        this.choice = z;
    }

    public void setLibelle(String str) {
        this.libelle = str;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public void setTotal(int i) {
        this.total = i;
    }
}
