package jp.bap.traning.simplechat;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dungpv on 6/7/18.
 */

public enum Event {
    MESSAGE_SEND("sendMessage"),
    MESSAGE_RECEIVER("receiverMessage"),

    //Call
    CALL("call"),
    CALL_CONTENT("callContent"),
    CALL_ACCEPT("callAccept"),
    CALL_STOP("callStop"),

    //Status
    CONNECTED("connected"),
    UNKNOWN("");

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

    public static Event fromAction(String action) {
        Event type = mapByEvent.get(action);
        if (type == null)
            return Event.UNKNOWN;
        return type;
    }
}
