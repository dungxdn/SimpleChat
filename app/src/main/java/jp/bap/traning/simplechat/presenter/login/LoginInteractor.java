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

class LoginInteractor {

    LoginInteractor() {
    }

    void login(String userName, String password, LoginView callback) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Common.URL_SERVER)
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
                    callback.onSuccess(response.body());
                    Log.d("Login", "onSuccess: " + response.body().getData().getAvatar());
                } else {
                    callback.onError(response.body().getMessage(), response.body().getStatus());
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                callback.onFailure();
            }
        });
        Log.d("LogIn", "LogIn Interactor");
    }
}
