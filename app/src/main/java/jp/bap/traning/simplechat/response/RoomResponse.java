package jp.bap.traning.simplechat.response;

import java.util.List;
import jp.bap.traning.simplechat.model.Room;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class RoomResponse extends BaseResponse {
    private List<Room> data;
}
