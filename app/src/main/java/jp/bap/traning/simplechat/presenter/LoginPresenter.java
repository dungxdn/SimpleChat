package jp.bap.traning.simplechat.presenter;

import android.content.SharedPreferences;

import io.realm.Realm;
import jp.bap.traning.simplechat.BaseActivity;
import jp.bap.traning.simplechat.database.UserDAO;
import jp.bap.traning.simplechat.interfaces.LoginInterface;
import jp.bap.traning.simplechat.model.User;

import static android.content.Context.MODE_PRIVATE;

public class LoginPresenter  {
    private static LoginPresenter shareInstance ;
    public LoginInterface loginInterface;
    UserDAO userDAO =  new UserDAO();

    public static LoginPresenter getShareInstance() {
        if(shareInstance==null) {
            shareInstance= new LoginPresenter();
        }
        return shareInstance;
    }

    public void logIn(String userName, String password) {
        if(userDAO.checkUser(userName,password) == true) {
            loginInterface.loginSuccess(userName,password);
        }
        else {
            loginInterface.loginFailed();
        }
    }




}
