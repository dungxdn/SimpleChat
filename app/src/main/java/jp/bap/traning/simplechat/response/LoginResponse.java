package jp.bap.traning.simplechat.response;

import jp.bap.traning.simplechat.model.User;
import lombok.Data;

/**
 * Created by dungpv on 6/14/18.
 */

@Data
public class LoginResponse extends BaseRespone {
    private User data;
}
