package jp.bap.traning.simplechat.presenter.message;

import java.util.ArrayList;

import jp.bap.traning.simplechat.database.MessageDAO;
import jp.bap.traning.simplechat.model.Message;

public class MessageInteractor {
    MessageDAO messageDAO;
    public MessageInteractor() {
        messageDAO = new MessageDAO();
    }

    public void insertOrUpdateMessage(Message message) {
        messageDAO.insertOrUpdateMessage(message);
    }

    public void getAllMessage(int roomID, MessageView callBack) {
        ArrayList<Message> messsagesList = new ArrayList<>();
        messsagesList = messageDAO.getAllMessage(roomID);
        if (messsagesList.size() > 0) {
            callBack.getAllMessage(messsagesList);
        } else {
            callBack.errorGetAllMessage(roomID);
        }
    }

    public Message getAMessage(long idMessage) {
        Message message = new Message();
        message = messageDAO.getAMessage(idMessage);
        if(message.getContent()==null) {
            return null;
        }
        else {
            return message;
        }
    }

    public void deleteMessage(long idMessage) {
        messageDAO.deleteMessage(idMessage);
    }
}
