package jp.bap.traning.simplechat.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import jp.bap.traning.simplechat.presenter.login.LoginPresenter;
import jp.bap.traning.simplechat.presenter.login.LoginView;
import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.response.UserResponse;
import jp.bap.traning.simplechat.utils.Common;
import jp.bap.traning.simplechat.utils.SharedPrefs;

@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity {
    private LoginPresenter mLoginPresenter;

    @ViewById
    EditText edtUserName;
    @ViewById
    EditText edtPassword;
    @ViewById
    AVLoadingIndicatorView indicatorView;

    @Override
    public void afterView() {
        init();
    }

    @Click
    void btnLogin() {
        indicatorView.show();
        String userName = edtUserName.getText().toString();
        String password = edtPassword.getText().toString();
        if (userName.isEmpty() || password.isEmpty()) {
            indicatorView.hide();
            Toast.makeText(LoginActivity.this, "Please input usename and password!", Toast.LENGTH_SHORT).show();
        } else {
            mLoginPresenter.logIn(userName, password);
        }
    }

    @Click
    void btnSignUp() {
        indicatorView.hide();
        SignUpActivity_.intent(this).start();
    }

    @Override
    public void onBackPressed() {

    }

    public void init() {
        this.mLoginPresenter = new LoginPresenter(new LoginView() {
            @Override
            public void onLoginSuccess(UserResponse userResponse) {
                indicatorView.hide();
                Common.connectToServerSocket(LoginActivity.this, Common.URL_SERVER, userResponse.getData().getId());
            }

            @Override
            public void onLoginFailed() {
                indicatorView.hide();
                Toast.makeText(LoginActivity.this, "Email or Password wrong ! Please try again !", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(UserResponse result) {

            }

            @Override
            public void onError(String message, int code) {

            }
        });
    }

    @Override
    public void onConnectedSocket() {
        super.onConnectedSocket();
        MainActivity_.intent(this).start();
        finish();
    }
}
