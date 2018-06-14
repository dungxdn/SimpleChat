package jp.bap.traning.simplechat.response;

import lombok.Data;

@Data
public class BaseResponse {
    private int status;
    private String message;
}