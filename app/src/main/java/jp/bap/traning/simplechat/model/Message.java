package jp.bap.traning.simplechat.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Message extends RealmObject {
    @PrimaryKey
    private long id;
    private String content;
    private int userID;
    private int roomID;
    private String type;

    public Message() {
    }

    public Message(String content, int userID, int roomID, String type) {
        this.id = System.currentTimeMillis();
        this.content = content;
        this.userID = userID;
        this.roomID = roomID;
        this.type = type;
    }
}
