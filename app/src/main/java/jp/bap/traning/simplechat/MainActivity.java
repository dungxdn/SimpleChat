package jp.bap.traning.simplechat;

import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import jp.bap.traning.simplechat.chat.ChatService;
import jp.bap.traning.simplechat.database.UserDAO;
import jp.bap.traning.simplechat.model.User;
import jp.bap.traning.simplechat.presenter.SharedPrefs;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    private final String TAG = getClass().getSimpleName();
    @ViewById
    AppCompatEditText mEdtInputMessage;
    @ViewById
    AppCompatEditText mEdtRoomId;
    private int mMineId = SharedPrefs.getInstance().getData(SharedPrefs.KEY_SAVE_ID, Integer.class);

    @Override
    public void afterView() {
        User user = new UserDAO().getUser(mMineId);
        if (user != null) {
            Log.d(TAG, "afterView: " + user);
        }
    }

    @Click(R.id.mBtnSend)
    public void onSend() {
        String message = mEdtInputMessage.getText().toString();
        int roomId = Integer.parseInt(mEdtRoomId.getText().toString());
        ChatService.getChat().sendMessage(message, roomId);
    }

//    @Click(R.id.mBtnSend)
//    void onClick(View view) {
//        if (ChatService.getChat() != null) {
//            ChatService.getChat().sendMessage(mEditText.getText().toString());
//        }
//    }
}
