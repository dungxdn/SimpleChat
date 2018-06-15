package jp.bap.traning.simplechat.presenter.login;

import android.content.Context;
import android.util.Log;
import jp.bap.traning.simplechat.BaseApp;
import jp.bap.traning.simplechat.response.RoomResponse;
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
        Call<UserResponse> mCallUser = ApiClient.getService().getUser(userName, password);
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

        Call<RoomResponse> mCallRooms = ApiClient.getService().getListRoom();
        mCallRooms.enqueue(new Callback<RoomResponse>() {
            @Override
            public void onResponse(Call<RoomResponse> call, Response<RoomResponse> response) {
                if (response.body().getStatus() == 200){
                    if (response.body().getData().getRooms().size() != 0){
                        Log.e("rooms", response.body().getData().getRooms().get(0).toString());
                    }else {
                        Log.e("rooms", "empty");
                    }
                }
            }

            @Override
            public void onFailure(Call<RoomResponse> call, Throwable t) {
                Log.e("rooms", "fail");
            }
        });
    }
}
