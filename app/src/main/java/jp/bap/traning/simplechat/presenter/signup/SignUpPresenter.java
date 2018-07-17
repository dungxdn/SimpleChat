package jp.bap.traning.simplechat.presenter.signup;

import jp.bap.traning.simplechat.response.SignUpResponse;

public class SignUpPresenter {

    private SignUpInteractor mSignUpInteractor;

    public SignUpPresenter() {
        mSignUpInteractor = new SignUpInteractor();
    }

    public void signUp(String userName, String firstName, String lastName, String password,
                       SignUpView callback) {
        mSignUpInteractor.signUp(userName, firstName, lastName, password, new SignUpView() {

            @Override
            public void onSuccess(SignUpResponse result) {
                callback.onSuccess(result);
            }

            @Override
            public void onFailure() {
                callback.onFailure();
            }

            @Override
            public void onError(String message, int code) {
                callback.onError(message, code);
            }
        });
    }
}
