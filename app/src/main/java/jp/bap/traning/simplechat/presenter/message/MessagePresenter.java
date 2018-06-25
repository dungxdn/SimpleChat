package jp.bap.traning.simplechat.presenter.message;

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
       if(roomID<0) {
            mCallback.errorGetAllMessage(roomID);
       }
       else {
           mInteractor.getAllMessage(roomID,mCallback);
       }
    }
}
