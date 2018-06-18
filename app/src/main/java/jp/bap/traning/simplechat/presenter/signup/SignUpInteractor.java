package jp.bap.traning.simplechat.presenter.signup;

import jp.bap.traning.simplechat.response.SignUpResponse;
import jp.bap.traning.simplechat.service.ApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpInteractor {

    public void signUp(String userName, String firstName, String lastName,
            String password, SignUpView callback){
        Call<SignUpResponse> mCallUser = ApiClient.getService().addUser(userName,
                firstName, lastName, password);
        mCallUser.enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                callback.onSiginUpSuccess(response.body());
            }

            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                callback.onSignUpFailed();
            }
        });
    }
}
