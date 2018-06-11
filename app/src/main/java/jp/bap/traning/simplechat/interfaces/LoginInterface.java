package jp.bap.traning.simplechat.interfaces;

public interface LoginInterface {
    void loginSuccess(String email,String password);
    void loginFailed();
}
