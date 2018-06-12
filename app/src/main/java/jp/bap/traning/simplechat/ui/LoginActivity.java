package jp.bap.traning.simplechat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by dungpv on 6/8/18.
 */

@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity {
    private final String TAG = getClass().getSimpleName();
    private static final String URL_SERVER = "http://172.16.0.31:3000";

    AppCompatEditText mEditText;

    AppCompatButton mBtnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void afterView() {

    }

    void onClick() {
        hideKeyboard();
        if (ChatService.getChat() == null) {
            Intent i = new Intent(this, ChatService.class);
            i.putExtra("host", URL_SERVER);
            i.putExtra("token", mEditText.getText().toString());
            startService(i);
        }
    }

    @Override
    public void onSocketConnected() {
        super.onSocketConnected();
        MainActivity_.intent(this).start();
        finish();
    }
}
