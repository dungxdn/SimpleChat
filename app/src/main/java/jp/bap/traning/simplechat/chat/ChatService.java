package jp.bap.traning.simplechat.chat;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import jp.bap.traning.simplechat.interfaces.ListenerInterface;

/**
 * Created by dungpv on 6/7/18.
 */

public class ChatService extends Service implements ListenerInterface {
    private final String TAG = getClass().getSimpleName();
    private static ChatManager sChatManager;
    private Socket mSocket;

    public static ChatManager getChat() {
        return sChatManager;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getExtras() != null) {
            String host = intent.getStringExtra("host");
            String token = intent.getStringExtra("token");
            initSocket(host, token);
        }
        return START_STICKY;
    }

    private void initSocket(String host, String token) {
        try {
            IO.Options opts = new IO.Options();
            opts.query = "token=" + token;
            mSocket = IO.socket(host, opts);
            mSocket.connect();
            onSocketSystem();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEvent(Event event, JSONObject data) {

    }

    @Override
    public void onEmit(Event event, JSONObject data) {

    }

    private void onSocketSystem() {
        Log.d(TAG, "onSocketEvent: " + mSocket);
        mSocket
                .on(Socket.EVENT_CONNECT, args -> {
                    Log.d(TAG, "EVENT_CONNECT");
                    if (sChatManager == null) {
                        sChatManager = new ChatManager(mSocket);
                        sChatManager.addListenerSocket(this);
                    }
                })
                .on(Socket.EVENT_RECONNECT, args -> Log.d(TAG, "EVENT_RECONNECT"))
                .on(Socket.EVENT_DISCONNECT, args -> Log.w(TAG, "EVENT_DISCONNECT"))
                .on(Socket.EVENT_ERROR, args -> Log.w(TAG, "EVENT_ERROR"))
                .on(Socket.EVENT_RECONNECT_ERROR, args -> Log.e(TAG, "EVENT_RECONNECT_ERROR"));
    }
}
