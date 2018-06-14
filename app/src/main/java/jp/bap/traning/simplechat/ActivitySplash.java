package jp.bap.traning.simplechat;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;

import jp.bap.traning.simplechat.Presenter.SharedPrefs;
import jp.bap.traning.simplechat.view.activities.LoginActivity_;

/**
 * Created by dungpv on 6/13/18.
 */

@Fullscreen
@EActivity(R.layout.activity_splash)
public class ActivitySplash extends BaseActivity {
    @Override
    public void afterView() {
        int mMineId = SharedPrefs.getInstance().getData(SharedPrefs.KEY_SAVE_ID, Integer.class);
        if (mMineId == 0) {
            LoginActivity_.intent(this).start();
            finish();
        } else {
            Common.connectToServerSocket(this, Common.URL_SERVER, mMineId);
        }
    }

    @Override
    public void onConnectedSocket() {
        super.onConnectedSocket();
        MainActivity_.intent(this).start();
        finish();
    }
}
