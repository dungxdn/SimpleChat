package jp.bap.traning.simplechat.database;

import java.util.List;

import io.realm.Realm;
import jp.bap.traning.simplechat.model.Room;

/**
 * Created by dungpv on 6/18/18.
 */

public class RoomDAO {

    public void insertOrUpdate(List<Room> rooms) {
        Realm mRealm = Realm.getDefaultInstance();
        mRealm.executeTransaction(realm -> realm.copyToRealmOrUpdate(rooms));
        mRealm.close();
    }
}
