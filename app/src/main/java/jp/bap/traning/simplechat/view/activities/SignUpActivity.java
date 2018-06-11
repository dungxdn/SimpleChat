package jp.bap.traning.simplechat.view.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

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
    EditText edtRegisterEmail;
    @ViewById
    EditText edtRegisterPassword;
    @Click
    void btnSignUp() {
        indicatorView.show();
        String email = edtRegisterEmail.getText().toString();
        String password = edtRegisterPassword.getText().toString();
        if(email.isEmpty() || password.isEmpty()) {
            indicatorView.hide();
            Toast.makeText(SignUpActivity.this,"Please input usename and password!",Toast.LENGTH_SHORT).show();
        }
        else {

        }
    }
    @Click
    void btnSignIn() {
        startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
