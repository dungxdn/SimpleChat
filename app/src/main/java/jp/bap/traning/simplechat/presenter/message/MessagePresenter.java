package jp.bap.traning.simplechat.presenter.message;

import jp.bap.traning.simplechat.model.Message;

public class MessagePresenter {
    private MessageView mCallback;
    private MesssageInteractor mInteractor;

    public MessagePresenter() {
        mInteractor = new MesssageInteractor();
    }
    public MessagePresenter(MessageView callback) {
        mCallback = callback;
        mInteractor = new MesssageInteractor();
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
