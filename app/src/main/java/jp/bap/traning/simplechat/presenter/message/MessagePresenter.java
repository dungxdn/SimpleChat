package jp.bap.traning.simplechat.presenter.message;

import android.content.ClipboardManager;

import jp.bap.traning.simplechat.model.Message;

public class MessagePresenter {
    private MessageView mCallback;
    private MessageInteractor mInteractor;

    public MessagePresenter() {
        mInteractor = new MessageInteractor();
    }

    public MessagePresenter(MessageView callback) {
        mCallback = callback;
        mInteractor = new MessageInteractor();
    }

    public void insertOrUpdateMessage(Message message) {
        mInteractor.insertOrUpdateMessage(message);
    }

    public void getAllMessage(int roomID) {
        if (roomID < 0) {
            mCallback.errorGetAllMessage(roomID);
        } else {
            mInteractor.getAllMessage(roomID, mCallback);
        }
    }

    public Message getAMessage(long idMessage) {
        if (idMessage < 0) {
            return null;
        } else {
            return mInteractor.getAMessage(idMessage);
        }
    }

    public void deleteMessage(long idMessage) {
        mInteractor.deleteMessage(idMessage);
    }


}
