package jp.bap.traning.simplechat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.WindowFeature;
import org.json.JSONException;
import org.json.JSONObject;

import jp.bap.traning.simplechat.chat.Event;
import jp.bap.traning.simplechat.service.CallbackManager;

import jp.bap.traning.simplechat.chat.ChatService;

/**
 * Created by dungpv on 6/7/18.
 */

@EActivity
@WindowFeature(Window.FEATURE_NO_TITLE)
public abstract class BaseActivity extends AppCompatActivity implements CallbackManager.Listener {
    CallbackManager mCallback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCallback = new CallbackManager(this);
        mCallback.register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCallback.unregister();
    }

    public abstract void afterView();

    @AfterViews
    public void initView() {
        this.afterView();
    }

    @Override
    public void onMessage(Event type, JSONObject data) {
        switch (type) {
            case CONNECT:
                onConnectedSocket();
                break;

            case MESSAGE_RECEIVER:
                try {
                    String message = data.getString("content");
                    int roomId = data.getInt("roomId");
                    onReceiverMessage(message, roomId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }
    }

    public void onConnectedSocket() {}

    public void onReceiverMessage(String message, int roomId) {}
}
