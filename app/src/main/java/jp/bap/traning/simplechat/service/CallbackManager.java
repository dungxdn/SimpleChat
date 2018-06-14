package jp.bap.traning.simplechat.service;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import jp.bap.traning.simplechat.utils.Common;
import jp.bap.traning.simplechat.utils.Event;

/**
 * Created by dungpv on 6/14/18.
 */

public class CallbackManager {
    private CallbackReceiver mReceiver;
    private Listener mListener;
    private Context mContext;

    public interface Listener {
        void onMessage(Event type, JSONObject data);

    }

    public CallbackManager(Context context) {
        mContext = context;
        mReceiver = new CallbackReceiver();

    }

    public void register(Listener listener) {
        mListener = listener;
        IntentFilter filter = new IntentFilter(Common.ACTION_SOCKET_EVENT);
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mReceiver, filter);
    }

    public void unregister() {
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mReceiver);
        mListener = null;
    }

    class CallbackReceiver extends android.content.BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getStringExtra("action");
            String data_json = intent.getStringExtra("data");
            JSONObject data = null;
            try {
                data = new JSONObject(data_json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Event type = Event.fromEvent(action);
            if (!TextUtils.isEmpty(action)) {
                if (mListener != null && data != null) {
                    mListener.onMessage(type, data);
                }
            }
        }
    }
}
