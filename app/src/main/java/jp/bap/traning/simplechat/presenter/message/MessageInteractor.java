package jp.bap.traning.simplechat.presenter.message;

import java.util.ArrayList;

import jp.bap.traning.simplechat.database.MessageDAO;
import jp.bap.traning.simplechat.model.Message;

class MessageInteractor {
    private MessageDAO messageDAO;

    MessageInteractor() {
        messageDAO = new MessageDAO();
    }

    void insertOrUpdateMessage(Message message) {
        messageDAO.insertOrUpdateMessage(message);
    }

    void getAllMessage(int roomID, MessageView callBack) {
        ArrayList<Message> messsagesList = new ArrayList<>();
        messsagesList = messageDAO.getAllMessage(roomID);
        if (messsagesList.size() > 0) {
            callBack.getAllMessage(messsagesList);
        } else {
            callBack.errorGetAllMessage(roomID);
        }
    }

    Message getAMessage(long idMessage) {
        Message message = new Message();
        message = messageDAO.getAMessage(idMessage);
        if (message.getContent() == null) {
            return null;
        } else {
            return message;
        }
    }

    void deleteMessage(long idMessage) {
        messageDAO.deleteMessage(idMessage);
    }
}
