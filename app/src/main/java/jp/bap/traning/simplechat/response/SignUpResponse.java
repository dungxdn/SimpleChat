package jp.bap.traning.simplechat.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SignUpResponse extends BaseResponse{
    int id;
}
