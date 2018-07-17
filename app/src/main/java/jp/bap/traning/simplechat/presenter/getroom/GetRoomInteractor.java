package jp.bap.traning.simplechat.presenter.getroom;

import jp.bap.traning.simplechat.response.GetRoomResponse;
import jp.bap.traning.simplechat.service.ApiClient;
import jp.bap.traning.simplechat.utils.Common;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class GetRoomInteractor {

    GetRoomInteractor() {
    }

    void getRoom(int roomId, GetRoomView callback) {
        Call<GetRoomResponse> mCallUser = ApiClient.getService().getRoom(roomId);
        mCallUser.enqueue(new Callback<GetRoomResponse>() {
            @Override
            public void onResponse(Call<GetRoomResponse> call, Response<GetRoomResponse> response) {
                if (response.body().getStatus() == Common.STATUS_SUCCESS) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(response.body().getMessage(), response.body().getStatus());
                }
            }

            @Override
            public void onFailure(Call<GetRoomResponse> call, Throwable t) {
                callback.onFailure();
            }
        });
    }
}
