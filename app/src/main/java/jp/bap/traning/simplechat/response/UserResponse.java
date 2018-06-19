package jp.bap.traning.simplechat.response;

import jp.bap.traning.simplechat.model.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserResponse extends BaseResponse {
    private User data;
}
