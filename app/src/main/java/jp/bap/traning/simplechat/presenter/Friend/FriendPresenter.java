package jp.bap.traning.simplechat.presenter.Friend;

import jp.bap.traning.simplechat.database.UserDAO;
import jp.bap.traning.simplechat.model.User;

/**
 * Created by Admin on 6/15/2018.
 */

public class FriendPresenter {
    public User getUserLogin(int id){
        return new UserDAO().getUser(id);
    }
}
