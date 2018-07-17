package jp.bap.traning.simplechat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.multidex.MultiDexApplication;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import jp.bap.traning.simplechat.utils.SharedPrefs;

import org.androidannotations.annotations.EApplication;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.rx.RealmObservableFactory;
import jp.bap.traning.simplechat.utils.AllModule;

/**
 * Created by dungpv on 6/7/18.
 */

@EApplication
public class BaseApp extends MultiDexApplication {
    private volatile static BaseApp sInstance = null;

    public static BaseApp getInstance() {
        if (sInstance == null) {
            sInstance = new BaseApp();
        }
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .modules(Realm.getDefaultModule(), new AllModule())
                .name(Realm.DEFAULT_REALM_NAME)
                .rxFactory(new RealmObservableFactory())
                .schemaVersion(0)
//                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

        if (BuildConfig.DEBUG) {
            /**
             * set up for view realm db in #chrome://inspect/
             */
            Stetho.initialize(
                    Stetho.newInitializerBuilder(this)
                            .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                            .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                            .build());
        }
    }
}
