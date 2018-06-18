package jp.bap.traning.simplechat.presenter.signup;

import jp.bap.traning.simplechat.response.SignUpResponse;

public class SignUpPresenter implements SignUpView{

    private SignUpInteractor mSignUpInteractor;
    private SignUpView mSignUpView;

    public SignUpPresenter(SignUpView signUpView){
        mSignUpInteractor = new SignUpInteractor();
        mSignUpView = signUpView;
    }

    public void signUp(String userName, String firstName, String lastName, String password){
        mSignUpInteractor.signUp(userName, firstName, lastName, password, mSignUpView);
    }

    @Override
    public void onSiginUpSuccess(SignUpResponse signUpResponse) {
        mSignUpView.onSiginUpSuccess(signUpResponse);
    }

    @Override
    public void onSignUpFailed() {

    }

    @Override
    public void onSuccess(SignUpResponse result) {

    }

    @Override
    public void onError(String message, int code) {

    }
}
