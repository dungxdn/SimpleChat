package jp.bap.traning.simplechat.database;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.RealmChangeListener;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

import javax.annotation.Nullable;

import jp.bap.traning.simplechat.model.Message;

public class MessageDAO {

    public MessageDAO() {
    }

    public void insertOrUpdateMessage(Message message) {
        Realm mRealm = Realm.getDefaultInstance();
        mRealm.executeTransaction(realm -> {
            realm.insertOrUpdate(message);
        });
        mRealm.close();
    }

    public ArrayList<Message> getAllMessage(int roomID) {
        ArrayList<Message> messageList = new ArrayList<>();
        Realm mRealm = Realm.getDefaultInstance();
        RealmResults<Message> results = mRealm.where(Message.class).equalTo("roomID", roomID).findAll();
        for (Message message : results) {
            messageList.add(mRealm.copyFromRealm(message));
        }
//        mRealm.close();
        return messageList;
    }

    public Message getAMessage(long idMessage) {
        Realm mRealm = Realm.getDefaultInstance();
        Message message = mRealm.where(Message.class)
                .equalTo("id", idMessage)
                .findFirst();
        Message result = null;
        if (message != null) {
            result = mRealm.copyFromRealm(message);
        }
        mRealm.close();
        return result;
    }

    public void deleteMessage(long idMessage) {
        Realm mRealm = Realm.getDefaultInstance();
        Message message = mRealm.where(Message.class)
                .equalTo("id", idMessage)
                .findFirst();
        mRealm.executeTransaction(realm -> {
            if (message != null) {
                message.deleteFromRealm();
            }
        });
        mRealm.close();
    }
}
