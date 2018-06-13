package jp.bap.traning.simplechat.ui;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;

import jp.bap.traning.simplechat.Common;
import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.presenter.SharedPrefs;
import jp.bap.traning.simplechat.view.activities.LoginActivity_;

/**
 * Created by dungpv on 6/13/18.
 */

@Fullscreen
@EActivity(R.layout.activity_splash)
public class SplashActivity extends BaseActivity {
    @Override
    public void afterView() {
        int mMineId = SharedPrefs.getInstance().getData(SharedPrefs.KEY_SAVE_ID, Integer.class);
        if (mMineId == 0) {
            LoginActivity_.intent(this).start();
            finish();
        } else {
            Common.connectServerChat(this, Common.URL_CHAT, mMineId);
        }
    }

    @Override
    public void onSocketConnected() {
        super.onSocketConnected();
        MainActivity_.intent(this).start();
        finish();
    }
}
