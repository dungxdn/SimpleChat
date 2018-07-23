package jp.bap.traning.simplechat.ui;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import jp.bap.traning.simplechat.database.UserDAO;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import jp.bap.traning.simplechat.presenter.login.LoginPresenter;
import jp.bap.traning.simplechat.presenter.login.LoginView;
import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.response.UserResponse;
import jp.bap.traning.simplechat.utils.Common;

@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity {
    private LoginPresenter mLoginPresenter;
    @ViewById
    EditText edtUserName;
    @ViewById
    EditText edtPassword;
    @ViewById
    ProgressBar mProgressBar;

    @Override
    public void afterView() {
        init();
    }

    @Click
    void btnLogin() {
        Common.hideKeyboard(LoginActivity.this);
        showProgressBar(mProgressBar);
        String userName = edtUserName.getText().toString();
        String password = edtPassword.getText().toString();
        if (!isConnectedNetwork()) {
            NetworkActivity_.intent(this).start();
            hiddenProgressBar(mProgressBar);
        } else if (userName.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, getResources().getString(R.string.text_not_input_username_pass),
                    Toast.LENGTH_SHORT).show();
            hiddenProgressBar(mProgressBar);
        } else {
            mLoginPresenter.logIn(userName, password, new LoginView() {
                @Override
                public void onSuccess(UserResponse result) {
                    hiddenProgressBar(mProgressBar);
                    setResult(Common.REQUEST_LOGIN);
//                    new UserDAO().insertOrUpdate(result.getData());
                    finish();
                }

                @Override
                public void onError(String message, int code) {
                    hiddenProgressBar(mProgressBar);
                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure() {
                    hiddenProgressBar(mProgressBar);
                }
            });
        }
    }

    @Click
    void btnSignUp() {
        edtUserName.setText("");
        edtPassword.setText("");
        startActivity(new Intent(this, SignUpActivity_.class));
    }

    public void init() {
        mProgressBar.setVisibility(View.GONE);
        this.mLoginPresenter = new LoginPresenter();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}