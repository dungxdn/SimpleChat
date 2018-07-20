package jp.bap.traning.simplechat.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import jp.bap.traning.simplechat.presenter.signup.SignUpPresenter;
import jp.bap.traning.simplechat.presenter.signup.SignUpView;
import jp.bap.traning.simplechat.response.SignUpResponse;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import jp.bap.traning.simplechat.R;

@EActivity(R.layout.activity_sign_up)
public class SignUpActivity extends BaseActivity {

    private SignUpPresenter mSignUpPresenter;

    @ViewById
    AVLoadingIndicatorView indicatorView;
    @ViewById
    EditText edtUsername;
    @ViewById
    EditText edtFirstname;
    @ViewById
    EditText edtLastname;
    @ViewById
    EditText edtPassword;
    @ViewById
    EditText edtConfirmPassword;
    @ViewById
    ProgressBar mProgressBar;

    @Override
    public void afterView() {
        mSignUpPresenter = new SignUpPresenter();
        mProgressBar.setVisibility(View.GONE);
    }

    @Click
    void btnSignUp() {
        if (edtUsername.getText().toString().isEmpty() || edtFirstname.getText()
                .toString()
                .isEmpty() || edtLastname.getText().toString().isEmpty() || edtPassword.getText()
                .toString()
                .isEmpty() || edtConfirmPassword.getText().toString().isEmpty()) {
            Toast.makeText(this, getResources().getString(R.string.text_please_inpur_all_info), Toast.LENGTH_SHORT).show();
        } else {
            if (edtPassword.getText().toString().
                    equals(edtConfirmPassword.getText().toString())) {
                showProgressBar(mProgressBar);
                mSignUpPresenter.signUp(edtUsername.getText().toString(),
                        edtFirstname.getText().toString(), edtLastname.getText().toString(),
                        edtPassword.getText().toString(), new SignUpView() {

                            @Override
                            public void onSuccess(SignUpResponse result) {
                                edtUsername.getText().clear();
                                edtFirstname.getText().clear();
                                edtLastname.getText().clear();
                                edtPassword.getText().clear();
                                edtConfirmPassword.getText().clear();
                                hiddenProgressBar(mProgressBar);
                                Toast.makeText(SignUpActivity.this, getResources().getString(R.string.text_success_register),
                                        Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            @Override
                            public void onError(String message, int code) {
                                Log.d("SignUp Error: ", message + ", " + code);
                                Toast.makeText(SignUpActivity.this,
                                        getResources().getString(R.string.text_error_register), Toast.LENGTH_SHORT)
                                        .show();
                            }

                            @Override
                            public void onFailure() {
                                Toast.makeText(SignUpActivity.this,
                                        getResources().getString(R.string.text_fail_register), Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });
            } else {
                Toast.makeText(this, "Password is not confirm!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Click
    void btnSignIn() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
