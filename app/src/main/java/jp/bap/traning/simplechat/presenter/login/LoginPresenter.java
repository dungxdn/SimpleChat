package jp.bap.traning.simplechat.presenter.login;

import jp.bap.traning.simplechat.response.UserResponse;

public class LoginPresenter implements LoginView {
    private LoginView mLoginView;
    private LoginInteractor mLoginInteractor;

    public LoginPresenter(LoginView loginView) {
        mLoginInteractor = new LoginInteractor();
        mLoginView = loginView;
    }

    public void logIn(String userName, String password) {
        mLoginInteractor.login(userName, password, mLoginView);
    }

    @Override
    public void onLoginSuccess(UserResponse userResponse) {
        mLoginView.onSuccess(userResponse);

    }

    @Override
    public void onLoginFailed() {
        mLoginView.onLoginFailed();
    }

    @Override
    public void onSuccess(UserResponse result) {
    }

    @Override
    public void onError(String message, int code) {
    }
}
