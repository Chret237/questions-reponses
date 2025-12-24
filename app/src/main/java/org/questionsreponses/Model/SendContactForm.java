package org.questionsreponses.Model;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ProgressBar;
import com.github.clans.fab.BuildConfig;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import org.questionsreponses.C0598R;
import org.questionsreponses.View.Interfaces.CommonView;

/* loaded from: classes-dex2jar.jar:org/questionsreponses/Model/SendContactForm.class */
public class SendContactForm extends AsyncTask<Void, Void, String> {
    private String actionForm;
    private Context context;
    private HttpURLConnection httpURLConnection;
    private CommonView.ICommonPresenter iPresenter;
    private HashMap<String, String> postDataParams;
    private ProgressBar progressBar;
    private String codeRetour = BuildConfig.FLAVOR;
    private URL url = null;

    private String getPostDataString(HashMap<String, String> map) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        boolean z = true;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (z) {
                z = false;
            } else {
                sb.append("&");
            }
            sb.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            sb.append("=");
            sb.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public String doInBackground(Void... voidArr) throws IOException {
        try {
            URL url = new URL(this.actionForm);
            this.url = url;
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            this.httpURLConnection = httpURLConnection;
            httpURLConnection.setReadTimeout(10000);
            this.httpURLConnection.setConnectTimeout(10000);
            this.httpURLConnection.setRequestMethod("POST");
            this.httpURLConnection.setDoInput(true);
            this.httpURLConnection.setDoOutput(true);
            OutputStream outputStream = this.httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            bufferedWriter.write(getPostDataString(this.postDataParams));
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            if (this.httpURLConnection.getResponseCode() == 200) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.httpURLConnection.getInputStream()));
                while (true) {
                    String line = bufferedReader.readLine();
                    if (line == null) {
                        break;
                    }
                    this.codeRetour += line;
                }
            }
            this.httpURLConnection.disconnect();
            return this.codeRetour;
        } catch (Exception e) {
            return this.context.getResources().getString(C0598R.string.unstable_connection);
        }
    }

    public void initializeData(Context context, HashMap<String, String> map, String str, ProgressBar progressBar, CommonView.ICommonPresenter iCommonPresenter) {
        this.context = context;
        this.postDataParams = map;
        this.actionForm = str;
        this.progressBar = progressBar;
        this.iPresenter = iCommonPresenter;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public void onPostExecute(String str) {
        super.onPostExecute((SendContactForm) str);
        this.iPresenter.onSendContactFormFinished(this.context, str);
    }

    @Override // android.os.AsyncTask
    protected void onPreExecute() {
        super.onPreExecute();
        this.progressBar.setVisibility(0);
    }
}
