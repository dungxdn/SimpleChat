package jp.bap.traning.simplechat.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

import io.socket.client.IO;
import io.socket.client.Socket;
import jp.bap.traning.simplechat.model.User;
import jp.bap.traning.simplechat.utils.Common;
import jp.bap.traning.simplechat.utils.Event;

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
            int token = intent.getIntExtra("token", 0);
            initSocket(host, token);
        }
        return START_NOT_STICKY;
    }

    private void initSocket(String host, int token) {
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
        Log.d(TAG, "onEvent: " + event.getEvent() + " " + data);
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
                    sendReceiver(Event.CONNECT, new JSONObject());
                })
                .on(Socket.EVENT_RECONNECT, args -> Log.d(TAG, "EVENT_RECONNECT"))
                .on(Socket.EVENT_DISCONNECT, args -> Log.w(TAG, "EVENT_DISCONNECT"))
                .on(Socket.EVENT_ERROR, args -> Log.w(TAG, "EVENT_ERROR"))
                .on(Socket.EVENT_RECONNECT_ERROR, args -> Log.e(TAG, "EVENT_RECONNECT_ERROR"));
    }

    private void sendReceiver(Event type, JSONObject data) {
        Intent i = new Intent(Common.ACTION_SOCKET_EVENT);
        i.putExtra("action", type.getEvent());
        i.putExtra("data", data.toString());
        LocalBroadcastManager.getInstance(this).sendBroadcast(i);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        mSocket.disconnect();
        sChatManager = null;
    }

}
