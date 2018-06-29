package jp.bap.traning.simplechat.model;

import java.util.Comparator;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import javax.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Room extends RealmObject {
    @PrimaryKey
    int roomId;
    @Nullable
    String roomName;
    String avatar;
    /*
    Room 2 person -> type = 0;
    Group -> type = 1;
     */
    int type;
    RealmList<User> users;
    Message lastMessage;
}
