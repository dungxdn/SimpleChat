package jp.bap.traning.simplechat.presenter.chattalks;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.ByteArrayOutputStream;

public class ChatTalksPresenter {
    private ChatTalksListener chatTalksListener;

    public ChatTalksPresenter(ChatTalksListener mCallBack) {
        chatTalksListener = mCallBack;
    }

    public ChatTalksPresenter() {
    }

    // Check if message is a link
    public boolean containsLink(String input) {
        boolean result = false;
        String[] parts = input.split("\\s+");
        for (String item : parts) {
            if (android.util.Patterns.WEB_URL.matcher(item).matches()) {
                result = true;
                break;
            }
        }
        return result;
    }

    // insert into link
    private String insertHTTPToLink(String link) {
        String result = "";
        if (link.startsWith("http") || link.startsWith("Http")) {
            result = link;
        } else {
            result = "http://" + link;
        }
        return result;
    }

    //Covert bitmap to string
    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    //Covert string to bitmap
    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    // Scale bitmap less
    public Bitmap readBitmapAndScale(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;                              //Chỉ đọc thông tin ảnh, không đọc dữ liwwuj
        BitmapFactory.decodeFile(path, options);                          //Đọc thông tin ảnh
        options.inSampleSize = 4; //Scale bitmap xuống 4 lần
        options.inJustDecodeBounds = false; //Cho phép đọc dữ liệu ảnh ảnh
        return BitmapFactory.decodeFile(path, options);
    }

    //Request to URL
    public void requestURL(String link) {
        new PareseURL(chatTalksListener).execute(new String[]{insertHTTPToLink(link)});

    }

}
