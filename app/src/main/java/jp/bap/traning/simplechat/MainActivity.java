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
    @ViewById
    AppCompatEditText mEtRoomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void afterView() {

    }

    @Click(R.id.mBtnCall)
    void onClick() {
        hideKeyboard();
        CallActivity_.intent(this)
                .isIncoming(false)
                .roomId(mEtRoomId.getText().toString())
                .start();
    }

    @Override
    public void onCall(String userName) {
        super.onCall(userName);
        CallActivity_.intent(this)
                .isIncoming(true)
                .roomId(userName)
                .start();
    }
}
