package jp.bap.traning.simplechat.database;

import io.realm.Realm;
import jp.bap.traning.simplechat.model.User;

public class UserDAO {

    public UserDAO() {
    }

    public void insertOrUpdate(User user) {
        Realm mRealm = Realm.getDefaultInstance();
        mRealm.executeTransaction(realm -> {
            realm.insertOrUpdate(user);
        });
        mRealm.close();
    }

    public User getUser(int id) {
        Realm mRealm = Realm.getDefaultInstance();
        User user = mRealm.where(User.class)
                .equalTo("id", id)
                .findFirst();
        User result = null;
        if (user != null) {
            result = mRealm.copyFromRealm(user);
        }
        mRealm.close();
        return result;
    }
}