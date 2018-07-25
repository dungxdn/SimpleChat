package jp.bap.traning.simplechat.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.model.User;
import jp.bap.traning.simplechat.utils.Common;

@EActivity(R.layout.activity_call_busy)
public class CallBusyActivity extends BaseActivity {

    @ViewById
    CircleImageView mAvatar;
    @ViewById
    AppCompatTextView txtName;
    @ViewById
    AppCompatTextView txtStatus;
    @Extra
    User mUser;
    @Extra
    int status;

    @Override
    public void afterView() {
        overridePendingTransition(R.anim.anim_fade_in,0);
        SoundManage.stop(this);
        if (mUser != null) {
            Common.setAvatar(this, mUser.getId(), mAvatar);
            txtName.setText(mUser.getFirstName() + " " + mUser.getLastName());
            if (status == Common.CALL_BUSY) {
                txtStatus.setText(txtName.getText() + " is having another conversation");
            } else {        //this case is: status == Common.CALL_NO_ONE
                txtStatus.setText(txtName.getText() + " is not online. Please try in the next time!");
            }
        }
    }

    @Click(R.id.imgBtnCancel)
    void finishActivity() {
        finish();
    }
}
