package jp.bap.traning.simplechat.webrtc;

import android.util.Log;

import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;

/**
 * Created by Admin on 7/5/2018.
 */

public class CustomSdpObserver implements SdpObserver {
    String logTag;

    public CustomSdpObserver(String tag) {
        logTag = getClass().getCanonicalName();
        logTag = logTag + " " + tag;
    }

    @Override
    public void onCreateSuccess(SessionDescription sessionDescription) {
        Log.d(logTag, "onCreateSuccess: call with sessionDescription: " + sessionDescription);
    }

    @Override
    public void onSetSuccess() {
        Log.d(logTag, "onSetSuccess: ");
    }

    @Override
    public void onCreateFailure(String s) {
        Log.d(logTag, "onCreateFailure: call with s: " + s);
    }

    @Override
    public void onSetFailure(String s) {
        Log.d(logTag, "onSetFailure: call with S: "+s);
    }
}
