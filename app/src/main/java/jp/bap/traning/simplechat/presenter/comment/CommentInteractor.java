package jp.bap.traning.simplechat.presenter.comment;

import java.util.ArrayList;

import jp.bap.traning.simplechat.database.CommentDAO;
import jp.bap.traning.simplechat.model.Comment;

public class CommentInteractor {
    private CommentDAO mCommentDAO;

    CommentInteractor() {
        mCommentDAO = new CommentDAO();
    }

    void insertOrUpdate(Comment mComment) {
        mCommentDAO.insertOrUpdateComment(mComment);
    }

    void getAllComment(long newsId, CommentView mCommentView) {
        ArrayList<Comment> comments = new ArrayList<>();
        comments = mCommentDAO.getAllComment(newsId);
        if (comments.size() > 0) {
            mCommentView.getAllComment(comments);
        } else {
            mCommentView.errorGetAllComment(newsId);
        }
    }
}
