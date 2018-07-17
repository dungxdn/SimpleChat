package jp.bap.traning.simplechat.database;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import jp.bap.traning.simplechat.model.Comment;

public class CommentDAO {

    public CommentDAO() {
    }

    public void insertOrUpdateComment(Comment mComment){
        Realm mRealm = Realm.getDefaultInstance();
        mRealm.executeTransaction(realm -> {
            realm.insertOrUpdate(mComment);
        });
        mRealm.close();
    }

    public ArrayList<Comment> getAllComment(long     newsID){
        ArrayList<Comment> comments = new ArrayList<>();
        Realm mRealm = Realm.getDefaultInstance();
        RealmResults<Comment> results = mRealm.where(Comment.class).equalTo("idNews", newsID).findAll();
        for (Comment comment : results) {
            comments.add(mRealm.copyFromRealm(comment));
        }
        return comments;
    }
}
