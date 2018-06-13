package jp.bap.traning.simplechat.interfaces;

import org.json.JSONObject;

import jp.bap.traning.simplechat.chat.Event;

public interface ListenerInterface {
    void onEvent(Event event, JSONObject data);
    void onEmit(Event event, JSONObject data);
}
