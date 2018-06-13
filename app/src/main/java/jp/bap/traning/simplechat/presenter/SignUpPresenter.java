package jp.bap.traning.simplechat.presenter;

import jp.bap.traning.simplechat.database.UserDAO;
import jp.bap.traning.simplechat.interfaces.SignUpInterface;
import jp.bap.traning.simplechat.model.User;

public class SignUpPresenter {
    private static SignUpPresenter shareInstance;
    public SignUpInterface signUpInterface;
    UserDAO userDAO = new UserDAO();

    public static SignUpPresenter getShareInstance() {
        if(shareInstance == null) {
            shareInstance= new SignUpPresenter();
        }
        return shareInstance;
    }

    public void signUp(String userName,String password, String avatar, String firstName) {
        if(userDAO.validUser(userName)==true) {
            signUpInterface.signUpFailed();
        }
        else {
            userDAO.addUser(new User(userName,password,avatar,firstName,"How are you today?"));
            signUpInterface.signUpSuccess(userName);
        }
    }
}
