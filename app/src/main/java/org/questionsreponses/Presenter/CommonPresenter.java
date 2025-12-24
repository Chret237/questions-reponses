package org.questionsreponses.Presenter;

import android.R;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import com.github.clans.fab.BuildConfig;
import com.github.kiulian.downloader.model.videos.formats.Format;
import com.google.gson.Gson;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.questionsreponses.C0598R;
import org.questionsreponses.Model.ApiClient;
import org.questionsreponses.Model.JsonReturn;
import org.questionsreponses.Model.Quizz;
import org.questionsreponses.Model.Ressource;
import org.questionsreponses.Model.SendContactForm;
import org.questionsreponses.Model.Setting;
import org.questionsreponses.Model.Survey;
import org.questionsreponses.Model.Youtube;
import org.questionsreponses.Retrofit.RetrofitData;
import org.questionsreponses.Retrofit.RetrofitView;
import org.questionsreponses.View.Activities.SearchResultActivity;
import org.questionsreponses.View.Adapters.FormatVideoAdapter;
import org.questionsreponses.View.Interfaces.CommonView;
import org.questionsreponses.View.Interfaces.HomeView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/Presenter/CommonPresenter.class */
public class CommonPresenter implements CommonView.ICommonPresenter {
    public static final String KEY_ALL_BIBLE_ID_LIST = "KEY_ALL_BIBLE_ID_LIST";
    public static final String KEY_AUDIO_BOTTOM_NAVIGATION_VIEW = "KEY_AUDIO_BOTTOM_NAVIGATION_VIEW";
    public static final String KEY_AUDIO_RETRIEVE_FROM_LVE_SERVER = "KEY_AUDIO_RETRIEVE_FROM_LVE_SERVER";
    public static final String KEY_GOSPEL_ID_LIST = "KEY_GOSPEL_ID_LIST";
    public static final String KEY_INFO_DOWNLOAD_BY_SHARE = "KEY_INFO_DOWNLOAD_BY_SHARE";
    public static final String KEY_JESUS_TESTAMENT_ID_LIST = "KEY_JESUS_TESTAMENT_ID_LIST";
    public static final String KEY_KETOUVIM_ID_LIST = "KEY_KETOUVIM_ID_LIST";
    public static final String KEY_NEVIIM_ID_LIST = "KEY_NEVIIM_ID_LIST";
    public static final String KEY_NOTIFICATION_IS_PLAYING = "KEY_NOTIFICATION_IS_PLAYING";
    public static final String KEY_NOTIF_AUDIO_PLAYER_LIST = "KEY_NOTIF_AUDIO_PLAYER_LIST";
    public static final String KEY_NOTIF_AUDIO_TO_PLAYER_AUDIO_ID = "KEY_NOTIF_AUDIO_TO_PLAYER_AUDIO_ID";
    public static final String KEY_NOTIF_AUDIO_TO_PLAYER_AUDIO_TIME_ELAPSED = "KEY_NOTIF_AUDIO_TO_PLAYER_AUDIO_TIME_ELAPSED";
    public static final String KEY_NOTIF_PLAYER_PLAY_NEXT = "KEY_NOTIF_PLAYER_PLAY_NEXT";
    public static final String KEY_NOTIF_PLAYER_PREVIOUS = "KEY_NOTIF_PLAYER_PREVIOUS";
    public static final String KEY_NOTIF_PLAYER_SELECTED = "KEY_NOTIF_PLAYER_SELECTED";
    public static final String KEY_PLAYER_AUDIO_TO_NOTIF_AUDIO_TIME_ELAPSED = "KEY_PLAYER_AUDIO_TO_NOTIF_AUDIO_TIME_ELAPSED";
    public static final String KEY_QUIZZ_ACTIVITY_LIST = "KEY_QUIZZ_ACTIVITY_LIST";
    public static final String KEY_QUIZZ_BOTTOM_NAVIGATION_VIEW = "KEY_QUIZZ_BOTTOM_NAVIGATION_VIEW";
    public static final String KEY_QUIZZ_GAME_IS_FINISHED = "KEY_QUIZZ_GAME_IS_FINISHED";
    public static final String KEY_QUIZZ_GAME_IS_NOT_FINISHED = "KEY_QUIZZ_GAME_IS_NOT_FINISHED";
    public static final String KEY_QUIZZ_GAME_LEVEL_DIFFICULTY = "KEY_QUIZZ_GAME_LEVEL_DIFFICULTY";
    public static final String KEY_QUIZZ_GAME_LIST_SELECTED = "KEY_QUIZZ_GAME_LIST_SELECTED";
    public static final String KEY_QUIZZ_GAME_RETURN_DATA = "KEY_QUIZZ_GAME_RETURN_DATA";
    public static final String KEY_QUIZZ_GAME_TITLE_SELECTED = "KEY_QUIZZ_GAME_TITLE_SELECTED";
    public static final String KEY_QUIZZ_GAME_WAS_NOT_FINISHED_BEFORE = "KEY_QUIZZ_GAME_WAS_NOT_FINISHED_BEFORE";
    public static final String KEY_QUIZZ_GAME_WITH_LEVEL_ID_LIST = "KEY_QUIZZ_GAME_WITH_LEVEL_ID_LIST";
    public static final String KEY_QUIZZ_GROUPKEY_SELECTED = "KEY_QUIZZ_GROUPKEY_SELECTED";
    public static final String KEY_QUIZZ_MIN_SCORE_TO_UNLOCK = "KEY_QUIZZ_MIN_SCORE_TO_UNLOCK";
    public static final String KEY_QUIZZ_SPIRITUAL_GROWTH_FAILED = "KEY_QUIZZ_SPIRITUAL_GROWTH_FAILED";
    public static final String KEY_QUIZZ_SPIRITUAL_GROWTH_LEVEL_SELECTED = "KEY_QUIZZ_SPIRITUAL_GROWTH_LEVEL_SELECTED";
    public static final String KEY_QUIZZ_SPIRITUAL_GROWTH_SUCCEEDED = "KEY_QUIZZ_SPIRITUAL_GROWTH_SUCCEEDED";
    public static final String KEY_QUIZZ_TOTAL_ERREUR_SELECTED = "KEY_QUIZZ_TOTAL_ERREUR_SELECTED";
    public static final String KEY_QUIZZ_TOTAL_TROUVE_SELECTED = "KEY_QUIZZ_TOTAL_TROUVE_SELECTED";
    public static final String KEY_SEARCH_FORM_KEY_WORD = "KEY_SEARCH_FORM_KEY_WORD";
    public static final String KEY_SEARCH_FORM_TYPE_RESSOURCE = "KEY_SEARCH_FORM_TYPE_RESSOURCE";
    public static final String KEY_TORAH_ID_LIST = "KEY_TORAH_ID_LIST";
    public static final String KEY_VIDEO_BOTTOM_NAVIGATION_VIEW = "KEY_VIDEO_BOTTOM_NAVIGATION_VIEW";
    public static final String KEY_VIDEO_PLAYER_RETURN_DATA = "KEY_VIDEO_PLAYER_RETURN_DATA";
    public static final String KEY_VIDEO_PLAYER_SEND_DATA = "KEY_VIDEO_PLAYER_SEND_DATA";
    public static final String KEY_VIDEO_RETRIEVE_FROM_LVE_SERVER = "KEY_VIDEO_RETRIEVE_FROM_LVE_SERVER";
    public static final int VALUE_PERMISSION_TO_SAVE_FILE = 123;
    public static final String VALUE_POSITION_AUDIO_SELECTED = "VALUE_POSITION_AUDIO_SELECTED";
    public static final String VALUE_POSITION_VIDEO_SELECTED = "VALUE_POSITION_VIDEO_SELECTED";
    public static final int VALUE_QUIZZ_GAME_SELECTED_REQUEST_CODE = 5;
    public static final int VALUE_QUIZZ_SPIRITUAL_GROWTH_REQUEST_CODE = 15;
    public static final int VALUE_RATING_TOTAL_STAR = 10;
    public static final String VALUE_RETRIEVE_QUIZZ_FROM_HOME_ACTIVITY = "VALUE_RETRIEVE_QUIZZ_FROM_HOME_ACTIVITY";
    public static final String VALUE_RETRIEVE_QUIZZ_FROM_QUIZZ_FRAGMENT = "VALUE_RETRIEVE_QUIZZ_FROM_QUIZZ_FRAGMENT";
    public static final String VALUE_TOTAL_AUDIO_RETRIEVE_FROM_LVE_SERVER = "VALUE_TOTAL_AUDIO_RETRIEVE_FROM_LVE_SERVER";
    public static final String VALUE_TOTAL_VIDEO_RETRIEVE_FROM_LVE_SERVER = "VALUE_TOTAL_AUDIO_RETRIEVE_FROM_LVE_SERVER";
    public static final int VALUE_TOTA_QUIZZ_BY_LEVEL = 20;
    public static final String VALUE_VIDEO_PLAY_NEXT = "VALUE_VIDEO_PLAY_NEXT";
    public static final String VALUE_VIDEO_PLAY_PREVIOUS = "VALUE_VIDEO_PLAY_PREVIOUS";
    public static final int VALUE_VIDEO_SELECTED_REQUEST_CODE = 10;
    private static String actionForm;
    private static String city;
    private static String civility;
    private static final String database = "questionsreponses.db";

    /* renamed from: db */
    private static SQLiteDatabase f91db;
    private static String email;
    private static String message;
    private static String name;
    private static HashMap<String, String> postDataParams;
    private static ProgressBar progressBar;
    private static ArrayList<Quizz> quizzListFromServer;
    private static String searchKeyWord;
    private static String searchTypeRessource;
    private Dialog contactDialogForm;
    private Dialog searchDialogForm;
    private SendContactForm sendContactForm;
    public static final String KEY_SETTING_CONFIRM_BEFORE_QUIT_APP = "KEY_SETTING_CONFIRM_BEFORE_QUIT_APP";
    public static final String KEY_SETTING_NUMBER_OF_QUIZZ = "KEY_SETTING_NUMBER_OF_QUIZZ";
    public static final String KEY_SETTING_CONCATENATE_AUDIO_READING = "KEY_SETTING_CONCATENATE_AUDIO_READING";
    public static final String KEY_SETTING_CONCATENATE_VIDEO_READING = "KEY_SETTING_CONCATENATE_VIDEO_READING";
    public static final String KEY_SETTING_WIFI_EXCLUSIF = "KEY_SETTING_WIFI_EXCLUSIF";
    public static final String KEY_SETTING_OFFLINE_MODE = "KEY_SETTING_OFFLINE_MODE";
    public static final String KEY_SETTING_AUTOMATIC_SOUND_READING = "KEY_SETTING_AUTOMATIC_SOUND_READING";
    public static final String KEY_SETTING_LEVEL_DIFFICULTY = "KEY_SETTING_LEVEL_DIFFICULTY";
    public static final String KEY_SETTING_INITIALIZE_QUIZZ_GAME = "KEY_SETTING_INITIALIZE_QUIZZ_GAME";
    public static final String[] KEY_SETTINGS = {KEY_SETTING_CONFIRM_BEFORE_QUIT_APP, KEY_SETTING_NUMBER_OF_QUIZZ, KEY_SETTING_CONCATENATE_AUDIO_READING, KEY_SETTING_CONCATENATE_VIDEO_READING, KEY_SETTING_WIFI_EXCLUSIF, KEY_SETTING_OFFLINE_MODE, KEY_SETTING_AUTOMATIC_SOUND_READING, KEY_SETTING_LEVEL_DIFFICULTY, KEY_SETTING_INITIALIZE_QUIZZ_GAME};
    private static final String[] FOLDER_NAME = {"/QR", "/QR/Audios/", "/QR/Videos/"};

    public static void buildDataBase(Context context) {
        f91db = context.openOrCreateDatabase(database, 0, null);
    }

    public static void buildTextViewToHtmlData(TextView textView, String str) {
        if (Build.VERSION.SDK_INT >= 24) {
            textView.setText(Html.fromHtml(verseBuilder(str), 0));
        } else {
            textView.setText(Html.fromHtml(verseBuilder(str)));
        }
    }

    public static void cancelCountDownTimer(CountDownTimer countDownTimer) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    public static String changeFormatDate(String str) {
        return str.split("-")[2].trim() + "/" + str.split("-")[1].trim() + "/" + str.split("-")[0].trim();
    }

    public static void createFolder(Context context, String str) {
        File file;
        try {
            if (Build.VERSION.SDK_INT >= 29) {
                file = new File(context.getExternalFilesDir(null).getAbsolutePath() + str);
            } else {
                file = new File(Environment.getExternalStoragePublicDirectory(Environment.getExternalStorageDirectory() + str).getAbsolutePath());
            }
            if (file.exists()) {
                return;
            }
            file.mkdir();
        } catch (Exception e) {
            Log.e("TAG_APP_ERROR", "CommonPresenter-->createFolder() : " + e.getMessage());
        }
    }

    private static ArrayList<String> extractUrls(String str) {
        ArrayList<String> arrayList = new ArrayList<>();
        Matcher matcher = Pattern.compile("((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)", 2).matcher(str);
        while (matcher.find()) {
            arrayList.add(str.substring(matcher.start(0), matcher.end(0)));
        }
        return arrayList;
    }

    public static String extractYoutubeId(String str) throws MalformedURLException {
        Matcher matcher = Pattern.compile("(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200c\u200b2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*").matcher(str);
        if (matcher.find()) {
            str = matcher.group();
        }
        return str;
    }

    public static Activity getActivity(View view) {
        Context context = view.getContext();
        while (true) {
            Context context2 = context;
            if (!(context2 instanceof ContextWrapper)) {
                return null;
            }
            if (context2 instanceof Activity) {
                return (Activity) context2;
            }
            context = ((ContextWrapper) context2).getBaseContext();
        }
    }

    public static ArrayList<Quizz> getAllQuizzInTermsOfCategory(Context context, String str) throws JSONException {
        ArrayList<Quizz> arrayList = new ArrayList<>();
        try {
            JSONArray jSONArray = new JSONArray(loadJSONFromAsset(context, "bible/" + str.replace("-", "_") + ".json"));
            for (int i = 0; i < jSONArray.length(); i++) {
                JSONObject jSONObject = jSONArray.getJSONObject(i);
                arrayList.add(new Quizz(jSONObject.getInt("id"), jSONObject.getString("question"), jSONObject.getString("categorie"), jSONObject.getString("explication"), jSONObject.getString("choix1"), jSONObject.getString("choix2"), jSONObject.getString("choix3"), jSONObject.getString("choixcorrecte")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x003f  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0069  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x008a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.util.List<org.questionsreponses.Model.Ressource> getAllResource(android.content.Context r11, java.lang.String r12) throws org.json.JSONException {
        /*
            Method dump skipped, instructions count: 358
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.questionsreponses.Presenter.CommonPresenter.getAllResource(android.content.Context, java.lang.String):java.util.List");
    }

    public static ArrayList<Quizz> getAllTheBible(Context context, String str) throws JSONException, IOException {
        ArrayList<Quizz> arrayList = new ArrayList<>();
        String str2 = str == null ? "bible/toute_la_bible.json" : str;
        try {
            String strLoadJSONFromAsset = str2;
            if (str == null) {
                strLoadJSONFromAsset = loadJSONFromAsset(context, str2);
            }
            JSONArray jSONArray = new JSONArray(strLoadJSONFromAsset);
            for (int i = 0; i < jSONArray.length(); i++) {
                JSONObject jSONObject = jSONArray.getJSONObject(i);
                arrayList.add(new Quizz(jSONObject.getInt("id"), jSONObject.getString("question"), jSONObject.getString("categorie"), jSONObject.getString("explication"), jSONObject.getString("choix1"), jSONObject.getString("choix2"), jSONObject.getString("choix3"), jSONObject.getString("choixcorrecte")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public static void getAllTheBibleFromServer(Context context) {
        if (isMobileConnected(context)) {
            ((HomeView.IApiRessource) ApiClient.getApiClientLeVraiEvangile(context).create(HomeView.IApiRessource.class)).getAllQuizz().enqueue(new Callback<List<Quizz>>(context) { // from class: org.questionsreponses.Presenter.CommonPresenter.14
                final Context val$context;

                {
                    this.val$context = context;
                }

                @Override // retrofit2.Callback
                public void onFailure(Call<List<Quizz>> call, Throwable th) {
                    ArrayList unused = CommonPresenter.quizzListFromServer = CommonPresenter.getAllTheBible(this.val$context, null);
                }

                @Override // retrofit2.Callback
                public void onResponse(Call<List<Quizz>> call, Response<List<Quizz>> response) {
                    ArrayList unused = CommonPresenter.quizzListFromServer = (ArrayList) response.body();
                }
            });
        } else {
            quizzListFromServer = getAllTheBible(context, null);
        }
    }

    public static ArrayList<Survey> getAllTheSurveyBy(String str) throws JSONException {
        ArrayList<Survey> arrayList = new ArrayList<>();
        try {
            JSONArray jSONArray = new JSONArray(str);
            for (int i = 0; i < jSONArray.length(); i++) {
                JSONObject jSONObject = jSONArray.getJSONObject(i);
                int i2 = jSONObject.getInt("id");
                String string = jSONObject.getString("categorie");
                String string2 = jSONObject.getString("question");
                String string3 = jSONObject.getString("proposition_1");
                String string4 = jSONObject.getString("proposition_2");
                String string5 = jSONObject.getString("proposition_3");
                String string6 = jSONObject.getString("explication");
                String string7 = jSONObject.getString("reponse");
                String string8 = jSONObject.getString("reponse_choisie");
                String string9 = jSONObject.getString("clef_groupe");
                String string10 = jSONObject.getString("total_question");
                String string11 = jSONObject.getString("total_trouve");
                String string12 = jSONObject.getString("total_erreur");
                arrayList.add(new Survey(i2, string2, string, string6, string3, string4, string5, string7, string8, string9, string10 == null ? 0 : Integer.parseInt(string10), string11 == null ? 0 : Integer.parseInt(string11), string12 == null ? 0 : Integer.parseInt(string12), jSONObject.getString("titre_quizz"), jSONObject.getString("date")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public static void getApplicationVolume(Context context) {
        ((AudioManager) context.getSystemService(Format.AUDIO)).adjustStreamVolume(3, 0, 1);
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x007b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.util.ArrayList<org.questionsreponses.Model.Quizz> getBibleByCategory(android.content.Context r4, java.lang.String r5, java.util.ArrayList<org.questionsreponses.Model.Quizz> r6) {
        /*
            Method dump skipped, instructions count: 227
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.questionsreponses.Presenter.CommonPresenter.getBibleByCategory(android.content.Context, java.lang.String, java.util.ArrayList):java.util.ArrayList");
    }

    public static String getCategorieBible(int i) {
        return new String[]{null, "torah-loi", "neviim-prophete", "ketouvim-ecrits", "evangiles", "testament-de-jesus", "culture-generale"}[i];
    }

    public static String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
    }

    public static String getDataFromSharePreferences(Context context, String str) {
        return context.getSharedPreferences("SHARED_PREFERENCES", 0).getString(str, BuildConfig.FLAVOR);
    }

    public static SQLiteDatabase getDb() {
        return f91db;
    }

    /*  JADX ERROR: NullPointerException in pass: ConstructorVisitor
        java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.RegisterArg.sameRegAndSVar(jadx.core.dex.instructions.args.InsnArg)" because "resultArg" is null
        	at jadx.core.dex.visitors.MoveInlineVisitor.processMove(MoveInlineVisitor.java:52)
        	at jadx.core.dex.visitors.MoveInlineVisitor.moveInline(MoveInlineVisitor.java:41)
        	at jadx.core.dex.visitors.ConstructorVisitor.visit(ConstructorVisitor.java:43)
        */
    private static java.lang.String getFileAbsolutePath(
    /*  JADX ERROR: Method generation error
        jadx.core.utils.exceptions.JadxRuntimeException: Code variable not set in r4v0 ??
        	at jadx.core.dex.instructions.args.SSAVar.getCodeVar(SSAVar.java:236)
        	at jadx.core.codegen.MethodGen.addMethodArguments(MethodGen.java:224)
        	at jadx.core.codegen.MethodGen.addDefinition(MethodGen.java:169)
        	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:405)
        	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:335)
        	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$3(ClassGen.java:301)
        	at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:186)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1604)
        	at java.base/java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
        	at java.base/java.util.stream.Sink$ChainedReference.end(Sink.java:261)
        	at java.base/java.util.stream.ReferencePipeline$7$1FlatMap.end(ReferencePipeline.java:284)
        	at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:571)
        	at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:560)
        	at java.base/java.util.stream.ForEachOps$ForEachOp.evaluateSequential(ForEachOps.java:153)
        	at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(ForEachOps.java:176)
        	at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:265)
        	at java.base/java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:632)
        	at jadx.core.codegen.ClassGen.addInnerClsAndMethods(ClassGen.java:297)
        	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:286)
        	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:270)
        	at jadx.core.codegen.ClassGen.addClassCode(ClassGen.java:161)
        	at jadx.core.codegen.ClassGen.makeClass(ClassGen.java:103)
        	at jadx.core.codegen.CodeGen.wrapCodeGen(CodeGen.java:45)
        	at jadx.core.codegen.CodeGen.generateJavaCode(CodeGen.java:34)
        	at jadx.core.codegen.CodeGen.generate(CodeGen.java:22)
        	at jadx.core.ProcessClass.process(ProcessClass.java:79)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:117)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:403)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:391)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:341)
        */
    /*  JADX ERROR: NullPointerException in pass: ConstructorVisitor
        java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.RegisterArg.sameRegAndSVar(jadx.core.dex.instructions.args.InsnArg)" because "resultArg" is null
        	at jadx.core.dex.visitors.MoveInlineVisitor.processMove(MoveInlineVisitor.java:52)
        	at jadx.core.dex.visitors.MoveInlineVisitor.moveInline(MoveInlineVisitor.java:41)
        */

    public static void getFileByDownloadManager(Context context, String str, String str2, String str3, String str4) {
        if (context != null) {
            try {
                File file = new File(getFileAbsolutePath(context, str2));
                Log.i("TAG_APP_DOWNLOAD", "PATH : " + file.getAbsolutePath());
                ((DownloadManager) context.getSystemService("download")).enqueue(new DownloadManager.Request(Uri.parse(str)).setTitle(str2).setDescription(context.getString(C0598R.string.lb_downloading)).setNotificationVisibility(0).setNotificationVisibility(1).setDestinationUri(Uri.fromFile(file)).setAllowedOverMetered(true).setAllowedOverRoaming(true));
                Toast.makeText(context, context.getResources().getString(C0598R.string.lb_downloading_progress), 0).show();
            } catch (Exception e) {
            }
        }
    }

    public static String[] getFolderName() {
        return FOLDER_NAME;
    }

    public static List<Ressource> getJsonToResourcesList(Context context, String str, String str2) throws JSONException {
        ArrayList arrayList = new ArrayList();
        try {
            JSONArray jSONArray = new JSONArray(getDataFromSharePreferences(context, str));
            for (int i = 0; i < jSONArray.length(); i++) {
                JSONObject jSONObject = jSONArray.getJSONObject(i);
                arrayList.add(new Ressource(jSONObject.getInt("id"), jSONObject.getString("titre"), jSONObject.getBoolean("etat"), jSONObject.getString("src"), jSONObject.getString("date"), jSONObject.getString("auteur"), jSONObject.getString("urlacces"), jSONObject.getString("duree"), str2));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public static String getKeyCategoryByTitle(Context context, String str) throws Resources.NotFoundException {
        String[] stringArray = context.getResources().getStringArray(C0598R.array.bible_category);
        if (str == null) {
            return KEY_QUIZZ_GAME_WITH_LEVEL_ID_LIST;
        }
        if (str.toLowerCase().trim().equalsIgnoreCase(stringArray[0])) {
            return KEY_ALL_BIBLE_ID_LIST;
        }
        if (str.toLowerCase().trim().equalsIgnoreCase(stringArray[1])) {
            return KEY_TORAH_ID_LIST;
        }
        if (str.toLowerCase().trim().equalsIgnoreCase(stringArray[2])) {
            return KEY_NEVIIM_ID_LIST;
        }
        if (str.toLowerCase().trim().equalsIgnoreCase(stringArray[3])) {
            return KEY_KETOUVIM_ID_LIST;
        }
        if (str.toLowerCase().trim().equalsIgnoreCase(stringArray[4])) {
            return KEY_GOSPEL_ID_LIST;
        }
        if (str.toLowerCase().trim().equalsIgnoreCase(stringArray[5])) {
            return KEY_JESUS_TESTAMENT_ID_LIST;
        }
        return null;
    }

    public static String getLibelleResultSearch(Context context, int i, String str) throws Resources.NotFoundException {
        String string;
        if (i == 0) {
            string = context.getResources().getString(C0598R.string.lb_search_result_find);
        } else if (str.equalsIgnoreCase(Format.VIDEO)) {
            if (i == 1) {
                string = context.getResources().getString(C0598R.string.lb_search_one_video_find);
            } else {
                string = i + " " + context.getResources().getString(C0598R.string.lb_search_more_video_find);
            }
        } else if (!str.equalsIgnoreCase(Format.AUDIO)) {
            string = null;
        } else if (i == 1) {
            string = context.getResources().getString(C0598R.string.lb_search_one_audio_find);
        } else {
            string = i + " " + context.getResources().getString(C0598R.string.lb_search_more_audio_find);
        }
        return string;
    }

    public static void getNavDrawerDimension(Context context, LinearLayout linearLayout) {
        ViewGroup.LayoutParams layoutParams = linearLayout.getLayoutParams();
        Hashtable<String, Integer> hashtableResolutionEcran = resolutionEcran(context);
        int iIntValue = hashtableResolutionEcran.get("largeur").intValue();
        int iIntValue2 = hashtableResolutionEcran.get("hauteur").intValue();
        if (iIntValue <= iIntValue2) {
            iIntValue2 = iIntValue;
        }
        layoutParams.width = (int) (iIntValue2 * 0.97f);
        linearLayout.setLayoutParams(layoutParams);
    }

    public static int getNextRessourceValue(int i, int i2) {
        int i3 = i2 - 1;
        int i4 = i3;
        if (i < i3) {
            i4 = i + 1;
        }
        return i4;
    }

    public static int getNotifPlayerNextValue(int i, int i2) {
        return i < i2 - 1 ? i + 1 : 0;
    }

    public static int getNotifPlayerPreviousValue(int i, int i2) {
        return i > 0 ? i - 1 : i2 - 1;
    }

    public static String getPlurialOfString(int i) {
        return i > 1 ? "S" : BuildConfig.FLAVOR;
    }

    public static int getPreviousRessourceValue(int i) {
        int i2 = i;
        if (i > 0) {
            i2 = i - 1;
        }
        return i2;
    }

    public static List<Ressource> getResultSearchBy(Context context, String str, String str2) throws JSONException {
        List<Ressource> allResource = getAllResource(context, str);
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < allResource.size(); i++) {
            Ressource ressource = allResource.get(i);
            if (removeAccents(ressource.getTitre().toLowerCase()).contains(str2.toLowerCase()) || removeAccents(ressource.getAuteur().toLowerCase()).contains(str2.toLowerCase())) {
                arrayList.add(ressource);
            }
        }
        return arrayList;
    }

    public static int getScrollToNextValue(int i, int i2) {
        int i3 = i;
        if (i < i2 - 2) {
            i3 = i + 2;
        }
        return i3;
    }

    public static int getScrollToPreviousValue(int i, int i2) {
        int i3 = i + 2;
        if (i3 > i2 - 1) {
            i = i3;
        }
        return i;
    }

    public static Setting getSettingObjectFromSharePreferences(Context context, String str) {
        try {
            JSONObject jSONObject = new JSONObject(getDataFromSharePreferences(context, str));
            return new Setting(jSONObject.getString("title"), jSONObject.getString("libelle"), jSONObject.getBoolean("choice"), jSONObject.getInt("total"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getShareDataLinkFilter(String str) {
        String[] strArrSplit = str.replace("\r", " ").replace("\n", " ").replace("embed/", "?v=").split(" ");
        if (strArrSplit.length <= 0) {
            return null;
        }
        for (int i = 0; i < strArrSplit.length; i++) {
            if (strArrSplit[i].trim().contains("youtube.com") || strArrSplit[i].trim().contains("youtu.be")) {
                return strArrSplit[i].trim();
            }
        }
        return null;
    }

    public static String getStringDate(String str) {
        return str.split("-")[2].trim() + " " + new String[]{"JAN", "FÉV", "MAR", "AVR", "MAI", "JUN", "JUL", "AOÜ", "SEP", "OCT", "NOV", "DÉC"}[Integer.parseInt(str.split("-")[1].trim()) - 1] + " " + str.split("-")[0].trim();
    }

    public static int getTotalMaxQuestion(Context context) {
        return getSettingObjectFromSharePreferences(context, KEY_SETTING_NUMBER_OF_QUIZZ).getTotal();
    }

    public static int getTotalQuizzIdListBy(String str) {
        return str.replace("-", " ").trim().split(" ").length;
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0025, code lost:
    
        if (r5.contains("youtu.be") != false) goto L11;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String getVideoIdFromYoutubeUrl(android.content.Context r4, java.lang.String r5) {
        /*
            Method dump skipped, instructions count: 420
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.questionsreponses.Presenter.CommonPresenter.getVideoIdFromYoutubeUrl(android.content.Context, java.lang.String):java.lang.String");
    }

    public static View getViewInTermsOfContext(Context context) {
        try {
            return ((Activity) context).getWindow().getDecorView().findViewById(R.id.content);
        } catch (Exception e) {
            Log.e("TAG_ERROR", "CommonPresenter-->getViewInTermsOfContext() : " + e.getMessage());
            return null;
        }
    }

    public static void initializeAppSetting(Context context) {
        String dataFromSharePreferences = getDataFromSharePreferences(context, KEY_SETTING_CONFIRM_BEFORE_QUIT_APP);
        if (dataFromSharePreferences == null || dataFromSharePreferences.isEmpty()) {
            saveDataInSharePreferences(context, KEY_SETTING_CONFIRM_BEFORE_QUIT_APP, new Gson().toJson(new Setting("Confirmer avant de quitter", "Demander la confirmation avant de sortir de l'application.", true, 0)));
            saveDataInSharePreferences(context, KEY_SETTING_NUMBER_OF_QUIZZ, new Gson().toJson(new Setting("QUIZ, Nombre de questions", "Le choix actuel par quiz est de", false, 20)));
            saveDataInSharePreferences(context, KEY_SETTING_CONCATENATE_AUDIO_READING, new Gson().toJson(new Setting("AUDIO, Enchaîner la lecture", "Lecture automatique des audios.", true, 0)));
            saveDataInSharePreferences(context, KEY_SETTING_CONCATENATE_VIDEO_READING, new Gson().toJson(new Setting("VIDEO, Enchaîner la lecture", "Lecture automatique des vidéos.", true, 0)));
            saveDataInSharePreferences(context, KEY_SETTING_WIFI_EXCLUSIF, new Gson().toJson(new Setting("WIFI exclusif", "Télécharger uniquement en Wi-Fi.", false, 0)));
            saveDataInSharePreferences(context, KEY_SETTING_OFFLINE_MODE, new Gson().toJson(new Setting("Mode Hors-ligne", "Utiliser la connexion internet que pour télécharger ou lire les vidéos et les audios.", false, 0)));
            saveDataInSharePreferences(context, KEY_SETTING_AUTOMATIC_SOUND_READING, new Gson().toJson(new Setting("Signal sonore automatique", "Activer l'émission d'un signal sonore automatique.", true, 0)));
            saveDataInSharePreferences(context, KEY_SETTING_LEVEL_DIFFICULTY, new Gson().toJson(new Setting("Niveaux de difficulté", "Augmenter le niveau de difficulté en reduisant le temps.", false, 1000)));
            saveDataInSharePreferences(context, KEY_SETTING_INITIALIZE_QUIZZ_GAME, new Gson().toJson(new Setting("Réinitialisation des données", "Supprimer les quizz terminés, les quizz en cours ou le jeu de croissance spirituelle.", false, 1500)));
            Log.i("TAG_DATA_SETTING", "The data setting is NULL");
            return;
        }
        String dataFromSharePreferences2 = getDataFromSharePreferences(context, KEY_SETTING_OFFLINE_MODE);
        if (dataFromSharePreferences2 == null || dataFromSharePreferences2.isEmpty()) {
            saveDataInSharePreferences(context, KEY_SETTING_OFFLINE_MODE, new Gson().toJson(new Setting("Mode Hors-ligne", "Utiliser la connexion internet que pour télécharger ou lire les vidéos et les audios.", false, 0)));
        }
        String dataFromSharePreferences3 = getDataFromSharePreferences(context, KEY_SETTING_AUTOMATIC_SOUND_READING);
        if (dataFromSharePreferences3 == null || dataFromSharePreferences3.isEmpty()) {
            saveDataInSharePreferences(context, KEY_SETTING_AUTOMATIC_SOUND_READING, new Gson().toJson(new Setting("Signal sonore automatique", "Activer l'émission d'un signal sonore automatique.", true, 0)));
        }
        String dataFromSharePreferences4 = getDataFromSharePreferences(context, KEY_SETTING_LEVEL_DIFFICULTY);
        if (dataFromSharePreferences4 == null || dataFromSharePreferences4.isEmpty()) {
            saveDataInSharePreferences(context, KEY_SETTING_LEVEL_DIFFICULTY, new Gson().toJson(new Setting("Niveaux de difficulté", "Augmenter le niveau de difficulté en reduisant le temps.", false, 1000)));
        }
        String dataFromSharePreferences5 = getDataFromSharePreferences(context, KEY_SETTING_INITIALIZE_QUIZZ_GAME);
        if (dataFromSharePreferences5 == null || dataFromSharePreferences5.isEmpty()) {
            saveDataInSharePreferences(context, KEY_SETTING_INITIALIZE_QUIZZ_GAME, new Gson().toJson(new Setting("Réinitialisation des données", "Supprimer les quizz terminés, les quizz en cours ou le jeu de croissance spirituelle.", false, 1500)));
        }
        Log.i("TAG_DATA_SETTING", "The data setting is not NULL");
    }

    public static void initializeCategoriesQuizz(Context context) {
        String dataFromSharePreferences = getDataFromSharePreferences(context, KEY_ALL_BIBLE_ID_LIST);
        if (dataFromSharePreferences == null || dataFromSharePreferences.isEmpty()) {
            saveDataInSharePreferences(context, KEY_ALL_BIBLE_ID_LIST, "-");
        }
        String dataFromSharePreferences2 = getDataFromSharePreferences(context, KEY_TORAH_ID_LIST);
        if (dataFromSharePreferences2 == null || dataFromSharePreferences2.isEmpty()) {
            saveDataInSharePreferences(context, KEY_TORAH_ID_LIST, "-");
        }
        String dataFromSharePreferences3 = getDataFromSharePreferences(context, KEY_NEVIIM_ID_LIST);
        if (dataFromSharePreferences3 == null || dataFromSharePreferences3.isEmpty()) {
            saveDataInSharePreferences(context, KEY_NEVIIM_ID_LIST, "-");
        }
        String dataFromSharePreferences4 = getDataFromSharePreferences(context, KEY_KETOUVIM_ID_LIST);
        if (dataFromSharePreferences4 == null || dataFromSharePreferences4.isEmpty()) {
            saveDataInSharePreferences(context, KEY_KETOUVIM_ID_LIST, "-");
        }
        String dataFromSharePreferences5 = getDataFromSharePreferences(context, KEY_GOSPEL_ID_LIST);
        if (dataFromSharePreferences5 == null || dataFromSharePreferences5.isEmpty()) {
            saveDataInSharePreferences(context, KEY_GOSPEL_ID_LIST, "-");
        }
        String dataFromSharePreferences6 = getDataFromSharePreferences(context, KEY_JESUS_TESTAMENT_ID_LIST);
        if (dataFromSharePreferences6 == null || dataFromSharePreferences6.isEmpty()) {
            saveDataInSharePreferences(context, KEY_JESUS_TESTAMENT_ID_LIST, "-");
        }
        String dataFromSharePreferences7 = getDataFromSharePreferences(context, KEY_QUIZZ_GAME_WITH_LEVEL_ID_LIST);
        if (dataFromSharePreferences7 == null || dataFromSharePreferences7.isEmpty()) {
            saveDataInSharePreferences(context, KEY_QUIZZ_GAME_WITH_LEVEL_ID_LIST, "-");
        }
    }

    public static void initializeNotificationTimeLapsed(Context context) {
        saveDataInSharePreferences(context, KEY_PLAYER_AUDIO_TO_NOTIF_AUDIO_TIME_ELAPSED, "0");
        saveDataInSharePreferences(context, KEY_NOTIF_AUDIO_TO_PLAYER_AUDIO_TIME_ELAPSED, "0");
        saveDataInSharePreferences(context, KEY_NOTIF_AUDIO_TO_PLAYER_AUDIO_ID, "0");
    }

    private static boolean isInteger(String str) throws NumberFormatException {
        boolean z;
        try {
            Integer.parseInt(str);
            z = true;
        } catch (NumberFormatException e) {
            z = false;
        }
        return z;
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x0042  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x006c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean isMobileConnected(android.content.Context r3) {
        /*
            r0 = r3
            java.lang.String r1 = "connectivity"
            java.lang.Object r0 = r0.getSystemService(r1)
            android.net.ConnectivityManager r0 = (android.net.ConnectivityManager) r0
            r3 = r0
            r0 = 0
            r6 = r0
            r0 = 0
            r5 = r0
            r0 = r6
            r4 = r0
            r0 = r3
            if (r0 == 0) goto L6e
            int r0 = android.os.Build.VERSION.SDK_INT
            r1 = 23
            if (r0 >= r1) goto L46
            r0 = r3
            android.net.NetworkInfo r0 = r0.getActiveNetworkInfo()
            r3 = r0
            r0 = r6
            r4 = r0
            r0 = r3
            if (r0 == 0) goto L6e
            r0 = r5
            r4 = r0
            r0 = r3
            boolean r0 = r0.isConnected()
            if (r0 == 0) goto L44
            r0 = r3
            int r0 = r0.getType()
            r1 = 1
            if (r0 == r1) goto L42
            r0 = r5
            r4 = r0
            r0 = r3
            int r0 = r0.getType()
            if (r0 != 0) goto L44
        L42:
            r0 = 1
            r4 = r0
        L44:
            r0 = r4
            return r0
        L46:
            r0 = r3
            android.net.Network r0 = r0.getActiveNetwork()
            r7 = r0
            r0 = r6
            r4 = r0
            r0 = r7
            if (r0 == 0) goto L6e
            r0 = r3
            r1 = r7
            android.net.NetworkCapabilities r0 = r0.getNetworkCapabilities(r1)
            r3 = r0
            r0 = r3
            r1 = 0
            boolean r0 = r0.hasTransport(r1)
            if (r0 != 0) goto L6c
            r0 = r6
            r4 = r0
            r0 = r3
            r1 = 1
            boolean r0 = r0.hasTransport(r1)
            if (r0 == 0) goto L6e
        L6c:
            r0 = 1
            r4 = r0
        L6e:
            r0 = r4
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.questionsreponses.Presenter.CommonPresenter.isMobileConnected(android.content.Context):boolean");
    }

    public static boolean isMobileWIFIConnected(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            if (connectivityManager == null) {
                return false;
            }
            if (Build.VERSION.SDK_INT >= 23) {
                Network activeNetwork = connectivityManager.getActiveNetwork();
                if (activeNetwork != null) {
                    return connectivityManager.getNetworkCapabilities(activeNetwork).hasTransport(1);
                }
                return false;
            }
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo == null) {
                return false;
            }
            boolean z = false;
            if (activeNetworkInfo.isConnected()) {
                z = false;
                if (activeNetworkInfo.getType() == 1) {
                    z = true;
                }
            }
            return z;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isPermissionToSaveFileAccepted(Context context) {
        return ContextCompat.checkSelfPermission(context, "android.permission.WRITE_EXTERNAL_STORAGE") == 0;
    }

    public static final boolean isValidEmail(CharSequence charSequence) {
        return !TextUtils.isEmpty(charSequence) && Patterns.EMAIL_ADDRESS.matcher(charSequence).matches();
    }

    public static boolean itsABookTitle(String str) {
        for (int i = 0; i < 66; i++) {
            if (str.toLowerCase().trim().startsWith(new String[]{"Genèse", "Exode", "Lévitique", "Nombres", "Deutéronome", "Josué", "Juges", "1 Samuel", "2 Samuel", "1 Rois", "2 Rois", "Ésaïe", "Jérémie", "Ézéchiel", "Osée", "Joël", "Amos", "Abdias", "Jonas", "Michée", "Nahum", "Habakuk", "Sophonie", "Aggée", "Zacharie", "Malachie", "Psaumes", "Proverbes", "Job", "Cantique des cantiques", "Ruth", "Lamentations de Jérémie", "Ecclésiaste", "Esther", "Daniel", "Esdras", "Néhémie", "1 Chroniques", "2 Chroniques", "Matthieu", "Marc", "Luc", "Jean", "Actes", "Jacques", "Galates", "1 Thessaloniciens", "2 Thessaloniciens", "1 Corinthiens", "2 Corinthiens", "Romains", "Éphésiens", "Philippiens", "Colossiens", "Philémon", "1 Timothée", "Tite", "1 Pierre", "2 Pierre", "2 Timothée", "Jude", "Hébreux", "1 Jean", "2 Jean", "3 Jean", "Apocalypse"}[i].toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private static String loadJSONFromAsset(Context context, String str) throws IOException {
        try {
            InputStream inputStreamOpen = context.getAssets().open(str);
            byte[] bArr = new byte[inputStreamOpen.available()];
            inputStreamOpen.read(bArr);
            inputStreamOpen.close();
            return new String(bArr, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String removeAccents(String str) {
        return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", BuildConfig.FLAVOR);
    }

    public static void removeAllDataFromSharePreferences(Context context) {
        try {
            context.getSharedPreferences("SHARED_PREFERENCES", 0).edit().clear();
        } catch (Exception e) {
        }
    }

    public static void removeDataFromSharePreferences(Context context, String str) {
        try {
            SharedPreferences.Editor editorEdit = context.getSharedPreferences("SHARED_PREFERENCES", 0).edit();
            editorEdit.remove(str);
            editorEdit.commit();
        } catch (Exception e) {
        }
    }

    public static void removeSomeSharePreferencesFromApp(Context context) {
        try {
            removeDataFromSharePreferences(context, KEY_QUIZZ_ACTIVITY_LIST);
            removeDataFromSharePreferences(context, KEY_VIDEO_PLAYER_SEND_DATA);
            removeDataFromSharePreferences(context, KEY_VIDEO_PLAYER_RETURN_DATA);
            removeDataFromSharePreferences(context, KEY_AUDIO_RETRIEVE_FROM_LVE_SERVER);
            removeDataFromSharePreferences(context, KEY_VIDEO_RETRIEVE_FROM_LVE_SERVER);
            removeDataFromSharePreferences(context, "VALUE_TOTAL_AUDIO_RETRIEVE_FROM_LVE_SERVER");
            removeDataFromSharePreferences(context, "VALUE_TOTAL_AUDIO_RETRIEVE_FROM_LVE_SERVER");
            removeDataFromSharePreferences(context, KEY_SEARCH_FORM_TYPE_RESSOURCE);
            removeDataFromSharePreferences(context, KEY_SEARCH_FORM_KEY_WORD);
            removeDataFromSharePreferences(context, VALUE_RETRIEVE_QUIZZ_FROM_HOME_ACTIVITY);
            removeDataFromSharePreferences(context, VALUE_RETRIEVE_QUIZZ_FROM_QUIZZ_FRAGMENT);
            removeDataFromSharePreferences(context, VALUE_POSITION_AUDIO_SELECTED);
            removeDataFromSharePreferences(context, VALUE_POSITION_VIDEO_SELECTED);
            removeDataFromSharePreferences(context, VALUE_VIDEO_PLAY_NEXT);
            removeDataFromSharePreferences(context, VALUE_VIDEO_PLAY_PREVIOUS);
            removeDataFromSharePreferences(context, KEY_QUIZZ_GAME_LIST_SELECTED);
            removeDataFromSharePreferences(context, KEY_QUIZZ_GROUPKEY_SELECTED);
            removeDataFromSharePreferences(context, KEY_QUIZZ_MIN_SCORE_TO_UNLOCK);
            String dataFromSharePreferences = getDataFromSharePreferences(context, KEY_NOTIFICATION_IS_PLAYING);
            if (dataFromSharePreferences != null && dataFromSharePreferences.equalsIgnoreCase("NO")) {
                removeDataFromSharePreferences(context, KEY_NOTIFICATION_IS_PLAYING);
                removeDataFromSharePreferences(context, KEY_PLAYER_AUDIO_TO_NOTIF_AUDIO_TIME_ELAPSED);
                removeDataFromSharePreferences(context, KEY_NOTIF_AUDIO_TO_PLAYER_AUDIO_TIME_ELAPSED);
                removeDataFromSharePreferences(context, KEY_NOTIF_AUDIO_TO_PLAYER_AUDIO_ID);
                removeDataFromSharePreferences(context, KEY_NOTIF_AUDIO_PLAYER_LIST);
                removeDataFromSharePreferences(context, KEY_NOTIF_PLAYER_SELECTED);
                removeDataFromSharePreferences(context, KEY_NOTIF_PLAYER_PLAY_NEXT);
                removeDataFromSharePreferences(context, KEY_NOTIF_PLAYER_PREVIOUS);
                Log.i("TAG_SHARE_PREFERENCES", "NOTIF SHARE_PREFERENCES KIES HAVE BEEN REMOVED");
            }
            Log.i("TAG_SHARE_PREFERENCES", "SHARE_PREFERENCES KIES HAVE BEEN REMOVED");
        } catch (Exception e) {
            showMessageSnackBar(getViewInTermsOfContext(context), context.getResources().getString(C0598R.string.lb_remove_sharepreferences_error));
        }
    }

    public static Hashtable<String, Integer> resolutionEcran(Context context) {
        Hashtable<String, Integer> hashtable = new Hashtable<>();
        Display defaultDisplay = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        int i = point.x;
        int i2 = point.y;
        hashtable.put("largeur", Integer.valueOf(i));
        hashtable.put("hauteur", Integer.valueOf(i2));
        return hashtable;
    }

    public static void saveDataInSharePreferences(Context context, String str, String str2) {
        try {
            SharedPreferences.Editor editorEdit = context.getSharedPreferences("SHARED_PREFERENCES", 0).edit();
            editorEdit.putString(str, str2);
            editorEdit.commit();
        } catch (Exception e) {
        }
    }

    public static void saveNotificationParameters(Context context, int i) throws JSONException {
        saveDataInSharePreferences(context, KEY_NOTIF_PLAYER_SELECTED, BuildConfig.FLAVOR + i);
        List<Ressource> jsonToResourcesList = getJsonToResourcesList(context, KEY_NOTIF_AUDIO_PLAYER_LIST, Format.AUDIO);
        saveDataInSharePreferences(context, KEY_NOTIF_PLAYER_PREVIOUS, BuildConfig.FLAVOR + getNotifPlayerPreviousValue(i, jsonToResourcesList.size()));
        saveDataInSharePreferences(context, KEY_NOTIF_PLAYER_PLAY_NEXT, BuildConfig.FLAVOR + getNotifPlayerNextValue(i, jsonToResourcesList.size()));
    }

    public static void saveSettingObjectInSharePreferences(Context context, String str, boolean z, int i) {
        Setting settingObjectFromSharePreferences = getSettingObjectFromSharePreferences(context, str);
        settingObjectFromSharePreferences.setChoice(z);
        settingObjectFromSharePreferences.setTotal(i);
        saveDataInSharePreferences(context, str, new Gson().toJson(settingObjectFromSharePreferences));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendContactFormData(Context context, String str, String str2, String str3, String str4, String str5) {
        Retrofit retrofitInstance = RetrofitData.getRetrofitInstance(context.getString(C0598R.string.url_site_web));
        if (retrofitInstance != null) {
            progressBar.setVisibility(0);
            ((RetrofitView.QuestionReponseApi) retrofitInstance.create(RetrofitView.QuestionReponseApi.class)).sendContactData(str, str2, str3, str4, str5).enqueue(new Callback<JsonReturn>(this, getViewInTermsOfContext(context), context) { // from class: org.questionsreponses.Presenter.CommonPresenter.12
                final CommonPresenter this$0;
                final Context val$context;
                final View val$view;

                {
                    this.this$0 = this;
                    this.val$view = view;
                    this.val$context = context;
                }

                @Override // retrofit2.Callback
                public void onFailure(Call<JsonReturn> call, Throwable th) {
                    CommonPresenter.progressBar.setVisibility(8);
                    CommonPresenter.showMessageSnackBar(this.val$view, this.val$context.getResources().getString(C0598R.string.youtube_server_error));
                }

                @Override // retrofit2.Callback
                public void onResponse(Call<JsonReturn> call, Response<JsonReturn> response) {
                    CommonPresenter.progressBar.setVisibility(8);
                    if (!response.isSuccessful()) {
                        CommonPresenter.showMessageSnackBar(this.val$view, this.val$context.getResources().getString(C0598R.string.youtube_server_error));
                        return;
                    }
                    CommonPresenter.showMessageSnackBar(this.val$view, response.body().getCode_message());
                    this.this$0.contactDialogForm.dismiss();
                }
            });
        }
    }

    public static void shareApplication(Context context) {
        String str = "Bonjour,\nJe tiens à te faire découvrir l'application android Questions/Réponses.\nClique sur le lien qui apparaît en bas de ce message, et n'hésite pas à en parler autour de toi.\nGloire à Jésus-Christ.\n\n" + context.getResources().getString(C0598R.string.url_play_store);
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        intent.putExtra("android.intent.extra.TEXT", str);
        intent.setType("text/plain");
        context.startActivity(Intent.createChooser(intent, "Partager l'application"));
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0062  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static void shareRessource(android.content.Context r5, org.questionsreponses.Model.Ressource r6, java.lang.String r7) throws android.content.res.Resources.NotFoundException {
        /*
            Method dump skipped, instructions count: 364
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.questionsreponses.Presenter.CommonPresenter.shareRessource(android.content.Context, org.questionsreponses.Model.Ressource, java.lang.String):void");
    }

    public static void showConfirmMessage(Context context, String str, String str2, HomeView.IHome iHome) {
        Hashtable<String, Integer> hashtableResolutionEcran = resolutionEcran(context);
        int iIntValue = hashtableResolutionEcran.get("largeur").intValue();
        int iIntValue2 = hashtableResolutionEcran.get("hauteur").intValue();
        if (iIntValue <= iIntValue2) {
            iIntValue2 = iIntValue;
        }
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(1);
        dialog.setContentView(C0598R.layout.dialog_confirm_before_out);
        dialog.getWindow().setLayout((int) (((int) (iIntValue2 * 0.75f)) * 1.3f), -2);
        TextView textView = (TextView) dialog.findViewById(2131296615);
        TextView textView2 = (TextView) dialog.findViewById(C0598R.id.msg_detail);
        textView.setText(str);
        buildTextViewToHtmlData(textView2, str2);
        ((Button) dialog.findViewById(C0598R.id.button_no)).setOnClickListener(new View.OnClickListener(dialog) { // from class: org.questionsreponses.Presenter.CommonPresenter.3
            final Dialog val$dialog;

            {
                this.val$dialog = dialog;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                this.val$dialog.dismiss();
            }
        });
        ((Button) dialog.findViewById(C0598R.id.button_yes)).setOnClickListener(new View.OnClickListener(dialog, iHome) { // from class: org.questionsreponses.Presenter.CommonPresenter.4
            final Dialog val$dialog;
            final HomeView.IHome val$iHome;

            {
                this.val$dialog = dialog;
                this.val$iHome = iHome;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                this.val$dialog.dismiss();
                new HomePresenter(this.val$iHome).closeQRApplication();
            }
        });
        dialog.show();
    }

    public static void showFormatVideo(Context context, ArrayList<Youtube> arrayList) {
        Hashtable<String, Integer> hashtableResolutionEcran = resolutionEcran(context);
        int iIntValue = hashtableResolutionEcran.get("largeur").intValue();
        int iIntValue2 = hashtableResolutionEcran.get("hauteur").intValue();
        if (iIntValue > iIntValue2) {
            iIntValue = iIntValue2;
        }
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(1);
        dialog.setContentView(C0598R.layout.dialog_format_video);
        Window window = dialog.getWindow();
        float f = (int) (iIntValue * 0.75f);
        window.setLayout((int) (1.3f * f), -2);
        ImageView imageView = (ImageView) dialog.findViewById(C0598R.id.header_video_image);
        TextView textView = (TextView) dialog.findViewById(C0598R.id.header_video_title);
        Youtube youtube = arrayList.get(0);
        String str = "https://i.ytimg.com/vi/" + youtube.getVideoId() + "/mqdefault.jpg";
        int i = (int) (f / 2.2f);
        textView.setText(youtube.getTitle());
        Picasso.with(context).load(str).memoryPolicy(MemoryPolicy.NO_CACHE, new MemoryPolicy[0]).resize(i, (int) (i * 0.5625f)).into(imageView);
        RecyclerView recyclerView = (RecyclerView) dialog.findViewById(C0598R.id.liste_format);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 1));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new FormatVideoAdapter(context, arrayList, dialog));
        ((Button) dialog.findViewById(C0598R.id.button_close)).setOnClickListener(new View.OnClickListener(dialog) { // from class: org.questionsreponses.Presenter.CommonPresenter.13
            final Dialog val$dialog;

            {
                this.val$dialog = dialog;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                this.val$dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void showMessage(Context context, String str, String str2, boolean z) {
        Hashtable<String, Integer> hashtableResolutionEcran = resolutionEcran(context);
        int iIntValue = hashtableResolutionEcran.get("largeur").intValue();
        int iIntValue2 = hashtableResolutionEcran.get("hauteur").intValue();
        if (iIntValue > iIntValue2) {
            iIntValue = iIntValue2;
        }
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(1);
        dialog.setContentView(C0598R.layout.dialog_message);
        dialog.getWindow().setLayout((int) (((int) (iIntValue * 0.75f)) * 1.3f), -2);
        TextView textView = (TextView) dialog.findViewById(2131296615);
        TextView textView2 = (TextView) dialog.findViewById(C0598R.id.msg_detail);
        textView.setText(str);
        buildTextViewToHtmlData(textView2, str2);
        ((Button) dialog.findViewById(C0598R.id.button_close)).setOnClickListener(new View.OnClickListener(dialog, z, context) { // from class: org.questionsreponses.Presenter.CommonPresenter.1
            final boolean val$closeActivity;
            final Context val$context;
            final Dialog val$dialog;

            {
                this.val$dialog = dialog;
                this.val$closeActivity = z;
                this.val$context = context;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                this.val$dialog.dismiss();
                if (this.val$closeActivity) {
                    ((Activity) this.val$context).finish();
                }
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener(z, context) { // from class: org.questionsreponses.Presenter.CommonPresenter.2
            final boolean val$closeActivity;
            final Context val$context;

            {
                this.val$closeActivity = z;
                this.val$context = context;
            }

            @Override // android.content.DialogInterface.OnDismissListener
            public void onDismiss(DialogInterface dialogInterface) {
                if (this.val$closeActivity) {
                    ((Activity) this.val$context).finish();
                }
            }
        });
        dialog.show();
    }

    public static void showMessageSnackBar(View view, String str) {
        if (view == null || str == null) {
            return;
        }
        try {
            Snackbar.make(view, str, 0).setAction("Action", (View.OnClickListener) null).show();
        } catch (Exception e) {
            Log.e("TAG_ERROR", "CommonPresenter-->showMessageSnackBar() : " + e.getMessage());
        }
    }

    public static void stopAudioMediaPlayer(MediaPlayer mediaPlayer) throws IllegalStateException {
        if (mediaPlayer == null || !mediaPlayer.isPlaying()) {
            return;
        }
        mediaPlayer.reset();
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    public static void stopVideoViewPlayer(VideoView videoView) {
        if (videoView == null || !videoView.isPlaying()) {
            return;
        }
        videoView.stopPlayback();
    }

    public static String verseBuilder(String str) {
        String str2;
        String[] strArrSplit = str.trim().split("\n");
        String str3 = BuildConfig.FLAVOR;
        for (int i = 0; i < strArrSplit.length; i++) {
            String strTrim = strArrSplit[i].trim();
            if (i != 0 || !strTrim.endsWith(":")) {
                String str4 = str3;
                if (!str3.equalsIgnoreCase(BuildConfig.FLAVOR)) {
                    str4 = str3 + "<br />";
                }
                String[] strArrSplit2 = strTrim.split(" ");
                if ((isInteger(strArrSplit2[0]) || strTrim.endsWith(":")) && strTrim.length() <= 40 && itsABookTitle(strTrim)) {
                    str3 = str4 + "<u><b><font color=\"#F5876E\">" + strArrSplit[i].trim() + "</font></b></u>";
                } else {
                    int i2 = 0;
                    while (true) {
                        str3 = str4;
                        if (i2 < strArrSplit2.length) {
                            if (i2 != 0) {
                                str2 = str4 + " " + strArrSplit2[i2];
                            } else if (isInteger(strArrSplit2[i2])) {
                                str2 = str4 + "<sup><u><b><font color=\"#F5876E\">" + strArrSplit2[i2] + "</font></b></u></sup>";
                            } else {
                                str2 = str4 + strArrSplit2[i2];
                            }
                            i2++;
                            str4 = str2;
                        }
                    }
                }
            } else if (strTrim.length() <= 40 && itsABookTitle(strTrim)) {
                str3 = str3 + "<u><b><font color=\"#F5876E\">" + strArrSplit[i].trim() + "</font></b></u>";
            } else if (strTrim.endsWith("?")) {
                str3 = str3 + "<b>" + strArrSplit[i].trim() + "</b>";
            } else {
                str3 = str3 + strArrSplit[i].trim();
            }
        }
        return str3.trim().replace("\n\n", "<br />").replace("\n", "<br />");
    }

    @Override // org.questionsreponses.View.Interfaces.CommonView.ICommonPresenter
    public void onSendContactFormFinished(Context context, String str) {
        progressBar.setVisibility(8);
        View viewInTermsOfContext = getViewInTermsOfContext(context);
        if (str.isEmpty() || str == null) {
            showMessageSnackBar(viewInTermsOfContext, context.getResources().getString(C0598R.string.unstable_connection));
        } else if (!str.contains("code_message")) {
            showMessageSnackBar(viewInTermsOfContext, str);
        } else {
            showMessageSnackBar(viewInTermsOfContext, ((JsonReturn) new Gson().fromJson(str, JsonReturn.class)).getCode_message());
            this.contactDialogForm.dismiss();
        }
    }

    public void showFormContact(Context context) throws Resources.NotFoundException {
        Hashtable<String, Integer> hashtableResolutionEcran = resolutionEcran(context);
        int iIntValue = hashtableResolutionEcran.get("largeur").intValue();
        int iIntValue2 = hashtableResolutionEcran.get("hauteur").intValue();
        if (iIntValue <= iIntValue2) {
            iIntValue2 = iIntValue;
        }
        Dialog dialog = new Dialog(context);
        this.contactDialogForm = dialog;
        dialog.requestWindowFeature(1);
        this.contactDialogForm.setContentView(C0598R.layout.dialog_form_contact);
        this.contactDialogForm.getWindow().setLayout((int) (((int) (iIntValue2 * 0.75f)) * 1.3f), -2);
        ImageButton imageButton = (ImageButton) this.contactDialogForm.findViewById(C0598R.id.btn_close_contact);
        Spinner spinner = (Spinner) this.contactDialogForm.findViewById(C0598R.id.spinner_civility);
        TextInputEditText textInputEditText = (TextInputEditText) this.contactDialogForm.findViewById(C0598R.id.edittext_nom);
        TextInputEditText textInputEditText2 = (TextInputEditText) this.contactDialogForm.findViewById(C0598R.id.edittext_email);
        TextInputEditText textInputEditText3 = (TextInputEditText) this.contactDialogForm.findViewById(C0598R.id.edittext_city);
        TextInputEditText textInputEditText4 = (TextInputEditText) this.contactDialogForm.findViewById(C0598R.id.edittexte_message);
        ImageButton imageButton2 = (ImageButton) this.contactDialogForm.findViewById(C0598R.id.btn_validate);
        ProgressBar progressBar2 = (ProgressBar) this.contactDialogForm.findViewById(C0598R.id.progress_contact);
        progressBar = progressBar2;
        progressBar2.setVisibility(8);
        String[] stringArray = context.getResources().getStringArray(C0598R.array.form_civility);
        spinner.setAdapter((SpinnerAdapter) new ArrayAdapter(context, C0598R.layout.item_spinner, stringArray));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(this, stringArray) { // from class: org.questionsreponses.Presenter.CommonPresenter.8
            final CommonPresenter this$0;
            final String[] val$listeCivilite;

            {
                this.this$0 = this;
                this.val$listeCivilite = stringArray;
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                String unused = CommonPresenter.civility = this.val$listeCivilite[i];
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener(this) { // from class: org.questionsreponses.Presenter.CommonPresenter.9
            final CommonPresenter this$0;

            {
                this.this$0 = this;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                this.this$0.contactDialogForm.dismiss();
            }
        });
        imageButton2.setOnClickListener(new View.OnClickListener(this, textInputEditText, context, textInputEditText2, textInputEditText3, textInputEditText4) { // from class: org.questionsreponses.Presenter.CommonPresenter.10
            final CommonPresenter this$0;
            final Context val$context;
            final TextInputEditText val$edittextCity;
            final TextInputEditText val$edittextEmail;
            final TextInputEditText val$edittextNom;
            final TextInputEditText val$edittexteMessage;

            {
                this.this$0 = this;
                this.val$edittextNom = textInputEditText;
                this.val$context = context;
                this.val$edittextEmail = textInputEditText2;
                this.val$edittextCity = textInputEditText3;
                this.val$edittexteMessage = textInputEditText4;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (this.val$edittextNom.getText().toString().trim().length() == 0) {
                    this.val$edittextNom.setError(this.val$context.getResources().getString(C0598R.string.field_requires));
                    return;
                }
                if (this.val$edittextEmail.getText().toString().trim().length() == 0) {
                    this.val$edittextEmail.setError(this.val$context.getResources().getString(C0598R.string.field_requires));
                    return;
                }
                if (!CommonPresenter.isValidEmail(this.val$edittextEmail.getText().toString().trim())) {
                    this.val$edittextEmail.setError(this.val$context.getResources().getString(C0598R.string.email_invalidate));
                    return;
                }
                if (this.val$edittextCity.getText().toString().trim().length() == 0) {
                    this.val$edittextCity.setError(this.val$context.getResources().getString(C0598R.string.field_requires));
                    return;
                }
                if (this.val$edittexteMessage.getText().toString().trim().length() == 0) {
                    this.val$edittexteMessage.setError(this.val$context.getResources().getString(C0598R.string.field_requires));
                    return;
                }
                String unused = CommonPresenter.name = this.val$edittextNom.getText().toString().trim();
                String unused2 = CommonPresenter.email = this.val$edittextEmail.getText().toString().trim();
                String unused3 = CommonPresenter.city = this.val$edittextCity.getText().toString().trim();
                String unused4 = CommonPresenter.message = this.val$edittexteMessage.getText().toString().trim();
                if (CommonPresenter.isMobileConnected(this.val$context)) {
                    this.this$0.sendContactFormData(this.val$context, CommonPresenter.civility, CommonPresenter.name, CommonPresenter.email, CommonPresenter.city, CommonPresenter.message);
                } else {
                    CommonPresenter.showMessageSnackBar(view, this.val$context.getResources().getString(C0598R.string.no_connection));
                }
            }
        });
        this.contactDialogForm.setOnDismissListener(new DialogInterface.OnDismissListener(this) { // from class: org.questionsreponses.Presenter.CommonPresenter.11
            final CommonPresenter this$0;

            {
                this.this$0 = this;
            }

            @Override // android.content.DialogInterface.OnDismissListener
            public void onDismiss(DialogInterface dialogInterface) {
                if (this.this$0.sendContactForm != null) {
                    this.this$0.sendContactForm.cancel(true);
                }
            }
        });
        this.contactDialogForm.show();
    }

    public void showFormSearch(Context context) throws Resources.NotFoundException {
        Hashtable<String, Integer> hashtableResolutionEcran = resolutionEcran(context);
        int iIntValue = hashtableResolutionEcran.get("largeur").intValue();
        int iIntValue2 = hashtableResolutionEcran.get("hauteur").intValue();
        if (iIntValue > iIntValue2) {
            iIntValue = iIntValue2;
        }
        Dialog dialog = new Dialog(context);
        this.searchDialogForm = dialog;
        dialog.requestWindowFeature(1);
        this.searchDialogForm.setContentView(C0598R.layout.dialog_form_research);
        this.searchDialogForm.getWindow().setLayout((int) (((int) (iIntValue * 0.75f)) * 1.3f), -2);
        ImageButton imageButton = (ImageButton) this.searchDialogForm.findViewById(C0598R.id.btn_close_search);
        Spinner spinner = (Spinner) this.searchDialogForm.findViewById(C0598R.id.spinner_type_ressource);
        TextInputEditText textInputEditText = (TextInputEditText) this.searchDialogForm.findViewById(C0598R.id.edittexte_search);
        ImageButton imageButton2 = (ImageButton) this.searchDialogForm.findViewById(C0598R.id.btn_search_validate);
        String[] stringArray = context.getResources().getStringArray(C0598R.array.form_type_ressource);
        spinner.setAdapter((SpinnerAdapter) new ArrayAdapter(context, C0598R.layout.item_spinner, stringArray));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(this, stringArray) { // from class: org.questionsreponses.Presenter.CommonPresenter.5
            final CommonPresenter this$0;
            final String[] val$listeTypeRessource;

            {
                this.this$0 = this;
                this.val$listeTypeRessource = stringArray;
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                String unused = CommonPresenter.searchTypeRessource = CommonPresenter.removeAccents(this.val$listeTypeRessource[i].replace("s", BuildConfig.FLAVOR).toLowerCase());
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener(this) { // from class: org.questionsreponses.Presenter.CommonPresenter.6
            final CommonPresenter this$0;

            {
                this.this$0 = this;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                this.this$0.searchDialogForm.dismiss();
            }
        });
        imageButton2.setOnClickListener(new View.OnClickListener(this, textInputEditText, context) { // from class: org.questionsreponses.Presenter.CommonPresenter.7
            final CommonPresenter this$0;
            final Context val$context;
            final TextInputEditText val$edittextSearch;

            {
                this.this$0 = this;
                this.val$edittextSearch = textInputEditText;
                this.val$context = context;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (this.val$edittextSearch.getText().toString().trim().length() == 0) {
                    this.val$edittextSearch.setError(this.val$context.getResources().getString(C0598R.string.field_requires));
                    return;
                }
                if (!CommonPresenter.isMobileConnected(this.val$context)) {
                    CommonPresenter.showMessageSnackBar(view, this.val$context.getResources().getString(C0598R.string.no_connection));
                    return;
                }
                String unused = CommonPresenter.searchKeyWord = this.val$edittextSearch.getText().toString().trim();
                Intent intent = new Intent(this.val$context, (Class<?>) SearchResultActivity.class);
                intent.putExtra(CommonPresenter.KEY_SEARCH_FORM_TYPE_RESSOURCE, CommonPresenter.searchTypeRessource);
                intent.putExtra(CommonPresenter.KEY_SEARCH_FORM_KEY_WORD, CommonPresenter.searchKeyWord);
                this.val$context.startActivity(intent);
            }
        });
        this.searchDialogForm.show();
    }
}
