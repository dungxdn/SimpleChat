package jp.bap.traning.simplechat.model;

import java.util.List;
import lombok.Data;

@Data
public class Room {
    int roomId;
    List<User> users;
}
