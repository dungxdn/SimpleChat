package jp.bap.traning.simplechat;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Created by dungpv on 6/7/18.
 */

public class ChatService extends Service implements ChatManager.Listener {
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
        sendReceiver(event, data);
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
                    sendReceiver(Event.CONNECTED, new JSONObject());
                })
                .on(Socket.EVENT_RECONNECT, args -> Log.d(TAG, "EVENT_RECONNECT"))
                .on(Socket.EVENT_DISCONNECT, args -> Log.w(TAG, "EVENT_DISCONNECT"))
                .on(Socket.EVENT_ERROR, args -> Log.w(TAG, "EVENT_ERROR"))
                .on(Socket.EVENT_RECONNECT_ERROR, args -> Log.e(TAG, "EVENT_RECONNECT_ERROR"));
    }

    private void sendReceiver(Event type, JSONObject data) {
        Log.d(TAG, "sendReceiver: " + type.getEvent());
        Intent intent = new Intent(type.getEvent());
        intent.putExtra("data", data.toString());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void startCall(){

    }
}
