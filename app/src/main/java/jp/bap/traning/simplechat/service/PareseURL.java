package jp.bap.traning.simplechat.service;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class PareseURL extends AsyncTask<String,Void,String>{
    @Override
    protected String doInBackground(String... strings) {
        StringBuffer stringBuffer = new StringBuffer();
        try{
            Document document = Jsoup.connect(strings[0]).get();
            // Get document (HTML page) title
            String title = document.title();
            stringBuffer.append("TiTle: "+title);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(s != null) {
            Log.e("ParseLink: ",s);
        }
        else {
            Log.e("ParseLink","Khong Parse duoc Link");
        }
    }
}
