package jp.bap.traning.simplechat.ui;

import android.support.v7.widget.AppCompatEditText;
import android.util.Log;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import jp.bap.traning.simplechat.presenter.rooms.GetRoomsPresenter;
import jp.bap.traning.simplechat.presenter.rooms.GetRoomsView;
import jp.bap.traning.simplechat.response.RoomResponse;
import jp.bap.traning.simplechat.service.ChatService;
import jp.bap.traning.simplechat.utils.SharedPrefs;
import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.database.UserDAO;
import jp.bap.traning.simplechat.model.User;

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
        /*String message = mEdtInputMessage.getText().toString();
        int roomId = Integer.parseInt(mEdtRoomId.getText().toString());
        ChatService.getChat().sendMessage(message, roomId);*/
        new GetRoomsPresenter(new GetRoomsView() {
            @Override
            public void onSuccess(RoomResponse result) {
                Log.d(TAG, "onSuccess: " + result);
            }

            @Override
            public void onError(String message, int code) {

            }
        }).request();
    }
}
