package org.questionsreponses.Model;

import android.content.Context;
import android.content.res.Resources;
import org.questionsreponses.C0598R;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/Model/ApiClient.class */
public class ApiClient {
    public static Retrofit retrofitLVE;
    public static Retrofit retrofitQR;

    public static Retrofit getApiClientLeVraiEvangile(Context context) throws Resources.NotFoundException {
        String string = context.getResources().getString(C0598R.string.url_site_web);
        if (retrofitLVE == null) {
            retrofitLVE = new Retrofit.Builder().baseUrl(string).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofitLVE;
    }

    public static Retrofit getApiClientQuestionsReponses(Context context) throws Resources.NotFoundException {
        String string = context.getResources().getString(C0598R.string.url_qr_site_web);
        if (retrofitQR == null) {
            retrofitQR = new Retrofit.Builder().baseUrl(string).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofitQR;
    }
}
