package jp.bap.traning.simplechat.presenter.rooms;

import java.util.List;

import jp.bap.traning.simplechat.database.RoomDAO;
import jp.bap.traning.simplechat.model.Room;
import jp.bap.traning.simplechat.response.RoomResponse;
import jp.bap.traning.simplechat.service.ApiClient;
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
                    List<Room> rooms = response.body().getData();
                    new RoomDAO().insertOrUpdate(rooms);
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
