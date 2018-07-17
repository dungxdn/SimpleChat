package jp.bap.traning.simplechat.database;

import android.util.Log;

import io.realm.ObjectChangeSet;
import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.RealmChangeListener;
import io.realm.RealmObjectChangeListener;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

import javax.annotation.Nullable;

import jp.bap.traning.simplechat.model.Message;
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

    public void insertOrUpdate(Room room) {
        Realm mRealm = Realm.getDefaultInstance();
        mRealm.executeTransaction(realm -> realm.copyToRealmOrUpdate(room));
        mRealm.close();
    }

    public ArrayList<Room> getAllRoom() {
        ArrayList<Room> list = new ArrayList<>();
        Realm mRealm = Realm.getDefaultInstance();
        RealmResults<Room> results = mRealm.where(Room.class).findAll();
        for (Room c : results) {
            list.add(mRealm.copyFromRealm(c));
        }
        mRealm.close();
        return list;
    }

    public Room getRoomWithUser(int userId) {
        Room room = null;
        Realm mRealm = Realm.getDefaultInstance();
        RealmResults<Room> results =
                mRealm.where(Room.class).findAll();

        for (Room c : results) {
            if (c.getType() == 0 &&
                    (c.getUsers().get(0).getId() == userId ||
                            c.getUsers().get(1).getId() == userId)) {
                room = mRealm.copyFromRealm(c);
                break;
            }
        }
        mRealm.close();
        return room;
    }

    public Room getRoomFromRoomId(int roomId) {
        Room result = null;
        Realm mRealm = Realm.getDefaultInstance();
        Room room = mRealm.where(Room.class)
                .equalTo("roomId", roomId)
                .findFirst();
        if (room != null) {
            result = mRealm.copyFromRealm(room);
        }
        mRealm.close();
        return result;
    }

}
