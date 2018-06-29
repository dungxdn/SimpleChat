package jp.bap.traning.simplechat.interfaces;

import java.util.List;

import jp.bap.traning.simplechat.response.AddRoomResponse;
import jp.bap.traning.simplechat.response.BaseResponse;
import jp.bap.traning.simplechat.response.GetRoomResponse;
import jp.bap.traning.simplechat.response.RoomResponse;
import jp.bap.traning.simplechat.response.SignUpResponse;
import jp.bap.traning.simplechat.response.UserResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {
    //login
    @FormUrlEncoded
    @POST("/user/login")
    Call<UserResponse> getUser(@Field("userName") String userName,
                               @Field("password") String password);

    //register
    @FormUrlEncoded
    @POST("user/register")
    Call<SignUpResponse> addUser(@Field("userName") String userName,
                                 @Field("firstName") String firstName,
                                 @Field("lastName") String lastName,
                                 @Field("password") String password);

    //get list room
    @GET("/rooms")
    Call<RoomResponse> getListRoom();

    // add room
    @FormUrlEncoded
    @POST("/room")
    Call<AddRoomResponse> createRoom(@Field("ids") List<Integer> ids,
                                     @Field("type") int type,
                                     @Field("roomName") String roomName);

    // get room
    @GET("/room/{roomId}")
    Call<GetRoomResponse> getRoom(@Path("roomId") int roomId);

    // update user
    @FormUrlEncoded
    @POST("/user")
    Call<BaseResponse> updateUser(@Field("firstName") String firstName,
                                  @Field("lastName") String lastName,
                                  @Field("avatar") String avatar);

}
