package jp.bap.traning.simplechat.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by dungpv on 6/8/18.
 */

public class CallbackManager{
    private static final String TAG = CallbackManager.class.getSimpleName();

    /**
     * interface CallbackManager
     */
    public interface Listener {
        void onSocketEvent(Event type, JSONObject data);
    }

    private Map<String, CallbackReceiver> _receivers;
    private Listener _listener;
    private Context _context;

    public CallbackManager(Context context) {
        _context = context;
        _receivers = new HashMap<>();

    }

    public void register(Event type, Listener listener) {
        IntentFilter filter = new IntentFilter(type.getEvent());
        _receivers.put(type.getEvent(), new CallbackReceiver());
        _listener = listener;
        LocalBroadcastManager.getInstance(_context).registerReceiver(_receivers.get(type.getEvent()), filter);
    }

    public void register(Listener listener) {
        if (_listener == listener) return;
        Map<String, Event> allType = Event.getAllType();
        if (allType.size() > 0) {
            for (Event type : allType.values()) {
                register(type, listener);
            }
        }
    }

    private void unregister(String action) {
        if (_receivers.containsKey(action)) {
            LocalBroadcastManager.getInstance(_context).unregisterReceiver(_receivers.get(action));
        }
        _listener = null;
    }

    public void unregister(Event type) {
        unregister(type.getEvent());
    }


    public void unregister() {
        if (_receivers != null && !_receivers.isEmpty()) {
            for (Iterator<String> keys = _receivers.keySet().iterator(); keys.hasNext(); ) {
                String key = keys.next();
                unregister(key);
            }
        }
    }

    public class CallbackReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String data_json = intent.getStringExtra("data");
            JSONObject data = null;
            try {
                data = new JSONObject(data_json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Event type = Event.fromAction(action);
            if (!TextUtils.isEmpty(action)) {
                if (_listener != null && data != null) {
                    _listener.onSocketEvent(type, data);
                } else {
                    Log.e(TAG, "onReceive: data null");
                }
            }
        }
    }
}