package jp.bap.traning.simplechat.presenter.addrooms;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import jp.bap.traning.simplechat.interfaces.ApiService;
import jp.bap.traning.simplechat.response.AddRoomResponse;
import jp.bap.traning.simplechat.response.UserResponse;
import jp.bap.traning.simplechat.service.ApiClient;
import jp.bap.traning.simplechat.utils.Common;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddRoomInteractor {

    public AddRoomInteractor() {
    }

    public void addRoom(List<Integer> ids, int type, AddRoomView callback) {

        Call<AddRoomResponse> mCallUser = ApiClient.getService().createRoom(ids, type);
        mCallUser.enqueue(new Callback<AddRoomResponse>() {
            @Override
            public void onResponse(Call<AddRoomResponse> call, Response<AddRoomResponse> response) {
                callback.onAddRoomSuccess(response.body());
                Log.e("addRoom", "success");
            }

            @Override
            public void onFailure(Call<AddRoomResponse> call, Throwable t) {
                callback.onAddRoomFail();
                Log.e("addRoom", "fail");
            }
        });
    }
}
