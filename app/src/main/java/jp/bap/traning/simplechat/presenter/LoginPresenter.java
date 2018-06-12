package jp.bap.traning.simplechat.presenter;

import jp.bap.traning.simplechat.interfaces.LoginInterface;

public class LoginPresenter  {
    private static LoginPresenter shareInstance ;
    public LoginInterface loginInterface;

    public static LoginPresenter getShareInstance() {
        if(shareInstance==null) {
            shareInstance= new LoginPresenter();
        }
        return shareInstance;
    }

    public void logIn(String userName, String password) {

    }


}
