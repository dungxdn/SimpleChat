package jp.bap.traning.simplechat.presenter.login;

import jp.bap.traning.simplechat.utils.SharedPrefs;
import jp.bap.traning.simplechat.response.UserResponse;
import jp.bap.traning.simplechat.database.UserDAO;
import jp.bap.traning.simplechat.model.User;
import jp.bap.traning.simplechat.service.ApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginInteractor {

    public LoginInteractor() {
    }

    public void login(String userName, String password, LoginView callback) {
        Call<UserResponse> mCall = ApiClient.getService().getUser(userName, password);
        mCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.body().getStatus() == 200) {
                    User user = response.body().getData();
                    //save db
                    new UserDAO().insertOrUpdate(user);

                    SharedPrefs.getInstance().putData(SharedPrefs.KEY_SAVE_ID, user.getId());
                    callback.onLoginSuccess(response.body());
                } else {
                    callback.onLoginFailed();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                callback.onLoginFailed();
            }
        });
    }
}