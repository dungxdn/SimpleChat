package jp.bap.traning.simplechat.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.WindowFeature;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import jp.bap.traning.simplechat.model.Message;
import jp.bap.traning.simplechat.model.User;
import jp.bap.traning.simplechat.service.CallbackManager;
import jp.bap.traning.simplechat.utils.Event;

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
                    Gson gson = new Gson();
                    String objectMessage = data.get("chatMessage").toString();
                    Message message = gson.fromJson(objectMessage, Message.class);
                    onReceiverMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public void onConnectedSocket() {
    }

    public void onReceiverMessage(Message message) {
    }
}
