package jp.bap.traning.simplechat.model;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Message {
    private int id;
    private String content;
    private int userID;
    private int roomID;

    public Message(int id, String content, int userID, int roomID) {
        this.id = id;
        this.content = content;
        this.userID = userID;
        this.roomID = roomID;
    }
}
