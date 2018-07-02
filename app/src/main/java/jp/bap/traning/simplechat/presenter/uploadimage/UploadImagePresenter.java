package jp.bap.traning.simplechat.presenter.uploadimage;

import java.io.File;

import jp.bap.traning.simplechat.response.ImageResponse;

/**
 * Created by Admin on 6/27/2018.
 */

public class UploadImagePresenter {
    private UploadImageInteractor mUploadImageInteractor;

    public UploadImagePresenter() {
        this.mUploadImageInteractor = new UploadImageInteractor();
    }
    public void uploadImage(String title, String description, String album, String account_url, File file, UploadImageView callback) {

        mUploadImageInteractor.uploadImage(title, description, album, account_url, file, new UploadImageView() {
            @Override
            public void onSuccess(ImageResponse result) {
                callback.onSuccess(result);
            }

            @Override
            public void onError(String message, int code) {
                callback.onError(message,code);
            }

            @Override
            public void onFailure() {
                callback.onFailure();
            }
        });
    }
}
