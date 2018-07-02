package jp.bap.traning.simplechat.ui;

import android.content.Intent;
import android.util.Log;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.OnActivityResult;

import io.realm.Realm;
import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.presenter.rooms.GetRoomsPresenter;
import jp.bap.traning.simplechat.presenter.rooms.GetRoomsView;
import jp.bap.traning.simplechat.response.RoomResponse;
import jp.bap.traning.simplechat.utils.Common;
import jp.bap.traning.simplechat.utils.SharedPrefs;

/**
 * Created by dungpv on 6/13/18.
 */

@Fullscreen
@EActivity(R.layout.activity_splash)
public class SplashActivity extends BaseActivity {
    private static final String TAG = SplashActivity.class.getClass().getSimpleName();

    @Override
    public void afterView() {
        Intent intent = getIntent();
        if(intent != null){
            if(intent.getIntExtra(Common.REQUEST_LOGOUT_KEY,-1) == Common.REQUEST_LOGOUT_VALUE){
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                realm.deleteAll();
                realm.commitTransaction();
            }
        }
        int mMineId = SharedPrefs.getInstance().getData(SharedPrefs.KEY_SAVE_ID, Integer.class);
        if (mMineId == 0) {
            LoginActivity_.intent(this).startForResult(Common.REQUEST_LOGIN);
        } else {
            init();
        }
    }

    @Override
    public void onConnectedSocket() {
        super.onConnectedSocket();
        MainActivity_.intent(this).start();
        finish();
    }

    @OnActivityResult(Common.REQUEST_LOGIN)
    public void init() {
        new GetRoomsPresenter(new GetRoomsView() {
            @Override
            public void onSuccess(RoomResponse result) {
                int mMineId =
                        SharedPrefs.getInstance().getData(SharedPrefs.KEY_SAVE_ID, Integer.class);
                Common.connectToServerSocket(SplashActivity.this, Common.URL_SERVER, mMineId);
            }

            @Override
            public void onError(String message, int code) {
                Log.e(TAG, "onError: " + message);
            }

            @Override
            public void onFailure() {

            }
        }).request();
    }
}
