package jp.bap.traning.simplechat.model;

import java.util.Comparator;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

import javax.annotation.Nullable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Data
@EqualsAndHashCode(callSuper = false)
public class Room extends RealmObject {
    @PrimaryKey
    int roomId;
    @Nullable
    String roomName;
    @Nullable
    String avatar;
    /*
    Room 2 person -> type = 0, avatar = null;
    Group -> type = 1, avatar = link;
     */
    int type;
    RealmList<User> users;
    Message lastMessage;

    public static Comparator<Room> roomComparator = new Comparator<Room>() {
        @Override
        //if String firstName1 > String firstName2 -> return >0; if == -> return 0; if < ->
        // return <0
        public int compare(Room t, Room t1) {
            if (t1.getLastMessage() != null) {
                if (t.getLastMessage() == null) {
                    return 1;
                } else {
                    if (t1.getLastMessage().getId() > t.getLastMessage().getId()) {
                        return 1;
                    } else if (t1.getLastMessage().getId() == t.getLastMessage().getId()) {
                        return 0;
                    } else {
                        return -1;
                    }
                }
            } else {
                return -1;
            }
        }
    };
}
