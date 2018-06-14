package jp.bap.traning.simplechat.Presenter.login;

import jp.bap.traning.simplechat.Presenter.BaseView;
import jp.bap.traning.simplechat.Response.UserResponse;

/**
 * Created by dungpv on 6/14/18.
 */

public interface LoginView extends BaseView<UserResponse> {
    void onLoginSuccess(UserResponse userResponse);

    void onLoginFailed();
}
