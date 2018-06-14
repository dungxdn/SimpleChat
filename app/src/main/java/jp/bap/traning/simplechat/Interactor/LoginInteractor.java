package jp.bap.traning.simplechat.Interactor;

import jp.bap.traning.simplechat.RetrofitAPIHandler.RetrofitAPIUtils;
import jp.bap.traning.simplechat.Response.UserResponse;
import jp.bap.traning.simplechat.interfaces.APIInterface;
import jp.bap.traning.simplechat.interfaces.LoginInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginInteractor {

    private APIInterface mAPIInterface;
    private LoginInterface mLoginInterface;

    public LoginInteractor(LoginInterface loginInterface) {
        mLoginInterface = loginInterface;
    }

    public void login(String userName, String password){
        mAPIInterface = RetrofitAPIUtils.getApiClient();
        Call<UserResponse> mCall = mAPIInterface.getUser(userName, password);
        mCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                mLoginInterface.onLoginSuccess(response.body());
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                mLoginInterface.onLoginFailed();
            }
        });
    }
}
