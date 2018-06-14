package jp.bap.traning.simplechat.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import com.wang.avi.AVLoadingIndicatorView;
import jp.bap.traning.simplechat.ui.MainActivity;
import jp.bap.traning.simplechat.presenter.login.LoginView;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import jp.bap.traning.simplechat.presenter.login.LoginPresenter;
import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.response.UserResponse;
import jp.bap.traning.simplechat.utils.Common;
import jp.bap.traning.simplechat.utils.SharedPrefs;

@EActivity(R.layout.activity_login)
public class LoginActivity extends Activity implements LoginView {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Init();
    }

    public void getSharedPreference() {
        edtUserName.setText(SharedPrefs.getInstance().getData(CURRENT_USERNAME, String.class) + "");
        edtPassword.setText(SharedPrefs.getInstance().getData(CURRENT_PASSWORD, String.class) + "");
    }

    @Override
    public void onLoginSuccess(UserResponse userResponse) {
        indicatorView.hide();
        Log.e("abc", userResponse.getData().toString());
        MainActivity_.intent(this).start();
        //Connect to Socket
        Common.connectToServerSocket(this,Common.URL_SERVER,userResponse.getData().getId());
        finish();
    }

    @Override
    public void onLoginFailed() {
        indicatorView.hide();
        Toast.makeText(LoginActivity.this, "Email or Password wrong ! Please try again !", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {

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
}
