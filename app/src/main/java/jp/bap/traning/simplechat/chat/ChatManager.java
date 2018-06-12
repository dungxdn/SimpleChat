package jp.bap.traning.simplechat.chat;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Ack;
import io.socket.client.Socket;

/**
 * Created by dungpv on 6/7/18.
 */

public class ChatManager {
    private static final String TAG = ChatManager.class.getSimpleName();
    interface Listener {
        void onEvent(Event event, JSONObject data);
        void onEmit(Event event, JSONObject data);
    }
    Socket mSocket;
    Listener mListener;

    ChatManager(Socket s) {
        mSocket = s;
        on(
                Event.MESSAGE_RECEIVER,
                Event.CALL,
                Event.CALL_CONTENT,
                Event.CALL_ACCEPT,
                Event.CALL_STOP
        );
    }

    public void addListenerSocket(Listener listener) {
        mListener = listener;
    }

    private void on(Event... events) {
        if (events.length > 0) {
            for (Event event : events) {
                mSocket.on(event.getEvent(), args -> {
                    Log.d(TAG, "Callback ON: " + event.getEvent() + " - " + args[0]);
                    if (args[args.length - 1] instanceof Ack) {
                        // TODO: 6/7/18
                        Log.d(TAG, "Callback ON: " + event + " had ack!");
                    }
                    if (mListener != null) {
                        mListener.onEvent(event, (JSONObject) args[0]);
                    }
                });
            }
        }
    }

    private void emit(final Event event, final Object object) {
        Log.d(TAG, "Packet EMIT: " + event.getEvent() + " - " + object);
        mSocket.emit(event.getEvent(), object, (Ack) args -> {
            Log.d(TAG, "Callback EMIT: " + event.getEvent() + " - " + args[0]);
            if (mListener != null) {
                mListener.onEmit(event, (JSONObject) args[0]);
            }
        });
    }

    public void sendMessage(String content, int roomId) {
        JSONObject data = new JSONObject();
        try {
            data.put("content", content);
            data.put("roomId", roomId);
            emit(Event.MESSAGE_SEND, data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void emitCallContent(JSONObject message, int roomId) {
        Log.d("emitMessage", message.toString());
        try {
            message.put("roomId", roomId);
            emit(Event.CALL_CONTENT, message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void emitCallStop(int roomId) {
        JSONObject data = new JSONObject();
        try {
            data.put("roomId", roomId);
            emit(Event.CALL_STOP, data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void emitCall(int roomId) {
        JSONObject data = new JSONObject();
        try {
            data.put("roomId", roomId);
            emit(Event.CALL, data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void emitCallAccept(int roomId) {
        JSONObject data = new JSONObject();
        try {
            data.put("roomId", roomId);
            emit(Event.CALL_ACCEPT, data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
