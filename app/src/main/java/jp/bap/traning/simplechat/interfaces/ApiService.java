package jp.bap.traning.simplechat.interfaces;

import jp.bap.traning.simplechat.response.LoginResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by dungpv on 6/14/18.
 */

public interface ApiService {

    @FormUrlEncoded
    @POST("/user/login")
    Call<LoginResponse> login(@Field("userName") String userName, @Field("password") String password);
}
