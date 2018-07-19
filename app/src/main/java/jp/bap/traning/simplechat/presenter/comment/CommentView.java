package jp.bap.traning.simplechat.presenter.comment;

import java.util.ArrayList;

import jp.bap.traning.simplechat.model.Comment;

public interface CommentView {
    void getAllComment(ArrayList<Comment> commentArrayList);

    void errorGetAllComment(long newsID);
}
