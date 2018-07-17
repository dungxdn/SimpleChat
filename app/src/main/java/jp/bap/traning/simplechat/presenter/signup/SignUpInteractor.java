package jp.bap.traning.simplechat.presenter.signup;

import jp.bap.traning.simplechat.response.SignUpResponse;
import jp.bap.traning.simplechat.service.ApiClient;
import jp.bap.traning.simplechat.utils.Common;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class SignUpInteractor {

    void signUp(String userName, String firstName, String lastName, String password,
                SignUpView callback) {
        Call<SignUpResponse> mCallUser =
                ApiClient.getService().addUser(userName, firstName, lastName, password);
        mCallUser.enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                if (response.body().getStatus() == Common.STATUS_SUCCESS) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(response.body().getMessage(), response.body().getStatus());
                }
            }

            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                callback.onFailure();
            }
        });
    }
}
