package jp.bap.traning.simplechat.view.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.SharedPref;
import org.xml.sax.InputSource;

import io.realm.Realm;
import jp.bap.traning.simplechat.BaseActivity;
import jp.bap.traning.simplechat.MainActivity;
import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.interfaces.LoginInterface;
import jp.bap.traning.simplechat.model.User;
import jp.bap.traning.simplechat.presenter.LoginPresenter;
import jp.bap.traning.simplechat.presenter.SharedPrefs;

@EActivity(R.layout.activity_login)
public class LoginActivity extends Activity implements LoginInterface {
    private LoginPresenter loginPresenter;
    public static final String CURRENT_USERNAME = "current_id";
    public static final String CURRENT_PASSWORD = "current_name";
    @ViewById
    EditText edtEmail;
    @ViewById
    EditText edtPassword;
    @ViewById
    AVLoadingIndicatorView indicatorView;
    @Click
    void btnLogin() {
        indicatorView.show();
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();
        if(email.isEmpty() || password.isEmpty()) {
            indicatorView.hide();
            Toast.makeText(LoginActivity.this,"Please input usename and password!",Toast.LENGTH_SHORT).show();
        }
        else {
            loginPresenter.logIn(email,password);
        }
    }

    @Click
    void btnSignUp() {
        indicatorView.hide();
        startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
    }

    @Click
    void btnRecover() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
//        init();
        loginPresenter = LoginPresenter.getShareInstance();
        loginPresenter.loginInterface = this;

    }

    public void init() {
        //get SharedPreference
        edtEmail.setText(SharedPrefs.getmInstance().getData(CURRENT_USERNAME,String.class)+"");
        edtPassword.setText(SharedPrefs.getmInstance().getData(CURRENT_PASSWORD,String.class)+"");

    }

    @Override
    public void loginSuccess(String userName, String password) {
        SharedPrefs.getmInstance().putData(CURRENT_USERNAME,userName);
        SharedPrefs.getmInstance().putData(CURRENT_PASSWORD,password);
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        indicatorView.hide();
        startActivity(intent);
    }

    @Override
    public void loginFailed() {
        indicatorView.hide();
        Toast.makeText(LoginActivity.this, "Email or Password wrong ! Please try again !", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {

    }
}
