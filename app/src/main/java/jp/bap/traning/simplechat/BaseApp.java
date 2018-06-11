package jp.bap.traning.simplechat;

import android.support.multidex.MultiDexApplication;

import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import org.androidannotations.annotations.EApplication;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.rx.RealmObservableFactory;

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

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .modules(Realm.getDefaultModule(), new AllModule())
                .name(Realm.DEFAULT_REALM_NAME)
                .rxFactory(new RealmObservableFactory())
                .schemaVersion(0)
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
