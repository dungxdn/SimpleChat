package jp.bap.traning.simplechat.presenter.rooms;

import jp.bap.traning.simplechat.response.RoomResponse;

/**
 * Created by dungpv on 6/15/18.
 */

public class GetRoomsPresenter {
    private GetRoomsView mCallback;
    private GetRoomsInteractor mInteractor;

    public GetRoomsPresenter(GetRoomsView callback) {
        mCallback = callback;
        mInteractor = new GetRoomsInteractor();
    }

    public void request() {
        mInteractor.request(new GetRoomsView() {
            @Override
            public void onSuccess(RoomResponse result) {
                mCallback.onSuccess(result);
            }

            @Override
            public void onError(String message, int code) {
                mCallback.onError(message, code);
            }

            @Override
            public void onFailure() {

            }
        });
    }
}
