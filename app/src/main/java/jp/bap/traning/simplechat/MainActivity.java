package jp.bap.traning.simplechat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    private final String TAG = getClass().getSimpleName();
    private static final String URL_SERVER = "http://172.16.0.31:3000";
    @ViewById
    AppCompatEditText mEditText;
    @ViewById
    AppCompatButton mBtnLogin;

    private final String callText = "Goto Call";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void afterView() {

    }

    @Click(R.id.mBtnLogin)
    void onClick(View view) {
        if (ChatService.getChat() == null) {
            Intent i = new Intent(this, ChatService.class);
            i.putExtra("host", URL_SERVER);
            i.putExtra("token", mEditText.getText().toString());
            startService(i);
        }
        if (mBtnLogin.getText().equals(callText)) {
            CallActivity_.intent(this).start();
        }
    }

    @Override
    public void onSocketConnected() {
        super.onSocketConnected();
        hideKeyboard();
        mEditText.setVisibility(View.GONE);
        mBtnLogin.setText(callText);
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
