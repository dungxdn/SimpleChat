package jp.bap.traning.simplechat.presenter.message;

import java.util.ArrayList;

import jp.bap.traning.simplechat.model.Message;

public interface MessageView {
    void getAllMessage(ArrayList<Message> messagesList);

    void errorGetAllMessage(int roomID);
}
