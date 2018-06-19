package jp.bap.traning.simplechat.presenter.message;

import java.util.ArrayList;

import jp.bap.traning.simplechat.database.MessageDAO;
import jp.bap.traning.simplechat.model.Message;

public class MesssageInteractor {
    MessageDAO messageDAO;
    public MesssageInteractor() {
        messageDAO = new MessageDAO();
    }

    public void insertOrUpdateMessage(Message message, MessageView callBack) {
        messageDAO.insertOrUpdateMessage(message);
        callBack.insertMessage(message);
    }

    public void getAllMessage(int roomID, MessageView callBack) {
        ArrayList<Message> messsagesList = new ArrayList<>();
        messsagesList = messageDAO.getAllMessage(roomID);
        if(messsagesList.size()>0) {
            callBack.getAllMessage(messsagesList);
        }
        else {
            callBack.errorGetAllMessage(roomID);
        }
    }
}
