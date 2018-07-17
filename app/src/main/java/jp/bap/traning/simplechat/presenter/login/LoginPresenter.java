package jp.bap.traning.simplechat.presenter.login;

import android.util.Log;

import jp.bap.traning.simplechat.response.UserResponse;

public class LoginPresenter {
    private LoginInteractor mLoginInteractor;

    public LoginPresenter() {
        mLoginInteractor = new LoginInteractor();
    }

    public void logIn(String userName, String password, LoginView callback) {
        mLoginInteractor.login(userName, password, new LoginView() {
            @Override
            public void onSuccess(UserResponse result) {
                callback.onSuccess(result);
            }

            @Override
            public void onError(String message, int code) {
                callback.onError(message, code);
            }

            @Override
            public void onFailure() {
                callback.onFailure();
            }
        });
        Log.d("LogIn", "LogIn Presenter");
    }
}
