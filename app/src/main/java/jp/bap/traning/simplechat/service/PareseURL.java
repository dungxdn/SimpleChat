package jp.bap.traning.simplechat.service;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import jp.bap.traning.simplechat.presenter.chattalks.ChatTalksListener;

public class PareseURL extends AsyncTask<String,Void,String>{
    private ChatTalksListener mCallBack;
    private static String link="";

    public PareseURL(ChatTalksListener callBack) {
        mCallBack = callBack;
    }

    @Override
    protected String doInBackground(String... strings) {
        String title="";
        try{
            link = strings[0];
            Document document = Jsoup.connect(strings[0]).get();
            // Get document (HTML page) title
            title = document.title();
        }
        catch (Exception e) {
            e.printStackTrace();

        }
        return title;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(s.trim().isEmpty() == false) {
            mCallBack.onRequestURLSuccess(link,s);
        }
        else {
            Log.e("ParseLink","Khong Parse duoc Link");
            mCallBack.onRequestURLFailed(link);
        }
    }
}
