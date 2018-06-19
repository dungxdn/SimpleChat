package jp.bap.traning.simplechat.presenter.login;

import android.util.Log;

import jp.bap.traning.simplechat.interfaces.ApiService;
import jp.bap.traning.simplechat.utils.Common;
import jp.bap.traning.simplechat.utils.SharedPrefs;
import jp.bap.traning.simplechat.response.UserResponse;
import jp.bap.traning.simplechat.database.UserDAO;
import jp.bap.traning.simplechat.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginInteractor {

    public LoginInteractor() {
    }

    public void login(String userName, String password, LoginView callback) {
        Log.e("login", "loginIniteractor");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Common.URL_SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<UserResponse> mCallUser = apiService.getUser(userName, password);
        mCallUser.enqueue(new Callback<UserResponse>() {
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
