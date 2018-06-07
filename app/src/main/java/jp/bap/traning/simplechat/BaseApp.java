package jp.bap.traning.simplechat;

import android.support.multidex.MultiDexApplication;

import org.androidannotations.annotations.EApplication;

/**
 * Created by dungpv on 6/7/18.
 */

@EApplication
public class BaseApp extends MultiDexApplication {
    private static BaseApp sInstance = null;

    public static synchronized BaseApp getInstance() {
        if (sInstance == null) {
            sInstance = new BaseApp();
        }
        return sInstance;
    }
}
