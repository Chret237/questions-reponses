package org.questionsreponses.Model;

import com.google.gson.annotations.SerializedName;
import org.questionsreponses.Retrofit.RetrofitData;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/Model/JsonReturn.class */
public class JsonReturn {

    @SerializedName("code_message")
    private String code_message;

    @SerializedName("code_retour")
    private String code_retour;

    public JsonReturn(String str, String str2) {
        this.code_retour = str;
        this.code_message = str2;
    }

    public String getCode_message() {
        return this.code_message;
    }

    public String getCode_retour() {
        return this.code_retour;
    }

    public String toString() {
        return RetrofitData.createGsonObject().toJson(this);
    }
}
