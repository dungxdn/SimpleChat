package jp.bap.traning.simplechat.database;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.RealmChangeListener;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import javax.annotation.Nullable;
import jp.bap.traning.simplechat.model.Message;

public class MessageDAO {

    Realm mRealmforListener = Realm.getDefaultInstance();

    public MessageDAO(){}

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
        RealmResults<Message> results = mRealm.where(Message.class).equalTo("roomID",roomID).findAll();
        for(Message message : results) {
            messageList.add(mRealm.copyFromRealm(message));
        }
//        mRealm.close();
        return messageList;
    }

    public void realmChanged(Listener listener){
        mRealmforListener.where(Message.class)
                .findAllAsync()
                .addChangeListener(new RealmChangeListener<RealmResults<Message>>() {
                    @Override
                    public void onChange(RealmResults<Message> messages) {
                        listener.onRealmChange(messages);
                    }
                });
    }

    public void removeRealmChangeListener(){
        mRealmforListener.where(Message.class).findAll().removeAllChangeListeners();
        mRealmforListener.close();
    }

    public interface Listener {
        void onRealmChange(RealmResults<Message> messages);
    }

}
