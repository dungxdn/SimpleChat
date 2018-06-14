package jp.bap.traning.simplechat.presenter.login;
import jp.bap.traning.simplechat.Response.UserResponse;
import jp.bap.traning.simplechat.presenter.BaseView;

/**
 * Created by dungpv on 6/14/18.
 */

public interface LoginView extends BaseView<UserResponse> {
    void onLoginSuccess(UserResponse userResponse);

    void onLoginFailed();
}
