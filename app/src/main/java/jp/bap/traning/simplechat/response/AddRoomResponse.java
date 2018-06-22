package jp.bap.traning.simplechat.response;

import jp.bap.traning.simplechat.model.RoomData;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AddRoomResponse extends BaseResponse {
    RoomData data;
}
