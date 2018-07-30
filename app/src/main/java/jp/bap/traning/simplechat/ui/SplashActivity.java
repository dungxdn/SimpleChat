package jp.bap.traning.simplechat.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import io.realm.Realm;
import jp.bap.traning.simplechat.service.ApiClient;
import jp.bap.traning.simplechat.service.ChatService;
import jp.bap.traning.simplechat.service.ImgurClient;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.OnActivityResult;

import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.presenter.rooms.GetRoomsPresenter;
import jp.bap.traning.simplechat.presenter.rooms.GetRoomsView;
import jp.bap.traning.simplechat.response.RoomResponse;
import jp.bap.traning.simplechat.utils.Common;
import jp.bap.traning.simplechat.utils.LocaleManager;
import jp.bap.traning.simplechat.utils.SharedPrefs;

/**
 * Created by dungpv on 6/13/18.
 */

@Fullscreen
@EActivity(R.layout.activity_splash)
public class SplashActivity extends BaseActivity {
    private static final String TAG = SplashActivity.class.getClass().getSimpleName();
    private static boolean isNotWifi = false;

    @Override
    public void afterView() {
        if (SharedPrefs.getInstance().getData(Common.KEY_CHOOSE_LANGUAGE, String.class).equals("")) {
            LocaleManager.setLocale(getApplicationContext(), "en");
        } else {
            LocaleManager.setLocale(getApplicationContext(), SharedPrefs.getInstance().getData(Common.KEY_CHOOSE_LANGUAGE, String.class));
        }
        setUp();
    }

    private void setUp() {
        Intent i = getIntent();
        if (i != null) {
            if (i.getIntExtra(Common.REQUEST_LOGOUT_KEY, -1) == Common.REQUEST_LOGOUT) {
                //Delete Realm
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                realm.deleteAll();
                realm.commitTransaction();
            }
        }
        if (isConnectedNetwork() == false) {
            isNotWifi = true;
            NetworkActivity_.intent(this).start();
        } else {
            int mMineId = SharedPrefs.getInstance().getData(SharedPrefs.KEY_SAVE_ID, Integer.class);
            if (mMineId == 0) {
                LoginActivity_.intent(this).startForResult(Common.REQUEST_LOGIN);
                overridePendingTransition(R.anim.anim_from_midle,R.anim.anim_to_midle);
            } else {
                init();
            }
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
                if (ChatService.getChat() == null) {
                    Intent i = new Intent(SplashActivity.this, ChatService.class);
                    i.putExtra("host", Common.URL_SERVER);
                    i.putExtra("token", mMineId);
                    getApplicationContext().startService(i);
                } else {
                    Log.d(TAG, "connectToServerSocket: Service started ");
                    MainActivity_.intent(SplashActivity.this).start();
                    finish();
                }
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

    @Override
    protected void onResume() {
        super.onResume();
        if (isNotWifi == true) {
            isNotWifi = false;
            setUp();
        }
    }

}