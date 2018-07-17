package jp.bap.traning.simplechat.response;

import jp.bap.traning.simplechat.model.GetRoomData;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GetRoomResponse extends BaseResponse {
    GetRoomData data;
}
