package jp.bap.traning.simplechat.interfaces;

import jp.bap.traning.simplechat.response.BaseResponse;
import jp.bap.traning.simplechat.response.RoomResponse;
import jp.bap.traning.simplechat.response.UserResponse;
import lombok.Getter;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @FormUrlEncoded
    @POST("/user/login")
    Call<UserResponse> getUser(@Field("userName") String userName,
            @Field("password") String password);

    @FormUrlEncoded
    @POST("user/register")
    Call<BaseResponse> addUser(@Field("userName") String userName,
            @Field("firstName") String firstName,
            @Field("lastName") String lastName,
            @Field("password") String password);

    @GET("/rooms")
    Call<RoomResponse> getListRoom();

}
