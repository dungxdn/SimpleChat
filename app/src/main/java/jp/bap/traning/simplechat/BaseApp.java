package jp.bap.traning.simplechat;

import android.support.multidex.MultiDexApplication;

import org.androidannotations.annotations.EApplication;

import io.realm.Realm;

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

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance=this;
        Realm.init(this);
    }
}
