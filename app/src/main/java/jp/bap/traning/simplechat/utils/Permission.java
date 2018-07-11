package jp.bap.traning.simplechat.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import static android.support.v4.app.ActivityCompat.requestPermissions;
import static android.support.v4.content.ContextCompat.checkSelfPermission;

public class Permission {

    public static final int sREQUEST_CODE_PERMISSION = 1;

    public static void initPermission(Activity activity, String permission) {
        if (checkVersion()) {
            if (checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(activity, new String[] { permission }, sREQUEST_CODE_PERMISSION);
            }
        }
    }

    private static boolean checkVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) return true;
        return false;
    }
}
