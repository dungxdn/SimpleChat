package jp.bap.traning.simplechat.presenter.getroom;

import jp.bap.traning.simplechat.response.GetRoomResponse;

public class GetRoomPresenter {

    private GetRoomInteractor mGetRoomInteractor;

    public GetRoomPresenter() {
        mGetRoomInteractor = new GetRoomInteractor();
    }

    public void getRoom(int roomId, GetRoomView callback) {
        mGetRoomInteractor.getRoom(roomId, new GetRoomView() {
            @Override
            public void onSuccess(GetRoomResponse result) {
                callback.onSuccess(result);
            }

            @Override
            public void onError(String message, int code) {
                callback.onError(message, code);
            }

            @Override
            public void onFailure() {
                callback.onFailure();
            }
        });
    }
}
