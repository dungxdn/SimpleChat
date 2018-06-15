package jp.bap.traning.simplechat.response;

import java.util.List;
import jp.bap.traning.simplechat.model.Room;
import jp.bap.traning.simplechat.model.RoomData;
import lombok.Data;

@Data
public class RoomResponse extends BaseResponse {
    private RoomData data;
}
