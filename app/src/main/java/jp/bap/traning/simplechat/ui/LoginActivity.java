package jp.bap.traning.simplechat.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import com.wang.avi.AVLoadingIndicatorView;
import jp.bap.traning.simplechat.presenter.login.LoginView;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import jp.bap.traning.simplechat.presenter.login.LoginPresenter;
import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.response.UserResponse;
import jp.bap.traning.simplechat.service.ChatManager;
import jp.bap.traning.simplechat.utils.Common;

@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity implements LoginView {
    private LoginPresenter mLoginPresenter;
    public static final String CURRENT_USERNAME = "current_id";
    public static final String CURRENT_PASSWORD = "current_name";

    @ViewById
    EditText edtUserName;
    @ViewById
    EditText edtPassword;
    @ViewById
    AVLoadingIndicatorView indicatorView;

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
        startActivity(new Intent(this, SignUpActivity_.class));
    }

    @Override
    public void afterView() {
        Init();
    }

    public void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage("Do you want to exit?")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.exit(0);
                    }
                })
                .show();
    }
    @Override
    public void onLoginSuccess(UserResponse userResponse) {
        indicatorView.hide();
        Log.e("abc", userResponse.getData().toString());
        ChatTalksActivity_.intent(this).start();
        //Connect to Socket
        Common.connectToServerSocket(LoginActivity.this,Common.URL_SERVER,userResponse.getData().getId());
    }

    @Override
    public void onLoginFailed() {
        indicatorView.hide();
        Toast.makeText(LoginActivity.this, "Email or Password wrong ! Please try again !", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        confirmDialog();
    }

    public void Init() {
        this.mLoginPresenter = new LoginPresenter(this);
    }

    @Override
    public void onSuccess(UserResponse result) {

    }

    @Override
    public void onError(String message, int code) {

    }

    @Override
    public void onConnectedSocket() {
        super.onConnectedSocket();
        Toast.makeText(LoginActivity.this,"Connect Success",Toast.LENGTH_SHORT).show();
        ChatTalksActivity_.intent(this).start();
        finish();

    }
}
