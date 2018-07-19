package jp.bap.traning.simplechat.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dungpv on 6/7/18.
 */

public enum Event {
    MESSAGE_SEND("sendMessage"),
    MESSAGE_RECEIVER("receiverMessage"),
    CONNECT("connect"),
    UNKNOWN(""),
    USER_ONLINE("getUsersOnline"),
    ON_USER_ONLINE("onUserOnline"),
    ON_USER_OFFLINE("onUserOffline"),
    CREATE_ROOM("onCreatedRoom"),
    NEWS("news"),
    LIKE_NEWS("likeNews"),
    COMMENT("comment"),
    CALL_BUSY("callBusy"),
    //Call
    CALL("call"),
    CALL_CONTENT("callContent"),
    CALL_ACCEPT("callAccept"),
    CALL_STOP("callStop"),
    //camera
    TURN_ON_CAMERA("turnOnCamera");

    String mEvent;

    Event(String e) {
        mEvent = e;
    }

    public String getEvent() {
        return mEvent;
    }

    private static final Map<String, Event> mapByEvent = new HashMap<>();

    static {
        for (Event type : Event.values()) {
            mapByEvent.put(type.mEvent, type);
        }
    }

    public static Map<String, Event> getAllType() {
        return mapByEvent;
    }

    public static Event fromEvent(String event) {
        Event type = mapByEvent.get(event);
        if (type == null)
            return Event.UNKNOWN;
        return type;
    }
}
