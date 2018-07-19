package jp.bap.traning.simplechat.service;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Ack;
import io.socket.client.Socket;
import jp.bap.traning.simplechat.model.Comment;
import jp.bap.traning.simplechat.model.Message;
import jp.bap.traning.simplechat.model.News;
import jp.bap.traning.simplechat.model.User;
import jp.bap.traning.simplechat.utils.Event;

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
                Event.USER_ONLINE,
                Event.ON_USER_OFFLINE,
                Event.ON_USER_ONLINE,
                Event.CREATE_ROOM,
                Event.NEWS,
                Event.LIKE_NEWS,
                Event.COMMENT,
                //call
                Event.CALL_BUSY,
                Event.CALL,
                Event.CALL_CONTENT,
                Event.CALL_ACCEPT,
                Event.CALL_STOP,
                Event.TURN_ON_CAMERA);
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

    public void emitSendMessage(Message message, int roomId) {
        JSONObject data = new JSONObject();
        try {
            Gson gson = new Gson();
            String objectMessage = gson.toJson(message);
            data.put("chatMessage", objectMessage);
            data.put("roomId", roomId);
            emit(Event.MESSAGE_SEND, data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void emitCreateNews(News news) {
        JSONObject data = new JSONObject();
        try {
            Gson gson = new Gson();
            String objectNews = gson.toJson(news);
            data.put("news", objectNews);
            emit(Event.NEWS, data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void emitOnComment(Comment comment){
        JSONObject data = new JSONObject();
        try {
            Gson gson = new Gson();
            String objectComment = gson.toJson(comment);
            data.put("comment", objectComment);
            emit(Event.COMMENT, data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void emitOnLikeNews(User user, News news) {
        JSONObject data = new JSONObject();
        try {
            Gson gson = new Gson();
            String objectNews = gson.toJson(news);
            String objectUser = gson.toJson(user);
            data.put("news", objectNews);
            data.put("user", objectUser);
            emit(Event.LIKE_NEWS, data);
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

    public void emitCall(int roomId, boolean isAudioCall) {
        JSONObject data = new JSONObject();
        try {
            data.put("roomId", roomId);
            data.put("isAudioCall", isAudioCall);
            emit(Event.CALL, data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void emitCallBusy(int status, int roomId) {
        JSONObject data = new JSONObject();
        try {
            data.put("status", status);
            data.put("roomId", roomId);
            emit(Event.CALL_BUSY, data);
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

    public void emitTurnOnCamera(int roomId, boolean isOn, int userId) {
        JSONObject data = new JSONObject();
        try {
            data.put("isOn", isOn);
            data.put("roomId", roomId);
            data.put("userId", userId);
            emit(Event.TURN_ON_CAMERA, data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getUsersOnline() {
        JSONObject data = new JSONObject();
        emit(Event.USER_ONLINE, data);
    }
}
