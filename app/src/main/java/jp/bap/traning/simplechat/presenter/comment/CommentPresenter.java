package jp.bap.traning.simplechat.presenter.comment;

import jp.bap.traning.simplechat.model.Comment;

public class CommentPresenter {
    private CommentView mCommentView;
    private CommentInteractor commentInteractor;

    public CommentPresenter() {
        commentInteractor = new CommentInteractor();
    }

    public CommentPresenter(CommentView commentView) {
        mCommentView = commentView;
        commentInteractor = new CommentInteractor();
    }

    public void insertOrUpdate(Comment comment){
        commentInteractor.insertOrUpdate(comment);
    }

    public void getAllComment(long newsID) {
        commentInteractor.getAllComment(newsID,mCommentView);
    }
}
