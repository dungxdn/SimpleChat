package jp.bap.traning.simplechat;

import io.realm.Realm;
import io.realm.annotations.Ignore;
import jp.bap.traning.simplechat.model.User;

/**
 * Created by dungpv on 6/11/18.
 */

public class UserDAO {
    private Realm mRealm;
    public UserDAO() {
        mRealm = Realm.getDefaultInstance();
    }

    public void addUser(User user) {
        mRealm.beginTransaction();
        mRealm.copyToRealm(user);
        mRealm.commitTransaction();
    }

    public boolean checkUser(String userName,String password) {
        User user = mRealm.where(User.class).equalTo("userName",userName).equalTo("password",password).findFirst();
        return user != null;
    }

    public User getUser(String userName) {
        return mRealm.where(User.class)
                .equalTo("userName", userName)
                .findFirst();
    }
}
