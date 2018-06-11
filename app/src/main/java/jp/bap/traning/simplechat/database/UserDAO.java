package jp.bap.traning.simplechat.database;

import io.realm.Realm;
import io.realm.RealmObject;
import jp.bap.traning.simplechat.model.User;

public class UserDAO {
    private Realm mRealm;

    public UserDAO() {
        mRealm = Realm.getDefaultInstance();
    }

    public void addUser(User user) {
        mRealm = Realm.getDefaultInstance();
        mRealm.beginTransaction();
        mRealm.copyToRealm(user);
        mRealm.commitTransaction();
    }

    public boolean checkUser(String userName,String password) {
        User user = mRealm.where(User.class).equalTo("userName",userName).equalTo("password",password).findFirst();
        return user != null;
    }

    public boolean validUser(String userName) {
        User user = mRealm.where(User.class).equalTo("userName",userName).findFirst();
        return user != null;
    }

    public User getUser(String userName) {
        return mRealm.where(User.class)
                .equalTo("userName", userName)
                .findFirst();
    }
}