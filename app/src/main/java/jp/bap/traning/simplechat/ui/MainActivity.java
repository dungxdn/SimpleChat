package jp.bap.traning.simplechat.ui;

import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import jp.bap.traning.simplechat.R;

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
                .roomId(Integer.parseInt(mEtRoomId.getText().toString()))
                .start();
    }

    @Override
    public void onCall(int roomId) {
        super.onCall(roomId);
        CallActivity_.intent(this)
                .isIncoming(true)
                .roomId(roomId)
                .start();
    }
}
