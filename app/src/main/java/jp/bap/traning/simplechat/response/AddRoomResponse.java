package jp.bap.traning.simplechat.response;

import jp.bap.traning.simplechat.model.RoomData;
import lombok.Data;

@Data
public class AddRoomResponse extends BaseResponse {
    RoomData data;
}
