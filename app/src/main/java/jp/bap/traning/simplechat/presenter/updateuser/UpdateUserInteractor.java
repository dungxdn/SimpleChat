package jp.bap.traning.simplechat.presenter.updateuser;

import jp.bap.traning.simplechat.response.BaseResponse;
import jp.bap.traning.simplechat.service.ApiClient;
import jp.bap.traning.simplechat.utils.Common;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class UpdateUserInteractor {

    UpdateUserInteractor() {
    }

    void updateUser(String firstName, String lastName, String avatar, UpdateUserView callback) {

        Call<BaseResponse> mCalluser = ApiClient.getService().updateUser(firstName, lastName, avatar);
        mCalluser.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.body().getStatus() == Common.STATUS_SUCCESS) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(response.body().getMessage(), response.body().getStatus());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                callback.onFailure();
            }
        });
    }
}
