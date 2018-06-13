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

import jp.bap.traning.simplechat.Common;
import jp.bap.traning.simplechat.chat.ChatService;
import jp.bap.traning.simplechat.ui.BaseActivity;
import jp.bap.traning.simplechat.ui.MainActivity;
import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.interfaces.LoginInterface;
import jp.bap.traning.simplechat.presenter.LoginPresenter;
import jp.bap.traning.simplechat.presenter.SharedPrefs;
import jp.bap.traning.simplechat.ui.MainActivity_;

@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity {
    @ViewById
    EditText edtEmail;
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
        int mMineId = Integer.parseInt(edtEmail.getText().toString());
        SharedPrefs.getInstance().putData(SharedPrefs.KEY_SAVE_ID, mMineId);
        Common.connectServerChat(this, Common.URL_CHAT, mMineId);
    }

    @Click
    void btnSignUp() {

    }

    @Click
    void btnRecover() {

    }

    public void init() {
    }

    @Override
    public void onSocketConnected() {
        super.onSocketConnected();
        MainActivity_.intent(this).start();
        finish();
    }
}
