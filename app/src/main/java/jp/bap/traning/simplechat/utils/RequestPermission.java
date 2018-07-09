package jp.bap.traning.simplechat.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


public class RequestPermission extends AppCompatActivity {
    private Activity mActivity;

    public interface ListenerPermission {
       void onGranted(int permission) ;
    }
    ListenerPermission mListenerPermission;

    public RequestPermission(Activity activity, ListenerPermission listenerPermission) {
        mActivity = activity;
        mListenerPermission = listenerPermission;
    }

    public void  requestCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                //Permission don't grant
                ActivityCompat.requestPermissions(mActivity,
                                            new String[]{Manifest.permission.CAMERA},
                                            Common.MY_PERMISSIONS_REQUEST_CAMERA);

            } else {
                mListenerPermission.onGranted(Common.MY_PERMISSIONS_REQUEST_CAMERA);
            }
        }
    }

    public void requestRecordAudioPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                //Permission don't grant
                ActivityCompat.requestPermissions(mActivity,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        Common.MY_PERMISSIONS_REQUEST_AUDIO);

            } else {
                mListenerPermission.onGranted(Common.MY_PERMISSIONS_REQUEST_AUDIO);
            }
        }
    }
}
