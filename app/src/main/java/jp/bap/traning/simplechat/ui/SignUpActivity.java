package jp.bap.traning.simplechat.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

import com.wang.avi.AVLoadingIndicatorView;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import jp.bap.traning.simplechat.R;

@EActivity(R.layout.activity_sign_up)
public class SignUpActivity extends Activity {

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

    @Click
    void btnSignUp() {
//        indicatorView.show();
//        String email = edtUsername.getText().toString();
//        String password = edtPassword.getText().toString();
//        if(email.isEmpty() || password.isEmpty()) {
//            indicatorView.hide();
//            Toast.makeText(SignUpActivity.this,"Please input usename and password!",Toast.LENGTH_SHORT).show();
//        }
//        else {
//
//        }
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
