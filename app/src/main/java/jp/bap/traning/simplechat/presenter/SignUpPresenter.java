package jp.bap.traning.simplechat.presenter;

import jp.bap.traning.simplechat.database.UserDAO;

public class SignUpPresenter {
    private static SignUpPresenter shareInstance;
    UserDAO userDAO = new UserDAO();

    public static SignUpPresenter getShareInstance() {
        if(shareInstance == null) {
            shareInstance= new SignUpPresenter();
        }
        return shareInstance;
    }

    public void signUp(String userName,String password, String avatar, String firstName) {

    }
}
