package jp.bap.traning.simplechat.view.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wang.avi.AVLoadingIndicatorView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.FileNotFoundException;
import java.io.InputStream;

import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.interfaces.SignUpInterface;
import jp.bap.traning.simplechat.presenter.SignUpPresenter;

@EActivity(R.layout.activity_sign_up)
public class SignUpActivity extends Activity implements SignUpInterface{
    private static final String URL_IMAGE_BAP = "https://itviec.com/system/production/employers/logos/2875/social_square.png?1472092777";
    public static int selectImage = 1;
    public  SignUpPresenter signUpPresenter;
    @ViewById
    AVLoadingIndicatorView indicatorView;
    @ViewById
    EditText edtRegisterUserName;
    @ViewById
    EditText edtRegisterPassword;
    @ViewById
    EditText edtRegisterFirstName;
    @ViewById
    ImageView imgAvatar;

    @Click
    void btnSignUp() {
        indicatorView.show();
        String userName = edtRegisterUserName.getText().toString();
        String password = edtRegisterPassword.getText().toString();
        String firstName = edtRegisterFirstName.getText().toString();
        if(userName.isEmpty() || password.isEmpty() || firstName.isEmpty()) {
            indicatorView.hide();
            Toast.makeText(SignUpActivity.this,"Please input usename and password!",Toast.LENGTH_SHORT).show();
        }
        else {
            signUpPresenter.signUp(userName,password,URL_IMAGE_BAP,firstName);
        }
    }
    @Click
    void btnSignIn() {
        finish();
    }


    @AfterViews
    protected void afterView() {
        //init
        signUpPresenter = SignUpPresenter.getShareInstance();
        signUpPresenter.signUpInterface = this;
        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SignUpActivity.this,"Image has been setting",Toast.LENGTH_SHORT).show();
                Glide.with(getApplicationContext()).load(URL_IMAGE_BAP).into(imgAvatar);
            }
        });
    }

    @Override
    public void signUpSuccess(String userName) {
        indicatorView.hide();
        Intent intent = new Intent(SignUpActivity.this, FriendActivity_.class);
        intent.putExtra("userName",userName);
        startActivity(intent);
    }

    @Override
    public void signUpFailed() {
        indicatorView.hide();
        Toast.makeText(SignUpActivity.this,"Error: Username was exist. Please Try Again!",Toast.LENGTH_LONG).show();
    }

}
