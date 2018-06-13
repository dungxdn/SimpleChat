package jp.bap.traning.simplechat.chat;

/**
 * Created by dungpv on 6/7/18.
 */

public enum Event {
    MESSAGE_SEND("sendMessage"),
    MESSAGE_RECEIVER("receiverMessage");

    String mEvent;
    Event(String e) {
        mEvent = e;
    }

    public String getEvent() {
        return mEvent;
    }
}
