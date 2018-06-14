package jp.bap.traning.simplechat.Interactor;

import jp.bap.traning.simplechat.Presenter.SharedPrefs;
import jp.bap.traning.simplechat.RetrofitAPIHandler.RetrofitAPIUtils;
import jp.bap.traning.simplechat.Response.UserResponse;
import jp.bap.traning.simplechat.interfaces.APIInterface;
import jp.bap.traning.simplechat.interfaces.LoginInterface;
import jp.bap.traning.simplechat.model.User;
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
                if (response.body().getStatus() == 200) {
                    User user = response.body().getData();
                    //save db

                    SharedPrefs.getInstance().putData(SharedPrefs.KEY_SAVE_ID, user.getId());
                    mLoginInterface.onLoginSuccess(response.body());
                } else {
                    mLoginInterface.onLoginFailed();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                mLoginInterface.onLoginFailed();
            }
        });
    }
}
