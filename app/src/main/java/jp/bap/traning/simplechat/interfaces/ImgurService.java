package jp.bap.traning.simplechat.interfaces;

import jp.bap.traning.simplechat.response.ImageResponse;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by Admin on 6/27/2018.
 */

public interface ImgurService {
    @Multipart
    @Headers({
            "Authorization: Client-ID a7dd900ff137757"
    })
    @POST("image")
    Call<ImageResponse> postImage(
            @Query("title") String title,
            @Query("description") String description,
            @Query("album") String albumId,
            @Query("account_url") String username,
            @Part MultipartBody.Part file);
}
