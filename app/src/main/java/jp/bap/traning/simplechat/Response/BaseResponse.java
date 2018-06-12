package jp.bap.traning.simplechat.Response;

import lombok.Data;

@Data
public class BaseResponse {
    private int status;
    private String message;
}