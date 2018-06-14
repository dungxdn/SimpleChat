package jp.bap.traning.simplechat.interfaces;

import jp.bap.traning.simplechat.Response.UserResponse;

public interface LoginInterface {
    void onLoginSuccess(UserResponse userResponse);

    void onLoginFailed();
}
