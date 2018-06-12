package jp.bap.traning.simplechat.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.WindowFeature;
import org.json.JSONException;
import org.json.JSONObject;

import jp.bap.traning.simplechat.chat.CallbackManager;
import jp.bap.traning.simplechat.chat.Event;

/**
 * Created by dungpv on 6/7/18.
 */

@EActivity
@WindowFeature(Window.FEATURE_NO_TITLE)
public abstract class BaseActivity extends AppCompatActivity implements CallbackManager.Listener {
    private CallbackManager mSocketCallback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public abstract void afterView();

    @AfterViews
    public void initView() {
        this.afterView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSocketCallback == null) {
            mSocketCallback = new CallbackManager(this);
        }
        mSocketCallback.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mSocketCallback != null) {
            mSocketCallback.unregister();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSocketCallback != null) {
            mSocketCallback.unregister();
        }
    }

    protected void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onSocketEvent(Event type, JSONObject data) {
        switch (type) {
            case CONNECTED:
                onSocketConnected();
                break;

            case CALL_CONTENT:
                onCallContent(data);
                break;

            case CALL_ACCEPT:
                onCallAccept();
                break;

            case CALL_STOP:
                onCallStop();
                break;

            case CALL:
                try {
                    int roomId = data.getInt("roomId");
                    onCall(roomId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public void onCallStop() {}

    public void onCallContent(JSONObject data) {}

    public void onCallAccept() {}

    public void onCall(int roomId) {}

    public void onSocketConnected() {}
}
