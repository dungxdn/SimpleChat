package jp.bap.traning.simplechat.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import static android.support.v4.app.ActivityCompat.requestPermissions;
import static android.support.v4.app.ActivityCompat.shouldShowRequestPermissionRationale;
import static android.support.v4.content.ContextCompat.checkSelfPermission;

public class Permission {

    public static final int sREQUEST_CODE_PERMISSION = 1;

    private static boolean checkVersion() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    private static boolean checkPermission(Activity activity, String... permissions) {
        for (String p : permissions) {
            if (checkSelfPermission(activity, p) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private static boolean checkShouldRequest(Activity activity, String... permissions) {
        for (String p : permissions) {
            if (checkSelfPermission(activity, p) != PackageManager.PERMISSION_GRANTED) {
                if (!shouldShowRequestPermissionRationale(activity, p)) return false;
            }
        }
        return true;
    }

    public static void initPermission(Activity activity, String... permissions) {
        if (checkVersion()) {
            if (!checkPermission(activity, permissions)) {
                requestPermissions(activity, permissions, sREQUEST_CODE_PERMISSION);
            }
        }
    }
}
