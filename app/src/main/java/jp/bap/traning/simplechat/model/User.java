package jp.bap.traning.simplechat.model;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User extends RealmObject {
    private String userName;
    private String password;

    @Ignore
    static Realm realm =Realm.getDefaultInstance();

    public static void addUser(String userName, String password) {
        User user = new User(userName,password);
        realm.beginTransaction();
        realm.copyToRealm(user);
        realm.commitTransaction();
    }

    public static boolean checkUser(String userName,String password) {
        User user = realm.where(User.class).equalTo("userName",userName).equalTo("password",password).findFirst();
        return user != null;
    }


}
