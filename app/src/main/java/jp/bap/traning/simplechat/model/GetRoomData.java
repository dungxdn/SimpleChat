package jp.bap.traning.simplechat.model;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GetRoomData extends RoomData {
    List<User> users;
}
