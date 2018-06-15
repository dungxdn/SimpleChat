package jp.bap.traning.simplechat.ui;

import android.support.v7.widget.AppCompatEditText;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.Objects;

import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.ui.BaseFragment;

/**
 * Created by Admin on 6/13/2018.
 */
@EFragment(R.layout.fragment_chat)
public class ChatFragment extends BaseFragment {
    @ViewById
    AppCompatEditText mEdtRoomId;

    @Override
    public void afterView() {

    }

    @Click(R.id.mBtnCall)
    void onClick() {
        String roomIdString = mEdtRoomId.getText().toString();
        if (!Objects.equals(roomIdString, "")) {
            int roomId = Integer.parseInt(roomIdString);
            CallActivity_.intent(this)
                    .isIncoming(false)
                    .roomId(roomId)
                    .start();
        }
    }
}
