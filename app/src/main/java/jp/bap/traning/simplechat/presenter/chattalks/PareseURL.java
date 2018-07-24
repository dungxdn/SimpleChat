package jp.bap.traning.simplechat.presenter.chattalks;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.lang.annotation.ElementType;

import jp.bap.traning.simplechat.presenter.chattalks.ChatTalksListener;

public class PareseURL extends AsyncTask<String, Void, String> {
    private ChatTalksListener mCallBack;
    private static String link = "";
    private static String srcImage="";

    PareseURL(ChatTalksListener callBack) {
        mCallBack = callBack;
    }

    @Override
    protected String doInBackground(String... strings) {
        String title = "";
        try {
            link = strings[0];
            Document document = Jsoup.connect(strings[0]).get();
            // Get document (HTML page) title
            title = document.title();
            Element mElement = document.select("img").first();
            srcImage = mElement.absUrl("src");
            Log.d("url: "," "+srcImage);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return title;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (!s.trim().isEmpty()) {
            mCallBack.onRequestURLSuccess(link, s, srcImage);
        } else {
            mCallBack.onRequestURLFailed(link);
        }
    }
}
