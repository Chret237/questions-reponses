package org.questionsreponses.Model;

import android.content.Context;
import android.os.AsyncTask;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.questionsreponses.View.Interfaces.DownloaderView;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/Model/LoadFormatVideo.class */
public class LoadFormatVideo extends AsyncTask<Void, Void, ArrayList<Youtube>> {
    private Context context;
    private ArrayList<Youtube> listYoutube;
    private DownloaderView.ILoadFormatVideoFinished loadFinished;
    private HttpURLConnection urlConnection;
    private String urlWS;
    private boolean videoMp4Existe = false;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public ArrayList<Youtube> doInBackground(Void... voidArr) throws JSONException {
        StringBuilder sb = new StringBuilder();
        this.listYoutube = new ArrayList<>();
        try {
            try {
                this.urlConnection = (HttpURLConnection) new URL(this.urlWS).openConnection();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new BufferedInputStream(this.urlConnection.getInputStream())));
                while (true) {
                    String line = bufferedReader.readLine();
                    if (line == null) {
                        break;
                    }
                    sb.append(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                JSONArray jSONArray = new JSONArray(sb.toString());
                for (int i = 0; i < jSONArray.length(); i++) {
                    JSONObject jSONObject = jSONArray.getJSONObject(i);
                    String string = jSONObject.getString("video_id");
                    String string2 = jSONObject.getString("itag");
                    String string3 = jSONObject.getString("quality");
                    String string4 = jSONObject.getString("type");
                    String string5 = jSONObject.getString("extension");
                    String string6 = jSONObject.getString("resolution");
                    String string7 = jSONObject.getString("url");
                    String string8 = jSONObject.getString("download_url");
                    String string9 = jSONObject.getString("expires");
                    String string10 = jSONObject.getString("ipbits");
                    String string11 = jSONObject.getString("ip");
                    String string12 = jSONObject.getString("view_count");
                    String string13 = jSONObject.getString("title");
                    String string14 = jSONObject.getString("size");
                    String string15 = jSONObject.getString("filename");
                    if (string4.toLowerCase().contains("video/mp4")) {
                        this.videoMp4Existe = true;
                    }
                    this.listYoutube.add(new Youtube(string, string2, string3, string4, string5, string6, string7, string8, string9, string10, string11, string12, string13, string14, string15));
                }
            } catch (JSONException e2) {
                this.listYoutube = null;
            }
            return this.listYoutube;
        } finally {
            this.urlConnection.disconnect();
        }
    }

    public void loadFormatVideoFromYoutube(Context context, String str, DownloaderView.ILoadFormatVideoFinished iLoadFormatVideoFinished) {
        this.context = context;
        this.urlWS = str;
        this.loadFinished = iLoadFormatVideoFinished;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public void onPostExecute(ArrayList<Youtube> arrayList) {
        super.onPostExecute((LoadFormatVideo) arrayList);
        if (arrayList == null) {
            this.loadFinished.onLoadFormatVideoFailure(this.context);
        } else {
            if (this.videoMp4Existe) {
                this.loadFinished.onLoadFormatVideoSuccess(arrayList);
                return;
            }
            LoadFormatVideo loadFormatVideo = new LoadFormatVideo();
            loadFormatVideo.loadFormatVideoFromYoutube(this.context, this.urlWS, this.loadFinished);
            loadFormatVideo.execute(new Void[0]);
        }
    }

    @Override // android.os.AsyncTask
    protected void onPreExecute() {
        super.onPreExecute();
    }
}
