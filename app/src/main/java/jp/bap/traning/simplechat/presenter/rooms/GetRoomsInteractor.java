package jp.bap.traning.simplechat.presenter.rooms;

import android.util.Log;
import jp.bap.traning.simplechat.database.UserDAO;
import jp.bap.traning.simplechat.model.User;
import jp.bap.traning.simplechat.presenter.login.LoginView;
import jp.bap.traning.simplechat.response.RoomResponse;
import jp.bap.traning.simplechat.response.UserResponse;
import jp.bap.traning.simplechat.service.ApiClient;
import jp.bap.traning.simplechat.utils.SharedPrefs;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dungpv on 6/15/18.
 */

public class GetRoomsInteractor {
    public void request(GetRoomsView callback) {
        Call<RoomResponse> mCallUser = ApiClient.getService().getListRoom();
        mCallUser.enqueue(new Callback<RoomResponse>() {
            @Override
            public void onResponse(Call<RoomResponse> call, Response<RoomResponse> response) {
                if (response.body().getStatus() == 200) {
                    Log.e("rooms", response.body().getData().toString());
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(response.body().getMessage(), response.body().getStatus());
                }
            }

            @Override
            public void onFailure(Call<RoomResponse> call, Throwable t) {
                callback.onError("", 400);
            }
        });
    }
}
