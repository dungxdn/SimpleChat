package jp.bap.traning.simplechat.Presenter;

import jp.bap.traning.simplechat.Interactor.LoginInteractor;
import jp.bap.traning.simplechat.Response.UserResponse;
import jp.bap.traning.simplechat.database.UserDAO;
import jp.bap.traning.simplechat.interfaces.LoginInterface;

public class LoginPresenter implements LoginInterface {
    private LoginInterface mLoginInterface;
    private LoginInteractor mLoginInteractor;

    public LoginPresenter(LoginInterface loginInterface) {
        mLoginInteractor = new LoginInteractor(this);
        mLoginInterface = loginInterface;
    }

    public void logIn(String userName, String password) {
        mLoginInteractor.login(userName, password);
    }

    @Override
    public void onLoginSuccess(UserResponse user) {
        mLoginInterface.onLoginSuccess(user);
    }

    @Override
    public void onLoginFailed() {
        mLoginInterface.onLoginFailed();
    }
}
