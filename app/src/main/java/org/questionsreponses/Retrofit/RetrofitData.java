package org.questionsreponses.Retrofit;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.questionsreponses.C0598R;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/Retrofit/RetrofitData.class */
public class RetrofitData {
    public static Gson createGsonObject() {
        return new GsonBuilder().serializeNulls().create();
    }

    public static Retrofit getRetrofitInstance(Context context) {
        return new Retrofit.Builder().baseUrl(context.getString(C0598R.string.lb_lien_webservice)).addConverterFactory(GsonConverterFactory.create()).build();
    }

    public static Retrofit getRetrofitInstance(String str) {
        return new Retrofit.Builder().baseUrl(str).addConverterFactory(GsonConverterFactory.create()).build();
    }
}
