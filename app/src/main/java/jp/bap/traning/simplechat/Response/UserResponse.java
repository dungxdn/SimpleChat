package jp.bap.traning.simplechat.Response;

import jp.bap.traning.simplechat.model.User;
import lombok.Data;

@Data
public class UserResponse extends BaseResponse {
    private User user;
}
