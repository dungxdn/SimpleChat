package jp.bap.traning.simplechat.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

import android.widget.Toast;
import com.wang.avi.AVLoadingIndicatorView;

import jp.bap.traning.simplechat.presenter.signup.SignUpPresenter;
import jp.bap.traning.simplechat.presenter.signup.SignUpView;
import jp.bap.traning.simplechat.response.SignUpResponse;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import jp.bap.traning.simplechat.R;

@EActivity(R.layout.activity_sign_up)
public class SignUpActivity extends Activity implements SignUpView{

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

    @AfterViews
    void afterView(){
        mSignUpPresenter = new SignUpPresenter(this);
    }

    @Click
    void btnSignUp() {
        if (edtUsername.getText().toString().isEmpty()||
                edtFirstname.getText().toString().isEmpty() ||
                edtLastname.getText().toString().isEmpty() ||
                edtPassword.getText().toString().isEmpty() ||
                edtConfirmPassword.getText().toString().isEmpty()){
            Toast.makeText(this, "Empty field!", Toast.LENGTH_SHORT).show();
        }else{
            if (edtPassword.getText().toString().equals(edtConfirmPassword.getText().toString())){
                mSignUpPresenter.signUp(edtUsername.getText().toString(), edtFirstname.getText().toString(),
                        edtLastname.getText().toString(), edtPassword.getText().toString());
            }else {
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

    @Override
    public void onSiginUpSuccess(SignUpResponse signUpResponse) {
        edtUsername.getText().clear();
        edtFirstname.getText().clear();
        edtLastname.getText().clear();
        edtPassword.getText().clear();
        edtConfirmPassword.getText().clear();
        Toast.makeText(this, "Register success!", Toast.LENGTH_SHORT).show();
        LoginActivity_.intent(this).start();
    }

    @Override
    public void onSignUpFailed() {
        Toast.makeText(this, "Sign up fail. Please check again!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(SignUpResponse result) {

    }

    @Override
    public void onError(String message, int code) {

    }
}
