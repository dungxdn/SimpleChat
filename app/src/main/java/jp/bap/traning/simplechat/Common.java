package jp.bap.traning.simplechat;

import android.content.Context;
import android.content.Intent;

import jp.bap.traning.simplechat.chat.ChatService;

/**
 * Created by dungpv on 6/13/18.
 */

public class Common {
    public static final String URL_CHAT = "http://172.16.1.77:3000";
    public static final String TURN_URL = "turn:turn.robin-aisystem.com:2022";
    public static final String TURN_USERNAME = "robin";
    public static final String TURN_PASSWORD = "EID5rvjx8Ls8wO9DALls1gAQa";

    public static void connectServerChat(Context context, String url, int userId) {
        if (ChatService.getChat() == null) {
            Intent i = new Intent(context, ChatService.class);
            i.putExtra("host", url);
            i.putExtra("token", userId);
            context.startService(i);
        }
    }
}
