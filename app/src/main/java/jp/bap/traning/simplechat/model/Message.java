package jp.bap.traning.simplechat.model;

import lombok.Data;

@Data
public class Message {
    private int id;
    private String content;
    private int userID;
    private int roomID;

    public Message(int id, String content, User user, int roomID) {
        this.id = id;
        this.content = content;
        this.userID = user.getId();
        this.roomID = roomID;
    }
}
