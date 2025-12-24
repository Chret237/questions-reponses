package org.questionsreponses.Retrofit;

import android.support.v4.app.NotificationCompat;
import org.questionsreponses.Model.JsonReturn;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/Retrofit/RetrofitView.class */
public class RetrofitView {

    /* loaded from: classes-dex2jar.jar:org/questionsreponses/Retrofit/RetrofitView$QuestionReponseApi.class */
    public interface QuestionReponseApi {
        @FormUrlEncoded
        @POST("webservice/contact/laisser-un-message/")
        Call<JsonReturn> sendContactData(@Field("civilite") String str, @Field("nom") String str2, @Field(NotificationCompat.CATEGORY_EMAIL) String str3, @Field("ville") String str4, @Field("detail") String str5);
    }
}
