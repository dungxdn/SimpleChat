package jp.bap.traning.simplechat.response;

import jp.bap.traning.simplechat.model.UploadedImage;
import lombok.Data;

/**
 * Created by Admin on 6/27/2018.
 */
@Data
public class ImageResponse {
    public boolean success;
    public int status;
    public UploadedImage data;

    @Override public String toString() {
        return "ImageResponse{" +
                "success=" + success +
                ", status=" + status +
                ", data=" + data.toString() +
                '}';
    }
}