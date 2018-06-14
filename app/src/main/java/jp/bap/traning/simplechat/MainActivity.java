package jp.bap.traning.simplechat;

import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    private final String TAG = getClass().getSimpleName();
    @ViewById
    AppCompatEditText mEdtInputMessage;
    @ViewById
    AppCompatEditText mEdtRoomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void afterView() {

    }

    @Click(R.id.mBtnSend)
    public void onSend() {
        String message = mEdtInputMessage.getText().toString();
        int roomId = Integer.parseInt(mEdtRoomId.getText().toString());
        ChatService.getChat().sendMessage(message, roomId);
    }
}
