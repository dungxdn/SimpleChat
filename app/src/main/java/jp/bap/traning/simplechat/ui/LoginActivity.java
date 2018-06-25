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
import jp.bap.traning.simplechat.service.ApiClient;
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
        String userName = edtUserName.getText().toString();
        String password = edtPassword.getText().toString();
        if (userName.isEmpty() || password.isEmpty()) {
            indicatorView.hide();
            Toast.makeText(LoginActivity.this, "Please input usename and password!", Toast.LENGTH_SHORT).show();
        } else {
            indicatorView.show();
            mLoginPresenter.logIn(userName, password, new LoginView() {
                @Override
                public void onSuccess(UserResponse result) {
                    indicatorView.hide();
                    setResult(Common.REQUEST_LOGIN);
                    Log.d("LogIn", "LogIn Activity");
                    finish();
                }

                @Override
                public void onError(String message, int code) {
                    indicatorView.hide();
                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure() {

                }
            });
        }
    }

    @Click
    void btnSignUp() {
        indicatorView.hide();
        startActivity(new Intent(this, SignUpActivity_.class));
        finish();
    }

    public void init() {
        this.mLoginPresenter = new LoginPresenter();
    }
}
