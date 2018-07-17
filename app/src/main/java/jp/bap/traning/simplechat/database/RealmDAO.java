package jp.bap.traning.simplechat.database;

import io.realm.Realm;
import io.realm.RealmChangeListener;

public class RealmDAO {
    private static int check = 0;
    private RealmChangeListener mRealmChangeListener;
    private Realm mRealmforListener;

    public RealmDAO() {
        mRealmforListener = Realm.getDefaultInstance();
    }

    public void realmChanged(Listener listener) {
        mRealmChangeListener = (o) -> {
            listener.onRealmChanged(o, check);
            check++;
        };
        mRealmforListener.addChangeListener(mRealmChangeListener);
    }

    public void removeRealmChanged() {
        mRealmforListener.removeChangeListener(mRealmChangeListener);
    }

    public void closeRealmChngeListener() {
        mRealmforListener.close();
    }

    public interface Listener {
        void onRealmChanged(Object o, int check);
    }
}
