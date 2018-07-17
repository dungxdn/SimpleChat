package jp.bap.traning.simplechat.presenter.uploadimage;

import android.util.Log;

import java.io.File;

import jp.bap.traning.simplechat.response.ImageResponse;
import jp.bap.traning.simplechat.service.ImgurClient;
import jp.bap.traning.simplechat.utils.Common;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Admin on 6/27/2018.
 */

class UploadImageInteractor {
    UploadImageInteractor() {
    }

    void uploadImage(String title, String description, String album, String account_url, File file, UploadImageView callback) {

        Call<ImageResponse> call = ImgurClient.getService().postImage(
                title,
                description,
                album,
                account_url,
                MultipartBody.Part.createFormData(
                        "image",
                        file.getName(),
                        RequestBody.create(MediaType.parse("image/*"), file)
                ));
        call.enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                    Log.d("upload", "success");
                } else{
                    callback.onError("",0);
                }
            }

            @Override
            public void onFailure(Call<ImageResponse> call, Throwable t) {
                callback.onFailure();
                Log.d("upload", "Failure");

            }
        });
    }

}
