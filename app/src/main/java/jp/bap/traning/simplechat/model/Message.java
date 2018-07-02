package jp.bap.traning.simplechat.model;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Message extends RealmObject implements Parcelable {
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

    protected Message(Parcel in) {
        id = in.readLong();
        content = in.readString();
        userID = in.readInt();
        roomID = in.readInt();
        type = in.readString();
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(content);
        parcel.writeInt(userID);
        parcel.writeInt(roomID);
        parcel.writeString(type);
    }
}
