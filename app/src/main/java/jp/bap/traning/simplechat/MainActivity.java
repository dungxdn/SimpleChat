package jp.bap.traning.simplechat;

import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    private final String TAG = getClass().getSimpleName();
    private static final String URL_SERVER = "http://172.16.0.31:3000";
    private final String mUserName = "dungpv";
    private Socket mSocket;
    {
        try {
            IO.Options opts = new IO.Options();
            opts.query = "token=" + mUserName;
            mSocket = IO.socket(URL_SERVER, opts);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @ViewById
    AppCompatEditText mEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void afterView() {
        mSocket.connect();
        onSocketEvent();
    }

    @Click(R.id.mBtnSend)
    void onClick(View view) {
        mSocket.emit("sendMessage", mEditText.getText().toString());
    }

    private void onSocketEvent() {
        Log.d(TAG, "onSocketEvent: " + mSocket);
        mSocket
                .on("receiverMessage", args -> Log.d(TAG, "receiverMessage: " + args[0].toString()))
                .on(Socket.EVENT_CONNECT, args -> Log.d(TAG, "EVENT_CONNECT"))
                .on(Socket.EVENT_RECONNECT, args -> Log.d(TAG, "EVENT_RECONNECT"))
                .on(Socket.EVENT_DISCONNECT, args -> Log.w(TAG, "EVENT_DISCONNECT"))
                .on(Socket.EVENT_ERROR, args -> Log.w(TAG, "EVENT_ERROR"))
                .on(Socket.EVENT_RECONNECT_ERROR, args -> Log.e(TAG, "EVENT_RECONNECT_ERROR"));
    }
}
